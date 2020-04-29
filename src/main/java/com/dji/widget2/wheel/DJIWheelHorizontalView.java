package com.dji.widget2.wheel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import com.dji.widget2.R;
import com.dji.widget2.wheel.DJIWheelAdapter;
import com.dji.widget2.wheel.DJIWheelView;

public class DJIWheelHorizontalView extends DJIWheelView {
    public /* bridge */ /* synthetic */ void releaseFreeze() {
        super.releaseFreeze();
    }

    public /* bridge */ /* synthetic */ void reset() {
        super.reset();
    }

    public /* bridge */ /* synthetic */ void setAdapter(@NonNull DJIWheelAdapter dJIWheelAdapter) {
        super.setAdapter(dJIWheelAdapter);
    }

    public /* bridge */ /* synthetic */ void setArrowVisibility(boolean z) {
        super.setArrowVisibility(z);
    }

    public /* bridge */ /* synthetic */ void setEnabled(boolean z) {
        super.setEnabled(z);
    }

    public /* bridge */ /* synthetic */ void setFreezeAfterOperation(boolean z) {
        super.setFreezeAfterOperation(z);
    }

    public /* bridge */ /* synthetic */ void setOnWheelPointerChangeListener(@Nullable DJIWheelView.OnWheelPointerChangeListener onWheelPointerChangeListener) {
        super.setOnWheelPointerChangeListener(onWheelPointerChangeListener);
    }

    public /* bridge */ /* synthetic */ void setPointerPosition(int i, boolean z) {
        super.setPointerPosition(i, z);
    }

    public DJIWheelHorizontalView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, DJIWheelAdapter.Orientation.HORIZONTAL);
        inflate(getContext(), R.layout.fpv_widget_wheel_horizontal_view, this);
    }
}
