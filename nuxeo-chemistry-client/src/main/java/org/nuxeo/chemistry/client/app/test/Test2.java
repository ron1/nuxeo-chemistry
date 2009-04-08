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

import java.net.URL;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.nuxeo.chemistry.client.app.APPObjectEntry;
import org.nuxeo.chemistry.client.app.xml.CmisFeedReader;
import org.nuxeo.chemistry.client.common.xml.StaxReader;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class Test2 {


    public static void main(String[] args) throws Exception {


        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(
                new URL("http://localhost:8080/cmis/default/children/ac8e70be-567c-404b-9ce0-68ac7e2ffa26").openStream()
        );

        
        List<APPObjectEntry> feed = CmisFeedReader.INSTANCE.read(null, new StaxReader(reader));
        for (APPObjectEntry entry : feed) {
            System.out.println(">>>> "+entry.getId()+", "+entry.getName()+", "+entry.getTypeId());    
        }
        
        

//        while (reader.hasNext()) {
//            int tok = reader.next();
//            if (tok == XMLStreamConstants.START_ELEMENT) {
//                System.out.println("=============== "+reader.getLocalName()+" - "+reader.getAttributeValue("", "href"));    
//            } else 
//                if (tok == XMLStreamConstants.ATTRIBUTE) {
//                System.out.println("@@@ "+reader.getLocalName()+" = " +reader.getText());
//            }
//        }
    }

}
