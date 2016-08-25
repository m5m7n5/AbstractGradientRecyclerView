package com.example.mbuenacasa.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.Collections;
import java.util.List;

/**
 * Created by mbuenacasa on 13/07/16.
 * This class adds to the RecyclerView items a gradient effect when they enter the view,
 * and it centers the items on the view, the selected item has a center color, and the side items
 * his respective side color.
 * <p/>
 * THIS CLASS NEEDS TO HAVE ON XML MATCH_PARENT OR A FIXED SIZE FOR HIS ORIENTATION, DON'T WRAP CONTENT
 * THE SIZE OF HIS ORIENTATION
 */
public abstract class AbstractGradientRecyclerView extends RecyclerView {

    /**
     * Colors to apply to the views.
     */
    private int centerColor;
    private int sideColor;
    private int whenSelectedColor;

    /**
     * Timer for apply the whenSelectedColor
     */
    private CountDownTimer timer;

    /**
     * Index in the adapter of the selected view index.
     */
    protected int selectedViewIndex;

    /**
     * Current selected view, the view which is in the middle of the recyclerView.
     */
    protected View selectedView;

    /**
     * A reference for itself, used in some functions.
     */
    protected AbstractGradientRecyclerView recyclerView;

    /**
     * Context provided by the parent.
     */
    protected Context context;

    /**
     * Orientation of the recycler.
     */
    private int orientation;

    /**
     * Flag value to know when the recycler has been decorated.
     */
    private boolean decorated;

    /**
     * An instance of a communicator, for communicate outside the Recycler.
     */
    private AbstractGradientRecyclerCommunicator communicator;

    /**
     * Start offset and end offset of the first and last item. To see them centered.
     */
    private int startOffset;
    private int endOffset;

    /**
     * Value to know when can be scrolled the recycler.
     */
    private boolean canScroll;


    private RecyclerView.OnScrollListener scrollListener;

    /**
     * Default constructor of the class
     *
     * @param context
     */
    public AbstractGradientRecyclerView(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Default constructor of the class
     *
     * @param context
     * @param attrs
     */
    public AbstractGradientRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        readXMLAttributes(attrs);
        init();
    }

    /**
     * Default constructor of the class
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AbstractGradientRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        readXMLAttributes(attrs);
        init();
    }

    /**
     * Method to set the communicator.
     *
     * @param communicator
     */
    public void setCommunicator(AbstractGradientRecyclerCommunicator communicator) {
        this.communicator = communicator;
    }

    /**
     * Init method that initializes all the variables and listeners needed.
     */
    private void init() {
        recyclerView = this;
        decorated = false;
        AbstractGradientLayoutManager dynamicLLM = new AbstractGradientLayoutManager(getContext());
        dynamicLLM.setOrientation(orientation);
        dynamicLLM.setCanScroll(canScroll);
        dynamicLLM.setAutoMeasureEnabled(false);
        setLayoutManager(dynamicLLM);
        settingCustomListeners();
        setResponseTime(150);
    }


    private void readXMLAttributes(@Nullable AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AbstractGradientRecyclerView, 0, 0);
            String center = a.getString(R.styleable.AbstractGradientRecyclerView_center_color);
            if (center != null) {
                centerColor = Color.parseColor(center);
            } else {
                centerColor = 0xFF000000;//Black
            }
            String side = a.getString(R.styleable.AbstractGradientRecyclerView_side_color);
            if (side != null) {
                sideColor = Color.parseColor(side);
            } else {
                sideColor = 0xFFFFFFFF;//White
            }
            String whenSelected = a.getString(R.styleable.AbstractGradientRecyclerView_when_selected_color);
            if (whenSelected != null) {
                whenSelectedColor = Color.parseColor(whenSelected);
            } else {
                whenSelectedColor = centerColor;
            }
            String orientation = a.getString(R.styleable.AbstractGradientRecyclerView_orientation);
            if (orientation != null) {
                this.orientation = Integer.parseInt(orientation);
            } else {
                this.orientation = LinearLayoutManager.HORIZONTAL;
            }
            String canScroll = a.getString(R.styleable.AbstractGradientRecyclerView_can_scroll);
            if (canScroll != null) {
                this.canScroll = Boolean.parseBoolean(canScroll);
            } else {
                this.canScroll = true;
            }
            a.recycle();
        }
    }

    /**
     * Method that initializes the timer with the new time as a countdown value.
     *
     * @param time
     */
    public void setResponseTime(int time) {
        timer = new CountDownTimer(time, time) {
            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {
                if (selectedView != null) {
                    getChildViewHolder(selectedView).changeColor(whenSelectedColor);
                }
                getChildViewHolder(selectedView).whenSelected();
                if (communicator != null) {
                    communicator.whenSelected(recyclerView, selectedView, selectedViewIndex);
                }
            }
        };
    }


    @Override
    public AbstractGradientViewHolder getChildViewHolder(View child) {
        return (AbstractGradientViewHolder) super.getChildViewHolder(child);
    }

    /**
     * This method is called when the TreeViewObserver initialized in the init function is called, an is called once, it
     * calculates the offset for the OffsetStartEndItemDecorator, to make the fist and end items scroll a little more,
     * to let them center in the view.
     *
     * @param recyclerView
     */
    protected void offset(RecyclerView recyclerView) {

        RecyclerView.Adapter adapter = getAdapter();
        AbstractGradientViewHolder firstViewHolder = (AbstractGradientViewHolder) adapter.createViewHolder(this, 0);
        adapter.onBindViewHolder(firstViewHolder, 0);
        firstViewHolder.itemView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        AbstractGradientViewHolder lastViewHolder = (AbstractGradientViewHolder) adapter.createViewHolder(this, 0);
        adapter.onBindViewHolder(lastViewHolder, adapter.getItemCount() - 1);
        lastViewHolder.itemView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

        if (orientation == LinearLayoutManager.HORIZONTAL) {
            int viewWidth = recyclerView.getWidth();
            startOffset = (viewWidth - firstViewHolder.itemView.getMeasuredWidth()) / 2;
            endOffset = (viewWidth - lastViewHolder.itemView.getMeasuredWidth()) / 2;
        } else {
            int viewHeight = recyclerView.getHeight();
            startOffset = (viewHeight - firstViewHolder.itemView.getMeasuredHeight()) / 2;
            endOffset = (viewHeight - lastViewHolder.itemView.getMeasuredHeight()) / 2;
        }
        recyclerView.addItemDecoration(new OffsetStartEndItemDecorator(startOffset, endOffset, orientation));
    }

    /**
     * This method is called when the TreeViewObserver initialized in the init function is called, an is called once, it
     * makes a init of the variables and a scroll to the startPosition.
     *
     * @param recyclerView
     */
    private void onStartRecycler(RecyclerView recyclerView) {
        AbstractGradientLayoutManager lm = (AbstractGradientLayoutManager) recyclerView.getLayoutManager();
        View v = lm.getChildAt(0);
        for (int i = 1; i < lm.getChildCount(); i++) {
            getChildViewHolder(getChildAt(i)).changeColor(sideColor);
        }
        if (v != null) {
            getChildViewHolder(v).changeColor(centerColor);
            selectedView = v;
            selectedViewIndex = recyclerView.getChildAdapterPosition(v);
            timer.start();
        }
    }

    /**
     * Method that iterates over the current visible views of the LinearLayoutManager and
     * returns which is the closest.
     *
     * @param recyclerView recyclerView to search the nearest view
     * @return returns the index of the current visible nearest view
     */
    public static int nearestView(RecyclerView recyclerView) {

        LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstMaxIndex = 0;
        Rect auxRectangle = new Rect();
        Rect firstMaxRectangle = new Rect();
        l.getChildAt(firstMaxIndex).getGlobalVisibleRect(firstMaxRectangle);

        for (int i = 0; i < l.getChildCount(); i++) {
            l.getChildAt(i).getGlobalVisibleRect(auxRectangle);
            if (Math.abs(getDistanceFromCenter(recyclerView, i)) <= Math.abs(getDistanceFromCenter(recyclerView, firstMaxIndex))) {
                firstMaxIndex = i;
                l.getChildAt(firstMaxIndex).getGlobalVisibleRect(firstMaxRectangle);
            }
        }
        return firstMaxIndex;

    }

    public int getOrientation() {
        return orientation;
    }

    /**
     * Method for initialize the scrollListener to do the effect.
     */
    private void settingCustomListeners() {

        scrollListener = new RecyclerView.OnScrollListener() {
            /**
             * This method changes the color and the alpha value of  the visible views to make
             * the desired effect.
             * @param recyclerView
             * @param dx
             * @param dy
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*
                For the correct working of this method, always should be a full element on the view.
                 */
                timer.cancel();

                updateGradientColors(recyclerView);

                selectedView = recyclerView.getChildAt(nearestView(recyclerView));
                selectedViewIndex = recyclerView.getChildAdapterPosition(selectedView);
                if (communicator != null) {
                    communicator.whenScrolled((AbstractGradientRecyclerView) recyclerView, selectedView, selectedViewIndex);
                }
            }

            /**
             * Method that manages the behaviour of the recycler when the user stops scrolling,
             * basically it centers the item and changes the color of the designed views, and starts
             * the timer for whenSelected method.
             * @param recyclerView
             * @param newState
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int nearest = nearestView(recyclerView);
                    int offset = getDistanceFromCenter(recyclerView, nearest);
                    if (getOrientation() == LinearLayoutManager.HORIZONTAL) {
                        recyclerView.smoothScrollBy(offset, 0);
                    } else {
                        recyclerView.smoothScrollBy(0, offset);
                    }
                    updateGradientColors(recyclerView);
                    selectedView = recyclerView.getChildAt(nearest);
                    selectedViewIndex = recyclerView.getChildAdapterPosition(selectedView);
                    timer.start();
                }
                if (communicator != null) {
                    communicator.whenScrollStateChanged(newState, (AbstractGradientRecyclerView) recyclerView, selectedView, selectedViewIndex);
                }
            }
        };
        addOnScrollListener(scrollListener);

    }


    private void updateGradientColors(RecyclerView recyclerView) {
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            Rect childRect = new Rect();
            child.getGlobalVisibleRect(childRect);
            Rect parentRect = new Rect();
            RecyclerView parent = (RecyclerView) child.getParent();
            parent.getGlobalVisibleRect(parentRect);
            if (((LinearLayoutManager) parent.getLayoutManager()).getOrientation() == LinearLayoutManager.HORIZONTAL) {
                int parentCenterX = parentRect.centerX();
                int childCenterX = childRect.centerX();
                int childRelativePosition = childCenterX - parentCenterX;
                int childWidth = childRect.right - childRect.left;
                if ((childWidth / 2) > 0) {
                    float childRelativePositionScaled = 2 * (childRelativePosition / (float) childWidth);
                    int color = generateGradientColor(centerColor, sideColor, (childRelativePositionScaled * childRelativePositionScaled));
                    ((AbstractGradientViewHolder) recyclerView.getChildViewHolder(child)).changeColor(color);
                }
                float alpha = (childWidth / (float) child.getMeasuredWidth()) * (childWidth / (float) child.getMeasuredWidth());
                child.setAlpha(alpha);
            } else {
                int parentCenterY = parentRect.centerY();
                int childCenterY = childRect.centerY();
                int childRelativePosition = childCenterY - parentCenterY;
                int childHeight = childRect.bottom - childRect.top;
                if ((childHeight / 2) > 0) {
                    float childRelativePositionScaled = 2 * (childRelativePosition / (float) childHeight);
                    int color = generateGradientColor(centerColor, sideColor, (childRelativePositionScaled * childRelativePositionScaled));
                    ((AbstractGradientViewHolder) recyclerView.getChildViewHolder(child)).changeColor(color);
                }
                float alpha = (childHeight / (float) child.getMeasuredHeight()) * (childHeight / (float) child.getMeasuredHeight());
                child.setAlpha(alpha);

            }
        }
    }

    /**
     * Method that returns a value of color between fromColor and toColor, depending of the variation
     * value, if variation is greater than 1, it returns toColor value (this method uses alpha value too),
     * if variation is lesser than 0, it returns fromColor value.
     *
     * @param fromColor start color of the gradient
     * @param toColor   end color of the gradient
     * @param variation percentage of the first color
     * @return a color between fromcolor and tocolor
     */
    private int generateGradientColor(int fromColor, int toColor, float variation) {

        if (variation >= 1) {
            return toColor;
        } else if (variation <= 0) {
            return fromColor;
        }
        //Split of the components into A R G B variables for both colors
        int A0 = (fromColor & 0xFF000000) >> 24;
        int R0 = (fromColor & 0x00FF0000) >> 16;
        int G0 = (fromColor & 0x0000FF00) >> 8;
        int B0 = fromColor & 0x000000FF;
        int A1 = (toColor & 0xFF000000) >> 24;
        int R1 = (toColor & 0x00FF0000) >> 16;
        int G1 = (toColor & 0x0000FF00) >> 8;
        int B1 = toColor & 0x000000FF;

        //Getting the color values for the target gradient color
        int totalChangeA = interpolate(A0, A1, variation);
        int totalChangeR = interpolate(R0, R1, variation);
        int totalChangeG = interpolate(G0, G1, variation);
        int totalChangeB = interpolate(B0, B1, variation);

        int auxVar = (((totalChangeR << 8) | totalChangeG) << 8) | totalChangeB;
        return auxVar | (totalChangeA << 24);

    }


    /**
     * Method used in generateGradientColor, it receives two integers and a changeRate value,
     * which needs to be between 0 and 1, and return a value between firs and second, depending
     * of the changeRate.
     *
     * @param first      start value
     * @param second     end value
     * @param changeRate portion of the first respect the second
     * @return a value between first and second
     */
    private int interpolate(int first, int second, float changeRate) {

        if (first < second) {
            return (int) ((second - first) * changeRate) + first;
        } else {
            return (int) ((first - second) * (1 - changeRate)) + second;
        }

    }


    /**
     * Method that returns the distance between an item of the linearLayoutManager and the center
     * of it. (Can be negative)
     *
     * @param rv    recycler view container of the view selected by the index
     * @param index index of the item in the recycler view
     * @return distance from the center
     */
    private static int getDistanceFromCenter(RecyclerView rv, int index) {
        LinearLayoutManager lm = (LinearLayoutManager) rv.getLayoutManager();
        if (lm.getOrientation() == LinearLayoutManager.HORIZONTAL) {
            Rect aux = new Rect();
            if (lm.getChildAt(index) != null) {
                lm.getChildAt(index).getGlobalVisibleRect(aux);
                Rect recyclerRectangle = new Rect();
                rv.getGlobalVisibleRect(recyclerRectangle);
                return aux.centerX() - recyclerRectangle.centerX();
            }
        } else {
            Rect aux = new Rect();
            if (lm.getChildAt(index) != null) {
                lm.getChildAt(index).getGlobalVisibleRect(aux);
                Rect recyclerRectangle = new Rect();
                rv.getGlobalVisibleRect(recyclerRectangle);
                return aux.centerY() - recyclerRectangle.centerY();
            }
        }
        return 0;
    }


    /**
     * Method that receives a view and returns his visible width
     *
     * @param v selected view
     * @return visible width of the view
     */
    private static int getVisibleWidth(View v) {
        Rect aux = new Rect();
        v.getGlobalVisibleRect(aux);
        return aux.width();
    }


    /**
     * Method that receives a view and returns his visible height
     *
     * @param v selected view
     * @return visible height of the view
     */
    private static int getVisibleHeight(View v) {
        Rect aux = new Rect();
        v.getGlobalVisibleRect(aux);
        return aux.height();
    }

    /**
     * Method equals for the abstractGradientRecyclerView
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof AbstractGradientRecyclerView) {
            return o == recyclerView;
        }
        return false;
    }

    /**
     * Override the onMeasure to put a listener, this listener will set the offset decorator, and
     * will apply an start color like an scroll.
     *
     * @param widthSpec
     * @param heightSpec
     */
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (!decorated) {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    offset(recyclerView);
                    onStartRecycler(recyclerView);
                    recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
            decorated = true;
        }
    }

    /**
     * This class is the reponsible of the scroll limit over the recyclers
     */
    public class OffsetStartEndItemDecorator extends RecyclerView.ItemDecoration {

        private int startOffset;
        private int endOffset;
        private int orientation;

        /**
         * Constructor of the class
         *
         * @param startOffset offset applied to the first child of the recycler
         * @param endOffset   offset applied to the last child of the recycler
         * @param orientation orientation for apply the offset to bottom/top
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

    /**
     * Implement this interface if you want to communicate some view between them and when you want to call
     * a method that goes only when scrolled.
     */
    public interface AbstractGradientRecyclerCommunicator {

        void whenSelected(AbstractGradientRecyclerView recyclerView, View selectedView, int selectedViewIndex);

        void whenScrolled(AbstractGradientRecyclerView recyclerView, View selectedView, int selectedViewIndex);

        void whenScrollStateChanged(int newState, AbstractGradientRecyclerView recyclerView, View selectedView, int selectedViewIndex);
    }

    /**
     * This class is used to lock some of the scrolls, is a linearLayoutManager where you can
     * change the value canScroll for make it scroll o for don't let it scroll.
     */
    public class AbstractGradientLayoutManager extends LinearLayoutManager {

        private boolean canScroll;

        /**
         * Default constructor
         *
         * @param context
         */
        public AbstractGradientLayoutManager(Context context) {
            super(context);
        }

        /**
         * Default constructor
         *
         * @param context
         * @param orientation
         * @param reverseLayout
         */
        public AbstractGradientLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        /**
         * Default constructor
         *
         * @param context
         * @param attrs
         * @param defStyleAttr
         * @param defStyleRes
         */
        public AbstractGradientLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        /**
         * Method that is checked when the vertical scroll event is called
         *
         * @return
         */
        @Override
        public boolean canScrollVertically() {
            return canScroll && this.getOrientation() == LinearLayoutManager.VERTICAL;
        }

        /**
         * Method that is checked when the horizontal scroll event is called
         *
         * @return
         */
        @Override
        public boolean canScrollHorizontally() {
            return canScroll && this.getOrientation() == LinearLayoutManager.HORIZONTAL;
        }

        /**
         * Changes the value of canScroll variable to your b variable
         *
         * @param b
         */
        public void setCanScroll(boolean b) {
            canScroll = b;
        }


        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
            RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }

        @Override
        public void scrollToPositionWithOffset(int position, int offset) {
            smoothScrollToPosition(recyclerView, null, position);
        }

        private class TopSnappedSmoothScroller extends LinearSmoothScroller {

            public TopSnappedSmoothScroller(Context context) {
                super(context);

            }

            //MAYBE THIS
            @Override
            protected void updateActionForInterimTarget(Action action) {
                super.updateActionForInterimTarget(action);
            }

            @Override
            protected void onTargetFound(View targetView, State state, Action action) {
                AbstractGradientRecyclerView recyclerView = (AbstractGradientRecyclerView) targetView.getParent();

                final int dx = getHorizontalDistance(targetView);
                final int dy = getVerticalDistance(targetView);

                if (recyclerView.getOrientation() == HORIZONTAL) {
                    final int distance = (int) Math.sqrt(dx * dx);
                    final int time = 10*calculateTimeForDeceleration(distance);
                    if (time > 0) {
                        action.update(dx, 0, time, mDecelerateInterpolator);
                    }
                } else {
                    final int distance = (int) Math.sqrt(dy * dy);
                    final int time = 10*calculateTimeForDeceleration(distance);
                    if (time > 0) {
                        action.update(0, dy, time, mDecelerateInterpolator);
                    }
                }
            }

            public int getHorizontalDistance(View view) {
                final RecyclerView.LayoutManager layoutManager = getLayoutManager();
                if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
                    return 0;
                }
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                        view.getLayoutParams();
                final int left = layoutManager.getDecoratedLeft(view) + layoutManager.getLeftDecorationWidth(view) - params.leftMargin;
                final int right = layoutManager.getDecoratedRight(view) + params.rightMargin;
                final int start = layoutManager.getPaddingLeft();
                final int end = layoutManager.getWidth() - layoutManager.getPaddingRight();
                int viewCenter = (right + left) / 2;
                int recyclerCenter = (end + start) / 2;
                return viewCenter - recyclerCenter;
            }

            public int getVerticalDistance(View view) {
                final RecyclerView.LayoutManager layoutManager = getLayoutManager();
                if (layoutManager == null || !layoutManager.canScrollVertically()) {
                    return 0;
                }
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                        view.getLayoutParams();

                final int top = layoutManager.getDecoratedTop(view) + layoutManager.getTopDecorationHeight(view) - params.topMargin;
                final int bottom = layoutManager.getDecoratedBottom(view) + params.bottomMargin;
                final int start = layoutManager.getPaddingTop();
                final int end = layoutManager.getHeight() - layoutManager.getPaddingBottom();
                int viewCenter = (bottom + top) / 2;
                int recyclerCenter = (end + start) / 2;
                return viewCenter - recyclerCenter;
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return AbstractGradientLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            }
        }
    }

    /**
     * Custom class for instantiate the viewHolder and, in he function offset, get the itemView
     * and apply it to the items.
     */
    public abstract class AbstractGradientViewHolder extends RecyclerView.ViewHolder {

        public AbstractGradientViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void changeColor(int color);

        public abstract void whenSelected();
    }

    /**
     * This class is only to make sure that if you're using this gradient, you should use this custom
     * view holder.
     *
     * @param <VH>
     */
    public abstract class AbstractGradientAdapter<VH extends AbstractGradientViewHolder, ViewData> extends RecyclerView.Adapter<VH> {

        protected List<ViewData> list = Collections.emptyList();
        protected Context context;

        public AbstractGradientAdapter(Context context) {
            this.context = context;
        }

        @Override
        public final int getItemCount() {
            return list.size();
        }

        public final void setList(List<ViewData> list) {
            this.list.clear();
            for (int i = 0; i < list.size(); i++) {
                this.list.add(list.get(i));
            }
        }

    }

    /**
     * Methods for force our classes to been used
     *
     * @param adapter
     */
    public void setAdapter(AbstractGradientAdapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public final void setAdapter(Adapter adapter) {
        this.setAdapter((AbstractGradientAdapter) adapter);
    }

    public int getEndOffset() {
        return endOffset;
    }

    public int getStartOffset() {
        return startOffset;
    }

    public RecyclerView.OnScrollListener getOnScrollListener() {
        return scrollListener;
    }
}