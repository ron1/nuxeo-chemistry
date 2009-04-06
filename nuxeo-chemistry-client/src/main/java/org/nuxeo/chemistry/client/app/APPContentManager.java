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


import org.apache.chemistry.repository.Repository;
import org.nuxeo.chemistry.client.ContentManager;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.NoSuchRepositoryException;
import org.nuxeo.chemistry.client.app.httpclient.HttpClientConnector;
import org.nuxeo.chemistry.client.common.AdapterFactory;
import org.nuxeo.chemistry.client.common.AdapterManager;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPContentManager implements ContentManager {

    protected String baseUrl;
    protected AdapterManager adapterMgr;
    protected Connector connector;

    protected SerializationManager serializationMgr;
    
    protected AdapterManager adapters;
    protected APPServiceDocument app;
    

    public APPContentManager(String url) {
        this (url, null, null, null);
    }

    protected APPContentManager(String url, Connector connector) {
        this (url, connector, null, null);
    }
            
    protected APPContentManager(String url, Connector connector, SerializationManager serializationMgr, AdapterManager adapterManager) {
        this.baseUrl = url;
        this.connector = connector;
        this.serializationMgr = serializationMgr;
        this.adapters = adapterManager;
        initialize();
    }


    protected void initialize() {
        if (connector == null) {
            connector = createConnector();
        }
        if (serializationMgr == null) {
            serializationMgr = createSerializationManager();
        }
        if (adapterMgr == null) {
            adapterMgr = createAdapterManager();
        }
        initializeHandlers();
    }
    
    protected void initializeHandlers() {
        registerSerializationHandler(new APPServiceDocumentHandler());
        registerSerializationHandler(new APPObjectEntryHandler());
    }
    
    protected AdapterManager createAdapterManager() {
        return new AdapterManager();
    }

    public AdapterManager getAdapterManager() {
        return adapters;
    }

    public synchronized AdapterFactory getAdapterFactory(Class<?> adaptee, Class<?> adapter) {
        return adapters.getAdapterFactory(adaptee, adapter);
    }

    public <T> T getAdapter(Object adaptee, Class<T> adapter) {
        return adapters.getAdapter(adaptee, adapter);
    }

    public void registerAdapters(Class<?> clazz, AdapterFactory factory) {
        adapters.registerAdapters(clazz, factory);
    }


    protected SerializationManager createSerializationManager() {
        return new DefaultSerializationManager();
    }

    protected Connector createConnector() {
        return new HttpClientConnector(this);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Connector getConnector() {
        return connector;
    }

    public SerializationManager getSerializationManager() {
        return serializationMgr;
    }

    public void registerSerializationHandler(SerializationHandler<?> handler) {
        serializationMgr.registerHandler(handler);
    }

    public void unregisterSerializationHandler(Class<?> clazz) {
        serializationMgr.unregisterHandler(clazz);
    }

    public Repository[] getRepositories() throws ContentManagerException {
        if (app == null) {
            Request req = new Request(getBaseUrl());
            Response resp = connector.get(req);
            if (!resp.isOk()) {
                throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
            }
            app = resp.getEntity(this, APPServiceDocument.class);
        }
        return app.getRepositories();
    }

    public Repository getRepository(String id)
            throws NoSuchRepositoryException, ContentManagerException {
        for (Repository repository : getRepositories()) {
            if (repository.getId().equals(id)) {
                return repository;
            }
        }
        throw new NoSuchRepositoryException(baseUrl, id);
    }

    public Repository getDefaultRepository() throws ContentManagerException {
        Repository[] repos = getRepositories();
        if (repos != null && repos.length > 0) {
            return repos[0];
        }
        throw new NoSuchRepositoryException(baseUrl, "default");
    }

    public void refresh() {
        app = null;
    }
    
}
