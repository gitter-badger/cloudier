package net.kyouko.cloudier.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import net.kyouko.cloudier.util.ImageUtil;
import net.kyouko.cloudier.util.PreferenceUtil;

public class CloudierApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

        if (!hasInitialSetup()) {
            performInitialSetup();
        }
    }


    private boolean hasInitialSetup() {
        return PreferenceUtil.with(this).readBoolean(PreferenceUtil.PREF_INITIAL_SETUP);
    }


    private void performInitialSetup() {
        PreferenceUtil.with(this).write()
                .writeInteger(PreferenceUtil.PREF_IMAGE_QUALITY_OVER_CELLULAR,
                        ImageUtil.QUALITY_LOW)
                .writeInteger(PreferenceUtil.PREF_IMAGE_QUALITY_OVER_WIFI,
                        ImageUtil.QUALITY_HIGH)
                .writeString(PreferenceUtil.PREF_IMAGE_SOURCE,
                        ImageUtil.PREFIX_APP)
                .writeBoolean(PreferenceUtil.PREF_INITIAL_SETUP, true)
                .apply();
    }

}
