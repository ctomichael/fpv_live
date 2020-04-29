package dji.midware.media.colors;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;
import dji.midware.natives.FPVController;

@EXClassNullAway
public class ColorFormatConvFactory {
    public static ColorFormatConv createColorFormatConv(int decoderColor, int encoderColor) {
        ColorFormatConv re = null;
        if (decoderColor == 21 && encoderColor == 19) {
            re = new ColorFormatConv() {
                /* class dji.midware.media.colors.ColorFormatConvFactory.AnonymousClass1 */

                public void convert(byte[] source, byte[] target, int width, int height) {
                    int nLenY = width * height;
                    int nLenU = nLenY / 4;
                    System.arraycopy(source, 0, target, 0, width * height);
                    for (int i = 0; i < nLenU; i++) {
                        target[nLenY + i] = source[(i * 2) + nLenY];
                        target[nLenY + nLenU + i] = source[(i * 2) + nLenY + 1];
                    }
                }
            };
        } else if (decoderColor != 21 || encoderColor == 19) {
        }
        if (re == null) {
            MediaLogger.show(new Exception("color convertor missing:" + decoderColor + "->" + encoderColor));
        }
        return re;
    }

    public static byte[] YV12toYUV420PackedSemiPlanar(byte[] input, byte[] output, int width, int height) {
        int frameSize = width * height;
        int qFrameSize = frameSize / 4;
        System.arraycopy(input, 0, output, 0, frameSize);
        for (int i = 0; i < qFrameSize; i++) {
            output[(i * 2) + frameSize] = input[frameSize + i + qFrameSize];
            output[(i * 2) + frameSize + 1] = input[frameSize + i];
        }
        return output;
    }

    public static byte[] YV12toYUV420Planar(byte[] input, byte[] output, int width, int height) {
        int frameSize = width * height;
        int qFrameSize = frameSize / 4;
        System.arraycopy(input, 0, output, 0, frameSize);
        System.arraycopy(input, frameSize, output, frameSize + qFrameSize, qFrameSize);
        System.arraycopy(input, frameSize + qFrameSize, output, frameSize, qFrameSize);
        return output;
    }

    public static void swapYV12toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height) {
        for (int i = 0; i < width * height; i++) {
            i420bytes[i] = yv12bytes[i];
        }
        for (int i2 = width * height; i2 < (width * height) + (((width / 2) * height) / 2); i2++) {
            i420bytes[i2] = yv12bytes[(((width / 2) * height) / 2) + i2];
        }
        for (int i3 = (width * height) + (((width / 2) * height) / 2); i3 < (width * height) + ((((width / 2) * height) / 2) * 2); i3++) {
            i420bytes[i3] = yv12bytes[i3 - (((width / 2) * height) / 2)];
        }
    }

    public static int convertFromYUVXToAnyYUV(Object src, Object dst, int w, int h, int targetFormat) {
        return FPVController.native_transcodeYUVConvert(src, dst, w, h, targetFormat);
    }
}
