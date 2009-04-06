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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class StaxReader extends StreamReaderDelegate {
    
    protected final static XMLInputFactory factory = XMLInputFactory.newInstance();
    static {
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
        factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, true);
        factory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_COALESCING, false);
        // network detached mode
        // copied form apache axiom
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, 
                Boolean.FALSE);
        // Some StAX parser such as Woodstox still try to load the external DTD subset,
        // even if IS_SUPPORTING_EXTERNAL_ENTITIES is set to false. To work around this,
        // we add a custom XMLResolver that returns empty documents. See WSTX-117 for
        // an interesting discussion about this.
        factory.setXMLResolver(new XMLResolver() {
            public Object resolveEntity(String publicID, String systemID, String baseURI,
                    String namespace) throws XMLStreamException {
                return new ByteArrayInputStream(new byte[0]);
            }
        });
    }


    public static StaxReader newReader(InputStream in) throws XMLStreamException {
        return new StaxReader(factory.createXMLStreamReader(in));
    }
    
    public static StaxReader newReader(Reader reader) throws XMLStreamException {
        return new StaxReader(factory.createXMLStreamReader(reader)); 
    }


    protected class Context {
        protected Context parent;
        protected int depth;
        protected String localName;
        protected String nsUri;
        protected boolean done;
        @Override
        public String toString() {
            return "{"+nsUri+"}"+localName+": "+depth+" > done: "+done;
        }
    }
    
    protected Context ctx;
    protected int depth = 0;
    protected String defNsUri;
    


    public StaxReader(XMLStreamReader reader) {
        super (reader);
    }
    
    public int getDepth() {
        return depth;
    }
    
    public Context getContext() {
        return ctx;
    }
    
    @Override
    public String getElementText() throws XMLStreamException {
        depth--;
        return super.getElementText();
    }
    
    public boolean fwd() throws XMLStreamException {
        if (!hasNext()) {
            return false;
        }        
        if (ctx != null && ctx.done) {
            return false;
        }
        int tok = next();
        if (tok == START_ELEMENT) {
            depth++;
        } else if (tok == END_ELEMENT) {
            depth--;            
        }
        if (ctx == null) {
            return true;
        }
        if (depth == ctx.depth && tok == END_ELEMENT) {
            if (ctx.localName.equals(getLocalName()) && ctx.nsUri.equals(getNamespaceURI())) {
                // context ended
                ctx.done = true;
            }
        }
        return true;
    }

    public boolean fwdTag() throws XMLStreamException {
        // we need to test first hasNext to be sure we fwd in the stream
        // this way we are sure we didn't end in the same element (without forwarding the stream)
        while(hasNext() && fwd()) { 
            if (getEventType() == START_ELEMENT) {
                return true;
            }
        }
        return false;
    }

    public boolean fwdTag(String localName) throws XMLStreamException {
        // we need to test first hasNext to be sure we fwd in the stream
        // this way we are sure we didn't end in the same element (without forwarding the stream)
        while(hasNext() && fwd()) { 
            if (getEventType() == START_ELEMENT && localName.equals(getLocalName())) {
                return true;
            }
        }
        return false;
    }

    public boolean fwdTag(String nsUri, String localName) throws XMLStreamException {
        // we need to test first hasNext to be sure we fwd in the stream
        // this way we are sure we didn't end in the same element (without forwarding the stream)
        while(hasNext() && fwd()) { 
            if (getEventType() == START_ELEMENT && localName.equals(getLocalName()) && nsUri.equals(getNamespaceURI())) {
                return true;
            }
        }
        return false;
    }

    public boolean fwdTag(QName name) throws XMLStreamException {
        // we need to test first hasNext to be sure we fwd in the stream
        // this way we are sure we didn't end in the same element (without forwarding the stream)
        while(hasNext() && fwd()) { 
            if (getEventType() == START_ELEMENT && name.equals(getName())) {
                return true;
            }
        }
        return false;
    }
    
    
    public boolean fwdSibling() throws XMLStreamException {
        int tok = getEventType();
        if (tok == END_ELEMENT) {
            return fwdSibling(depth+1);
        } else if (tok == START_ELEMENT) {
            return fwdSibling(depth);   
        } else { 
            throw new XMLStreamException("Ilegal state: current event must be START_ELEMENT or END_ELEMENT");
        }
    }

    protected boolean fwdSibling(int cdepth) throws XMLStreamException {
        // we need to test first hasNext to be sure we fwd in the stream
        // this way we are sure we didn't end in the same element (without forwarding the stream)
        while(hasNext() && fwd()) {
            if (depth < cdepth-1) {
                return false;
            } else if (depth > cdepth) {
                continue;
            } else if (getEventType() == START_ELEMENT) { //on same level
                if (depth == cdepth) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean fwdSibling(String localName) throws XMLStreamException {
        while (fwdSibling()) {
            if (localName.equals(getLocalName())) {
                return true;
            }
        }
        return false;
    }

    public boolean fwdSibling(QName name) throws XMLStreamException {
        while (fwdSibling()) {
            if (name.equals(getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean fwdSibling(String nsUri, String localName) throws XMLStreamException {
        while (fwdSibling()) {
            if (localName.equals(getLocalName()) && nsUri.equals(getNamespaceURI())) {
                return true;
            }
        }
        return false;
    }

    public ChildrenNavigator getChildren() throws XMLStreamException {
        return new ChildrenNavigator(this, depth+1); 
    }

    public ChildrenNavigator getChildren(final String localName) throws XMLStreamException {
        return new FilteredChildrenNavigator(this, depth+1) {
            @Override
            protected boolean accept() {
                return localName.equals(getLocalName());
            }
        };
    }

    public ChildrenNavigator getChildren(final String nsUri, final String localName) throws XMLStreamException {
        return new FilteredChildrenNavigator(this, depth+1) {
            @Override
            protected boolean accept() {
                return localName.equals(getLocalName()) && nsUri.equals(getNamespaceURI());
            }
        };
    }

    public ChildrenNavigator getChildren(final QName name) throws XMLStreamException {
        return new FilteredChildrenNavigator(this, depth+1) {
            @Override
            protected boolean accept() {
                return name.equals(getName());
            }
        };
    }

    
    public void push() throws XMLStreamException {
        if (getEventType() != START_ELEMENT) {
            throw new IllegalStateException("push must be called in a START_ELEMENT context");
        }
        if (ctx == null) {
            ctx = new Context();
        } else {
            Context newCtx = new Context();
            newCtx.parent = ctx;
            ctx = newCtx;
        }
        ctx.depth = depth-1;
        ctx.localName = getLocalName();
        ctx.nsUri = getNamespaceURI();
    }

    public void pop() throws XMLStreamException {
        if (ctx == null) {
            throw new IllegalStateException("Invalid call of pop. No current context exists");
        }
        if (!ctx.done) { // fwd the stream until the context end
            while (fwd());
        }
        // pop the context
        ctx = ctx.parent;
    }

    public String getAttributeValue(String localName) {
        int cnt = getAttributeCount();
        for (int i=0; i<cnt; i++) {
            if (localName.equals(getAttributeName(i).getLocalPart())) {
                return getAttributeValue(i);
            }
        }
        return null;
    }

    public String getDefaultNamespaceURI() {
        if (defNsUri == null) {
            defNsUri = getNamespaceURI("");
            if (defNsUri == null) {
                defNsUri = "";
            }
        }
        return defNsUri;
    }
    
    public String getAttributeValue(String namespaceURI, String localName) {
        if (namespaceURI == null) {
            namespaceURI = "";
        }
        String val = super.getAttributeValue(namespaceURI, localName);
        if (val == null && (namespaceURI.length() == 0 || namespaceURI.equals(getDefaultNamespaceURI()))) {
            val = getAttributeValue("", localName);
        }
        return val;
    }

    public String getAttributeValue(QName name) {
        return getAttributeValue(name.getNamespaceURI(), name.getLocalPart());
    }
    
       
}
