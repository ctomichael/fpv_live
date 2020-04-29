package com.dji.widget2;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Switch;

public class DJISwitch2 extends Switch {
    private static int STYLE_FPV = 1;
    private static int STYLE_NORMAL = 0;
    private float mStateAlpha = 0.3f;
    private int mStyle = STYLE_NORMAL;
    private View mView;

    public DJISwitch2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithStyle(context, attrs);
    }

    private void initWithStyle(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.DJISwitch2);
        this.mStyle = attributes.getInt(R.styleable.DJISwitch2_switch_dji_style, this.mStyle);
        attributes.recycle();
        setMinWidth(context.getResources().getDimensionPixelSize(R.dimen.cirsw_width));
        if (this.mStyle == STYLE_NORMAL) {
            setTrackResource(R.drawable.cirsw_track);
            setThumbResource(R.drawable.cirsw_thumb);
        } else if (this.mStyle == STYLE_FPV) {
            setTrackResource(R.drawable.cirsw_track_fpv);
            setThumbResource(R.drawable.cirsw_thumb);
        }
        setTextOn("");
        setTextOff("");
        setThumbTextPadding(context.getResources().getDimensionPixelSize(R.dimen.cirsw_text_padding));
    }

    public void setRelativeStateView(View view) {
        this.mView = view;
    }

    public void setRelativeStateView(View view, float stateAlpha) {
        this.mView = view;
        this.mStateAlpha = stateAlpha;
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (isFocused() || !isEnabled()) {
            setAlpha(this.mStateAlpha);
            if (this.mView != null) {
                this.mView.setAlpha(this.mStateAlpha);
                return;
            }
            return;
        }
        setAlpha(1.0f);
        if (this.mView != null) {
            this.mView.setAlpha(1.0f);
        }
    }
}
