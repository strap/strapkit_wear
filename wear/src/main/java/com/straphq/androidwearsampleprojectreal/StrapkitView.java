package com.straphq.androidwearsampleprojectreal;

/**
 * Created by jonahback on 9/29/14.
 */
public class StrapkitView {


    private String id;
    private String title;
    private StrapkitView child;

    public StrapkitView(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChild(StrapkitView child) {
        this.child = child;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }



}
