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

import org.apache.chemistry.repository.JoinCapability;
import org.apache.chemistry.repository.QueryCapability;
import org.apache.chemistry.repository.RepositoryCapabilities;

/**
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public class APPRepositoryCapabilities implements RepositoryCapabilities {

    protected JoinCapability joinCapability;    
    protected QueryCapability queryCapability;
    protected boolean hasUnfiling;
    protected boolean hasMultifiling;
    protected boolean hasVersionSpecificFiling;
    protected boolean isAllVersionsSearchable;
    protected boolean isPWCSearchable;
    protected boolean isPWCUpdatable;
    
    
    public JoinCapability getJoinCapability() {
        return joinCapability;
    }

    public QueryCapability getQueryCapability() {
        return queryCapability;
    }

    public boolean hasMultifiling() {
        return hasMultifiling;
    }

    public boolean hasUnfiling() {
        return hasUnfiling;
    }

    public boolean hasVersionSpecificFiling() {
        return hasVersionSpecificFiling;
    }

    public boolean isAllVersionsSearchable() {
        return isAllVersionsSearchable;
    }

    public boolean isPWCSearchable() {
        return isPWCSearchable;
    }

    public boolean isPWCUpdatable() {
        return isPWCUpdatable;
    }

    
    public void setAllVersionsSearchable(boolean isAllVersionsSearchable) {
        this.isAllVersionsSearchable = isAllVersionsSearchable;
    }
    
    public void setHasMultifiling(boolean hasMultifiling) {
        this.hasMultifiling = hasMultifiling;
    }
    
    public void setHasUnfiling(boolean hasUnfiling) {
        this.hasUnfiling = hasUnfiling;
    }
    
    public void setHasVersionSpecificFiling(boolean hasVersionSpecificFiling) {
        this.hasVersionSpecificFiling = hasVersionSpecificFiling;
    }
    
    public void setJoinCapability(JoinCapability joinCapability) {
        this.joinCapability = joinCapability;
    }
    
    public void setPWCSearchable(boolean isPWCSearchable) {
        this.isPWCSearchable = isPWCSearchable;
    }
    
    public void setPWCUpdatable(boolean isPWCUpdatable) {
        this.isPWCUpdatable = isPWCUpdatable;
    }
    
    public void setQueryCapability(QueryCapability queryCapability) {
        this.queryCapability = queryCapability;
    }
}
