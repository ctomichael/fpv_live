package dji.internal.mission.fsm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.mission.MissionState;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@EXClassNullAway
public class FiniteStateMachine implements FiniteStateMachineException {
    private MissionState currentState;
    private boolean isLooseModeEnabled = false;
    private Set<MissionState> stateSet = Collections.newSetFromMap(new ConcurrentHashMap());
    private Set<MissionState> superStateSet = Collections.newSetFromMap(new ConcurrentHashMap());
    private List<MissionState> tempFromStateArray = new CopyOnWriteArrayList();
    private Map<MissionState, List<MissionState>> transitionMap = new ConcurrentHashMap();

    @Nullable
    public synchronized MissionState getState() {
        return this.currentState;
    }

    public boolean transitTo(@NonNull MissionState state) {
        return internalTransitTo(state, false);
    }

    public void forceTransitTo(@NonNull MissionState state) {
        internalTransitTo(state, true);
    }

    private synchronized boolean internalTransitTo(@NonNull MissionState state, boolean isForced) {
        boolean z;
        if (!isForced) {
            if (!canTransitTo(state) && !this.isLooseModeEnabled) {
                z = false;
            }
        }
        this.currentState = state;
        z = true;
        return z;
    }

    public synchronized boolean canTransitTo(@NonNull MissionState state) {
        boolean z;
        boolean result = false;
        if (this.currentState != null) {
            if (this.superStateSet.contains(state)) {
                z = true;
            } else if (this.transitionMap.containsKey(this.currentState)) {
                result = this.transitionMap.get(this.currentState).contains(state);
            }
        }
        z = result;
        return z;
    }

    @NonNull
    public FiniteStateMachine add(@NonNull MissionState state) {
        this.stateSet.add(state);
        return this;
    }

    @NonNull
    public FiniteStateMachine superState(@NonNull MissionState... state) {
        for (MissionState eachStateToAdd : state) {
            this.superStateSet.add(eachStateToAdd);
        }
        return this;
    }

    @NonNull
    public FiniteStateMachine from(@NonNull MissionState... fromStates) {
        if (fromStates.length > 0) {
            this.tempFromStateArray.addAll(Arrays.asList(fromStates));
            this.stateSet.addAll(Arrays.asList(fromStates));
        }
        return this;
    }

    @NonNull
    public FiniteStateMachine fromAll() {
        for (MissionState eachState : this.stateSet) {
            this.tempFromStateArray.add(eachState);
        }
        return this;
    }

    @NonNull
    public FiniteStateMachine to(@NonNull MissionState... toStates) {
        if (this.tempFromStateArray == null || toStates.length <= 0) {
            throw new RuntimeException("Null To state or missing From state");
        }
        this.stateSet.addAll(Arrays.asList(toStates));
        for (MissionState eachFromState : this.tempFromStateArray) {
            List<MissionState> toList = this.transitionMap.get(eachFromState);
            if (toList == null) {
                toList = new ArrayList<>();
            }
            toList.addAll(Arrays.asList(toStates));
            this.transitionMap.put(eachFromState, toList);
        }
        this.tempFromStateArray.clear();
        return this;
    }

    public void setLooseModeEnabled(boolean isLooseModeEnabled2) {
        this.isLooseModeEnabled = isLooseModeEnabled2;
    }
}
