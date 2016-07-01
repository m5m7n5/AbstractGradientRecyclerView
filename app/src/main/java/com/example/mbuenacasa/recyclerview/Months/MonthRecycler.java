package com.example.mbuenacasa.recyclerview.Months;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbuenacasa.recyclerview.MyCustomRecyclerView;
import com.example.mbuenacasa.recyclerview.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbuenacasa on 1/07/16.
 */
public class MonthRecycler extends MyCustomRecyclerView {

    public void initRecyclerAsMonthRecycler(
            @NonNull RecyclerView rv,@NonNull Context context,
            int centerColor,int sideColor
    ){
        List<String> list = new ArrayList<>();
        list.add("");
        for(Months m:Months.values()){
            list.add(m.month);
        }
        list.add("");
        initCustomRecyclerView(rv,new MonthViewAdapter(list,context),context, LinearLayoutManager.HORIZONTAL,centerColor,sideColor);
    }

    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(R.id.monthname);
        tv.setTextColor(c);
    }

    private enum Months{
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

    public class MonthHolder extends CustomViewHolder{
        TextView month;

        public MonthHolder(View itemView) {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.monthname);
        }
    }

    public class MonthViewAdapter extends RecyclerView.Adapter<MonthHolder>{

        List<String> list = Collections.emptyList();
        Context context;

        public MonthViewAdapter(List<String> list,Context context){
            this.list = list;
            this.context = context;
        }

        @Override
        public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_month,parent,false);
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
}
