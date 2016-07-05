package com.example.mbuenacasa.recyclerview;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mbuenacasa.recyclerview.HoursView.HoursView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    HoursView hoursView;
    TextView textForDebug;
    CountDownTimer countDown;
    boolean counting;
    boolean decrementing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MyCustomFragment myCustomFragment = new MyCustomFragment();
        fragmentTransaction.add(R.id.fragmentlayout,myCustomFragment);
        fragmentTransaction.commit();

        List<Data> data = fill_with_data();

        RecyclerView rf = (RecyclerView) findViewById(R.id.recyclerview);
        FilmRecycler f = new FilmRecycler();
        f.initRecyclerAsFilmRecycler(rf,this,getResources().getColor(R.color.pink),getResources().getColor(R.color.white),data);
        RecyclerView rm = (RecyclerView) findViewById(R.id.monthRecyclerVew);
        MonthRecycler m = new MonthRecycler();
        m.initRecyclerAsMonthRecycler(rm,this,getResources().getColor(R.color.yellow),getResources().getColor(R.color.white));

        hoursView = (HoursView) findViewById(R.id.hoursView);
        hoursView.setSelectedHour(5);
        hoursView.setSelectedMinute(7);

        textForDebug = (TextView) findViewById(R.id.textForDebug);
        counting = false;
        decrementing = true;
        countDown = new CountDownTimer(10000,1000) {

            public void onTick(long millisUntilFinished) {
                if(decrementing) {
                    hoursView.softMinutesDecrement();
                }else{
                    hoursView.softMinutesIncrement();
                }
            }

            public void onFinish() {
                textForDebug.setText("Done!");
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
                decrementing=!decrementing;
            }
        });

    }

    public List<Data> fill_with_data() {

        List<Data> data = new ArrayList<>();

        data.add(new Data("Batman vs Superman", "Following the destruction of Metropolis, Batman embarks on a personal vendetta against Superman "));
        data.add(new Data("X-Men: Apocalypse", "X-Men: Apocalypse is an upcoming American superhero film based on the X-Men characters that appear in Marvel Comics "));
        data.add(new Data("Captain America: Civil War", "A feud between Captain America and Iron Man leaves the Avengers in turmoil.  "));
        data.add(new Data("Kung Fu Panda 3", "After reuniting with his long-lost father, Po  must train a village of pandas"));
        data.add(new Data("Warcraft", "Fleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. "));
        data.add(new Data("Alice in Wonderland", "Alice in Wonderland: Through the Looking Glass "));
        data.add(new Data("ASDF", "sgasfhafhaembarks on a personal vendetta against Superman "));
        data.add(new Data("ebrbbaerb-Men: Apocalypse", "Xa-: Apocalypse wrthhertyjis an uertjertjert based on the X-Men characters that appear in Marvel Comics "));
        data.add(new Data("Caaptain America: Civil War", "Aa feud between Captain America and Iron Man leaves the Avengers in turmoil.  "));
        data.add(new Data("Kaung Fu Panda 3", "Aafter reuniting with his long-lost father, Po  must train a village of pandas"));
        data.add(new Data("Waarcraft", "Faleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. "));
        data.add(new Data("Aalice in Wonderland", "Aalice in Wonderland: Through the Looking Glass "));
        data.add(new Data("Bsatman vs Superman", "Fsollowing the destruction of Metropolis, Batman embarks on a personal vendetta against Superman "));
        data.add(new Data("Xs-Men: Apocalypse", "Xs-Men: Apocalypse is an upcoming American superhero film based on the X-Men characters that appear in Marvel Comics "));
        data.add(new Data("Csaptain America: Civil War", "As feud between Captain America and Iron Man leaves the Avengers in turmoil.  "));
        data.add(new Data("Ksung Fu Panda 3", "Asfter reuniting with his long-lost father, Po  must train a village of pandas"));
        data.add(new Data("Wsarcraft", "Fsleeing their dying home to colonize another, fearsome orc warriors invade the peaceful realm of Azeroth. "));
        data.add(new Data("Aslice in Wonderland", "Aslice in Wonderland: Through the Looking Glass "));
        return data;
    }


}
