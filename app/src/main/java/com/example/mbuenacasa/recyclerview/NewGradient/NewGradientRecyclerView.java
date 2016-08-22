package com.example.mbuenacasa.recyclerview.NewGradient;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
    private ItemDecoration decoration = null;

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
        offset();
    }

    public class OffsetStartEndItemDecorator extends RecyclerView.ItemDecoration {

        private int startOffset;
        private int endOffset;
        private int orientation;

        /**
         * Constructor of the class
         *
         * @param startOffset
         * @param endOffset
         * @param orientation
         */
        public OffsetStartEndItemDecorator(int startOffset, int endOffset, int orientation) {
            this.startOffset = startOffset + 1;
            this.orientation = orientation;
            this.endOffset = endOffset + 1;
        }

        /**
         * Method that let's the first and the last view scroll a little more than the size of the list
         * it depends on the orientation of the recyclerView
         *
         * @param outRect
         * @param view
         * @param parent
         * @param state
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            int itemCount = state.getItemCount();
            if (parent.getChildAdapterPosition(view) == itemCount - 1) {
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    if (endOffset > 0) {
                        outRect.right = endOffset;
                    }
                } else if (orientation == LinearLayoutManager.VERTICAL) {
                    if (endOffset > 0) {
                        outRect.bottom = endOffset;
                    }
                }
            }
            if (parent.getChildAdapterPosition(view) == 0) {
                if (orientation == LinearLayoutManager.HORIZONTAL) {
                    if (startOffset > 0) {
                        outRect.left = startOffset;
                    }
                } else if (orientation == LinearLayoutManager.VERTICAL) {
                    if (startOffset > 0) {
                        outRect.top = startOffset;
                    }
                }
            }
        }
    }

    protected void offset() {

        if(decoration != null){
            this.removeItemDecoration(decoration);
        }
        RecyclerView.Adapter adapter = getAdapter();
        GradientViewHolder firstViewHolder = (GradientViewHolder) adapter.createViewHolder(this, 0);
        adapter.onBindViewHolder(firstViewHolder, 0);
        firstViewHolder.itemView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        GradientViewHolder lastViewHolder = (GradientViewHolder) adapter.createViewHolder(this, 0);
        adapter.onBindViewHolder(lastViewHolder, adapter.getItemCount() - 1);
        lastViewHolder.itemView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        int startOffset = 0;
        int endOffset = 0;
        if (((LinearLayoutManager)getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
            int viewWidth = this.getWidth();
            startOffset = (viewWidth - firstViewHolder.itemView.getMeasuredWidth()) / 2;
            endOffset = (viewWidth - lastViewHolder.itemView.getMeasuredWidth()) / 2;
        } else {
            int viewHeight = this.getHeight();
            startOffset = (viewHeight - firstViewHolder.itemView.getMeasuredHeight()) / 2;
            endOffset = (viewHeight - lastViewHolder.itemView.getMeasuredHeight()) / 2;
        }
        decoration = new OffsetStartEndItemDecorator(startOffset, endOffset, ((LinearLayoutManager)getLayoutManager()).getOrientation());
        this.addItemDecoration(decoration);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        offset();
    }
}
