package net.kyouko.cloudier.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for the timeline.
 *
 * @author beta
 */
public class Timeline {

    public Long timestamp;
    public Boolean hasNext;
    public List<Tweet> tweets = new ArrayList<>();
    public Map<String, String> userList = new HashMap<>();


    public boolean containsTweet(Tweet tweet) {
        for (Tweet t : tweets) {
            if (tweet.id.equals(t.id)) {
                return true;
            }
        }
        return false;
    }

}
