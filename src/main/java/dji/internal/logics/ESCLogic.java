package dji.internal.logics;

import dji.common.bus.LogicEventBus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.LogicManager;
import dji.internal.logics.Message;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.sdk.SDKUtils;
import dji.sdkcache.R;
import dji.thirdparty.rx.schedulers.Schedulers;

@EXClassNullAway
public class ESCLogic {
    private static final String BLOCK = "ESC block";
    private static final String ESCM_ERROR = "ESC module error";
    private static final String ESC_DISCONNECT = "ESC disconnect";
    private static final String NON_BALANCE = "ESC is not balance";
    private static final String OTHER = "ESC unknown error";
    private static final String PROPELLER_OFF = "Propeller ejection";
    private static final String RESISTANCE_ERROR = "ESC resistance error";
    private static final String SIGNAL_ERROR = "ESC signal error";
    private volatile Message previousMessage;

    public void init() {
        this.previousMessage = null;
    }

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static ESCLogic instance = new ESCLogic();

        private HOLDER() {
        }
    }

    private ESCLogic() {
        LogicManager.getInstance().addSubscription(LogicEventBus.getInstance().register(LogicManager.SensorShouldUpdateEvent.class, DataOsdGetPushHome.class).subscribeOn(Schedulers.computation()).subscribe(new ESCLogic$$Lambda$0(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$ESCLogic(Object newPushData) {
        updateESCStatusLowFrequency();
    }

    private String motorStateHasProblem(DataOsdGetPushHome.MotorEscmState state) {
        if (state == null || state == DataOsdGetPushHome.MotorEscmState.NON_CONNECT || state == DataOsdGetPushHome.MotorEscmState.MOTOR_OFF || state == DataOsdGetPushHome.MotorEscmState.MOTOR_UP || state == DataOsdGetPushHome.MotorEscmState.NON_SMART || state == DataOsdGetPushHome.MotorEscmState.MOTOR_IDLE) {
            return null;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.DISCONNECT) {
            return ESC_DISCONNECT;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.SIGNAL_ERROR) {
            return SIGNAL_ERROR;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.RESISTANCE_ERROR) {
            return RESISTANCE_ERROR;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.BLOCK) {
            return BLOCK;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.NON_BALANCE) {
            return NON_BALANCE;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.ESCM_ERROR) {
            return ESCM_ERROR;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.PROPELLER_OFF) {
            return PROPELLER_OFF;
        }
        if (state == DataOsdGetPushHome.MotorEscmState.OTHER) {
            return OTHER;
        }
        return state.name();
    }

    private String hasMotorEscmError(DataOsdGetPushHome home) {
        DataOsdGetPushHome.MotorEscmState[] allState;
        if (home == null) {
            return null;
        }
        for (DataOsdGetPushHome.MotorEscmState state : home.getMotorEscmState()) {
            String error = motorStateHasProblem(state);
            if (error != null) {
                return error;
            }
        }
        return null;
    }

    private void updateESCStatusLowFrequency() {
        if (DataOsdGetPushCommon.getInstance().getFlycVersion() >= 15) {
            Message.Type level = Message.Type.GOOD;
            String title = SDKUtils.getMidwareString(R.string.dji_logic_normal);
            String description = hasMotorEscmError(DataOsdGetPushHome.getInstance());
            if (description != null) {
                level = Message.Type.ERROR;
                title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
            }
            updateEventMessage(level, title, description);
        }
    }

    private void updateEventMessage(Message.Type level, String title, String description) {
        if (this.previousMessage == null || !this.previousMessage.equals(level, title, description)) {
            Message newMessage = new Message(level, title, description);
            this.previousMessage = newMessage;
            LogicEventBus.getInstance().post(new ESCEvent(newMessage));
            this.previousMessage = newMessage;
        }
    }

    public static ESCLogic getInstance() {
        return HOLDER.instance;
    }

    public static final class ESCEvent {
        private Message message;

        public ESCEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }
}
