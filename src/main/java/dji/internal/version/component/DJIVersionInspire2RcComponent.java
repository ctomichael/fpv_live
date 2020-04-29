package dji.internal.version.component;

import android.content.Context;
import android.os.Environment;
import android.util.Xml;
import dji.common.error.DJIError;
import dji.component.motorlock.model.LockKey;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.version.DJIModelUpgradePackList;
import dji.internal.version.DJIVersionBaseComponent;
import dji.internal.version.VersionController;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetCfgFile;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.natives.UpgradeVerify;
import dji.publics.protocol.ResponseBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import org.xmlpull.v1.XmlPullParser;

@EXClassNullAway
public class DJIVersionInspire2RcComponent extends DJIVersionBaseComponent {
    public static final int MAX_RETRY_TIMES = 10;
    private static final String TAG = "DJIVersionInspire2RcComponent";
    byte[] buffer = new byte[1024];
    int bufferSize = 0;
    File cfgFile = null;
    private FileOutputStream cfgFos = null;
    long cfgOffset = 0;
    /* access modifiers changed from: private */
    public String cfgPath = null;
    private DJIDataCallBack getCfgCallBack = new DJIDataCallBack() {
        /* class dji.internal.version.component.DJIVersionInspire2RcComponent.AnonymousClass1 */

        public void onSuccess(Object model) {
            int unused = DJIVersionInspire2RcComponent.this.retryTimes = 0;
            if (DJIVersionInspire2RcComponent.this.readLen == -1) {
                DJIVersionInspire2RcComponent.this.readLen = DJIVersionInspire2RcComponent.this.getCfgFile.getRelLength();
            }
            DJIVersionInspire2RcComponent.this.bufferSize = DJIVersionInspire2RcComponent.this.getCfgFile.getBuffer(DJIVersionInspire2RcComponent.this.buffer);
            DJIVersionInspire2RcComponent.this.cfgOffset += (long) DJIVersionInspire2RcComponent.this.bufferSize;
            DJIVersionInspire2RcComponent.this.remainLen = DJIVersionInspire2RcComponent.this.getCfgFile.getRemainLength();
            try {
                DJIVersionInspire2RcComponent.this.writeToLocal();
                if (DJIVersionInspire2RcComponent.this.remainLen > 0) {
                    DJIVersionInspire2RcComponent.this.getDeviceCFG();
                } else if (DJIVersionInspire2RcComponent.this.remainLen == 0) {
                    String outpath = DJIVersionInspire2RcComponent.this.cfgPath.replace("deviceCfg_rc", "deviceCfg_rc_verify");
                    boolean isOK = UpgradeVerify.native_verifyCfg(DJIVersionInspire2RcComponent.this.cfgPath, outpath, DJIVersionInspire2RcComponent.this.isCfgNeedVerify());
                    if (isOK) {
                        try {
                            DJIVersionInspire2RcComponent.this.getCfgModel(new File(outpath));
                            VersionController.logD(DJIVersionInspire2RcComponent.TAG, "Get Device Cfg Success" + DJIVersionInspire2RcComponent.this.packageVersion);
                            EventBus.getDefault().post(DJIVersionInspire2RcComponent.this);
                        } catch (FileNotFoundException e) {
                            VersionController.logD(DJIVersionInspire2RcComponent.TAG, DJILog.exceptionToString(e));
                        }
                    } else {
                        VersionController.logD(DJIVersionInspire2RcComponent.TAG, "getCfgCallBack native_verifyCfg=" + isOK);
                    }
                }
            } catch (IOException e2) {
                DJILog.e(DJIVersionInspire2RcComponent.TAG, DJILog.exceptionToString(e2), new Object[0]);
            }
        }

        public void onFailure(Ccode ccode) {
            if (DJIVersionInspire2RcComponent.this.retryTimes <= 10) {
                DJIVersionInspire2RcComponent.access$008(DJIVersionInspire2RcComponent.this);
                VersionController.logD(DJIVersionInspire2RcComponent.TAG, "retry to getDeviceCFG retryTimes" + DJIVersionInspire2RcComponent.this.retryTimes);
                DJIVersionInspire2RcComponent.this.getDeviceCFG();
                return;
            }
            VersionController.logD(DJIVersionInspire2RcComponent.TAG, "Fail" + DJIError.getDJIError(ccode).getDescription());
            int unused = DJIVersionInspire2RcComponent.this.retryTimes = 0;
        }
    };
    DataCommonGetCfgFile getCfgFile = DataCommonGetCfgFile.getInstance();
    private boolean isGetDevices = false;
    /* access modifiers changed from: private */
    public String packageVersion = "";
    long readLen = -1;
    long remainLen = 0;
    /* access modifiers changed from: private */
    public int retryTimes = 0;

    static /* synthetic */ int access$008(DJIVersionInspire2RcComponent x0) {
        int i = x0.retryTimes;
        x0.retryTimes = i + 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public String[] getFirmwareList() {
        return null;
    }

    /* access modifiers changed from: protected */
    public ArrayList<DJIModelUpgradePackList.DJIUpgradePack> getDJIUpgradePackList(DJIModelUpgradePackList model) {
        return null;
    }

    public void init(Context ctx) {
        this.cfgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DJI/" + ctx.getApplicationContext().getPackageName() + "/deviceCfg_rc.xml");
        this.cfgPath = Environment.getExternalStorageDirectory().getPath() + "/DJI/" + ctx.getApplicationContext().getPackageName() + "/deviceCfg_rc.xml";
        DJILog.d(TAG, this.cfgPath, new Object[0]);
        getDeviceCFG();
    }

    public void uninit() {
    }

    public String getComponentVersion() {
        return this.packageVersion;
    }

    /* access modifiers changed from: protected */
    public String getVersion(DJIModelUpgradePackList.DJIUpgradePack pack) {
        return this.packageVersion;
    }

    /* access modifiers changed from: protected */
    public String getComponentName() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public DeviceType getDeviceType() {
        return DeviceType.DM368_G;
    }

    /* access modifiers changed from: protected */
    public int getDeviceId() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public void writeToLocal() throws IOException {
        if (this.remainLen == 0) {
            if (this.cfgFos != null) {
                this.cfgFos.write(this.buffer, 0, this.bufferSize);
                this.cfgFos.flush();
                this.cfgFos.close();
                this.cfgFos = null;
            }
        } else if (this.remainLen >= 0) {
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

    /* access modifiers changed from: private */
    public void getDeviceCFG() {
        DataCommonGetCfgFile.getInstance().setReceiveType(getDeviceType()).setReceiveId(getDeviceId()).setType(DataCommonGetCfgFile.DJIUpgradeFileType.CFG).setLength(this.readLen).setOffset(this.cfgOffset).start(this.getCfgCallBack);
    }

    /* access modifiers changed from: private */
    public void getCfgModel(File file) throws FileNotFoundException {
        InputStream inStream = new FileInputStream(file);
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inStream, "UTF-8");
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0:
                        DJILog.d(TAG, "P4 Get version", new Object[0]);
                        break;
                    case 2:
                        String name = parser.getName();
                        if (!name.equalsIgnoreCase("device")) {
                            if (this.isGetDevices) {
                                if (!name.equalsIgnoreCase(LockKey.FIRMWARE_UPGRADE_LOCK)) {
                                    if (!name.equalsIgnoreCase("release")) {
                                        break;
                                    } else {
                                        this.packageVersion = parser.getAttributeValue(null, "version");
                                        break;
                                    }
                                } else {
                                    DJILog.d(TAG, "Get firmware", new Object[0]);
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            DJILog.d(TAG, "Get device type: " + parser.getAttributeValue(null, ResponseBase.STRING_ID), new Object[0]);
                            this.isGetDevices = true;
                            break;
                        }
                }
            }
            inStream.close();
        } catch (Exception e) {
            DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
        }
    }
}
