package com.dji.widget2.wheel;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.dji.widget2.R;
import com.dji.widget2.wheel.DJIWheelAdapter;

abstract class DJIWheelView extends RelativeLayout {
    private static final int COUNT_MAX_SCROLL_SOUND = 10;
    private static final int ID_UNINITIALIZED = -1;
    private DJIWheelAdapter mAdapter;
    /* access modifiers changed from: private */
    public boolean mIgnoreWheelChange = false;
    private boolean mIsNeedFreezeAfterOperation = false;
    protected ImageView mIvArrow;
    private CenterLayoutManager mManager;
    protected View mMaskView;
    private int mOffsetFromBegin;
    protected final DJIWheelAdapter.Orientation mOrientation;
    private OnWheelPointerChangeListener mOuterListener;
    private int mPointerPosition;
    protected RecyclerView mRecyclerView;
    private LinearSnapHelper mSnapHelper;
    private int mSoundId;
    private SoundPool mSoundPool;

    public interface OnWheelPointerChangeListener {
        void onPointerPositionChange(int i, boolean z);
    }

    public DJIWheelView(Context context, @Nullable AttributeSet attrs, DJIWheelAdapter.Orientation orientation) {
        super(context, attrs);
        this.mOrientation = orientation;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            init();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mSoundId = -1;
            this.mSoundPool = new SoundPool.Builder().setAudioAttributes(new AudioAttributes.Builder().setContentType(4).setUsage(1).build()).setMaxStreams(10).build();
            this.mSoundPool.load(getContext(), R.raw.voice_wheel_scroll, 0);
            this.mSoundPool.setOnLoadCompleteListener(new DJIWheelView$$Lambda$0(this));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$0$DJIWheelView(SoundPool soundPool, int sampleId, int status) {
        if (status == 0) {
            this.mSoundId = sampleId;
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mSoundId != -1) {
            this.mSoundPool.stop(this.mSoundId);
        }
        this.mSoundPool.release();
        this.mSoundId = -1;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculatePlaceHolderSize();
        reset();
        setPointerPosition(this.mPointerPosition - 1, false, true);
    }

    public void setAdapter(@NonNull DJIWheelAdapter adapter) {
        this.mAdapter = adapter;
        this.mAdapter.setOnItemClickListener(new DJIWheelView$$Lambda$1(this));
        this.mRecyclerView.setAdapter(adapter);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$setAdapter$1$DJIWheelView(int position) {
        setPointerPosition(position, true);
    }

    public void reset() {
        this.mPointerPosition = -1;
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setArrowVisibility(boolean visible) {
        this.mIvArrow.setVisibility(visible ? 0 : 4);
    }

    public void setPointerPosition(int position, boolean isFromUser) {
        setPointerPosition(position, isFromUser, false);
    }

    private void setPointerPosition(int position, boolean isFromUser, boolean isForce) {
        if (isForce || (position >= 0 && isEnabled())) {
            int realValue = position + 1;
            if (isFromUser) {
                this.mIgnoreWheelChange = false;
                this.mRecyclerView.smoothScrollToPosition(realValue);
                return;
            }
            this.mIgnoreWheelChange = true;
            this.mManager.scrollToPositionWithOffset(realValue, this.mAdapter.calculateTargetOffset());
            if (!(this.mOuterListener == null || realValue == this.mPointerPosition)) {
                this.mOuterListener.onPointerPositionChange(position, false);
            }
            this.mPointerPosition = realValue;
        }
    }

    public void releaseFreeze() {
        if (this.mIsNeedFreezeAfterOperation) {
            if (this.mAdapter != null) {
                this.mAdapter.setEnabled(true);
            }
            setEnabled(true);
            this.mMaskView.setVisibility(8);
        }
    }

    public synchronized void setFreezeAfterOperation(boolean isNeedFreezeAfterOperation) {
        this.mIsNeedFreezeAfterOperation = isNeedFreezeAfterOperation;
    }

    public void setOnWheelPointerChangeListener(@Nullable OnWheelPointerChangeListener listener) {
        this.mOuterListener = listener;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.mMaskView.setVisibility(enabled ? 8 : 0);
    }

    private void init() {
        this.mManager = new CenterLayoutManager(getContext());
        this.mManager.setOrientation(this.mOrientation == DJIWheelAdapter.Orientation.VERTICAL ? 1 : 0);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.fpv_widget_wheel_recycle);
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setLayoutManager(this.mManager);
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /* class com.dji.widget2.wheel.DJIWheelView.AnonymousClass1 */

            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == 0) {
                    DJIWheelView.this.handlePointerPositionChange();
                }
            }

            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (DJIWheelView.this.mIgnoreWheelChange) {
                    boolean unused = DJIWheelView.this.mIgnoreWheelChange = false;
                } else {
                    DJIWheelView.this.calculateScrollOffset();
                }
            }
        });
        this.mSnapHelper = new LinearSnapHelper();
        this.mSnapHelper.attachToRecyclerView(this.mRecyclerView);
        this.mIvArrow = (ImageView) findViewById(R.id.fpv_widget_wheel_arrow);
        this.mMaskView = findViewById(R.id.fpv_widget_wheel_mask);
    }

    private void calculatePlaceHolderSize() {
        if (this.mAdapter != null) {
            this.mAdapter.setWidgetSize(this.mManager.canScrollVertically() ? getHeight() : getWidth());
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void freezeAfterOperation() {
        if (this.mIsNeedFreezeAfterOperation) {
            if (this.mAdapter != null) {
                this.mAdapter.setEnabled(false);
            }
            setEnabled(false);
            this.mMaskView.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void handlePointerPositionChange() {
        int itemPosition;
        View snapView = this.mSnapHelper.findSnapView(this.mManager);
        if (snapView != null) {
            int[] distance = this.mSnapHelper.calculateDistanceToFinalSnap(this.mManager, snapView);
            int offset = 0;
            if (this.mManager.canScrollHorizontally()) {
                offset = distance != null ? distance[0] : 0;
            } else if (this.mManager.canScrollVertically()) {
                offset = distance != null ? distance[1] : 0;
            }
            if (offset == 0 && this.mPointerPosition != (itemPosition = ((Integer) snapView.getTag()).intValue())) {
                this.mPointerPosition = itemPosition;
                freezeAfterOperation();
                if (this.mOuterListener != null) {
                    this.mOuterListener.onPointerPositionChange(itemPosition - 1, true);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void calculateScrollOffset() {
        int offset;
        int offset2;
        if (this.mManager.canScrollVertically()) {
            int position = this.mManager.findFirstVisibleItemPosition();
            View firstVisibleChildView = this.mManager.findViewByPosition(position);
            int itemTop = firstVisibleChildView.getTop();
            if (position > 0) {
                int offset3 = (((getHeight() / 2) + ((position - 1) * firstVisibleChildView.getHeight())) - itemTop) / firstVisibleChildView.getHeight();
                if (offset3 != this.mOffsetFromBegin) {
                    this.mOffsetFromBegin = offset3;
                    playScrollSound();
                    return;
                }
                return;
            }
            int distance = -itemTop;
            int position2 = this.mManager.findFirstCompletelyVisibleItemPosition();
            if (position2 > 0 && (offset2 = distance / this.mManager.findViewByPosition(position2).getHeight()) != this.mOffsetFromBegin) {
                this.mOffsetFromBegin = offset2;
                playScrollSound();
                return;
            }
            return;
        }
        int position3 = this.mManager.findFirstVisibleItemPosition();
        View firstVisibleChildView2 = this.mManager.findViewByPosition(position3);
        int itemLeft = firstVisibleChildView2.getLeft();
        if (position3 > 0) {
            int offset4 = (((getWidth() / 2) + ((position3 - 1) * firstVisibleChildView2.getWidth())) - itemLeft) / firstVisibleChildView2.getWidth();
            if (offset4 != this.mOffsetFromBegin) {
                this.mOffsetFromBegin = offset4;
                playScrollSound();
                return;
            }
            return;
        }
        int distance2 = -itemLeft;
        int position4 = this.mManager.findFirstCompletelyVisibleItemPosition();
        if (position4 > 0 && (offset = distance2 / this.mManager.findViewByPosition(position4).getHeight()) != this.mOffsetFromBegin) {
            this.mOffsetFromBegin = offset;
            playScrollSound();
        }
    }

    private void playScrollSound() {
        if (this.mSoundId != -1) {
            this.mSoundPool.play(this.mSoundId, 0.5f, 0.5f, 0, 0, 1.0f);
        }
    }

    private static class CenterLayoutManager extends LinearLayoutManager {
        CenterLayoutManager(Context context) {
            super(context);
        }

        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
            super.smoothScrollToPosition(recyclerView, state, position);
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {
                /* class com.dji.widget2.wheel.DJIWheelView.CenterLayoutManager.AnonymousClass1 */

                public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                    return (((boxEnd - boxStart) / 2) + boxStart) - (((viewEnd - viewStart) / 2) + viewStart);
                }

                /* access modifiers changed from: protected */
                public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                    return 300.0f / ((float) displayMetrics.densityDpi);
                }
            };
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }

        public void scrollToPosition(int position) {
            super.scrollToPosition(position);
        }
    }
}
