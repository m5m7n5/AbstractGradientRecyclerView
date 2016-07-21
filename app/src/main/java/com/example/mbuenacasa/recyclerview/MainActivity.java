package com.example.mbuenacasa.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MonthRecyclerView month,month2;
    DatePickerView date;
    TimePickerView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MyCustomFragment myCustomFragment = new MyCustomFragment();
        fragmentTransaction.add(R.id.fragment_layout,myCustomFragment);
        fragmentTransaction.commit();

        month = (MonthRecyclerView) findViewById(R.id.month_view);
        date = (DatePickerView) findViewById(R.id.date_picker_view);
        time = (TimePickerView) findViewById(R.id.time_picker_view);

        month2 = (MonthRecyclerView) findViewById(R.id.month_view2);
        List<String> asd = new ArrayList<>();
        asd.add("SAD");
        asd.add("SADNESS");
        asd.add("SADLY");
        month2.setAdapterList(asd);
        time.getHoursRecycler().setAdapterList(asd);

    }
}
