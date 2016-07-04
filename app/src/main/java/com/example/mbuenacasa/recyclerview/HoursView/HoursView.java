package com.example.mbuenacasa.recyclerview.HoursView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mbuenacasa.recyclerview.R;

/**
 * Created by mbuenacasa on 1/07/16.
 */
public class HoursView extends RelativeLayout{

    private TextView minText;
    private TextView hourText;
    private RecyclerView hours;
    private RecyclerView minutes;
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

    public void init(){
        inflater.inflate(R.layout.hours_view,this,true);
        minText = (TextView) findViewById(R.id.customViewMinutes);
        hourText = (TextView) findViewById(R.id.customViewHours);
        hours = (RecyclerView) findViewById(R.id.hoursRecycler);
        minutes = (RecyclerView) findViewById(R.id.minutesRecycler);

        VerticalNumberRecycler v = new VerticalNumberRecycler();
        v.initRecyclerAsVerticalNumberRecycler(hours,this.getContext(),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.black),v.generateHoursNumbers());
        v.initRecyclerAsVerticalNumberRecycler(minutes,this.getContext(),getResources().getColor(R.color.colorPrimaryDark),getResources().getColor(R.color.black),v.generateMinutesNumbers());

    }

}
