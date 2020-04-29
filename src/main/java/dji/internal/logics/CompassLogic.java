package dji.internal.logics;

import dji.common.bus.LogicEventBus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.LogicManager;
import dji.internal.logics.Message;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.sdk.SDKUtils;
import dji.sdkcache.R;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.schedulers.Schedulers;

@EXClassNullAway
public class CompassLogic {
    private boolean compassError;
    private boolean needRefresh;
    private volatile Message previousButtonMessage;
    private volatile Message previousMessage;
    private int previousValue;

    public void init() {
        this.previousMessage = null;
        this.previousButtonMessage = null;
    }

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static CompassLogic instance = new CompassLogic();

        private HOLDER() {
        }
    }

    private CompassLogic() {
        this.compassError = false;
        this.needRefresh = true;
        Subscription subscriptionActionButton = LogicEventBus.getInstance().register(ProductType.class).subscribeOn(Schedulers.computation()).subscribe(new CompassLogic$$Lambda$0(this));
        Subscription subscriptionCompass = LogicEventBus.getInstance().register(LogicManager.SensorShouldUpdateEvent.class).subscribeOn(Schedulers.computation()).subscribe(new CompassLogic$$Lambda$1(this));
        Subscription subscriptionCommon = LogicEventBus.getInstance().register(DataOsdGetPushCommon.class).subscribeOn(Schedulers.computation()).subscribe(new CompassLogic$$Lambda$2(this));
        LogicManager.getInstance().addSubscription(subscriptionActionButton);
        LogicManager.getInstance().addSubscription(subscriptionCompass);
        LogicManager.getInstance().addSubscription(subscriptionCommon);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$CompassLogic(ProductType newProductType) {
        Message message;
        if (CommonUtil.supportRedundancySenor()) {
            message = new Message(Message.Type.GOOD, SDKUtils.getMidwareString(R.string.dji_logic_detail), null);
        } else {
            message = new Message(Message.Type.GOOD, SDKUtils.getMidwareString(R.string.dji_logic_calibrate), null);
        }
        if (this.previousButtonMessage == null || !message.getTitle().equals(this.previousButtonMessage.getTitle())) {
            this.previousButtonMessage = message;
            LogicEventBus.getInstance().post(new CompassButtonEvent(message));
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$1$CompassLogic(LogicManager.SensorShouldUpdateEvent newEvent) {
        updateCompass();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$2$CompassLogic(DataOsdGetPushCommon newPushData) {
        if (this.needRefresh) {
            this.compassError = newPushData.getCompassError();
            updateCompass();
        }
    }

    private void updateCompass() {
        int magStat = DJIFlycParamInfoManager.read(LogicManager.getInstance().SENSOR_CONFIG[1]).value.intValue();
        if (this.previousValue != magStat || this.previousMessage == null) {
            this.previousValue = magStat;
            Message.Type level = Message.Type.GOOD;
            String title = "";
            String description = null;
            if (CommonUtil.supportRedundancySenor()) {
                switch (this.previousValue) {
                    case 0:
                    case 1:
                        level = Message.Type.GOOD;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_normal);
                        break;
                    case 2:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        description = SDKUtils.getMidwareString(R.string.dji_logic_compass_calibrate);
                        break;
                    case 3:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        if (!CommonUtil.isA3Series()) {
                            description = SDKUtils.getMidwareString(R.string.dji_logic_compass_keep_away_from_magnetic);
                            break;
                        } else {
                            description = SDKUtils.getMidwareString(R.string.dji_logic_check_install_direction);
                            break;
                        }
                    case 4:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        description = SDKUtils.getMidwareString(R.string.dji_logic_restart_aircraft);
                        break;
                }
            } else if (this.compassError) {
                level = Message.Type.ERROR;
                title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
            } else {
                level = Message.Type.GOOD;
                title = SDKUtils.getMidwareString(R.string.dji_logic_normal);
            }
            if (this.previousMessage == null || !this.previousMessage.equals(level, title, description)) {
                Message newMessage = new Message(level, title, description);
                LogicEventBus.getInstance().post(new CompassEvent(newMessage));
                this.previousMessage = newMessage;
            }
        }
    }

    public static CompassLogic getInstance() {
        return HOLDER.instance;
    }

    public static final class CompassEvent {
        private Message message;

        public CompassEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }

    public static final class CompassButtonEvent {
        private Message message;

        public CompassButtonEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }
}
