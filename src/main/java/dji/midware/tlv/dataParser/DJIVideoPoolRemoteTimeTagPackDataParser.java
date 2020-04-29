package dji.midware.tlv.dataParser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.tlv.DJISTLVBasePack;
import dji.midware.tlv.DJIVideoPoolRemoteTimeTagPack;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIVideoPoolRemoteTimeTagPackDataParser implements IDJISTLVPackDataParser {
    public DJISTLVBasePack.DJISTLVBasePackData parse(byte[] data, int offset, int length) {
        DJIVideoPoolRemoteTimeTagPack.DJIVideoPoolRemoteTimeTagPackData packData = new DJIVideoPoolRemoteTimeTagPack.DJIVideoPoolRemoteTimeTagPackData();
        packData.localTime = BytesUtil.getInt(data, offset, 4);
        int offset2 = offset + 4;
        packData.remoteTime = BytesUtil.getInt(data, offset2, 4);
        packData.caMediaTime = BytesUtil.getInt(data, offset2 + 4, 4);
        return packData;
    }

    public byte[] serialize(DJISTLVBasePack.DJISTLVBasePackData data) {
        byte[] bytes = new byte[length()];
        if (data instanceof DJIVideoPoolRemoteTimeTagPack.DJIVideoPoolRemoteTimeTagPackData) {
            DJIVideoPoolRemoteTimeTagPack.DJIVideoPoolRemoteTimeTagPackData packData = (DJIVideoPoolRemoteTimeTagPack.DJIVideoPoolRemoteTimeTagPackData) data;
            byte[] localTime = BytesUtil.getBytes(packData.localTime);
            byte[] remoteTime = BytesUtil.getBytes(packData.remoteTime);
            byte[] caMediaTime = BytesUtil.getBytes(packData.caMediaTime);
            BytesUtil.arraycopy(localTime, bytes, 0);
            int offset = 0 + 4;
            BytesUtil.arraycopy(remoteTime, bytes, offset);
            BytesUtil.arraycopy(caMediaTime, bytes, offset + 4);
        }
        return bytes;
    }

    public int length() {
        return new DJIVideoPoolRemoteTimeTagPack.DJIVideoPoolRemoteTimeTagPackData().length();
    }
}
