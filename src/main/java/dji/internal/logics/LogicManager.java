package dji.internal.logics;

import dji.common.bus.LogicEventBus;
import dji.common.error.DJIError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycGetPushCheckStatus;
import dji.midware.data.model.P3.DataGimbalGetPushParams;
import dji.midware.data.model.P3.DataOsdGetPushChannalStatus;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.model.P3.DataWifiGetPushElecSignal;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscriber;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.functions.Action1;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import java.util.concurrent.TimeUnit;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class LogicManager {
    final String[] SENSOR_CONFIG;
    private CompositeSubscription subscriptions;

    public static final class SensorShouldUpdateEvent {
    }

    private static class HOLDER {
        /* access modifiers changed from: private */
        public static final LogicManager logicManager = new LogicManager();

        private HOLDER() {
        }
    }

    private LogicManager() {
        this.SENSOR_CONFIG = new String[]{ParamCfgName.GSTATUS_TOPOLOGY_VERIFY_IMU, ParamCfgName.GSTATUS_TOPOLOGY_VERIFY_MAG};
        this.subscriptions = new CompositeSubscription();
        DJIEventBusUtil.register(this);
    }

    public static LogicManager getInstance() {
        return HOLDER.logicManager;
    }

    public void init() {
        addSubscription(LogicEventBus.getInstance().register(ProductType.class).subscribeOn(Schedulers.computation()).flatMap(new Func1<ProductType, Observable<Boolean>>() {
            /* class dji.internal.logics.LogicManager.AnonymousClass2 */

            public Observable<Boolean> call(ProductType productType) {
                return LogicManager.this.getSensorStatus();
            }
        }).subscribe(new Action1<Boolean>() {
            /* class dji.internal.logics.LogicManager.AnonymousClass1 */

            public void call(Boolean successful) {
                if (successful.booleanValue()) {
                    LogicEventBus.getInstance().post(new SensorShouldUpdateEvent());
                    LogicManager.this.getSensorStatus().delay(2000, TimeUnit.MILLISECONDS).subscribe();
                    return;
                }
                LogicManager.this.getSensorStatus().delay(1000, TimeUnit.MILLISECONDS).subscribe();
            }
        }));
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        if (!this.subscriptions.isUnsubscribed()) {
            this.subscriptions.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    public void startFPVTipLogic() {
        FPVTipLogic.getInstance().init();
    }

    public void stopFPVTipLogic() {
        FPVTipLogic.getInstance().destroy();
    }

    public void startGimbalLogic() {
        GimbalLogic.getInstance().init();
    }

    public void startRadioChannelQualityLogic() {
        RadioChannelQualityLogic.getInstance().init();
    }

    public void startIMULogic() {
        IMULogic.getInstance().init();
    }

    public void startCompassLogic() {
        CompassLogic.getInstance().init();
    }

    public void startESCLogic() {
        ESCLogic.getInstance().init();
    }

    public void startBatteryLogic() {
        BatteryLogic.getInstance().init();
    }

    public void startVisionLogic() {
        VisionLogic.getInstance().init();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon common) {
        LogicEventBus.getInstance().post(common);
        LogicEventBus.getInstance().post(new SensorShouldUpdateEvent());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushParams gimbal) {
        LogicEventBus.getInstance().post(gimbal);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome pushHome) {
        LogicEventBus.getInstance().post(pushHome);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushChannalStatus push) {
        LogicEventBus.getInstance().post(push);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushElecSignal push) {
        LogicEventBus.getInstance().post(push);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ProductType productType) {
        LogicEventBus.getInstance().post(productType);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushCheckStatus flyc) {
        LogicEventBus.getInstance().post(flyc);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(Data2100GetPushCheckStatus status) {
        LogicEventBus.getInstance().post(status);
    }

    /* access modifiers changed from: private */
    public Observable<Boolean> getSensorStatus() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            /* class dji.internal.logics.LogicManager.AnonymousClass3 */

            public /* bridge */ /* synthetic */ void call(Object obj) {
                call((Subscriber<? super Boolean>) ((Subscriber) obj));
            }

            public void call(final Subscriber<? super Boolean> subscriber) {
                new DataFlycGetParams().setInfos(LogicManager.this.SENSOR_CONFIG).start(new DJIDataCallBack() {
                    /* class dji.internal.logics.LogicManager.AnonymousClass3.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(true);
                            subscriber.onCompleted();
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(false);
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        });
    }

    public static final class SDKRegistrationEvent {
        private DJIError error;

        public SDKRegistrationEvent(DJIError error2) {
            this.error = error2;
        }

        public DJIError getError() {
            return this.error;
        }
    }
}
