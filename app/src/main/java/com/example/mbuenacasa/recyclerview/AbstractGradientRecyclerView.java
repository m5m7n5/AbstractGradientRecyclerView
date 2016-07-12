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
    private boolean flag;
    private int orientation;

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
        int nearest = nearestView(rv);
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
        this.orientation = orientation;
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
        flag = true;

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
     * @param recyclerView
     * @return
     */
    protected static int nearestView(RecyclerView recyclerView){

        LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstMaxIndex = 0;
        Rect auxRectangle = new Rect();
        Rect firstMaxRectangle = new Rect();
        l.getChildAt(firstMaxIndex).getGlobalVisibleRect(firstMaxRectangle);

        for(int i=0;i<l.getChildCount();i++){
            l.getChildAt(i).getGlobalVisibleRect(auxRectangle);
            if(Math.abs(getDistanceFromCenter(recyclerView,i))<=Math.abs(getDistanceFromCenter(recyclerView,firstMaxIndex))){
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
                super.onScrolled(recyclerView, dx, dy);
                if(flag){
                    int startOffset;
                    if(orientation == LinearLayoutManager.HORIZONTAL){
                        int viewWidth = recyclerView.getWidth();
                        View first = recyclerView.getChildViewHolder(recyclerView.getChildAt(0)).itemView;
                        startOffset = (viewWidth-first.getWidth())/2;
                    }else{
                        int viewHeight = recyclerView.getHeight();
                        View first = recyclerView.getChildViewHolder(recyclerView.getChildAt(0)).itemView;
                        startOffset = (viewHeight-first.getHeight())/2;
                    }
                    recyclerView.addItemDecoration(new OffsetStartEndItemDecorator(startOffset,startOffset,orientation));
                }
                /*
                Para el correcto funcionamiento de este método supongo que en la vista siempre se ve un elemento completo,
                es decir, que por lo menos hay un elemento entero en el layout.
                 */
                timer.cancel();
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int length = l.getChildCount();
                int firstMaxIndex = nearestView(recyclerView);
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
                    if(Math.abs(getDistanceFromCenter(recyclerView,i))<=Math.abs(getDistanceFromCenter(recyclerView,secondMaxIndex)) && i!=firstMaxIndex){
                        secondMaxIndex = i;
                    }
                }

                if(secondMaxIndex == firstMaxIndex && firstMaxIndex == 0){
                    secondMaxIndex = 1;
                }

                if(l.getOrientation() == LinearLayoutManager.HORIZONTAL){
                    double getChangeValue = Math.abs(getDistanceFromCenter(recyclerView, firstMaxIndex) / (double) l.getChildAt(firstMaxIndex).getWidth());
                    changeColorFromView(l.getChildAt(firstMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                    getChangeValue = Math.abs(getDistanceFromCenter(recyclerView, secondMaxIndex) / (double) l.getChildAt(firstMaxIndex).getWidth());
                    changeColorFromView(l.getChildAt(secondMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                }else{
                    double getChangeValue = Math.abs(getDistanceFromCenter(recyclerView, firstMaxIndex) / (double) l.getChildAt(firstMaxIndex).getHeight());
                    changeColorFromView(l.getChildAt(firstMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                    getChangeValue = Math.abs(getDistanceFromCenter(recyclerView, secondMaxIndex) / (double) l.getChildAt(firstMaxIndex).getHeight());
                    changeColorFromView(l.getChildAt(secondMaxIndex), generateGradientColor(centerColor, sideColor, (float) (getChangeValue)));
                }


                /*
                Aplico el degradado al primero y al último elemento visible
                Utilizando el elemento mas cercano al centro calculo el valor alfa de cambio y lo aplico a los extremos
                 */
                applyAlpha(l);
                if(flag) {
                    onScrollStateChanged(recyclerView, RecyclerView.SCROLL_STATE_IDLE);
                    flag = false;
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                    timer.start();
                    int nearest = nearestView(recyclerView);
                    int offset = getDistanceFromCenter(recyclerView,nearest);
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
     * @param rv
     * @param index
     * @return
     */
    private static int getDistanceFromCenter(RecyclerView rv,int index){
        LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
        if(lm.getOrientation() == LinearLayoutManager.HORIZONTAL){
            Rect aux = new Rect();
            lm.getChildAt(index).getGlobalVisibleRect(aux);
            Rect recyclerRectangle = new Rect();
            rv.getGlobalVisibleRect(recyclerRectangle);
            return aux.centerX()-recyclerRectangle.centerX();
        }else{
            Rect aux = new Rect();
            lm.getChildAt(index).getGlobalVisibleRect(aux);
            Rect recyclerRectangle = new Rect();
            rv.getGlobalVisibleRect(recyclerRectangle);
            return aux.centerY()-recyclerRectangle.centerY();
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof AbstractGradientRecyclerView){
            return ((AbstractGradientRecyclerView) o).getRecyclerView() == recyclerView;
        }
        return false;
    }

    public class OffsetStartEndItemDecorator extends RecyclerView.ItemDecoration{

        private int startOffset;
        private int endOffset;
        private int orientation;

        public OffsetStartEndItemDecorator(int startOffset, int endOffset, int orientation){
            this.startOffset = startOffset;
            this.orientation = orientation;
            this.endOffset = endOffset;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int itemCount = state.getItemCount();
            if (parent.getChildAdapterPosition(view) == itemCount - 1) {
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    if (endOffset > 0) {
                        outRect.right = endOffset;
                    }
                } else if (orientation == LinearLayoutManager.VERTICAL) {
                    if (endOffset > 0) {
                        outRect.bottom = endOffset;
                    }
                }
            }else if(parent.getChildAdapterPosition(view) == 0){
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    if (startOffset > 0) {
                        outRect.left = startOffset;
                    }
                } else if (orientation == LinearLayoutManager.VERTICAL) {
                    if (startOffset > 0) {
                        outRect.top = startOffset;
                    }
                }
            }
        }

    }

}
