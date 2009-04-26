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

import java.util.List;

import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.repository.Repository;
import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.common.atom.ReadContext;
import org.nuxeo.chemistry.client.common.atom.XmlObjectWriter;


/**
 *
 * Invokes a remote content manager over HTTP protocols, such as AtomPub.
 *
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface Connector {

    APPContentManager getAPPContentManager();

    <T> Response post(Request operation, XmlObjectWriter<T> writer, T object) throws ContentManagerException;

    <T> Response put(Request operation, XmlObjectWriter<T> writer, T object) throws ContentManagerException;

    Response get(Request operation) throws ContentManagerException;

    Response head(Request operation) throws ContentManagerException;

    Response delete(Request operation) throws ContentManagerException;

    Type getType(ReadContext ctx, String href) throws ContentManagerException;
    
    ObjectEntry getObject(ReadContext ctx, String href) throws ContentManagerException;
    
    List<ObjectEntry> getObjectFeed(ReadContext ctx, String href) throws ContentManagerException;
    
    List<ObjectEntry> getTypeFeed(ReadContext ctx, String href) throws ContentManagerException;
    
    Repository[] getServiceDocument(ReadContext ctx, String href) throws ContentManagerException;

    Response putObject(Request req, ObjectEntry entry) throws ContentManagerException;
    
    Response putQuery(Request req, String query) throws ContentManagerException;

    Response postObject(Request req, ObjectEntry entry) throws ContentManagerException;
    
    Response postQuery(Request req, String query) throws ContentManagerException;

}
