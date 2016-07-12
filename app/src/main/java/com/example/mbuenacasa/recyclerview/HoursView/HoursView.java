package com.example.mbuenacasa.recyclerview.HoursView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.mbuenacasa.recyclerview.AbstractGradientRecyclerView;
import com.example.mbuenacasa.recyclerview.R;

import java.util.List;

/**
 * Created by mbuenacasa on 1/07/16.
 */
public class HoursView extends RelativeLayout{

    public RecyclerView hours;
    private RecyclerView minutes;
    private VerticalStringRecycler hoursV;
    private VerticalStringRecycler minutesV;
    private LayoutInflater inflater;

    public HoursView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    public HoursView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    public HoursView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init(){
        inflater.inflate(R.layout.view_hours_selector,this,true);
        hours = (RecyclerView) findViewById(R.id.hoursRecycler);
        minutes = (RecyclerView) findViewById(R.id.minutesRecycler);

        hoursV = new VerticalStringRecycler();
        minutesV = new VerticalStringRecycler();
        int centerColor = getResources().getColor(R.color.msa_dark_grey);
        int sideColor = (centerColor & 0x00FFFFFF) | 0x8F000000;

        List<String> list1 = hoursV.generateHoursNumbers();
        List<String> list2 = minutesV.generateMinutesNumbers();

        hoursV.initRecyclerAsVerticalNumberRecycler(hours,this.getContext(),centerColor,sideColor,list1);
        minutesV.initRecyclerAsVerticalNumberRecycler(minutes,this.getContext(),centerColor,sideColor,list2);

    }

    public void setSelectedHour(int value){
        hours.getLayoutManager().scrollToPosition(23-value);
        hours.smoothScrollBy(1,1);
    }

    public void setSelectedMinute(int value){
        minutes.getLayoutManager().scrollToPosition(59-value);
        minutes.smoothScrollBy(1,1);
    }

    public int getSelectedHour(){
        return Integer.parseInt(hoursV.getSelectedString());
    }

    public int getSelectedMinute(){
        return Integer.parseInt(minutesV.getSelectedString());
    }

    public void softHoursIncrement(){
        if(getSelectedHour()!=24){
            hours.smoothScrollBy(0,-AbstractGradientRecyclerView.getOffsetForOneMovement(hours));
        }
    }

    public void softMinutesIncrement(){
        if(getSelectedMinute() == 59 && getSelectedHour()<24){
            setSelectedMinute(0);
            softHoursIncrement();
        }else if(getSelectedMinute()<60){
            minutes.smoothScrollBy(0,-AbstractGradientRecyclerView.getOffsetForOneMovement(minutes));
        }
    }

    public void softHoursDecrement(){
        if(getSelectedHour()!=0){
            hours.smoothScrollBy(0,AbstractGradientRecyclerView.getOffsetForOneMovement(hours));
        }
    }

    public void softMinutesDecrement(){
        if(getSelectedMinute() == 0 && getSelectedHour()>0){
            setSelectedMinute(59);
            softHoursDecrement();
        }else if(getSelectedMinute()>0){
            minutes.smoothScrollBy(0,AbstractGradientRecyclerView.getOffsetForOneMovement(minutes));
        }
    }
}
