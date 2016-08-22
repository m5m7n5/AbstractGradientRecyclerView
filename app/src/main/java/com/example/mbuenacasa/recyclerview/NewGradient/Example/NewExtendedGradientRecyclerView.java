package com.example.mbuenacasa.recyclerview.NewGradient.Example;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mbuenacasa.recyclerview.NewGradient.NewGradientRecyclerView;
import com.example.mbuenacasa.recyclerview.R;

/**
 * Created by mbuenacasa on 22/08/16.
 */
public class NewExtendedGradientRecyclerView extends NewGradientRecyclerView {

    public NewExtendedGradientRecyclerView(Context context) {
        super(context);
    }

    public NewExtendedGradientRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NewExtendedGradientRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        this.setLayoutManager(linearLayoutManager);
        this.setAdapter(new NewExtendedAdapter());
        this.addItemDecoration(new GradientItemDecoration());
    }

    public class NewExtendedAdapter extends GradientViewAdapter<NewItemHolder,String>{

        @Override
        public NewItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_new_string, parent, false);
            NewItemHolder holder = new NewItemHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(NewItemHolder holder, int position) {
            holder.decorate(list.get(position));
        }

    }


    public class NewItemHolder extends GradientViewHolder<String>{

        public TextView textView;

        public NewItemHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.asdf);
        }

        @Override
        public void decorate(String itemList) {
            textView.setText(itemList);
        }
    }

    public class GradientItemDecoration extends ItemDecoration{

        @Override
        public void onDraw(Canvas c, RecyclerView parent, State state) {
            for(int i=0;i<parent.getChildCount();i++){
                parent.getChildAt(i).invalidate();
            }
        }
    }

}
