package com.straphq.strapkit.framework.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CardFrame;
import android.util.Log;
import android.view.View;

import com.straphq.strapkit.framework.R;
import com.straphq.strapkit.framework.StrapKitBaseActivity;

/**
 * Created by martinza on 1/6/15.
 */
public class StrapKitCardFragment extends StrapKitBaseView {

    private static final String TAG = StrapKitCardFragment.class.getSimpleName();

    private String title;
    private String body;
    public String onClick;
    private String icon;

    //private CardFragment mCardFragment;

    @Override
    public void initialize(final StrapKitBaseActivity activity) {
        CardFragment mCardFragment = null;
        if (mCardFragment == null) {
            FragmentManager fragmentManager = activity.getFragmentManager();

            mCardFragment = CardFragment.create(title, body);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, mCardFragment);
            fragmentTransaction.commit();

            View view = activity.findViewById(R.id.frame_layout);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickFunction(activity, onClick);
                }
            });
        }
    }
}
