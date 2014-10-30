package com.straphq.strapkit.ui;

import android.support.wearable.view.WearableListView;

import com.straphq.strapkit.StrapkitBridge;

/**
 * Created by jonahback on 10/10/14.
 */
public class StrapkitListListener implements WearableListView.ClickListener {
    private StrapkitBridge mBridge = null;
    private String mListID;

    public StrapkitListListener(String listID, StrapkitBridge bridge) {
        mBridge = bridge;
        mListID = listID;

    }

    public void onClick(WearableListView.ViewHolder holder) {
        mBridge.onSelect(mListID, holder.getPosition());
    }
    public void onTopEmptyRegionClick() {

    }
}
