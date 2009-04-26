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
package org.nuxeo.chemistry.client.common.atom;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.abdera.model.Element;
import org.apache.abdera.model.Workspace;
import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.repository.JoinCapability;
import org.apache.chemistry.repository.QueryCapability;
import org.apache.chemistry.repository.Repository;
import org.apache.chemistry.repository.RepositoryInfo;
import org.nuxeo.chemistry.client.app.model.APPRepository;
import org.nuxeo.chemistry.client.app.model.APPRepositoryCapabilities;
import org.nuxeo.chemistry.client.app.model.APPRepositoryInfo;
import org.nuxeo.chemistry.client.common.xml.ChildrenNavigator;
import org.nuxeo.chemistry.client.common.xml.StaxReader;

/**
 * 
 * Base class for service document reader implementations.
 * 
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public abstract class ServiceDocumentReader<T extends Repository> {

    protected abstract T createRepository(ReadContext ctx);
    
    protected abstract void addCollection(T repo, String href, String type);
    
    protected abstract void setInfo(T repo, RepositoryInfo info);
    
    public T[] read(ReadContext context, InputStream in) throws IOException {
        try {
            StaxReader reader = StaxReader.newReader(in);
            if (!reader.fwdTag("service")) {
                throw new IOException("Invalid APP service document");
            }
            ArrayList<Repository> repos = new ArrayList<Repository>();
            ChildrenNavigator workspaces = reader.getChildren("workspace");            
            while (workspaces.next()) {
                T repo = createRepository(context);
                ChildrenNavigator children = reader.getChildren();
                while (children.next()) {
                    QName name = reader.getName();
                    if (name.equals(ATOM.COLLECTION)) {
                        String href = reader.getAttributeValue("href");
                        String type = reader.getAttributeValue("collectionType");
                        addCollection(repo, href, type);
                    } else if (name.equals(CMIS.REPOSITORY_INFO)) {
                        RepositoryInfo info = readRepositoryInfo(context, reader);
                        setInfo(repo, info);
                    }
                }
                repos.add(repo);
            }
            return (T[])repos.toArray(new APPRepository[repos.size()]); 
        } catch (XMLStreamException e) {
            IOException ioe = new IOException();
            ioe.initCause(e);
            throw ioe;
        }
    }
    

    protected RepositoryInfo readRepositoryInfo(ReadContext context, StaxReader reader) throws XMLStreamException {
        APPRepositoryCapabilities caps = null;
        Map<String, Object> map = new HashMap<String, Object>();
        ChildrenNavigator nav = reader.getChildren();
        while (nav.next()) {
            String localName = reader.getLocalName();
            if (localName.equals(CMIS.CAPABILITIES.getLocalPart())) {
                caps = new APPRepositoryCapabilities();
                String cq = QueryCapability.METADATA_ONLY.toString();
                String ft = null;
                ChildrenNavigator capElems = reader.getChildren();
                while (capElems.next()) {
                    localName = reader.getLocalName();
                    if (localName.equals(CMIS.CAPABILITY_ALL_VERSIONS_SEARCHABLE.getLocalPart())) {
                        caps.setAllVersionsSearchable(Boolean.parseBoolean(reader.getElementText()));
                    } else if (localName.equals(CMIS.CAPABILITY_MULTIFILING.getLocalPart())) {
                        caps.setHasMultifiling(Boolean.parseBoolean(reader.getElementText()));
                    } else if (localName.equals(CMIS.CAPABILITY_PWC_SEARCHABLE.getLocalPart())) {
                        caps.setPWCSearchable(Boolean.parseBoolean(reader.getElementText()));
                    } else if (localName.equals(CMIS.CAPABILITY_PWC_UPDATEABLE.getLocalPart())) {
                        caps.setPWCUpdatable(Boolean.parseBoolean(reader.getElementText()));
                    } else if (localName.equals(CMIS.CAPABILITY_UNFILING.getLocalPart())) {
                        caps.setHasUnfiling(Boolean.parseBoolean(reader.getElementText()));
                    } else if (localName.equals(CMIS.CAPABILITY_VERSION_SPECIFIC_FILING.getLocalPart())) {
                        caps.setHasVersionSpecificFiling(Boolean.parseBoolean(reader.getElementText()));
                    } else if (localName.equals(CMIS.CAPABILITY_QUERY.getLocalPart())) {
                        cq = reader.getElementText();
                    } else if (localName.equals(CMIS.CAPABILITY_FULL_TEXT.getLocalPart())) {
                        ft = reader.getElementText();
                    } else if (localName.equals(CMIS.CAPABILITY_JOIN.getLocalPart())) {
                        String join = reader.getElementText();
                        if ("innerandouter".equals(join)) {
                            caps.setJoinCapability(JoinCapability.INNER_AND_OUTER);    
                        } else if ("inneronly".equals(join)) {
                            caps.setJoinCapability(JoinCapability.INNER_ONLY);
                        } else {
                            caps.setJoinCapability(JoinCapability.NO_JOIN);
                        }
                    }
                }
                if ("both".equals(cq)) {
                    if ("fulltextonly".equals(ft)) {
                        caps.setQueryCapability(QueryCapability.BOTH_SEPARATE);
                    } else {
                        caps.setQueryCapability(QueryCapability.BOTH_COMBINED);    
                    }
                } else {
                    caps.setQueryCapability(QueryCapability.valueOf(cq));
                }
            } else if (localName.equals("repositorySpecificInformation")) {
                readRepositorySpecificInformation(context,  reader);
            } else {
                map.put(localName, reader.getElementText());
            }
        }
        return new APPRepositoryInfo(caps, map);
    }
    
    protected RepositoryInfo getRepositoryInfo(Workspace ws) {                
        Element repoInfo = ws.getFirstChild(CMIS.REPOSITORY_INFO);
        APPRepositoryCapabilities caps = null;
        Map<String, Object> map = new HashMap<String, Object>();
        for (Element el : repoInfo.getElements()) {
            String localName = el.getQName().getLocalPart();
            if (localName.equals(CMIS.CAPABILITIES.getLocalPart())) {
                caps = new APPRepositoryCapabilities();
                String cq = QueryCapability.METADATA_ONLY.toString();
                String ft = null;
                for (Element el2 : el.getElements()) {
                    localName = el2.getQName().getLocalPart();
                    if (localName.equals(CMIS.CAPABILITY_ALL_VERSIONS_SEARCHABLE.getLocalPart())) {
                        caps.setAllVersionsSearchable(Boolean.parseBoolean(el2.getText()));
                    } else if (localName.equals(CMIS.CAPABILITY_MULTIFILING.getLocalPart())) {
                        caps.setHasMultifiling(Boolean.parseBoolean(el2.getText()));
                    } else if (localName.equals(CMIS.CAPABILITY_PWC_SEARCHABLE.getLocalPart())) {
                        caps.setPWCSearchable(Boolean.parseBoolean(el2.getText()));
                    } else if (localName.equals(CMIS.CAPABILITY_PWC_UPDATEABLE.getLocalPart())) {
                        caps.setPWCUpdatable(Boolean.parseBoolean(el2.getText()));
                    } else if (localName.equals(CMIS.CAPABILITY_UNFILING.getLocalPart())) {
                        caps.setHasUnfiling(Boolean.parseBoolean(el2.getText()));
                    } else if (localName.equals(CMIS.CAPABILITY_VERSION_SPECIFIC_FILING.getLocalPart())) {
                        caps.setHasVersionSpecificFiling(Boolean.parseBoolean(el2.getText()));
                    } else if (localName.equals(CMIS.CAPABILITY_QUERY.getLocalPart())) {
                        cq = el2.getText();
                    } else if (localName.equals(CMIS.CAPABILITY_FULL_TEXT.getLocalPart())) {
                        ft = el2.getText();
                    } else if (localName.equals(CMIS.CAPABILITY_JOIN.getLocalPart())) {
                        String join = el2.getText();
                        if ("innerandouter".equals(join)) {
                            caps.setJoinCapability(JoinCapability.INNER_AND_OUTER);    
                        } else if ("inneronly".equals(join)) {
                            caps.setJoinCapability(JoinCapability.INNER_ONLY);
                        } else {
                            caps.setJoinCapability(JoinCapability.NO_JOIN);
                        }
                    }
                }
                if ("both".equals(cq)) {
                    if ("fulltextonly".equals(ft)) {
                        caps.setQueryCapability(QueryCapability.BOTH_SEPARATE);
                    } else {
                        caps.setQueryCapability(QueryCapability.BOTH_COMBINED);    
                    }
                } else {
                    caps.setQueryCapability(QueryCapability.valueOf(cq));
                }
            } else if (localName.equals("repositorySpecificInformation")) {
                // TODO
            } else {
                map.put(localName, el.getText());
            }
        }
        return new APPRepositoryInfo(caps, map);
    }

    protected void readRepositorySpecificInformation(ReadContext context, StaxReader reader) {
        // do nothing
    }
    

}
