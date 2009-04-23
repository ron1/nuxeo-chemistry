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
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.Document;
import org.apache.chemistry.Folder;
import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.Policy;
import org.apache.chemistry.Relationship;
import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.property.Property;
import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.common.atom.BuildContext;
import org.nuxeo.chemistry.client.common.atom.ValueAdapter;
import org.nuxeo.chemistry.client.common.atom.XmlProperty;
import org.nuxeo.chemistry.client.common.xml.XMLWriter;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPObjectEntry extends AbstractObjectEntry {

    protected APPConnection connection;
    protected Map<String, XmlProperty> properties;
    protected Set<String> allowableActions; //TODO use set?
    
    protected String id;
    protected String name;
    protected String typeId;

    public APPObjectEntry(APPConnection connection) {
        this (connection, new HashMap<String, XmlProperty>(), new HashSet<String>());
    }
    
    public APPObjectEntry(APPConnection connection, Map<String, XmlProperty> properties, Set<String> allowableActions) {
        this.connection = connection;
        this.properties = properties;
        this.allowableActions = allowableActions;
    }    

    public APPConnection getConnection() {
        return connection;
    }
    
    public Collection<String> getAllowableActions() {
        return allowableActions;
    }

    public Serializable getValue(String name) {
        XmlProperty p = properties.get(name);
        return p != null ? p.getValue() : null;
    }

    @Override
    public Connector getConnector() {
        return connection.getConnector();
    }

    @Override
    public String getTypeId() {
        if (typeId == null) {
            typeId = super.getTypeId();
        }
        return super.getTypeId();
    }

    @Override
    public String getId() {
        if (id == null) {
            id = super.getId();
        }
        return id;
    }
    
    @Override
    public String getName() {
        if (name == null) {
            name = super.getName();
        }
        return name;
    }
    
    public Type getType() {
        return connection.getRepository().getType(getTypeId());
    }
    
    public Document getDocument() {
        APPDocument doc = getRemoteEntity(
                new BuildContext(getConnection(), getType()), 
                getEditLink(), APPDocument.class); 
        return doc;
    }
    
    public Folder getFolder() {
        return getRemoteEntity(new BuildContext(getConnection(), getType()), 
                getEditLink(), APPFolder.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Property> getProperties() {
        return (Map)properties;
    }
    
    public Property getProperty(String name) {
        return properties.get(name); 
    }

    public URI getURI() {
        String value = getEditLink();
        if (value == null) {
            value = getLink("self");
            if (value == null) {
                value = getLink("alternate");
            }
        }
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Not an URI: "+value);
        }
    }

    public boolean hasContentStream() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Policy getPolicy() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Relationship getRelationship() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public Collection<ObjectEntry> getRelationships() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    @Override
    public String toString() {
        return getName();
    }
    

    public void writeObjectTo(XMLWriter xw) throws IOException {
        xw.element(CMIS.OBJECT);
        xw.start();
        xw.element(CMIS.PROPERTIES);
        xw.start();
        for (XmlProperty p : properties.values()) {
            ValueAdapter va = p.getAdapter();
            xw.element(va.getPropertyName()).attr(CMIS.NAME, p.getName());
            xw.start();            
            if (p.isValueLoaded()) {                
                Serializable v = p.getValue();
                if (v != null) {
                    if (v.getClass().isArray()) {
                        Serializable[] ar = (Serializable[])v;
                        for (Serializable val : ar) {
                            xw.element(CMIS.VALUE).content(va.writeValue(val));
                        }                        
                    } else {
                        xw.element(CMIS.VALUE).content(va.writeValue(v));
                    }
                }
            } else {
                Object v = p.getXmlValue();
                if (v != null) {
                    if (v.getClass() == String.class) {
                        xw.element(CMIS.VALUE).content((String)v);
                    } else {
                        List<String> list = (List<String>)v;
                        for (String val : list) {
                            xw.element(CMIS.VALUE).content(val);    
                        }
                    }
                }
            }
            xw.end();
        }
        xw.end();
        xw.end();
    }
    
}
