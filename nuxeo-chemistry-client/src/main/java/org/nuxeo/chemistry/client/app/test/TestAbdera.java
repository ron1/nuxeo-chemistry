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

import org.nuxeo.chemistry.client.app.APPContentManager;
import org.nuxeo.chemistry.client.app.xml.ATOM;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TestAbdera implements ATOM {

    static APPContentManager cm;
    
    
//    public static void main(String[] args) throws Exception {
//        double s = System.currentTimeMillis();
//        cm = new APPContentManager("http://localhost:8080/cmis");
//        
//        
//        URL url = new URL("http://localhost:8080/cmis/default/children/ac8e70be-567c-404b-9ce0-68ac7e2ffa26");
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        InputStream in = url.openStream();
//        FileUtils.copy(in, baos);
//        in.close();        
//        byte[] bytes1 = baos.toByteArray();
//
//        url = new URL("http://fdsys.blogspot.com/atom.xml");
//        baos = new ByteArrayOutputStream();
//        in = url.openStream();
//        FileUtils.copy(in, baos);
//        in.close();        
//        byte[] bytes2 = baos.toByteArray();
//
//        System.out.println(new String(bytes2));
//
//        //parseWithAbdera(url.openStream());
//
//        parseWithMine(new ByteArrayInputStream(bytes1));
//        parseWithMine(new ByteArrayInputStream(bytes1));
//        parseWithMine(new ByteArrayInputStream(bytes1));
//        parseWithMine(new ByteArrayInputStream(bytes2));
//        parseWithMine(new ByteArrayInputStream(bytes2));
//        parseWithMine(new ByteArrayInputStream(bytes2));
//
//        parseWithAxis(new ByteArrayInputStream(bytes1));
//        parseWithAxis(new ByteArrayInputStream(bytes1));
//        parseWithAxis(new ByteArrayInputStream(bytes1));
//        parseWithAxis(new ByteArrayInputStream(bytes2));
//        parseWithAxis(new ByteArrayInputStream(bytes2));
//        parseWithAxis(new ByteArrayInputStream(bytes2));
//        
//        parseWithAbdera(new ByteArrayInputStream(bytes1));
//        parseWithAbdera(new ByteArrayInputStream(bytes1));
//        parseWithAbdera(new ByteArrayInputStream(bytes1));
//        parseWithAbdera(new ByteArrayInputStream(bytes2));
//        parseWithAbdera(new ByteArrayInputStream(bytes2));
//        parseWithAbdera(new ByteArrayInputStream(bytes2));
//
//        
//    }
//
//
//    static void parseWithAxis(InputStream in) throws Exception {
//        double s = System.currentTimeMillis();
//        XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(in);
//        StAXOMBuilder builder = new StAXOMBuilder(parser);
//        OMElement feed = builder.getDocumentElement();
//        ((OMElementImpl)feed).setParent(null);
//        if (!feed.getQName().equals(ATOM.FEED)) {
//            throw new IllegalArgumentException("not a feed");
//        }
//        Iterator<OMElement> it = feed.getChildrenWithName(ATOM.ENTRY);
//        while (it.hasNext()) {
//            OMElement entry = it.next();
//            Iterator<OMElement> links = entry.getChildrenWithName(ATOM.LINK);
//            while (links.hasNext()) {
//                links.next().getAllAttributes();
//            }
//            OMElement cmis = feed.getFirstChildWithName(CMIS.OBJECT);
//            if (cmis == null) continue;
//            OMElement props = cmis.getFirstChildWithName(CMIS.PROPERTIES);
//            Iterator<OMElement> pit = props.getChildren();
//            while (pit.hasNext()) {
//                OMElement p = pit.next();
//                String localName = p.getLocalName();
//                if (localName.startsWith("property")) {
//                    Iterator<OMElement> vit = p.getChildrenWithName(CMIS.VALUE);
//                    p.getAttribute(CMIS.NAME);
//                    String type = localName.substring(8);
//                    ArrayList<Value> arr = new ArrayList<Value>();
//                    while (vit.hasNext()) {
//                        OMElement v = vit.next();
//                        arr.add(new StringValue(v.getText()));
//                    }
//                }
//            }
//        }
//        System.out.println(">>> "+((System.currentTimeMillis()-s)/1000));        
//    }
//
//    static void parseWithAbdera(InputStream in) throws Exception {
//        InStream fin = new InStream(in);
//        double s = System.currentTimeMillis();
//        Document<org.apache.abdera.model.Feed> doc = Abdera.getInstance().getParser().parse(fin);
//        //fin.close();
////        List<Element> els = doc.getRoot().getElements();
////        for (Element el : els) {
////            el.getAttributes();
////            el.getQName();            
////        }
////        System.out.println(doc.getRoot().getLinks());
//        
//        //doc.getRoot().getAsFeed().get;
//        System.out.println("abdera >>> "+((System.currentTimeMillis()-s)/1000)+" = "+fin.cnt);
//    }
//
//    static void parseWithMine(InputStream in) throws Exception {
//        double s = System.currentTimeMillis();
//        CmisFeedReader fr = new CmisFeedReader();
//        List<APPObjectEntry> feed = fr.read(in);
//        feed.size();
//        System.out.println("mine >>> "+((System.currentTimeMillis()-s)/1000));
//    }
//
//    
//    static class InStream extends FilterInputStream {
//        int cnt = 0;
//        public InStream(InputStream in) {
//            super (in);
//        }
//        @Override
//        public int read(byte[] b) throws IOException {
//            cnt+=b.length;
//            return super.read(b);
//        }
//        @Override
//        public int read(byte[] b, int off, int len) throws IOException {
//            cnt+=len;
//            return super.read(b, off, len);
//        }
//        @Override
//        public int read() throws IOException {
//            cnt++;
//            return super.read();
//        }
//        @Override
//        public void close() throws IOException {
//            System.out.println("-----------------close: "+cnt);
//            super.close();
//        }
//    }

}
