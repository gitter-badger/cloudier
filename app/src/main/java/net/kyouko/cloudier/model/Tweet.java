package net.kyouko.cloudier.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for a piece of tweet.
 *
 * @author beta
 */
public class Tweet extends BaseTweet {

    public SourceTweet sourceTweet;
    public Map<String, String> userList = new HashMap<>();

}
