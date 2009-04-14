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
package org.nuxeo.ecm.core.chemistry;

import java.util.HashSet;
import java.util.Set;

import org.apache.chemistry.property.Property;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class BuiltinProperties {

    private static Set<String> builtins = new HashSet<String>();
    
    static {
        builtins.add(Property.NAME);
        builtins.add(Property.ALLOWED_CHILD_OBJECT_TYPE_IDS);
        builtins.add(Property.CHANGE_TOKEN);
        builtins.add(Property.CHECKIN_COMMENT);
        builtins.add(Property.CONTENT_STREAM_ALLOWED);
        builtins.add(Property.CONTENT_STREAM_FILENAME);
        builtins.add(Property.CONTENT_STREAM_LENGTH);
        builtins.add(Property.CONTENT_STREAM_MIME_TYPE);
        builtins.add(Property.CONTENT_STREAM_URI);
        builtins.add(Property.CREATED_BY);
        builtins.add(Property.CREATION_DATE);
        builtins.add(Property.ID);
        builtins.add(Property.IS_IMMUTABLE);
        builtins.add(Property.IS_LATEST_MAJOR_VERSION);
        builtins.add(Property.IS_LATEST_VERSION);
        builtins.add(Property.IS_MAJOR_VERSION);
        builtins.add(Property.IS_VERSION_SERIES_CHECKED_OUT);
        builtins.add(Property.LAST_MODIFICATION_DATE);
        builtins.add(Property.LAST_MODIFIED_BY);
        builtins.add(Property.PARENT_ID);
        builtins.add(Property.POLICY_NAME);
        builtins.add(Property.POLICY_TEXT);
        builtins.add(Property.SOURCE_ID);
        builtins.add(Property.TARGET_ID);
        builtins.add(Property.TYPE_ID);
        builtins.add(Property.URI);
        builtins.add(Property.VERSION_LABEL);
        builtins.add(Property.VERSION_SERIES_CHECKED_OUT_BY);
        builtins.add(Property.VERSION_SERIES_CHECKED_OUT_ID);
        builtins.add(Property.VERSION_SERIES_ID);
    }
    
    public static boolean isBuiltin(String key) {
        return builtins.contains(key);
    }
    
}
