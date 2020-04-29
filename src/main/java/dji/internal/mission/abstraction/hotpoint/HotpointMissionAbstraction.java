package dji.internal.mission.abstraction.hotpoint;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import dji.common.bus.MissionEventBus;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.flightcontroller.FlightMode;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.mission.MissionUtils;
import dji.common.mission.hotpoint.HotpointExecutionData;
import dji.common.mission.hotpoint.HotpointHeading;
import dji.common.mission.hotpoint.HotpointMission;
import dji.common.mission.hotpoint.HotpointStartPoint;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;
import dji.internal.mission.abstraction.hotpoint.HotpointAbstractionDataHolder;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataFlycCancelHotPointMission;
import dji.midware.data.model.P3.DataFlycGetHPMaxAngularVelocity;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataFlycHotPointMissionDownload;
import dji.midware.data.model.P3.DataFlycHotPointMissionSwitch;
import dji.midware.data.model.P3.DataFlycHotPointResetCamera;
import dji.midware.data.model.P3.DataFlycHotPointResetParams;
import dji.midware.data.model.P3.DataFlycHotPointResetRadius;
import dji.midware.data.model.P3.DataFlycStartHotPointMissionWithInfo;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class HotpointMissionAbstraction extends BaseMissionAbstraction {
    private static final int HOTPOINT_TYPE = 2;
    private static final int INITIALIZING = 0;
    private static final int MOVING = 1;
    private static final int NO_MISSION = 0;
    private static final int PAUSED = 2;
    /* access modifiers changed from: private */
    public HotpointAbstractionCacheData cacheData = new HotpointAbstractionCacheData();
    /* access modifiers changed from: private */
    public MissionState candidateState;
    private DJIParamAccessListener connectionListener = new HotpointMissionAbstraction$$Lambda$0(this);
    /* access modifiers changed from: private */
    public boolean isDownloading;
    /* access modifiers changed from: private */
    public boolean isRecovering;
    private boolean isSpeedUpdating;
    private int previousMission = 0;

    public HotpointMissionAbstraction() {
        reset();
        tryRecoverMissionState();
        startListen();
        refreshEventBusInformation();
    }

    private void startListen() {
        DJIEventBusUtil.register(this);
        CacheHelper.addProductListener(this.connectionListener, ProductKeys.MODEL_NAME);
    }

    private void refreshEventBusInformation() {
        if (DataFlycGetPushWayPointMissionInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushWayPointMissionInfo.getInstance());
        }
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCommon.getInstance());
        }
        if (DataOsdGetPushHome.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushHome.getInstance());
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        this.cacheData = null;
        CacheHelper.removeListener(this.connectionListener);
    }

    /* access modifiers changed from: protected */
    public FiniteStateMachine buildFSM() {
        return new FiniteStateMachine().add(MissionState.RECOVERING).to(MissionState.READY_TO_EXECUTE, MissionState.EXECUTION_PAUSED, MissionState.EXECUTING).from(MissionState.NOT_READY).to(MissionState.READY_TO_EXECUTE).from(MissionState.READY_TO_EXECUTE).to(MissionState.READY_TO_EXECUTE, MissionState.EXECUTION_STARTING, MissionState.INITIAL_PHASE, MissionState.EXECUTING).from(MissionState.EXECUTION_STARTING).to(MissionState.EXECUTING, MissionState.READY_TO_EXECUTE, MissionState.INITIAL_PHASE).from(MissionState.INITIAL_PHASE).to(MissionState.EXECUTION_STOPPING, MissionState.INITIAL_PHASE, MissionState.EXECUTION_PAUSING, MissionState.EXECUTING, MissionState.EXECUTION_PAUSED, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTING).to(MissionState.EXECUTING, MissionState.EXECUTION_PAUSING, MissionState.EXECUTION_STOPPING, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTION_PAUSING).to(MissionState.EXECUTION_PAUSED, MissionState.EXECUTING, MissionState.INITIAL_PHASE, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTION_PAUSED).to(MissionState.EXECUTION_RESUMING, MissionState.EXECUTION_STOPPING, MissionState.EXECUTION_PAUSED, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTION_RESUMING).to(MissionState.EXECUTING, MissionState.EXECUTION_PAUSED, MissionState.READY_TO_EXECUTE, MissionState.INITIAL_PHASE).from(MissionState.EXECUTION_STOPPING).to(MissionState.READY_TO_EXECUTE, MissionState.EXECUTION_PAUSED, MissionState.EXECUTING, MissionState.INITIAL_PHASE).fromAll().to(MissionState.NOT_READY).fromAll().to(MissionState.DISCONNECTED).superState(MissionState.NOT_SUPPORTED);
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, MissionEvent event) {
        return transitToState(state, new HotpointAbstractionDataHolder.HotpointBuilder(event));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$HotpointMissionAbstraction(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (key != null && key.getParamKey().equals(ProductKeys.MODEL_NAME)) {
            if (newValue == null || newValue.getData() == null) {
                reset();
                transitToState(MissionState.DISCONNECTED, MissionEvent.DISCONNECTED);
                return;
            }
            tryRecoverMissionState();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon param) {
        if (param.isGetted() && this.cacheData != null) {
            if (!(this.cacheData.getRcMode() == null || this.cacheData.getRcMode() == param.getModeChannel())) {
                transitToState(MissionState.NOT_READY, MissionEvent.RC_MODE_CHANGED);
            }
            this.cacheData.setRcMode(param.getModeChannel());
            this.cacheData.setFcMode(param.getFlycState());
            tryRecoverMissionState();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome param) {
        if (param.isGetted() && this.cacheData != null) {
            if (this.cacheData.isNavigationEnabled() == null || this.cacheData.isNavigationEnabled().booleanValue() != param.isFlycInNavigationMode()) {
                if (param.isFlycInNavigationMode()) {
                    transitToState(MissionState.READY_TO_EXECUTE, MissionEvent.ENTER_NAVIGATION_MODE);
                    CommonCallbacks.CompletionCallback callback = resetListeningCompletionCallback();
                    if (callback != null) {
                        this.handler.removeCallbacks(this.startingNavigationModeListerningRunnable);
                        CallbackUtils.onSuccess(callback);
                    }
                } else {
                    transitToState(MissionState.NOT_READY, MissionEvent.EXIT_NAVIGATION_MODE);
                }
            }
            this.cacheData.setNavigationEnabled(param.isFlycInNavigationMode());
            tryRecoverMissionState();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataFlycGetPushWayPointMissionInfo param) {
        HotpointAbstractionDataHolder.HotpointBuilder holder;
        if (param.getMissionType() == 2) {
            MissionState state = getStateFromMissionStatus(param);
            if (this.previousMission == 0) {
                holder = new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_STARTED);
            } else {
                holder = new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE);
            }
            if (this.cacheData != null) {
                this.cacheData.renewMissionDate();
            }
            holder.extra(new HotpointExecutionData(param));
            if (state.equals(MissionState.EXECUTING) || state.equals(MissionState.INITIAL_PHASE) || state.equals(MissionState.EXECUTION_PAUSED)) {
                transitToState(state, holder);
            }
        } else {
            if (this.cacheData != null) {
                this.cacheData.replaceMission(null);
            }
            MissionState currentState = getFSMState();
            if (currentState.equals(MissionState.EXECUTION_STOPPING)) {
                transitToState(MissionState.READY_TO_EXECUTE, MissionEvent.EXECUTION_STOPPED);
            } else if (currentState.equals(MissionState.EXECUTING)) {
                transitToState(MissionState.READY_TO_EXECUTE, MissionEvent.EXECUTION_INTERRUPTED);
            }
        }
        this.previousMission = param.getMissionType();
        if (this.cacheData != null) {
            this.cacheData.setMissionStatus(param);
            tryRecoverMissionState();
        }
    }

    private MissionState getStateFromMissionStatus(DataFlycGetPushWayPointMissionInfo missionInfo) {
        MissionState state = MissionState.UNKNOWN;
        if (missionInfo != null) {
            switch (missionInfo.getHotPointMissionStatus()) {
                case 0:
                    state = MissionState.INITIAL_PHASE;
                    break;
                case 1:
                    state = MissionState.EXECUTING;
                    break;
                case 2:
                    state = MissionState.EXECUTION_PAUSED;
                    break;
            }
        }
        if (getFSMState().equals(MissionState.EXECUTION_PAUSING) && state.equals(MissionState.EXECUTING)) {
            return MissionState.EXECUTION_PAUSING;
        }
        if (getFSMState().equals(MissionState.EXECUTION_STOPPING) && state.equals(MissionState.EXECUTING)) {
            return MissionState.EXECUTION_STOPPING;
        }
        if (getFSMState().equals(MissionState.EXECUTION_RESUMING) && state.equals(MissionState.EXECUTION_PAUSED)) {
            return MissionState.EXECUTION_RESUMING;
        }
        if (!getFSMState().equals(MissionState.EXECUTION_STOPPING) || !state.equals(MissionState.EXECUTION_PAUSED)) {
            return state;
        }
        return MissionState.EXECUTION_STOPPING;
    }

    public void notifyListener(AbstractionDataHolder holder) {
        if (!holder.equals(this.previousDataHolder)) {
            this.previousDataHolder = holder;
            MissionEventBus.getInstance().post(holder);
        }
    }

    public void reset() {
        this.cacheData.reset();
        this.isRecovering = false;
        this.candidateState = MissionState.RECOVERING;
    }

    public void tryRecoverMissionState() {
        if (this.fsm.getState() == null) {
            if (DJIProductManager.getInstance().getType() != ProductType.OTHER) {
                this.fsm.forceTransitTo(MissionState.RECOVERING);
            } else if (isOsmo()) {
                this.fsm.forceTransitTo(MissionState.NOT_SUPPORTED);
            } else {
                this.fsm.forceTransitTo(MissionState.DISCONNECTED);
            }
        }
        MissionState curState = getFSMState();
        if (curState.equals(MissionState.DISCONNECTED) || curState.equals(MissionState.RECOVERING) || curState.equals(MissionState.NOT_SUPPORTED)) {
            MissionState state = computeCurrentState();
            if (!this.cacheData.isMissionValid()) {
                this.candidateState = state;
                if (canDownloadMission(this.candidateState)) {
                    recoverMission();
                }
            }
            if (curState.equals(state)) {
                return;
            }
            if (curState.equals(MissionState.RECOVERING)) {
                forceTransitToState(state, MissionEvent.INITIALIZED);
            } else {
                forceTransitToState(state, MissionEvent.CONNECTED);
            }
        }
    }

    private MissionState computeCurrentState() {
        if (DJIProductManager.getInstance().getType() == ProductType.OTHER) {
            return MissionState.DISCONNECTED;
        }
        if (isOsmo() || !doesProductSupportNavigationMode()) {
            return MissionState.NOT_SUPPORTED;
        }
        FlightMode flightControllerFlightMode = (FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE);
        if (flightControllerFlightMode == null) {
            return MissionState.RECOVERING;
        }
        if (!CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.NAVIGATION_MODE_ENABLED))) {
            return MissionState.NOT_READY;
        }
        MissionState result = MissionState.READY_TO_EXECUTE;
        boolean isRawMissionStatusInitialized = DataFlycGetPushWayPointMissionInfo.getInstance().isGetted();
        if (!this.cacheData.isMissionStatusInitialized() && !isRawMissionStatusInitialized && this.cacheData.getFcMode() != null) {
            return MissionState.RECOVERING;
        }
        if ((this.cacheData.isMissionStatusInitialized() || isRawMissionStatusInitialized) && flightControllerFlightMode.equals(FlightMode.GPS_HOT_POINT)) {
            result = MissionState.EXECUTING;
        }
        if (!result.equals(MissionState.EXECUTING) || DataFlycGetPushWayPointMissionInfo.getInstance().getHotPointMissionStatus() != 2) {
            return result;
        }
        return MissionState.EXECUTION_PAUSED;
    }

    /* access modifiers changed from: private */
    public void recoverMission() {
        if (!this.isRecovering) {
            this.isRecovering = true;
            final HotpointMission data = new HotpointMission();
            hotPointMissionDownloader(data, new CommonCallbacks.CompletionCallback() {
                /* class dji.internal.mission.abstraction.hotpoint.HotpointMissionAbstraction.AnonymousClass1 */

                public void onResult(DJIError error) {
                    boolean stillValid = false;
                    boolean unused = HotpointMissionAbstraction.this.isRecovering = false;
                    if (HotpointMissionAbstraction.this.getFSMState().equals(MissionState.RECOVERING) && HotpointMissionAbstraction.this.canDownloadMission(HotpointMissionAbstraction.this.candidateState)) {
                        stillValid = true;
                    }
                    if (!stillValid) {
                        return;
                    }
                    if (error != null) {
                        HotpointMissionAbstraction.this.recoverMission();
                        return;
                    }
                    HotpointMissionAbstraction.this.cacheData.replaceMission(data);
                    HotpointMissionAbstraction.this.cacheData.renewMissionDate();
                    HotpointMissionAbstraction.this.forceTransitToState(HotpointMissionAbstraction.this.candidateState, MissionEvent.INITIALIZED);
                }
            });
        }
    }

    private boolean canDownloadMission() {
        return canDownloadMission(getFSMState());
    }

    public boolean canDownloadMission(MissionState currentState) {
        return currentState.equals(MissionState.EXECUTING) || currentState.equals(MissionState.EXECUTION_PAUSED) || currentState.equals(MissionState.EXECUTION_PAUSING) || currentState.equals(MissionState.EXECUTION_RESUMING) || currentState.equals(MissionState.INITIAL_PHASE);
    }

    public void downloadMission(final HotpointMission data, final CommonCallbacks.CompletionCallback callback) {
        if (!canDownloadMission() || this.isDownloading) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        this.isDownloading = true;
        hotPointMissionDownloader(data, new CommonCallbacks.CompletionCallback() {
            /* class dji.internal.mission.abstraction.hotpoint.HotpointMissionAbstraction.AnonymousClass2 */

            public void onResult(DJIError error) {
                if (error != null) {
                    boolean unused = HotpointMissionAbstraction.this.isDownloading = false;
                } else {
                    HotpointMissionAbstraction.this.cacheData.replaceMission(data);
                    HotpointMissionAbstraction.this.cacheData.renewMissionDate();
                }
                HotpointMissionAbstraction.this.triggerDownloadEvent(data, error);
                CallbackUtils.onResult(callback, error);
            }
        });
    }

    public void hotPointMissionDownloader(final HotpointMission data, final CommonCallbacks.CompletionCallback callback) {
        final DataFlycHotPointMissionDownload hotPointMissionDownload = DataFlycHotPointMissionDownload.getInstance();
        hotPointMissionDownload.start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.hotpoint.HotpointMissionAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                boolean z = true;
                boolean unused = HotpointMissionAbstraction.this.isDownloading = false;
                if (hotPointMissionDownload.getResult() == 0) {
                    data.setHotpoint(new LocationCoordinate2D(MissionUtils.Degree(hotPointMissionDownload.getHotPointLatitude()), MissionUtils.Degree(hotPointMissionDownload.getHotPointLongitude())));
                    data.setAltitude(hotPointMissionDownload.getHotPointAttitude());
                    data.setRadius(hotPointMissionDownload.getHotPointRadius());
                    data.setAngularVelocity(hotPointMissionDownload.getHotPointAngleSpeed());
                    HotpointMission hotpointMission = data;
                    if (hotPointMissionDownload.getHotPointClockWise().value() != 1) {
                        z = false;
                    }
                    hotpointMission.setClockwise(z);
                    if (data.isClockwise()) {
                        data.setAngularVelocity(data.getAngularVelocity() * -1.0f);
                    }
                    data.setStartPoint(HotpointStartPoint.find(hotPointMissionDownload.getHotPointToStartPointMode().value()));
                    data.setHeading(HotpointHeading.find(hotPointMissionDownload.getHotPointCameraDir().value()));
                    CallbackUtils.onSuccess(callback);
                    return;
                }
                CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: private */
    public void triggerDownloadEvent(HotpointMission data, DJIError error) {
        MissionEvent event;
        if (error != null) {
            event = MissionEvent.DOWNLOAD_FAILED;
        } else {
            event = MissionEvent.DOWNLOAD_DONE;
        }
        HotpointAbstractionDataHolder.HotpointBuilder holder = new HotpointAbstractionDataHolder.HotpointBuilder(event);
        holder.extra(data);
        holder.currentState(getFSMState());
        notifyListener(holder.build());
    }

    public void startMission(HotpointMission data, CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STARTING, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (data != null && isValidParameters(data)) {
            DataFlycStartHotPointMissionWithInfo info = DataFlycStartHotPointMissionWithInfo.getInstance();
            info.setLatitude(MissionUtils.Radian(data.getHotpoint().getLatitude()));
            info.setLongitude(MissionUtils.Radian(data.getHotpoint().getLongitude()));
            info.setAltitude(data.getAltitude());
            info.setRadious(data.getRadius());
            info.setVelocity(data.getAngularVelocity());
            info.setRotationDir(DataFlycStartHotPointMissionWithInfo.ROTATION_DIR.find(data.isClockwise() ? 1 : 0));
            info.setCameraDir(DataFlycStartHotPointMissionWithInfo.CAMERA_DIR.find(data.getHeading().value()));
            info.setToStartPointMode(DataFlycStartHotPointMissionWithInfo.TO_START_POINT_MODE.find(data.getStartPoint().value()));
            info.start(getDataCallbackForTempState(new HotpointMissionAbstraction$$Lambda$1(info), MissionState.EXECUTION_STARTING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.READY_TO_EXECUTE, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_START_FAILED), callback));
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_PARAMETERS_INVALID);
        }
    }

    public void pauseMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_PAUSING, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycHotPointMissionSwitch missionSwitch = DataFlycHotPointMissionSwitch.getInstance();
        missionSwitch.setSwitch(DataFlycHotPointMissionSwitch.HOTPOINTMISSIONSWITCH.PAUSE);
        missionSwitch.start(getDataCallbackForTempState(new HotpointMissionAbstraction$$Lambda$2(missionSwitch), MissionState.EXECUTION_PAUSING, desiredMissionStatesHelper(MissionState.EXECUTION_PAUSED), MissionState.EXECUTING, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_PAUSE_FAILED), callback));
    }

    public void resumeMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_RESUMING, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycHotPointMissionSwitch missionSwitch = DataFlycHotPointMissionSwitch.getInstance();
        missionSwitch.setSwitch(DataFlycHotPointMissionSwitch.HOTPOINTMISSIONSWITCH.RESUME);
        missionSwitch.start(getDataCallbackForTempState(new HotpointMissionAbstraction$$Lambda$3(missionSwitch), MissionState.EXECUTION_RESUMING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.EXECUTION_PAUSED, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_RESUME_FAILED), callback));
    }

    public void stopMission(CommonCallbacks.CompletionCallback callback) {
        MissionState previousState = getFSMState();
        if (!tryTransitToTempState(MissionState.EXECUTION_STOPPING, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycCancelHotPointMission mission = DataFlycCancelHotPointMission.getInstance();
        mission.start(getDataCallbackForTempState(new HotpointMissionAbstraction$$Lambda$4(mission), MissionState.EXECUTION_STOPPING, desiredMissionStatesHelper(MissionState.READY_TO_UPLOAD), previousState, new HotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_STOP_FAILED), callback));
    }

    public boolean canUpdateMissionParameters() {
        return canDownloadMission(getFSMState());
    }

    public void updateVelocity(float angularVelocity, CommonCallbacks.CompletionCallback callback) {
        int i = 1;
        if (!canUpdateMissionParameters() || this.isSpeedUpdating) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        this.isSpeedUpdating = true;
        DataFlycHotPointResetParams params = DataFlycHotPointResetParams.getInstance();
        if (angularVelocity >= 0.0f) {
            i = 0;
        }
        params.setRotationDir(DataFlycStartHotPointMissionWithInfo.ROTATION_DIR.find(i));
        params.setVelocity(Math.abs(angularVelocity));
        params.start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.hotpoint.HotpointMissionAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
        this.tempStateTimer.startTimer(MissionState.EXECUTING, false, 0.5d, new HotpointMissionAbstraction$$Lambda$5(this, angularVelocity, callback));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$updateVelocity$5$HotpointMissionAbstraction(float angularVelocity, CommonCallbacks.CompletionCallback callback) {
        this.isSpeedUpdating = false;
        if (((double) Math.abs(((float) MissionUtils.Degree((double) ((((float) this.cacheData.getMissionStatus().getHotPointSpeed()) * 0.1f) / (((float) this.cacheData.getMissionStatus().getHotPointRadius()) / 100.0f)))) - Math.abs(angularVelocity))) < 1.0d) {
            CallbackUtils.onSuccess(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void updateRadius(float radius, CommonCallbacks.CompletionCallback callback) {
        if (!canUpdateMissionParameters() || this.isSpeedUpdating) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (((double) radius) < 5.0d || ((double) radius) > 500.0d) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_PARAMETERS_INVALID);
        } else {
            this.isSpeedUpdating = true;
            DataFlycHotPointResetRadius radiusResetTo = DataFlycHotPointResetRadius.getInstance();
            radiusResetTo.setRadius(radius);
            radiusResetTo.start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.hotpoint.HotpointMissionAbstraction.AnonymousClass5 */

                public void onSuccess(Object model) {
                }

                public void onFailure(Ccode ccode) {
                }
            });
            this.tempStateTimer.startTimer(MissionState.EXECUTING, false, 0.5d, new HotpointMissionAbstraction$$Lambda$6(this, radius, callback));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$updateRadius$6$HotpointMissionAbstraction(float radius, CommonCallbacks.CompletionCallback callback) {
        this.isSpeedUpdating = false;
        if (Math.abs(((double) (((float) this.cacheData.getMissionStatus().getHotPointRadius()) / 100.0f)) - ((double) radius)) < 0.1d) {
            CallbackUtils.onSuccess(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void resetHeading(CommonCallbacks.CompletionCallback callback) {
        if (!canUpdateMissionParameters()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            DataFlycHotPointResetCamera.getInstance().start(CallbackUtils.getDJIDataCallback(callback));
        }
    }

    public static void getMaxAngularVelocityForRadius(@FloatRange(from = 5.0d, to = 500.0d) final double radius, @NonNull final CommonCallbacks.CompletionCallbackWith<Float> callback) {
        String fcFirmwareVersion = (String) CacheHelper.getFlightController(DJISDKCacheKeys.FIRMWARE_VERSION);
        if (fcFirmwareVersion == null || "03.03.05".compareTo(fcFirmwareVersion) > 0) {
            CallbackUtils.onSuccess(callback, Float.valueOf(maxAngularVelocityForRadius(radius)));
            return;
        }
        final DataFlycGetHPMaxAngularVelocity dataGet = new DataFlycGetHPMaxAngularVelocity();
        dataGet.setRadius(radius).start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.hotpoint.HotpointMissionAbstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                if (dataGet.getResult() == 0) {
                    CallbackUtils.onSuccess(callback, Float.valueOf(((float) MissionUtils.Degree(((double) dataGet.getMaxAngularVelocity()) / radius)) - 5.0E-6f));
                } else {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public static float maxAngularVelocityForRadius(double radius) {
        float acc;
        if (radius < 5.0d || radius > 500.0d) {
            return 0.0f;
        }
        float maxAcc = 3.5f;
        float minRadius = 5.0f;
        DJISDKCacheParamValue value = CacheHelper.getValueSynchronously(KeyHelper.getFlightControllerKey(FlightControllerKeys.HOTPOINT_MAX_ACCELERATION), DJIVideoDecoder.connectLosedelay);
        Float max = Float.valueOf(3.5f);
        if (value != null) {
            max = (Float) value.getData();
        }
        if (max != null) {
            maxAcc = max.floatValue();
        }
        DJISDKCacheParamValue minValue = CacheHelper.getValueSynchronously(KeyHelper.getFlightControllerKey(FlightControllerKeys.HOTPOINT_MIN_RADIUS), DJIVideoDecoder.connectLosedelay);
        Float min = null;
        if (minValue != null) {
            min = (Float) minValue.getData();
        }
        if (min != null) {
            minRadius = min.floatValue();
        }
        if (radius >= ((double) (25.0f + minRadius))) {
            acc = maxAcc;
        } else {
            acc = (float) ((((radius - ((double) minRadius)) / 25.0d) * ((double) (maxAcc - 2.0f))) + 2.0d);
        }
        double Vmax = Math.sqrt(((double) acc) * radius);
        if (Vmax > 10.0d) {
            Vmax = 10.0d;
        }
        return (float) (Math.floor(Math.toDegrees(Vmax / radius) * 10.0d) / 10.0d);
    }

    public static float getMaxAngularVelocitySynchronously(double radius) {
        String fcFirmwareVersion = (String) CacheHelper.getFlightController(DJISDKCacheKeys.FIRMWARE_VERSION);
        if (fcFirmwareVersion != null && "03.03.05".compareTo(fcFirmwareVersion) <= 0) {
            final CountDownLatch cdl = new CountDownLatch(1);
            final DataFlycGetHPMaxAngularVelocity dataGet = new DataFlycGetHPMaxAngularVelocity();
            final float[] curVelocity = {0.0f};
            final double d = radius;
            dataGet.setRadius(radius).start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.hotpoint.HotpointMissionAbstraction.AnonymousClass7 */

                public void onSuccess(Object model) {
                    if (dataGet.getResult() == 0) {
                        curVelocity[0] = ((float) MissionUtils.Degree(((double) dataGet.getMaxAngularVelocity()) / d)) - 5.0E-6f;
                    }
                    cdl.countDown();
                }

                public void onFailure(Ccode ccode) {
                    cdl.countDown();
                }
            });
            try {
                cdl.await(500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (curVelocity[0] > 0.0f) {
                return curVelocity[0];
            }
        }
        return maxAngularVelocityForRadius(radius);
    }

    private boolean isValidParameters(HotpointMission data) {
        if (data.getHotpoint() == null || data.getHeading() == null || data.getStartPoint() == null || !MissionUtils.checkValidGPSCoordinate(data.getHotpoint().getLatitude(), data.getHotpoint().getLongitude()) || data.getRadius() < 5.0d || data.getRadius() > 500.0d || data.getAltitude() < 5.0d || data.getAltitude() > 500.0d) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean doesProductSupportNavigationMode() {
        DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
        if (droneType == DataOsdGetPushCommon.DroneType.PomatoRTK || droneType == DataOsdGetPushCommon.DroneType.WM160) {
            return false;
        }
        return super.doesProductSupportNavigationMode();
    }
}
