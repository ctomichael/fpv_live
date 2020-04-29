package com.dji.widget2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class RoundLinearLayout extends LinearLayout {
    private RectF rectF;
    private int roundLayoutRadius;
    private Path roundPath;

    public RoundLinearLayout(Context context) {
        super(context);
    }

    public RoundLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public RoundLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundLinearLayout);
        this.roundLayoutRadius = typedArray.getDimensionPixelSize(R.styleable.RoundLinearLayout_backgroundRadius, 0);
        typedArray.recycle();
        setWillNotDraw(false);
        this.roundPath = new Path();
        this.rectF = new RectF();
    }

    private void setRoundPath() {
        this.roundPath.addRoundRect(this.rectF, (float) this.roundLayoutRadius, (float) this.roundLayoutRadius, Path.Direction.CW);
    }

    public void setRoundLayoutRadius(int roundLayoutRadius2) {
        this.roundLayoutRadius = roundLayoutRadius2;
        setRoundPath();
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.rectF.set(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight());
        setRoundPath();
    }

    public void draw(Canvas canvas) {
        if (((float) this.roundLayoutRadius) > 0.0f) {
            canvas.clipPath(this.roundPath);
        }
        super.draw(canvas);
    }
}
