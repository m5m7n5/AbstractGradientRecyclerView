package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mbuenacasa on 1/07/16.
 */
public abstract class MyCustomRecyclerView{

    private CustomViewHolder ViewHolder;
    private RecyclerView recyclerView;
    private int centerColor;
    private int sideColor;
    private RecyclerView.Adapter adapter;


    protected void initCustomRecyclerView(@NonNull RecyclerView rv,
                                @NonNull RecyclerView.Adapter adapter,
                                @NonNull Context context,
                                int orientation,
                                int centerColor,
                                int sideColor){

        recyclerView = rv;
        this.centerColor = centerColor;
        this.sideColor = sideColor;
        this.adapter = adapter;
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(orientation);
        recyclerView.setLayoutManager(llm);
        settingCustomListeners();

    }

    private void settingCustomListeners(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int length = l.getChildCount();
                for (int i = 1; i < length - 1; i++) {
                    l.getChildAt(i).setAlpha(1);
                }
                int mediumval = length / 2;
                Rect first = new Rect();
                Rect last = new Rect();
                Rect medium = new Rect();
                l.getChildAt(0).getGlobalVisibleRect(first);
                l.getChildAt(length - 1).getGlobalVisibleRect(last);
                l.getChildAt(mediumval).getGlobalVisibleRect(medium);
                float alfavalue = (float) (first.right - first.left) / (medium.right - medium.left);
                l.getChildAt(0).setAlpha(alfavalue * alfavalue);
                alfavalue = (float) (last.right - last.left) / (medium.right - medium.left);
                l.getChildAt(length - 1).setAlpha(alfavalue * alfavalue);

                if (mediumval+1<l.getChildCount()){
                    if (dx >= 0) {
                        double getChangeValue = Math.abs(getDistanceFromCenter(l, mediumval - 1) / (double) getWidthOfView(l));
                        changeColorFromView(l.getChildAt(mediumval - 1), generateGradientColor(centerColor, sideColor, (float) getChangeValue));
                        getChangeValue = Math.abs(getDistanceFromCenter(l, mediumval) / (double) getWidthOfView(l));
                        changeColorFromView(l.getChildAt(mediumval), generateGradientColor(centerColor, sideColor, (float) getChangeValue));
                        getChangeValue = Math.abs(getDistanceFromCenter(l, mediumval + 1) / (double) getWidthOfView(l));
                        changeColorFromView(l.getChildAt(mediumval + 1), generateGradientColor(centerColor, sideColor, (float) getChangeValue));//(pink*alfavalue) para un efecto arcoiris
                    } else {
                        double getChangeValue = Math.abs(getDistanceFromCenter(l, mediumval - 1) / (double) getWidthOfView(l));
                        changeColorFromView(l.getChildAt(mediumval - 1), generateGradientColor(centerColor, sideColor, (float) getChangeValue));
                        getChangeValue = Math.abs(getDistanceFromCenter(l, mediumval) / (double) getWidthOfView(l));
                        changeColorFromView(l.getChildAt(mediumval), generateGradientColor(centerColor, sideColor, (float) getChangeValue));//(pink*alfavalue) para un efecto arcoiris
                        getChangeValue = Math.abs(getDistanceFromCenter(l, mediumval + 1) / (double) getWidthOfView(l));
                        changeColorFromView(l.getChildAt(mediumval + 1), generateGradientColor(centerColor, sideColor, (float) getChangeValue));
                    }
                }


            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int offset = getDistanceFromCenter(l,l.getChildCount()/2);
                    recyclerView.smoothScrollBy(offset,0);
                    //recyclerView.scrollBy(offset, 0);
                    for(int i=0;i<l.getChildCount();i++){
                        changeColorFromView(l.getChildAt(i),sideColor);
                    }
                    //TODO VER PORQUE CAMBIA MAL DE COLOR
                    changeColorFromView(l.getChildAt(l.getChildCount()/2),centerColor);
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                final int action = MotionEventCompat.getActionMasked(e);
                if(action==MotionEvent.ACTION_UP || action==MotionEvent.ACTION_CANCEL){
                    e.getAction();
                    LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                    lm.getChildCount();
                    int medium = lm.getChildCount()/2;
                    Rect auxiliarRectangle = new Rect();
                    int [] center = getAbsoluteCenter(lm);
                    int previous = medium-1;
                    Rect previousRect = new Rect();
                    lm.getChildAt(previous).getGlobalVisibleRect(previousRect);
                    for(int i=(medium);i<medium+2;i++){
                        lm.getChildAt(i).getGlobalVisibleRect(auxiliarRectangle);
                        if(Math.abs(auxiliarRectangle.centerX()-center[0])<=Math.abs(previousRect.centerX()-center[0])){
                            previous = i;
                            lm.getChildAt(previous).getGlobalVisibleRect(previousRect);
                        }
                    }
                    int offset = getDistanceFromCenter(lm,previous);
                    //rv.scrollBy(offset,0);
                    rv.smoothScrollBy(offset,0);
                    previous = lm.getChildCount()/2;
                    //TODO VER PORQUE CAMBIA MAL DE COLOR
                    changeColorFromView(lm.getChildAt(previous),centerColor);
                    return false;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    //Métodos para cambiar los colores en funcion de las distancias al centro

    protected abstract void changeColorFromView(View v, int c);


    private int [] getAbsoluteCenter(LinearLayoutManager lm){
        int width = lm.getWidth();
        int height = lm.getHeight();
        int [] values = {0,0};
        Rect first = new Rect();
        Rect last = new Rect();
        lm.getChildAt(0).getGlobalVisibleRect(first);
        lm.getChildAt(lm.getChildCount()-1).getGlobalVisibleRect(last);
        values[0] = first.left+width/2;
        values[1] = last.top+height/2;
        return values;
    }

    private int generateGradientColor(int fromColor,int toColor, float variation){
        if(variation>=1){
            return toColor;
        }
        //Calculo las componentes de cada color para calcular la tasa de cambio total
        int R0 = (fromColor & 0xFF0000)>>16;
        int G0 = (fromColor & 0x00FF00)>>8;
        int B0 = (fromColor & 0x0000FF)>>0;
        int R1 = (toColor & 0xFF0000)>>16;
        int G1 = (toColor & 0x00FF00)>>8;
        int B1 = (toColor & 0x0000FF)>>0;


        int totalChangeR = interpolate(R0,R1,variation);
        int totalChangeG = interpolate(G0,G1,variation);
        int totalChangeB = interpolate(B0,B1,variation);

        int auxVar = (((totalChangeR << 8) | totalChangeG ) << 8 ) | totalChangeB;
        return fromColor + auxVar;

    }

    private int interpolate(int first,int second,float changeRate){
        if(first<second) {
            return (int) ((second - first) * changeRate);
        }else{
            return (int) ((first-second) * (1-changeRate));
        }
    }

    private int getDistanceFromCenter(LinearLayoutManager lm,int index){
        if(lm.getOrientation() == LinearLayoutManager.HORIZONTAL){
            Rect aux = new Rect();
            lm.getChildAt(index).getGlobalVisibleRect(aux);
            int [] center = getAbsoluteCenter(lm);
            return aux.centerX()-center[0];
        }else{
            Rect aux = new Rect();
            lm.getChildAt(index).getGlobalVisibleRect(aux);
            int [] center = getAbsoluteCenter(lm);
            return aux.centerY()-center[1];
        }

    }

    private int getWidthOfView(LinearLayoutManager lm){
        Rect r = new Rect();
        lm.getChildAt(lm.getChildCount()/2).getGlobalVisibleRect(r);
        return r.right-r.left;
    }

    public static abstract class MyCustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder>{
        public abstract void changeColorFromView(View v,int color);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        public CustomViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class CustomLinearLayoutManager extends LinearLayoutManager {

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position)
        {



        }

    }

}
