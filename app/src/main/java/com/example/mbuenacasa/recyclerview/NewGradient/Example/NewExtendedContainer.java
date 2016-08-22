package com.example.mbuenacasa.recyclerview.NewGradient.Example;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.mbuenacasa.recyclerview.NewGradient.GradientContainerView;
import com.example.mbuenacasa.recyclerview.R;

/**
 * Created by mbuenacasa on 22/08/16.
 */
public class NewExtendedContainer extends GradientContainerView {

    public NewExtendedContainer(Context context) {
        super(context);
        setGradientColor(context.getResources().getColor(R.color.red),context.getResources().getColor(R.color.green));
    }

    public NewExtendedContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGradientColor(context.getResources().getColor(R.color.red),context.getResources().getColor(R.color.green));
    }

    public NewExtendedContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGradientColor(context.getResources().getColor(R.color.red),context.getResources().getColor(R.color.green));
    }

    @Override
    public void changeColor(int color) {
        ((TextView)findViewById(R.id.asdf)).setTextColor(color);
    }
}
