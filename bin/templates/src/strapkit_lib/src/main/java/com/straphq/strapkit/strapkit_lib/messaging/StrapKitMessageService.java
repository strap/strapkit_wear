package com.straphq.strapkit.strapkit_lib.messaging;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.straphq.strapkit.strapkit_lib.util.StrapKitExecData;
import com.straphq.strapkit.strapkit_lib.util.StrapKitJsInterface;

import java.util.List;

public class StrapKitMessageService extends Service implements DataApi.DataListener, MessageApi.MessageListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = StrapKitMessageService.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    private StrapKitJsInterface mJsInterface;

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();

    private Handler mHandler = new Handler();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        StrapKitMessageService getService() {
            // Return this instance of LocalService so clients can call public methods
            return StrapKitMessageService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //init();
        init();
        Log.d(TAG, "Successfully created service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //init();
        Log.d(TAG, "Initialized and started service");
        return START_STICKY;
    }

    private void init() {

        Log.d(TAG, "Initializing google api client");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Log.d(TAG, "Connecting to google api");
        mGoogleApiClient.connect();

    }

    private void initializeJs() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mJsInterface = new StrapKitJsInterface(StrapKitMessageService.this);
                mJsInterface.startWebView();

                sendMessage("/start_app", "");
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Handle Google Api Client connected
        Log.d(TAG, "Successfully connected");
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int suspendedReason) {
        // Handle a connection suspended event
        Log.d(TAG, "Connection suspended: " + suspendedReason);
    }

    /*
    onDataChanged: will be used for getting sensor data from Android Wear if developer requests
     */
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {

        for (DataEvent event : dataEvents) {
            String path = event.getDataItem().getUri().getPathSegments().get(0);
            Log.d(TAG, "uri: " + path);
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            DataMap map = dataMapItem.getDataMap();

            switch (path){
                case "callback":
                    mJsInterface.sendCallback(map);
                    break;
                default:
                    break;
            }


        }
        // must call release() here to prevent dataEvents from growing too large.
        dataEvents.release();
    }

    public void sendData(String path, StrapKitExecData execData) {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(path);
        execData.setDataMap(dataMapRequest.getDataMap());
        PutDataRequest request = dataMapRequest.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request);
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equals("/start_app")) {
            initializeJs();
        }
    }

    public void sendMessage(final String path, final String message) {
        Log.d(TAG, "Sending message: " + path + ", " + message);
        Log.d(TAG, "Google Api Client: " + mGoogleApiClient.isConnected());
        Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodesResult) {
                List<Node> nodes = nodesResult.getNodes();
                Log.d(TAG, "node size: " + nodes.size());
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
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "OnConnectionFailed: " + connectionResult.getErrorCode());
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // The Android Wear app is not installed... we need to alert the app
            Log.d(TAG, "Wear api is not available");
        }
    }
}
