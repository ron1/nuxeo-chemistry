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

import org.apache.chemistry.ObjectEntry;
import org.apache.chemistry.type.Type;
import org.nuxeo.chemistry.client.common.atom.EntryReader;
import org.nuxeo.chemistry.client.common.atom.FeedReader;
import org.nuxeo.chemistry.client.common.atom.ServiceDocumentReader;
import org.nuxeo.chemistry.client.common.atom.XmlObjectWriter;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface IOProvider {

    EntryReader<? extends ObjectEntry> getObjectEntryReader();
    
    EntryReader<? extends Type> getTypeEntryReader();
    
    ServiceDocumentReader<?> getServiceDocumentReader();
    
    FeedReader<List<ObjectEntry>> getObjectFeedReader();
    
    FeedReader<Map<String, Type>> getTypeFeedReader();

    XmlObjectWriter<ObjectEntry> getObjectEntryWriter();
    
    XmlObjectWriter<String> getQueryWriter();

}
