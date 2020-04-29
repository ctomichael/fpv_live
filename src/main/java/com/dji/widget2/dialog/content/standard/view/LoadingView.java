package com.dji.widget2.dialog.content.standard.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.DialogViewInterface;

public class LoadingView extends LinearLayout implements DialogViewInterface {
    protected ProgressBar mProgressBar;
    protected TextView mTvContent;

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View}
     arg types: [int, com.dji.widget2.dialog.content.standard.view.LoadingView, int]
     candidates:
      ClspMth{android.view.LayoutInflater.inflate(org.xmlpull.v1.XmlPullParser, android.view.ViewGroup, boolean):android.view.View}
      ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View} */
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.app_layout_dialog_loading_content, (ViewGroup) this, true);
    }

    public void setTheme(@NonNull DialogAttributes.Theme theme) {
        switch (theme) {
            case LIGHT:
                this.mTvContent.setTextColor(getResources().getColor(R.color.black));
                this.mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_circle_gradient));
                return;
            case FPV:
                this.mTvContent.setTextColor(getResources().getColor(R.color.white));
                this.mProgressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_circle_gradient_indark));
                return;
            default:
                return;
        }
    }

    public void setContent(@NonNull CharSequence text) {
        this.mTvContent.setText(text);
        updateVisibility();
    }

    public void setContent(@StringRes int resId) {
        this.mTvContent.setText(resId);
        updateVisibility();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTvContent = (TextView) findViewById(R.id.tv_content);
        this.mProgressBar = (ProgressBar) findViewById(R.id.loading_progress_bar);
    }

    private void updateVisibility() {
        this.mTvContent.setVisibility(TextUtils.isEmpty(this.mTvContent.getText()) ? 8 : 0);
    }
}
