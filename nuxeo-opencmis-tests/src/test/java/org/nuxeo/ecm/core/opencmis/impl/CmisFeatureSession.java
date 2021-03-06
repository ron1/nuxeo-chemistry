/*
 * (C) Copyright 2006-2014 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Florent Guillaume
 */
package org.nuxeo.ecm.core.opencmis.impl;

import org.apache.chemistry.opencmis.client.api.Session;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

import com.google.inject.Binder;

/**
 * Base feature that starts a CMIS client session.
 */
public abstract class CmisFeatureSession extends CmisFeatureConfiguration {

    public static final String USERNAME = "Administrator";

    public static final String PASSWORD = "test";

    protected boolean isHttp;

    protected boolean isAtomPub;

    protected boolean isBrowser;

    @Override
    public void configure(FeaturesRunner runner, Binder binder) {
        binder.bind(CmisFeatureSession.class).toInstance(this);
    }

    public abstract Session setUpCmisSession(String repositoryName);

    public abstract void tearDownCmisSession();

    public void setLocal() {
        isHttp = false;
        isAtomPub = false;
        isBrowser = false;
    }

    public void setAtomPub() {
        isHttp = true;
        isAtomPub = true;
        isBrowser = false;
    }

    public void setBrowser() {
        isHttp = true;
        isAtomPub = false;
        isBrowser = true;
    }

    public void setWebServices() {
        isHttp = true;
        isAtomPub = false;
        isBrowser = false;
    }

}
