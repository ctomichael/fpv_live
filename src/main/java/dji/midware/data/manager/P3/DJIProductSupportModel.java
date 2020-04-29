package dji.midware.data.manager.P3;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import dji.log.DJILog;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.IProductSupportModel;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.util.BackgroundLooper;
import java.util.HashMap;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIProductSupportModel implements IProductSupportModel {
    private static final long DELAY_CONDITION_CHANGE = 200;
    private static final long DELAY_INIT = 1000;
    public static final String LOG_SAVE_TAG = "recognize";
    private static final int MSGID_CONDITION_CHANGE = 4096;
    private volatile DataCameraGetPushStateInfo.CameraType mCameraType;
    private volatile DataOsdGetPushCommon.DroneType mDroneType;
    private Handler mHandler;
    private final Handler.Callback mHandlerCB;
    private final HashMap<ProductType, ProductType> mNotSupportProductTypes;
    private final HashMap<ProductType, ProductType> mNotSupportRcTypes;
    private volatile int mNotSupportType;
    private final HashMap<DataCameraGetPushStateInfo.CameraType, DataCameraGetPushStateInfo.CameraType> mSupportCameraTypes;
    private final HashMap<DataOsdGetPushCommon.DroneType, DataOsdGetPushCommon.DroneType> mSupportDroneTypes;

    public static DJIProductSupportModel getInstance() {
        return SingletonHolder.sInstance;
    }

    public boolean isSupportGo() {
        if (needCheck() && (this.mNotSupportType & 255) != 0) {
            return false;
        }
        return true;
    }

    public int getNotSupportType() {
        if (!needCheck()) {
            return 0;
        }
        return this.mNotSupportType;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo stateInfo) {
        DataCameraGetPushStateInfo.CameraType cameraType;
        if (needCheck() && (cameraType = stateInfo.getCameraType()) != this.mCameraType) {
            this.mCameraType = cameraType;
            if (!this.mHandler.hasMessages(4096)) {
                DJILog.logWriteD(LOG_SAVE_TAG, "Product support mode camera type Changed", LOG_SAVE_TAG, new Object[0]);
                this.mHandler.sendEmptyMessageDelayed(4096, 200);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon osdGetPushCommon) {
        DataOsdGetPushCommon.DroneType droneType;
        if (needCheck() && this.mDroneType != (droneType = osdGetPushCommon.getDroneType())) {
            this.mDroneType = droneType;
            if (!this.mHandler.hasMessages(4096)) {
                DJILog.logWriteD(LOG_SAVE_TAG, "Product support mode drone type Changed", LOG_SAVE_TAG, new Object[0]);
                this.mHandler.sendEmptyMessageDelayed(4096, 200);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ProductType productType) {
        if (needCheck() && !this.mHandler.hasMessages(4096)) {
            DJILog.logWriteD(LOG_SAVE_TAG, "Product support mode productType Changed", LOG_SAVE_TAG, new Object[0]);
            this.mHandler.sendEmptyMessageDelayed(4096, 200);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DJIProductManager.DJIProductRcEvent productRcEvent) {
        if (needCheck() && DJIProductManager.DJIProductRcEvent.Changed == productRcEvent && !this.mHandler.hasMessages(4096)) {
            DJILog.logWriteD(LOG_SAVE_TAG, "Product support mode DJIProductRcEvent.Changed", LOG_SAVE_TAG, new Object[0]);
            this.mHandler.sendEmptyMessageDelayed(4096, 200);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent cameraEvent) {
        if (needCheck()) {
            if (DataCameraEvent.ConnectOK == cameraEvent) {
                if (!this.mHandler.hasMessages(4096)) {
                    this.mHandler.sendEmptyMessageDelayed(4096, 200);
                }
            } else if (DataCameraEvent.ConnectLose == cameraEvent) {
                this.mDroneType = null;
                this.mCameraType = null;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent dataEvent) {
        if (needCheck() && DataEvent.ConnectLose == dataEvent) {
            this.mDroneType = null;
            this.mCameraType = null;
            if (!this.mHandler.hasMessages(4096)) {
                DJILog.logWriteD(LOG_SAVE_TAG, "Product support mode DataEvent.ConnectLose", LOG_SAVE_TAG, new Object[0]);
                this.mHandler.sendEmptyMessageDelayed(4096, 200);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleConditionChanged() {
        updateSupportModel(checkNotSupportType());
    }

    private int checkNotSupportType() {
        if (!ServiceManager.getInstance().isConnected()) {
            DJILog.logWriteD(LOG_SAVE_TAG, "Product support mode is not connect return!!!", LOG_SAVE_TAG, new Object[0]);
            return 0;
        }
        int type = 0;
        DataOsdGetPushCommon osdGetPushCommon = DataOsdGetPushCommon.getInstance();
        if (osdGetPushCommon.isGetted() && !osdGetPushCommon.isPushLosed()) {
            if (!this.mSupportDroneTypes.containsKey(osdGetPushCommon.getDroneType())) {
                type = 0 | 1;
            }
        }
        DataCameraGetPushStateInfo cameraGetPushStateInfo = DataCameraGetPushStateInfo.getInstance();
        if (cameraGetPushStateInfo.isGetted() && !cameraGetPushStateInfo.isPushLosed()) {
            if (!this.mSupportCameraTypes.containsKey(cameraGetPushStateInfo.getCameraType())) {
                type |= 2;
            }
        }
        DJIProductManager productManager = DJIProductManager.getInstance();
        if (productManager != null) {
            ProductType rcType = productManager.getRcType();
            if (productManager.isRcSeted() && rcType != null && this.mNotSupportRcTypes.containsKey(rcType)) {
                type |= 4;
            }
            ProductType productType = productManager.getType();
            if (productManager.isRemoteSeted() && productType != null && this.mNotSupportProductTypes.containsKey(productType)) {
                type |= 8;
            }
        }
        DJILog.logWriteD(LOG_SAVE_TAG, "Product support model drone type: " + osdGetPushCommon.getDroneType() + ", cameraType: " + cameraGetPushStateInfo.getCameraType() + ", rcType: " + productManager.getRcType() + ", productType: " + productManager.getType(), LOG_SAVE_TAG, new Object[0]);
        return type;
    }

    private void updateSupportModel(int type) {
        if (type != this.mNotSupportType) {
            this.mNotSupportType = type;
            EventBus.getDefault().post(IProductSupportModel.ProductSupportEvent.SUPPORT_CHANGED);
        }
    }

    @Deprecated
    public void removeDroneType(@Nullable DataOsdGetPushCommon.DroneType droneType) {
        this.mSupportDroneTypes.remove(droneType);
    }

    @Deprecated
    public void removeCameraType(@Nullable DataCameraGetPushStateInfo.CameraType cameraType) {
        this.mSupportCameraTypes.remove(cameraType);
    }

    private static boolean needCheck() {
        return true;
    }

    private DJIProductSupportModel() {
        this.mSupportDroneTypes = new HashMap<>();
        this.mSupportCameraTypes = new HashMap<>();
        this.mNotSupportRcTypes = new HashMap<>();
        this.mNotSupportProductTypes = new HashMap<>();
        this.mNotSupportType = 0;
        this.mDroneType = null;
        this.mCameraType = null;
        this.mHandler = null;
        this.mHandlerCB = new Handler.Callback() {
            /* class dji.midware.data.manager.P3.DJIProductSupportModel.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 4096:
                        DJIProductSupportModel.this.handleConditionChanged();
                        return true;
                    default:
                        return true;
                }
            }
        };
        if (needCheck()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            DataCameraGetPushStateInfo.CameraType[] cameraTypeArr = GO_CAMERATYPE_SUPPORT_LIST;
            for (DataCameraGetPushStateInfo.CameraType cameraType : cameraTypeArr) {
                this.mSupportCameraTypes.put(cameraType, cameraType);
            }
            DataOsdGetPushCommon.DroneType[] droneTypeArr = GO_DRONETYPE_SUPPORT_LIST;
            for (DataOsdGetPushCommon.DroneType droneType : droneTypeArr) {
                this.mSupportDroneTypes.put(droneType, droneType);
            }
            ProductType[] productTypeArr = GO_RC_NOTSUPPORT_LIST;
            for (ProductType rcType : productTypeArr) {
                this.mNotSupportRcTypes.put(rcType, rcType);
            }
            ProductType[] productTypeArr2 = GO_PRODUCT_NOTSUPPORT_LIST;
            for (ProductType productType : productTypeArr2) {
                this.mNotSupportProductTypes.put(productType, productType);
            }
            this.mHandler = new Handler(BackgroundLooper.getLooper(), this.mHandlerCB);
            this.mHandler.sendEmptyMessageDelayed(4096, 1000);
        }
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIProductSupportModel sInstance = new DJIProductSupportModel();

        private SingletonHolder() {
        }
    }
}
