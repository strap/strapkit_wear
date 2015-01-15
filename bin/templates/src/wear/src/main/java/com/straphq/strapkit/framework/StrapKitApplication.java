package com.straphq.strapkit.framework;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

/**
 * Created by martinza on 1/11/15.
 */
public class StrapKitApplication extends Application {

    private static final String TAG = StrapKitApplication.class.getSimpleName();

    private boolean mHasFinishedSplash = false;

    private Queue<Intent> baseActivities = new LinkedList<>();

    public void launchNewActivity(Intent activityIntent) {
        if (mHasFinishedSplash) {
            startActivity(activityIntent);
        } else {
            baseActivities.add(activityIntent);
        }
    }

    public boolean hasFinishedSplash() {
        return mHasFinishedSplash;
    }

    public void finishedSplashPage() {
        mHasFinishedSplash = true;
        int activitySize = baseActivities.size();
        for (int i = 0; i < activitySize; i++) {
            Intent intent = baseActivities.remove();
            Log.d(TAG, "Intent: " + intent.getSerializableExtra(StrapKitBaseActivity.ARGS_VIEW_DEFINITIONS));
            startActivity(intent);
        }
    }

    public void setHasShownSplash(Boolean hasShown) {
        mHasFinishedSplash = hasShown;
    }
}
