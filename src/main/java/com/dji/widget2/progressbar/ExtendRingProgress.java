package com.dji.widget2.progressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.dji.widget2.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class ExtendRingProgress extends ConstraintLayout {
    public static final int BOLD = 1;
    public static final int BOLD_ITALIC = 3;
    public static final int ITALIC = 2;
    public static final int NORMAL = 0;
    protected static final int STYLE_MASK = 3;
    private boolean isAutoUpdateContentText = true;
    private ImageView mFirmwareUpWidgetIvImg;
    private RingProgress mFirmwareUpWidgetRingProgress;
    private TextView mFirmwareUpWidgetTvProgress;
    private int mTextColor = -1;
    private int mTextSize = -1;
    private int mTextStyle = 0;
    private float progress = 0.0f;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }

    public ExtendRingProgress(Context context) {
        super(context);
        init(context, null, R.style.RingProgressTheme);
    }

    public ExtendRingProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, R.style.RingProgressTheme);
    }

    public ExtendRingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        inflate(context, R.layout.firmware_up_widget_progress, this);
        this.mFirmwareUpWidgetRingProgress = (RingProgress) findViewById(R.id.firmware_up_widget_ring_progress);
        this.mFirmwareUpWidgetTvProgress = (TextView) findViewById(R.id.firmware_up_widget_tv_progress);
        this.mFirmwareUpWidgetIvImg = (ImageView) findViewById(R.id.firmware_up_widget_iv_img);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExtendRingProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();
    }

    private void initByAttributes(TypedArray attributes) {
        int finishedStrokeColor = this.mFirmwareUpWidgetRingProgress.getFinishedStrokeColor();
        int unfinishedStrokeColor = this.mFirmwareUpWidgetRingProgress.getUnfinishedStrokeColor();
        float finishedStrokeWidth = this.mFirmwareUpWidgetRingProgress.getFinishedStrokeWidth();
        float unfinishedStrokeWidth = this.mFirmwareUpWidgetRingProgress.getUnfinishedStrokeWidth();
        int startingDegree = this.mFirmwareUpWidgetRingProgress.getStartingDegree();
        float progress2 = this.mFirmwareUpWidgetRingProgress.getProgress();
        int finishedStrokeColor2 = attributes.getColor(R.styleable.ExtendRingProgress_ring_finished_color, finishedStrokeColor);
        int unfinishedStrokeColor2 = attributes.getColor(R.styleable.ExtendRingProgress_ring_unfinished_color, unfinishedStrokeColor);
        float progress3 = attributes.getFloat(R.styleable.ExtendRingProgress_ring_progress, progress2);
        float finishedStrokeWidth2 = attributes.getDimension(R.styleable.ExtendRingProgress_ring_finished_stroke_width, finishedStrokeWidth);
        float unfinishedStrokeWidth2 = attributes.getDimension(R.styleable.ExtendRingProgress_ring_unfinished_stroke_width, unfinishedStrokeWidth);
        int startingDegree2 = attributes.getInt(R.styleable.ExtendRingProgress_ring_circle_starting_degree, startingDegree);
        this.mTextStyle = attributes.getInt(R.styleable.ExtendRingProgress_ring_circle_center_textStyle, this.mTextStyle);
        this.mTextSize = attributes.getDimensionPixelSize(R.styleable.ExtendRingProgress_extend_ring_text_size, this.mTextSize);
        this.mTextColor = attributes.getColor(R.styleable.ExtendRingProgress_extend_ring_text_color, this.mTextColor);
        this.mFirmwareUpWidgetTvProgress.setTypeface(Typeface.defaultFromStyle(this.mTextStyle));
        if (this.mTextSize > 0) {
            this.mFirmwareUpWidgetTvProgress.setTextSize(0, (float) this.mTextSize);
        }
        if (this.mTextColor > 0) {
            this.mFirmwareUpWidgetTvProgress.setTextColor(this.mTextColor);
        }
        this.mFirmwareUpWidgetRingProgress.setProgressNonInvalidate(progress3).setFinishedStrokeColorNonInvalidate(finishedStrokeColor2).setUnfinishedStrokeColorNonInvalidate(unfinishedStrokeColor2).setFinishedStrokeWidthNonInvalidate(finishedStrokeWidth2).setUnfinishedStrokeWidthNonInvalidate(unfinishedStrokeWidth2).setStartingDegreeNonInvalidate(startingDegree2).invalidate();
    }

    public void setProgress(float progress2) {
        this.progress = progress2;
        if (this.isAutoUpdateContentText) {
            this.mFirmwareUpWidgetTvProgress.setText(String.format(Locale.getDefault(), "%.0f%%", Float.valueOf(progress2)));
        }
        this.mFirmwareUpWidgetRingProgress.setProgress(progress2);
    }

    public int getMax() {
        return this.mFirmwareUpWidgetRingProgress.getMax();
    }

    public void setMax(int max) {
        this.mFirmwareUpWidgetRingProgress.setMax(max);
    }

    public RingProgress setProgressNonInvalidate(float progress2) {
        return this.mFirmwareUpWidgetRingProgress.setProgressNonInvalidate(progress2);
    }

    public RingProgress setFinishedStrokeColorNonInvalidate(int finishedStrokeColor) {
        return this.mFirmwareUpWidgetRingProgress.setFinishedStrokeColorNonInvalidate(finishedStrokeColor);
    }

    public RingProgress setUnfinishedStrokeColorNonInvalidate(int unfinishedStrokeColor) {
        return this.mFirmwareUpWidgetRingProgress.setUnfinishedStrokeColorNonInvalidate(unfinishedStrokeColor);
    }

    public RingProgress setStartingDegreeNonInvalidate(int startingDegree) {
        return this.mFirmwareUpWidgetRingProgress.setStartingDegreeNonInvalidate(startingDegree);
    }

    public RingProgress setFinishedStrokeWidthNonInvalidate(float finishedStrokeWidth) {
        return this.mFirmwareUpWidgetRingProgress.setFinishedStrokeWidthNonInvalidate(finishedStrokeWidth);
    }

    public RingProgress setUnfinishedStrokeWidthNonInvalidate(float unfinishedStrokeWidth) {
        return this.mFirmwareUpWidgetRingProgress.setUnfinishedStrokeWidthNonInvalidate(unfinishedStrokeWidth);
    }

    public float getFinishedStrokeWidth() {
        return this.mFirmwareUpWidgetRingProgress.getFinishedStrokeWidth();
    }

    public void setFinishedStrokeWidth(float finishedStrokeWidth) {
        this.mFirmwareUpWidgetRingProgress.setFinishedStrokeWidth(finishedStrokeWidth);
    }

    public float getUnfinishedStrokeWidth() {
        return this.mFirmwareUpWidgetRingProgress.getUnfinishedStrokeWidth();
    }

    public void setUnfinishedStrokeWidth(float unfinishedStrokeWidth) {
        this.mFirmwareUpWidgetRingProgress.setUnfinishedStrokeWidth(unfinishedStrokeWidth);
    }

    public int getFinishedStrokeColor() {
        return this.mFirmwareUpWidgetRingProgress.getFinishedStrokeColor();
    }

    public void setFinishedStrokeColor(int finishedStrokeColor) {
        this.mFirmwareUpWidgetRingProgress.setFinishedStrokeColor(finishedStrokeColor);
    }

    public int getUnfinishedStrokeColor() {
        return this.mFirmwareUpWidgetRingProgress.getUnfinishedStrokeColor();
    }

    public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
        this.mFirmwareUpWidgetRingProgress.setUnfinishedStrokeColor(unfinishedStrokeColor);
    }

    public int getInnerBackgroundColor() {
        return this.mFirmwareUpWidgetRingProgress.getInnerBackgroundColor();
    }

    public void setInnerBackgroundColor(int innerBackgroundColor) {
        this.mFirmwareUpWidgetRingProgress.setInnerBackgroundColor(innerBackgroundColor);
    }

    public int getStartingDegree() {
        return this.mFirmwareUpWidgetRingProgress.getStartingDegree();
    }

    public void setStartingDegree(int startingDegree) {
        this.mFirmwareUpWidgetRingProgress.setStartingDegree(startingDegree);
    }

    public void switchToCenterText(String string) {
        this.mFirmwareUpWidgetIvImg.setVisibility(8);
        this.mFirmwareUpWidgetTvProgress.setVisibility(0);
        this.mFirmwareUpWidgetTvProgress.setText(string);
    }

    public void switchToCenterText() {
        this.mFirmwareUpWidgetIvImg.setVisibility(8);
        this.mFirmwareUpWidgetTvProgress.setVisibility(0);
    }

    public void switchToCenterImg(@DrawableRes int resId) {
        this.mFirmwareUpWidgetIvImg.setVisibility(0);
        this.mFirmwareUpWidgetTvProgress.setVisibility(8);
        this.mFirmwareUpWidgetIvImg.setImageResource(resId);
    }

    public void switchToCenterImg() {
        this.mFirmwareUpWidgetIvImg.setVisibility(0);
        this.mFirmwareUpWidgetTvProgress.setVisibility(8);
    }

    public void hideCenterContent() {
        this.mFirmwareUpWidgetIvImg.setVisibility(8);
        this.mFirmwareUpWidgetTvProgress.setVisibility(8);
    }

    public float getProgress() {
        return this.progress;
    }

    public void setContentText(CharSequence text) {
        this.mFirmwareUpWidgetTvProgress.setText(text);
    }

    public void setContentTextAutoUpdate(boolean isEnable) {
        this.isAutoUpdateContentText = isEnable;
    }

    public void setContentTextColor(@ColorInt int color) {
        this.mFirmwareUpWidgetTvProgress.setTextColor(color);
    }

    public void setCenterTextStyle(int textStyle) {
        if ((textStyle & -4) != 0) {
            textStyle = 0;
        }
        if (this.mTextStyle != textStyle) {
            this.mTextStyle = textStyle;
            this.mFirmwareUpWidgetTvProgress.setTypeface(Typeface.defaultFromStyle(this.mTextStyle));
        }
    }

    public void setCenterTextSize(int sizePx) {
        if (sizePx > 0 && this.mTextSize != sizePx) {
            this.mTextStyle = sizePx;
            this.mFirmwareUpWidgetTvProgress.setTextSize(0, (float) this.mTextSize);
        }
    }

    public void setCenterTextColor(int textColor) {
        if (textColor > 0 && this.mTextColor != textColor) {
            this.mTextColor = textColor;
            this.mFirmwareUpWidgetTvProgress.setTextColor(this.mTextColor);
        }
    }
}
