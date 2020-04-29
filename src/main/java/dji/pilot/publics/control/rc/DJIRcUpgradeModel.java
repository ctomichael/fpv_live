package dji.pilot.publics.control.rc;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCommonGetDeviceStatus;
import dji.midware.data.model.P3.DataCommonRequestReceiveData;
import dji.midware.data.model.P3.DataCommonRequestUpgrade;
import dji.midware.data.model.P3.DataCommonRestartDevice;
import dji.midware.data.model.P3.DataCommonTranslateComplete;
import dji.midware.data.model.P3.DataCommonTranslateData;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import dji.pilot.publics.R;
import dji.pilot.publics.control.rc.DJIRcPackageParser;
import dji.pilot.publics.model.DJIUpgradeCode;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.util.Arrays;

@EXClassNullAway
public class DJIRcUpgradeModel implements DJIUpgradeCode {
    private static final int CODE_FAIL = 1;
    private static final int CODE_SUCCESS = 0;
    private static final boolean DELAY_BY_TRANSLATEDATA = true;
    private static final long DELAY_FAIL = 50;
    private static final long DELAY_FAKE_UPDATE = 1000;
    private static final long DELAY_RECHECK_STATUS = 2200;
    private static final long DELAY_REQUEST_RECEIVE_DATA = 130000;
    private static final long DELAY_TRANSLATEDATA = 50;
    private static final long DELAY_WAIT_CONN = 6000;
    private static final int LENGTH_BUFFER = 2048;
    private static final int MGG_ID_RESTART_MODEL = 262;
    private static final int MSG_ID_CHECK_STATUS = 257;
    private static final int MSG_ID_CHECK_STATUS_PREV = 266;
    private static final int MSG_ID_DELAY_TRANSLATEDATA = 512;
    private static final int MSG_ID_FAKE_UPDATE = 768;
    private static final int MSG_ID_FIRSTDATA_DELAY = 263;
    private static final int MSG_ID_RECHECK_STATUS = 264;
    private static final int MSG_ID_REQUEST_RECEIVEDATA = 258;
    private static final int MSG_ID_REQUEST_UPGRADE = 256;
    private static final int MSG_ID_RESTART_MODEL_PREV = 265;
    private static final int MSG_ID_TRANSLATE_COMPLETE = 261;
    private static final int MSG_ID_TRANSLATE_DATA = 260;
    private static final int MSG_ID_TRANSLATE_DATA_DELAY = 259;
    private static final int RETRY_TIMES = 5;
    /* access modifiers changed from: private */
    public static final String TAG = DJIRcUpgradeModel.class.getSimpleName();
    private long mCurrentLength = 0;
    private int mDelayPackLength = 0;
    private DeviceType mDeviceType = null;
    private DJIRcPackageParser.FirmwareInfo mFirmwareInfo = null;
    private boolean mFirstDelay = false;
    /* access modifiers changed from: private */
    public ModelHandler mHandler = null;
    private OnModelUpgradeListener mListener = null;
    private RandomAccessFile mPackageRAF = null;
    /* access modifiers changed from: private */
    public int mReceiveDataLength = 0;
    private boolean mRestart = false;
    private TranslateDataInfo mTranslateDataInfo = null;
    /* access modifiers changed from: private */
    public volatile boolean mWaitForConn = false;
    /* access modifiers changed from: private */
    public boolean mbDisconnected = false;
    final DataCommonTranslateData translater = new DataCommonTranslateData();

    interface OnModelUpgradeListener {
        void onFail(DJIRcUpgradeModel dJIRcUpgradeModel, Ccode ccode, int i, int i2);

        void onStart(DJIRcUpgradeModel dJIRcUpgradeModel, int i);

        void onSuccess(DJIRcUpgradeModel dJIRcUpgradeModel, int i);

        void onUpdate(DJIRcUpgradeModel dJIRcUpgradeModel, int i, int i2);
    }

    public DJIRcUpgradeModel(DeviceType type, DJIRcPackageParser.FirmwareInfo firmware, RandomAccessFile raf, OnModelUpgradeListener listener) {
        this.mDeviceType = type;
        this.mFirmwareInfo = firmware;
        this.mListener = listener;
        this.mPackageRAF = raf;
        try {
            this.mPackageRAF.seek(0);
        } catch (Exception e) {
        }
        this.mHandler = new ModelHandler();
        if (this.mDeviceType == DeviceType.FPGA_G) {
            this.mFirstDelay = true;
            this.mDelayPackLength = (int) (((((long) firmware.mDataLength) * 1000) / 3) / DELAY_REQUEST_RECEIVE_DATA);
        }
    }

    public void setWaitForConn(boolean wait) {
        this.mWaitForConn = wait;
    }

    public void setNeedRestart(boolean restart) {
        this.mRestart = restart;
    }

    public DeviceType getDeviceType() {
        return this.mDeviceType;
    }

    public void startUpgrade() {
        DJILogHelper.getInstance().LOGD(TAG, "startUpgrade", false, true);
        notifyStart(0);
        if (this.mRestart) {
            restartModel(265, 0, Ccode.UNDEFINED);
        } else {
            requestEnterUpgradeMode(0, Ccode.UNDEFINED);
        }
    }

    public void disconnect() {
        this.mbDisconnected = true;
    }

    public void clear() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    private void notifyFail(Ccode errCode, int err, int errResId) {
        if (this.mListener != null) {
            this.mListener.onFail(this, errCode, err, errResId);
        }
    }

    /* access modifiers changed from: private */
    public void notifySuccess(int arg) {
        if (this.mListener != null) {
            this.mListener.onSuccess(this, arg);
        }
    }

    private void notifyStart(int arg) {
        if (this.mListener != null) {
            this.mListener.onStart(this, arg);
        }
    }

    private void notifyUpdate(int progress, int count) {
        if (this.mListener != null) {
            this.mListener.onUpdate(this, progress, count);
        }
    }

    /* access modifiers changed from: private */
    public void requestEnterUpgradeMode(final int retryTimes, Ccode code) {
        DJILogHelper.getInstance().LOGD(TAG, "requestEnterUpgradeMode[" + retryTimes + "]c[" + code + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (retryTimes < 5) {
            new DataCommonRequestUpgrade().setReceiveType(this.mDeviceType).setReceiveId(0).start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeModel.AnonymousClass1 */

                public void onSuccess(Object model) {
                    DJIRcUpgradeModel.this.mHandler.obtainMessage(256, 0, retryTimes).sendToTarget();
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(256, 1, retryTimes, ccode), 50);
                }
            });
        } else {
            notifyFail(code, 101, R.string.checklist_rc_upgrade_fail_request_upgrade_mode);
        }
    }

    /* access modifiers changed from: private */
    public void checkModelStatus(final int msgId, final int retryTimes, Ccode code) {
        DJILogHelper.getInstance().LOGD(TAG, "checkModelStatus id[" + msgId + "]times[" + retryTimes + "]c[" + code + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (retryTimes < 5) {
            final DataCommonGetDeviceStatus getter = new DataCommonGetDeviceStatus();
            getter.setReceiveType(this.mDeviceType).setReceiveId(0).setVersioin(0, 0).start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeModel.AnonymousClass2 */

                public void onSuccess(Object model) {
                    DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(msgId, 0, getter.getMode()), 50);
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(msgId, 1, retryTimes, ccode), 50);
                }
            });
            return;
        }
        notifyFail(code, 102, R.string.rcupgrade_fail_check_status);
    }

    /* access modifiers changed from: private */
    public void requestReceiveData(final int retryTimes, Ccode code) {
        DJILogHelper.getInstance().LOGD(TAG, "requestReceiveData[" + retryTimes + "]c[" + code + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (retryTimes < 5) {
            final DataCommonRequestReceiveData request = new DataCommonRequestReceiveData();
            request.setReceiveType(this.mDeviceType).setReceiveId(0).setDataLength((long) this.mFirmwareInfo.mDataLength).start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeModel.AnonymousClass3 */

                public void onSuccess(Object model) {
                    DJIRcUpgradeModel.this.mHandler.obtainMessage(258, 0, request.getReceiveDataLength()).sendToTarget();
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(258, 1, retryTimes, ccode), 50);
                }
            });
            return;
        }
        notifyFail(code, 103, R.string.rcupgrade_fail_receive_data);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
        return 0;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public short calFpgaCheckSum() {
        /*
            r13 = this;
            r8 = 2048(0x800, float:2.87E-42)
            r1 = 0
            r9 = 2048(0x800, float:2.87E-42)
            byte[] r0 = new byte[r9]     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.io.RandomAccessFile r9 = r13.mPackageRAF     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            dji.pilot.publics.control.rc.DJIRcPackageParser$FirmwareInfo r10 = r13.mFirmwareInfo     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            int r10 = r10.mFileOffset     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            long r10 = (long) r10     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r9.seek(r10)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            dji.pilot.publics.control.rc.DJIRcPackageParser$FirmwareInfo r9 = r13.mFirmwareInfo     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            int r7 = r9.mDataLength     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r6 = 0
            if (r7 <= r8) goto L_0x0033
            r2 = r8
        L_0x0019:
            r4 = 0
        L_0x001b:
            if (r7 <= 0) goto L_0x003c
            java.io.RandomAccessFile r9 = r13.mPackageRAF     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r10 = 0
            int r6 = r9.read(r0, r10, r2)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r9 = -1
            if (r6 == r9) goto L_0x003c
            r3 = 0
        L_0x0028:
            if (r3 >= r6) goto L_0x0035
            byte r9 = r0[r3]     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r9 = r9 & 255(0xff, float:3.57E-43)
            long r10 = (long) r9     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            long r4 = r4 + r10
            int r3 = r3 + 1
            goto L_0x0028
        L_0x0033:
            r2 = r7
            goto L_0x0019
        L_0x0035:
            int r7 = r7 - r6
            if (r7 <= r8) goto L_0x003a
            r2 = r8
        L_0x0039:
            goto L_0x001b
        L_0x003a:
            r2 = r7
            goto L_0x0039
        L_0x003c:
            int r8 = (int) r4     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            short r1 = (short) r8     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            dji.log.DJILogHelper r8 = dji.log.DJILogHelper.getInstance()     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r9 = dji.pilot.publics.control.rc.DJIRcUpgradeModel.TAG     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r10.<init>()     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r11 = "checkSum["
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.StringBuilder r10 = r10.append(r1)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r11 = "]sumHex["
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            byte[] r11 = dji.midware.util.BytesUtil.getBytes(r1)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r11 = dji.midware.util.BytesUtil.byte2hex(r11)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r11 = "]"
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
            r11 = 0
            r12 = 1
            r8.LOGD(r9, r10, r11, r12)     // Catch:{ Exception -> 0x007a, all -> 0x0078 }
        L_0x0077:
            return r1
        L_0x0078:
            r8 = move-exception
            throw r8
        L_0x007a:
            r8 = move-exception
            goto L_0x0077
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.control.rc.DJIRcUpgradeModel.calFpgaCheckSum():short");
    }

    private void getDataBuf() {
        Arrays.fill(this.mTranslateDataInfo.mDataBuf, (byte) 0);
        int offset = this.mTranslateDataInfo.mSequence * this.mReceiveDataLength;
        int readCount = this.mReceiveDataLength;
        if (this.mReceiveDataLength + offset >= this.mFirmwareInfo.mDataLength) {
            readCount = this.mFirmwareInfo.mDataLength - offset;
            this.mTranslateDataInfo.mbLast = true;
        } else {
            this.mTranslateDataInfo.mbLast = false;
        }
        int length = readCount;
        if (this.mDelayPackLength != 0) {
            if (this.mTranslateDataInfo.mbLast) {
                length = (int) (((long) this.mFirmwareInfo.mDataLength) - this.mCurrentLength);
            } else {
                length = (readCount * 2) / 3;
            }
        }
        try {
            this.mPackageRAF.seek((long) (this.mFirmwareInfo.mFileOffset + offset));
            int count = 0;
            do {
                int rLength = this.mPackageRAF.read(this.mTranslateDataInfo.mDataBuf, count, readCount - count);
                if (rLength == -1) {
                    break;
                }
                count += rLength;
            } while (count < readCount);
        } catch (Exception e) {
        }
        this.mCurrentLength += (long) length;
        notifyUpdate(length, this.mFirmwareInfo.mDataLength);
    }

    /* access modifiers changed from: private */
    public void handleRequestReceiveData() {
        if (this.mDeviceType == DeviceType.FPGA_G) {
        }
        this.mHandler.sendEmptyMessageDelayed(512, 50);
    }

    /* access modifiers changed from: private */
    public void handleFakeUpdate() {
        if (ServiceManager.getInstance().isConnected()) {
            this.mCurrentLength += (long) this.mDelayPackLength;
            notifyUpdate(this.mDelayPackLength, this.mFirmwareInfo.mDataLength);
            if (this.mHandler.hasMessages(263)) {
                this.mHandler.sendEmptyMessageDelayed(768, 1000);
                return;
            }
            return;
        }
        notifyFail(Ccode.NOCONNECT, 103, R.string.rcupgrade_fail_receive_data);
    }

    /* access modifiers changed from: private */
    public void handleTranslateData(int arg, int arg2, Ccode code) {
        if (this.mTranslateDataInfo == null) {
            this.mTranslateDataInfo = new TranslateDataInfo();
            this.mTranslateDataInfo.mDataBuf = new byte[this.mReceiveDataLength];
        }
        if (arg != 0) {
            int sequence = arg2 + 1;
            this.mTranslateDataInfo.mErrCode = code;
            if (sequence == this.mTranslateDataInfo.mSequence || !(code == Ccode.FM_NONSEQUENCE || code == Ccode.UNDEFINED)) {
                this.mTranslateDataInfo.mRetryTimes++;
                translateData();
            } else if (this.mReceiveDataLength * sequence < this.mFirmwareInfo.mDataLength) {
                this.mTranslateDataInfo.mRetryTimes = 0;
                this.mTranslateDataInfo.mSequence = sequence;
                getDataBuf();
                translateData();
            } else {
                this.mTranslateDataInfo.mbLast = true;
                if (this.mDeviceType == DeviceType.FPGA_G) {
                    byte[] src = BytesUtil.getBytes(calFpgaCheckSum());
                    Arrays.fill(this.mFirmwareInfo.mMD5, (byte) 0);
                    System.arraycopy(src, 0, this.mFirmwareInfo.mMD5, 0, 2);
                }
                translateComplete(0, Ccode.UNDEFINED);
            }
        } else if (this.mTranslateDataInfo.mbLast) {
            if (this.mDeviceType == DeviceType.FPGA_G) {
                byte[] src2 = BytesUtil.getBytes(calFpgaCheckSum());
                Arrays.fill(this.mFirmwareInfo.mMD5, (byte) 0);
                System.arraycopy(src2, 0, this.mFirmwareInfo.mMD5, 0, 2);
            }
            translateComplete(0, Ccode.UNDEFINED);
        } else if (!this.mFirstDelay || this.mTranslateDataInfo.mSequence != 0) {
            if (this.mTranslateDataInfo.mSequence == 0) {
                DJILogHelper.getInstance().LOGD(TAG, "translate time start[" + (System.currentTimeMillis() / 1000) + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
            }
            this.mTranslateDataInfo.mRetryTimes = 0;
            this.mTranslateDataInfo.mErrCode = Ccode.TIMEOUT;
            this.mTranslateDataInfo.mSequence++;
            getDataBuf();
            translateData();
        } else {
            this.mFirstDelay = false;
            this.mHandler.sendEmptyMessageDelayed(263, DELAY_REQUEST_RECEIVE_DATA);
            this.mHandler.sendEmptyMessageDelayed(768, 1000);
        }
    }

    private void translateData() {
        if (this.mTranslateDataInfo.mRetryTimes < 5) {
            this.translater.setReceiveType(this.mDeviceType).setReceiveId(0).setSequence(this.mTranslateDataInfo.mSequence).setData(this.mTranslateDataInfo.mDataBuf).start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeModel.AnonymousClass4 */

                public void onSuccess(Object model) {
                    DJIRcUpgradeModel.this.mHandler.obtainMessage(260, 0, 0).sendToTarget();
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(260, 1, DJIRcUpgradeModel.this.translater.getSequence(), ccode), 50);
                }
            });
        } else {
            notifyFail(this.mTranslateDataInfo.mErrCode, 104, R.string.rcupgrade_fail_translate_data);
        }
    }

    /* access modifiers changed from: private */
    public void translateComplete(final int retryTimes, Ccode code) {
        DJILogHelper.getInstance().LOGD(TAG, "translateComplete[" + retryTimes + "]c[" + code + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (retryTimes < 5) {
            new DataCommonTranslateComplete().setReceiveType(this.mDeviceType).setReceiveId(0).setMD5(this.mFirmwareInfo.mMD5).start(new DJIDataCallBack() {
                /* class dji.pilot.publics.control.rc.DJIRcUpgradeModel.AnonymousClass5 */

                public void onSuccess(Object model) {
                    DJIRcUpgradeModel.this.mHandler.obtainMessage(261, 0, 0).sendToTarget();
                }

                public void onFailure(Ccode ccode) {
                    DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(261, 1, retryTimes, ccode), 50);
                }
            });
        } else {
            notifyFail(code, 105, R.string.rcupgrade_fail_translate_complete);
        }
    }

    /* access modifiers changed from: private */
    public void restartModel(final int msgId, final int retryTimes, Ccode code) {
        int delay;
        if (this.mRestart) {
            DJILogHelper.getInstance().LOGD(TAG, "restartModel[" + retryTimes + "]c[" + code + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
            if (retryTimes < 5) {
                DataCommonRestartDevice restart = new DataCommonRestartDevice();
                if (msgId == 265) {
                    delay = 1000;
                } else {
                    delay = 0;
                }
                restart.setReceiveType(this.mDeviceType).setReceiveId(0).setRestartType(0).setDelay(delay).start(new DJIDataCallBack() {
                    /* class dji.pilot.publics.control.rc.DJIRcUpgradeModel.AnonymousClass6 */

                    public void onSuccess(Object model) {
                        DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(msgId, 0, 0), DJIRcUpgradeModel.DELAY_RECHECK_STATUS);
                    }

                    public void onFailure(Ccode ccode) {
                        if (msgId == 265) {
                            DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(msgId, 1, retryTimes, ccode), 50);
                        } else {
                            DJIRcUpgradeModel.this.mHandler.sendMessageDelayed(DJIRcUpgradeModel.this.mHandler.obtainMessage(msgId, 0, 0), DJIRcUpgradeModel.DELAY_RECHECK_STATUS);
                        }
                    }
                });
                return;
            }
            notifyFail(code, 102, R.string.rcupgrade_fail_restart_model);
            return;
        }
        notifySuccess(0);
    }

    private static final class TranslateDataInfo {
        public byte[] mDataBuf;
        public Ccode mErrCode;
        public int mRetryTimes;
        public int mSequence;
        public boolean mbLast;

        private TranslateDataInfo() {
            this.mRetryTimes = 0;
            this.mSequence = -1;
            this.mErrCode = Ccode.TIMEOUT;
            this.mDataBuf = null;
            this.mbLast = false;
        }
    }

    private static final class ModelHandler extends Handler {
        private final WeakReference<DJIRcUpgradeModel> mOutCls;

        private ModelHandler(DJIRcUpgradeModel model) {
            super(Looper.getMainLooper());
            this.mOutCls = new WeakReference<>(model);
        }

        public void handleMessage(Message msg) {
            DJIRcUpgradeModel model = this.mOutCls.get();
            if (model != null && !model.mbDisconnected) {
                if (model.mWaitForConn) {
                    boolean unused = model.mWaitForConn = false;
                    DJILogHelper.getInstance().LOGD(DJIRcUpgradeModel.TAG, "==== Wait For Here ====", false, true);
                    if (msg.arg1 == 1) {
                        msg.arg2 = 0;
                    }
                    sendMessageDelayed(obtainMessage(msg.what, msg.arg1, msg.arg2, msg.obj), DJIRcUpgradeModel.DELAY_WAIT_CONN);
                    return;
                }
                switch (msg.what) {
                    case 256:
                        if (msg.arg1 == 0) {
                            model.checkModelStatus(257, 0, Ccode.UNDEFINED);
                            return;
                        } else {
                            model.requestEnterUpgradeMode(msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        }
                    case 257:
                        if (msg.arg1 != 0) {
                            model.checkModelStatus(257, msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        } else if (msg.arg2 == 1) {
                            model.requestReceiveData(0, Ccode.UNDEFINED);
                            return;
                        } else {
                            model.requestEnterUpgradeMode(0, Ccode.UNDEFINED);
                            return;
                        }
                    case 258:
                        if (msg.arg1 == 0) {
                            int unused2 = model.mReceiveDataLength = msg.arg2;
                            model.handleRequestReceiveData();
                            return;
                        }
                        model.requestReceiveData(msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                        return;
                    case 259:
                        model.handleTranslateData(0, 0, Ccode.UNDEFINED);
                        return;
                    case 260:
                        model.handleTranslateData(msg.arg1, msg.arg2, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                        return;
                    case 261:
                        if (msg.arg1 == 0) {
                            model.restartModel(262, 0, Ccode.UNDEFINED);
                            return;
                        } else {
                            model.translateComplete(msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        }
                    case 262:
                        if (msg.arg1 == 0) {
                            model.checkModelStatus(264, 0, Ccode.UNDEFINED);
                            return;
                        } else {
                            model.restartModel(262, msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        }
                    case 263:
                        removeMessages(768);
                        model.handleTranslateData(0, 0, Ccode.UNDEFINED);
                        return;
                    case 264:
                        if (msg.arg1 != 0) {
                            model.checkModelStatus(264, msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        } else if (msg.arg2 == 0) {
                            model.notifySuccess(2);
                            return;
                        } else {
                            model.checkModelStatus(264, msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        }
                    case 265:
                        if (msg.arg1 == 0) {
                            model.requestEnterUpgradeMode(0, Ccode.UNDEFINED);
                            return;
                        } else {
                            model.restartModel(265, msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        }
                    case 266:
                        if (msg.arg1 == 0) {
                            model.requestEnterUpgradeMode(0, Ccode.UNDEFINED);
                            return;
                        } else {
                            model.checkModelStatus(266, msg.arg2 + 1, msg.obj instanceof Ccode ? (Ccode) msg.obj : Ccode.UNDEFINED);
                            return;
                        }
                    case 512:
                        model.handleTranslateData(0, 0, Ccode.UNDEFINED);
                        return;
                    case 768:
                        model.handleFakeUpdate();
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
