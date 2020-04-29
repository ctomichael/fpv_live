package dji.sdksharedlib.hardware.abstractions.payload;

import dji.common.error.DJIError;
import dji.common.payload.PayloadWidget;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataGetPushDataFromPayload;
import dji.midware.data.model.P3.DataGetPushFloatHintMsgFromPayload;
import dji.midware.data.model.P3.DataGetPushPayloadStatus;
import dji.midware.data.model.P3.DataGetPushPayloadWidgetProperty;
import dji.midware.data.model.P3.DataGetPushPayloadWidgetStatus;
import dji.midware.data.model.P3.DataPayloadGetUploadLimit;
import dji.midware.data.model.P3.DataPayloadSendDataToWidget;
import dji.midware.data.model.P3.DataPayloadSetDate;
import dji.midware.data.model.P3.DataPayloadSetRealUploadRate;
import dji.midware.data.model.P3.DataPayloadSetWidget;
import dji.midware.data.model.P3.DataPayloadTurnOnOffPush;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DoubleCameraSupportUtil;
import dji.pilot.fpv.model.IEventObjects;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.PayloadKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIPayloadAbstraction extends DJISDKCacheHWAbstraction {
    private static final int SAMPLE_PERIOD = 2000;
    private static final int SAMPLE_PERIOD_IN_SECOND = 2;
    private static final String TAG = "DJIPayloadAbstraction";
    private int currentUploadRate = 0;
    private AtomicBoolean initSuccess = new AtomicBoolean(false);
    private long lastUploadTime = 0;
    /* access modifiers changed from: private */
    public long totalSizeInOnePeriod = (((long) this.uploadBandwitdhLimit) * 2);
    private long totalSizeOfBytesInLastSecond = 0;
    /* access modifiers changed from: private */
    public float uploadBandwitdhLimit = 500.0f;
    private List<PayloadWidget> widgetList = new ArrayList(5);
    private Object widgetListLock = new Object();

    public void init(String parentPath, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(parentPath, index, storeLayer, onValueChangeListener);
        this.index = index;
        EventBus.getDefault().register(this);
        DataPayloadTurnOnOffPush setter = new DataPayloadTurnOnOffPush();
        setter.turnOn();
        setter.setReceiverId(getReceiverIdByIndex());
        setter.start((DJIDataCallBack) null);
        CacheHelper.addListener(new DJIParamAccessListener() {
            /* class dji.sdksharedlib.hardware.abstractions.payload.DJIPayloadAbstraction.AnonymousClass1 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
            }
        }, KeyHelper.getPayloadKey(index, PayloadKeys.IS_SYNCING_TIME_BETWEEN_APP_AND_PAYLOAD));
    }

    public void destroy() {
        this.initSuccess.set(false);
        if (this.widgetList != null) {
            this.widgetList.clear();
        }
        EventBus.getDefault().unregister(this);
        super.destroy();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(PayloadKeys.class, getClass());
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataGetPushPayloadStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGetPushPayloadStatus.getInstance());
        }
        if (DataGetPushPayloadWidgetStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGetPushPayloadWidgetStatus.getInstance());
        }
        if (DataGetPushDataFromPayload.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGetPushDataFromPayload.getInstance());
        }
        if (DataGetPushFloatHintMsgFromPayload.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGetPushFloatHintMsgFromPayload.getInstance());
        }
        if (DataGetPushPayloadWidgetProperty.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataGetPushPayloadWidgetProperty.getInstance());
        }
        getPayloadUploadLimit(new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.payload.DJIPayloadAbstraction.AnonymousClass2 */

            public void onSuccess(Object o) {
                DJILog.d(DJIPayloadAbstraction.TAG, "limit=" + DJIPayloadAbstraction.this.uploadBandwitdhLimit, new Object[0]);
                DJIPayloadAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf(DJIPayloadAbstraction.this.uploadBandwitdhLimit), PayloadKeys.GET_UPSTREAM_BANDWIDTH);
            }

            public void onFails(DJIError error) {
                DJILog.d(DJIPayloadAbstraction.TAG, "fail! use default limit=" + DJIPayloadAbstraction.this.uploadBandwitdhLimit + " error:" + error.getDescription(), new Object[0]);
                DJIPayloadAbstraction.this.notifyValueChangeForKeyPath(Float.valueOf(DJIPayloadAbstraction.this.uploadBandwitdhLimit), PayloadKeys.GET_UPSTREAM_BANDWIDTH);
            }
        });
    }

    @Getter(PayloadKeys.GET_UPSTREAM_BANDWIDTH)
    public void getPayloadUploadLimit(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataPayloadGetUploadLimit data = new DataPayloadGetUploadLimit();
        data.setReceiverId(getReceiverIdByIndex());
        data.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.payload.DJIPayloadAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                Float result = Float.valueOf((float) data.getLimit());
                float unused = DJIPayloadAbstraction.this.uploadBandwitdhLimit = result.floatValue();
                long unused2 = DJIPayloadAbstraction.this.totalSizeInOnePeriod = ((long) DJIPayloadAbstraction.this.uploadBandwitdhLimit) * 2;
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Action(PayloadKeys.CONFIGURE_WIDGET_VALUE)
    public void setValueForWidget(DJISDKCacheHWAbstraction.InnerCallback callback, Integer index, PayloadWidget.PayloadWidgetType type, Integer value) {
        if (!this.initSuccess.get()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
        } else if (this.widgetList == null || indexAndTypeValid(index.intValue(), type.value())) {
            ((DataPayloadSetWidget) new DataPayloadSetWidget().setWidgetIndex(index.intValue()).setWidgetType(type.value()).setWidgetValue(value.intValue()).setReceiverId(getReceiverIdByIndex(), DataPayloadSetWidget.class)).start(CallbackUtils.defaultCB(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Action(PayloadKeys.GET_WIDGET)
    public void getValueOfWidget(DJISDKCacheHWAbstraction.InnerCallback callback, Integer index, PayloadWidget.PayloadWidgetType type) {
        PayloadWidget payloadWidget;
        if (!this.initSuccess.get()) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
        } else if (this.widgetList == null || indexAndTypeValid(index.intValue(), type.value())) {
            Integer value = DataGetPushPayloadWidgetStatus.getInstance().getValue(type.value(), index.intValue());
            if (value.intValue() != Integer.MAX_VALUE) {
                if (type == PayloadWidget.PayloadWidgetType.INPUT) {
                    payloadWidget = new PayloadWidget(type, index.intValue(), value.intValue(), DataGetPushPayloadWidgetProperty.getInstance().getWidgetName(type.value(), index.intValue()), DataGetPushPayloadWidgetProperty.getInstance().getHintMsgOfWidget(type.value(), index.intValue()));
                } else if (type == PayloadWidget.PayloadWidgetType.LIST) {
                    payloadWidget = new PayloadWidget(type, index.intValue(), value.intValue(), DataGetPushPayloadWidgetProperty.getInstance().getWidgetName(type.value(), index.intValue()), DataGetPushPayloadWidgetProperty.getInstance().getSubListOfWidget(type.value(), index.intValue()));
                } else {
                    payloadWidget = new PayloadWidget(type, index.intValue(), value.intValue(), DataGetPushPayloadWidgetProperty.getInstance().getWidgetName(type.value(), index.intValue()));
                }
                CallbackUtils.onSuccess(callback, payloadWidget);
                return;
            }
            CallbackUtils.onFailure(callback, DJIError.COMMON_SYSTEM_BUSY);
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    private boolean indexAndTypeValid(int index, int type) {
        if (this.widgetList == null || this.widgetList.size() <= 0) {
            return false;
        }
        boolean find = false;
        synchronized (this.widgetListLock) {
            Iterator<PayloadWidget> it2 = this.widgetList.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                PayloadWidget widget = it2.next();
                if (widget.getWidgetType().value() == type && widget.getWidgetIndex() == index) {
                    find = true;
                    break;
                }
            }
        }
        return find;
    }

    @Action(PayloadKeys.SEND_DATA_TO_PAYLOAD)
    public synchronized void sendDataToPayload(DJISDKCacheHWAbstraction.InnerCallback callback, byte[] data) {
        if (data != null) {
            if (data.length != 0 && data.length <= 32) {
                sendData(data, callback);
            }
        }
        CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
    }

    @Getter(PayloadKeys.IS_SYNCING_TIME_BETWEEN_APP_AND_PAYLOAD)
    public void syncTime(DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataPayloadSetDate) DataPayloadSetDate.getInstance().setReceiverId(getReceiverIdByIndex(), DataPayloadSetDate.class)).start((DJIDataCallBack) null);
        if (this.currentUploadRate != 0 && System.currentTimeMillis() - this.lastUploadTime > IEventObjects.PopViewItem.DURATION_DISAPPEAR) {
            this.totalSizeOfBytesInLastSecond = 0;
            this.currentUploadRate = 0;
        }
        ((DataPayloadSetRealUploadRate) DataPayloadSetRealUploadRate.getInstance().setRealRate(this.currentUploadRate).setReceiverId(getReceiverIdByIndex(), DataPayloadSetRealUploadRate.class)).start((DJIDataCallBack) null);
        CallbackUtils.onSuccess(callback, Boolean.TRUE);
    }

    private void sendData(byte[] data, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        long diff = System.currentTimeMillis() - this.lastUploadTime;
        if (diff >= 2000) {
            this.currentUploadRate = (int) (this.totalSizeOfBytesInLastSecond / 2);
            this.lastUploadTime = System.currentTimeMillis();
            this.totalSizeOfBytesInLastSecond = 0;
        } else if (this.totalSizeOfBytesInLastSecond < this.totalSizeInOnePeriod) {
            this.totalSizeOfBytesInLastSecond += (long) data.length;
        } else {
            try {
                Thread.sleep((2000 - diff) + 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (data.length <= 32) {
            DataPayloadSendDataToWidget sendDataToWidget = new DataPayloadSendDataToWidget();
            sendDataToWidget.setReceiverId(getReceiverIdByIndex());
            sendDataToWidget.setData(data).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.payload.DJIPayloadAbstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushPayloadStatus params) {
        if (params.isGetted() && isValidSender(params.getSenderId())) {
            notifyValueChangeForKeyPath(params.getPayloadName(), PayloadKeys.PAYLOAD_PRODUCT_NAME);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushPayloadWidgetStatus params) {
        if (params.isGetted() && isValidSender(params.getSenderId())) {
            if (params.getTotalNumOfWidget() == DataGetPushPayloadWidgetProperty.getInstance().getWidgetNamesSize() && params.getGotWidgetSize() == params.getTotalNumOfWidget()) {
                DataPayloadTurnOnOffPush setter = new DataPayloadTurnOnOffPush();
                setter.turnOff();
                setter.start((DJIDataCallBack) null);
                this.initSuccess.compareAndSet(false, true);
            } else {
                DJILog.d(TAG, "1=" + params.getTotalNumOfWidget() + " 2=" + DataGetPushPayloadWidgetProperty.getInstance().getWidgetNamesSize() + " 3=" + params.getGotWidgetSize(), new Object[0]);
            }
            synchronized (this.widgetListLock) {
                this.widgetList = getAllWidgets(params);
            }
            if (this.widgetList != null && this.widgetList.size() > 0) {
                notifyValueChangeForKeyPath(this.widgetList.toArray(new PayloadWidget[this.widgetList.size()]), PayloadKeys.GET_WIDGETS);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushPayloadWidgetProperty params) {
        if (params.isGetted() && DataGetPushPayloadWidgetStatus.getInstance().isGetted() && isValidSender(params.getSenderId())) {
            if (params.getWidgetNamesSize() == DataGetPushPayloadWidgetStatus.getInstance().getTotalNumOfWidget() && DataGetPushPayloadWidgetStatus.getInstance().getGotWidgetSize() == params.getWidgetNamesSize()) {
                this.initSuccess.compareAndSet(false, true);
                DataPayloadTurnOnOffPush setter = new DataPayloadTurnOnOffPush();
                setter.turnOff();
                setter.start((DJIDataCallBack) null);
            }
            DJILog.d(TAG, "11=" + DataGetPushPayloadWidgetStatus.getInstance().getTotalNumOfWidget() + " 22=" + DataGetPushPayloadWidgetProperty.getInstance().getWidgetNamesSize() + " 33=" + DataGetPushPayloadWidgetStatus.getInstance().getGotWidgetSize(), new Object[0]);
            synchronized (this.widgetListLock) {
                this.widgetList = getAllWidgets(DataGetPushPayloadWidgetStatus.getInstance());
            }
            if (this.widgetList != null && this.widgetList.size() > 0) {
                notifyValueChangeForKeyPath(this.widgetList.toArray(new PayloadWidget[this.widgetList.size()]), PayloadKeys.GET_WIDGETS);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushDataFromPayload params) {
        if (params.isGetted() && isValidSender(params.getSenderId())) {
            notifyValueChangeForKeyPath(params.getData(), PayloadKeys.GET_DATA_FROM_PAYLOAD);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushFloatHintMsgFromPayload params) {
        if (params.isGetted() && isValidSender(params.getSenderId())) {
            notifyValueChangeForKeyPath(params.getData(), PayloadKeys.GET_FLOAT_WINDOW_MSG_FROM_PAYLOAD);
        }
    }

    private List<PayloadWidget> getAllWidgets(DataGetPushPayloadWidgetStatus params) {
        if (params.getAllWidgetValue().size() <= 0 || !this.initSuccess.get()) {
            return null;
        }
        ArrayList<PayloadWidget> result = new ArrayList<>(params.getAllWidgetValue().size());
        for (Map.Entry<String, Integer> item : params.getAllWidgetValue().entrySet()) {
            String key = (String) item.getKey();
            int splitPosition = key.indexOf(":");
            int type = Integer.valueOf(key.substring(0, splitPosition)).intValue();
            int index = Integer.valueOf(key.substring(splitPosition + 1)).intValue();
            result.add(new PayloadWidget(PayloadWidget.PayloadWidgetType.find(type), index, ((Integer) item.getValue()).intValue(), DataGetPushPayloadWidgetProperty.getInstance().getWidgetName(type, index), DataGetPushPayloadWidgetProperty.getInstance().getHintMsgOfWidget(type, index), DataGetPushPayloadWidgetProperty.getInstance().getSubListOfWidget(type, index)));
        }
        return result;
    }

    /* access modifiers changed from: protected */
    public int getReceiverIdByIndex() {
        return DoubleCameraSupportUtil.getPayloadIdInProtocol(this.index);
    }

    /* access modifiers changed from: protected */
    public int getExpectedSenderIdByIndex() {
        return DoubleCameraSupportUtil.getPayloadIdInProtocol(this.index);
    }

    /* access modifiers changed from: protected */
    public boolean isValidSender(int senderId) {
        if (senderId != getExpectedSenderIdByIndex()) {
            return false;
        }
        return true;
    }
}
