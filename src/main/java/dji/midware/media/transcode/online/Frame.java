package dji.midware.media.transcode.online;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.datacontainer.DynamicByteBuffer;
import kotlin.jvm.internal.ByteCompanionObject;
import org.msgpack.core.MessagePack;

@EXClassNullAway
public class Frame {
    private final DynamicByteBuffer data;
    private long frameIndex;
    int height;
    private boolean isKeyFrame = false;
    public final FrameType type;
    int width;

    public enum FrameType {
        YUV,
        H264
    }

    public int getSize() {
        return this.data.size();
    }

    public DynamicByteBuffer getBuffer() {
        return this.data;
    }

    public long getIndex() {
        return this.frameIndex;
    }

    public void setIndex(long index) {
        this.frameIndex = index;
    }

    public boolean isKeyFrame() {
        return this.isKeyFrame;
    }

    public void setIsKeyFrame(boolean isKeyFrame2) {
        this.isKeyFrame = isKeyFrame2;
    }

    /* access modifiers changed from: protected */
    public void convertRGBAtoYUVFullSwing(byte[] yuv420sp, byte[] argb, int width2, int height2) {
        int uvIndex;
        int yIndex;
        int yIndex2 = 0;
        int uvIndex2 = width2 * height2;
        int index = 0;
        int j = 0;
        while (j < height2) {
            int i = 0;
            while (true) {
                uvIndex = uvIndex2;
                yIndex = yIndex2;
                if (i >= width2) {
                    break;
                }
                byte b = argb[(index << 2) + 2];
                byte b2 = argb[(index << 2) + 1];
                byte b3 = argb[(index << 2) + 0];
                int Y = ((((b * 76) + (b2 * 150)) + (b3 * 29)) + 128) >> 8;
                int U = (((((b * MessagePack.Code.FIXEXT2) - (b2 * 84)) + (b3 * ByteCompanionObject.MAX_VALUE)) + 128) >> 8) + 128;
                int V = (((((b * ByteCompanionObject.MAX_VALUE) - (b2 * 106)) - (b3 * 21)) + 128) >> 8) + 128;
                yIndex2 = yIndex + 1;
                if (Y < 0) {
                    Y = 0;
                } else if (Y > 255) {
                    Y = 255;
                }
                yuv420sp[yIndex] = (byte) Y;
                if (j % 2 == 0 && index % 2 == 0) {
                    int uvIndex3 = uvIndex + 1;
                    if (V < 0) {
                        V = 0;
                    } else if (V > 255) {
                        V = 255;
                    }
                    yuv420sp[uvIndex] = (byte) V;
                    uvIndex = uvIndex3 + 1;
                    if (U < 0) {
                        U = 0;
                    } else if (U > 255) {
                        U = 255;
                    }
                    yuv420sp[uvIndex3] = (byte) U;
                }
                uvIndex2 = uvIndex;
                index++;
                i++;
            }
            j++;
            uvIndex2 = uvIndex;
            yIndex2 = yIndex;
        }
    }

    public Frame(FrameType type2, int capacity) {
        this.type = type2;
        switch (type2) {
            case H264:
                this.data = new DynamicByteBuffer(capacity, false);
                return;
            case YUV:
                this.data = new DynamicByteBuffer(capacity, true);
                return;
            default:
                this.data = null;
                throw new RuntimeException("unknown frame type");
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Frame)) {
            return false;
        }
        Frame objFrame = (Frame) obj;
        if (this.frameIndex == objFrame.frameIndex && this.type == objFrame.type) {
            return true;
        }
        return false;
    }

    public void setResolution(int decoderOutWidth, int decoderOutHeight) {
        this.width = decoderOutWidth;
        this.height = decoderOutHeight;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
