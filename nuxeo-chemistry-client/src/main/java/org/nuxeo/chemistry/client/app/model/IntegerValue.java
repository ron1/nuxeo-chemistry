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

import javax.xml.namespace.QName;

import org.apache.chemistry.atompub.CMIS;



/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class IntegerValue extends Value<Integer> {

    protected String raw;
    protected Integer value;

    public IntegerValue() {
    }
    
    public IntegerValue(String value) {
        this.raw = value;
    }
    
    @Override
    public QName getCmisTagName() {
        return CMIS.PROPERTY_INTEGER;
    }
    
    
    public Integer get() {
        if (value == null) {
            value = Integer.valueOf(raw);
        }
        return value;
    }
    
    public void set(Integer value) {
        this.value = value;
        raw = null;
    }
    
    public String getXML() {
        if (raw == null) {
            return value.toString();
        }
        return raw;
    }
    
    @Override
    public void setXML(String val) {
        this.raw = val;
        this.value = null;
    }
    
    @Override
    public Class<?> getType() {
        return Integer.class;
    }

    public static IntegerValue fromNumber(Number value) {
        IntegerValue v = new IntegerValue();
        v.value = new Integer(value.intValue());
        return v;
    }

}
