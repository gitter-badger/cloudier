package net.kyouko.cloudier.api.tweet;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

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

import org.json.JSONObject;

/**
 * A custom {@link Request} to retweet a tweet.
 *
 * @author beta
 */
public class RetweetRequest extends Request<TweetId> {

    private final static String API_URL = "https://open.t.qq.com/api/t/re_add";


    private Long tweetId;
    private String content;

    /**
     * Creates a new instance.
     *
     * @param activity        a {@link RequestActivity} that will execute this request
     * @param tweetId         ID of tweet on which to retweet
     * @param content         content of the tweet to send
     * @param successListener listener for request success
     * @param errorListener   listener for request error
     */
    public RetweetRequest(RequestActivity activity, Long tweetId, String content,
                          RequestSuccessListener<TweetId> successListener,
                          RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);

        this.tweetId = tweetId;
        this.content = content;
    }


    @Override
    public void execute() {
        JsonParams params = new JsonParams();
        params.put("reid", String.valueOf(tweetId));
        params.put("content", content);

        request = new PostRequest(API_URL,
                RequestUtil.generatePostRequestParams((Context) activity, params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            RetweetRequestResult result = new RetweetRequestResult(response);
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


    private class RetweetRequestResult extends RequestResult<TweetId> {

        public RetweetRequestResult(JSONObject response) throws RequestError {
            super(response);
        }


        @Override
        protected void parseResponse(JSONObject response) throws RequestError {
            data = RequestUtil.parseTweetIdFromResponse(response);
        }

    }

}
