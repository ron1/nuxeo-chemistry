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



/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class BooleanValue extends Value<Boolean> {

    protected Boolean value;

    public BooleanValue() {
    }
    
    public BooleanValue(String xml) {
        value = Boolean.valueOf(xml);
    }
    
    public Boolean get() {
        return value;
    }
    
    public void set(Boolean value) {
        this.value = value; 
    }
    
    public String getXML() {
        return value.toString();
    }
    
    @Override
    public void setXML(String val) {
        this.value = Boolean.valueOf(val);
    }
    
    @Override
    public Class<?> getType() {
        return Boolean.class;
    }
    
}
