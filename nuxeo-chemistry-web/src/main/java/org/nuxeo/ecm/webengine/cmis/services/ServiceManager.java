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
package org.nuxeo.ecm.webengine.cmis.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.chemistry.Connection;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ServiceManager {

    protected Map<String, ServiceDescriptor> services = new ConcurrentHashMap<String, ServiceDescriptor>();
    
    private static ServiceManager instance = new ServiceManager();
    public static ServiceManager getInstance() {
        return instance;
    }
    
    public ServiceDescriptor getServiceDescriptor(String id) {
        return services.get(id);
    }
    
    public void registerService(ServiceDescriptor sd) {
        services.put(sd.getId(), sd);
    }

    public void unregisterService(String id) {
        services.remove(id);
    }
    
    public ServiceDescriptor[] getServices() {
        return services.values().toArray(new ServiceDescriptor[services.size()]);
    }
    
}
