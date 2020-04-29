package dji.midware.tlv.reader;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.natives.GroudStation;
import dji.midware.tlv.DJISTLVBasePack;
import dji.midware.tlv.DJISTLVPackFactory;
import dji.midware.tlv.dataParser.IDJISTLVPackDataParser;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJISTLVPackReader {
    public static DJISTLVBasePack testParse(byte[] data) {
        return new DJISTLVPackReader().parse(data, 0);
    }

    public DJISTLVBasePack parse(byte[] data) {
        return parse(data, 0);
    }

    public DJISTLVBasePack parse(byte[] data, int offset) {
        if (offset >= data.length) {
            return null;
        }
        int offsetStart = offset;
        byte magic = BytesUtil.getByte(data, offset, 1);
        int offset2 = offset + 1;
        int type = BytesUtil.getInt(data, offset2, 4);
        int offset3 = offset2 + 4;
        DJISTLVBasePack pack = DJISTLVPackFactory.getPack(DJISTLVPackFactory.Type.find(type));
        pack.magic = magic;
        pack.type = type;
        if (!pack.checkMagic()) {
            DJILog.e("tlv", "magic incorrect:" + ((int) pack.magic), new Object[0]);
            return null;
        }
        pack.version = BytesUtil.getFloat(data, offset3, 4);
        int offset4 = offset3 + 4;
        pack.length = BytesUtil.getInt(data, offset4, 4);
        int offset5 = offset4 + 4;
        if (pack.length <= 0) {
            DJILog.e("tlv", "length incorrect:" + pack.length, new Object[0]);
            return null;
        }
        pack.index = BytesUtil.getInt(data, offset5, 4);
        int offset6 = offset5 + 4;
        pack.headCrc = BytesUtil.getByte(data, offset6, 1);
        int offset7 = offset6 + 1;
        int length = (offset7 - offsetStart) - 1;
        byte[] headBytes = new byte[length];
        System.arraycopy(data, offsetStart, headBytes, 0, length);
        byte headCrc = GroudStation.native_calcCrc8(headBytes);
        if (pack.headCrc != headCrc) {
            DJILog.e("tlv", "head crc incorrect:" + ((int) pack.headCrc) + " src is:" + ((int) headCrc), new Object[0]);
            return null;
        }
        IDJISTLVPackDataParser parser = DJISTLVPackFactory.getDataParser(DJISTLVPackFactory.Type.find(type));
        pack.payload = parser.parse(data, offset7, parser.length());
        int offset8 = offset7 + parser.length();
        pack.packCrc = BytesUtil.getShort(data, offset8, 2);
        int length2 = offset8 - offsetStart;
        byte[] packBytes = new byte[length2];
        System.arraycopy(data, offsetStart, packBytes, 0, length2);
        short packCrc = GroudStation.native_calcCrc16(packBytes);
        if (pack.packCrc == packCrc) {
            return pack;
        }
        DJILog.e("tlv", "pack crc incorrect:" + ((int) pack.packCrc) + " src is:" + ((int) packCrc), new Object[0]);
        return null;
    }
}
