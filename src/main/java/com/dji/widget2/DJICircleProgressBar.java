package com.dji.widget2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

public class DJICircleProgressBar extends View {
    private int mDirection;
    private int mInsideColor;
    private int mMaxProgress;
    private int mOutsideColor;
    private float mOutsideRadius;
    private Paint mPaint;
    private float mProgress;
    private float mProgressWidth;

    enum DirectionEnum {
        LEFT(0, 180.0f),
        TOP(1, 270.0f),
        RIGHT(2, 0.0f),
        BOTTOM(3, 90.0f);
        
        private final float degree;
        private final int direction;

        private DirectionEnum(int direction2, float degree2) {
            this.direction = direction2;
            this.degree = degree2;
        }

        public int getDirection() {
            return this.direction;
        }

        public float getDegree() {
            return this.degree;
        }

        public boolean equalsDescription(int direction2) {
            return this.direction == direction2;
        }

        public static DirectionEnum getDirection(int direction2) {
            DirectionEnum[] values = values();
            for (DirectionEnum enumObject : values) {
                if (enumObject.equalsDescription(direction2)) {
                    return enumObject;
                }
            }
            return RIGHT;
        }

        public static float getDegree(int direction2) {
            DirectionEnum enumObject = getDirection(direction2);
            if (enumObject == null) {
                return 0.0f;
            }
            return enumObject.getDegree();
        }
    }

    public DJICircleProgressBar(Context context) {
        this(context, null);
    }

    public DJICircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DJICircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMaxProgress = 100;
        this.mProgress = 0.0f;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomCircleProgressBar, defStyleAttr, 0);
        this.mOutsideColor = a.getColor(R.styleable.CustomCircleProgressBar_outside_color, ContextCompat.getColor(getContext(), R.color.cirsw_track_check));
        this.mOutsideRadius = a.getDimension(R.styleable.CustomCircleProgressBar_outside_radius, (float) getResources().getDimensionPixelSize(R.dimen.s_15_dp));
        this.mInsideColor = a.getColor(R.styleable.CustomCircleProgressBar_inside_color, ContextCompat.getColor(getContext(), R.color.cirsw_hover));
        this.mProgressWidth = a.getDimension(R.styleable.CustomCircleProgressBar_progress_width, (float) getResources().getDimensionPixelSize(R.dimen.s_2_dp));
        this.mDirection = a.getInt(R.styleable.CustomCircleProgressBar_direction, 1);
        a.recycle();
        this.mPaint = new Paint();
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"DrawAllocation"})
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int circlePoint = getWidth() / 2;
        this.mPaint.setColor(this.mInsideColor);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mProgressWidth);
        this.mPaint.setAntiAlias(true);
        float radius = (((float) getWidth()) - this.mProgressWidth) / 2.0f;
        canvas.drawCircle((float) circlePoint, (float) circlePoint, radius, this.mPaint);
        this.mPaint.setColor(this.mOutsideColor);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(new RectF(((float) circlePoint) - radius, ((float) circlePoint) - radius, ((float) circlePoint) + radius, ((float) circlePoint) + radius), DirectionEnum.getDegree(this.mDirection), (this.mProgress / ((float) this.mMaxProgress)) * 360.0f, false, this.mPaint);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int size = View.MeasureSpec.getSize(widthMeasureSpec);
        if (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824) {
            width = size;
        } else {
            width = (int) (this.mOutsideRadius * 2.0f);
        }
        int size2 = View.MeasureSpec.getSize(heightMeasureSpec);
        if (View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) {
            height = size2;
        } else {
            height = (int) (this.mOutsideRadius * 2.0f);
        }
        setMeasuredDimension(width, height);
    }

    public void setOutsideColor(@ColorRes int outsideColor) {
        this.mOutsideColor = ContextCompat.getColor(getContext(), outsideColor);
    }

    public float getOutsideRadius() {
        return this.mOutsideRadius;
    }

    public void setOutsideRadius(float outsideRadius) {
        this.mOutsideRadius = outsideRadius;
    }

    public void setInsideColor(@ColorRes int insideColor) {
        this.mInsideColor = ContextCompat.getColor(getContext(), insideColor);
    }

    public float getProgressWidth() {
        return this.mProgressWidth;
    }

    public void setProgressWidth(float progressWidth) {
        this.mProgressWidth = progressWidth;
    }

    public synchronized int getMaxProgress() {
        return this.mMaxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            throw new IllegalArgumentException("maxProgress should not be less than 0");
        }
        this.mMaxProgress = maxProgress;
    }

    public synchronized float getProgress() {
        return this.mProgress;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > this.mMaxProgress) {
            progress = this.mMaxProgress;
        }
        this.mProgress = (float) progress;
        postInvalidate();
    }
}
