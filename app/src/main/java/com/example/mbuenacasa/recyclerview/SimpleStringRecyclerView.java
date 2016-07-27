package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbuenacasa on 27/07/16.
 */
public class SimpleStringRecyclerView extends AbstractGradientRecyclerView {

    //String holder needs to be inside stringHolderContainerFilename
    @IdRes
    private int stringHolder;

    @LayoutRes
    private int stringHolderContainerFilename;


    public SimpleStringRecyclerView(Context context) {
        super(context);
        init();
    }

    public SimpleStringRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleStringRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Initializes the adapter with a default list
     */
    private void init(){
        ArrayList<String> list = new ArrayList<>();
        list.add("asdf1");
        list.add("asdf2");
        list.add("asdf3");
        list.add("asdf4");
        list.add("asdf5");

        setAdapterList(list);
    }

    /**
     * Changes the adapter list, a call to notifyDataSetChanged should be done after this method
     * @param list list for the adapter
     */
    public void setAdapterList(List<String> list) {
        this.setAdapter(new StringAdapter(context));
        ((StringAdapter)this.getAdapter()).setList(list);
    }

    public void setStringHolder(@IdRes int res){
        stringHolder = res;
    }

    public void setStringHolderContainerFilename(@LayoutRes int res){
        stringHolderContainerFilename = res;
    }

    @Override
    protected void whenSelected(View v) {

    }

    @Override
    protected void changeColorFromView(View v, int c) {
        TextView tv = (TextView) v.findViewById(stringHolder);
        tv.setTextColor(c);
    }

    private class StringHolder extends AbstractGradientViewHolder{

        TextView textView;

        public StringHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(stringHolder);
        }
    }

    public class StringAdapter extends AbstractGradientAdapter<StringHolder,String>{

        public StringAdapter(Context context) {
            super(context);
            this.list = new ArrayList<>();
            this.context = context;
        }

        @Override
        public StringHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(stringHolderContainerFilename, parent, false);
            StringHolder holder = new StringHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(StringHolder holder, int position) {
            if(position >= 0 && position < list.size()) {
                holder.textView.setText(list.get(position));
            }
        }
    }

    public String getSelectedString(){
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        return ((TextView)lm.getChildAt(nearestView(recyclerView)).findViewById(stringHolder)).getText().toString();
    }

}