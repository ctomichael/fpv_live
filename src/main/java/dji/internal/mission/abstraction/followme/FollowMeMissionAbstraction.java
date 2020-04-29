package dji.internal.mission.abstraction.followme;

import android.support.annotation.NonNull;
import dji.common.bus.MissionEventBus;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.flightcontroller.FlightMode;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.mission.MissionUtils;
import dji.common.mission.followme.FollowMeEvent;
import dji.common.mission.followme.FollowMeExecutionData;
import dji.common.mission.followme.FollowMeMission;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;
import dji.internal.mission.abstraction.followme.FollowMeAbstractionDataHolder;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataFlycCancelFollowMeMission;
import dji.midware.data.model.P3.DataFlycFollowMeMissionSwitch;
import dji.midware.data.model.P3.DataFlycGetPushWayPointMissionInfo;
import dji.midware.data.model.P3.DataFlycSendGpsInfo;
import dji.midware.data.model.P3.DataFlycStartFollowMeWithInfo;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class FollowMeMissionAbstraction extends BaseMissionAbstraction {
    private static final int FOLLOWME_TYPE = 3;
    private static final int NO_MISSION = 0;
    private static final String TAG = "FollowMeMission";
    private FollowMeAbstractionCacheData cacheData = new FollowMeAbstractionCacheData();
    private DJIParamAccessListener connectionListener = new FollowMeMissionAbstraction$$Lambda$0(this);
    private int previousMission = 0;

    public FollowMeMissionAbstraction() {
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
        return new FiniteStateMachine().from(MissionState.NOT_READY).to(MissionState.READY_TO_EXECUTE, MissionState.NOT_READY).from(MissionState.READY_TO_EXECUTE).to(MissionState.READY_TO_EXECUTE, MissionState.EXECUTION_STARTING).from(MissionState.EXECUTION_STARTING).to(MissionState.EXECUTING, MissionState.READY_TO_EXECUTE).from(MissionState.EXECUTING).to(MissionState.EXECUTING, MissionState.EXECUTION_PAUSING, MissionState.EXECUTION_STOPPING, MissionState.READY_TO_EXECUTE, MissionState.GOT_STUCK).from(MissionState.EXECUTION_PAUSING).to(MissionState.EXECUTION_PAUSED, MissionState.EXECUTING).from(MissionState.EXECUTION_PAUSED).to(MissionState.EXECUTION_RESUMING, MissionState.EXECUTION_STOPPING, MissionState.GOT_STUCK).from(MissionState.EXECUTION_RESUMING).to(MissionState.EXECUTING, MissionState.EXECUTION_PAUSED).from(MissionState.EXECUTION_STOPPING).to(MissionState.READY_TO_EXECUTE, MissionState.EXECUTION_PAUSED, MissionState.EXECUTING).from(MissionState.GOT_STUCK).to(MissionState.READY_TO_EXECUTE, MissionState.NOT_READY).fromAll().to(MissionState.NOT_READY).fromAll().to(MissionState.DISCONNECTED).add(MissionState.RECOVERING).superState(MissionState.NOT_SUPPORTED);
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, MissionEvent event) {
        return transitToState(state, new FollowMeAbstractionDataHolder.FollowMeBuilder(event));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$FollowMeMissionAbstraction(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
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
        if (!getFSMState().equals(MissionState.NOT_SUPPORTED) && param.isGetted()) {
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
        if (!getFSMState().equals(MissionState.NOT_SUPPORTED) && param.isGetted()) {
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
    public void onEvent3BackgroundThread(DataFlycGetPushWayPointMissionInfo param) {
        FollowMeAbstractionDataHolder.FollowMeBuilder holder;
        MissionState currentState = getFSMState();
        if (param.getMissionType() == 3) {
            if (param.getFollowMeStatus() == 2) {
                transitToState(MissionState.GOT_STUCK, new FollowMeAbstractionDataHolder.FollowMeBuilder(FollowMeEvent.GOT_STUCK));
            }
            if (!getFSMState().equals(MissionState.EXECUTION_PAUSING) && !getFSMState().equals(MissionState.EXECUTION_STOPPING)) {
                if (this.previousMission == 0) {
                    holder = new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_STARTED);
                } else {
                    holder = new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE);
                }
                holder.extra(new FollowMeExecutionData(param));
                transitToState(MissionState.EXECUTING, holder);
            }
        } else if (currentState.equals(MissionState.EXECUTION_STOPPING) || currentState.equals(MissionState.EXECUTING) || currentState.equals(MissionState.EXECUTION_PAUSED) || currentState.equals(MissionState.EXECUTION_PAUSING) || currentState.equals(MissionState.EXECUTION_RESUMING) || currentState.equals(MissionState.GOT_STUCK)) {
            MissionEvent event = MissionEvent.EXECUTION_STOPPED;
            DJIError error = DJIMissionError.getDJIErrorByCode(param.getFollowMeReason());
            if (error != null) {
                event = MissionEvent.EXECUTION_INTERRUPTED;
            }
            FollowMeAbstractionDataHolder.FollowMeBuilder holder2 = new FollowMeAbstractionDataHolder.FollowMeBuilder(event);
            holder2.error(error);
            transitToState(MissionState.READY_TO_EXECUTE, holder2);
        }
        this.previousMission = param.getMissionType();
        this.cacheData.setMissionStatus(param);
        if (currentState != null && currentState.equals(MissionState.GOT_STUCK)) {
            specialStopFollowMeMission();
        }
        tryRecoverMissionState();
        triggerTimerIfNecessary();
    }

    private void specialStopFollowMeMission() {
        DataFlycCancelFollowMeMission.getInstance().start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.followme.FollowMeMissionAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
    }

    private void triggerTimerIfNecessary() {
    }

    public void notifyListener(AbstractionDataHolder holder) {
        if (!holder.equals(this.previousDataHolder)) {
            this.previousDataHolder = holder;
            MissionEventBus.getInstance().post(holder);
        }
    }

    public void reset() {
        this.cacheData.reset();
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
            if (!curState.equals(state)) {
                forceTransitToState(state, MissionEvent.INITIALIZED);
            }
        }
    }

    private MissionState computeCurrentState() {
        DataFlycGetPushWayPointMissionInfo missionStatus;
        if (isOsmo() || !doesProductSupportNavigationMode()) {
            return MissionState.NOT_SUPPORTED;
        }
        if (DJIProductManager.getInstance().getType() == ProductType.OTHER) {
            return MissionState.DISCONNECTED;
        }
        FlightMode flightControllerFlightMode = (FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE);
        if (flightControllerFlightMode == null) {
            return MissionState.RECOVERING;
        }
        if (!CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.NAVIGATION_MODE_ENABLED))) {
            DJILog.d("HAIHAI", "computeCurrentState MissionState.NOT_READY ", new Object[0]);
            return MissionState.NOT_READY;
        }
        MissionState missionState = MissionState.READY_TO_EXECUTE;
        boolean isRawMissionStatusInitialized = DataFlycGetPushWayPointMissionInfo.getInstance().isGetted();
        if (!this.cacheData.isMissionStatusInitialized() && !isRawMissionStatusInitialized && this.cacheData.getFcMode() != null) {
            return MissionState.RECOVERING;
        }
        if ((!this.cacheData.isMissionStatusInitialized() && !isRawMissionStatusInitialized) || !flightControllerFlightMode.equals(FlightMode.GPS_FOLLOW_ME) || (missionStatus = this.cacheData.getMissionStatus()) == null) {
            return missionState;
        }
        if (missionStatus.getFollowMeStatus() == 2) {
            return MissionState.GOT_STUCK;
        }
        return MissionState.EXECUTING;
    }

    public boolean isMissionCanUpdate() {
        MissionState currentState = getFSMState();
        return currentState != null && (currentState.equals(MissionState.EXECUTING) || currentState.equals(MissionState.EXECUTION_PAUSING) || currentState.equals(MissionState.EXECUTION_PAUSED) || currentState.equals(MissionState.EXECUTION_RESUMING));
    }

    public void updateCoordinate(LocationCoordinate2D location, CommonCallbacks.CompletionCallback callback) {
        if (!isMissionCanUpdate()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycSendGpsInfo info = DataFlycSendGpsInfo.getInstance();
        info.setLatitude(MissionUtils.Radian(location.getLatitude()));
        info.setLongitude(MissionUtils.Radian(location.getLongitude()));
        info.start(CallbackUtils.getDJIDataCallback(callback));
    }

    public void updateCoordinate(LocationCoordinate2D location, float altitude, CommonCallbacks.CompletionCallback callback) {
        if (!isMissionCanUpdate()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycSendGpsInfo info = DataFlycSendGpsInfo.getInstance();
        info.setLatitude(MissionUtils.Radian(location.getLatitude()));
        info.setLongitude(MissionUtils.Radian(location.getLongitude()));
        info.setAltitude((short) ((int) (10.0f * altitude)));
        info.start(CallbackUtils.getDJIDataCallback(callback));
    }

    public void startMission(FollowMeMission mission, CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STARTING, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (mission != null && isValidParameters(mission)) {
            DataFlycStartFollowMeWithInfo info = DataFlycStartFollowMeWithInfo.getInstance();
            info.setUserLatitude(MissionUtils.Radian(mission.getLatitude()));
            info.setUserLongitude(MissionUtils.Radian(mission.getLongitude()));
            info.setAltitude((short) ((int) (mission.getAltitude() * 10.0f)));
            info.setFollowMode(DataFlycStartFollowMeWithInfo.FOLLOWMODE.find(0));
            info.setYawMode(DataFlycStartFollowMeWithInfo.YAWMODE.find(mission.getHeading().value()));
            info.start(getDataCallbackForTempState(new FollowMeMissionAbstraction$$Lambda$1(info), MissionState.EXECUTION_STARTING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.READY_TO_EXECUTE, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_START_FAILED), callback));
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_PARAMETERS_INVALID);
        }
    }

    public void pauseMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_PAUSING, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycFollowMeMissionSwitch missionSwitch = DataFlycFollowMeMissionSwitch.getInstance();
        missionSwitch.missionSwitch(DataFlycFollowMeMissionSwitch.FOLLOWMEMISSIONSWITCH.PAUSE).start(getDataCallbackForTempState(new FollowMeMissionAbstraction$$Lambda$2(missionSwitch), MissionState.EXECUTION_PAUSING, desiredMissionStatesHelper(MissionState.EXECUTION_PAUSED), MissionState.EXECUTING, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_FAILED), callback));
    }

    public void resumeMission(CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_RESUMING, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycFollowMeMissionSwitch missionSwitch = DataFlycFollowMeMissionSwitch.getInstance();
        missionSwitch.missionSwitch(DataFlycFollowMeMissionSwitch.FOLLOWMEMISSIONSWITCH.RESUME).start(getDataCallbackForTempState(new FollowMeMissionAbstraction$$Lambda$3(missionSwitch), MissionState.EXECUTION_RESUMING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.EXECUTION_PAUSED, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_RESUME_FAILED), callback));
    }

    public void stopMission(CommonCallbacks.CompletionCallback callback) {
        MissionState previousState = getFSMState();
        if (!tryTransitToTempState(MissionState.EXECUTION_STOPPING, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataFlycCancelFollowMeMission cancelFollowMeMission = DataFlycCancelFollowMeMission.getInstance();
        cancelFollowMeMission.start(getDataCallbackForTempState(new FollowMeMissionAbstraction$$Lambda$4(cancelFollowMeMission), MissionState.EXECUTION_STOPPING, desiredMissionStatesHelper(MissionState.READY_TO_UPLOAD), previousState, new FollowMeAbstractionDataHolder.FollowMeBuilder(MissionEvent.EXECUTION_STOP_FAILED), callback));
    }

    /* access modifiers changed from: protected */
    public boolean doesProductSupportNavigationMode() {
        DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
        if (droneType == DataOsdGetPushCommon.DroneType.PomatoRTK || droneType == DataOsdGetPushCommon.DroneType.WM160) {
            return false;
        }
        return super.doesProductSupportNavigationMode();
    }

    private boolean isValidParameters(@NonNull FollowMeMission data) {
        return data.getHeading() != null && MissionUtils.checkValidGPSCoordinate(data.getLatitude(), data.getLongitude()) && data.getAltitude() >= 0.0f;
    }
}
