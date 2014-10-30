package com.straphq.strapkit_lib.ui;

import java.io.Serializable;

/**
 * Created by jonahback on 9/29/14.
 */
public class StrapkitView implements Serializable {


    private String id;
    private String title;
    private StrapkitView child;
    private int type;

    public StrapkitView() {

    }

    public StrapkitView(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChild(StrapkitView child) {
        this.child = child;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }



}
