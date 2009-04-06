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

import java.util.NoSuchElementException;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ValueFactory {

    public static Value<?> createValue(Object o) {
        
        return null;
    }

    public static Value<?> createValue(String id, String value) {
        if (id.equals("String")) {
            return new StringValue(value);
        } else if (id.equals("Id")) {
            return new StringValue(value);
        } else if (id.equals("Boolean")) {
            return new BooleanValue(value);
        } else if (id.equals("DateTime")) {
            return new DateValue(value);
        } else if (id.equals("Integer")) {
            return new IntegerValue(value);
        } else if (id.equals("Decimal")) {
            return new DecimalValue(value);
        } else if (id.equals("Html")) {
            return new StringValue(value);
        } else if (id.equals("Xml")) {
            return new StringValue(value);
        }
        throw new NoSuchElementException("No such value type: "+id);
    }

}
