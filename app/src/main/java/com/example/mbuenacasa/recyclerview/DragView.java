package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by mbuenacasa on 20/07/16.
 */
public class DragView extends RelativeLayout {

    private ViewDragHelper mDragHelper;
    private TextView mDragView;
    private TextView mButtonView;
    private OnClickListener buttonListener;

    public DragView(Context context) {
        super(context);
        init();
    }

    public DragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onFinishInflate() {
        mDragView = (TextView) findViewById(R.id.view_for_drag);
        mButtonView = (TextView) findViewById(R.id.view_for_drag_button);
        mButtonView.setOnClickListener(null);
        buttonListener = new OnClickListener() {
            @Override
            public void onClick(View view) {
                mButtonView.setBackgroundColor(0xFF00FF00);
            }
        };
        super.onFinishInflate();
    }

    private void init() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == mDragView;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                final int leftBound = -mButtonView.getWidth();
                final int rightBound = getWidth() - mDragView.getWidth();

                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

                return newLeft;

            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (xvel > 0) {
                    mDragHelper.settleCapturedViewAt(0, 0);
                    mButtonView.setOnClickListener(null);
                } else {
                    mDragHelper.settleCapturedViewAt(-mButtonView.getWidth(), 0);
                    mButtonView.setOnClickListener(buttonListener);
                }
                invalidate();
            }

        });

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }


}

