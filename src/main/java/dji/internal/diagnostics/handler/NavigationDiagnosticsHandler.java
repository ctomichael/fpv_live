package dji.internal.diagnostics.handler;

import dji.common.flightcontroller.flightassistant.QuickShotException;
import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.internal.diagnostics.handler.util.DiagnosticsMapModel;
import dji.internal.diagnostics.handler.util.DiagnosticsModel;
import dji.internal.diagnostics.handler.util.UpdateInterface;
import dji.internal.logics.CommonUtil;
import dji.midware.data.model.P3.DataEyePushVisionTip;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.thirdparty.io.reactivex.Observable;
import dji.thirdparty.io.reactivex.ObservableEmitter;
import dji.thirdparty.io.reactivex.disposables.CompositeDisposable;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import java.util.HashSet;
import java.util.Set;

public class NavigationDiagnosticsHandler extends DiagnosticsBaseHandler {
    private static final DJISDKCacheKey EXECUTING_KEY = KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_EXECUTING_QUICK_MOVIE);
    private static final DJISDKCacheKey MULTI_EXECUTING_KEY = KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_EXECUTING);
    private static final DJISDKCacheKey MULTI_QUICK_SHOT_ENABLE_KEY = KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED);
    private static final DJISDKCacheKey QUICK_SHOT_EXCEPTION = KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.QUICK_SHOT_EXCEPTION);
    private static final DJIDiagnosticsType TYPE = DJIDiagnosticsType.NAVIGATION;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private DiagnosticsModel<QuickShotException> mQSModel;
    private DiagnosticsModel<Boolean> mQsExecutingModel;
    private DiagnosticsModel<Boolean> mQsStopModel;

    public NavigationDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        initModels();
    }

    private void initModels() {
        DiagnosticsMapModel<QuickShotException> qsMapModel = new DiagnosticsMapModel<>();
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_FINISH), QuickShotException.QM_NORMAL_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_REACH_HEIGHT_LIMIT), QuickShotException.QM_FLYLIMIT_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_REACH_DISTANCE_LIMIT), QuickShotException.QM_DISTANCE_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_TASK_TIMEOUT), QuickShotException.QM_TIMEOUT_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_GPS_SIGNAL_WEAK), QuickShotException.QM_GPS_UNVALID_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_JOYSTICK_INTERRUPT), QuickShotException.QM_RC_INTERRUPT_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_TASK_START_FAIL), QuickShotException.QM_INIT_FAILED_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_CAMERA_MODE_ERROR), QuickShotException.QM_CAMERA_WRONG_COMEBACK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_NOT_TAKEOFF), QuickShotException.QM_NOT_IN_AIR);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_FC_MODE_ERROR), QuickShotException.QM_FC_MODE_ERROR);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_AVOIDANCE), QuickShotException.QM_OBSTACLE_AVOID);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_TASK_PAUSE), QuickShotException.QM_USER_PAUSE);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_GIMBAL_BLOCK), QuickShotException.PANO_SHOT_GIMBAL_STUCK);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_TAKE_CONTROL_FAIL), QuickShotException.PANO_SHOT_FAIL_TAKE_CONTROL);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_STORAGE_NOT_ENOUGH), QuickShotException.PANO_SHOT_STORAGE_NOT_ENOUGH);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_TARGET_LOST), QuickShotException.TRACKIING_SHOT_TARGET_LOST);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_TAKE_PANO_PHOTO_FAIL), QuickShotException.PANO_SHOT_TAKE_PHOTO_FAILED);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_PANO_STITCHING_FAIL), QuickShotException.PANO_SHOT_PHOTO_STITCHING_FAILED);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_CALIBRATION_FAIL), QuickShotException.PANO_SHOT_LOADING_CALIBRATION_FAILED);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_ADJUST_CAMERA_PARAM_FAIL), QuickShotException.PANO_SHOT_ADJUST_CAMERA_PARAMETER_FAILED);
        qsMapModel.put(Integer.valueOf((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_TAKE_PANO_TIMEOUT), QuickShotException.PANO_SHOT_TIME_OUT);
        this.mQSModel = qsMapModel.autoDisappear(this).keepForAWhile(this);
        Boolean bool = Boolean.TRUE;
        bool.getClass();
        this.mQsExecutingModel = new DiagnosticsIfModel((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_START, NavigationDiagnosticsHandler$$Lambda$0.get$Lambda(bool)).autoDisappear(this).keepForAWhile(this);
        this.mQsStopModel = new DiagnosticsIfModel((int) DJIDiagnosticsError.Navigation.QUICK_SHOT_STOP, new NavigationDiagnosticsHandler$$Lambda$1(this)).autoDisappear(this).keepForAWhile(this);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initModels$0$NavigationDiagnosticsHandler(Boolean isExecuting, Boolean lastIsExecuting) {
        if (isSupportTargetFix() || !((Boolean) CacheHelper.getValue(MULTI_QUICK_SHOT_ENABLE_KEY, false)).booleanValue() || !Boolean.TRUE.equals(lastIsExecuting) || Boolean.TRUE.equals(isExecuting) || DataEyePushVisionTip.getInstance().getQuickMovieProgress() >= 99 || DataEyePushVisionTip.getInstance().getQuickMovieProgress() <= 0) {
            return false;
        }
        return true;
    }

    private boolean isSupportTargetFix() {
        return CommonUtil.isWM240(null) || CommonUtil.isWM160(null);
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        this.mCompositeDisposable.add(observeSharedLibKey(QUICK_SHOT_EXCEPTION, this.mQSModel, this));
        this.mCompositeDisposable.add(observeSharedLibKey(EXECUTING_KEY, this.mQsExecutingModel, this));
        this.mCompositeDisposable.add(observeSharedLibKey(EXECUTING_KEY, this.mQsStopModel, this));
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
        this.mCompositeDisposable.clear();
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        DiagnosticsModel<QuickShotException> diagnosticsModel = this.mQSModel;
        DJIDiagnosticsType dJIDiagnosticsType = TYPE;
        diagnosisList.getClass();
        diagnosticsModel.doIfError(dJIDiagnosticsType, NavigationDiagnosticsHandler$$Lambda$2.get$Lambda(diagnosisList));
        DiagnosticsModel<Boolean> diagnosticsModel2 = this.mQsExecutingModel;
        DJIDiagnosticsType dJIDiagnosticsType2 = TYPE;
        diagnosisList.getClass();
        diagnosticsModel2.doIfError(dJIDiagnosticsType2, NavigationDiagnosticsHandler$$Lambda$3.get$Lambda(diagnosisList));
        DiagnosticsModel<Boolean> diagnosticsModel3 = this.mQsStopModel;
        DJIDiagnosticsType dJIDiagnosticsType3 = TYPE;
        diagnosisList.getClass();
        diagnosticsModel3.doIfError(dJIDiagnosticsType3, NavigationDiagnosticsHandler$$Lambda$4.get$Lambda(diagnosisList));
        return diagnosisList;
    }

    public void reset(int componentIndex) {
        this.mQSModel.reset();
        this.mQsExecutingModel.reset();
        this.mQsStopModel.reset();
    }

    private <T> Disposable observeSharedLibKey(DJISDKCacheKey key, DiagnosticsModel<T> model, UpdateInterface updater) {
        return Observable.create(new NavigationDiagnosticsHandler$$Lambda$5(key)).map(new NavigationDiagnosticsHandler$$Lambda$6(model)).subscribe(new NavigationDiagnosticsHandler$$Lambda$7(updater));
    }

    static final /* synthetic */ void lambda$observeSharedLibKey$3$NavigationDiagnosticsHandler(DJISDKCacheKey key, ObservableEmitter emitter) throws Exception {
        DJIParamAccessListener listener = new NavigationDiagnosticsHandler$$Lambda$8(emitter, key);
        CacheHelper.addListener(listener, key);
        emitter.setCancellable(new NavigationDiagnosticsHandler$$Lambda$9(listener));
    }
}
