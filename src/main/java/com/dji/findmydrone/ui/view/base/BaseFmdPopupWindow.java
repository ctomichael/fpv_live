package com.dji.findmydrone.ui.view.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public abstract class BaseFmdPopupWindow extends PopupWindow {
    protected View mContentView;
    protected Context mContext;

    /* access modifiers changed from: protected */
    public abstract int getLayoutId();

    /* access modifiers changed from: protected */
    public abstract void show(View view);

    public BaseFmdPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        setBackgroundDrawable(null);
        this.mContentView = LayoutInflater.from(context).inflate(getLayoutId(), (ViewGroup) null);
        setContentView(this.mContentView);
        setWidth(-2);
        setHeight(-2);
        this.mContentView.measure(makeDropDownMeasureSpec(getWidth()), makeDropDownMeasureSpec(getHeight()));
    }

    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == -2) {
            mode = 0;
        } else {
            mode = 1073741824;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
}
