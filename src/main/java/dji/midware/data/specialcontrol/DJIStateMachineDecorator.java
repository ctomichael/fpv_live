package dji.midware.data.specialcontrol;

import android.os.Looper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.tools.sm.IState;
import dji.tools.sm.StateMachine;
import java.util.ArrayList;
import java.util.List;

@EXClassNullAway
public class DJIStateMachineDecorator extends StateMachine {
    protected List<Callback> mCallbackList = new ArrayList();

    public interface Callback {
        void onStateChange(IState iState);
    }

    public void addCallback(Callback callback) {
        this.mCallbackList.add(callback);
    }

    public void removeCallback(Callback callback) {
        this.mCallbackList.remove(callback);
    }

    protected DJIStateMachineDecorator(String name) {
        super(name);
    }

    protected DJIStateMachineDecorator(String name, Looper looper) {
        super(name, looper);
    }

    public void transitionDecorator(IState destState) {
        transitionTo(destState);
    }
}
