package net.kyouko.cloudier.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for a piece of tweet.
 *
 * @author beta
 */
public class Tweet extends BaseTweet implements Serializable {

    public boolean hasSourceTweet = false;
    public SourceTweet sourceTweet = null;
    public Map<String, String> userList = new HashMap<>();

}
