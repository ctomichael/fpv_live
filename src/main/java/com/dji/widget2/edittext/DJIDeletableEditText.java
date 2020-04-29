package com.dji.widget2.edittext;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.dji.widget2.R;

public class DJIDeletableEditText extends DJIForbidCNEdtiText {
    private final int RIGHT = 2;
    private Drawable mDrawableDelete;
    private Drawable mDrawableOff;

    public DJIDeletableEditText(Context context) {
        super(context);
        init();
    }

    public DJIDeletableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DJIDeletableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DJIDeletableEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.mDrawableDelete = getContext().getDrawable(R.drawable.account_ui_text_delete);
        this.mDrawableDelete.setBounds(0, 0, this.mDrawableDelete.getMinimumWidth(), this.mDrawableDelete.getMinimumHeight());
    }

    /* access modifiers changed from: protected */
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (getText().toString().length() == 0) {
            setCompoundDrawables(null, null, null, null);
        } else if (getText().toString().length() > 0) {
            setCompoundDrawables(null, null, this.mDrawableDelete, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 1:
                Drawable drawableRight = getCompoundDrawables()[2];
                if (drawableRight != null && event.getRawX() >= ((float) (((getRight() - drawableRight.getBounds().width()) - getCompoundDrawablePadding()) - getPaddingRight()))) {
                    delete();
                    return true;
                }
        }
        return super.onTouchEvent(event);
    }

    private void delete() {
        setText((CharSequence) null);
    }
}
