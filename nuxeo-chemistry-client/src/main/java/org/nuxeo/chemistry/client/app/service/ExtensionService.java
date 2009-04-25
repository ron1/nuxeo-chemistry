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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An extension service implementation must have a constructor that takes 
 * one argument of type {@link ServiceContext}
 * used by the service to initialize itslef 
 *  
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtensionService {

    /**
     * Whether or not the service is a singleton (relative to its scope).
     * For connection bound services the singleton will live in the scope of the connection. This means
     * a new different will be used for each connection.
     * If not bound to a connection a singleton service will live as long the repository which is bound id used..
     * Non singleton services will be instantiated at each lookup.   
     * @return true if singleton, false otherwise.
     */
    boolean singleton() default true;
    
    /**
     * Whether or not this service require to be bound on a connection
     * @return true if the service requires a connection, false otherwise
     */
    boolean requiresConnection() default false;
    
}
