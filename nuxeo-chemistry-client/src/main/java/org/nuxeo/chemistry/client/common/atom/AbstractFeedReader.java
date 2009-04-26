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
import java.io.Reader;
import java.net.URL;

import javax.xml.stream.XMLStreamException;

import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class AbstractFeedReader<T, E> implements FeedReader<T>, ATOM {
    
        
    protected EntryReader<E> entryBuilder;
    
    protected abstract T createFeed(StaxReader reader);
    protected abstract void addEntry(T feed, E entry);

    
    
    protected AbstractFeedReader(EntryReader<E> entryBuilder) {
        this.entryBuilder = entryBuilder;
    }

    public EntryReader<E> getEntryBuilder() {
        return entryBuilder;
    }
    
    public void setEntryBuilder(EntryReader<E> entryBuilder) {
        this.entryBuilder = entryBuilder;
    }
    
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
        StaxReader xr = StaxReader.newReader(in);
        try {
            return read(ctx, xr);
        } finally {
            xr.close();
        }
    }

    public T read(ReadContext ctx, Reader reader) throws XMLStreamException {
        StaxReader xr = StaxReader.newReader(reader);
        try {
            return read(ctx, xr);
        } finally {
            xr.close();
        }
    }

    
    public T read(ReadContext ctx, StaxReader reader) throws XMLStreamException {
        if (!reader.getFirstTag(ATOM.FEED)) {
            throw new XMLStreamException("Parse error: Not an atom feed");
        }
        //create a new feed object to be filled 
        T feed = createFeed(reader);
        ChildrenNavigator nav = reader.getChildren();
        while(nav.next() && !isDone(ctx, reader)) {
            String nsUri = reader.getNamespaceURI();            
            if (ATOM.ATOM_NS.equals(nsUri)) {
                if ("entry".equals(reader.getLocalName())) {
                    addEntry(feed, entryBuilder.read(ctx, reader));
                } else {
                    readAtomElement(ctx, reader, nsUri, feed);
                }
            } else {
                readExtensionElement(ctx, reader, nsUri, feed);
            }
        }
        return feed;
    }
    
    protected boolean isDone(ReadContext ctx, StaxReader reader) throws XMLStreamException {
        return false;
    }


    protected void readAtomElement(ReadContext ctx, StaxReader reader, String nsUri, T feed) throws XMLStreamException {
    }

    protected void readExtensionElement(ReadContext ctx, StaxReader reader, String nsUri, T feed) throws XMLStreamException {
    }
    
    /** entry builder */
    
    
}
