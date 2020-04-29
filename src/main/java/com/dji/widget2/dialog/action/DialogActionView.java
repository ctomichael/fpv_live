package com.dji.widget2.dialog.action;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.DialogViewInterface;

public class DialogActionView extends LinearLayout implements DialogAction, DialogViewInterface {
    protected View mDivider1;
    protected View mDivider2;
    protected View mDividerHorizontal;
    protected TextView mTvNegative;
    protected TextView mTvNeutral;
    protected TextView mTvPositive;

    public DialogActionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setTheme(@NonNull DialogAttributes.Theme theme) {
        switch (theme) {
            case LIGHT:
                this.mTvNegative.setTextColor(getResources().getColor(R.color.black));
                this.mTvPositive.setTextColor(getResources().getColor(R.color.black));
                this.mTvNeutral.setTextColor(getResources().getColor(R.color.black));
                this.mDividerHorizontal.setBackgroundResource(R.color.black_05);
                this.mDivider1.setBackgroundResource(R.color.black_05);
                this.mDivider2.setBackgroundResource(R.color.black_05);
                return;
            case FPV:
                this.mTvNegative.setTextColor(getResources().getColor(R.color.white));
                this.mTvPositive.setTextColor(getResources().getColor(R.color.white));
                this.mTvNeutral.setTextColor(getResources().getColor(R.color.white));
                this.mDividerHorizontal.setBackgroundResource(R.color.white_10);
                this.mDivider1.setBackgroundResource(R.color.white_10);
                this.mDivider2.setBackgroundResource(R.color.white_10);
                return;
            default:
                return;
        }
    }

    @NonNull
    public DialogAction setNegativeButton(int resId, @Nullable View.OnClickListener listener) {
        this.mTvNegative.setText(resId);
        this.mTvNegative.setOnClickListener(listener);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setNegativeButton(@NonNull CharSequence text, @Nullable View.OnClickListener listener) {
        this.mTvNegative.setText(text);
        this.mTvNegative.setOnClickListener(listener);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setNegativeButtonText(int resId) {
        this.mTvNegative.setText(resId);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setNegativeButtonText(@NonNull CharSequence text) {
        this.mTvNegative.setText(text);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setNegativeButtonClickListener(@Nullable View.OnClickListener listener) {
        this.mTvNegative.setOnClickListener(listener);
        return this;
    }

    @NonNull
    public DialogAction setNegativeButtonColor(int color) {
        this.mTvNegative.setTextColor(color);
        return this;
    }

    @NonNull
    public DialogAction setNegativeButtonEnable(boolean enable) {
        this.mTvNegative.setEnabled(enable);
        return this;
    }

    @NonNull
    public DialogAction setPositiveButton(int resId, @NonNull View.OnClickListener listener) {
        this.mTvPositive.setText(resId);
        this.mTvPositive.setOnClickListener(listener);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setPositiveButton(@NonNull CharSequence text, @NonNull View.OnClickListener listener) {
        this.mTvPositive.setText(text);
        this.mTvPositive.setOnClickListener(listener);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setPositiveButtonText(int resId) {
        this.mTvPositive.setText(resId);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setPositiveButtonText(@NonNull CharSequence text) {
        this.mTvPositive.setText(text);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setPositiveButtonClickListener(@Nullable View.OnClickListener listener) {
        this.mTvPositive.setOnClickListener(listener);
        return this;
    }

    @NonNull
    public DialogAction setPositiveButtonColor(int color) {
        this.mTvPositive.setTextColor(color);
        return this;
    }

    @NonNull
    public DialogAction setPositiveButtonEnable(boolean enable) {
        this.mTvPositive.setEnabled(enable);
        return this;
    }

    @NonNull
    public DialogAction setNeutralButton(int resId, @NonNull View.OnClickListener listener) {
        this.mTvNeutral.setText(resId);
        this.mTvNeutral.setOnClickListener(listener);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setNeutralButton(@NonNull CharSequence text, @NonNull View.OnClickListener listener) {
        this.mTvNeutral.setText(text);
        this.mTvNeutral.setOnClickListener(listener);
        return this;
    }

    @NonNull
    public DialogAction setNeutralButtonText(int resId) {
        this.mTvNeutral.setText(resId);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setNeutralButtonText(@NonNull CharSequence text) {
        this.mTvNeutral.setText(text);
        updateVisibility();
        return this;
    }

    @NonNull
    public DialogAction setNeutralButtonClickListener(@Nullable View.OnClickListener listener) {
        this.mTvNeutral.setOnClickListener(listener);
        return this;
    }

    @NonNull
    public DialogAction setNeutralButtonColor(int color) {
        this.mTvNeutral.setTextColor(color);
        return this;
    }

    @NonNull
    public DialogAction setNeutralButtonEnable(boolean enable) {
        this.mTvNeutral.setEnabled(enable);
        return this;
    }

    private void init() {
        inflate(getContext(), R.layout.app_layout_dialog_action, this);
        this.mTvNegative = (TextView) findViewById(R.id.tv_negative);
        this.mTvPositive = (TextView) findViewById(R.id.tv_positive);
        this.mTvNeutral = (TextView) findViewById(R.id.tv_neutral);
        this.mDividerHorizontal = findViewById(R.id.view_content_divider);
        this.mDivider1 = findViewById(R.id.view_action_divider_1);
        this.mDivider2 = findViewById(R.id.view_action_divider_2);
        updateVisibility();
    }

    private void updateVisibility() {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        boolean isShown;
        int i6 = 0;
        TextView textView = this.mTvNegative;
        if (TextUtils.isEmpty(this.mTvNegative.getText())) {
            i = 8;
        } else {
            i = 0;
        }
        textView.setVisibility(i);
        TextView textView2 = this.mTvPositive;
        if (TextUtils.isEmpty(this.mTvPositive.getText())) {
            i2 = 8;
        } else {
            i2 = 0;
        }
        textView2.setVisibility(i2);
        TextView textView3 = this.mTvNeutral;
        if (TextUtils.isEmpty(this.mTvNeutral.getText())) {
            i3 = 8;
        } else {
            i3 = 0;
        }
        textView3.setVisibility(i3);
        View view = this.mDivider1;
        if (this.mTvNegative.getVisibility() == 0) {
            i4 = this.mTvPositive.getVisibility();
        } else {
            i4 = 8;
        }
        view.setVisibility(i4);
        View view2 = this.mDivider2;
        if (this.mTvNegative.getVisibility() == 0 || this.mTvPositive.getVisibility() == 0) {
            i5 = this.mTvNeutral.getVisibility();
        } else {
            i5 = 8;
        }
        view2.setVisibility(i5);
        if (this.mTvNegative.getVisibility() == 0 || this.mTvPositive.getVisibility() == 0 || this.mTvNeutral.getVisibility() == 0) {
            isShown = true;
        } else {
            isShown = false;
        }
        View view3 = this.mDividerHorizontal;
        if (!isShown) {
            i6 = 8;
        }
        view3.setVisibility(i6);
    }
}
