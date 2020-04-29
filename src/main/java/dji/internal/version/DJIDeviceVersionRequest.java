package dji.internal.version;

import android.os.Handler;
import android.os.Message;
import com.google.android.gms.common.ConnectionResult;
import com.secneo.ProtectMeVmpMethod;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EXClassNullAway
public class DJIDeviceVersionRequest {
    private static final int DEFAULT_EVENT = 1;
    private static final int MAX_RETRY = 3;
    private static final String TAG = "FirmwareVersionRequest";
    private ResultCallBack callback = null;
    private String[] firmwareList = null;
    /* access modifiers changed from: private */
    public HashMap<String, DJIDeviceVersion> firmwareMap = null;
    private ArrayList<DJIDeviceVersion> firmwareVersionList = null;
    private Handler handler;
    private int retryTime = 0;
    private ArrayList<DJIDeviceVersion> tmpList;

    public interface ResultCallBack {
        void onResultCallBack(boolean z, ArrayList<DJIDeviceVersion> arrayList);
    }

    public DJIDeviceVersionRequest(String[] firmwareList2, ResultCallBack callback2) {
        this.firmwareList = firmwareList2;
        this.firmwareMap = new HashMap<>();
        this.firmwareVersionList = new ArrayList<>();
        if (firmwareList2 != null) {
            for (String f : firmwareList2) {
                DJIDeviceVersion fv = new DJIDeviceVersion(f);
                this.firmwareMap.put(f, fv);
                this.firmwareVersionList.add(fv);
            }
        }
        this.callback = callback2;
        this.retryTime = 3;
        this.handler = new Handler(BackgroundLooper.getLooper(), new Handler.Callback() {
            /* class dji.internal.version.DJIDeviceVersionRequest.AnonymousClass1 */

            public boolean handleMessage(Message msg) {
                if (msg.what != 1) {
                    return false;
                }
                new Thread() {
                    /* class dji.internal.version.DJIDeviceVersionRequest.AnonymousClass1.AnonymousClass1 */

                    public void run() {
                        DJIDeviceVersionRequest.this.getDeviceVersionFromComponent();
                    }
                }.start();
                return false;
            }
        });
        getDeviceVersionFromComponent();
    }

    /* access modifiers changed from: private */
    public void getDeviceVersionFromComponent() {
        this.tmpList = new ArrayList<>();
        for (Map.Entry<String, DJIDeviceVersion> entry : this.firmwareMap.entrySet()) {
            this.tmpList.add(entry.getValue());
        }
        getNextDeviceVersion();
    }

    /* access modifiers changed from: private */
    @ProtectMeVmpMethod
    public void getNextDeviceVersion() {
        if (this.tmpList.size() > 0) {
            DJIDeviceVersion f = this.tmpList.get(0);
            this.tmpList.remove(0);
            GetVersionCallBack verCallBack = new GetVersionCallBack(f);
            if (DeviceType.isDM368_G(f.deviceType) || DeviceType.isFPGA_G(f.deviceType) || DeviceType.isTRANSFORM_G(f.deviceType) || DeviceType.isOSD(f.deviceType) || (DeviceType.isCAMERA(f.deviceType) && (f.moduleId == 0 || f.moduleId == 1))) {
                new DataCommonGetVersion().setDeviceType(f.deviceType).start(verCallBack, 500, 5);
            } else {
                new DataCommonGetVersion().setDeviceType(f.deviceType).setDeviceModel(f.moduleId).start(verCallBack, 500, 5);
            }
        } else if (this.firmwareMap.size() == 0) {
            finishGetVersion(true);
        } else {
            log("checkIsFinish retry time : " + this.retryTime);
            this.retryTime--;
            if (this.retryTime <= 0) {
                finishGetVersion(false);
            } else if (!this.handler.hasMessages(1)) {
                this.handler.sendEmptyMessageDelayed(1, (long) ((3 - this.retryTime) * ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED));
            }
        }
    }

    private class GetVersionCallBack implements DJIDataCallBack {
        private long MIN_VALUE = Long.MIN_VALUE;
        private DJIDeviceVersion firmwareVersion;

        public GetVersionCallBack(DJIDeviceVersion f) {
            this.firmwareVersion = f;
        }

        @ProtectMeVmpMethod
        public void onSuccess(Object model) {
            DataCommonGetVersion data = (DataCommonGetVersion) model;
            if (DeviceType.isFLYC(this.firmwareVersion.deviceType) && this.firmwareVersion.moduleId == 5) {
                this.firmwareVersion.setVersion(data.getLoader("."));
            } else if (!DeviceType.isCAMERA(this.firmwareVersion.deviceType) || this.firmwareVersion.moduleId != 1) {
                this.firmwareVersion.setVersion(data.getFirmVer("."));
            } else {
                this.firmwareVersion.setVersion(data.getLoader("."));
            }
            if ((DeviceType.isDM368_G(this.firmwareVersion.deviceType) || DeviceType.isFPGA_G(this.firmwareVersion.deviceType) || DeviceType.isTRANSFORM_G(this.firmwareVersion.deviceType) || DeviceType.isOSD(this.firmwareVersion.deviceType)) && this.firmwareVersion.moduleId != data.getLoaderByte(2)) {
                this.firmwareVersion.version = this.MIN_VALUE;
                this.firmwareVersion.versionStr = null;
            }
            if (DeviceType.isDM368_G(this.firmwareVersion.deviceType) && this.firmwareVersion.version == 0) {
                this.firmwareVersion.version = this.MIN_VALUE;
                this.firmwareVersion.versionStr = null;
            }
            DJIDeviceVersionRequest.this.firmwareMap.remove(this.firmwareVersion.firmware);
            DJIDeviceVersionRequest.this.getNextDeviceVersion();
        }

        public void onFailure(Ccode ccode) {
            DJIDeviceVersionRequest.this.log("fail : " + this.firmwareVersion.firmware);
            this.firmwareVersion.version = this.MIN_VALUE;
            DJIDeviceVersionRequest.this.getNextDeviceVersion();
        }
    }

    private void finishGetVersion(boolean ret) {
        if (this.callback != null) {
            this.callback.onResultCallBack(ret, this.firmwareVersionList);
        }
        this.firmwareVersionList = null;
        this.firmwareList = null;
        this.callback = null;
    }

    /* access modifiers changed from: private */
    public void log(String log) {
    }
}
