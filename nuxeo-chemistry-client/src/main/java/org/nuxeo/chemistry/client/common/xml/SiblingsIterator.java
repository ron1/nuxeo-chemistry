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

import javax.xml.stream.XMLStreamException;

/**
 * An element iterator that iterates only over the sibling elements. 
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class SiblingsIterator<T> extends ElementIterator<T> {

    protected int depth;
    
    public SiblingsIterator(StaxReader sr) throws XMLStreamException {
        this (sr, sr.getElementDepth());
    }
    
    public SiblingsIterator(StaxReader sr, int depth) {
        super (sr);
        this.depth = depth;
    }

    protected boolean forward() throws XMLStreamException {
        return reader.fwdSibling(depth);
    }
            
}
