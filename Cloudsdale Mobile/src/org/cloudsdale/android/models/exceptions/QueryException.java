package org.cloudsdale.android.models.exceptions;

public class QueryException extends Exception {

    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -8230002899300580899L;
    
    private int errorCode;
    private String message;
    
    public QueryException(String message, int code) {
        this.message = message;
        this.errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }
    
    public String getMessage() {
        return message;
    }
    
}
