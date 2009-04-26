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
package org.nuxeo.chemistry.client.app.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.ContentStream;
import org.apache.chemistry.Document;
import org.apache.chemistry.Folder;
import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.property.Property;
import org.apache.chemistry.property.PropertyDefinition;
import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.app.Request;
import org.nuxeo.chemistry.client.app.Response;
import org.nuxeo.chemistry.client.common.atom.ReadContext;
import org.nuxeo.chemistry.client.common.atom.XmlProperty;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPDocument extends APPObjectEntry implements Document {

    

    public APPDocument(APPConnection connection, String typeId) {
        this (connection, new HashMap<String, XmlProperty>());
        Type type = connection.getRepository().getType(typeId);
        XmlProperty p = new XmlProperty(type.getPropertyDefinition(Property.TYPE_ID), (String)null);
        p.setValueUnsafe(typeId);
        properties.put(p.getName(), p);
//TODO a document doesn;t have the parent id property
//        if (parentId != null) {
//            p = new XmlProperty(type.getPropertyDefinition(Property.PARENT_ID), (String)null);
//            p.setValueUnsafe(parentId);
//            properties.put(p.getName(), p);
//        }
    }

    public APPDocument(APPConnection connection, Map<String,XmlProperty> properties) {
        super (connection, properties, null);
    }

    public APPDocument(APPConnection connection, Map<String,XmlProperty> properties, Set<String> allowableActions) {
        super (connection, properties, allowableActions);
    }
    
    public APPDocument(APPConnection connection, APPObjectEntry entry) {
        super (connection, entry.properties, entry.allowableActions);
        links = entry.links;
    }
    

    /**
     * Advanced method to be able to put properties bypassing checks. 
     * This is overwriting any existing properties with the same name. 
     * This method is intended for internal use. Clients may use it but at their own risk.   
     * @param name
     * @param value
     */
    public void putValue(String name, Serializable value) {
        XmlProperty p = new XmlProperty(getType().getPropertyDefinition(Property.TYPE_ID), (String)null);
        p.setValueUnsafe(typeId);
        properties.put(p.getName(), p);
    }
    
    public void setValue(String name, Serializable value) {
        XmlProperty p = properties.get(name);
        if (p != null) {
            p.setValue(value);
        } else {
            PropertyDefinition pd = getType().getPropertyDefinition(name);
            if (pd == null) {
                throw new IllegalArgumentException("No such property: "+name);
            }
            p = new XmlProperty(pd);
            p.setValue(value);
            properties.put(name, p);
        }
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
        if (this instanceof Folder) {
            return (Folder)this;
        }
        throw new UnsupportedOperationException("Not a folder"); 
    }
    
    public Folder getParent() {
        String href = getLink(CMIS.LINK_PARENT);
        if (href != null) {
            return getConnection().toFolder((APPObjectEntry)getConnector().getObject(new ReadContext(getConnection()), href));
        }
        return null;
    }
    
    public void save() {
        try {
            if (getId() == null) {
                create();
            } else {
                update();
            }
        } catch (ContentManagerException e) { //TODO
            throw new RuntimeException(e);
        }
    }
    
    protected void create() throws ContentManagerException {
        String href = getLink(CMIS.LINK_PARENT);
        if (href == null) {
           throw new IllegalArgumentException("Cannot create entry: no 'cmis-parent' link is present"); 
        }
        Request req = new Request(href);
        req.setHeader("Content-Type", "application/atom+xml;type=entry");
        Response resp = getConnector().postObject(req, this);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
        }        
        //TODO get the response to update the content of the posted document 
        //resp.getEntity(get, APPDocument.class);
    }

    protected void update() throws ContentManagerException {
        String href = getEditLink();
        if (href == null) {
           throw new IllegalArgumentException("Cannot edit entry: no 'edit' link is present"); 
        }
        Request req = new Request(href);
        req.setHeader("Content-Type", "application/atom+xml;type=entry");
        Response resp = getConnector().putObject(req, this);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
        }                
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
