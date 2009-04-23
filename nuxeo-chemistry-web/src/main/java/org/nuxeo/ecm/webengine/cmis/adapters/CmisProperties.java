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
package org.nuxeo.ecm.webengine.cmis.adapters;

import javax.xml.namespace.QName;

import org.apache.chemistry.atompub.CMIS;

/**
 * TODO to be merged with {@link CMIS}
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface CmisProperties {

    public final static QName PROP_STRING_DEF = new QName(CMIS.CMIS_NS, "propertyStringDefinition");
    public final static QName PROP_BOOLEAN_DEF = new QName(CMIS.CMIS_NS, "propertyBooleanDefinition");
    public final static QName PROP_INTEGER_DEF = new QName(CMIS.CMIS_NS, "propertyIntegerDefinition");
    public final static QName PROP_DECIMAL_DEF = new QName(CMIS.CMIS_NS, "propertyDecimalDefinition");
    public final static QName PROP_ID_DEF = new QName(CMIS.CMIS_NS, "propertyIdDefinition");    
    public final static QName PROP_DATETIME_DEF = new QName(CMIS.CMIS_NS, "propertyDateTimeDefinition");
    public final static QName PROP_URI_DEF = new QName(CMIS.CMIS_NS, "propertyUriDefinition");
    public final static QName PROP_XML_DEF = new QName(CMIS.CMIS_NS, "propertyXmlDefinition");
    public final static QName PROP_HTML_DEF = new QName(CMIS.CMIS_NS, "propertyHtmlDefinition");
    
    
    public final static QName ID = new QName(CMIS.CMIS_NS, "id");
    public final static QName PROPERTY_TYPE = new QName(CMIS.CMIS_NS, "propertyType");
    
    public final static QName DEFAULT_VALUE = new QName(CMIS.CMIS_NS, "defaultValue");
    public final static QName OPEN_CHOICE = new QName(CMIS.CMIS_NS, "openChoice");
    public final static QName ORDERABLE = new QName(CMIS.CMIS_NS, "orderable");
    public final static QName INHERITED = new QName(CMIS.CMIS_NS, "inherited");
    public final static QName REQUIRED = new QName(CMIS.CMIS_NS, "required");
    public final static QName UPDATEABILITY = new QName(CMIS.CMIS_NS, "updateability");
    public final static QName CARDINALITY = new QName(CMIS.CMIS_NS, "cardinality");
    public final static QName MAX_LENGTH = new QName(CMIS.CMIS_NS, "maxLength");
    
    
}
