package com.example.mbuenacasa.recyclerview.NewGradient;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbuenacasa on 22/08/16.
 */
public abstract class NewGradientRecyclerView extends RecyclerView {

    protected Context mContext;

    public NewGradientRecyclerView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public NewGradientRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public NewGradientRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    protected abstract void init();

    public abstract class GradientViewHolder<Item> extends RecyclerView.ViewHolder {

        public GradientViewHolder(View itemView) {
            super(itemView);
            if(!(itemView instanceof GradientContainerView)){
                throw new RuntimeException("Item view in gradientViewHolder is not a GradientContainerView");
            }
        }

        public abstract void decorate(Item itemList);
    }

    public abstract class GradientViewAdapter<VH extends GradientViewHolder<Item>,Item> extends RecyclerView.Adapter<VH>{

        protected List<Item> list;

        public GradientViewAdapter(){
            list = new ArrayList<>();
        }

        public final int getItemCount(){
            return list.size();
        }

        public final void setList(List<Item> list){
            this.list = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                this.list.add(list.get(i));
            }
        }

    }

    @Override
    public final void setAdapter(Adapter adapter) {
        setAdapter((GradientViewAdapter) adapter);
    }

    private void setAdapter(GradientViewAdapter adapter){
        super.setAdapter(adapter);
    }

    public void setAdapterList(List<?> list){
        ((GradientViewAdapter) getAdapter()).setList(list);
    }
}
