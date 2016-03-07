package net.kyouko.cloudier.activity;

import com.android.volley.Request;

/**
 * Interface for activities that will execute requests.
 *
 * @author beta
 */
public interface RequestActivity {
    void executeRequest(Request request);
}
