package dji.internal.logics;

import dji.common.bus.LogicEventBus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.Message;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.schedulers.Schedulers;

@EXClassNullAway
public class RadioChannelQualityLogic {
    private static final String FLY_WITH_CAUTION = "Strong Interference. Fly with caution.";
    private static final String GOOD = "Good";
    private static final String POOR = "Poor";
    private Message message;
    private volatile DataOsdGetPushChannalStatus.CHANNEL_STATUS previousValue1;
    private volatile DataWifiGetPushElecSignal.SIGNAL_STATUS previousValue2;

    public void init() {
        this.previousValue1 = null;
        this.previousValue2 = null;
    }

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static RadioChannelQualityLogic instance = new RadioChannelQualityLogic();

        private HOLDER() {
        }
    }

    private RadioChannelQualityLogic() {
        Subscription subscription1 = LogicEventBus.getInstance().register(DataOsdGetPushChannalStatus.class).subscribeOn(Schedulers.computation()).subscribe(new RadioChannelQualityLogic$$Lambda$0(this));
        Subscription subscription2 = LogicEventBus.getInstance().register(DataWifiGetPushElecSignal.class).subscribeOn(Schedulers.computation()).subscribe(new RadioChannelQualityLogic$$Lambda$1(this));
        LogicManager.getInstance().addSubscription(subscription1);
        LogicManager.getInstance().addSubscription(subscription2);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$RadioChannelQualityLogic(DataOsdGetPushChannalStatus newPushData) {
        updateSensor1();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$1$RadioChannelQualityLogic(DataWifiGetPushElecSignal newPushData) {
        updateSensor2();
    }

    private void updateSensor1() {
        DataOsdGetPushChannalStatus.CHANNEL_STATUS status = DataOsdGetPushChannalStatus.getInstance().getChannelStatus();
        if (status != this.previousValue1) {
            this.previousValue1 = status;
            if (isChannelPoor(status)) {
                this.message = new Message(Message.Type.WARNING, POOR, FLY_WITH_CAUTION);
            } else {
                this.message = new Message(Message.Type.GOOD, GOOD, null);
            }
            LogicEventBus.getInstance().post(new RadioChannelQualityEvent(this.message));
        }
    }

    private void updateSensor2() {
        DataWifiGetPushElecSignal.SIGNAL_STATUS wifiStatus = DataWifiGetPushElecSignal.getInstance().getSignalStatus();
        if (this.previousValue2 != wifiStatus) {
            this.previousValue2 = wifiStatus;
            if (isWifiSignalPoor(wifiStatus)) {
                this.message = new Message(Message.Type.WARNING, POOR, FLY_WITH_CAUTION);
            } else {
                this.message = new Message(Message.Type.GOOD, GOOD, null);
            }
            LogicEventBus.getInstance().post(new RadioChannelQualityEvent(this.message));
        }
    }

    public static RadioChannelQualityLogic getInstance() {
        return HOLDER.instance;
    }

    public static final class RadioChannelQualityEvent {
        private Message message;

        public RadioChannelQualityEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }

    private boolean isChannelPoor(DataOsdGetPushChannalStatus.CHANNEL_STATUS status) {
        return (DataOsdGetPushChannalStatus.CHANNEL_STATUS.Excellent == status || DataOsdGetPushChannalStatus.CHANNEL_STATUS.Good == status || DataOsdGetPushChannalStatus.CHANNEL_STATUS.Medium == status) ? false : true;
    }

    private boolean isWifiSignalPoor(DataWifiGetPushElecSignal.SIGNAL_STATUS status) {
        return (DataWifiGetPushElecSignal.SIGNAL_STATUS.Good == status || DataWifiGetPushElecSignal.SIGNAL_STATUS.Medium == status) ? false : true;
    }
}
