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
package org.nuxeo.chemistry.shell;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.nuxeo.chemistry.client.common.Path;
import org.nuxeo.chemistry.shell.command.CommandRegistry;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface Application {

    void login(String username, char[] password);
    
    URL getServerUrl();
    
    String getUsername();
    
    String getHost();
    
    File getWorkingDirectory();
    
    void setWorkingDirectory(File file);
    
    Context getContext();
    
    void setContext(Context ctx);
    
    Context resolve(Path path);
    
    CommandRegistry getCommandRegistry();
    
    Context getRootContext();
    
    void setData(String key, Object data);
    
    Object getData(String key);

    File getFile(String path);
    
    void connect(String uri) throws IOException;
    
    void connect(URL uri) throws IOException;
    
    void disconnect();
    
    boolean isConnected();
    
    
    //TODO
    public String getHelp(String cmdName);
    
}
