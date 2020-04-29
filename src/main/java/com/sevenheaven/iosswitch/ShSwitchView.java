package com.sevenheaven.iosswitch;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;

public class ShSwitchView extends View {
    private static final int backgroundColor = -3355444;
    private static final long commonDuration = 300;
    private static final int foregroundColor = -657931;
    private static final int intrinsicHeight = 0;
    private static final int intrinsicWidth = 0;
    /* access modifiers changed from: private */
    public int centerX;
    private int centerY;
    private int colorStep;
    private float cornerRadius;
    private boolean dirtyAnimation;
    private GestureDetector gestureDetector;
    private GestureDetector.SimpleOnGestureListener gestureListener;
    private int height;
    /* access modifiers changed from: private */
    public ValueAnimator innerContentAnimator;
    private RectF innerContentBound;
    /* access modifiers changed from: private */
    public float innerContentRate;
    private float intrinsicInnerHeight;
    private float intrinsicInnerWidth;
    private float intrinsicKnobWidth;
    private boolean isAttachedToWindow;
    /* access modifiers changed from: private */
    public boolean isOn;
    private RectF knobBound;
    /* access modifiers changed from: private */
    public ValueAnimator knobExpandAnimator;
    /* access modifiers changed from: private */
    public float knobExpandRate;
    private float knobMaxExpandWidth;
    /* access modifiers changed from: private */
    public ValueAnimator knobMoveAnimator;
    /* access modifiers changed from: private */
    public float knobMoveRate;
    /* access modifiers changed from: private */
    public boolean knobState;
    /* access modifiers changed from: private */
    public OnSwitchStateChangeListener onSwitchStateChangeListener;
    private int outerStrokeWidth;
    private RectF ovalForPath;
    private Paint paint;
    /* access modifiers changed from: private */
    public boolean preIsOn;
    private Path roundRectPath;
    private Drawable shadowDrawable;
    private int shadowSpace;
    private RectF tempForRoundRect;
    private int tempTintColor;
    private int tintColor;
    private int width;

    public interface OnSwitchStateChangeListener {
        void onSwitchStateChange(boolean z);
    }

    public ShSwitchView(Context context) {
        this(context, null);
    }

    public ShSwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShSwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.gestureListener = new GestureDetector.SimpleOnGestureListener() {
            /* class com.sevenheaven.iosswitch.ShSwitchView.AnonymousClass1 */

            public boolean onDown(MotionEvent event) {
                if (!ShSwitchView.this.isEnabled()) {
                    return false;
                }
                boolean unused = ShSwitchView.this.preIsOn = ShSwitchView.this.isOn;
                ShSwitchView.this.innerContentAnimator.setFloatValues(ShSwitchView.this.innerContentRate, 0.0f);
                ShSwitchView.this.innerContentAnimator.start();
                ShSwitchView.this.knobExpandAnimator.setFloatValues(ShSwitchView.this.knobExpandRate, 1.0f);
                ShSwitchView.this.knobExpandAnimator.start();
                return true;
            }

            public void onShowPress(MotionEvent event) {
            }

            public boolean onSingleTapUp(MotionEvent event) {
                boolean z;
                boolean unused = ShSwitchView.this.isOn = ShSwitchView.this.knobState;
                if (ShSwitchView.this.preIsOn == ShSwitchView.this.isOn) {
                    boolean unused2 = ShSwitchView.this.isOn = !ShSwitchView.this.isOn;
                    ShSwitchView shSwitchView = ShSwitchView.this;
                    if (!ShSwitchView.this.knobState) {
                        z = true;
                    } else {
                        z = false;
                    }
                    boolean unused3 = shSwitchView.knobState = z;
                }
                if (ShSwitchView.this.knobState) {
                    ShSwitchView.this.knobMoveAnimator.setFloatValues(ShSwitchView.this.knobMoveRate, 1.0f);
                    ShSwitchView.this.knobMoveAnimator.start();
                    ShSwitchView.this.innerContentAnimator.setFloatValues(ShSwitchView.this.innerContentRate, 0.0f);
                    ShSwitchView.this.innerContentAnimator.start();
                } else {
                    ShSwitchView.this.knobMoveAnimator.setFloatValues(ShSwitchView.this.knobMoveRate, 0.0f);
                    ShSwitchView.this.knobMoveAnimator.start();
                    ShSwitchView.this.innerContentAnimator.setFloatValues(ShSwitchView.this.innerContentRate, 1.0f);
                    ShSwitchView.this.innerContentAnimator.start();
                }
                ShSwitchView.this.knobExpandAnimator.setFloatValues(ShSwitchView.this.knobExpandRate, 0.0f);
                ShSwitchView.this.knobExpandAnimator.start();
                if (!(ShSwitchView.this.onSwitchStateChangeListener == null || ShSwitchView.this.isOn == ShSwitchView.this.preIsOn)) {
                    ShSwitchView.this.onSwitchStateChangeListener.onSwitchStateChange(ShSwitchView.this.isOn);
                }
                return true;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                boolean z;
                if (e2.getX() > ((float) ShSwitchView.this.centerX)) {
                    if (!ShSwitchView.this.knobState) {
                        boolean unused = ShSwitchView.this.knobState = !ShSwitchView.this.knobState;
                        ShSwitchView.this.knobMoveAnimator.setFloatValues(ShSwitchView.this.knobMoveRate, 1.0f);
                        ShSwitchView.this.knobMoveAnimator.start();
                        ShSwitchView.this.innerContentAnimator.setFloatValues(ShSwitchView.this.innerContentRate, 0.0f);
                        ShSwitchView.this.innerContentAnimator.start();
                    }
                } else if (ShSwitchView.this.knobState) {
                    ShSwitchView shSwitchView = ShSwitchView.this;
                    if (!ShSwitchView.this.knobState) {
                        z = true;
                    } else {
                        z = false;
                    }
                    boolean unused2 = shSwitchView.knobState = z;
                    ShSwitchView.this.knobMoveAnimator.setFloatValues(ShSwitchView.this.knobMoveRate, 0.0f);
                    ShSwitchView.this.knobMoveAnimator.start();
                }
                return true;
            }
        };
        this.innerContentRate = 1.0f;
        this.colorStep = backgroundColor;
        this.dirtyAnimation = false;
        this.isAttachedToWindow = false;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShSwitchView);
        this.tintColor = ta.getColor(R.styleable.ShSwitchView_tintColor, -6493879);
        this.tempTintColor = this.tintColor;
        int defaultOuterStrokeWidth = (int) TypedValue.applyDimension(1, 1.5f, context.getResources().getDisplayMetrics());
        int defaultShadowSpace = (int) TypedValue.applyDimension(1, 5.0f, context.getResources().getDisplayMetrics());
        this.outerStrokeWidth = ta.getDimensionPixelOffset(R.styleable.ShSwitchView_outerStrokeWidth, defaultOuterStrokeWidth);
        this.shadowSpace = ta.getDimensionPixelOffset(R.styleable.ShSwitchView_shadowSpace, defaultShadowSpace);
        ta.recycle();
        this.knobBound = new RectF();
        this.innerContentBound = new RectF();
        this.ovalForPath = new RectF();
        this.tempForRoundRect = new RectF();
        this.paint = new Paint(1);
        this.roundRectPath = new Path();
        this.gestureDetector = new GestureDetector(context, this.gestureListener);
        this.gestureDetector.setIsLongpressEnabled(false);
        if (Build.VERSION.SDK_INT >= 11) {
            setLayerType(1, null);
        }
        initAnimators();
        this.shadowDrawable = context.getResources().getDrawable(R.drawable.shadow);
    }

    private void initAnimators() {
        this.innerContentAnimator = ValueAnimator.ofFloat(this.innerContentRate, 1.0f);
        this.knobExpandAnimator = ValueAnimator.ofFloat(this.knobExpandRate, 1.0f);
        this.knobMoveAnimator = ValueAnimator.ofFloat(this.knobMoveRate, 1.0f);
        this.innerContentAnimator.setDuration((long) commonDuration);
        this.knobExpandAnimator.setDuration((long) commonDuration);
        this.knobMoveAnimator.setDuration((long) commonDuration);
        this.innerContentAnimator.setInterpolator(new DecelerateInterpolator());
        this.knobExpandAnimator.setInterpolator(new DecelerateInterpolator());
        this.knobMoveAnimator.setInterpolator(new DecelerateInterpolator());
        this.innerContentAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /* class com.sevenheaven.iosswitch.ShSwitchView.AnonymousClass2 */

            public void onAnimationUpdate(ValueAnimator animation) {
                ShSwitchView.this.setInnerContentRate(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
        this.knobExpandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /* class com.sevenheaven.iosswitch.ShSwitchView.AnonymousClass3 */

            public void onAnimationUpdate(ValueAnimator animation) {
                ShSwitchView.this.setKnobExpandRate(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
        this.knobMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            /* class com.sevenheaven.iosswitch.ShSwitchView.AnonymousClass4 */

            public void onAnimationUpdate(ValueAnimator animation) {
                ShSwitchView.this.setKnobMoveRate(((Float) animation.getAnimatedValue()).floatValue());
            }
        });
    }

    public void setOnSwitchStateChangeListener(OnSwitchStateChangeListener onSwitchStateChangeListener2) {
        this.onSwitchStateChangeListener = onSwitchStateChangeListener2;
    }

    public OnSwitchStateChangeListener getOnSwitchStateChangeListener() {
        return this.onSwitchStateChangeListener;
    }

    /* access modifiers changed from: package-private */
    public void setInnerContentRate(float rate) {
        this.innerContentRate = rate;
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public float getInnerContentRate() {
        return this.innerContentRate;
    }

    /* access modifiers changed from: package-private */
    public void setKnobExpandRate(float rate) {
        this.knobExpandRate = rate;
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public float getKnobExpandRate() {
        return this.knobExpandRate;
    }

    /* access modifiers changed from: package-private */
    public void setKnobMoveRate(float rate) {
        this.knobMoveRate = rate;
        invalidate();
    }

    /* access modifiers changed from: package-private */
    public float getKnobMoveRate() {
        return this.knobMoveRate;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.isAttachedToWindow = true;
        if (this.dirtyAnimation) {
            this.knobState = this.isOn;
            if (this.knobState) {
                this.knobMoveAnimator.setFloatValues(this.knobMoveRate, 1.0f);
                this.knobMoveAnimator.start();
                this.innerContentAnimator.setFloatValues(this.innerContentRate, 0.0f);
                this.innerContentAnimator.start();
            } else {
                this.knobMoveAnimator.setFloatValues(this.knobMoveRate, 0.0f);
                this.knobMoveAnimator.start();
                this.innerContentAnimator.setFloatValues(this.innerContentRate, 1.0f);
                this.innerContentAnimator.start();
            }
            this.knobExpandAnimator.setFloatValues(this.knobExpandRate, 0.0f);
            this.knobExpandAnimator.start();
            if (!(this.onSwitchStateChangeListener == null || this.isOn == this.preIsOn)) {
                this.onSwitchStateChangeListener.onSwitchStateChange(this.isOn);
            }
            this.dirtyAnimation = false;
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isAttachedToWindow = false;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.width = View.MeasureSpec.getSize(widthMeasureSpec);
        this.height = View.MeasureSpec.getSize(heightMeasureSpec);
        if (((float) this.height) / ((float) this.width) < 0.33333f) {
            this.height = (int) (((float) this.width) * 0.33333f);
            super.setMeasuredDimension(View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.getMode(widthMeasureSpec)), View.MeasureSpec.makeMeasureSpec(this.height, View.MeasureSpec.getMode(heightMeasureSpec)));
        }
        this.centerX = this.width / 2;
        this.centerY = this.height / 2;
        this.cornerRadius = (float) (this.centerY - this.shadowSpace);
        this.innerContentBound.left = (float) (this.outerStrokeWidth + this.shadowSpace);
        this.innerContentBound.top = (float) (this.outerStrokeWidth + this.shadowSpace);
        this.innerContentBound.right = (float) ((this.width - this.outerStrokeWidth) - this.shadowSpace);
        this.innerContentBound.bottom = (float) ((this.height - this.outerStrokeWidth) - this.shadowSpace);
        this.intrinsicInnerWidth = this.innerContentBound.width();
        this.intrinsicInnerHeight = this.innerContentBound.height();
        this.knobBound.left = (float) (this.outerStrokeWidth + this.shadowSpace);
        this.knobBound.top = (float) (this.outerStrokeWidth + this.shadowSpace);
        this.knobBound.right = (float) ((this.height - this.outerStrokeWidth) - this.shadowSpace);
        this.knobBound.bottom = (float) ((this.height - this.outerStrokeWidth) - this.shadowSpace);
        this.intrinsicKnobWidth = this.knobBound.height();
        this.knobMaxExpandWidth = ((float) this.width) * 0.7f;
        if (this.knobMaxExpandWidth > this.knobBound.width() * 1.25f) {
            this.knobMaxExpandWidth = this.knobBound.width() * 1.25f;
        }
    }

    public boolean isOn() {
        return this.isOn;
    }

    public void setOn(boolean on) {
        setOn(on, false);
    }

    public void setOn(boolean on, boolean animated) {
        if (this.isOn != on) {
            if (this.isAttachedToWindow || !animated) {
                this.isOn = on;
                this.knobState = this.isOn;
                if (!animated) {
                    if (on) {
                        setKnobMoveRate(1.0f);
                        setInnerContentRate(0.0f);
                    } else {
                        setKnobMoveRate(0.0f);
                        setInnerContentRate(1.0f);
                    }
                    setKnobExpandRate(0.0f);
                } else {
                    if (this.knobState) {
                        this.knobMoveAnimator.setFloatValues(this.knobMoveRate, 1.0f);
                        this.knobMoveAnimator.start();
                        this.innerContentAnimator.setFloatValues(this.innerContentRate, 0.0f);
                        this.innerContentAnimator.start();
                    } else {
                        this.knobMoveAnimator.setFloatValues(this.knobMoveRate, 0.0f);
                        this.knobMoveAnimator.start();
                        this.innerContentAnimator.setFloatValues(this.innerContentRate, 1.0f);
                        this.innerContentAnimator.start();
                    }
                    this.knobExpandAnimator.setFloatValues(this.knobExpandRate, 0.0f);
                    this.knobExpandAnimator.start();
                }
                if (this.onSwitchStateChangeListener != null && this.isOn != this.preIsOn) {
                    this.onSwitchStateChangeListener.onSwitchStateChange(this.isOn);
                    return;
                }
                return;
            }
            this.dirtyAnimation = true;
            this.isOn = on;
        }
    }

    public void setTintColor(int tintColor2) {
        this.tintColor = tintColor2;
        this.tempTintColor = this.tintColor;
    }

    public int getTintColor() {
        return this.tintColor;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 1:
            case 3:
                if (!this.knobState) {
                    this.innerContentAnimator.setFloatValues(this.innerContentRate, 1.0f);
                    this.innerContentAnimator.start();
                }
                this.knobExpandAnimator.setFloatValues(this.knobExpandRate, 0.0f);
                this.knobExpandAnimator.start();
                this.isOn = this.knobState;
                if (!(this.onSwitchStateChangeListener == null || this.isOn == this.preIsOn)) {
                    this.onSwitchStateChangeListener.onSwitchStateChange(this.isOn);
                    break;
                }
        }
        return this.gestureDetector.onTouchEvent(event);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            this.tintColor = this.tempTintColor;
        } else {
            this.tintColor = RGBColorTransform(0.5f, this.tempTintColor, -1);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, int):void}
     arg types: [int, int, float, int]
     candidates:
      ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, long):void}
      ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, int):void} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, int):void}
     arg types: [int, int, int, int]
     candidates:
      ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, long):void}
      ClspMth{android.graphics.Paint.setShadowLayer(float, float, float, int):void} */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = (this.intrinsicInnerWidth / 2.0f) * this.innerContentRate;
        float h = (this.intrinsicInnerHeight / 2.0f) * this.innerContentRate;
        this.innerContentBound.left = ((float) this.centerX) - w;
        this.innerContentBound.top = ((float) this.centerY) - h;
        this.innerContentBound.right = ((float) this.centerX) + w;
        this.innerContentBound.bottom = ((float) this.centerY) + h;
        float w2 = this.intrinsicKnobWidth + ((this.knobMaxExpandWidth - this.intrinsicKnobWidth) * this.knobExpandRate);
        if (this.knobBound.left + (this.knobBound.width() / 2.0f) > ((float) this.centerX)) {
            this.knobBound.left = this.knobBound.right - w2;
        } else {
            this.knobBound.right = this.knobBound.left + w2;
        }
        float kw = this.knobBound.width();
        float w3 = ((((float) this.width) - kw) - ((float) ((this.shadowSpace + this.outerStrokeWidth) * 2))) * this.knobMoveRate;
        this.colorStep = RGBColorTransform(this.knobMoveRate, backgroundColor, this.tintColor);
        this.knobBound.left = ((float) (this.shadowSpace + this.outerStrokeWidth)) + w3;
        this.knobBound.right = this.knobBound.left + kw;
        this.paint.setColor(this.colorStep);
        this.paint.setStyle(Paint.Style.FILL);
        drawRoundRect((float) this.shadowSpace, (float) this.shadowSpace, (float) (this.width - this.shadowSpace), (float) (this.height - this.shadowSpace), this.cornerRadius, canvas, this.paint);
        this.paint.setColor((int) foregroundColor);
        canvas.drawRoundRect(this.innerContentBound, this.innerContentBound.height() / 2.0f, this.innerContentBound.height() / 2.0f, this.paint);
        this.paint.setShadowLayer(2.0f, 0.0f, (float) (this.shadowSpace / 2), isEnabled() ? 536870912 : SQLiteDatabase.CREATE_IF_NECESSARY);
        canvas.drawRoundRect(this.knobBound, this.cornerRadius - ((float) this.outerStrokeWidth), this.cornerRadius - ((float) this.outerStrokeWidth), this.paint);
        this.paint.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
        this.paint.setColor((int) backgroundColor);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(1.0f);
        canvas.drawRoundRect(this.knobBound, this.cornerRadius - ((float) this.outerStrokeWidth), this.cornerRadius - ((float) this.outerStrokeWidth), this.paint);
    }

    private void drawRoundRect(float left, float top, float right, float bottom, float radius, Canvas canvas, Paint paint2) {
        this.tempForRoundRect.left = left;
        this.tempForRoundRect.top = top;
        this.tempForRoundRect.right = right;
        this.tempForRoundRect.bottom = bottom;
        canvas.drawRoundRect(this.tempForRoundRect, radius, radius, paint2);
    }

    private int RGBColorTransform(float progress, int fromColor, int toColor) {
        int or = (fromColor >> 16) & 255;
        int og = (fromColor >> 8) & 255;
        int ob = fromColor & 255;
        return -16777216 | ((or + ((int) (((float) (((toColor >> 16) & 255) - or)) * progress))) << 16) | ((og + ((int) (((float) (((toColor >> 8) & 255) - og)) * progress))) << 8) | (ob + ((int) (((float) ((toColor & 255) - ob)) * progress)));
    }
}
