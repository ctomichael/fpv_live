package dji.internal.mission.abstraction.tapfly;

import android.graphics.PointF;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.bus.MissionEventBus;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.flightcontroller.FlightMode;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.mission.tapfly.BypassDirection;
import dji.common.mission.tapfly.TapFlyExecutionState;
import dji.common.mission.tapfly.TapFlyMission;
import dji.common.mission.tapfly.TapFlyMode;
import dji.common.mission.tapfly.Vector;
import dji.common.product.Model;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.common.util.DirectionUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;
import dji.internal.mission.abstraction.tapfly.TapFlyAbstractionDataHolder;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushPointState;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataSingleCommonCtrl;
import dji.midware.data.model.P3.DataSingleSetPointPos;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class TapFlyAbstraction extends BaseMissionAbstraction implements DJIParamAccessListener {
    private static final Model[] MODELS_WITH_TAP_FLY_ABILITY = {Model.PHANTOM_4, Model.MAVIC_PRO, Model.PHANTOM_4_PRO, Model.INSPIRE_2, Model.MATRICE_200, Model.MATRICE_210_RTK, Model.MATRICE_210, Model.MATRICE_PM420, Model.MATRICE_PM420PRO, Model.MATRICE_PM420PRO_RTK, Model.PHANTOM_4_ADVANCED, Model.MAVIC_AIR, Model.PHANTOM_4_PRO_V2, Model.MAVIC_2_ZOOM, Model.MAVIC_2_PRO, Model.MAVIC_2_ENTERPRISE};
    private static final Model[] MODELS_WITH_TAP_FLY_MODE_ABILITY = {Model.PHANTOM_4_PRO, Model.INSPIRE_2, Model.MATRICE_200, Model.MATRICE_210_RTK, Model.MATRICE_210, Model.MATRICE_PM420, Model.MATRICE_PM420PRO, Model.MATRICE_PM420PRO_RTK, Model.PHANTOM_4_ADVANCED, Model.MAVIC_AIR, Model.PHANTOM_4_PRO_V2, Model.MAVIC_2_ZOOM, Model.MAVIC_2_PRO, Model.MAVIC_2_ENTERPRISE};
    private static final int STATE_TRANSITING_DELAY_TIME = 3000;
    @Nullable
    private DataOsdGetPushCommon.RcModeChannel cacheRcModeChannel;
    @Nullable
    private TapFlyMission cacheTapFlyMission;
    /* access modifiers changed from: private */
    public boolean hasCallbacks;
    private TapFlyMode mode = TapFlyMode.UNKNOWN;
    @Nullable
    private DJIError persistentError;
    private int pointSequence = 0;
    @Nullable
    private TapFlyExecutionState.Builder progressStateBuilder;
    private int sessionId = -1;
    private Runnable startStateListerningRunnable = new Runnable() {
        /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass9 */

        public void run() {
            if (DJIMissionError.STOPPED_BY_USER.equals(TapFlyAbstraction.this.getDJIError(DataEyeGetPushException.getInstance())) && MissionState.CAN_NOT_START.equals(TapFlyAbstraction.this.getFSMState())) {
                TapFlyAbstraction.this.forceTransitToState(MissionState.IDLE, MissionEvent.EXECUTION_STOPPED);
                TapFlyAbstraction.this.specialStop();
            }
            boolean unused = TapFlyAbstraction.this.hasCallbacks = false;
        }
    };

    private void resetCacheData() {
        this.cacheTapFlyMission = null;
        this.cacheRcModeChannel = null;
    }

    private void cloneTapFlyMission(@NonNull TapFlyMission data) {
        this.cacheTapFlyMission = new TapFlyMission();
        this.cacheTapFlyMission.speed = data.speed;
        this.cacheTapFlyMission.isHorizontalObstacleAvoidanceEnabled = data.isHorizontalObstacleAvoidanceEnabled;
        this.cacheTapFlyMission.tapFlyMode = data.tapFlyMode;
        this.cacheTapFlyMission.target = data.target;
    }

    public TapFlyAbstraction() {
        resetCacheData();
        this.progressStateBuilder = new TapFlyExecutionState.Builder();
        startListening();
        tryRecoverState();
    }

    /* access modifiers changed from: protected */
    public void notifyListener(AbstractionDataHolder holder) {
        if (!holder.equals(this.previousDataHolder)) {
            this.previousDataHolder = holder;
            MissionEventBus.getInstance().post(holder);
        }
    }

    public void destroy() {
        this.progressStateBuilder = null;
        stopListening();
    }

    private void startListening() {
        DJIEventBusUtil.register(this);
    }

    private void stopListening() {
        DJIEventBusUtil.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (event == DataEvent.ConnectOK) {
            tryRecoverState();
            return;
        }
        resetCacheData();
        transitToState(MissionState.DISCONNECTED, MissionEvent.DISCONNECTED);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon common) {
        MissionState computedState = computeFSMState();
        if (!computedState.equals(getFSMState())) {
            TapFlyExecutionState executionState = null;
            DJIError error = null;
            if (isPointStatePackValid()) {
                executionState = createExecuteState(DataEyeGetPushPointState.getInstance());
                error = getError(DataEyeGetPushPointState.getInstance());
            }
            if (error != null) {
                TapFlyAbstractionDataHolder.TapFlyBuilder holder = new TapFlyAbstractionDataHolder.TapFlyBuilder(MissionEvent.EXECUTION_FAILED);
                holder.error(error);
                transitToState(computedState, holder);
            } else if (isInTapFlyingState(computedState)) {
                TapFlyAbstractionDataHolder.TapFlyBuilder holder2 = new TapFlyAbstractionDataHolder.TapFlyBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE);
                holder2.extra(executionState);
                transitToState(computedState, holder2);
            } else {
                transitToState(computedState, new TapFlyAbstractionDataHolder.TapFlyBuilder());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(@NonNull DataEyeGetPushPointState state) {
        if (!getFSMState().equals(MissionState.NOT_SUPPORTED)) {
            if (this.sessionId == -1 || this.sessionId == state.getSessionId()) {
                this.sessionId = state.getSessionId();
                if (this.persistentError == null) {
                    if (getFSMState().equals(MissionState.EXECUTION_STARTING) && !state.isRunningDelay() && DataEyeGetPushException.getInstance().isInTapFly()) {
                        resetCacheData();
                    }
                    tryRecoverState();
                    if (!getFSMState().equals(MissionState.RECOVERING) && !getFSMState().equals(MissionState.DISCONNECTED) && !getFSMState().equals(MissionState.NOT_SUPPORTED)) {
                        MissionState currentState = computeFSMState();
                        TapFlyExecutionState executeState = null;
                        if (isInTapFlyingState(currentState) && (executeState = createExecuteState(state)) == null) {
                            currentState = MissionState.EXECUTION_RESETTING;
                        }
                        updatePersistentError(currentState);
                        updateMode(currentState);
                        DJIError error = getError(DataEyeGetPushPointState.getInstance());
                        TapFlyAbstractionDataHolder.TapFlyBuilder holder = new TapFlyAbstractionDataHolder.TapFlyBuilder();
                        if (error != null) {
                            holder.error(error);
                            transitToState(currentState, holder);
                        } else if (isInTapFlyingState(currentState)) {
                            holder.extra(executeState);
                            transitToState(currentState, holder);
                        } else {
                            transitToState(currentState, holder);
                        }
                        if (!isInTapFlyingState(getFSMState()) && !MissionState.EXECUTION_STARTING.equals(getFSMState())) {
                            specialStop();
                        }
                    }
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushException exception) {
        if (!getFSMState().equals(MissionState.NOT_SUPPORTED)) {
            tryRecoverState();
            MissionState curState = getFSMState();
            if (!curState.equals(MissionState.DISCONNECTED) && !curState.equals(MissionState.NOT_SUPPORTED) && !curState.equals(MissionState.RECOVERING)) {
                MissionState state = computeFSMState();
                updateMode(state);
                updatePersistentError(state);
                TapFlyExecutionState executionState = null;
                DJIError error = null;
                if (isPointStatePackValid()) {
                    executionState = createExecuteState(DataEyeGetPushPointState.getInstance());
                    error = getError(DataEyeGetPushPointState.getInstance());
                }
                TapFlyAbstractionDataHolder.TapFlyBuilder holder = new TapFlyAbstractionDataHolder.TapFlyBuilder();
                if (error != null) {
                    holder.error(error);
                    transitToState(state, holder);
                } else if (isInTapFlyingState(state)) {
                    holder.extra(executionState);
                    transitToState(state, holder);
                } else {
                    transitToState(state, holder);
                }
                if (getFSMState().equals(MissionState.EXECUTION_RESETTING)) {
                    specialStop();
                }
            }
        }
    }

    public void onValueChange(@Nullable DJISDKCacheKey key, DJISDKCacheParamValue oldValue, @Nullable DJISDKCacheParamValue newValue) {
        if (key == null || newValue == null || newValue.getData() == null) {
            transitToState(MissionState.DISCONNECTED, MissionEvent.DISCONNECTED);
            resetCacheData();
        } else if (key.getParamKey().equals(ProductKeys.MODEL_NAME)) {
            tryRecoverState();
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public FiniteStateMachine buildFSM() {
        return new FiniteStateMachine().from(MissionState.CAN_NOT_START).to(MissionState.IDLE, MissionState.DISCONNECTED).from(MissionState.IDLE).to(MissionState.CAN_NOT_START, MissionState.EXECUTION_STARTING, MissionState.DISCONNECTED, MissionState.EXECUTING).from(MissionState.EXECUTION_STARTING).to(MissionState.EXECUTING, MissionState.IDLE, MissionState.CAN_NOT_START, MissionState.EXECUTION_RESETTING, MissionState.DISCONNECTED).from(MissionState.EXECUTING).to(MissionState.EXECUTING, MissionState.DISCONNECTED, MissionState.CAN_NOT_START, MissionState.IDLE, MissionState.EXECUTION_RESETTING).from(MissionState.EXECUTION_PAUSED).to(MissionState.EXECUTING, MissionState.IDLE, MissionState.CAN_NOT_START, MissionState.DISCONNECTED).from(MissionState.DISCONNECTED).to(MissionState.CAN_NOT_START, MissionState.IDLE, MissionState.EXECUTING, MissionState.NOT_SUPPORTED, MissionState.EXECUTION_STARTING).from(MissionState.RECOVERING).to(MissionState.DISCONNECTED, MissionState.CAN_NOT_START, MissionState.IDLE, MissionState.EXECUTING).from(MissionState.EXECUTION_RESETTING).to(MissionState.DISCONNECTED, MissionState.IDLE, MissionState.CAN_NOT_START).superState(MissionState.NOT_SUPPORTED);
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, MissionEvent event) {
        return transitToState(state, new TapFlyAbstractionDataHolder.TapFlyBuilder(event));
    }

    public void startMission(@NonNull TapFlyMission data, final CommonCallbacks.CompletionCallback callback) {
        boolean isInSportMode = true;
        if (!tryTransitToTempState(MissionState.EXECUTION_STARTING, new TapFlyAbstractionDataHolder.TapFlyBuilder(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            if (DataOsdGetPushCommon.getInstance().getFlycState() != DataOsdGetPushCommon.FLYC_STATE.SPORT) {
                isInSportMode = false;
            }
            if (isInSportMode) {
                CallbackUtils.onFailure(callback, DJIMissionError.MODE_ERROR);
            } else {
                CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            }
        } else {
            cloneTapFlyMission(data);
            if (shouldEnableTapflySwitch()) {
                CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.TAP_FLY_ENABLED, true, new DJISetCallback() {
                    /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass1 */

                    public void onSuccess() {
                        TapFlyAbstraction.this.configureStartingParams(callback);
                    }

                    public void onFails(DJIError error) {
                        CallbackUtils.onFailure(callback, error);
                    }
                });
            } else {
                configureStartingParams(callback);
            }
        }
    }

    public void stopMission(final CommonCallbacks.CompletionCallback callback) {
        if (getFSMState() != MissionState.EXECUTING) {
            CallbackUtils.onFailure(callback, DJIMissionError.NO_MISSION_RUNNING);
        } else if (shouldEnableTapflySwitch()) {
            CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.TAP_FLY_ENABLED, false, new DJISetCallback() {
                /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass2 */

                public void onSuccess() {
                    TapFlyAbstraction.this.internalStopControl(callback);
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        } else {
            internalStopControl(callback);
        }
    }

    public void resetHeading(CommonCallbacks.CompletionCallback callback) {
        if (!isInTapFlyingState(getFSMState()) || this.mode != TapFlyMode.FREE) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void setAutoFlightSpeed(float speed, final CommonCallbacks.CompletionCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_USER_SPEED).setUserSpeed(speed).start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback);
            }

            public void onFailure(@NonNull Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void getAutoFlightSpeed(final CommonCallbacks.CompletionCallbackWith<Float> callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_USER_SPEED).start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getUserSpeed()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public TapFlyMode getMode() {
        return this.mode;
    }

    public void setHorizontalObstacleBypassEnabled(boolean enable, final CommonCallbacks.CompletionCallback callback) {
        int value;
        if (enable) {
            value = 1;
        } else {
            value = 0;
        }
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_PARALLEL_GO).setParallelGo(value).start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback);
            }

            public void onFailure(@NonNull Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void getHorizontalObstacleBypassEnabled(final CommonCallbacks.CompletionCallbackWith<Boolean> callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_PARALLEL_GO).start(new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.isHorizontalObstacleBypassEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void setupEnvironment(final CommonCallbacks.CompletionCallback callback) {
        if (environmentConditionCheck(callback)) {
            boolean isNoviceEnabled = CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.NOVICE_MODE_ENABLED));
            final boolean isNavigationEnabled = CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.NAVIGATION_MODE_ENABLED));
            if (isNoviceEnabled) {
                DJISDKCache.getInstance().setValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.NOVICE_MODE_ENABLED), false, new DJISetCallback() {
                    /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass7 */

                    public void onSuccess() {
                        TapFlyAbstraction.this.handleNavigation(isNavigationEnabled, callback);
                    }

                    public void onFails(DJIError error) {
                        CallbackUtils.onFailure(callback, error);
                    }
                });
            } else {
                handleNavigation(isNavigationEnabled, callback);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleNavigation(boolean isNavigationModeEnabled, CommonCallbacks.CompletionCallback callback) {
        if (!isNavigationModeEnabled) {
            DJISDKCache.getInstance().setValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.NAVIGATION_MODE_ENABLED), true, CallbackUtils.getSetCallback(callback));
        } else {
            CallbackUtils.onSuccess(callback);
        }
    }

    private boolean environmentConditionCheck(CommonCallbacks.CompletionCallback callback) {
        if (((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME)) == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_DISCONNECTED);
            return false;
        } else if (isTapFlySupported()) {
            return true;
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
            return false;
        }
    }

    private boolean isTapFlySupported() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        for (Model tmpModel : MODELS_WITH_TAP_FLY_ABILITY) {
            if (model == tmpModel) {
                return true;
            }
        }
        return false;
    }

    private boolean isTapFlyModeSupported() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        for (Model tmpModel : MODELS_WITH_TAP_FLY_MODE_ABILITY) {
            if (model == tmpModel) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void specialStop() {
        if (shouldEnableTapflySwitch()) {
            CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.TAP_FLY_ENABLED, false, new DJISetCallback() {
                /* class dji.internal.mission.abstraction.tapfly.TapFlyAbstraction.AnonymousClass8 */

                public void onSuccess() {
                    TapFlyAbstraction.this.internalStopControl(null);
                }

                public void onFails(DJIError error) {
                    TapFlyAbstraction.this.internalStopControl(null);
                }
            });
        } else {
            internalStopControl(null);
        }
    }

    private TapFlyExecutionState createExecuteState(@NonNull DataEyeGetPushPointState state) {
        BypassDirection bypassDirection = BypassDirection.NONE;
        if (state.detourUp()) {
            bypassDirection = BypassDirection.OVER;
        } else if (state.detourRight()) {
            bypassDirection = BypassDirection.RIGHT;
        } else if (state.detourLeft()) {
            bypassDirection = BypassDirection.LEFT;
        }
        float[] dir = DirectionUtils.calculateCurrMovingDir(new float[]{state.getAxisX(), state.getAxisY(), state.getAxisZ()});
        if (dir[0] == -1.0f && dir[1] == -1.0f) {
            return null;
        }
        TapFlyExecutionState.Builder builder = new TapFlyExecutionState.Builder();
        builder.bypassDirection(bypassDirection).direction(new Vector(state.getAxisX(), state.getAxisY(), state.getAxisZ())).speed(state.getMaxSpeed()).imageLocation(new PointF(dir[0], dir[1]));
        if (!Double.isNaN((double) DataOsdGetPushCommon.getInstance().getYaw())) {
            builder.relativeHeading((float) (((Math.atan2((double) state.getAxisY(), (double) state.getAxisX()) * 180.0d) / 3.141592653589793d) - (((double) DataOsdGetPushCommon.getInstance().getYaw()) / 10.0d)));
        } else {
            builder.relativeHeading(0.0f);
        }
        return builder.build();
    }

    private void tryRecoverState() {
        if (this.fsm.getState() == null) {
            if (((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME)) == null) {
                this.fsm.forceTransitTo(MissionState.DISCONNECTED);
            } else if (isTapFlySupported()) {
                this.fsm.forceTransitTo(MissionState.RECOVERING);
            } else {
                this.fsm.forceTransitTo(MissionState.NOT_SUPPORTED);
            }
        }
        MissionState currentState = getFSMState();
        if (currentState.equals(MissionState.DISCONNECTED) || currentState.equals(MissionState.RECOVERING) || currentState.equals(MissionState.NOT_SUPPORTED)) {
            MissionState state = computeFSMState();
            updateMode(state);
            updatePersistentError(state);
            if (currentState.equals(state)) {
                return;
            }
            if (currentState.equals(MissionState.RECOVERING)) {
                forceTransitToState(state, MissionEvent.INITIALIZED);
            } else {
                forceTransitToState(state, MissionEvent.CONNECTED);
            }
        }
    }

    private void updateMode(MissionState state) {
        TapFlyMode mode2 = TapFlyMode.UNKNOWN;
        if (isInTapFlyingState(state) && isPointStatePackValid()) {
            mode2 = getModeFromProtocolToSDK(DataEyeGetPushPointState.getInstance().getTapMode());
        }
        if (this.mode != mode2) {
            this.mode = mode2;
        }
    }

    private void updatePersistentError(MissionState state) {
        DJIError error = null;
        if (state.equals(MissionState.CAN_NOT_START)) {
            error = getDJIError(DataEyeGetPushException.getInstance());
        }
        if (error != this.persistentError) {
            this.persistentError = error;
        }
    }

    @Nullable
    private MissionState computeFSMState() {
        if (DJIProductManager.getInstance().getType() == ProductType.OTHER) {
            return MissionState.DISCONNECTED;
        }
        if (!isExceptionStateValid()) {
            return MissionState.RECOVERING;
        }
        if (((FlightMode) CacheHelper.getFlightController(FlightControllerKeys.FLIGHT_MODE)) == FlightMode.GPS_SPORT) {
            return MissionState.CAN_NOT_START;
        }
        return getState(DataEyeGetPushException.getInstance());
    }

    @Nullable
    private MissionState getState(@NonNull DataEyeGetPushException exceptionPack) {
        DJIError error = getDJIError(exceptionPack);
        if (error != null) {
            if (DJIMissionError.STOPPED_BY_USER.equals(error)) {
                if (this.hasCallbacks && this.handler != null) {
                    this.handler.removeCallbacks(this.startStateListerningRunnable);
                }
                setupStateListeningTimer();
            }
            return MissionState.CAN_NOT_START;
        } else if (!isTapFlying(exceptionPack)) {
            return MissionState.IDLE;
        } else {
            if (isPointStatePackValid()) {
                return getState(DataEyeGetPushPointState.getInstance());
            }
            if (getFSMState().equals(MissionState.DISCONNECTED)) {
                return MissionState.RECOVERING;
            }
            return getFSMState();
        }
    }

    private boolean isTapFlying(@NonNull DataEyeGetPushException exceptionPack) {
        return exceptionPack.isInTapFly();
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    @NonNull
    private MissionState getState(@NonNull DataEyeGetPushPointState state) {
        switch (state.getTragetMode()) {
            case HIDE:
                if (getFSMState().equals(MissionState.EXECUTION_STARTING) && state.isRunningDelay()) {
                    return MissionState.EXECUTION_STARTING;
                }
                if (getFSMState().equals(MissionState.EXECUTION_STARTING) || getFSMState().equals(MissionState.IDLE)) {
                    return MissionState.IDLE;
                }
                break;
            case CANT_FLY:
                break;
            case FLYING:
            default:
                if (getError(state) != null) {
                    return MissionState.EXECUTION_RESETTING;
                }
                if (state.isPaused()) {
                    return MissionState.EXECUTION_PAUSED;
                }
                return MissionState.EXECUTING;
        }
        return MissionState.EXECUTION_RESETTING;
    }

    private boolean shouldEnableTapflySwitch() {
        return DJISDKCache.getInstance().isKeySupported(KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.TAP_FLY_ENABLED));
    }

    /* access modifiers changed from: private */
    public void configureStartingParams(CommonCallbacks.CompletionCallback callback) {
        int sequence = generateTapFlySequence(this.pointSequence);
        float[] sensorPoint = switchSpace(this.cacheTapFlyMission.target.x, this.cacheTapFlyMission.target.y);
        DataSingleSetPointPos pointPos = new DataSingleSetPointPos();
        if (!isTapFlyModeSupported()) {
            pointPos.setPosXY(sensorPoint[0], sensorPoint[1]).setSessionId((short) sequence).setTapMode(DataSingleSetPointPos.TapMode.find(TapFlyMode.FORWARD.value()));
        } else if (CacheHelper.getProduct(ProductKeys.MODEL_NAME) == Model.PHANTOM_4_ADVANCED && this.cacheTapFlyMission.tapFlyMode == TapFlyMode.BACKWARD) {
            transitToState(MissionState.IDLE, MissionEvent.EXECUTION_START_FAILED);
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            return;
        } else {
            pointPos.setPosXY(sensorPoint[0], sensorPoint[1]).setSessionId((short) sequence).setTapMode(DataSingleSetPointPos.TapMode.find(this.cacheTapFlyMission.tapFlyMode.value()));
        }
        pointPos.start(getDataCallbackForTempState(TapFlyAbstraction$$Lambda$0.$instance, MissionState.EXECUTION_STARTING, desiredMissionStatesHelper(MissionState.EXECUTING), MissionState.IDLE, new TapFlyAbstractionDataHolder.TapFlyBuilder(MissionEvent.EXECUTION_FAILED), new TapFlyAbstraction$$Lambda$1(this, sequence, callback)));
    }

    static final /* synthetic */ int lambda$configureStartingParams$0$TapFlyAbstraction() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$configureStartingParams$1$TapFlyAbstraction(int sequence, CommonCallbacks.CompletionCallback callback, DJIError error) {
        if (error == null) {
            this.sessionId = sequence;
            CallbackUtils.onSuccess(callback);
            return;
        }
        CallbackUtils.onFailure(callback, error);
    }

    /* access modifiers changed from: private */
    public void internalStopControl(CommonCallbacks.CompletionCallback callback) {
        new DataSingleCommonCtrl().setCtrlCmd(DataSingleCommonCtrl.CtrlState.STOP).start(CallbackUtils.getDJIDataCallback(callback));
    }

    private Message obtainMessage(int messageId, CommonCallbacks.CompletionCallback callback) {
        return Message.obtain(this.handler, messageId, callback);
    }

    @NonNull
    private float[] switchSpace(float streamX, float streamY) {
        float[] rst = new float[2];
        DirectionUtils.adjustXY(rst, normalize(streamX), normalize(streamY));
        return rst;
    }

    private float normalize(float value) {
        if (value > 1.0f) {
            return 1.0f;
        }
        if (value < 0.0f) {
            return 0.0f;
        }
        return value;
    }

    private int generateTapFlySequence(int sequence) {
        if (sequence >= 512 || sequence <= 0) {
            return 1;
        }
        return sequence + 1;
    }

    private boolean doesExceptionPackContainError(@NonNull DataEyeGetPushException exception) {
        return getDJIError(exception) != null;
    }

    private boolean doesPointStatePackContainError() {
        return getError(DataEyeGetPushPointState.getInstance()) != null;
    }

    /* access modifiers changed from: private */
    public DJIError getDJIError(@NonNull DataEyeGetPushException exception) {
        if (exception.isFusionDataAbnormal()) {
            return DJIMissionError.VISION_DATA_ABNORMAL;
        }
        if (exception.isInNonFlyZone()) {
            return DJIMissionError.AIRCRAFT_IN_NO_FLY_ZONE;
        }
        if (exception.isOutOfControl()) {
            return DJIMissionError.VISION_SYSTEM_NOT_AUTHORIZED;
        }
        if (exception.isNonFlying()) {
            return DJIMissionError.AIRCRAFT_NOT_IN_THE_AIR;
        }
        if (exception.isFrontSensorDemarkAbnormal()) {
            return DJIMissionError.VISION_SYSTEM_NEEDS_CALIBRATION;
        }
        if (exception.isFrontImageDiff()) {
            return DJIMissionError.FEATURE_POINT_CANNOT_MATCH;
        }
        if (exception.isFronImageUnderExposure()) {
            return DJIMissionError.VISION_SENSOR_UNDEREXPOSED;
        }
        if (exception.isFronImageOverExposure()) {
            return DJIMissionError.VISION_SENSOR_OVEREXPOSED;
        }
        if (exception.isVisualDataAbnormal()) {
            return DJIMissionError.VISION_SENSOR_LOW_QUALITY;
        }
        if (exception.isRunningDelay()) {
            return DJIMissionError.VISION_SYSTEM_ERROR;
        }
        if (exception.cantDetour()) {
            return DJIMissionError.CANNOT_BYPASS_OBSTACLE;
        }
        if (exception.isUserQuickPullPitch()) {
            return DJIMissionError.STOPPED_BY_USER;
        }
        if (exception.rcNotInFMode()) {
            return DJIMissionError.RC_MODE_ERROR;
        }
        return null;
    }

    private DJIError getError(@NonNull DataEyeGetPushPointState state) {
        if (state.isNonInFlying()) {
            return DJIMissionError.AIRCRAFT_NOT_IN_THE_AIR;
        }
        if (state.isFrontDemarkError()) {
            return DJIMissionError.VISION_SYSTEM_NEEDS_CALIBRATION;
        }
        if (state.isFrontImageDiff()) {
            return DJIMissionError.FEATURE_POINT_CANNOT_MATCH;
        }
        if (state.isFrontImageUnderExposure()) {
            return DJIMissionError.VISION_SENSOR_UNDEREXPOSED;
        }
        if (state.isFrontImageOverExposure()) {
            return DJIMissionError.VISION_SENSOR_OVEREXPOSED;
        }
        if (state.isRunningDelay()) {
            return DJIMissionError.VISION_SYSTEM_ERROR;
        }
        if (state.cantDetour()) {
            return DJIMissionError.CANNOT_BYPASS_OBSTACLE;
        }
        if (state.isUserQuickPullPitch()) {
            return DJIMissionError.STOPPED_BY_USER;
        }
        if (state.rcNotInFMode()) {
            return DJIMissionError.RC_MODE_ERROR;
        }
        return null;
    }

    public boolean isInTapFlyingState() {
        return isInTapFlyingState(getFSMState());
    }

    private boolean isInTapFlyingState(MissionState state) {
        return state.equals(MissionState.EXECUTING) || state.equals(MissionState.EXECUTION_PAUSED);
    }

    @NonNull
    private TapFlyMode getModeFromProtocolToSDK(@NonNull DataSingleSetPointPos.TapMode mode2) {
        switch (mode2) {
            case POSITIVE_FLY:
                return TapFlyMode.FORWARD;
            case REVERSE_FLY:
                return TapFlyMode.BACKWARD;
            case HEADLESS_PARALLEL:
                return TapFlyMode.FREE;
            default:
                return TapFlyMode.UNKNOWN;
        }
    }

    private boolean isPointStatePackValid() {
        return DataEyeGetPushPointState.getInstance().isGetted();
    }

    private boolean isExceptionStateValid() {
        return DataEyeGetPushException.getInstance().isGetted();
    }

    private void setupStateListeningTimer() {
        if (this.handler != null) {
            this.handler.postDelayed(this.startStateListerningRunnable, 3000);
            this.hasCallbacks = true;
        }
    }
}
