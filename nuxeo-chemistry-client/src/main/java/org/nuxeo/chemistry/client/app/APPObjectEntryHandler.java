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
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.nuxeo.chemistry.client.app.xml.CmisFeedReader;



/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPObjectEntryHandler implements SerializationHandler<APPObjectEntry> {

    
    public String getContentType() {
        return "application/atom+xml"; //TODO
    }

    public Class<APPObjectEntry> getObjectType() {
        return APPObjectEntry.class;
    }

    public List<APPObjectEntry> readFeed(Object context, InputStream in)  throws IOException {
        try {
            return CmisFeedReader.getInstance().read(in);
        } catch (XMLStreamException e) {
            IOException ioe = new IOException("Failed to read feed");
            ioe.initCause(e);
            throw ioe;
        }
    }

    public APPObjectEntry readEntity(Object context, InputStream in)
            throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void writeEntity(APPObjectEntry object, OutputStream out)
            throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
