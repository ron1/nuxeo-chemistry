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
package org.nuxeo.chemistry.shell.app;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.repository.Repository;
import org.nuxeo.chemistry.client.app.APPConnection;
import org.nuxeo.chemistry.client.app.APPContentManager;
import org.nuxeo.chemistry.client.common.Path;
import org.nuxeo.chemistry.shell.AbstractContext;
import org.nuxeo.chemistry.shell.Context;
import org.nuxeo.chemistry.shell.console.ColorHelper;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ChemistryContext extends AbstractContext {

    //public static final String CONN_KEY = "chemistry.connection";
    
    protected APPContentManager cm;
    protected APPConnection conn;
    protected ObjectEntry entry;
    protected String[] keys;
    protected String[] ls;
    protected Map<String,ObjectEntry> children;
    
    public ChemistryContext(ChemistryApp app, Path path, APPConnection conn, ObjectEntry entry) {
        super (app, path);
        this.cm = app.getContentManager();
        this.conn = conn;
        this.entry = entry;
    }

    public APPConnection getConnection() {
        return conn;
    }
    
    public ObjectEntry getEntry() {
        return entry;
    }
    
    public APPContentManager getContentManager() {
        return cm;
    }
    
    public Repository getRepository() {
        return conn.getRepository();
    }
    
    public Context getContext(String name) {
        load();
        ObjectEntry e = children.get(name);
        if (e != null) {
            return new ChemistryContext((ChemistryApp)app, path.append(name), conn, e);
        }
        return null;
    }
    

    public String[] ls() {
        load();
        return ls;
    }

    public String[] entries() {
        load();
        return keys;
    }
    
    public boolean exists(String name) {
        load();
        return children.get(name) != null;
    }

    public void reset() {
        children = null;
        keys = null;
        ls = null;
    }

    protected void load() {
        if (children == null) {
            List<ObjectEntry> feed = conn.getChildren(entry);
            children = new LinkedHashMap<String, ObjectEntry>();
            keys = new String[feed.size()];
            ls = new String[keys.length];
            int i = 0;
            for (ObjectEntry entry : feed) {
                children.put(entry.getName(), entry);
                keys[i] = entry.getName();
                ls[i++] = ColorHelper.decorateNameByType(entry.getName(), entry.getTypeId());
            }
        }
    }
    
    public void delete(String name) {
        throw new UnsupportedOperationException("DELETE not yet implemented"); 
    }

    public void post(String name, InputStream in) {
        throw new UnsupportedOperationException("POST not yet implemented");
    }

    public void put(String name, InputStream in) {
        throw new UnsupportedOperationException("PUT not yet implemented");
    }

}
