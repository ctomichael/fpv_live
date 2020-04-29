package dji.publics.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import dji.frame.widget.R;
import dji.publics.utils.V_DisplayUtil;

public class DJIVSeekBar extends View {
    private static final int MAX_LEVEL = 10000;
    protected int mGravity = 80;
    private boolean mIsDragging = false;
    protected int mMax = 1;
    private OnVSBChangeListener mOnChangedListener = null;
    protected int mProgress = 0;
    protected Drawable mProgressDrawable = null;
    protected int mProgressWidth = 0;
    private int mScaledTouchSlop = 0;
    protected int mSecondaryProgress = 0;
    protected Drawable mSecondaryThumb = null;
    protected Drawable mThumb = null;
    private float mTouchDownY = 0.0f;
    private float mTouchProgressOffset = 0.0f;

    public interface OnVSBChangeListener {
        void onProgressChanged(DJIVSeekBar dJIVSeekBar, int i, boolean z);

        void onStartTrackingTouch(DJIVSeekBar dJIVSeekBar);

        void onStopTrackingTouch(DJIVSeekBar dJIVSeekBar);
    }

    public DJIVSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VerticalSB, 0, 0);
        this.mProgressWidth = ta.getDimensionPixelSize(R.styleable.VerticalSB_progressHeight, this.mProgressWidth);
        Drawable drawable = ta.getDrawable(R.styleable.VerticalSB_progressDrawable);
        if (drawable != null) {
            setProgressDrawable(drawable);
        }
        setMax(ta.getInt(R.styleable.VerticalSB_max, this.mMax));
        setProgress(ta.getInt(R.styleable.VerticalSB_progress, this.mProgress));
        setSecondaryProgress(ta.getInt(R.styleable.VerticalSB_secondaryProgress, this.mSecondaryProgress));
        Drawable thumb = ta.getDrawable(R.styleable.VerticalSB_thumb);
        if (thumb != null) {
            setThumb(thumb);
        }
        Drawable sThumb = ta.getDrawable(R.styleable.VerticalSB_secondaryThumb);
        if (sThumb != null) {
            setSecondaryThumb(sThumb);
        }
        ta.recycle();
    }

    public void setOnChangeListener(OnVSBChangeListener listener) {
        this.mOnChangedListener = listener;
    }

    public void setProgressDrawable(Drawable d) {
        boolean needUpdate;
        Drawable d2 = tileify(d, false, 0);
        if (this.mProgressDrawable == null || d2 == this.mProgressDrawable) {
            needUpdate = false;
        } else {
            this.mProgressDrawable.setCallback(null);
            needUpdate = true;
        }
        if (d2 != null) {
            d2.setCallback(this);
        }
        this.mProgressDrawable = d2;
        postInvalidate();
        if (needUpdate) {
            updateDrawableBounds(getWidth(), getHeight());
            doRefreshProgress(16908301, this.mProgress, false, false);
            doRefreshProgress(16908303, this.mSecondaryProgress, false, false);
        }
    }

    public void setThumb(Drawable thumb) {
        boolean needUpdate;
        if (this.mThumb == null || thumb == this.mThumb) {
            needUpdate = false;
        } else {
            needUpdate = true;
        }
        if (!(thumb == null || !needUpdate || (thumb.getIntrinsicWidth() == this.mThumb.getIntrinsicWidth() && thumb.getIntrinsicHeight() == this.mThumb.getIntrinsicHeight()))) {
            requestLayout();
        }
        this.mThumb = thumb;
        invalidate();
        if (needUpdate) {
            updateDrawableBounds(getWidth(), getHeight());
        }
    }

    public void setSecondaryThumb(Drawable thumb) {
        this.mSecondaryThumb = thumb;
        if (thumb != null) {
            setDrawableBounds(getWidth(), getHeight(), thumb, this.mMax == 0 ? 0.0f : ((float) this.mSecondaryProgress) / ((float) this.mMax));
        }
        invalidate();
    }

    public int getMax() {
        return this.mMax;
    }

    public void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != this.mMax) {
            this.mMax = max;
            if (this.mProgress > max) {
                this.mProgress = max;
            }
            if (this.mThumb != null) {
                setDrawableBounds(getWidth(), getHeight(), this.mThumb, this.mMax == 0 ? 0.0f : ((float) this.mProgress) / ((float) this.mMax));
            }
            postInvalidate();
            doRefreshProgress(16908301, this.mProgress, false, false);
            doRefreshProgress(16908303, this.mSecondaryProgress, false, false);
        }
    }

    public int getProgress() {
        return this.mProgress;
    }

    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    private void setProgress(int progress, boolean fromUser) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > this.mMax) {
            progress = this.mMax;
        }
        if (progress != this.mProgress) {
            this.mProgress = progress;
            doRefreshProgress(16908301, this.mProgress, fromUser, true);
        }
    }

    public int getSecondaryProgress() {
        return this.mSecondaryProgress;
    }

    public void setSecondaryProgress(int secondProgress) {
        if (secondProgress < 0) {
            secondProgress = 0;
        }
        if (secondProgress > this.mMax) {
            secondProgress = this.mMax;
        }
        if (secondProgress != this.mSecondaryProgress) {
            this.mSecondaryProgress = secondProgress;
            doRefreshProgress(16908303, this.mSecondaryProgress, false, false);
        }
    }

    /* access modifiers changed from: protected */
    public void updateDrawableBounds(int w, int h) {
        Drawable d = this.mProgressDrawable;
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            int thumbHeight = thumb.getIntrinsicHeight();
        }
        int thumbWidth = thumb == null ? 0 : thumb.getIntrinsicWidth();
        int trackWidth = this.mProgressWidth;
        int max = this.mMax;
        float scale = max > 0 ? ((float) this.mProgress) / ((float) max) : 0.0f;
        if (thumb != null) {
            setDrawableBounds(w, h, thumb, scale);
        }
        int gapForCenteringTrack = (thumbWidth - trackWidth) / 2;
        int bottom = (h - getPaddingTop()) - getPaddingBottom();
        if (d != null) {
            d.setBounds(gapForCenteringTrack, 0, gapForCenteringTrack + trackWidth, bottom);
        }
    }

    /* access modifiers changed from: protected */
    public void setDrawableBounds(int w, int h, Drawable thumb, float scale) {
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        int available = ((h - getPaddingTop()) - getPaddingBottom()) - thumbHeight;
        int thumbLeft = (w - thumbWidth) / 2;
        int thumbPos = (getPaddingTop() + available) - ((int) (((float) available) * scale));
        thumb.setBounds(thumbLeft, thumbPos, thumbLeft + thumbWidth, thumbPos + thumbHeight);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case 0:
                if (!isInScrollingContainer()) {
                    setPressed(true);
                    if (this.mThumb != null) {
                        invalidate(this.mThumb.getBounds());
                    }
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    attemptClaimDrag();
                    break;
                } else {
                    this.mTouchDownY = event.getY();
                    break;
                }
            case 1:
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                invalidate();
                break;
            case 2:
                if (!this.mIsDragging) {
                    if (Math.abs(event.getY() - this.mTouchDownY) > ((float) this.mScaledTouchSlop)) {
                        setPressed(true);
                        if (this.mThumb != null) {
                            invalidate(this.mThumb.getBounds());
                        }
                        onStartTrackingTouch();
                        trackTouchEvent(event);
                        attemptClaimDrag();
                        break;
                    }
                } else {
                    trackTouchEvent(event);
                    break;
                }
                break;
            case 3:
                if (this.mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate();
                break;
        }
        return true;
    }

    private void trackTouchEvent(MotionEvent event) {
        float scale;
        int height = getHeight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int available = (height - paddingTop) - paddingBottom;
        int y = (int) event.getY();
        float progress = 0.0f;
        if (y > height - paddingBottom) {
            scale = 0.0f;
        } else if (y < paddingTop) {
            scale = 1.0f;
        } else {
            scale = ((float) ((available - y) + paddingTop)) / ((float) available);
            progress = this.mTouchProgressOffset;
        }
        setProgress((int) (progress + (((float) getMax()) * scale)), true);
    }

    private void attemptClaimDrag() {
        ViewParent vp = getParent();
        if (vp != null) {
            vp.requestDisallowInterceptTouchEvent(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void onStartTrackingTouch() {
        this.mIsDragging = true;
        if (this.mOnChangedListener != null) {
            this.mOnChangedListener.onStartTrackingTouch(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void onStopTrackingTouch() {
        this.mIsDragging = false;
        if (this.mOnChangedListener != null) {
            this.mOnChangedListener.onStopTrackingTouch(this);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isInScrollingContainer() {
        ViewParent vp = getParent();
        while (vp != null && (vp instanceof ViewGroup)) {
            if (((ViewGroup) vp).shouldDelayChildPressedState()) {
                return true;
            }
            vp = vp.getParent();
        }
        return false;
    }

    public Drawable getThumb() {
        return this.mThumb;
    }

    /* access modifiers changed from: protected */
    public boolean verifyDrawable(Drawable who) {
        return who == this.mProgressDrawable || who == this.mThumb || who == this.mSecondaryThumb || super.verifyDrawable(who);
    }

    public void invalidateDrawable(Drawable drawable) {
        if (verifyDrawable(drawable)) {
            Rect dirty = drawable.getBounds();
            int scrollX = getScrollX() + getPaddingLeft();
            int scrollY = getScrollY() + getPaddingRight();
            invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            return;
        }
        super.invalidateDrawable(drawable);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        updateDrawableBounds(w, h);
        if (this.mSecondaryThumb != null) {
            setDrawableBounds(w, h, this.mSecondaryThumb, this.mMax == 0 ? 0.0f : ((float) this.mSecondaryProgress) / ((float) this.mMax));
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(Math.max(this.mThumb == null ? 0 : this.mThumb.getIntrinsicWidth(), this.mProgressWidth) + getPaddingLeft() + getPaddingRight(), View.MeasureSpec.getSize(heightMeasureSpec) + getPaddingTop() + getPaddingBottom());
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable d = this.mProgressDrawable;
        if (d != null) {
            canvas.save();
            canvas.translate((float) getPaddingLeft(), (float) getPaddingTop());
            d.draw(canvas);
            canvas.restore();
        }
        if (this.mSecondaryThumb != null) {
            canvas.save();
            canvas.translate((float) getPaddingLeft(), (float) getPaddingTop());
            this.mSecondaryThumb.draw(canvas);
            canvas.restore();
        }
        if (this.mThumb != null) {
            canvas.save();
            canvas.translate((float) getPaddingLeft(), (float) getPaddingTop());
            this.mThumb.draw(canvas);
            canvas.restore();
        }
    }

    private synchronized void doRefreshProgress(int id, int progress, boolean fromUser, boolean callToApp) {
        float scale = this.mMax > 0 ? ((float) progress) / ((float) this.mMax) : 0.0f;
        Drawable d = this.mProgressDrawable;
        if (d != null) {
            Drawable progressDrawable = null;
            if (d instanceof LayerDrawable) {
                progressDrawable = ((LayerDrawable) d).findDrawableByLayerId(id);
            }
            int level = (int) (10000.0f * scale);
            if (progressDrawable == null) {
                progressDrawable = d;
            }
            progressDrawable.setLevel(level);
        } else {
            invalidate();
        }
        if (id == 16908301) {
            Drawable thumb = this.mThumb;
            if (thumb != null) {
                setDrawableBounds(getWidth(), getHeight(), thumb, scale);
                invalidate();
            }
            if (callToApp && this.mOnChangedListener != null) {
                this.mOnChangedListener.onProgressChanged(this, progress, fromUser);
            }
        } else if (id == 16908303) {
            Drawable thumb2 = this.mSecondaryThumb;
            if (thumb2 != null) {
                setDrawableBounds(getWidth(), getHeight(), thumb2, scale);
                invalidate();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void initAttrs(Context context) {
        this.mProgressWidth = V_DisplayUtil.dip2px(context, 4.0f);
        this.mMax = 100;
        this.mProgress = 0;
        this.mSecondaryProgress = 0;
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    private Shape getDrawableShape() {
        return new RoundRectShape(new float[]{5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f, 5.0f}, null, null);
    }

    /* JADX WARN: Type inference failed for: r9v6, types: [android.graphics.drawable.ClipDrawable], assign insn: 0x0074: CONSTRUCTOR  (r9v6 ? I:android.graphics.drawable.ClipDrawable) = 
      (r7v0 'shapeDrawable' android.graphics.drawable.ShapeDrawable A[D('shapeDrawable' android.graphics.drawable.ShapeDrawable)])
      (80 int)
      (2 int)
     call: android.graphics.drawable.ClipDrawable.<init>(android.graphics.drawable.Drawable, int, int):void type: CONSTRUCTOR */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.drawable.Drawable tileify(android.graphics.drawable.Drawable r13, boolean r14, int r15) {
        /*
            r12 = this;
            boolean r9 = r13 instanceof android.graphics.drawable.LayerDrawable
            if (r9 == 0) goto L_0x0040
            r1 = r13
            android.graphics.drawable.LayerDrawable r1 = (android.graphics.drawable.LayerDrawable) r1
            int r0 = r1.getNumberOfLayers()
            android.graphics.drawable.Drawable[] r6 = new android.graphics.drawable.Drawable[r0]
            r3 = 0
        L_0x000e:
            if (r3 >= r0) goto L_0x002e
            int r4 = r1.getId(r3)
            android.graphics.drawable.Drawable r10 = r1.getDrawable(r3)
            r9 = 16908301(0x102000d, float:2.3877265E-38)
            if (r4 == r9) goto L_0x0022
            r9 = 16908303(0x102000f, float:2.387727E-38)
            if (r4 != r9) goto L_0x002c
        L_0x0022:
            r9 = 1
        L_0x0023:
            android.graphics.drawable.Drawable r9 = r12.tileify(r10, r9, r4)
            r6[r3] = r9
            int r3 = r3 + 1
            goto L_0x000e
        L_0x002c:
            r9 = 0
            goto L_0x0023
        L_0x002e:
            android.graphics.drawable.LayerDrawable r5 = new android.graphics.drawable.LayerDrawable
            r5.<init>(r6)
            r3 = 0
        L_0x0034:
            if (r3 >= r0) goto L_0x0079
            int r9 = r1.getId(r3)
            r5.setId(r3, r9)
            int r3 = r3 + 1
            goto L_0x0034
        L_0x0040:
            boolean r9 = r13 instanceof android.graphics.drawable.BitmapDrawable
            if (r9 == 0) goto L_0x007a
            android.graphics.drawable.BitmapDrawable r13 = (android.graphics.drawable.BitmapDrawable) r13
            android.graphics.Bitmap r8 = r13.getBitmap()
            android.graphics.drawable.ShapeDrawable r7 = new android.graphics.drawable.ShapeDrawable
            android.graphics.drawable.shapes.Shape r9 = r12.getDrawableShape()
            r7.<init>(r9)
            android.graphics.BitmapShader r2 = new android.graphics.BitmapShader
            android.graphics.Shader$TileMode r9 = android.graphics.Shader.TileMode.REPEAT
            android.graphics.Shader$TileMode r10 = android.graphics.Shader.TileMode.CLAMP
            r2.<init>(r8, r9, r10)
            android.graphics.Paint r9 = r7.getPaint()
            r9.setShader(r2)
            r9 = 16908288(0x1020000, float:2.387723E-38)
            if (r9 != r15) goto L_0x006d
            int r9 = r8.getWidth()
            r12.mProgressWidth = r9
        L_0x006d:
            if (r14 == 0) goto L_0x0078
            android.graphics.drawable.ClipDrawable r9 = new android.graphics.drawable.ClipDrawable
            r10 = 80
            r11 = 2
            r9.<init>(r7, r10, r11)
            r7 = r9
        L_0x0078:
            r5 = r7
        L_0x0079:
            return r5
        L_0x007a:
            r5 = r13
            goto L_0x0079
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.publics.widget.DJIVSeekBar.tileify(android.graphics.drawable.Drawable, boolean, int):android.graphics.drawable.Drawable");
    }
}
