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
public abstract class AbstractDegradeRecyclerView {

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
                /*
                Para el correcto funcionamiento de este método supongo que en la vista siempre se ve un elemento completo,
                es decir, que por lo menos hay un elemento entero en el layout.
                 */
                //TODO Repasar el algoritmo para que coja maximos y minimos correctamente y hacer que se calcule en vertical tambien
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int length = l.getChildCount();
                int firstMaxIndex = 0;
                firstMaxIndex = nearesView(l);
                for (int i = 0; i < length - 1; i++) {
                    if(i!=firstMaxIndex) {
                        changeColorFromView(l.getChildAt(i), sideColor);
                    }
                    l.getChildAt(i).setAlpha(1);
                }
                /*
                Aplico la degradación de color a los 2 elementos mas cercanos al centro, para ello calculo el máximo y un segundo máximo,
                como en pantalla no habrán muchos objetos, éste cálculo no sera muy costoso.
                 */

                int secondMaxIndex = 0;
                Rect auxRectangle = new Rect();
                Rect secondMaxRectangle = new Rect();
                l.getChildAt(secondMaxIndex).getGlobalVisibleRect(secondMaxRectangle);

                for(int i=0;i<length;i++){
                    l.getChildAt(i).getGlobalVisibleRect(auxRectangle);
                    if(Math.abs(getDistanceFromCenter(l,i))<=Math.abs(getDistanceFromCenter(l,secondMaxIndex)) && i!=firstMaxIndex){
                        secondMaxIndex = i;
                        l.getChildAt(secondMaxIndex).getGlobalVisibleRect(secondMaxRectangle);
                    }
                }
                //TODO FIXIT FALLA MUY BESTIA
                double getChangeValue = Math.abs(getDistanceFromCenter(l, firstMaxIndex) / (double) getWidthOfView(l));
                changeColorFromView(l.getChildAt(firstMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                getChangeValue = Math.abs(getDistanceFromCenter(l, secondMaxIndex) / (double) getWidthOfView(l));
                changeColorFromView(l.getChildAt(secondMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                /*
                Aplico el degradado al primero y al último elemento visible
                Utilizando el elemento mas cercano al centro calculo el valor alfa de cambio y lo aplico a los extremos
                 */
                int firstAlphaIndex = 0;
                int lastAlphaIndex  = length-1;
                Rect firstAlphaRect = new Rect();
                Rect lastAlphaRect = new Rect();
                Rect forSize = new Rect();
                l.getChildAt(firstAlphaIndex).getGlobalVisibleRect(firstAlphaRect);
                l.getChildAt(lastAlphaIndex).getGlobalVisibleRect(lastAlphaRect);
                l.getChildAt(firstMaxIndex).getGlobalVisibleRect(forSize);

                if(l.getOrientation()==LinearLayoutManager.HORIZONTAL){
                    float alfavalue = (float) (firstAlphaRect.right - firstAlphaRect.left) /  (forSize.right - forSize.left);
                    l.getChildAt(0).setAlpha(alfavalue * alfavalue);
                    alfavalue = (float) (lastAlphaRect.right - lastAlphaRect.left) / (forSize.right - forSize.left);
                    l.getChildAt(length - 1).setAlpha(alfavalue * alfavalue);
                }else{
                    float alfavalue = (float) (firstAlphaRect.top - firstAlphaRect.bottom) / (forSize.top - forSize.bottom);
                    l.getChildAt(0).setAlpha(alfavalue * alfavalue);
                    alfavalue = (float) (lastAlphaRect.top - lastAlphaRect.bottom) / (forSize.top - forSize.bottom);
                    l.getChildAt(length - 1).setAlpha(alfavalue * alfavalue);
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int nearest = nearesView(l);
                    int offset = getDistanceFromCenter(l,nearest);
                    if(l.getOrientation() == LinearLayoutManager.HORIZONTAL){
                        recyclerView.smoothScrollBy(offset,0);
                    }else{
                        recyclerView.smoothScrollBy(0,offset);
                    }
                    for(int i=0;i<l.getChildCount();i++){
                        if(i!=nearest) {
                            changeColorFromView(l.getChildAt(i), sideColor);
                        }
                    }
                    //TODO VER PORQUE CAMBIA MAL DE COLOR
                    changeColorFromView(l.getChildAt(nearest),centerColor);
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
                    int previous = nearesView(lm);
                    int offset = getDistanceFromCenter(lm,previous);
                    if(lm.getOrientation() == LinearLayoutManager.HORIZONTAL){
                        rv.smoothScrollBy(offset,0);
                    }else{
                        rv.smoothScrollBy(0,offset);
                    }
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
        lm.getChildAt(0).getGlobalVisibleRect(first);
        values[0] = first.left+width/2;
        values[1] = first.top+height/2;
        return values;
    }

    private int nearesView(LinearLayoutManager l){

        int firstMaxIndex = 0;
        Rect auxRectangle = new Rect();
        Rect firstMaxRectangle = new Rect();
        l.getChildAt(firstMaxIndex).getGlobalVisibleRect(firstMaxRectangle);

        for(int i=0;i<l.getChildCount();i++){
            l.getChildAt(i).getGlobalVisibleRect(auxRectangle);
            if(Math.abs(getDistanceFromCenter(l,i))<=Math.abs(getDistanceFromCenter(l,firstMaxIndex))){
                firstMaxIndex = i;
                l.getChildAt(firstMaxIndex).getGlobalVisibleRect(firstMaxRectangle);
            }
        }
        return firstMaxIndex;
    }

    private int generateGradientColor(int fromColor,int toColor, float variation){
        if(variation>=1){
            return toColor;
        }
        //Calculo las componentes de cada color para calcular la tasa de cambio total
        int A0 = (fromColor & 0xFF000000)>>24;
        int R0 = (fromColor & 0x00FF0000)>>16;
        int G0 = (fromColor & 0x0000FF00)>>8;
        int B0 = (fromColor & 0x000000FF)>>0;
        int A1 = (toColor & 0xFF000000)>>24;
        int R1 = (toColor & 0x00FF0000)>>16;
        int G1 = (toColor & 0x0000FF00)>>8;
        int B1 = (toColor & 0x000000FF)>>0;


        int totalChangeA = interpolate(A0,A1,variation);
        int totalChangeR = interpolate(R0,R1,variation);
        int totalChangeG = interpolate(G0,G1,variation);
        int totalChangeB = interpolate(B0,B1,variation);

        int auxVar = (((totalChangeR << 8) | totalChangeG ) << 8 ) | totalChangeB;
        return auxVar | (totalChangeA<<24);

    }

    private int interpolate(int first,int second,float changeRate){
        if(first<second) {
            return (int) ((second - first) * changeRate) + first;
        }else{
            return (int) ((first-second) * (1-changeRate)) + second;
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
        if(lm.getOrientation() == LinearLayoutManager.HORIZONTAL) {
            Rect r = new Rect();
            lm.getChildAt(lm.getChildCount() / 2).getGlobalVisibleRect(r);
            return r.right - r.left;
        }else{
            Rect r = new Rect();
            lm.getChildAt(lm.getChildCount() / 2).getGlobalVisibleRect(r);
            return r.bottom - r.top;
        }
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
