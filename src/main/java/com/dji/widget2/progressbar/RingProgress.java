package com.dji.widget2.progressbar;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;
import com.adobe.xmp.XMPError;
import com.dji.widget2.R;

public class RingProgress extends View {
    private boolean clockwise;
    private final int default_finished_color;
    private final int default_startingDegree;
    private final float default_stroke_width;
    private final int default_unfinished_color;
    private RectF finishedOuterRect;
    private Paint finishedPaint;
    private int finishedStrokeColor;
    private float finishedStrokeWidth;
    private int innerBackgroundColor;
    private Paint innerCirclePaint;
    private int max;
    private final int min_size;
    private float progress;
    private int startingDegree;
    private RectF unfinishedOuterRect;
    private Paint unfinishedPaint;
    private int unfinishedStrokeColor;
    private float unfinishedStrokeWidth;

    public RingProgress(Context context) {
        this(context, null);
    }

    public RingProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.progress = 0.0f;
        this.max = 100;
        this.default_finished_color = Color.rgb(255, 255, 255);
        this.default_unfinished_color = Color.rgb((int) XMPError.BADSTREAM, (int) XMPError.BADSTREAM, (int) XMPError.BADSTREAM);
        this.default_startingDegree = 0;
        this.finishedOuterRect = new RectF();
        this.unfinishedOuterRect = new RectF();
        this.min_size = (int) dp2px(getResources(), 100.0f);
        this.default_stroke_width = dp2px(getResources(), 4.0f);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RingProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void initPainters() {
        this.finishedPaint = new Paint();
        this.finishedPaint.setColor(this.finishedStrokeColor);
        this.finishedPaint.setStyle(Paint.Style.STROKE);
        this.finishedPaint.setAntiAlias(true);
        this.finishedPaint.setStrokeWidth(this.finishedStrokeWidth);
        this.finishedPaint.setStrokeCap(Paint.Cap.ROUND);
        this.unfinishedPaint = new Paint();
        this.unfinishedPaint.setColor(this.unfinishedStrokeColor);
        this.unfinishedPaint.setStyle(Paint.Style.STROKE);
        this.unfinishedPaint.setAntiAlias(true);
        this.unfinishedPaint.setStrokeWidth(this.unfinishedStrokeWidth);
        this.innerCirclePaint = new Paint();
        this.innerCirclePaint.setColor(this.innerBackgroundColor);
        this.innerCirclePaint.setAntiAlias(true);
    }

    public void initByAttributes(TypedArray attributes) {
        this.finishedStrokeColor = attributes.getColor(R.styleable.RingProgress_ring_finished_color, this.default_finished_color);
        this.unfinishedStrokeColor = attributes.getColor(R.styleable.RingProgress_ring_unfinished_color, this.default_unfinished_color);
        setProgress(attributes.getFloat(R.styleable.RingProgress_ring_progress, 0.0f));
        this.finishedStrokeWidth = attributes.getDimension(R.styleable.RingProgress_ring_finished_stroke_width, this.default_stroke_width);
        this.unfinishedStrokeWidth = attributes.getDimension(R.styleable.RingProgress_ring_unfinished_stroke_width, this.default_stroke_width);
        this.startingDegree = attributes.getInt(R.styleable.RingProgress_ring_circle_starting_degree, 0);
        this.clockwise = attributes.getBoolean(R.styleable.RingProgress_ring_circle_spin_clockwise, true);
    }

    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    private float getProgressAngle() {
        return (getProgress() / ((float) this.max)) * 360.0f;
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 100.0d) float progress2) {
        this.progress = progress2;
        if (this.progress > ((float) getMax())) {
            this.progress = (float) getMax();
        }
        invalidate();
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int max2) {
        if (max2 > 0) {
            this.max = max2;
            invalidate();
        }
    }

    public RingProgress setProgressNonInvalidate(@FloatRange(from = 0.0d, to = 100.0d) float progress2) {
        this.progress = progress2;
        if (this.progress > ((float) getMax())) {
            this.progress = (float) getMax();
        }
        return this;
    }

    public RingProgress setFinishedStrokeColorNonInvalidate(int finishedStrokeColor2) {
        this.finishedStrokeColor = finishedStrokeColor2;
        return this;
    }

    public RingProgress setUnfinishedStrokeColorNonInvalidate(int unfinishedStrokeColor2) {
        this.unfinishedStrokeColor = unfinishedStrokeColor2;
        return this;
    }

    public RingProgress setStartingDegreeNonInvalidate(int startingDegree2) {
        this.startingDegree = startingDegree2;
        return this;
    }

    public RingProgress setFinishedStrokeWidthNonInvalidate(float finishedStrokeWidth2) {
        this.finishedStrokeWidth = finishedStrokeWidth2;
        return this;
    }

    public RingProgress setUnfinishedStrokeWidthNonInvalidate(float unfinishedStrokeWidth2) {
        this.unfinishedStrokeWidth = unfinishedStrokeWidth2;
        return this;
    }

    public float getFinishedStrokeWidth() {
        return this.finishedStrokeWidth;
    }

    public void setFinishedStrokeWidth(float finishedStrokeWidth2) {
        this.finishedStrokeWidth = finishedStrokeWidth2;
        invalidate();
    }

    public float getUnfinishedStrokeWidth() {
        return this.unfinishedStrokeWidth;
    }

    public void setUnfinishedStrokeWidth(float unfinishedStrokeWidth2) {
        this.unfinishedStrokeWidth = unfinishedStrokeWidth2;
        invalidate();
    }

    public int getFinishedStrokeColor() {
        return this.finishedStrokeColor;
    }

    public void setFinishedStrokeColor(int finishedStrokeColor2) {
        this.finishedStrokeColor = finishedStrokeColor2;
        invalidate();
    }

    public int getUnfinishedStrokeColor() {
        return this.unfinishedStrokeColor;
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor2) {
        this.unfinishedStrokeColor = unfinishedStrokeColor2;
        invalidate();
    }

    public int getInnerBackgroundColor() {
        return this.innerBackgroundColor;
    }

    public void setInnerBackgroundColor(int innerBackgroundColor2) {
        this.innerBackgroundColor = innerBackgroundColor2;
        invalidate();
    }

    public int getStartingDegree() {
        return this.startingDegree;
    }

    public void setStartingDegree(int startingDegree2) {
        this.startingDegree = startingDegree2;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        if (mode == 1073741824) {
            return size;
        }
        int result = this.min_size;
        if (mode == Integer.MIN_VALUE) {
            return Math.min(result, size);
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float delta = Math.max(this.finishedStrokeWidth, this.unfinishedStrokeWidth);
        this.finishedOuterRect.set(delta, delta, ((float) getWidth()) - delta, ((float) getHeight()) - delta);
        this.unfinishedOuterRect.set(delta, delta, ((float) getWidth()) - delta, ((float) getHeight()) - delta);
        float finishedArcAngle = this.clockwise ? getProgressAngle() : -getProgressAngle();
        float unfinishedArcAngle = this.clockwise ? 360.0f - getProgressAngle() : getProgressAngle() - 360.0f;
        canvas.drawCircle(((float) getWidth()) / 2.0f, ((float) getHeight()) / 2.0f, ((((float) getWidth()) - Math.min(this.finishedStrokeWidth, this.unfinishedStrokeWidth)) + Math.abs(this.finishedStrokeWidth - this.unfinishedStrokeWidth)) / 2.0f, this.innerCirclePaint);
        canvas.drawArc(this.unfinishedOuterRect, ((float) getStartingDegree()) + finishedArcAngle, unfinishedArcAngle, false, this.unfinishedPaint);
        canvas.drawArc(this.finishedOuterRect, (float) getStartingDegree(), finishedArcAngle, false, this.finishedPaint);
    }

    public static float dp2px(Resources resources, float dp) {
        return (dp * resources.getDisplayMetrics().density) + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        return sp * resources.getDisplayMetrics().scaledDensity;
    }
}
