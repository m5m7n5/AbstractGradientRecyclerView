package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbuenacasa on 14/07/16.
 * This class consist in a textview recycler view
 */
public class SimpleStringRecyclerView extends AbstractGradientRecyclerView {


    private final int HOURS_IN_A_DAY=24;
    private final int MINUTES_IN_AN_HOUR=60;
    /**
     * Default constructor
     * @param context
     */
    public SimpleStringRecyclerView(Context context) {
        super(context);
        init();
    }

    /**
     * Default constructor
     * @param context
     * @param attrs
     */
    public SimpleStringRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Default constructor
     * @param context
     * @param attrs
     * @param defStyle
     */
    public SimpleStringRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Initializes the adapter with a default list
     */
    private void init(){
        setAdapterList(generateHoursNumbers());
    }

    /**
     * Changes the adapter list, a call to notifyDataSetChanged should be done after this method
     * @param list list for the adapter
     */
    public void setAdapterList(List<String> list) {
        this.setAdapter(new SimpleStringAdapter(this.context));
        ((SimpleStringAdapter)this.getAdapter()).setList(list);
    }

    /**
     * Method that does nothing but we need to implement it from the abstract class.
     * @param v Current view on the center of the recyclerView
     */
    @Override
    protected void whenSelected(View v) {

    }

    /**
     * Method overrided for changing the color of the text from the textView
     * @param v View that is currently going to change his color
     * @param c Color to apply on the desired elements
     */
    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(R.id.vertical_recycler_view_text);
        tv.setTextColor(c);
    }

    /**
     * Method that generates a list of numbers from HOURS_IN_A_DAY to 0
     * @return
     */
    public List<String> generateHoursNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i = HOURS_IN_A_DAY-1; i>=0;i--){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    /**
     * Method that generates a list of numbers from MINUTES_IN_AN_HOUR to 0
     * @return
     */
    public List<String> generateMinutesNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i=MINUTES_IN_AN_HOUR-1;i>=0;i--){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    /**
     * Custom ViewHolder for the adapter
     */
    public class NumberHolder extends AbstractGradientViewHolder {
        TextView numbers;

        public NumberHolder(View itemView) {
            super(itemView);
            numbers = (TextView) itemView.findViewById(R.id.vertical_recycler_view_text);
        }
    }

    /**
     * Custom adapter for the recycler
     */
    public class SimpleStringAdapter extends AbstractGradientAdapter<NumberHolder> {

        List<String> list = Collections.emptyList();
        Context context;

        public SimpleStringAdapter(Context context) {
            this.list = new ArrayList<>();
            this.context = context;
        }

        @Override
        public NumberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_vertical_string, parent, false);
            NumberHolder holder = new NumberHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(NumberHolder holder, int position) {
            holder.numbers.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<String> list){
            this.list.clear();
            for(int i=0;i<list.size();i++){
                this.list.add(list.get(i));
            }
        }
    }

    /**
     * Method that returns the current selected string
     * @return
     */
    public String getSelectedString(){
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        return ((TextView)lm.getChildAt(nearestView(recyclerView)).findViewById(R.id.vertical_recycler_view_text)).getText().toString();
    }
}

