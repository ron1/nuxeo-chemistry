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
package org.nuxeo.chemistry.client.app.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.ObjectEntry;
import org.nuxeo.chemistry.client.common.atom.AbstractFeedReader;
import org.nuxeo.chemistry.client.common.atom.EntryReader;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPObjectFeedReader extends AbstractFeedReader<List<ObjectEntry>, APPObjectEntry> {

    private static APPObjectFeedReader builder = new APPObjectFeedReader(); 
    
    public static APPObjectFeedReader getBuilder() {
        return builder;
    }
    
    public APPObjectFeedReader(EntryReader<APPObjectEntry> entryBuilder) {
        super (entryBuilder);
        
    }
    
    public APPObjectFeedReader() {     
        this (APPObjectEntryReader.getBuilder());
    }
    
    @Override
    protected List<ObjectEntry> createFeed(StaxReader reader) {
        return new ArrayList<ObjectEntry>();
    }

    @Override
    protected void addEntry(List<ObjectEntry> feed, APPObjectEntry entry) {
        feed.add(entry);
    }

  
}
