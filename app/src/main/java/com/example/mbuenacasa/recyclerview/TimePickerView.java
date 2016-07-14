package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.mbuenacasa.recyclerview.HoursView.VerticalStringRecycler;

import java.util.List;

/**
 * Created by mbuenacasa on 14/07/16.
 */
public class TimePickerView extends RelativeLayout{

    private LayoutInflater inflater;
    private HourRecyclerView hours;
    private HourRecyclerView minutes;

    public TimePickerView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public TimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.view_new_hours_selector,this,true);
        hours = (HourRecyclerView) findViewById(R.id.new_hours_view_hours_recycler);
        hours.initAdapter(hours.generateHoursNumbers());

        minutes = (HourRecyclerView) findViewById(R.id.new_hours_view_minutes_recycler);
        minutes.initAdapter(minutes.generateMinutesNumbers());

    }

}
