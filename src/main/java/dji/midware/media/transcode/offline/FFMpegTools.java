package dji.midware.media.transcode.offline;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public interface FFMpegTools {
    double h264ToMp4ConvertorGetProgress();

    void h264ToMp4ConvertorInit();

    int h264ToMp4ConvertorStart(String str, String str2, int i);

    void h264ToMp4ConvertorStop();
}
