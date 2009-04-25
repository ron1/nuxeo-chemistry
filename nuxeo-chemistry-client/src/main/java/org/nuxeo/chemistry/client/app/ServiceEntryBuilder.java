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

import org.nuxeo.chemistry.client.app.service.ServiceInfo;
import org.nuxeo.chemistry.client.common.atom.AbstractEntryBuilder;
import org.nuxeo.chemistry.client.common.atom.BuildContext;
import org.nuxeo.chemistry.client.common.xml.ParseException;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ServiceEntryBuilder extends AbstractEntryBuilder<ServiceInfo> {

    
    
    @Override
    protected ServiceInfo createObject(BuildContext ctx) {
        return new ServiceInfo();
    }


    @Override
    protected void readAtomElement(BuildContext ctx, StaxReader reader,
            ServiceInfo object) throws XMLStreamException {
        String name = reader.getLocalName();        
        if ("id".equals(name)) {
            String id = reader.getElementText();
            int p = id.lastIndexOf(':');
            if (p == -1) {
                throw new ParseException("Invalid service id. "+id);
            }
            id = id.substring(p+1);
            object.setId(id);
        } else if ("link".equals(name)) {
            String rel = reader.getAttributeValue("rel"); 
            if ("edit".equals(rel)) {
                object.setHref(rel);
            }
        } else if ("title".equals(name)) {
            object.setTitle(reader.getElementText());
        } else if ("content".equals(name)) {
            object.setSummary(reader.getElementText());
        }
    }
    
}
