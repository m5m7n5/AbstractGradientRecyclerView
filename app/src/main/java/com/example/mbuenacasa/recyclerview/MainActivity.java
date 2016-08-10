package com.example.mbuenacasa.recyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MonthRecyclerView month;
    DatePickerView date;
    TimePickerView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MyCustomFragment myCustomFragment = new MyCustomFragment();
        fragmentTransaction.add(R.id.fragment_layout, myCustomFragment);
        fragmentTransaction.commit();

        month = (MonthRecyclerView) findViewById(R.id.month_view);
        date = (DatePickerView) findViewById(R.id.date_picker_view);
        time = (TimePickerView) findViewById(R.id.time_picker_view);

        BaseDragView b = (BaseDragView) findViewById(R.id.base_drag_view);
        b.inflate(R.layout.draggable_try);

        BaseCollapsableView c = (BaseCollapsableView) findViewById(R.id.base_collapsable_view);
        c.inflate(R.layout.collapsable_try);
        c = (BaseCollapsableView) findViewById(R.id.base_collapsable_view2);
        c.inflate(R.layout.collapsable_try);
    }
}
