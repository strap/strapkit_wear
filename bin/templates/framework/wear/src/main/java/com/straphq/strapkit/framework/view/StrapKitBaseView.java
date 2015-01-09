package com.straphq.strapkit.framework.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.straphq.strapkit.framework.StrapKitBaseActivity;
import com.straphq.strapkit.framework.StrapKitSplashActivity;
import com.straphq.strapkit.framework.messaging.StrapKitWearListener;
import com.straphq.strapkit.framework.util.StrapKitBridge;

import java.io.Serializable;

/**
 * Created by martinza on 1/6/15.
 */
public abstract class StrapKitBaseView implements Serializable {

    public abstract void initialize(StrapKitBaseActivity activity);

    public void onClickFunction(StrapKitBaseActivity activity, String function) {
        //StrapKitBridge.handleMessage(activity, "/show", function);
        activity.sendMessage("/onClick", function);
    }

}
