package dji.midware.data.manager.P3;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBaseProcessor;
import dji.midware.data.model.P3.DataAppOperation;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.RecvPack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.BytesUtil;
import dji.publics.DJIExecutor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public abstract class DataBase {
    protected static final int DELAY_PUSH_LOSE = 4000;
    protected static final int OVERALL_MSG = 255;
    private static final String TAG = "DataBase";
    private static ExecutorService cachedThreadPool = DJIExecutor.getExecutorFor(DJIExecutor.Purpose.URGENT);
    protected static Handler handler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
        /* class dji.midware.data.manager.P3.DataBase.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 255:
                    ((DataBase) msg.obj).setPushLose();
                    return false;
                default:
                    ((DataBase) msg.obj).setPushLose(msg.what);
                    return false;
            }
        }
    });
    private static HashMap<Integer, ArrayList<Integer>> mytestlist = new HashMap<>();
    private static DataBaseProcessor processor = DataBaseProcessor.getInstance();
    protected byte[] _recData;
    protected byte[] _sendData;
    public Ccode ccode;
    private DATA_TYPE dataType;
    private DJIEncryManager encryManager;
    private Future<?> future;
    protected boolean isNeedPushLosed;
    protected boolean isPushLosed;
    protected SparseArray<Boolean> isPushLosedList;
    protected boolean isRegist;
    protected boolean isRemoteModel;
    protected Pack pack;
    private DataBaseProcessor.ProcessCallback processCallback;
    protected SparseArray<byte[]> recDatas;
    protected int receiverID;
    private int recordType;
    protected RecvPack recvPack;
    protected int senderID;
    private boolean testDump;

    protected enum DATA_TYPE {
        PUSH,
        LOCAL,
        REMOTE
    }

    /* access modifiers changed from: protected */
    public abstract void doPack();

    public DataBase() {
        this(true);
    }

    public DataBase(boolean isRegist2) {
        this.recDatas = new SparseArray<>(2);
        this.dataType = DATA_TYPE.REMOTE;
        this.isRegist = true;
        this.isPushLosed = true;
        this.isPushLosedList = new SparseArray<>(2);
        this.isNeedPushLosed = false;
        this.isRemoteModel = false;
        this.receiverID = -1;
        this.senderID = -1;
        this.recordType = -1;
        this.ccode = Ccode.OK;
        this.processCallback = new DataBaseProcessor.ProcessCallback() {
            /* class dji.midware.data.manager.P3.DataBase.AnonymousClass2 */

            public void onResult(@NonNull SendPack sendPack, DJIDataCallBack callBack, RecvPack recvPack, boolean notifyInMainThread) {
                if (recvPack != null) {
                    sendPack.bufferObject.willRepeat(false);
                    sendPack.bufferObject.noUsed();
                    DataBase.this.sendPackResult(sendPack, callBack, recvPack, notifyInMainThread);
                } else if (sendPack != null) {
                    sendPack.bufferObject.willRepeat(false);
                    sendPack.bufferObject.noUsed();
                    RecvPack recvPack2 = RecvPack.obtain(null);
                    recvPack2.ccode = Ccode.TIMEOUT.value();
                    DataBase.this.sendPackResult(sendPack, callBack, recvPack2, notifyInMainThread);
                }
            }
        };
        this.testDump = false;
        this.dataType = getDataType();
        this.encryManager = DJIEncryManager.getInstance();
        this.isRegist = isRegist2;
        if (isRegist2) {
            DataBaseCenter.getInstance().addDataBase(this);
        }
    }

    /* access modifiers changed from: protected */
    public DATA_TYPE getDataType() {
        return this.dataType;
    }

    public void clear() {
        this._recData = null;
        if (this.recDatas != null) {
            this.recDatas.clear();
        }
        cancelFuture();
    }

    public void cancel() {
        if (cancelFuture()) {
        }
    }

    public int getRecDataLen() {
        if (this._recData != null) {
            return this._recData.length;
        }
        return 0;
    }

    public byte[] getRecData() {
        return this._recData;
    }

    public void setRecData(byte[] data) {
        this._recData = data;
    }

    private synchronized void setRecvPack(RecvPack recvPack2) {
        if (this.recvPack != null) {
            this.recvPack.recycle();
            this.recvPack = null;
        }
        this.recvPack = recvPack2;
    }

    public void onRecvPackSeted(RecvPack recvPack2) {
    }

    public void outerSetPushRecPack(Pack pack2) {
        setPushRecPack(pack2);
    }

    /* access modifiers changed from: protected */
    public int getPushLoseDelayTime() {
        return DELAY_PUSH_LOSE;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack2) {
        boolean needPostPushLoseChanged = false;
        if (this.isNeedPushLosed) {
            this.isPushLosed = false;
            if (this.isPushLosedList != null) {
                Boolean value = this.isPushLosedList.get(pack2.senderId, null);
                this.isPushLosedList.put(pack2.senderId, Boolean.FALSE);
                if (value == null || value.booleanValue()) {
                    needPostPushLoseChanged = true;
                }
            }
            handler.removeMessages(255, this);
            handler.sendMessageDelayed(handler.obtainMessage(255, this), (long) getPushLoseDelayTime());
            handler.removeMessages(pack2.senderId, this);
            handler.sendMessageDelayed(handler.obtainMessage(pack2.senderId, this), (long) getPushLoseDelayTime());
        }
        RecvPack lastPack = null;
        if (this.pack != null && (this.pack instanceof RecvPack)) {
            lastPack = (RecvPack) this.pack;
        }
        this.pack = pack2;
        if (lastPack != null) {
            lastPack.recycle();
        }
        setPushRecData(pack2.data);
        if (needPostPushLoseChanged) {
            notifyPushLoseIndexChanged(true);
        }
    }

    /* access modifiers changed from: protected */
    public void setPushLose() {
        this.isPushLosed = true;
        if (!this.isRegist || !ServiceManager.getInstance().isConnected() || !this.isRemoteModel || !this.isRemoteModel || ServiceManager.getInstance().isRemoteOK()) {
        }
    }

    /* access modifiers changed from: protected */
    public void setPushLose(int index) {
        if (this.isPushLosedList != null) {
            Boolean value = this.isPushLosedList.get(this.pack.senderId, null);
            this.isPushLosedList.put(index, Boolean.TRUE);
            if (value == null || !value.booleanValue()) {
                notifyPushLoseIndexChanged(false);
            }
        }
    }

    public int getDataNumber() {
        int number = 0;
        if (this.isPushLosedList != null) {
            synchronized (this.isPushLosedList) {
                int size = this.isPushLosedList.size();
                for (int i = 0; i < size; i++) {
                    Boolean value = this.isPushLosedList.valueAt(i);
                    if (value != null && !value.booleanValue()) {
                        number++;
                    }
                }
            }
            return number;
        } else if (this.isPushLosed) {
            return 0;
        } else {
            return 1;
        }
    }

    /* access modifiers changed from: protected */
    public void notifyPushLoseIndexChanged(boolean fromGetPush) {
    }

    /* access modifiers changed from: protected */
    public void post() {
        if (ServiceManager.isAlive()) {
            EventBus.getDefault().post(this);
        }
    }

    public boolean isPushLosed() {
        return this.isPushLosed;
    }

    public boolean isPushLosed(int receiverID2) {
        if (this.isPushLosedList == null || this.isPushLosedList.get(receiverID2) == null) {
            return this.isPushLosed;
        }
        return this.isPushLosedList.get(receiverID2).booleanValue();
    }

    /* access modifiers changed from: protected */
    public boolean isWantPush() {
        return true;
    }

    private boolean isNeedPushAll() {
        return this.pack != null && ((this.pack.cmdId == CmdIdCommon.CmdIdType.GetPushUpgradeStatus.value() && this.pack.cmdSet == CmdSet.COMMON.value()) || (this.pack.cmdId == CmdIdCommon.CmdIdType.TransferFile.value() && this.pack.cmdSet == CmdSet.COMMON.value()));
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        if (isNeedPushAll()) {
            return true;
        }
        if (this.pack != null) {
            byte[] oldData = this.recDatas.get(this.pack.senderId);
            if (oldData == null || !Arrays.equals(oldData, data)) {
                return true;
            }
            return false;
        } else if (Arrays.equals(this._recData, data)) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        boolean ischanged = isChanged(data);
        if (this.pack != null) {
            this.recDatas.put(this.pack.senderId, data);
        }
        setRecData(data);
        if (ischanged && isWantPush() && this.isRegist) {
            post();
        }
    }

    public boolean isGetted() {
        return this._recData != null;
    }

    public boolean isGetted(int index) {
        if (index <= -1 || this.recDatas == null || this.recDatas.get(index) == null) {
            return false;
        }
        return true;
    }

    public void swapValidData(int index) {
        if ((this.pack == null || this.pack.senderId != index) && isGetted(index)) {
            this._recData = this.recDatas.get(index);
            if (this.pack != null) {
                this.pack.senderId = index;
            }
        }
    }

    public boolean isGettedPack() {
        return this.pack != null;
    }

    /* access modifiers changed from: protected */
    public byte[] getSendData() {
        doPack();
        return this._sendData;
    }

    public <T extends Number> T get(int from, int length, Class cls) {
        return get(from, length, cls, -1);
    }

    public <T extends Number> T get(int from, int length, Class<T> cls, int index) {
        byte[] originRecData = this._recData;
        if (index >= 0 && this.recDatas.size() > 0 && this.recDatas.get(index) != null) {
            originRecData = this.recDatas.get(index);
        }
        if (cls == Short.class) {
            return Short.valueOf(BytesUtil.getShort(originRecData, from, length));
        }
        if (cls == Integer.class) {
            return Integer.valueOf(BytesUtil.getInt(originRecData, from, length));
        }
        if (cls == Long.class) {
            return Long.valueOf(BytesUtil.getLong(originRecData, from, length));
        }
        if (cls == Float.class) {
            return Float.valueOf(BytesUtil.getFloat(originRecData, from, length));
        }
        if (cls == Double.class) {
            return Double.valueOf(BytesUtil.getDouble(originRecData, from, length));
        }
        if (cls == BigInteger.class) {
            return Integer.valueOf(BytesUtil.getInt(originRecData, from, length));
        }
        if (cls == Byte.class) {
            return Byte.valueOf(BytesUtil.getByte(originRecData, from, length));
        }
        return null;
    }

    @Deprecated
    public <T extends Number> T getByCopy(int from, int length, Class<T> cls, int index) {
        byte[] data;
        byte[] originRecData = this._recData;
        if (index >= 0 && this.recDatas.size() > 0 && this.recDatas.get(index) != null) {
            originRecData = this.recDatas.get(index);
        }
        if (originRecData == null) {
            data = new byte[length];
        } else if (originRecData.length < from + length) {
            data = new byte[length];
            if (originRecData.length > from) {
                System.arraycopy(originRecData, from, data, 0, originRecData.length - from);
            }
        } else {
            data = BytesUtil.readBytes(originRecData, from, length);
        }
        if (cls == Short.class) {
            return Short.valueOf(BytesUtil.getShort(data));
        }
        if (cls == Integer.class) {
            return Integer.valueOf(BytesUtil.getInt(data));
        }
        if (cls == Long.class) {
            return Long.valueOf(BytesUtil.getLong(data));
        }
        if (cls == Float.class) {
            return Float.valueOf(BytesUtil.getFloat(data));
        }
        if (cls == Double.class) {
            return Double.valueOf(BytesUtil.getDouble(data));
        }
        if (cls == BigInteger.class) {
            return Integer.valueOf(BytesUtil.getInt(data));
        }
        if (cls == Byte.class) {
            return Byte.valueOf((byte) BytesUtil.getSignedInt(data[0]));
        }
        return null;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public byte[] getRecData(int from, int length, int index) {
        byte[] originRecData = this._recData;
        if (index >= 0 && this.recDatas.size() > 0 && this.recDatas.get(index) != null) {
            originRecData = this.recDatas.get(index);
        }
        int bytesLen = originRecData.length;
        if (originRecData == null || bytesLen == 0 || from < 0 || bytesLen <= from) {
            return null;
        }
        if (length > bytesLen - from) {
            length = bytesLen - from;
        }
        if (length > 0) {
            return BytesUtil.readBytes(originRecData, from, length);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public String get(int from, int length) {
        return get(from, length, 0);
    }

    /* access modifiers changed from: protected */
    public String get(int from, int length, int index) {
        byte[] originRecData = this._recData;
        if (this.recDatas.size() > 0 && this.recDatas.get(index) != null) {
            originRecData = this.recDatas.get(index);
        }
        if (originRecData == null) {
            return "";
        }
        return BytesUtil.getString(BytesUtil.readBytes(originRecData, from, length));
    }

    /* access modifiers changed from: protected */
    public String getUTF8(int from, int length) {
        return getUTF8(from, length, 0);
    }

    /* access modifiers changed from: protected */
    public String getUTF8(int from, int length, int index) {
        byte[] originRecData = this._recData;
        if (this.recDatas.size() > 0 && this.recDatas.get(index) != null) {
            originRecData = this.recDatas.get(index);
        }
        if (originRecData == null) {
            return "";
        }
        return BytesUtil.getStringUTF8(BytesUtil.readBytes(originRecData, from, length));
    }

    /* access modifiers changed from: protected */
    public void start(SendPack pack2, DJIDataCallBack callBack) {
        start(pack2, callBack, false);
    }

    /* access modifiers changed from: protected */
    public void start(SendPack pack2, DJIDataCallBack callBack, boolean notifyInMainThread) {
        if (!this.testDump || pack2 == null) {
            if (this.receiverID != -1) {
                pack2.receiverId = this.receiverID;
            }
            pack2.data = getSendData();
            pack2.doPack();
            recordSend(pack2);
            if (!this.encryManager.isOldFirmware() && pack2.cmdSet != CmdSet.COMMON.value() && this.encryManager.isNeedEncrypt(pack2.buffer)) {
                pack2.buffer = this.encryManager.simpleEncrypt(pack2.buffer, pack2.seq);
                pack2.encryptType = DataConfig.EncryptType.SIMPLE.value();
                this.encryManager.encryptData(pack2.buffer);
                pack2.reCrc();
            }
            try {
                submitTask(pack2, callBack, notifyInMainThread, true);
            } catch (NullPointerException | RejectedExecutionException e) {
                DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
            }
        } else {
            collectpackInfo(pack2);
        }
    }

    private void submitTask(SendPack pack2, DJIDataCallBack callBack, boolean notifyInMainThread, boolean hasAck) {
        if (this.dataType == DATA_TYPE.LOCAL && this._recData != null) {
            callbackSuccess(callBack, notifyInMainThread);
        }
        if (pack2.repeatTimes == 2 && pack2.timeOut == 1000 && pack2.cmdSet == CmdSet.FLYC.value()) {
            pack2.repeatTimes = 3;
        }
        processor.sendCmd(pack2, callBack, this.processCallback, notifyInMainThread, hasAck, false);
    }

    /* access modifiers changed from: private */
    public void sendPackResult(SendPack pack2, DJIDataCallBack callBack, RecvPack recvPack2, boolean notifyInMainThread) {
        setRecvPack(recvPack2);
        final RecvPack recvPack3 = recvPack2;
        final SendPack sendPack = pack2;
        final DJIDataCallBack dJIDataCallBack = callBack;
        final boolean z = notifyInMainThread;
        this.future = cachedThreadPool.submit(new Callable<Integer>() {
            /* class dji.midware.data.manager.P3.DataBase.AnonymousClass3 */

            public Integer call() {
                if (recvPack3.isNeedCcode) {
                    if (Ccode.OK._equals(recvPack3.ccode) || (sendPack.cmdSet == CmdSet.CAMERA.value() && Ccode.SUCCEED._equals(recvPack3.ccode))) {
                        DataBase.this.setRecData(recvPack3.data);
                        DataBase.this.onRecvPackSeted(recvPack3);
                        DataBase.this.callbackSuccess(dJIDataCallBack, z);
                        DataBase.this.recordAckSuccess(sendPack);
                    } else {
                        DataBase.this.setRecData(recvPack3.data);
                        DataBase.this.onRecvPackSeted(recvPack3);
                        DataBase.this.callBackFailure(dJIDataCallBack, Ccode.find(recvPack3.ccode), z);
                        DataBase.this.recordAckFailed(sendPack);
                    }
                } else if (recvPack3.data != null) {
                    DataBase.this.setRecData(recvPack3.data);
                    DataBase.this.onRecvPackSeted(recvPack3);
                    DataBase.this.callbackSuccess(dJIDataCallBack, z);
                    DataBase.this.recordAckSuccess(sendPack);
                } else {
                    DataBase.this.callBackFailure(dJIDataCallBack, Ccode.INVALID_PARAM, z);
                    DataBase.this.recordAckFailed(sendPack);
                }
                return 1;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void syncStart(SendPack pack2) {
        final AtomicBoolean isReturn = new AtomicBoolean(false);
        final CountDownLatch latch = new CountDownLatch(1);
        start(pack2, new DJIDataCallBack() {
            /* class dji.midware.data.manager.P3.DataBase.AnonymousClass4 */

            public void onSuccess(Object model) {
                isReturn.set(true);
                latch.countDown();
            }

            public void onFailure(Ccode ccode) {
                DataBase.this.ccode = ccode;
                isReturn.set(true);
                latch.countDown();
            }
        });
        if (!isReturn.get()) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void join() {
        if (this.future != null) {
            try {
                this.future.get();
            } catch (Exception e) {
                DJILog.exceptionToString(e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void callbackSuccess(final DJIDataCallBack callBack, boolean notifyInMainThread) {
        if (callBack == null) {
            return;
        }
        if (!notifyInMainThread) {
            callBack.onSuccess(this);
        } else if (handler != null && callBack != null) {
            handler.post(new Runnable() {
                /* class dji.midware.data.manager.P3.DataBase.AnonymousClass5 */

                public void run() {
                    callBack.onSuccess(DataBase.this);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void callBackFailure(final DJIDataCallBack callBack, Ccode code, boolean notifyInMainThread) {
        if (callBack == null) {
            return;
        }
        if (!notifyInMainThread) {
            callBack.onFailure(code);
        } else if (handler != null) {
            handler.post(new Runnable() {
                /* class dji.midware.data.manager.P3.DataBase.AnonymousClass6 */

                public void run() {
                    callBack.onSuccess(DataBase.this);
                }
            });
        }
    }

    public void interrupt() {
        try {
            cancelFuture();
        } catch (Exception e) {
        }
    }

    private boolean cancelFuture() {
        if (this.future == null || this.future.isCancelled() || this.future.isDone()) {
            return false;
        }
        this.future.cancel(true);
        this.future = null;
        return true;
    }

    public static void dumpPackInfo() {
        String dumString = "";
        if (mytestlist.size() > 0) {
            for (Integer index : mytestlist.keySet()) {
                String dumString2 = (((dumString + "cmdset=0x") + BytesUtil.byte2hex(index.intValue())) + "\n") + "cmdid=";
                Iterator it2 = mytestlist.get(index).iterator();
                while (it2.hasNext()) {
                    dumString2 = ((dumString2 + "0x") + BytesUtil.byte2hex(((Integer) it2.next()).intValue())) + ", ";
                }
                dumString = dumString2 + "\n";
            }
            Log.e("", dumString);
        }
    }

    private static void collectpackInfo(SendPack pack2) {
        ArrayList<Integer> list;
        if (pack2.senderType == DeviceType.APP.value()) {
            if (mytestlist.containsKey(Integer.valueOf(pack2.cmdSet))) {
                list = mytestlist.get(Integer.valueOf(pack2.cmdSet));
            } else {
                list = new ArrayList<>();
            }
            if (!list.contains(Integer.valueOf(pack2.cmdId))) {
                list.add(Integer.valueOf(pack2.cmdId));
                mytestlist.put(Integer.valueOf(pack2.cmdSet), list);
            }
        }
    }

    public DataBase setReceiverId(int id) {
        this.receiverID = id;
        return this;
    }

    public <T> T setReceiverId(int id, Class<T> cls) {
        this.receiverID = id;
        return this;
    }

    /* access modifiers changed from: protected */
    public void start(SendPack pack2) {
        if (!this.testDump || pack2 == null) {
            submitTask(preprocessPack(pack2), null, false, false);
        } else {
            collectpackInfo(pack2);
        }
    }

    /* access modifiers changed from: protected */
    public int startWithSpecialChannel(SendPack pack2) {
        return processor.sendCmd(preprocessPack(pack2), null, this.processCallback, false, false, true);
    }

    private SendPack preprocessPack(SendPack pack2) {
        if (this.receiverID != -1) {
            pack2.receiverId = this.receiverID;
        }
        pack2.data = getSendData();
        pack2.doPack();
        if (!this.encryManager.isOldFirmware() && this.encryManager.isNeedEncrypt(pack2.buffer)) {
            pack2.buffer = this.encryManager.simpleEncrypt(pack2.buffer, pack2.seq);
            pack2.encryptType = DataConfig.EncryptType.SIMPLE.value();
            this.encryManager.encryptData(pack2.buffer);
            pack2.reCrc();
        }
        return pack2;
    }

    public void postMockPush(byte[] mockData, int pos, int length) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        DataBase data;
        int i;
        if (mockData != null && pos >= 0 && length > 0) {
            Class clz = getClass();
            Constructor constructorWithParam = null;
            Constructor constructorWithNoParam = null;
            try {
                constructorWithParam = clz.getConstructor(Boolean.TYPE);
            } catch (NoSuchMethodException e) {
                try {
                    constructorWithNoParam = clz.getConstructor(new Class[0]);
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                }
            }
            if (constructorWithParam != null) {
                data = (DataBase) constructorWithParam.newInstance(false);
            } else if (constructorWithNoParam != null) {
                data = (DataBase) constructorWithNoParam.newInstance(new Object[0]);
            } else {
                return;
            }
            byte[] origin = this._recData;
            if (origin == null || origin.length < pos + length) {
                i = pos + length;
            } else {
                i = this._recData.length;
            }
            byte[] replace = new byte[i];
            if (origin != null) {
                System.arraycopy(origin, 0, replace, 0, origin.length);
            }
            System.arraycopy(mockData, 0, replace, pos, length);
            data._recData = replace;
            DJILogHelper.getInstance().LOGD("", "DiagnosticsTest: \n" + byteArrayToStr(data._recData));
            EventBus.getDefault().post(data);
        }
    }

    private String byteArrayToStr(byte[] byteArr) {
        if (byteArr == null) {
            return null;
        }
        if (byteArr.length == 0) {
            return "Null";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteArr.length; i++) {
            sb.append(i).append(": ").append(Integer.toHexString(byteArr[i] & 255)).append("\n");
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public boolean needRecord(SendPack pack2) {
        if (pack2 == null || pack2.cmdSet != CmdSet.FLYC.value()) {
            return false;
        }
        if (pack2.cmdId == CmdIdFlyc.CmdIdType.FunctionControl.value()) {
            this.recordType = 0;
            return true;
        } else if (pack2.cmdId != CmdIdFlyc.CmdIdType.SetHomePoint.value()) {
            return false;
        } else {
            this.recordType = 1;
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void recordSend(SendPack pack2) {
        if (needRecord(pack2) && pack2.data != null && pack2.data.length >= 1) {
            DataAppOperation appOperation = new DataAppOperation(false);
            byte b = pack2.data[0];
            if (this.recordType == 0) {
                appOperation.setData(b, DataAppOperation.APP_OPERATION_STATE.SEND);
            } else if (this.recordType == 1) {
                appOperation.setData(b, DataAppOperation.APP_OPERATION_STATE.SEND, true);
            }
            EventBus.getDefault().post(appOperation);
        }
    }

    /* access modifiers changed from: protected */
    public void recordAckSuccess(SendPack pack2) {
        if (needRecord(pack2) && pack2.data != null && pack2.data.length >= 1) {
            DataAppOperation appOperation = new DataAppOperation(false);
            byte b = pack2.data[0];
            if (this.recordType == 0) {
                appOperation.setData(b, DataAppOperation.APP_OPERATION_STATE.ACK_SUCCESS);
            } else if (this.recordType == 1) {
                appOperation.setData(b, DataAppOperation.APP_OPERATION_STATE.ACK_SUCCESS, true);
            }
            EventBus.getDefault().post(appOperation);
        }
    }

    /* access modifiers changed from: protected */
    public void recordAckFailed(SendPack pack2) {
        if (needRecord(pack2) && pack2.data != null && pack2.data.length >= 1) {
            DataAppOperation appOperation = new DataAppOperation(false);
            byte b = pack2.data[0];
            if (this.recordType == 0) {
                appOperation.setData(b, DataAppOperation.APP_OPERATION_STATE.ACK_FAILED);
            } else if (this.recordType == 1) {
                appOperation.setData(b, DataAppOperation.APP_OPERATION_STATE.ACK_FAILED, true);
            }
            EventBus.getDefault().post(appOperation);
        }
    }

    public int getSenderId() {
        if (isGettedPack()) {
            return this.pack.senderId;
        }
        return 0;
    }

    public void setNeedPushLosed(boolean needPushLosed) {
        this.isNeedPushLosed = needPushLosed;
        handler.removeCallbacksAndMessages(null);
    }

    public Pack getPack() {
        return this.pack;
    }
}
