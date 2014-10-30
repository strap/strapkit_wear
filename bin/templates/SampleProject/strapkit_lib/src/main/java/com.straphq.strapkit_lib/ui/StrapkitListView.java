package com.straphq.strapkit_lib.ui;


import com.google.android.gms.wearable.*;
import com.straphq.strapkit_lib.ui.*;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jonahback on 10/1/14.
 */
public class StrapkitListView extends com.straphq.strapkit_lib.ui.StrapkitView implements Serializable {

    private ArrayList<String> listItems;

    public StrapkitListView(String viewID) {
        super(viewID);
    }

    public void setListItems(ArrayList<String> listItems) {
        this.listItems = listItems;
    }
    public ArrayList<String> getListItems() {
        return listItems;
    }

}
