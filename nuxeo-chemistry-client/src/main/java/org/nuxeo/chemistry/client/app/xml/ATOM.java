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

import javax.xml.namespace.QName;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface ATOM {

    public final static String APP_NS = "http://www.w3.org/2007/app";
    public final static String ATOM_NS = "http://www.w3.org/2005/Atom";
    public final static QName FEED = new QName(ATOM_NS, "feed");
    public final static QName ENTRY = new QName(ATOM_NS, "entry");
    public final static QName LINK = new QName(ATOM_NS, "link");
    public final static QName COLLECTION = new QName(APP_NS, "collection");
}
