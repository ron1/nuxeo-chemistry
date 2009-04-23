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
import org.apache.chemistry.property.Property;
import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.ParseException;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class AbstractObjectBuilder<T> extends AbstractEntryBuilder<T> {

    protected abstract void readProperty(BuildContext ctx, StaxReader reader, T object, XmlProperty p);
    
    protected void readCmisElement(BuildContext ctx, StaxReader reader, T object) throws XMLStreamException {
        if (reader.getLocalName().equals(CMIS.OBJECT.getLocalPart())) {
            readCmisObject(ctx, reader, object);
        }
    }
    
    protected void readCmisObject(BuildContext ctx, StaxReader reader, T object) throws XMLStreamException {
        ChildrenNavigator children = reader.getChildren();
        while (children.next()) {
            readObjectChildElement(ctx, reader, object);
        }
    }

    protected void readObjectChildElement(BuildContext ctx, StaxReader reader, T object) throws XMLStreamException {
        if (reader.getNamespaceURI().equals(CMIS.OBJECT.getNamespaceURI())) {
            if (reader.getLocalName().equals(CMIS.PROPERTIES.getLocalPart())) {
                readProperties(ctx, reader, object);
            } else if (reader.getLocalName().equals(CMIS.ALLOWABLE_ACTIONS.getLocalPart())) {                    
                readAllowableActions(ctx, reader, object);
            } else { // unknown tag
                readOtherCmisElement(ctx, reader, object);
            }
        } else {
            readEntryElement(ctx, reader, object);
        }
    }
    
    protected void readProperties(BuildContext ctx, StaxReader reader, T object) throws XMLStreamException {
        PropertyIterator it = new PropertyIterator(reader);
        ArrayList<XmlProperty> prefetch = null;
        Type entryType = ctx.getType();
        if (entryType == null) {
            prefetch = new ArrayList<XmlProperty>();
            while (it.hasNext()) {
                XmlProperty p = it.next();
                //System.out.println(" prefetch >>>>> "+reader.getName()+" -> "+p.value);
                if (Property.TYPE_ID.equals(p.value)) {
                    entryType = ctx.getRepository().getType((String)p.getXmlValue());
                    if (entryType == null) {
                        throw new ParseException("No such type: "+p.value);
                    }
                    prefetch.add(p);
                    break;
                } else {
                    prefetch.add(p);
                }
            }
            if (entryType == null) {
                throw new IllegalStateException("Type not known");
            }
        }
        if (prefetch != null) {
            for (XmlProperty p : prefetch) {
                p.def = entryType.getPropertyDefinition((String)p.value);
                if (p.def == null) {
                    throw new ParseException("No such property definition: "+p.value+" in type "+entryType);
                }
                p.value = XmlProperty.NULL; 
                //System.out.println("adding prefetched >>>>> "+reader.getName()+" -> "+p.getXmlValue());                    
                readProperty(ctx, reader, object, p);
            }
        }
        // consume the rest of the stream
        while (it.hasNext()) {
            XmlProperty p = it.next();
            p.def = entryType.getPropertyDefinition((String)p.value);
            if (p.def == null) {
                throw new ParseException("No such property definition: "+p.value+" in type "+entryType);
            }
            p.value = XmlProperty.NULL; 
            //System.out.println("adding non prefetched >>>>> "+reader.getName()+" -> "+p.getXmlValue());
            readProperty(ctx, reader, object, p);
        }
    }
    
    
    protected void readAllowableActions(BuildContext ctx, StaxReader reader, T object) throws XMLStreamException {
        // TODO not yet implemented
    }

    protected void readOtherCmisElement(BuildContext ctx, StaxReader reader, T object) throws XMLStreamException {
        // do nothing
    }

}
