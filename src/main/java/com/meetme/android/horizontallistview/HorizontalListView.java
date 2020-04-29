package com.meetme.android.horizontallistview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EdgeEffect;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Scroller;
import dji.frame.widget.R;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class HorizontalListView extends AdapterView<ListAdapter> {
    private static final String BUNDLE_ID_CURRENT_X = "BUNDLE_ID_CURRENT_X";
    private static final String BUNDLE_ID_PARENT_STATE = "BUNDLE_ID_PARENT_STATE";
    private static final float FLING_DEFAULT_ABSORB_VELOCITY = 30.0f;
    private static final float FLING_FRICTION = 0.009f;
    private static final int INSERT_AT_END_OF_LIST = -1;
    private static final int INSERT_AT_START_OF_LIST = 0;
    protected ListAdapter mAdapter;
    private DataSetObserver mAdapterDataObserver = new DataSetObserver() {
        /* class com.meetme.android.horizontallistview.HorizontalListView.AnonymousClass2 */

        public void onChanged() {
            boolean unused = HorizontalListView.this.mDataChanged = true;
            boolean unused2 = HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
            HorizontalListView.this.unpressTouchedChild();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }

        public void onInvalidated() {
            boolean unused = HorizontalListView.this.mHasNotifiedRunningLowOnData = false;
            HorizontalListView.this.unpressTouchedChild();
            HorizontalListView.this.reset();
            HorizontalListView.this.invalidate();
            HorizontalListView.this.requestLayout();
        }
    };
    /* access modifiers changed from: private */
    public boolean mBlockTouchAction = false;
    private OnScrollStateChangedListener.ScrollState mCurrentScrollState = OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE;
    protected int mCurrentX;
    private int mCurrentlySelectedAdapterIndex;
    /* access modifiers changed from: private */
    public boolean mDataChanged = false;
    private Runnable mDelayedLayout = new Runnable() {
        /* class com.meetme.android.horizontallistview.HorizontalListView.AnonymousClass3 */

        public void run() {
            HorizontalListView.this.requestLayout();
        }
    };
    private int mDisplayOffset;
    private Drawable mDivider = null;
    private int mDividerWidth = 0;
    private EdgeEffect mEdgeGlowLeft;
    private EdgeEffect mEdgeGlowRight;
    protected Scroller mFlingTracker = new Scroller(getContext());
    /* access modifiers changed from: private */
    public GestureDetector mGestureDetector;
    private final GestureListener mGestureListener = new GestureListener();
    /* access modifiers changed from: private */
    public boolean mHasNotifiedRunningLowOnData = false;
    private int mHeightMeasureSpec;
    private boolean mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = false;
    /* access modifiers changed from: private */
    public int mLeftViewAdapterIndex;
    private int mMaxX = Integer.MAX_VALUE;
    protected int mNextX;
    /* access modifiers changed from: private */
    public View.OnClickListener mOnClickListener;
    private OnScrollStateChangedListener mOnScrollStateChangedListener = null;
    private Rect mRect = new Rect();
    private List<Queue<View>> mRemovedViewsCache = new ArrayList();
    private Integer mRestoreX = null;
    private int mRightViewAdapterIndex;
    private RunningOutOfDataListener mRunningOutOfDataListener = null;
    private int mRunningOutOfDataThreshold = 0;
    private View mViewBeingTouched = null;

    public interface OnScrollStateChangedListener {

        public enum ScrollState {
            SCROLL_STATE_IDLE,
            SCROLL_STATE_TOUCH_SCROLL,
            SCROLL_STATE_FLING
        }

        void onScrollStateChanged(ScrollState scrollState);
    }

    public interface RunningOutOfDataListener {
        void onRunningOutOfData();
    }

    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mEdgeGlowLeft = new EdgeEffect(context);
        this.mEdgeGlowRight = new EdgeEffect(context);
        this.mGestureDetector = new GestureDetector(context, this.mGestureListener);
        bindGestureDetector();
        initView();
        retrieveXmlConfiguration(context, attrs);
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= 11) {
            HoneycombPlus.setFriction(this.mFlingTracker, FLING_FRICTION);
        }
    }

    private void bindGestureDetector() {
        setOnTouchListener(new View.OnTouchListener() {
            /* class com.meetme.android.horizontallistview.HorizontalListView.AnonymousClass1 */

            public boolean onTouch(View v, MotionEvent event) {
                return HorizontalListView.this.mGestureDetector.onTouchEvent(event);
            }
        });
    }

    /* access modifiers changed from: private */
    public void requestParentListViewToNotInterceptTouchEvents(Boolean disallowIntercept) {
        if (this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent != disallowIntercept.booleanValue()) {
            for (View view = this; view.getParent() instanceof View; view = (View) view.getParent()) {
                if ((view.getParent() instanceof ListView) || (view.getParent() instanceof ScrollView)) {
                    view.getParent().requestDisallowInterceptTouchEvent(disallowIntercept.booleanValue());
                    this.mIsParentVerticiallyScrollableViewDisallowingInterceptTouchEvent = disallowIntercept.booleanValue();
                    return;
                }
            }
        }
    }

    private void retrieveXmlConfiguration(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HorizontalListView);
            Drawable d = a.getDrawable(R.styleable.HorizontalListView_android_divider);
            if (d != null) {
                setDivider(d);
            }
            int dividerWidth = a.getDimensionPixelSize(R.styleable.HorizontalListView_dividerWidth, 0);
            if (dividerWidth != 0) {
                setDividerWidth(dividerWidth);
            }
            a.recycle();
        }
    }

    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_ID_PARENT_STATE, super.onSaveInstanceState());
        bundle.putInt(BUNDLE_ID_CURRENT_X, this.mCurrentX);
        return bundle;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mRestoreX = Integer.valueOf(bundle.getInt(BUNDLE_ID_CURRENT_X));
            super.onRestoreInstanceState(bundle.getParcelable(BUNDLE_ID_PARENT_STATE));
        }
    }

    public void setDivider(Drawable divider) {
        this.mDivider = divider;
        if (divider != null) {
            setDividerWidth(divider.getIntrinsicWidth());
        } else {
            setDividerWidth(0);
        }
    }

    public void setDividerWidth(int width) {
        this.mDividerWidth = width;
        requestLayout();
        invalidate();
    }

    private void initView() {
        this.mLeftViewAdapterIndex = -1;
        this.mRightViewAdapterIndex = -1;
        this.mDisplayOffset = 0;
        this.mCurrentX = 0;
        this.mNextX = 0;
        this.mMaxX = Integer.MAX_VALUE;
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
    }

    /* access modifiers changed from: private */
    public void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    public void setSelection(int position) {
        this.mCurrentlySelectedAdapterIndex = position;
    }

    public View getSelectedView() {
        return getChild(this.mCurrentlySelectedAdapterIndex);
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mAdapterDataObserver);
        }
        if (adapter != null) {
            this.mHasNotifiedRunningLowOnData = false;
            this.mAdapter = adapter;
            this.mAdapter.registerDataSetObserver(this.mAdapterDataObserver);
        }
        initializeRecycledViewCache(this.mAdapter.getViewTypeCount());
        reset();
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    private void initializeRecycledViewCache(int viewTypeCount) {
        this.mRemovedViewsCache.clear();
        for (int i = 0; i < viewTypeCount; i++) {
            this.mRemovedViewsCache.add(new LinkedList());
        }
    }

    private View getRecycledView(int adapterIndex) {
        int itemViewType = this.mAdapter.getItemViewType(adapterIndex);
        if (isItemViewTypeValid(itemViewType)) {
            return (View) this.mRemovedViewsCache.get(itemViewType).poll();
        }
        return null;
    }

    private void recycleView(int adapterIndex, View view) {
        int itemViewType = this.mAdapter.getItemViewType(adapterIndex);
        if (isItemViewTypeValid(itemViewType)) {
            this.mRemovedViewsCache.get(itemViewType).offer(view);
        }
    }

    private boolean isItemViewTypeValid(int itemViewType) {
        return itemViewType < this.mRemovedViewsCache.size();
    }

    private void addAndMeasureChild(View child, int viewPos) {
        addViewInLayout(child, viewPos, getLayoutParams(child), true);
        measureChild(child);
    }

    private void measureChild(View child) {
        int childWidthSpec;
        ViewGroup.LayoutParams childLayoutParams = getLayoutParams(child);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(this.mHeightMeasureSpec, getPaddingTop() + getPaddingBottom(), childLayoutParams.height);
        if (childLayoutParams.width > 0) {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(childLayoutParams.width, 1073741824);
        } else {
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private ViewGroup.LayoutParams getLayoutParams(View child) {
        ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
        if (layoutParams == null) {
            return new ViewGroup.LayoutParams(-2, -1);
        }
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"WrongCall"})
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mAdapter != null) {
            invalidate();
            if (this.mDataChanged) {
                int oldCurrentX = this.mCurrentX;
                initView();
                removeAllViewsInLayout();
                this.mNextX = oldCurrentX;
                this.mDataChanged = false;
            }
            if (this.mRestoreX != null) {
                this.mNextX = this.mRestoreX.intValue();
                this.mRestoreX = null;
            }
            if (this.mFlingTracker.computeScrollOffset()) {
                this.mNextX = this.mFlingTracker.getCurrX();
            }
            if (this.mNextX < 0) {
                this.mNextX = 0;
                if (this.mEdgeGlowLeft.isFinished()) {
                    this.mEdgeGlowLeft.onAbsorb((int) determineFlingAbsorbVelocity());
                }
                this.mFlingTracker.forceFinished(true);
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            } else if (this.mNextX > this.mMaxX) {
                this.mNextX = this.mMaxX;
                if (this.mEdgeGlowRight.isFinished()) {
                    this.mEdgeGlowRight.onAbsorb((int) determineFlingAbsorbVelocity());
                }
                this.mFlingTracker.forceFinished(true);
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }
            int dx = this.mCurrentX - this.mNextX;
            removeNonVisibleChildren(dx);
            fillList(dx);
            positionChildren(dx);
            this.mCurrentX = this.mNextX;
            if (determineMaxX()) {
                onLayout(changed, left, top, right, bottom);
            } else if (!this.mFlingTracker.isFinished()) {
                postOnAnimation(this.mDelayedLayout);
            } else if (this.mCurrentScrollState == OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING) {
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }
        }
    }

    /* access modifiers changed from: protected */
    public float getLeftFadingEdgeStrength() {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();
        if (this.mCurrentX == 0) {
            return 0.0f;
        }
        if (this.mCurrentX < horizontalFadingEdgeLength) {
            return ((float) this.mCurrentX) / ((float) horizontalFadingEdgeLength);
        }
        return 1.0f;
    }

    /* access modifiers changed from: protected */
    public float getRightFadingEdgeStrength() {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();
        if (this.mCurrentX == this.mMaxX) {
            return 0.0f;
        }
        if (this.mMaxX - this.mCurrentX < horizontalFadingEdgeLength) {
            return ((float) (this.mMaxX - this.mCurrentX)) / ((float) horizontalFadingEdgeLength);
        }
        return 1.0f;
    }

    private float determineFlingAbsorbVelocity() {
        if (Build.VERSION.SDK_INT >= 14) {
            return IceCreamSandwichPlus.getCurrVelocity(this.mFlingTracker);
        }
        return 30.0f;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mHeightMeasureSpec = heightMeasureSpec;
    }

    private boolean determineMaxX() {
        View rightView;
        if (!isLastItemInAdapter(this.mRightViewAdapterIndex) || (rightView = getRightmostChild()) == null) {
            return false;
        }
        int oldMaxX = this.mMaxX;
        this.mMaxX = (this.mCurrentX + (rightView.getRight() - getPaddingLeft())) - getRenderWidth();
        if (this.mMaxX < 0) {
            this.mMaxX = 0;
        }
        if (this.mMaxX != oldMaxX) {
            return true;
        }
        return false;
    }

    private void fillList(int dx) {
        int edge = 0;
        View child = getRightmostChild();
        if (child != null) {
            edge = child.getRight();
        }
        fillListRight(edge, dx);
        int edge2 = 0;
        View child2 = getLeftmostChild();
        if (child2 != null) {
            edge2 = child2.getLeft();
        }
        fillListLeft(edge2, dx);
    }

    private void removeNonVisibleChildren(int dx) {
        int measuredWidth;
        View child = getLeftmostChild();
        while (child != null && child.getRight() + dx <= 0) {
            int i = this.mDisplayOffset;
            if (isLastItemInAdapter(this.mLeftViewAdapterIndex)) {
                measuredWidth = child.getMeasuredWidth();
            } else {
                measuredWidth = this.mDividerWidth + child.getMeasuredWidth();
            }
            this.mDisplayOffset = measuredWidth + i;
            recycleView(this.mLeftViewAdapterIndex, child);
            removeViewInLayout(child);
            this.mLeftViewAdapterIndex++;
            child = getLeftmostChild();
        }
        View child2 = getRightmostChild();
        while (child2 != null && child2.getLeft() + dx >= getWidth()) {
            recycleView(this.mRightViewAdapterIndex, child2);
            removeViewInLayout(child2);
            this.mRightViewAdapterIndex--;
            child2 = getRightmostChild();
        }
    }

    private void fillListRight(int rightEdge, int dx) {
        while (rightEdge + dx + this.mDividerWidth < getWidth() && this.mRightViewAdapterIndex + 1 < this.mAdapter.getCount()) {
            this.mRightViewAdapterIndex++;
            if (this.mLeftViewAdapterIndex < 0) {
                this.mLeftViewAdapterIndex = this.mRightViewAdapterIndex;
            }
            View child = this.mAdapter.getView(this.mRightViewAdapterIndex, getRecycledView(this.mRightViewAdapterIndex), this);
            addAndMeasureChild(child, -1);
            rightEdge += (this.mRightViewAdapterIndex == 0 ? 0 : this.mDividerWidth) + child.getMeasuredWidth();
            determineIfLowOnData();
        }
    }

    private void fillListLeft(int leftEdge, int dx) {
        while ((leftEdge + dx) - this.mDividerWidth > 0 && this.mLeftViewAdapterIndex >= 1) {
            this.mLeftViewAdapterIndex--;
            View child = this.mAdapter.getView(this.mLeftViewAdapterIndex, getRecycledView(this.mLeftViewAdapterIndex), this);
            addAndMeasureChild(child, 0);
            leftEdge -= this.mLeftViewAdapterIndex == 0 ? child.getMeasuredWidth() : this.mDividerWidth + child.getMeasuredWidth();
            this.mDisplayOffset -= leftEdge + dx == 0 ? child.getMeasuredWidth() : this.mDividerWidth + child.getMeasuredWidth();
        }
    }

    private void positionChildren(int dx) {
        int childCount = getChildCount();
        if (childCount > 0) {
            this.mDisplayOffset += dx;
            int leftOffset = this.mDisplayOffset;
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                int left = leftOffset + getPaddingLeft();
                int top = getPaddingTop();
                child.layout(left, top, left + child.getMeasuredWidth(), top + child.getMeasuredHeight());
                leftOffset += child.getMeasuredWidth() + this.mDividerWidth;
            }
        }
    }

    private View getLeftmostChild() {
        return getChildAt(0);
    }

    private View getRightmostChild() {
        return getChildAt(getChildCount() - 1);
    }

    private View getChild(int adapterIndex) {
        if (adapterIndex < this.mLeftViewAdapterIndex || adapterIndex > this.mRightViewAdapterIndex) {
            return null;
        }
        getChildAt(adapterIndex - this.mLeftViewAdapterIndex);
        return null;
    }

    /* access modifiers changed from: private */
    public int getChildIndex(int x, int y) {
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            getChildAt(index).getHitRect(this.mRect);
            if (this.mRect.contains(x, y)) {
                return index;
            }
        }
        return -1;
    }

    private boolean isLastItemInAdapter(int index) {
        return index == this.mAdapter.getCount() + -1;
    }

    private int getRenderHeight() {
        return (getHeight() - getPaddingTop()) - getPaddingBottom();
    }

    private int getRenderWidth() {
        return (getWidth() - getPaddingLeft()) - getPaddingRight();
    }

    public void scrollTo(int x) {
        this.mFlingTracker.startScroll(this.mNextX, 0, x - this.mNextX, 0);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        requestLayout();
    }

    public int getFirstVisiblePosition() {
        return this.mLeftViewAdapterIndex;
    }

    public int getLastVisiblePosition() {
        return this.mRightViewAdapterIndex;
    }

    private void drawEdgeGlow(Canvas canvas) {
        if (this.mEdgeGlowLeft != null && !this.mEdgeGlowLeft.isFinished() && isEdgeGlowEnabled()) {
            int restoreCount = canvas.save();
            int height = getHeight();
            canvas.rotate(-90.0f, 0.0f, 0.0f);
            canvas.translate((float) ((-height) + getPaddingBottom()), 0.0f);
            this.mEdgeGlowLeft.setSize(getRenderHeight(), getRenderWidth());
            if (this.mEdgeGlowLeft.draw(canvas)) {
                invalidate();
            }
            canvas.restoreToCount(restoreCount);
        } else if (this.mEdgeGlowRight != null && !this.mEdgeGlowRight.isFinished() && isEdgeGlowEnabled()) {
            int restoreCount2 = canvas.save();
            int width = getWidth();
            canvas.rotate(90.0f, 0.0f, 0.0f);
            canvas.translate((float) getPaddingTop(), (float) (-width));
            this.mEdgeGlowRight.setSize(getRenderHeight(), getRenderWidth());
            if (this.mEdgeGlowRight.draw(canvas)) {
                invalidate();
            }
            canvas.restoreToCount(restoreCount2);
        }
    }

    private void drawDividers(Canvas canvas) {
        int count = getChildCount();
        Rect bounds = this.mRect;
        this.mRect.top = getPaddingTop();
        this.mRect.bottom = this.mRect.top + getRenderHeight();
        for (int i = 0; i < count; i++) {
            if (i != count - 1 || !isLastItemInAdapter(this.mRightViewAdapterIndex)) {
                View child = getChildAt(i);
                bounds.left = child.getRight();
                bounds.right = child.getRight() + this.mDividerWidth;
                if (bounds.left < getPaddingLeft()) {
                    bounds.left = getPaddingLeft();
                }
                if (bounds.right > getWidth() - getPaddingRight()) {
                    bounds.right = getWidth() - getPaddingRight();
                }
                drawDivider(canvas, bounds);
                if (i == 0 && child.getLeft() > getPaddingLeft()) {
                    bounds.left = getPaddingLeft();
                    bounds.right = child.getLeft();
                    drawDivider(canvas, bounds);
                }
            }
        }
    }

    private void drawDivider(Canvas canvas, Rect bounds) {
        if (this.mDivider != null) {
            this.mDivider.setBounds(bounds);
            this.mDivider.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDividers(canvas);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawEdgeGlow(canvas);
    }

    /* access modifiers changed from: protected */
    public void dispatchSetPressed(boolean pressed) {
    }

    /* access modifiers changed from: protected */
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        this.mFlingTracker.fling(this.mNextX, 0, (int) (-velocityX), 0, 0, this.mMaxX, 0, 0);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_FLING);
        requestLayout();
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean onDown(MotionEvent e) {
        int index;
        this.mBlockTouchAction = !this.mFlingTracker.isFinished();
        this.mFlingTracker.forceFinished(true);
        setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
        unpressTouchedChild();
        if (!this.mBlockTouchAction && (index = getChildIndex((int) e.getX(), (int) e.getY())) >= 0) {
            this.mViewBeingTouched = getChildAt(index);
            if (this.mViewBeingTouched != null) {
                this.mViewBeingTouched.setPressed(true);
                refreshDrawableState();
            }
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void unpressTouchedChild() {
        if (this.mViewBeingTouched != null) {
            this.mViewBeingTouched.setPressed(false);
            refreshDrawableState();
            this.mViewBeingTouched = null;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private GestureListener() {
        }

        public boolean onDown(MotionEvent e) {
            return HorizontalListView.this.onDown(e);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontalListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            HorizontalListView.this.requestParentListViewToNotInterceptTouchEvents(true);
            HorizontalListView.this.setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_TOUCH_SCROLL);
            HorizontalListView.this.unpressTouchedChild();
            HorizontalListView.this.mNextX += (int) distanceX;
            HorizontalListView.this.updateOverscrollAnimation(Math.round(distanceX));
            HorizontalListView.this.requestLayout();
            return true;
        }

        public boolean onSingleTapConfirmed(MotionEvent e) {
            HorizontalListView.this.unpressTouchedChild();
            AdapterView.OnItemClickListener onItemClickListener = HorizontalListView.this.getOnItemClickListener();
            int index = HorizontalListView.this.getChildIndex((int) e.getX(), (int) e.getY());
            if (index >= 0 && !HorizontalListView.this.mBlockTouchAction) {
                View child = HorizontalListView.this.getChildAt(index);
                int adapterIndex = HorizontalListView.this.mLeftViewAdapterIndex + index;
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(HorizontalListView.this, child, adapterIndex, HorizontalListView.this.mAdapter.getItemId(adapterIndex));
                    return true;
                }
            }
            if (HorizontalListView.this.mOnClickListener != null && !HorizontalListView.this.mBlockTouchAction) {
                HorizontalListView.this.mOnClickListener.onClick(HorizontalListView.this);
            }
            return false;
        }

        public void onLongPress(MotionEvent e) {
            HorizontalListView.this.unpressTouchedChild();
            int index = HorizontalListView.this.getChildIndex((int) e.getX(), (int) e.getY());
            if (index >= 0 && !HorizontalListView.this.mBlockTouchAction) {
                View child = HorizontalListView.this.getChildAt(index);
                AdapterView.OnItemLongClickListener onItemLongClickListener = HorizontalListView.this.getOnItemLongClickListener();
                if (onItemLongClickListener != null) {
                    int adapterIndex = HorizontalListView.this.mLeftViewAdapterIndex + index;
                    if (onItemLongClickListener.onItemLongClick(HorizontalListView.this, child, adapterIndex, HorizontalListView.this.mAdapter.getItemId(adapterIndex))) {
                        HorizontalListView.this.performHapticFeedback(0);
                    }
                }
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            if (this.mFlingTracker == null || this.mFlingTracker.isFinished()) {
                setCurrentScrollState(OnScrollStateChangedListener.ScrollState.SCROLL_STATE_IDLE);
            }
            requestParentListViewToNotInterceptTouchEvents(false);
            releaseEdgeGlow();
        } else if (event.getAction() == 3) {
            unpressTouchedChild();
            releaseEdgeGlow();
            requestParentListViewToNotInterceptTouchEvents(false);
        }
        return super.onTouchEvent(event);
    }

    private void releaseEdgeGlow() {
        if (this.mEdgeGlowLeft != null) {
            this.mEdgeGlowLeft.onRelease();
        }
        if (this.mEdgeGlowRight != null) {
            this.mEdgeGlowRight.onRelease();
        }
    }

    public void destroy() {
        this.mEdgeGlowLeft = null;
        this.mEdgeGlowRight = null;
        this.mGestureDetector = null;
    }

    public void setRunningOutOfDataListener(RunningOutOfDataListener listener, int numberOfItemsLeftConsideredLow) {
        this.mRunningOutOfDataListener = listener;
        this.mRunningOutOfDataThreshold = numberOfItemsLeftConsideredLow;
    }

    private void determineIfLowOnData() {
        if (this.mRunningOutOfDataListener != null && this.mAdapter != null && this.mAdapter.getCount() - (this.mRightViewAdapterIndex + 1) < this.mRunningOutOfDataThreshold && !this.mHasNotifiedRunningLowOnData) {
            this.mHasNotifiedRunningLowOnData = true;
            this.mRunningOutOfDataListener.onRunningOutOfData();
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setOnScrollStateChangedListener(OnScrollStateChangedListener listener) {
        this.mOnScrollStateChangedListener = listener;
    }

    /* access modifiers changed from: private */
    public void setCurrentScrollState(OnScrollStateChangedListener.ScrollState newScrollState) {
        if (!(this.mCurrentScrollState == newScrollState || this.mOnScrollStateChangedListener == null)) {
            this.mOnScrollStateChangedListener.onScrollStateChanged(newScrollState);
        }
        this.mCurrentScrollState = newScrollState;
    }

    /* access modifiers changed from: private */
    public void updateOverscrollAnimation(int scrolledOffset) {
        if (this.mEdgeGlowLeft != null && this.mEdgeGlowRight != null) {
            int nextScrollPosition = this.mCurrentX + scrolledOffset;
            if (this.mFlingTracker != null && !this.mFlingTracker.isFinished()) {
                return;
            }
            if (nextScrollPosition < 0) {
                this.mEdgeGlowLeft.onPull(((float) Math.abs(scrolledOffset)) / ((float) getRenderWidth()));
                if (!this.mEdgeGlowRight.isFinished()) {
                    this.mEdgeGlowRight.onRelease();
                }
            } else if (nextScrollPosition > this.mMaxX) {
                this.mEdgeGlowRight.onPull(((float) Math.abs(scrolledOffset)) / ((float) getRenderWidth()));
                if (!this.mEdgeGlowLeft.isFinished()) {
                    this.mEdgeGlowLeft.onRelease();
                }
            }
        }
    }

    private boolean isEdgeGlowEnabled() {
        if (this.mAdapter == null || this.mAdapter.isEmpty() || this.mMaxX <= 0) {
            return false;
        }
        return true;
    }

    @TargetApi(11)
    private static final class HoneycombPlus {
        private HoneycombPlus() {
        }

        static {
            if (Build.VERSION.SDK_INT < 11) {
                throw new RuntimeException("Should not get to HoneycombPlus class unless sdk is >= 11!");
            }
        }

        public static void setFriction(Scroller scroller, float friction) {
            if (scroller != null) {
                scroller.setFriction(friction);
            }
        }
    }

    @TargetApi(14)
    private static final class IceCreamSandwichPlus {
        private IceCreamSandwichPlus() {
        }

        static {
            if (Build.VERSION.SDK_INT < 14) {
                throw new RuntimeException("Should not get to IceCreamSandwichPlus class unless sdk is >= 14!");
            }
        }

        public static float getCurrVelocity(Scroller scroller) {
            return scroller.getCurrVelocity();
        }
    }
}
