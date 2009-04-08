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
import java.util.UUID;

import org.apache.chemistry.Document;
import org.apache.chemistry.Folder;
import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.property.Property;
import org.apache.chemistry.type.BaseType;
import org.nuxeo.chemistry.client.app.model.DataMap;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPFolder extends APPDocument implements Folder {

    
    public APPFolder(APPConnection connection) {
        this (connection, null);
    }

    public APPFolder(APPConnection connection, DataMap dataMap) {
        super (connection, dataMap);
    }

    public List<ObjectEntry> getChildren(BaseType type, String orderBy) {        
        return connection.getChildren(this); //TODO type and orderBy
    }

    @Override
    public Folder getFolder() {
        return this;
    }
    
    public Document newDocument(String typeId) {
        APPDocument doc = new APPDocument(connection);
        DataMap map = new DataMap();
        map.set(Property.ID, "TransientDoc#"+UUID.randomUUID().toString());
        map.set(Property.TYPE_ID, typeId);
        doc.init(new DataMap());
        return doc;
    }

    public Folder newFolder(String typeId) {
        APPFolder doc = new APPFolder(connection);
        DataMap map = new DataMap();
        map.set(Property.ID, "TransientDoc#"+UUID.randomUUID().toString());
        map.set(Property.TYPE_ID, typeId);
        doc.init(new DataMap());
        return doc;
    }

}
