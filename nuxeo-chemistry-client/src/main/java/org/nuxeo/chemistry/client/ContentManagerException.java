package org.nuxeo.chemistry.client;



/**
 * @author matic
 *
 */
public  class ContentManagerException extends RuntimeException {

    private static final long serialVersionUID = -1328541867538528968L;

    public ContentManagerException(String message) {
        super(message);
    }

    public ContentManagerException(String message, Exception e) {
        super(message,e);
    }
}
