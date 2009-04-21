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

import org.nuxeo.chemistry.client.Credentials;
import org.nuxeo.chemistry.client.CredentialsProvider;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class DefaultCredentialsProvider implements CredentialsProvider {

    protected Credentials credentials;
    
    public DefaultCredentialsProvider(Credentials credentials) {
        this.credentials = credentials;
    }
    
    public DefaultCredentialsProvider(String username, char[] password) {
        this(new Credentials(username, password));
    }

    public DefaultCredentialsProvider(String username, String password) {
        this (username, password.toCharArray());
    }

    public Credentials getCredentials() {
        return credentials;
    }

}
