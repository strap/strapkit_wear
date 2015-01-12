package com.straphq.strapkit.framework;

import android.app.Application;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by martinza on 1/11/15.
 */
public class StrapKitApplication extends Application {

    private boolean mHasFinishedSplash = false;

    private ArrayList<Intent> baseActivities = new ArrayList<>();

    public void launchNewActivity(Intent activityIntent) {
        if (mHasFinishedSplash) {
            startActivity(activityIntent);
        } else {
            baseActivities.add(activityIntent);
        }
    }

    public void finishedSplashPage() {
        mHasFinishedSplash = true;
        Collections.reverse(baseActivities);
        for (Intent intent : baseActivities) {
            startActivity(intent);
        }
    }
}
