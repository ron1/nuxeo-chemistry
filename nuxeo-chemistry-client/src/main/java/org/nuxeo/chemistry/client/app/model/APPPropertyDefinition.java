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
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.property.Choice;
import org.apache.chemistry.property.PropertyDefinition;
import org.apache.chemistry.property.PropertyType;
import org.apache.chemistry.property.Updatability;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.chemistry.client.common.atom.ValueAdapter;
import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPPropertyDefinition implements PropertyDefinition {

    public final static Log log = LogFactory.getLog(APPPropertyDefinition.class);
    
    protected Map<String,Object> map;   
    
    protected String name;

    protected PropertyType type;

    protected boolean multiValued;

    protected Updatability updatability;


    protected APPPropertyDefinition(Map<String,Object> map) {
        this.map = map;
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return (String)map.get("id");
    }

    public String getDisplayName() {
        return (String)map.get("displayName");
    }

    public String getDescription() {
        return (String)map.get("description");
    }

    public boolean isInherited() {
        Boolean v = (Boolean)map.get("inherited");
        return v == null ? false : v.booleanValue();
    }

    public PropertyType getType() {
        String t = (String)map.get("propertyType");
        if (t != null) {
            type = types.get(t);
            if (type == null) {
                type = PropertyType.STRING;
            }
        }
        return type;
    }

    public boolean isMultiValued() {
        return multiValued;
    }

    public List<Choice> getChoices() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    protected final boolean getBooleanValue(String name) {
        Boolean v = (Boolean)map.get(name);
        return v != null ? v.booleanValue() : Boolean.FALSE;        
    }


    public boolean isOpenChoice() {
        return getBooleanValue("openChoice");
    }

    public boolean isRequired() {
        return getBooleanValue("required");
    }

    public Serializable getDefaultValue() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Updatability getUpdatability() {
        if (updatability == null) {
            String text = (String)map.get("updateability");
            if ("readonly".equals(text)) {
                updatability = Updatability.READ_ONLY;
            } else if ("whencheckedout".equals(text)) {
                updatability = Updatability.WHEN_CHECKED_OUT;
            } else {
                updatability = Updatability.READ_WRITE;
            }            
        }
        return updatability;
    }

    public boolean isQueryable() {
        return getBooleanValue("queryable");
    }

    public boolean isOrderable() {
        return getBooleanValue("orderable");
    }

    public int getPrecision() {
        Integer v = (Integer)map.get("precision");
        return v != null ? v.intValue() : 32;        
    }

    public Integer getMinValue() {
        Integer v = (Integer)map.get("minValue");
        return v != null ? v.intValue() : null;        
    }

    public Integer getMaxValue() {
        Integer v = (Integer)map.get("maxValue");
        return v != null ? v.intValue() : null;        
    }

    public int getMaxLength() {
        Integer v = (Integer)map.get("maxLength");
        return v != null ? v.intValue() : -1;        
    }

    public URI getSchemaURI() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getEncoding() {
        return (String)map.get("encoding");
    }

    public boolean validates(Serializable value) {
        return validationError(value) == null;
    }

    public String validationError(Serializable value) {
        if (getUpdatability() == Updatability.READ_ONLY) {
            // TODO Updatability.WHEN_CHECKED_OUT
            return "Property is read-only";
        }
        if (value == null) {
            if (isRequired()) {
                return "Property is required";
            }
            return null;
        }
        boolean multi = isMultiValued();
        if (multi != value.getClass().isArray()) {
            return multi ? "Property is multi-valued"
                    : "Property is single-valued";
        }
        Class<?> klass;
        switch (getType()) {
        case STRING:
        case ID:
            klass = String.class;
            break;
        case DECIMAL:
            klass = BigDecimal.class;
            break;
        case INTEGER:
            klass = Integer.class; // TODO Long
            break;
        case BOOLEAN:
            klass = Boolean.class;
            break;
        case DATETIME:
            klass = Calendar.class;
            break;
        case URI:
            klass = URI.class;
            break;
        case XML:
            klass = String.class; // TODO
            break;
        case HTML:
            klass = String.class; // TODO
            break;
        default:
            throw new UnsupportedOperationException(type.toString());
        }
        if (multi) {
            for (int i = 0; i < Array.getLength(value); i++) {
                Object v = Array.get(value, i);
                if (v == null) {
                    return "Array value cannot contain null elements";
                }
                if (!klass.isInstance(v)) {
                    return "Array value has type " + v.getClass()
                            + " instead of " + klass.getName();
                }
            }
        } else {
            if (!klass.isInstance(value)) {
                return "Value has type " + value.getClass() + " instead of "
                        + klass.getName();
            }
        }
        return null;
    }

    
    public static APPPropertyDefinition fromXml(StaxReader reader) throws XMLStreamException {
        HashMap<String,Object> map = new HashMap<String, Object>();
        APPPropertyDefinition pd = new APPPropertyDefinition(map);
        ChildrenNavigator nav = reader.getChildren();
        while (nav.next()) {
            String tag = reader.getLocalName();
            if ("name".equals(tag)) {
                pd.name = reader.getElementText();
            } else if ("cardinality".equals(tag)) {
                String text = reader.getElementText();
                pd.multiValued = isMultiValued(text);
            } else if ("defaultValue".equals(tag)) {
                // TODO not yet implemented
            } else if (tag.startsWith("choice")) {
                //TODO not yet implemented
            } else {
                ValueAdapter adapter = adapters.get(tag);
                Object val = null;
                if (adapter == null) {
                    val = reader.getElementText();
                } else {
                    val = adapter.readValue(reader.getElementText());
                }
                map.put(tag, val);
            }
        }        
        return pd;
    }

    

    
    public static boolean isMultiValued(String text) {
        return "multi".equals(text);
    }

    static abstract class Setter {
        public abstract void set(APPPropertyDefinition pd, StaxReader reader) throws XMLStreamException;
    }

    static Map<String,ValueAdapter> adapters = new HashMap<String, ValueAdapter>();
    static Map<String,PropertyType> types = new HashMap<String, PropertyType>();
    
    static {
        adapters.put("inherited", ValueAdapter.BOOLEAN);
        adapters.put("required", ValueAdapter.BOOLEAN);
        adapters.put("queryable", ValueAdapter.BOOLEAN);
        adapters.put("orderable", ValueAdapter.BOOLEAN);
        adapters.put("openChoice", ValueAdapter.BOOLEAN);
        adapters.put("maxLength", ValueAdapter.INTEGER);

        types.put("string", PropertyType.STRING);
        types.put("boolean", PropertyType.BOOLEAN);
        types.put("integer", PropertyType.INTEGER);
        types.put("decimal", PropertyType.DECIMAL);
        types.put("id", PropertyType.ID);
        types.put("datetime", PropertyType.DATETIME);
        types.put("uri", PropertyType.URI);
        types.put("xml", PropertyType.XML);
        types.put("html", PropertyType.HTML);
    }

}
