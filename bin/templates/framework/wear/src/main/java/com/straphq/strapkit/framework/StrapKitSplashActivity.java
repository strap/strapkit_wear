package com.straphq.strapkit.framework;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import com.straphq.strapkit.framework.messaging.StrapKitWearListener;


public class StrapKitSplashActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener {


    public static final String READY_TO_CLOSE_FILTER = "com.straphq.strapkit.framework.READY_TO_CLOSE_FILTER";
    public static final String GET_APP_INFO_FILTER = "com.straphq.strapkit.framework.GET_APP_INFO_FILTER";

    public static final String TAG = StrapKitSplashActivity.class.getSimpleName();

    private Boolean mIsReadyToClose = false;
    private Boolean mTimeHasPassed = false;
    private Handler mHandler = new Handler();
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ((StrapKitApplication) getApplication()).setHasShownSplash(false);

        mGoogleApiClient = new GoogleApiClient.Builder(this.getApplicationContext())
                .addApi(Wearable.API)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        Intent intent = new Intent(this, StrapKitWearListener.class);
        startService(intent);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Setting has time passed");
                mTimeHasPassed = true;
                closeSplashActivity();
            }
        }, 1500);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent intent = new Intent();
        intent.setAction(GET_APP_INFO_FILTER);
        sendStickyBroadcast(intent);

        IntentFilter filter = new IntentFilter();
        filter.addAction(READY_TO_CLOSE_FILTER);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Setting ready to close");
            mIsReadyToClose = true;
            closeSplashActivity();
        }
    };

    private void closeSplashActivity() {
        if (mIsReadyToClose && mTimeHasPassed) {
            Log.d(TAG, "Close splash Activity");
            ((StrapKitApplication) getApplication()).finishedSplashPage();
            finish();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {
            connectionResult.startResolutionForResult(this, 1);
        } catch (Exception e) {
            Log.d(TAG, "failed");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
