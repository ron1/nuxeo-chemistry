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
package org.nuxeo.ecm.webengine.cmis;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.nuxeo.ecm.webengine.abdera.AbderaRequest;
import org.nuxeo.ecm.webengine.abdera.AbderaService;
import org.nuxeo.ecm.webengine.atom.WorkspaceResource;
import org.nuxeo.ecm.webengine.model.Resource;
import org.nuxeo.ecm.webengine.model.WebObject;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@WebObject(type="CmisWorkspace")
public class CMISWorkspaceResource extends WorkspaceResource {

    @Path("services")
    public Resource getServices() {
        return ctx.newObject("CmisServices", ws);
    }
    
    @Path("objects/{id}")
    public Resource getObject(@PathParam("id") String id) {        
        return newObject("CmisObject", id, ws);
    }

    @GET
    @Path("types/{id}")
    @Produces("application/atom+xml;type=entry")
    public Response getType(@PathParam("id") String id) {
        AbderaRequest.setParameter(ctx, "objectid", id);
        return AbderaService.getEntry(ctx, ws.getCollection("types_children").getCollectionAdapter());
    }

}
