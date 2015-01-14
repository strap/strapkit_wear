package com.straphq.strapkit.framework.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.straphq.strapkit.framework.R;

/**
 * Created by martinza on 1/10/15.
 */
public class StrapKitListItemView extends LinearLayout
        implements WearableListView.OnCenterProximityListener {

    private TextView mName;
    private ImageView mBaseIcon;

    private final float mFadedTextAlpha;
    private final int mFadedCircleColor;
    private final int mChosenCircleColor;

    public StrapKitListItemView(Context context) {
        this(context, null);
    }

    public StrapKitListItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrapKitListItemView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);

        mFadedTextAlpha = 50 / 100f;
        mFadedCircleColor = getResources().getColor(R.color.grey);
        mChosenCircleColor = getResources().getColor(R.color.blue);
    }

    // Get references to the icon and text in the item layout definition
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // These are defined in the layout file for list items
        // (see next section)
        mName = (TextView) findViewById(R.id.base_text);
        mBaseIcon = (ImageView) findViewById(R.id.base_icon);
    }

    @Override
    public void onCenterPosition(boolean animate) {
        mName.setAlpha(1f);
        ((GradientDrawable) mBaseIcon.getDrawable()).setColor(mChosenCircleColor);
    }

    @Override
    public void onNonCenterPosition(boolean animate) {
        mName.setAlpha(mFadedTextAlpha);
        ((GradientDrawable) mBaseIcon.getDrawable()).setColor(mFadedCircleColor);
    }
}
