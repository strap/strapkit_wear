package com.straphq.androidwearsampleprojectreal;

import android.app.Activity;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
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
    private StrapkitActivity listener;
    private View.OnTouchListener onTouchListener;

    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        listener = this;
        onTouchListener = this;


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);

            }

        });

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



    }

    public void updateView(final StrapkitView v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout layout = new LinearLayout(getApplicationContext());
                if(v.getType() == 1) {

                    TextView textView = new TextView(getApplicationContext());
                    textView.setText(v.getTitle());
                    textView.setTag(v.getId());
                    textView.setOnTouchListener(onTouchListener);


                    textView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT));
                    layout.addView(textView);
                }

                if(v.getType() == 2) {
                    StrapkitListView list = (StrapkitListView) v;
                    WearableListView listView = new WearableListView(getApplicationContext());
                    listView.setGreedyTouchMode(true);
                    listView.setAdapter(new StrapkitListAdapter(getApplicationContext(),list));

                    listView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT));



                    layout.addView(listView);

                }

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
