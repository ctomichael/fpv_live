package dji.midware.aoabridge;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.msgpack.core.MessagePack;

@EXClassNullAway
public class DJIBaseCommData {
    public static final int DJIBridgeDataSender_androidCommon = 5;
    public static final int DJIBridgeDataSender_mfiCommon = 2;
    public static final int DJIBridgeDataSender_noUsed = 99;
    public static final int DJIBridgeDataSender_usb = 3;
    public static final int DJIBridgeDataSender_usbVideo = 4;
    private static final byte[] endSignal = {MessagePack.Code.UINT8, MessagePack.Code.ARRAY32};
    private static final byte[] headerSignal = {-86, -69};
    public int eventType;
    private HashMap<Integer, byte[]> info = new HashMap<>();
    public int sender;

    public enum DJIBaseCommDataTag {
        DJIBaseCommData_Test(0),
        DJIBaseCommData_Who(1),
        DJIBaseCommData_Event(2),
        DJIBaseCommData_Data(3);
        
        private int tag = 0;

        private DJIBaseCommDataTag(int i) {
            this.tag = i;
        }

        public int getTag() {
            return this.tag;
        }
    }

    public class DJIBaseCommDataEncodeHeader implements Serializable {
        public byte[] data;
        public int size;
        public int tag;

        public DJIBaseCommDataEncodeHeader() {
        }
    }

    public ArrayList<Byte> encodeData() {
        ArrayList<Byte> dataArray = new ArrayList<>();
        for (Map.Entry entry : this.info.entrySet()) {
            Object tag = entry.getKey();
            byte[] data = (byte[]) entry.getValue();
            DJIBaseCommDataEncodeHeader header = new DJIBaseCommDataEncodeHeader();
            header.tag = ((Integer) tag).intValue();
            header.size = data.length;
            header.data = data;
            byte[] bytes = ByteBuffer.allocate(4).putInt(header.tag).array();
            for (int i = bytes.length - 1; i >= 0; i--) {
                dataArray.add(Byte.valueOf(bytes[i]));
            }
            byte[] byteSize = ByteBuffer.allocate(4).putInt(header.size).array();
            for (int i2 = byteSize.length - 1; i2 >= 0; i2--) {
                dataArray.add(Byte.valueOf(byteSize[i2]));
            }
            for (int i3 = data.length - 1; i3 >= 0; i3--) {
                dataArray.add(Byte.valueOf(data[i3]));
            }
        }
        return dataArray;
    }

    public int numberWithTag(DJIBaseCommDataTag tag) {
        byte[] data = this.info.get(Integer.valueOf(tag.getTag()));
        int value = 0;
        for (int i = 0; i < data.length; i++) {
            value |= (data[i] << (((data.length - 1) - i) * 8)) & (255 << (((data.length - 1) - i) * 8));
        }
        return value;
    }

    public byte[] dataWithTag(int tag) {
        return this.info.get(Integer.valueOf(tag));
    }

    public void setNumberWithTag(int number, int tag) {
        byte[] data = new byte[4];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ((number >> (((data.length - 1) - i) * 8)) & 255);
        }
        setDataWithTag(data, tag);
    }

    public void setDataWithTag(byte[] data, int tag) {
        this.info.put(Integer.valueOf(tag), data);
    }

    private byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];
        for (int i = 0; i < oBytes.length; i++) {
            bytes[i] = oBytes[i].byteValue();
        }
        return bytes;
    }

    public byte[] packedData() {
        Byte[] enData;
        ArrayList<Byte> data = new ArrayList<>();
        data.add(Byte.valueOf(headerSignal[0]));
        data.add(Byte.valueOf(headerSignal[1]));
        ArrayList<Byte> encodeData = encodeData();
        short encodeDataLength = (short) encodeData.toArray().length;
        data.add(Byte.valueOf((byte) (encodeDataLength & 255)));
        data.add(Byte.valueOf((byte) ((encodeDataLength >> 8) & 255)));
        for (Byte b : (Byte[]) encodeData.toArray(new Byte[encodeData.size()])) {
            data.add(Byte.valueOf(b.byteValue()));
        }
        data.add(Byte.valueOf(endSignal[0]));
        data.add(Byte.valueOf(endSignal[1]));
        return toPrimitives((Byte[]) data.toArray(new Byte[data.size()]));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v0, resolved type: int[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v0, resolved type: int[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v18, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v20, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v22, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v24, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v26, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v28, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v31, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v33, resolved type: int} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean parseData(byte[] r14, int r15) {
        /*
            r13 = this;
            if (r14 == 0) goto L_0x0006
            r10 = 30
            if (r15 == r10) goto L_0x0008
        L_0x0006:
            r10 = 0
        L_0x0007:
            return r10
        L_0x0008:
            r0 = 0
            int r1 = r0 + 1
            byte r2 = r14[r0]
            int r0 = r1 + 1
            byte r3 = r14[r1]
            byte[] r10 = dji.midware.aoabridge.DJIBaseCommData.headerSignal
            r11 = 0
            byte r10 = r10[r11]
            if (r2 != r10) goto L_0x0035
            byte[] r10 = dji.midware.aoabridge.DJIBaseCommData.headerSignal
            r11 = 1
            byte r10 = r10[r11]
            if (r3 != r10) goto L_0x0035
            int r10 = r15 + -2
            byte r10 = r14[r10]
            byte[] r11 = dji.midware.aoabridge.DJIBaseCommData.endSignal
            r12 = 0
            byte r11 = r11[r12]
            if (r10 != r11) goto L_0x0035
            int r10 = r15 + -1
            byte r10 = r14[r10]
            byte[] r11 = dji.midware.aoabridge.DJIBaseCommData.endSignal
            r12 = 1
            byte r11 = r11[r12]
            if (r10 == r11) goto L_0x0037
        L_0x0035:
            r10 = 0
            goto L_0x0007
        L_0x0037:
            r5 = 4
            r8 = 12
            r6 = 16
            r9 = 24
            r10 = 2
            int[] r4 = new int[r10]
            r10 = 2
            int[] r7 = new int[r10]
            r10 = 0
            byte r11 = r14[r5]
            r4[r10] = r11
            r10 = 1
            byte r11 = r14[r6]
            r4[r10] = r11
            r10 = 0
            byte r11 = r14[r8]
            r7[r10] = r11
            r10 = 1
            byte r11 = r14[r9]
            r7[r10] = r11
            r10 = 0
            r10 = r4[r10]
            dji.midware.aoabridge.DJIBaseCommData$DJIBaseCommDataTag r11 = dji.midware.aoabridge.DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Who
            int r11 = r11.ordinal()
            if (r10 != r11) goto L_0x007a
            r10 = 0
            r10 = r7[r10]
            r13.sender = r10
        L_0x0068:
            r10 = 1
            r10 = r4[r10]
            dji.midware.aoabridge.DJIBaseCommData$DJIBaseCommDataTag r11 = dji.midware.aoabridge.DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Who
            int r11 = r11.ordinal()
            if (r10 != r11) goto L_0x008b
            r10 = 1
            r10 = r7[r10]
            r13.sender = r10
        L_0x0078:
            r10 = 1
            goto L_0x0007
        L_0x007a:
            r10 = 0
            r10 = r4[r10]
            dji.midware.aoabridge.DJIBaseCommData$DJIBaseCommDataTag r11 = dji.midware.aoabridge.DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Event
            int r11 = r11.ordinal()
            if (r10 != r11) goto L_0x0068
            r10 = 0
            r10 = r7[r10]
            r13.eventType = r10
            goto L_0x0068
        L_0x008b:
            r10 = 1
            r10 = r4[r10]
            dji.midware.aoabridge.DJIBaseCommData$DJIBaseCommDataTag r11 = dji.midware.aoabridge.DJIBaseCommData.DJIBaseCommDataTag.DJIBaseCommData_Event
            int r11 = r11.ordinal()
            if (r10 != r11) goto L_0x0078
            r10 = 1
            r10 = r7[r10]
            r13.eventType = r10
            goto L_0x0078
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.aoabridge.DJIBaseCommData.parseData(byte[], int):boolean");
    }
}
