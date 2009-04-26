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
package org.nuxeo.chemistry.client.app.httpclient;

import java.util.List;

import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.repository.Repository;
import org.apache.chemistry.type.Type;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.app.APPContentManager;
import org.nuxeo.chemistry.client.app.Connector;
import org.nuxeo.chemistry.client.app.Request;
import org.nuxeo.chemistry.client.app.Response;
import org.nuxeo.chemistry.client.common.atom.ReadContext;
import org.nuxeo.chemistry.client.common.atom.XmlObjectWriter;



/**
 *
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 */
public class HttpClientConnector implements Connector {

    protected HttpClient client;
    protected APPContentManager cm;

    public HttpClientConnector(APPContentManager cm) {
        this.cm = cm;
        this.client = new HttpClient();
        this.client.getParams().setAuthenticationPreemptive(true);
        this.client.getParams().setParameter(
                CredentialsProvider.PROVIDER, new MyCredentialsProvider());
    }

    public APPContentManager getAPPContentManager() {
        return cm;
    }


    protected void setMethodParams(HttpMethod method, Request request) {
        List<String> params = request.getParameters();
        if (params != null) {
            int len=params.size()>>1;
            if (len > 0) {
                NameValuePair[] qs = new NameValuePair[len];
                for (int i=0, k=0; i<len; i++,k+=2) {
                    qs[i] =  new NameValuePair(params.get(k), params.get(k+1));
                }
                method.setQueryString(qs);
            }
        }
    }

    protected void setMethodHeaders(HttpMethod method, Request request) {
        List<String> headers = request.getHeaders();
        if (headers != null) {
            int len=headers.size();
            for (int k=0; k<len; k+=2) {
                method.addRequestHeader(headers.get(k), headers.get(k+1));
            }
        }
    }


    public Response get(Request request) throws ContentManagerException {
        try {
            GetMethod method = new GetMethod(request.getUrl());
            setMethodParams(method, request);
            setMethodHeaders(method, request);
            client.executeMethod(method);
            return new HttpClientResponse(this, method);
        } catch (Exception e) {
            throw new ContentManagerException("GET request failed", e);
        }
    }

    public Response delete(Request request) throws ContentManagerException {
        try {
            DeleteMethod method = new DeleteMethod(request.getUrl());
            setMethodParams(method, request);
            setMethodHeaders(method, request);
            client.executeMethod(method);
            return new HttpClientResponse(this, method);
        } catch (Exception e) {
            throw new ContentManagerException("DELETE request failed", e);
        }
    }

    public Response head(Request request) throws ContentManagerException {
        try {
            HeadMethod method = new HeadMethod(request.getUrl());
            setMethodParams(method, request);
            setMethodHeaders(method, request);
            client.executeMethod(method);
            return new HttpClientResponse(this, method);
        } catch (Exception e) {
            throw new ContentManagerException("HEAD request failed", e);
        }
    }

    public <T> Response post(Request request, XmlObjectWriter<T> writer, T object) throws ContentManagerException {
        try {
            PostMethod method = new PostMethod(request.getUrl());
            setMethodParams(method, request);
            setMethodHeaders(method, request);
            method.setRequestEntity(new ObjectRequestEntity(writer, object));
            client.executeMethod(method);
            return new HttpClientResponse(this, method);
        } catch (Exception e) {
            throw new ContentManagerException("POST request failed", e);
        }
    }

    public <T> Response put(Request request, XmlObjectWriter<T> writer, T object) throws ContentManagerException {
        try {
            PutMethod method = new PutMethod(request.getUrl());
            setMethodParams(method, request);
            setMethodHeaders(method, request);
            method.setRequestEntity(new ObjectRequestEntity(writer, object));
            client.executeMethod(method);
            return new HttpClientResponse(this, method);
        } catch (Exception e) {
            throw new ContentManagerException("PUT request failed", e);
        }
    }
    

    public Type getType(ReadContext ctx, String href) {
        Request req = new Request(href);
        Response resp = get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode()+"\n\n"+resp.getString());
        }        
        return resp.getType(ctx);
    }

    public ObjectEntry getObject(ReadContext ctx, String href) {
        Request req = new Request(href);
        Response resp = get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode()+"\n\n"+resp.getString());
        }        
        return resp.getObject(ctx);
    }

    public List<ObjectEntry> getObjectFeed(ReadContext ctx, String href)
            throws ContentManagerException {
        Request req = new Request(href);
        Response resp = get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode()+"\n\n"+resp.getString());
        }        
        return resp.getObjectFeed(ctx);
    }

    public List<ObjectEntry> getTypeFeed(ReadContext ctx, String href)
            throws ContentManagerException {
        Request req = new Request(href);
        Response resp = get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode()+"\n\n"+resp.getString());
        }        
        return resp.getObjectFeed(ctx);
    }
    
    public Repository[] getServiceDocument(ReadContext ctx, String href)
            throws ContentManagerException {
        Request req = new Request(href);
        Response resp = get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode()+"\n\n"+resp.getString());
        }        
        return resp.getServiceDocument(ctx);
    }

    public Response putObject(Request req, ObjectEntry entry)
            throws ContentManagerException {
        return put(req, cm.getIO().getObjectEntryWriter(), entry);
    }
    
    public Response putQuery(Request req, String query)
            throws ContentManagerException {
        return put(req, cm.getIO().getQueryWriter(), query);
    }

    public Response postObject(Request req, ObjectEntry entry)
        throws ContentManagerException {
        return post(req, cm.getIO().getObjectEntryWriter(), entry);
    }

    public Response postQuery(Request req, String query)
        throws ContentManagerException {
        return post(req, cm.getIO().getQueryWriter(), query);
    }

    protected class MyCredentialsProvider implements CredentialsProvider {
        public Credentials getCredentials(AuthScheme scheme, String host,
                int port, boolean proxy)
                throws CredentialsNotAvailableException {
            org.nuxeo.chemistry.client.CredentialsProvider login = (org.nuxeo.chemistry.client.CredentialsProvider)cm.getCurrentLogin();
            if (login == null) {
                return null;
            }
            org.nuxeo.chemistry.client.Credentials credentials = login.getCredentials();
            if (credentials == null) {
                return null;
            }
            return new UsernamePasswordCredentials(credentials.getUsername(), new String(credentials.getPassword()));
        }
    }

}
