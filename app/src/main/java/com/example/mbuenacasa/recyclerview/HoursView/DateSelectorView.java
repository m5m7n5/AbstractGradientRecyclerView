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
    private ArrayList<MonthInformation> monthsAndDays;

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
        inflater.inflate(R.layout.view_date_selector,this,true);
        RecyclerView daysRV = (RecyclerView) findViewById(R.id.date_selector_view_days_recycler);
        RecyclerView monthsRV = (RecyclerView) findViewById(R.id.date_selector_view_months_recycler);
        RecyclerView yearsRV = (RecyclerView) findViewById(R.id.date_selector_view_years_recycler);

        int centerColor = getResources().getColor(R.color.msa_dark_grey);
        int sideColor = (centerColor & 0x00FFFFFF) | 0x8F000000;

        monthsAndDays = new ArrayList<>();
        for(MonthInformation m:MonthInformation.values()){
            monthsAndDays.add(m);
        }

        days = new VerticalStringRecycler();
        months = new VerticalStringRecycler();
        years = new VerticalStringRecycler();
        //TODO cambiar harcoded
        List<String> daysList = monthsAndDays.get(0).dayList;

        List<String> monthsList = new ArrayList<>();
        monthsList.add(" ");
        for(int i=0;i<monthsAndDays.size()-1;i++){
            monthsList.add(monthsAndDays.get(i).text);
        }
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
            RecyclerView daysRecycler = days.getRecyclerView();
            int currentDaysCount = daysRecycler.getAdapter().getItemCount()-2;
            int destinyDaysCount = monthsAndDays.get(index-1).monthDays;
            if(Integer.parseInt(years.getSelectedString())%4==0 && index == 2/*February*/) {
                destinyDaysCount++;
            }
            changeAdapter(currentDaysCount,destinyDaysCount,daysRecycler);
            centerDays();

        }else if(years==aRecycler){
            RecyclerView daysRecycler = days.getRecyclerView();
            int currentDaysCount = daysRecycler.getAdapter().getItemCount()-2;
            //Esto indica que es bisiesto (no compruebo la condicion de 400)
            if(Integer.parseInt(years.getSelectedString())%4==0){
                if(months.getSelectedString()==MonthInformation.FEBRUARY.text) {
                    changeAdapter(currentDaysCount,MonthInformation.FEBRUARY2.monthDays,daysRecycler);
                    centerDays();
                }
            }else{
                if(months.getSelectedString()==MonthInformation.FEBRUARY.text) {
                    changeAdapter(currentDaysCount,MonthInformation.FEBRUARY.monthDays,daysRecycler);
                    centerDays();
                }
            }
        }
    }

    private void changeAdapter(int currentDaysCount,int destinyDaysCount,RecyclerView daysRecycler){
        if(currentDaysCount!=destinyDaysCount) {
            List<String> list = ((VerticalStringRecycler.VerticalStringAdapter) daysRecycler.getAdapter()).list;
            if (currentDaysCount > destinyDaysCount) {
                for (int i = 0; i < (currentDaysCount - destinyDaysCount); i++) {
                    list.remove(list.size() - 2);
                    daysRecycler.getAdapter().notifyItemRemoved(list.size() - 2);
                    daysRecycler.getAdapter().notifyItemRangeChanged(list.size() - 2, list.size());
                }

            } else {
                for (int i = 0; i < (destinyDaysCount - currentDaysCount); i++) {
                    list.add(list.size() - 1, Integer.toString(list.size() - 1));
                    daysRecycler.getAdapter().notifyItemInserted(list.size() - 1);
                }
            }
        }
    }

    private void centerDays(){
        if(Integer.parseInt(days.getSelectedString())>25||Integer.parseInt(days.getSelectedString())<5) {
            days.getRecyclerView().smoothScrollToPosition(15);
        }
    }

    private enum MonthInformation{
        JANUARY("JAN",31),
        FEBRUARY("FEB",28),
        MARCH("MAR",31),
        APRIL("APR",30),
        MAY("MAY",31),
        JUNE("JUN",30),
        JULY("JUL",31),
        AUGUST("AUG",31),
        SEPTEMBER("SEP",30),
        OCTOBER("OCT",31),
        NOVEMBER("NOV",30),
        DECEMBER("DEC",31),
        FEBRUARY2("FEB",29);

        public String text;
        public List<String> dayList;
        public int monthDays;

        MonthInformation(String string,int numberOfDays){
            text = string;
            dayList = new ArrayList<>();
            dayList.add(" ");
            monthDays = numberOfDays;
            for(int i=1;i<=numberOfDays;i++){
                dayList.add(Integer.toString(i));
            }
            dayList.add(" ");
        }
    }

}
