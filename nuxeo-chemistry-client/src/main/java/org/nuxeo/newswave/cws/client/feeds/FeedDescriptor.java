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
package org.nuxeo.newswave.cws.client.feeds;

import java.util.List;

import org.apache.chemistry.ObjectEntry;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.app.Request;
import org.nuxeo.chemistry.client.app.Response;
import org.nuxeo.chemistry.client.app.model.APPConnection;
import org.nuxeo.chemistry.client.common.atom.ReadContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class FeedDescriptor {

    protected FeedService service;
    protected String url;
    protected String title;
    protected int pageSize; // recommended page size

    protected FeedDescriptor() {
        
    }
    
    public FeedDescriptor(FeedService service, String url, String title, int pageSize) {
        this.service = service;
        this.url = url;
        this.title = title;
        this.pageSize = pageSize;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getPageSize() {
        return pageSize;
    }

    public List<ObjectEntry> query() throws ContentManagerException {
        APPConnection session = (APPConnection)service.getConnection();
        Request req = new Request(url);
        Response resp = session.getConnector().get(req);
        return resp.getObjectFeed(new ReadContext(service.getConnection()));
    }

    public List<ObjectEntry> query(String query) throws ContentManagerException {
        APPConnection session = (APPConnection)service.getConnection();
        Request req = new Request(url);
        req.setParameter("query", query);
        Response resp = session.getConnector().get(req);
        return resp.getObjectFeed(new ReadContext(service.getConnection()));
    }

    public List<ObjectEntry> query(int offset, int pageSize) throws ContentManagerException {
        APPConnection session = (APPConnection)service.getConnection();
        Request req = new Request(url);
        req.setParameter("offset", Integer.toString(offset));
        req.setParameter("length", Integer.toString(pageSize));
        Response resp = session.getConnector().get(req);
        return resp.getObjectFeed(new ReadContext(service.getConnection()));
    }

    public List<ObjectEntry> query(String query, int offset, int pageSize) throws ContentManagerException {
        APPConnection session = (APPConnection)service.getConnection();
        Request req = new Request(url);
        req.setParameter("query", query);
        req.setParameter("offset", Integer.toString(offset));
        req.setParameter("length", Integer.toString(pageSize));
        Response resp = session.getConnector().get(req);
        return resp.getObjectFeed(new ReadContext(service.getConnection()));
    }

}
