package com.example.mbuenacasa.recyclerview.HoursView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.mbuenacasa.recyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbuenacasa on 7/07/16.
 */
public class DateSelectorView extends RelativeLayout {

    private LayoutInflater inflater;
    private RecyclerView days;
    private RecyclerView months;
    private RecyclerView years;

    public DateSelectorView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public DateSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public DateSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.date_selector_view,this,true);
        days = (RecyclerView) findViewById(R.id.date_selector_view_days_recycler);
        months = (RecyclerView) findViewById(R.id.date_selector_view_months_recycler);
        years = (RecyclerView) findViewById(R.id.date_selector_view_years_recycler);

        int centerColor = getResources().getColor(R.color.msa_dark_grey);
        int sideColor = centerColor | 0x44000000;

        VerticalStringRecycler vsr = new VerticalStringRecycler();
        //TODO cambiar harcoded
        List<String> daysList = new ArrayList<>();
        daysList.add(" ");
        for(int i=1;i<31;i++){
            daysList.add(Integer.toString(i));
        }
        daysList.add(" ");

        List<String> monthsList = new ArrayList<>();
        monthsList.add(" ");
        monthsList.add("JAN");monthsList.add("FEB");monthsList.add("MAR");monthsList.add("APR");
        monthsList.add("MAY");monthsList.add("JUN");monthsList.add("JUL");monthsList.add("AUG");
        monthsList.add("SEP");monthsList.add("OCT");monthsList.add("NOV");monthsList.add("DEC");
        monthsList.add(" ");

        List<String> yearsList = new ArrayList<>();
        yearsList.add(" ");
        yearsList.add("2014");yearsList.add("2015");yearsList.add("2016");
        yearsList.add(" ");

        vsr.initRecyclerAsVerticalNumberRecycler(
                days,this.getContext(),centerColor,sideColor,daysList
        );
        vsr.initRecyclerAsVerticalNumberRecycler(
                months,this.getContext(),centerColor,sideColor,monthsList
        );
        vsr.initRecyclerAsVerticalNumberRecycler(
                years,this.getContext(),centerColor,sideColor,yearsList
        );
    }
}
