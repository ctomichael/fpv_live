package com.dji.component.fpv.widget.preview;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.dji.component.fpv.base.AbstractViewModel;
import com.dji.component.fpv.base.BulletinBoardEvent;
import com.dji.component.fpv.base.BulletinBoardKey;
import com.dji.component.fpv.base.BulletinBoardProvider;
import com.dji.rx.sharedlib.SharedLibPushObservable;
import dji.common.camera.CameraRecordingState;
import dji.midware.util.AutoVideoSizeCalculator;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class PreviewShellModel extends AbstractViewModel {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private PublishSubject<String> mDimensionRatioSubject = PublishSubject.create();
    private Rect mLargeRect = new Rect(-1, -1, -1, -1);
    private PublishSubject<Rect> mLargeRectSubject = PublishSubject.create();
    private SharedLibPushObservable<Boolean> mObservableConnection;
    private SharedLibPushObservable<Boolean> mObservableIsShootingPhoto;
    private SharedLibPushObservable<CameraRecordingState> mObservableRecordingState;
    private PreviewHelper mPreviewHelper;
    private BehaviorSubject<String> mPreviewStateSubject = BehaviorSubject.createDefault(BulletinBoardKey.Preview.State.LARGE);
    private Rect mSmallOriginRect = new Rect(-1, -1, -1, -1);
    private Rect mSmallRect = new Rect(-1, -1, -1, -1);
    private PublishSubject<Rect> mSmallRectSubject = PublishSubject.create();

    PreviewShellModel(BulletinBoardProvider bridge) {
        super(bridge);
    }

    public void onCreateView() {
        this.mCompositeDisposable.add(getBulletinBoard().getObservable(BulletinBoardKey.Preview.STATE).subscribe(new PreviewShellModel$$Lambda$0(this)));
        getBulletinBoard().putString(BulletinBoardKey.Preview.STATE, getBulletinBoard().getString(BulletinBoardKey.Preview.STATE, BulletinBoardKey.Preview.State.LARGE));
        this.mCompositeDisposable.add(this.mPreviewHelper.liveviewSizeObservable().subscribe(new PreviewShellModel$$Lambda$1(this)));
        this.mObservableConnection = new SharedLibPushObservable<>(KeyHelper.getCameraKey(0, DJISDKCacheKeys.CONNECTION), false);
        this.mObservableIsShootingPhoto = new SharedLibPushObservable<>(KeyHelper.getCameraKey(0, CameraKeys.IS_SHOOTING_PHOTO), false);
        this.mObservableRecordingState = new SharedLibPushObservable<>(KeyHelper.getCameraKey(0, CameraKeys.RECORDING_STATE), CameraRecordingState.UNKNOWN);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onCreateView$0$PreviewShellModel(Object s) throws Exception {
        this.mPreviewStateSubject.onNext((String) s);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onCreateView$1$PreviewShellModel(Rect r) throws Exception {
        AutoVideoSizeCalculator.Options.VideoType videoType = detectVideoTypeByVideoSize(r);
        this.mDimensionRatioSubject.onNext(detectDimensionRatioByVideoType(videoType));
        updateLargeRect(r, videoType);
        updateSmallRect(r, videoType);
        if (isLargePreview()) {
            this.mLargeRectSubject.onNext(this.mLargeRect);
        } else if (isSmallPreview()) {
            this.mSmallRectSubject.onNext(this.mSmallRect);
        }
    }

    public void onDestroyView() {
        this.mCompositeDisposable.clear();
    }

    /* access modifiers changed from: package-private */
    public void performClick() {
        if (getBulletinBoard().getString(BulletinBoardKey.Preview.STATE).equals(BulletinBoardKey.Preview.State.SMALL)) {
            getBulletinBoard().putParcelable(BulletinBoardKey.MapSwitch.ACTION_MAP_PREVIEW_SWITCH, BulletinBoardEvent.CLICK_EVENT);
        }
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Observable<Boolean> getConnectionObservable() {
        return this.mObservableConnection.map(PreviewShellModel$$Lambda$2.$instance);
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Observable<Boolean> getIsShootingPhotoObservable() {
        return this.mObservableIsShootingPhoto.map(PreviewShellModel$$Lambda$3.$instance);
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public Observable<CameraRecordingState> getCameraRecordingState() {
        return this.mObservableRecordingState.map(PreviewShellModel$$Lambda$4.$instance);
    }

    /* access modifiers changed from: package-private */
    public Observable<String> getDimensionRatioObservable() {
        return this.mDimensionRatioSubject.hide();
    }

    /* access modifiers changed from: package-private */
    public Observable<Rect> getSmallRectObservable() {
        return this.mSmallRectSubject.hide();
    }

    /* access modifiers changed from: package-private */
    public Observable<Rect> getLargeRectObservable() {
        return this.mLargeRectSubject.hide();
    }

    /* access modifiers changed from: package-private */
    public Rect getSmallRect() {
        return this.mSmallRect;
    }

    /* access modifiers changed from: package-private */
    public Rect getLargeRect() {
        return this.mLargeRect;
    }

    /* access modifiers changed from: package-private */
    public boolean isSmallPreview() {
        return getBulletinBoard().getString(BulletinBoardKey.Preview.STATE).equals(BulletinBoardKey.Preview.State.SMALL);
    }

    /* access modifiers changed from: package-private */
    public boolean isLargePreview() {
        return getBulletinBoard().getString(BulletinBoardKey.Preview.STATE).equals(BulletinBoardKey.Preview.State.LARGE);
    }

    /* access modifiers changed from: package-private */
    public Observable<String> getPreviewAdjustSizeObservable() {
        return this.mPreviewStateSubject.hide();
    }

    /* access modifiers changed from: package-private */
    public int[] getScreenSize() {
        return getBulletinBoard().getIntArray(BulletinBoardKey.Fpv.SCREEN_SIZE);
    }

    /* access modifiers changed from: package-private */
    public void handleVideoLayoutChange(int[] rc) {
        getBulletinBoard().putIntArray(BulletinBoardKey.Preview.POSITION_VIDEO, rc);
    }

    /* access modifiers changed from: package-private */
    public void handleScreenBitmapReady(@Nullable Bitmap bitmap) {
        if (bitmap != null) {
            getBulletinBoard().putParcelable(BulletinBoardKey.Preview.EVENT_FRAME_BITMAP_READY, bitmap);
        }
    }

    /* access modifiers changed from: package-private */
    public void initSmallPreview(Rect smallPreviewRect) {
        this.mSmallRect.set(smallPreviewRect);
        this.mSmallOriginRect.set(smallPreviewRect);
    }

    /* access modifiers changed from: package-private */
    public void onCreatePreviewHelper(PreviewHelper previewHelper) {
        this.mPreviewHelper = previewHelper;
    }

    private String detectDimensionRatioByVideoType(AutoVideoSizeCalculator.Options.VideoType videoType) {
        switch (videoType) {
            case Ratio4X3:
                return "4:3";
            case Ratio3X2:
                return "3:2";
            case Ratio5x4:
                return "5:4";
            default:
                return "16:9";
        }
    }

    private AutoVideoSizeCalculator.Options.VideoType detectVideoTypeByVideoSize(Rect r) {
        return AutoVideoSizeCalculator.findMathRatioType((1.0f * ((float) r.width())) / ((float) r.height()));
    }

    private void updateLargeRect(Rect r, AutoVideoSizeCalculator.Options.VideoType videoType) {
        int screenWidth = getScreenSize()[0];
        int screenHeight = getScreenSize()[1];
        int left = (screenWidth - r.width()) / 2;
        int right = left + r.width();
        int top = (screenHeight - r.height()) / 2;
        this.mLargeRect.set(left, top, right, top + r.height());
    }

    private void updateSmallRect(Rect r, AutoVideoSizeCalculator.Options.VideoType videoType) {
        switch (videoType) {
            case Ratio4X3:
                this.mSmallRect.right = this.mSmallRect.left + ((this.mSmallOriginRect.height() / 3) * 4);
                return;
            case Ratio3X2:
                this.mSmallRect.right = this.mSmallRect.left + ((this.mSmallOriginRect.height() / 2) * 3);
                return;
            case Ratio5x4:
                this.mSmallRect.right = this.mSmallRect.left + ((this.mSmallOriginRect.height() / 4) * 5);
                return;
            case Ratio16X9:
                this.mSmallRect.right = this.mSmallRect.left + ((this.mSmallOriginRect.height() / 9) * 16);
                return;
            default:
                return;
        }
    }
}
