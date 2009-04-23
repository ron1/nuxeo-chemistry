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
package org.nuxeo.chemistry.client.app;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.common.atom.ATOM;
import org.nuxeo.chemistry.client.common.atom.BuildContext;
import org.nuxeo.chemistry.client.common.xml.XMLWriter;



/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPObjectEntryHandler implements SerializationHandler<org.nuxeo.chemistry.client.app.APPObjectEntry> {
    
    public String getContentType() {
        return "application/atom+xml"; //TODO
    }

    public Class<org.nuxeo.chemistry.client.app.APPObjectEntry> getObjectType() {
        return org.nuxeo.chemistry.client.app.APPObjectEntry.class;
    }

    public List<org.nuxeo.chemistry.client.app.APPObjectEntry> readFeed(BuildContext context, InputStream in)  throws IOException {
        try {
            APPFeedBuilder builder = APPFeedBuilder.getBuilder();
            return builder.read(context, in);

//            return CmisFeedReader.INSTANCE.read(context, in);
        } catch (XMLStreamException e) {
            IOException ioe = new IOException("Failed to read feed");
            ioe.initCause(e);
            throw ioe;
        }
    }

    public org.nuxeo.chemistry.client.app.APPObjectEntry readEntity(BuildContext context, InputStream in)
            throws IOException {
        try {
            APPObjectEntryBuilder builder = APPObjectEntryBuilder.getBuilder();
            return builder.read(context, in);
            //return CmisEntryReader.INSTANCE.read(context, in);
        } catch (XMLStreamException e) {
            IOException ioe = new IOException("Failed to read feed");
            ioe.initCause(e);
            throw ioe;
        }
    }

    public void writeEntity(org.nuxeo.chemistry.client.app.APPObjectEntry object, OutputStream out)
            throws IOException {
        XMLWriter xw = new XMLWriter(new OutputStreamWriter(out));
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

    protected void writeCmisObject(org.nuxeo.chemistry.client.app.APPObjectEntry object, XMLWriter xw) throws IOException {
//        xw.element(CMIS.OBJECT);
//        xw.start();
//        object.getProperties();
//        DataMap map = object.getDataMap();
//        map.writeTo(xw);
//        xw.end();
        object.writeObjectTo(xw);
    }
}
