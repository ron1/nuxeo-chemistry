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
package org.nuxeo.chemistry.client.app.service;

import org.apache.chemistry.Connection;
import org.apache.chemistry.repository.Repository;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ServiceContext {

    protected ServiceInfo info;
    protected Repository repository;
    protected Connection connection;
    
    public ServiceContext(ServiceInfo info, Repository repository) {
        this.info = info;
        this.repository = repository;
    }
    
    public ServiceContext(ServiceInfo info, Connection connection) {
        this (info, connection.getRepository());
        this.connection = connection;
    }
    
    public ServiceInfo getInfo() {
        return info;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public Repository getRepository() {
        return repository;
    }
    
}
