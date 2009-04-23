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

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.chemistry.client.common.atom.AbstractFeedBuilder;
import org.nuxeo.chemistry.client.common.atom.EntryBuilder;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPFeedBuilder extends AbstractFeedBuilder<List<APPObjectEntry>, APPObjectEntry> {

    private static APPFeedBuilder builder = new APPFeedBuilder(); 
    
    public static APPFeedBuilder getBuilder() {
        return builder;
    }
    
    public APPFeedBuilder(EntryBuilder<APPObjectEntry> entryBuilder) {
        super (entryBuilder);
        
    }
    
    public APPFeedBuilder() {     
        this (APPObjectEntryBuilder.getBuilder());
    }
    
    @Override
    protected List<APPObjectEntry> createFeed(StaxReader reader) {
        return new ArrayList<APPObjectEntry>();
    }

    @Override
    protected void addEntry(List<APPObjectEntry> feed, APPObjectEntry entry) {
        feed.add(entry);
    }

  
}
