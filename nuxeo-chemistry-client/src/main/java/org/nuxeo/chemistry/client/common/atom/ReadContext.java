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

import org.apache.chemistry.Connection;
import org.apache.chemistry.repository.Repository;
import org.apache.chemistry.type.Type;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ReadContext extends HashMap<Object, Object> {

    private static final long serialVersionUID = 1L;

    protected Type type;
    protected Repository repository;
    protected Connection connection;
    
    
    public ReadContext() {
        
    }
    
    public ReadContext(Connection connection) {
        this (connection, null);
    }

    public ReadContext(Repository repository) {
        this (repository, null);
    }

    public ReadContext(Repository repository, Type type) {
        this.type = type;
        this.repository = repository;
        if (repository == null) {
            throw new IllegalArgumentException("A BuildContext must be bound to a repository");
        }
    }

    public ReadContext(Connection connection, Type type) {
        this.connection = connection;
        this.type = type;
        this.repository = connection.getRepository();
        if (repository == null) {
            throw new IllegalArgumentException("A BuildContext must be bound to a repository");
        }
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
