package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by mbuenacasa on 1/07/16.
 */
public abstract class AbstractGradientRecyclerView {

    private int centerColor;
    private int sideColor;
    private CountDownTimer timer;
    protected int selectedViewIndex;
    protected RecyclerView recyclerView;
    protected View selectedView;

    protected abstract void whenSelected(View v);

    protected abstract void changeColorFromView(View v, int c);


    /**
     * Method that returns the current recyclerView
     * @return
     */
    public RecyclerView getRecyclerView(){
        return recyclerView;
    }


    /**
     * Method that returns the needed offset to do an scroll of one item
     * @param rv
     * @return
     */
    public static int getOffsetForOneMovement(RecyclerView rv){

        LinearLayoutManager l = (LinearLayoutManager) rv.getLayoutManager();
        int nearest = nearestView(l);
        Rect auxiliar = new Rect();
        l.getChildAt(nearest).getGlobalVisibleRect(auxiliar);
        if(l.getOrientation()==LinearLayoutManager.HORIZONTAL){
            return auxiliar.width();
        }else {
            return auxiliar.height();
        }
    }


    /**
     * Method that initializes the recyclerView passed as argument with the adapter, and other needed variables
     * such as orientation of the recyclerView and colors for the effects.
     * @param rv
     * @param adapter
     * @param context
     * @param orientation
     * @param centerColor
     * @param sideColor
     */
    protected void initCustomRecyclerView(@NonNull RecyclerView rv,
                                @NonNull AbstractGradientRecyclerAdapter adapter,
                                @NonNull Context context,
                                int orientation,
                                int centerColor,
                                int sideColor){

        recyclerView = rv;
        this.centerColor = centerColor;
        this.sideColor = sideColor;
        recyclerView.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(orientation);
        recyclerView.setLayoutManager(llm);
        settingCustomListeners();
        //Timer que ejecutará el método whenSelected(selectedView) cuando pase un tiempo determinado seleccionado.
        timer = new CountDownTimer(200,200) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                whenSelected(selectedView);
            }
        };

    }


    /**
     * Method to change the layoutManager of the current recyclerView
     * @param lm
     */
    protected void changeLayoutManager(RecyclerView.LayoutManager lm){

        recyclerView.setLayoutManager(lm);

    }


    /**
     * Method that iterates over the current visible views of the LinearLayoutManager and
     * returns which is the closest.
     * @param l
     * @return
     */
    protected static int nearestView(LinearLayoutManager l){

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


    /**
     * Method for initialize the scrollListener to do the effect.
     */
    private void settingCustomListeners(){

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                /*
                Para el correcto funcionamiento de este método supongo que en la vista siempre se ve un elemento completo,
                es decir, que por lo menos hay un elemento entero en el layout.
                 */
                super.onScrolled(recyclerView, dx, dy);
                timer.cancel();
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int length = l.getChildCount();
                int firstMaxIndex = 0;
                firstMaxIndex = nearestView(l);
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

                for(int i=0;i<length;i++){
                    if(Math.abs(getDistanceFromCenter(l,i))<=Math.abs(getDistanceFromCenter(l,secondMaxIndex)) && i!=firstMaxIndex){
                        secondMaxIndex = i;
                    }
                }

                if(l.getOrientation() == LinearLayoutManager.HORIZONTAL){
                    double getChangeValue = Math.abs(getDistanceFromCenter(l, firstMaxIndex) / (double) l.getChildAt(firstMaxIndex).getWidth());
                    changeColorFromView(l.getChildAt(firstMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                    getChangeValue = Math.abs(getDistanceFromCenter(l, secondMaxIndex) / (double) l.getChildAt(firstMaxIndex).getWidth());
                    changeColorFromView(l.getChildAt(secondMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                }else{
                    double getChangeValue = Math.abs(getDistanceFromCenter(l, firstMaxIndex) / (double) l.getChildAt(firstMaxIndex).getHeight());
                    changeColorFromView(l.getChildAt(firstMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                    getChangeValue = Math.abs(getDistanceFromCenter(l, secondMaxIndex) / (double) l.getChildAt(firstMaxIndex).getHeight());
                    changeColorFromView(l.getChildAt(secondMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                }


                /*
                Aplico el degradado al primero y al último elemento visible
                Utilizando el elemento mas cercano al centro calculo el valor alfa de cambio y lo aplico a los extremos
                 */
                applyAlpha(l);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    timer.start();
                    int nearest = nearestView(l);
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
                    changeColorFromView(l.getChildAt(nearest),centerColor);
                    selectedView = l.getChildAt(nearest);
                    selectedViewIndex = recyclerView.getChildAdapterPosition(l.getChildAt(nearest));;
                }
            }
        });

    }


    /**
     * Method that iterates over the current visible items and changes alpha values of the items,
     * depending of the current visible portion.
     * @param l
     */
    private static void applyAlpha(LinearLayoutManager l){
        int length = l.getChildCount();
        float alphavalue;
        if(l.getOrientation()==LinearLayoutManager.HORIZONTAL) {
            for (int i = 0; i < length; i++) {
                if(l.getChildAt(i).getWidth()>0) {
                    alphavalue = (float) getVisibleWidth(l.getChildAt(i)) / l.getChildAt(i).getWidth();
                    if (alphavalue < 1) {
                        l.getChildAt(i).setAlpha(alphavalue * alphavalue);
                    }
                }
            }
        }else{
            for (int i = 0; i < length; i++) {
                if(l.getChildAt(i).getWidth()>0) {
                    alphavalue = (float) getVisibleHeight(l.getChildAt(i)) / l.getChildAt(i).getHeight();
                    if (alphavalue < 1) {
                        l.getChildAt(i).setAlpha(alphavalue * alphavalue);
                    }
                }
            }
        }
    }


    /**
     * Method that returns the current center of the linearLayoutManager in coordinates
     * of the screen.
     * @param lm
     * @return
     */
    private static int [] getAbsoluteCenter(LinearLayoutManager lm){

        int width = lm.getWidth();
        int height = lm.getHeight();
        int [] values = {0,0};
        Rect first = new Rect();
        lm.getChildAt(0).getGlobalVisibleRect(first);
        values[0] = first.left+width/2;
        values[1] = first.top+height/2;
        return values;

    }


    /**
     * Method that returns a value of color between fromColor and toColor, depending of the variation
     * value, if variation is greater than 1, it returns toColor value (this method uses alpha value too)
     * @param fromColor
     * @param toColor
     * @param variation
     * @return
     */
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


    /**
     * Method used in generateGradientColor, it receives two integers and a changeRate value,
     * which needs to be between 0 and 1, and return a value between firs and second, depending
     * of the changeRate.
     * @param first
     * @param second
     * @param changeRate
     * @return
     */
    private int interpolate(int first,int second,float changeRate){

        if(first<second) {
            return (int) ((second - first) * changeRate) + first;
        }else{
            return (int) ((first-second) * (1-changeRate)) + second;
        }

    }


    /**
     * Method that returns the distance between an item of the linearLayoutManager and the center
     * of it. (Can be negative)
     * @param lm
     * @param index
     * @return
     */
    private static int getDistanceFromCenter(LinearLayoutManager lm,int index){

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


    /**
     * Method that receives a view and returns his visible width
     * @param v
     * @return
     */
    private static int getVisibleWidth(View v){
        Rect aux = new Rect();
        v.getGlobalVisibleRect(aux);
        return aux.width();
    }


    /**
     * Method that receives a view and returns his visible height
     * @param v
     * @return
     */
    private static int getVisibleHeight(View v){
        Rect aux = new Rect();
        v.getGlobalVisibleRect(aux);
        return aux.height();
    }

    //Custom classes
    public class AbstractGradientRecyclerViewHolder extends RecyclerView.ViewHolder{

        public AbstractGradientRecyclerViewHolder(View itemView) {
            super(itemView);
        }

    }

    public abstract class AbstractGradientRecyclerAdapter<VH extends AbstractGradientRecyclerViewHolder> extends RecyclerView.Adapter<VH>{

    }

}
