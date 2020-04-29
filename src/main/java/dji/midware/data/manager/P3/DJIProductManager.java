package dji.midware.data.manager.P3;

import android.content.Context;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.logic.camera.DJICameraLogic;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.model.P3.Data1860GetPushCheckStatus;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataGimbalGetPushType;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushPowerStatus;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.ContextUtil;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.DjiSharedPreferencesManager;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIProductManager {
    private static final String STTAG = "Set Type:";
    private static final String TAG = "DJIProductManager";
    private static DJIProductManager instance = null;
    private final String PRODUCT_CAMERA_KEY = "PRODUCT_CAMERA_KEY";
    private final String PRODUCT_RC_KEY = "PRODUCT_RC_KEY";
    private final String PRODUCT_TYPE_KEY = "PRODUCT_TYPE_KEY";
    private DataCameraGetPushStateInfo.CameraType cameraType;
    private Context ctx;
    private DataOsdGetPushCommon.DroneType droneType;
    private ProductType lastProductType = ProductType.Tomato;
    private DataCameraGetPushStateInfo.CameraType productCameraType = null;
    private ProductType productRcType = null;
    private ProductType productType = ProductType.Tomato;
    private volatile boolean rcSeted = false;
    private volatile boolean remoteSeted = false;

    public enum DJIProductStatus {
        RemoteSetted
    }

    public enum DJIProductRcEvent {
        Changed
    }

    public static synchronized DJIProductManager build(Context context) {
        DJIProductManager dJIProductManager;
        synchronized (DJIProductManager.class) {
            if (instance == null) {
                instance = new DJIProductManager(context);
                instance.init();
            } else {
                DJIEventBusUtil.register(instance);
            }
            dJIProductManager = instance;
        }
        return dJIProductManager;
    }

    public static DJIProductManager getInstance() {
        return instance == null ? build(ContextUtil.getContext()) : instance;
    }

    private DJIProductManager(Context context) {
        this.ctx = context.getApplicationContext();
    }

    public void init() {
        int pvalue = DjiSharedPreferencesManager.getInt(this.ctx, "PRODUCT_TYPE_KEY", ProductType.Tomato.value());
        int rvalue = DjiSharedPreferencesManager.getInt(this.ctx, "PRODUCT_RC_KEY", pvalue);
        int cvalue = DjiSharedPreferencesManager.getInt(this.ctx, "PRODUCT_CAMERA_KEY", 0);
        this.productType = ProductType.find(pvalue);
        this.productRcType = ProductType.find(rvalue);
        this.productCameraType = DataCameraGetPushStateInfo.CameraType.find(cvalue);
        this.lastProductType = this.productType;
        DJILogHelper.getInstance().LOGI(TAG, "lType=" + this.productType + " lrcType=" + this.productRcType, false, true);
        DJIEventBusUtil.register(this);
        DJILogHelper.getInstance().LOGI(TAG, "DJIProductManager register");
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    public ProductType getRcType() {
        return this.productRcType;
    }

    public DataCameraGetPushStateInfo.CameraType getCameraType() {
        return this.productCameraType;
    }

    private void setCameraType(DataCameraGetPushStateInfo.CameraType ct) {
        this.productCameraType = ct;
        DjiSharedPreferencesManager.putInt(this.ctx, "PRODUCT_CAMERA_KEY", this.productCameraType.value());
    }

    public ProductType getType() {
        return this.productType;
    }

    public int getSubType() {
        Data1860GetPushCheckStatus status = Data1860GetPushCheckStatus.getInstance();
        int subType = status.isGetted() ? status.getDeviceSubType() : 0;
        if (getType() == ProductType.WM240) {
            DataCameraGetPushStateInfo.CameraType cameraType2 = DataCameraGetPushStateInfo.getInstance().getCameraType();
            if (cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240_1) {
                subType = 1;
            } else if (cameraType2 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC240) {
                subType = 2;
            }
        }
        if (getType() != ProductType.WM245) {
            return subType;
        }
        DataCameraGetPushStateInfo.CameraType cameraType3 = DataCameraGetPushStateInfo.getInstance().getCameraType();
        if (cameraType3 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC245_IMX477) {
            return 1;
        }
        if (cameraType3 == DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC2403) {
            return 2;
        }
        return subType;
    }

    public ProductType getLastType() {
        return this.lastProductType;
    }

    public boolean isRemoteSeted() {
        return this.remoteSeted;
    }

    public synchronized void setRemoteSeted(boolean isSeted) {
        this.remoteSeted = isSeted;
        if (isSeted) {
            EventBus.getDefault().post(DJIProductStatus.RemoteSetted);
        }
    }

    public synchronized void setType(ProductType type) {
        if (type != null) {
            if (this.productType != type) {
                DJILogHelper.getInstance().LOGI(TAG, "type=" + type + " last=" + this.productType, false, true);
                this.lastProductType = this.productType;
                this.productType = type;
                EventBus.getDefault().post(this.productType);
                correctRcType();
                DjiSharedPreferencesManager.putInt(this.ctx, "PRODUCT_TYPE_KEY", this.productType.value());
            }
        }
    }

    public synchronized void updateRcType(ProductType type) {
        if (type == ProductType.litchiX && this.productType == ProductType.litchiS) {
            type = ProductType.litchiS;
        }
        if (this.productRcType != type) {
            this.productRcType = type;
            EventBus.getDefault().post(DJIProductRcEvent.Changed);
            DjiSharedPreferencesManager.putInt(this.ctx, "PRODUCT_RC_KEY", this.productRcType.value());
        }
        DJILog.d(TAG, "updateRCType rcType: " + this.productRcType, new Object[0]);
        DJILogHelper.getInstance().LOGI(TAG, "===rcType===[" + type + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
    }

    private void correctRcType() {
        DJILog.d(TAG, "correct RC type: ", new Object[0]);
        DJILog.d(TAG, "get model: " + UsbAccessoryService.getInstance().getUsbModel(), new Object[0]);
        if (UsbAccessoryService.getInstance().getUsbModel() == DJIUsbAccessoryReceiver.UsbModel.AG) {
            updateRcType(ProductType.PomatoRTK);
            return;
        }
        if (this.productType == ProductType.litchiS && this.productRcType == ProductType.litchiX) {
            updateRcType(ProductType.litchiS);
        }
        if (this.productType == ProductType.Longan && this.productRcType == ProductType.litchiC) {
            updateRcType(ProductType.Longan);
        }
        if (this.productType == ProductType.A2 || this.productType == ProductType.A3 || this.productType == ProductType.PM820 || this.productType == ProductType.PM820PRO || this.productType == ProductType.N3) {
            updateRcType(ProductType.Grape2);
        }
        if (this.productRcType == null) {
            updateRcType(this.productType);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0025  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void updateRcTypeByLinkType(dji.midware.link.DJILinkType r10) {
        /*
            r9 = this;
            monitor-enter(r9)
            r3 = 0
            r1 = 0
            int[] r4 = dji.midware.data.manager.P3.DJIProductManager.AnonymousClass1.$SwitchMap$dji$midware$link$DJILinkType     // Catch:{ all -> 0x00bb }
            int r5 = r10.ordinal()     // Catch:{ all -> 0x00bb }
            r4 = r4[r5]     // Catch:{ all -> 0x00bb }
            switch(r4) {
                case 1: goto L_0x0052;
                case 2: goto L_0x0052;
                case 3: goto L_0x0052;
                case 4: goto L_0x0081;
                case 5: goto L_0x0105;
                default: goto L_0x000e;
            }     // Catch:{ all -> 0x00bb }
        L_0x000e:
            r4 = 0
            r9.setRcSeted(r4)     // Catch:{ all -> 0x00bb }
        L_0x0012:
            if (r3 == 0) goto L_0x0029
            dji.midware.data.config.P3.ProductType r4 = r9.productRcType     // Catch:{ all -> 0x00bb }
            if (r4 == r3) goto L_0x0029
            r9.productRcType = r3     // Catch:{ all -> 0x00bb }
            org.greenrobot.eventbus.EventBus r4 = org.greenrobot.eventbus.EventBus.getDefault()     // Catch:{ all -> 0x00bb }
            dji.midware.data.manager.P3.DJIProductManager$DJIProductRcEvent r5 = dji.midware.data.manager.P3.DJIProductManager.DJIProductRcEvent.Changed     // Catch:{ all -> 0x00bb }
            r4.post(r5)     // Catch:{ all -> 0x00bb }
            if (r1 == 0) goto L_0x0029
            r4 = 0
            r9.setRcSeted(r4)     // Catch:{ all -> 0x00bb }
        L_0x0029:
            dji.log.DJILogHelper r4 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x00bb }
            java.lang.String r5 = ""
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bb }
            r6.<init>()     // Catch:{ all -> 0x00bb }
            java.lang.String r7 = "===rcType bylink===["
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00bb }
            java.lang.StringBuilder r6 = r6.append(r3)     // Catch:{ all -> 0x00bb }
            java.lang.String r7 = "]"
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ all -> 0x00bb }
            java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x00bb }
            r7 = 0
            r8 = 1
            r4.LOGI(r5, r6, r7, r8)     // Catch:{ all -> 0x00bb }
            monitor-exit(r9)
            return
        L_0x0052:
            dji.midware.data.config.P3.ProductType r4 = r9.productRcType     // Catch:{ all -> 0x00bb }
            boolean r4 = dji.midware.data.config.P3.ProductType.isValidType(r4)     // Catch:{ all -> 0x00bb }
            if (r4 == 0) goto L_0x007d
            dji.midware.data.config.P3.ProductType r4 = r9.productRcType     // Catch:{ all -> 0x00bb }
            boolean r4 = r4.isFromWifi()     // Catch:{ all -> 0x00bb }
            if (r4 != 0) goto L_0x007d
            dji.midware.usb.P3.UsbAccessoryService r4 = dji.midware.usb.P3.UsbAccessoryService.getInstance()     // Catch:{ all -> 0x00bb }
            dji.midware.usb.P3.DJIUsbAccessoryReceiver$UsbModel r4 = r4.getUsbModel()     // Catch:{ all -> 0x00bb }
            dji.midware.usb.P3.DJIUsbAccessoryReceiver$UsbModel r5 = dji.midware.usb.P3.DJIUsbAccessoryReceiver.UsbModel.AG     // Catch:{ all -> 0x00bb }
            if (r4 != r5) goto L_0x0012
            java.lang.String r4 = "DJIProductManager"
            java.lang.String r5 = "2"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x00bb }
            dji.log.DJILog.d(r4, r5, r6)     // Catch:{ all -> 0x00bb }
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.PomatoRTK     // Catch:{ all -> 0x00bb }
            goto L_0x0012
        L_0x007d:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.Tomato     // Catch:{ all -> 0x00bb }
            r1 = 1
            goto L_0x0012
        L_0x0081:
            dji.midware.data.model.P3.DataCameraGetPushStateInfo r4 = dji.midware.data.model.P3.DataCameraGetPushStateInfo.getInstance()     // Catch:{ all -> 0x00bb }
            boolean r4 = r4.isGetted()     // Catch:{ all -> 0x00bb }
            if (r4 == 0) goto L_0x00fd
            dji.midware.data.model.P3.DataCameraGetPushStateInfo r4 = dji.midware.data.model.P3.DataCameraGetPushStateInfo.getInstance()     // Catch:{ all -> 0x00bb }
            r5 = 0
            dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType r4 = r4.getCameraType(r5)     // Catch:{ all -> 0x00bb }
            r9.cameraType = r4     // Catch:{ all -> 0x00bb }
            dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType r4 = r9.cameraType     // Catch:{ all -> 0x00bb }
            r9.setCameraType(r4)     // Catch:{ all -> 0x00bb }
            int[] r4 = dji.midware.data.manager.P3.DJIProductManager.AnonymousClass1.$SwitchMap$dji$midware$data$model$P3$DataCameraGetPushStateInfo$CameraType     // Catch:{ all -> 0x00bb }
            dji.midware.data.model.P3.DataCameraGetPushStateInfo$CameraType r5 = r9.cameraType     // Catch:{ all -> 0x00bb }
            int r5 = r5.ordinal()     // Catch:{ all -> 0x00bb }
            r4 = r4[r5]     // Catch:{ all -> 0x00bb }
            switch(r4) {
                case 1: goto L_0x00be;
                case 2: goto L_0x00c1;
                case 3: goto L_0x00c4;
                case 4: goto L_0x00c4;
                case 5: goto L_0x00ee;
                case 6: goto L_0x00f1;
                case 7: goto L_0x00f4;
                case 8: goto L_0x00f4;
                case 9: goto L_0x00f7;
                case 10: goto L_0x00f7;
                case 11: goto L_0x00fa;
                default: goto L_0x00a8;
            }     // Catch:{ all -> 0x00bb }
        L_0x00a8:
            r4 = 0
            r9.setRcSeted(r4)     // Catch:{ all -> 0x00bb }
        L_0x00ac:
            boolean r4 = r9.isRemoteSeted()     // Catch:{ all -> 0x00bb }
            if (r4 != 0) goto L_0x0012
            r9.setType(r3)     // Catch:{ all -> 0x00bb }
            r4 = 1
            r9.setRcSeted(r4)     // Catch:{ all -> 0x00bb }
            goto L_0x0012
        L_0x00bb:
            r4 = move-exception
            monitor-exit(r9)
            throw r4
        L_0x00be:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.Longan     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00c1:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.LonganZoom     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00c4:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.KumquatX     // Catch:{ all -> 0x00bb }
            boolean r4 = r9.isRemoteSeted()     // Catch:{ all -> 0x00bb }
            if (r4 != 0) goto L_0x00ac
            r9.setType(r3)     // Catch:{ all -> 0x00bb }
            java.lang.String r4 = "Set Type:"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bb }
            r5.<init>()     // Catch:{ all -> 0x00bb }
            java.lang.String r6 = "updateRcTypeByLinkType:"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x00bb }
            java.lang.StringBuilder r5 = r5.append(r3)     // Catch:{ all -> 0x00bb }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00bb }
            android.util.Log.d(r4, r5)     // Catch:{ all -> 0x00bb }
            r4 = 1
            r9.setRcSeted(r4)     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00ee:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.Mammoth     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00f1:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.WM230     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00f4:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.WM240     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00f7:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.WM245     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00fa:
            dji.midware.data.config.P3.ProductType r3 = dji.midware.data.config.P3.ProductType.WM160     // Catch:{ all -> 0x00bb }
            goto L_0x00ac
        L_0x00fd:
            boolean r4 = r9.isRemoteSeted()     // Catch:{ all -> 0x00bb }
            if (r4 != 0) goto L_0x0012
            goto L_0x0012
        L_0x0105:
            java.lang.String r4 = "DJIProductManager"
            java.lang.String r5 = "ble link"
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x00bb }
            dji.log.DJILog.d(r4, r5, r6)     // Catch:{ all -> 0x00bb }
            dji.midware.data.config.P3.ProductType r2 = dji.midware.data.config.P3.ProductType.LonganMobile     // Catch:{ all -> 0x00bb }
            java.lang.String r4 = "DJIProductManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bb }
            r5.<init>()     // Catch:{ all -> 0x00bb }
            java.lang.String r6 = "DataGimbalGetPushType getted？ :"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x00bb }
            dji.midware.data.model.P3.DataGimbalGetPushType r6 = dji.midware.data.model.P3.DataGimbalGetPushType.getInstance()     // Catch:{ all -> 0x00bb }
            boolean r6 = r6.isGetted()     // Catch:{ all -> 0x00bb }
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x00bb }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00bb }
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x00bb }
            dji.log.DJILog.d(r4, r5, r6)     // Catch:{ all -> 0x00bb }
            dji.midware.data.model.P3.DataGimbalGetPushType r4 = dji.midware.data.model.P3.DataGimbalGetPushType.getInstance()     // Catch:{ all -> 0x00bb }
            boolean r4 = r4.isGetted()     // Catch:{ all -> 0x00bb }
            if (r4 == 0) goto L_0x016d
            dji.midware.data.model.P3.DataGimbalGetPushType r4 = dji.midware.data.model.P3.DataGimbalGetPushType.getInstance()     // Catch:{ all -> 0x00bb }
            dji.midware.data.model.P3.DataGimbalGetPushType$DJIGimbalType r0 = r4.getType()     // Catch:{ all -> 0x00bb }
            dji.midware.data.model.P3.DataGimbalGetPushType$DJIGimbalType r4 = dji.midware.data.model.P3.DataGimbalGetPushType.DJIGimbalType.HG300     // Catch:{ all -> 0x00bb }
            if (r0 != r4) goto L_0x017c
            dji.midware.data.config.P3.ProductType r2 = dji.midware.data.config.P3.ProductType.LonganMobile     // Catch:{ all -> 0x00bb }
        L_0x0150:
            java.lang.String r4 = "DJIProductManager"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bb }
            r5.<init>()     // Catch:{ all -> 0x00bb }
            java.lang.String r6 = "DataGimbalGetPushType getted, Type:"
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ all -> 0x00bb }
            java.lang.StringBuilder r5 = r5.append(r0)     // Catch:{ all -> 0x00bb }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00bb }
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x00bb }
            dji.log.DJILog.d(r4, r5, r6)     // Catch:{ all -> 0x00bb }
        L_0x016d:
            if (r2 == 0) goto L_0x0012
            r4 = 1
            r9.setRcSeted(r4)     // Catch:{ all -> 0x00bb }
            r9.setType(r2)     // Catch:{ all -> 0x00bb }
            r4 = 1
            r9.setRemoteSeted(r4)     // Catch:{ all -> 0x00bb }
            goto L_0x0012
        L_0x017c:
            dji.midware.data.model.P3.DataGimbalGetPushType$DJIGimbalType r4 = dji.midware.data.model.P3.DataGimbalGetPushType.DJIGimbalType.HG301     // Catch:{ all -> 0x00bb }
            if (r0 != r4) goto L_0x0150
            dji.midware.data.config.P3.ProductType r2 = dji.midware.data.config.P3.ProductType.LonganMobile2     // Catch:{ all -> 0x00bb }
            goto L_0x0150
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.manager.P3.DJIProductManager.updateRcTypeByLinkType(dji.midware.link.DJILinkType):void");
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJILinkType linkType) {
        DJILogHelper.getInstance().LOGI("", "===DJIProductManager===[" + linkType + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        updateRcTypeByLinkType(linkType);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushPowerStatus stateInfo) {
        DJILogHelper.getInstance().LOGI("", "===DataOsdGetPushPowerStatus===[" + stateInfo.getPowerStatus() + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        ProductType mType = null;
        if (this.cameraType != null) {
            switch (this.cameraType) {
                case DJICameraTypeFC350:
                    mType = ProductType.Longan;
                    break;
                case DJICameraTypeCV600:
                    mType = ProductType.LonganZoom;
                    break;
                case DJICameraTypeFC550:
                    mType = ProductType.LonganPro;
                    break;
                case DJICameraTypeFC550Raw:
                    mType = ProductType.LonganRaw;
                    break;
            }
        }
        if (mType != null) {
            setType(mType);
            setRemoteSeted(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo stateInfo) {
        if (stateInfo.isGetted() && !stateInfo.isPushLosed() && stateInfo.getSenderId() == 0) {
            DataCameraGetPushStateInfo.CameraType cType = stateInfo.getCameraType();
            if ((this.cameraType != cType && DJICameraLogic.isMachineCamera(cType)) || !isRemoteSeted() || this.productType == ProductType.Grape2) {
                this.cameraType = stateInfo.getCameraType();
                setCameraType(this.cameraType);
                DJILogHelper.getInstance().LOGI(STTAG, "cameraType=" + this.cameraType, false, true);
                ProductType productType2 = null;
                switch (this.cameraType) {
                    case DJICameraTypeFC350:
                        switch (DJILinkDaemonService.getInstance().getLinkType()) {
                            case ADB:
                            case AOA:
                                if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M200) {
                                    if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210) {
                                        if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210RTK) {
                                            if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420) {
                                                if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420PRO) {
                                                    if (DataOsdGetPushCommon.getInstance().isGetted() && DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.PM420PRO_RTK) {
                                                        productType2 = ProductType.PM420PRO_RTK;
                                                        break;
                                                    } else {
                                                        productType2 = ProductType.Orange;
                                                        break;
                                                    }
                                                } else {
                                                    productType2 = ProductType.PM420PRO;
                                                    break;
                                                }
                                            } else {
                                                productType2 = ProductType.PM420;
                                                break;
                                            }
                                        } else {
                                            productType2 = ProductType.M210RTK;
                                            break;
                                        }
                                    } else {
                                        productType2 = ProductType.M210;
                                        break;
                                    }
                                } else {
                                    productType2 = ProductType.M200;
                                    break;
                                }
                                break;
                            case HOST:
                            default:
                                productType2 = ProductType.Longan;
                                break;
                            case WIFI:
                                productType2 = ProductType.Longan;
                                break;
                        }
                    case DJICameraTypeCV600:
                        switch (DJILinkDaemonService.getInstance().getLinkType()) {
                            case ADB:
                            case AOA:
                                productType2 = ProductType.OrangeCV600;
                                break;
                            case WIFI:
                                productType2 = ProductType.LonganZoom;
                                break;
                        }
                    case DJICameraTypeFC220:
                        productType2 = ProductType.KumquatX;
                        break;
                    case DJICameraTypeFC220S:
                        productType2 = ProductType.KumquatS;
                        break;
                    case DJICameraTypeFC1102:
                        productType2 = ProductType.Mammoth;
                        break;
                    case DJICameraTypeFC230:
                        productType2 = ProductType.WM230;
                        break;
                    case DJICameraTypeFC240:
                    case DJICameraTypeFC240_1:
                        productType2 = ProductType.WM240;
                        break;
                    case DJICameraTypeFC245_IMX477:
                    case DJICameraTypeFC2403:
                        productType2 = ProductType.WM245;
                        break;
                    case DJICameraTypeFC160:
                        productType2 = ProductType.WM160;
                        break;
                    case DJICameraTypeFC550:
                        switch (DJILinkDaemonService.getInstance().getLinkType()) {
                            case ADB:
                            case AOA:
                            case HOST:
                                productType2 = ProductType.BigBanana;
                                break;
                            case WIFI:
                                productType2 = ProductType.LonganRaw;
                                break;
                            default:
                                productType2 = ProductType.LonganRaw;
                                break;
                        }
                    case DJICameraTypeFC550Raw:
                        switch (DJILinkDaemonService.getInstance().getLinkType()) {
                            case ADB:
                            case AOA:
                                productType2 = ProductType.OrangeRAW;
                                break;
                            case WIFI:
                                productType2 = ProductType.LonganRaw;
                                break;
                        }
                    case DJICameraTypeFC300S:
                        productType2 = ProductType.litchiS;
                        break;
                    case DJICameraTypeFC300X:
                        productType2 = ProductType.litchiX;
                        break;
                    case DJICameraTypeFC260:
                        productType2 = ProductType.litchiC;
                        break;
                    case DJICameraTypeFC330X:
                        productType2 = ProductType.Tomato;
                        break;
                    case DJICameraTypeFC300XW:
                        productType2 = ProductType.P34K;
                        break;
                    case DJICameraTypeFC6310:
                        DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
                        DataOsdGetPushCommon.DroneType droneType2 = common.isGetted() ? common.getDroneType() : DataOsdGetPushCommon.DroneType.Unknown;
                        if (droneType2 != DataOsdGetPushCommon.DroneType.Potato) {
                            if (droneType2 != DataOsdGetPushCommon.DroneType.PomatoRTK) {
                                productType2 = ProductType.Pomato;
                                break;
                            } else {
                                productType2 = ProductType.PomatoRTK;
                                break;
                            }
                        } else {
                            productType2 = ProductType.Potato;
                            break;
                        }
                    case DJICameraTypeFC6310S:
                        productType2 = ProductType.PomatoSDR;
                        break;
                    case DJICameraTypeTau336:
                    case DJICameraTypeTau640:
                        if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.OpenFrame) {
                            if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M200) {
                                if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210) {
                                    if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.M210RTK) {
                                        if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420) {
                                            if (!DataOsdGetPushCommon.getInstance().isGetted() || DataOsdGetPushCommon.getInstance().getDroneType() != DataOsdGetPushCommon.DroneType.PM420PRO) {
                                                if (DataOsdGetPushCommon.getInstance().isGetted() && DataOsdGetPushCommon.getInstance().getDroneType() == DataOsdGetPushCommon.DroneType.PM420PRO_RTK) {
                                                    productType2 = ProductType.PM420PRO_RTK;
                                                    break;
                                                } else {
                                                    productType2 = ProductType.Olives;
                                                    break;
                                                }
                                            } else {
                                                productType2 = ProductType.PM420PRO;
                                                break;
                                            }
                                        } else {
                                            productType2 = ProductType.PM420;
                                            break;
                                        }
                                    } else {
                                        productType2 = ProductType.M210RTK;
                                        break;
                                    }
                                } else {
                                    productType2 = ProductType.M210;
                                    break;
                                }
                            } else {
                                productType2 = ProductType.M200;
                                break;
                            }
                        } else {
                            productType2 = ProductType.N1;
                            break;
                        }
                        break;
                    case DJICameraTypeFC6510:
                    case DJICameraTypeFC6520:
                    case DJICameraTypeFC6540:
                        if (DataOsdGetPushCommon.getInstance().isGetted()) {
                            DataOsdGetPushCommon.DroneType droneType3 = DataOsdGetPushCommon.getInstance().getDroneType();
                            if (!droneType3.equals(DataOsdGetPushCommon.DroneType.M200)) {
                                if (!droneType3.equals(DataOsdGetPushCommon.DroneType.M210)) {
                                    if (!droneType3.equals(DataOsdGetPushCommon.DroneType.M210RTK)) {
                                        if (!droneType3.equals(DataOsdGetPushCommon.DroneType.PM420)) {
                                            if (!droneType3.equals(DataOsdGetPushCommon.DroneType.PM420PRO)) {
                                                if (!droneType3.equals(DataOsdGetPushCommon.DroneType.PM420PRO_RTK)) {
                                                    if (!droneType3.equals(DataOsdGetPushCommon.DroneType.Orange2)) {
                                                        if (droneType3.equals(DataOsdGetPushCommon.DroneType.Unknown)) {
                                                            productType2 = ProductType.Orange2;
                                                            break;
                                                        }
                                                    } else {
                                                        productType2 = ProductType.Orange2;
                                                        break;
                                                    }
                                                } else {
                                                    productType2 = ProductType.PM420PRO_RTK;
                                                    break;
                                                }
                                            } else {
                                                productType2 = ProductType.PM420PRO;
                                                break;
                                            }
                                        } else {
                                            productType2 = ProductType.PM420;
                                            break;
                                        }
                                    } else {
                                        productType2 = ProductType.M210RTK;
                                        break;
                                    }
                                } else {
                                    productType2 = ProductType.M210;
                                    break;
                                }
                            } else {
                                productType2 = ProductType.M200;
                                break;
                            }
                        }
                        break;
                }
                if (productType2 != null) {
                    setType(productType2);
                    DJILogHelper.getInstance().LOGI(STTAG, "DataCameraGetPushStateInfo:" + productType2, false, true);
                    setRemoteSeted(true);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon pushCommon) {
        if (!pushCommon.isPushLosed() && this.droneType == null) {
            this.droneType = pushCommon.getDroneType();
            switch (this.droneType) {
                case OpenFrame:
                    setType(ProductType.N1);
                    setRemoteSeted(true);
                    break;
                case A3:
                    setType(ProductType.A3);
                    setRemoteSeted(true);
                    break;
                case PM820:
                    setType(ProductType.PM820);
                    setRemoteSeted(true);
                    break;
                case PM820PRO:
                    setType(ProductType.PM820PRO);
                    setRemoteSeted(true);
                    break;
                case A2:
                    setType(ProductType.A2);
                    setRemoteSeted(true);
                    break;
                case Orange2:
                    setType(ProductType.Orange2);
                    setRemoteSeted(true);
                    break;
                case N3:
                    setType(ProductType.N3);
                    setRemoteSeted(true);
                    break;
                case P4:
                    setType(ProductType.Tomato);
                    setRemoteSeted(true);
                    break;
                case Potato:
                    setType(ProductType.Potato);
                    setRemoteSeted(true);
                    break;
                case Pomato:
                    setType(ProductType.Pomato);
                    setRemoteSeted(true);
                    break;
                case wm220:
                    setType(ProductType.KumquatX);
                    setRemoteSeted(true);
                    break;
                case Mammoth:
                    DJILogHelper.getInstance().LOGI("", "droneType=" + this.droneType, false, true);
                    setType(ProductType.Mammoth);
                    setRcSeted(true);
                    break;
                case WM230:
                    setType(ProductType.WM230);
                    setRcSeted(true);
                    break;
                case WM240:
                    setType(ProductType.WM240);
                    setRcSeted(true);
                    break;
                case WM245:
                    setType(ProductType.WM245);
                    setRcSeted(true);
                    break;
                case M200:
                    setType(ProductType.M200);
                    setRemoteSeted(true);
                    break;
                case M210:
                    setType(ProductType.M210);
                    setRemoteSeted(true);
                    break;
                case M210RTK:
                    setType(ProductType.M210RTK);
                    setRemoteSeted(true);
                    break;
                case PM420:
                    setType(ProductType.PM420);
                    setRemoteSeted(true);
                    break;
                case PM420PRO:
                    setType(ProductType.PM420PRO);
                    setRemoteSeted(true);
                    break;
                case PM420PRO_RTK:
                    setType(ProductType.PM420PRO_RTK);
                    setRemoteSeted(true);
                    break;
                case PomatoSdr:
                    setType(ProductType.PomatoSDR);
                    setRemoteSeted(true);
                    break;
                case PomatoRTK:
                    setType(ProductType.PomatoRTK);
                    setRemoteSeted(true);
                    break;
                case WM160:
                    setType(ProductType.WM160);
                    setRemoteSeted(true);
                    break;
            }
            DJILogHelper.getInstance().LOGI(TAG, "According To DroneType=" + this.droneType + " and SetType " + getType(), false, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        switch (event) {
            case ConnectLose:
                this.droneType = null;
                this.cameraType = null;
                setRemoteSeted(false);
                ServiceManager.getInstance().onDisconnect();
                return;
            default:
                return;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        switch (event) {
            case ConnectLose:
                setRcSeted(false);
                setRemoteSeted(false);
                return;
            default:
                return;
        }
    }

    public boolean isRcSeted() {
        return this.rcSeted;
    }

    public synchronized void setRcSeted(boolean isSeted) {
        this.rcSeted = isSeted;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGimbalGetPushType gimbalTypePush) {
        DataGimbalGetPushType.DJIGimbalType gimbalType = gimbalTypePush.getType();
        ProductType pType = null;
        if (gimbalType == DataGimbalGetPushType.DJIGimbalType.HG301) {
            pType = ProductType.LonganMobile2;
        }
        DJILog.d(TAG, "DataGimbalGetPushType push, Type:" + gimbalType, new Object[0]);
        if (pType != null) {
            setRcSeted(true);
            setType(pType);
            setRemoteSeted(true);
        }
    }
}
