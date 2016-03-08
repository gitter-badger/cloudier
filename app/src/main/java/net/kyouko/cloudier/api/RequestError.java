package net.kyouko.cloudier.api;

import com.android.volley.VolleyError;

import org.json.JSONException;

/**
 * A custom exception representing an error in requests.
 *
 * @author beta
 */
public class RequestError extends Exception {


    public final static int JSON_ERROR_CODE = -1;
    public final static int NETWORK_ERROR_CODE = -2;


    public int errorCode;
    public String errorMessage;


    /**
     * Creates a new instance with the given parameters.
     *
     * @param errorCode    error code
     * @param errorMessage error message
     */
    public RequestError(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }


    public static RequestError createJsonError(JSONException ex) {
        return new RequestError(JSON_ERROR_CODE, ex.getLocalizedMessage());
    }


    public static RequestError createNetworkError(VolleyError error) {
        return new RequestError(NETWORK_ERROR_CODE, error.getLocalizedMessage());
    }

}
