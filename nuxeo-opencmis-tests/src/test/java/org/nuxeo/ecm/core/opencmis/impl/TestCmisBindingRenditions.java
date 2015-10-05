/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Florent Guillaume
 */
package org.nuxeo.ecm.core.opencmis.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import org.apache.chemistry.opencmis.commons.data.CacheHeaderContentStream;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.data.LastModifiedContentStream;
import org.apache.chemistry.opencmis.commons.data.ObjectData;
import org.apache.chemistry.opencmis.commons.data.RenditionData;
import org.apache.chemistry.opencmis.commons.impl.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.opencmis.impl.server.NuxeoCmisService;
import org.nuxeo.ecm.core.storage.sql.ra.PoolingRepositoryFactory;
import org.nuxeo.ecm.core.test.annotations.Granularity;
import org.nuxeo.ecm.core.test.annotations.RepositoryConfig;
import org.nuxeo.ecm.platform.rendition.Rendition;
import org.nuxeo.ecm.platform.rendition.service.RenditionService;
import org.nuxeo.runtime.test.runner.Deploy;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

@RunWith(FeaturesRunner.class)
@Features({ CmisFeature.class, CmisFeatureConfiguration.class })
@Deploy({ "org.nuxeo.ecm.platform.commandline.executor", //
        "org.nuxeo.ecm.core.convert.api", //
        "org.nuxeo.ecm.core.convert", //
        "org.nuxeo.ecm.core.convert.plugins", //
        "org.nuxeo.ecm.platform.convert", //
        "org.nuxeo.ecm.platform.rendition.api", //
        "org.nuxeo.ecm.platform.rendition.core", //
        "org.nuxeo.ecm.automation.core" //
})
@RepositoryConfig(cleanup = Granularity.METHOD, repositoryFactoryClass = PoolingRepositoryFactory.class)
public class TestCmisBindingRenditions extends TestCmisBindingBase {

    @Inject
    protected CoreSession coreSession;

    @Inject
    protected RenditionService renditionService;

    @Before
    public void setUp() throws Exception {
        setUpBinding(coreSession);
        setUpData(coreSession);
    }

    @After
    public void tearDown() {
        tearDownBinding();
    }

    protected ObjectData getObjectByPath(String path) {
        return objService.getObjectByPath(repositoryId, path, null, null, null, null, null, null, null);
    }

    public static final Comparator<RenditionData> RENDITION_CMP = new Comparator<RenditionData>() {
        @Override
        public int compare(RenditionData a, RenditionData b) {
            return a.getStreamId().compareTo(b.getStreamId());
        };
    };

    public static final int TEXT_PNG_ICON_SIZE = 394;

    @Test
    public void testRenditions() throws Exception {
        ObjectData ob = getObjectByPath("/testfolder1/testfile1");

        // list renditions
        List<RenditionData> renditions = objService.getRenditions(repositoryId, ob.getId(), "*", null, null, null);
        assertEquals(5, renditions.size());
        Collections.sort(renditions, RENDITION_CMP);

        RenditionData ren;
        ren = renditions.get(0);
        assertEquals("cmis:thumbnail", ren.getKind());
        assertEquals("nuxeo:icon", ren.getStreamId());
        assertEquals("image/png", ren.getMimeType());
        assertEquals("text.png", ren.getTitle());

        ren = renditions.get(1);
        assertEquals("nuxeo:rendition", ren.getKind());
        assertEquals("nuxeo:rendition:pdf", ren.getStreamId());
        assertEquals("application/pdf", ren.getMimeType());
        assertEquals("label.rendition.pdf", ren.getTitle());

        ContentStream cs;
        cs = objService.getContentStream(repositoryId, ob.getId(), "nuxeo:icon", null, null, null);
        assertEquals("image/png", cs.getMimeType());
        assertEquals("text.png", cs.getFileName());
        assertEquals(TEXT_PNG_ICON_SIZE, cs.getBigLength().longValue());

        DocumentModel doc = coreSession.getDocument(new IdRef(ob.getId()));
        Rendition rendition = renditionService.getRendition(doc, "pdf", true);
        cs = objService.getContentStream(repositoryId, ob.getId(), "nuxeo:rendition:pdf", null, null, null);
        assertEquals("application/pdf", cs.getMimeType());
        assertEquals("testfile.txt.pdf", cs.getFileName());
        assertTrue(cs instanceof CacheHeaderContentStream);
        CacheHeaderContentStream chcs;
        chcs = (CacheHeaderContentStream) cs;
        assertEquals(DigestUtils.md5Hex(chcs.getStream()), chcs.getETag());
        LastModifiedContentStream lmcs;
        lmcs = (LastModifiedContentStream) cs;
        assertEquals(rendition.getModificationDate(), lmcs.getLastModified());
    }

    @Test
    public void testRenditionFilter() throws Exception {
        ObjectData ob = getObjectByPath("/testfolder1/testfile1");

        // cmis:none
        List<RenditionData> renditions = objService.getRenditions(repositoryId, ob.getId(), Constants.RENDITION_NONE,
                null, null, null);
        assertEquals(0, renditions.size());

        // null is cmis:none
        renditions = objService.getRenditions(repositoryId, ob.getId(), null, null, null, null);
        assertEquals(0, renditions.size());

        // specific kind
        renditions = objService.getRenditions(repositoryId, ob.getId(), "cmis:thumbnail", null, null, null);
        assertEquals(1, renditions.size());
        assertEquals("nuxeo:icon", renditions.get(0).getStreamId());

        // non-existent mimetype
        renditions = objService.getRenditions(repositoryId, ob.getId(), "foo/bar", null, null, null);
        assertEquals(0, renditions.size());

        // specific mimetype
        renditions = objService.getRenditions(repositoryId, ob.getId(), "application/pdf", null, null, null);
        assertEquals(1, renditions.size());
        assertEquals("nuxeo:rendition:pdf", renditions.get(0).getStreamId());

        // wildcard mimetype
        renditions = objService.getRenditions(repositoryId, ob.getId(), "application/*", null, null, null);
        assertEquals(3, renditions.size());
        assertEquals("nuxeo:rendition:pdf", renditions.get(0).getStreamId());

        // several kind / mimetypes
        renditions = objService.getRenditions(repositoryId, ob.getId(), "foo/*,foo/bar,foo", null, null, null);
        assertEquals(0, renditions.size());

        renditions = objService.getRenditions(repositoryId, ob.getId(), "application/*,foo/bar,foo", null, null, null);
        assertEquals(3, renditions.size());
        assertEquals("nuxeo:rendition:pdf", renditions.get(0).getStreamId());

        renditions = objService.getRenditions(repositoryId, ob.getId(), "application/*,cmis:thumbnail", null, null,
                null);
        assertEquals(4, renditions.size());
    }

}
