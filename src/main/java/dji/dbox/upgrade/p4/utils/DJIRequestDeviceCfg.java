package dji.dbox.upgrade.p4.utils;

import android.content.Context;
import android.os.Environment;
import com.dji.frame.util.V_FileUtil;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.paser.DJIUpXmlPaser;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataCommonGetCfgFile;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.natives.UpgradeVerify;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@EXClassNullAway
public class DJIRequestDeviceCfg {
    /* access modifiers changed from: private */
    public String TAG = getClass().getSimpleName();
    private byte[] buffer = new byte[1024];
    private int bufferSize = 0;
    private File cfgFile = null;
    private FileOutputStream cfgFos = null;
    private long cfgOffset = 0;
    private String cfgPath = "";
    private final Context context;
    /* access modifiers changed from: private */
    public DJIUpCfgModel deviceCfgModel;
    private final int deviceId;
    /* access modifiers changed from: private */
    public final DeviceType deviceType;
    private DJIDataCallBack getCfgCallBack = new DJIDataCallBack() {
        /* class dji.dbox.upgrade.p4.utils.DJIRequestDeviceCfg.AnonymousClass1 */

        public void onSuccess(Object model) {
            DJIRequestDeviceCfg.this.getSuc();
        }

        public void onFailure(Ccode ccode) {
            DJIUpConstants.LOGD(DJIRequestDeviceCfg.this.TAG, "getCfgCallBack " + DJIRequestDeviceCfg.this.deviceType + " ccode=" + ccode);
            if (ccode == Ccode.USER_CANCEL) {
                DJIRequestDeviceCfg.this.listener.onFailed();
                return;
            }
            switch (ccode) {
                case CfgVersionNotMatch:
                case CfgInvalid:
                case CfgNotExisted:
                    DJIUpCfgModel unused = DJIRequestDeviceCfg.this.deviceCfgModel = DJIUpCfgModel.makeNullDeviceCfg(DJIRequestDeviceCfg.this.productId);
                    DJIRequestDeviceCfg.this.listener.onSuccess(DJIRequestDeviceCfg.this.deviceCfgModel);
                    return;
                default:
                    if ((ccode == Ccode.TIMEOUT || ccode == Ccode.NOCONNECT) && DJIRequestDeviceCfg.this.retryTime != 3) {
                        DJIUpConstants.LOGD(DJIRequestDeviceCfg.this.TAG, "getCfgCallBack " + DJIRequestDeviceCfg.this.deviceType + " retryTime=" + DJIRequestDeviceCfg.this.retryTime);
                        DJIRequestDeviceCfg.access$608(DJIRequestDeviceCfg.this);
                        DJIRequestDeviceCfg.this.getDeviceCFG();
                        return;
                    }
                    int unused2 = DJIRequestDeviceCfg.this.retryTime = 0;
                    DJIUpCfgModel unused3 = DJIRequestDeviceCfg.this.deviceCfgModel = DJIUpCfgModel.makeNullDeviceCfg(DJIRequestDeviceCfg.this.productId);
                    DJIRequestDeviceCfg.this.deviceCfgModel.isTimeoutCase = true;
                    DJIRequestDeviceCfg.this.listener.onSuccess(DJIRequestDeviceCfg.this.deviceCfgModel);
                    return;
            }
        }
    };
    private DataCommonGetCfgFile getCfgFile = new DataCommonGetCfgFile();
    private boolean isSpecialLen = false;
    private boolean isToLocal = false;
    /* access modifiers changed from: private */
    public final DJIRequestCfgListener listener;
    /* access modifiers changed from: private */
    public final String productId;
    private long readLen = ((long) this.readLenDefine);
    private int readLenDefine = -1;
    private long remainLen = 0;
    /* access modifiers changed from: private */
    public int retryTime = 0;

    public interface DJIRequestCfgListener {
        void onFailed();

        void onSuccess(DJIUpCfgModel dJIUpCfgModel);
    }

    static /* synthetic */ int access$608(DJIRequestDeviceCfg x0) {
        int i = x0.retryTime;
        x0.retryTime = i + 1;
        return i;
    }

    public DJIRequestDeviceCfg(Context context2, DeviceType deviceType2, int deviceId2, DJIUpDeviceType productId2, DJIRequestCfgListener listener2) {
        this.context = context2;
        this.deviceType = deviceType2;
        this.deviceId = deviceId2;
        this.productId = productId2.toString();
        this.isToLocal = DJIUpDeviceType.isSupportCompareOffline(productId2);
        this.listener = listener2;
        String dirPath = context2.getFilesDir() + DJIUpConstants.directory + productId2;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.cfgPath = dirPath + "/deviceCfg-" + deviceType2 + ".xml";
        this.cfgFile = new File(this.cfgPath);
    }

    private static String getLocalPath(Context context2) {
        return Environment.getExternalStorageDirectory() + "/DJI/" + context2.getPackageName() + "/offlineCfg/";
    }

    private static String getLocalName(Context context2, String productId2, String deviceType2) {
        return getLocalPath(context2) + productId2 + "-deviceCfg_verify-" + deviceType2 + ".xml";
    }

    public void setStartLen(int len) {
        this.isSpecialLen = true;
        this.readLen = (long) len;
        this.readLenDefine = len;
    }

    public void cancel() {
        this.getCfgFile.cancel();
        resetStatus();
        this.getCfgFile = new DataCommonGetCfgFile();
        DJIUpConstants.LOGD(this.TAG, this.deviceType + " getCfgCallBack cancel");
    }

    public void resetStatus() {
        this.cfgOffset = 0;
        this.readLen = -1;
        this.remainLen = 0;
        this.bufferSize = 0;
        this.deviceCfgModel = null;
    }

    public void startGetDeviceCfg() {
        DJIUpConstants.LOGD(this.TAG, this.deviceType + " getCfgCallBack start isToLocal=" + this.isToLocal);
        if (!this.isToLocal || ServiceManager.getInstance().isConnected()) {
            if (this.cfgFos != null) {
                try {
                    this.cfgFos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                this.cfgFos = null;
            }
            getDeviceCFG();
            return;
        }
        File oFile = new File(getLocalName(this.context, this.productId, this.deviceType.toString()));
        if (oFile.exists()) {
            try {
                this.deviceCfgModel = DJIUpXmlPaser.getCfgModel(oFile);
                if (this.deviceCfgModel != null) {
                    this.listener.onSuccess(this.deviceCfgModel);
                } else {
                    makeNullCfgForWifiProduct();
                }
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
                makeNullCfgForWifiProduct();
            }
        } else {
            makeNullCfgForWifiProduct();
        }
    }

    private void makeNullCfgForWifiProduct() {
        this.deviceCfgModel = DJIUpCfgModel.makeNullDeviceCfg(this.productId);
        if (this.listener != null) {
            this.listener.onSuccess(this.deviceCfgModel);
        }
    }

    /* access modifiers changed from: private */
    public void getDeviceCFG() {
        this.getCfgFile.setReceiveType(this.deviceType).setReceiveId(this.deviceId).setType(DataCommonGetCfgFile.DJIUpgradeFileType.CFG).setLength(this.readLen).setOffset(this.cfgOffset).start(this.getCfgCallBack);
    }

    /* access modifiers changed from: private */
    public void getSuc() {
        this.retryTime = 0;
        if (!this.isSpecialLen && this.readLen == ((long) this.readLenDefine)) {
            this.readLen = this.getCfgFile.getRelLength();
        }
        this.bufferSize = this.getCfgFile.getBuffer(this.buffer);
        this.cfgOffset += (long) this.bufferSize;
        this.remainLen = this.getCfgFile.getRemainLength();
        try {
            writeToLocal();
            if (this.remainLen > 0) {
                DJIUpConstants.LOGD(this.TAG, this.deviceType + " getCfgCallBack suc but not over. remainLen=" + this.remainLen);
                getDeviceCFG();
            } else if (this.remainLen == 0) {
                String outpath = this.cfgPath.replace("deviceCfg", "deviceCfg_verify");
                DJIUpConstants.LOGD(this.TAG, this.deviceType + " getCfgCallBack suc isToLocal=" + this.isToLocal);
                if (UpgradeVerify.native_verifyCfg(this.cfgPath, outpath, DJIUpDeviceType.find(this.productId).isNeedVerify())) {
                    try {
                        this.deviceCfgModel = DJIUpXmlPaser.getCfgModel(new File(outpath));
                        this.listener.onSuccess(this.deviceCfgModel);
                        if (this.isToLocal) {
                            File dir = new File(getLocalPath(this.context));
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            String topath = getLocalName(this.context, this.productId, this.deviceType.toString());
                            V_FileUtil.copyFile(outpath, topath);
                            DJIUpConstants.LOGD(this.TAG, "outpath=" + outpath);
                            DJIUpConstants.LOGD(this.TAG, "topath=" + topath);
                        }
                    } catch (FileNotFoundException e) {
                        DJIUpConstants.LOGD(this.TAG, "e=" + e.getMessage());
                        this.listener.onFailed();
                        e.printStackTrace();
                    }
                } else {
                    DJIUpConstants.LOGD(this.TAG, "getCfgCallBack " + this.deviceType + " native_verifyCfg=false");
                    this.listener.onFailed();
                }
            }
        } catch (IOException e2) {
            DJIUpConstants.LOGD(this.TAG, "write error : " + e2);
            e2.printStackTrace();
        }
    }

    private void writeToLocal() throws IOException {
        if (this.remainLen == 0) {
            if (this.cfgFos != null) {
                this.cfgFos.write(this.buffer, 0, this.bufferSize);
                this.cfgFos.flush();
                this.cfgFos.close();
                this.cfgFos = null;
            }
        } else if (this.remainLen < 0) {
            DJIUpConstants.LOGD(this.TAG, "getCfgCallBack " + this.deviceType + " writeToLocal faild");
        } else {
            if (this.cfgFos == null) {
                if (this.cfgFile.exists()) {
                    this.cfgFile.delete();
                }
                this.cfgFile.createNewFile();
                this.cfgFos = new FileOutputStream(this.cfgFile);
            }
            this.cfgFos.write(this.buffer, 0, this.bufferSize);
            this.cfgFos.flush();
        }
    }
}
