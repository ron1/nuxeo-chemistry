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
import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.repository.Repository;
import org.nuxeo.chemistry.client.app.APPContentManager;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class Test {

    public static void main(String[] args) throws Exception {        
        double s = System.currentTimeMillis();
//        Abdera.getInstance().getConfiguration().addExtensionFactory(
//                new CmisExtensionFactory());
        s = System.currentTimeMillis();


        APPContentManager cm = new APPContentManager("http://localhost:8080/cmis");

//        APPFeedService.install(cm);
        
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
        
        System.out.println(">>> done in "+((System.currentTimeMillis()-s)/1000)+" sec.");

        //entry =  entry.getChild("default-domain");
      

        
//      Connection conn = repo.getConnection(null);
//        FeedService feedsvc = repo.getExtension(FeedService.class);
//        Feed<FeedDescriptor> feeds = feedsvc.getFeeds();
//        System.out.println("Remote Feeds: ");
//        for (FeedDescriptor fd : feeds.getEntries()) {
//            System.out.println(fd.getTitle()+" - "+fd.getUrl());
//        }
//
//        FeedDescriptor fd = feeds.getEntries().get(0);
//        Feed<ObjectEntry> docs = fd.query();
//
//        int i = 1;
//        System.out.println("### Docs in '"+fd.getTitle()+"'");
//        for (ObjectEntry entry :  docs.getEntries()) {
//            System.out.println(i+". "+entry.getName());
//            i++;
//        }




//        Document doc = entry.getDocument();
//
//        NewsItem ni = entry.getDocument(NewsItem.class);

    }

}
