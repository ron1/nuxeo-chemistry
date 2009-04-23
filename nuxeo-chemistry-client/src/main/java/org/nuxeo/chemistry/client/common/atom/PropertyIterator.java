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
package org.nuxeo.chemistry.client.common.atom;

import java.util.ArrayList;

import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.common.xml.ChildrenIterator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * Iterate over property elements in the current CMIS object on the stream.
 * Each iteration produces a {@link XmlProperty} object. The returned object is not yet
 * ready to use since it have a null property definition. The property definition cannot 
 * be known before the object type is detected. This means the caller is responsible 
 * to fill in the property definition as soon as it detects the object type.  
 * To be able to choose the right property definition the caller must know the name of the property. 
 * This name will be passed through the value member. The caller must reset this member to {@link XmlProperty#NULL}
 * after the property definition will be set on the property.
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class PropertyIterator extends ChildrenIterator<XmlProperty> {

    public PropertyIterator(StaxReader sr) throws XMLStreamException {
        super (sr);
    }
    
    @Override
    protected boolean accept() {
        return reader.getNamespaceURI().equals(CMIS.CMIS_NS) 
            && reader.getLocalName().startsWith("property");
    }
    
    @Override
    protected XmlProperty createValue() throws XMLStreamException {
        String key = reader.getAttributeValue(CMIS.NAME);
        if (key == null) {
            throw new XMLStreamException("Parse error. Invalid CMIS property at line: "+ 
                    reader.getLocation().getLineNumber()+". No name specified");
        }
        ValueIterator vi = new ValueIterator(reader);
        XmlProperty xp = new XmlProperty();
        xp.value = key; // use value to temporary store the key
        if (!vi.hasNext()) {
            return xp;
        }
        String val = vi.next();
        if (!vi.hasNext()) {
            xp.xmlValue = val;
            return xp;
        }
        ArrayList<String> vals = new ArrayList<String>();
        vals.add(val);
        do {
            val = vi.next();
            vals.add(val);
        } while (vi.hasNext());
        xp.xmlValue = vals;
        return xp;
    }

    
    public static void main(String[] args) throws Exception {        
        StaxReader sr = StaxReader.newReader(PropertyIterator.class.getResource("test.xml").openStream());
        
        sr.getFirstTag(CMIS.PROPERTIES);
        PropertyIterator pi = new PropertyIterator(sr);
        while (pi.hasNext()) {
            XmlProperty p = pi.next();
            System.out.println(">>>>> "+p.value);    
        }
        
    }
}
