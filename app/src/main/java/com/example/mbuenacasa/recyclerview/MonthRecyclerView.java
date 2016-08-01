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
public class MonthRecyclerView extends AbstractGradientRecyclerView {

    public MonthRecyclerView(Context context) {
        super(context);
        init();
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MonthRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Method that initializes the view and his components, in this case it sets a default monthList
     */
    private void init() {
        List<String> list = new ArrayList<>();
        for (MonthRecyclerView.Months mo : MonthRecyclerView.Months.values()) {
            list.add(mo.month);
        }
        this.setAdapterList(list);
    }

    /**
     * Method that changes the current adapter for a new adapter with the list passed as an argument,
     * a call to notifyDataSetChanged should be made.
     *
     * @param monthList
     */
    public void setAdapterList(List<String> monthList) {
        this.setAdapter(new MonthViewAdapter(monthList, this.context));
    }

    @Override
    public void whenSelected(View v) {
    }

    /**
     * Method that changes the desired views of the view with the color passed as an argument
     *
     * @param v View that is currently going to change his color
     * @param c Color to apply on the desired elements
     */
    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(R.id.month_holder_textview);
        tv.setTextColor(c);
    }

    /**
     * Public enum with the name of the months
     */
    public enum Months {
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

        Months(String month) {
            this.month = month;
        }

    }

    /**
     * Custom holder for the adapter
     */
    public class MonthHolder extends AbstractGradientViewHolder {
        TextView month;

        public MonthHolder(View itemView) {
            super(itemView);
            month = (TextView) itemView.findViewById(R.id.month_holder_textview);
        }
    }

    /**
     * Custom adapter
     */
    public class MonthViewAdapter extends AbstractGradientAdapter<MonthHolder, String> {

        public MonthViewAdapter(List<String> list, Context context) {
            super(context);
            this.list = list;
            this.context = context;
        }

        @Override
        public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_month, parent, false);
            MonthHolder holder = new MonthHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(MonthHolder holder, int position) {
            holder.month.setText(list.get(position));
        }
    }

    /**
     * Method that returns the selected month as an string
     *
     * @return
     */
    public String getSelectedMonth() {
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        return ((TextView) lm.getChildAt(nearestView(recyclerView)).findViewById(R.id.month_holder_textview)).getText().toString();
    }

}
