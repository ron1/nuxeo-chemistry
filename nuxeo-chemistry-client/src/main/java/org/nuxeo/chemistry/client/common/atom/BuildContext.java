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

import java.util.HashMap;
import java.util.Map;

import org.apache.chemistry.Connection;
import org.apache.chemistry.repository.Repository;
import org.apache.chemistry.type.Type;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class BuildContext {

    protected Connection connection;
    protected Type type;
    protected Repository repository;
    protected Map<String,Object> data; //TODO use a more compact structure since data has few members
    
    public BuildContext() {
        
    }
    
    public BuildContext(Connection connection) {
        this (connection, null);
    }

    public BuildContext(Repository repository) {
        this (repository, null);
    }

    public BuildContext(Repository repository, Type type) {
        this.type = type;
        this.repository = repository;
        if (repository == null) {
            throw new IllegalArgumentException("A BuildContext must be bound to a repository");
        }
    }

    public BuildContext(Connection connection, Type type) {
        this.connection = connection;
        this.type = type;
        this.repository = connection.getRepository();
        if (repository == null) {
            throw new IllegalArgumentException("A BuildContext must be bound to a repository");
        }
    }

    public void setData(String key, Object data) {
        if (this.data == null) {
            this.data = new HashMap<String, Object>(); 
        }
        this.data.put(key, data);
    }

    public Object getData(String key) {
        return data != null ? data.get(key) : null; 
    }

    public Repository getRepository() {
        return repository;
    }
    
    public Connection getConnection() {
        return connection;
    }

    public Type getType() {
        return type;
    }
            
}
