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

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.nuxeo.chemistry.client.common.xml.StaxReader;



/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class AbstractEntryReader<T> implements EntryReader<T> {

    protected abstract T newEntry(StaxReader reader);
    
    protected boolean isDone(StaxReader reader) {
        return false;
    }
    
    public T read(StaxReader reader) throws XMLStreamException {
        if (!reader.fwd()) {
            throw new XMLStreamException("Parse error: empty XML");
        }
        if (!ENTRY.equals(reader.getName())) {
            throw new XMLStreamException("Parse error: Not an atom entry");
        }
        return readEntry(reader);
    }

    public T readEntry(StaxReader reader) throws XMLStreamException {
        T entry = newEntry(reader);
        while(reader.fwdTag() && !isDone(reader)) {
            QName name = reader.getName();
            if (ATOM_NS.equals(name.getNamespaceURI())) {
                readAtomTag(reader, name, entry);
            } else {
                readExtensionTag(reader, name, entry);
            }
        }
        return entry;
    }
    
    protected void readAtomTag(StaxReader reader, QName name, T entry) throws XMLStreamException {
        
    }
    
    protected void readExtensionTag(StaxReader reader, QName name, T entry) throws XMLStreamException {
        
    }
    
}
