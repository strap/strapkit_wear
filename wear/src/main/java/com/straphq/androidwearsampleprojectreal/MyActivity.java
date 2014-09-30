package com.straphq.androidwearsampleprojectreal;

import android.app.Activity;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.*;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.common.api.*;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.straphq.wear_sdk.Strap;

import org.json.JSONException;

import java.io.IOException;
import java.util.Date;


public class MyActivity extends Activity implements View.OnTouchListener, StrapkitActivity {

//  String strapAppID = this.getString(R.string.strap_app_id);
    String strapAppID = "";
    private static Strap strap = null;
    private GoogleApiClient mGoogleApiClient;
    private StrapkitBridge bridge;

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final StrapkitActivity listener = this;


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

                mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                        .addConnectionCallbacks(new ConnectionCallbacks() {
                            @Override
                            public void onConnected(Bundle connectionHint) {
                                Log.d("TAG", "onConnected: " + connectionHint);
                                //strap.logEvent("/app/started");
                                Wearable.DataApi.addListener(mGoogleApiClient, bridge);
                            }
                            @Override
                            public void onConnectionSuspended(int cause) {
                                Log.d("TAG", "onConnectionSuspended: " + cause);
                            }
                        })
                        .addOnConnectionFailedListener(new OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.d("TAG", "onConnectionFailed: " + result);
                            }
                        })
                        .addApi(Wearable.API)
                        .build();

                bridge = new StrapkitBridge(mGoogleApiClient, listener);



                StrapkitView v = new StrapkitView("blar");
                v.setChild(null);
                v.setTitle("W--t. Strapkit.");
                updateView(v);
            }

        });
    }

    public void updateView(final StrapkitView v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout layout = new LinearLayout(getApplicationContext());
                TextView textView = new TextView(getApplicationContext());
                textView.setText(v.getTitle());
                textView.setTag(v.getId());


                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT,
                        LinearLayout.LayoutParams.FILL_PARENT));
                layout.addView(textView);
                setContentView(layout);
            }
        });
    }

    public boolean onTouch(View v, MotionEvent e) {
        String viewID = (String)v.getTag();
        bridge.onTouch(viewID);
        return true;
    }

}
