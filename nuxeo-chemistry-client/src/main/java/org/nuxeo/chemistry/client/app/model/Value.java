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
package org.nuxeo.chemistry.client.app.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class Value<T> {

    
    public Value<T> next;
    
    public abstract QName getCmisTagName();
    
    public abstract Class<?> getType();
    
    public abstract void setXML(String val);
    
    public abstract String getXML();
    
    public abstract T get();

    public abstract void set(T value);

    
    public boolean isArray() {
        return next != null;
    }

    @SuppressWarnings("unchecked")
    public T[] asArray() {
        List<T> list = asList();
        Object ar = Array.newInstance(getType(), list.size());        
        return list.toArray((T[])ar);
    }
    
    public List<T> asList() {
        if (next == null) {
            return Collections.singletonList(get());
        } else {
            ArrayList<T> list = new ArrayList<T>();
            list.add(get());
            list.add(next.get());
            Value<T> p = next.next;
            while (p != null) {
                list.add(p.get());
                p = p.next;
            }
            return list;
        }        
    }

    @SuppressWarnings("unchecked")
    public Value<T> newInstance() {
        try {
            return (Value<T>)getClass().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to instantiate value class "+getClass());
        }
    }
    
}
