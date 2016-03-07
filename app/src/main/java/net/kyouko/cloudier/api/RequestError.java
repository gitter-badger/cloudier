package net.kyouko.cloudier.api;

/**
 * A custom exception representing an error in requests.
 *
 * @author beta
 */
public class RequestError extends Exception {

    public int errorCode;
    public String errorMessage;


    /**
     * Creates a new empty instance.
     */
    public RequestError() {
    }


    /**
     * Creates a new instance with {@code message}.
     */
    public RequestError(String message) {
        super(message);
    }


    /**
     * Creates a new instance with the given parameters.
     *
     * @param message message of the exception
     * @param errorCode error code
     * @param errorMessage error message
     */
    public RequestError(String message, int errorCode, String errorMessage) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
