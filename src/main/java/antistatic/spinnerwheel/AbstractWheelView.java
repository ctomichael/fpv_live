package antistatic.spinnerwheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import dji.frame.widget.R;

public abstract class AbstractWheelView extends AbstractWheel {
    protected static final int DEF_ITEMS_DIMMED_ALPHA = 50;
    protected static final int DEF_ITEM_OFFSET_PERCENT = 10;
    protected static final int DEF_ITEM_PADDING = 10;
    protected static final int DEF_SELECTION_DIVIDER_ACTIVE_ALPHA = 70;
    protected static final int DEF_SELECTION_DIVIDER_DIMMED_ALPHA = 70;
    protected static final int DEF_SELECTION_DIVIDER_SIZE = 2;
    protected static final String PROPERTY_SELECTOR_PAINT_COEFF = "selectorPaintCoeff";
    protected static final String PROPERTY_SEPARATORS_PAINT_ALPHA = "separatorsPaintAlpha";
    private static int itemID = -1;
    private final String LOG_TAG;
    protected Animator mDimSelectorWheelAnimator;
    protected Animator mDimSeparatorsAnimator;
    protected int mItemOffsetPercent;
    protected int mItemsDimmedAlpha;
    protected int mItemsPadding;
    protected Drawable mSelectionDivider;
    protected int mSelectionDividerActiveAlpha;
    protected int mSelectionDividerDimmedAlpha;
    protected Paint mSelectorWheelPaint;
    protected Bitmap mSeparatorsBitmap;
    protected Paint mSeparatorsPaint;
    protected Bitmap mSpinBitmap;

    /* access modifiers changed from: protected */
    public abstract void drawItems(Canvas canvas);

    /* access modifiers changed from: protected */
    public abstract void measureLayout();

    public abstract void setSelectorPaintCoeff(float f);

    public AbstractWheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        StringBuilder append = new StringBuilder().append(AbstractWheelView.class.getName()).append(" #");
        int i = itemID + 1;
        itemID = i;
        this.LOG_TAG = append.append(i).toString();
    }

    /* access modifiers changed from: protected */
    public void initAttributes(AttributeSet attrs, int defStyle) {
        super.initAttributes(attrs, defStyle);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AbstractWheelView, defStyle, 0);
        this.mItemsDimmedAlpha = a.getInt(R.styleable.AbstractWheelView_itemsDimmedAlpha, 50);
        this.mSelectionDividerActiveAlpha = a.getInt(R.styleable.AbstractWheelView_selectionDividerActiveAlpha, 70);
        this.mSelectionDividerDimmedAlpha = a.getInt(R.styleable.AbstractWheelView_selectionDividerDimmedAlpha, 70);
        this.mItemOffsetPercent = a.getInt(R.styleable.AbstractWheelView_itemOffsetPercent, 10);
        this.mItemsPadding = a.getDimensionPixelSize(R.styleable.AbstractWheelView_itemsPadding, 10);
        this.mSelectionDivider = a.getDrawable(R.styleable.AbstractWheelView_selectionDivider);
        a.recycle();
    }

    /* access modifiers changed from: protected */
    public void initData(Context context) {
        super.initData(context);
        this.mDimSelectorWheelAnimator = ObjectAnimator.ofFloat(this, PROPERTY_SELECTOR_PAINT_COEFF, 1.0f, 0.5f);
        this.mDimSeparatorsAnimator = ObjectAnimator.ofInt(this, PROPERTY_SEPARATORS_PAINT_ALPHA, this.mSelectionDividerActiveAlpha, this.mSelectionDividerDimmedAlpha);
        this.mSeparatorsPaint = new Paint();
        this.mSeparatorsPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        this.mSeparatorsPaint.setAlpha(this.mSelectionDividerDimmedAlpha);
        this.mSelectorWheelPaint = new Paint();
        this.mSelectorWheelPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    /* access modifiers changed from: protected */
    public void recreateAssets(int width, int height) {
        this.mSpinBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.mSeparatorsBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        setSelectorPaintCoeff(0.4f);
    }

    public void setSeparatorsPaintAlpha(int alpha) {
        this.mSeparatorsPaint.setAlpha(alpha);
        invalidate();
    }

    public void setSelectionDivider(Drawable selectionDivider) {
        this.mSelectionDivider = selectionDivider;
    }

    /* access modifiers changed from: protected */
    public void onScrollTouched() {
        this.mDimSelectorWheelAnimator.cancel();
        this.mDimSeparatorsAnimator.cancel();
        setSelectorPaintCoeff(1.0f);
        setSeparatorsPaintAlpha(this.mSelectionDividerActiveAlpha);
    }

    /* access modifiers changed from: protected */
    public void onScrollTouchedUp() {
        super.onScrollTouchedUp();
        fadeSelectorWheel(750);
        lightSeparators(750);
    }

    /* access modifiers changed from: protected */
    public void onScrollFinished() {
        fadeSelectorWheel(500);
        lightSeparators(500);
    }

    private void fadeSelectorWheel(long animationDuration) {
        this.mDimSelectorWheelAnimator.setDuration(animationDuration);
        this.mDimSelectorWheelAnimator.start();
    }

    private void lightSeparators(long animationDuration) {
        this.mDimSeparatorsAnimator.setDuration(animationDuration);
        this.mDimSeparatorsAnimator.start();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mViewAdapter != null && this.mViewAdapter.getItemsCount() > 0) {
            if (rebuildItems()) {
                measureLayout();
            }
            doItemsLayout();
            drawItems(canvas);
        }
    }
}
