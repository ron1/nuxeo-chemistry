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


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.repository.Repository;
import org.nuxeo.chemistry.client.ContentManager;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.Credentials;
import org.nuxeo.chemistry.client.CredentialsProvider;
import org.nuxeo.chemistry.client.NoSuchRepositoryException;
import org.nuxeo.chemistry.client.app.httpclient.HttpClientConnector;
import org.nuxeo.chemistry.client.common.AdapterManager;
import org.nuxeo.chemistry.client.common.atom.ReadContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPContentManager implements ContentManager {

    protected String baseUrl;
    protected Connector connector;

    protected Repository[] repos;
    
    protected CredentialsProvider login;
    protected IOProvider ioProvider;
    
    protected static ThreadLocal<List<CredentialsProvider>> loginStack = new ThreadLocal<List<CredentialsProvider>>();
    protected static Map<Class<?>, Class<?>> services = new Hashtable<Class<?>, Class<?>>();
    

    public APPContentManager(String url) {
        this (url, null);
    }

            
    protected APPContentManager(String url, Connector connector) {
        this.baseUrl = url;
        this.connector = connector;
        initialize();
    }


    protected void initialize() {
        if (connector == null) {
            connector = createConnector();
        }
    }
    
    
    protected AdapterManager createAdapterManager() {
        return new AdapterManager();
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


    public Repository[] getRepositories() throws ContentManagerException {
        if (repos == null) {
            Request req = new Request(getBaseUrl());
            Response resp = connector.get(req);
            if (!resp.isOk()) {
                throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
            }
            ReadContext ctx = new ReadContext();
            ctx.put(APPContentManager.class, this);
            repos = resp.getServiceDocument(ctx);
        }
        return repos;
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
        repos = null;
    }
    
    public void login(String username, String pass) {
        login = new DefaultCredentialsProvider(username, pass.toCharArray());
    }

    public void pushLogin(String username, String pass) {
        List<CredentialsProvider> stack = loginStack.get();
        if (stack == null) {
            stack = new ArrayList<CredentialsProvider>();
            loginStack.set(stack);
        }
        stack.add(new DefaultCredentialsProvider(username, pass.toCharArray()));
    }
    
    
    public void popLogin() {
        List<CredentialsProvider> stack = loginStack.get();
        if (stack != null && !stack.isEmpty()) {
            stack.remove(stack.size()-1);
        }
    }
    
    public void logout() {
        login = null;
    }
    
    public Credentials getCurrentLogin() {
        List<CredentialsProvider> stack = loginStack.get();
        return stack == null || stack.isEmpty() ? login.getCredentials() : stack.get(stack.size()-1).getCredentials();
    }
    
    public CredentialsProvider getCredentialsProvider() {
        return login;
    }    

    public void setCredentialsProvider(CredentialsProvider provider) {
        login = provider;
    }

    public IOProvider getIO() {
        if (ioProvider == null) {
            ioProvider = new DefaultIOProvider();
        }
        return ioProvider;
    }

    public void setIO(IOProvider readers) {
        this.ioProvider = readers;
    }
    
    
    public static void registerService(Class<?> itf, Class<?> impl) {
        services.put(itf, impl);
    }
    
    public static void unregisterService(Class<?> itf) {
        services.remove(itf);
    }
    
    public static Class<?> getServiceClass(Class<?> itf) {
        return services.get(itf);
    }
    
}
