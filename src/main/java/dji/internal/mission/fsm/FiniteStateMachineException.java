package dji.internal.mission.fsm;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface FiniteStateMachineException {
    public static final RuntimeException NOT_REACHABLE_STATE_EXCEPTION = new RuntimeException(new String("Could not transit state from %s to %s"));
}
