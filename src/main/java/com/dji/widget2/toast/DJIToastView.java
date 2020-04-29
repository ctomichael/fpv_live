package com.dji.widget2.toast;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.dji.widget2.R;

public class DJIToastView extends FrameLayout {
    private ImageView mIvHint;
    private TextView mTvContent;

    public @interface Theme {
        public static final int Dark = 2;
        public static final int Fpv = 3;
        public static final int Light = 1;
    }

    public @interface Layout {
        public static final int ICON_WITH_TEXT = 2;
        public static final int TEXT_ONLY = 1;
    }

    public DJIToastView(Context context) {
        this(context, null);
    }

    public DJIToastView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DJIToastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View}
     arg types: [int, com.dji.widget2.toast.DJIToastView, int]
     candidates:
      ClspMth{android.view.LayoutInflater.inflate(org.xmlpull.v1.XmlPullParser, android.view.ViewGroup, boolean):android.view.View}
      ClspMth{android.view.LayoutInflater.inflate(int, android.view.ViewGroup, boolean):android.view.View} */
    private void init() {
        if (!isInEditMode()) {
            ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R.layout.layout_fpv_toast, (ViewGroup) this, true);
            setBackground(ContextCompat.getDrawable(getContext(), R.drawable.toast_bg));
            setClipToOutline(true);
            this.mIvHint = (ImageView) findViewById(R.id.iv_hint);
            this.mTvContent = (TextView) findViewById(R.id.tv_content);
            setTheme(3);
        }
    }

    public void setLayoutType(@Layout int layoutType) {
        if (this.mIvHint != null && this.mTvContent != null) {
            switch (layoutType) {
                case 1:
                    this.mIvHint.setVisibility(8);
                    this.mTvContent.setVisibility(0);
                    return;
                case 2:
                    this.mIvHint.setVisibility(0);
                    this.mTvContent.setVisibility(0);
                    return;
                default:
                    return;
            }
        }
    }

    public void setTheme(@Theme int theme) {
        if (this.mIvHint != null && this.mTvContent != null) {
            switch (theme) {
                case 1:
                    setLightTheme();
                    return;
                case 2:
                    setDarkTheme();
                    return;
                case 3:
                    setFpvTheme();
                    return;
                default:
                    return;
            }
        }
    }

    public void setIcon(int resId) {
        if (this.mIvHint != null) {
            this.mIvHint.setImageResource(resId);
        }
    }

    public void setText(int resId) {
        if (this.mTvContent != null) {
            this.mTvContent.setText(resId);
        }
    }

    public void setText(CharSequence text) {
        if (this.mTvContent != null) {
            this.mTvContent.setText(text);
        }
    }

    private void setDarkTheme() {
        getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.album_005), PorterDuff.Mode.SRC_IN);
        this.mTvContent.setTextColor(ContextCompat.getColor(getContext(), R.color.white_100));
    }

    private void setLightTheme() {
        getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.grey_1), PorterDuff.Mode.SRC_IN);
        this.mTvContent.setTextColor(ContextCompat.getColor(getContext(), R.color.black_100));
    }

    private void setFpvTheme() {
        getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.black_80), PorterDuff.Mode.SRC_IN);
        this.mTvContent.setTextColor(ContextCompat.getColor(getContext(), R.color.white_100));
    }
}
