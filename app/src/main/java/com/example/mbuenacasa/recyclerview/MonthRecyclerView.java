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
 * Created by mbuenacasa on 13/07/16.
 */
public class MonthRecyclerView extends AbstractGradientRecyclerView2{

    public MonthRecyclerView(Context context) {
        super(context);

        List<String> list = new ArrayList<>();
        for(Months m:Months.values()){
            list.add(m.month);
        }
        this.setAdapter(new MonthViewAdapter(list,context));
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        List<String> list = new ArrayList<>();
        for(Months m:Months.values()){
            list.add(m.month);
        }
        this.setAdapter(new MonthViewAdapter(list,context));
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        List<String> list = new ArrayList<>();
        for(Months m:Months.values()){
            list.add(m.month);
        }
        this.setAdapter(new MonthViewAdapter(list,context));
    }

    @Override
    public void whenSelected(View v){
    }

    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(R.id.month_holder_textview);
        tv.setTextColor(c);
    }

    public enum Months{
        JANUARY("January"),
        FEBRUARY("February"),
        MARCH("March"),
        APRIL("April"),
        MAY("May"),
        JUNE("June"),
        JULY("July"),
        AUGUST("August"),
        SEPTEMBER("September"),
        OCTOBER("October"),
        NOVEMBER("November"),
        DECEMBER("December");

        public String month;

        Months(String month){
            this.month = month;
        }

    }

    public class MonthHolder extends RecyclerView.ViewHolder {
        TextView month;

        public MonthHolder(View itemView) {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.month_holder_textview);
        }
    }

    public class MonthViewAdapter extends RecyclerView.Adapter<MonthHolder> {

        List<String> list = Collections.emptyList();
        Context context;

        public MonthViewAdapter(List<String> list,Context context){
            this.list = list;
            this.context = context;
        }

        @Override
        public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_month,parent,false);
            MonthHolder holder = new MonthHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(MonthHolder holder, int position) {
            holder.month.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public String getSelectedMonth(){
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        return ((TextView)lm.getChildAt(nearestView(recyclerView)).findViewById(R.id.month_holder_textview)).getText().toString();
    }

}
