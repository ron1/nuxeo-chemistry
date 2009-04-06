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

import java.util.Calendar;

import org.nuxeo.chemistry.client.common.DateParser;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class DateValue extends Value<Calendar> {

    protected String raw;
    protected Calendar value;
    
    public DateValue() {
    }
    
    public DateValue(Calendar value) {
        this.value = value;
    }
    
    public DateValue(String value) {
        this.raw = value;
    }
    
    public String getXML() {
        if (raw == null) {
            return DateParser.formatW3CDateTime(value.getTime());
        }
        return raw;
    }
    
    public Calendar get() {
        if (value == null) {
           value = Calendar.getInstance();
           value.setTime(DateParser.parseW3CDateTime(raw));
        }
        return value;
    }
    
    public void set(Calendar value) {
        this.value = value;
        this.raw = null;
    }
    
    @Override
    public void setXML(String val) {
        this.raw = val;
        this.value  = null;
    }
    
    @Override
    public Class<?> getType() {
        return Calendar.class;
    }
    
}
