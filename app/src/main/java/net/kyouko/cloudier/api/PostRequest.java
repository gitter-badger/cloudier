package net.kyouko.cloudier.api;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Custom Volley request to post params to the server and retrieve a {@link JSONObject} response.
 *
 * @author beta
 */
public class PostRequest extends com.android.volley.Request<JSONObject> {

    private Map<String, String> params;
    private final Response.Listener<JSONObject> listener;


    public PostRequest(String url, Map<String, String> params,
                       Response.Listener<JSONObject> listener,
                       Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.params = params;
        this.listener = listener;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException ex) {
            return Response.error(new ParseError(ex));
        } catch (JSONException ex) {
            return Response.error(new ParseError(ex));
        }
    }


    @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }

}
