package com.dji.widget2.dialog.content.standard.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dji.widget2.R;
import com.dji.widget2.dialog.DialogAttributes;
import com.dji.widget2.dialog.DialogViewInterface;

public class MessageView extends LinearLayout implements DialogViewInterface {
    protected ImageView mIvIcon;
    protected TextView mTvMain;
    protected TextView mTvSub;

    public MessageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTheme(@NonNull DialogAttributes.Theme theme) {
        switch (theme) {
            case LIGHT:
                this.mTvMain.setTextColor(getResources().getColor(R.color.black));
                this.mTvSub.setTextColor(getResources().getColor(R.color.black));
                return;
            case FPV:
                this.mTvMain.setTextColor(getResources().getColor(R.color.white));
                this.mTvSub.setTextColor(getResources().getColor(R.color.white));
                return;
            default:
                return;
        }
    }

    public void setMainText(@NonNull CharSequence text) {
        this.mTvMain.setText(text);
        updateVisibility();
    }

    public void setMainText(@StringRes int resId) {
        this.mTvMain.setText(resId);
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

    public void setSubTextGravity(int gravity) {
        this.mTvSub.setGravity(gravity);
    }

    public void setIcon(@DrawableRes int resId) {
        this.mIvIcon.setVisibility(0);
        this.mIvIcon.setImageResource(resId);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTvMain = (TextView) findViewById(R.id.tv_main);
        this.mTvSub = (TextView) findViewById(R.id.tv_second);
        this.mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        this.mIvIcon.setVisibility(8);
        this.mTvSub.setMovementMethod(ScrollingMovementMethod.getInstance());
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
