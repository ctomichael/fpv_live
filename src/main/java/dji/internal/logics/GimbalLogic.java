package dji.internal.logics;

import dji.common.bus.LogicEventBus;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.Message;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.thirdparty.rx.schedulers.Schedulers;

@EXClassNullAway
public class GimbalLogic implements DJIParamAccessListener {
    private static final String ABNORMAL = "Abnormal";
    private static final String GIMBAL_STUCK = "Gimbal Motor Overloaded";
    private static final String GIMBAL_STUCK_TIP = "Remove gimbal clamp. Contact DJI Support if this error persists.";
    private static final String NORMAL = "Normal";
    private static final String OFFLINE = "N/A";
    private Boolean isGimbalConnected;

    private static final class HOLDER {
        /* access modifiers changed from: private */
        public static GimbalLogic instance = new GimbalLogic();

        private HOLDER() {
        }
    }

    private GimbalLogic() {
        LogicManager.getInstance().addSubscription(LogicEventBus.getInstance().register(DataGimbalGetPushParams.class).subscribeOn(Schedulers.computation()).subscribe(new GimbalLogic$$Lambda$0(this)));
        CacheHelper.addGimbalListener(this, DJISDKCacheKeys.CONNECTION);
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$new$0$GimbalLogic(DataGimbalGetPushParams newPushData) {
        updateSensor();
    }

    public void init() {
        initKeyValues();
    }

    private void initKeyValues() {
        this.isGimbalConnected = (Boolean) CacheHelper.getValue(KeyHelper.getGimbalKey(DJISDKCacheKeys.CONNECTION));
        updateSensor();
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        if (key != null && newValue != null && newValue.getData() != null) {
            if (key.getParamKey().equals(DJISDKCacheKeys.CONNECTION)) {
                this.isGimbalConnected = (Boolean) newValue.getData();
            }
            updateSensor();
        }
    }

    private void updateSensor() {
        Message message;
        if (this.isGimbalConnected == null || !this.isGimbalConnected.booleanValue()) {
            LogicEventBus.getInstance().post(new GimbalEvent(new Message(Message.Type.OFFLINE, "N/A", null)));
            return;
        }
        if (DataGimbalGetPushParams.getInstance().isStuck()) {
            message = new Message(Message.Type.ERROR, ABNORMAL, GIMBAL_STUCK);
        } else {
            message = new Message(Message.Type.GOOD, NORMAL, null);
        }
        LogicEventBus.getInstance().post(new GimbalEvent(message));
    }

    public static GimbalLogic getInstance() {
        return HOLDER.instance;
    }

    public static final class GimbalEvent {
        private Message message;

        public GimbalEvent(Message message2) {
            this.message = message2;
        }

        public Message getMessage() {
            return this.message;
        }
    }
}
