package com.example.mbuenacasa.recyclerview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mbuenacasa on 30/06/16.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener{
    TextView title;
    TextView description;

    MyViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
        listenersAsignation();
    }

    private void listenersAsignation(){
        title.setOnClickListener(this);
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
        }
        return false;
    }
}
