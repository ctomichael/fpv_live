package com.dji.widget2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class CircleProgressBar extends View {
    /* access modifiers changed from: private */
    public int mCircleCenterX;
    /* access modifiers changed from: private */
    public int mCircleCenterY;
    /* access modifiers changed from: private */
    public RectF mCircleRectF = new RectF();
    private Paint mPaint = new Paint();
    private float mPercent;
    private int mProgressColor;
    private int mProgressTextColor;
    private Rect mProgressTextRect = new Rect();
    private int mProgressTextSize;
    private float mRadius;
    private int mRingColor;
    /* access modifiers changed from: private */
    public float mRingWidth;
    private boolean mTextVisible;

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        this.mRingColor = typedArray.getColor(R.styleable.CircleProgressBar_ring_color, -7829368);
        this.mProgressColor = typedArray.getColor(R.styleable.CircleProgressBar_progress_color, -1);
        this.mRingWidth = typedArray.getDimension(R.styleable.CircleProgressBar_ring_width, getResources().getDimension(R.dimen.s_4_dp));
        this.mRadius = typedArray.getDimension(R.styleable.CircleProgressBar_radius, getResources().getDimension(R.dimen.s_67_dp));
        this.mProgressTextColor = typedArray.getColor(R.styleable.CircleProgressBar_text_color, -1);
        this.mProgressTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressBar_text_size, getResources().getDimensionPixelSize(R.dimen.s_14_dp));
        this.mTextVisible = typedArray.getBoolean(R.styleable.CircleProgressBar_text_visible, true);
        typedArray.recycle();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            /* class com.dji.widget2.CircleProgressBar.AnonymousClass1 */

            public void onGlobalLayout() {
                CircleProgressBar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int unused = CircleProgressBar.this.mCircleCenterX = CircleProgressBar.this.getWidth() / 2;
                int unused2 = CircleProgressBar.this.mCircleCenterY = CircleProgressBar.this.getHeight() / 2;
                RectF unused3 = CircleProgressBar.this.mCircleRectF = new RectF(CircleProgressBar.this.mRingWidth / 2.0f, CircleProgressBar.this.mRingWidth / 2.0f, ((float) CircleProgressBar.this.getWidth()) - (CircleProgressBar.this.mRingWidth / 2.0f), ((float) CircleProgressBar.this.getHeight()) - (CircleProgressBar.this.mRingWidth / 2.0f));
            }
        });
    }

    public void setPercent(float percent) {
        this.mPercent = percent;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        if (View.MeasureSpec.getMode(widthMeasureSpec) == 1073741824) {
            width = widthSize;
        } else {
            width = (int) ((this.mRadius * 2.0f) + this.mRingWidth);
        }
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (View.MeasureSpec.getMode(heightMeasureSpec) == 1073741824) {
            height = heightSize;
        } else {
            height = (int) ((this.mRadius * 2.0f) + this.mRingWidth);
        }
        setMeasuredDimension(width, height);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCircleCenterX = getWidth() / 2;
        this.mCircleCenterY = getHeight() / 2;
        this.mCircleRectF.left = this.mRingWidth / 2.0f;
        this.mCircleRectF.top = this.mRingWidth / 2.0f;
        this.mCircleRectF.right = ((float) getWidth()) - (this.mRingWidth / 2.0f);
        this.mCircleRectF.bottom = ((float) getHeight()) - (this.mRingWidth / 2.0f);
        this.mPaint.setColor(this.mRingColor);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(this.mRingWidth);
        this.mPaint.setAntiAlias(true);
        canvas.drawCircle((float) this.mCircleCenterX, (float) this.mCircleCenterY, this.mRadius, this.mPaint);
        this.mPaint.setColor(this.mProgressColor);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(this.mCircleRectF, 270.0f, this.mPercent * 360.0f, false, this.mPaint);
        if (this.mTextVisible) {
            this.mPaint.setColor(this.mProgressTextColor);
            this.mPaint.setTextSize((float) this.mProgressTextSize);
            this.mPaint.setStrokeWidth(0.0f);
            this.mPaint.setStyle(Paint.Style.FILL);
            String progressText = ((int) (this.mPercent * 100.0f)) + "%";
            this.mPaint.getTextBounds(progressText, 0, progressText.length(), this.mProgressTextRect);
            Paint.FontMetricsInt fontMetrics = this.mPaint.getFontMetricsInt();
            canvas.drawText(progressText, (float) ((getMeasuredWidth() / 2) - (this.mProgressTextRect.width() / 2)), (float) ((((getMeasuredHeight() - fontMetrics.bottom) + fontMetrics.top) / 2) - fontMetrics.top), this.mPaint);
        }
    }
}
