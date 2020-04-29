package dji.logic.mc;

import android.os.Handler;
import android.os.Message;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.params.P3.ParamCfgName;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIMcHelper implements ParamCfgName {
    private static final String[] CFG_CTRLMODES = {ParamCfgName.GCONFIG_CONTROL_CONTROLMODE_0, ParamCfgName.GCONFIG_CONTROL_CONTROLMODE_1, ParamCfgName.GCONFIG_CONTROL_CONTROLMODE_2};
    private static final int CODE_FAIL = 1;
    private static final int CODE_SUCCESS = 0;
    private static final DataOsdGetPushCommon.RcModeChannel[] DEFAULT_RCMODECHLS = {DataOsdGetPushCommon.RcModeChannel.CHANNEL_F, DataOsdGetPushCommon.RcModeChannel.CHANNEL_A, DataOsdGetPushCommon.RcModeChannel.CHANNEL_P};
    private static final DataOsdGetPushCommon.RcModeChannel[] DEFAULT_RCMODECHLS_SPORT = {DataOsdGetPushCommon.RcModeChannel.CHANNEL_A, DataOsdGetPushCommon.RcModeChannel.CHANNEL_S, DataOsdGetPushCommon.RcModeChannel.CHANNEL_P};
    private static final int MSG_ID_GET_RCMODECHLS = 4096;
    private static final int MSG_ID_REGET_RCMODECHLS = 4097;
    private static final String TAG = DJIMcHelper.class.getSimpleName();
    private volatile int mFlycVersion = Integer.MIN_VALUE;
    private volatile boolean mGetted = false;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
        /* class dji.logic.mc.DJIMcHelper.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 4096:
                    if (1 == msg.arg1) {
                        DJIMcHelper.this.regetRcModeChannel();
                        return false;
                    } else if (msg.arg1 != 0) {
                        return false;
                    } else {
                        DJIMcHelper.this.getRcModeChannelByParam();
                        return false;
                    }
                case 4097:
                    DJIMcHelper.this.regetRcModeChannel();
                    return false;
                default:
                    return false;
            }
        }
    });
    private final ReentrantReadWriteLock.ReadLock mRLocker = this.mRWLocker.readLock();
    private final ReentrantReadWriteLock mRWLocker = new ReentrantReadWriteLock(false);
    private final DataOsdGetPushCommon.RcModeChannel[] mRcModeChannels = {DataOsdGetPushCommon.RcModeChannel.CHANNEL_F, DataOsdGetPushCommon.RcModeChannel.CHANNEL_A, DataOsdGetPushCommon.RcModeChannel.CHANNEL_P};
    private final DataOsdGetPushCommon.RcModeChannel[] mTmpRcModeChls = {DataOsdGetPushCommon.RcModeChannel.CHANNEL_F, DataOsdGetPushCommon.RcModeChannel.CHANNEL_A, DataOsdGetPushCommon.RcModeChannel.CHANNEL_P};
    private final ReentrantReadWriteLock.WriteLock mWLocker = this.mRWLocker.writeLock();
    final DataFlycGetParams setter = new DataFlycGetParams();

    public static DJIMcHelper getInstance() {
        if (SingletonHolder.mInstance == null) {
            DJIMcHelper unused = SingletonHolder.mInstance = new DJIMcHelper();
        }
        if (!EventBus.getDefault().isRegistered(SingletonHolder.mInstance)) {
            SingletonHolder.mInstance.init();
        }
        return SingletonHolder.mInstance;
    }

    public boolean isGetted() {
        return this.mGetted;
    }

    public DataOsdGetPushCommon.RcModeChannel getRcModeChannel(int index) {
        DataOsdGetPushCommon.RcModeChannel rcModeChannel;
        this.mRLocker.lock();
        int retIndex = index;
        if (retIndex >= 0) {
            try {
                if (retIndex < this.mRcModeChannels.length) {
                    rcModeChannel = this.mRcModeChannels[retIndex];
                    return rcModeChannel;
                }
            } finally {
                this.mRLocker.unlock();
            }
        }
        rcModeChannel = DataOsdGetPushCommon.RcModeChannel.CHANNEL_UNKNOWN;
        this.mRLocker.unlock();
        return rcModeChannel;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        if (DataEvent.ConnectLose == event) {
            resetRcModeChannels();
            this.mGetted = false;
            this.mFlycVersion = Integer.MIN_VALUE;
            this.mHandler.removeMessages(4096);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (DataCameraEvent.ConnectLose == event) {
            resetRcModeChannels();
            this.mGetted = false;
            this.mFlycVersion = Integer.MIN_VALUE;
            this.mHandler.removeMessages(4096);
            return;
        }
        if (DataCameraEvent.ConnectOK == event) {
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon common) {
        int version = common.getFlycVersion();
        if (version != this.mFlycVersion) {
            this.mFlycVersion = version;
            this.mHandler.removeMessages(4096);
            if (version >= 14) {
                this.mGetted = false;
                regetRcModeChannel();
            }
            DJILogHelper.getInstance().LOGD(TAG, "Get RcMode version[" + this.mFlycVersion + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ProductType type) {
        if (!this.mGetted) {
            resetRcModeChannels();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: private */
    public void getRcModeChannelByParam() {
        this.mGetted = true;
        ParamInfo rcMode0 = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_CONTROL_CONTROLMODE_0);
        ParamInfo rcMode1 = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_CONTROL_CONTROLMODE_1);
        ParamInfo rcMode2 = DJIFlycParamInfoManager.read(ParamCfgName.GCONFIG_CONTROL_CONTROLMODE_2);
        this.mTmpRcModeChls[0] = DataOsdGetPushCommon.RcModeChannel.realFind(rcMode0.value.intValue());
        if (DataOsdGetPushCommon.RcModeChannel.CHANNEL_P == this.mTmpRcModeChls[0]) {
            this.mTmpRcModeChls[0] = DataOsdGetPushCommon.RcModeChannel.CHANNEL_F;
        }
        this.mTmpRcModeChls[1] = DataOsdGetPushCommon.RcModeChannel.realFind(rcMode1.value.intValue());
        this.mTmpRcModeChls[2] = DataOsdGetPushCommon.RcModeChannel.realFind(rcMode2.value.intValue());
        if (!(this.mTmpRcModeChls[0] == this.mRcModeChannels[0] && this.mTmpRcModeChls[1] == this.mRcModeChannels[1] && this.mTmpRcModeChls[2] == this.mRcModeChannels[2])) {
            this.mWLocker.lock();
            try {
                this.mRcModeChannels[0] = this.mTmpRcModeChls[0];
                this.mRcModeChannels[1] = this.mTmpRcModeChls[1];
                this.mRcModeChannels[2] = this.mTmpRcModeChls[2];
                DJILogHelper.getInstance().LOGD(TAG, "Param ModeChls[" + this.mRcModeChannels[0] + ";" + this.mRcModeChannels[1] + ";" + this.mRcModeChannels[2] + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
                this.mWLocker.unlock();
                EventBus.getDefault().post(DataOsdGetPushCommon.getInstance());
            } catch (Throwable th) {
                this.mWLocker.unlock();
                throw th;
            }
        }
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4097, 0, 0), 1500);
    }

    /* access modifiers changed from: private */
    public void regetRcModeChannel() {
        if (!DJIFlycParamInfoManager.isInited() || !DataOsdGetPushCommon.getInstance().isGetted()) {
            return;
        }
        if (Integer.MIN_VALUE != this.mFlycVersion && this.mFlycVersion < 14) {
            return;
        }
        if (DJIFlycParamInfoManager.read(CFG_CTRLMODES[CFG_CTRLMODES.length - 1]) != null) {
            this.setter.setInfos(CFG_CTRLMODES).start(new DJIDataCallBack() {
                /* class dji.logic.mc.DJIMcHelper.AnonymousClass2 */

                public void onSuccess(Object model) {
                    DJIMcHelper.this.mHandler.sendMessageDelayed(DJIMcHelper.this.mHandler.obtainMessage(4096, 0, 0), 0);
                }

                public void onFailure(Ccode ccode) {
                    DJIMcHelper.this.mHandler.sendMessageDelayed(DJIMcHelper.this.mHandler.obtainMessage(4096, 1, 0), 200);
                }
            });
        } else {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(4097, 0, 0), 1500);
        }
    }

    private void resetRcModeChannels() {
        this.mWLocker.lock();
        try {
            DataOsdGetPushCommon.RcModeChannel[] chls = DEFAULT_RCMODECHLS_SPORT;
            if (isOldProduct(DJIProductManager.getInstance().getType())) {
                chls = DEFAULT_RCMODECHLS;
            }
            int length = chls.length;
            for (int i = 0; i < length; i++) {
                this.mRcModeChannels[i] = chls[i];
            }
        } finally {
            this.mWLocker.unlock();
        }
    }

    private boolean isOldProduct(ProductType productType) {
        if (productType == ProductType.litchiX || productType == ProductType.litchiS || productType == ProductType.Orange || productType == ProductType.PM820) {
            return true;
        }
        return false;
    }

    private DJIMcHelper() {
    }

    private void init() {
        DJIEventBusUtil.register(this);
        resetRcModeChannels();
        if (DataOsdGetPushCommon.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushCommon.getInstance());
        }
        if (ServiceManager.getInstance().isRemoteOK()) {
            onEvent3BackgroundThread(DataCameraEvent.ConnectOK);
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static DJIMcHelper mInstance = null;

        private SingletonHolder() {
        }
    }
}
