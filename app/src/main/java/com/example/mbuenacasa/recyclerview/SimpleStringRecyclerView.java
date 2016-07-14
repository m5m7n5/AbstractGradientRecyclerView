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
 */
public class SimpleStringRecyclerView extends AbstractGradientRecyclerView2{

    public SimpleStringRecyclerView(Context context) {
        super(context);
    }

    public SimpleStringRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleStringRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void initAdapter(List<String> list) {
        this.setAdapter(new VerticalStringAdapter(list,this.context));
    }

    @Override
    protected void whenSelected(View v) {

    }

    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(R.id.hour_recycler_view_text_view);
        tv.setTextColor(c);
    }

    public List<String> generateHoursNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i=23;i>=0;i--){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    public List<String> generateMinutesNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i=59;i>=0;i--){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    public class NumberHolder extends RecyclerView.ViewHolder {
        TextView numbers;

        public NumberHolder(View itemView) {
            super(itemView);
            numbers = (TextView) itemView.findViewById(R.id.hour_recycler_view_text_view);
        }
    }

    public class VerticalStringAdapter extends RecyclerView.Adapter<NumberHolder> {

        List<String> list = Collections.emptyList();
        Context context;

        public VerticalStringAdapter(List<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public NumberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_hour_recycler_view, parent, false);
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
    }

    public String getSelectedString(){
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        return ((TextView)lm.getChildAt(nearestView(recyclerView)).findViewById(R.id.hour_recycler_view_text_view)).getText().toString();
    }

}
