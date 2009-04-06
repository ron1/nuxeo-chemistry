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

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class DataMap {
    
    protected Map<String, Value<Object>> map = new HashMap<String, Value<Object>>();

    public Object get(String key) {
        Value<Object> v = map.get(key);
        if (v != null) {
            return v.get();
        }
        return null;
    }
    
    public void set(String key, Object value) {
        Value<Object> v = map.get(key);
        if (v != null) {
            v.set(value);
        }
        throw new NoSuchElementException("No such element in map: "+key);
    }
    
    public String[] getKeys() {
        return map.keySet().toArray(new String[map.size()]);
    }
    
    public int size() {
        return map.size();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    
    @SuppressWarnings("unchecked")
    public void put(String key, Value<?> value) {
        map.put(key, (Value<Object>)value);
    }
}
