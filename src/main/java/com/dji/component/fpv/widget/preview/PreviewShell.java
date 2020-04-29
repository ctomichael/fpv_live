package com.dji.component.fpv.widget.preview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.dji.component.fpv.base.ViewModelBinder;
import com.dji.component.fpv.widget.preview.PreviewHelper;
import com.dji.component.fpv.widget.subline.SubLineMainView;
import com.dji.component.fpv.widget.subline.SubLineMainViewModel;
import com.dji.component.fpv.widget.subline.SubLineTypeInfo;
import com.jakewharton.rxbinding2.view.RxView;
import dji.common.camera.CameraRecordingState;
import dji.log.DJILog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreviewShell extends ConstraintLayout implements ViewModelBinder<PreviewShellModel>, PreviewHelper.SizeProvider {
    private static final String TAG = "PreviewShell";
    private Guideline mBottomGuideline;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Guideline mLeftGuideline;
    private PreviewHelper mPreviewHelper;
    private PreviewShellModel mPreviewShellModel;
    private Guideline mRightGuideline;
    private View mShotMaskView;
    private CompositeDisposable mSubLineDisposable = new CompositeDisposable();
    private SubLineMainView mSubLineMainView;
    private SubLineMainViewModel mSubLineMainViewModel;
    /* access modifiers changed from: private */
    public CardView mSurfaceContainerCard;
    private VideoSurfaceView mSurfaceView;

    public PreviewShell(@NonNull Context context) {
        super(context);
    }

    public PreviewShell(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PreviewShell(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(PreviewShellModel previewShellModel) {
        this.mPreviewShellModel = previewShellModel;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!isInEditMode()) {
            this.mSurfaceContainerCard = (CardView) findViewById(R.id.view_surface_container);
            this.mSurfaceView = (VideoSurfaceView) findViewById(R.id.view_surface);
            this.mShotMaskView = findViewById(R.id.view_shot_mask);
            this.mSubLineMainView = (SubLineMainView) findViewById(R.id.sub_line_main_view);
            this.mLeftGuideline = (Guideline) findViewById(R.id.guideline_left);
            this.mRightGuideline = (Guideline) findViewById(R.id.guideline_right);
            this.mBottomGuideline = (Guideline) findViewById(R.id.guideline_bottom);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mPreviewHelper = new PreviewHelper(getContext(), this.mSurfaceView, this);
            this.mPreviewShellModel.onCreatePreviewHelper(this.mPreviewHelper);
            this.mPreviewShellModel.onCreateView();
            post(new PreviewShell$$Lambda$0(this));
            this.mCompositeDisposable.add(RxView.clicks(this.mSurfaceView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new PreviewShell$$Lambda$1(this)));
            this.mCompositeDisposable.add(this.mPreviewShellModel.getDimensionRatioObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(new PreviewShell$$Lambda$2(this)));
            this.mCompositeDisposable.add(this.mPreviewShellModel.getPreviewAdjustSizeObservable().switchMap(new PreviewShell$$Lambda$3(this)).observeOn(AndroidSchedulers.mainThread()).subscribe(new PreviewShell$$Lambda$4(this)));
            this.mCompositeDisposable.add(RxView.layoutChanges(this.mSurfaceContainerCard).subscribe(new PreviewShell$$Lambda$5(this)));
            this.mCompositeDisposable.add(this.mPreviewShellModel.getConnectionObservable().subscribe(new PreviewShell$$Lambda$6(this)));
            this.mCompositeDisposable.add(this.mPreviewShellModel.getIsShootingPhotoObservable().filter(PreviewShell$$Lambda$7.$instance).doOnNext(new PreviewShell$$Lambda$8(this)).delay(250, TimeUnit.MILLISECONDS, Schedulers.io()).doOnNext(new PreviewShell$$Lambda$9(this)).observeOn(AndroidSchedulers.mainThread()).doOnNext(new PreviewShell$$Lambda$10(this)).subscribe());
            this.mCompositeDisposable.add(this.mPreviewShellModel.getCameraRecordingState().filter(PreviewShell$$Lambda$11.$instance).doOnNext(new PreviewShell$$Lambda$12(this)).subscribe());
            this.mSubLineMainViewModel = new SubLineMainViewModel(null, getContext());
            this.mSubLineMainViewModel.onCreateView();
            this.mSubLineDisposable.add(this.mSubLineMainViewModel.getObservable().subscribe(new PreviewShell$$Lambda$13(this)));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$0$PreviewShell() {
        this.mPreviewShellModel.initSmallPreview(generateSmallPreviewRect());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$1$PreviewShell(Object o) throws Exception {
        this.mPreviewShellModel.performClick();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$2$PreviewShell(String s) throws Exception {
        DJILog.logWriteD(TAG, "setGuide dimenRatio = " + s, new Object[0]);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) this.mSurfaceContainerCard.getLayoutParams();
        lp.dimensionRatio = s;
        this.mSurfaceContainerCard.setLayoutParams(lp);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ ObservableSource lambda$onAttachedToWindow$3$PreviewShell(String s) throws Exception {
        if (this.mPreviewShellModel.isSmallPreview()) {
            return this.mPreviewShellModel.getSmallRectObservable();
        }
        if (this.mPreviewShellModel.isLargePreview()) {
            return this.mPreviewShellModel.getLargeRectObservable();
        }
        return Observable.just(new Rect(-1, -1, -1, -1));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$4$PreviewShell(Rect rect) throws Exception {
        DJILog.logWriteD(TAG, "setGuide rect = " + rect, new Object[0]);
        if (rect.width() != 0 && rect.height() != 0) {
            setGuidelineBegin(this.mBottomGuideline, rect.bottom);
            setGuidelineBegin(this.mLeftGuideline, rect.left);
            setGuidelineBegin(this.mRightGuideline, rect.right);
            DJILog.logWriteD(TAG, "setSurfaceFixedSize [%s, %s]", Integer.valueOf(rect.width()), Integer.valueOf(rect.height()));
            this.mSurfaceView.getHolder().setFixedSize(rect.width(), rect.height());
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$5$PreviewShell(Object o) throws Exception {
        handleLayoutChange();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$6$PreviewShell(Boolean isConnect) throws Exception {
        if (!isConnect.booleanValue()) {
            DJILog.logWriteD(TAG, "SurfaceTexture toGrey", new Object[0]);
            this.mSurfaceView.toGray();
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$8$PreviewShell(Boolean al) throws Exception {
        this.mShotMaskView.setVisibility(0);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$9$PreviewShell(Boolean al) throws Exception {
        this.mPreviewShellModel.handleScreenBitmapReady(this.mSurfaceView.getBitmap());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$10$PreviewShell(Boolean al) throws Exception {
        this.mShotMaskView.setVisibility(4);
    }

    static final /* synthetic */ boolean lambda$onAttachedToWindow$11$PreviewShell(CameraRecordingState cameraRecordingState) throws Exception {
        return cameraRecordingState == CameraRecordingState.STOPPING;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$12$PreviewShell(CameraRecordingState al) throws Exception {
        this.mPreviewShellModel.handleScreenBitmapReady(this.mSurfaceView.getBitmap());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onAttachedToWindow$13$PreviewShell(List lineTypeInfoList) throws Exception {
        this.mSubLineMainView.setSubLineDrawType(lineTypeInfoList);
        this.mSubLineMainView.setVisibility(8);
        Iterator it2 = lineTypeInfoList.iterator();
        while (it2.hasNext()) {
            if (((SubLineTypeInfo) it2.next()).isCanDraw()) {
                this.mSubLineMainView.setVisibility(0);
            }
        }
    }

    public void switchToSmall() {
        Rect smallRect = this.mPreviewShellModel.getSmallRect();
        setGuidelineBegin(this.mLeftGuideline, smallRect.left);
        setGuidelineBegin(this.mRightGuideline, smallRect.right);
        setGuidelineBegin(this.mBottomGuideline, smallRect.bottom);
        this.mSurfaceContainerCard.setRadius(getResources().getDimension(R.dimen.s_4_dp));
    }

    public Rect getSmallRect() {
        return new Rect(this.mPreviewShellModel.getSmallRect());
    }

    public Observable<Integer> switchToLarge() {
        return Observable.create(new PreviewShell$$Lambda$14(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$switchToLarge$17$PreviewShell(final ObservableEmitter emitter) throws Exception {
        Rect smallRect = this.mPreviewShellModel.getSmallRect();
        float leftStart = (float) smallRect.left;
        float rightStart = (float) smallRect.right;
        float bottomStart = (float) smallRect.bottom;
        Rect largeRect = this.mPreviewShellModel.getLargeRect();
        float leftEnd = (float) largeRect.left;
        float rightEnd = (float) largeRect.right;
        float bottomEnd = (float) largeRect.bottom;
        ValueAnimator leftAnimator = ValueAnimator.ofFloat(leftStart, leftEnd);
        ValueAnimator rightAnimator = ValueAnimator.ofFloat(rightStart, rightEnd);
        ValueAnimator bottomAnimator = ValueAnimator.ofFloat(bottomStart, bottomEnd);
        leftAnimator.addUpdateListener(new PreviewShell$$Lambda$15(this));
        rightAnimator.addUpdateListener(new PreviewShell$$Lambda$16(this));
        bottomAnimator.addUpdateListener(new PreviewShell$$Lambda$17(this));
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            /* class com.dji.component.fpv.widget.preview.PreviewShell.AnonymousClass1 */

            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                emitter.onNext(1);
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                emitter.onNext(2);
                PreviewShell.this.mSurfaceContainerCard.setRadius(0.0f);
            }
        });
        animatorSet.playTogether(leftAnimator, rightAnimator, bottomAnimator);
        animatorSet.start();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$14$PreviewShell(ValueAnimator animation) {
        setGuidelineBegin(this.mLeftGuideline, ((Float) animation.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$15$PreviewShell(ValueAnimator animation) {
        setGuidelineBegin(this.mRightGuideline, ((Float) animation.getAnimatedValue()).intValue());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$16$PreviewShell(ValueAnimator animation) {
        setGuidelineBegin(this.mBottomGuideline, ((Float) animation.getAnimatedValue()).intValue());
    }

    private Rect generateSmallPreviewRect() {
        Rect rect = new Rect(-1, -1, -1, -1);
        rect.left = getResources().getDimensionPixelSize(R.dimen.s_8_dp);
        rect.right = rect.left + getResources().getDimensionPixelSize(R.dimen.s_200_dp);
        rect.bottom = getHeight() - getResources().getDimensionPixelSize(R.dimen.s_5_dp);
        rect.top = rect.bottom - getResources().getDimensionPixelSize(R.dimen.s_120_dp);
        return rect;
    }

    private void handleLayoutChange() {
        this.mPreviewShellModel.handleVideoLayoutChange(new int[]{this.mSurfaceContainerCard.getLeft(), this.mSurfaceContainerCard.getTop(), this.mSurfaceContainerCard.getRight(), this.mSurfaceContainerCard.getBottom()});
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mCompositeDisposable.clear();
        this.mPreviewShellModel.onDestroyView();
        this.mPreviewHelper.onDetachedFromWindow();
        this.mPreviewHelper.stopDecoder();
        this.mPreviewHelper = null;
        this.mSubLineDisposable.clear();
    }

    private void setGuidelineBegin(Guideline guideline, int margin) {
        DJILog.logWriteD(TAG, "setGuidelineBegin: %s, %s", guideline, Integer.valueOf(margin));
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) guideline.getLayoutParams();
        lp.guideBegin = margin;
        lp.guideEnd = -1;
        lp.guidePercent = -1.0f;
        guideline.setLayoutParams(lp);
    }

    public int getShowWidth() {
        ViewGroup vg = (this.mSurfaceView == null || this.mSurfaceView.getParent() == null || this.mSurfaceView.getParent().getParent() == null) ? null : (ViewGroup) this.mSurfaceView.getParent().getParent();
        if (vg == null) {
            return 0;
        }
        return vg.getWidth();
    }

    public int getShowHeight() {
        ViewGroup vg = (this.mSurfaceView == null || this.mSurfaceView.getParent() == null || this.mSurfaceView.getParent().getParent() == null) ? null : (ViewGroup) this.mSurfaceView.getParent().getParent();
        if (vg == null) {
            return 0;
        }
        return vg.getHeight();
    }
}
