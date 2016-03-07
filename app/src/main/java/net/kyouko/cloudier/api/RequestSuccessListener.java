package net.kyouko.cloudier.api;

/**
 * A listener interface used when a request succeeds.
 *
 * @author beta
 */
public interface RequestSuccessListener<ResultType> {
    void onRequestSuccess(ResultType result);
}
