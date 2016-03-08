package net.kyouko.cloudier.api;

import org.json.JSONObject;

/**
 * Base class for request results.
 *
 * @author beta
 */
public abstract class RequestResult<T> {

    protected T data;


    public RequestResult(JSONObject response) throws RequestError {
        parseResponse(response);
    }


    /**
     * Parses the request response.
     *
     * @param response a {@link JSONObject} returned from the request
     */
    protected abstract void parseResponse(JSONObject response) throws RequestError;


    public T getData() {
        return data;
    }

}
