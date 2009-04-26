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
package org.nuxeo.newswave.cws.client.feeds;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class DefaultFeed<T> extends ArrayList<T> implements Feed<T> {

    private static final long serialVersionUID = 1L;

    protected String id;
    protected String url;
    protected String title;
    protected String author;
    protected long lastModified;

    public DefaultFeed() {
    }

    public DefaultFeed(int size) {
        super(size);
    }

    public DefaultFeed(List<T> entries) {
        super (entries);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return url;
    }

    public String getAuthor() {
        return author;
    }


    public long lastModified() {
        return lastModified;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

}
