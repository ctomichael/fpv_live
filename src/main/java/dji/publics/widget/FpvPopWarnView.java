package dji.publics.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dji.frame.widget.R;
import dji.midware.media.DJIVideoDecoder;

public class FpvPopWarnView extends RelativeLayout {
    private static FpvPopWarnView mInstance = null;
    int continueTimeUnit = DJIVideoDecoder.connectLosedelay;
    ObjectAnimator continusAnim;
    ObjectAnimator fadeInAnim;
    int fadeTimeUnit = 1000;
    boolean isPlaying = false;
    AnimatorSet mAnimSet;
    ImageView mIv;
    LinearLayout mLy;
    TextView mTv;
    AnimatorSet scaleSet;
    int scaleTimeUnit = 150;

    public enum POPTIME {
        LENGTH_SHORT,
        LENGTH_LONG
    }

    public static synchronized FpvPopWarnView getInstance(Context ctx) {
        FpvPopWarnView fpvPopWarnView;
        synchronized (FpvPopWarnView.class) {
            if (mInstance == null) {
                mInstance = new FpvPopWarnView(ctx);
            }
            fpvPopWarnView = mInstance;
        }
        return fpvPopWarnView;
    }

    private FpvPopWarnView(Context context) {
        super(context);
        init();
    }

    private void init() {
        View views = LayoutInflater.from(getContext()).inflate(R.layout.fpv_pop_warn_view, (ViewGroup) null);
        this.mLy = (LinearLayout) views.findViewById(R.id.longan_pop_warning_ly);
        this.mIv = (ImageView) views.findViewById(R.id.longan_pop_warn_iv);
        this.mTv = (TextView) views.findViewById(R.id.longan_pop_warn_tv);
        if (views.getParent() != null) {
            ((ViewGroup) views.getParent()).removeView(views);
        }
        addView(views, new RelativeLayout.LayoutParams(-1, -1));
        ((ViewGroup) ((Activity) getContext()).getWindow().getDecorView().getRootView()).addView(this);
        initAnim();
    }

    private void initAnim() {
        this.fadeInAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f);
        this.fadeInAnim.setInterpolator(new DecelerateInterpolator());
        this.fadeInAnim.setDuration((long) this.fadeTimeUnit);
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(this, "scaleX", 0.1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(this, "scaleY", 0.1f);
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(this, "alpha", 10.0f);
        this.scaleSet = new AnimatorSet();
        this.scaleSet.setDuration((long) this.scaleTimeUnit);
        this.scaleSet.setInterpolator(new DecelerateInterpolator());
        this.scaleSet.addListener(new AnimatorListenerAdapter() {
            /* class dji.publics.widget.FpvPopWarnView.AnonymousClass1 */

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                FpvPopWarnView.this.setVisibility(4);
                FpvPopWarnView.this.setAlpha(1.0f);
                FpvPopWarnView.this.setScaleX(1.0f);
                FpvPopWarnView.this.setScaleY(1.0f);
            }
        });
        this.scaleSet.playTogether(scaleDownX, scaleDownY, fadeOut);
        this.continusAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f);
        this.continusAnim.setDuration((long) this.continueTimeUnit);
        this.mAnimSet = new AnimatorSet();
        this.mAnimSet.playSequentially(this.fadeInAnim, this.continusAnim, this.scaleSet);
        this.mAnimSet.addListener(new AnimatorListenerAdapter() {
            /* class dji.publics.widget.FpvPopWarnView.AnonymousClass2 */

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                FpvPopWarnView.this.setVisibility(0);
                FpvPopWarnView.this.setAlpha(0.3f);
                FpvPopWarnView.this.isPlaying = true;
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                FpvPopWarnView.this.isPlaying = false;
            }
        });
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    /* access modifiers changed from: protected */
    public void pop(int imageId, String content) {
        if (this.isPlaying) {
            this.mAnimSet.cancel();
        }
        this.mIv.setImageResource(imageId);
        this.mTv.setText(content);
        if (imageId == 0) {
            this.mIv.setVisibility(8);
            this.mLy.setMinimumWidth(getResources().getDimensionPixelOffset(R.dimen.dp_50_in_sw320dp));
        } else {
            this.mIv.setVisibility(0);
            this.mLy.setMinimumWidth(getResources().getDimensionPixelOffset(R.dimen.dp_170_in_sw320dp));
        }
        this.mAnimSet.start();
    }

    /* access modifiers changed from: protected */
    public void pop(int imageId, int contentId) {
        pop(imageId, getContext().getString(contentId));
    }

    public void pop(int imageId, int contentId, POPTIME time) {
        if (time == POPTIME.LENGTH_SHORT) {
            this.continusAnim.setDuration(0L);
        } else if (time == POPTIME.LENGTH_LONG) {
            this.continusAnim.setDuration((long) this.continueTimeUnit);
        }
        pop(imageId, contentId);
    }

    public void pop(int imageId, String contentId, POPTIME time) {
        if (time == POPTIME.LENGTH_SHORT) {
            this.continusAnim.setDuration(0L);
        } else if (time == POPTIME.LENGTH_LONG) {
            this.continusAnim.setDuration((long) this.continueTimeUnit);
        }
        pop(imageId, contentId);
    }

    public static void dispatchOnDestroy() {
        mInstance = null;
    }
}
