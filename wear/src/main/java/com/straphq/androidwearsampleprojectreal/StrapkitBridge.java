package com.straphq.androidwearsampleprojectreal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

/**
 * Created by jonahback on 9/29/14.
 */
public class StrapkitBridge implements DataApi.DataListener{

    private GoogleApiClient mGoogleApiClient;
    private StrapkitActivity activity;

    public StrapkitBridge() {

    }

    public StrapkitBridge(GoogleApiClient apiClient, StrapkitActivity activity) {
        mGoogleApiClient = apiClient;
        this.activity = activity;
        mGoogleApiClient.connect();
        Wearable.DataApi.addListener(mGoogleApiClient, this);

        initializeView();
    }

    private void initializeView() {
        PutDataMapRequest dataMapRequest = PutDataMapRequest.create("/strapkitInit");
        dataMapRequest.getDataMap().putString("date", new Date().toString());
    }

    public void onTouch(String viewID) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/onTouch");
        dataMap.getDataMap().putString("id", viewID);
        dataMap.getDataMap().putString("date", new Date().toString());
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);

        return;
    }

    public void onDataChanged(DataEventBuffer buffer) {
        for (DataEvent event : buffer) {
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            DataMap map = dataMapItem.getDataMap();
            if(event.getDataItem().getUri().getPathSegments().get(0).equals("views")) {


                StrapkitView v = new StrapkitView(map);

                if(v.getType() == 2) {
                    v = new StrapkitListView(map);
                }

                activity.updateView(v);
            }
        }
    }
}
