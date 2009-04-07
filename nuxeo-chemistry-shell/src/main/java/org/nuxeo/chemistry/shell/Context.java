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

import java.io.InputStream;

import org.nuxeo.chemistry.client.common.Path;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface Context {

    public String pwd();
    public String[] entries();
    public String[] ls(); //colored entries
    public void post(String name, InputStream in);
    public void put(String name, InputStream in);
    public void delete(String name);
    public boolean exists(String name);
    public Context getContext(String name);
    public Path getPath();
    public Application getApplication();
    public void reset();
    
}
