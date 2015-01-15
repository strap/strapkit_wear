package com.straphq.strapkit.framework;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.straphq.strapkit.framework.view.StrapKitBaseView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StrapKitBaseActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = StrapKitBaseActivity.class.getSimpleName();

    public static final String ARGS_VIEW_DEFINITIONS = "args_view_definitions";
    public static final String ARGS_BACKGROUND_COLOR = "args_background_color";

    private GoogleApiClient mGoogleApiClient;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Serializable serializable = getIntent().getSerializableExtra(ARGS_VIEW_DEFINITIONS);
        if (serializable == null) {
            finish();
        } else {
            ArrayList<StrapKitBaseView> views = (ArrayList<StrapKitBaseView>) serializable;
            for (StrapKitBaseView view : views) {
                view.initialize(this);
            }
        }

        String backgroundColor = getIntent().getStringExtra(ARGS_BACKGROUND_COLOR);
        if (backgroundColor != null) {
            View view = findViewById(R.id.base_view);
            view.setBackgroundColor(Color.parseColor(backgroundColor));
        }
    }

    public void sendMessage(final String path, final String message) {

        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodesResult) {
                List<Node> nodes = nodesResult.getNodes();
                for (Node node : nodes) {
                    Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, message.getBytes() ).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            Log.d(TAG, "Received message result" + sendMessageResult.getRequestId());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();

        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
