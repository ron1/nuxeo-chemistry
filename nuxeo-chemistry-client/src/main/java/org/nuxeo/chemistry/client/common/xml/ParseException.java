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

import javax.xml.stream.XMLStreamReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ParseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    
    public ParseException(XMLStreamReader reader, String message) {
        this ("Parse Error at "+reader.getLocation().getLineNumber()+":"+reader.getLocation().getColumnNumber()+". "+message);
    }

    public ParseException(XMLStreamReader reader, Throwable cause) {
        this ("Parse Error at "+reader.getLocation().getLineNumber()+":"+reader.getLocation().getColumnNumber(), cause);
    }

    
    public ParseException(XMLStreamReader reader) {
        this (reader, "");
    }

    public ParseException(String message) {
        super (message);
    }

    public ParseException(String message, Throwable cause) {
        super (message, cause);
    }

    public ParseException(Throwable cause) {
        super (cause);
    }

}
