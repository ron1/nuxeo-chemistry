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

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.property.PropertyDefinition;
import org.nuxeo.chemistry.client.common.atom.AbstractEntryReader;
import org.nuxeo.chemistry.client.common.atom.ReadContext;
import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TypeEntryReader extends AbstractEntryReader<APPType> {

    public final static TypeEntryReader INSTANCE = new TypeEntryReader();
    
    @Override
    protected APPType createObject(ReadContext ctx) {
        APPType type = new APPType((APPRepository)ctx.getRepository());
        return type;
    }
    

    @Override
    protected void readAtomElement(ReadContext ctx, StaxReader reader,
            APPType object) throws XMLStreamException {
        // read only links - optimization to avoid useless operations
        if ("link".equals(reader.getLocalName())) {
            String rel = reader.getAttributeValue(ATOM_NS, "rel");
            String href = reader.getAttributeValue(ATOM_NS, "href");
            object.addLink(rel, href);
        }
    }
    
    @Override
    protected void readCmisElement(ReadContext context, StaxReader reader, APPType entry)
            throws XMLStreamException {
        if (CMIS.DOCUMENT_TYPE.getLocalPart().equals(reader.getLocalName())) {
            ChildrenNavigator children = reader.getChildren();
            Map<String,String> map = new HashMap<String,String>();
            Map<String, PropertyDefinition> pdefs = null;
            while (children.next()) {
                if (reader.getLocalName().startsWith("property")) {
                    if (pdefs == null) {
                        pdefs = new HashMap<String,PropertyDefinition>();
                    }
                    PropertyDefinition pdef = readPropertyDef(reader);
                    if (pdef.getName() == null){
                        throw new IllegalArgumentException("Invalid property definition: no name given");
                    }
                    pdefs.put(pdef.getName(), pdef);
                } else {                
                    try {
                        map.put(reader.getLocalName(), reader.getElementText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            entry.init(map, pdefs);
        }
    }

    protected PropertyDefinition readPropertyDef(StaxReader reader) throws XMLStreamException {
        return APPPropertyDefinition.fromXml(reader);
    }
}
