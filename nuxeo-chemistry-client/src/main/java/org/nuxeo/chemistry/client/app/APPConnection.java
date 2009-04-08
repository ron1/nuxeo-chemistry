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
import java.util.List;

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
import org.apache.chemistry.property.Property;
import org.apache.chemistry.repository.Repository;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.app.model.DataMap;
import org.nuxeo.chemistry.client.app.model.StringValue;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPConnection implements Connection {

    protected ObjectEntry root;
    
    protected Connector connector;
    protected APPRepository repository;


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
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public ObjectEntry getRootEntry() {
        if (root == null) {
            DataMap map = new DataMap();
            //Map<String, Serializable> props = new HashMap<String, Serializable>();
            //props.put(Property.ID, repository.info.getRootFolderId());
            //props.put(Property.NAME, "Root");
            map.put(Property.ID, new StringValue(repository.info.getRootFolderId()));
            map.put(Property.NAME, new StringValue("Root"));
            APPObjectEntry entry = new APPObjectEntry(this, map);
            //TODO the URL to the root itself ... this need to be fixed
            // a way is to get the root children feed and use the self link
            // for now we hard-code logic
            String href = repository.getCollectionHref(CMIS.COL_ROOT_CHILDREN);
            String self = href.substring(0, href.length()-"children".length());
            self += "objects/" + repository.info.getRootFolderId();
            entry.addLink("self", self); 
            entry.addLink("edit", self); 
            entry.addLink(CMIS.LINK_CHILDREN, href);
            entry.addLink(CMIS.LINK_DESCENDANTS, repository.getCollectionHref(CMIS.COL_ROOT_DESCENDANTS));
            root = entry;
        }
        return root;
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
        return (List<ObjectEntry>)resp.getFeed(this, APPObjectEntry.class);
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
        throw new UnsupportedOperationException("Not yet implemented");        
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

    public CMISObject getObject(String objectId, ReturnVersion returnVersion) {
        throw new UnsupportedOperationException("Not yet implemented");
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


}
