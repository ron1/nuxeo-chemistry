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

import org.apache.chemistry.Connection;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.app.APPConnection;
import org.nuxeo.chemistry.client.app.APPContentManager;
import org.nuxeo.chemistry.client.app.Request;
import org.nuxeo.chemistry.client.app.Response;
import org.nuxeo.chemistry.client.common.AdapterFactory;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPFeedService implements FeedService {

    protected APPConnection connection;

    public static void install(APPContentManager cm) {
        cm.registerSerializationHandler(new APPFeedsHandler());
        cm.registerAdapters(Connection.class, new AdapterFactory() {
            public Class<?>[] getAdapterTypes() {
                return new Class<?>[] { FeedService.class };
            }
            public <T> T getAdapter(Object obj, Class<T> adapter) {
                return (T)new APPFeedService((APPConnection)obj);
            }
        });
    }

    public APPFeedService(APPConnection session) {
        this.connection = session;
    }

    public APPConnection getConnection() {
        return connection;
    }


    public List<FeedDescriptor> getFeeds() throws ContentManagerException {
        Request req = new Request(connection.getBaseUrl()+"/feeds"); // TODO use atom collections
        Response resp = connection.getConnector().get(req);
        return (List)resp.getFeed(this, FeedDescriptor.class);
    }

}
