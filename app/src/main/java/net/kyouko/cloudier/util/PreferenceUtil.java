package net.kyouko.cloudier.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Util class for reading and writing preferences.
 *
 * @author beta
 */
public class PreferenceUtil {

    public final static String PREF_INITIAL_SETUP = "initial_setup";

    public final static String PREF_ACCOUNT_ACCESS_TOKEN = "account.access_token";
    public final static String PREF_ACCOUNT_EXPIRES_IN = "account.expires_in";
    public final static String PREF_ACCOUNT_OPENID = "account.openid";
    public final static String PREF_ACCOUNT_OPENKEY = "account.openkey";
    public final static String PREF_ACCOUNT_REFRESH_TOKEN = "account.refresh_token";
    public final static String PREF_ACCOUNT_USERNAME = "account.username";
    public final static String PREF_ACCOUNT_NICKNAME = "account.nick_name";
    public final static String PREF_ACCOUNT_AVATAR_URL = "account.avatar_url";

    public final static String PREF_IMAGE_QUALITY_OVER_CELLULAR = "image.quality_over_cellular";
    public final static String PREF_IMAGE_QUALITY_OVER_WIFI = "image.quality_over_wifi";
    public final static String PREF_IMAGE_SOURCE = "image.source";

    private final static String PREF_KEY = "net.kyouko.cloudier.pref";


    private SharedPreferences preferences;


    /**
     * Private constructor to create a new instance.
     *
     * @param context context used to read and write preferences
     */
    private PreferenceUtil(Context context) {
        preferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }


    /**
     * Creates a new instance with the given {@code context}.
     *
     * @param context context used to read and write preferences
     * @return a working instance of {@code PreferenceUtil}
     */
    public static PreferenceUtil with(Context context) {
        return new PreferenceUtil(context);
    }


    public boolean readBoolean(String key) {
        return preferences.getBoolean(key, false);
    }


    public int readInt(String key) {
        return preferences.getInt(key, 0);
    }


    public String readString(String key) {
        return preferences.getString(key, "");
    }


    public Writer write() {
        return new Writer(preferences.edit());
    }


    public class Writer {

        private SharedPreferences.Editor editor;


        public Writer(SharedPreferences.Editor editor) {
            this.editor = editor;
        }


        public Writer writeBoolean(String key, boolean value) {
            editor.putBoolean(key, value);
            return this;
        }


        public Writer writeInteger(String key, int value) {
            editor.putInt(key, value);
            return this;
        }


        public Writer writeString(String key, String value) {
            editor.putString(key, value);
            return this;
        }


        public void apply() {
            editor.apply();
        }

    }

}
