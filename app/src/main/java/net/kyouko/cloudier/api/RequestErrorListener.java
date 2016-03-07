package net.kyouko.cloudier.api;

/**
 * A listener interface used when a request failed.
 *
 * @author beta
 */
public interface RequestErrorListener {
    void onRequestError(RequestError error);
}
