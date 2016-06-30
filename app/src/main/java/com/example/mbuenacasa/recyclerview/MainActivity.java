package com.example.mbuenacasa.recyclerview;

import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

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
        //recyclerView1.getLayoutManager().scrollToPosition(5);
        recyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
                int length = l.getChildCount();
                for(int i=1;i<length-1;i++){
                    l.getChildAt(i).setAlpha(1);
                }
                int mediumval = length/2;
                Rect first = new Rect();
                Rect last = new Rect();
                Rect medium = new Rect();
                l.getChildAt(0).getGlobalVisibleRect(first);
                l.getChildAt(length-1).getGlobalVisibleRect(last);
                l.getChildAt(mediumval).getGlobalVisibleRect(medium);
                float alfavalue = (float) (first.right-first.left)/(medium.right-medium.left);
                l.getChildAt(0).setAlpha(alfavalue*alfavalue);
                alfavalue = (float) (last.right-last.left)/(medium.right-medium.left);
                l.getChildAt(length-1).setAlpha(alfavalue*alfavalue);



                //Descomentar para que cambie mientras me muevo por la lista y comentar las siguientes lineas
                changeColorFromView(l.getChildAt(mediumval-1),getResources().getColor(R.color.white));
                //changeColorFromView(l.getChildAt(mediumval),getResources().getColor(R.color.pink));
                changeColorFromView(l.getChildAt(mediumval+1),getResources().getColor(R.color.white));
                //changeColorFromView(l.getChildAt(mediumval),getResources().getColor(R.color.white));


            }
        });
        recyclerView1.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                final int action = MotionEventCompat.getActionMasked(e);

                if(action==MotionEvent.ACTION_UP || action==MotionEvent.ACTION_CANCEL){
                    e.getAction();
                    LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
                    lm.getChildCount();
                    int medium = lm.getChildCount()/2;
                    Rect auxiliarRectangle = new Rect();
                    Log.v("asd","asd");
                    int [] center = getAbsoluteCenter(lm);
                    int previous = medium-1;
                    Rect previousRect = new Rect();
                    lm.getChildAt(previous).getGlobalVisibleRect(previousRect);
                    for(int i=(medium);i<medium+2;i++){
                        lm.getChildAt(i).getGlobalVisibleRect(auxiliarRectangle);
                        if(Math.abs(auxiliarRectangle.centerX()-center[0])<=Math.abs(previousRect.centerX()-center[0])){
                            previous = i;
                            lm.getChildAt(previous).getGlobalVisibleRect(previousRect);
                        }
                    }
                    lm.getChildAt(previous).getGlobalVisibleRect(previousRect);
                    int offset = previousRect.centerX()-center[0];
                    rv.scrollBy(offset,0);
                    changeColorFromView(lm.getChildAt(previous),getResources().getColor(R.color.pink));
                    return false;
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

    private void changeColorFromView(View v, int c){
        TextView title = (TextView) v.findViewById(R.id.title);
        title.setTextColor(c);
        TextView description = (TextView) v.findViewById(R.id.description);
        description.setTextColor(c);
    }

    private int [] getAbsoluteCenter(LinearLayoutManager lm){
        int width = lm.getWidth();
        int height = lm.getHeight();
        int [] values = {0,0};
        Rect first = new Rect();
        Rect last = new Rect();
        lm.getChildAt(0).getGlobalVisibleRect(first);
        lm.getChildAt(lm.getChildCount()-1).getGlobalVisibleRect(last);
        values[0] = first.left+width/2;
        values[1] = last.top+height/2;
        return values;
    }
}
