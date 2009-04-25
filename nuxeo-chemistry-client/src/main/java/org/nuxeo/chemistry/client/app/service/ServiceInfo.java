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

import java.lang.reflect.Constructor;

import org.nuxeo.chemistry.client.app.APPContentManager;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ServiceInfo {

    public String id;
    
    public String title;
    
    public String summary;
    
    public String href;    
    
    protected Class<?> itf;
    protected Constructor<?> ctor;
    protected boolean isSingleton;
    protected boolean requiresConnection;
    
    public ServiceInfo() {
    }
    
    public void setId(String id) {        
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public void setHref(String href) {
        this.href = href;
    }
    
    public String getId() {
        return id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public String getHref() {
        return href;
    }
    
    public boolean isSingleton() {
        getServiceCtor();
        return isSingleton;
    }
    
    public boolean requiresConnection() {
        getServiceCtor();
        return requiresConnection;
    }
    
    public Constructor<?> getServiceCtor() {
        if (ctor == null) {
            try {
                Class<?> itf = Class.forName(id);
                Class<?> clazz = APPContentManager.getServiceClass(itf);
                if (clazz == null) { // no service registered for the given interface
                    return null;
                }
                ExtensionService anno = clazz.getAnnotation(ExtensionService.class);
                if (anno == null) {
                    throw new IllegalStateException("Class "+clazz+" is not an extension service!");
                }
                requiresConnection = anno.requiresConnection();
                isSingleton = anno.singleton();
                ctor = clazz.getConstructor(new Class<?>[] { ServiceContext.class });
            } catch (Exception e) {
                e.printStackTrace(); //TODO handle errors
            }
        }
        return ctor;    
    }
    
    public Class<?> getServiceClass() {
        Constructor<?> ctor = getServiceCtor();
        return ctor == null ? null : ctor.getDeclaringClass();
    }

    public Class<?> getInterfaceClass() {
        return itf;        
    }

    public Object newInstance(ServiceContext ctx) {
        Constructor<?> ctor = getServiceCtor();
        if (ctor == null) {
            return null;
        }
        try {
            return ctor.newInstance(new Object[] {ctx});
        } catch (Exception e) {
            e.printStackTrace();// TODO handle errors
            return null;
        }
    }
}
