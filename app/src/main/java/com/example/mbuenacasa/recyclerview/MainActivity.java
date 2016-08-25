package com.example.mbuenacasa.recyclerview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mbuenacasa.recyclerview.NewGradient.Example.NewExtendedGradientRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MonthRecyclerView month;
    NewDatePickerView date;
    TimePickerView time;

    NewExtendedGradientRecyclerView recyclerView;

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
        date = (NewDatePickerView) findViewById(R.id.date_picker_view);
        time = (TimePickerView) findViewById(R.id.time_picker_view);

        BaseDragView b = (BaseDragView) findViewById(R.id.base_drag_view);
        b.inflate(R.layout.draggable_try);

        BaseCollapsableView c = (BaseCollapsableView) findViewById(R.id.base_collapsable_view);
        c.inflate(R.layout.collapsable_try);
        c = (BaseCollapsableView) findViewById(R.id.base_collapsable_view2);
        c.inflate(R.layout.collapsable_try);

        recyclerView = (NewExtendedGradientRecyclerView) findViewById(R.id.new_gradient_recycler_view);
        List<String> asd = new ArrayList<>();
        for(int i = 0;i<10;i++){
            asd.add(Integer.toString(i));
        }
        recyclerView.setAdapterList(asd);
    }
}
