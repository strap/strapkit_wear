package com.straphq.androidwearsampleprojectreal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
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
    }

    public void onTouch(String viewID) {
        PutDataMapRequest dataMap = PutDataMapRequest.create("/views/" + viewID + "/onTouch");
        dataMap.getDataMap().putString("date", new Date().toString());
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(mGoogleApiClient, request);
    }

    public void onDataChanged(DataEventBuffer buffer) {
        for (DataEvent event : buffer) {
            DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
            DataMap map = dataMapItem.getDataMap();
            if(event.getDataItem().getUri().getPathSegments().get(0).equals("views")) {
                StrapkitView v = new StrapkitView(event.getDataItem().getUri().getPathSegments().get(1));
                v.setTitle(map.getString("title"));

                activity.updateView(v);
            }
        }
    }
}
