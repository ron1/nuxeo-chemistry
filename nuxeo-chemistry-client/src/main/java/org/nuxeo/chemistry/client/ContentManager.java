package org.nuxeo.chemistry.client;


import org.apache.chemistry.repository.Repository;

/**
 * The entry point to CMIS repositories exposed by a server.
 *
 * An instance of this service can be used to enumerate available repositories, and fetch repository objects given they name.
 * This interface doesn't define how to register new repositories.
 * <p>
 * There will be different implementations depending on how the repositories are accessed i.e.
 * whether or not the ContentManager is used to access remote or local repositories.
 * <p>
 * The discovery mechanism used by the implementation to detect repositories is up to the implementors.
 * Repositories connected through APP will use APP discovery, local repositories will use repository specific java API etc.
 *
 * @author <a href="mailto:bs@nuxeo.com">Bogdan Stefanescu</a>
 *
 */
public interface ContentManager {    
    
    Repository[] getRepositories() throws ContentManagerException;

    Repository getDefaultRepository() throws ContentManagerException;

    Repository getRepository(String id) throws NoSuchRepositoryException, ContentManagerException;

}
