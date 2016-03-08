package net.kyouko.cloudier.api.user;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import net.kyouko.cloudier.api.JsonParams;
import net.kyouko.cloudier.api.Request;
import net.kyouko.cloudier.api.RequestError;
import net.kyouko.cloudier.api.RequestErrorListener;
import net.kyouko.cloudier.api.RequestResult;
import net.kyouko.cloudier.api.RequestSuccessListener;
import net.kyouko.cloudier.model.User;
import net.kyouko.cloudier.ui.activity.RequestActivity;
import net.kyouko.cloudier.util.RequestUtil;

import org.json.JSONObject;

/**
 * A custom {@link Request} to fetch the information of a user.
 *
 * @author beta
 */
public class UserInfoRequest extends Request<User> {

    private final static String API_URL = "https://open.t.qq.com/api/user/other_info";


    private String username;


    /**
     * Creates a new instance of request.
     *
     * @param activity               a {@link RequestActivity} to execute the request
     * @param username               username of the user whose info should be fetched
     * @param requestSuccessListener listener for request success
     * @param requestErrorListener   listener for request error
     */
    public UserInfoRequest(RequestActivity activity, String username,
                           RequestSuccessListener<User> requestSuccessListener,
                           RequestErrorListener requestErrorListener) {
        super(activity, requestSuccessListener, requestErrorListener);

        this.username = username;
    }


    @Override
    public void execute() {
        JsonParams params = new JsonParams();
        params.put("name", username);

        String url = RequestUtil.generateGetRequestUrl((Context) activity, API_URL, params);

        request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserInfoRequestResult result = new UserInfoRequestResult(response);
                            handleResult(result.getData());
                        } catch (RequestError requestError) {
                            handleError(requestError);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(RequestError.createNetworkError(error));
                    }
                }
        );
        request.setShouldCache(false);

        activity.executeRequest(request);
    }


    private class UserInfoRequestResult extends RequestResult<User> {

        public UserInfoRequestResult(JSONObject response) throws RequestError {
            super(response);
        }


        @Override
        protected void parseResponse(JSONObject response) throws RequestError {
            data = RequestUtil.parseUserFromResponse(response);
        }

    }

}
