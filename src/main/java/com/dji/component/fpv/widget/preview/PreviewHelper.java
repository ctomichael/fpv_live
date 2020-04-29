package com.dji.component.fpv.widget.preview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import com.dji.component.fpv.widget.preview.VideoSurfaceView;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import com.dji.video.framing.internal.decoder.decoderinterface.IReceiveDataCallback;
import com.dji.video.framing.internal.opengl.surface.SurfaceInterface;
import dji.log.DJILog;
import dji.midware.natives.FPVController;
import dji.midware.util.AutoVideoSizeCalculator;
import dji.midware.util.DjiSharedPreferencesManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.PublishSubject;

public class PreviewHelper implements VideoSurfaceView.DJIGLSurfaceViewListener {
    private static final String TAG = "PreviewHelper";
    private AutoVideoSizeCalculator mCalculator;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Context mContext;
    private IReceiveDataCallback mDecoderCallback;
    private PublishSubject<Rect> mLiveviewSubject = PublishSubject.create();
    /* access modifiers changed from: private */
    public PublishSubject<Boolean> mResetSignalSubject = PublishSubject.create();
    private SizeProvider mSizeProvider;
    private DJIVideoDecoderController mVideoDecoder;
    private VideoSurfaceView mVideoSurfaceView;

    public interface SizeProvider {
        int getShowHeight();

        int getShowWidth();
    }

    public PreviewHelper(Context context, VideoSurfaceView view, @NonNull SizeProvider sizeProvider) {
        init(context, view, sizeProvider);
    }

    public Observable<Rect> liveviewSizeObservable() {
        return this.mLiveviewSubject.hide();
    }

    public void onRenderCreatedPrepared() {
        notifyCalculator();
    }

    public DJIVideoDecoderController onRenderCreated(SurfaceInterface surface) {
        if (this.mVideoDecoder == null) {
            initDecoder(surface);
        } else {
            this.mVideoDecoder.setSurface(surface);
            this.mVideoDecoder.setRecvDataCallBack(this.mDecoderCallback);
        }
        return this.mVideoDecoder;
    }

    public void onRenderDestroy() {
        if (this.mVideoDecoder != null) {
            this.mVideoDecoder.setSurface(null);
            this.mVideoDecoder.setRecvDataCallBack(null);
        }
    }

    public void onDetachedFromWindow() {
        DJILog.logWriteD(TAG, "onDetachedFromWindow", new Object[0]);
        this.mVideoSurfaceView.setRenderListener(null);
        this.mCompositeDisposable.clear();
        this.mContext = null;
        this.mVideoSurfaceView = null;
        if (this.mCalculator != null) {
            this.mCalculator.setListener(null);
        }
        this.mCalculator = null;
        this.mDecoderCallback = null;
    }

    private void init(Context context, VideoSurfaceView view, SizeProvider sizeProvider) {
        DJILog.logWriteD(TAG, "init", new Object[0]);
        this.mContext = context;
        this.mVideoSurfaceView = view;
        this.mSizeProvider = sizeProvider;
        this.mCalculator = new AutoVideoSizeCalculator();
        this.mCalculator.setListener(new PreviewHelper$$Lambda$0(this));
        this.mDecoderCallback = new IReceiveDataCallback() {
            /* class com.dji.component.fpv.widget.preview.PreviewHelper.AnonymousClass1 */

            public void oneFrameComeIn() {
            }

            public void resetVideoSurface(int videoWidth, int videoHeight) {
                DJILog.logWriteD(PreviewHelper.TAG, "resetVideoSurface, vWidth = " + videoWidth + ", vHeight = " + videoHeight, new Object[0]);
                PreviewHelper.this.mResetSignalSubject.onNext(true);
            }
        };
        this.mVideoSurfaceView.setRenderer();
        this.mVideoSurfaceView.setRenderListener(this);
        this.mCompositeDisposable.add(this.mResetSignalSubject.doOnEach(PreviewHelper$$Lambda$1.$instance).observeOn(AndroidSchedulers.mainThread()).subscribe(new PreviewHelper$$Lambda$2(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$init$0$PreviewHelper(int width, int height, int realWidth, int realHeight) {
        DJILog.logWriteD(TAG, "onSizeChange: width = " + width + "height = " + height + ", realWidth = " + realWidth + ", realHeight = " + realHeight, new Object[0]);
        this.mLiveviewSubject.onNext(new Rect(0, 0, realWidth, realHeight));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$init$2$PreviewHelper(Boolean o) throws Exception {
        notifyCalculator();
    }

    private void initDecoder(SurfaceInterface renderManager) {
        this.mVideoDecoder = new DJIVideoDecoderController(this.mContext, renderManager);
        this.mVideoDecoder.setRecvDataCallBack(this.mDecoderCallback);
        FPVController.native_setDecodeMode(DjiSharedPreferencesManager.getBoolean(this.mContext, "DecodeMode", false));
    }

    private int getShowWidth() {
        return this.mSizeProvider.getShowWidth();
    }

    private int getShowHeight() {
        return this.mSizeProvider.getShowHeight();
    }

    private void notifyCalculator() {
        DJILog.logWriteD(TAG, "notifyCalculator", new Object[0]);
        AutoVideoSizeCalculator.Options options = this.mCalculator.getOptions();
        DJIVideoDecoder decoder = this.mVideoDecoder == null ? null : this.mVideoDecoder.getDecoder();
        options.setVideoTypeByVideoSize(decoder == null ? 16 : decoder.width, decoder == null ? 9 : decoder.height, 0);
        options.setIsRotated(false);
        int width = getShowWidth();
        int height = getShowHeight();
        if (width < height) {
            int tmp = width;
            width = height;
            height = tmp;
        }
        options.setScreenTypeByScreenSize(width, height);
        DJILog.logWriteD(TAG, "ScreenSize[%s, %s], CalculatorOptions[%s, %s]", Integer.valueOf(width), Integer.valueOf(height), options.getScreenType(), options.getVideoType());
        this.mCalculator.notifyCalc();
    }

    public void stopDecoder() {
        if (this.mVideoDecoder != null) {
            this.mVideoDecoder.stopVideoDecoder();
            this.mVideoDecoder.destroy();
            this.mVideoDecoder = null;
        }
    }
}
