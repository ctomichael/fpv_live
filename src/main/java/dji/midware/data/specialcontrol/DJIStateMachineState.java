package dji.midware.data.specialcontrol;

import android.os.Message;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.specialcontrol.DJIStateMachineDecorator;
import dji.tools.sm.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EXClassNullAway
public class DJIStateMachineState extends State {
    private final String TAG = "DJIStateMachineState";
    private List<DJIStateMachineDecorator.Callback> callbacks = new ArrayList();
    private DJIStateMachineDecorator mStateMachine;
    private Map<Integer, State> mStateTransmMap = new HashMap();

    public DJIStateMachineState(DJIStateMachineDecorator machine) {
        this.mStateMachine = machine;
    }

    public void addStateTransmission(int msgWhat, State _state) {
        this.mStateTransmMap.put(Integer.valueOf(msgWhat), _state);
    }

    public void enter() {
        super.enter();
        if (this.callbacks != null) {
            for (DJIStateMachineDecorator.Callback cb : this.callbacks) {
                cb.onStateChange(this);
            }
        }
    }

    public void exit() {
        super.exit();
    }

    public boolean processMessage(Message msg) {
        if (this.mStateMachine == null) {
            return super.processMessage(msg);
        }
        State toState = this.mStateTransmMap.get(Integer.valueOf(msg.what));
        if (toState == null) {
            return super.processMessage(msg);
        }
        this.mStateMachine.transitionDecorator(toState);
        return true;
    }
}
