package dji.midware.parser.plugins;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.natives.FPVController;
import dji.midware.parser.plugins.DJIPluginRingBufferParser;
import dji.midware.util.BytesUtil;

@EXClassNullAway
public class DJIPlaybackChanneParser {
    /* access modifiers changed from: private */
    public String TAG = getClass().getSimpleName();
    private int bodyLen = 0;
    private final byte[] header = {68, 74, 65, 86};
    private final boolean isDebug = false;
    /* access modifiers changed from: private */
    public int packType = 0;
    /* access modifiers changed from: private */
    public int packVer = 0;
    byte[] plenBuffer = new byte[4];
    /* access modifiers changed from: private */
    public byte[] ptsBuffer = new byte[8];
    private DJIPluginRingBufferParser ringBufferParser;

    public DJIPlaybackChanneParser() {
        DJIRingBufferModel bufferModel = new DJIRingBufferModel();
        bufferModel.header = this.header;
        bufferModel.secondHeaderLen = 16;
        this.ringBufferParser = new DJIPluginRingBufferParser(1048576, bufferModel, new DJIPluginRingBufferParser.DJIRingBufferParserListener() {
            /* class dji.midware.parser.plugins.DJIPlaybackChanneParser.AnonymousClass1 */

            public int parseSecondHeader(byte[] buffer, int offset, int count) {
                int index = offset + 1;
                System.arraycopy(buffer, index, DJIPlaybackChanneParser.this.plenBuffer, 0, 3);
                int packLen = (int) BytesUtil.getUInt(DJIPlaybackChanneParser.this.plenBuffer, 0);
                int index2 = index + 3;
                int unused = DJIPlaybackChanneParser.this.packVer = (buffer[index2] & 240) >> 4;
                int unused2 = DJIPlaybackChanneParser.this.packType = buffer[index2] & 15;
                int index3 = index2 + 1;
                System.arraycopy(buffer, index3, DJIPlaybackChanneParser.this.ptsBuffer, 0, 2);
                int ptsIndex = 0 + 2;
                int index4 = index3 + 3;
                System.arraycopy(buffer, index4, DJIPlaybackChanneParser.this.ptsBuffer, ptsIndex, 2);
                int ptsIndex2 = ptsIndex + 2;
                int index5 = index4 + 3;
                System.arraycopy(buffer, index5, DJIPlaybackChanneParser.this.ptsBuffer, ptsIndex2, 2);
                int index6 = index5 + 3;
                System.arraycopy(buffer, index6, DJIPlaybackChanneParser.this.ptsBuffer, ptsIndex2 + 2, 2);
                int index7 = index6 + 2;
                return packLen;
            }

            public void onGetBody(byte[] buffer, int offset, int count) {
                if (DJIPlaybackChanneParser.this.packType == DJIPlaybackType.Video.value()) {
                    FPVController.native_transferVideoDataDirect(buffer, offset, count, DJIPlaybackChanneParser.this.ptsBuffer, DJIPlaybackChanneParser.this.ptsBuffer.length);
                } else if (DJIPlaybackChanneParser.this.packType == DJIPlaybackType.Audio.value()) {
                    ServiceManager.getInstance().createAudioDecoder();
                    FPVController.native_transferAudioData(buffer, offset, count, DJIPlaybackChanneParser.this.ptsBuffer, DJIPlaybackChanneParser.this.ptsBuffer.length);
                } else {
                    DJILogHelper.getInstance().LOGD(DJIPlaybackChanneParser.this.TAG, "packType=" + DJIPlaybackChanneParser.this.packType + " packVer=" + DJIPlaybackChanneParser.this.packVer);
                }
            }
        });
        this.ringBufferParser.setName("Playback");
    }

    public void parse(byte[] buffer, int offset, int count) {
        this.ringBufferParser.parse(buffer, offset, count);
    }

    public enum DJIPlaybackType {
        Video(1),
        Audio(2);
        
        private int mValue = 0;

        private DJIPlaybackType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }
}
