package com.example.mbuenacasa.recyclerview.HoursView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.mbuenacasa.recyclerview.AbstractGradientRecyclerView;
import com.example.mbuenacasa.recyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbuenacasa on 7/07/16.
 */
public class DateSelectorView extends RelativeLayout implements VerticalStringRecycler.Communicator {

    private LayoutInflater inflater;
    private VerticalStringRecycler days;
    private VerticalStringRecycler months;
    private VerticalStringRecycler years;

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
        RecyclerView daysRV = (RecyclerView) findViewById(R.id.date_selector_view_days_recycler);
        RecyclerView monthsRV = (RecyclerView) findViewById(R.id.date_selector_view_months_recycler);
        RecyclerView yearsRV = (RecyclerView) findViewById(R.id.date_selector_view_years_recycler);

        int centerColor = getResources().getColor(R.color.msa_dark_grey);
        int sideColor = centerColor | 0x44000000;

        days = new VerticalStringRecycler();
        months = new VerticalStringRecycler();
        years = new VerticalStringRecycler();
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
        yearsList.add("2011");yearsList.add("2012");yearsList.add("2013");
        yearsList.add("2014");yearsList.add("2015");yearsList.add("2016");
        yearsList.add(" ");

        days.initRecyclerAsVerticalNumberRecycler(
                daysRV,this.getContext(),centerColor,sideColor,daysList
        );
        months.initRecyclerAsVerticalNumberRecycler(
                monthsRV,this.getContext(),centerColor,sideColor,monthsList
        );
        years.initRecyclerAsVerticalNumberRecycler(
                yearsRV,this.getContext(),centerColor,sideColor,yearsList
        );
        days.setCommunicator(this);
        months.setCommunicator(this);
        years.setCommunicator(this);
    }

    @Override
    public void selectionChanged(AbstractGradientRecyclerView aRecycler, View view, int index){
        if(days==aRecycler){
        }else if(months==aRecycler){
        }else if(years==aRecycler){
            days.getRecyclerView().getLayoutManager();
        }
    }
}
