package com.dji.privacyconfig.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import dji.pilot.publics.R;

@Keep
public class PullDownSwitchView extends LinearLayout {
    private boolean hasDescImg = false;
    private boolean isPullDown = false;
    private ImageView mDescImage;
    private TextView mDescTv;
    private Drawable mDownArrow;
    @Nullable
    private OnPullDownStateChangeListener mOnPullDownStateChangeListener;
    private Switch mSwitch;
    private TextView mTitleTv;
    private Drawable mUpArrow;

    public interface OnPullDownStateChangeListener {
        void onPullDownStateChange(boolean z);
    }

    public PullDownSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.privacy_flow_pulldown_switch_view, this);
        this.mTitleTv = (TextView) findViewById(R.id.pulldown_sw_title);
        this.mSwitch = (Switch) findViewById(R.id.pulldown_sw_btn);
        this.mDescTv = (TextView) findViewById(R.id.pulldown_sw_desc_tv);
        this.mDescImage = (ImageView) findViewById(R.id.pulldown_sw_desc_iv);
        this.mDownArrow = context.getResources().getDrawable(R.drawable.pulldown_sw_down_arrow);
        this.mUpArrow = context.getResources().getDrawable(R.drawable.pulldown_sw_down_up);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PulldownSwitchViewSt);
        String titleName = typedArray.getString(R.styleable.PulldownSwitchViewSt_switch_name);
        String descStr = typedArray.getString(R.styleable.PulldownSwitchViewSt_switch_desc);
        Drawable descImg = typedArray.getDrawable(R.styleable.PulldownSwitchViewSt_switch_desc_img);
        typedArray.recycle();
        this.mTitleTv.setText(titleName);
        this.mTitleTv.setOnClickListener(new View.OnClickListener() {
            /* class com.dji.privacyconfig.view.PullDownSwitchView.AnonymousClass1 */

            public void onClick(View v) {
                PullDownSwitchView.this.onTitleClickHandlePulldown();
            }
        });
        this.mDescTv.setText(descStr);
        this.mDescTv.setVisibility(8);
        if (descImg != null) {
            this.mDescImage.setImageDrawable(descImg);
            this.mDescImage.setVisibility(8);
            this.hasDescImg = true;
        }
    }

    /* access modifiers changed from: private */
    public void onTitleClickHandlePulldown() {
        this.isPullDown = !this.isPullDown;
        if (this.isPullDown) {
            this.mTitleTv.setCompoundDrawablesWithIntrinsicBounds(this.mUpArrow, (Drawable) null, (Drawable) null, (Drawable) null);
            this.mDescTv.setVisibility(0);
            if (this.hasDescImg) {
                this.mDescImage.setVisibility(0);
            }
        } else {
            pullUp();
        }
        if (this.mOnPullDownStateChangeListener != null) {
            this.mOnPullDownStateChangeListener.onPullDownStateChange(this.isPullDown);
        }
    }

    public void pullUp() {
        this.mTitleTv.setCompoundDrawablesWithIntrinsicBounds(this.mDownArrow, (Drawable) null, (Drawable) null, (Drawable) null);
        this.mDescTv.setVisibility(8);
        this.mDescImage.setVisibility(8);
    }

    public boolean isCheck() {
        return this.mSwitch.isChecked();
    }

    public void setChecked(boolean checked) {
        this.mSwitch.setChecked(checked);
    }

    public void setTitleTxt(CharSequence text) {
        this.mTitleTv.setText(text);
    }

    public void setDescTxt(CharSequence text) {
        this.mDescTv.setText(text);
    }

    public void setDescTextViewMovementMethod(MovementMethod method) {
        this.mDescTv.setMovementMethod(method);
    }

    public void setOnPullDownStateChangeListener(OnPullDownStateChangeListener onPullDownStateChangeListener) {
        this.mOnPullDownStateChangeListener = onPullDownStateChangeListener;
    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.mSwitch.setOnCheckedChangeListener(listener);
    }
}
