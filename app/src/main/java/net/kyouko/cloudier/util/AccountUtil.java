package net.kyouko.cloudier.util;

import android.content.Context;

import net.kyouko.cloudier.model.Account;

/**
 * Util class for reading and saving an account.
 *
 * @author beta
 */
public class AccountUtil {

    public static Account readAccount(Context context) {
        PreferenceUtil pref = PreferenceUtil.with(context);

        Account account = new Account();

        account.accessToken = pref.readString(PreferenceUtil.PREF_ACCOUNT_ACCESS_TOKEN);
        account.expiresIn = pref.readString(PreferenceUtil.PREF_ACCOUNT_EXPIRES_IN);
        account.openId = pref.readString(PreferenceUtil.PREF_ACCOUNT_OPENID);
        account.openKey = pref.readString(PreferenceUtil.PREF_ACCOUNT_OPENKEY);
        account.refreshToken = pref.readString(PreferenceUtil.PREF_ACCOUNT_REFRESH_TOKEN);
        account.username = pref.readString(PreferenceUtil.PREF_ACCOUNT_USERNAME);
        account.nickname = pref.readString(PreferenceUtil.PREF_ACCOUNT_NICKNAME);
        account.avatarUrl = pref.readString(PreferenceUtil.PREF_ACCOUNT_AVATAR_URL);

        return account;
    }


    public static void saveAccount(Context context, Account account) {
        PreferenceUtil pref = PreferenceUtil.with(context);

        pref.write()
                .writeString(PreferenceUtil.PREF_ACCOUNT_ACCESS_TOKEN, account.accessToken)
                .writeString(PreferenceUtil.PREF_ACCOUNT_EXPIRES_IN, String.valueOf(account.expiresIn))
                .writeString(PreferenceUtil.PREF_ACCOUNT_OPENID, account.openId)
                .writeString(PreferenceUtil.PREF_ACCOUNT_OPENKEY, account.openKey)
                .writeString(PreferenceUtil.PREF_ACCOUNT_REFRESH_TOKEN, account.refreshToken)
                .writeString(PreferenceUtil.PREF_ACCOUNT_USERNAME, account.username)
                .writeString(PreferenceUtil.PREF_ACCOUNT_NICKNAME, account.nickname)
                .writeString(PreferenceUtil.PREF_ACCOUNT_AVATAR_URL, account.avatarUrl)
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
