package dji.midware.data.specialcontrol;

import android.os.Looper;
import dji.log.DJILog;
import io.reactivex.disposables.Disposable;

public class OldSpecialControlStateMachine extends DJIStateMachineDecorator {
    private static int INITIAL_INTEGER = 0;
    public static final int MSG_RECV_ACK;
    public static final int MSG_START = INITIAL_INTEGER;
    public static final int MSG_TIMOUT;
    public static final String TAG_OLD_SPECIAL_CTRL = "OldSpecialControl";
    /* access modifiers changed from: private */
    public SpecialControlRisingTrigger mRisingTrigger;

    static {
        INITIAL_INTEGER = 0;
        int i = INITIAL_INTEGER + 1;
        INITIAL_INTEGER = i;
        MSG_RECV_ACK = i;
        int i2 = INITIAL_INTEGER + 1;
        INITIAL_INTEGER = i2;
        MSG_TIMOUT = i2;
    }

    public OldSpecialControlStateMachine(String name, Looper looper, SpecialControlRisingTrigger risingTrigger) {
        super(name, looper);
        this.mRisingTrigger = risingTrigger;
        DJIStateMachineState stateIdle = new StateIdle(this);
        DJIStateMachineState stateHighLevel = new StateHighLevelSending(this);
        DJIStateMachineState stateLowLevel = new StateLowLevelSending(this);
        DJIStateMachineState stateTimeout = new StateTimeOut(this);
        DJIStateMachineState stateLowLevelResend = new StateLowLevelResend(this);
        addState(stateIdle);
        setInitialState(stateIdle);
        addState(stateHighLevel);
        addState(stateLowLevel);
        addState(stateTimeout);
        addState(stateLowLevelResend);
        stateIdle.addStateTransmission(MSG_START, stateHighLevel);
        stateHighLevel.addStateTransmission(MSG_RECV_ACK, stateLowLevel);
        stateHighLevel.addStateTransmission(MSG_TIMOUT, stateIdle);
        stateLowLevel.addStateTransmission(MSG_RECV_ACK, stateIdle);
        stateLowLevel.addStateTransmission(MSG_TIMOUT, stateTimeout);
        stateTimeout.addStateTransmission(MSG_START, stateLowLevelResend);
        stateLowLevelResend.addStateTransmission(MSG_TIMOUT, stateTimeout);
        stateLowLevelResend.addStateTransmission(MSG_RECV_ACK, stateHighLevel);
    }

    class StateIdle extends DJIStateMachineState {
        public StateIdle(DJIStateMachineDecorator machine) {
            super(machine);
        }

        public void enter() {
            OldSpecialControlStateMachine.this.logSave("enter stateIdle");
            super.enter();
        }

        public void exit() {
            OldSpecialControlStateMachine.this.logSave("--exit stateIdle");
            super.exit();
        }
    }

    class StateHighLevelSending extends DJIStateMachineState {
        Disposable mDisposable;

        public StateHighLevelSending(DJIStateMachineDecorator machine) {
            super(machine);
        }

        public void enter() {
            super.enter();
            OldSpecialControlStateMachine.this.logSave("enter StateHighLevelSending");
            this.mDisposable = OldSpecialControlStateMachine.this.mRisingTrigger.sendHighLevel();
        }

        public void exit() {
            OldSpecialControlStateMachine.this.logSave("--exit StateHighLevelSending");
            if (this.mDisposable != null) {
                this.mDisposable.dispose();
            }
            super.exit();
        }
    }

    class StateLowLevelSending extends DJIStateMachineState {
        Disposable mDisposable;

        public StateLowLevelSending(DJIStateMachineDecorator machine) {
            super(machine);
        }

        public void enter() {
            super.enter();
            OldSpecialControlStateMachine.this.logSave("enter StateLowLevelSending");
            this.mDisposable = OldSpecialControlStateMachine.this.mRisingTrigger.sendLowLevel();
        }

        public void exit() {
            OldSpecialControlStateMachine.this.logSave("--exit StateLowLevelSending");
            if (this.mDisposable != null) {
                this.mDisposable.dispose();
            }
            super.exit();
        }
    }

    class StateTimeOut extends DJIStateMachineState {
        public StateTimeOut(DJIStateMachineDecorator machine) {
            super(machine);
        }

        public void enter() {
            super.enter();
            OldSpecialControlStateMachine.this.logSave("enter StateTimeOut");
        }

        public void exit() {
            OldSpecialControlStateMachine.this.logSave("--exit StateTimeOut");
            super.exit();
        }
    }

    class StateLowLevelResend extends DJIStateMachineState {
        Disposable mDisposable;

        public StateLowLevelResend(DJIStateMachineDecorator machine) {
            super(machine);
        }

        public void enter() {
            super.enter();
            OldSpecialControlStateMachine.this.logSave("enter StateLowLevelResend");
            this.mDisposable = OldSpecialControlStateMachine.this.mRisingTrigger.sendLowLevel();
        }

        public void exit() {
            OldSpecialControlStateMachine.this.logSave("--exit StateLowLevelResend");
            if (this.mDisposable != null) {
                this.mDisposable.dispose();
            }
            super.exit();
        }
    }

    /* access modifiers changed from: private */
    public void logSave(String content) {
        DJILog.logWriteI(TAG_OLD_SPECIAL_CTRL, content, TAG_OLD_SPECIAL_CTRL, new Object[0]);
    }
}
