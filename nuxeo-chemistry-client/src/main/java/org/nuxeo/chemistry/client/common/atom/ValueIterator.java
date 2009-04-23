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

import javax.xml.stream.XMLStreamException;

import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.common.xml.ChildrenIterator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ValueIterator extends ChildrenIterator<String> {

    public ValueIterator(StaxReader sr) throws XMLStreamException {
        super (sr);
    }
    
    protected boolean accept() {
        return (reader.getLocalName().equals(CMIS.VALUE.getLocalPart()) 
                && reader.getNamespaceURI().equals(CMIS.VALUE.getNamespaceURI()));
    }

    @Override
    protected String createValue() throws XMLStreamException {
        return reader.getElementText();
    }
    
}
