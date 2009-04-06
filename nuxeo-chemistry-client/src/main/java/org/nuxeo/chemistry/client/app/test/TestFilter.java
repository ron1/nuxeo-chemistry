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
package org.nuxeo.chemistry.client.app.test;

import java.io.StringReader;

import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TestFilter {

    public static void main(String[] args) throws Exception {
        
        XMLStreamReader xr = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader("<a>ssss<c>test1</c>ss<b>testx</b></a>"));
        xr = XMLInputFactory.newInstance().createFilteredReader(xr, new StreamFilter() {
            public boolean accept(XMLStreamReader xr) {
                return xr.getEventType() == XMLStreamConstants.START_ELEMENT;
            }
        });
        
        while (xr.hasNext()) {
            int tok = xr.next();
            System.out.println("toK "+tok+" - "+xr.getLocalName()+" - "+(xr.hasText() ? xr.getText() : ""));
        }
        
    }
}
