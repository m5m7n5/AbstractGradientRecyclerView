package com.example.mbuenacasa.recyclerview.HoursView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbuenacasa.recyclerview.AbstractGradientRecyclerView;
import com.example.mbuenacasa.recyclerview.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mbuenacasa on 1/07/16.
 */
public class VerticalStringRecycler extends AbstractGradientRecyclerView {

    private Communicator communicator;

    public void initRecyclerAsVerticalNumberRecycler(@NonNull RecyclerView rv, @NonNull Context context,
                                                     int centerColor, int sideColor,List<String> stringList){
        initCustomRecyclerView(rv,new VerticalStringAdapter(stringList,context),context, LinearLayoutManager.VERTICAL,centerColor,sideColor);
        communicator = null;

    }

    public void setCommunicator(Communicator communicator){
        this.communicator = communicator;
    }

    @Override
    public void whenSelected(View v){
        if(communicator!=null){
            communicator.selectionChanged(this,v,selectedViewIndex);
        }
    }

    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(R.id.vertical_recycler_view_text);
        tv.setTextColor(c);
    }

    public List<String> generateHoursNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i=23;i>=0;i--){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    public List<String> generateMinutesNumbers(){
        List<String> aux = new ArrayList<>();
        for(int i=59;i>=0;i--){
            aux.add(Integer.toString(i));
        }
        return aux;
    }

    public class NumberHolder extends AbstractGradientRecyclerViewHolder {
        TextView numbers;

        public NumberHolder(View itemView) {
            super(itemView);
            numbers = (TextView) itemView.findViewById(R.id.vertical_recycler_view_text);
        }
    }

    public class VerticalStringAdapter extends AbstractGradientRecyclerAdapter<NumberHolder> {

        List<String> list = Collections.emptyList();
        Context context;

        public VerticalStringAdapter(List<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public NumberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_vertical_string, parent, false);
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

    public String getSelectedString(){
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        return ((TextView)lm.getChildAt(nearestView(lm)).findViewById(R.id.vertical_recycler_view_text)).getText().toString();
    }

    public interface Communicator {

        void selectionChanged(AbstractGradientRecyclerView aRecycler, View view,int index);

    }

}
