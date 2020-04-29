package com.dji.widget2.span;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import com.dji.widget2.R;

public class JumpClickSpan extends ClickableSpan {
    @ColorRes
    private int mColorRes;
    private Context mContext;
    private OnClickListener mListener;

    public interface OnClickListener {
        void onClick();
    }

    public JumpClickSpan(Context context, @NonNull OnClickListener listener) {
        this(context, R.color.white, listener);
    }

    public JumpClickSpan(Context context, @ColorRes int mColorRes2, @NonNull OnClickListener listener) {
        this.mColorRes = R.color.white;
        this.mColorRes = mColorRes2;
        this.mListener = listener;
        this.mContext = context;
    }

    public JumpClickSpan(JumpClickSpan span) {
        this.mColorRes = R.color.white;
        this.mListener = span.mListener;
        this.mContext = span.mContext;
    }

    public void updateDrawState(TextPaint ds) {
        ds.setColor(ContextCompat.getColor(this.mContext, this.mColorRes));
        ds.setTypeface(Typeface.DEFAULT_BOLD);
        ds.setUnderlineText(false);
    }

    public void onClick(View widget) {
        if (this.mListener != null) {
            this.mListener.onClick();
        }
    }
}
