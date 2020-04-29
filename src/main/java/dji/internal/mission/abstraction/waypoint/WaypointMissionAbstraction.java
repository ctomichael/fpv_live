package dji.internal.mission.abstraction.waypoint;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.bus.MissionEventBus;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.error.DJISDKError;
import dji.common.flightcontroller.FlightMode;
import dji.common.flightcontroller.RemoteControllerFlightMode;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.mission.MissionUtils;
import dji.common.mission.waypoint.Waypoint;
import dji.common.mission.waypoint.WaypointAction;
import dji.common.mission.waypoint.WaypointActionType;
import dji.common.mission.waypoint.WaypointDownloadProgress;
import dji.common.mission.waypoint.WaypointEvent;
import dji.common.mission.waypoint.WaypointExecutionProgress;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionExecuteState;
import dji.common.mission.waypoint.WaypointMissionFinishedAction;
import dji.common.mission.waypoint.WaypointMissionFlightPathMode;
import dji.common.mission.waypoint.WaypointMissionGotoWaypointMode;
import dji.common.mission.waypoint.WaypointMissionHeadingMode;
import dji.common.mission.waypoint.WaypointMissionInterruption;
import dji.common.mission.waypoint.WaypointTurnMode;
import dji.common.mission.waypoint.WaypointUploadProgress;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;
import dji.internal.mission.abstraction.waypoint.WaypointAbstractionDataHolder;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataFlycDownloadWayPointMissionMsg;
import dji.midware.data.model.P3.DataFlycDownloadWayPointMsgByIndex;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionCurrentEvent;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataFlycSetFlightIdleSpeed;
import dji.midware.data.model.P3.DataFlycUploadWayPointMissionMsg;
import dji.midware.data.model.P3.DataFlycUploadWayPointMsgByIndex;
import dji.midware.data.model.P3.DataFlycWayPointMissionPauseOrResume;
import dji.midware.data.model.P3.DataFlycWayPointMissionSwitch;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.model.P3.DataSpecialLockRcControl;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class WaypointMissionAbstraction extends BaseMissionAbstraction {
    private static final boolean DEBUG = true;
    private static final int EXECUTE_FINISHED = 1;
    private static final int NONE_MISSION_TYPE = 0;
    private static final int REACHED = 2;
    private static final String TAG = "WaypointMission";
    private static final int UPLOADED = 0;
    private static final String WAYPOINT_EVENT = "WaypointEvent";
    private static final String WAYPOINT_INFO = "WaypointInfo";
    private static final int WAYPOINT_TYPE = 1;
    /* access modifiers changed from: private */
    @Nullable
    public WaypointAbstractionCacheData cacheData = new WaypointAbstractionCacheData();
    private boolean canSetupEnvironementParams;
    /* access modifiers changed from: private */
    public MissionState candidateState;
    @Nullable
    private DJIParamAccessListener connectionListener = new DJIParamAccessListener() {
        /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass1 */

        public void onValueChange(@Nullable DJISDKCacheKey key, DJISDKCacheParamValue oldValue, @Nullable DJISDKCacheParamValue newValue) {
            if (key != null && key.getParamKey().equals(ProductKeys.MODEL_NAME)) {
                if (newValue == null || newValue.getData() == null) {
                    WaypointMissionAbstraction.this.reset();
                    WaypointMissionAbstraction.this.transitToState(MissionState.DISCONNECTED, WaypointEvent.DISCONNECTED);
                    return;
                }
                WaypointMissionAbstraction.this.tryResumeFSMIfNecessary();
            }
        }
    };
    /* access modifiers changed from: private */
    public int downloadRetryTimes = 0;
    /* access modifiers changed from: private */
    @NonNull
    public AtomicBoolean isDownloading = new AtomicBoolean();
    /* access modifiers changed from: private */
    @NonNull
    public AtomicBoolean isRecovering = new AtomicBoolean();
    /* access modifiers changed from: private */
    public int retryTimes = 0;
    /* access modifiers changed from: private */
    public boolean uploadingMissionStoped = false;

    static /* synthetic */ int access$1308(WaypointMissionAbstraction x0) {
        int i = x0.downloadRetryTimes;
        x0.downloadRetryTimes = i + 1;
        return i;
    }

    static /* synthetic */ int access$608(WaypointMissionAbstraction x0) {
        int i = x0.retryTimes;
        x0.retryTimes = i + 1;
        return i;
    }

    public WaypointMissionAbstraction() {
        reset();
        tryResumeFSMIfNecessary();
        startListen();
        refreshEventBusInformation();
    }

    private void startListen() {
        DJIEventBusUtil.register(this);
        CacheHelper.addProductListener(this.connectionListener, ProductKeys.MODEL_NAME);
        CacheHelper.addFlightControllerListener(this.connectionListener, FlightControllerKeys.WAYPOINT_PROTOCOL_VERSION, FlightControllerKeys.MAX_WAYPOINT_NUM);
    }

    private void refreshEventBusInformation() {
        if (DataFlycGetPushWayPointMissionCurrentEvent.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushWayPointMissionCurrentEvent.getInstance());
        }
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
    @NonNull
    public FiniteStateMachine buildFSM() {
        return new FiniteStateMachine().from(MissionState.NOT_READY).to(MissionState.READY_TO_UPLOAD).from(MissionState.READY_TO_UPLOAD).to(MissionState.UPLOAD_STARTING).from(MissionState.READY_TO_RETRY_UPLOAD).to(MissionState.UPLOAD_STARTING).from(MissionState.UPLOAD_STARTING).to(MissionState.READY_TO_UPLOAD, MissionState.READY_TO_RETRY_UPLOAD, MissionState.UPLOADING).from(MissionState.UPLOADING).to(MissionState.UPLOADING, MissionState.READY_TO_UPLOAD, MissionState.READY_TO_RETRY_UPLOAD, MissionState.READY_TO_EXECUTE).from(MissionState.READY_TO_EXECUTE).to(MissionState.READY_TO_EXECUTE, MissionState.EXECUTION_STARTING, MissionState.UPLOAD_STARTING).from(MissionState.EXECUTION_STARTING).to(MissionState.EXECUTING, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTING).to(MissionState.EXECUTING, MissionState.EXECUTION_PAUSING, MissionState.EXECUTION_PAUSED, MissionState.EXECUTION_STOPPING, MissionState.READY_TO_UPLOAD).from(MissionState.EXECUTION_PAUSING).to(MissionState.EXECUTION_PAUSED, MissionState.EXECUTING).from(MissionState.EXECUTION_PAUSED).to(MissionState.EXECUTION_PAUSED, MissionState.EXECUTING, MissionState.EXECUTION_RESUMING, MissionState.EXECUTION_STOPPING).from(MissionState.EXECUTION_RESUMING).to(MissionState.EXECUTING, MissionState.EXECUTION_PAUSED).from(MissionState.EXECUTION_STOPPING).to(MissionState.READY_TO_UPLOAD, MissionState.EXECUTION_PAUSED, MissionState.EXECUTING).fromAll().to(MissionState.NOT_READY).fromAll().to(MissionState.DISCONNECTED).add(MissionState.RECOVERING).superState(MissionState.NOT_SUPPORTED);
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, MissionEvent event) {
        return transitToState(state, new WaypointAbstractionDataHolder.WaypointBuilder(event));
    }

    /* access modifiers changed from: protected */
    public void forceTransitToState(@NonNull MissionState state, MissionEvent event) {
        forceTransitToState(state, new WaypointAbstractionDataHolder.WaypointBuilder(event));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataOsdGetPushCommon param) {
        if (param.isGetted() && this.cacheData != null) {
            if (this.cacheData.fcMode == null || ((param.getFlycState() == DataOsdGetPushCommon.FLYC_STATE.NaviGo && this.cacheData.fcMode.equals(DataOsdGetPushCommon.FLYC_STATE.NaviGo)) || param.getFlycState() != DataOsdGetPushCommon.FLYC_STATE.NaviGo || this.cacheData.fcMode == DataOsdGetPushCommon.FLYC_STATE.NaviGo)) {
            }
            this.cacheData.rcMode = param.getModeChannel();
            this.cacheData.fcMode = param.getFlycState();
            tryResumeFSMIfNecessary();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataOsdGetPushHome param) {
        if (param.isGetted()) {
            if (!(this.cacheData.isSimulatorOn == null || this.cacheData.isSimulatorOn.booleanValue() == param.isFlycInSimulationMode() || param.isFlycInSimulationMode())) {
                notifyListener(new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.SIMULATION_OFF).build());
            }
            if (this.cacheData.isNavigationEnabled == null || this.cacheData.isNavigationEnabled.booleanValue() != param.isFlycInNavigationMode()) {
                if (param.isFlycInNavigationMode()) {
                    transitToState(MissionState.READY_TO_UPLOAD, WaypointEvent.ENTER_NAVIGATION_MODE);
                    CommonCallbacks.CompletionCallback callback = resetListeningCompletionCallback();
                    if (callback != null) {
                        this.handler.removeCallbacks(this.startingNavigationModeListerningRunnable);
                        CallbackUtils.onSuccess(callback);
                    }
                } else {
                    transitToState(MissionState.NOT_READY, WaypointEvent.EXIT_NAVIGATION_MODE);
                }
            }
            this.cacheData.isSimulatorOn = Boolean.valueOf(param.isFlycInSimulationMode());
            this.cacheData.isNavigationEnabled = Boolean.valueOf(param.isFlycInNavigationMode());
            this.cacheData.isBeginnerModeEnabled = Boolean.valueOf(param.isBeginnerMode());
            this.cacheData.isMultipleModeEnabled = Boolean.valueOf(param.isMultipleModeOpen());
            tryResumeFSMIfNecessary();
        }
    }

    private WaypointExecutionProgress getExecutionProgressWithEvent(DataFlycGetPushWayPointMissionCurrentEvent event) {
        if (event.getEventType() == 2) {
            int waypointIndex = event.getReachIncidentWayPointIndex();
            WaypointExecutionProgress progress = new WaypointExecutionProgress();
            progress.targetWaypointIndex = event.getReachIncidentWayPointIndex();
            progress.isWaypointReached = true;
            progress.executeState = WaypointMissionExecuteState.find(event.getReachIncidentCurrentStatus());
            int waypointCount = 0;
            if (!(this.cacheData == null || this.cacheData.missionBuilder == null)) {
                waypointCount = this.cacheData.missionBuilder.getWaypointCount();
                if (this.cacheData.missionBuilder.getFlightPathMode() == WaypointMissionFlightPathMode.CURVED && waypointIndex == waypointCount - 2 && this.cacheData.prevTargetIndex != null && waypointIndex < this.cacheData.prevTargetIndex.intValue()) {
                    waypointIndex = waypointCount - 1;
                }
            }
            progress.totalWaypointCount = waypointCount;
            progress.targetWaypointIndex = waypointIndex;
            this.cacheData.prevReachedIndex = Integer.valueOf(waypointIndex);
            return progress;
        }
        if (!(event.getEventType() != 1 || this.cacheData == null || this.cacheData.missionBuilder == null)) {
            int waypointCount2 = this.cacheData.missionBuilder.getWaypointCount();
            if (this.cacheData.missionBuilder.getFinishedAction() == WaypointMissionFinishedAction.GO_FIRST_WAYPOINT) {
                WaypointExecutionProgress progress2 = new WaypointExecutionProgress();
                progress2.isWaypointReached = true;
                progress2.targetWaypointIndex = 0;
                progress2.executeState = WaypointMissionExecuteState.RETURN_TO_FIRST_WAYPOINT;
                progress2.totalWaypointCount = waypointCount2;
                return progress2;
            } else if (this.cacheData.missionBuilder.getFlightPathMode() == WaypointMissionFlightPathMode.CURVED) {
                WaypointExecutionProgress progress3 = new WaypointExecutionProgress();
                int rawIndex = waypointCount2 - 1;
                if (this.cacheData.prevReachedIndex.intValue() != rawIndex) {
                    progress3.isWaypointReached = true;
                    progress3.targetWaypointIndex = rawIndex;
                    this.cacheData.prevReachedIndex = Integer.valueOf(rawIndex);
                    progress3.executeState = WaypointMissionExecuteState.CURVE_MODE_MOVING;
                    progress3.totalWaypointCount = waypointCount2;
                    return progress3;
                }
            }
        }
        return null;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataFlycGetPushWayPointMissionCurrentEvent event) {
        printEventLog(event);
        if (event.isGetted()) {
            switch (event.getEventType()) {
                case 0:
                    this.cacheData.renewMissionDate();
                    transitToState(MissionState.READY_TO_EXECUTE, WaypointEvent.UPLOAD_DONE);
                    break;
                case 1:
                case 2:
                    WaypointAbstractionDataHolder.WaypointBuilder builder = new WaypointAbstractionDataHolder.WaypointBuilder();
                    builder.extra(getExecutionProgressWithEvent(event));
                    builder.event(MissionEvent.EXECUTION_PROGRESS_UPDATE);
                    transitToState(MissionState.EXECUTING, builder);
                    break;
            }
        }
        if (this.cacheData != null) {
            this.cacheData.missionEvent = Integer.valueOf(event.getEventType());
        }
        tryResumeFSMIfNecessary();
    }

    private void printEventLog(@NonNull DataFlycGetPushWayPointMissionCurrentEvent event) {
        DJILog.d(WAYPOINT_EVENT, "==========================================Thread: " + Thread.currentThread().getName(), new Object[0]);
        DJILog.d(WAYPOINT_EVENT, "WaypointEventType: " + event.getEventType(), new Object[0]);
        switch (event.getEventType()) {
            case 0:
                DJILog.d(WAYPOINT_EVENT, "WaypointUploadEstimateTime: " + event.getUploadIncidentEstimatedTime(), new Object[0]);
                DJILog.d(WAYPOINT_EVENT, "WaypointUploadIsValid: " + event.getUploadIncidentIsValid(), new Object[0]);
                break;
            case 1:
                DJILog.d(WAYPOINT_EVENT, "WaypointFinishIsRepeat: " + event.getFinishIncidentIsRepeat(), new Object[0]);
                break;
            case 2:
                DJILog.d(WAYPOINT_EVENT, "WaypointReachCurrentStatus: " + event.getReachIncidentCurrentStatus(), new Object[0]);
                DJILog.d(WAYPOINT_EVENT, "WaypointReachCurrentIndex: " + event.getReachIncidentWayPointIndex(), new Object[0]);
                break;
        }
        DJILog.d(WAYPOINT_EVENT, "==========================================", new Object[0]);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataFlycGetPushWayPointMissionInfo param) {
        printInfoLog(param);
        if (param.getMissionType() == 1 && this.cacheData.isMissionStatusInited()) {
            MissionEvent event = getEventFromMissionStatus(param);
            MissionState state = getStateFromMissionStatus(param);
            WaypointAbstractionDataHolder.WaypointBuilder holder = new WaypointAbstractionDataHolder.WaypointBuilder(event);
            if (event == WaypointEvent.EXECUTION_PROGRESS_UPDATE || event == MissionEvent.EXECUTION_PAUSED || event == MissionEvent.EXECUTION_RESUMED) {
                this.cacheData.renewMissionDate();
                holder.extra(getExecutionProgress(param));
                this.cacheData.lastProgress = getExecutionProgress(param);
            }
            transitToState(state, holder);
        } else if (param.getMissionType() == 0 && this.cacheData.isMissionStatusInited() && this.cacheData.missionStatus != null && this.cacheData.missionStatus.getMissionType() == 1) {
            MissionState currentState = getFSMState();
            this.cacheData.replaceMission(null);
            if (currentState.equals(MissionState.EXECUTION_STOPPING)) {
                transitToState(MissionState.READY_TO_UPLOAD, WaypointEvent.EXECUTION_STOPPED);
            } else if (currentState.equals(MissionState.EXECUTING)) {
                transitToState(MissionState.READY_TO_UPLOAD, WaypointEvent.EXECUTION_FINISHED);
            }
        }
        byte[] data = new byte[param.getRecData().length];
        System.arraycopy(param.getRecData(), 0, data, 0, data.length);
        this.cacheData.missionStatus.setRecData(data);
        tryResumeFSMIfNecessary();
    }

    private void printInfoLog(@NonNull DataFlycGetPushWayPointMissionInfo param) {
    }

    @NonNull
    private MissionState getStateFromMissionStatus(@NonNull DataFlycGetPushWayPointMissionInfo param) {
        if (param.getMissionType() != 1) {
            throw new RuntimeException("The status is not for Waypoint.");
        } else if (param.getRunningStatus() == DataFlycGetPushWayPointMissionInfo.RunningStatus.Running && getFSMState().equals(MissionState.EXECUTION_PAUSING)) {
            return MissionState.EXECUTION_PAUSING;
        } else {
            if (param.getRunningStatus().equals(DataFlycGetPushWayPointMissionInfo.RunningStatus.Paused) && getFSMState().equals(MissionState.EXECUTION_RESUMING)) {
                return MissionState.EXECUTION_RESUMING;
            }
            if (param.getRunningStatus().equals(DataFlycGetPushWayPointMissionInfo.RunningStatus.Paused) && getFSMState().equals(MissionState.EXECUTION_STOPPING)) {
                return MissionState.EXECUTION_STOPPING;
            }
            if (param.getRunningStatus() == DataFlycGetPushWayPointMissionInfo.RunningStatus.Running && getFSMState().equals(MissionState.EXECUTION_STOPPING)) {
                return MissionState.EXECUTION_STOPPING;
            }
            switch (param.getRunningStatus()) {
                case Running:
                    return MissionState.EXECUTING;
                case Paused:
                    return MissionState.EXECUTION_PAUSED;
                case NotRunning:
                    if (this.cacheData.missionStatus.getMissionType() == 1 || param.getErrorNotification() == 0 || param.getCurrentStatus() == 0 || param.getTargetWayPoint() == 0) {
                        return MissionState.READY_TO_EXECUTE;
                    }
                    return MissionState.EXECUTING;
                default:
                    return MissionState.UNKNOWN;
            }
        }
    }

    @NonNull
    private MissionEvent getEventFromMissionStatus(@NonNull DataFlycGetPushWayPointMissionInfo param) {
        if (param.getMissionType() != 1) {
            throw new RuntimeException("The status is not for Waypoint.");
        } else if (this.cacheData.missionStatus.getMissionType() == 0) {
            return MissionEvent.EXECUTION_STARTED;
        } else {
            DataFlycGetPushWayPointMissionInfo.RunningStatus previousState = this.cacheData.missionStatus.getRunningStatus();
            DataFlycGetPushWayPointMissionInfo.RunningStatus currentState = param.getRunningStatus();
            if (previousState != DataFlycGetPushWayPointMissionInfo.RunningStatus.Paused && currentState == DataFlycGetPushWayPointMissionInfo.RunningStatus.Paused) {
                return WaypointEvent.EXECUTION_PAUSED;
            }
            if (previousState == DataFlycGetPushWayPointMissionInfo.RunningStatus.NotRunning || currentState != DataFlycGetPushWayPointMissionInfo.RunningStatus.NotRunning) {
                if (previousState == DataFlycGetPushWayPointMissionInfo.RunningStatus.Paused && currentState == DataFlycGetPushWayPointMissionInfo.RunningStatus.Running) {
                    return WaypointEvent.EXECUTION_RESUMED;
                }
                if (currentState == DataFlycGetPushWayPointMissionInfo.RunningStatus.Running || currentState == DataFlycGetPushWayPointMissionInfo.RunningStatus.Paused) {
                    return WaypointEvent.EXECUTION_PROGRESS_UPDATE;
                }
                return WaypointEvent.EXECUTION_PROGRESS_UPDATE;
            } else if (param.getErrorNotification() == 1) {
                return WaypointEvent.EXECUTION_INTERRUPTED;
            } else {
                return WaypointEvent.EXECUTION_STOPPED;
            }
        }
    }

    public void notifyListener(AbstractionDataHolder holder) {
        if (!holder.equals(this.previousDataHolder)) {
            this.previousDataHolder = holder;
            MissionEventBus.getInstance().post(holder);
        }
    }

    public void reset() {
        if (this.cacheData != null) {
            this.cacheData.reset();
        }
        this.isRecovering.set(false);
        this.isDownloading.set(false);
        this.candidateState = MissionState.RECOVERING;
        this.canSetupEnvironementParams = false;
    }

    public void tryResumeFSMIfNecessary() {
        if (this.fsm.getState() == null) {
            ProductType productType = DJIProductManager.getInstance() != null ? DJIProductManager.getInstance().getType() : null;
            if (isOsmo()) {
                DJILog.d("HAIHAI", "NOT_SUPPORTED isOsmo:" + isOsmo(), new Object[0]);
                this.fsm.forceTransitTo(MissionState.NOT_SUPPORTED);
            } else if (productType == null || productType == ProductType.OTHER) {
                this.fsm.forceTransitTo(MissionState.DISCONNECTED);
            } else {
                this.fsm.forceTransitTo(MissionState.RECOVERING);
            }
        }
        MissionState curState = getFSMState();
        if (curState.equals(MissionState.DISCONNECTED) || curState.equals(MissionState.RECOVERING) || curState.equals(MissionState.NOT_SUPPORTED)) {
            MissionState state = computeCurrentState();
            if (this.cacheData == null || !this.cacheData.isMissionValid()) {
                this.candidateState = state;
                if (canDownloadMission(this.candidateState) == null) {
                    recoverMission();
                    state = MissionState.RECOVERING;
                }
            }
            if (curState.equals(state)) {
                return;
            }
            if (curState.equals(MissionState.RECOVERING)) {
                forceTransitToState(state, WaypointEvent.INITIALIZED);
            } else {
                forceTransitToState(state, MissionEvent.CONNECTED);
            }
        }
    }

    @NonNull
    private MissionState computeCurrentState() {
        if (DJIProductManager.getInstance().getType() == ProductType.OTHER) {
            return MissionState.DISCONNECTED;
        }
        if (isOsmo() || !doesProductSupportNavigationMode()) {
            DJILog.d("HAIHAI", "NOT_SUPPORTED isOsmo:" + isOsmo() + " supportNavigation:" + doesProductSupportNavigationMode(), new Object[0]);
            return MissionState.NOT_SUPPORTED;
        }
        MissionState missionState = MissionState.RECOVERING;
        if (this.cacheData != null && this.cacheData.isNavigationEnabled == null) {
            return missionState;
        }
        if (this.cacheData != null && !this.cacheData.isNavigationEnabled.booleanValue()) {
            return MissionState.NOT_READY;
        }
        MissionState result = MissionState.READY_TO_UPLOAD;
        FlightMode flightControllerFlightMode = (FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE);
        if (flightControllerFlightMode == null) {
            return MissionState.RECOVERING;
        }
        if (this.cacheData != null) {
            if (!this.cacheData.isMissionStatusInited() && this.cacheData.fcMode != null) {
                return MissionState.RECOVERING;
            }
            if (this.cacheData.isMissionStatusInited() && (flightControllerFlightMode.equals(FlightMode.GPS_WAYPOINT) || flightControllerFlightMode.equals(FlightMode.AUTO_TAKEOFF))) {
                result = MissionState.EXECUTING;
            } else if ((this.cacheData.fcMode != null && this.cacheData.fcMode == DataOsdGetPushCommon.FLYC_STATE.NaviGo) || this.cacheData.fcMode == DataOsdGetPushCommon.FLYC_STATE.AutoTakeoff) {
                result = MissionState.EXECUTING;
            }
        }
        if (!result.equals(MissionState.EXECUTING) || !DataFlycGetPushWayPointMissionInfo.getInstance().getRunningStatus().equals(DataFlycGetPushWayPointMissionInfo.RunningStatus.Paused)) {
            return result;
        }
        return MissionState.EXECUTION_PAUSED;
    }

    /* access modifiers changed from: private */
    public void recoverMission() {
        if (!this.isRecovering.get()) {
            this.isRecovering.set(true);
            final WaypointMission.Builder builder = new WaypointMission.Builder();
            downloadMissionSummary(builder, new CommonCallbacks.CompletionCallback() {
                /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass2 */

                public void onResult(@Nullable DJIError error) {
                    boolean stillValid = false;
                    WaypointMissionAbstraction.this.isRecovering.set(false);
                    if (WaypointMissionAbstraction.this.getFSMState().equals(MissionState.RECOVERING) && WaypointMissionAbstraction.this.canDownloadMission(WaypointMissionAbstraction.this.candidateState) == null) {
                        stillValid = true;
                    }
                    if (!stillValid) {
                        return;
                    }
                    if (error != null) {
                        WaypointMissionAbstraction.this.recoverMission();
                        return;
                    }
                    if (WaypointMissionAbstraction.this.cacheData != null) {
                        WaypointMissionAbstraction.this.cacheData.replaceMission(builder);
                        WaypointMissionAbstraction.this.cacheData.renewMissionDate();
                    }
                    WaypointMissionAbstraction.this.forceTransitToState(WaypointMissionAbstraction.this.candidateState, WaypointEvent.INITIALIZED);
                }
            });
        }
    }

    public boolean canSetupEnvironementParams() {
        return this.canSetupEnvironementParams;
    }

    @Nullable
    public DJIError loadMission(@NonNull WaypointMission.Builder tempOperatingMission) {
        DJIError error = tempOperatingMission.checkParameters();
        if (error != null) {
            return error;
        }
        if (!getFSMState().equals(MissionState.NOT_READY) && !getFSMState().equals(MissionState.RECOVERING) && !getFSMState().equals(MissionState.READY_TO_UPLOAD) && !getFSMState().equals(MissionState.READY_TO_RETRY_UPLOAD) && !getFSMState().equals(MissionState.READY_TO_EXECUTE)) {
            return DJIError.COMMON_EXECUTION_FAILED;
        }
        if (getFSMState().equals(MissionState.READY_TO_EXECUTE)) {
            forceTransitToState(MissionState.READY_TO_UPLOAD, WaypointEvent.MISSION_RELOADED);
        }
        return null;
    }

    public void upload(@NonNull final WaypointMission.Builder builder, final CommonCallbacks.CompletionCallback callback) {
        if (builder == null) {
            CallbackUtils.onFailure(callback, DJIMissionError.NULL_MISSION);
        } else if (!builder.isMissionComplete()) {
            CallbackUtils.onFailure(callback, DJIMissionError.INCOMPLETE_MISSION);
        } else if (!this.fsm.transitTo(MissionState.UPLOAD_STARTING)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            this.uploadingMissionStoped = false;
            if (this.cacheData != null) {
                this.cacheData.replaceMission(builder);
                this.cacheData.uploadProgress = null;
            }
            uploadWaypointSummary(builder, new CommonCallbacks.CompletionCallback() {
                /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass3 */

                public void onResult(@Nullable DJIError error) {
                    if (error != null) {
                        boolean unused = WaypointMissionAbstraction.this.tryTransitToUploadState(-1, error, builder, false);
                        CallbackUtils.onResult(callback, error);
                    } else if (WaypointMissionAbstraction.this.uploadingMissionStoped) {
                        boolean unused2 = WaypointMissionAbstraction.this.tryTransitToUploadState(-1, DJIMissionError.USER_CANCEL_UPLOADING_MISSION, builder, true);
                        CallbackUtils.onResult(callback, DJIMissionError.USER_CANCEL_UPLOADING_MISSION);
                    } else if (WaypointMissionAbstraction.this.tryTransitToUploadState(-1, null, builder, true)) {
                        WaypointMissionAbstraction.this.recursiveUploadSingleWaypoint(builder, 0, callback);
                    } else {
                        CallbackUtils.onResult(callback, DJIMissionError.WAYPOINT_UPLOAD_NOT_COMPLETE);
                    }
                }
            });
        }
    }

    public void cancelUploadingMission(CommonCallbacks.CompletionCallback callback) {
        if (!getFSMState().equals(MissionState.UPLOADING)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        this.uploadingMissionStoped = true;
        CallbackUtils.onSuccess(callback);
    }

    public void retryUpload(CommonCallbacks.CompletionCallback callback) {
        if (!getFSMState().equals(MissionState.READY_TO_RETRY_UPLOAD)) {
            CallbackUtils.onFailure(callback, DJIMissionError.COMMON_EXECUTION_FAILED);
            return;
        }
        this.fsm.transitTo(MissionState.UPLOAD_STARTING);
        int index = 0;
        if (this.cacheData != null) {
            index = this.cacheData.uploadProgress.uploadedWaypointIndex;
        }
        this.uploadingMissionStoped = false;
        if (this.cacheData != null) {
            recursiveUploadSingleWaypoint(this.cacheData.missionBuilder, index, callback);
        }
    }

    public void recursiveUploadSingleWaypoint(@NonNull final WaypointMission.Builder missionBuilder, final int index, final CommonCallbacks.CompletionCallback callback) {
        uploadWaypoint(missionBuilder, index, new CommonCallbacks.CompletionCallback() {
            /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass4 */

            public void onResult(@Nullable DJIError error) {
                String description;
                if (error == null) {
                    int unused = WaypointMissionAbstraction.this.retryTimes = 0;
                    if (WaypointMissionAbstraction.this.uploadingMissionStoped) {
                        boolean unused2 = WaypointMissionAbstraction.this.tryTransitToUploadState(index, DJIMissionError.USER_CANCEL_UPLOADING_MISSION, missionBuilder, true);
                        CallbackUtils.onResult(callback, DJIMissionError.USER_CANCEL_UPLOADING_MISSION);
                        return;
                    } else if (index == missionBuilder.getWaypointCount() - 1) {
                        WaypointMissionAbstraction.this.notifyLastWaypointUploaded(index, missionBuilder);
                        CallbackUtils.onResult(callback, null);
                    } else if (!WaypointMissionAbstraction.this.tryTransitToUploadState(index, null, missionBuilder, true) || index + 1 >= missionBuilder.getWaypointCount()) {
                        DJILog.d(WaypointMissionAbstraction.TAG, "wait the callback", new Object[0]);
                        CallbackUtils.onFailure(callback, DJIMissionError.WAYPOINT_UPLOAD_NOT_COMPLETE);
                        WaypointMissionAbstraction.this.tempStateTimer.startTimer(MissionState.UPLOADING, false, 2.0d, new Runnable() {
                            /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass4.AnonymousClass1 */

                            public void run() {
                                WaypointAbstractionDataHolder.WaypointBuilder holder = new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.UPLOAD_FAILED);
                                holder.error(DJIError.COMMON_TIMEOUT);
                                boolean unused = WaypointMissionAbstraction.this.transitToState(MissionState.READY_TO_RETRY_UPLOAD, holder);
                            }
                        });
                    } else {
                        WaypointMissionAbstraction.this.recursiveUploadSingleWaypoint(missionBuilder, index + 1, callback);
                    }
                } else {
                    if (WaypointMissionAbstraction.this.retryTimes >= 3 || error != DJIError.COMMON_TIMEOUT) {
                        boolean unused3 = WaypointMissionAbstraction.this.tryTransitToUploadState(index, error, missionBuilder, true);
                        int unused4 = WaypointMissionAbstraction.this.retryTimes = 0;
                        CallbackUtils.onFailure(callback, error);
                    } else {
                        WaypointMissionAbstraction.this.recursiveUploadSingleWaypoint(missionBuilder, index, callback);
                        WaypointMissionAbstraction.access$608(WaypointMissionAbstraction.this);
                    }
                    DJILog.d("HAIHAI", "Error State " + error.getDescription(), new Object[0]);
                }
                StringBuilder append = new StringBuilder().append("waypoint uploaded ").append(index).append("\n Error: ");
                if (error == null) {
                    description = "null";
                } else {
                    description = error.getDescription();
                }
                DJILog.d(WaypointMissionAbstraction.TAG, append.append(description).append("\n state: ").append(WaypointMissionAbstraction.this.getFSMState().getName()).toString(), new Object[0]);
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean tryTransitToUploadState(int index, @Nullable DJIError error, @Nullable WaypointMission.Builder missionBuilder, boolean isWaypointSummaryUploaded) {
        MissionState state;
        MissionState state2 = MissionState.UPLOADING;
        WaypointUploadProgress progress = new WaypointUploadProgress();
        progress.isSummaryUploaded = isWaypointSummaryUploaded;
        progress.uploadedWaypointIndex = index;
        if (missionBuilder != null) {
            progress.totalWaypointCount = missionBuilder.getWaypointCount();
        }
        WaypointAbstractionDataHolder.WaypointBuilder abstractionDataHolder = new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.UPLOAD_PROGRESS_UPDATE);
        abstractionDataHolder.error(error);
        abstractionDataHolder.currentState(getFSMState());
        abstractionDataHolder.event(WaypointEvent.UPLOAD_PROGRESS_UPDATE);
        abstractionDataHolder.extra(progress);
        if (error != null) {
            if (index == -1 || !error.equals(DJIError.COMMON_TIMEOUT)) {
                state = MissionState.READY_TO_RETRY_UPLOAD;
            } else {
                state = MissionState.READY_TO_RETRY_UPLOAD;
            }
            abstractionDataHolder.event(WaypointEvent.UPLOAD_FAILED);
            return transitToState(state, abstractionDataHolder);
        }
        this.cacheData.uploadProgress = progress;
        if (Boolean.valueOf(transitToState(state2, abstractionDataHolder)).booleanValue()) {
            return true;
        }
        abstractionDataHolder.error(getCurrentError());
        abstractionDataHolder.currentState(getFSMState());
        abstractionDataHolder.event(WaypointEvent.UPLOAD_FAILED);
        notifyListener(abstractionDataHolder.build());
        return false;
    }

    /* access modifiers changed from: private */
    public void notifyLastWaypointUploaded(int index, @Nullable WaypointMission.Builder missionBuilder) {
        WaypointUploadProgress progress = new WaypointUploadProgress();
        progress.isSummaryUploaded = true;
        progress.uploadedWaypointIndex = index;
        if (missionBuilder != null) {
            progress.totalWaypointCount = missionBuilder.getWaypointCount();
        }
        WaypointAbstractionDataHolder.WaypointBuilder abstractionDataHolder = new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.UPLOAD_DONE);
        abstractionDataHolder.currentState(getFSMState());
        abstractionDataHolder.event(WaypointEvent.UPLOAD_DONE);
        abstractionDataHolder.extra(progress);
        notifyListener(abstractionDataHolder.build());
    }

    @NonNull
    private DJIError getCurrentError() {
        if (getFSMState().equals(MissionState.DISCONNECTED) || getFSMState().equals(MissionState.RECOVERING)) {
            return DJISDKError.CONNECTION_TO_SDK_FAILED;
        }
        return DJIError.COMMON_EXECUTION_FAILED;
    }

    public void updateSetupEnvironmentParams() {
        if (getFSMState().equals(MissionState.DISCONNECTED)) {
            if (canSetupEnvironementParams()) {
                this.canSetupEnvironementParams = false;
            }
        } else if (getFSMState().equals(MissionState.NOT_READY) && getFSMState().equals(MissionState.DISCONNECTED)) {
            if (this.cacheData.isBeginnerModeEnabled != null && this.cacheData.isBeginnerModeEnabled.booleanValue() && canSetupEnvironementParams()) {
                this.canSetupEnvironementParams = false;
            }
            if (this.cacheData.isMultipleModeEnabled == null || !this.cacheData.isMultipleModeEnabled.booleanValue()) {
                boolean canSetup = isInCorrectRCMode();
                if (canSetupEnvironementParams() != canSetup) {
                    this.canSetupEnvironementParams = canSetup;
                }
            } else if (!canSetupEnvironementParams()) {
                this.canSetupEnvironementParams = true;
            }
        } else if (!canSetupEnvironementParams()) {
            this.canSetupEnvironementParams = true;
        }
    }

    private boolean isInCorrectRCMode() {
        RemoteControllerFlightMode currentMode = (RemoteControllerFlightMode) CacheHelper.getFlightController(FlightControllerKeys.CURRENT_MODE);
        if (currentMode != null) {
            return currentMode.isMissionAvailable();
        }
        return false;
    }

    @Nullable
    private DJIError canDownloadMission() {
        return canDownloadMission(getFSMState());
    }

    @Nullable
    public DJIError canDownloadMission(@NonNull MissionState currentState) {
        if (currentState.equals(MissionState.EXECUTING) || currentState.equals(MissionState.EXECUTION_PAUSED) || currentState.equals(MissionState.EXECUTION_PAUSING) || currentState.equals(MissionState.EXECUTION_RESUMING) || currentState.equals(MissionState.EXECUTION_STARTING) || currentState.equals(MissionState.READY_TO_EXECUTE)) {
            return null;
        }
        if (currentState.equals(MissionState.DISCONNECTED)) {
            return DJIError.COMMON_DISCONNECTED;
        }
        if (currentState.equals(MissionState.NOT_SUPPORTED)) {
            return DJIError.COMMON_UNSUPPORTED;
        }
        return DJIError.COMMON_EXECUTION_FAILED;
    }

    public void downloadMission(@NonNull final WaypointMission.Builder builder, final CommonCallbacks.CompletionCallback callback) {
        DJIError error = canDownloadMission();
        if (error != null || this.isDownloading.get()) {
            this.isDownloading.set(false);
            CallbackUtils.onFailure(callback, error);
        } else if (builder.isMissionComplete()) {
            this.isDownloading.set(false);
            CallbackUtils.onSuccess(callback);
            postMissionEvent(builder);
        } else {
            this.isDownloading.set(true);
            downloadMissionSummary(builder, new CommonCallbacks.CompletionCallback() {
                /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass5 */

                public void onResult(@Nullable DJIError error) {
                    boolean isSummaryDownloaded = false;
                    if (error == null && builder.getWaypointCount() > 0) {
                        WaypointMissionAbstraction.this.recursiveDownloadSingleWaypoint(builder, callback);
                    } else if (error != null) {
                        WaypointMissionAbstraction.this.isDownloading.set(false);
                        CallbackUtils.onFailure(callback, error);
                    } else {
                        WaypointMissionAbstraction.this.isDownloading.set(false);
                        CallbackUtils.onSuccess(callback);
                    }
                    if (error == null) {
                        isSummaryDownloaded = true;
                    }
                    WaypointMissionAbstraction.this.triggerDownloadEvent(builder, error, isSummaryDownloaded);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void recursiveDownloadSingleWaypoint(@NonNull final WaypointMission.Builder missionBuilder, final CommonCallbacks.CompletionCallback callback) {
        if (missionBuilder.isMissionComplete()) {
            this.isDownloading.set(false);
            CallbackUtils.onSuccess(callback);
            return;
        }
        downloadNextWaypoint(missionBuilder, new CommonCallbacks.CompletionCallback() {
            /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass6 */

            public void onResult(@Nullable DJIError error) {
                if (error == null) {
                    int unused = WaypointMissionAbstraction.this.downloadRetryTimes = 0;
                    if (WaypointMissionAbstraction.this.cacheData != null) {
                        WaypointMissionAbstraction.this.cacheData.replaceMission(missionBuilder);
                        WaypointMissionAbstraction.this.cacheData.renewMissionDate();
                        if (!WaypointMissionAbstraction.this.cacheData.missionBuilder.isMissionComplete()) {
                            WaypointMissionAbstraction.this.recursiveDownloadSingleWaypoint(missionBuilder, callback);
                        } else {
                            WaypointMissionAbstraction.this.isDownloading.set(false);
                            CallbackUtils.onSuccess(callback);
                        }
                    }
                    WaypointMissionAbstraction.this.triggerDownloadEvent(missionBuilder, error, true);
                } else if (WaypointMissionAbstraction.this.downloadRetryTimes >= 3 || error != DJIError.COMMON_TIMEOUT) {
                    WaypointMissionAbstraction.this.isDownloading.set(false);
                    CallbackUtils.onFailure(callback, error);
                    WaypointMissionAbstraction.this.triggerDownloadEvent(missionBuilder, error, true);
                } else {
                    WaypointMissionAbstraction.this.recursiveDownloadSingleWaypoint(missionBuilder, callback);
                    WaypointMissionAbstraction.access$1308(WaypointMissionAbstraction.this);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void triggerDownloadEvent(@NonNull WaypointMission.Builder downloadedMissionBuilder, @Nullable DJIError error, boolean isSummaryDownloaded) {
        WaypointEvent event = WaypointEvent.DOWNLOAD_PROGRESS_UPDATE;
        WaypointDownloadProgress progress = new WaypointDownloadProgress();
        progress.isSummaryDownloaded = isSummaryDownloaded;
        if (error != null) {
            event = WaypointEvent.DOWNLOAD_FAILED;
        } else {
            int wpListSize = 0;
            int wpCount = downloadedMissionBuilder.getWaypointCount();
            progress.totalWaypointCount = wpCount;
            if (downloadedMissionBuilder.getWaypointList() != null) {
                wpListSize = downloadedMissionBuilder.getWaypointList().size();
            }
            if (wpCount == wpListSize) {
                event = WaypointEvent.DOWNLOAD_DONE;
            }
            if (wpListSize > 0) {
                progress.downloadedWaypointIndex = wpListSize - 1;
            }
        }
        WaypointAbstractionDataHolder.WaypointBuilder holder = new WaypointAbstractionDataHolder.WaypointBuilder(event);
        holder.error(error);
        holder.extra(progress);
        holder.currentState(getFSMState());
        notifyListener(holder.build());
    }

    public void startMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STARTING, new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycWayPointMissionSwitch missionSwitch = new DataFlycWayPointMissionSwitch();
        missionSwitch.setCMD(DataFlycWayPointMissionSwitch.CMD.START);
        missionSwitch.start(getDataCallbackForTempState(new WaypointMissionAbstraction$$Lambda$0(missionSwitch), MissionState.EXECUTION_STARTING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.READY_TO_EXECUTE, new WaypointAbstractionDataHolder.WaypointBuilder(MissionEvent.EXECUTION_START_FAILED), callback));
    }

    public void pauseMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_PAUSING, new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycWayPointMissionPauseOrResume missionPauseOrResume = new DataFlycWayPointMissionPauseOrResume();
        missionPauseOrResume.setCMD(DataFlycWayPointMissionPauseOrResume.CMD.PAUSE);
        missionPauseOrResume.start(getDataCallbackForTempState(new WaypointMissionAbstraction$$Lambda$1(missionPauseOrResume), MissionState.EXECUTION_PAUSING, desiredMissionStatesHelper(MissionState.EXECUTION_PAUSED), MissionState.EXECUTING, new WaypointAbstractionDataHolder.WaypointBuilder(MissionEvent.EXECUTION_PAUSE_FAILED), callback));
    }

    public void resumeMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_RESUMING, new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycWayPointMissionPauseOrResume missionPauseOrResume = new DataFlycWayPointMissionPauseOrResume();
        missionPauseOrResume.setCMD(DataFlycWayPointMissionPauseOrResume.CMD.RESUME);
        missionPauseOrResume.start(getDataCallbackForTempState(new WaypointMissionAbstraction$$Lambda$2(missionPauseOrResume), MissionState.EXECUTION_RESUMING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.EXECUTION_PAUSED, new WaypointAbstractionDataHolder.WaypointBuilder(MissionEvent.EXECUTION_RESUME_FAILED), callback));
    }

    public void stopMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STOPPING, new WaypointAbstractionDataHolder.WaypointBuilder(WaypointEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycWayPointMissionSwitch missionSwitch = new DataFlycWayPointMissionSwitch();
        missionSwitch.setCMD(DataFlycWayPointMissionSwitch.CMD.CANCEL);
        missionSwitch.start(getDataCallbackForTempState(new WaypointMissionAbstraction$$Lambda$3(missionSwitch), MissionState.EXECUTION_STOPPING, desiredMissionStatesHelper(MissionState.READY_TO_UPLOAD), MissionState.EXECUTING, new WaypointAbstractionDataHolder.WaypointBuilder(MissionEvent.EXECUTION_STOP_FAILED), callback));
    }

    public void setAutoFlightSpeed(@FloatRange(from = -15.0d, to = 15.0d) float speed, CommonCallbacks.CompletionCallback callback) {
        DataFlycSetFlightIdleSpeed.getInstance().setSpeed(speed).start(CallbackUtils.getDJIDataCallback(callback));
    }

    private void uploadWaypointSummary(@Nullable WaypointMission.Builder missionBuilder, CommonCallbacks.CompletionCallback callback) {
        if (missionBuilder == null || missionBuilder.getWaypointList() == null || missionBuilder.getWaypointList().size() == 0) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            MissionUtils.generateUploadWayPointMissionMsg(missionBuilder).start(CallbackUtils.getDJIDataCallback(callback));
        }
    }

    private void uploadWaypoint(@Nullable WaypointMission.Builder missionBuilder, int index, final CommonCallbacks.CompletionCallback callback) {
        if (missionBuilder == null || missionBuilder.getWaypointList() == null || missionBuilder.getWaypointList().size() == 0) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        }
        final DataFlycUploadWayPointMsgByIndex waypointProtocol = MissionUtils.getWayPointMsgByIndex(missionBuilder.getWaypointList().get(index), index);
        waypointProtocol.start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                int result = waypointProtocol.getResult();
                if (result == 0) {
                    DataSpecialLockRcControl.getInstance().setButtonToBeLocked(DataSpecialLockRcControl.ButtonType.SHUTTER);
                    DataSpecialLockRcControl.getInstance().setButtonToBeLocked(DataSpecialLockRcControl.ButtonType.RECORD);
                    DataSpecialLockRcControl.getInstance().setEnabled(true).start((DJIDataCallBack) null);
                    CallbackUtils.onSuccess(callback);
                    return;
                }
                CallbackUtils.onFailure(callback, DJIMissionError.getDJIErrorByCode(result));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    private void downloadMissionSummary(@NonNull final WaypointMission.Builder builder, final CommonCallbacks.CompletionCallback callback) {
        DJILog.d(TAG, "downloadMissionSummary", new Object[0]);
        final DataFlycDownloadWayPointMissionMsg downloadWayPointMissionMsg = new DataFlycDownloadWayPointMissionMsg();
        downloadWayPointMissionMsg.start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                boolean z;
                boolean z2 = true;
                if (downloadWayPointMissionMsg.isGetted()) {
                    DJILog.d(WaypointMissionAbstraction.TAG, "download wp mission msg getted, id:" + downloadWayPointMissionMsg.getMissionID(), new Object[0]);
                    int count = downloadWayPointMissionMsg.getWayPointCount();
                    builder.maxFlightSpeed(downloadWayPointMissionMsg.getCmdSpeed());
                    builder.autoFlightSpeed(downloadWayPointMissionMsg.getIdleSpeed());
                    builder.finishedAction(WaypointMissionFinishedAction.find(downloadWayPointMissionMsg.getFinishAction().value()));
                    builder.repeatTimes(downloadWayPointMissionMsg.getRepeatNum());
                    builder.headingMode(WaypointMissionHeadingMode.find(downloadWayPointMissionMsg.getYawMode().value()));
                    builder.flightPathMode(WaypointMissionFlightPathMode.find(downloadWayPointMissionMsg.getTraceMode().value()));
                    builder.gotoFirstWaypointMode(WaypointMissionGotoWaypointMode.find(downloadWayPointMissionMsg.getGotoFirstFlag()));
                    WaypointMission.Builder builder = builder;
                    if (downloadWayPointMissionMsg.getActionOnRCLost() == DataFlycUploadWayPointMissionMsg.ACTION_ON_RC_LOST.EXIT_WP) {
                        z = true;
                    } else {
                        z = false;
                    }
                    builder.setExitMissionOnRCSignalLostEnabled(z);
                    WaypointMission.Builder builder2 = builder;
                    if (downloadWayPointMissionMsg.getGimbalPitchMode() != DataFlycUploadWayPointMissionMsg.GIMBAL_PITCH_MODE.PITCH_SMOOTH) {
                        z2 = false;
                    }
                    builder2.setGimbalPitchRotationEnabled(z2);
                    builder.setPointOfInterest(new LocationCoordinate2D(MissionUtils.Degree(downloadWayPointMissionMsg.getHPLat()), MissionUtils.Degree(downloadWayPointMissionMsg.getHPLng())));
                    builder.waypointCount(count);
                    builder.setMissionID(downloadWayPointMissionMsg.getMissionID());
                    EventBus.getDefault().post(MissionUtils.createDownloadedWPSummaryEvent(downloadWayPointMissionMsg.getBodyData()));
                    CallbackUtils.onSuccess(callback);
                    return;
                }
                CallbackUtils.onFailure(callback, DJIMissionError.FAILED);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    private void downloadNextWaypoint(@Nullable WaypointMission.Builder missionBuilder, CommonCallbacks.CompletionCallback callback) {
        final int index;
        if (missionBuilder != null) {
            if (missionBuilder.getWaypointList() == null || missionBuilder.getWaypointList().size() == 0) {
                index = 0;
            } else {
                index = missionBuilder.getWaypointList().size();
            }
            final DataFlycDownloadWayPointMsgByIndex downloadWayPointMsgByIndex = DataFlycDownloadWayPointMsgByIndex.getInstance();
            final WaypointMission.Builder builder = missionBuilder;
            final CommonCallbacks.CompletionCallback completionCallback = callback;
            downloadWayPointMsgByIndex.setIndex(index).start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass9 */

                public void onSuccess(Object model) {
                    DJILog.d(WaypointMissionAbstraction.TAG, "download wp by index success index=" + index, new Object[0]);
                    if (downloadWayPointMsgByIndex.getResult() == 0) {
                        double latitude = MissionUtils.Degree(downloadWayPointMsgByIndex.getLatitude());
                        double longitude = MissionUtils.Degree(downloadWayPointMsgByIndex.getLongitude());
                        int actionNum = downloadWayPointMsgByIndex.getActionNum();
                        int repeatNum = downloadWayPointMsgByIndex.getRepeatNum();
                        Waypoint point = new Waypoint(latitude, longitude, downloadWayPointMsgByIndex.getAltitude());
                        point.actionRepeatTimes = repeatNum;
                        point.heading = downloadWayPointMsgByIndex.getTgtYaw();
                        point.altitude = downloadWayPointMsgByIndex.getAltitude();
                        point.cornerRadiusInMeters = downloadWayPointMsgByIndex.getDampingDis();
                        point.actionTimeoutInSeconds = downloadWayPointMsgByIndex.getMaxReachTime();
                        point.turnMode = WaypointTurnMode.find(downloadWayPointMsgByIndex.getTurnMode().value());
                        point.speed = (float) downloadWayPointMsgByIndex.getSpeed();
                        if (downloadWayPointMsgByIndex.getCameraActionType() == 2) {
                            point.shootPhotoTimeInterval = (float) downloadWayPointMsgByIndex.getCameraActionInterval();
                            point.shootPhotoDistanceInterval = 0.0f;
                        } else if (downloadWayPointMsgByIndex.getCameraActionType() == 1) {
                            point.shootPhotoDistanceInterval = (float) downloadWayPointMsgByIndex.getCameraActionInterval();
                            point.shootPhotoTimeInterval = 0.0f;
                        }
                        point.gimbalPitch = ((float) downloadWayPointMsgByIndex.getTgtPitch()) / 10.0f;
                        ArrayList<DataFlycUploadWayPointMsgByIndex.ACTION> actionResult = downloadWayPointMsgByIndex.getAction();
                        ArrayList<Integer> parameterResult = downloadWayPointMsgByIndex.getParameter();
                        for (int j = 0; j < actionNum; j++) {
                            point.addAction(new WaypointAction(WaypointActionType.find(actionResult.get(j).value()), parameterResult.get(j).intValue()));
                        }
                        builder.getWaypointList().add(point);
                        CallbackUtils.onSuccess(completionCallback);
                    } else {
                        CallbackUtils.onFailure(completionCallback, DJIError.COMMON_EXECUTION_FAILED);
                    }
                    EventBus.getDefault().post(MissionUtils.createDownloadedWPDetailedEvent(downloadWayPointMsgByIndex.getBodyData()));
                }

                public void onFailure(@NonNull Ccode ccode) {
                    CallbackUtils.onFailure(completionCallback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    @Nullable
    private WaypointExecutionProgress getExecutionProgress(@NonNull DataFlycGetPushWayPointMissionInfo missionInfo) {
        if (missionInfo.getMissionType() == 0 || this.cacheData == null || this.cacheData.missionBuilder == null) {
            return null;
        }
        int waypointCount = this.cacheData.missionBuilder.getWaypointCount();
        WaypointExecutionProgress progress = new WaypointExecutionProgress(missionInfo);
        progress.totalWaypointCount = waypointCount;
        WaypointMissionFinishedAction finishedAction = this.cacheData.missionBuilder.getFinishedAction();
        int repeatTime = this.cacheData.missionBuilder.getRepeatTimes();
        int goToFirstWaypointOffset = finishedAction == WaypointMissionFinishedAction.GO_FIRST_WAYPOINT ? 1 : 0;
        int rawIndex = missionInfo.getTargetWayPoint();
        int finalIndex = rawIndex + goToFirstWaypointOffset;
        if (finalIndex >= waypointCount) {
            finalIndex = 0;
        }
        boolean isReached = false;
        boolean isConfirmed = true;
        WaypointMissionExecuteState actionState = WaypointMissionExecuteState.find(missionInfo.getCurrentStatus());
        boolean isConflictCase = finishedAction == WaypointMissionFinishedAction.GO_FIRST_WAYPOINT && rawIndex == 0;
        switch (actionState) {
            case INITIALIZING:
                if (rawIndex == 0) {
                    finalIndex = 0;
                    isReached = false;
                    break;
                }
                break;
            case MOVING:
                isReached = false;
                if (isConflictCase && repeatTime > 1) {
                    if (this.cacheData.prevReachedIndex == null) {
                        finalIndex = 0;
                        isConfirmed = false;
                        break;
                    } else {
                        finalIndex = (this.cacheData.prevReachedIndex.intValue() + 1) % waypointCount;
                        break;
                    }
                }
            case CURVE_MODE_MOVING:
                isReached = false;
                break;
            case BEGIN_ACTION:
            case DOING_ACTION:
            case FINISHED_ACTION:
                isReached = true;
                if (!isConflictCase) {
                    if (finalIndex != 0) {
                        if (finalIndex == waypointCount - 1) {
                            if (this.cacheData.lastProgress != null && this.cacheData.lastProgress.targetWaypointIndex == finalIndex - 1) {
                                finalIndex--;
                                break;
                            }
                        } else {
                            finalIndex--;
                            break;
                        }
                    } else {
                        finalIndex = waypointCount - 1;
                        break;
                    }
                } else if (this.cacheData.prevReachedIndex == null) {
                    finalIndex = 0;
                    isReached = false;
                    break;
                } else {
                    finalIndex = this.cacheData.prevReachedIndex.intValue() % waypointCount;
                    break;
                }
            case CURVE_MODE_TURNING:
                isReached = true;
                break;
            case RETURN_TO_FIRST_WAYPOINT:
                isReached = false;
                finalIndex = 0;
                break;
        }
        if (this.cacheData.lastProgress != null && !this.cacheData.lastProgress.isWaypointReached && !isReached && this.cacheData.lastProgress.targetWaypointIndex != finalIndex) {
            isReached = true;
        }
        if (this.cacheData.missionBuilder != null && this.cacheData.lastProgress != null && this.cacheData.missionBuilder.getFlightPathMode() == WaypointMissionFlightPathMode.CURVED && this.cacheData.lastProgress.isWaypointReached && !isReached && this.cacheData.lastProgress.targetWaypointIndex == finalIndex) {
            isReached = true;
        }
        progress.isWaypointReached = isReached;
        progress.targetWaypointIndex = finalIndex;
        if (!isConfirmed) {
            this.cacheData.prevTargetIndex = null;
            this.cacheData.prevReachedIndex = null;
            return progress;
        } else if (isReached) {
            this.cacheData.prevReachedIndex = Integer.valueOf(finalIndex);
            return progress;
        } else {
            this.cacheData.prevTargetIndex = Integer.valueOf(finalIndex);
            return progress;
        }
    }

    private void postMissionEvent(WaypointMission.Builder builder) {
        EventBus.getDefault().post(MissionUtils.createDownloadedWPSummaryEvent(MissionUtils.generateUploadWayPointMissionMsg(builder).getSendDataForRecord()));
        new Thread(new WaypointMissionAbstraction$$Lambda$4(builder), TAG).start();
    }

    static final /* synthetic */ void lambda$postMissionEvent$4$WaypointMissionAbstraction(WaypointMission.Builder builder) {
        int index = 0;
        for (Waypoint waypoint : builder.getWaypointList()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                DJILog.e(TAG, "Post mission event exception:" + e.getMessage(), new Object[0]);
            }
            EventBus.getDefault().post(MissionUtils.createDownloadedWPDetailedEvent(MissionUtils.getWayPointMsgByIndex(waypoint, index).getSendDataForRecord()));
            index++;
        }
    }

    @Nullable
    public WaypointAbstractionCacheData getCacheData() {
        return this.cacheData;
    }

    public void getPreviousInterruption(@NonNull final CommonCallbacks.CompletionCallbackWith<WaypointMissionInterruption> callback) {
        DJISDKCache.getInstance().getValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.WAYPOINT_MISSION_INTERRUPTION), new DJIGetCallback() {
            /* class dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction.AnonymousClass10 */

            public void onSuccess(DJISDKCacheParamValue value) {
                CallbackUtils.onSuccess(callback, value.getData());
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
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
