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

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.CMISObject;
import org.apache.chemistry.Connection;
import org.apache.chemistry.Document;
import org.apache.chemistry.Folder;
import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.Policy;
import org.apache.chemistry.Relationship;
import org.apache.chemistry.RelationshipDirection;
import org.apache.chemistry.ReturnVersion;
import org.apache.chemistry.SPI;
import org.apache.chemistry.Unfiling;
import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.repository.Repository;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.app.service.ServiceContext;
import org.nuxeo.chemistry.client.app.service.ServiceInfo;
import org.nuxeo.chemistry.client.common.atom.BuildContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPConnection implements Connection {

    protected APPFolder root;
    
    protected Connector connector;
    protected APPRepository repository;
    protected Map<Class<?>, Object> singletons = new Hashtable<Class<?>, Object>();


    public APPConnection(APPRepository repo) {
        this.repository = repo;
        this.connector = repo.cm.getConnector(); // TODO clone connector to be able to use different logins
    }

    public Repository getRepository() {
        return repository;
    }

    public Connector getConnector() {
        return connector;
    }

    public String getBaseUrl() {
        return repository.cm.getBaseUrl();
    }

    public void close() {
        // do nothing? or clear login?
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public Folder getRootFolder() {
        if (root == null) {
            String rootId = repository.info.getRootFolderId();
            root = (APPFolder)getObject(rootId, ReturnVersion.THIS);
        }
        return root;
    }
    
    public ObjectEntry getRootEntry() {
        return getRootFolder();
    }
    
    public String getEntryLink(ObjectEntry entry, String rel) {
        return ((APPObjectEntry)entry).getLink(rel);
    }
    
    @SuppressWarnings("unchecked")
    public List<ObjectEntry> getChildren(ObjectEntry folder) {
        String href = getEntryLink(folder, CMIS.LINK_CHILDREN);
        Request req = new Request(href);
        Response resp = connector.get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
        }        
        return (List<ObjectEntry>)resp.getFeed(new BuildContext(this),
                APPObjectEntry.class);
    }

    public void addObjectToFolder(ObjectEntry object, ObjectEntry folder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void applyPolicy(Policy policy, ObjectEntry object) {
        throw new UnsupportedOperationException("Not yet implemented");        
    }

    public void cancelCheckOut(ObjectEntry document) {
        throw new UnsupportedOperationException("Not yet implemented");        
    }

    public CMISObject checkIn(ObjectEntry document, boolean major,
            String comment) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public CMISObject checkOut(ObjectEntry document) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void deleteAllVersions(ObjectEntry document) {
        throw new UnsupportedOperationException("Not yet implemented");        
    }

    public void deleteObject(ObjectEntry object) {
        APPObjectEntry oe = (APPObjectEntry)object;
        Request req = new Request(oe.getEditLink());
        Response resp = getConnector().delete(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
        }        
    }

    public Collection<String> deleteTree(ObjectEntry folder, Unfiling unfiling,
            boolean continueOnFailure) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Collection<ObjectEntry> getAllVersions(ObjectEntry document,
            String filter) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Collection<Policy> getAppliedPolicies(ObjectEntry object) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public CMISObject getLatestVersion(ObjectEntry document, boolean major) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO temporary implementation to have something working until search is implemented
     * Versions not yet supported
     */
    public CMISObject getObject(String objectId, ReturnVersion returnVersion) {
		if (returnVersion == null) {
            returnVersion = ReturnVersion.THIS;
        }		
        String href = repository.getCollectionHref("root-children");        
        int p = href.lastIndexOf("/");
        if (p == href.length()-1) {
            p = href.lastIndexOf("/", href.length()-1);
        }
        if (p > -1) {
            href = href.substring(0, p+1);            
        }
        href += "objects/"+objectId;
        Request req = new Request(href);
        Response resp = connector.get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
        }        

        APPObjectEntry entry = resp.getEntity(new BuildContext(this),
                APPObjectEntry.class);
        switch(entry.getType().getBaseType()) {
        case DOCUMENT:
            return new APPDocument(this, entry);
        case FOLDER:
            return new APPFolder(this, entry);
        case POLICY:
            throw new UnsupportedOperationException("Not yet implemented");
        case RELATIONSHIP:
            throw new UnsupportedOperationException("Not yet implemented");
        default: 
            throw new IllegalArgumentException("Unknown object base type"+entry.getType().getBaseType());        
        }

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<ObjectEntry> getRelationships(ObjectEntry object,
            RelationshipDirection direction, String typeId,
            boolean includeSubRelationshipTypes) {
        throw new UnsupportedOperationException("Not yet implemented"); 
    }

    public SPI getSPI() {
        throw new UnsupportedOperationException("Not yet implemented"); 
    }

    public void moveObject(ObjectEntry object, ObjectEntry targetFolder,
            ObjectEntry sourceFolder) {
        throw new UnsupportedOperationException("Not yet implemented");        
    }

    public Document newDocument(String typeId, ObjectEntry folder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Folder newFolder(String typeId, ObjectEntry folder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Policy newPolicy(String typeId, ObjectEntry folder) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Relationship newRelationship(String typeId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Collection<ObjectEntry> query(String statement,
            boolean searchAllVersions) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void removeObjectFromFolder(ObjectEntry object, ObjectEntry folder) {
        throw new UnsupportedOperationException("Not yet implemented");        
    }

    public void removePolicy(Policy policy, ObjectEntry object) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    
    /**
     * Get an extension service in connection scope 
     * @param <T>
     * @param clazz service interface class
     * @param connection the connection to bound the service on
     * @return the service instance or null if none
     */
    public <T> T getService(Class<T> clazz) {
        repository.loadServices(); // be sure services information is loaded
        ServiceInfo info = repository.services.get(clazz);
        if (info != null) {
            if (info.isSingleton()) {
                Object service = getSingletonService(clazz);
                if (service != null) {
                    return (T)service;
                }
            }
            ServiceContext ctx = new ServiceContext(info, this);
            try {
                Object service = info.newInstance(ctx);
                if (info.isSingleton()) {
                    putSingletonService(clazz, service);
                }
                return (T)service;
            } catch (Exception e) {
                // do nothing
            }
        }
        return null;
    }

    protected void putSingletonService(Class<?> clazz, Object service) {
        singletons.put(clazz, service);
    }
    
    protected Object getSingletonService(Class<?> clazz) {
        return singletons.get(clazz);
    }

}
