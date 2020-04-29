package com.mapbox.mapboxsdk.annotations;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.mapbox.mapboxsdk.R;

@Deprecated
public class BubbleLayout extends LinearLayout {
    public static final float DEFAULT_STROKE_WIDTH = -1.0f;
    private ArrowDirection arrowDirection;
    private float arrowHeight;
    private float arrowPosition;
    private float arrowWidth;
    private Bubble bubble;
    private int bubbleColor;
    private float cornersRadius;
    private int strokeColor;
    private float strokeWidth;

    public BubbleLayout(@NonNull Context context) {
        this(context, null, 0);
    }

    public BubbleLayout(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.mapbox_BubbleLayout);
        this.arrowDirection = new ArrowDirection(a.getInt(R.styleable.mapbox_BubbleLayout_mapbox_bl_arrowDirection, 0));
        this.arrowWidth = a.getDimension(R.styleable.mapbox_BubbleLayout_mapbox_bl_arrowWidth, convertDpToPixel(8.0f, context));
        this.arrowHeight = a.getDimension(R.styleable.mapbox_BubbleLayout_mapbox_bl_arrowHeight, convertDpToPixel(8.0f, context));
        this.arrowPosition = a.getDimension(R.styleable.mapbox_BubbleLayout_mapbox_bl_arrowPosition, convertDpToPixel(12.0f, context));
        this.cornersRadius = a.getDimension(R.styleable.mapbox_BubbleLayout_mapbox_bl_cornersRadius, 0.0f);
        this.bubbleColor = a.getColor(R.styleable.mapbox_BubbleLayout_mapbox_bl_bubbleColor, -1);
        this.strokeWidth = a.getDimension(R.styleable.mapbox_BubbleLayout_mapbox_bl_strokeWidth, -1.0f);
        this.strokeColor = a.getColor(R.styleable.mapbox_BubbleLayout_mapbox_bl_strokeColor, -7829368);
        a.recycle();
        initPadding();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initDrawable(0, getWidth(), 0, getHeight());
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(@NonNull Canvas canvas) {
        if (this.bubble != null) {
            this.bubble.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }

    static float convertDpToPixel(float dp, Context context) {
        return ((float) (context.getResources().getDisplayMetrics().densityDpi / 160)) * dp;
    }

    public ArrowDirection getArrowDirection() {
        return this.arrowDirection;
    }

    @NonNull
    public BubbleLayout setArrowDirection(ArrowDirection arrowDirection2) {
        resetPadding();
        this.arrowDirection = arrowDirection2;
        initPadding();
        return this;
    }

    public float getArrowWidth() {
        return this.arrowWidth;
    }

    @NonNull
    public BubbleLayout setArrowWidth(float arrowWidth2) {
        resetPadding();
        this.arrowWidth = arrowWidth2;
        initPadding();
        return this;
    }

    public float getArrowHeight() {
        return this.arrowHeight;
    }

    @NonNull
    public BubbleLayout setArrowHeight(float arrowHeight2) {
        resetPadding();
        this.arrowHeight = arrowHeight2;
        initPadding();
        return this;
    }

    public float getArrowPosition() {
        return this.arrowPosition;
    }

    @NonNull
    public BubbleLayout setArrowPosition(float arrowPosition2) {
        resetPadding();
        this.arrowPosition = arrowPosition2;
        initPadding();
        return this;
    }

    public float getCornersRadius() {
        return this.cornersRadius;
    }

    @NonNull
    public BubbleLayout setCornersRadius(float cornersRadius2) {
        this.cornersRadius = cornersRadius2;
        requestLayout();
        return this;
    }

    public int getBubbleColor() {
        return this.bubbleColor;
    }

    @NonNull
    public BubbleLayout setBubbleColor(int bubbleColor2) {
        this.bubbleColor = bubbleColor2;
        requestLayout();
        return this;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    @NonNull
    public BubbleLayout setStrokeWidth(float strokeWidth2) {
        resetPadding();
        this.strokeWidth = strokeWidth2;
        initPadding();
        return this;
    }

    public int getStrokeColor() {
        return this.strokeColor;
    }

    @NonNull
    public BubbleLayout setStrokeColor(int strokeColor2) {
        this.strokeColor = strokeColor2;
        requestLayout();
        return this;
    }

    private void initPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        switch (this.arrowDirection.getValue()) {
            case 0:
                paddingLeft = (int) (((float) paddingLeft) + this.arrowWidth);
                break;
            case 1:
                paddingRight = (int) (((float) paddingRight) + this.arrowWidth);
                break;
            case 2:
                paddingTop = (int) (((float) paddingTop) + this.arrowHeight);
                break;
            case 3:
                paddingBottom = (int) (((float) paddingBottom) + this.arrowHeight);
                break;
        }
        if (this.strokeWidth > 0.0f) {
            paddingLeft = (int) (((float) paddingLeft) + this.strokeWidth);
            paddingRight = (int) (((float) paddingRight) + this.strokeWidth);
            paddingTop = (int) (((float) paddingTop) + this.strokeWidth);
            paddingBottom = (int) (((float) paddingBottom) + this.strokeWidth);
        }
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    private void initDrawable(int left, int right, int top, int bottom) {
        if (right >= left && bottom >= top) {
            this.bubble = new Bubble(new RectF((float) left, (float) top, (float) right, (float) bottom), this.arrowDirection, this.arrowWidth, this.arrowHeight, this.arrowPosition, this.cornersRadius, this.bubbleColor, this.strokeWidth, this.strokeColor);
        }
    }

    private void resetPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        switch (this.arrowDirection.getValue()) {
            case 0:
                paddingLeft = (int) (((float) paddingLeft) - this.arrowWidth);
                break;
            case 1:
                paddingRight = (int) (((float) paddingRight) - this.arrowWidth);
                break;
            case 2:
                paddingTop = (int) (((float) paddingTop) - this.arrowHeight);
                break;
            case 3:
                paddingBottom = (int) (((float) paddingBottom) - this.arrowHeight);
                break;
        }
        if (this.strokeWidth > 0.0f) {
            paddingLeft = (int) (((float) paddingLeft) - this.strokeWidth);
            paddingRight = (int) (((float) paddingRight) - this.strokeWidth);
            paddingTop = (int) (((float) paddingTop) - this.strokeWidth);
            paddingBottom = (int) (((float) paddingBottom) - this.strokeWidth);
        }
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }
}
