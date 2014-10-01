package com.straphq.androidwearsampleprojectreal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.wearable.*;
import com.google.android.gms.wearable.DataApi;

import android.util.Log;
import android.webkit.WebView;


public class MyActivity extends Activity {

    public GoogleApiClient mGoogleApiClient = null;
    private StrapKit strapKit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d("TAG", "onConnected: " + connectionHint);

                        WebView jsEnv = new WebView(getApplicationContext());

                        jsEnv.getSettings().setJavaScriptEnabled(true);

                        jsEnv.addJavascriptInterface(strapKit, "strapkit_bridge");
                        strapKit.mWebView = jsEnv;
                        String test = "<html><script>window.strapkit_bridge.setView('js on phone', 'web');</script></html>";
                        String html = "<html><script src=\"file:///android_asset/app.js></script><body></body></html>";
                        //jsEnv.loadData(test, "text/html", "utf-8");

                        StrapKit_Test tester = new StrapKit_Test(strapKit);
                        tester.basicView();

                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d("", "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d("", "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
        strapKit = new StrapKit(getApplicationContext(), mGoogleApiClient);
        mGoogleApiClient.connect();
        Wearable.DataApi.addListener(mGoogleApiClient, strapKit);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
