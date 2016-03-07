package net.kyouko.cloudier.util;

import android.content.Context;

import net.kyouko.cloudier.api.Params;
import net.kyouko.cloudier.application.Config;
import net.kyouko.cloudier.model.Account;

import java.util.Map;

/**
 * Util class for network requests.
 *
 * @author beta
 */
public class RequestUtil {

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

}
