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

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.bindings.CmisBindingFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.enums.CmisVersion;
import org.apache.chemistry.opencmis.commons.server.CallContext;
import org.apache.chemistry.opencmis.commons.server.CmisService;
import org.apache.chemistry.opencmis.commons.spi.BindingsObjectFactory;
import org.apache.chemistry.opencmis.server.impl.CallContextImpl;
import org.apache.chemistry.opencmis.server.shared.ThresholdOutputStreamFactory;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.opencmis.bindings.NuxeoCmisServiceFactory;
import org.nuxeo.ecm.core.opencmis.bindings.NuxeoCmisServiceFactoryManager;
import org.nuxeo.ecm.core.opencmis.impl.client.NuxeoBinding;
import org.nuxeo.ecm.core.opencmis.impl.client.NuxeoSession;
import org.nuxeo.ecm.core.opencmis.impl.server.NuxeoCmisService;
import org.nuxeo.ecm.core.opencmis.tests.Helper;
import org.nuxeo.runtime.api.Framework;

/**
 * Test the high-level session using a local connection.
 */
public class TestNuxeoSessionLocal extends NuxeoSessionTestCase {

    private static final int THRESHOLD = 4 * 1024 * 1024;

    private static final int MAX_SIZE = -1;

    @Override
    public void setUpCmisSession() throws Exception {
        setUpCmisSession(USERNAME);
    }

    @Override
    protected void setUpCmisSession(String username) throws Exception {

        NuxeoCmisServiceFactoryManager manager =
                Framework.getLocalService(NuxeoCmisServiceFactoryManager.class);
        NuxeoCmisServiceFactory serviceFactory = manager.getNuxeoCmisServiceFactory();
        ThresholdOutputStreamFactory streamFactory = ThresholdOutputStreamFactory.newInstance(
                new File(System.getProperty("java.io.tmpdir")),
                THRESHOLD, MAX_SIZE, false);
        HttpServletRequest request = null;
        HttpServletResponse response = null;
        CallContextImpl context = new CallContextImpl(
                CallContext.BINDING_LOCAL, CmisVersion.CMIS_1_1, getRepositoryId(),
                FakeServletContext.getServletContext(), request, response,
                serviceFactory, streamFactory);
        context.put(CallContext.USERNAME, username);
        context.put(CallContext.PASSWORD, PASSWORD);
        CmisService service = serviceFactory.getService(context);
        NuxeoBinding binding = new NuxeoBinding(service);
        session = new NuxeoSession(binding, context);
    }

    @Override
    public void tearDownCmisSession() throws Exception {
        if (session != null) {
            session.getBinding().close();
            session.clear();
            session = null;
        }
    }

    protected String createDocumentMyDocType() {
        BindingsObjectFactory factory = 
                ((NuxeoSession) session).getBinding().getObjectFactory();
        List<PropertyData<?>> props = new ArrayList<PropertyData<?>>();
        props.add(factory.createPropertyIdData(PropertyIds.NAME, "My Title"));
        props.add(factory.createPropertyIdData(PropertyIds.OBJECT_TYPE_ID,
                "MyDocType"));
        props.add(factory.createPropertyStringData("my:string", "abc"));
        props.add(factory.createPropertyBooleanData("my:boolean", Boolean.TRUE));
        props.add(factory.createPropertyIntegerData("my:integer",
                BigInteger.valueOf(123)));
        props.add(factory.createPropertyIntegerData("my:long",
                BigInteger.valueOf(123)));
        props.add(factory.createPropertyDecimalData("my:double",
                BigDecimal.valueOf(123.456)));
        GregorianCalendar expectedDate = Helper.getCalendar(2010, 9, 30, 16, 4,
                55);
        props.add(factory.createPropertyDateTimeData("my:date", expectedDate));
        Properties properties = factory.createPropertiesData(props);
        ((NuxeoSession) session).getService();
        String id = ((NuxeoSession) session).getService().createDocument(getRepositoryId(), 
                properties, rootFolderId, null, null, null, null, null, null);
        assertNotNull(id);
        return id;
    }
}
