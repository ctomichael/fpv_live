package dji.internal.mission.abstraction.intelligenthotpoint;

import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.bus.MissionEventBus;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.flightcontroller.flightassistant.IntelligentHotpointMissionMode;
import dji.common.flightcontroller.flightassistant.PoiException;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.mission.MissionUtils;
import dji.common.mission.intelligenthotpoint.IntelligentHotpointExecutionData;
import dji.common.mission.intelligenthotpoint.IntelligentHotpointMission;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;
import dji.internal.mission.abstraction.intelligenthotpoint.IntelligentHotpointAbstractionDataHolder;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataEyeGetPushPOIExecutionParams;
import dji.midware.data.model.P3.DataEyeGetPushPOITargetInformation;
import dji.midware.data.model.P3.DataEyeSetPOIAction;
import dji.midware.data.model.P3.DataEyeSetPOIInitialTarget;
import dji.midware.data.model.P3.DataEyeSetPOIParams;
import dji.midware.data.model.P3.DataEyeSetPOIStartWithGPS;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class IntelligentHotpointMissionAbstraction extends BaseMissionAbstraction implements DJIParamAccessListener {
    private static final int ESTIMATING = 4;
    private static final int EXECUTING = 5;
    private static final int IDLE = 1;
    private static final float MIN_VALUE_OFFSET = 0.1f;
    private static final int NONE = 0;
    private static final int PAUSED = 6;
    private static final int READY_TO_ESTIMATE = 3;
    private static final int RUNNING = 0;
    private static final int UNKNOWN = 255;
    private static final int WATCHING = 2;
    private IntelligentHotpointAbstractionCacheData cacheData;
    private IntelligentHotpointExecutionData data;
    private boolean isParameterUpdating = false;

    public IntelligentHotpointMissionAbstraction() {
        init();
    }

    private void init() {
        this.cacheData = new IntelligentHotpointAbstractionCacheData();
        reset();
        tryRecoverMissionState();
        startListen();
        refreshEventBusInformation();
    }

    private void startListen() {
        DJIEventBusUtil.register(this);
        CacheHelper.addProductListener(this, ProductKeys.MODEL_NAME);
        CacheHelper.addFlightAssistantListener(this, IntelligentFlightAssistantKeys.IS_POI2_ENABLED, IntelligentFlightAssistantKeys.POI_MISSION_MODE);
    }

    private void refreshEventBusInformation() {
        if (DataEyeGetPushPOIExecutionParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushPOIExecutionParams.getInstance());
        }
        if (DataEyeGetPushPOITargetInformation.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushPOITargetInformation.getInstance());
        }
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCommon.getInstance());
        }
    }

    public void destroy() {
        super.destroy();
        this.cacheData = null;
        CacheHelper.removeListener(this);
        DJIEventBusUtil.unRegister(this);
    }

    public void reset() {
        this.cacheData.reset();
    }

    /* access modifiers changed from: protected */
    @NonNull
    public FiniteStateMachine buildFSM() {
        return new FiniteStateMachine().add(MissionState.RECOVERING).to(MissionState.EXECUTION_PAUSED, MissionState.EXECUTING, MissionState.MEASURING_TARGET, MissionState.WAITING_FOR_CONFIRMATION, MissionState.RECOGNIZING_TARGET, MissionState.READY_TO_START).from(MissionState.NOT_READY).to(MissionState.READY_TO_START).from(MissionState.READY_TO_START).to(MissionState.SEND_TRACK_TARGET, MissionState.EXECUTION_STARTING, MissionState.RECOGNIZING_TARGET, MissionState.MEASURING_TARGET, MissionState.EXECUTING, MissionState.EXECUTION_PAUSED, MissionState.READY_TO_START).from(MissionState.SEND_TRACK_TARGET).to(MissionState.RECOGNIZING_TARGET, MissionState.READY_TO_START).from(MissionState.RECOGNIZING_TARGET).to(MissionState.WAITING_FOR_CONFIRMATION, MissionState.READY_TO_START).from(MissionState.WAITING_FOR_CONFIRMATION).to(MissionState.MEASURING_TARGET_STARTING, MissionState.WAITING_FOR_CONFIRMATION, MissionState.READY_TO_START).from(MissionState.MEASURING_TARGET_STARTING).to(MissionState.MEASURING_TARGET, MissionState.WAITING_FOR_CONFIRMATION, MissionState.READY_TO_START).from(MissionState.EXECUTION_STARTING).to(MissionState.EXECUTING, MissionState.READY_TO_START).from(MissionState.MEASURING_TARGET).to(MissionState.EXECUTING, MissionState.MEASURING_TARGET, MissionState.READY_TO_START).from(MissionState.EXECUTING).to(MissionState.EXECUTION_PAUSING, MissionState.EXECUTION_STOPPING, MissionState.EXECUTING, MissionState.READY_TO_START).from(MissionState.EXECUTION_PAUSING).to(MissionState.EXECUTION_PAUSED, MissionState.EXECUTING, MissionState.READY_TO_START).from(MissionState.EXECUTION_PAUSED).to(MissionState.EXECUTION_RESUMING, MissionState.EXECUTION_STOPPING, MissionState.EXECUTION_PAUSED, MissionState.READY_TO_START).from(MissionState.EXECUTION_RESUMING).to(MissionState.EXECUTING, MissionState.EXECUTION_PAUSED, MissionState.READY_TO_START).from(MissionState.EXECUTION_STOPPING).to(MissionState.EXECUTION_PAUSED, MissionState.EXECUTING, MissionState.READY_TO_START).fromAll().to(MissionState.NOT_READY).fromAll().to(MissionState.DISCONNECTED).superState(MissionState.NOT_SUPPORTED);
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, MissionEvent event) {
        return transitToState(state, new AbstractionDataHolder.Builder(event));
    }

    /* access modifiers changed from: protected */
    public void notifyListener(AbstractionDataHolder holder) {
        if (!holder.equals(this.previousDataHolder)) {
            this.previousDataHolder = holder;
            MissionEventBus.getInstance().post(holder);
        }
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (key.getParamKey().equals(ProductKeys.MODEL_NAME)) {
            if (newValue == null || newValue.getData() == null) {
                reset();
                transitToState(MissionState.DISCONNECTED, MissionEvent.DISCONNECTED);
            } else {
                tryRecoverMissionState();
            }
        }
        if (CacheHelper.isDataValid(key, newValue)) {
            if (key.getParamKey().equals(IntelligentFlightAssistantKeys.IS_POI2_ENABLED)) {
                handlePoi2EnabledChanged(newValue);
            }
            if (key.getParamKey().equals(IntelligentFlightAssistantKeys.POI_MISSION_MODE)) {
                handleIntelligentHotpointMissionModeChanged(newValue);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handlePoi2EnabledChanged(DJISDKCacheParamValue value) {
        boolean isPoi2Enabled = CacheHelper.toBool(value.getData());
        if (!(this.cacheData == null || isPoi2Enabled == this.cacheData.isPoi2Enabled())) {
            this.cacheData.setPoi2Enabled(isPoi2Enabled);
        }
        if (isPoi2Enabled) {
            transitToState(MissionState.READY_TO_START, MissionEvent.ENTER_POI_MODE);
        } else {
            forceTransitToState(MissionState.NOT_READY, MissionEvent.EXIT_POI_MODE);
        }
    }

    /* access modifiers changed from: protected */
    public void handleIntelligentHotpointMissionModeChanged(DJISDKCacheParamValue value) {
        IntelligentHotpointMissionMode missionMode;
        if (value != null && (value.getData() instanceof IntelligentHotpointMissionMode) && (missionMode = (IntelligentHotpointMissionMode) value.getData()) != IntelligentHotpointMissionMode.NONE && missionMode != IntelligentHotpointMissionMode.UNKNOWN && this.cacheData != null && missionMode != this.cacheData.getMissionMode()) {
            this.cacheData.setMissionMode(missionMode);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataEyeGetPushPOIExecutionParams params) {
        handleIntelligentHotpointExecuteStateChanged(params);
    }

    /* access modifiers changed from: protected */
    public void handleIntelligentHotpointExecuteStateChanged(DataEyeGetPushPOIExecutionParams params) {
        if (params.isGetted()) {
            MissionState state = getStateFromMissionParams(params);
            MissionEvent event = getEventFromNextState(state, params);
            if (this.cacheData == null || this.cacheData.getRcMode() == DataOsdGetPushCommon.RcModeChannel.CHANNEL_P || state != MissionState.NOT_READY || event != MissionEvent.EXECUTION_STOPPED) {
                if (!state.equals(MissionState.UNKNOWN) && !event.equals(MissionEvent.UNKNOWN)) {
                    IntelligentHotpointAbstractionDataHolder.HotpointBuilder holder = new IntelligentHotpointAbstractionDataHolder.HotpointBuilder();
                    this.data = new IntelligentHotpointExecutionData();
                    DJIMissionError error = null;
                    PoiException poiException = PoiException.find(params.getException());
                    if (!(poiException == PoiException.NONE || poiException == PoiException.UNKNOWN)) {
                        error = getErrorFromMissionParams(poiException);
                    }
                    this.data.updateMissionData(params, error);
                    holder.event(event);
                    holder.extra(this.data);
                    transitToState(state, holder);
                }
            } else if (!tryTransitToTempState(state, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(event))) {
                CallbackUtils.onResult(null, DJIMissionError.RC_MODE_ERROR);
                return;
            }
            if (this.cacheData != null) {
                this.cacheData.renewMissionDate();
                this.cacheData.setMissionStatus(params);
            }
            tryRecoverMissionState();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataEyeGetPushPOITargetInformation poiTargetInfo) {
        handleIntelligentHotpointTargetInfosChanged(poiTargetInfo);
    }

    /* access modifiers changed from: protected */
    public void handleIntelligentHotpointTargetInfosChanged(DataEyeGetPushPOITargetInformation targetInformation) {
        if (targetInformation.isGetted()) {
            MissionState currentState = getFSMState();
            if (this.cacheData == null || this.cacheData.getRcMode() == DataOsdGetPushCommon.RcModeChannel.CHANNEL_P || currentState != MissionState.NOT_READY) {
                if (currentState == MissionState.WAITING_FOR_CONFIRMATION) {
                    IntelligentHotpointAbstractionDataHolder.HotpointBuilder holder = new IntelligentHotpointAbstractionDataHolder.HotpointBuilder();
                    if (this.data == null) {
                        this.data = new IntelligentHotpointExecutionData();
                    }
                    this.data.setRectF(targetInformation);
                    holder.event(MissionEvent.UNKNOWN);
                    holder.extra(this.data);
                    transitToState(MissionState.WAITING_FOR_CONFIRMATION, holder);
                }
                if (this.cacheData != null) {
                    this.cacheData.renewMissionDate();
                    this.cacheData.setMissionTargetInfo(targetInformation);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataOsdGetPushCommon param) {
        if (param.isGetted() && this.cacheData != null) {
            if (!(this.cacheData.getRcMode() == null || this.cacheData.getRcMode() == param.getModeChannel())) {
                transitToState(MissionState.NOT_READY, MissionEvent.RC_MODE_CHANGED);
            }
            this.cacheData.setRcMode(param.getModeChannel());
            tryRecoverMissionState();
        }
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
        MissionState missionState = MissionState.RECOVERING;
        if (!CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.IS_POI2_ENABLED))) {
            return MissionState.NOT_READY;
        }
        MissionState result = MissionState.READY_TO_START;
        boolean isMissionStatusInitialized = false;
        if (this.cacheData != null && this.cacheData.isMissionStatusInitialized()) {
            isMissionStatusInitialized = true;
        }
        if (isMissionStatusInitialized) {
            return getStateFromMissionParams(this.cacheData.getMissionStatus());
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public boolean canUpdateMissionParameters() {
        return canDownloadMission(getFSMState());
    }

    public boolean canDownloadMission(@NonNull MissionState currentState) {
        return currentState.equals(MissionState.EXECUTING) || currentState.equals(MissionState.EXECUTION_PAUSED) || currentState.equals(MissionState.EXECUTION_PAUSING) || currentState.equals(MissionState.EXECUTION_RESUMING) || currentState.equals(MissionState.MEASURING_TARGET);
    }

    public void setupEnvironment(@Nullable CommonCallbacks.CompletionCallback callback) {
        boolean isPoi2Enabled = CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.IS_POI2_ENABLED));
        if (!(this.cacheData == null || isPoi2Enabled == this.cacheData.isPoi2Enabled())) {
            this.cacheData.setPoi2Enabled(isPoi2Enabled);
        }
        if (!isPoi2Enabled || getFSMState() == MissionState.NOT_READY) {
            setPoi2Enabled(true, callback);
        } else {
            CallbackUtils.onSuccess(callback);
        }
    }

    public void setPoi2Enabled(@NonNull boolean poi2Enabled, @Nullable final CommonCallbacks.CompletionCallback callback) {
        if (poi2Enabled == CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.IS_POI2_ENABLED))) {
            CallbackUtils.onSuccess(callback);
        } else {
            CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.IS_POI2_ENABLED, Boolean.valueOf(poi2Enabled), new DJISetCallback() {
                /* class dji.internal.mission.abstraction.intelligenthotpoint.IntelligentHotpointMissionAbstraction.AnonymousClass1 */

                public void onSuccess() {
                    CallbackUtils.onSuccess(callback);
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    public void sendTrackingRectF(@NonNull RectF rectF, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.SEND_TRACK_TARGET, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.RECOGNIZING_TARGET_FAILED))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataEyeSetPOIInitialTarget.getInstance().setPosition(rectF.centerX(), rectF.centerY(), rectF.width(), rectF.height()).start(getDataCallbackForTempState(IntelligentHotpointMissionAbstraction$$Lambda$0.$instance, MissionState.SEND_TRACK_TARGET, desiredMissionStatesHelper(MissionState.RECOGNIZING_TARGET), MissionState.READY_TO_START, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.RECOGNIZING_TARGET_FAILED), callback));
    }

    static final /* synthetic */ int lambda$sendTrackingRectF$0$IntelligentHotpointMissionAbstraction() {
        return 0;
    }

    public void startTrackingMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.MEASURING_TARGET_STARTING, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_FAILED))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataEyeSetPOIAction.getInstance().setActionType(DataEyeSetPOIAction.ActionType.ESTIMATE).start(getDataCallbackForTempState(IntelligentHotpointMissionAbstraction$$Lambda$1.$instance, MissionState.MEASURING_TARGET_STARTING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.READY_TO_START, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_STARTED), callback));
    }

    static final /* synthetic */ int lambda$startTrackingMission$1$IntelligentHotpointMissionAbstraction() {
        return 0;
    }

    public void startMission(@NonNull IntelligentHotpointMission mission, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STARTING, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_FAILED))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (isValidParameters(mission)) {
            DataEyeSetPOIStartWithGPS.getInstance().setInformation(mission.getHotpoint().getLatitude(), mission.getHotpoint().getLongitude(), mission.getRadius(), mission.getAltitude(), mission.getAngularVelocity()).start(getDataCallbackForTempState(IntelligentHotpointMissionAbstraction$$Lambda$2.$instance, MissionState.EXECUTION_STARTING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.READY_TO_START, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_FAILED), callback));
        } else if (callback != null) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_PARAMETERS_INVALID);
        }
    }

    static final /* synthetic */ int lambda$startMission$2$IntelligentHotpointMissionAbstraction() {
        return 0;
    }

    public void pauseMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_PAUSING, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_FAILED))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataEyeSetPOIAction.getInstance().setActionType(DataEyeSetPOIAction.ActionType.PAUSE).start(getDataCallbackForTempState(IntelligentHotpointMissionAbstraction$$Lambda$3.$instance, MissionState.EXECUTION_PAUSING, desiredMissionStatesHelper(MissionState.EXECUTION_PAUSED), MissionState.EXECUTING, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_PAUSE_FAILED), callback));
    }

    static final /* synthetic */ int lambda$pauseMission$3$IntelligentHotpointMissionAbstraction() {
        return 0;
    }

    public void resumeMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_RESUMING, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_FAILED))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataEyeSetPOIAction.getInstance().setActionType(DataEyeSetPOIAction.ActionType.RESUME).start(getDataCallbackForTempState(IntelligentHotpointMissionAbstraction$$Lambda$4.$instance, MissionState.EXECUTION_RESUMING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.EXECUTION_PAUSED, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_RESUME_FAILED), callback));
    }

    static final /* synthetic */ int lambda$resumeMission$4$IntelligentHotpointMissionAbstraction() {
        return 0;
    }

    public void stopMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (!tryTransitToTempState(MissionState.EXECUTION_STOPPING, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_FAILED))) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        DataEyeSetPOIAction.getInstance().setActionType(DataEyeSetPOIAction.ActionType.STOP).start(getDataCallbackForTempState(IntelligentHotpointMissionAbstraction$$Lambda$5.$instance, MissionState.EXECUTION_STOPPING, desiredMissionStatesHelper(MissionState.READY_TO_START), MissionState.EXECUTING, new IntelligentHotpointAbstractionDataHolder.HotpointBuilder(MissionEvent.EXECUTION_STOP_FAILED), callback));
    }

    static final /* synthetic */ int lambda$stopMission$5$IntelligentHotpointMissionAbstraction() {
        return 0;
    }

    public void updateRadius(float radius, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (!canUpdateMissionParameters() || this.isParameterUpdating) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (((double) radius) < 5.0d || ((double) radius) > 500.0d) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_PARAMETERS_INVALID);
        } else {
            this.isParameterUpdating = true;
            new DataEyeSetPOIParams().setType(DataEyeSetPOIParams.ParamType.RADIUS).setValue(Float.valueOf(radius)).start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.intelligenthotpoint.IntelligentHotpointMissionAbstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                }

                public void onFailure(Ccode ccode) {
                }
            });
            this.tempStateTimer.startTimer(MissionState.EXECUTING, false, 0.5d, new IntelligentHotpointMissionAbstraction$$Lambda$6(this, radius, callback));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$updateRadius$6$IntelligentHotpointMissionAbstraction(float radius, @Nullable CommonCallbacks.CompletionCallback callback) {
        this.isParameterUpdating = false;
        if (Math.abs(this.cacheData.getMissionStatus().getTargetRadius() - radius) < 0.1f) {
            CallbackUtils.onSuccess(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void updateAltitude(float altitude, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (!canUpdateMissionParameters() || this.isParameterUpdating) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (((double) altitude) < 5.0d || ((double) altitude) > 500.0d) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_PARAMETERS_INVALID);
        } else {
            this.isParameterUpdating = true;
            new DataEyeSetPOIParams().setType(DataEyeSetPOIParams.ParamType.HEIGHT).setValue(Float.valueOf(altitude)).start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.intelligenthotpoint.IntelligentHotpointMissionAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                }

                public void onFailure(Ccode ccode) {
                }
            });
            this.tempStateTimer.startTimer(MissionState.EXECUTING, false, 0.5d, new IntelligentHotpointMissionAbstraction$$Lambda$7(this, altitude, callback));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$updateAltitude$7$IntelligentHotpointMissionAbstraction(float altitude, @Nullable CommonCallbacks.CompletionCallback callback) {
        this.isParameterUpdating = false;
        if (Math.abs(this.cacheData.getMissionStatus().getTargetHeight() - altitude) < 0.1f) {
            CallbackUtils.onSuccess(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void updateAngularVelocity(float angularVelocity, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (!canUpdateMissionParameters() || this.isParameterUpdating) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        this.isParameterUpdating = true;
        float velocity = (float) MissionUtils.Radian((double) (angularVelocity * CacheHelper.toFloat(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.POI_RADIUS))));
        if (Math.abs(velocity - CacheHelper.toFloat(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.POI_MAXIMUM_SPEED))) < 0.1f) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_SPEED_TOO_HIGH);
        }
        new DataEyeSetPOIParams().setType(DataEyeSetPOIParams.ParamType.VELOCITY).setValue(Float.valueOf(velocity)).start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.intelligenthotpoint.IntelligentHotpointMissionAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
        this.tempStateTimer.startTimer(MissionState.EXECUTING, false, 0.5d, new IntelligentHotpointMissionAbstraction$$Lambda$8(this, velocity, callback));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$updateAngularVelocity$8$IntelligentHotpointMissionAbstraction(float velocity, @Nullable CommonCallbacks.CompletionCallback callback) {
        this.isParameterUpdating = false;
        if (Math.abs(this.cacheData.getMissionStatus().getSpeed() - velocity) < 0.1f) {
            CallbackUtils.onSuccess(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void updateHeadOrientation(int orientation, @Nullable final CommonCallbacks.CompletionCallback callback) {
        if (!canUpdateMissionParameters()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            new DataEyeSetPOIParams().setType(DataEyeSetPOIParams.ParamType.ORIENTATION).setValue(Integer.valueOf(orientation)).start(new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.intelligenthotpoint.IntelligentHotpointMissionAbstraction.AnonymousClass5 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    private MissionState getStateFromMissionParams(@NonNull DataEyeGetPushPOIExecutionParams params) {
        MissionState state = MissionState.UNKNOWN;
        int poiException = params.getException();
        if (poiException == 0 || poiException == 255) {
            switch (params.getState()) {
                case 0:
                    state = MissionState.NOT_READY;
                    break;
                case 1:
                    state = MissionState.READY_TO_START;
                    break;
                case 2:
                    state = MissionState.RECOGNIZING_TARGET;
                    break;
                case 3:
                    state = MissionState.WAITING_FOR_CONFIRMATION;
                    break;
                case 4:
                    state = MissionState.MEASURING_TARGET;
                    break;
                case 5:
                    state = MissionState.EXECUTING;
                    break;
                case 6:
                    state = MissionState.EXECUTION_PAUSED;
                    break;
            }
            return state;
        } else if (this.cacheData == null || !this.cacheData.isPoi2Enabled()) {
            return MissionState.NOT_READY;
        } else {
            return MissionState.READY_TO_START;
        }
    }

    /* access modifiers changed from: protected */
    public MissionEvent getEventFromNextState(@NonNull MissionState state, @NonNull DataEyeGetPushPOIExecutionParams params) {
        MissionState currentState = getFSMState();
        MissionEvent event = MissionEvent.UNKNOWN;
        if (state.equals(MissionState.NOT_READY) || state.equals(MissionState.READY_TO_START)) {
            if (currentState.equals(state)) {
                return MissionEvent.UNKNOWN;
            }
            int poiException = params.getException();
            if (!(poiException == 0 || poiException == 255)) {
                return MissionEvent.EXECUTION_INTERRUPTED;
            }
        }
        if (state.equals(MissionState.NOT_READY) && currentState.equals(MissionState.READY_TO_START)) {
            event = MissionEvent.EXIT_POI_MODE;
        }
        if (state.equals(MissionState.READY_TO_START)) {
            if (!currentState.equals(MissionState.NOT_READY)) {
                return MissionEvent.EXECUTION_STOPPED;
            }
            event = MissionEvent.ENTER_POI_MODE;
        }
        if (currentState.equals(MissionState.SEND_TRACK_TARGET)) {
            return MissionEvent.RECOGNIZING_TARGET_STARTED;
        }
        if (currentState.equals(MissionState.EXECUTION_PAUSING)) {
            return MissionEvent.EXECUTION_PAUSED;
        }
        if (currentState.equals(MissionState.EXECUTION_STOPPING)) {
            return MissionEvent.EXECUTION_STOPPED;
        }
        if (currentState.equals(MissionState.EXECUTION_RESUMING)) {
            return MissionEvent.EXECUTION_STARTED;
        }
        if (state.equals(MissionState.RECOGNIZING_TARGET) && !currentState.equals(MissionState.RECOGNIZING_TARGET)) {
            event = MissionEvent.RECOGNIZING_TARGET_STARTED;
        }
        if (state.equals(MissionState.WAITING_FOR_CONFIRMATION) && !currentState.equals(MissionState.WAITING_FOR_CONFIRMATION)) {
            event = MissionEvent.WAITING_FOR_CONFIRMATION;
        }
        if (state.equals(MissionState.MEASURING_TARGET) && !currentState.equals(MissionState.MEASURING_TARGET)) {
            event = MissionEvent.MEASURING_TARGET_STARTED;
        }
        if (state.equals(MissionState.EXECUTING)) {
            if (!currentState.equals(MissionState.EXECUTING)) {
                event = MissionEvent.EXECUTION_STARTED;
            } else {
                event = MissionEvent.EXECUTION_PROGRESS_UPDATE;
            }
        }
        if (state.equals(MissionState.EXECUTION_PAUSED) && !currentState.equals(MissionState.EXECUTION_PAUSED)) {
            event = MissionEvent.EXECUTION_PAUSED;
        }
        return event;
    }

    /* access modifiers changed from: protected */
    public DJIMissionError getErrorFromMissionParams(@NonNull PoiException poiException) {
        DJIMissionError error = DJIMissionError.UNKNOWN;
        if (poiException == PoiException.NONE || poiException == PoiException.UNKNOWN) {
            return null;
        }
        switch (poiException) {
            case PER_CONF_VISION_ERR:
            case PER_CONF_SYSTEM_ERR:
            case PRO_PERC_CONN_TIMEOUT:
            case PER_ESTI_VISION_ERR:
            case STA_NAVIGATION_ERR:
            case PRO_PATH_FOLLOWER_ERR:
            case PRO_PATH_GENERATE_ERR:
                error = DJIMissionError.SYSTEM_ABNORMAL;
                break;
            case PER_CONF_TARGET_TOO_SMALL:
                error = DJIMissionError.TARGET_AREA_IS_TOO_SMALL;
                break;
            case PER_CONG_NO_ENOUGH_FEATURE:
                error = DJIMissionError.INSUFFICIENT_FEATURES_IN_TARGET_AREA;
                break;
            case PER_CONF_TRACKING_LOST:
                error = DJIMissionError.LOST_TARGET_WHILE_WATCHING;
                break;
            case PER_ESTI_TRACKING_LOST:
                error = DJIMissionError.LOST_TARGET_WHILE_EXECUTING;
                break;
            case PER_ESTI_RE_PROJ_ERR:
                error = DJIMissionError.REPROJECTION_FAILED_WHILE_EXECUTING;
                break;
            case PER_ESTI_OUT_OF_RANGE:
                error = DJIMissionError.TARGET_TOO_CLOSE;
                break;
            case PER_ESTI_IMAGE_SZ_CHANGE:
                error = DJIMissionError.TARGET_SIZE_CHANGED;
                break;
            case STA_GPS_ERR:
                error = DJIMissionError.GPS_ABNORMAL;
                break;
            case PRO_HOT_POINT_INVALID:
                error = DJIMissionError.HOT_POINT_INVALID;
                break;
            case PRO_WATCH_FAILED:
                error = DJIMissionError.WATCH_TARGET_FAILED;
                break;
            case PRO_ESTIMATE_TIMEOUT:
                error = DJIMissionError.ESTIMATE_TIMEOUT;
                break;
            case PRO_ESTIMATE_RES_INVALID:
                error = DJIMissionError.ESTIMATE_RESULT_INVALID;
                break;
            case USER_BUTTON_STOP:
                error = DJIMissionError.EXIT_BY_USER_BUTTON;
                break;
            case RADIUS_LIMIT:
                error = DJIMissionError.RADIUS_LIMIT;
                break;
            case HEIGHT_LIMIT:
                error = DJIMissionError.ALTITUDE_LIMIT;
                break;
            case OBSTACLE_EST:
                error = DJIMissionError.OBSTACLE_ENCOUNTERED_WHILE_ESTIMATING;
                break;
            case OBSTACLE_PRO:
                error = DJIMissionError.OBSTACLE_ENCOUNTERED_WHILE_SURROUNDING;
                break;
            case TARGET_TOO_FAR:
                error = DJIMissionError.TARGET_IS_TOO_FAR_AWAY;
                break;
            case HEIGHT_TOO_LOW:
                error = DJIMissionError.HOTPOINT_ALTITUDE_TOO_LOW;
                break;
        }
        return error;
    }

    private boolean isValidParameters(@NonNull IntelligentHotpointMission data2) {
        if (data2 == null || data2.getHotpoint() == null || !MissionUtils.checkValidGPSCoordinate(data2.getHotpoint().getLatitude(), data2.getHotpoint().getLongitude()) || ((double) data2.getRadius()) < 5.0d || ((double) data2.getRadius()) > 500.0d || Math.abs(((float) MissionUtils.Radian((double) (data2.getAngularVelocity() * data2.getRadius()))) - CacheHelper.toFloat(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.POI_MAXIMUM_SPEED))) < 0.1f || ((double) data2.getAltitude()) < 5.0d || ((double) data2.getAltitude()) > 500.0d) {
            return false;
        }
        return true;
    }
}
