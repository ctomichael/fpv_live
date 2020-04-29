package dji.internal.logics;

import dji.common.bus.LogicEventBus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.LogicManager;
import dji.internal.logics.Message;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetPushCheckStatus;
import dji.midware.sdk.SDKUtils;
import dji.sdkcache.R;
import dji.thirdparty.rx.schedulers.Schedulers;

@EXClassNullAway
public class IMULogic {
    private volatile Message previousMessage;
    private int previousValue;

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static IMULogic instance = new IMULogic();

        private HOLDER() {
        }
    }

    private IMULogic() {
        this.previousMessage = null;
        LogicManager.getInstance().addSubscription(LogicEventBus.getInstance().register(LogicManager.SensorShouldUpdateEvent.class, DataFlycGetPushCheckStatus.class).subscribeOn(Schedulers.computation()).subscribe(new IMULogic$$Lambda$0(this)));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$IMULogic(Object newPushData) {
        updateSensor();
    }

    private void updateSensor() {
        int imuStat = DJIFlycParamInfoManager.read(LogicManager.getInstance().SENSOR_CONFIG[0]).value.intValue();
        if (this.previousValue != imuStat || this.previousMessage == null) {
            this.previousValue = imuStat;
            Message.Type level = Message.Type.GOOD;
            String title = "";
            String description = null;
            if (CommonUtil.supportRedundancySenor()) {
                switch (imuStat) {
                    case 0:
                    case 1:
                        level = Message.Type.GOOD;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_normal);
                        break;
                    case 2:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        description = SDKUtils.getMidwareString(R.string.dji_logic_imu_warming_up);
                        break;
                    case 3:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        description = SDKUtils.getMidwareString(R.string.dji_logic_imu_calibrate);
                        break;
                    case 4:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        description = SDKUtils.getMidwareString(R.string.dji_logic_restart_aircraft);
                        break;
                    case 5:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        description = SDKUtils.getMidwareString(R.string.dji_logic_check_install_direction);
                        break;
                    case 6:
                        level = Message.Type.ERROR;
                        title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
                        description = SDKUtils.getMidwareString(R.string.dji_logic_imu_installation_error);
                        break;
                }
            } else if (imuStat == 0) {
                level = Message.Type.GOOD;
                title = SDKUtils.getMidwareString(R.string.dji_logic_normal);
            } else {
                level = Message.Type.ERROR;
                title = SDKUtils.getMidwareString(R.string.dji_logic_abnormal);
            }
            if (this.previousMessage == null || !this.previousMessage.equals(level, title, description)) {
                Message newMessage = new Message(level, title, description);
                LogicEventBus.getInstance().post(new IMUEvent(newMessage));
                this.previousMessage = newMessage;
            }
        }
    }

    public static IMULogic getInstance() {
        return HOLDER.instance;
    }

    public void init() {
        this.previousMessage = null;
    }

    public static final class IMUEvent {
        private Message message;

        public IMUEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }
}
