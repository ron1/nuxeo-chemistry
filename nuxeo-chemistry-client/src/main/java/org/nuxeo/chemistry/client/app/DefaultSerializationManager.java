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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nuxeo.chemistry.client.ContentManagerException;
import org.nuxeo.chemistry.client.common.ClassLookup;
import org.nuxeo.chemistry.client.common.ClassRegistry;
import org.nuxeo.chemistry.client.common.atom.BuildContext;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@SuppressWarnings("unchecked")
public class DefaultSerializationManager implements SerializationManager, ClassRegistry {

    protected Map<Class<?>, SerializationHandler<?>> registry;

    public DefaultSerializationManager() {
        registry = new HashMap<Class<?>, SerializationHandler<?>>();
    }


    public void put(Class<?> clazz, Object value) {
        registry.put(clazz, (SerializationHandler<?>)value);
    }

    public Object get(Class<?> clazz) {
        return registry.get(clazz);
    }

    public synchronized <T> SerializationHandler<T>  getHandler(Class<T> clazz) {
        return (SerializationHandler<T>)ClassLookup.lookup(clazz, this);
    }

    public <T> SerializationHandler<T> getHandler(String contentType) {
        // TODO not yet implemented
        //return null;
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    public synchronized void registerHandler(SerializationHandler<?> handler) {
        registry.put(handler.getObjectType(), handler);
    }

    public synchronized void unregisterHandler(Class<?> clazz) {
        registry.remove(clazz);
    }

    public void writeEntity(Object object, OutputStream out) throws ContentManagerException {
        SerializationHandler ch = getHandler(object.getClass());
        if (ch == null) {
            throw new ContentManagerException("Content object not registered: "+object.getClass());
        }
        try {
            ch.writeEntity(object, out);
        } catch (IOException e) {
            throw new ContentManagerException("Failed to write object: "+object.getClass(), e);
        }
    }

    public <T> T readEntity(BuildContext context, Class<T> type, InputStream in) throws ContentManagerException {
        SerializationHandler ch = getHandler(type);
        if (ch == null) {
            throw new ContentManagerException("Content object not registered: "+type);
        }
        try {
            return (T)ch.readEntity(context, in);
        } catch (IOException e) {
            throw new ContentManagerException("Failed to read object: "+type, e);
        }
    }

    public <T> List<T> readFeed(BuildContext context, Class<?> type, InputStream in) throws ContentManagerException {
        SerializationHandler ch = getHandler(type);
        if (ch == null) {
            throw new ContentManagerException("Content object not registered: "+type);
        }
        try {
            return ch.readFeed(context, in);
        } catch (IOException e) {
            throw new ContentManagerException("Failed to read object: "+type, e);
        }
    }


}
