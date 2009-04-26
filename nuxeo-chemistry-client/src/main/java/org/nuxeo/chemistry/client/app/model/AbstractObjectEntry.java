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

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Calendar;
import java.util.List;

import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.property.Property;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
@SuppressWarnings("unchecked")
public abstract class AbstractObjectEntry extends APPObject implements ObjectEntry {

    public Serializable getValue(String name, Serializable defaultValue) {
        Serializable value = getValue(name);
        return value == null ? defaultValue : value;
    }

    public String getTypeId() {
        return getString(Property.TYPE_ID);
    }
    
    public String getId() {
        return getString(Property.ID);
    }

    public String getName() {
        return getString(Property.NAME);
    }
    
    public String getCreatedBy() {
        return getString(Property.CREATED_BY);
    }
    
    public Calendar getLastModificationDate() {
        return getDateTime(Property.LAST_MODIFICATION_DATE);
    }        

    public String getCheckinComment() {
        return getString(Property.CHECKIN_COMMENT);
    }
    
    public Calendar getCreationDate() {
        return getDateTime(Property.CREATION_DATE);
    }
    
    public String getChangeToken() {
        return getString(Property.CHANGE_TOKEN);
    }
        
    public String getLastModifiedBy() {
        return getString(Property.LAST_MODIFIED_BY);
    }
    
    public String getVersionLabel() {
        return getString(Property.VERSION_LABEL);
    }
    
    public String getVersionSeriesCheckedOutBy() {
        return getString(Property.VERSION_SERIES_CHECKED_OUT_BY);
    }
    
    public String getVersionSeriesCheckedOutId() {
        return getString(Property.VERSION_SERIES_CHECKED_OUT_ID);
    }
    
    public String getVersionSeriesId() {
        return getString(Property.VERSION_SERIES_ID);
    }
    
    public boolean isImmutable() {
        Boolean value = getBoolean(Property.IS_IMMUTABLE);
        return value != null ? value.booleanValue() : false;
    }
    
    public boolean isLatestMajorVersion() {
        Boolean value = getBoolean(Property.IS_LATEST_MAJOR_VERSION);
        return value != null ? value.booleanValue() : false;
    }
    
    public boolean isLatestVersion() {
        Boolean value = getBoolean(Property.IS_LATEST_VERSION);
        return value != null ? value.booleanValue() : false;
    }
    
    public boolean isMajorVersion() {
        Boolean value = getBoolean(Property.IS_MAJOR_VERSION);
        return value != null ? value.booleanValue() : false;
    }
    
    public boolean isVersionSeriesCheckedOut() {
        Boolean value = getBoolean(Property.IS_VERSION_SERIES_CHECKED_OUT);
        return value != null ? value.booleanValue() : false;
    }
    
    public String getString(String name) {
        return (String)getValue(name);
    }

    public String[] getStrings(String name) {
        Object value = getValue(name);
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            List<String> list = (List<String>)value;
            return list.toArray(new String[list.size()]);
        } else {
            return new String[] {(String)value};
        }
    }
    
    public String getId(String name) {
        return getString(name);
    }
    
    public String[] getIds(String name) {
        return getStrings(name);
    }
    
    public String getHTML(String name) {
        return getString(name);
    }
    
    public String[] getHTMLs(String name) {
        return getStrings(name);
    }

    public String getXML(String name) {
        return getString(name);
    }
    
    public String[] getXMLs(String name) {
        return getStrings(name);
    }
    
    public Calendar getDateTime(String name) {
        return (Calendar)getValue(name);     
    }
    
    public Calendar[] getDateTimes(String name) {
        Object value = getValue(name);
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            List<Calendar> list = (List<Calendar>)value;
            return list.toArray(new Calendar[list.size()]);
        } else {
            return new Calendar[] {(Calendar)value};
        }        
    }
    
    public Boolean getBoolean(String name) {
        return (Boolean)getValue(name);
    }
    
    public Boolean[] getBooleans(String name) {
        Object value = getValue(name);
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            List<Boolean> list = (List<Boolean>)value;
            return list.toArray(new Boolean[list.size()]);
        } else {
            return new Boolean[] {(Boolean)value};
        }
    }
    
    public Integer getInteger(String name) {
        return (Integer)getValue(name);
    }
    
    public Integer[] getIntegers(String name) {
        Object value = getValue(name);
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            List<Integer> list = (List<Integer>)value;
            return list.toArray(new Integer[list.size()]);
        } else {
            return new Integer[] {(Integer)value};
        }
    }
    
    public BigDecimal getDecimal(String name) {
        return (BigDecimal)getValue(name);
    }
    
    public BigDecimal[] getDecimals(String name) {
        Object value = getValue(name);
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            List<BigDecimal> list = (List<BigDecimal>)value;
            return list.toArray(new BigDecimal[list.size()]);
        } else {
            return new BigDecimal[] {(BigDecimal)value};
        }
    }
    
    public URI getURI(String name) {
        return (URI)getValue(name);
    }
    
    public URI[] getURIs(String name) {
        Object value = getValue(name);
        if (value == null) {
            return null;
        }
        if (value instanceof List) {
            List<URI> list = (List<URI>)value;
            return list.toArray(new URI[list.size()]);
        } else {
            return new URI[] {(URI)value};
        }
    }

    
}
