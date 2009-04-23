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
package org.nuxeo.chemistry.client.common.atom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.Connection;
import org.apache.chemistry.type.Type;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class XmlObject {

    protected Type type;
    protected Connection connection;
    protected Map<String,Serializable> properties;
    protected Set<String> allowableActions;
    
    public XmlObject(Connection connection, Type type) {
        this.type = type;
        this.connection = connection;
        properties = new HashMap<String, Serializable>(); 
    }

    public XmlObject(Connection connection) {
        this (connection, null);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public Map<String,Serializable> getProperties() {
        return properties;
    }
    
}
