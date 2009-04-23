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
import java.util.Map;
import java.util.Set;

import org.apache.chemistry.Document;
import org.apache.chemistry.Folder;
import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.type.BaseType;
import org.nuxeo.chemistry.client.common.atom.XmlProperty;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPFolder extends APPDocument implements Folder {

    
    public APPFolder(APPConnection connection, String typeId) {
        super (connection, typeId);
    }

    public APPFolder(APPConnection connection, String typeId, String parentId) {
        super (connection, typeId, parentId);
    }


    public List<ObjectEntry> getChildren(BaseType type, String orderBy) {        
        return connection.getChildren(this); //TODO type and orderBy
    }
    
    public APPFolder(APPConnection connection, Map<String,XmlProperty> properties, Set<String> allowableActions) {
        super (connection, properties, allowableActions);
    }
    
    public APPFolder(APPConnection connection, APPObjectEntry entry) {
        super (connection, entry.properties, entry.allowableActions);
        links = entry.links;
    }
    

    @Override
    public Folder getFolder() {
        return this;
    }
    
    public Document newDocument(String typeId) {
        APPDocument doc = new APPDocument(connection, typeId, getId());
        doc.addLink(CMIS.LINK_PARENT, getEditLink());
        return doc;
    }

    public Folder newFolder(String typeId) {
        APPFolder doc = new APPFolder(connection, typeId, getId());
        doc.addLink(CMIS.LINK_PARENT, getEditLink());
        return doc;
    }

}
