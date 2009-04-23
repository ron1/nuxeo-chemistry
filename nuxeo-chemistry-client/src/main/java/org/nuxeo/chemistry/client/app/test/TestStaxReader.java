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
import java.io.StringWriter;

import javax.xml.namespace.QName;

import org.nuxeo.chemistry.client.common.atom.ATOM;
import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;
import org.nuxeo.chemistry.client.common.xml.XMLWriter;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class TestStaxReader {

    public static void main(String[] args) throws Exception {
        StringWriter sw = new StringWriter();
        XMLWriter xw = new XMLWriter(sw, 4);
        xw.start()
            .element("feed").xmlns(ATOM.ATOM_NS).start()
                .element(ATOM.LINK).attr("ref", "self").attr("href", "http://test1")
                .element(ATOM.LINK).attr("ref", "edit").attr("href", "http://test2")
                .element(ATOM.ENTRY).start()
                    .element("title").content("Entry1")
                    .element(ATOM.LINK).attr("ref", "edit").attr("href", "http://edit1")
                .end()
                .element(ATOM.ENTRY).start()
                    .element("title").content("Entry2")
                    .element(ATOM.LINK).attr("ref", "edit").attr("href", "http://edit2")
                .end()
                .element(ATOM.ENTRY).start()
                    .element("title").content("Entry3")
                    .element(ATOM.LINK).attr("ref", "edit").attr("href", "http://edit3")
                .end()
             .end()
        .end();
        
        String content = sw.toString();
        System.out.println(content);
        System.out.println("---------------------");
        testFwdTagLocalNameNoPush(content);
        System.out.println("---------------------");
        testFwdTagLocalName(content);
        System.out.println("---------------------");
        testFwdTagQName(content);
        System.out.println("---------------------");
        testFwdTagBothNames(content);
        System.out.println("---------------------");
        testFwdTagInEntryContext(content);
        System.out.println("---------------------");
        testFwdTagTitleInEntryContext(content);
        System.out.println("---------------------");
        testFwdSiblingEntryTag(content);
        System.out.println("---------------------");
        testFwdSiblingFeedTag(content);
        System.out.println("---------------------");
        testFwdSiblingFeedLinkTag(content);
        
        System.out.println("---------------------");
        testFwdChildren(content);
        System.out.println("---------------------");
        testFwdChildrenByLocalName(content);
        
    }



    public static void testFwdTagLocalNameNoPush(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("feed")) {
            //sr.push();
            while (sr.fwdTag("title")) {
                System.out.println("title tags no push >> "+sr.getName());
            }
            //sr.pop();
        }        
    }

    public static void testFwdTagLocalName(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("feed")) {
            sr.push();
            while (sr.fwdTag("title")) {
                System.out.println("title tags by local name >> "+sr.getName());
            }
            sr.pop();
        }        
    }

    public static void testFwdTagQName(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("feed")) {
            sr.push();
            while (sr.fwdTag(new QName(ATOM.ATOM_NS, "title"))) {
                System.out.println("title tags by qname >> "+sr.getName());
            }
            sr.pop();
        }        
    }
 
    public static void testFwdTagBothNames(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("feed")) {
            sr.push();
            while (sr.fwdTag(ATOM.ATOM_NS, "title")) {
                System.out.println("title tags by both names >> "+sr.getName());
            }
            sr.pop();
        }        
    }

    public static void testFwdTagInEntryContext(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("entry")) {
            sr.push();
            while (sr.fwdTag()) {
                System.out.println("first entry tags >> "+sr.getName());
            }
            sr.pop();
        }        
    }

    public static void testFwdTagTitleInEntryContext(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        while (sr.fwdTag("entry")) {
            sr.push();
            while (sr.fwdTag("title")) {
                System.out.println("entry title tag >> "+sr.getName()+" - "+sr.getElementText());
            }
            sr.pop();
        }        
    }

    public static void testFwdSiblingEntryTag(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("entry")) {
            while (sr.fwdSibling()) {
                System.out.println("sibling tags after first entry >> "+sr.getName());
            }
        }        
    }

    public static void testFwdSiblingFeedTag(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("link")) {
            while (sr.fwdSibling()) {
                System.out.println("sibling tags after first link in feed >> "+sr.getName());
            }
        }        
    }

    public static void testFwdSiblingFeedLinkTag(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("link")) {
            while (sr.fwdSibling("link")) {
                System.out.println("sibling links after first link in feed >> "+sr.getName());
            }
        }        
    }

    public static void testFwdChildren(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("feed")) {
            ChildrenNavigator nav = sr.getChildren();
            while (nav.next()) {
                System.out.println("feed children >> "+sr.getName());
            }
        }        
    }

    public static void testFwdChildrenByLocalName(String content) throws Exception {
        StaxReader sr = StaxReader.newReader(new StringReader(content));                
        if (sr.fwdTag("feed")) {
            ChildrenNavigator nav = sr.getChildren("entry");
            while (nav.next()) {
                System.out.println("feed entries >> "+sr.getName());
            }
        }        
    }

}
