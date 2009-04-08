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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.nuxeo.chemistry.client.common.xml.StaxReader;



/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class AbstractEntryReader<T> implements EntryReader<T> {

    protected abstract T newEntry(Object context, StaxReader reader);
    
    protected boolean isDone(StaxReader reader) {
        return false;
    }
    
    public T read(Object context, File file) throws XMLStreamException, IOException {
        InputStream in = new FileInputStream(file);
        try {
            return read(context, in);
        } finally {
            in.close();
        }
    }
    
    public T read(Object context, URL url) throws XMLStreamException, IOException {
        InputStream in = url.openStream();
        try {
            return read(context, in);
        } finally {
            in.close();
        }
    }
    
    public T read(Object context, InputStream in) throws XMLStreamException {        
        StaxReader xr = StaxReader.newReader(in);
        try {
            return read(context, xr);
        } finally {
            xr.close();
        }
    }

    public T read(Object context, Reader reader) throws XMLStreamException {
        StaxReader xr = StaxReader.newReader(reader);
        try {
            return read(context, xr);
        } finally {
            xr.close();
        }
    }
    
    public T read(Object context, StaxReader reader) throws XMLStreamException {
        if (!reader.fwd()) {
            throw new XMLStreamException("Parse error: empty XML");
        }
        if (!ENTRY.equals(reader.getName())) {
            throw new XMLStreamException("Parse error: Not an atom entry");
        }
        return readEntry(context, reader);
    }

    public T readEntry(Object context, StaxReader reader) throws XMLStreamException {
        T entry = newEntry(context, reader);
        while(reader.fwdTag() && !isDone(reader)) {
            QName name = reader.getName();
            if (ATOM_NS.equals(name.getNamespaceURI())) {
                readAtomTag(context, reader, name, entry);
            } else {
                readExtensionTag(context, reader, name, entry);
            }
        }
        return entry;
    }
    
    protected void readAtomTag(Object context, StaxReader reader, QName name, T entry) throws XMLStreamException {
        
    }
    
    protected void readExtensionTag(Object context, StaxReader reader, QName name, T entry) throws XMLStreamException {
        
    }
    
}
