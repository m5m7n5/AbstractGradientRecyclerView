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
import java.util.Date;
import java.util.List;

/**
 * Created by mbuenacasa on 7/07/16.
 */
public class DateSelectorView extends RelativeLayout implements VerticalStringRecycler.Communicator {

    final int monthPerYear = 12;
    private LayoutInflater inflater;
    private VerticalStringRecycler days;
    private VerticalStringRecycler months;
    private VerticalStringRecycler years;
    private ArrayList<List<String>> daysData;
    private ArrayList<List<String>> monthsData;
    private List<String> yearsData;
    private int currentSelectedMonth;
    private int currentSelectedYear;
    private int startMonth;
    private int endMonth;
    private int yearsQuantity;


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

        days = new VerticalStringRecycler();
        months = new VerticalStringRecycler();
        years = new VerticalStringRecycler();
        //TODO Generar correctamente los meses
        //initData(new Date(2016, 1, 1),new Date(2016, 3, 30));
        initData(new Date(2016, 4, 1),new Date(2016, 6, 31));
        days.initRecyclerAsVerticalNumberRecycler(
                daysRV,this.getContext(),centerColor,sideColor,daysData.get(0)
        );
        months.initRecyclerAsVerticalNumberRecycler(
                monthsRV,this.getContext(),centerColor,sideColor,monthsData.get(0)
        );
        years.initRecyclerAsVerticalNumberRecycler(
                yearsRV,this.getContext(),centerColor,sideColor,yearsData
        );
        days.setCommunicator(this);
        months.setCommunicator(this);
        years.setCommunicator(this);
        currentSelectedMonth = 0;
        currentSelectedYear = 0;

    }

    @Override
    public void selectionChanged(AbstractGradientRecyclerView aRecycler, View view, int index){

        if(days==aRecycler){
        }else if(months==aRecycler){

            if(currentSelectedYear==0) {
                currentSelectedMonth = index-1;
            }else if(currentSelectedYear == yearsQuantity){
                currentSelectedMonth = daysData.size()-endMonth-1+(index-1);
            }else{
                currentSelectedMonth = (currentSelectedYear-1)*monthPerYear+index+startMonth-1;
            }
            days.setAdapterItems(daysData.get(currentSelectedMonth));
            days.getRecyclerView().getAdapter().notifyDataSetChanged();


        }else if(years==aRecycler){
            currentSelectedYear = index-1;
            if(currentSelectedYear==0) {
                currentSelectedMonth = 0;
            }else if(currentSelectedYear == yearsQuantity){
                //TODO Shitty
                currentSelectedMonth = daysData.size()-endMonth-1;
            }else{
                currentSelectedMonth = (currentSelectedYear-1)*monthPerYear+startMonth-1;
            }
            days.setAdapterItems(daysData.get(currentSelectedMonth));
            days.getRecyclerView().getAdapter().notifyDataSetChanged();
            months.setAdapterItems(monthsData.get(currentSelectedYear));
            months.getRecyclerView().getAdapter().notifyDataSetChanged();
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

    private void fillDaysData(Date startDate,Date endDate){

        int startYear = startDate.getYear();
        int startMonth = startDate.getMonth();
        int startDay = startDate.getDate();
        int endYear = endDate.getYear();
        int endMonth = endDate.getMonth();
        int endDay = endDate.getDate();

        int quantityOfMonths = (monthPerYear-startMonth)+endMonth+monthPerYear*(endYear-startYear-1);

        ArrayList<Integer> months = new ArrayList<>();
        for(MonthInformation m:MonthInformation.values()){
            months.add(m.monthDays);
        }

        daysData = new ArrayList<>();
        List<String> auxList = new ArrayList<>();
        auxList.add(" ");
        for(int i=startDay;i<=months.get((startMonth)%monthPerYear);i++){
            auxList.add(Integer.toString(i));
        }
        auxList.add(" ");
        daysData.add(auxList);
        for(int i=1;i<quantityOfMonths;i++){
            auxList = new ArrayList<>();
            auxList.add(" ");
            for(int j=1;j<=months.get((i+startMonth)%monthPerYear);j++){
                auxList.add(Integer.toString(j));
            }
            auxList.add(" ");
            daysData.add(auxList);
        }
        auxList = new ArrayList<>();
        auxList.add(" ");
        for(int i=1;i<=endDay;i++){
            auxList.add(Integer.toString(i));
        }
        auxList.add(" ");
        daysData.add(auxList);
    }

    private void fillMonthsData(Date startDate,Date endDate){

        int startYear = startDate.getYear();
        int startMonth = startDate.getMonth();
        int endYear = endDate.getYear();
        int endMonth = endDate.getMonth();
        this.startMonth = startMonth;
        this.endMonth = endMonth;
        int quantityOfYears = endYear-startYear-1;

        ArrayList<String> months = new ArrayList<>();
        for(MonthInformation m:MonthInformation.values()){
            months.add(m.text);
        }
        monthsData = new ArrayList<>();
        List<String> auxList = new ArrayList<>();
        if(quantityOfYears!=-1){
            auxList.add(" ");
            for(int i=startMonth;i<monthPerYear;i++){
                auxList.add(months.get(i));
            }
            auxList.add(" ");
        }else{
            auxList.add(" ");
            for(int i=startMonth;i<=endMonth;i++){
                auxList.add(months.get(i));
            }
            auxList.add(" ");
        }

        monthsData.add(auxList);
        for(int i=0;i<quantityOfYears;i++){
            auxList = new ArrayList<>();
            auxList.add(" ");
            for(int j=0;j<monthPerYear;j++){
                auxList.add(months.get(j));
            }
            auxList.add(" ");
            monthsData.add(auxList);
        }

        auxList = new ArrayList<>();

        auxList.add(" ");
        for(int i=0;i<=endMonth;i++){
            auxList.add(months.get(i));
        }
        auxList.add(" ");

        monthsData.add(auxList);
    }

    private void fillYearsData(Date startDate,Date endDate){

        int startYear = startDate.getYear();
        int endYear = endDate.getYear();
        this.yearsQuantity = endYear - startYear;
        yearsData = new ArrayList<>();
        yearsData.add(" ");
        for(int i=startYear;i<=endYear;i++){
            yearsData.add(Integer.toString(i));
        }
        yearsData.add(" ");

    }

    private void initData(Date startDate,Date endDate){

        fillYearsData(startDate,endDate);
        fillMonthsData(startDate,endDate);
        fillDaysData(startDate,endDate);

    }

}
