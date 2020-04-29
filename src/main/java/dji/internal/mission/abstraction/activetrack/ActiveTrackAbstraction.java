package dji.internal.mission.abstraction.activetrack;

import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.bus.MissionEventBus;
import dji.common.camera.SettingsDefinitions;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.gimbal.GimbalMode;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.mission.activetrack.ActiveTrackCannotConfirmReason;
import dji.common.mission.activetrack.ActiveTrackMission;
import dji.common.mission.activetrack.ActiveTrackMode;
import dji.common.mission.activetrack.ActiveTrackTargetState;
import dji.common.mission.activetrack.ActiveTrackTargetType;
import dji.common.mission.activetrack.ActiveTrackTrackingState;
import dji.common.mission.activetrack.QuickShotMode;
import dji.common.mission.activetrack.SubjectSensingState;
import dji.common.product.Model;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.abstraction.BaseMissionAbstraction;
import dji.internal.mission.abstraction.activetrack.ActiveTrackAbstractionDataHolder;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingInformation;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingState;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.P3.DataEyePushVisionTip;
import dji.midware.data.model.P3.DataEyeSetQuickMovieParams;
import dji.midware.data.model.P3.DataEyeStartMultiTracking;
import dji.midware.data.model.P3.DataEyeStopMultiTracking;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataSingleCommonCtrl;
import dji.midware.data.model.P3.DataSingleCtrlTrackSelect;
import dji.midware.data.model.P3.DataSingleSetTrackSelect;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.CameraKeys;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import java.util.ArrayList;
import java.util.Iterator;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class ActiveTrackAbstraction extends BaseMissionAbstraction {
    private static final int ACTIVE_TRACK_EVENT_PLACE_HOLDER = -1;
    private static final Model[] AIRCRAFT_SUPPORT_TARGET_FIX = {Model.MAVIC_2_ZOOM, Model.MAVIC_2_PRO, Model.MAVIC_2_ENTERPRISE};
    private static final Model[] AIRCRAFT_WITH_ACTIVE_TRACK_ABILITY = {Model.PHANTOM_4, Model.MAVIC_PRO, Model.PHANTOM_4_PRO, Model.INSPIRE_2, Model.MATRICE_210_RTK, Model.MATRICE_210, Model.MATRICE_200, Model.MATRICE_PM420PRO_RTK, Model.MATRICE_PM420PRO, Model.MATRICE_PM420, Model.MATRICE_600, Model.MATRICE_600_PRO, Model.PHANTOM_4_ADVANCED, Model.Spark, Model.MAVIC_AIR, Model.MAVIC_2_ZOOM, Model.MAVIC_2_PRO, Model.MAVIC_2_ENTERPRISE, Model.PHANTOM_4_PRO_V2};
    private static final Model[] AIRCRAFT_WITH_MULTI_TRACK_ABILITY = {Model.MAVIC_AIR, Model.MAVIC_2_ZOOM, Model.MAVIC_2_PRO, Model.MAVIC_2_ENTERPRISE};
    private static final int CONFIRM_MISSION_DELAY_TIME = 2000;
    private boolean isGestureModeEnabled;
    private ActiveTrackMode mode;
    private DJIError persistentError;
    private QuickShotMode quickShotMode;
    private int trackingSequency = 0;

    private void reset() {
    }

    public ActiveTrackAbstraction() {
        startListen();
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
        stopListen();
    }

    private void startListen() {
        DJIEventBusUtil.register(this);
    }

    private void stopListen() {
        DJIEventBusUtil.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (event == DataEvent.ConnectLose) {
            transitToState(MissionState.DISCONNECTED, new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder());
            reset();
        } else if (event == DataEvent.ConnectOK) {
            tryRecoverState();
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon common) {
        onEvent3BackgroundThread(DataEyeGetPushException.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushException exception) {
        ActiveTrackTrackingState targetState;
        if (getFSMState() != MissionState.NOT_SUPPORTED) {
            tryRecoverState();
            MissionState curState = getFSMState();
            if (curState != MissionState.RECOVERING && curState != MissionState.DISCONNECTED && curState != MissionState.NOT_SUPPORTED) {
                MissionState computedState = computeCurrentState();
                updateGestureModeEnabled();
                updateMode(computedState);
                updatePersistentError(computedState);
                MissionState nextState = getLatestState(exception);
                if (!isMultiTargetSensingSupported() || this.isGestureModeEnabled) {
                    targetState = getTrackingState(DataEyeGetPushTrackStatus.getInstance());
                } else if (isInTrackingState(computedState)) {
                    targetState = getMultiTrackingState(DataEyeGetPushMultiTrackingInformation.getInstance());
                } else if (computedState.equals(MissionState.AUTO_SENSING) || computedState.equals(MissionState.AUTO_SENSING_FOR_QUICK_SHOT)) {
                    targetState = getTargetSensingState(DataEyeGetPushMultiTrackingInformation.getInstance());
                } else {
                    targetState = null;
                }
                DJIError error = getDJIError(DataEyeGetPushTrackStatus.getInstance());
                if (error != null) {
                    targetState = null;
                }
                if (!isInTrackingState(nextState) && !isMultiTargetSensingSupported()) {
                    targetState = null;
                }
                if (isSmartCaptureEnabled()) {
                    targetState = null;
                }
                ActiveTrackAbstractionDataHolder.ActiveTrackBuilder ActiveTrackAbstractionDataHolder = new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder();
                ActiveTrackAbstractionDataHolder.error(error);
                ActiveTrackAbstractionDataHolder.extra(targetState);
                transitToState(nextState, ActiveTrackAbstractionDataHolder);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushTrackStatus status) {
        if (getFSMState() != MissionState.NOT_SUPPORTED) {
            tryRecoverState();
            MissionState curState = getFSMState();
            if (curState != MissionState.RECOVERING && curState != MissionState.DISCONNECTED && curState != MissionState.NOT_SUPPORTED) {
                MissionState computedState = computeCurrentState();
                updatePersistentError(computedState);
                updateMode(computedState);
                updateGestureModeEnabled();
                ActiveTrackTrackingState targetState = getTrackingState(status);
                DJIError error = getDJIError(status);
                if (!isInTrackingState(computedState) || isSmartCaptureEnabled()) {
                    targetState = null;
                }
                ActiveTrackAbstractionDataHolder.ActiveTrackBuilder ActiveTrackAbstractionDataHolder = new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder();
                ActiveTrackAbstractionDataHolder.error(error);
                ActiveTrackAbstractionDataHolder.extra(targetState);
                transitToState(computedState, ActiveTrackAbstractionDataHolder);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushMultiTrackingInformation information) {
        ActiveTrackTrackingState targetState;
        if (getFSMState() != MissionState.NOT_SUPPORTED) {
            tryRecoverState();
            MissionState curState = getFSMState();
            if (curState != MissionState.RECOVERING && curState != MissionState.DISCONNECTED && curState != MissionState.NOT_SUPPORTED) {
                MissionState computedState = computeCurrentState();
                updatePersistentError(computedState);
                updateMode(computedState);
                updateGestureModeEnabled();
                if (isInTrackingState(computedState)) {
                    targetState = getMultiTrackingState(information);
                } else if (computedState.equals(MissionState.AUTO_SENSING) || computedState.equals(MissionState.AUTO_SENSING_FOR_QUICK_SHOT)) {
                    targetState = getTargetSensingState(information);
                } else {
                    targetState = null;
                }
                DJIError error = getDJIError(DataEyeGetPushException.getInstance().getTrackStatus());
                ActiveTrackAbstractionDataHolder.ActiveTrackBuilder ActiveTrackAbstractionDataHolder = new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder();
                ActiveTrackAbstractionDataHolder.error(error);
                ActiveTrackAbstractionDataHolder.extra(targetState);
                transitToState(computedState, ActiveTrackAbstractionDataHolder);
            }
        }
    }

    private void updateGestureModeEnabled() {
        boolean enabled = false;
        if (isWarningStatusValid()) {
            enabled = isGestureModeEnabled(DataEyeGetPushException.getInstance());
        }
        if (this.isGestureModeEnabled != enabled) {
            this.isGestureModeEnabled = enabled;
        }
    }

    private void updateMode(MissionState state) {
        ActiveTrackMode mode2 = ActiveTrackMode.UNKNOWN;
        if (!state.equals(MissionState.CANNOT_START) && !state.equals(MissionState.DETECTING_HUMAN) && !state.equals(MissionState.IDLE) && !state.equals(MissionState.RECOVERING) && isTrackingRectDataValid()) {
            mode2 = activeTrackModeForPackCommandType();
        }
        if (this.mode != mode2) {
            this.mode = mode2;
        }
    }

    private void updatePersistentError(MissionState state) {
        DJIError error = null;
        if (state == MissionState.CANNOT_START) {
            error = getDJIError(DataEyeGetPushException.getInstance().getTrackStatus());
        }
        if (error != this.persistentError) {
            this.persistentError = error;
        }
    }

    /* access modifiers changed from: protected */
    @NonNull
    public FiniteStateMachine buildFSM() {
        FiniteStateMachine tmpFiniteState = new FiniteStateMachine().add(MissionState.DISCONNECTED).add(MissionState.CANNOT_CONFIRM).add(MissionState.AIRCRAFT_FOLLOWING).add(MissionState.QUICK_MOVIE).add(MissionState.CANNOT_START).add(MissionState.DETECTING_HUMAN).add(MissionState.AUTO_SENSING).add(MissionState.AUTO_SENSING_FOR_QUICK_SHOT).add(MissionState.FINDING_TRACKED_TARGET).add(MissionState.IDLE).superState(MissionState.NOT_SUPPORTED).add(MissionState.ONLY_CAMERA_FOLLOWING).add(MissionState.RECOVERING).add(MissionState.WAITING_FOR_CONFIRMATION);
        tmpFiniteState.setLooseModeEnabled(true);
        return tmpFiniteState;
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, MissionEvent event) {
        return transitToState(state, new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder(event));
    }

    public void setGestureModeEnabled(boolean enabled, CommonCallbacks.CompletionCallback callback) {
        if (!enabled || getFSMState().equals(MissionState.CANNOT_START) || getFSMState().equals(MissionState.IDLE) || getFSMState().equals(MissionState.DETECTING_HUMAN)) {
            CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.ADVANCED_GESTURE_CONTROL_ENABLED, Boolean.valueOf(enabled), CallbackUtils.getSetCallback(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    public void setQuickShotSensorEnabled(boolean enabled, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (enabled && !getFSMState().equals(MissionState.CANNOT_START) && !getFSMState().equals(MissionState.IDLE)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (!enabled || !isGestureModeEnabled(DataEyeGetPushException.getInstance())) {
            CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED, Boolean.valueOf(enabled), CallbackUtils.getSetCallback(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_CONFLICT);
        }
    }

    public void setActiveTrackSensorEnabled(boolean enabled, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (enabled && !getFSMState().equals(MissionState.CANNOT_START) && !getFSMState().equals(MissionState.IDLE)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (!enabled || !isGestureModeEnabled(DataEyeGetPushException.getInstance())) {
            CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.IS_MULTI_TRACKING_ENABLED, Boolean.valueOf(enabled), CallbackUtils.getSetCallback(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_CONFLICT);
        }
    }

    public void setRetreatEnabled(boolean enabled, CommonCallbacks.CompletionCallback callback) {
        CacheHelper.setFlightAssistant(IntelligentFlightAssistantKeys.ACTIVE_BACKWARD_FLYING_ENABLED, Boolean.valueOf(enabled), CallbackUtils.getSetCallback(callback));
    }

    public void getRetreatEnabled(CommonCallbacks.CompletionCallbackWith<Boolean> callback) {
        CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.ACTIVE_BACKWARD_FLYING_ENABLED, CallbackUtils.getGetCallback(callback));
    }

    public void startTracking(ActiveTrackMission mission, CommonCallbacks.CompletionCallback callback) {
        RectF trackingRect;
        if (!isMultiTargetSensingSupported() && !getFSMState().equals(MissionState.IDLE)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (isMultiTargetSensingSupported() && !getFSMState().equals(MissionState.IDLE) && !getFSMState().equals(MissionState.AUTO_SENSING) && !getFSMState().equals(MissionState.AUTO_SENSING_FOR_QUICK_SHOT)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (mission == null || mission.getTargetRect() == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            if (mission.getTargetRect().width() == 0.0f || mission.getTargetRect().height() == 0.0f) {
                trackingRect = new RectF(mission.getTargetRect().centerX(), mission.getTargetRect().centerY(), mission.getTargetRect().centerX(), mission.getTargetRect().centerY());
            } else {
                trackingRect = mission.getTargetRect();
            }
            this.trackingSequency = mission.getTargetIndex();
            if (mission.getMode() == ActiveTrackMode.QUICK_SHOT) {
                if (mission.getQuickShotMode() == null || mission.getQuickShotMode() == QuickShotMode.UNKNOWN) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
                    return;
                }
                this.quickShotMode = mission.getQuickShotMode();
            }
            new DataSingleSetTrackSelect().setMode(convertModeToTrackingMode(mission.getMode())).setCenterX(trackingRect.centerX(), trackingRect.centerY(), trackingRect.width(), trackingRect.height()).setSessionId((short) mission.getTargetIndex()).setIsHotTracking(mission.getMode() == ActiveTrackMode.SPOTLIGHT_HEAT_POINT).start(CallbackUtils.getDJIDataCallback(callback));
        }
    }

    public void stopTracking(CommonCallbacks.CompletionCallback callback) {
        if (!isRunningTrackingMission()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        setGestureModeEnabled(false, null);
        if (isMultiTargetSensingSupported()) {
            DataEyeStopMultiTracking.getInstance().setCmdType(0).start(CallbackUtils.getDJIDataCallback(callback));
        } else {
            new DataSingleCommonCtrl().setCtrlCmd(DataSingleCommonCtrl.CtrlState.STOP).start(CallbackUtils.getDJIDataCallback(callback));
        }
    }

    public void switchMode(ActiveTrackMode mode2, CommonCallbacks.CompletionCallback callback) {
        if (!isRunningTrackingMission()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (this.isGestureModeEnabled) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_CONFLICT);
        } else {
            CacheHelper.setValue(KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.ACTIVE_TRACK_MODE), mode2, CallbackUtils.getSetCallback(callback));
        }
    }

    public void acceptConfirmation(CommonCallbacks.CompletionCallback callback) {
        if (!getFSMState().equals(MissionState.WAITING_FOR_CONFIRMATION)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            return;
        }
        short sequence = (short) (this.trackingSequency != Integer.MIN_VALUE ? this.trackingSequency : 0);
        if (isMultiTargetSensingSupported()) {
            ArrayList<Integer> indexes = new ArrayList<>();
            indexes.add(Integer.valueOf(this.trackingSequency));
            startAutoSensingMission(this.mode, this.quickShotMode, indexes, callback);
            return;
        }
        startNormalActiveTrackMission(this.mode, sequence, callback);
    }

    public void acceptConfirmationWithMission(@NonNull ActiveTrackMission activeTrackMission, @Nullable CommonCallbacks.CompletionCallback callback) {
        if (!getFSMState().equals(MissionState.WAITING_FOR_CONFIRMATION) && !getFSMState().equals(MissionState.AUTO_SENSING) && !getFSMState().equals(MissionState.AUTO_SENSING_FOR_QUICK_SHOT)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (activeTrackMission == null || activeTrackMission.getMode() == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            ArrayList<Integer> indexes = new ArrayList<>();
            indexes.add(Integer.valueOf(activeTrackMission.getTargetIndex()));
            startAutoSensingMission(activeTrackMission.getMode(), activeTrackMission.getQuickShotMode(), indexes, callback);
        }
    }

    public void rejectConfirmation(CommonCallbacks.CompletionCallback callback) {
        int i = 0;
        if (!getFSMState().equals(MissionState.WAITING_FOR_CONFIRMATION)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (isMultiTargetSensingSupported()) {
            DataEyeStopMultiTracking.getInstance().setCmdType(0).start(CallbackUtils.getDJIDataCallback(callback));
        } else {
            if (this.trackingSequency != Integer.MIN_VALUE) {
                i = this.trackingSequency;
            }
            new DataSingleCtrlTrackSelect().setCtrlState(DataSingleCtrlTrackSelect.TrackCtrlState.CANCEL).setSessionId((short) i).start(CallbackUtils.getDJIDataCallback(callback));
        }
    }

    public void setCircularSpeed(float speed, final CommonCallbacks.CompletionCallback callback) {
        if (getFSMState().equals(MissionState.AIRCRAFT_FOLLOWING) || getFSMState().equals(MissionState.QUICK_MOVIE)) {
            DJISDKCache.getInstance().setValue(KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.ACTIVE_TRACK_CIRCULAR_SPEED), Float.valueOf(speed), new DJISetCallback() {
                /* class dji.internal.mission.abstraction.activetrack.ActiveTrackAbstraction.AnonymousClass1 */

                public void onSuccess() {
                    CallbackUtils.onSuccess(callback);
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
    }

    public void getCircularSpeed(CommonCallbacks.CompletionCallbackWith<Float> callback) {
        if (getFSMState().equals(MissionState.AIRCRAFT_FOLLOWING) || getFSMState().equals(MissionState.QUICK_MOVIE)) {
            DJISDKCache.getInstance().getValue(KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.ACTIVE_TRACK_CIRCULAR_SPEED), CallbackUtils.getGetCallback(callback));
            return;
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
    }

    public void setRecommendedConfiguration(CommonCallbacks.CompletionCallback callback) {
        DJISDKCacheThreadManager.invoke(new RecommendedConfigurationRunnable(callback), false);
    }

    private class RecommendedConfigurationRunnable implements Runnable {
        private CommonCallbacks.CompletionCallback callback;
        /* access modifiers changed from: private */
        public DJIError djiError = null;
        private long maxRunLoop = 50;
        private int result = 0;
        private DJISetCallback setCallback = new DJISetCallback() {
            /* class dji.internal.mission.abstraction.activetrack.ActiveTrackAbstraction.RecommendedConfigurationRunnable.AnonymousClass1 */

            public void onSuccess() {
                synchronized (this) {
                    RecommendedConfigurationRunnable.access$008(RecommendedConfigurationRunnable.this);
                }
            }

            public void onFails(DJIError error) {
                synchronized (this) {
                    RecommendedConfigurationRunnable.access$008(RecommendedConfigurationRunnable.this);
                    DJIError unused = RecommendedConfigurationRunnable.this.djiError = error;
                }
            }
        };
        private int settingCount = 0;

        static /* synthetic */ int access$008(RecommendedConfigurationRunnable x0) {
            int i = x0.result;
            x0.result = i + 1;
            return i;
        }

        public RecommendedConfigurationRunnable(CommonCallbacks.CompletionCallback callback2) {
            this.callback = callback2;
        }

        public void run() {
            this.settingCount++;
            CacheHelper.setCamera("Mode", SettingsDefinitions.CameraMode.SHOOT_PHOTO, this.setCallback);
            this.settingCount++;
            CacheHelper.setCamera(CameraKeys.AE_LOCK, false, this.setCallback);
            this.settingCount++;
            CacheHelper.setCamera(CameraKeys.EXPOSURE_MODE, SettingsDefinitions.ExposureMode.PROGRAM, this.setCallback);
            this.settingCount++;
            CacheHelper.setCamera(CameraKeys.EXPOSURE_COMPENSATION, SettingsDefinitions.ExposureCompensation.N_0_0, this.setCallback);
            this.settingCount++;
            CacheHelper.setCamera(CameraKeys.METERING_MODE, SettingsDefinitions.MeteringMode.CENTER, this.setCallback);
            this.settingCount++;
            CacheHelper.setCamera(CameraKeys.PHOTO_QUICK_VIEW_DURATION, 0, this.setCallback);
            this.settingCount++;
            CacheHelper.setGimbal("Mode", GimbalMode.YAW_FOLLOW, this.setCallback);
            if (DJISDKCache.getInstance().isKeySupported(KeyHelper.getCameraKey(0, CameraKeys.ORIENTATION)) && ((SettingsDefinitions.Orientation) CacheHelper.getCamera(0, CameraKeys.ORIENTATION)) != SettingsDefinitions.Orientation.LANDSCAPE) {
                this.settingCount++;
                CacheHelper.setCamera(CameraKeys.ORIENTATION, SettingsDefinitions.Orientation.LANDSCAPE, this.setCallback);
            }
            long currentRunLoop = 0;
            while (this.result < this.settingCount) {
                currentRunLoop++;
                try {
                    Thread.sleep(100);
                    if (currentRunLoop > this.maxRunLoop) {
                        if (this.callback != null) {
                            this.callback.onResult(DJIError.COMMON_TIMEOUT);
                            return;
                        }
                        return;
                    }
                } catch (InterruptedException e) {
                    if (currentRunLoop > this.maxRunLoop) {
                        if (this.callback != null) {
                            this.callback.onResult(DJIError.COMMON_TIMEOUT);
                            return;
                        }
                        return;
                    }
                } catch (Throwable th) {
                    if (currentRunLoop <= this.maxRunLoop) {
                        throw th;
                    } else if (this.callback != null) {
                        this.callback.onResult(DJIError.COMMON_TIMEOUT);
                        return;
                    } else {
                        return;
                    }
                }
            }
            if (this.callback != null) {
                this.callback.onResult(this.djiError);
            }
        }
    }

    public void stopAircraftFollowing(CommonCallbacks.CompletionCallback callback) {
        if ((!getFSMState().equals(MissionState.AIRCRAFT_FOLLOWING) && !getFSMState().equals(MissionState.QUICK_MOVIE) && !getFSMState().equals(MissionState.FINDING_TRACKED_TARGET)) || (this.mode != ActiveTrackMode.PROFILE && this.mode != ActiveTrackMode.TRACE)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else if (isMultiTargetSensingSupported()) {
            CallbackUtils.onFailure(callback, DJIMissionError.COMMAND_NOT_SUPPORTED_BY_FIRMWARE);
        } else {
            new DataSingleCtrlTrackSelect().setCtrlState(DataSingleCtrlTrackSelect.TrackCtrlState.PAUSE).setSessionId((short) (this.trackingSequency != Integer.MIN_VALUE ? this.trackingSequency : 0)).start((DJIDataCallBack) null);
        }
    }

    private void tryRecoverState() {
        if (this.fsm.getState() == null) {
            if (((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME)) == null) {
                forceTransitToState(MissionState.DISCONNECTED, new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder());
            } else if (isActiveTrackSupported()) {
                forceTransitToState(MissionState.RECOVERING, new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder());
            } else {
                forceTransitToState(MissionState.NOT_SUPPORTED, new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder());
            }
        }
        MissionState curState = getFSMState();
        if (curState.equals(MissionState.DISCONNECTED) || curState.equals(MissionState.RECOVERING) || curState.equals(MissionState.NOT_SUPPORTED)) {
            MissionState state = computeCurrentState();
            updateGestureModeEnabled();
            updateMode(state);
            updatePersistentError(state);
            if (!curState.equals(state)) {
                forceTransitToState(state, new ActiveTrackAbstractionDataHolder.ActiveTrackBuilder());
            }
        }
    }

    private MissionState computeCurrentState() {
        MissionState curState = getFSMState();
        if (((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME)) == null) {
            return MissionState.DISCONNECTED;
        }
        if (!isWarningStatusValid()) {
            return MissionState.RECOVERING;
        }
        MissionState nonTrackingState = MissionState.IDLE;
        if (isGestureModeEnabled(DataEyeGetPushException.getInstance())) {
            if (isMultiTargetSensingSupported()) {
                return MissionState.CANNOT_START;
            }
            nonTrackingState = MissionState.DETECTING_HUMAN;
        }
        if (isMultiTrackingEnabled()) {
            nonTrackingState = MissionState.AUTO_SENSING;
        }
        if (isMultiQuickShotEnabled()) {
            nonTrackingState = MissionState.AUTO_SENSING_FOR_QUICK_SHOT;
        }
        if (!isTracking(DataEyeGetPushException.getInstance())) {
            if (DataEyeGetPushException.getInstance().getTrackStatus() != DataEyeGetPushException.TrackExceptionStatus.NORMAL) {
                return MissionState.CANNOT_START;
            }
            return nonTrackingState;
        } else if (!isTrackingRectDataValid()) {
            return curState;
        } else {
            return getLatestState(DataEyeGetPushException.getInstance());
        }
    }

    private boolean isTracking(DataEyeGetPushException exception) {
        return (exception.isInTracking() || hasMultiTrackingStarted()) && !isExitingExceptionConde(exception.getTrackStatus());
    }

    private MissionState getLatestState(DataEyeGetPushException exception) {
        if (activeTrackModeForPackCommandType() != ActiveTrackMode.SPOTLIGHT_HEAT_POINT && exception.getTrackStatus() != DataEyeGetPushException.TrackExceptionStatus.NORMAL) {
            return MissionState.CANNOT_START;
        }
        if (isMultiTargetSensingSupported() && isGestureModeEnabled(exception)) {
            return MissionState.CANNOT_START;
        }
        if (!(exception.isInTracking() || hasMultiTrackingStarted())) {
            if (isGestureModeEnabled(exception)) {
                return MissionState.DETECTING_HUMAN;
            }
            if (isMultiTrackingEnabled()) {
                return MissionState.AUTO_SENSING;
            }
            if (isMultiQuickShotEnabled()) {
                return MissionState.AUTO_SENSING_FOR_QUICK_SHOT;
            }
            return MissionState.IDLE;
        } else if (!isTrackingRectDataValid()) {
            return MissionState.IDLE;
        } else {
            return getLatestState(DataEyeGetPushTrackStatus.getInstance());
        }
    }

    private MissionState getLatestState(DataEyeGetPushTrackStatus status) {
        MissionState nonTrackingState = MissionState.IDLE;
        boolean isGestureModeEnabled2 = isGestureModeEnabled(DataEyeGetPushException.getInstance());
        if (isGestureModeEnabled2) {
            nonTrackingState = MissionState.DETECTING_HUMAN;
        }
        if (isMultiTrackingEnabled()) {
            nonTrackingState = MissionState.AUTO_SENSING;
        }
        if (isMultiQuickShotEnabled()) {
            nonTrackingState = MissionState.AUTO_SENSING_FOR_QUICK_SHOT;
        }
        if (activeTrackModeForPackCommandType() != ActiveTrackMode.SPOTLIGHT_HEAT_POINT && DataEyeGetPushException.getInstance().getTrackStatus() != DataEyeGetPushException.TrackExceptionStatus.NORMAL) {
            return nonTrackingState;
        }
        if (!isMultiTargetSensingSupported() || isGestureModeEnabled2) {
            return getExecutingState(status);
        }
        return getExecutingState(DataEyeGetPushMultiTrackingState.getInstance());
    }

    private MissionState getExecutingState(DataEyeGetPushTrackStatus status) {
        if (activeTrackModeForPackCommandType() == ActiveTrackMode.SPOTLIGHT_HEAT_POINT) {
            return MissionState.ONLY_CAMERA_FOLLOWING;
        }
        DataEyeGetPushTrackStatus.TrackMode rectMode = status.getRectMode();
        boolean isRectValid = isRectDimissionValid(status);
        boolean isCriticalException = isCriticalException(DataEyeGetPushTrackStatus.getInstance().getException());
        if (rectMode == DataEyeGetPushTrackStatus.TrackMode.LOST || rectMode == DataEyeGetPushTrackStatus.TrackMode.DETECT_AFTER_LOST || !isRectValid) {
            return MissionState.FINDING_TRACKED_TARGET;
        }
        DataSingleVisualParam.TrackingMode trackingType = status.getTrackingMode();
        boolean isSpotlight = trackingType == DataSingleVisualParam.TrackingMode.WATCH_TARGET || trackingType == DataSingleVisualParam.TrackingMode.HEAD_LOCK;
        if (rectMode == DataEyeGetPushTrackStatus.TrackMode.NORMAL || rectMode == DataEyeGetPushTrackStatus.TrackMode.TRACKING || rectMode == DataEyeGetPushTrackStatus.TrackMode.WEAK) {
            if (isSpotlight) {
                return MissionState.ONLY_CAMERA_FOLLOWING;
            }
            if (trackingType == DataSingleVisualParam.TrackingMode.QUICK_MOVIE) {
                return MissionState.QUICK_MOVIE;
            }
            return MissionState.AIRCRAFT_FOLLOWING;
        } else if (rectMode == DataEyeGetPushTrackStatus.TrackMode.CONFIRM && isCriticalException) {
            return MissionState.CANNOT_CONFIRM;
        } else {
            if (rectMode != DataEyeGetPushTrackStatus.TrackMode.CONFIRM || isCriticalException) {
                return MissionState.UNKNOWN;
            }
            return MissionState.WAITING_FOR_CONFIRMATION;
        }
    }

    private MissionState getExecutingState(DataEyeGetPushMultiTrackingState state) {
        boolean isCriticalException = isCriticalException(state.getTrackingException());
        if (!isMultiTrackWaitingForConfirmation()) {
            if (isMultiTrackExecuting()) {
                ArrayList<DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation> targets = DataEyeGetPushMultiTrackingInformation.getInstance().getMultiTrackingTargetsInformation();
                if (targets.size() > 0) {
                    DataEyeGetPushTrackStatus.TrackMode rectMode = DataEyeGetPushTrackStatus.TrackMode.find(targets.get(0).mode);
                    if (rectMode == DataEyeGetPushTrackStatus.TrackMode.LOST) {
                        return MissionState.FINDING_TRACKED_TARGET;
                    }
                    DataSingleVisualParam.TrackingMode trackingType = DataSingleVisualParam.TrackingMode.find(state.getOverallTrackingMode());
                    if (rectMode == DataEyeGetPushTrackStatus.TrackMode.NORMAL || rectMode == DataEyeGetPushTrackStatus.TrackMode.TRACKING || rectMode == DataEyeGetPushTrackStatus.TrackMode.WEAK || rectMode == DataEyeGetPushTrackStatus.TrackMode.DETECT_AFTER_LOST) {
                        if (trackingType == DataSingleVisualParam.TrackingMode.SPOTLIGHT) {
                            return MissionState.ONLY_CAMERA_FOLLOWING;
                        }
                        if (trackingType == DataSingleVisualParam.TrackingMode.QUICK_MOVIE) {
                            return MissionState.QUICK_MOVIE;
                        }
                        return MissionState.AIRCRAFT_FOLLOWING;
                    }
                }
            }
            return MissionState.UNKNOWN;
        } else if (isCriticalException) {
            return MissionState.CANNOT_CONFIRM;
        } else {
            return MissionState.WAITING_FOR_CONFIRMATION;
        }
    }

    private void startAutoSensingMission(ActiveTrackMode mode2, final QuickShotMode quickShotMode2, final ArrayList<Integer> indexes, final CommonCallbacks.CompletionCallback callback) {
        if (isMultiQuickShotEnabled()) {
            if (quickShotMode2 == null || quickShotMode2 == QuickShotMode.UNKNOWN) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
            } else if (shouldConfirmMissionTwice()) {
                configQuickMovieForAutoSensing(false, quickShotMode2, indexes, new DJIDataCallBack() {
                    /* class dji.internal.mission.abstraction.activetrack.ActiveTrackAbstraction.AnonymousClass2 */

                    public void onSuccess(Object model) {
                        BackgroundLooper.postDelayed(new ConfirmTrackingRunnable(true, quickShotMode2, indexes, CallbackUtils.getDJIDataCallback(callback)), 2000);
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, ccode);
                    }
                });
            } else {
                configQuickMovieForAutoSensing(true, quickShotMode2, indexes, CallbackUtils.getDJIDataCallback(callback));
            }
        } else if (isMultiTrackingEnabled()) {
            DataEyeStartMultiTracking.getInstance().setTrackingMode(convertModeToTrackingMode(mode2)).setNumberOfTracking(indexes.size()).setIndexList(indexes).start(CallbackUtils.getDJIDataCallback(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        }
    }

    private void startNormalActiveTrackMission(ActiveTrackMode mode2, final short index, final CommonCallbacks.CompletionCallback callback) {
        if (mode2 == ActiveTrackMode.QUICK_SHOT) {
            startQuickMovie(true, this.quickShotMode, new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.activetrack.ActiveTrackAbstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    new DataSingleCtrlTrackSelect().setCtrlState(DataSingleCtrlTrackSelect.TrackCtrlState.CONFIRM).setSessionId(index).start(CallbackUtils.getDJIDataCallback(callback));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else {
            new DataSingleCtrlTrackSelect().setCtrlState(DataSingleCtrlTrackSelect.TrackCtrlState.CONFIRM).setSessionId(index).start(CallbackUtils.getDJIDataCallback(callback));
        }
    }

    /* access modifiers changed from: private */
    public void configQuickMovieForAutoSensing(boolean isStarted, QuickShotMode mode2, final ArrayList<Integer> indexes, final DJIDataCallBack callback) {
        if (callback != null) {
            startQuickMovie(isStarted, mode2, new DJIDataCallBack() {
                /* class dji.internal.mission.abstraction.activetrack.ActiveTrackAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    DataEyeStartMultiTracking.getInstance().setTrackingMode(DataSingleVisualParam.TrackingMode.QUICK_MOVIE).setNumberOfTracking(indexes.size()).setIndexList(indexes).start(callback);
                }

                public void onFailure(Ccode ccode) {
                    callback.onFailure(ccode);
                }
            });
        }
    }

    private class ConfirmTrackingRunnable implements Runnable {
        private final DJIDataCallBack dataCallBack;
        private final ArrayList<Integer> indexes;
        private final boolean isStarted;
        private final QuickShotMode quickShotMode;

        public ConfirmTrackingRunnable(boolean isStarted2, QuickShotMode quickShotMode2, ArrayList<Integer> indexes2, DJIDataCallBack dataCallBack2) {
            this.isStarted = isStarted2;
            this.quickShotMode = quickShotMode2;
            this.indexes = indexes2;
            this.dataCallBack = dataCallBack2;
        }

        public void run() {
            ActiveTrackAbstraction.this.configQuickMovieForAutoSensing(this.isStarted, this.quickShotMode, this.indexes, this.dataCallBack);
        }
    }

    private void startQuickMovie(boolean isStarted, QuickShotMode type, DJIDataCallBack callback) {
        int isStart = isStarted ? 1 : 0;
        float time = 60.0f;
        float distance = 0.0f;
        float circleSpeed = 0.0f;
        float leaveSpeed = 0.0f;
        float verticalSpeed = 0.0f;
        switch (type) {
            case UNKNOWN:
                isStart = 0;
                time = 0.0f;
                break;
            case DRONIE:
                circleSpeed = 0.0f;
                leaveSpeed = -4.0f;
                verticalSpeed = 1.0f;
                distance = 40.0f;
                break;
            case CIRCLE:
                circleSpeed = 4.0f;
                leaveSpeed = 0.0f;
                verticalSpeed = 0.0f;
                distance = 40.0f;
                break;
            case HELIX:
                circleSpeed = -5.0f;
                leaveSpeed = -4.0f;
                verticalSpeed = 1.0f;
                distance = 40.0f;
                break;
            case ROCKET:
                circleSpeed = 0.0f;
                leaveSpeed = 0.0f;
                verticalSpeed = 2.0f;
                distance = 40.0f;
                break;
            case BOOMERANG:
                circleSpeed = 0.0f;
                leaveSpeed = 0.0f;
                verticalSpeed = 0.0f;
                break;
            case DOLLY_ZOOM:
                circleSpeed = 1.0f;
                leaveSpeed = 1.0f;
                verticalSpeed = 1.0f;
                distance = 40.0f;
                break;
        }
        ArrayList<DataEyeSetQuickMovieParams.ActionParam> actionParams = new ArrayList<>();
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.ACTION_TYPE, Integer.valueOf(type.value())));
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.IS_START, Integer.valueOf(isStart)));
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.VELOCITY_X, Float.valueOf(leaveSpeed)));
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.VELOCITY_Y, Float.valueOf(circleSpeed)));
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.VELOCITY_Z, Float.valueOf(verticalSpeed)));
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.DISTANCE, Float.valueOf(distance)));
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.TIME, Float.valueOf(time)));
        actionParams.add(new DataEyeSetQuickMovieParams.ActionParam(DataEyeSetQuickMovieParams.ActionParamIndex.END_OF_PARAMS));
        DataEyeSetQuickMovieParams.getInstance().setActionParams(actionParams).start(callback);
    }

    private boolean isCriticalException(DataEyeGetPushTrackStatus.TrackException exception) {
        switch (exception) {
            case NONE:
            case LOW_DETECT:
            case APP_HALT:
                return false;
            default:
                return true;
        }
    }

    private boolean isExitingExceptionConde(DataEyeGetPushException.TrackExceptionStatus exception) {
        if (activeTrackModeForPackCommandType() == ActiveTrackMode.SPOTLIGHT_HEAT_POINT) {
            return false;
        }
        switch (exception) {
            case LOST_TIMEOUT:
            case INVALID_SPEED:
            case NONE_IMAGE:
            case LOW_FRAME:
            case NFZ:
            case RCCONN_TIMEOUT:
            case APPCONN_TIMEOUT:
            case LOST_CONTROL:
            case APP_STOP:
            case EXIT_BYSELF:
            case TOO_SMALL:
            case TOO_LARGE:
            case NO_DETECT:
            case PITCH_LOW:
                return true;
            default:
                return false;
        }
    }

    private boolean isActiveTrackSupported() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        for (Model tmpModel : AIRCRAFT_WITH_ACTIVE_TRACK_ABILITY) {
            if (tmpModel == model) {
                return true;
            }
        }
        return false;
    }

    private boolean isMultiTargetSensingSupported() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        for (Model tmpModel : AIRCRAFT_WITH_MULTI_TRACK_ABILITY) {
            if (tmpModel == model) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldConfirmMissionTwice() {
        Model model = (Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME);
        for (Model tmpModel : AIRCRAFT_SUPPORT_TARGET_FIX) {
            if (tmpModel == model) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public DataSingleVisualParam.TrackingMode convertModeToTrackingMode(ActiveTrackMode mode2) {
        switch (mode2) {
            case TRACE:
                return DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
            case SPOTLIGHT:
                if (((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME)) == Model.MAVIC_AIR) {
                    return DataSingleVisualParam.TrackingMode.SPOTLIGHT;
                }
                return DataSingleVisualParam.TrackingMode.HEAD_LOCK;
            case SPOTLIGHT_PRO:
                return DataSingleVisualParam.TrackingMode.WATCH_TARGET;
            case SPOTLIGHT_HEAT_POINT:
                return DataSingleVisualParam.TrackingMode.WATCH_TARGET;
            case PROFILE:
                return DataSingleVisualParam.TrackingMode.FIXED_ANGLE;
            case QUICK_SHOT:
                return DataSingleVisualParam.TrackingMode.QUICK_MOVIE;
            default:
                return DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
        }
    }

    private boolean isTrackingRectDataValid() {
        return DataEyeGetPushTrackStatus.getInstance().isGetted() || DataEyeGetPushMultiTrackingInformation.getInstance().isGetted();
    }

    private boolean isWarningStatusValid() {
        return DataEyeGetPushException.getInstance().isGetted();
    }

    public boolean isGestureModeEnabled(DataEyeGetPushException exception) {
        return exception.isMovingObjectDetectEnable();
    }

    private boolean isRectDimissionValid(DataEyeGetPushTrackStatus status) {
        return (status.getCenterX() == 0.0f && status.getCenterY() == 0.0f && status.getWidth() == 0.0f && status.getHeight() == 0.0f) ? false : true;
    }

    public boolean isInTrackingState() {
        return isInTrackingState(getFSMState());
    }

    private boolean isInTrackingState(MissionState state) {
        if (state.equals(MissionState.FINDING_TRACKED_TARGET) || state.equals(MissionState.AIRCRAFT_FOLLOWING) || state.equals(MissionState.ONLY_CAMERA_FOLLOWING) || state.equals(MissionState.CANNOT_CONFIRM) || state.equals(MissionState.WAITING_FOR_CONFIRMATION) || state.equals(MissionState.QUICK_MOVIE)) {
            return true;
        }
        return false;
    }

    private boolean isSmartCaptureEnabled() {
        return isMultiTargetSensingSupported() && this.isGestureModeEnabled;
    }

    private ActiveTrackMode activeTrackModeForPackCommandType() {
        DataSingleVisualParam.TrackingMode trackingMode;
        if (isMultiTargetSensingSupported()) {
            trackingMode = DataSingleVisualParam.TrackingMode.find(DataEyeGetPushMultiTrackingState.getInstance().getOverallTrackingMode());
        } else {
            trackingMode = DataEyeGetPushTrackStatus.getInstance().getTrackingMode();
        }
        DataEyeGetPushTrackStatus.TargetObjType targetObjType = DataEyeGetPushTrackStatus.getInstance().getTargetType();
        switch (trackingMode) {
            case HEADLESS_FOLLOW:
                return ActiveTrackMode.TRACE;
            case HEAD_LOCK:
            case SPOTLIGHT:
                return ActiveTrackMode.SPOTLIGHT;
            case FIXED_ANGLE:
                return ActiveTrackMode.PROFILE;
            case WATCH_TARGET:
                if (targetObjType == DataEyeGetPushTrackStatus.TargetObjType.HOT_POINT) {
                    return ActiveTrackMode.SPOTLIGHT_HEAT_POINT;
                }
                return ActiveTrackMode.SPOTLIGHT_PRO;
            case QUICK_MOVIE:
                return ActiveTrackMode.QUICK_SHOT;
            default:
                return ActiveTrackMode.UNKNOWN;
        }
    }

    private DJIError getDJIError(DataEyeGetPushException.TrackExceptionStatus exception) {
        switch (exception) {
            case LOST_TIMEOUT:
                return DJIMissionError.TRACKING_TARGET_LOST;
            case INVALID_SPEED:
                return DJIMissionError.VISION_DATA_ABNORMAL;
            case NONE_IMAGE:
                return DJIMissionError.NO_VIDEO_FEED;
            case LOW_FRAME:
                return DJIMissionError.VIDEO_FRAME_RATE_TOO_LOW;
            case NFZ:
                return DJIMissionError.AIRCRAFT_IN_NO_FLY_ZONE;
            case RCCONN_TIMEOUT:
            case APPCONN_TIMEOUT:
            case LOST_CONTROL:
            case APP_STOP:
            case EXIT_BYSELF:
            default:
                return null;
            case TOO_SMALL:
                return DJIMissionError.TRACKING_RECT_TOO_SMALL;
            case TOO_LARGE:
                return DJIMissionError.TRACKING_RECT_TOO_LARGE;
            case NO_DETECT:
                return DJIMissionError.TRACKING_TARGET_NOT_ENOUGH_FEATURES;
            case PITCH_LOW:
                return DJIMissionError.TRACKING_GIMBAL_PITCH_TOO_LOW;
        }
    }

    private DJIError getDJIError(DataEyeGetPushTrackStatus status) {
        DataEyeGetPushTrackStatus.TrackMode mode2;
        if (activeTrackModeForPackCommandType() == ActiveTrackMode.SPOTLIGHT_HEAT_POINT || (mode2 = status.getRectMode()) == DataEyeGetPushTrackStatus.TrackMode.CONFIRM || mode2 == DataEyeGetPushTrackStatus.TrackMode.NORMAL || mode2 == DataEyeGetPushTrackStatus.TrackMode.WEAK || mode2 == DataEyeGetPushTrackStatus.TrackMode.TRACKING) {
            return null;
        }
        return getDJIError(DataEyeGetPushException.getInstance().getTrackStatus());
    }

    private ActiveTrackTrackingState getTrackingState(DataEyeGetPushTrackStatus status) {
        if (status.getRectMode() == DataEyeGetPushTrackStatus.TrackMode.LOST || !isRectDimissionValid(status)) {
            return null;
        }
        ActiveTrackTargetState state = getTargetState(status);
        ActiveTrackTargetType type = getTargetType(status.getTargetType());
        RectF rectF = new RectF(status.getCenterX() - (status.getWidth() / 2.0f), status.getCenterY() - (status.getHeight() / 2.0f), status.getCenterX() + (status.getWidth() / 2.0f), status.getCenterY() + (status.getHeight() / 2.0f));
        int targetIndex = status.getSessionId();
        int completionPercentage = DataEyePushVisionTip.getInstance().getQuickMovieProgress();
        return new ActiveTrackTrackingState.Builder().state(state).targetType(type).targetRect(rectF).index(targetIndex).completionPercentage(completionPercentage).reason(getReason(status.getException())).build();
    }

    @Nullable
    private ActiveTrackTrackingState getMultiTrackingState(@NonNull DataEyeGetPushMultiTrackingInformation information) {
        ArrayList<DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation> targets = information.getMultiTrackingTargetsInformation();
        if (targets.size() > 0) {
            DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation trackingTarget = targets.get(0);
            if (DataEyeGetPushTrackStatus.TrackMode.find(trackingTarget.mode) != DataEyeGetPushTrackStatus.TrackMode.LOST) {
                ActiveTrackTargetState state = getTargetStateWithTrackMode(DataEyeGetPushTrackStatus.TrackMode.find(trackingTarget.mode));
                ActiveTrackTargetType type = getTargetType(DataEyeGetPushTrackStatus.TargetObjType.find(trackingTarget.type));
                RectF rectF = new RectF(trackingTarget.centerX - (trackingTarget.width / 2.0f), trackingTarget.centerY - (trackingTarget.height / 2.0f), trackingTarget.centerX + (trackingTarget.width / 2.0f), trackingTarget.centerY + (trackingTarget.height / 2.0f));
                int targetIndex = trackingTarget.index;
                int completionPercentage = DataEyePushVisionTip.getInstance().getQuickMovieProgress();
                return new ActiveTrackTrackingState.Builder().state(state).targetType(type).targetRect(rectF).index(targetIndex).completionPercentage(completionPercentage).reason(getReason(DataEyeGetPushMultiTrackingState.getInstance().getTrackingException())).build();
            }
        }
        return null;
    }

    @Nullable
    private ActiveTrackTrackingState getTargetSensingState(@NonNull DataEyeGetPushMultiTrackingInformation information) {
        ArrayList<DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation> targets = information.getMultiTrackingTargetsInformation();
        ArrayList<SubjectSensingState> targetSensingInformations = new ArrayList<>();
        Iterator<DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation> it2 = targets.iterator();
        while (it2.hasNext()) {
            DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation target = it2.next();
            if (DataEyeGetPushTrackStatus.TrackMode.find(target.mode) != DataEyeGetPushTrackStatus.TrackMode.LOST) {
                targetSensingInformations.add(new SubjectSensingState(getTargetStateWithTrackMode(DataEyeGetPushTrackStatus.TrackMode.find(target.mode)), getTargetType(DataEyeGetPushTrackStatus.TargetObjType.find(target.type)), new RectF(target.centerX - (target.width / 2.0f), target.centerY - (target.height / 2.0f), target.centerX + (target.width / 2.0f), target.centerY + (target.height / 2.0f)), target.index));
            }
        }
        int arraySize = targetSensingInformations.size();
        if (arraySize <= 0) {
            return null;
        }
        return new ActiveTrackTrackingState.Builder().autoSensedSubjects((SubjectSensingState[]) targetSensingInformations.toArray(new SubjectSensingState[arraySize])).reason(getReason(DataEyeGetPushMultiTrackingState.getInstance().getTrackingException())).build();
    }

    private ActiveTrackTargetState getTargetState(DataEyeGetPushTrackStatus status) {
        if (activeTrackModeForPackCommandType() == ActiveTrackMode.SPOTLIGHT_HEAT_POINT) {
            return ActiveTrackTargetState.TRACKING_WITH_HIGH_CONFIDENCE;
        }
        return getTargetStateWithTrackMode(status.getRectMode());
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private ActiveTrackTargetState getTargetStateWithTrackMode(DataEyeGetPushTrackStatus.TrackMode trackMode) {
        DataEyeGetPushTrackStatus.TrackException exceptionStatus;
        DataSingleVisualParam.TrackingMode trackingMode;
        switch (trackMode) {
            case NORMAL:
                return ActiveTrackTargetState.TRACKING_WITH_HIGH_CONFIDENCE;
            case WEAK:
            case TRACKING:
                return ActiveTrackTargetState.TRACKING_WITH_LOW_CONFIDENCE;
            case CONFIRM:
                if (isMultiTargetSensingSupported()) {
                    exceptionStatus = DataEyeGetPushMultiTrackingState.getInstance().getTrackingException();
                    trackingMode = DataSingleVisualParam.TrackingMode.find(DataEyeGetPushMultiTrackingState.getInstance().getOverallTrackingMode());
                } else {
                    exceptionStatus = DataEyeGetPushTrackStatus.getInstance().getException();
                    trackingMode = DataEyeGetPushTrackStatus.getInstance().getTrackingMode();
                }
                if (isCriticalException(exceptionStatus)) {
                    return ActiveTrackTargetState.CANNOT_CONFIRM;
                }
                if (trackingMode == DataSingleVisualParam.TrackingMode.WATCH_TARGET || trackingMode == DataSingleVisualParam.TrackingMode.HEAD_LOCK) {
                    return ActiveTrackTargetState.TRACKING_WITH_HIGH_CONFIDENCE;
                }
                return ActiveTrackTargetState.WAITING_FOR_CONFIRMATION;
            case DETECT_AFTER_LOST:
                if (isMultiTargetSensingSupported()) {
                    return ActiveTrackTargetState.TRACKING_WITH_HIGH_CONFIDENCE;
                }
                break;
        }
        return ActiveTrackTargetState.UNKNOWN;
    }

    private ActiveTrackTargetType getTargetType(DataEyeGetPushTrackStatus.TargetObjType targetObjType) {
        switch (targetObjType) {
            case UNKNOWN:
            case ANIMAL:
                return ActiveTrackTargetType.UNKNOWN;
            case PERSON:
                return ActiveTrackTargetType.HUMAN;
            case CAR:
                return ActiveTrackTargetType.CAR;
            case VAN:
                return ActiveTrackTargetType.VAN;
            case BIKE:
                return ActiveTrackTargetType.BIKE;
            case BOAT:
                return ActiveTrackTargetType.BOAT;
            case HOT_POINT:
                return ActiveTrackTargetType.HOT_POINT;
            default:
                return ActiveTrackTargetType.UNKNOWN;
        }
    }

    private ActiveTrackCannotConfirmReason getReason(DataEyeGetPushTrackStatus.TrackException exception) {
        if (activeTrackModeForPackCommandType() == ActiveTrackMode.SPOTLIGHT_HEAT_POINT) {
            return ActiveTrackCannotConfirmReason.NONE;
        }
        switch (exception) {
            case NONE:
            case LOW_DETECT:
            case APP_HALT:
                return ActiveTrackCannotConfirmReason.NONE;
            case TOO_HIGH:
                return ActiveTrackCannotConfirmReason.TARGET_TOO_HIGH;
            case OBSTACLE:
                return ActiveTrackCannotConfirmReason.BLOCKED_BY_OBSTACLE;
            case PITCH_LOW:
                return ActiveTrackCannotConfirmReason.GIMBAL_ATTITUDE_ERROR;
            case TOO_FAR:
                return ActiveTrackCannotConfirmReason.TARGET_TOO_FAR;
            case DRONE_TOO_HIGH:
                return ActiveTrackCannotConfirmReason.TARGET_TOO_HIGH;
            case DRONE_TOO_LOW:
                return ActiveTrackCannotConfirmReason.AIRCRAFT_TOO_LOW;
            case RADAR_FAILED:
                return ActiveTrackCannotConfirmReason.OBSTACLE_SENSOR_ERROR;
            default:
                return ActiveTrackCannotConfirmReason.UNKNOWN;
        }
    }

    private boolean isRunningTrackingMission() {
        return getFSMState().equals(MissionState.CANNOT_CONFIRM) || getFSMState().equals(MissionState.AIRCRAFT_FOLLOWING) || getFSMState().equals(MissionState.WAITING_FOR_CONFIRMATION) || getFSMState().equals(MissionState.ONLY_CAMERA_FOLLOWING) || getFSMState().equals(MissionState.FINDING_TRACKED_TARGET) || getFSMState().equals(MissionState.QUICK_MOVIE) || getFSMState().equals(MissionState.AUTO_SENSING) || getFSMState().equals(MissionState.AUTO_SENSING_FOR_QUICK_SHOT);
    }

    private static boolean isMultiTrackingEnabled() {
        return CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.IS_MULTI_TRACKING_ENABLED));
    }

    private static boolean isMultiQuickShotEnabled() {
        return CacheHelper.toBool(CacheHelper.getFlightAssistant(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED));
    }

    private static boolean hasMultiTrackingStarted() {
        return DataEyeGetPushMultiTrackingState.getInstance().isInTracking() && (isMultiTrackingEnabled() || isMultiQuickShotEnabled());
    }

    private static boolean isMultiTrackWaitingForConfirmation() {
        return DataEyeGetPushMultiTrackingState.getInstance().isInSingleTracking() && (isMultiTrackingEnabled() || isMultiQuickShotEnabled());
    }

    public static boolean isMultiTrackExecuting() {
        return DataEyeGetPushMultiTrackingState.getInstance().isTrackingExecuting() && (isMultiTrackingEnabled() || isMultiQuickShotEnabled());
    }

    public ActiveTrackMode getTrackingMode() {
        if (DataEyeGetPushException.getInstance().isInTracking() || hasMultiTrackingStarted()) {
            return activeTrackModeForPackCommandType();
        }
        return ActiveTrackMode.UNKNOWN;
    }
}
