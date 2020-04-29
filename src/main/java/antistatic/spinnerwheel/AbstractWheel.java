package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import antistatic.spinnerwheel.WheelScroller;
import antistatic.spinnerwheel.adapters.WheelViewAdapter;
import dji.frame.widget.R;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWheel extends View {
    private static final boolean DEF_IS_CYCLIC = false;
    private static final int DEF_VISIBLE_ITEMS = 4;
    private static int itemID = -1;
    private final String LOG_TAG;
    private List<OnWheelChangedListener> changingListeners = new LinkedList();
    private List<OnWheelClickedListener> clickingListeners = new LinkedList();
    protected int mCurrentItemIdx = 0;
    private DataSetObserver mDataObserver;
    protected int mFirstItemIdx;
    protected boolean mIsAllVisible;
    protected boolean mIsCyclic;
    protected boolean mIsScrollingPerformed;
    protected LinearLayout mItemsLayout;
    protected int mLayoutHeight;
    protected int mLayoutWidth;
    private WheelRecycler mRecycler = new WheelRecycler(this);
    protected WheelScroller mScroller;
    protected int mScrollingOffset;
    protected WheelViewAdapter mViewAdapter;
    protected int mVisibleItems;
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList();

    /* access modifiers changed from: protected */
    public abstract void createItemsLayout();

    /* access modifiers changed from: protected */
    public abstract WheelScroller createScroller(WheelScroller.ScrollingListener scrollingListener);

    /* access modifiers changed from: protected */
    public abstract void doItemsLayout();

    /* access modifiers changed from: protected */
    public abstract int getBaseDimension();

    /* access modifiers changed from: protected */
    public abstract int getItemDimension();

    /* access modifiers changed from: protected */
    public abstract float getMotionEventPosition(MotionEvent motionEvent);

    /* access modifiers changed from: protected */
    public abstract void recreateAssets(int i, int i2);

    public AbstractWheel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        StringBuilder append = new StringBuilder().append(AbstractWheel.class.getName()).append(" #");
        int i = itemID + 1;
        itemID = i;
        this.LOG_TAG = append.append(i).toString();
        if (!isInEditMode()) {
            initAttributes(attrs, defStyle);
            initData(context);
        }
    }

    /* access modifiers changed from: protected */
    public void initAttributes(AttributeSet attrs, int defStyle) {
        if (!isInEditMode()) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AbstractWheelView, defStyle, 0);
            this.mVisibleItems = a.getInt(R.styleable.AbstractWheelView_visibleItems, 4);
            this.mIsAllVisible = a.getBoolean(R.styleable.AbstractWheelView_isAllVisible, false);
            this.mIsCyclic = a.getBoolean(R.styleable.AbstractWheelView_isCyclic, false);
            a.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void initData(Context context) {
        this.mDataObserver = new DataSetObserver() {
            /* class antistatic.spinnerwheel.AbstractWheel.AnonymousClass1 */

            public void onChanged() {
                AbstractWheel.this.invalidateItemsLayout(false);
            }

            public void onInvalidated() {
                AbstractWheel.this.invalidateItemsLayout(true);
            }
        };
        this.mScroller = createScroller(new WheelScroller.ScrollingListener() {
            /* class antistatic.spinnerwheel.AbstractWheel.AnonymousClass2 */

            public void onStarted() {
                AbstractWheel.this.mIsScrollingPerformed = true;
                AbstractWheel.this.notifyScrollingListenersAboutStart();
                AbstractWheel.this.onScrollStarted();
            }

            public void onTouch() {
                AbstractWheel.this.onScrollTouched();
            }

            public void onTouchUp() {
                if (!AbstractWheel.this.mIsScrollingPerformed) {
                    AbstractWheel.this.onScrollTouchedUp();
                }
            }

            public void onScroll(int distance) {
                AbstractWheel.this.doScroll(distance);
                int dimension = AbstractWheel.this.getBaseDimension();
                if (AbstractWheel.this.mScrollingOffset > dimension) {
                    AbstractWheel.this.mScrollingOffset = dimension;
                    AbstractWheel.this.mScroller.stopScrolling();
                } else if (AbstractWheel.this.mScrollingOffset < (-dimension)) {
                    AbstractWheel.this.mScrollingOffset = -dimension;
                    AbstractWheel.this.mScroller.stopScrolling();
                }
            }

            public void onFinished() {
                if (AbstractWheel.this.mIsScrollingPerformed) {
                    AbstractWheel.this.notifyScrollingListenersAboutEnd();
                    AbstractWheel.this.mIsScrollingPerformed = false;
                    AbstractWheel.this.onScrollFinished();
                }
                AbstractWheel.this.mScrollingOffset = 0;
                AbstractWheel.this.invalidate();
            }

            public void onJustify() {
                if (Math.abs(AbstractWheel.this.mScrollingOffset) > 1) {
                    AbstractWheel.this.mScroller.scroll(AbstractWheel.this.mScrollingOffset, 0);
                }
            }
        });
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.currentItem = getCurrentItem();
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mCurrentItemIdx = ss.currentItem;
        postDelayed(new Runnable() {
            /* class antistatic.spinnerwheel.AbstractWheel.AnonymousClass3 */

            public void run() {
                AbstractWheel.this.invalidateItemsLayout(false);
            }
        }, 100);
    }

    static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            /* class antistatic.spinnerwheel.AbstractWheel.SavedState.AnonymousClass1 */

            public SavedState createFromParcel(Parcel in2) {
                return new SavedState(in2);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentItem;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in2) {
            super(in2);
            this.currentItem = in2.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentItem);
        }
    }

    /* access modifiers changed from: protected */
    public void onScrollStarted() {
    }

    /* access modifiers changed from: protected */
    public void onScrollTouched() {
    }

    /* access modifiers changed from: protected */
    public void onScrollTouchedUp() {
    }

    /* access modifiers changed from: protected */
    public void onScrollFinished() {
    }

    public void stopScrolling() {
        this.mScroller.stopScrolling();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mScroller.setInterpolator(interpolator);
    }

    public void scroll(int itemsToScroll, int time) {
        int distance = (getItemDimension() * itemsToScroll) - this.mScrollingOffset;
        onScrollTouched();
        this.mScroller.scroll(distance, time);
    }

    /* access modifiers changed from: private */
    public void doScroll(int delta) {
        this.mScrollingOffset += delta;
        int itemDimension = getItemDimension();
        int count = this.mScrollingOffset / itemDimension;
        int pos = this.mCurrentItemIdx - count;
        int itemCount = this.mViewAdapter.getItemsCount();
        int fixPos = this.mScrollingOffset % itemDimension;
        if (Math.abs(fixPos) <= itemDimension / 2) {
            fixPos = 0;
        }
        if (this.mIsCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--;
                count++;
            } else if (fixPos < 0) {
                pos++;
                count--;
            }
            while (pos < 0) {
                pos += itemCount;
            }
            pos %= itemCount;
        } else if (pos < 0) {
            count = this.mCurrentItemIdx;
            pos = 0;
        } else if (pos >= itemCount) {
            count = (this.mCurrentItemIdx - itemCount) + 1;
            pos = itemCount - 1;
        } else if (pos > 0 && fixPos > 0) {
            pos--;
            count++;
        } else if (pos < itemCount - 1 && fixPos < 0) {
            pos++;
            count--;
        }
        int offset = this.mScrollingOffset;
        if (pos != this.mCurrentItemIdx) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        int baseDimension = getBaseDimension();
        this.mScrollingOffset = offset - (count * itemDimension);
        if (this.mScrollingOffset > baseDimension) {
            this.mScrollingOffset = (this.mScrollingOffset % baseDimension) + baseDimension;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int w = r - l;
            int h = b - t;
            doItemsLayout();
            if (!(this.mLayoutWidth == w && this.mLayoutHeight == h)) {
                recreateAssets(getMeasuredWidth(), getMeasuredHeight());
            }
            this.mLayoutWidth = w;
            this.mLayoutHeight = h;
        }
    }

    public void invalidateItemsLayout(boolean clearCaches) {
        if (clearCaches) {
            this.mRecycler.clearAll();
            if (this.mItemsLayout != null) {
                this.mItemsLayout.removeAllViews();
            }
            this.mScrollingOffset = 0;
        } else if (this.mItemsLayout != null) {
            this.mRecycler.recycleItems(this.mItemsLayout, this.mFirstItemIdx, new ItemsRange());
        }
        invalidate();
    }

    public int getVisibleItems() {
        return this.mVisibleItems;
    }

    public void setVisibleItems(int count) {
        this.mVisibleItems = count;
    }

    public void setAllItemsVisible(boolean isAllVisible) {
        this.mIsAllVisible = isAllVisible;
        invalidateItemsLayout(false);
    }

    public WheelViewAdapter getViewAdapter() {
        return this.mViewAdapter;
    }

    public void setViewAdapter(WheelViewAdapter viewAdapter) {
        if (this.mViewAdapter != null) {
            this.mViewAdapter.unregisterDataSetObserver(this.mDataObserver);
        }
        this.mViewAdapter = viewAdapter;
        if (this.mViewAdapter != null) {
            this.mViewAdapter.registerDataSetObserver(this.mDataObserver);
        }
        invalidateItemsLayout(true);
    }

    public int getCurrentItem() {
        return this.mCurrentItemIdx;
    }

    public void setCurrentItem(int index, boolean animated) {
        int scroll;
        if (this.mViewAdapter != null && this.mViewAdapter.getItemsCount() != 0) {
            int itemCount = this.mViewAdapter.getItemsCount();
            if (index < 0 || index >= itemCount) {
                if (this.mIsCyclic) {
                    while (index < 0) {
                        index += itemCount;
                    }
                    index %= itemCount;
                } else {
                    return;
                }
            }
            if (index == this.mCurrentItemIdx) {
                return;
            }
            if (animated) {
                int itemsToScroll = index - this.mCurrentItemIdx;
                if (this.mIsCyclic && (scroll = (Math.min(index, this.mCurrentItemIdx) + itemCount) - Math.max(index, this.mCurrentItemIdx)) < Math.abs(itemsToScroll)) {
                    itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                }
                scroll(itemsToScroll, 0);
                return;
            }
            this.mScrollingOffset = 0;
            int old = this.mCurrentItemIdx;
            this.mCurrentItemIdx = index;
            notifyChangingListeners(old, this.mCurrentItemIdx);
            invalidate();
        }
    }

    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    public boolean isCyclic() {
        return this.mIsCyclic;
    }

    public void setCyclic(boolean isCyclic) {
        this.mIsCyclic = isCyclic;
        invalidateItemsLayout(false);
    }

    public void addChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.add(listener);
    }

    public void removeChangingListener(OnWheelChangedListener listener) {
        this.changingListeners.remove(listener);
    }

    /* access modifiers changed from: protected */
    public void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : this.changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    public void addScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.add(listener);
    }

    public void removeScrollingListener(OnWheelScrollListener listener) {
        this.scrollingListeners.remove(listener);
    }

    /* access modifiers changed from: protected */
    public void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : this.scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    public void addClickingListener(OnWheelClickedListener listener) {
        this.clickingListeners.add(listener);
    }

    public void removeClickingListener(OnWheelClickedListener listener) {
        this.clickingListeners.remove(listener);
    }

    /* access modifiers changed from: protected */
    public void notifyClickListenersAboutClick(int item) {
        for (OnWheelClickedListener listener : this.clickingListeners) {
            listener.onItemClicked(this, item);
        }
    }

    /* access modifiers changed from: protected */
    public boolean rebuildItems() {
        boolean updated;
        ItemsRange range = getItemsRange();
        if (this.mItemsLayout != null) {
            int first = this.mRecycler.recycleItems(this.mItemsLayout, this.mFirstItemIdx, range);
            if (this.mFirstItemIdx != first) {
                updated = true;
            } else {
                updated = false;
            }
            this.mFirstItemIdx = first;
        } else {
            createItemsLayout();
            updated = true;
        }
        if (!updated) {
            if (this.mFirstItemIdx == range.getFirst() && this.mItemsLayout.getChildCount() == range.getCount()) {
                updated = false;
            } else {
                updated = true;
            }
        }
        if (this.mFirstItemIdx <= range.getFirst() || this.mFirstItemIdx > range.getLast()) {
            this.mFirstItemIdx = range.getFirst();
        } else {
            int i = this.mFirstItemIdx - 1;
            while (i >= range.getFirst() && addItemView(i, true)) {
                this.mFirstItemIdx = i;
                i--;
            }
        }
        int first2 = this.mFirstItemIdx;
        for (int i2 = this.mItemsLayout.getChildCount(); i2 < range.getCount(); i2++) {
            if (!addItemView(this.mFirstItemIdx + i2, false) && this.mItemsLayout.getChildCount() == 0) {
                first2++;
            }
        }
        this.mFirstItemIdx = first2;
        return updated;
    }

    private ItemsRange getItemsRange() {
        if (this.mIsAllVisible) {
            int baseDimension = getBaseDimension();
            int itemDimension = getItemDimension();
            if (itemDimension != 0) {
                this.mVisibleItems = (baseDimension / itemDimension) + 1;
            }
        }
        int start = this.mCurrentItemIdx - (this.mVisibleItems / 2);
        int end = (start + this.mVisibleItems) - (this.mVisibleItems % 2 == 0 ? 0 : 1);
        if (this.mScrollingOffset != 0) {
            if (this.mScrollingOffset > 0) {
                start--;
            } else {
                end++;
            }
        }
        if (!isCyclic()) {
            if (start < 0) {
                start = 0;
            }
            if (this.mViewAdapter == null) {
                end = 0;
            } else if (end > this.mViewAdapter.getItemsCount()) {
                end = this.mViewAdapter.getItemsCount();
            }
        }
        return new ItemsRange(start, (end - start) + 1);
    }

    /* access modifiers changed from: protected */
    public boolean isValidItemIndex(int index) {
        return this.mViewAdapter != null && this.mViewAdapter.getItemsCount() > 0 && (this.mIsCyclic || (index >= 0 && index < this.mViewAdapter.getItemsCount()));
    }

    private boolean addItemView(int index, boolean first) {
        View view = getItemView(index);
        if (view == null) {
            return false;
        }
        if (first) {
            this.mItemsLayout.addView(view, 0);
        } else {
            this.mItemsLayout.addView(view);
        }
        return true;
    }

    private View getItemView(int index) {
        if (this.mViewAdapter == null || this.mViewAdapter.getItemsCount() == 0) {
            return null;
        }
        int count = this.mViewAdapter.getItemsCount();
        if (!isValidItemIndex(index)) {
            return this.mViewAdapter.getEmptyItem(this.mRecycler.getEmptyItem(), this.mItemsLayout);
        }
        while (index < 0) {
            index += count;
        }
        return this.mViewAdapter.getItem(index % count, this.mRecycler.getItem(), this.mItemsLayout);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int distance;
        if (!isEnabled() || getViewAdapter() == null) {
            return true;
        }
        switch (event.getAction()) {
            case 0:
            case 2:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                }
                break;
            case 1:
                if (!this.mIsScrollingPerformed) {
                    int distance2 = ((int) getMotionEventPosition(event)) - (getBaseDimension() / 2);
                    if (distance2 > 0) {
                        distance = distance2 + (getItemDimension() / 2);
                    } else {
                        distance = distance2 - (getItemDimension() / 2);
                    }
                    int items = distance / getItemDimension();
                    if (items != 0 && isValidItemIndex(this.mCurrentItemIdx + items)) {
                        notifyClickListenersAboutClick(this.mCurrentItemIdx + items);
                        break;
                    }
                }
                break;
        }
        return this.mScroller.onTouchEvent(event);
    }
}
