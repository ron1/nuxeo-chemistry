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
package org.nuxeo.chemistry.client.app;

import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.common.atom.AbstractObjectBuilder;
import org.nuxeo.chemistry.client.common.atom.BuildContext;
import org.nuxeo.chemistry.client.common.atom.XmlProperty;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPObjectEntryBuilder extends AbstractObjectBuilder<APPObjectEntry> {

    private static APPObjectEntryBuilder builder = new APPObjectEntryBuilder(); 
    
    public static APPObjectEntryBuilder getBuilder() {
        return builder;
    }

    @Override
    protected APPObjectEntry createObject(BuildContext ctx) {
        Type type = ctx.getType();
        if (type == null) {
            return new APPObjectEntry((APPConnection)ctx.getConnection());
        } else {
            switch (type.getBaseType()) {
            case DOCUMENT:
                return new APPDocument((APPConnection)ctx.getConnection(), type.getId());
            case FOLDER:
                return new APPFolder((APPConnection)ctx.getConnection(), type.getId());
            case RELATIONSHIP:
                throw new UnsupportedOperationException("Relationship type not yet supported");
            case POLICY:
                throw new UnsupportedOperationException("Relationship type not yet supported");
            }
            throw new UnsupportedOperationException("Type not known: "+type);
        }
    }

    @Override
    protected void readProperty(BuildContext ctx, StaxReader reader, APPObjectEntry object,
            XmlProperty p) {
        object.properties.put(p.getName(), p);
    }

    @Override
    protected void readAtomElement(BuildContext ctx, StaxReader reader, APPObjectEntry object)
            throws XMLStreamException {
        String name = reader.getLocalName();
        if ("link".equals(name)) {
            String rel = reader.getAttributeValue(ATOM_NS, "rel");
            String href = reader.getAttributeValue(ATOM_NS, "href");
            object.addLink(rel, href);
//        } else if ("id".equals(name)) {
//            object.id = new URI(id);
        }        
    }
    
}
