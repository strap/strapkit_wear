package com.straphq.strapkit.strapkit_lib.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootReceiver extends BroadcastReceiver {
    public OnBootReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, StrapKitMessageService.class);
        context.startService(serviceIntent);
    }
}
