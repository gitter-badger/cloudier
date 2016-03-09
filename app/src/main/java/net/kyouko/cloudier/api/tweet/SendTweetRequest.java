package net.kyouko.cloudier.api.tweet;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import net.kyouko.cloudier.api.JsonParams;
import net.kyouko.cloudier.api.PostRequest;
import net.kyouko.cloudier.api.Request;
import net.kyouko.cloudier.api.RequestError;
import net.kyouko.cloudier.api.RequestErrorListener;
import net.kyouko.cloudier.api.RequestResult;
import net.kyouko.cloudier.api.RequestSuccessListener;
import net.kyouko.cloudier.model.TweetId;
import net.kyouko.cloudier.ui.activity.RequestActivity;
import net.kyouko.cloudier.util.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A custom {@link Request} to send a tweet.
 *
 * @author beta
 */
public class SendTweetRequest extends Request<TweetId> {

    private final static String API_URL = "https://open.t.qq.com/api/t/add";


    private String content;


    /**
     * Creates a new instance.
     *
     * @param activity        a {@link RequestActivity} that will execute this request
     * @param content         content of the tweet to send
     * @param successListener listener for request success
     * @param errorListener   listener for request error
     */
    public SendTweetRequest(RequestActivity activity, String content,
                            RequestSuccessListener<TweetId> successListener,
                            RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);

        this.content = content;
    }


    @Override
    public void execute() {
        final JsonParams params = new JsonParams();
        params.put("content", content);

        request = new PostRequest(API_URL,
                RequestUtil.generatePostRequestParams((Context) activity, params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SendTweetRequestResult result = new SendTweetRequestResult(response);
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


    private class SendTweetRequestResult extends RequestResult<TweetId> {

        public SendTweetRequestResult(JSONObject response) throws RequestError {
            super(response);
        }


        @Override
        protected void parseResponse(JSONObject response) throws RequestError {
            data = RequestUtil.parseTweetIdFromResponse(response);
        }

    }
}
