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
public class DJIVersionDM368Component extends DJIVersionBaseComponent {
    private static final String TAG = "DJIVersionDM368Component";
    /* access modifiers changed from: private */
    public byte[] buffer = new byte[1024];
    /* access modifiers changed from: private */
    public int bufferSize = 0;
    private File cfgFile = null;
    private FileOutputStream cfgFos = null;
    /* access modifiers changed from: private */
    public long cfgOffset = 0;
    /* access modifiers changed from: private */
    public String cfgPath = null;
    private DJIDataCallBack getCfgCallBack = new DJIDataCallBack() {
        /* class dji.internal.version.component.DJIVersionDM368Component.AnonymousClass1 */

        public void onSuccess(Object model) {
            int unused = DJIVersionDM368Component.this.retryTimes = 0;
            if (DJIVersionDM368Component.this.readLen == -1) {
                long unused2 = DJIVersionDM368Component.this.readLen = DJIVersionDM368Component.this.getCfgFile.getRelLength();
            }
            int unused3 = DJIVersionDM368Component.this.bufferSize = DJIVersionDM368Component.this.getCfgFile.getBuffer(DJIVersionDM368Component.this.buffer);
            long unused4 = DJIVersionDM368Component.this.cfgOffset = DJIVersionDM368Component.this.cfgOffset + ((long) DJIVersionDM368Component.this.bufferSize);
            long unused5 = DJIVersionDM368Component.this.remainLen = DJIVersionDM368Component.this.getCfgFile.getRemainLength();
            try {
                DJIVersionDM368Component.this.writeToLocal();
                if (DJIVersionDM368Component.this.remainLen > 0) {
                    DJIVersionDM368Component.this.getDeviceCFG();
                } else if (DJIVersionDM368Component.this.remainLen == 0) {
                    String outpath = DJIVersionDM368Component.this.cfgPath.replace("deviceCfg", "deviceCfg_verify");
                    boolean isOK = UpgradeVerify.native_verifyCfg(DJIVersionDM368Component.this.cfgPath, outpath, DJIVersionDM368Component.this.isCfgNeedVerify());
                    if (isOK) {
                        try {
                            DJIVersionDM368Component.this.getCfgModel(new File(outpath));
                            VersionController.logD(DJIVersionDM368Component.TAG, "Get Device Cfg Success: " + DJIVersionDM368Component.this.packageVersion);
                            EventBus.getDefault().post(DJIVersionDM368Component.this);
                        } catch (FileNotFoundException e) {
                            VersionController.logD(DJIVersionDM368Component.TAG, DJILog.exceptionToString(e));
                        }
                    } else {
                        VersionController.logD(DJIVersionDM368Component.TAG, "getCfgCallBack native_verifyCfg=" + isOK);
                    }
                }
            } catch (IOException e2) {
                DJILog.e(DJIVersionDM368Component.TAG, DJILog.exceptionToString(e2), new Object[0]);
            }
        }

        public void onFailure(Ccode ccode) {
            if (DJIVersionDM368Component.this.retryTimes <= 10) {
                DJIVersionDM368Component.access$008(DJIVersionDM368Component.this);
                VersionController.logD(DJIVersionDM368Component.TAG, "retry to getDeviceCFG retryTimes" + DJIVersionDM368Component.this.retryTimes);
                DJIVersionDM368Component.this.getDeviceCFG();
                return;
            }
            VersionController.logD(DJIVersionDM368Component.TAG, "Fail" + DJIError.getDJIError(ccode).getDescription());
            int unused = DJIVersionDM368Component.this.retryTimes = 0;
        }
    };
    /* access modifiers changed from: private */
    public DataCommonGetCfgFile getCfgFile = new DataCommonGetCfgFile();
    private boolean isGetDevices = false;
    /* access modifiers changed from: private */
    public String packageVersion = "";
    /* access modifiers changed from: private */
    public long readLen = -1;
    /* access modifiers changed from: private */
    public long remainLen = 0;
    /* access modifiers changed from: private */
    public int retryTimes = 0;

    static /* synthetic */ int access$008(DJIVersionDM368Component x0) {
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
        this.cfgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DJI/" + ctx.getApplicationContext().getPackageName() + "/deviceCfg.xml");
        this.cfgPath = Environment.getExternalStorageDirectory().getPath() + "/DJI/" + ctx.getApplicationContext().getPackageName() + "/deviceCfg.xml";
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
        return DJIVersionDM368Component.class.getSimpleName();
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

    /* access modifiers changed from: protected */
    public DeviceType getReceiverType() {
        return DeviceType.DM368;
    }

    /* access modifiers changed from: protected */
    public int getReceiverID() {
        return 1;
    }

    /* access modifiers changed from: private */
    public void getDeviceCFG() {
        this.getCfgFile.setReceiveType(getReceiverType()).setReceiveId(getReceiverID()).setType(DataCommonGetCfgFile.DJIUpgradeFileType.CFG).setLength(this.readLen).setOffset(this.cfgOffset).start(this.getCfgCallBack);
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
