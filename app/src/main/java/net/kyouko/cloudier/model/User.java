package net.kyouko.cloudier.model;

/**
 * Class for information of a user.
 *
 * @author beta
 */
public class User {

    public final static int GENDER_MALE = 1;
    public final static int GENDER_FEMALE = 2;
    public final static int GENDER_UNKNOWN = 0;

    public String openId;
    public String username;
    public String nickName;
    public String avatarUrl;
    public Integer gender;
    public Integer tweetCount;
    public Integer followerCount;
    public Integer followingCount;
    public Boolean isFollowingCurrentUser;
    public Boolean isFollowedByCurrentUser;
    public Boolean isInBlackListOfCurrentUser;

}
