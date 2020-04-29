package com.dji.component.fpv.widget.histogram;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.dji.component.fpv.widget.preview.R;
import dji.fieldAnnotation.EXClassNullAway;
import dji.publics.interfaces.DJIViewShowInterface;
import java.util.ArrayList;

@EXClassNullAway
public class HistogramChartView extends View implements DJIViewShowInterface {
    private static final boolean TEST = false;
    protected int mBgColor = 0;
    protected int mBorderColor = 0;
    protected float mCubicIntensity = 0.2f;
    protected float[] mData = null;
    private int mDeltaX = 0;
    private int mDeltaY = 0;
    protected boolean mEnableCubic = true;
    protected int mFillColor = 0;
    protected int mGridWidth = 2;
    private boolean mIsDragging = false;
    protected int mLineColor = 0;
    private ConstraintLayout.LayoutParams mLyParam = null;
    protected final Paint mPaint = new Paint();
    protected final Path mPath = new Path();
    protected final ArrayList<CPoint> mPoints = new ArrayList<>();
    protected float mXInterval = 0.0f;
    protected float mXMax = 58.0f;
    protected float mYInterval = 0.0f;
    protected float mYMax = 256.0f;
    protected float mYOffset = 0.0f;
    protected boolean mbDrawGrid = true;

    public HistogramChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(float[] datas) {
        this.mData = datas;
        if (this.mXInterval != 0.0f) {
            int w = getWidth();
            int h = getHeight();
            transformDataToPoints(w, h);
            resetPath(w, h);
            postInvalidate();
        }
    }

    public void show() {
        if (getVisibility() != 0) {
            setVisibility(0);
        }
    }

    public void hide() {
        if (getVisibility() != 4) {
            setVisibility(4);
        }
    }

    public void go() {
        if (getVisibility() != 8) {
            setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            Resources res = getResources();
            this.mBgColor = res.getColor(R.color.black_half);
            this.mFillColor = res.getColor(R.color.chart_fill);
            this.mLineColor = res.getColor(R.color.chart_line);
            this.mBorderColor = res.getColor(R.color.chart_border);
            setWillNotDraw(false);
            this.mPaint.setAntiAlias(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawColor(this.mBgColor);
        if (this.mbDrawGrid) {
            drawGrid(canvas);
        }
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(this.mFillColor);
        canvas.drawPath(this.mPath, this.mPaint);
    }

    private void drawGrid(Canvas canvas) {
        float w = (float) getWidth();
        float h = (float) getHeight();
        this.mPaint.setStrokeWidth((float) this.mGridWidth);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(this.mBorderColor);
        canvas.drawRect(0.0f, 0.0f, w, h, this.mPaint);
        this.mPaint.setColor(this.mLineColor);
        float horizontalOffset = (w - ((float) (this.mGridWidth * 6))) / 5.0f;
        float x = ((float) Math.round(((float) this.mGridWidth) * 0.5f)) + horizontalOffset + ((float) this.mGridWidth);
        int i = 0;
        while (i < 4) {
            canvas.drawLine(x, 1.0f, x, h, this.mPaint);
            i++;
            x += ((float) this.mGridWidth) + horizontalOffset;
        }
    }

    private void resetPath(int w, int h) {
        if (this.mEnableCubic) {
            resetCubicPath(w, h);
        } else {
            resetLinearPath(w, h);
        }
    }

    private void calculateInterval(int w, int h) {
        this.mYOffset = 20.0f;
        this.mXInterval = ((float) w) / (this.mXMax - 1.0f);
        this.mYInterval = (((float) h) - this.mYOffset) / this.mYMax;
    }

    private void transformDataToPoints(int w, int h) {
        this.mPoints.clear();
        if (this.mData != null && this.mData.length > 1) {
            this.mPoints.add(new CPoint(0.0f, ((float) h) - (this.mData[0] * this.mYInterval)));
            int length = this.mData.length;
            for (int j = 1; j < length - 1; j++) {
                this.mPoints.add(new CPoint(((float) j) * this.mXInterval, ((float) h) - (this.mData[j] * this.mYInterval)));
            }
            this.mPoints.add(new CPoint((float) w, ((float) h) - (this.mData[length - 1] * this.mYInterval)));
        }
    }

    private void resetLinearPath(int w, int h) {
        this.mPath.reset();
        if (!this.mPoints.isEmpty()) {
            this.mPath.moveTo(this.mPoints.get(0).mX, this.mPoints.get(0).mY);
            int size = this.mPoints.size();
            for (int i = 1; i < size; i++) {
                CPoint p = this.mPoints.get(i);
                this.mPath.lineTo(p.mX, p.mY);
            }
            this.mPath.lineTo(this.mPoints.get(size - 1).mX, (float) h);
            this.mPath.lineTo(0.0f, (float) h);
        }
        this.mPath.close();
    }

    private void resetCubicPath(int w, int h) {
        this.mPath.reset();
        if (!this.mPoints.isEmpty()) {
            int size = this.mPoints.size();
            for (int i = 0; i < size; i++) {
                CPoint point = this.mPoints.get(i);
                if (i == 0) {
                    CPoint next = this.mPoints.get(i + 1);
                    next.mDx = (next.mX - point.mX) * this.mCubicIntensity;
                    next.mDy = (next.mY - point.mY) * this.mCubicIntensity;
                } else if (i == size - 1) {
                    CPoint prev = this.mPoints.get(i - 1);
                    point.mDx = (point.mX - prev.mX) * this.mCubicIntensity;
                    point.mDy = (point.mY - prev.mY) * this.mCubicIntensity;
                } else {
                    CPoint next2 = this.mPoints.get(i + 1);
                    CPoint prev2 = this.mPoints.get(i - 1);
                    point.mDx = (next2.mX - prev2.mX) * this.mCubicIntensity;
                    point.mDy = (next2.mY - prev2.mY) * this.mCubicIntensity;
                }
                if (i == 0) {
                    this.mPath.moveTo(point.mX, point.mY);
                } else {
                    CPoint prev3 = this.mPoints.get(i - 1);
                    this.mPath.cubicTo(prev3.mX + prev3.mDx, prev3.mY + prev3.mDy, point.mX - point.mDx, point.mY - point.mDy, point.mX, point.mY);
                }
            }
            this.mPath.lineTo(this.mPoints.get(size - 1).mX, (float) h);
            this.mPath.lineTo(0.0f, (float) h);
        }
        this.mPath.close();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (!isInEditMode() && w != oldw) {
            calculateInterval(w, h);
            transformDataToPoints(w, h);
            resetPath(w, h);
        }
    }

    private static final class CPoint {
        public float mDx;
        public float mDy;
        public float mX;
        public float mY;

        private CPoint(float x, float y) {
            this.mX = 0.0f;
            this.mY = 0.0f;
            this.mDx = 0.0f;
            this.mDy = 0.0f;
            this.mX = x;
            this.mY = y;
        }
    }

    private void trackMotion(MotionEvent event) {
        int i = 0;
        int x = (int) (event.getX() - ((float) this.mDeltaX));
        int y = (int) (event.getY() - ((float) this.mDeltaY));
        int marginLeft = this.mLyParam.leftMargin + x;
        int marginBottom = this.mLyParam.bottomMargin - y;
        ConstraintLayout.LayoutParams layoutParams = this.mLyParam;
        if (marginLeft < 0) {
            marginLeft = 0;
        }
        layoutParams.leftMargin = marginLeft;
        ConstraintLayout.LayoutParams layoutParams2 = this.mLyParam;
        if (marginBottom >= 0) {
            i = marginBottom;
        }
        layoutParams2.bottomMargin = i;
        setLayoutParams(this.mLyParam);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                this.mIsDragging = true;
                this.mLyParam = (ConstraintLayout.LayoutParams) getLayoutParams();
                this.mDeltaX = (int) event.getX();
                this.mDeltaY = (int) event.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case 1:
            case 3:
                if (this.mIsDragging) {
                    this.mIsDragging = false;
                    break;
                }
                break;
            case 2:
                if (this.mIsDragging) {
                    trackMotion(event);
                    break;
                }
                break;
        }
        return true;
    }
}
