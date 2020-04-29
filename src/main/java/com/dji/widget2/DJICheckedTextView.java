package com.dji.widget2;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.util.AttributeSet;

public class DJICheckedTextView extends AppCompatCheckedTextView {
    private static final int[] CHECKED_STATE_SET = {16842912};

    public DJICheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        updateTextColorAndBackgroundColor(isChecked());
        setGravity(17);
        setTextAlignment(4);
        setTextSize(0, (float) getContext().getResources().getDimensionPixelSize(R.dimen.s_15_dp));
    }

    /* access modifiers changed from: protected */
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        updateTextColorAndBackgroundColor(checked);
        refreshDrawableState();
    }

    private void updateTextColorAndBackgroundColor(boolean checked) {
        if (checked) {
            setTextColor(ContextCompat.getColor(getContext(), R.color.black_100));
            setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey_1));
            return;
        }
        setTextColor(ContextCompat.getColor(getContext(), R.color.grey_1));
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_10));
    }
}
