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
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface EntryReader<T> extends XMLStreamConstants, ATOM {

    public T read(Object context, URL url) throws XMLStreamException, IOException;
    
    public T read(Object context, File file) throws XMLStreamException, IOException;
    
    public T read(Object context, Reader reader) throws XMLStreamException;
    
    public T read(Object context, InputStream in) throws XMLStreamException;
    
    public T read(Object context, StaxReader reader) throws XMLStreamException;
    
    public T readEntry(Object context, StaxReader reader) throws XMLStreamException;
    
}
