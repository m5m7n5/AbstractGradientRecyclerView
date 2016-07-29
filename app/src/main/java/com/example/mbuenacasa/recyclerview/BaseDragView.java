package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by mbuenacasa on 29/07/16.
 */
public class BaseDragView extends RelativeLayout {

    private ViewDragHelper mDragHelper;
    private Context mContext;
    private View mUpperView;
    private View mBackView;
    private ViewGroup container;

    public BaseDragView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BaseDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public BaseDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

    }

    protected void inflate(@LayoutRes int res) {
        LayoutInflater.from(mContext).inflate(res, this, true);
        container = (ViewGroup) getChildAt(0);
        mBackView = container.getChildAt(0);
        mUpperView = container.getChildAt(1);
        container.getChildCount();

        initHelper();
    }

    private void initHelper(){
        CallBackHelper cb = new CallBackHelper();
        cb.enableHorizontalDrag();
        cb.setDragDirection(CallBackHelper.RIGHT_LEFT_DRAG);
        mDragHelper = ViewDragHelper.create((ViewGroup) this.getChildAt(0),cb);
    }


    private class CallBackHelper extends ViewDragHelper.Callback {

        private boolean canDragHorizontal = false;
        private boolean canDragVertical = false;

        private int dragDirection;

        public static final int LEFT_RIGHT_DRAG = 0x01;
        public static final int RIGHT_LEFT_DRAG = 0x02;
        public static final int TOP_BOTTOM_DRAG = 0x04;
        public static final int BOTTOM_TOP_DRAG = 0x08;

        public void setDragDirection(int dir){
            dragDirection = dir;
        }

        public void enableVerticalDrag(){
            disableDrag();
            canDragVertical = true;
        }

        public void enableHorizontalDrag(){
            disableDrag();
            canDragHorizontal = true;
        }

        public void disableDrag(){
            canDragVertical = false;
            canDragHorizontal = false;
        }




        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if(canDragHorizontal){
                if(dragDirection == LEFT_RIGHT_DRAG ){
                    int leftBound = mBackView.getWidth();
                    int rightBound = getWidth() - mUpperView.getWidth();
                    int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                    return newLeft;
                }else if(dragDirection == RIGHT_LEFT_DRAG){
                    int leftBound = -mBackView.getWidth();
                    int rightBound = getWidth() - mUpperView.getWidth();
                    int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                    return newLeft;
                }
            }
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if(canDragVertical){
                if(dragDirection == TOP_BOTTOM_DRAG){
                    int leftBound = mBackView.getHeight();
                    int rightBound = getWidth() - mUpperView.getWidth();
                    int newLeft = Math.min(Math.max(top, leftBound), rightBound);
                    return newLeft;
                }else if(dragDirection == BOTTOM_TOP_DRAG){
                    int leftBound = -mBackView.getHeight();
                    int rightBound = getWidth() - mUpperView.getWidth();
                    int newLeft = Math.min(Math.max(top, leftBound), rightBound);
                    return newLeft;
                }
            }
            return 0;
        }

        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return super.getViewHorizontalDragRange(child);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return super.getViewVerticalDragRange(child);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mUpperView;
        }

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
        if(mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
