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
package org.nuxeo.chemistry.client.common.xml;

import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.stream.XMLStreamException;

/**
 * An iterator over the XML elements in the stream that create element objects each time next method is called. 
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class ElementIterator<T> implements Iterator<T> {

    protected StaxReader reader;
    protected Boolean hasNext;
    
    public ElementIterator(StaxReader sr) {
        this.reader = sr;
    }

    protected abstract T createValue() throws XMLStreamException;

    protected boolean forward() throws XMLStreamException {
        return reader.fwd();
    }
    
    protected boolean accept() {
        return true;
    }
    
    public boolean hasNext() {
        if (hasNext == null) {
            try {
                while (forward()) {
                    if (accept()) {
                        hasNext = Boolean.TRUE;
                        return true;
                    }
                }
            } catch (Exception e) {
                throw new ParseException(e);
            }
            hasNext = Boolean.FALSE;
            return false;
        }        
        
        return hasNext.booleanValue();
    }

    public T next() {
        if (hasNext == null) {
            hasNext();
        }
        if (!hasNext) {
            throw new NoSuchElementException("No more elements in stream");
        }
        hasNext = null;
        try {
            return createValue();
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }
        
    
}
