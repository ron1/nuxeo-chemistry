/*
 * (C) Copyright 2006-2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     bstefanescu
 */
package org.nuxeo.chemistry.client.common.atom;

import java.io.IOException;
import java.util.Date;

import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.common.xml.XMLWriter;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class ObjectEntryWriter extends AbstractXmlObjectWriter<ObjectEntry> {
    
    @Override
    public String getContentType() {
        return "application/atom+xml;type=entry";
    }
    
    @Override
    public void write(ObjectEntry object, XMLWriter xw) throws IOException {
        try {
            xw.start();
            xw.element("entry").xmlns(ATOM.ATOM_NS).xmlns(CMIS.CMIS_PREFIX, CMIS.CMIS_NS);
            xw.start();
            // atom requires an ID to be set even on new created entries .. 
            xw.element("id").content("urn:uuid:"+object.getId());
            xw.element("title").content(object.getName());
            xw.element("updated").content(new Date());            
            xw.element("content").content(""); // TODO fake content for now
            writeCmisObject(object, xw);
            xw.end();
            xw.end();
        } catch (Exception e) {
            e.printStackTrace(); // TODO
            throw new RuntimeException(e);
        } finally {
            xw.close();
        }
    }

    protected abstract void writeCmisObject(ObjectEntry object, XMLWriter xw) throws IOException;
    
}
