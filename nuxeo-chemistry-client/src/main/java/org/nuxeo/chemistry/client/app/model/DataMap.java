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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.common.xml.XMLWriter;

/**
 * Map implementation to store lazy parsed values (from XML strings). 
 * This is internal to the object implementation and must not be used by the user.  
 *  
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
    
    @SuppressWarnings("unchecked")
    public void set(String key, Object value) {
        Value<Object> v = map.get(key);
        if (v != null) {
            v.set(value);
        } else {
            Value<Object> val = (Value<Object>)ValueFactory.createValue(value);
            map.put(key, val);            
        }
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
    

    public void writeTo(XMLWriter xw) throws IOException {
        xw.element(CMIS.PROPERTIES).start();
        
        for (Map.Entry<String, Value<Object>> entry : map.entrySet()) {
            Value<Object> val = entry.getValue();
            xw.element(val.getCmisTagName()).attr(CMIS.NAME, entry.getKey()).start()
                .element(CMIS.VALUE).content(val.getXML())
            .end();
            entry.getKey();
        }
        
        xw.end();
    }
    
}
