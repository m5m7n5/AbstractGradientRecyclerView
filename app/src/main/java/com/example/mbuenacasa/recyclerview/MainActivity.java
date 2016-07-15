package com.example.mbuenacasa.recyclerview;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textForDebug;
    CountDownTimer countDown;
    boolean counting;
    boolean decrementing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //android.app.FragmentManager fragmentManager = getFragmentManager();
        //android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //MyCustomFragment myCustomFragment = new MyCustomFragment();
        //fragmentTransaction.add(R.id.fragmentlayout,myCustomFragment);
        //fragmentTransaction.commit();

        textForDebug = (TextView) findViewById(R.id.textForDebug);
        counting = false;
        decrementing = true;

        MonthRecyclerView m = (MonthRecyclerView) findViewById(R.id.new_month_recycler);

        countDown = new CountDownTimer(10000,1000) {

            public void onTick(long millisUntilFinished) {
                if(decrementing) {
                    //hoursView.softMinutesDecrement();
                }else{
                    //hoursView.softMinutesIncrement();
                }
            }

            public void onFinish() {
                //textForDebug.setText(Integer.toString(hoursView.getSelectedHour()));
                counting = false;
                ((Button)(findViewById(R.id.buttonCountdown))).setText("Start counting");
            }

        };

        (findViewById(R.id.buttonCountdown)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!counting) {
                    countDown.start();
                    counting = true;
                    ((Button)view).setText("Cancel counting");
                }else{
                    counting = false;
                    countDown.cancel();
                    ((Button)view).setText("Start counting");
                }
            }
        });
        (findViewById(R.id.buttonReverse)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(decrementing){
                    ((Button)view).setText("Incrementing");
                }else{
                    ((Button)view).setText("Decrementing");
                }
                textForDebug.setText(((DatePickerView)findViewById(R.id.date_picker)).getDateAsFormatedString()+" "+((TimePickerView)findViewById(R.id.hoursView)).getCurrentTimeAsString());
                decrementing=!decrementing;
            }
        });

    }

}
