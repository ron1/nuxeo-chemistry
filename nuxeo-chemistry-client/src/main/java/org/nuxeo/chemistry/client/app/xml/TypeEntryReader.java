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

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.app.APPRepository;
import org.nuxeo.chemistry.client.app.APPType;
import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TypeEntryReader extends AbstractEntryReader<APPType> {

    public final static TypeEntryReader INSTANCE = new TypeEntryReader();
    
    @Override
    protected APPType newEntry(Object context, StaxReader reader) {
        APPType type = new APPType((APPRepository)context);
        return type;
    }
    
    protected void addLink(APPType entry, String type, String href) {
        entry.addLink(type, href);
    }
    

    @Override
    protected void readAtomTag(Object context, StaxReader reader, QName name, APPType entry)
            throws XMLStreamException {
        // read only links - optimization to avoid useless operations
        if ("link".equals(name.getLocalPart())) {
            String rel = reader.getAttributeValue(ATOM_NS, "rel");
            String href = reader.getAttributeValue(ATOM_NS, "href");
            addLink(entry, rel, href);
        }
    }
    
    @Override
    protected void readExtensionTag(Object context, StaxReader reader, QName name, APPType entry)
            throws XMLStreamException {
        if (CMIS.DOCUMENT_TYPE.equals(name)) {
            ChildrenNavigator children = reader.getChildren();
            Map<String,String> map = new HashMap<String,String>();
            while (children.next()) {
                map.put(reader.getLocalName(), reader.getElementText());
            }
            entry.init(map);
        }
    }
    
}
