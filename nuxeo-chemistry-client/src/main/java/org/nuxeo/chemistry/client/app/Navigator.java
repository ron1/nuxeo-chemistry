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
package org.nuxeo.chemistry.client.app;

import java.util.List;

import org.apache.chemistry.Connection;
import org.apache.chemistry.ObjectEntry;
import org.nuxeo.chemistry.client.common.Path;

/**
 * Temporary class to ease navigation until this will be added to the API 
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class Navigator {
    
    protected Connection conn;
    
    public Navigator(Connection conn) {
        this.conn = conn;
    }

    public ObjectEntry getChild(ObjectEntry entry, String name) {
        List<ObjectEntry> children = conn.getChildren(entry);
        for (ObjectEntry child : children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    public ObjectEntry resolve(String path) {
        return resolve(new Path(path));
    }

    public ObjectEntry resolve(Path path) {
        ObjectEntry entry = conn.getRootEntry();
        for (int i=0,len=path.segmentCount(); i<len; i++) {
            entry = getChild(entry, path.segment(i));
            if (entry == null) {
                return null;
            }
        }
        return entry;
    }

    public Path getPath(ObjectEntry entry) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
