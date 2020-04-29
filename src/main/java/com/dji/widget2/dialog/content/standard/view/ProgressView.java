package com.dji.widget2.dialog.content.standard.view;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.DialogViewInterface;
import com.dji.widget2.progressbar.ExtendRingProgress;

public class ProgressView extends LinearLayout implements DialogViewInterface {
    protected ExtendRingProgress mProgressBar;
    protected TextView mTvMain;
    protected TextView mTvSub;

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTheme(@NonNull DialogAttributes.Theme theme) {
        switch (theme) {
            case LIGHT:
                this.mProgressBar.setFinishedStrokeColor(getResources().getColor(R.color.grey_10));
                this.mProgressBar.setUnfinishedStrokeColor(getResources().getColor(R.color.black_10));
                this.mProgressBar.setContentTextColor(getResources().getColor(R.color.black_100));
                this.mTvMain.setTextColor(getResources().getColor(R.color.black_100));
                return;
            case FPV:
                this.mProgressBar.setFinishedStrokeColor(getResources().getColor(R.color.white_90));
                this.mProgressBar.setUnfinishedStrokeColor(getResources().getColor(R.color.white_10));
                this.mProgressBar.setContentTextColor(getResources().getColor(R.color.white_100));
                this.mTvMain.setTextColor(getResources().getColor(R.color.white_100));
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTvMain = (TextView) findViewById(R.id.progress_dialog_main_text);
        this.mProgressBar = (ExtendRingProgress) findViewById(R.id.progress_dialog_progress_bar);
        this.mTvSub = (TextView) findViewById(R.id.progress_dialog_sub_text);
        this.mProgressBar.setMax(100);
        this.mProgressBar.setContentTextAutoUpdate(true);
    }

    public void setMainText(@StringRes int resId) {
        this.mTvMain.setText(resId);
        updateVisibility();
    }

    public void setMainText(@NonNull CharSequence text) {
        this.mTvMain.setText(text);
        updateVisibility();
    }

    public void setSubText(@NonNull CharSequence text) {
        this.mTvSub.setText(text);
        updateVisibility();
    }

    public void setSubText(@StringRes int resId) {
        this.mTvSub.setText(resId);
        updateVisibility();
    }

    public void setProgressTextColor(@ColorInt int color) {
        this.mProgressBar.setContentTextColor(color);
    }

    public void setProgress(float percent) {
        this.mProgressBar.setProgress(100.0f * percent);
    }

    public void setProgressColor(@ColorInt int color) {
        this.mProgressBar.setFinishedStrokeColor(color);
    }

    public void setProgressBackgroundColor(@ColorInt int color) {
        this.mProgressBar.setUnfinishedStrokeColor(color);
    }

    public void setAutoUpdateProgressText(boolean isAutoUpdate) {
        this.mProgressBar.setContentTextAutoUpdate(isAutoUpdate);
    }

    public void setProgressText(@NonNull CharSequence text) {
        this.mProgressBar.setContentText(text);
    }

    public void setShowProgressText(boolean isShown) {
        if (isShown) {
            this.mProgressBar.switchToCenterText();
        } else {
            this.mProgressBar.hideCenterContent();
        }
    }

    private void updateVisibility() {
        int i;
        int i2 = 8;
        TextView textView = this.mTvMain;
        if (TextUtils.isEmpty(this.mTvMain.getText())) {
            i = 8;
        } else {
            i = 0;
        }
        textView.setVisibility(i);
        TextView textView2 = this.mTvSub;
        if (!TextUtils.isEmpty(this.mTvSub.getText())) {
            i2 = 0;
        }
        textView2.setVisibility(i2);
    }
}
