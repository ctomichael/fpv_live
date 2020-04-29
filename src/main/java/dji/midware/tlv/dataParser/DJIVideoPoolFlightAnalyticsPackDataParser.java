package dji.midware.tlv.dataParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVBasePack;
import dji.midware.tlv.DJIVideoPoolFlightAnalyticsPack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIVideoPoolFlightAnalyticsPackDataParser implements IDJISTLVPackDataParser {
    public DJISTLVBasePack.DJISTLVBasePackData parse(byte[] data, int offset, int length) {
        DJIVideoPoolFlightAnalyticsPack.DJIVideoPoolFlightAnalyticsPackData packData = new DJIVideoPoolFlightAnalyticsPack.DJIVideoPoolFlightAnalyticsPackData();
        packData.caMediaTimeMs = BytesUtil.getInt(data, offset, 4);
        int offset2 = offset + 4;
        packData.normalizedSpeed = BytesUtil.getFloat(data, offset2, 4);
        int offset3 = offset2 + 4;
        packData.accl = BytesUtil.getFloat(data, offset3, 4);
        int offset4 = offset3 + 4;
        packData.jerk = BytesUtil.getFloat(data, offset4, 4);
        int offset5 = offset4 + 4;
        packData.cameraSpeedPitch = BytesUtil.getFloat(data, offset5, 4);
        int offset6 = offset5 + 4;
        packData.cameraSpeedYaw = BytesUtil.getFloat(data, offset6, 4);
        packData.cameraSpeedRotate = BytesUtil.getFloat(data, offset6 + 4, 4);
        return packData;
    }

    public byte[] serialize(DJISTLVBasePack.DJISTLVBasePackData data) {
        byte[] bytes = new byte[length()];
        if (data instanceof DJIVideoPoolFlightAnalyticsPack.DJIVideoPoolFlightAnalyticsPackData) {
            DJIVideoPoolFlightAnalyticsPack.DJIVideoPoolFlightAnalyticsPackData packData = (DJIVideoPoolFlightAnalyticsPack.DJIVideoPoolFlightAnalyticsPackData) data;
            byte[] caMediaTimeMs = BytesUtil.getBytes(packData.caMediaTimeMs);
            byte[] normalizedSpeed = BytesUtil.getBytes(packData.normalizedSpeed);
            byte[] accl = BytesUtil.getBytes(packData.accl);
            byte[] jerk = BytesUtil.getBytes(packData.jerk);
            byte[] cameraSpeedPitch = BytesUtil.getBytes(packData.cameraSpeedPitch);
            byte[] cameraSpeedYaw = BytesUtil.getBytes(packData.cameraSpeedYaw);
            byte[] cameraSpeedRotate = BytesUtil.getBytes(packData.cameraSpeedRotate);
            BytesUtil.arraycopy(caMediaTimeMs, bytes, 0);
            int offset = 0 + 4;
            BytesUtil.arraycopy(normalizedSpeed, bytes, offset);
            int offset2 = offset + 4;
            BytesUtil.arraycopy(accl, bytes, offset2);
            int offset3 = offset2 + 4;
            BytesUtil.arraycopy(jerk, bytes, offset3);
            int offset4 = offset3 + 4;
            BytesUtil.arraycopy(cameraSpeedPitch, bytes, offset4);
            int offset5 = offset4 + 4;
            BytesUtil.arraycopy(cameraSpeedYaw, bytes, offset5);
            BytesUtil.arraycopy(cameraSpeedRotate, bytes, offset5 + 4);
        }
        return bytes;
    }

    public int length() {
        return new DJIVideoPoolFlightAnalyticsPack.DJIVideoPoolFlightAnalyticsPackData().length();
    }
}
