package org.cloudsdale.android.exceptions;

public class CloudsdaleQueryException extends Exception {

    /**
     * Generated Serial UID
     */
    private static final long serialVersionUID = -8230002899300580899L;
    
    private int errorCode;
    private String message;
    
    public CloudsdaleQueryException(String message, int code) {
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
