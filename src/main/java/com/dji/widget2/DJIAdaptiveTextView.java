package com.dji.widget2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;
import dji.publics.DJIUI.DJITextView;

public class DJIAdaptiveTextView extends DJITextView {
    private static final float OFFSET = 1.0f;
    private float mDefaultTextSize;
    private int mMaxLine;
    private int mMaxTextSize;
    private int mMinTextSize;
    private boolean mScaleDown = false;
    private boolean mScaleUp = false;
    private boolean mSetTextSizeWhenAdaptive = false;
    private TextPaint mTextPaint = new TextPaint();

    public DJIAdaptiveTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DJIAdaptiveTextView);
        this.mScaleDown = ta.getBoolean(R.styleable.DJIAdaptiveTextView_scale_down, true);
        this.mScaleUp = ta.getBoolean(R.styleable.DJIAdaptiveTextView_scale_up, false);
        this.mMaxTextSize = ta.getDimensionPixelSize(R.styleable.DJIAdaptiveTextView_maxTextSize, Integer.MAX_VALUE);
        this.mMinTextSize = ta.getDimensionPixelSize(R.styleable.DJIAdaptiveTextView_minTextSize, 0);
        this.mMaxLine = ta.getInteger(R.styleable.DJIAdaptiveTextView_maxLine, Integer.MAX_VALUE);
        this.mDefaultTextSize = getTextSize();
        ta.recycle();
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    public void setText(CharSequence text, TextView.BufferType type) {
        if (this.mDefaultTextSize > 0.0f) {
            setTextSizeWhenAdaptive(this.mDefaultTextSize);
        }
        super.setText(text, type);
    }

    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        if (this.mSetTextSizeWhenAdaptive) {
            this.mSetTextSizeWhenAdaptive = false;
        } else {
            this.mDefaultTextSize = getTextSize();
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        adaptiveTextSize();
        super.onDraw(canvas);
    }

    private void setTextSizeWhenAdaptive(float size) {
        this.mSetTextSizeWhenAdaptive = true;
        setTextSize(0, size);
    }

    /* access modifiers changed from: protected */
    public void adaptiveTextSize() {
        if (this.mScaleUp || this.mScaleDown) {
            int width = (getWidth() - getPaddingLeft()) - getPaddingRight();
            int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
            int textAlignment = getTextAlignment();
            float spacingMulti = getLineSpacingMultiplier();
            float spacingAdd = getLineSpacingExtra();
            if (this.mTextPaint != null && width > 0 && height > 0) {
                String text = getText().toString();
                float textSize = getTextSize();
                this.mTextPaint.setTextSize(textSize);
                StaticLayout layout = getStaticLayout(text, this.mTextPaint, width, textAlignment, spacingMulti, spacingAdd);
                if (this.mScaleDown && ((layout.getHeight() > height || layout.getLineCount() > this.mMaxLine) && textSize > ((float) this.mMinTextSize))) {
                    while (true) {
                        textSize -= 1.0f;
                        if (textSize >= ((float) this.mMinTextSize)) {
                            this.mTextPaint.setTextSize(textSize);
                            StaticLayout layout2 = getStaticLayout(text, this.mTextPaint, width, textAlignment, spacingMulti, spacingAdd);
                            if (layout2.getHeight() <= height && layout2.getLineCount() <= this.mMaxLine) {
                                break;
                            }
                        } else {
                            textSize = (float) this.mMinTextSize;
                            break;
                        }
                    }
                } else if (this.mScaleUp && layout.getHeight() < height && layout.getLineCount() < this.mMaxLine && textSize < ((float) this.mMaxTextSize)) {
                    while (true) {
                        textSize += 1.0f;
                        if (textSize <= ((float) this.mMaxTextSize)) {
                            this.mTextPaint.setTextSize(textSize);
                            layout = getStaticLayout(text, this.mTextPaint, width, textAlignment, spacingMulti, spacingAdd);
                            if (layout.getHeight() < height) {
                                if (layout.getLineCount() >= this.mMaxLine) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            textSize = (float) this.mMaxTextSize;
                            break;
                        }
                    }
                    if (layout.getHeight() > height || layout.getLineCount() > this.mMaxLine) {
                        textSize -= 1.0f;
                    }
                }
                setTextSizeWhenAdaptive(textSize);
            }
        }
    }

    private StaticLayout getStaticLayout(String text, TextPaint textPaint, int width, int textAlignment, float spacingMulti, float spacingAdd) {
        if (width < 0) {
            width = 0;
        }
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        if (textAlignment == 3) {
            alignment = Layout.Alignment.ALIGN_OPPOSITE;
        } else if (textAlignment == 4) {
            alignment = Layout.Alignment.ALIGN_CENTER;
        }
        return new StaticLayout(text, textPaint, width, alignment, spacingMulti, spacingAdd, true);
    }
}
