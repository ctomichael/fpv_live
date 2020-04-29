package dji.publics.widget.djiviewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import dji.frame.widget.R;

public class OutlineContainer extends FrameLayout implements Animatable {
    private static final long ANIMATION_DURATION = 500;
    private static final long FRAME_DURATION = 16;
    /* access modifiers changed from: private */
    public float mAlpha = 1.0f;
    /* access modifiers changed from: private */
    public final Interpolator mInterpolator = new Interpolator() {
        /* class dji.publics.widget.djiviewpager.OutlineContainer.AnonymousClass1 */

        public float getInterpolation(float t) {
            float t2 = t - 1.0f;
            return (t2 * t2 * t2) + 1.0f;
        }
    };
    private boolean mIsRunning = false;
    private Paint mOutlinePaint;
    /* access modifiers changed from: private */
    public long mStartTime;
    /* access modifiers changed from: private */
    public final Runnable mUpdater = new Runnable() {
        /* class dji.publics.widget.djiviewpager.OutlineContainer.AnonymousClass2 */

        public void run() {
            long duration = AnimationUtils.currentAnimationTimeMillis() - OutlineContainer.this.mStartTime;
            if (duration >= 500) {
                float unused = OutlineContainer.this.mAlpha = 0.0f;
                OutlineContainer.this.invalidate();
                OutlineContainer.this.stop();
                return;
            }
            float unused2 = OutlineContainer.this.mAlpha = OutlineContainer.this.mInterpolator.getInterpolation(1.0f - (((float) duration) / 500.0f));
            OutlineContainer.this.invalidate();
            OutlineContainer.this.postDelayed(OutlineContainer.this.mUpdater, 16);
        }
    };

    public OutlineContainer(Context context) {
        super(context);
        init();
    }

    public OutlineContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OutlineContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.mOutlinePaint = new Paint();
        this.mOutlinePaint.setAntiAlias(true);
        this.mOutlinePaint.setStrokeWidth((float) Util.dpToPx(getResources(), 2));
        this.mOutlinePaint.setColor(getResources().getColor(R.color.holo_blue));
        this.mOutlinePaint.setStyle(Paint.Style.STROKE);
        int padding = Util.dpToPx(getResources(), 10);
        setPadding(padding, padding, padding, padding);
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int offset = Util.dpToPx(getResources(), 5);
        if (this.mOutlinePaint.getColor() != DJIViewPager.sOutlineColor) {
            this.mOutlinePaint.setColor(DJIViewPager.sOutlineColor);
        }
        this.mOutlinePaint.setAlpha((int) (this.mAlpha * 255.0f));
        canvas.drawRect(new Rect(offset, offset, getMeasuredWidth() - offset, getMeasuredHeight() - offset), this.mOutlinePaint);
    }

    public void setOutlineAlpha(float alpha) {
        this.mAlpha = alpha;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public void start() {
        if (!this.mIsRunning) {
            this.mIsRunning = true;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            post(this.mUpdater);
        }
    }

    public void stop() {
        if (this.mIsRunning) {
            this.mIsRunning = false;
        }
    }
}
