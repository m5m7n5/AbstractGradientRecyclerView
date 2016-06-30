package com.example.mbuenacasa.recyclerview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by mbuenacasa on 30/06/16.
 */
public class MyViewHolder extends RecyclerView.ViewHolder{
    CardView cv;
    TextView title;
    TextView description;

    MyViewHolder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.description);
    }
}
