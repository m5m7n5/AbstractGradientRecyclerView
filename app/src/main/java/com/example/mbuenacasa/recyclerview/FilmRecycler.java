package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by mbuenacasa on 5/07/16.
 */
public class FilmRecycler extends AbstractGradientRecyclerView {

    public void initRecyclerAsFilmRecycler(
            @NonNull RecyclerView rv, @NonNull Context context,
            int centerColor, int sideColor,List<Data> lst){
        initCustomRecyclerView(rv,new FilmRecyclerAdapter(lst,context),context, LinearLayoutManager.HORIZONTAL,centerColor,sideColor);
    }

    @Override
    public void whenSelected(View v){
    }

    @Override
    protected void changeColorFromView(View v, int c){
        TextView tv = (TextView) v.findViewById(R.id.title);
        tv.setTextColor(c);
        tv = (TextView) v.findViewById(R.id.description);
        tv.setTextColor(c);
    }

    public class FilmHolder extends AbstractGradientRecyclerViewHolder implements View.OnClickListener,View.OnTouchListener{
        TextView title;
        TextView description;

        public FilmHolder(View itemView){
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            listenersInit();
        }

        private void listenersInit(){
            title.setOnClickListener(this);
            description.setOnClickListener(this);
            title.setOnTouchListener(this);
            description.setOnTouchListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view  == title){
                description.setText("Oh, you touch my tralala");
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                description.setText("YEEEEEEEEEEEEEEEEEES");
            }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                description.setText("NOOOOOOOOOOOOOOOOOO");
            }else if(motionEvent.getAction() == MotionEvent.ACTION_CANCEL){
                description.setText("You canceled, so Rude");
            }
            return false;
        }

    }

    public class FilmRecyclerAdapter extends AbstractGradientRecyclerAdapter<FilmHolder>{

        List<Data> list = Collections.emptyList();
        Context context;

        public FilmRecyclerAdapter(List<Data> list,Context context){
            this.list = list;
            this.context = context;
        }

        @Override
        public FilmHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.film_format,parent,false);
            FilmHolder holder = new FilmHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(FilmHolder holder, int position) {
            holder.title.setText(list.get(position).title);
            holder.description.setText(list.get(position).description);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }


    }

}
