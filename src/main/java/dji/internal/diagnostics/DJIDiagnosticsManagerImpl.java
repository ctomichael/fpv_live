package dji.internal.diagnostics;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import dji.component.accountcenter.IMemberProtocol;
import dji.diagnostics.DJIDiagnosticsManager;
import dji.diagnostics.DiagnosticsInformationListener;
import dji.diagnostics.model.DJIDiagnostics;
import dji.internal.diagnostics.handler.Air1860DiagnosticsHandler;
import dji.internal.diagnostics.handler.BatteryDiagnosticsHandler;
import dji.internal.diagnostics.handler.CameraDiagnosticsHandler;
import dji.internal.diagnostics.handler.CenterBoardDiagnosticsHandler;
import dji.internal.diagnostics.handler.FlightControllerDiagnosticsHandler;
import dji.internal.diagnostics.handler.GimbalDiagnosticsHandler;
import dji.internal.diagnostics.handler.LightbridgeDiagnosticsHandler;
import dji.internal.diagnostics.handler.NavigationDiagnosticsHandler;
import dji.internal.diagnostics.handler.ProductDiagnosticsHandler;
import dji.internal.diagnostics.handler.RemoteControllerDiagnosticsHandler;
import dji.internal.diagnostics.handler.VisionDiagnosticsHandler;
import dji.internal.diagnostics.handler.util.DiagnosticsLog;
import dji.midware.util.BackgroundLooper;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.schedulers.Schedulers;
import dji.thirdparty.io.reactivex.subjects.PublishSubject;
import dji.thirdparty.io.reactivex.subjects.Subject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class DJIDiagnosticsManagerImpl extends DJIDiagnosticsManager implements DJIParamAccessListener {
    private static final String TAG = "diagnostics";
    private final Object EMPTY;
    private CopyOnWriteArrayList<DiagnosticsBaseHandler> diagnosticsBaseHandlers;
    private DiagnosticsHandlerUpdateObserver diagnosticsHandlerUpdateObserver;
    private long lastNoticeTime;
    private Air1860DiagnosticsHandler mAirLinkHandler;
    private boolean mBatteryConnect;
    private BatteryDiagnosticsHandler mBatteryHandler;
    private DJISDKCacheKey mBatteryKey;
    private CameraDiagnosticsHandler mCamera0Handler;
    private DJISDKCacheKey mCamera0Key;
    private CameraDiagnosticsHandler mCamera1Handler;
    private DJISDKCacheKey mCamera1Key;
    private CameraDiagnosticsHandler mCamera2Handler;
    private DJISDKCacheKey mCamera2Key;
    private boolean[] mCameraConnect;
    private CenterBoardDiagnosticsHandler mCenterBoardHandler;
    private Subject<Object> mConnectionSubject;
    private FlightControllerDiagnosticsHandler mFcHandler;
    private boolean mFlightControllerConnect;
    private DJISDKCacheKey mFlightControllerKey;
    private GimbalDiagnosticsHandler mGimbal0Handler;
    private DJISDKCacheKey mGimbal0Key;
    private GimbalDiagnosticsHandler mGimbal1Handler;
    private DJISDKCacheKey mGimbal1Key;
    private boolean[] mGimbalConnect;
    private Set<DJIDiagnostics> mLastDiagnostics;
    private LightbridgeDiagnosticsHandler mLightbridgeHandler;
    private CopyOnWriteArrayList<DiagnosticsInformationListener> mListeners;
    private NavigationDiagnosticsHandler mNavigationHandler;
    private boolean mRemoteControllerConnect;
    private RemoteControllerDiagnosticsHandler mRemoteControllerHandler;
    private DJISDKCacheKey mRemoteControllerKey;
    private Disposable mRemoveHandlerSubscribe;
    private Handler mUIHandler;
    private VisionDiagnosticsHandler mVisionHandler;
    private int times;

    private DJIDiagnosticsManagerImpl() {
        this.diagnosticsBaseHandlers = new CopyOnWriteArrayList<>();
        this.mUIHandler = new Handler(Looper.getMainLooper());
        this.mListeners = new CopyOnWriteArrayList<>();
        this.mFlightControllerConnect = false;
        this.mRemoteControllerConnect = false;
        this.mBatteryConnect = false;
        this.mCameraConnect = new boolean[]{false, false, false};
        this.mGimbalConnect = new boolean[]{false, false};
        this.mConnectionSubject = PublishSubject.create();
        this.EMPTY = new Object();
        this.mLastDiagnostics = new HashSet();
        this.lastNoticeTime = 0;
        this.times = 0;
        init();
    }

    private static class InstanceHolder {
        /* access modifiers changed from: private */
        public static final DJIDiagnosticsManagerImpl INSTANCE = new DJIDiagnosticsManagerImpl();

        private InstanceHolder() {
        }
    }

    public static DJIDiagnosticsManagerImpl getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private void init() {
        this.diagnosticsHandlerUpdateObserver = new DJIDiagnosticsManagerImpl$$Lambda$0(this);
        this.mConnectionSubject.debounce(100, TimeUnit.MILLISECONDS).subscribe(new DJIDiagnosticsManagerImpl$$Lambda$1(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$init$1$DJIDiagnosticsManagerImpl(Object noCare) throws Exception {
        BackgroundLooper.post(new DJIDiagnosticsManagerImpl$$Lambda$6(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onConnectionChanged */
    public void bridge$lambda$0$DJIDiagnosticsManagerImpl() {
        boolean curFc = ((Boolean) CacheHelper.getValue(this.mFlightControllerKey, false)).booleanValue();
        boolean curRc = ((Boolean) CacheHelper.getValue(this.mRemoteControllerKey, false)).booleanValue();
        boolean curBattery = ((Boolean) CacheHelper.getValue(this.mBatteryKey, false)).booleanValue();
        boolean curCamera0 = ((Boolean) CacheHelper.getValue(this.mCamera0Key, false)).booleanValue();
        boolean curCamera1 = ((Boolean) CacheHelper.getValue(this.mCamera1Key, false)).booleanValue();
        boolean curCamera2 = ((Boolean) CacheHelper.getValue(this.mCamera2Key, false)).booleanValue();
        boolean curGimbal0 = ((Boolean) CacheHelper.getValue(this.mGimbal0Key, false)).booleanValue();
        boolean curGimbal1 = ((Boolean) CacheHelper.getValue(this.mGimbal1Key, false)).booleanValue();
        boolean changed = false;
        if (this.mFlightControllerConnect && !curFc) {
            this.mFcHandler.reset(0);
            this.mCenterBoardHandler.reset(0);
            this.mVisionHandler.reset(0);
            this.mNavigationHandler.reset(0);
            this.mAirLinkHandler.reset(0);
            changed = true;
        }
        if (this.mRemoteControllerConnect && !curRc) {
            this.mRemoteControllerHandler.reset(0);
            this.mLightbridgeHandler.reset(0);
            changed = true;
        }
        if (this.mBatteryConnect && !curBattery) {
            this.mBatteryHandler.reset(0);
            changed = true;
        }
        if (this.mCameraConnect[0] && !curCamera0) {
            this.mCamera0Handler.reset(0);
            changed = true;
        }
        if (this.mCameraConnect[1] && !curCamera1) {
            this.mCamera1Handler.reset(1);
            changed = true;
        }
        if (this.mCameraConnect[2] && !curCamera2) {
            this.mCamera2Handler.reset(2);
            changed = true;
        }
        if (this.mGimbalConnect[0] && !curGimbal0) {
            this.mGimbal0Handler.reset(0);
            changed = true;
        }
        if (this.mGimbalConnect[1] && !curGimbal1) {
            this.mGimbal1Handler.reset(1);
            changed = true;
        }
        this.mFlightControllerConnect = curFc;
        this.mRemoteControllerConnect = curRc;
        this.mBatteryConnect = curBattery;
        this.mCameraConnect[0] = curCamera0;
        this.mCameraConnect[1] = curCamera1;
        this.mCameraConnect[2] = curCamera2;
        this.mGimbalConnect[0] = curGimbal0;
        this.mGimbalConnect[1] = curGimbal1;
        if (changed) {
            lambda$init$0$DJIDiagnosticsManagerImpl();
        }
    }

    private void destroy() {
        if (this.mRemoveHandlerSubscribe != null) {
            this.mRemoveHandlerSubscribe.dispose();
        }
        unregisterAllHandlers();
        this.diagnosticsBaseHandlers.clear();
        this.diagnosticsHandlerUpdateObserver = null;
        removeAllListeners();
    }

    public void addDiagnosticsInformationListener(@NonNull DiagnosticsInformationListener listener) {
        DiagnosticsLog.logi(TAG, "addDiagnosticsInformationListener() called," + listener);
        this.mListeners.add(listener);
        if (this.mListeners.size() < 1 || !this.diagnosticsBaseHandlers.isEmpty()) {
            this.mUIHandler.post(new DJIDiagnosticsManagerImpl$$Lambda$2(this, listener));
        } else {
            initHandlers();
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$addDiagnosticsInformationListener$2$DJIDiagnosticsManagerImpl(@NonNull DiagnosticsInformationListener listener) {
        listener.onUpdate(new ArrayList(this.mLastDiagnostics));
    }

    public void removeDiagnosticsInformationListener(DiagnosticsInformationListener listener) {
        DiagnosticsLog.logi(TAG, "removeDiagnosticsInformationListener() called," + listener);
        this.mListeners.remove(listener);
        if (this.mListeners.isEmpty() && !this.diagnosticsBaseHandlers.isEmpty()) {
            delayRemoveHandler();
        }
    }

    public List<DJIDiagnostics> getCurrentDiagnostics() {
        return new ArrayList(this.mLastDiagnostics);
    }

    public boolean hasDJIDiagnosticCode(int code) {
        Iterator it2 = new ArrayList(this.mLastDiagnostics).iterator();
        while (it2.hasNext()) {
            if (((DJIDiagnostics) it2.next()).getCode() == code) {
                return true;
            }
        }
        return false;
    }

    private void delayRemoveHandler() {
        if (this.mRemoveHandlerSubscribe != null) {
            this.mRemoveHandlerSubscribe.dispose();
        }
        this.mRemoveHandlerSubscribe = Observable.timer(5, TimeUnit.SECONDS).filter(new DJIDiagnosticsManagerImpl$$Lambda$3(this)).observeOn(Schedulers.io()).subscribe(new DJIDiagnosticsManagerImpl$$Lambda$4(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$delayRemoveHandler$3$DJIDiagnosticsManagerImpl(Long noCare) throws Exception {
        return this.mListeners.isEmpty() && !this.diagnosticsBaseHandlers.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$delayRemoveHandler$4$DJIDiagnosticsManagerImpl(Long noCare) throws Exception {
        unregisterAllHandlers();
        this.diagnosticsBaseHandlers.clear();
    }

    private void initHandlers() {
        this.diagnosticsBaseHandlers.clear();
        this.mLastDiagnostics.clear();
        this.mCenterBoardHandler = new CenterBoardDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mVisionHandler = new VisionDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mFcHandler = new FlightControllerDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mAirLinkHandler = new Air1860DiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mBatteryHandler = new BatteryDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mRemoteControllerHandler = new RemoteControllerDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mGimbal0Handler = new GimbalDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mGimbal1Handler = new GimbalDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver, 1);
        this.mCamera0Handler = new CameraDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mCamera1Handler = new CameraDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver, 1);
        this.mCamera2Handler = new CameraDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver, 2);
        this.mLightbridgeHandler = new LightbridgeDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.mNavigationHandler = new NavigationDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver);
        this.diagnosticsBaseHandlers.add(new ProductDiagnosticsHandler(this.diagnosticsHandlerUpdateObserver));
        this.diagnosticsBaseHandlers.add(this.mAirLinkHandler);
        this.diagnosticsBaseHandlers.add(this.mBatteryHandler);
        this.diagnosticsBaseHandlers.add(this.mCenterBoardHandler);
        this.diagnosticsBaseHandlers.add(this.mLightbridgeHandler);
        this.diagnosticsBaseHandlers.add(this.mNavigationHandler);
        this.diagnosticsBaseHandlers.add(this.mRemoteControllerHandler);
        this.diagnosticsBaseHandlers.add(this.mVisionHandler);
        this.diagnosticsBaseHandlers.add(this.mFcHandler);
        this.diagnosticsBaseHandlers.add(this.mCamera0Handler);
        this.diagnosticsBaseHandlers.add(this.mCamera1Handler);
        this.diagnosticsBaseHandlers.add(this.mCamera2Handler);
        this.diagnosticsBaseHandlers.add(this.mGimbal0Handler);
        this.diagnosticsBaseHandlers.add(this.mGimbal1Handler);
        this.mFlightControllerKey = KeyHelper.getFlightControllerKey(DJISDKCacheKeys.CONNECTION);
        this.mRemoteControllerKey = KeyHelper.getRemoteControllerKey(DJISDKCacheKeys.CONNECTION);
        this.mBatteryKey = KeyHelper.getBatteryKey(DJISDKCacheKeys.CONNECTION);
        this.mGimbal0Key = KeyHelper.getGimbalKey(0, DJISDKCacheKeys.CONNECTION);
        this.mGimbal1Key = KeyHelper.getGimbalKey(1, DJISDKCacheKeys.CONNECTION);
        this.mCamera0Key = KeyHelper.getCameraKey(0, DJISDKCacheKeys.CONNECTION);
        this.mCamera1Key = KeyHelper.getCameraKey(1, DJISDKCacheKeys.CONNECTION);
        this.mCamera2Key = KeyHelper.getCameraKey(2, DJISDKCacheKeys.CONNECTION);
        CacheHelper.addListener(this, this.mFlightControllerKey, this.mRemoteControllerKey, this.mBatteryKey, this.mGimbal0Key, this.mGimbal1Key, this.mCamera0Key, this.mCamera1Key, this.mCamera2Key);
        initConnectValue();
        registerAllHandlers();
    }

    private void initConnectValue() {
        this.mFlightControllerConnect = ((Boolean) CacheHelper.getValue(this.mFlightControllerKey, false)).booleanValue();
        this.mRemoteControllerConnect = ((Boolean) CacheHelper.getValue(this.mRemoteControllerKey, false)).booleanValue();
        this.mBatteryConnect = ((Boolean) CacheHelper.getValue(this.mBatteryKey, false)).booleanValue();
        this.mCameraConnect[0] = ((Boolean) CacheHelper.getValue(this.mCamera0Key, false)).booleanValue();
        this.mCameraConnect[1] = ((Boolean) CacheHelper.getValue(this.mCamera1Key, false)).booleanValue();
        this.mCameraConnect[2] = ((Boolean) CacheHelper.getValue(this.mCamera2Key, false)).booleanValue();
        this.mGimbalConnect[0] = ((Boolean) CacheHelper.getValue(this.mGimbal0Key, false)).booleanValue();
        this.mGimbalConnect[1] = ((Boolean) CacheHelper.getValue(this.mGimbal1Key, false)).booleanValue();
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        this.mConnectionSubject.onNext(this.EMPTY);
    }

    private void registerAllHandlers() {
        DiagnosticsLog.logi(TAG, "registerAllHandlers() called");
        if (this.diagnosticsBaseHandlers != null) {
            Iterator<DiagnosticsBaseHandler> it2 = this.diagnosticsBaseHandlers.iterator();
            while (it2.hasNext()) {
                it2.next().register();
            }
        }
    }

    private void unregisterAllHandlers() {
        DiagnosticsLog.logi(TAG, "unregisterAllHandlers() called");
        if (this.diagnosticsBaseHandlers != null) {
            Iterator<DiagnosticsBaseHandler> it2 = this.diagnosticsBaseHandlers.iterator();
            while (it2.hasNext()) {
                it2.next().unregister();
            }
        }
    }

    private void removeAllListeners() {
        this.mListeners.clear();
    }

    /* access modifiers changed from: private */
    /* renamed from: onDiagnosticsChange */
    public void lambda$init$0$DJIDiagnosticsManagerImpl() {
        Set<DJIDiagnostics> allDianosisSet = gatherDJIDiagnostics();
        if (Objects.equals(this.mLastDiagnostics, allDianosisSet)) {
            DiagnosticsLog.logi(TAG, "diagnostics无变化");
            return;
        }
        this.mLastDiagnostics = allDianosisSet;
        List<DJIDiagnostics> allDianosis = new ArrayList<>(allDianosisSet);
        StringBuilder logmsg = new StringBuilder();
        for (DJIDiagnostics allDianosi : allDianosis) {
            logmsg.append(IMemberProtocol.STRING_SEPERATOR_LEFT).append(allDianosi.getCode()).append(allDianosi.getComponentIndex() == 0 ? "" : ", " + allDianosi.getComponentIndex()).append(allDianosi.getExtra() == null ? "" : ", " + allDianosi.getExtra()).append("]; ");
        }
        DiagnosticsLog.logi(TAG, logmsg.toString());
        Iterator<DiagnosticsInformationListener> it2 = this.mListeners.iterator();
        while (it2.hasNext()) {
            this.mUIHandler.post(new DJIDiagnosticsManagerImpl$$Lambda$5(it2.next(), allDianosis));
        }
    }

    private void debugFrequency() {
        long noticeTime = System.currentTimeMillis();
        if (noticeTime - this.lastNoticeTime < 1000) {
            this.times++;
            if (this.times > 10) {
                DiagnosticsLog.loge(TAG, "notify to busy! ", new Throwable("log"));
                this.times = 0;
            }
        } else {
            this.times = 0;
        }
        this.lastNoticeTime = noticeTime;
    }

    @NonNull
    private Set<DJIDiagnostics> gatherDJIDiagnostics() {
        Set<DJIDiagnostics> diagnosticsFromHandler;
        Set<DJIDiagnostics> allDianosis = new HashSet<>();
        Iterator<DiagnosticsBaseHandler> it2 = this.diagnosticsBaseHandlers.iterator();
        while (it2.hasNext()) {
            DiagnosticsBaseHandler handler = it2.next();
            if (!(handler == null || (diagnosticsFromHandler = handler.getDiagnosisList()) == null || diagnosticsFromHandler.isEmpty())) {
                allDianosis.addAll(diagnosticsFromHandler);
            }
        }
        return allDianosis;
    }
}
