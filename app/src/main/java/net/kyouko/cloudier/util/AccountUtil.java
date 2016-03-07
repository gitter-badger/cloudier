package net.kyouko.cloudier.util;

import android.content.Context;

import net.kyouko.cloudier.model.Account;

/**
 * Util class for reading and saving an account.
 *
 * @author beta
 */
public class AccountUtil {

    private final static String PREF_ACCESS_TOKEN = "ACCOUNT.ACCESS_TOKEN";
    private final static String PREF_EXPIRES_IN = "ACCOUNT.EXPIRES_IN";
    private final static String PREF_OPENID = "ACCOUNT.OPENID";
    private final static String PREF_OPENKEY = "ACCOUNT.OPENKEY";
    private final static String PREF_REFRESH_TOKEN = "ACCOUNT.REFRESH_TOKEN";
    private final static String PREF_USERNAME = "ACCOUNT.USERNAME";
    private final static String PREF_NICK_NAME = "ACCOUNT.NICK_NAME";
    private final static String PREF_AVATAR_URL = "ACCOUNT.AVATAR_URL";


    public static Account readAccount(Context context) {
        PreferenceUtil pref = PreferenceUtil.with(context);

        Account account = new Account();

        account.accessToken = pref.readString(PREF_ACCESS_TOKEN);
        account.expiresIn = pref.readString(PREF_EXPIRES_IN);
        account.openId = pref.readString(PREF_OPENID);
        account.openKey = pref.readString(PREF_OPENKEY);
        account.refreshToken = pref.readString(PREF_REFRESH_TOKEN);
        account.username = pref.readString(PREF_USERNAME);
        account.nickname = pref.readString(PREF_NICK_NAME);
        account.avatarUrl = pref.readString(PREF_AVATAR_URL);

        return account;
    }


    public static void saveAccount(Context context, Account account) {
        PreferenceUtil pref = PreferenceUtil.with(context);

        pref.write()
                .writeString(PREF_ACCESS_TOKEN, account.accessToken)
                .writeString(PREF_EXPIRES_IN, String.valueOf(account.expiresIn))
                .writeString(PREF_OPENID, account.openId)
                .writeString(PREF_OPENKEY, account.openKey)
                .writeString(PREF_REFRESH_TOKEN, account.refreshToken)
                .writeString(PREF_USERNAME, account.username)
                .writeString(PREF_NICK_NAME, account.nickname)
                .writeString(PREF_AVATAR_URL, account.avatarUrl)
                .apply();
    }


    public static Account parseAccountFromUrl(String url) {
        url += "&";

        Account account = new Account();

        account.accessToken = UrlUtil.getValueFromUrl(url, "access_token");
        account.expiresIn = UrlUtil.getValueFromUrl(url, "expires_in");
        account.openId = UrlUtil.getValueFromUrl(url, "openid");
        account.openKey = UrlUtil.getValueFromUrl(url, "openkey");
        account.refreshToken = UrlUtil.getValueFromUrl(url, "refresh_token");
        account.username = UrlUtil.getValueFromUrl(url, "name");
        account.nickname = UrlUtil.getValueFromUrl(url, "nick");

        return account;
    }

}
