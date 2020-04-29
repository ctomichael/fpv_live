package dji.midware.util.save;

import android.os.Environment;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogUtils;
import dji.midware.media.MediaLogger;
import dji.midware.usb.P3.AoaReportHelper;
import dji.midware.util.BytesUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

@EXClassNullAway
public class StreamSaver {
    private static final ThreadLocal<SimpleDateFormat> DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
        /* class dji.midware.util.save.StreamSaver.AnonymousClass1 */

        {
            set(new SimpleDateFormat("mm_ss:SSS"));
        }
    };
    public static boolean SAVE_WM230VideoDebug_Open = false;
    public static boolean SAVE_usbHybridDataStream_Open = false;
    public static boolean SAVE_usbHybridDataType_Open = false;
    public static final String SAVE_videoCodecQueueIn_Name = "dji_video_codecqueuein";
    public static boolean SAVE_videoCodecQueueIn_Open = false;
    public static final String SAVE_videoDataReceiver_Name = "dji_video_datareceiver";
    public static boolean SAVE_videoDataReceiver_Open = false;
    public static final String SAVE_videoUsb_Name = "dji_video_usbaccessary";
    public static boolean SAVE_videoUsb_Open = false;
    public static final String SAVE_videoWifi_Name = "dji_video_wifi";
    public static final String Save_usbHybridDataStream_Name = "dji_usbHybridDataStream";
    public static final String Save_usbHybridDataType_Name = "dji_usbHybridDataType";
    public static String Save_wm230VideoDebug_Name;
    public static String TAG = "StreamSaver";
    public static String VIDEO_LOG_TAG = "DJIVideo";
    private static HashMap<String, StreamSaver> instanceSet = new HashMap<>();
    public static boolean videoDebugEnabledUsedInSDK = false;
    private final String name;
    private int saveToFileFrameIndex = 0;
    private final String saveVideoPath;
    private int sizeToFileSizeSum = 0;
    private FileOutputStream videoStreamFrameFile = null;
    private FileOutputStream videoStreamIndexFile = null;

    public static void setSAVE_usbHybridDataStream_Open(boolean usbHybridDataStream_Open) {
        if (usbHybridDataStream_Open != SAVE_usbHybridDataStream_Open) {
            if (usbHybridDataStream_Open) {
                DJILog.logWriteD(AoaReportHelper.TAG_CONNECT_DEBUG, "A lot of data, no video fps, start save raw aoa data...", AoaReportHelper.TAG_CONNECT_DEBUG, new Object[0]);
            } else {
                DJILog.logWriteD(AoaReportHelper.TAG_CONNECT_DEBUG, "A lot of data, no video fps, stop!!!! saving raw aoa data...", AoaReportHelper.TAG_CONNECT_DEBUG, new Object[0]);
            }
            SAVE_usbHybridDataStream_Open = usbHybridDataStream_Open;
        }
    }

    public static StreamSaver getInstance(String name2) {
        StreamSaver ss = instanceSet.get(name2);
        if (ss != null) {
            return ss;
        }
        StreamSaver ss2 = new StreamSaver(name2);
        instanceSet.put(name2, ss2);
        return ss2;
    }

    public StreamSaver(String name2) {
        this.name = name2;
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/dji_video_test");
        if (!file.exists()) {
            file.mkdir();
        }
        this.saveVideoPath = Environment.getExternalStorageDirectory().getPath() + "/dji_video_test/" + name2;
        Log.e(TAG, "creating StreamSaver[" + name2 + "]: " + this.saveVideoPath);
    }

    public void write(byte[] data, int offset, int size) {
        try {
            if (this.videoStreamFrameFile == null) {
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/dji_video_test");
                if (!file.exists()) {
                    file.mkdir();
                }
                this.videoStreamFrameFile = new FileOutputStream(new File(this.saveVideoPath + DJILogUtils.formatNow("yyyy-MM-dd+HH-mm-ss")));
            }
            if (this.videoStreamIndexFile == null) {
                this.videoStreamIndexFile = new FileOutputStream(new File(this.saveVideoPath + DJILogUtils.formatNow("yyyy-MM-dd+HH-mm-ss") + ".index"));
            }
            if (this.videoStreamFrameFile != null && this.videoStreamIndexFile != null) {
                this.videoStreamFrameFile.write(data, offset, size);
                StringBuilder sb = new StringBuilder();
                Locale locale = Locale.US;
                Object[] objArr = new Object[4];
                objArr[0] = DATE_FORMAT.get().format(new Date());
                objArr[1] = Integer.valueOf(size);
                objArr[2] = Integer.valueOf(this.sizeToFileSizeSum);
                objArr[3] = BytesUtil.byte2hex(Arrays.copyOfRange(data, offset, size < 16 ? size : 16));
                this.videoStreamIndexFile.write(sb.append(String.format(locale, "time=%s size=%d offset=%x first=[%s]", objArr)).append("\n").toString().getBytes());
                this.saveToFileFrameIndex++;
                this.sizeToFileSizeSum += size;
                this.videoStreamFrameFile.flush();
                this.videoStreamIndexFile.flush();
            }
        } catch (Exception e) {
            MediaLogger.e(getTAG(), e);
        }
    }

    private String getTAG() {
        return TAG + "_" + this.name;
    }

    public void resetSavedFileState() {
        try {
            if (this.videoStreamFrameFile != null) {
                this.videoStreamFrameFile.flush();
                this.videoStreamFrameFile.close();
                this.videoStreamFrameFile = null;
            }
            if (this.videoStreamIndexFile != null) {
                this.videoStreamIndexFile.flush();
                this.videoStreamIndexFile.close();
                this.videoStreamIndexFile = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        resetSavedFileState();
        instanceSet.remove(this.name);
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        destroy();
    }
}
