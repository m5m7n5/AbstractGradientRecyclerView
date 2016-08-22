package com.example.mbuenacasa.recyclerview.NewGradient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by mbuenacasa on 22/08/16.
 */
public abstract class GradientContainerView extends RelativeLayout {

    protected int centerColor;
    protected int sideColor;

    public GradientContainerView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public GradientContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public GradientContainerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    public void setGradientColor(int centerColor, int sideColor){
        this.centerColor = centerColor;
        this.sideColor = sideColor;
    }

    public abstract void changeColor(int color);



    @Override
    protected void onDraw(Canvas canvas) {
        Rect childRect = new Rect();
        this.getGlobalVisibleRect(childRect);
        Rect parentRect = new Rect();
        RecyclerView parent = (RecyclerView) this.getParent();
        parent.getGlobalVisibleRect(parentRect);
        if(((LinearLayoutManager)parent.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL){
            int parentCenterX = parentRect.centerX();
            int childCenterX = childRect.centerX();
            int childRelativePosition = childCenterX - parentCenterX;
            int childWidth = childRect.right - childRect.left;
            if((childWidth/2)>0){
                double childRelativePositionScaled = childRelativePosition/(childWidth/(double)2);
                int color = generateGradientColor(centerColor,sideColor,(float)(childRelativePositionScaled*childRelativePositionScaled));
                changeColor(color);
            }
            setAlpha(childWidth/(float)getMeasuredWidth());
        }else{
            int parentCenterY = parentRect.centerY();
            int childCenterY = childRect.centerY();
            int childRelativePosition = childCenterY - parentCenterY;
            int childHeight = childRect.bottom - childRect.top;
            if((childHeight/2)>0){
                double childRelativePositionScaled = childRelativePosition/(childHeight/(double)2);
                int color = generateGradientColor(centerColor,sideColor,(float)(childRelativePositionScaled*childRelativePositionScaled));
                changeColor(color);
            }
            setAlpha(childHeight/(float)getMeasuredHeight());

        }

        super.onDraw(canvas);
    }

    private int generateGradientColor(int fromColor, int toColor, float variation) {

        if (variation >= 1) {
            return toColor;
        }
        //Split of the components into A R G B variables for both colors
        int A0 = (fromColor & 0xFF000000) >> 24;
        int R0 = (fromColor & 0x00FF0000) >> 16;
        int G0 = (fromColor & 0x0000FF00) >> 8;
        int B0 = fromColor & 0x000000FF;
        int A1 = (toColor & 0xFF000000) >> 24;
        int R1 = (toColor & 0x00FF0000) >> 16;
        int G1 = (toColor & 0x0000FF00) >> 8;
        int B1 = toColor & 0x000000FF;

        //Getting the color values for the target gradient color
        int totalChangeA = interpolate(A0, A1, variation);
        int totalChangeR = interpolate(R0, R1, variation);
        int totalChangeG = interpolate(G0, G1, variation);
        int totalChangeB = interpolate(B0, B1, variation);

        int auxVar = (((totalChangeR << 8) | totalChangeG) << 8) | totalChangeB;
        return auxVar | (totalChangeA << 24);

    }

    private int interpolate(int first, int second, float changeRate) {

        if (first < second) {
            return (int) ((second - first) * changeRate) + first;
        } else {
            return (int) ((first - second) * (1 - changeRate)) + second;
        }

    }

}
