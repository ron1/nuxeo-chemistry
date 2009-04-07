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

import java.io.IOException;

import org.nuxeo.chemistry.client.app.APPContentManager;
import org.nuxeo.chemistry.shell.AbstractApplication;
import org.nuxeo.chemistry.shell.Context;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ChemistryApp extends AbstractApplication {

    protected APPContentManager cm;


    public ChemistryApp() {                
    }
    
    @Override
    protected void doConnect() throws IOException {
        cm = new APPContentManager(serverUrl.toExternalForm());
    }
    
    public void disconnect() {
        cm = null;
    }
    
    public boolean isConnected() {
        return cm != null;
    }
    
    public Context getRootContext() {
        return new ChemistryRootContext(this);
    }
    
    public APPContentManager getContentManager() {
        return cm;
    }

}
