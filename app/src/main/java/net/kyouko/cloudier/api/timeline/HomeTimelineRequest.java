package net.kyouko.cloudier.api.timeline;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import net.kyouko.cloudier.api.RequestError;
import net.kyouko.cloudier.api.RequestResult;
import net.kyouko.cloudier.ui.activity.RequestActivity;
import net.kyouko.cloudier.api.JsonParams;
import net.kyouko.cloudier.api.Request;
import net.kyouko.cloudier.api.RequestErrorListener;
import net.kyouko.cloudier.api.RequestSuccessListener;
import net.kyouko.cloudier.model.Timeline;
import net.kyouko.cloudier.util.RequestUtil;

import org.json.JSONObject;

/**
 * A custom {@link Request} to fetch the timeline of home page.
 *
 * @author beta
 */
public class HomeTimelineRequest extends Request<Timeline> {

    private final static String API_URL = "https://open.t.qq.com/api/statuses/home_timeline";

    public final static int TYPE_ALL = 0;
    public final static int TYPE_ORIGINAL = 0x1;
    public final static int TYPE_RETWEETS = 0x2;

    public final static int CONTENT_TYPE_ALL = 0;
    public final static int CONTENT_TYPE_WITH_TEXTS = 1;
    public final static int CONTENT_TYPE_WITH_LINKS = 2;
    public final static int CONTENT_TYPE_WITH_PICS = 4;
    public final static int CONTENT_TYPE_WITH_VIDEOS = 8;

    public final static int PAGE_FLAG_FIRST = 0;
    public final static int PAGE_FLAG_DOWN = 1;
    public final static int PAGE_FLAG_UP = 2;


    private int type = TYPE_ALL;
    private int contentType = CONTENT_TYPE_ALL;
    private int requestNumber = 30;
    private int pageFlag = PAGE_FLAG_FIRST;
    private long pageTime = 0;


    /**
     * Creates a new intance of request.
     *
     * @see #HomeTimelineRequest(RequestActivity, int, int, int, int, long, RequestSuccessListener, RequestErrorListener)
     */
    public HomeTimelineRequest(RequestActivity activity,
                               RequestSuccessListener<Timeline> successListener,
                               RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);
    }


    /**
     * Creates a new intance of request.
     *
     * @see #HomeTimelineRequest(RequestActivity, int, int, int, int, long, RequestSuccessListener, RequestErrorListener)
     */
    public HomeTimelineRequest(RequestActivity activity, int requestNumber,
                               RequestSuccessListener<Timeline> successListener,
                               RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);
        this.requestNumber = requestNumber;
    }


    /**
     * Creates a new intance of request.
     *
     * @see #HomeTimelineRequest(RequestActivity, int, int, int, int, long, RequestSuccessListener, RequestErrorListener)
     */
    public HomeTimelineRequest(RequestActivity activity, int type, int contentType, int requestNumber,
                               RequestSuccessListener<Timeline> successListener,
                               RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);
        this.type = type;
        this.contentType = contentType;
        this.requestNumber = requestNumber;
    }


    /**
     * Creates a new intance of request.
     *
     * @see #HomeTimelineRequest(RequestActivity, int, int, int, int, long, RequestSuccessListener, RequestErrorListener)
     */
    public HomeTimelineRequest(RequestActivity activity, int pageFlag, long pageTime,
                               RequestSuccessListener<Timeline> successListener,
                               RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);
        this.pageFlag = pageFlag;
        this.pageTime = pageTime;
    }


    /**
     * Creates a new intance of request.
     *
     * @see #HomeTimelineRequest(RequestActivity, int, int, int, int, long, RequestSuccessListener, RequestErrorListener)
     */
    public HomeTimelineRequest(RequestActivity activity, int requestNumber, int pageFlag, long pageTime,
                               RequestSuccessListener<Timeline> successListener,
                               RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);
        this.requestNumber = requestNumber;
        this.pageFlag = pageFlag;
        this.pageTime = pageTime;
    }


    /**
     * Creates a new intance of request.
     *
     * @see #HomeTimelineRequest(RequestActivity, int, int, int, int, long, RequestSuccessListener, RequestErrorListener)
     */
    public HomeTimelineRequest(RequestActivity activity, int type, int contentType,
                               int pageFlag, long pageTime,
                               RequestSuccessListener<Timeline> successListener,
                               RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);
        this.type = type;
        this.contentType = contentType;
        this.pageFlag = pageFlag;
        this.pageTime = pageTime;
    }


    /**
     * Creates a new instance of request.
     *
     * @param activity        a {@link RequestActivity} to execute the request
     * @param type            type filter of tweets to fetch
     * @param contentType     type filter of content
     * @param requestNumber   number of tweets to fetch
     * @param pageFlag        flag determining to fetch the first page or page up/down
     * @param pageTime        time of the first/last tweet in last request used for paging up/down
     * @param successListener listener for request success
     * @param errorListener   listener for request error
     */
    public HomeTimelineRequest(RequestActivity activity, int type, int contentType, int requestNumber,
                               int pageFlag, long pageTime,
                               RequestSuccessListener<Timeline> successListener,
                               RequestErrorListener errorListener) {
        super(activity, successListener, errorListener);
        this.type = type;
        this.contentType = contentType;
        this.requestNumber = requestNumber;
        this.pageFlag = pageFlag;
        this.pageTime = pageTime;
    }


    @Override
    public void execute() {
        JsonParams params = new JsonParams();
        params.put("type", Integer.toString(type));
        params.put("contenttype", Integer.toString(contentType));
        params.put("reqnum", Integer.toString(requestNumber));
        params.put("pageflag", Integer.toString(pageFlag));
        params.put("pagetime", Long.toString(pageTime));

        String url = RequestUtil.generateGetRequestUrl((Context) activity, API_URL, params);

        request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            HomeTimelineRequestResult result = new HomeTimelineRequestResult(response);
                            handleResult(result.getData());
                        } catch (RequestError error) {
                            handleError(error);
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


    private class HomeTimelineRequestResult extends RequestResult<Timeline> {

        public HomeTimelineRequestResult(JSONObject response) throws RequestError {
            super(response);
        }


        @Override
        protected void parseResponse(JSONObject response) throws RequestError {
            data = RequestUtil.parseTimelineFromResponse(response);
        }

    }

}
