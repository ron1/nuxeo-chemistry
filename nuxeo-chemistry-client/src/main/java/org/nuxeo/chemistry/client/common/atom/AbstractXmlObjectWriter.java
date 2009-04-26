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
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.nuxeo.chemistry.client.common.xml.XMLWriter;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class AbstractXmlObjectWriter<T> implements XmlObjectWriter<T> {

    public abstract String getContentType();
    
    public void write(T object, File file) throws IOException {
        FileWriter w = new FileWriter(file);
        try {
            write(object, w);
        } finally {
            w.close();
        }
    }

    public void write(T object, OutputStream out) throws IOException {
        write(object, new XMLWriter(new OutputStreamWriter(out)));
    }
    
    public void write(T object, Writer writer) throws IOException {
        write(object, new XMLWriter(writer));
    }
    
    public abstract void write(T object, XMLWriter writer) throws IOException;
    
}
