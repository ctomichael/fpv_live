package dji.midware.media.newframing;

import android.os.Handler;
import android.os.Message;
import com.dji.video.framing.DJIVideoHEVCFomatManager;
import com.dji.video.framing.VideoLog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetVideoTransferFormat;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIVideoHevcControl {
    private static final int HEVC_SEND_DURATION = 500;
    private static final int HEVC_SEND_RETRY_COUNT = 3;
    private static final int MSG_CHECK_DELAY = 200;
    private static final int MSG_SWITCH_PLAYBACK_MODE = 1;
    private static final String TAG = "DJIVideoHevcControl";
    private Handler.Callback mCallback = new Handler.Callback() {
        /* class dji.midware.media.newframing.DJIVideoHevcControl.AnonymousClass2 */

        public boolean handleMessage(Message msg) {
            boolean z;
            switch (msg.what) {
                case 1:
                    boolean isFpvMode = ((Boolean) msg.obj).booleanValue();
                    VideoLog.w(DJIVideoHevcControl.TAG, "handleMessage() checkPlayBackStatus fpv mode ? " + isFpvMode, new Object[0]);
                    DJIVideoHEVCFomatManager instance = DJIVideoHEVCFomatManager.getInstance();
                    if (!isFpvMode) {
                        z = true;
                    } else {
                        z = false;
                    }
                    instance.setPlaybackStatus(z);
                    DJIVideoHevcControl.this.sendChangeVideoTransferFormatToAir();
                    break;
            }
            return false;
        }
    };
    private Disposable mDisposable;
    private Handler mHandler;
    private HevcDisposeStatusListener mHevcDisposeStatusListener;
    private Subject<Object> mSendSubject = BehaviorSubject.create().toSerialized();

    interface HevcDisposeStatusListener {
        void onHevcControlRelease();
    }

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIVideoHevcControl INSTANCE = new DJIVideoHevcControl();

        private SingletonHolder() {
        }
    }

    public static DJIVideoHevcControl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void initHevcControl() {
        VideoLog.w(TAG, "initHevcControl()", new Object[0]);
        DJIEventBusUtil.register(this);
        if (this.mDisposable == null) {
            this.mDisposable = getSendChangeVideoTransferFormatToAirSingleObservable().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DJIVideoHevcControl$$Lambda$0(this), DJIVideoHevcControl$$Lambda$1.$instance);
        }
        this.mHandler = new Handler(this.mCallback);
    }

    /* access modifiers changed from: private */
    /* renamed from: dealHevcResponse */
    public synchronized void bridge$lambda$0$DJIVideoHevcControl(Object model) {
        if (model instanceof DataBase) {
            byte[] ans = ((DataBase) model).getRecData();
            boolean isSupportHevc = DJIVideoHEVCFomatManager.getInstance().isSupportHevcMode();
            boolean isInPlaybackMode = DJIVideoHEVCFomatManager.getInstance().isPlaybackStatus();
            VideoLog.w(TAG, "dealHevcResponse() success isSupportHevc:" + isSupportHevc + " isInPlaybackMode:" + isInPlaybackMode + " ans:" + Arrays.toString(ans), new Object[0]);
            if (ans != null && ans.length > 1 && (ans[1] & 255) == DataCameraSetVideoTransferFormat.VideoTransferFormat.H265.value() && isSupportHevc && !isInPlaybackMode) {
                DJIVideoHEVCFomatManager.getInstance().setHevcMode(true);
            }
        }
        DJIVideoHEVCFomatManager.getInstance().setHevcMode(false);
    }

    public synchronized void releaseHevcControl() {
        VideoLog.w(TAG, "releaseHevcControl()", new Object[0]);
        DJIEventBusUtil.unRegister(this);
        if (this.mDisposable != null) {
            this.mDisposable.dispose();
            this.mDisposable = null;
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        if (this.mHevcDisposeStatusListener != null) {
            this.mHevcDisposeStatusListener.onHevcControlRelease();
        }
    }

    public void setHevcDisposeStatusListener(HevcDisposeStatusListener hevcDisposeStatusListener) {
        this.mHevcDisposeStatusListener = hevcDisposeStatusListener;
    }

    public void sendChangeVideoTransferFormatToAir() {
        this.mSendSubject.onNext(new Object());
    }

    private Observable<Object> getSendChangeVideoTransferFormatToAirSingleObservable() {
        return this.mSendSubject.throttleFirst(500, TimeUnit.MILLISECONDS).switchMap(new DJIVideoHevcControl$$Lambda$3(Observable.create(new DJIVideoHevcControl$$Lambda$2(this))));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$getSendChangeVideoTransferFormatToAirSingleObservable$1$DJIVideoHevcControl(final ObservableEmitter emitter) throws Exception {
        boolean isSupportHevc = DJIVideoHEVCFomatManager.getInstance().isSupportHevcMode();
        VideoLog.w(TAG, "sendChangeVideoTransferFormatToAirSingle()  isSupportHevc:" + isSupportHevc, new Object[0]);
        DataCameraSetVideoTransferFormat.getInstance().setVideoFormat(isSupportHevc ? DataCameraSetVideoTransferFormat.VideoTransferFormat.H265 : DataCameraSetVideoTransferFormat.VideoTransferFormat.H264).start(new DJIDataCallBack() {
            /* class dji.midware.media.newframing.DJIVideoHevcControl.AnonymousClass1 */

            public void onSuccess(Object model) {
                emitter.onNext(model);
                emitter.onComplete();
            }

            public void onFailure(Ccode ccode) {
                emitter.tryOnError(new Throwable("error code:" + ccode));
            }
        });
    }

    private void checkPlayBackStatus(boolean isFpvMode) {
        if (this.mHandler == null) {
            VideoLog.e(TAG, "checkPlayBackStatus() mHandler is null", new Object[0]);
        } else if (isFpvMode && DJIVideoHEVCFomatManager.getInstance().isPlaybackStatus()) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, true), 200);
        } else if (!isFpvMode && !DJIVideoHEVCFomatManager.getInstance().isPlaybackStatus()) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, false), 200);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo pushStateInfo) {
        if (pushStateInfo != null && pushStateInfo.isGetted()) {
            DataCameraGetMode.MODE mode = pushStateInfo.getMode();
            if (mode == DataCameraGetMode.MODE.TAKEPHOTO || mode == DataCameraGetMode.MODE.RECORD) {
                checkPlayBackStatus(true);
            } else if (mode == DataCameraGetMode.MODE.PLAYBACK) {
                checkPlayBackStatus(false);
            }
        }
    }
}
