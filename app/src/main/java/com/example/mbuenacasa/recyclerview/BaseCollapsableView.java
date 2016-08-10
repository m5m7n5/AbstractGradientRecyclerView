package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

/**
 * Created by mbuenacasa on 10/08/16.
 */
public class BaseCollapsableView extends RelativeLayout {

    private Context mContext;
    private ViewGroup mContainer;
    private View mAllwaysVisibleView;
    private View mCollapsableView;
    private boolean mCollapsed;

    public BaseCollapsableView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BaseCollapsableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public BaseCollapsableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

    }

    /**
     * For correct working of this method, you should have only 2 nested views in your layout resource
     *
     * @param res
     */
    protected void inflate(@LayoutRes int res) {
        View v = LayoutInflater.from(mContext).inflate(res, this, false);
        mContainer = (ViewGroup) v;
        mAllwaysVisibleView = mContainer.getChildAt(0);
        mCollapsableView = mContainer.getChildAt(1);
        if (mContainer.getChildCount() != 2) {
            throw new IllegalArgumentException("The BaseCollapsableView xlm needs to have only 2 views"
                    + "in the first level");
        }
        addView(v);
        mAllwaysVisibleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCollapsed) {
                    expand(mCollapsableView);
                    mCollapsed = false;
                }else{
                    collapse(mCollapsableView);
                    mCollapsed = true;
                }
            }
        });
        collapse(mCollapsableView);
        mCollapsed = true;
    }


    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        long duration = (int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density);
        a.setDuration(10*duration);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        long duration = (int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density);
        a.setDuration(10*duration);
        v.startAnimation(a);
    }


}
