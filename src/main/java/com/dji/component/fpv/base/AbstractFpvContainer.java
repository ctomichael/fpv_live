package com.dji.component.fpv.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public abstract class AbstractFpvContainer extends RelativeLayout implements IFpvContainer {
    public AbstractFpvContainer(Context context) {
        super(context);
    }

    public AbstractFpvContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbstractFpvContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbstractFpvContainer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ViewGroup getFpvRootView() {
        return this;
    }
}
