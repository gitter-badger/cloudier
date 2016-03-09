package net.kyouko.cloudier.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.kyouko.cloudier.api.Params;
import net.kyouko.cloudier.api.RequestError;
import net.kyouko.cloudier.application.Config;
import net.kyouko.cloudier.model.Account;
import net.kyouko.cloudier.model.BaseTweet;
import net.kyouko.cloudier.model.SourceTweet;
import net.kyouko.cloudier.model.Timeline;
import net.kyouko.cloudier.model.Tweet;
import net.kyouko.cloudier.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

/**
 * Util class for network requests.
 *
 * @author beta
 */
public class RequestUtil {


    private static RequestQueue requestQueue;


    public static synchronized RequestQueue getRequestQueue(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        return requestQueue;
    }


    public static String generateGetRequestUrl(Context context, String baseUrl, Params params) {
        String url = baseUrl + "?";

        Account account = AccountUtil.readAccount(context);
        url += "oauth_consumer_key=" + Config.TENCENT_APP_KEY;
        url += "&access_token=" + account.accessToken;
        url += "&openid=" + account.openId;
        url += "&clientip=" + NetworkUtil.getIpAddress();
        url += "&oauth_version=" + "2.a";
        url += "&scope=" + "all";

        for (Map.Entry<String, String> entry : params.entrySet()) {
            url += "&" + entry.getKey() + "=" + entry.getValue();
        }

        return url;
    }


    /**
     * Returns a {@link RequestError} if there is error with the {@code reponse} or the JSON is invalid.
     */
    private static RequestError getRequestErrorFromResponse(JSONObject response) {
        try {
            int errorCode = response.getInt("errcode");
            if (errorCode == 0) {
                return null;
            } else {
                String errorMessage = response.getString("msg");
                return new RequestError(errorCode, errorMessage);
            }
        } catch (JSONException ex) {
            return RequestError.createJsonError(ex);
        }
    }


    /**
     * Parses a {@link JSONObject} into a {@link Timeline}.
     *
     * @param response a {@link JSONObject} returned from a request representing a timeline
     * @return a {@link Timeline} object
     * @throws RequestError if there's error in response or JSON is invalid
     */
    public static Timeline parseTimelineFromResponse(JSONObject response) throws RequestError {
        RequestError requestError = getRequestErrorFromResponse(response);
        if (requestError != null) {
            throw requestError;
        }

        Timeline timeline = new Timeline();

        try {
            JSONObject timelineObject = response.getJSONObject("data");

            timeline.timestamp = timelineObject.getLong("timestamp");
            timeline.hasNext = (timelineObject.getInt("hasnext") == 1);

            JSONArray tweetsArray = timelineObject.getJSONArray("info");
            for (int i = 0; i < tweetsArray.length(); i += 1) {
                timeline.tweets.add(parseTweetFromJson(tweetsArray.getJSONObject(i)));
            }

            JSONObject usersObject = timelineObject.getJSONObject("user");
            Iterator<String> keyIterator = usersObject.keys();
            while (keyIterator.hasNext()) {
                String username = keyIterator.next();
                String nickname = usersObject.getString(username);

                timeline.userList.put(username, nickname);
            }
        } catch (JSONException ex) {
            throw RequestError.createJsonError(ex);
        }

        return timeline;
    }


    /**
     * Returns the brief information of the sender of a tweet.
     *
     * @param tweetObject a {@link JSONObject} representing a tweet
     * @return a {@link net.kyouko.cloudier.model.BaseTweet.BriefUser} for the uesr info
     * @throws JSONException if JSON is invalid
     */
    private static BaseTweet.BriefUser parseTweetUserFromJson(JSONObject tweetObject) throws JSONException {
        BaseTweet.BriefUser user = new BaseTweet.BriefUser();

        user.avatarUrl = tweetObject.getString("head");
        user.username = tweetObject.getString("name");
        user.nickName = tweetObject.getString("nick");
        user.openId = tweetObject.getString("openid");

        return user;
    }


    /**
     * Parses a {@link JSONObject} into a {@link Tweet}.
     *
     * @param tweetObject a {@link JSONObject} representing a tweet
     * @return a {@link Tweet} object
     * @throws JSONException if JSON is invalid
     */
    public static Tweet parseTweetFromJson(JSONObject tweetObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.user = parseTweetUserFromJson(tweetObject);
        tweet.retweetedCount = tweetObject.getInt("count");
        tweet.from = tweetObject.getString("from");
        tweet.fromUrl = tweetObject.getString("fromurl");
        tweet.id = Long.parseLong(tweetObject.getString("id"));
        tweet.timestamp = tweetObject.getLong("timestamp");
        tweet.likedCount = tweetObject.getInt("likecount");
        tweet.commentedCount = tweetObject.getInt("mcount");
        tweet.content = tweetObject.getString("text");
        tweet.originalContent = tweetObject.getString("origtext");
        tweet.sentBySelf = (tweetObject.getInt("self") == 1);
        tweet.status = tweetObject.getInt("status");
        tweet.type = tweetObject.getInt("type");

        if (tweetObject.has("source") && !tweetObject.isNull("source")) {
            JSONObject sourceTweetObject = tweetObject.getJSONObject("source");
            tweet.sourceTweet = parseSourceTweetFromJson(sourceTweetObject);
            tweet.hasSourceTweet = true;
        }

        if (tweetObject.has("image") && !tweetObject.isNull("image")) {
            JSONArray images = tweetObject.getJSONArray("image");
            if (images != null) {
                for (int i = 0; i < images.length(); i += 1) {
                    tweet.images.add(images.getString(i));
                }
            }
        }

        if (tweetObject.has("user") && !tweetObject.isNull("user")) {
            JSONObject users = tweetObject.getJSONObject("user");
            Iterator<String> keyIterator = users.keys();
            while (keyIterator.hasNext()) {
                String username = keyIterator.next();
                String nickname = users.getString(username);

                tweet.userList.put(username, nickname);
            }
        }

        return tweet;
    }


    /**
     * Parses a {@link JSONObject]} into a {@link SourceTweet}.
     *
     * @param sourceTweetObject a {@link JSONObject} representing a source tweet
     * @return a {@link SourceTweet} object
     * @throws JSONException if JSON is invalid
     */
    public static SourceTweet parseSourceTweetFromJson(JSONObject sourceTweetObject) throws JSONException {
        SourceTweet sourceTweet = new SourceTweet();

        sourceTweet.user = parseTweetUserFromJson(sourceTweetObject);
        sourceTweet.retweetedCount = sourceTweetObject.getInt("count");
        sourceTweet.from = sourceTweetObject.getString("from");
        sourceTweet.fromUrl = sourceTweetObject.getString("fromurl");
        sourceTweet.id = Long.parseLong(sourceTweetObject.getString("id"));
        sourceTweet.timestamp = sourceTweetObject.getLong("timestamp");
        sourceTweet.likedCount = sourceTweetObject.getInt("likecount");
        sourceTweet.commentedCount = sourceTweetObject.getInt("mcount");
        sourceTweet.content = sourceTweetObject.getString("text");
        sourceTweet.originalContent = sourceTweetObject.getString("origtext");
        sourceTweet.sentBySelf = (sourceTweetObject.getInt("self") == 1);
        sourceTweet.status = sourceTweetObject.getInt("status");
        sourceTweet.type = sourceTweetObject.getInt("type");

        if (sourceTweetObject.has("image") && !sourceTweetObject.isNull("image")) {
            JSONArray images = sourceTweetObject.getJSONArray("image");
            if (images != null) {
                for (int i = 0; i < images.length(); i += 1) {
                    sourceTweet.images.add(images.getString(i));
                }
            }
        }

        return sourceTweet;
    }


    /**
     * Parses a {@link JSONObject} into a {@link User}.
     *
     * @param response a {@link JSONObject} returned from a request representing a user
     * @return a {@link User} object
     * @throws RequestError if there's error in response or JSON is invalid
     */
    public static User parseUserFromResponse(JSONObject response) throws RequestError {
        RequestError requestError = getRequestErrorFromResponse(response);
        if (requestError != null) {
            throw requestError;
        }

        User user = new User();

        try {
            JSONObject data = response.getJSONObject("data");

            user.username = data.getString("name");
            user.nickName = data.getString("nick");
            user.openId = data.getString("openid");
            user.avatarUrl = data.getString("head");
            user.gender = data.getInt("sex");
            user.followerCount = data.getInt("fansnum");
            user.followingCount = data.getInt("idolnum");
            user.tweetCount = data.getInt("tweetnum");
            user.isFollowingCurrentUser = (data.getInt("ismyfans") == 1);
            user.isFollowedByCurrentUser = (data.getInt("ismyidol") == 1);
            user.isInBlackListOfCurrentUser = (data.getInt("ismyblack") == 1);
        } catch (JSONException ex) {
            throw RequestError.createJsonError(ex);
        }

        return user;
    }

}
