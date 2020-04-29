package dji.internal.diagnostics.handler;

import android.content.Context;
import android.os.Looper;
import dji.common.product.Model;
import dji.common.util.LocationUtils;
import dji.component.accountcenter.IMemberProtocol;
import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.extramodel.ExtraHeightRadiusLimit;
import dji.internal.diagnostics.handler.redundancy.RedundancyErrorInfo;
import dji.internal.diagnostics.handler.redundancy.RedundancySystemAdapter;
import dji.internal.diagnostics.handler.takeofffail.TakeoffFailErrorAdapter;
import dji.internal.diagnostics.handler.util.DiagnosticsFlyAttiModel;
import dji.internal.diagnostics.handler.util.DiagnosticsFlycStateFcModel;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.internal.diagnostics.handler.util.DiagnosticsImuInitFailModel;
import dji.internal.diagnostics.handler.util.DiagnosticsLog;
import dji.internal.diagnostics.handler.util.DiagnosticsMapModel;
import dji.internal.diagnostics.handler.util.DiagnosticsModel;
import dji.internal.diagnostics.handler.util.DiagnosticsMotorFailModel;
import dji.internal.diagnostics.handler.util.DiagnosticsOnlyOneModel;
import dji.internal.diagnostics.handler.util.DiagnosticsRedundancySetModel;
import dji.internal.diagnostics.handler.util.DiagnosticsSwitchModel;
import dji.internal.logics.CommonUtil;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataADSBGetPushWarning;
import dji.midware.data.model.P3.DataFlycGetPushCheckStatus;
import dji.midware.data.model.P3.DataFlycGetPushFlycInstallError;
import dji.midware.data.model.P3.DataFlycGetPushForbidStatus;
import dji.midware.data.model.P3.DataFlycGetPushSmartBattery;
import dji.midware.data.model.P3.DataFlycRedundancyStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.ContextUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.thirdparty.io.reactivex.Maybe;
import dji.thirdparty.io.reactivex.MaybeEmitter;
import dji.thirdparty.io.reactivex.android.schedulers.AndroidSchedulers;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FlightControllerDiagnosticsHandler extends DiagnosticsBaseHandler implements DJIParamAccessListener {
    private static final Integer COMPASS_STATUS_ABNORMAL = 2;
    private static final Integer COMPASS_STATUS_NEED_RESTART = 4;
    private static final int DISCONNECTED = 0;
    private static final int IMU_NORMAL = 1;
    private static final double INVALID_DISTANCE = -1.0d;
    private static final int REDUNDANCY_CHECK_DIRECTION = 5;
    private static final int REDUNDANCY_HEATING = 2;
    private static final int REDUNDANCY_NEED_CALIBRATION = 3;
    private static final int REDUNDANCY_NEED_REBOOT = 4;
    private static final int REDUNDANCY_OFFSET_ERROR = 6;
    private static final String TAG = "diagnostics";
    private static final int WARNING_EXTREME_HEIGHT = 3000;
    private static final int WARNING_HEIGHT = 2500;
    private static final int WM160_WARNING_HEIGHT = 1500;
    private final DJIDiagnosticsType TYPE = DJIDiagnosticsType.FLIGHT_CONTROLLER;
    private List<DiagnosticsModel<Integer>> mCompassCheckStatusModels;
    private DJISDKCacheKey mCompassStatusKey;
    /* access modifiers changed from: private */
    public Context mContext;
    private List<DiagnosticsModel<DataADSBGetPushWarning>> mDiagnosticsADSModels;
    private DiagnosticsFlyAttiModel mDiagnosticsFlyAttiModel;
    private List<DiagnosticsModel<DataFlycGetPushFlycInstallError>> mDiagnosticsFlycInstallModels;
    private List<DiagnosticsModel<DataFlycGetPushCheckStatus>> mDiagnosticsFlycStatusModels;
    private List<DiagnosticsModel<DataOsdGetPushCommon>> mDiagnosticsGetPushCommonModels;
    private List<DiagnosticsModel<DataOsdGetPushHome>> mDiagnosticsGetPushHomeModels;
    private DiagnosticsFlycStateFcModel mDiagnosticsGpsExitFcModel;
    private DiagnosticsModel<Integer> mDiagnosticsImuErrorModel;
    private DiagnosticsMapModel<Integer> mDiagnosticsImuStatusModels;
    private DiagnosticsOnlyOneModel<DataOsdGetPushCommon> mDiagnosticsLowPowHintModel;
    private DiagnosticsFlycStateFcModel mDiagnosticsOnlySupportAttiModel;
    private DJISDKCacheKey mIMURedundancyStatusKey;
    private DataOsdGetPushCommon.MotorStartFailedCause mLastMotorFailedCause;
    private DiagnosticsMotorFailModel mMotorStartFailedModel;
    private DiagnosticsMapModel<DataOsdGetPushCommon.MotorFailReason> mMotorStopModel;
    private DJISDKCacheKey mRcModeChannelKey;
    private DiagnosticsMapModel<DataOsdGetPushCommon.RcModeChannel> mRcModeModel;
    private DiagnosticsRedundancySetModel mRedundancyModel;
    private Disposable mRedundancyPollDispose;
    private DiagnosticsModel<DataFlycGetPushSmartBattery> mSmartBatteryNeedGoHomeModel;

    public FlightControllerDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        obtainContext();
        initDiagnosticsList();
    }

    private void initDiagnosticsList() {
        this.mCompassCheckStatusModels = new ArrayList();
        this.mDiagnosticsFlycStatusModels = new ArrayList();
        this.mDiagnosticsGetPushCommonModels = new ArrayList();
        this.mDiagnosticsGetPushHomeModels = new ArrayList();
        this.mDiagnosticsFlycInstallModels = new ArrayList();
        this.mDiagnosticsADSModels = new ArrayList();
        this.mDiagnosticsImuStatusModels = new DiagnosticsMapModel<>();
        this.mCompassStatusKey = KeyHelper.getFlightControllerKey(FlightControllerKeys.REDUNDANCY_COMPASS_STATUS);
        this.mIMURedundancyStatusKey = KeyHelper.getFlightControllerKey(FlightControllerKeys.REDUNDANCY_IMU_STATUS);
        initFlightAttiType();
        initGoHomeType();
        initLowPowerHintList();
        initSensorErrorType();
        initImuErrorType();
        initCompassErrorType();
        initCannotTakeoffType();
        initMotorStopType();
        initStatusWarningType();
        initRedundancySwitchType();
        initAirSenseSystemType();
        initHeightLimitType();
        this.mRcModeChannelKey = KeyHelper.getFlightControllerKey(FlightControllerKeys.RC_MODE_CHANNEL);
        this.mRcModeModel = new DiagnosticsMapModel<>();
        this.mRcModeModel.autoDisappear(this);
        this.mRcModeModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.CHANGED_TO_SPORT_MODE), DataOsdGetPushCommon.RcModeChannel.CHANNEL_S);
        this.mRcModeModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.CHANGED_TO_TRIPOD_MODE), DataOsdGetPushCommon.RcModeChannel.CHANNEL_T);
        this.mRcModeModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.CHANGED_TO_POSITION_MODE), DataOsdGetPushCommon.RcModeChannel.CHANNEL_P);
        this.mDiagnosticsGpsExitFcModel = new DiagnosticsFlycStateFcModel(new FlightControllerDiagnosticsHandler$$Lambda$0(this), this);
        this.mDiagnosticsOnlySupportAttiModel = new DiagnosticsFlycStateFcModel(FlightControllerDiagnosticsHandler$$Lambda$1.$instance, this);
        this.mRedundancyModel = new DiagnosticsRedundancySetModel();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Integer lambda$initDiagnosticsList$0$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon.FLYC_STATE oldFlycState, DataOsdGetPushCommon pushCommon) {
        if (oldFlycState == null) {
            return null;
        }
        DataOsdGetPushCommon.FLYC_STATE curFlycState = pushCommon.getFlycState();
        if (CommonUtil.checkIsAttiMode(oldFlycState) || !CommonUtil.checkIsAttiMode(curFlycState) || !CommonUtil.checkUseNewModeChannel(pushCommon)) {
            return null;
        }
        if (isMultiModeOpen() && pushCommon.getModeChannel() == DataOsdGetPushCommon.RcModeChannel.CHANNEL_A) {
            return null;
        }
        if (pushCommon.getFlycVersion() < 5 || !CommonUtil.isCompassDisturb(pushCommon.getNonGpsCause())) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.WEAK_SIGNAL_CAUSE_EXIT_GPS_MODE);
        }
        return Integer.valueOf((int) DJIDiagnosticsError.FlightController.INTERFERENCE_CAUSE_EXIT_GPS_MODE);
    }

    static final /* synthetic */ Integer lambda$initDiagnosticsList$1$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon.FLYC_STATE oldFlycState, DataOsdGetPushCommon pushCommon) {
        DataOsdGetPushCommon.FLYC_STATE curFlycState = pushCommon.getFlycState();
        if (pushCommon.getModeChannel() != DataOsdGetPushCommon.RcModeChannel.CHANNEL_P || CommonUtil.checkIsAttiMode(oldFlycState) || !CommonUtil.checkIsAttiMode(curFlycState) || !pushCommon.isMotorUp()) {
            return null;
        }
        return Integer.valueOf((int) DJIDiagnosticsError.FlightController.ONLY_SUPPORT_ATTI_MODE);
    }

    private void initHeightLimitType() {
        this.mDiagnosticsGetPushHomeModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HEIGHT_LIMIT_REASON_NO_GPS, FlightControllerDiagnosticsHandler$$Lambda$2.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HEIGHT_LIMIT_REASON_COMPASS_INTERRUPT, new FlightControllerDiagnosticsHandler$$Lambda$3(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HEIGHT_LIMIT_REASON_REAL_NAME_ERROR, FlightControllerDiagnosticsHandler$$Lambda$4.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HEIGHT_LIMIT_REASON_GPS_COURSE_UNALIGNED, FlightControllerDiagnosticsHandler$$Lambda$5.$instance)));
    }

    static final /* synthetic */ boolean lambda$initHeightLimitType$2$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.HeightLimitStatus.NON_GPS.equals(pushHome.getHeightLimitStatus()) && pushHome.isReatchLimitHeight();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initHeightLimitType$3$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.HeightLimitStatus.ORIENTATION_NEED_CALI.equals(pushHome.getHeightLimitStatus()) && !pushHome.isReatchLimitHeight() && isMotorUp();
    }

    static final /* synthetic */ boolean lambda$initHeightLimitType$4$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.HeightLimitStatus.LIMIT_BY_REALNAME.equals(pushHome.getHeightLimitStatus()) && pushHome.isReatchLimitHeight();
    }

    static final /* synthetic */ boolean lambda$initHeightLimitType$5$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.HeightLimitStatus.ORIENTATION_NEED_CALI.equals(pushHome.getHeightLimitStatus()) && pushHome.isReatchLimitHeight();
    }

    private boolean isMultiModeOpen() {
        return !DataOsdGetPushHome.getInstance().isBeginnerMode() && DataOsdGetPushHome.getInstance().isMultipleModeOpen();
    }

    private void initAirSenseSystemType() {
        this.mDiagnosticsADSModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.AIR_SENSE_SYSTEM_WARNING, FlightControllerDiagnosticsHandler$$Lambda$6.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.AIR_SENSE_SYSTEM_SERIOUS_WARNING, FlightControllerDiagnosticsHandler$$Lambda$7.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.AIR_SENSE_SYSTEM_DISCONNECTED, FlightControllerDiagnosticsHandler$$Lambda$8.$instance)));
    }

    static final /* synthetic */ boolean lambda$initAirSenseSystemType$7$FlightControllerDiagnosticsHandler(DataADSBGetPushWarning adsWaring) {
        return DataADSBGetPushWarning.DJIWarningType.Three.equals(adsWaring.getWarningType()) || DataADSBGetPushWarning.DJIWarningType.Four.equals(adsWaring.getWarningType());
    }

    static final /* synthetic */ boolean lambda$initAirSenseSystemType$8$FlightControllerDiagnosticsHandler(DataADSBGetPushWarning adsWaring) {
        return !adsWaring.isConnectAdsb();
    }

    private void initRedundancySwitchType() {
        this.mDiagnosticsGetPushHomeModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_BACKUP_ABNORMAL, new FlightControllerDiagnosticsHandler$$Lambda$9(this)), new DiagnosticsSwitchModel(DJIDiagnosticsError.FlightController.IMU_SWITCH, FlightControllerDiagnosticsHandler$$Lambda$10.$instance, this), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COMPASS_BACKUP_ABNORMAL, new FlightControllerDiagnosticsHandler$$Lambda$11(this)), new DiagnosticsSwitchModel(DJIDiagnosticsError.FlightController.COMPASS_SWITCH, FlightControllerDiagnosticsHandler$$Lambda$12.$instance, this), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.BAROMETER_BACKUP_ABNORMAL, new FlightControllerDiagnosticsHandler$$Lambda$13(this)), new DiagnosticsSwitchModel(DJIDiagnosticsError.FlightController.BAROMETER_SWITCH, FlightControllerDiagnosticsHandler$$Lambda$14.$instance, this), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GPS_BACKUP_ABNORMAL, new FlightControllerDiagnosticsHandler$$Lambda$15(this)), new DiagnosticsSwitchModel(DJIDiagnosticsError.FlightController.GPS_SWITCH, FlightControllerDiagnosticsHandler$$Lambda$16.$instance, this), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.NAVIGATION_SYSTEM_BACKUP_ABNORMAL, new FlightControllerDiagnosticsHandler$$Lambda$17(this)), new DiagnosticsSwitchModel(DJIDiagnosticsError.FlightController.NAVIGATION_SYSTEM_SWITCH, FlightControllerDiagnosticsHandler$$Lambda$18.$instance, this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initRedundancySwitchType$9$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return isMotorUp() && pushHome.isIMUAbnormal();
    }

    static final /* synthetic */ boolean lambda$initRedundancySwitchType$10$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getIMUSwitchFlag() == 1;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initRedundancySwitchType$11$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return isMotorUp() && pushHome.isCompassAbnormal();
    }

    static final /* synthetic */ boolean lambda$initRedundancySwitchType$12$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getCompassSwitchFlag() == 1;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initRedundancySwitchType$13$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return isMotorUp() && pushHome.isBarometerAbnormal();
    }

    static final /* synthetic */ boolean lambda$initRedundancySwitchType$14$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getBarometerSwitchFlag() == 1;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initRedundancySwitchType$15$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return isMotorUp() && pushHome.isGpsAbnormal();
    }

    static final /* synthetic */ boolean lambda$initRedundancySwitchType$16$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getGpsSwitchFlag() == 1;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initRedundancySwitchType$17$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return isMotorUp() && pushHome.isNavigationSysAbnormal();
    }

    static final /* synthetic */ boolean lambda$initRedundancySwitchType$18$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getNavigationSysSwitchFlag() == 1;
    }

    private void initStatusWarningType() {
        this.mDiagnosticsGetPushHomeModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GALE_WARNING, FlightControllerDiagnosticsHandler$$Lambda$19.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GALE_SERIOUS_WARNING, FlightControllerDiagnosticsHandler$$Lambda$20.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HIGHLAND_PADDLE_CHECK_WRONG, FlightControllerDiagnosticsHandler$$Lambda$21.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.PLANE_PADDLE_CHECK_WRONG, FlightControllerDiagnosticsHandler$$Lambda$22.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.WARNING_ALTITUDE_LOCATION, FlightControllerDiagnosticsHandler$$Lambda$23.$instance, FlightControllerDiagnosticsHandler$$Lambda$24.$instance).updateExtraPeriodic(this), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HIGH_ALTITUDE_LOCATION, FlightControllerDiagnosticsHandler$$Lambda$25.$instance, FlightControllerDiagnosticsHandler$$Lambda$26.$instance).updateExtraPeriodic(this), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.EXTREME_HIGH_ALTITUDE_LOCATION, FlightControllerDiagnosticsHandler$$Lambda$27.$instance, FlightControllerDiagnosticsHandler$$Lambda$28.$instance).updateExtraPeriodic(this), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.WATER_SURFACE_WARNING, FlightControllerDiagnosticsHandler$$Lambda$29.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.PADDLE_HAS_ICE_ON_IT, FlightControllerDiagnosticsHandler$$Lambda$30.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GPS_SIGNAL_BLOCKED_BY_GIMBAL, FlightControllerDiagnosticsHandler$$Lambda$31.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.PROTECTION_COVER_IS_ON, new FlightControllerDiagnosticsHandler$$Lambda$32(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COVER_APP_ENABLE_NO_LIMIT, new FlightControllerDiagnosticsHandler$$Lambda$33(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COVER_APP_ENABLE_LIMIT, new FlightControllerDiagnosticsHandler$$Lambda$34(this), new FlightControllerDiagnosticsHandler$$Lambda$35(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COVER_FLIGHT_ENABLE_NO_LIMIT, new FlightControllerDiagnosticsHandler$$Lambda$36(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COVER_FLIGHT_ENABLE_LIMIT, new FlightControllerDiagnosticsHandler$$Lambda$37(this), new FlightControllerDiagnosticsHandler$$Lambda$38(this)), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.ENV_STATE_TEMP_TOO_LOW, FlightControllerDiagnosticsHandler$$Lambda$39.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.ENV_STATE_TEMP_TOO_HIGH, FlightControllerDiagnosticsHandler$$Lambda$40.$instance)));
        this.mDiagnosticsFlycStatusModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.MC_IN_READ_SD_MODE, FlightControllerDiagnosticsHandler$$Lambda$41.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.KERNEL_BOARD_HIGH_TEMPERATURE, FlightControllerDiagnosticsHandler$$Lambda$42.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.ATTITUDE_ERROR, FlightControllerDiagnosticsHandler$$Lambda$43.$instance)));
        this.mDiagnosticsGetPushCommonModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.ENABLE_NEAR_GROUND_ALERT, FlightControllerDiagnosticsHandler$$Lambda$44.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.MOTOR_BLOCKED, FlightControllerDiagnosticsHandler$$Lambda$45.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.NO_PADDLE_ON_MOTOR, FlightControllerDiagnosticsHandler$$Lambda$46.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.NOT_ENOUGH_FORCE, FlightControllerDiagnosticsHandler$$Lambda$47.$instance).keepForAWhile(this, DJIVideoDecoder.connectLosedelay), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.OUT_OF_FLIGHT_RADIUS_LIMIT, FlightControllerDiagnosticsHandler$$Lambda$48.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.OVER_HEAT_LEVEL2_GOHOME, FlightControllerDiagnosticsHandler$$Lambda$49.$instance)));
        this.mDiagnosticsFlycInstallModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HEADING_CONTROL_ABNORMAL, FlightControllerDiagnosticsHandler$$Lambda$50.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.TILT_CONTROL_ABNORMAL, FlightControllerDiagnosticsHandler$$Lambda$51.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.AIRCRAFT_VIBRATION_ABNORMAL, FlightControllerDiagnosticsHandler$$Lambda$52.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.HOVER_FORCE_NOT_ENOUGH, FlightControllerDiagnosticsHandler$$Lambda$53.$instance)));
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$21$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return CommonUtil.isWM160(null) && pushHome.getHeight() > 1500.0f && DataOsdGetPushCommon.getInstance().isMotorUp();
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$22$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getHeight() > 2500.0f && pushHome.getHeight() < 3000.0f && DataOsdGetPushCommon.getInstance().isMotorUp();
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$23$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getHeight() >= 3000.0f && DataOsdGetPushCommon.getInstance().isMotorUp();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initStatusWarningType$24$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.isExtraLoadDetected() && !isPropCoverLimitV2();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initStatusWarningType$25$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.PropCoverLimitState.PROP_COVER_APP_ENABLE_NO_LIMIT == pushHome.getPropCoverLimitState() && isPropCoverLimitV2();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initStatusWarningType$26$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.PropCoverLimitState.PROP_COVER_APP_ENABLE_LIMIT == pushHome.getPropCoverLimitState() && isPropCoverLimitV2();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Object lambda$initStatusWarningType$27$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return getLimitExtra();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initStatusWarningType$28$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.PropCoverLimitState.PROP_COVER_FC_ENABLE_NO_LIMIT == pushHome.getPropCoverLimitState() && isPropCoverLimitV2();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initStatusWarningType$29$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return DataOsdGetPushHome.PropCoverLimitState.PROP_COVER_FC_ENABLE_LIMIT == pushHome.getPropCoverLimitState() && isPropCoverLimitV2();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ Object lambda$initStatusWarningType$30$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return getLimitExtra();
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$31$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getEnvTempState() == DataOsdGetPushHome.EnvTempState.TEMP_LOW;
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$32$FlightControllerDiagnosticsHandler(DataOsdGetPushHome pushHome) {
        return pushHome.getEnvTempState() == DataOsdGetPushHome.EnvTempState.TEMP_HIGH;
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$34$FlightControllerDiagnosticsHandler(DataFlycGetPushFlycInstallError installError) {
        return installError.getYawInstallErrorLevel() >= 2 || installError.getRollInstallErrorLevel() >= 2 || installError.getPitchInstallErrorLevel() >= 2;
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$35$FlightControllerDiagnosticsHandler(DataFlycGetPushFlycInstallError installError) {
        return installError.getGyroXInstallErrorLevel() >= 2 || installError.getGyroYInstallErrorLevel() >= 2 || installError.getGyroZInstallErrorLevel() >= 2;
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$36$FlightControllerDiagnosticsHandler(DataFlycGetPushFlycInstallError installError) {
        return installError.getAccXInstallErrorLevel() >= 2 || installError.getAccYInstallErrorLevel() >= 2 || installError.getAccZInstallErrorLevel() >= 2;
    }

    static final /* synthetic */ boolean lambda$initStatusWarningType$37$FlightControllerDiagnosticsHandler(DataFlycGetPushFlycInstallError installError) {
        return installError.getThrustInstallErrorLevel() >= 2;
    }

    private boolean isPropCoverLimitV2() {
        return CommonUtil.isWM160(null);
    }

    private void initCompassErrorType() {
        this.mDiagnosticsGetPushCommonModels.addAll(Arrays.asList(new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.COMPASS_DEAD, DataOsdGetPushCommon.IMU_INITFAIL_REASON.CompassDead), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.COMPASS_NEED_CALIBRATION, DataOsdGetPushCommon.IMU_INITFAIL_REASON.CompassModTooLarge), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.COMPASS_NEED_CALIBRATION, DataOsdGetPushCommon.IMU_INITFAIL_REASON.CompassNoiseTooLarge), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COMPASS_ABNORMAL, FlightControllerDiagnosticsHandler$$Lambda$54.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COMPASS_ABNORMAL, FlightControllerDiagnosticsHandler$$Lambda$55.$instance)));
        this.mDiagnosticsGetPushHomeModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COMPASS_ABNORMAL, FlightControllerDiagnosticsHandler$$Lambda$56.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COMPASS_INSTALL_ERROR, FlightControllerDiagnosticsHandler$$Lambda$57.$instance).ignoreTempChange(this)));
        List<DiagnosticsModel<Integer>> list = this.mCompassCheckStatusModels;
        Integer num = COMPASS_STATUS_ABNORMAL;
        num.getClass();
        list.add(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COMPASS_ABNORMAL, FlightControllerDiagnosticsHandler$$Lambda$58.get$Lambda(num)));
        List<DiagnosticsModel<Integer>> list2 = this.mCompassCheckStatusModels;
        Integer num2 = COMPASS_STATUS_NEED_RESTART;
        num2.getClass();
        list2.add(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.COMPASS_NEED_RESTART, FlightControllerDiagnosticsHandler$$Lambda$59.get$Lambda(num2)));
    }

    static final /* synthetic */ boolean lambda$initCompassErrorType$38$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return DataOsdGetPushCommon.NON_GPS_CAUSE.SPEED_ERROR_LARGE.equals(pushCommon.getNonGpsCause()) || DataOsdGetPushCommon.NON_GPS_CAUSE.YAW_ERROR_LARGE.equals(pushCommon.getNonGpsCause()) || DataOsdGetPushCommon.NON_GPS_CAUSE.COMPASS_ERROR_LARGE.equals(pushCommon.getNonGpsCause());
    }

    private void initImuErrorType() {
        this.mDiagnosticsImuErrorModel = new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_ERROR, FlightControllerDiagnosticsHandler$$Lambda$60.$instance);
        this.mDiagnosticsGetPushHomeModels.add(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_ERROR, FlightControllerDiagnosticsHandler$$Lambda$61.$instance));
        this.mDiagnosticsImuStatusModels.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.IMU_HEATING), 2);
        this.mDiagnosticsImuStatusModels.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.IMU_NEED_CALIBRATE), 3);
        this.mDiagnosticsGetPushCommonModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_HEATING, FlightControllerDiagnosticsHandler$$Lambda$62.$instance), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.IMU_INIT_FAIL_FOR_MOVING, DataOsdGetPushCommon.IMU_INITFAIL_REASON.WaitingMcStationary), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.IMU_INIT_FAIL_FOR_MOVING, DataOsdGetPushCommon.IMU_INITFAIL_REASON.AcceMoveTooLarge), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.IMU_INIT_FAIL_FOR_MOVING, DataOsdGetPushCommon.IMU_INITFAIL_REASON.McHeaderMoved), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.IMU_INIT_FAIL_FOR_MOVING, DataOsdGetPushCommon.IMU_INITFAIL_REASON.McVirbrated), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.IMU_NEED_CALIBRATE, DataOsdGetPushCommon.IMU_INITFAIL_REASON.GyroBiasTooLarge), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.IMU_NEED_CALIBRATE, DataOsdGetPushCommon.IMU_INITFAIL_REASON.AcceBiasTooLarge), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_NEED_CALIBRATE, FlightControllerDiagnosticsHandler$$Lambda$63.$instance)));
        this.mDiagnosticsFlycStatusModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_INIT_FAILED, FlightControllerDiagnosticsHandler$$Lambda$64.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_NEED_CALIBRATE, FlightControllerDiagnosticsHandler$$Lambda$65.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_CALIBRATION_INCOMPLETE, FlightControllerDiagnosticsHandler$$Lambda$66.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_DATA_ERROR, FlightControllerDiagnosticsHandler$$Lambda$67.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.IMU_PARAMETER_ERROR, FlightControllerDiagnosticsHandler$$Lambda$68.$instance)));
    }

    static final /* synthetic */ boolean lambda$initImuErrorType$39$FlightControllerDiagnosticsHandler(Integer imuStatus) {
        return (imuStatus.intValue() == 1 || imuStatus.intValue() == 0 || imuStatus.intValue() == 2) ? false : true;
    }

    static final /* synthetic */ boolean lambda$initImuErrorType$40$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return !pushCommon.isImuPreheatd();
    }

    static final /* synthetic */ boolean lambda$initImuErrorType$41$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return DataOsdGetPushCommon.MotorStartFailedCause.IMUNeedCalibration.equals(push.getMotorStartCauseNoStartAction()) || DataOsdGetPushCommon.MotorStartFailedCause.IMUNeedCalibration.equals(push.getMotorFailedCause());
    }

    static final /* synthetic */ boolean lambda$initImuErrorType$42$FlightControllerDiagnosticsHandler(DataFlycGetPushCheckStatus status) {
        return status.getIMUAdvanceCaliStatus() || status.getIMUBasicCaliStatus() || status.getVersionStatus();
    }

    static final /* synthetic */ boolean lambda$initImuErrorType$43$FlightControllerDiagnosticsHandler(DataFlycGetPushCheckStatus status) {
        return status.getLastIMUAdvanceCaliStatus() || status.getLastIMUBasicCaliStatus();
    }

    static final /* synthetic */ boolean lambda$initImuErrorType$44$FlightControllerDiagnosticsHandler(DataFlycGetPushCheckStatus status) {
        return status.getIMUHorizontalCaliStatus() || status.getIMUDirectionStatus();
    }

    private void initSensorErrorType() {
        this.mDiagnosticsFlycStatusModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.MC_ERROR, FlightControllerDiagnosticsHandler$$Lambda$69.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.BAROMETER_INIT_FAILED, FlightControllerDiagnosticsHandler$$Lambda$70.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GYROSCOPE_DATA_ERROR, FlightControllerDiagnosticsHandler$$Lambda$71.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.ACCELEROMETER_DATA_ERROR, FlightControllerDiagnosticsHandler$$Lambda$72.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.DATA_RECORDER_ERROR, FlightControllerDiagnosticsHandler$$Lambda$73.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.BAROMETER_DATA_ERROR, FlightControllerDiagnosticsHandler$$Lambda$74.$instance)));
        this.mDiagnosticsGetPushCommonModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.SENSOR_ERROR, FlightControllerDiagnosticsHandler$$Lambda$75.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.ESC_ERROR, FlightControllerDiagnosticsHandler$$Lambda$76.$instance, FlightControllerDiagnosticsHandler$$Lambda$77.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.BAROMETER_ERROR, FlightControllerDiagnosticsHandler$$Lambda$78.$instance), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.BAROMETER_DEAD, DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerDead), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.BAROMETER_DATA_ERROR, DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerNegative), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.BAROMETER_NOISE_TOO_LARGE, DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerNoiseTooLarge), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.GYROSCOPE_DEAD, DataOsdGetPushCommon.IMU_INITFAIL_REASON.GyroDead), new DiagnosticsImuInitFailModel(DJIDiagnosticsError.FlightController.ACCELEROMETER_DEAD, DataOsdGetPushCommon.IMU_INITFAIL_REASON.AcceDead), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GPS_ERROR, FlightControllerDiagnosticsHandler$$Lambda$79.$instance)));
    }

    static final /* synthetic */ boolean lambda$initSensorErrorType$45$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        if (DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerNoiseTooLarge.equals(pushCommon.getIMUinitFailReason()) || DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerDead.equals(pushCommon.getIMUinitFailReason()) || DataOsdGetPushCommon.IMU_INITFAIL_REASON.BarometerNegative.equals(pushCommon.getIMUinitFailReason()) || DataOsdGetPushCommon.IMU_INITFAIL_REASON.GyroDead.equals(pushCommon.getIMUinitFailReason()) || DataOsdGetPushCommon.IMU_INITFAIL_REASON.AcceDead.equals(pushCommon.getIMUinitFailReason()) || DataOsdGetPushCommon.IMU_INITFAIL_REASON.CompassDead.equals(pushCommon.getIMUinitFailReason())) {
            return true;
        }
        return false;
    }

    static final /* synthetic */ boolean lambda$initSensorErrorType$46$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return !DataOsdGetPushCommon.NON_GPS_CAUSE.ALREADY.equals(pushCommon.getNonGpsCause()) && !DataOsdGetPushCommon.NON_GPS_CAUSE.FORBIN.equals(pushCommon.getNonGpsCause());
    }

    private void initGoHomeType() {
        this.mDiagnosticsGetPushCommonModels.addAll(Arrays.asList(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GOING_HOME, FlightControllerDiagnosticsHandler$$Lambda$80.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GOING_HOME_PREASCENDING, FlightControllerDiagnosticsHandler$$Lambda$81.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GOING_HOME_ALIGN, FlightControllerDiagnosticsHandler$$Lambda$82.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GOING_HOME_ASCENDING, FlightControllerDiagnosticsHandler$$Lambda$83.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GOING_HOME_CRUISE, FlightControllerDiagnosticsHandler$$Lambda$84.$instance), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.OUT_OF_CONTROL, FlightControllerDiagnosticsHandler$$Lambda$85.$instance).ignoreTempChange(this, 3000), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.OUT_OF_CONTROL_GO_HOME, FlightControllerDiagnosticsHandler$$Lambda$86.$instance).ignoreTempChange(this, 3000), new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.GO_HOME_ERROR, FlightControllerDiagnosticsHandler$$Lambda$87.$instance)));
        this.mSmartBatteryNeedGoHomeModel = new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.BATTERY_ELECTRICITY_GO_HOME, FlightControllerDiagnosticsHandler$$Lambda$88.$instance);
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$47$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState()) && DataOsdGetPushCommon.GOHOME_STATUS.STANDBY.equals(push.getGohomeStatus());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$48$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState()) && DataOsdGetPushCommon.GOHOME_STATUS.PREASCENDING.equals(push.getGohomeStatus());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$49$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState()) && DataOsdGetPushCommon.GOHOME_STATUS.ALIGN.equals(push.getGohomeStatus());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$50$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState()) && DataOsdGetPushCommon.GOHOME_STATUS.ASCENDING.equals(push.getGohomeStatus());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$51$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState()) && DataOsdGetPushCommon.GOHOME_STATUS.CRUISE.equals(push.getGohomeStatus());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$52$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return !push.getRcState() && !DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$53$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return !push.getRcState() && DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$54$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon push) {
        return !checkGpsValid(push) && DataOsdGetPushCommon.FLYC_STATE.GoHome.equals(push.getFlycState());
    }

    static final /* synthetic */ boolean lambda$initGoHomeType$55$FlightControllerDiagnosticsHandler(DataFlycGetPushSmartBattery smartBattery) {
        return (smartBattery.getStatus() & 4) != 0;
    }

    private void initFlightAttiType() {
        this.mDiagnosticsFlyAttiModel = new DiagnosticsFlyAttiModel(FlightControllerDiagnosticsHandler$$Lambda$89.$instance);
    }

    static final /* synthetic */ Integer lambda$initFlightAttiType$56$FlightControllerDiagnosticsHandler(boolean gpsValid, boolean singleBattery, boolean isMotorUp, boolean visionUsed, boolean inAtti, boolean inHeightLimitZone) {
        if (inAtti && inHeightLimitZone) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.LIMIT_SPACE_ATTI);
        }
        if (inAtti && !inHeightLimitZone && isMotorUp && singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.SINGLE_BATTERY_NORMAL_FLIGHT_ATTI);
        }
        if (inAtti && !inHeightLimitZone && isMotorUp && !singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_FLIGHT_ATTI);
        }
        if (inAtti && !inHeightLimitZone && !isMotorUp) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_READY_ATTI);
        }
        if (visionUsed && !gpsValid && inHeightLimitZone && isMotorUp) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.LIMIT_SPACE_VISION);
        }
        if (visionUsed && !gpsValid && !inHeightLimitZone && isMotorUp && singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.SINGLE_BATTERY_NORMAL_FLIGHT_VISION);
        }
        if (visionUsed && !gpsValid && !inHeightLimitZone && isMotorUp && !singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_FLIGHT_VISION);
        }
        if (visionUsed && !gpsValid && !isMotorUp) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_READY_VISION);
        }
        if (!visionUsed && !gpsValid && inHeightLimitZone) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.LIMIT_SPACE_NO_GPS);
        }
        if (!visionUsed && !gpsValid && !inHeightLimitZone && isMotorUp && singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.SINGLE_BATTERY_NORMAL_FLIGHT_NO_GPS);
        }
        if (!visionUsed && !gpsValid && !inHeightLimitZone && isMotorUp && !singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_FLIGHT_NO_GPS);
        }
        if (!visionUsed && !gpsValid && !inHeightLimitZone && !isMotorUp) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_READY_NO_GPS);
        }
        if (gpsValid && inHeightLimitZone) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.LIMIT_SPACE);
        }
        if (gpsValid && !inHeightLimitZone && isMotorUp && singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.SINGLE_BATTERY_NORMAL_FLIGHT);
        }
        if (gpsValid && !inHeightLimitZone && isMotorUp && !singleBattery) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_FLIGHT);
        }
        if (gpsValid && !inHeightLimitZone && !isMotorUp) {
            return Integer.valueOf((int) DJIDiagnosticsError.FlightController.NORMAL_READY);
        }
        DiagnosticsLog.loge(TAG, "initFlightAttiType() called null, gpsValid=[" + gpsValid + "], singleBattery=[" + singleBattery + "], isMotorUp=[" + isMotorUp + "], visionUsed=[" + visionUsed + "], inAtti=[" + inAtti + "], inHeightLimitZone=[" + inHeightLimitZone + IMemberProtocol.STRING_SEPERATOR_RIGHT);
        return null;
    }

    private void initLowPowerHintList() {
        this.mDiagnosticsLowPowHintModel = new DiagnosticsOnlyOneModel<>();
        this.mDiagnosticsLowPowHintModel.put(DJIDiagnosticsError.FlightController.SMART_LOW_POWER, FlightControllerDiagnosticsHandler$$Lambda$90.$instance, FlightControllerDiagnosticsHandler$$Lambda$91.$instance);
        this.mDiagnosticsLowPowHintModel.put(DJIDiagnosticsError.FlightController.SMART_LOW_POWER_GO_HOME, FlightControllerDiagnosticsHandler$$Lambda$92.$instance);
        this.mDiagnosticsLowPowHintModel.put(DJIDiagnosticsError.FlightController.SMART_SERIOUS_LOW_POWER, new FlightControllerDiagnosticsHandler$$Lambda$93(this));
        this.mDiagnosticsLowPowHintModel.put(DJIDiagnosticsError.FlightController.SMART_SERIOUS_LOW_POWER_LANDING, new FlightControllerDiagnosticsHandler$$Lambda$94(this));
        this.mDiagnosticsLowPowHintModel.put(DJIDiagnosticsError.FlightController.SERIOUS_LOW_VOLTAGE, new FlightControllerDiagnosticsHandler$$Lambda$95(this));
        this.mDiagnosticsLowPowHintModel.put(DJIDiagnosticsError.FlightController.SERIOUS_LOW_VOLTAGE_LANDING, new FlightControllerDiagnosticsHandler$$Lambda$96(this));
    }

    static final /* synthetic */ boolean lambda$initLowPowerHintList$57$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return pushCommon.getVoltageWarning() == 1 && DataOsdGetPushCommon.FLYC_STATE.GoHome != pushCommon.getFlycState();
    }

    static final /* synthetic */ boolean lambda$initLowPowerHintList$58$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        if ((pushCommon.getVoltageWarning() != 1 || DataOsdGetPushCommon.FLYC_STATE.GoHome != pushCommon.getFlycState()) && !DataOsdGetPushCommon.FLIGHT_ACTION.WARNING_POWER_GOHOME.equals(pushCommon.getFlightAction())) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initLowPowerHintList$59$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return (DataOsdGetPushCommon.FLIGHT_ACTION.SMART_POWER_LANDING.equals(pushCommon.getFlightAction()) || pushCommon.getVoltageWarning() == 2) && !isLandingMode(pushCommon.getFlycState());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initLowPowerHintList$60$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return (DataOsdGetPushCommon.FLIGHT_ACTION.SMART_POWER_LANDING.equals(pushCommon.getFlightAction()) || pushCommon.getVoltageWarning() == 2) && isLandingMode(pushCommon.getFlycState());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initLowPowerHintList$61$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return DataOsdGetPushCommon.FLIGHT_ACTION.SERIOUS_LOW_VOLTAGE_LANDING.equals(pushCommon.getFlightAction()) && !isLandingMode(pushCommon.getFlycState());
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$initLowPowerHintList$62$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return DataOsdGetPushCommon.FLIGHT_ACTION.SERIOUS_LOW_VOLTAGE_LANDING.equals(pushCommon.getFlightAction()) && isLandingMode(pushCommon.getFlycState());
    }

    public void initMotorStopType() {
        this.mMotorStopModel = new DiagnosticsMapModel<>();
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_TAKE_OFF_EXCEPTION), DataOsdGetPushCommon.MotorFailReason.TAKEOFF_EXCEPTION);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_ESC_STALL_NEAR_GROUND), DataOsdGetPushCommon.MotorFailReason.ESC_STALL_NEAR_GROUND);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_ESC_UNBALANCE_ON_GROUND), DataOsdGetPushCommon.MotorFailReason.ESC_UNBALANCE_ON_GRD);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_ESC_PART_EMPTY_ON_GROUND), DataOsdGetPushCommon.MotorFailReason.ESC_PART_EMPTY_ON_GRD);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_ENGINE_START_FAILED), DataOsdGetPushCommon.MotorFailReason.ENGINE_START_FAILED);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_AUTO_TAKEOFF_LAUNCH_FAILED), DataOsdGetPushCommon.MotorFailReason.AUTO_TAKEOFF_LANCH_FAILED);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_ROLL_OVER_ON_GROUND), DataOsdGetPushCommon.MotorFailReason.ROLL_OVER_ON_GRD);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_ESC_SHORT_CIRCUIT), DataOsdGetPushCommon.MotorFailReason.ESC_SHORT_CIRCUIT);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_HEIGHT_EXCEPTION), DataOsdGetPushCommon.MotorFailReason.START_FLY_HEIGHT_ERROR);
        this.mMotorStopModel.put(Integer.valueOf((int) DJIDiagnosticsError.FlightController.MOTOR_STOP_FOR_IMPACT_DETECTED), DataOsdGetPushCommon.MotorFailReason.BE_IMPACT);
    }

    private void initCannotTakeoffType() {
        this.mMotorStartFailedModel = new DiagnosticsMotorFailModel(TakeoffFailErrorAdapter.getTakeoffFailCauseCodeMap(), this);
        this.mDiagnosticsFlycStatusModels.add(new DiagnosticsIfModel((int) DJIDiagnosticsError.FlightController.CANNOT_TAKE_OFF_BATTERY_NOT_IN_POSITION, FlightControllerDiagnosticsHandler$$Lambda$97.$instance, FlightControllerDiagnosticsHandler$$Lambda$98.$instance).updateExtraPeriodic(this));
        this.mDiagnosticsGetPushCommonModels.add(new DiagnosticsIfModel((int) DJIDiagnosticsError.Custom.CAN_NOT_TAKE_OFF_ERROR, FlightControllerDiagnosticsHandler$$Lambda$99.$instance));
    }

    static final /* synthetic */ boolean lambda$initCannotTakeoffType$64$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        return !pushCommon.isMotorUp() && pushCommon.getMotorStartCauseNoStartAction() != DataOsdGetPushCommon.MotorStartFailedCause.None;
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        if (DataFlycGetPushCheckStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushCheckStatus.getInstance());
        }
        if (DataOsdGetPushHome.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushHome.getInstance());
        }
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCommon.getInstance());
        }
        if (DataFlycGetPushForbidStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushForbidStatus.getInstance());
        }
        if (DataFlycGetPushForbidStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushFlycInstallError.getInstance());
        }
        if (DataADSBGetPushWarning.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataADSBGetPushWarning.getInstance());
        }
        if (DataFlycGetPushSmartBattery.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushSmartBattery.getInstance());
        }
        CacheHelper.addListener(this, this.mIMURedundancyStatusKey, this.mCompassStatusKey, this.mRcModeChannelKey);
        CacheHelper.addFlightControllerListener(this, FlightControllerKeys.PROP_COVER_LIMIT_HEIGHT, FlightControllerKeys.PROP_COVER_LIMIT_RADIUS);
        onIMURedundancyStatus();
        onCompassStatus();
        onRcModeChannel(null);
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
        CacheHelper.removeListener(this);
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        for (DiagnosticsModel<DataFlycGetPushCheckStatus> diagnosticsFlycStatusModel : this.mDiagnosticsFlycStatusModels) {
            DJIDiagnosticsType dJIDiagnosticsType = this.TYPE;
            diagnosisList.getClass();
            diagnosticsFlycStatusModel.doIfError(dJIDiagnosticsType, FlightControllerDiagnosticsHandler$$Lambda$100.get$Lambda(diagnosisList));
        }
        DiagnosticsModel<Integer> diagnosticsModel = this.mDiagnosticsImuErrorModel;
        DJIDiagnosticsType dJIDiagnosticsType2 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsModel.doIfError(dJIDiagnosticsType2, FlightControllerDiagnosticsHandler$$Lambda$101.get$Lambda(diagnosisList));
        DiagnosticsMapModel<Integer> diagnosticsMapModel = this.mDiagnosticsImuStatusModels;
        DJIDiagnosticsType dJIDiagnosticsType3 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsMapModel.doIfError(dJIDiagnosticsType3, FlightControllerDiagnosticsHandler$$Lambda$102.get$Lambda(diagnosisList));
        DiagnosticsOnlyOneModel<DataOsdGetPushCommon> diagnosticsOnlyOneModel = this.mDiagnosticsLowPowHintModel;
        DJIDiagnosticsType dJIDiagnosticsType4 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsOnlyOneModel.doIfError(dJIDiagnosticsType4, FlightControllerDiagnosticsHandler$$Lambda$103.get$Lambda(diagnosisList));
        for (DiagnosticsModel<DataOsdGetPushHome> model : this.mDiagnosticsGetPushHomeModels) {
            DJIDiagnosticsType dJIDiagnosticsType5 = this.TYPE;
            diagnosisList.getClass();
            model.doIfError(dJIDiagnosticsType5, FlightControllerDiagnosticsHandler$$Lambda$104.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataOsdGetPushCommon> model2 : this.mDiagnosticsGetPushCommonModels) {
            DJIDiagnosticsType dJIDiagnosticsType6 = this.TYPE;
            diagnosisList.getClass();
            model2.doIfError(dJIDiagnosticsType6, FlightControllerDiagnosticsHandler$$Lambda$105.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataFlycGetPushFlycInstallError> model3 : this.mDiagnosticsFlycInstallModels) {
            DJIDiagnosticsType dJIDiagnosticsType7 = this.TYPE;
            diagnosisList.getClass();
            model3.doIfError(dJIDiagnosticsType7, FlightControllerDiagnosticsHandler$$Lambda$106.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<DataADSBGetPushWarning> model4 : this.mDiagnosticsADSModels) {
            DJIDiagnosticsType dJIDiagnosticsType8 = this.TYPE;
            diagnosisList.getClass();
            model4.doIfError(dJIDiagnosticsType8, FlightControllerDiagnosticsHandler$$Lambda$107.get$Lambda(diagnosisList));
        }
        for (DiagnosticsModel<Integer> model5 : this.mCompassCheckStatusModels) {
            DJIDiagnosticsType dJIDiagnosticsType9 = this.TYPE;
            diagnosisList.getClass();
            model5.doIfError(dJIDiagnosticsType9, FlightControllerDiagnosticsHandler$$Lambda$108.get$Lambda(diagnosisList));
        }
        DiagnosticsFlyAttiModel diagnosticsFlyAttiModel = this.mDiagnosticsFlyAttiModel;
        DJIDiagnosticsType dJIDiagnosticsType10 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsFlyAttiModel.doIfError(dJIDiagnosticsType10, FlightControllerDiagnosticsHandler$$Lambda$109.get$Lambda(diagnosisList));
        DiagnosticsMotorFailModel diagnosticsMotorFailModel = this.mMotorStartFailedModel;
        DJIDiagnosticsType dJIDiagnosticsType11 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsMotorFailModel.doIfError(dJIDiagnosticsType11, FlightControllerDiagnosticsHandler$$Lambda$110.get$Lambda(diagnosisList));
        DiagnosticsMapModel<DataOsdGetPushCommon.MotorFailReason> diagnosticsMapModel2 = this.mMotorStopModel;
        DJIDiagnosticsType dJIDiagnosticsType12 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsMapModel2.doIfError(dJIDiagnosticsType12, FlightControllerDiagnosticsHandler$$Lambda$111.get$Lambda(diagnosisList));
        DiagnosticsRedundancySetModel diagnosticsRedundancySetModel = this.mRedundancyModel;
        DJIDiagnosticsType dJIDiagnosticsType13 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsRedundancySetModel.doIfError(dJIDiagnosticsType13, FlightControllerDiagnosticsHandler$$Lambda$112.get$Lambda(diagnosisList));
        DiagnosticsMapModel<DataOsdGetPushCommon.RcModeChannel> diagnosticsMapModel3 = this.mRcModeModel;
        DJIDiagnosticsType dJIDiagnosticsType14 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsMapModel3.doIfError(dJIDiagnosticsType14, FlightControllerDiagnosticsHandler$$Lambda$113.get$Lambda(diagnosisList));
        DiagnosticsFlycStateFcModel diagnosticsFlycStateFcModel = this.mDiagnosticsOnlySupportAttiModel;
        DJIDiagnosticsType dJIDiagnosticsType15 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsFlycStateFcModel.doIfError(dJIDiagnosticsType15, FlightControllerDiagnosticsHandler$$Lambda$114.get$Lambda(diagnosisList));
        DiagnosticsFlycStateFcModel diagnosticsFlycStateFcModel2 = this.mDiagnosticsGpsExitFcModel;
        DJIDiagnosticsType dJIDiagnosticsType16 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsFlycStateFcModel2.doIfError(dJIDiagnosticsType16, FlightControllerDiagnosticsHandler$$Lambda$115.get$Lambda(diagnosisList));
        DiagnosticsModel<DataFlycGetPushSmartBattery> diagnosticsModel2 = this.mSmartBatteryNeedGoHomeModel;
        DJIDiagnosticsType dJIDiagnosticsType17 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsModel2.doIfError(dJIDiagnosticsType17, FlightControllerDiagnosticsHandler$$Lambda$116.get$Lambda(diagnosisList));
        return diagnosisList;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushCheckStatus status) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$117(this, status));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$65$FlightControllerDiagnosticsHandler(DataFlycGetPushCheckStatus status) {
        boolean changed = false;
        for (DiagnosticsModel<DataFlycGetPushCheckStatus> diagnosticsFlycStatusModel : this.mDiagnosticsFlycStatusModels) {
            changed |= diagnosticsFlycStatusModel.statusApply(status);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome homeData) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$118(this, homeData));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$66$FlightControllerDiagnosticsHandler(DataOsdGetPushHome homeData) {
        boolean changed = false;
        for (DiagnosticsModel<DataOsdGetPushHome> model : this.mDiagnosticsGetPushHomeModels) {
            changed |= model.statusApply(homeData);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon pushCommon) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$119(this, pushCommon));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$67$FlightControllerDiagnosticsHandler(DataOsdGetPushCommon pushCommon) {
        boolean changed = this.mDiagnosticsLowPowHintModel.statusApply(pushCommon) | this.mDiagnosticsFlyAttiModel.statusApply(pushCommon);
        for (DiagnosticsModel<DataOsdGetPushCommon> model : this.mDiagnosticsGetPushCommonModels) {
            changed |= model.statusApply(pushCommon);
        }
        if ((changed | this.mMotorStartFailedModel.statusApply(pushCommon) | this.mMotorStopModel.statusApply(pushCommon.getMotorFailReason()) | this.mDiagnosticsOnlySupportAttiModel.statusApply(pushCommon)) || this.mDiagnosticsGpsExitFcModel.statusApply(pushCommon)) {
            notifyChange();
        }
        applyRedundancyError(pushCommon);
    }

    private void applyRedundancyError(DataOsdGetPushCommon pushCommon) {
        DataOsdGetPushCommon.MotorStartFailedCause currentCause = pushCommon.getMotorStartCauseNoStartAction();
        if (currentCause != this.mLastMotorFailedCause) {
            this.mLastMotorFailedCause = currentCause;
            if (this.mRedundancyPollDispose != null) {
                this.mRedundancyPollDispose.dispose();
            }
            if (currentCause == DataOsdGetPushCommon.MotorStartFailedCause.NS_ABNORMAL) {
                this.mRedundancyPollDispose = fetchRedundancyErrorInfo().toObservable().repeatWhen(FlightControllerDiagnosticsHandler$$Lambda$120.$instance).subscribe(new FlightControllerDiagnosticsHandler$$Lambda$121(this), FlightControllerDiagnosticsHandler$$Lambda$122.$instance);
            } else if (this.mRedundancyModel.statusApply((Set<RedundancyErrorInfo>) Collections.emptySet())) {
                notifyChange();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$applyRedundancyError$70$FlightControllerDiagnosticsHandler(Set set) throws Exception {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$130(this, set));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$69$FlightControllerDiagnosticsHandler(Set set) {
        if (this.mLastMotorFailedCause != DataOsdGetPushCommon.MotorStartFailedCause.NS_ABNORMAL) {
            set.clear();
        }
        if (this.mRedundancyModel.statusApply((Set<RedundancyErrorInfo>) set)) {
            notifyChange();
        }
    }

    static final /* synthetic */ void lambda$applyRedundancyError$71$FlightControllerDiagnosticsHandler(Throwable throwable) throws Exception {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushForbidStatus forbidStatus) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$123(this, forbidStatus));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$72$FlightControllerDiagnosticsHandler(DataFlycGetPushForbidStatus forbidStatus) {
        if (this.mDiagnosticsFlyAttiModel.limitHeightApply(Integer.valueOf(forbidStatus.getLimitMaxHeight()))) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushFlycInstallError installError) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$124(this, installError));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$73$FlightControllerDiagnosticsHandler(DataFlycGetPushFlycInstallError installError) {
        boolean changed = false;
        for (DiagnosticsModel<DataFlycGetPushFlycInstallError> model : this.mDiagnosticsFlycInstallModels) {
            changed |= model.statusApply(installError);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataADSBGetPushWarning installError) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$125(this, installError));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$74$FlightControllerDiagnosticsHandler(DataADSBGetPushWarning installError) {
        boolean changed = false;
        for (DiagnosticsModel<DataADSBGetPushWarning> model : this.mDiagnosticsADSModels) {
            changed |= model.statusApply(installError);
        }
        if (changed) {
            notifyChange();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushSmartBattery smartBattery) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$126(this, smartBattery));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onEvent3BackgroundThread$75$FlightControllerDiagnosticsHandler(DataFlycGetPushSmartBattery smartBattery) {
        if (this.mSmartBatteryNeedGoHomeModel.statusApply(smartBattery)) {
            notifyChange();
        }
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        postToDiagnosticBackgroudThread(new FlightControllerDiagnosticsHandler$$Lambda$127(this, key, oldValue));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onValueChange$76$FlightControllerDiagnosticsHandler(DJISDKCacheKey key, DJISDKCacheParamValue oldValue) {
        if (key.equals(this.mIMURedundancyStatusKey)) {
            onIMURedundancyStatus();
        }
        if (key.equals(this.mCompassStatusKey)) {
            onCompassStatus();
        }
        if (key.equals(this.mRcModeChannelKey)) {
            onRcModeChannel(oldValue);
        }
    }

    private void onRcModeChannel(DJISDKCacheParamValue oldValue) {
        if (CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.MULTI_MODE_OPEN)) && oldValue != null) {
            if (this.mRcModeModel.statusApply((DataOsdGetPushCommon.RcModeChannel) CacheHelper.getValue(this.mRcModeChannelKey, DataOsdGetPushCommon.RcModeChannel.CHANNEL_UNKNOWN))) {
                notifyChange();
            }
        }
    }

    private boolean isUsingSoftJoyStick() {
        return CommonUtil.isWM160(null);
    }

    private void onCompassStatus() {
        int compassStatus = CacheHelper.toInt(CacheHelper.getValue(this.mCompassStatusKey));
        boolean changed = false;
        for (DiagnosticsModel<Integer> model : this.mCompassCheckStatusModels) {
            changed |= model.statusApply(Integer.valueOf(compassStatus));
        }
        if (changed) {
            notifyChange();
        }
    }

    private void onIMURedundancyStatus() {
        if (supportRedundancySensor()) {
            int imuStatus = CacheHelper.toInt(CacheHelper.getValue(this.mIMURedundancyStatusKey));
            if (this.mDiagnosticsImuErrorModel.statusApply(Integer.valueOf(imuStatus)) || this.mDiagnosticsImuStatusModels.statusApply(Integer.valueOf(imuStatus))) {
                notifyChange();
            }
        }
    }

    private Maybe<Set<RedundancyErrorInfo>> fetchRedundancyErrorInfo() {
        return Maybe.create(new FlightControllerDiagnosticsHandler$$Lambda$128(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$fetchRedundancyErrorInfo$77$FlightControllerDiagnosticsHandler(final MaybeEmitter emitter) throws Exception {
        final DataFlycRedundancyStatus reduSta = DataFlycRedundancyStatus.getInstance();
        reduSta.setCmdType(DataFlycRedundancyStatus.RS_CMD_TYPE.ASK_ERR_STATE).start(new DJIDataCallBack() {
            /* class dji.internal.diagnostics.handler.FlightControllerDiagnosticsHandler.AnonymousClass1 */

            public void onSuccess(Object model) {
                DiagnosticsLog.logi(FlightControllerDiagnosticsHandler.TAG, "DataFlycRedundancyStatus success");
                List imuStatus = reduSta.getIMUStatus();
                Set infoHashSet = new HashSet();
                if (FlightControllerDiagnosticsHandler.this.mContext == null) {
                    FlightControllerDiagnosticsHandler.this.obtainContext();
                    DiagnosticsLog.loge(FlightControllerDiagnosticsHandler.TAG, "DataFlycRedundancyStatus success, but context = null");
                    emitter.onComplete();
                    return;
                }
                for (DataFlycRedundancyStatus.IMUStatus status : imuStatus) {
                    RedundancyErrorInfo Info = RedundancySystemAdapter.getRedundancyErrorInfo(FlightControllerDiagnosticsHandler.this.mContext, status);
                    if (Info.getDiagnosticsCode() == 0) {
                        DiagnosticsLog.loge(FlightControllerDiagnosticsHandler.TAG, String.format("imu status can't be not matched to diagnostics code,dev_type=[%s],error_code=[%s]", Integer.valueOf(status.devType), Integer.valueOf(status.devErrCode)));
                    } else {
                        infoHashSet.add(Info);
                    }
                }
                emitter.onSuccess(infoHashSet);
            }

            public void onFailure(Ccode ccode) {
                DiagnosticsLog.loge(FlightControllerDiagnosticsHandler.TAG, "DataFlycRedundancyStatus failed. ccode=[" + ccode + IMemberProtocol.STRING_SEPERATOR_RIGHT);
                emitter.onComplete();
            }
        });
    }

    public void reset(int componentIndex) {
        for (DiagnosticsModel<DataFlycGetPushCheckStatus> diagnosticsFlycStatusModel : this.mDiagnosticsFlycStatusModels) {
            diagnosticsFlycStatusModel.reset();
        }
        this.mDiagnosticsImuErrorModel.reset();
        this.mDiagnosticsImuStatusModels.reset();
        this.mDiagnosticsLowPowHintModel.reset();
        for (DiagnosticsModel<DataOsdGetPushHome> model : this.mDiagnosticsGetPushHomeModels) {
            model.reset();
        }
        for (DiagnosticsModel<DataOsdGetPushCommon> model2 : this.mDiagnosticsGetPushCommonModels) {
            model2.reset();
        }
        for (DiagnosticsModel<DataFlycGetPushFlycInstallError> model3 : this.mDiagnosticsFlycInstallModels) {
            model3.reset();
        }
        for (DiagnosticsModel<DataADSBGetPushWarning> model4 : this.mDiagnosticsADSModels) {
            model4.reset();
        }
        for (DiagnosticsModel<Integer> model5 : this.mCompassCheckStatusModels) {
            model5.reset();
        }
        this.mDiagnosticsFlyAttiModel.reset();
        this.mMotorStartFailedModel.reset();
        this.mMotorStopModel.reset();
        this.mRedundancyModel.reset();
        this.mRcModeModel.reset();
        this.mDiagnosticsOnlySupportAttiModel.reset();
        this.mDiagnosticsGpsExitFcModel.reset();
        this.mSmartBatteryNeedGoHomeModel.reset();
        if (this.mRedundancyPollDispose != null) {
            this.mRedundancyPollDispose.dispose();
        }
    }

    /* access modifiers changed from: private */
    public void obtainContext() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            AndroidSchedulers.mainThread().scheduleDirect(new FlightControllerDiagnosticsHandler$$Lambda$129(this));
        } else {
            this.mContext = ContextUtil.getContext();
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$obtainContext$78$FlightControllerDiagnosticsHandler() {
        this.mContext = ContextUtil.getContext();
    }

    private boolean supportRedundancySensor() {
        return ((Model) CacheHelper.getValue(KeyHelper.getProductKey(ProductKeys.MODEL_NAME))) == Model.Spark || CommonUtil.supportRedundancySenor();
    }

    public static boolean checkGpsValid(DataOsdGetPushCommon common) {
        if (!common.isGetted()) {
            return false;
        }
        if (CommonUtil.isOldMC(common.getDroneType()) || common.getFlycVersion() < 6) {
            return CommonUtil.checkGpsNumValid(common.getGpsNum());
        }
        return common.getGpsLevel() >= 3;
    }

    private boolean isLandingMode(DataOsdGetPushCommon.FLYC_STATE state) {
        if (state == DataOsdGetPushCommon.FLYC_STATE.AutoLanding || state == DataOsdGetPushCommon.FLYC_STATE.AttiLangding || state == DataOsdGetPushCommon.FLYC_STATE.FORCE_LANDING) {
            return true;
        }
        return false;
    }

    private boolean isMotorUp() {
        return DataOsdGetPushCommon.getInstance().isGetted() && DataOsdGetPushCommon.getInstance().isMotorUp();
    }

    private double calculateDistanceBetweenHomeAndAircraft() {
        Double homeLatitude = (Double) CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.HOME_LOCATION_LATITUDE), Double.valueOf(Double.MIN_NORMAL));
        Double homeLongitude = (Double) CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.HOME_LOCATION_LONGITUDE), Double.valueOf(Double.MIN_NORMAL));
        Double aircraftLatitude = (Double) CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.AIRCRAFT_LOCATION_LATITUDE), Double.valueOf(Double.MIN_NORMAL));
        Double aircraftLongitude = (Double) CacheHelper.getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.AIRCRAFT_LOCATION_LONGITUDE), Double.valueOf(Double.MIN_NORMAL));
        if (!(LocationUtils.checkValidGPSCoordinate(homeLatitude.doubleValue(), homeLongitude.doubleValue()) && LocationUtils.checkValidGPSCoordinate(aircraftLatitude.doubleValue(), aircraftLongitude.doubleValue()))) {
            return INVALID_DISTANCE;
        }
        return LocationUtils.gps2m(homeLatitude.doubleValue(), homeLongitude.doubleValue(), aircraftLatitude.doubleValue(), aircraftLongitude.doubleValue());
    }

    private ExtraHeightRadiusLimit getLimitExtra() {
        return new ExtraHeightRadiusLimit(CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.PROP_COVER_LIMIT_HEIGHT)), CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.PROP_COVER_LIMIT_RADIUS)));
    }
}
