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

import java.net.URI;

/**
 * TODO use URI ?
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class URIValue extends Value<String> {

    protected String value;

    public URIValue() {
    }
    
    public URIValue(String value) {
        this.value = value;
    }
    
    public void set(String value) {
        this.value = value;
    }
    
    public String get() {
        return value;
    }
    
    public String getXML() {
        return value;
    }
    
    @Override
    public void setXML(String val) {
        this.value = val;
    }
    
    @Override
    public Class<?> getType() {
        return String.class;
    }
    
    public static URIValue fromURI(URI value) {
        return new URIValue(value.toString());
    }


}
