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
package org.nuxeo.ecm.core.opencmis.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.commons.data.ObjectData;
import org.apache.chemistry.opencmis.commons.data.Properties;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.chemistry.opencmis.commons.data.PropertyString;
import org.apache.chemistry.opencmis.commons.spi.BindingsObjectFactory;
import org.apache.chemistry.opencmis.commons.spi.Holder;
import org.apache.chemistry.opencmis.commons.spi.ObjectService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.ecm.core.opencmis.impl.client.NuxeoBinding;
import org.nuxeo.ecm.core.opencmis.impl.server.NuxeoTypeHelper;
import org.nuxeo.ecm.core.opencmis.tests.Helper;
import org.nuxeo.runtime.api.Framework;

public class TestNuxeoBindingComplexProperties extends NuxeoBindingTestCase {

    protected ObjectService objService;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        Helper.makeNuxeoRepository(nuxeotc.getSession());
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public void initBinding() throws Exception {
        super.initBinding();
        objService = binding.getObjectService();
    }

    protected ObjectData getObjectByPath(String path) {
        return objService.getObjectByPath(repositoryId, path, null, null, null,
                null, null, null, null);
    }

    /**
     * Test the encoding logic in ComplexTypelSONHandler
     * and the wiring through the CMIS services
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetComplexListProperty() throws Exception {
        //Enable complex properties
        Framework.getProperties().setProperty(NuxeoTypeHelper.ENABLE_COMPLEX_PROPERTIES, "true");
        
        //Create a complex property to encode
        @SuppressWarnings("rawtypes")
        ArrayList<Map> list = new ArrayList<Map>();
        for (int i = 1; i <= 3; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            list.add(map);
            map.put("stringProp", "testString" + i);
            map.put("enumProp", "ValueA");
            ArrayList<String> arrayProp = new ArrayList<String>();
            map.put("arrayProp", arrayProp);
            for (int j = 1; j <= i; j++) {
                arrayProp.add(Integer.toString(j));
            }
        }

        //Set the property value on a document
        CoreSession session = nuxeotc.session;
        DocumentModel doc = session.createDocumentModel("/",null,"complexFile");
        doc.setPropertyValue("complexTest:listItem", list);
        doc = session.createDocument(doc);
        session.save();
        doc.refresh();
        assertTrue(session.exists(new IdRef(doc.getId())));

        //Get the property as CMIS will see it from the object service
        Properties p = objService.getProperties(repositoryId, doc.getId(), null, null);
        assertNotNull(p);
        List<Object> cmisValues = (List<Object>) 
                p.getProperties().get("complexTest:listItem").getValues();
        assertEquals("Wrong number of marshaled values", list. size(), cmisValues.size());

        //Verify the JSON produced is valid and matches the original objects
        for (int i = 0; i < cmisValues.size(); i++) {
            assertNotNull(cmisValues.get(i));
            JSONObject obj = new JSONObject(cmisValues.get(i).toString());
            Map<String, Object> orig = list.get(i);
            for (String key: orig.keySet()) {
                if (orig.get(key) instanceof ArrayList) {
                    ArrayList<Object> origList = (ArrayList<Object>) orig.get(key);
                    JSONArray array = obj.getJSONArray(key);
                    for (int j = 0; j < origList.size(); j++) {
                        assertEquals("Wrong value at index [" + i + "] key [" + key 
                                + "] index [" + j + "]", origList.get(j), array.get(j));
                    }
                } else {
                    assertEquals("Wrong value at index [" + i + "] key [" + key + "]",
                            orig.get(key), obj.get(key));
                }
            }
        }
    }

    /**
     * Test the decoding logic in ComplexTypeJSONHandler
     * and the wiring through the CMIS services
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSetComplexListProperty() throws Exception {
        //Enable complex properties
        Framework.getProperties().setProperty(NuxeoTypeHelper.ENABLE_COMPLEX_PROPERTIES, "true");

        //Create some JSON to pass into the CMIS service
        ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
        for (int i = 1; i <= 3; i++) {
            JSONObject item = new JSONObject();
            jsonObjects.add(item);
            item.put("stringProp", "testString" + i);
            item.put("enumProp","ValueA");
            JSONArray arrayProp = new JSONArray();
            item.put("arrayProp", arrayProp);
            for (int j = 1; j <= i; j++) {
                arrayProp.put(Integer.toString(j));
            }
        }

        //Get a document with the right property schema
        //Use CMISBinding CoreSession rather than nuxeotc.session
        //to workaround session caching issues that cause intermittent failures
        //CoreSession session = nuxeotc.session;
        CoreSession session = ((NuxeoBinding) binding).getCoreSession();

        DocumentModel doc = session.createDocumentModel("/", null, "complexFile");
        doc = session.createDocument(doc);
        session.save();
        doc.refresh();
        assertTrue(session.exists(new IdRef(doc.getId())));
    
        //Set the property as a JSON string through the CMIS service
        BindingsObjectFactory bof = binding.getObjectFactory();
        ArrayList<String> stringArr = new ArrayList<String>();
        for (JSONObject json : jsonObjects) {
            stringArr.add(json.toString());
        }
        PropertyString prop = bof.createPropertyStringData("complexTest:listItem", stringArr);
        Properties props = bof.createPropertiesData(
                Collections.<PropertyData<?>> singletonList(prop));
        Holder<String> objectIdHolder = new Holder<String>(doc.getId());
        objService.updateProperties(repositoryId, objectIdHolder, null, props, null);

        //Verify the properties produced in Nuxeo match the input JSON
        doc.refresh(DocumentModel.REFRESH_ALL, new String[] {"complexTest"});
        List<Object> list = (List<Object>) doc.getPropertyValue("complexTest:listItem");
        assertNotNull(list);
        assertEquals("Wrong number of elements in list", jsonObjects.size(), list.size());
        for (int i = 0; i < list.size(); i++) {
            JSONObject orig = jsonObjects.get(i);
            Map<String, Object> obj = (Map<String, Object>) list.get(i);
            for (String key : JSONObject.getNames(orig)) {
                if (orig.get(key) instanceof JSONArray) {
                    JSONArray origList = orig.getJSONArray(key);
                    Object[] objList = (Object[]) obj.get(key);
                    for (int j = 0; j < origList.length(); j++) {
                        assertEquals("Wrong value at index [" + i + "] key [" + key 
                                + "] index [" + j + "]", origList.get(j), objList[j]);
                    }
                } else {
                    assertEquals("Wrong value at index [" + i + "] key [" 
                            + key + "]", orig.get(key), obj.get(key));
                }
            }
        }
    }

    /**
      * Test the encoding logic in ComplexTypeJSONHandler
      * and the wiring through the CMIS services
      */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetComplexProperty() throws Exception {
        //Enable complex properties
        Framework.getProperties().setProperty(NuxeoTypeHelper.ENABLE_COMPLEX_PROPERTIES, "true");

        //Create a complex property to encode
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("stringProp", "testString");
        map.put("enumProp","ValueA");
        ArrayList<String> arrayProp = new ArrayList<String>();
        map.put("arrayProp", arrayProp);
        for (int j = 1; j <= 5; j++) {
            arrayProp.add(Integer.toString(j));
        }
        map.put("intProp", 123);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(1234500000000L);
        map.put("dateProp", cal);
        map.put("boolProp", true);
        map.put("floatProp", 123.45d);

        //Set the property value on a document
        CoreSession session = nuxeotc.session;
        DocumentModel doc = session.createDocumentModel("/", null, "complexFile");
        doc.setPropertyValue("complexTest:complexItem", map);
        ByteArrayBlob blob = new ByteArrayBlob("Test content".getBytes("UTF-8"), 
                "text/plain", "UTF-8", "test.txt", null);
        doc.setProperty("file", "content", blob);
        doc = session.createDocument(doc);
        session.save();
        doc.refresh();
        assertTrue(session.exists(new IdRef(doc.getId())));

        //Get the property as CMIS will see it from the object service
        Properties p = objService.getProperties(repositoryId, doc.getId(), null, null);
        assertNotNull(p);
        String jsonStr = p.getProperties().get(
                "complexTest:complexItem").getFirstValue().toString();
        assertEquals("Complex item should get marshaled as a single string value", 1,
                p.getProperties().get("complexTest:complexItem").getValues().size());
          
        //Verify the JSON produced is valid and matches the original objects
        assertNotNull(jsonStr);
        JSONObject obj = new JSONObject(jsonStr);
        for (String key : map.keySet()) {
            if (map.get(key) instanceof ArrayList) {
                ArrayList<Object> origList = (ArrayList<Object>) map.get(key);
                JSONArray array = obj.getJSONArray(key);
                for (int i = 0; i < origList.size(); i++) {
                    assertEquals("Wrong value at key [" + key + "] index [" + i + "]",
                            origList.get(i), array.get(i));
                }
            } else {
                Object originalVal;
                if (map.get(key) instanceof Calendar) {
                    originalVal = ((Calendar) map.get(key)).getTimeInMillis();
                } else {
                    originalVal = map.get(key);
                }
                assertEquals("Wrong value at key [" + key + "]", originalVal, obj.get(key));
            }
        }
    }

    /**
     * Test the decoding logic in ComplexTypeJSONHandler
     * and the wiring through the CMIS services
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testSetComplexProperty() throws Exception {
        //Enable complex properties
        Framework.getProperties().setProperty(NuxeoTypeHelper.ENABLE_COMPLEX_PROPERTIES, "true");

        //Create some JSON to pass into the CMIS service
        JSONObject item = new JSONObject();
        item.put("stringProp", "testString");
        item.put("enumProp","ValueA");
        JSONArray arrayProp = new JSONArray();
        item.put("arrayProp", arrayProp);
        for (int i = 1; i <= 5; i++) {
            arrayProp.put(Integer.toString(i));
        }
        item.put("intProp", 123L);
        item.put("dateProp", 1234500000000L);
        item.put("floatProp", 123.456);
        item.put("boolProp", false);
        String jsonStr = item.toString();

        //Get a document with the right property schema
        CoreSession session = nuxeotc.session;
        DocumentModel doc = session.createDocumentModel("/", null, "complexFile");
        doc = session.createDocument(doc);
        session.save();
        doc.refresh();
        assertTrue(session.exists(new IdRef(doc.getId())));
                
        //Set the property as a JSON string through the CMIS service
        Properties props = createProperties("complexTest:complexItem", jsonStr);
        Holder<String> objectIdHolder = new Holder<String>(doc.getId());
        objService.updateProperties(repositoryId, objectIdHolder, null, props, null);

        //Verify the properties produced in Nuxeo match the input JSON
        doc.refresh();
        Map<String, Object> obj = (Map<String, Object>) 
                doc.getPropertyValue("complexTest:complexItem");
        assertNotNull(obj);
        for (String key: JSONObject.getNames(item)) {
            if (item.get(key) instanceof JSONArray) {
                JSONArray origList = item.getJSONArray(key);
                Object[] objList = (Object[]) obj.get(key);
                for (int i = 0; i < origList.length(); i++) {
                    assertEquals("Wrong value at key [" + key + "] index [" + i + "]",
                            origList.get(i), objList[i]);
                }
            } else {
                Object objValue;
                if (obj.get(key) instanceof Calendar) {
                    objValue = ((Calendar) obj.get(key)).getTimeInMillis();
                } else {
                    objValue = obj.get(key);
                }
                assertEquals("Wrong value at key [" + key + "]", item.get(key), objValue);
            }
        }
    }

    /**
     * Test that complex types are not exposed unless the
     * enabled property is set
     */
    public void testEnableComplexTypes() throws Exception {
        //Don't enable complex properties for this test

        //Set a complex property on a document
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("stringProp", "testString");
        
        //Set the property value on a document
        CoreSession session = nuxeotc.session;
        DocumentModel doc = session.createDocumentModel("/", null, "complexFile");
        doc.setPropertyValue("complexTest:complexItem", map);
        doc = session.createDocument(doc);
        session.save();
        doc.refresh();
        assertTrue(session.exists(new IdRef(doc.getId())));

        //Get the property as CMIS will see it from the object service
        Properties p = objService.getProperties(repositoryId, doc.getId(), null, null);
        assertNotNull(p);
        assertNull(
                "Complex property should not be exposed when not enabled in framework properties",
                p.getProperties().get("complexTest:complexItem"));
    }

}
