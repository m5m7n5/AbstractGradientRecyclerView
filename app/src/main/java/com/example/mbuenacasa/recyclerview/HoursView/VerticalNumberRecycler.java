package com.example.mbuenacasa.recyclerview.HoursView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbuenacasa.recyclerview.MyCustomRecyclerView;
import com.example.mbuenacasa.recyclerview.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbuenacasa on 1/07/16.
 */
public class VerticalNumberRecycler extends MyCustomRecyclerView{

    public void initRecyclerAsVerticalNumberRecycler(@NonNull RecyclerView rv, @NonNull Context context,
                                                     int centerColor, int sideColor,List<String> numbers){
        initCustomRecyclerView(rv,new VerticalNumberAdapter(numbers,context),context, LinearLayoutManager.VERTICAL,centerColor,sideColor);


    }

    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(R.id.hoursTextView);
        tv.setTextColor(c);
    }

    public List<String> generateHoursNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i=0;i<24;i++){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    public List<String> generateMinutesNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i=0;i<60;i++){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    public class NumberHolder extends CustomViewHolder{
        TextView numbers;

        public NumberHolder(View itemView) {
            super(itemView);
            numbers = (TextView) itemView.findViewById(R.id.hoursTextView);
        }
    }

    public class VerticalNumberAdapter extends RecyclerView.Adapter<NumberHolder>{

        List<String> list = Collections.emptyList();
        Context context;

        public VerticalNumberAdapter(List<String> list,Context context){
            this.list = list;
            this.context = context;
        }

        @Override
        public NumberHolder onCreateViewHolder(ViewGroup parent,int viewType){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hours_format,parent,false);
            NumberHolder holder = new NumberHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(NumberHolder holder, int position) {
            holder.numbers.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }



}
