package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mbuenacasa on 25/08/16.
 */
public class NewDatePickerView extends RelativeLayout implements AbstractGradientRecyclerView.AbstractGradientRecyclerCommunicator {

    final int monthsPerYear = 12;
    private LayoutInflater inflater;
    private SimpleStringRecyclerView days;
    private SimpleStringRecyclerView months;
    private SimpleStringRecyclerView years;
    private List<String> yearsData;
    private List<String> indexes;
    private Map<String, ArrayList<String>> daysInMonthMap;
    private Map<String, ArrayList<String>> monthsDataMap;
    private Map<String, ArrayList<String>> daysDataMap;
    private int previousDaysPosition;
    private int previousMonthsPosition;
    private boolean scrollingDays;
    private boolean scrollingMonths;
    private boolean scrollingYears;
    private List<String> longDaysList;
    private List<String> longMonthsList;

    public NewDatePickerView(Context context) {

        super(context);
        inflater = LayoutInflater.from(context);
        init();

    }

    public NewDatePickerView(Context context, AttributeSet attrs) {

        super(context, attrs);
        inflater = LayoutInflater.from(context);
        init();

    }

    public NewDatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        init();

    }

    private void init() {

        inflater.inflate(R.layout.view_date_picker, this, true);
        days = (SimpleStringRecyclerView) findViewById(R.id.date_picker_days_recycler);
        months = (SimpleStringRecyclerView) findViewById(R.id.date_picker_months_recycler);
        years = (SimpleStringRecyclerView) findViewById(R.id.date_picker_years_recycler);
        List<String> monthsStrings = new ArrayList<>();

        days.setStringHolder(R.id.hour_recycler_view_text_view);
        days.setStringHolderContainerFilename(R.layout.holder_hour_recycler_view);
        months.setStringHolder(R.id.hour_recycler_view_text_view);
        months.setStringHolderContainerFilename(R.layout.holder_hour_recycler_view);
        years.setStringHolder(R.id.hour_recycler_view_text_view);
        years.setStringHolderContainerFilename(R.layout.holder_hour_recycler_view);


        //TODO Quitar hardcoded strings
        monthsStrings.add("JAN");
        monthsStrings.add("FEB");
        monthsStrings.add("MAR");
        monthsStrings.add("APR");
        monthsStrings.add("MAY");
        monthsStrings.add("JUN");
        monthsStrings.add("JUL");
        monthsStrings.add("AUG");
        monthsStrings.add("SEP");
        monthsStrings.add("OCT");
        monthsStrings.add("NOV");
        monthsStrings.add("DEC");

        Calendar calendarInstance = Calendar.getInstance();
        Date start = new Date(calendarInstance.get(Calendar.YEAR), calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.DAY_OF_MONTH));
        Date end = new Date(calendarInstance.get(Calendar.YEAR) + 1, calendarInstance.get(Calendar.MONTH), calendarInstance.get(Calendar.DAY_OF_MONTH));
        generateTablesAndValues(monthsStrings, start, end);


        longDaysList = new ArrayList<>();
        longMonthsList = new ArrayList<>();

        for (int i = 0; i < yearsData.size(); i++) {
            List<String> monthList = monthsDataMap.get(yearsData.get(i));
            for (int j = 0; j < monthList.size(); j++) {
                List<String> daysList = daysDataMap.get(monthsDataMap.get(yearsData.get(i)).get(j) + yearsData.get(i));
                for (int k = 0; k < daysList.size(); k++) {
                    longDaysList.add(daysList.get(k));
                }
                longMonthsList.add(monthList.get(j));
            }
        }

        //days.setAdapterList(daysDataMap.get(indexes.get(start.getMonth()) + Integer.toString(start.getYear())));
        //months.setAdapterList(monthsDataMap.get(Integer.toString(start.getYear())));
        days.setAdapterList(longDaysList);
        previousDaysPosition = 0;
        months.setAdapterList(longMonthsList);
        previousMonthsPosition = 0;
        years.setAdapterList(yearsData);

        days.setCommunicator(this);
        months.setCommunicator(this);
        years.setCommunicator(this);

    }

    private void generateTablesAndValues(List<String> monthsStrings, Date startDate, Date endDate) {

        int startYear = startDate.getYear();
        int startMonth = startDate.getMonth();
        int startDay = startDate.getDate();
        int endYear = endDate.getYear();
        int endMonth = endDate.getMonth();
        int endDay = endDate.getDate();

        int quantityOfMonths = 12 * (endYear - startYear) + endMonth - startMonth;

        List<Integer> monthsDays = new ArrayList<>();

        //Position of the days quantity in the ArrayList monthQuantity
        monthsDays.add(3);
        monthsDays.add(0);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);
        monthsDays.add(2);
        monthsDays.add(3);

        Map<String, Integer> monthsDayIndexMap = new LinkedHashMap<>();
        for (int i = 0; i < monthsStrings.size(); i++) {
            monthsDayIndexMap.put(monthsStrings.get(i), monthsDays.get(i));
        }
        indexes = new ArrayList<>(monthsDayIndexMap.keySet());


        ArrayList<Integer> monthQuantity = new ArrayList<>();
        monthQuantity.add(28);
        monthQuantity.add(29);
        monthQuantity.add(30);
        monthQuantity.add(31);

        ArrayList<ArrayList<String>> aux = new ArrayList<>();
        for (int i = 0; i < monthQuantity.size(); i++) {
            ArrayList<String> aux2 = new ArrayList<>();
            for (int j = 1; j <= monthQuantity.get(i); j++) {
                aux2.add(Integer.toString(j));
            }
            aux.add(aux2);
        }

        daysInMonthMap = new HashMap<>();
        for (String s : monthsDayIndexMap.keySet()) {
            daysInMonthMap.put(s, aux.get(monthsDayIndexMap.get(s)));
        }


        daysDataMap = new HashMap<>();
        monthsDataMap = new HashMap<>();

        if (quantityOfMonths == 0) {
            //Produces the list of days for the corresponding month and year
            ArrayList<String> aux2 = new ArrayList<>();
            for (int i = startDay; i <= endDay; i++) {
                aux2.add(Integer.toString(i));
            }
            //Adds the list of days to the corresponding month and year
            daysDataMap.put(indexes.get(startMonth) + Integer.toString(startYear), aux2);
            //Produces the list of months for the year
            aux2 = new ArrayList<>();
            aux2.add(indexes.get(startMonth));
            //Adds month list
            monthsDataMap.put(Integer.toString(startYear), aux2);
        } else if ((endYear - startYear) == 0) {
            //Produces the days for the initial month
            ArrayList<String> aux2 = new ArrayList<>();
            for (int i = startDay; i <= monthQuantity.get(monthsDays.get(startMonth)); i++) {
                aux2.add(Integer.toString(i));
            }
            //Adds the days of the initial month depending on the month and the year
            daysDataMap.put(indexes.get(startMonth) + Integer.toString(startYear), aux2);

            //Adding the rest of the months
            for (int i = (startMonth + 1); i < endMonth; i++) {
                daysDataMap.put(indexes.get(i) + Integer.toString(startYear), daysInMonthMap.get(indexes.get(i)));
            }
            //Produces the days of the last month
            aux2 = new ArrayList<>();
            for (int i = 1; i <= endDay; i++) {
                aux2.add(Integer.toString(i));
            }
            //Adds the days of the last month depending on the last month and the year
            daysDataMap.put(indexes.get(endMonth) + Integer.toString(startYear), aux2);
            //Produces the months of the corresponding year
            aux2 = new ArrayList<>();
            for (int i = startMonth; i <= endMonth; i++) {
                aux2.add(indexes.get(i));
            }
            //Adds the list of the months to his year
            monthsDataMap.put(Integer.toString(startYear), aux2);
        } else {
            //Produces the list of days of the first month
            ArrayList<String> aux2 = new ArrayList<>();
            for (int i = startDay; i <= monthQuantity.get(monthsDays.get(startMonth)); i++) {
                aux2.add(Integer.toString(i));
            }
            //Adds this list to the first month and his corresponding year
            daysDataMap.put(indexes.get(startMonth) + Integer.toString(startYear), aux2);
            //Adds the list of days of the next months until the year ends
            for (int i = (startMonth + 1); i < monthsPerYear; i++) {
                daysDataMap.put(indexes.get(i) + Integer.toString(startYear), daysInMonthMap.get(indexes.get(i)));
            }
            // i goes over the years and j goes over the months
            for (int i = (startYear + 1); i < endYear; i++) {
                for (int j = 0; j < monthsPerYear; j++) {
                    daysDataMap.put(indexes.get(j) + Integer.toString(i), daysInMonthMap.get(indexes.get(j)));
                }
            }
            //Adds the needed months before the last because it can have less days
            for (int i = 0; i < endMonth; i++) {
                daysDataMap.put(indexes.get(i) + Integer.toString(endYear), daysInMonthMap.get(indexes.get(i)));
            }
            //Adds the last month
            aux2 = new ArrayList<>();
            for (int i = 1; i <= endDay; i++) {
                aux2.add(Integer.toString(i));
            }
            daysDataMap.put(indexes.get(endMonth) + Integer.toString(endYear), aux2);
            //Produces the months for all the years except the first and the last
            aux2 = new ArrayList<>();
            for (int i = 0; i < monthsPerYear; i++) {
                aux2.add(indexes.get(i));
            }
            for (int i = (startYear + 1); i < endYear; i++) {
                monthsDataMap.put(Integer.toString(i), aux2);
            }
            //Produces the months of the first year
            aux2 = new ArrayList<>();
            for (int i = startMonth; i < monthsPerYear; i++) {
                aux2.add(indexes.get(i));
            }
            monthsDataMap.put(Integer.toString(startYear), aux2);
            //Produces the months of the last year
            aux2 = new ArrayList<>();
            for (int i = 0; i <= endMonth; i++) {
                aux2.add(indexes.get(i));
            }
            monthsDataMap.put(Integer.toString(endYear), aux2);

        }

        yearsData = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            yearsData.add(Integer.toString(i));
        }
        //TODO comprobar si es bisiesto y ver en febrero
    }

    @Override
    public void whenSelected(AbstractGradientRecyclerView aRecycler, View view, int index) {
        scrollingDays = false;
        scrollingMonths = false;
        scrollingYears = false;
        /*
        ((AbstractGradientRecyclerView.AbstractGradientLayoutManager) days.getLayoutManager()).setCanScroll(true);
        ((AbstractGradientRecyclerView.AbstractGradientLayoutManager) months.getLayoutManager()).setCanScroll(true);
        ((AbstractGradientRecyclerView.AbstractGradientLayoutManager) years.getLayoutManager()).setCanScroll(true);
        /*
        SimpleStringRecyclerView.StringAdapter daysAdapter = (SimpleStringRecyclerView.StringAdapter) days.getAdapter();
        SimpleStringRecyclerView.StringAdapter monthsAdapter = (SimpleStringRecyclerView.StringAdapter) months.getAdapter();
        if (days == aRecycler) {
        } else if (months == aRecycler) {
            daysAdapter.setList(daysDataMap.get(months.getSelectedString() + years.getSelectedString()));
            daysAdapter.notifyDataSetChanged();
            days.smoothScrollToPosition(0);
        } else if (years == aRecycler) {
            monthsAdapter.setList(monthsDataMap.get(years.getSelectedString()));
            monthsAdapter.notifyDataSetChanged();
            daysAdapter.setList(daysDataMap.get(monthsDataMap.get(years.getSelectedString()).get(0) + years.getSelectedString()));
            daysAdapter.notifyDataSetChanged();
            months.smoothScrollToPosition(0);
            days.smoothScrollToPosition(0);
        }
        */
    }

    @Override
    public void whenScrolled(AbstractGradientRecyclerView recyclerView, View selectedView, int selectedViewIndex) {
        if (recyclerView == days) {
            int diff = Integer.parseInt(longDaysList.get(previousDaysPosition)) - Integer.parseInt(longDaysList.get(selectedViewIndex));
            if (Math.abs(diff) > 4) {
                if (diff > 0) {
                    if (months.getSelectedViewIndex() < months.getAdapter().getItemCount() - 1) {
                        months.smoothScrollToPosition(months.getSelectedViewIndex() + 1);
                    }
                } else {
                    if (months.getSelectedViewIndex() >= 1) {
                        months.smoothScrollToPosition(months.getSelectedViewIndex() - 1);
                    }
                }


            }
            previousDaysPosition = selectedViewIndex;
            //((LinearLayoutManager)(months.getLayoutManager())).scrollToPositionWithOffset(selectedViewIndex%months.getAdapter().getItemCount(),months.getEndOffset());
        } else if (recyclerView == months && scrollingMonths) {
            //if(previousMonthsPosition != selectedViewIndex){
            //previousMonthsPosition = selectedViewIndex;
            //days.smoothScrollToPosition(selectedViewIndex % months.getAdapter().getItemCount());
            //}
        }
    }

    @Override
    public void whenScrollStateChanged(int newState, AbstractGradientRecyclerView recyclerView, View selectedView, int selectedViewIndex) {
/*
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
            if (days == recyclerView) {
                scrollingDays = true;
                scrollingMonths = false;
                scrollingYears = false;
            } else if (months == recyclerView) {
                scrollingDays = false;
                scrollingMonths = true;
                scrollingYears = false;
            } else if (years == recyclerView) {
                scrollingDays = false;
                scrollingMonths = false;
                scrollingYears = true;
            }
        }
        */

    }

    public String getDateAsFormattedString() {
        return days.getSelectedString() + "/" + Integer.toString(indexes.indexOf(months.getSelectedString()) + 1) + "/" + years.getSelectedString();
    }

}
