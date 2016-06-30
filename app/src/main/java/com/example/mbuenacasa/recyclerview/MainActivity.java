package com.example.mbuenacasa.recyclerview;

import android.graphics.Rect;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {



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

        final RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview);
        MyRecycleViewAdapter adapter = new MyRecycleViewAdapter(data, getApplication());
        recyclerView1.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(llm);
        recyclerView1.getLayoutManager().scrollToPosition(5);
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int length = l.getChildCount();
                int width = recyclerView.getWidth();
                int height =  recyclerView.getHeight();
                for(int i=1;i<length-1;i++){
                    l.getChildAt(i).setAlpha(1);
                }
                l.getChildAt(0).setAlpha((float) 0.25);
                l.getChildAt(length-1).setAlpha((float) 0.25);

                //Rect r11 = new Rect();
                //l.getChildAt(firstComplete).getGlobalVisibleRect(r11);

                /*
                for(int i=0;i<length;i++){
                    l.getChildAt(i).getGlobalVisibleRect();
                }
                */
            }
        });
        recyclerView1.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                if(e.getAction()==MotionEvent.ACTION_UP){
                    e.getAction();
                    LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                    lm.getChildCount();
                    //lm.scrollToPosition(2);
                    //lm.scrollToPositionWithOffset(2,100/*Esto son píxeles*/);
                    //Centrar el la lista
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



        /*
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerview2);
        MyRecycleViewAdapter adapter2 = new MyRecycleViewAdapter(data, getApplication());
        recyclerView2.setAdapter(adapter2);
        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(llm2);
        recyclerView2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lenght = l.getChildCount();
                for(int i=1;i<lenght-1;i++){
                    l.getChildAt(i).setAlpha(1);
                }
                l.getChildAt(0).setAlpha((float) 0.25);
                l.getChildAt(lenght-1).setAlpha((float) 0.25);
            }
        });
        */

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
