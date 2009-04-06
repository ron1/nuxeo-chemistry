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

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TestAxiom {

    public static void main(String[] args) throws Exception {
        
        
        //create the parser
        InputStream in = new URL("http://localhost:8080/cmis/default/children/ac8e70be-567c-404b-9ce0-68ac7e2ffa26").openStream();
        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(in);
        StAXOMBuilder builder = new StAXOMBuilder(parser);
        //get the root element
        OMElement documentElement = builder.getDocumentElement();
        //XMLStreamReader xr = documentElement.getXMLStreamReaderWithoutCaching();
//        in.close();
        //documentElement.getChildrenWithName(null);
        //dump the out put to console with caching
        System.out.println(documentElement.toString());
        System.out.println(documentElement.toStringWithConsume());

        
    }
    
}
