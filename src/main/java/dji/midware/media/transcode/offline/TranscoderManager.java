package dji.midware.media.transcode.offline;

import android.os.Build;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.media.DJIVideoUtil;

@EXClassNullAway
public class TranscoderManager {
    public static String TAG = "TranscoderManager";
    public static FFMpegTools ffmpegTools;
    private static TranscoderInterface instance = null;
    public static byte[] pps;
    public static byte[] sps;

    private static synchronized TranscoderInterface getInstance() {
        TranscoderInterface transcoderInterface;
        synchronized (TranscoderManager.class) {
            if (instance != null) {
                transcoderInterface = instance;
            } else if (DJIVideoUtil.test) {
                instance = new TranscoderFFmpeg();
                transcoderInterface = instance;
            } else if (Build.VERSION.SDK_INT >= 18) {
                instance = new TranscoderAndroid();
                transcoderInterface = instance;
            } else if (DJIVideoUtil.useFFMpegForLowSDK) {
                instance = new TranscoderFFmpeg();
                transcoderInterface = instance;
            } else {
                instance = new TranscoderAndroid();
                transcoderInterface = instance;
            }
        }
        return transcoderInterface;
    }

    public static synchronized void destroy() {
        synchronized (TranscoderManager.class) {
            if (instance != null) {
                instance.onDestroy();
                instance = null;
            }
        }
    }

    public static void start(String inputFileName, String outputFileName, TranscoderListener listener) {
        getInstance().start(inputFileName, outputFileName, listener);
    }

    public static void stop() {
        if (instance != null) {
            getInstance().stop();
        }
    }

    public static boolean isTranscoding(String h264FullPath) {
        if (h264FullPath != null && instance != null && h264FullPath.equals(instance.getInputFileName()) && instance.isTranscoding()) {
            return true;
        }
        return false;
    }

    public static int getCurProgress(String h264FullPath) {
        if (isTranscoding(h264FullPath)) {
            return instance.getCurProgress();
        }
        return 0;
    }

    public static void onUIDeactive() {
        if (instance != null) {
            instance.rebindListener(null);
        }
    }

    public static boolean isTranscoding() {
        return instance != null && instance.isTranscoding();
    }

    public static void rebindListener(TranscoderListener listener) {
        if (instance != null) {
            instance.rebindListener(listener);
        }
    }

    public static void setFFMpegTools(FFMpegTools tools) {
        ffmpegTools = tools;
    }
}
