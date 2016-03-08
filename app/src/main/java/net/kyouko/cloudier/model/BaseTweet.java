package net.kyouko.cloudier.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for tweets and source tweets.
 *
 * @author beta
 */
public abstract class BaseTweet extends TimelineEntry {

    public final static int TYPE_ORIGINAL = 1;
    public final static int TYPE_RETWEET = 2;
    public final static int TYPE_MESSAGE = 3;
    public final static int TYPE_REPLY = 4;
    public final static int TYPE_EMPTY_REPLY = 5;
    public final static int TYPE_REFERENCE = 6;
    public final static int TYPE_COMMENT = 7;

    public final static int STATUS_NORMAL = 0;
    public final static int STATUS_DELETED_BY_SYSTEM = 1;
    public final static int STATUS_REVIEWING = 2;
    public final static int STATUS_DELETED_BY_USER = 3;
    public final static int STATUS_ROOT_DELETED = 4;


    public Long id;
    public BriefUser user = new BriefUser();
    public Boolean sentBySelf;
    public Long timestamp;
    public Integer type;
    public String content;
    public String originalContent;
    public Integer retweetedCount;
    public Integer commentedCount;
    public Integer likedCount;
    public String from;
    public String fromUrl;
    public List<String> images = new ArrayList<>();
    public Integer status;


    /**
     * Class for the brief information of a user used in tweets.
     */
    public static class BriefUser {

        public String username;
        public String nickName;
        public String openId;
        public String avatarUrl;

    }

}
