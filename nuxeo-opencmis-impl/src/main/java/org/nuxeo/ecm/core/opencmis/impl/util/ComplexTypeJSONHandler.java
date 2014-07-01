/* 
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 */
package org.nuxeo.ecm.core.opencmis.impl.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.nuxeo.ecm.core.schema.types.ComplexType;
import org.nuxeo.ecm.core.schema.types.Field;
import org.nuxeo.ecm.core.schema.types.ListType;
import org.nuxeo.ecm.core.schema.types.SimpleType;
import org.nuxeo.ecm.core.schema.types.Type;

/**
 * Utilities around handling JSON marshalling/unmarshalling of ComplexTypes.
 */
public class ComplexTypeJSONHandler {

    private ComplexTypeJSONHandler() {
        // utility class
    }
    
    public static Serializable decodeList(ListType type, String json) throws JSONException {
        JSONArray array = new JSONArray(json);
        return (Serializable) decodeList(type, array);
    }
    
    protected static List<Object> decodeList(ListType lt, JSONArray jsonArray) throws JSONException {
        List<Object> result = new ArrayList<Object>();
        Type currentObjectType = lt.getFieldType();
        for (int i = 0; i < jsonArray.length(); i++) {
            if (currentObjectType.isListType()) {
                result.add(decodeList((ListType) currentObjectType, 
                        jsonArray.getJSONArray(i)));
            } else if (currentObjectType.isComplexType()) {
                result.add(decodeComplex((ComplexType) currentObjectType,
                        jsonArray.getJSONObject(i)));
            } else if (currentObjectType.isSimpleType()) {
                result.add(currentObjectType.decode(jsonArray.getString(i)));
            }
        }
        return result;
    }

    public static Serializable decodeComplex(ComplexType ct, String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        return (Serializable) decodeComplex(ct, jsonObject);
    }

    protected static Object decodeComplex(ComplexType ct, JSONObject jsonObject) 
            throws JSONException {
        Map<String, Object> result = new HashMap<String, Object>();
        Collection<Field> fields = ct.getFields();
        for (Field field : fields) {
            String name = field.getName().getPrefixedName();
            if (jsonObject.has(name)) {
                if (field.getType().isListType()) {
                    result.put(name, decodeList((ListType) field.getType(), 
                            jsonObject.getJSONArray(name)));
                } else if (field.getType().isComplexType()) {
                    result.put(name, decodeComplex((ComplexType) field.getType(), 
                            jsonObject.getJSONObject(name)));
                } else if (field.getType().isSimpleType()) {
                    SimpleType type = (SimpleType) field.getType();
                    Object value;
                    value = decodeSimple(type, jsonObject, name);
                    result. put(name, value);
                }
            }
        }
        return result;
    }

    private static Object decodeSimple(SimpleType type, JSONObject jsonObject,
            String name) throws JSONException {
        Object value;
        Type[] typeHierarchy = type.getTypeHierarchy();
        String baseTypeName = type.getName();
        if (typeHierarchy != null && typeHierarchy.length > 0) {
            baseTypeName = typeHierarchy[typeHierarchy.length - 1].getName();
        }
        /*
         * Various known types need to be interpreted
         * from JSON primitives
         */
        if (jsonObject.isNull(name)) {
            value = null;
        } else if ("boolean".equals(baseTypeName)) {
            value = jsonObject.getBoolean(name);
        } else if ("decimal".equals(baseTypeName) 
                || "float".equals(baseTypeName)
                || "double".equals(baseTypeName)) {
            value = jsonObject.getDouble(name);
        } else if ("integer".equals(baseTypeName)
                || "long".equals(baseTypeName)
                || "int".equals(baseTypeName)
                || "short".equals(baseTypeName)
                || "byte".equals(baseTypeName)
                || baseTypeName.endsWith("Integer")
                || baseTypeName.endsWith("Long")
                || baseTypeName.endsWith("Int")
                || baseTypeName.endsWith("Short")
                || baseTypeName.endsWith("Byte")) {
            value = jsonObject.getLong(name);
        } else if ("dateTime".equals(baseTypeName)
                || "date".equals(baseTypeName)
                || "time".equals(baseTypeName)) {
            long time = jsonObject.getLong(name);
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(time);
            value = cal;
        } else {
            //Not a type with a specific JSON encoding;
            //let the field's Type class decode it
            value = type.decode(jsonObject.getString(name));
        }
        return value;
    }

    public static String encodeComplex(ComplexType type, Map<String, Object> value) 
            throws JSONException {
        JSONObject root = encodeToJSON(type, value);
        return root.toString();
    }
    
    @SuppressWarnings("unchecked")
    protected static JSONObject encodeToJSON(ComplexType type, Map<String, Object> value) 
            throws JSONException {
        JSONObject obj = new JSONObject();
        for (Field field: type.getFields()) {
            String name = field.getName().getPrefixedName();
            if (value.containsKey(name)) {
                if (field.getType().isComplexType()) {
                    obj.put(name, encodeToJSON((ComplexType) field.getType(),
                            (Map<String, Object>) value.get(name)));
                } else if (field.getType().isListType()) {
                    obj.put(name, encodeToJSON((ListType) field.getType(), 
                            Arrays.asList((Object[]) value.get(name))));
                } else if (field.getType().isSimpleType()) {
                    Object val = value.get(name);
                    /*
                     * For various known types, using specific setter methods
                     * on JSONObject will stop it from writing everything as
                     * quoted strings.
                     */
                    if (val == null) {
                        //Nothing to set
                    } else if (val instanceof Long) {
                        obj.put(name, ((Long) val).longValue());
                    } else if (val instanceof Integer) {
                        obj.put(name, ((Integer) val).longValue());
                    } else if (val instanceof Double) {
                        obj.put(name, ((Double) val).doubleValue());
                    } else if (val instanceof Float) {
                        obj.put(name, ((Float) val).floatValue());
                    } else if (val instanceof Boolean) {
                        obj.put(name, ((Boolean) val).booleanValue());
                    } else if (val instanceof Calendar) {
                        obj.put(name, ((Calendar) val).getTimeInMillis());
                    } else if (val instanceof Date) {
                        obj.put(name, ((Date) val).getTime());
                    } else {
                        //Not a type with a specific 35ON encoding;
                        //let the field's Type class string-­encode it
                        obj.put(name, field.getType().encode(value.get(name)));
                    }
                }
            }
        }
        return obj;
    }
    
    public static String encodeList(ListType type, List<Object> value) throws JSONException {
        JSONArray array = encodeToJSON(type, value);
        return array.toString();
    }

    @SuppressWarnings("unchecked")
    protected static JSONArray encodeToJSON(ListType type, List<Object> value) 
            throws JSONException {
        JSONArray array = new JSONArray();
        for (Object obj : value) {
            if (type.getFieldType().isListType()) {
                array.put(encodeToJSON((ListType) type.getFieldType(), (List<Object>) obj));
            } else if (type.getFieldType().isComplexType()) {
                array.put(encodeToJSON((ComplexType) type.getFieldType(), (Map<String, Object>) obj));
            } else if (type.getFieldType().isSimpleType()) {
                array.put(type.getFieldType().encode(obj));    
            }
        }
        return array;
    }
    
}
