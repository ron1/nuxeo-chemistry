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
package org.nuxeo.chemistry.shell.context;

import java.io.IOException;
import java.io.InputStream;

import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.repository.Repository;
import org.nuxeo.chemistry.client.ContentManager;
import org.nuxeo.chemistry.client.app.APPConnection;
import org.nuxeo.chemistry.client.app.APPContentManager;
import org.nuxeo.chemistry.client.common.Path;
import org.nuxeo.chemistry.shell.Console;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ChemistryRootContext extends AbstractContext {

    protected String[] keys;

    public ChemistryRootContext(ChemistryApp app) {
        super (app, Path.ROOT);
    }
    
    public APPContentManager getContentManager() {
        return ((ChemistryApp)app).getContentManager();
    }
    
    public Context getContext(String name) {
        load();
        ContentManager cm = getContentManager();
        if (cm == null) {
            try {
                Console.getDefault().error("Not connected: cannot browse repository");
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Repository repo = cm.getRepository(name);
        if (repo != null) {            
            APPConnection conn = (APPConnection)repo.getConnection(null);
            ObjectEntry entry = conn.getRootEntry();
            return new ChemistryContext((ChemistryApp)app, path.append(name), conn, entry);
        }
        return null;
    }
    

    public boolean exists(String name) {
        if (load()) {
            return getContentManager().getRepository(name) != null;
        }
        return false;
    }

    public String[] ls() {
        if (load()) {
            return keys;
        }
        return new String[0]; 
    }

    protected boolean load() {
        if (keys == null) {
            ContentManager cm = getContentManager();
            if (cm == null) {
                try {
                    Console.getDefault().error("Not connected: cannot browse repository");
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Repository[] repos = cm.getRepositories();
            keys = new String[repos.length];
            for (int i=0; i<repos.length; i++) {
                keys[i] = repos[i].getName();
            }
        }
        return true;
    }
    
    public void reset() {
        keys = null;
        APPContentManager cm = getContentManager();
        if (cm != null) {
            cm.refresh();
        }
    }
    
    public void delete(String name) {
        throw new UnsupportedOperationException("Delete not supported on root");
    }

    public void post(String name, InputStream in) {
        throw new UnsupportedOperationException("Delete not supported on root");
    }

    public void put(String name, InputStream in) {
        throw new UnsupportedOperationException("Put not supported on root");
    }

}
