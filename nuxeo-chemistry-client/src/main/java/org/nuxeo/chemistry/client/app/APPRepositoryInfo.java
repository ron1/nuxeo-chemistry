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

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.repository.RepositoryCapabilities;
import org.apache.chemistry.repository.RepositoryEntry;
import org.apache.chemistry.repository.RepositoryInfo;
import org.w3c.dom.Document;

/**
 * TODO: getURI what for?
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPRepositoryInfo implements RepositoryInfo {

    protected Map<String, Object> map;
    protected RepositoryCapabilities caps;
    
    public APPRepositoryInfo(RepositoryCapabilities caps, Map<String, Object> map) {
        this.map = map;
        this.caps = caps;
    }

    public URI getURI() {
        throw new UnsupportedOperationException("Not yet implemented"); 
    }

    public String getString(String name) {
        return (String)map.get(name);
    }
    
    public String getId() {
        return getString(CMIS.REPOSITORY_ID.getLocalPart());
    }

    public String getName() {
        return getString(CMIS.REPOSITORY_NAME.getLocalPart());
    }

    public String getRelationshipName() {
        return getString(CMIS.REPOSITORY_RELATIONSHIP.getLocalPart());
    }

    public String getDescription() {
        return getString(CMIS.REPOSITORY_DESCRIPTION.getLocalPart());
    }

    public String getProductName() {
        return getString(CMIS.PRODUCT_NAME.getLocalPart());
    }

    public String getProductVersion() {
        return getString(CMIS.PRODUCT_VERSION.getLocalPart());
    }

    public String getRootFolderId() {
        return getString(CMIS.ROOT_FOLDER_ID.getLocalPart());
    }

    public String getVendorName() {
        return getString(CMIS.VENDOR_NAME.getLocalPart());
    }

    public String getVersionSupported() {
        return getString(CMIS.VERSIONS_SUPPORTED.getLocalPart());
    }

    public Document getRepositorySpecificInformation() {        
        return (Document)map.get(CMIS.REPOSITORY_SPECIFIC_INFORMATION.getLocalPart());
    }

    public RepositoryCapabilities getCapabilities() {
        return caps;
    }

    public Collection<RepositoryEntry> getRelatedRepositories() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String toString() {
        return getName()+" - "+getURI();
    }
}
