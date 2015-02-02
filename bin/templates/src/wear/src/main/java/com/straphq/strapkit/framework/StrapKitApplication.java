package com.straphq.strapkit.framework;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.straphq.strapkit.framework.util.PluginManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by martinza on 1/11/15.
 */
public class StrapKitApplication extends Application {

    private PluginManager pluginManager;

    @Override
    public void onCreate() {
        super.onCreate();

        pluginManager = new PluginManager(this);
        pluginManager.init();
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    private static final String TAG = StrapKitApplication.class.getSimpleName();

    private boolean mHasFinishedSplash = false;

    public boolean hasFinishedSplash() {
        return mHasFinishedSplash;
    }

    public void finishedSplashPage() {
        mHasFinishedSplash = true;
    }

    public void setHasShownSplash(Boolean hasShown) {
        mHasFinishedSplash = hasShown;
    }
}