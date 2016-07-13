package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by mbuenacasa on 13/07/16.
 */
public abstract class AbstractGradientRecyclerView2 extends RecyclerView{

    private int centerColor;
    private int sideColor;
    private CountDownTimer timer;
    protected int selectedViewIndex;
    protected RecyclerView recyclerView;
    protected View selectedView;
    private int orientation;

    public AbstractGradientRecyclerView2(Context context) {
        super(context);
    }

    public AbstractGradientRecyclerView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractGradientRecyclerView2(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected abstract void whenSelected(View v);

    protected abstract void changeColorFromView(View v, int c);

    protected void onCreateCall(@NonNull Context context,@NonNull AbstractGradientRecyclerView2.AbstractGradientRecyclerAdapter adapter, int orientation, int centerColor,int sideColor){
        this.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(orientation);
        setLayoutManager(llm);
        settingCustomListeners();
        this.orientation = orientation;
        this.sideColor = sideColor;
        this.centerColor = centerColor;
        recyclerView = this;
        timer = new CountDownTimer(200,200) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                whenSelected(selectedView);
            }
        };
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                offset(recyclerView);
                onStartRecycler(recyclerView);
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });
    }



    protected void offset(RecyclerView recyclerView){
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
    /**
     * Method to change the layoutManager of the current recyclerView
     * @param lm changes the layoutManager of the recyclerView
     */
    protected void changeLayoutManager(RecyclerView.LayoutManager lm){

        recyclerView.setLayoutManager(lm);

    }


    /**
     * Method that iterates over the current visible views of the LinearLayoutManager and
     * returns which is the closest.
     * @param recyclerView recyclerView to search the nearest view
     * @return returns the index of the current visible nearest view
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

        addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
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
                    selectedViewIndex = recyclerView.getChildAdapterPosition(l.getChildAt(nearest));
                    timer.start();
                }
            }
        });

    }


    private void onStartRecycler(RecyclerView recyclerView){
        LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.scrollTo(0,0);
        int nearest = 0;
        for(int i=1;i<l.getChildCount();i++){
            changeColorFromView(l.getChildAt(i), sideColor);
        }
        changeColorFromView(l.getChildAt(nearest),centerColor);
        selectedView = l.getChildAt(nearest);
        selectedViewIndex = recyclerView.getChildAdapterPosition(l.getChildAt(nearest));
        timer.start();
    }


    /**
     * Method that iterates over the current visible items and changes alpha values of the items,
     * depending of the current visible portion.
     * @param l LinearLayoutManager to apply alpha
     */
    private static void applyAlpha(LinearLayoutManager l){
        int length = l.getChildCount();
        float alpha;
        if(l.getOrientation()==LinearLayoutManager.HORIZONTAL) {
            for (int i = 0; i < length; i++) {
                if(l.getChildAt(i).getWidth()>0) {
                    alpha = (float) getVisibleWidth(l.getChildAt(i)) / l.getChildAt(i).getWidth();
                    if (alpha < 1) {
                        l.getChildAt(i).setAlpha(alpha * alpha);
                    }else{
                        l.getChildAt(i).setAlpha(1);
                    }
                }
            }
        }else{
            for (int i = 0; i < length; i++) {
                if(l.getChildAt(i).getWidth()>0) {
                    alpha = (float) getVisibleHeight(l.getChildAt(i)) / l.getChildAt(i).getHeight();
                    if (alpha < 1) {
                        l.getChildAt(i).setAlpha(alpha * alpha);
                    }else{
                        l.getChildAt(i).setAlpha(1);
                    }
                }
            }
        }
    }


    /**
     * Method that returns a value of color between fromColor and toColor, depending of the variation
     * value, if variation is greater than 1, it returns toColor value (this method uses alpha value too)
     * @param fromColor start color of the gradient
     * @param toColor end color of the gradient
     * @param variation percentage of the first color
     * @return a color between fromcolor and tocolor
     */
    private int generateGradientColor(int fromColor,int toColor, float variation){

        if(variation>=1){
            return toColor;
        }
        //Calculo las componentes de cada color para calcular la tasa de cambio total
        int A0 = (fromColor & 0xFF000000)>>24;
        int R0 = (fromColor & 0x00FF0000)>>16;
        int G0 = (fromColor & 0x0000FF00)>>8;
        int B0 = fromColor & 0x000000FF;
        int A1 = (toColor & 0xFF000000)>>24;
        int R1 = (toColor & 0x00FF0000)>>16;
        int G1 = (toColor & 0x0000FF00)>>8;
        int B1 = toColor & 0x000000FF;


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
     * @param first start value
     * @param second end value
     * @param changeRate portion of the first respect the second
     * @return a value between first and second
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
     * @param rv recycler view container of the view selected by the index
     * @param index index of the item in the recycler view
     * @return distance from the center
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
     * @param v selected view
     * @return visible width of the view
     */
    private static int getVisibleWidth(View v){
        Rect aux = new Rect();
        v.getGlobalVisibleRect(aux);
        return aux.width();
    }


    /**
     * Method that receives a view and returns his visible height
     * @param v selected view
     * @return visible height of the view
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
            this.startOffset = startOffset+1;
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

