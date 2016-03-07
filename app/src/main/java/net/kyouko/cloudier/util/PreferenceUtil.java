package net.kyouko.cloudier.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by beta on 12/22/15.
 */
public class PreferenceUtil {

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


        public Writer writeString(String key, String value) {
            editor.putString(key, value).apply();
            return this;
        }


        public void apply() {
            editor.apply();
        }

    }

}
