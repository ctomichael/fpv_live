package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.midware.data.model.P3.DataDm368_gGetPushCheckStatus;
import dji.midware.data.model.P3.DataGlassGetPushCheckStatus;
import dji.midware.data.model.P3.DataOsdGetPushCheckStatusV2;
import dji.midware.data.packages.P3.Pack;

@EXClassNullAway
public class NewCheckStatusHelper {
    public static void findNewCheckStatus(Pack pack) {
        if (pack != null && pack.data.length >= 2) {
            int senderType = pack.data[0] & 31;
            int i = pack.data[0] >> 5;
            byte b = pack.data[1];
            byte[] setData = new byte[b];
            System.arraycopy(pack.data, 2, setData, 0, b);
            pack.data = setData;
            if (senderType != DeviceType.CAMERA.value() && senderType != DeviceType.FLYC.value() && senderType != DeviceType.GIMBAL.value() && senderType != DeviceType.CENTER.value() && senderType != DeviceType.RC.value() && senderType != DeviceType.OFDM.value() && senderType != DeviceType.BATTERY.value()) {
                if (senderType == DeviceType.DM368_G.value()) {
                    DataDm368_gGetPushCheckStatus.getInstance().outerSetPushRecPack(pack);
                } else if (senderType == DeviceType.OSD.value()) {
                    DataOsdGetPushCheckStatusV2.getInstance().outerSetPushRecPack(pack);
                } else if (senderType != DeviceType.TRANSFORM_G.value() && senderType != DeviceType.DM368.value()) {
                    if (senderType == DeviceType.DOUBLE.value()) {
                        Data2100GetPushCheckStatus.getInstance().outerSetPushRecPack(pack);
                    } else if (senderType == DeviceType.GLASS.value()) {
                        DataGlassGetPushCheckStatus.getInstance().outerSetPushRecPack(pack);
                    }
                }
            }
        }
    }
}
