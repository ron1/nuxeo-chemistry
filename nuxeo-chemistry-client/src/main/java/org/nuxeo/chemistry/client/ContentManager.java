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

    
    void setCredentialsProvider(CredentialsProvider provider);
    
    CredentialsProvider getCredentialsProvider();

    
    /**
     * Login as the given user. All the subsequent connections made by this content manager will use
     * this login. If you want to temporary run some code using another login you should use {@link #loginAs(String, String)} 
     * and then {@link #logout()} to revert back to the previous login.
     * 
     * @param username
     * @param pass
     */
    void login(String username, String pass);
    
    
    /**
     * Login as the given user but only on the current thread. The other threads will not use this login.
     * You can use to change the current login for the current thread. 
     * This is working like a stack stored in a thread local variable. 
     * You can call {@link #loginAs(String, String)} several times and then logout     
     * @param username
     * @param pass
     */
    void pushLogin(String username, String pass);
    
    void popLogin();
    
    /**
     * Remove global login
     */
    void logout();
    
    /**
     * Get the current login or null if none
     * @return
     */
    Credentials getCurrentLogin();
    
}
