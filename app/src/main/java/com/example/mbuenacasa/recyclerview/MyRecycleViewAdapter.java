package com.example.mbuenacasa.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mbuenacasa on 29/06/16.
 */
public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyDataObjectHolder> {
    @Override
    public MyDataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyDataObjectHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyDataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MyDataObjectHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
