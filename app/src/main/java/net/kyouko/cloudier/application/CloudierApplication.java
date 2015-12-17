package net.kyouko.cloudier.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class CloudierApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }

}
