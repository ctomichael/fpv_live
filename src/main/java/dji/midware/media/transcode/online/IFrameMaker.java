package dji.midware.media.transcode.online;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.MediaLogger;
import dji.midware.natives.FPVController;
import java.nio.ByteBuffer;

@EXClassNullAway
public class IFrameMaker {
    ByteBuffer encodeJNIBuffer = ByteBuffer.allocateDirect(11059200);
    private boolean isInit = false;

    public synchronized void init(byte[] sps_pps_frame, int androidColor, int width, int height) {
        if (this.isInit) {
            deinit();
        }
        this.isInit = true;
        try {
            this.encodeJNIBuffer.clear();
            this.encodeJNIBuffer.put(sps_pps_frame);
            MediaLogger.show("init encoder/IFrameMaker with color=" + androidColor + " width=" + width + " height=" + height);
            Log.i(OnlineTranscoder.TAG_Internal, "transcode_x264 init result: " + FPVController.TranscodeResult.find(FPVController.native_transcodeX264Init(this.encodeJNIBuffer, sps_pps_frame.length, androidColor, width, height)).toString());
            Log.i(OnlineTranscoder.TAG_Internal, "transcode_x264 start result: " + FPVController.TranscodeResult.find(FPVController.native_transcodeX264Start()).toString());
        } catch (Exception e) {
            Log.e(OnlineTranscoder.TAG_Internal, MediaLogger.eToStr(e));
        } catch (Error er) {
            Log.e(OnlineTranscoder.TAG_Internal, er.getMessage());
        }
        return;
    }

    public synchronized void deinit() {
        if (this.isInit) {
            this.isInit = false;
            try {
                Log.i(OnlineTranscoder.TAG_Internal, "transcode_x264 stop result: " + FPVController.TranscodeResult.find(FPVController.native_transcodeX264Stop()).toString());
                Log.i(OnlineTranscoder.TAG_Internal, "transcode_x264 deinit result: " + FPVController.TranscodeResult.find(FPVController.native_transcodeX264Deinit()).toString());
            } catch (Exception e) {
                Log.e(OnlineTranscoder.TAG_Internal, MediaLogger.eToStr(e));
            } catch (Error er) {
                Log.e(OnlineTranscoder.TAG_Internal, er.getMessage());
            }
        }
        return;
    }

    public synchronized void convertFromYUVtoIDR(Frame frame) {
        if (!this.isInit) {
            Log.e(OnlineTranscoder.TAG_Internal, "can't convert YUF to I-frame because IFrameMaker hasn't been initialized. ");
        } else {
            long startTime = System.currentTimeMillis();
            Log.i(OnlineTranscoder.TAG_Internal, "Before converting IDR index=" + frame.getIndex() + " data size=" + frame.getSize() + " time=" + startTime);
            try {
                this.encodeJNIBuffer.clear();
                int re = FPVController.native_transcodeX264Encode(frame.getBuffer().lockAndReadData(), frame.getSize(), this.encodeJNIBuffer);
                frame.getBuffer().unLock();
                if (re < 0) {
                    Log.i(OnlineTranscoder.TAG_Internal, "transcode_x264 convert result: " + FPVController.TranscodeResult.find(re).toString());
                } else {
                    Log.i(OnlineTranscoder.TAG_Internal, "transcode_x264 convert result: SUCCESS. re=" + re);
                    this.encodeJNIBuffer.position(0);
                    this.encodeJNIBuffer.limit(re);
                    frame.getBuffer().setData(this.encodeJNIBuffer);
                }
            } catch (Exception e) {
                Log.e(OnlineTranscoder.TAG_Internal, MediaLogger.eToStr(e));
            } catch (Error er) {
                Log.e(OnlineTranscoder.TAG_Internal, er.getMessage());
            }
            Log.i(OnlineTranscoder.TAG_Internal, "after converting IDR index=" + frame.getIndex() + " data size=" + frame.getSize() + " execution duration (ms)=" + (System.currentTimeMillis() - startTime));
        }
    }
}
