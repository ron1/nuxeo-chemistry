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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface EntryReader<T> {

    T read(ReadContext ctx, URL url) throws XMLStreamException, IOException;
    
    T read(ReadContext ctx, File file) throws XMLStreamException, IOException;

    T read(ReadContext ctx, InputStream in) throws XMLStreamException;
    
    T read(ReadContext ctx, XMLStreamReader xr) throws XMLStreamException;
    
    T read(ReadContext ctx, StaxReader sr) throws XMLStreamException;        
    
}
