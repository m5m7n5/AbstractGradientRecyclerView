package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by mbuenacasa on 13/07/16.
 */
public abstract class StringRecyclerView extends AbstractGradientRecyclerView2 {

    public StringRecyclerView(Context context) {
        super(context);
    }

    public StringRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StringRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



}
