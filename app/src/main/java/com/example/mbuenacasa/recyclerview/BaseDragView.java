package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by mbuenacasa on 29/07/16.
 */
public class BaseDragView extends RelativeLayout {

    private ViewDragHelper mHelper;
    private Context mContext;

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
        LayoutInflater.from(mContext).inflate(res,this,true);
        getChildCount();
    }

}
