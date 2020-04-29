package dji.publics.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;
import dji.pilot.publics.R;
import java.util.ArrayList;

public class DJIStageView extends ViewAnimator {
    private Animation mAnimLeftIn;
    private Animation mAnimLeftOut;
    private Animation mAnimRightIn;
    private Animation mAnimRightOut;
    private Context mContext;
    private int mCurrentStage;
    protected LayoutInflater mInflater;
    private OnStageChangeListener mOnStageChangeListener;
    private OnOptListener mOptListener;
    private final ArrayList<StageInfo> mStageViews;

    public interface BaseStageView {
        void dispatchOnPause();

        void dispatchOnResume();

        void dispatchOnStart();

        void dispatchOnStop();

        View getView();
    }

    public interface FirstStageListener {
        void dispatchOnPause();

        void dispatchOnResume();

        void dispatchOnStart();

        void dispatchOnStop();
    }

    public interface OnStageChangeListener {
        void onStageChange(int i, int i2, int i3);

        void onStageStop(int i);
    }

    public interface MoreStageView extends BaseStageView {
        boolean handleClose();

        boolean handleGoBack();
    }

    public interface OnOptListener {
        boolean handleClose();
    }

    public DJIStageView(Context context) {
        this(context, null);
    }

    public DJIStageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mStageViews = new ArrayList<>(4);
        this.mCurrentStage = 0;
        this.mContext = null;
        this.mInflater = null;
        this.mOnStageChangeListener = null;
        this.mOptListener = null;
        this.mAnimLeftIn = null;
        this.mAnimLeftOut = null;
        this.mAnimRightIn = null;
        this.mAnimRightOut = null;
        if (!isInEditMode()) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(this.mContext);
            initializeAnims();
        }
    }

    public void stop() {
        if (this.mOnStageChangeListener != null) {
            this.mOnStageChangeListener.onStageStop(0);
        }
    }

    public void condictionStop(int condiction) {
        if (this.mOnStageChangeListener != null) {
            this.mOnStageChangeListener.onStageStop(condiction);
        }
    }

    public void setOnStageChangeListener(OnStageChangeListener listener) {
        this.mOnStageChangeListener = listener;
    }

    public void setOnOptListener(OnOptListener listener) {
        this.mOptListener = listener;
    }

    /* access modifiers changed from: protected */
    public BaseStageView generateStageViewByType(int layoutId, int stage) {
        return (BaseStageView) this.mInflater.inflate(layoutId, (ViewGroup) null);
    }

    public boolean canGoBack() {
        return this.mCurrentStage > 1;
    }

    public boolean handleGoBack() {
        StageInfo stage = null;
        if (this.mCurrentStage >= 1) {
            stage = this.mStageViews.get(this.mCurrentStage - 1);
        }
        if (stage == null || !(stage.mStageView instanceof MoreStageView)) {
            return false;
        }
        return ((MoreStageView) stage.mStageView).handleGoBack();
    }

    public boolean handleClose() {
        StageInfo stage = null;
        if (this.mCurrentStage >= 1) {
            stage = this.mStageViews.get(this.mCurrentStage - 1);
        }
        if (stage == null || !(stage.mStageView instanceof MoreStageView)) {
            return false;
        }
        return ((MoreStageView) stage.mStageView).handleClose();
    }

    public boolean closeViews() {
        if (this.mOptListener != null) {
            return this.mOptListener.handleClose();
        }
        dispatchOnStop(true);
        return true;
    }

    public int getCurrentStage() {
        return this.mCurrentStage;
    }

    public int getCurrentStageTitleResId() {
        if (this.mCurrentStage >= 1) {
            return this.mStageViews.get(this.mCurrentStage - 1).mTitleResId;
        }
        return 0;
    }

    public BaseStageView getCurrentStageView() {
        if (this.mCurrentStage >= 1) {
            return this.mStageViews.get(this.mCurrentStage - 1).mStageView;
        }
        return null;
    }

    public boolean isEmpty() {
        return this.mCurrentStage == 0;
    }

    public void changeFirstStageView(int layoutId, int titleResId, boolean notify) {
        if (!this.mStageViews.isEmpty() && this.mStageViews.get(0).mLayoutId != layoutId) {
            for (int i = this.mCurrentStage - 1; i >= 0; i--) {
                StageInfo tmp = this.mStageViews.get(i);
                if (notify) {
                    if (i == this.mCurrentStage - 1) {
                        tmp.mStageView.dispatchOnPause();
                    }
                    tmp.mStageView.dispatchOnStop();
                }
                removeView(tmp.mStageView.getView());
            }
            this.mStageViews.clear();
            this.mCurrentStage = 0;
            StageInfo stage = new StageInfo();
            stage.mStageView = generateStageViewByType(layoutId, this.mCurrentStage + 1);
            stage.mLayoutId = layoutId;
            stage.mTitleResId = titleResId;
            this.mStageViews.add(stage);
            addView(stage.mStageView.getView(), this.mCurrentStage);
            setDisplayedChild(this.mCurrentStage);
            this.mCurrentStage++;
            if (notify) {
                stage.mStageView.dispatchOnStart();
                stage.mStageView.dispatchOnResume();
                notifyStageChanged(stage.mTitleResId);
            }
        }
    }

    private StageInfo generateStageInfo(int layoutId, int titleResId) {
        StageInfo stage = null;
        if (this.mStageViews.size() > this.mCurrentStage) {
            stage = this.mStageViews.get(this.mCurrentStage);
        }
        if (stage == null) {
            StageInfo stage2 = new StageInfo();
            stage2.mStageView = generateStageViewByType(layoutId, this.mCurrentStage + 1);
            stage2.mLayoutId = layoutId;
            stage2.mTitleResId = titleResId;
            this.mStageViews.add(stage2);
            addView(stage2.mStageView.getView(), this.mCurrentStage);
            return stage2;
        } else if (stage.mLayoutId != layoutId) {
            removeView(stage.mStageView.getView());
            stage.mStageView = generateStageViewByType(layoutId, this.mCurrentStage + 1);
            stage.mLayoutId = layoutId;
            stage.mTitleResId = titleResId;
            addView(stage.mStageView.getView(), this.mCurrentStage);
            return stage;
        } else {
            stage.mTitleResId = titleResId;
            return stage;
        }
    }

    public BaseStageView createStageView(int layoutId, int titleResId, boolean anim) {
        if (this.mCurrentStage >= 1) {
            StageInfo before = this.mStageViews.get(this.mCurrentStage - 1);
            if (before.mLayoutId == layoutId && titleResId == before.mTitleResId) {
                return before.mStageView;
            }
            before.mStageView.dispatchOnPause();
        }
        StageInfo stage = generateStageInfo(layoutId, titleResId);
        BaseStageView view = stage.mStageView;
        if (this.mCurrentStage >= 1) {
            view.dispatchOnStart();
            view.dispatchOnResume();
        }
        setAnims(anim, true);
        setDisplayedChild(this.mCurrentStage);
        this.mCurrentStage++;
        notifyStageChanged(stage.mTitleResId);
        return view;
    }

    public BaseStageView createStageViewWithTag(int layoutId, int titleResId, boolean anim, Object tag) {
        if (this.mCurrentStage >= 1) {
            StageInfo before = this.mStageViews.get(this.mCurrentStage - 1);
            if (before.mLayoutId == layoutId && titleResId == before.mTitleResId) {
                return before.mStageView;
            }
            before.mStageView.dispatchOnPause();
        }
        StageInfo stage = generateStageInfo(layoutId, titleResId);
        if (tag != null) {
            stage.mStageView.getView().setTag(tag);
        }
        BaseStageView view = stage.mStageView;
        if (this.mCurrentStage >= 1) {
            view.dispatchOnStart();
            view.dispatchOnResume();
        }
        setAnims(anim, true);
        setDisplayedChild(this.mCurrentStage);
        this.mCurrentStage++;
        notifyStageChanged(stage.mTitleResId);
        return view;
    }

    public BaseStageView createStageViewWithAnim(int layoutId, int titleResId, boolean anim, int animType) {
        StageInfo stage = null;
        if (this.mStageViews.size() > this.mCurrentStage) {
            stage = this.mStageViews.get(this.mCurrentStage);
        }
        if (this.mCurrentStage >= 1) {
            StageInfo before = this.mStageViews.get(this.mCurrentStage - 1);
            if (before.mLayoutId == layoutId && titleResId == before.mTitleResId) {
                return before.mStageView;
            }
            before.mStageView.dispatchOnPause();
        }
        if (stage == null) {
            stage = new StageInfo();
            stage.mStageView = generateStageViewByType(layoutId, this.mCurrentStage + 1);
            stage.mLayoutId = layoutId;
            stage.mTitleResId = titleResId;
            this.mStageViews.add(stage);
            addView(stage.mStageView.getView(), this.mCurrentStage);
        } else if (stage.mLayoutId != layoutId) {
            removeView(stage.mStageView.getView());
            stage.mStageView = generateStageViewByType(layoutId, this.mCurrentStage + 1);
            stage.mLayoutId = layoutId;
            stage.mTitleResId = titleResId;
            addView(stage.mStageView.getView(), this.mCurrentStage);
        } else {
            stage.mTitleResId = titleResId;
        }
        BaseStageView view = stage.mStageView;
        if (this.mCurrentStage >= 1) {
            view.dispatchOnStart();
            view.dispatchOnResume();
        }
        if (animType > 0) {
            setAnims(anim, true);
        } else {
            setAnims(anim, false);
        }
        setDisplayedChild(this.mCurrentStage);
        this.mCurrentStage++;
        notifyStageChanged(stage.mTitleResId);
        return view;
    }

    public BaseStageView destoryStageView(boolean anim) {
        if (this.mCurrentStage <= 1) {
            return null;
        }
        StageInfo stage = null;
        if (this.mStageViews.size() > this.mCurrentStage - 1) {
            stage = this.mStageViews.get(this.mCurrentStage - 1);
        }
        if (stage == null) {
            return null;
        }
        BaseStageView view = stage.mStageView;
        view.dispatchOnPause();
        view.dispatchOnStop();
        setAnims(anim, false);
        this.mCurrentStage--;
        setDisplayedChild(this.mCurrentStage - 1);
        StageInfo stage2 = this.mStageViews.get(this.mCurrentStage - 1);
        stage2.mStageView.dispatchOnResume();
        notifyStageChanged(stage2.mTitleResId);
        return view;
    }

    public void dispatchOnStart(boolean anim) {
        if (this.mCurrentStage >= 1) {
            StageInfo stage = this.mStageViews.get(this.mCurrentStage - 1);
            stage.mStageView.dispatchOnStart();
            stage.mStageView.dispatchOnResume();
        }
    }

    public void dispatchOnStop(boolean anim) {
        if (this.mCurrentStage >= 1) {
            for (int i = this.mCurrentStage - 1; i >= 1; i--) {
                StageInfo stage = this.mStageViews.get(i);
                stage.mStageView.dispatchOnPause();
                stage.mStageView.dispatchOnStop();
            }
            StageInfo first = this.mStageViews.get(0);
            setAnims(anim, false);
            this.mCurrentStage = 1;
            setDisplayedChild(0);
            first.mStageView.dispatchOnPause();
            first.mStageView.dispatchOnStop();
            notifyStageChanged(first.mTitleResId);
        }
    }

    public void clearAllStage() {
        if (this.mCurrentStage >= 1) {
            setAnims(false, false);
            this.mCurrentStage = 1;
            setDisplayedChild(0);
        }
    }

    public void stopAllStage() {
        if (this.mCurrentStage >= 1) {
            for (int i = this.mCurrentStage - 1; i >= 0; i--) {
                StageInfo stage = this.mStageViews.get(i);
                if (i == this.mCurrentStage - 1) {
                    stage.mStageView.dispatchOnPause();
                }
                stage.mStageView.dispatchOnStop();
            }
        }
    }

    public void startAllStage() {
        if (this.mCurrentStage >= 1) {
            for (int i = 0; i <= this.mCurrentStage - 1; i++) {
                StageInfo stage = this.mStageViews.get(i);
                stage.mStageView.dispatchOnStart();
                if (i == this.mCurrentStage - 1) {
                    stage.mStageView.dispatchOnResume();
                }
            }
        }
    }

    private void setAnims(boolean anim, boolean create) {
        if (!anim) {
            setInAnimation(null);
            setOutAnimation(null);
        } else if (create) {
            setInAnimation(this.mAnimRightIn);
            setOutAnimation(this.mAnimLeftOut);
        } else {
            setInAnimation(this.mAnimLeftIn);
            setOutAnimation(this.mAnimRightOut);
        }
    }

    private void initializeAnims() {
        this.mAnimLeftIn = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_left_in);
        this.mAnimLeftOut = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_left_out);
        this.mAnimRightIn = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_right_in);
        this.mAnimRightOut = AnimationUtils.loadAnimation(this.mContext, R.anim.slide_right_out);
    }

    private void notifyStageChanged(int titleResId) {
        if (this.mOnStageChangeListener != null) {
            this.mOnStageChangeListener.onStageChange(this.mCurrentStage, titleResId, 0);
        }
    }

    private static final class StageInfo {
        public int mLayoutId;
        public BaseStageView mStageView;
        public int mTitleResId;

        private StageInfo() {
            this.mLayoutId = 0;
            this.mTitleResId = 0;
            this.mStageView = null;
        }
    }
}
