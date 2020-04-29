package com.dji.widget2;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class DJIRadioLayout extends LinearLayout {
    private static final float DISABLE_ALPHA = 0.5f;
    private static final float ENABLE_ALPHA = 1.0f;
    private OnCheckedChangedListener mCheckedChangedListener;
    private boolean mMutualModeClosed = false;

    public interface OnCheckedChangedListener {
        void onCheckedChange(Checkable checkable, int i, boolean z);
    }

    public DJIRadioLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setCheckedChangedListener(OnCheckedChangedListener checkedChangedListener) {
        this.mCheckedChangedListener = checkedChangedListener;
    }

    public void checkByIndex(int index) {
        if (index >= 0 && index <= getChildCount()) {
            updateCheckedStatus(index, false);
        }
    }

    public void checkById(int viewId) {
        View childView = findViewById(viewId);
        if (childView != null) {
            updateCheckedStatus(getChildIndexInParent(childView), false);
        }
    }

    public void setMutualModeClosed() {
        this.mMutualModeClosed = true;
    }

    public void resetMutualMode() {
        this.mMutualModeClosed = false;
    }

    private void init() {
        setClipToOutline(true);
    }

    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        if (child.getId() == -1) {
            child.setId(View.generateViewId());
        }
        child.setEnabled(isEnabled());
        if (child instanceof Checkable) {
            child.setOnClickListener(new DJIRadioLayout$$Lambda$0(this));
        }
        super.addView(child, index, params);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$addView$0$DJIRadioLayout(View v) {
        if (!((Checkable) v).isChecked() || this.mMutualModeClosed) {
            updateCheckedStatus(getChildIndexInParent(v), true);
        }
    }

    /* access modifiers changed from: protected */
    public LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LinearLayout.LayoutParams(0, -1, 1.0f);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).setEnabled(enabled);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (isEnabled()) {
            setAlpha(1.0f);
        } else {
            setAlpha(DISABLE_ALPHA);
        }
    }

    private int getChildIndexInParent(View v) {
        if (v == null) {
            return -1;
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i) == v) {
                return i;
            }
        }
        return -1;
    }

    private void updateCheckedStatus(int checkedIndex, boolean isCausedByUser) {
        int count = getChildCount();
        if (checkedIndex >= 0 && checkedIndex <= count) {
            for (int i = 0; i < count; i++) {
                if (i == checkedIndex) {
                    View childView = getChildAt(i);
                    if (childView instanceof Checkable) {
                        if (this.mMutualModeClosed) {
                            updateCheckedStatusInternal((Checkable) childView, !((Checkable) childView).isChecked(), i, isCausedByUser);
                        } else {
                            updateCheckedStatusInternal((Checkable) childView, true, i, isCausedByUser);
                        }
                    }
                } else if (!this.mMutualModeClosed) {
                    View childView2 = getChildAt(i);
                    if (childView2 instanceof Checkable) {
                        updateCheckedStatusInternal((Checkable) childView2, false, i, isCausedByUser);
                    }
                }
            }
        }
    }

    private void updateCheckedStatusInternal(Checkable checkable, boolean checked, int index, boolean isCausedByUser) {
        checkable.setChecked(checked);
        if ((this.mMutualModeClosed || checked) && this.mCheckedChangedListener != null) {
            this.mCheckedChangedListener.onCheckedChange(checkable, index, isCausedByUser);
        }
        if (checkable instanceof View) {
            ((View) checkable).invalidateOutline();
        }
    }
}
