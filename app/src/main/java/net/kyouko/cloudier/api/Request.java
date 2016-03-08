package net.kyouko.cloudier.api;

import android.util.Log;

import net.kyouko.cloudier.ui.activity.RequestActivity;

/**
 * Base class for all the API requests.
 *
 * @author beta
 */
public abstract class Request<ResultType> {

    protected RequestActivity activity;
    protected com.android.volley.Request request;
    protected RequestSuccessListener<ResultType> successListener;
    protected RequestErrorListener errorListener;


    /**
     * Creates a new instance.
     *
     * @param activity        a {@link RequestActivity} that will execute this request
     * @param successListener listener for request success
     * @param errorListener   listener for request error
     */
    public Request(RequestActivity activity,
                   RequestSuccessListener<ResultType> successListener,
                   RequestErrorListener errorListener) {
        this.activity = activity;
        this.successListener = successListener;
        this.errorListener = errorListener;
    }


    /**
     * Executes the request.
     */
    public abstract void execute();


    /**
     * Handles the request result.
     */
    public void handleResult(ResultType result) {
        if (successListener != null) {
            successListener.onRequestSuccess(result);
        }
    }


    /**
     * Handles the request error.
     */
    protected void handleError(RequestError error) {
        Log.e("[Request error]", error.getLocalizedMessage());
        if (errorListener != null) {
            errorListener.onRequestError(error);
        }
    }

}
