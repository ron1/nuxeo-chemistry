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
package org.nuxeo.chemistry.client.app.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.app.APPObjectEntry;
import org.nuxeo.chemistry.client.app.model.DataMap;
import org.nuxeo.chemistry.client.app.model.Value;
import org.nuxeo.chemistry.client.app.model.ValueFactory;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * 
 * TODO: optimize date parser
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class CmisEntryReader extends AbstractEntryReader<APPObjectEntry> {
    
    
    public APPObjectEntry newEntry(StaxReader reader) {
        return new APPObjectEntry();
    }

    protected void addLink(APPObjectEntry entry, String type, String href) {
        entry.addLink(type, href);
    }
    
    
    @Override
    protected void readAtomTag(StaxReader reader, QName name,
            APPObjectEntry entry) throws XMLStreamException {
        // read only links - optimization to avoid useless operations
        if ("link".equals(name.getLocalPart())) {
            String rel = reader.getAttributeValue(ATOM_NS, "rel");
            String href = reader.getAttributeValue(ATOM_NS, "href");
            addLink(entry, rel, href);
        }
    }
    
    @Override
    public void readExtensionTag(StaxReader reader, QName name, 
            APPObjectEntry entry) throws XMLStreamException {
        if (CMIS.OBJECT.equals(name)) {
            reader.push();
            while (reader.fwdTag()) {
                if ("properties".equals(reader.getLocalName())) {
                    reader.push();
                    readProperties(reader, entry);
                    reader.pop();
                } else {
                    // ingore other tags - not impl for now   
                }
            }
            reader.pop();
        }
    }
    

    protected void readProperties(StaxReader reader, APPObjectEntry entry) throws XMLStreamException {
        DataMap map = new DataMap();
        while (reader.fwdTag()) {  
            int tok = reader.getEventType();            
            if (tok == START_ELEMENT) {
                String localName = reader.getLocalName();
                if(localName.startsWith("property")) {                    
                    String type = localName.substring(8);
                    String name = reader.getAttributeValue(CMIS.NAME);
                    Object v = readValues(new ValueIterator(reader, name, type));
                    //Value<?> v = readValues(reader, type);
                    map.put(name, (Value<?>)v);
                }
            } 
        }
        entry.init(map);
    }

    @SuppressWarnings("unchecked")
    protected Object readValues(ValueIterator it) throws XMLStreamException {
        Value<?> v = null, last = null;
        if (it.hasNext()) {
            v = ValueFactory.createValue(it.type, it.next());
        }
        last = v;
        while (it.hasNext()) {
            last.next = (Value)ValueFactory.createValue(it.type, it.next());
            last = last.next;
        }
        return v;
    }

    
    static class ValueIterator implements Iterator<String> {
        protected StaxReader sr;
        protected String name;
        protected String type;
        private boolean hasNext;
        protected ValueIterator(StaxReader sr, String name, String type) throws XMLStreamException {      
            this.sr = sr;
            this.type = type;
            this.name = name;
            if (sr.fwd()) {
                int tok = sr.getEventType();
                String localName = sr.getLocalName();
                if (tok == START_ELEMENT && localName.equals("value")) {
                    hasNext = true;
                }
            } else {
                hasNext = false;
            }
        }
        public boolean hasNext() {
            return hasNext;
        }
        public String next() {
            try {
                String value = sr.getElementText();
                if (sr.fwd()) {
                    int tok = sr.getEventType();
                    String localName = sr.getLocalName();
                    if (tok == START_ELEMENT && localName.equals("value")) {
                        hasNext = true;
                    } else {
                        hasNext = false;
                    }
                } else {
                    hasNext = false;
                }
                return value;
            } catch (Exception e) {
                NoSuchElementException ee = new NoSuchElementException();
                ee.initCause(e);
                throw ee;
            }
        }
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }
    
}
