/*
 * Copyright (c) 2006-2014 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 */
package org.nuxeo.ecm.core.opencmis.bindings;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XNodeMap;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * Nuxeo CmisServiceFactory Descriptor.
 */
@XObject(value = "factory")
public class NuxeoCmisServiceFactoryDescriptor {

    @XNode("@name")
    protected String name;

    @XNode("@class")
    private Class<? extends NuxeoCmisServiceFactory> factoryClass;

    public Class<? extends NuxeoCmisServiceFactory> getFactoryClass() {
        return factoryClass == null 
                ? NuxeoCmisServiceFactory.class : factoryClass;
    }

    public void setNuxeoCmisServiceFactoryClass(Class<? extends NuxeoCmisServiceFactory> klass) {
        factoryClass = klass;
    }
    
    @XNodeMap(value = "parameter", key = "@name", type = HashMap.class, componentType = String.class)
    public Map<String, String> factoryParameters = new HashMap<String, String>();

    public NuxeoCmisServiceFactoryDescriptor() {
    }

    /** Copy constructor. */
    public NuxeoCmisServiceFactoryDescriptor(NuxeoCmisServiceFactoryDescriptor other) {
        factoryClass = other.factoryClass;
        factoryParameters = new HashMap<String, String>(other.factoryParameters);
    }

    public void merge(NuxeoCmisServiceFactoryDescriptor other) {
        if (other.factoryClass != null) {
            factoryClass = other.factoryClass;
        }
        factoryParameters.putAll(other.factoryParameters);
    }

}
