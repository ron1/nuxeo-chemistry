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
import java.util.List;

import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.common.atom.BuildContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class APPObject {
    
    protected List<String> links;


    public APPObject() {
        links = new ArrayList<String>();
    }

    public abstract Connector getConnector();

        
    public void addLink(String rel, String href) {
        links.add(rel == null ? "" : rel);
        links.add(href);
    }

    public String[] getLinks() {
       return links.toArray(new String[links.size()]);  
    }
    
    public String getLink(String rel) {
        for (int i=0, len=links.size(); i<len; i+=2) {
            if (rel.equals(links.get(i))) {
                return links.get(i+1);
            }
        }
        return null;
    }

    public <T> T getRemoteEntity(BuildContext ctx, String href, Class<T> clazz) {
        Request req = new Request(href);
        Response resp = getConnector().get(req);
        if (!resp.isOk()) {
            throw new ContentManagerException("Remote server returned error code: "+resp.getStatusCode());
        }        
        return (T)resp.getEntity(ctx, clazz);
    }


    public <T> T getLinkedEntity(BuildContext ctx, String linkRel, Class<T> clazz) {
        String href = getLink(linkRel);
        return href == null ? null : getRemoteEntity(ctx, href, clazz);
    }
    
    public String getEditLink() {
        String href = getLink("edit");
        return href == null ? getLink("self") : href;
    }       
        

}
