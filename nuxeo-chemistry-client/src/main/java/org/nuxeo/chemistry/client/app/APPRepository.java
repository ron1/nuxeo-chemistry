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
 *     matic
 */
package org.nuxeo.chemistry.client.app;

import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.Connection;
import org.apache.chemistry.repository.Repository;
import org.apache.chemistry.repository.RepositoryInfo;
import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.ContentManager;

/**
 * An APP client repository proxy
 * 
 * @author Bogdan Stefanescu
 *
 */
public class APPRepository implements Repository {

    protected APPContentManager cm;
    protected RepositoryInfo info;
    protected String id;
    
    protected Map<String,String> collections =
        new HashMap<String,String>();

    public APPRepository(APPContentManager cm) {
        this.cm = cm;
    }
            
    public APPRepository(APPContentManager cm, RepositoryInfo info) {
        this.cm = cm;
        this.info = info;
    }
    
    public void setInfo(RepositoryInfo info) {
        this.info = info;
    }
    
    public ContentManager getContentManager() {
        return cm;
    }
    
    public String getId() {
        if (id == null) {
            id = info.getId();
        }
        return id;
    }
    
    public String getName() {
        return info.getName();
    }

    public URI getURI() {
        throw new UnsupportedOperationException("Not yet implemented");
    }    

    public <T> T getExtension(Class<T> klass) {
        return cm.getAdapter(Repository.class, klass);
    }    
    
    public Connection getConnection(Map<String, Serializable> parameters) {
        return new APPConnection(this);
    }
    
    public void addCollection(String type, String href) {
        collections.put(type, href);
    }

    public RepositoryInfo getInfo() {
        return info;
    }

    public Type getType(String typeId) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    public List<Type> getTypes(String typeId,
            boolean returnPropertyDefinitions, int maxItems, int skipCount,
            boolean[] hasMoreItems) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    public Collection<Type> getTypes(String typeId,
            boolean returnPropertyDefinitions) {
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    public String getRelationshipName() {
        return info.getRelationshipName();
    }

    public String getCollectionHref(String type) {
        return collections.get(type);
    }
}
