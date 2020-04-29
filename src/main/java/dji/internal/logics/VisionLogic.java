package dji.internal.logics;

import dji.common.bus.LogicEventBus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.LogicManager;
import dji.internal.logics.Message;
import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.midware.sdk.SDKUtils;
import dji.sdkcache.R;
import dji.thirdparty.rx.schedulers.Schedulers;

@EXClassNullAway
public class VisionLogic {
    private volatile Message previousMessage;

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static VisionLogic instance = new VisionLogic();

        private HOLDER() {
        }
    }

    private VisionLogic() {
        this.previousMessage = null;
        LogicManager.getInstance().addSubscription(LogicEventBus.getInstance().register(LogicManager.SensorShouldUpdateEvent.class, Data2100GetPushCheckStatus.class).subscribeOn(Schedulers.computation()).subscribe(new VisionLogic$$Lambda$0(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$VisionLogic(Object newPushData) {
        updateVisionStatus();
    }

    private void updateVisionStatus() {
        Message.Type level = Message.Type.GOOD;
        String title = SDKUtils.getMidwareString(R.string.dji_logic_normal);
        String description = null;
        Data2100GetPushCheckStatus status = Data2100GetPushCheckStatus.getInstance();
        if (status.hasVisionError()) {
            level = Message.Type.ERROR;
            title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
            description = SDKUtils.getMidwareString(R.string.fpv_tip_vision_error);
        }
        if (status.isBackSightDemarkAbnormal()) {
            level = Message.Type.ERROR;
            title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
            description = SDKUtils.getMidwareString(R.string.fpv_tip_back_vision_cali);
        }
        if (status.isDownSightDemarkAbnormal()) {
            level = Message.Type.ERROR;
            title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
            description = SDKUtils.getMidwareString(R.string.fpv_tip_down_vision_cali);
        }
        if (status.isForeSightDemarkAbnormal()) {
            level = Message.Type.ERROR;
            title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
            description = SDKUtils.getMidwareString(R.string.fpv_tip_front_vision_cali);
        }
        if (this.previousMessage == null || !this.previousMessage.equals(level, title, description)) {
            Message newMessage = new Message(level, title, description);
            LogicEventBus.getInstance().post(new VisionEvent(newMessage));
            this.previousMessage = newMessage;
        }
    }

    public static VisionLogic getInstance() {
        return HOLDER.instance;
    }

    public void init() {
        this.previousMessage = null;
    }

    public static final class VisionEvent {
        private Message message;

        public VisionEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }
}
