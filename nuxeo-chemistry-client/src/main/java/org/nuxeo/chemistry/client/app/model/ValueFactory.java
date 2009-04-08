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

import java.math.BigDecimal;
import java.net.URI;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;


/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class ValueFactory {

    public static Value<?> createValue(Object o) {
        Class<?> clazz = o.getClass();
        if (clazz == String.class) {
            return StringValue.fromString((String)o);
        } else if (clazz == Boolean.class) {
            return BooleanValue.fromBoolean((Boolean)o);
        } else if (clazz == URI.class) {
            return URIValue.fromURI((URI)o);        
        } else if (clazz == Calendar.class) {
            return DateValue.fromCalendar((Calendar)o);
        } else if (clazz == Date.class) {
            return DateValue.fromDate((Date)o);
        } else if (clazz == BigDecimal.class) {
          return DecimalValue.fromDecimal((BigDecimal)o);
        } else if (Number.class.isAssignableFrom(clazz)) {
            return IntegerValue.fromNumber((Number)o);
//TODO
//        } else if (clazz == XmlText.class) {
//            //TODO
//        } else if (clazz == HtmlText.class) {
//          //TODO
//        } else if (clazz == Id.class) {
//          //TODO            
        }
        throw new UnsupportedOperationException("Not yet implemented");
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
