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
import java.io.Serializable;
import java.util.Map;

import org.apache.chemistry.ContentStream;
import org.apache.chemistry.Document;
import org.apache.chemistry.Folder;
import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.property.Property;
import org.nuxeo.chemistry.client.app.model.DataMap;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPDocument extends APPObjectEntry implements Document {

    public void setValue(String name, Serializable value) {
        map.set(name, value);
    }

    public APPDocument(APPConnection connection) {
        this (connection, null);
    }

    public APPDocument(APPConnection connection, DataMap dataMap) {
        super (connection, dataMap);
    }

    public void setName(String name) {
        this.name = name;
        setValue(Property.NAME, name);
    }

    public void setValues(Map<String, Serializable> values) {
        for (Map.Entry<String,Serializable> entry : values.entrySet()) {
            setValue(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public Document getDocument() {
        return this;
    }

    @Override
    public Folder getFolder() {
        throw new UnsupportedOperationException("Not a folder"); 
    }
    
    public Folder getParent() {
        return getLinkedEntity(CMIS.LINK_PARENT, APPFolder.class);
    }
    
    public void save() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ContentStream getContentStream() {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public InputStream getStream() throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
    public void setContentStream(ContentStream contentStream)
            throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");        
    }

}
