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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.chemistry.atompub.CMIS;
import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class AbstractEntryReader<T> implements EntryReader<T>, ATOM {

    
    protected abstract T createObject(ReadContext ctx);

    public T read(ReadContext ctx, File file) throws XMLStreamException, IOException {
        InputStream in = new FileInputStream(file);
        try {
            return read(ctx, in);
        } finally {
            in.close();
        }
    }
    
    public T read(ReadContext ctx, URL url) throws XMLStreamException, IOException {
        InputStream in = url.openStream();
        try {
            return read(ctx, in);
        } finally {
            in.close();
        }
    }
    
    public T read(ReadContext ctx, InputStream in) throws XMLStreamException {
        return read(ctx, StaxReader.newReader(in));
    }
        
    public T read(ReadContext ctx, XMLStreamReader reader) throws XMLStreamException {
        return read(ctx, StaxReader.newReader(reader));
    }
    
    public T read(ReadContext ctx, StaxReader reader) throws XMLStreamException {
        if (!reader.getFirstTag(ATOM.ENTRY)) {
            return null;
        }
        T object = createObject(ctx);
        ChildrenNavigator children = reader.getChildren();
        while (children.next()) {
            if (reader.getNamespaceURI().equals(CMIS.CMIS_NS)) {
                readCmisElement(ctx, reader, object);
            } else {
                readEntryElement(ctx, reader, object);
            }
        }
        return object;
    }
    
    protected void readCmisElement(ReadContext ctx, StaxReader reader, T object) throws XMLStreamException {
        // do nothing
    }
    

    protected void readEntryElement(ReadContext ctx, StaxReader reader, T object) throws XMLStreamException {
        if (reader.getNamespaceURI().equals(ATOM.ATOM_NS)) {
            readAtomElement(ctx, reader, object);
        } else {
            readExtensionElement(ctx, reader, object);
        }
    }

    
    protected void readAtomElement(ReadContext ctx, StaxReader reader, T object) throws XMLStreamException {
        // do nothing
    }

    protected void readExtensionElement(ReadContext ctx, StaxReader reader, T object) throws XMLStreamException {
        // do nothing
    }

}
