package dji.internal.version;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;

@EXClassNullAway
public class DJIDeviceVersion {
    private static final String TAG = "FirmwareVersion";
    public DeviceType deviceType = null;
    public String firmware = null;
    public int moduleId = -1;
    public long version = -1;
    public String versionStr = null;

    public DJIDeviceVersion(String f, String v) {
        setFirmware(f);
        setVersion(v);
    }

    public DJIDeviceVersion(String firmware2) {
        setFirmware(firmware2);
    }

    public void setFirmware(String f) {
        this.firmware = f;
        this.deviceType = DeviceType.find(Integer.valueOf(f.substring(0, 2)).intValue());
        this.moduleId = Integer.valueOf(f.substring(2, 4)).intValue();
    }

    public void setVersion(String v) {
        this.versionStr = v;
        String[] tmp = v.split("\\.");
        if (tmp.length != 4) {
        }
        if (this.deviceType != DeviceType.CAMERA || (this.moduleId != 0 && this.moduleId != 1)) {
            this.version = (Long.parseLong(tmp[0]) * 256 * 256 * 256) + (Long.parseLong(tmp[1]) * 256 * 256) + (Long.parseLong(tmp[2]) * 256) + Long.parseLong(tmp[3]);
        } else if (DJIProductManager.getInstance().getType() == ProductType.litchiC && Long.parseLong(tmp[0]) == 2) {
            this.version = (Long.parseLong(tmp[0]) * 256 * 256 * 256) + (Long.parseLong(tmp[1]) * 256 * 256) + Long.parseLong(tmp[2] + tmp[3]);
        } else {
            this.version = (Long.parseLong(tmp[0]) * 256 * 256 * 256) + (Long.parseLong(tmp[1]) * 256 * 256) + (Long.parseLong(tmp[2]) * 256) + Long.parseLong(tmp[3]);
        }
    }

    public String toString() {
        return this.firmware + ":" + this.versionStr;
    }
}
