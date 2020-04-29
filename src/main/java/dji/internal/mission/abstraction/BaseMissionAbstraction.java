package dji.internal.mission.abstraction;

import android.os.Handler;
import android.support.annotation.NonNull;
import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.mission.MissionEvent;
import dji.common.mission.MissionState;
import dji.common.util.CallbackUtils;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mission.MissionResultPicker;
import dji.internal.mission.abstraction.AbstractionDataHolder;
import dji.internal.mission.fsm.FSMTempStateTimer;
import dji.internal.mission.fsm.FiniteStateMachine;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.listener.DJISetCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@EXClassNullAway
public abstract class BaseMissionAbstraction {
    protected static final double DEFAULT_TIMEOUT_FOR_OPERATION = 0.5d;
    private static final String NULL_FSM_EXCEPTION = "FSM is null!";
    private static final String NULL_TIMER_EXCEPTION = "Temp state timer is null!";
    protected final long DEFAULT_TIMEOUT_FOR_NAVIGATION = 3000;
    protected List<MissionState> filteringStates = new CopyOnWriteArrayList();
    protected FiniteStateMachine fsm = buildFSM();
    protected Handler handler = new Handler(BackgroundLooper.getLooper());
    protected AbstractionDataHolder previousDataHolder = new AbstractionDataHolder.Builder().currentState(getFSMState()).build();
    protected CommonCallbacks.CompletionCallback startListeningCompletionCallback;
    protected Runnable startingNavigationModeListerningRunnable = new Runnable() {
        /* class dji.internal.mission.abstraction.BaseMissionAbstraction.AnonymousClass3 */

        public void run() {
            CallbackUtils.onFailure(BaseMissionAbstraction.this.resetListeningCompletionCallback(), DJIError.COMMON_TIMEOUT);
        }
    };
    /* access modifiers changed from: protected */
    public FSMTempStateTimer tempStateTimer = new FSMTempStateTimer();

    /* access modifiers changed from: protected */
    @NonNull
    public abstract FiniteStateMachine buildFSM();

    /* access modifiers changed from: protected */
    public abstract void notifyListener(AbstractionDataHolder abstractionDataHolder);

    /* access modifiers changed from: protected */
    public abstract boolean transitToState(@NonNull MissionState missionState, MissionEvent missionEvent);

    public void destroy() {
        this.tempStateTimer = null;
        this.filteringStates = null;
        this.previousDataHolder = null;
    }

    /* access modifiers changed from: protected */
    public void notifyListener(MissionEvent event) {
        AbstractionDataHolder.Builder builder = new AbstractionDataHolder.Builder(event);
        builder.currentState(getFSMState());
        notifyListener(builder.build());
    }

    @NonNull
    public MissionState getFSMState() {
        if (this.fsm == null) {
            throw new RuntimeException(NULL_FSM_EXCEPTION);
        } else if (this.fsm.getState() == null) {
            return MissionState.UNKNOWN;
        } else {
            return this.fsm.getState();
        }
    }

    /* access modifiers changed from: protected */
    public boolean transitToState(@NonNull MissionState state, @NonNull AbstractionDataHolder.Builder holder) {
        return internalTransitToState(state, holder, false);
    }

    /* access modifiers changed from: protected */
    public void forceTransitToState(@NonNull MissionState state, @NonNull AbstractionDataHolder.Builder holder) {
        internalTransitToState(state, holder, true);
    }

    /* access modifiers changed from: protected */
    public void forceTransitToState(@NonNull MissionState state, MissionEvent event) {
        internalTransitToState(state, new AbstractionDataHolder.Builder(event), true);
    }

    /* access modifiers changed from: protected */
    public boolean tryTransitToTempState(@NonNull MissionState state, AbstractionDataHolder.Builder builder) {
        if (!this.fsm.canTransitTo(state)) {
            return false;
        }
        MissionState previousState = this.fsm.getState();
        if (!transitToState(state, builder)) {
            return false;
        }
        setFilteringStates(previousState);
        return true;
    }

    private void setFilteringStates(MissionState... filteringStates2) {
        this.filteringStates.clear();
        for (MissionState missionState : filteringStates2) {
            this.filteringStates.add(missionState);
        }
    }

    /* access modifiers changed from: private */
    public void cleanAllFilteringStates() {
        this.filteringStates.clear();
    }

    /* access modifiers changed from: protected */
    public DJIDataCallBack getDataCallbackForTempState(MissionResultPicker picker, MissionState tempState, ArrayList<MissionState> desireStates, MissionState failureState, AbstractionDataHolder.Builder builder, CommonCallbacks.CompletionCallback callback) {
        return getDataCallbackForTempState(picker, tempState, desireStates, DEFAULT_TIMEOUT_FOR_OPERATION, failureState, builder, callback);
    }

    /* access modifiers changed from: protected */
    public DJIDataCallBack getDataCallbackForTempState(MissionResultPicker picker, MissionState tempState, ArrayList<MissionState> desireStates, double timeoutInSeconds, MissionState failureState, AbstractionDataHolder.Builder builder, CommonCallbacks.CompletionCallback callback) {
        final MissionResultPicker missionResultPicker = picker;
        final MissionState missionState = tempState;
        final double d = timeoutInSeconds;
        final MissionState missionState2 = failureState;
        final AbstractionDataHolder.Builder builder2 = builder;
        final ArrayList<MissionState> arrayList = desireStates;
        final CommonCallbacks.CompletionCallback completionCallback = callback;
        return new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.BaseMissionAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (missionResultPicker.getMissionActionResult() == 0) {
                    if (BaseMissionAbstraction.this.fsm.getState() == missionState) {
                        BaseMissionAbstraction.this.tempStateTimer.startTimer(missionState, false, new BaseMissionAbstraction$1$$Lambda$0(this), d, new BaseMissionAbstraction$1$$Lambda$1(this, missionState2, builder2));
                    } else if (arrayList.contains(BaseMissionAbstraction.this.fsm.getState())) {
                        BaseMissionAbstraction.this.cleanAllFilteringStates();
                    } else {
                        BaseMissionAbstraction.this.failureOperation(missionState2, builder2);
                    }
                    CallbackUtils.onSuccess(completionCallback);
                    return;
                }
                DJIError error = DJIMissionError.getDJIErrorByCode(missionResultPicker.getMissionActionResult());
                BaseMissionAbstraction.this.failureOperation(missionState2, builder2);
                CallbackUtils.onFailure(completionCallback, error);
            }

            /* access modifiers changed from: package-private */
            public final /* synthetic */ void lambda$onSuccess$0$BaseMissionAbstraction$1() {
                BaseMissionAbstraction.this.cleanAllFilteringStates();
            }

            /* access modifiers changed from: package-private */
            public final /* synthetic */ void lambda$onSuccess$1$BaseMissionAbstraction$1(MissionState failureState, AbstractionDataHolder.Builder builder) {
                BaseMissionAbstraction.this.failureOperation(failureState, builder.error(DJIError.COMMON_TIMEOUT));
            }

            public void onFailure(Ccode ccode) {
                if (ccode != Ccode.TIMEOUT) {
                    BaseMissionAbstraction.this.failureOperation(missionState2, builder2);
                    CallbackUtils.onFailure(completionCallback, ccode);
                } else if (BaseMissionAbstraction.this.fsm.getState() == missionState) {
                    BaseMissionAbstraction.this.tempStateTimer.startTimer(missionState, false, new BaseMissionAbstraction$1$$Lambda$2(this), d, new BaseMissionAbstraction$1$$Lambda$3(this, missionState2, builder2));
                    CallbackUtils.onFailure(completionCallback, DJIError.COMMON_TIMEOUT);
                } else if (arrayList.contains(BaseMissionAbstraction.this.fsm.getState())) {
                    BaseMissionAbstraction.this.cleanAllFilteringStates();
                    CallbackUtils.onSuccess(completionCallback);
                } else {
                    BaseMissionAbstraction.this.failureOperation(missionState2, builder2);
                    CallbackUtils.onFailure(completionCallback, DJIError.COMMON_TIMEOUT);
                }
            }

            /* access modifiers changed from: package-private */
            public final /* synthetic */ void lambda$onFailure$2$BaseMissionAbstraction$1() {
                BaseMissionAbstraction.this.cleanAllFilteringStates();
            }

            /* access modifiers changed from: package-private */
            public final /* synthetic */ void lambda$onFailure$3$BaseMissionAbstraction$1(MissionState failureState, AbstractionDataHolder.Builder builder) {
                BaseMissionAbstraction.this.failureOperation(failureState, builder.error(DJIError.COMMON_TIMEOUT));
            }
        };
    }

    /* access modifiers changed from: private */
    public void failureOperation(MissionState failureState, AbstractionDataHolder.Builder builder) {
        cleanAllFilteringStates();
        transitToState(failureState, builder);
    }

    private boolean internalTransitToState(@NonNull MissionState state, @NonNull AbstractionDataHolder.Builder holder, boolean isForced) {
        boolean successful;
        if (this.fsm == null) {
            throw new RuntimeException(NULL_FSM_EXCEPTION);
        } else if (this.tempStateTimer == null) {
            throw new RuntimeException(NULL_TIMER_EXCEPTION);
        } else if (this.filteringStates != null && this.filteringStates.contains(state)) {
            return false;
        } else {
            MissionState previousState = getFSMState();
            if (isForced) {
                this.fsm.forceTransitTo(state);
                successful = true;
            } else {
                successful = this.fsm.transitTo(state);
            }
            MissionState currentState = getFSMState();
            if (currentState != previousState) {
                this.tempStateTimer.notifyStateChange(currentState);
            }
            holder.currentState(currentState);
            holder.previousState(previousState);
            notifyListener(holder.build());
            return successful;
        }
    }

    public void setupEnvironment(final CommonCallbacks.CompletionCallback callback) {
        if ((!CacheHelper.toBool(CacheHelper.getFlightController(FlightControllerKeys.NAVIGATION_MODE_ENABLED)) || getFSMState().equals(MissionState.NOT_READY)) && this.startListeningCompletionCallback == null) {
            this.startListeningCompletionCallback = callback;
            setupStartListerningTimer();
            DJISDKCache.getInstance().setValue(KeyHelper.getFlightControllerKey(FlightControllerKeys.NAVIGATION_MODE_ENABLED), true, new DJISetCallback() {
                /* class dji.internal.mission.abstraction.BaseMissionAbstraction.AnonymousClass2 */

                public void onSuccess() {
                }

                public void onFails(DJIError error) {
                    if (BaseMissionAbstraction.this.resetListeningCompletionCallback() != null) {
                        BaseMissionAbstraction.this.handler.removeCallbacks(BaseMissionAbstraction.this.startingNavigationModeListerningRunnable);
                    }
                    CallbackUtils.onFailure(callback, error);
                }
            });
            return;
        }
        CallbackUtils.onSuccess(callback);
    }

    private void setupStartListerningTimer() {
        this.handler.postDelayed(this.startingNavigationModeListerningRunnable, 3000);
    }

    /* access modifiers changed from: protected */
    public synchronized CommonCallbacks.CompletionCallback resetListeningCompletionCallback() {
        CommonCallbacks.CompletionCallback callback = null;
        synchronized (this) {
            if (this.startListeningCompletionCallback != null) {
                callback = this.startListeningCompletionCallback;
                this.startListeningCompletionCallback = null;
            }
        }
        return callback;
    }

    @NonNull
    public DJIDataCallBack generateCommonCallBackForOperationWithCurrentState(int value, MissionState currentState, @NonNull MissionState failureState, MissionEvent event, CommonCallbacks.CompletionCallback callback) {
        final AbstractionDataHolder.Builder abstractionDataHolder = new AbstractionDataHolder.Builder(event);
        abstractionDataHolder.previousState(currentState);
        final int i = value;
        final CommonCallbacks.CompletionCallback completionCallback = callback;
        final MissionState missionState = failureState;
        final MissionState missionState2 = currentState;
        return new DJIDataCallBack() {
            /* class dji.internal.mission.abstraction.BaseMissionAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                if (i == 0) {
                    CallbackUtils.onSuccess(completionCallback);
                    return;
                }
                BaseMissionAbstraction.this.transitToState(missionState, abstractionDataHolder);
                CallbackUtils.onFailure(completionCallback, DJIMissionError.getDJIErrorByCode(i));
            }

            public void onFailure(@NonNull Ccode ccode) {
                if (DJIError.getDJIError(ccode) == DJIError.COMMON_TIMEOUT) {
                    BaseMissionAbstraction.this.tempStateTimer.startTimer(missionState2, false, BaseMissionAbstraction.DEFAULT_TIMEOUT_FOR_OPERATION, new BaseMissionAbstraction$4$$Lambda$0(this, missionState2, missionState, abstractionDataHolder));
                } else {
                    BaseMissionAbstraction.this.transitToState(missionState, abstractionDataHolder);
                }
                CallbackUtils.onFailure(completionCallback, ccode);
            }

            /* access modifiers changed from: package-private */
            public final /* synthetic */ void lambda$onFailure$0$BaseMissionAbstraction$4(MissionState currentState, @NonNull MissionState failureState, AbstractionDataHolder.Builder abstractionDataHolder) {
                if (currentState == BaseMissionAbstraction.this.getFSMState()) {
                    BaseMissionAbstraction.this.transitToState(failureState, abstractionDataHolder);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public boolean isOsmo() {
        return CacheHelper.toBool(CacheHelper.getProduct(ProductKeys.IS_OSMO));
    }

    /* access modifiers changed from: protected */
    public boolean doesProductSupportNavigationMode() {
        DataOsdGetPushCommon.DroneType droneType = DataOsdGetPushCommon.getInstance().getDroneType();
        if (droneType == DataOsdGetPushCommon.DroneType.Unknown || droneType == DataOsdGetPushCommon.DroneType.ACEONE || droneType == DataOsdGetPushCommon.DroneType.WKM || droneType == DataOsdGetPushCommon.DroneType.NAZA || droneType == DataOsdGetPushCommon.DroneType.NoFlyc) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public ArrayList<MissionState> desiredMissionStatesHelper(MissionState... missionStates) {
        ArrayList<MissionState> desiredMissionStates = new ArrayList<>();
        for (MissionState missionState : missionStates) {
            desiredMissionStates.add(missionState);
        }
        return desiredMissionStates;
    }
}
