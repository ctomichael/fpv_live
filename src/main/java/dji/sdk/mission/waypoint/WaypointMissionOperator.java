package dji.sdk.mission.waypoint;

import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.bus.MissionEventBus;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.mission.MissionEvent;
import dji.common.mission.StateHelper;
import dji.common.mission.waypoint.WaypointDownloadProgress;
import dji.common.mission.waypoint.WaypointEvent;
import dji.common.mission.waypoint.WaypointExecutionProgress;
import dji.common.mission.waypoint.WaypointMission;
import dji.common.mission.waypoint.WaypointMissionDownloadEvent;
import dji.common.mission.waypoint.WaypointMissionExecutionEvent;
import dji.common.mission.waypoint.WaypointMissionInterruption;
import dji.common.mission.waypoint.WaypointMissionState;
import dji.common.mission.waypoint.WaypointMissionUploadEvent;
import dji.common.mission.waypoint.WaypointUploadProgress;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.internal.mission.abstraction.MultiInterestPointsMissionBuilder;
import dji.internal.mission.abstraction.waypoint.WaypointAbstractionDataHolder;
import dji.internal.mission.abstraction.waypoint.WaypointMissionAbstraction;
import dji.log.DJILog;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WaypointMissionOperator {
    /* access modifiers changed from: private */
    public WaypointMissionAbstraction abstraction = new WaypointMissionAbstraction();
    private final Map<WaypointMissionOperatorListener, Subscription> listenerToSubscriptionMap = new ConcurrentHashMap();
    /* access modifiers changed from: private */
    public WaypointMission.Builder mutableOperatingMission;

    public void destroy() {
        this.abstraction.destroy();
        this.mutableOperatingMission = null;
        for (Subscription eachSubscription : this.listenerToSubscriptionMap.values()) {
            if (eachSubscription != null && !eachSubscription.isUnsubscribed()) {
                eachSubscription.unsubscribe();
            }
        }
        this.listenerToSubscriptionMap.clear();
    }

    public void clearMission() {
        this.mutableOperatingMission = null;
    }

    public void addListener(@NonNull final WaypointMissionOperatorListener listener) {
        if (listener != null) {
            this.listenerToSubscriptionMap.put(listener, MissionEventBus.getInstance().serializedRegister(WaypointAbstractionDataHolder.class).observeOn(Schedulers.computation()).subscribe(new Action1<WaypointAbstractionDataHolder>() {
                /* class dji.sdk.mission.waypoint.WaypointMissionOperator.AnonymousClass1 */

                public void call(WaypointAbstractionDataHolder waypointAbstractionDataHolder) {
                    WaypointMissionOperator.this.notifyListener(listener, waypointAbstractionDataHolder);
                }
            }));
        }
    }

    /* access modifiers changed from: private */
    public void notifyListener(@NonNull WaypointMissionOperatorListener listener, @NonNull WaypointAbstractionDataHolder dataHolder) {
        WaypointMissionState previousState = StateHelper.convertToWaypointPublicState(dataHolder.getPreviousState());
        WaypointMissionState currentState = StateHelper.convertToWaypointPublicState(dataHolder.getCurrentState());
        Object extra = dataHolder.getExtra();
        DJIError error = dataHolder.getError();
        MissionEvent event = dataHolder.getEvent();
        if (event != null && (event.equals(MissionEvent.EXECUTION_FINISHED) || event.equals(MissionEvent.EXECUTION_STOPPED))) {
            listener.onExecutionFinish(error);
        } else if (event != null && event.equals(MissionEvent.EXECUTION_STARTED)) {
            listener.onExecutionStart();
        } else if (event != null && event.equals(WaypointEvent.MISSION_RELOADED)) {
            WaypointMissionUploadEvent.Builder uploadEvent = new WaypointMissionUploadEvent.Builder();
            uploadEvent.currentState(currentState);
            uploadEvent.previousState(previousState);
            uploadEvent.error(error);
            listener.onUploadUpdate(uploadEvent.build());
        } else if (extra == null) {
            WaypointMissionUploadEvent.Builder uploadEvent2 = new WaypointMissionUploadEvent.Builder();
            if (currentState == WaypointMissionState.READY_TO_EXECUTE && previousState == WaypointMissionState.UPLOADING) {
                uploadEvent2.currentState(currentState);
                uploadEvent2.previousState(previousState);
                uploadEvent2.error(error);
                listener.onUploadUpdate(uploadEvent2.build());
            }
        } else if (extra instanceof WaypointUploadProgress) {
            WaypointMissionUploadEvent.Builder uploadEvent3 = new WaypointMissionUploadEvent.Builder();
            uploadEvent3.previousState(previousState);
            if (currentState != WaypointMissionState.UNKNOWN) {
                uploadEvent3.currentState(currentState);
                uploadEvent3.progress((WaypointUploadProgress) extra);
                uploadEvent3.error(error);
                listener.onUploadUpdate(uploadEvent3.build());
            }
        } else if (extra instanceof WaypointDownloadProgress) {
            WaypointMissionDownloadEvent.Builder downloadEvent = new WaypointMissionDownloadEvent.Builder();
            downloadEvent.progress((WaypointDownloadProgress) extra);
            downloadEvent.error(error);
            listener.onDownloadUpdate(downloadEvent.build());
        } else if ((extra instanceof WaypointExecutionProgress) || (event != null && event.equals(MissionEvent.EXECUTION_PROGRESS_UPDATE))) {
            WaypointMissionExecutionEvent.Builder executionEvent = new WaypointMissionExecutionEvent.Builder();
            executionEvent.previousState(previousState);
            if (currentState != WaypointMissionState.UNKNOWN) {
                executionEvent.currentState(currentState);
                if (extra instanceof WaypointExecutionProgress) {
                    executionEvent.progress((WaypointExecutionProgress) extra);
                }
                executionEvent.error(error);
                listener.onExecutionUpdate(executionEvent.build());
            }
        }
    }

    public void removeListener(@NonNull WaypointMissionOperatorListener listener) {
        Subscription existingSubscription;
        if (listener != null && (existingSubscription = this.listenerToSubscriptionMap.remove(listener)) != null && !existingSubscription.isUnsubscribed()) {
            existingSubscription.unsubscribe();
        }
    }

    @Nullable
    public DJIError loadMission(@NonNull WaypointMission mission) {
        if (mission == null) {
            return DJIMissionError.NULL_MISSION;
        }
        this.mutableOperatingMission = new WaypointMission.Builder(mission);
        DJIError error = this.abstraction.loadMission(this.mutableOperatingMission);
        if (error == null) {
            return error;
        }
        this.mutableOperatingMission = null;
        return error;
    }

    public void uploadMission(@Nullable final CommonCallbacks.CompletionCallback callback) {
        if (this.mutableOperatingMission == null) {
            CallbackUtils.onFailure(callback, DJIMissionError.NULL_MISSION);
        } else if (!this.mutableOperatingMission.isMissionComplete()) {
            CallbackUtils.onFailure(callback, DJIMissionError.INCOMPLETE_MISSION);
        } else {
            DJIError error = checkPrerequisite();
            if (error != null) {
                CallbackUtils.onFailure(callback, error);
            } else if (this.abstraction.getFSMState().equals(WaypointMissionState.NOT_READY)) {
                this.abstraction.setupEnvironment(new CommonCallbacks.CompletionCallback() {
                    /* class dji.sdk.mission.waypoint.WaypointMissionOperator.AnonymousClass2 */

                    public void onResult(DJIError error) {
                        if (error != null) {
                            CallbackUtils.onFailure(callback, error);
                        } else {
                            WaypointMissionOperator.this.abstraction.upload(WaypointMissionOperator.this.mutableOperatingMission, callback);
                        }
                    }
                });
            } else if (this.abstraction.getFSMState().equals(WaypointMissionState.READY_TO_UPLOAD) || this.abstraction.getFSMState().equals(WaypointMissionState.READY_TO_EXECUTE) || this.abstraction.getFSMState().equals(WaypointMissionState.READY_TO_RETRY_UPLOAD)) {
                this.abstraction.upload(this.mutableOperatingMission, callback);
            } else {
                CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
                DJILog.d("HAIHAI", "NOT READY FOR UPLOAD" + this.abstraction.getFSMState().getName(), new Object[0]);
            }
        }
    }

    public void downloadMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (this.mutableOperatingMission == null) {
            this.mutableOperatingMission = new MultiInterestPointsMissionBuilder();
        }
        this.abstraction.downloadMission(this.mutableOperatingMission, callback);
    }

    public void retryUploadMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (this.mutableOperatingMission == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            this.abstraction.retryUpload(callback);
        }
    }

    public void cancelUploadingMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (this.mutableOperatingMission == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
        } else {
            this.abstraction.cancelUploadingMission(callback);
        }
    }

    public void startMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (this.mutableOperatingMission == null || !this.mutableOperatingMission.isMissionComplete()) {
            CallbackUtils.onFailure(callback, DJIMissionError.MISSION_INFO_INVALID);
        } else {
            this.abstraction.startMission(callback);
        }
    }

    public void pauseMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        this.abstraction.pauseMission(callback);
    }

    public void resumeMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        this.abstraction.resumeMission(callback);
    }

    public void stopMission(@Nullable CommonCallbacks.CompletionCallback callback) {
        if (this.abstraction.getFSMState().equals(WaypointMissionState.EXECUTING) || this.abstraction.getFSMState().equals(WaypointMissionState.EXECUTION_PAUSED)) {
            this.abstraction.stopMission(callback);
        } else {
            CallbackUtils.onFailure(callback, DJIMissionError.COMMON_STATE_ERROR);
        }
    }

    @NonNull
    public WaypointMissionState getCurrentState() {
        return StateHelper.convertToWaypointPublicState(this.abstraction.getFSMState());
    }

    public void setAutoFlightSpeed(@FloatRange(from = -15.0d, to = 15.0d) float speed, @Nullable CommonCallbacks.CompletionCallback callback) {
        this.abstraction.setAutoFlightSpeed(speed, callback);
    }

    @Nullable
    public WaypointMission getLoadedMission() {
        WaypointMission.Builder builder = getLoadedMissionBuilder();
        if (builder == null) {
            return null;
        }
        return builder.build();
    }

    public void getPreviousInterruption(CommonCallbacks.CompletionCallbackWith<WaypointMissionInterruption> callback) {
        this.abstraction.getPreviousInterruption(callback);
    }

    private DJIError checkPrerequisite() {
        return null;
    }

    public WaypointMission.Builder getLoadedMissionBuilder() {
        if (this.mutableOperatingMission == null) {
            if (this.abstraction.getCacheData() == null) {
                return null;
            }
            this.mutableOperatingMission = this.abstraction.getCacheData().missionBuilder;
            if (this.mutableOperatingMission == null) {
                return null;
            }
        }
        return this.mutableOperatingMission;
    }
}
