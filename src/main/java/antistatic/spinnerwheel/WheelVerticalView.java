package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import antistatic.spinnerwheel.WheelScroller;
import dji.frame.widget.R;

public class WheelVerticalView extends AbstractWheelView {
    private static int itemID = -1;
    private final String LOG_TAG;
    private int mItemHeight;
    protected int mSelectionDividerHeight;

    public WheelVerticalView(Context context) {
        this(context, null);
    }

    public WheelVerticalView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.abstractWheelViewStyle);
    }

    public WheelVerticalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        StringBuilder append = new StringBuilder().append(WheelVerticalView.class.getName()).append(" #");
        int i = itemID + 1;
        itemID = i;
        this.LOG_TAG = append.append(i).toString();
        this.mItemHeight = 0;
    }

    /* access modifiers changed from: protected */
    public void initAttributes(AttributeSet attrs, int defStyle) {
        super.initAttributes(attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WheelVerticalView, defStyle, 0);
        this.mSelectionDividerHeight = a.getDimensionPixelSize(R.styleable.WheelVerticalView_selectionDividerHeight, 2);
        a.recycle();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, int[], float[], android.graphics.Shader$TileMode):void}
     arg types: [int, int, int, float, int[], float[], android.graphics.Shader$TileMode]
     candidates:
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, long, long, android.graphics.Shader$TileMode):void}
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, long[], float[], android.graphics.Shader$TileMode):void}
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, int, int, android.graphics.Shader$TileMode):void}
      ClspMth{android.graphics.LinearGradient.<init>(float, float, float, float, int[], float[], android.graphics.Shader$TileMode):void} */
    public void setSelectorPaintCoeff(float coeff) {
        LinearGradient shader;
        int h = getMeasuredHeight();
        int ih = getItemDimension();
        float p1 = (1.0f - (((float) ih) / ((float) h))) / 2.0f;
        float p2 = (1.0f + (((float) ih) / ((float) h))) / 2.0f;
        float z = ((float) this.mItemsDimmedAlpha) * (1.0f - coeff);
        float c1f = z + (255.0f * coeff);
        if (this.mVisibleItems == 2) {
            int c1 = Math.round(c1f) << 24;
            int c2 = Math.round(z) << 24;
            shader = new LinearGradient(0.0f, 0.0f, 0.0f, (float) h, new int[]{c2, c1, -16777216, -16777216, c1, c2}, new float[]{0.0f, p1, p1, p2, p2, 1.0f}, Shader.TileMode.CLAMP);
        } else {
            float p3 = (1.0f - (((float) (ih * 3)) / ((float) h))) / 2.0f;
            float p4 = (1.0f + (((float) (ih * 3)) / ((float) h))) / 2.0f;
            float c3f = ((255.0f * p3) / p1) * coeff;
            int c12 = Math.round(c1f) << 24;
            int c22 = Math.round(z + c3f) << 24;
            int c3 = Math.round(c3f) << 24;
            shader = new LinearGradient(0.0f, 0.0f, 0.0f, (float) h, new int[]{0, c3, c22, c12, -16777216, -16777216, c12, c22, c3, 0}, new float[]{0.0f, p3, p3, p1, p1, p2, p2, p4, p4, 1.0f}, Shader.TileMode.CLAMP);
        }
        this.mSelectorWheelPaint.setShader(shader);
        invalidate();
    }

    /* access modifiers changed from: protected */
    public WheelScroller createScroller(WheelScroller.ScrollingListener scrollingListener) {
        return new WheelVerticalScroller(getContext(), scrollingListener);
    }

    /* access modifiers changed from: protected */
    public float getMotionEventPosition(MotionEvent event) {
        return event.getY();
    }

    /* access modifiers changed from: protected */
    public int getBaseDimension() {
        return getHeight();
    }

    /* access modifiers changed from: protected */
    public int getItemDimension() {
        if (this.mItemHeight != 0) {
            return this.mItemHeight;
        }
        if (this.mItemsLayout == null || this.mItemsLayout.getChildAt(0) == null) {
            return getBaseDimension() / this.mVisibleItems;
        }
        this.mItemHeight = this.mItemsLayout.getChildAt(0).getMeasuredHeight();
        return this.mItemHeight;
    }

    /* access modifiers changed from: protected */
    public void createItemsLayout() {
        if (this.mItemsLayout == null) {
            this.mItemsLayout = new LinearLayout(getContext());
            this.mItemsLayout.setOrientation(1);
        }
    }

    /* access modifiers changed from: protected */
    public void doItemsLayout() {
        this.mItemsLayout.layout(0, 0, getMeasuredWidth() - (this.mItemsPadding * 2), getMeasuredHeight());
    }

    /* access modifiers changed from: protected */
    public void measureLayout() {
        this.mItemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.mItemsLayout.measure(View.MeasureSpec.makeMeasureSpec(getWidth() - (this.mItemsPadding * 2), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        rebuildItems();
        int width = calculateLayoutWidth(widthSize, widthMode);
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = Math.max(getItemDimension() * (this.mVisibleItems - (this.mItemOffsetPercent / 100)), getSuggestedMinimumHeight());
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(height, heightSize);
            }
        }
        setMeasuredDimension(width, height);
    }

    private int calculateLayoutWidth(int widthSize, int mode) {
        int width;
        this.mItemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        this.mItemsLayout.measure(View.MeasureSpec.makeMeasureSpec(widthSize, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        int width2 = this.mItemsLayout.getMeasuredWidth();
        if (mode == 1073741824) {
            width = widthSize;
        } else {
            width = Math.max(width2 + (this.mItemsPadding * 2), getSuggestedMinimumWidth());
            if (mode == Integer.MIN_VALUE && widthSize < width) {
                width = widthSize;
            }
        }
        this.mItemsLayout.measure(View.MeasureSpec.makeMeasureSpec(width - (this.mItemsPadding * 2), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        return width;
    }

    /* access modifiers changed from: protected */
    public void drawItems(Canvas canvas) {
        canvas.save();
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int ih = getItemDimension();
        this.mSpinBitmap.eraseColor(0);
        Canvas c = new Canvas(this.mSpinBitmap);
        Canvas cSpin = new Canvas(this.mSpinBitmap);
        c.translate((float) this.mItemsPadding, (float) ((-(((this.mCurrentItemIdx - this.mFirstItemIdx) * ih) + ((ih - getHeight()) / 2))) + this.mScrollingOffset));
        this.mItemsLayout.draw(c);
        this.mSeparatorsBitmap.eraseColor(0);
        Canvas cSeparators = new Canvas(this.mSeparatorsBitmap);
        if (this.mSelectionDivider != null) {
            int topOfTopDivider = ((getHeight() - ih) - this.mSelectionDividerHeight) / 2;
            int bottomOfTopDivider = topOfTopDivider + this.mSelectionDividerHeight;
            this.mSelectionDivider.setBounds(0, topOfTopDivider, w, bottomOfTopDivider);
            this.mSelectionDivider.draw(cSeparators);
            this.mSelectionDivider.setBounds(0, topOfTopDivider + ih, w, bottomOfTopDivider + ih);
            this.mSelectionDivider.draw(cSeparators);
        }
        cSpin.drawRect(0.0f, 0.0f, (float) w, (float) h, this.mSelectorWheelPaint);
        cSeparators.drawRect(0.0f, 0.0f, (float) w, (float) h, this.mSeparatorsPaint);
        canvas.drawBitmap(this.mSpinBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(this.mSeparatorsBitmap, 0.0f, 0.0f, (Paint) null);
        canvas.restore();
    }
}
