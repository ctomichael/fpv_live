package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import antistatic.spinnerwheel.WheelScroller;
import dji.frame.widget.R;

public class WheelHorizontalView extends AbstractWheelView {
    private static int itemID = -1;
    private final String LOG_TAG;
    private int itemWidth;
    protected int mSelectionDividerWidth;

    public WheelHorizontalView(Context context) {
        this(context, null);
    }

    public WheelHorizontalView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.abstractWheelViewStyle);
    }

    public WheelHorizontalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        StringBuilder append = new StringBuilder().append(WheelVerticalView.class.getName()).append(" #");
        int i = itemID + 1;
        itemID = i;
        this.LOG_TAG = append.append(i).toString();
        this.itemWidth = 0;
    }

    /* access modifiers changed from: protected */
    public void initAttributes(AttributeSet attrs, int defStyle) {
        super.initAttributes(attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WheelHorizontalView, defStyle, 0);
        this.mSelectionDividerWidth = a.getDimensionPixelSize(R.styleable.WheelHorizontalView_selectionDividerWidth, 2);
        a.recycle();
    }

    public void setSelectionDividerWidth(int selectionDividerWidth) {
        this.mSelectionDividerWidth = selectionDividerWidth;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, int[], float[], android.graphics.Shader$TileMode):void}
     arg types: [int, int, float, int, int[], float[], android.graphics.Shader$TileMode]
     candidates:
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, long, long, android.graphics.Shader$TileMode):void}
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, long[], float[], android.graphics.Shader$TileMode):void}
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, int, int, android.graphics.Shader$TileMode):void}
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, int[], float[], android.graphics.Shader$TileMode):void} */
    public void setSelectorPaintCoeff(float coeff) {
        LinearGradient shader;
        if (this.mItemsDimmedAlpha < 100) {
            int w = getMeasuredWidth();
            int iw = getItemDimension();
            float p1 = (1.0f - (((float) iw) / ((float) w))) / 2.0f;
            float p2 = (1.0f + (((float) iw) / ((float) w))) / 2.0f;
            float z = ((float) this.mItemsDimmedAlpha) * (1.0f - coeff);
            float c1f = z + (255.0f * coeff);
            if (this.mVisibleItems == 2) {
                int c1 = Math.round(c1f) << 24;
                int c2 = Math.round(z) << 24;
                shader = new LinearGradient(0.0f, 0.0f, (float) w, 0.0f, new int[]{c2, c1, -16777216, -16777216, c1, c2}, new float[]{0.0f, p1, p1, p2, p2, 1.0f}, Shader.TileMode.CLAMP);
            } else {
                float p3 = (1.0f - (((float) (iw * 3)) / ((float) w))) / 2.0f;
                float p4 = (1.0f + (((float) (iw * 3)) / ((float) w))) / 2.0f;
                float c3f = ((255.0f * p3) / p1) * coeff;
                int c12 = Math.round(c1f) << 24;
                int c22 = Math.round(z + c3f) << 24;
                int c3 = Math.round(c3f) << 24;
                shader = new LinearGradient(0.0f, 0.0f, (float) w, 0.0f, new int[]{117440512, c3, c22, c12, -16777216, -16777216, c12, c22, c3, 117440512}, new float[]{0.0f, p3, p3, p1, p1, p2, p2, p4, p4, 1.0f}, Shader.TileMode.CLAMP);
            }
            this.mSelectorWheelPaint.setShader(shader);
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public WheelScroller createScroller(WheelScroller.ScrollingListener scrollingListener) {
        return new WheelHorizontalScroller(getContext(), scrollingListener);
    }

    /* access modifiers changed from: protected */
    public float getMotionEventPosition(MotionEvent event) {
        return event.getX();
    }

    /* access modifiers changed from: protected */
    public int getBaseDimension() {
        return getWidth();
    }

    /* access modifiers changed from: protected */
    public int getItemDimension() {
        if (this.itemWidth != 0) {
            return this.itemWidth;
        }
        if (this.mItemsLayout == null || this.mItemsLayout.getChildAt(0) == null) {
            return getBaseDimension() / this.mVisibleItems;
        }
        this.itemWidth = this.mItemsLayout.getChildAt(0).getMeasuredWidth();
        return this.itemWidth;
    }

    /* access modifiers changed from: protected */
    public void onScrollTouchedUp() {
        super.onScrollTouchedUp();
        int cnt = this.mItemsLayout.getChildCount();
        Log.e(this.LOG_TAG, " ----- layout: " + this.mItemsLayout.getMeasuredWidth() + this.mItemsLayout.getMeasuredHeight());
        Log.e(this.LOG_TAG, " -------- dumping " + cnt + " items");
        for (int i = 0; i < cnt; i++) {
            View itm = this.mItemsLayout.getChildAt(i);
            Log.e(this.LOG_TAG, " item #" + i + ": " + itm.getWidth() + "x" + itm.getHeight());
            itm.forceLayout();
        }
        Log.e(this.LOG_TAG, " ---------- dumping finished ");
    }

    /* access modifiers changed from: protected */
    public void createItemsLayout() {
        if (this.mItemsLayout == null) {
            this.mItemsLayout = new LinearLayout(getContext());
            this.mItemsLayout.setOrientation(0);
        }
    }

    /* access modifiers changed from: protected */
    public void doItemsLayout() {
        this.mItemsLayout.layout(0, 0, getMeasuredWidth(), getMeasuredHeight() - (this.mItemsPadding * 2));
    }

    /* access modifiers changed from: protected */
    public void measureLayout() {
        this.mItemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.mItemsLayout.measure(View.MeasureSpec.makeMeasureSpec(getWidth() + getItemDimension(), 0), View.MeasureSpec.makeMeasureSpec(getHeight(), Integer.MIN_VALUE));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        rebuildItems();
        int height = calculateLayoutHeight(heightSize, heightMode);
        if (widthMode == 1073741824) {
            width = widthSize;
        } else {
            width = Math.max(getItemDimension() * (this.mVisibleItems - (this.mItemOffsetPercent / 100)), getSuggestedMinimumWidth());
            if (widthMode == Integer.MIN_VALUE) {
                width = Math.min(width, widthSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    private int calculateLayoutHeight(int heightSize, int mode) {
        int height;
        this.mItemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.mItemsLayout.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(heightSize, 0));
        int height2 = this.mItemsLayout.getMeasuredHeight();
        if (mode == 1073741824) {
            height = heightSize;
        } else {
            height = Math.max(height2 + (this.mItemsPadding * 2), getSuggestedMinimumHeight());
            if (mode == Integer.MIN_VALUE && heightSize < height) {
                height = heightSize;
            }
        }
        this.mItemsLayout.measure(View.MeasureSpec.makeMeasureSpec(400, 1073741824), View.MeasureSpec.makeMeasureSpec(height - (this.mItemsPadding * 2), 1073741824));
        return height;
    }

    /* access modifiers changed from: protected */
    public void drawItems(Canvas canvas) {
        canvas.save();
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int iw = getItemDimension();
        this.mSpinBitmap.eraseColor(0);
        Canvas c = new Canvas(this.mSpinBitmap);
        Canvas cSpin = new Canvas(this.mSpinBitmap);
        c.translate((float) ((-(((this.mCurrentItemIdx - this.mFirstItemIdx) * iw) + ((iw - getWidth()) / 2))) + this.mScrollingOffset), (float) this.mItemsPadding);
        this.mItemsLayout.draw(c);
        this.mSeparatorsBitmap.eraseColor(0);
        Canvas cSeparators = new Canvas(this.mSeparatorsBitmap);
        if (this.mSelectionDivider != null) {
            int leftOfLeftDivider = ((getWidth() - iw) - this.mSelectionDividerWidth) / 2;
            int rightOfLeftDivider = leftOfLeftDivider + this.mSelectionDividerWidth;
            cSeparators.save();
            cSeparators.clipRect(leftOfLeftDivider, 0, rightOfLeftDivider, h);
            this.mSelectionDivider.setBounds(leftOfLeftDivider, 0, rightOfLeftDivider, h);
            this.mSelectionDivider.draw(cSeparators);
            cSeparators.restore();
            cSeparators.save();
            int leftOfRightDivider = leftOfLeftDivider + iw;
            int rightOfRightDivider = rightOfLeftDivider + iw;
            cSeparators.clipRect(leftOfRightDivider, 0, rightOfRightDivider, h);
            this.mSelectionDivider.setBounds(leftOfRightDivider, 0, rightOfRightDivider, h);
            this.mSelectionDivider.draw(cSeparators);
            cSeparators.restore();
        }
        cSpin.drawRect(0.0f, 0.0f, (float) w, (float) h, this.mSelectorWheelPaint);
        cSeparators.drawRect(0.0f, 0.0f, (float) w, (float) h, this.mSeparatorsPaint);
        canvas.drawBitmap(this.mSpinBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(this.mSeparatorsBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.restore();
    }
}
