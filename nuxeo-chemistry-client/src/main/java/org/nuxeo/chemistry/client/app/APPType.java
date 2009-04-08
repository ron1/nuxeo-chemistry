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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.property.PropertyDefinition;
import org.apache.chemistry.type.BaseType;
import org.apache.chemistry.type.ContentStreamPresence;
import org.apache.chemistry.type.Type;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPType extends APPObject implements Type {

    protected Map<String,String> map;
    
    protected APPRepository repository;
    protected BaseType baseType;
    protected String typeId;
    protected String parentId;
    protected Map<String, PropertyDefinition> propertyDefs;
    
    
    public APPType(APPRepository repository) {
        this (repository, null);
    }
    
    public APPType(APPRepository repository, Map<String,String> props) {
        this.repository = repository;
        this.map = props;
    }

    public void init(Map<String,String> properties) {
        if (this.map != null) {
            throw new IllegalStateException("Type is already intialized");
        }
        this.map = properties; 
    }
    
    @Override
    public Connector getConnector() {
        return repository.cm.connector;
    }
    
    public APPRepository getRepository() {
        return repository;
    }
    
    public boolean isFolder() {
        return getBaseType() == BaseType.FOLDER;
    }

    public BaseType getBaseType() {
        if (baseType == null) {
            String val = map.get(CMIS.BASE_TYPE.getLocalPart());
            if ("document".equals(val)) {
                baseType = BaseType.DOCUMENT;
            } else if ("folder".equals(val)) {
                baseType = BaseType.FOLDER;
            } else if ("relationship".equals(val)) {
                baseType = BaseType.RELATIONSHIP;
            } else if ("policy".equals(val)) {
                baseType = BaseType.POLICY;
            } else {
                throw new IllegalArgumentException("Invalid value for baseType property: |"+val+"|");
            }
        }
        return baseType;
    }

    public String getBaseTypeQueryName() {
        return map.get(CMIS.BASE_TYPE_QUERY_NAME.getLocalPart());
    }


    public String getDescription() {
        return map.get(CMIS.DESCRIPTION.getLocalPart());
    }

    public String getDisplayName() {
        return map.get(CMIS.DISPLAY_NAME.getLocalPart());
    }

    public String getId() {
        if (typeId == null) {
            typeId = map.get(CMIS.TYPE_ID.getLocalPart());
        }
        return typeId;
    }

    public String getParentId() {
        if (parentId == null) {
            parentId = map.get(CMIS.TYPE_ID.getLocalPart());
        }
        return parentId;
    }

    public PropertyDefinition getPropertyDefinition(String name) {
        loadPropertyDef();
        return propertyDefs.get(name);
    }

    public Collection<PropertyDefinition> getPropertyDefinitions() {
        loadPropertyDef();
        return Collections.unmodifiableCollection(propertyDefs.values());
    }

    public String getQueryName() {
        return map.get(CMIS.QUERY_NAME);
    }

    public boolean isControllable() {
        String v = map.get(CMIS.CONTROLLABLE) ; 
        return v != null && "true".equals(v);
    }

    public boolean isCreatable() {
        String v = map.get(CMIS.CREATABLE) ; 
        return v != null && "true".equals(v);
    }

    public boolean isFileable() {
        String v = map.get(CMIS.FILEABLE) ; 
        return v != null && "true".equals(v);
    }

    public boolean isQueryable() {
        String v = map.get(CMIS.QUERYABLE) ; 
        return v != null && "true".equals(v);
    }

    public boolean isVersionable() {
        String v = map.get(CMIS.VERSIONABLE) ; 
        return v != null && "true".equals(v);
    }
    
    public boolean isIncludedInSuperTypeQuery() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String[] getAllowedSourceTypes() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String[] getAllowedTargetTypes() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public ContentStreamPresence getContentStreamAllowed() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected void loadPropertyDef() {
        if (propertyDefs == null) {
            if (true) throw new UnsupportedOperationException("Not yet implemented");
            APPType typeDef = getLinkedEntity("edit", APPType.class);
            propertyDefs = typeDef.propertyDefs;
            //TODO
        }
    }
    
    @Override
    public String toString() {
        return getId();
    }
}
