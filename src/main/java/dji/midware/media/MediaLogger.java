package dji.midware.media;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.media.metadata.VideoMetadataManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Locale;

@EXClassNullAway
public class MediaLogger {
    private static boolean DEBUG = false;
    private static String TAG = "MediaLogger";
    private static MediaLogger instance = null;
    private BufferedWriter bufferedWriter = null;
    private FileWriter fileWriter = null;

    private static MediaLogger getInstance() {
        if (instance == null) {
            instance = new MediaLogger();
        }
        return instance;
    }

    private MediaLogger() {
        try {
            this.fileWriter = new FileWriter(VideoMetadataManager.getSourceVideoDirectory() + "MediaLogger.log");
            this.bufferedWriter = new BufferedWriter(this.fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void show(String tag, Object obj) {
        String content = "null";
        if (obj != null) {
            content = obj.toString();
        }
        DJILogHelper.getInstance().LOGD(tag, content, false, true);
        if (DEBUG) {
            Log.i(TAG, content);
        }
    }

    public static void show(String content) {
        DJILogHelper.getInstance().LOGD(TAG, content, false, true);
        if (DEBUG) {
            Log.i(TAG, content);
        }
    }

    public static void show(String tag, String content) {
        DJILogHelper.getInstance().LOGD(TAG, content, false, true);
        if (DEBUG) {
            Log.i(tag, content);
        }
    }

    public static void show(boolean isDebug, String TAG2, String content) {
        if (DEBUG && DJIVideoUtil.isDebug(isDebug)) {
            show(TAG2, content);
        }
    }

    public static void show(Exception e) {
        String content = eToStr(e);
        DJILogHelper.getInstance().LOGD(TAG, content, true, true);
        if (DEBUG) {
            Log.i(TAG, content);
        }
    }

    public static void e(String TAG2, Exception e) {
        if (DEBUG) {
            Log.e(TAG2, eToStr(e));
        }
    }

    public static void e(String TAG2, String content) {
        if (DEBUG) {
            Log.e(TAG2, content);
        }
    }

    public static void e(boolean isDebug, String TAG2, String content) {
        if (DEBUG && DJIVideoUtil.isDebug(isDebug)) {
            e(TAG2, content);
        }
    }

    public static void toPhone(String TAG2, Exception e) {
        DJILogHelper.getInstance().LOGD(TAG2, eToStr(e), false, true);
    }

    public static void toPhoneAndFile(String TAG2, String content) {
        DJILogHelper.getInstance().LOGD(TAG2, content, true, true);
    }

    public static void eToView(String TAG2, Exception e) {
        DJILogHelper.getInstance().LOGD(TAG2, eToStr(e), true, true);
    }

    public static void i(String TAG2, String content) {
        if (DEBUG && DJIVideoUtil.isDebug(true)) {
            Log.i(TAG2, content);
        }
    }

    public static void d(String TAG2, String content) {
        if (DEBUG && DJIVideoUtil.isDebug(true)) {
            Log.d(TAG2, content);
        }
    }

    public static void i(boolean isDebug, String TAG2, String content) {
        if (DEBUG && DJIVideoUtil.isDebug(isDebug)) {
            i(TAG2, content);
        }
    }

    public static void d(boolean isDebug, String TAG2, String content) {
        if (DEBUG && DJIVideoUtil.isDebug(isDebug)) {
            d(TAG2, content);
        }
    }

    public static void iToFile(String TAG2, String content) {
        if (DEBUG) {
            try {
                getInstance().bufferedWriter.write(String.format(Locale.US, "%s [%s]:%s\n", new Date().toString(), TAG2, content));
                getInstance().bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void eToFile(String TAG2, Exception e) {
        eToFile(TAG2, eToStr(e));
    }

    public static String eToStr(Exception e) {
        StringWriter sWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(sWriter));
        return sWriter.toString();
    }

    public static void eToFile(String TAG2, String content) {
        try {
            getInstance().bufferedWriter.write(String.format(Locale.US, "%s [%s]^^^EXCEPTION^^^:%s\n", new Date().toString(), TAG2, content));
            getInstance().bufferedWriter.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void onDestroy() {
        try {
            if (this.bufferedWriter != null) {
                this.bufferedWriter.close();
            }
            if (this.fileWriter != null) {
                this.fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
