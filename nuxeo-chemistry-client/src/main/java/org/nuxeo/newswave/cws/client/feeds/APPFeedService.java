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

import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.app.Request;
import org.nuxeo.chemistry.client.app.Response;
import org.nuxeo.chemistry.client.app.model.APPConnection;
import org.nuxeo.chemistry.client.app.service.ExtensionService;
import org.nuxeo.chemistry.client.app.service.ServiceContext;
import org.nuxeo.chemistry.client.common.atom.ListFeedReader;
import org.nuxeo.chemistry.client.common.atom.ReadContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@ExtensionService
public class APPFeedService implements FeedService {

    protected String url;
    protected APPConnection connection;
    protected ListFeedReader<FeedDescriptor> reader = new ListFeedReader<FeedDescriptor>(new FeedDescriptorEntryReader());

    public APPFeedService(ServiceContext ctx) {
        url = ctx.getInfo().getHref();
    }
    
    public APPFeedService(APPConnection session) {
        this.connection = session;
    }

    public APPConnection getConnection() {
        return connection;
    }


    public List<FeedDescriptor> getFeeds() throws ContentManagerException {
        Request req = new Request(url); 
        Response resp = connection.getConnector().get(req);
        ReadContext ctx = new ReadContext();
        ctx.put(APPFeedService.class, this);
        try {
            return reader.read(ctx, resp.getStream());
        } catch (Exception e) {
            throw new ContentManagerException(e);
        }
    }

}
