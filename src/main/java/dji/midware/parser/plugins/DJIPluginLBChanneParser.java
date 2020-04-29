package dji.midware.parser.plugins;

import com.google.zxing.client.result.ExpandedProductParsedResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.parser.plugins.DJIPluginRingBufferParser;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIPluginLBChanneParser {
    private String TAG = getClass().getSimpleName();
    /* access modifiers changed from: private */
    public int bodyLen = 0;
    /* access modifiers changed from: private */
    public int channelID = 0;
    /* access modifiers changed from: private */
    public int checkSum = 0;
    private final int headerLen = 12;
    private final boolean isDebug = false;
    private final byte[] magic = {0, 0, 1, -1};
    private DJIPluginRingBufferParser ringBufferParser;
    /* access modifiers changed from: private */
    public int rseverd = 0;
    /* access modifiers changed from: private */
    public int version = 0;

    public interface DJILBChannelListener {
        void onRecv(int i, byte[] bArr, int i2, int i3);
    }

    public DJIPluginLBChanneParser(final DJILBChannelListener listener) {
        DJIRingBufferModel bufferModel = new DJIRingBufferModel();
        bufferModel.header = this.magic;
        bufferModel.secondHeaderLen = 8;
        this.ringBufferParser = new DJIPluginRingBufferParser(1048576, bufferModel, new DJIPluginRingBufferParser.DJIRingBufferParserListener() {
            /* class dji.midware.parser.plugins.DJIPluginLBChanneParser.AnonymousClass1 */

            public int parseSecondHeader(byte[] buffer, int offset, int count) {
                int index = offset;
                int unused = DJIPluginLBChanneParser.this.bodyLen = BytesUtil.getUShort(buffer, index);
                int index2 = index + 4;
                int unused2 = DJIPluginLBChanneParser.this.version = buffer[index2];
                int index3 = index2 + 1;
                int unused3 = DJIPluginLBChanneParser.this.channelID = buffer[index3];
                int index4 = index3 + 1;
                int unused4 = DJIPluginLBChanneParser.this.checkSum = buffer[index4];
                int index5 = index4 + 1;
                int unused5 = DJIPluginLBChanneParser.this.rseverd = buffer[index5];
                int index6 = index5 + 1;
                if (DJIPluginLBChanneParser.this.headerXor(buffer, offset, count) != 0) {
                    return -1;
                }
                return DJIPluginLBChanneParser.this.bodyLen;
            }

            public void onGetBody(byte[] buffer, int offset, int count) {
                listener.onRecv(DJIPluginLBChanneParser.this.channelID, buffer, offset, count);
            }
        });
        this.ringBufferParser.setName(ExpandedProductParsedResult.POUND);
    }

    public void parse(byte[] buffer, int offset, int count) {
        this.ringBufferParser.parse(buffer, offset, count);
    }

    /* access modifiers changed from: private */
    public int headerXor(byte[] buffer, int offset, int count) {
        byte b = this.magic[0];
        for (int i = 1; i < this.magic.length; i++) {
            b ^= this.magic[i];
        }
        for (int i2 = 0; i2 < count; i2++) {
            b ^= buffer[offset + i2];
        }
        return b;
    }

    public enum DJILBChannelID {
        LiveView(17),
        FileDownload(18),
        SecondaryLiveView(19),
        ThirdLiveViewZ30(21),
        FourthLiveViewXT(23),
        PayloadCMD(24),
        PayloadLiveView(25),
        ARPush(35),
        FlightLog(64);
        
        private int mValue = 0;

        private DJILBChannelID(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }
}
