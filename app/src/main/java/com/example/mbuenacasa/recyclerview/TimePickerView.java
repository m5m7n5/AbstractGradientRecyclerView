package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by mbuenacasa on 14/07/16.
 * Custom class of the timePickerView that places SimpleStringRecycler
 */
public class TimePickerView extends RelativeLayout{

    private LayoutInflater inflater;
    private SimpleStringRecyclerView hours;
    private SimpleStringRecyclerView minutes;

    /**
     * Custom constructor
     * @param context
     */
    public TimePickerView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        init();
    }

    /**
     * Custom constructor
     * @param context
     * @param attrs
     */
    public TimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();
    }

    /**
     * Custom constructor
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();
    }

    /**
     * Method that initializes the view and his components
     */
    private void init(){

        inflater.inflate(R.layout.view_hours_selector,this,true);
        hours = (SimpleStringRecyclerView) findViewById(R.id.hours_view_hours_recycler);
        minutes = (SimpleStringRecyclerView) findViewById(R.id.hours_view_minutes_recycler);
        hours.setStringHolder(R.id.hour_recycler_view_text_view);
        hours.setStringHolderContainerFilename(R.layout.holder_hour_recycler_view);
        minutes.setStringHolder(R.id.hour_recycler_view_text_view);
        minutes.setStringHolderContainerFilename(R.layout.holder_hour_recycler_view);


    }

    /**
     * Returns the current time with hh:mm format
     * @return
     */
    public String getCurrentTimeAsString(){
        return hours.getSelectedString() +":"+ minutes.getSelectedString();
    }

    /**
     * Method that returns the selected hour as an int
     * @return
     */
    public int getSelectedHour(){
        return Integer.parseInt(hours.getSelectedString());
    }

    /**
     * Method that returns the selected minute as an int
     * @return
     */
    public int getSelectedMinute(){
        return Integer.parseInt(minutes.getSelectedString());
    }

    /**
     * Method that returns the timestamp value of the selected hour, in seconds
     * @return
     */
    public int getTimestampSeconds(){
        return getSelectedHour()*3600+getSelectedMinute()*60;
    }

    /**
     * Method that returns the timestamp value of the selected hour, in miliseconds
     * @return
     */
    public int getTimeStampMiliSeconds(){
        return getTimestampSeconds()*1000;
    }

    public SimpleStringRecyclerView getHoursRecycler() {
        return hours;
    }
}
