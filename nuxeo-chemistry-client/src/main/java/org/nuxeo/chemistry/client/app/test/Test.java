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
package org.nuxeo.chemistry.client.app.test;

import java.util.List;

import org.apache.chemistry.Connection;
import org.apache.chemistry.Document;
import org.apache.chemistry.Folder;
import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.repository.Repository;
import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.app.APPContentManager;
import org.nuxeo.chemistry.client.app.model.APPRepository;
import org.nuxeo.chemistry.client.app.model.APPType;
import org.nuxeo.chemistry.client.common.Navigator;
import org.nuxeo.newswave.cws.client.feeds.APPFeedService;
import org.nuxeo.newswave.cws.client.feeds.FeedService;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class Test {

    public static void main(String[] args) throws Exception {        
        double s = System.currentTimeMillis();
        s = System.currentTimeMillis();


        APPContentManager cm = new APPContentManager("http://localhost:8080/cmis");

        Repository repo = cm.getDefaultRepository();
        Connection conn = repo.getConnection(null);
        
        ObjectEntry root = conn.getRootEntry();
        List<ObjectEntry> entries = conn.getChildren(root);
        
        ObjectEntry last = null;
        for (ObjectEntry entry : entries) {
            System.out.println(">> "+entry.getName()+" ["+entry.getTypeId()+"] - "+entry.getId());
        }
        last = entries.get(0);
        entries = conn.getChildren(last);

        System.out.println("--------------------------------");        
        for (ObjectEntry entry : entries) {
            last = entry;
            System.out.println(">> "+entry.getName()+" ["+entry.getTypeId()+"] - "+entry.getId());
        }
        last = entries.get(0);
        entries = conn.getChildren(last);
        System.out.println("--------------------------------");
        for (ObjectEntry entry : entries) {
            last = entry;
            System.out.println(">> "+entry.getName()+" ["+entry.getTypeId()+"] - "+entry.getId());
        }
        
        System.out.println(">>> traversal done in "+((System.currentTimeMillis()-s)/1000)+" sec.");

        
        Type type = last.getType();
        System.out.println("Section type: "+type+" - "+((APPType)type).isFolder());
        
        s = System.currentTimeMillis();
        Document doc = last.getDocument();
        Folder folder = (Folder)doc;
        System.out.println("author: "+folder.getCreatedBy());
        System.out.println(">>> doc fetched in "+((System.currentTimeMillis()-s)/1000)+" sec.");
        
        Folder f = folder.newFolder("Folder");
        f.setName("some_folder_1");
        f.setValue("dc:title", "My Folder");        
        f.save();
        
        Navigator nav = new Navigator(conn);
        s = System.currentTimeMillis();
        ObjectEntry entry = nav.resolve("/default-domain/workspaces/some_folder");
        System.out.println(">>> doc fetched by path in "+((System.currentTimeMillis()-s)/1000)+" sec.");
        System.out.println("entry title: "+entry.getString("dc:title"));
//        conn.deleteObject(entry);
        
        System.out.println(">>>>> "+repo.getType("Folder"));
        doc = entry.getDocument();
        System.out.println(doc.getId());
        doc.setValue("dc:title", "My Modified Title 4");
        doc.save();
        
        entry = nav.resolve("/default-domain/workspaces/some_folder");        
        System.out.println("entry modif title: "+entry.getString("dc:title"));

        APPContentManager.registerService(FeedService.class, APPFeedService.class);
        FeedService fs = ((APPRepository)repo).getExtension(FeedService.class);
        System.out.println(fs);
        
    }

}
