package dji.midware.util.save;

import android.content.Context;
import android.os.SystemClock;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.log.GlobalConfig;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.MediaLogger;
import dji.midware.natives.FPVController;
import dji.midware.stat.StatService;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.BytesUtil;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@EXClassNullAway
public class TestUtil {
    public static String GPIOTAG = "GPIO_TAG";
    public static boolean LOAD_STREAM_FROM_FILE = false;
    public static String TAG = "TestUtil";
    public static boolean TEST_GPIO_DJIVideoDataRecver = false;
    public static final boolean TEST_GPIO_UsbAccessoryService = false;
    public static final String VIDEO_TEMPORARY_TEST_TAG = "VideoTest";
    private static int gpio_frame_count = 0;
    private static DataInputStream gpio_in = null;
    private static DataOutputStream gpio_out = null;
    private static int test_gpio_w = 0;

    public static void openStatistics() {
        StatService.OPEN = true;
    }

    public static void openDelayMeasure() {
        openStatistics();
        StreamDelaySaver.IS_SAVE_PACKET_DELAY = true;
    }

    public static void openStreamingDebug() {
        if (GlobalConfig.DEBUG) {
            openStatistics();
            StreamSaver.SAVE_usbHybridDataStream_Open = true;
            StreamSaver.SAVE_videoCodecQueueIn_Open = true;
            StreamSaver.SAVE_videoDataReceiver_Open = true;
            StreamSaver.SAVE_videoUsb_Open = true;
            UsbAccessoryService.STREAM_DEBUG = true;
        }
    }

    public static void closeStreamingDebug() {
    }

    public static void checkDebugSwitch(Context context) {
        if (GlobalConfig.DEBUG) {
            if (new File("/sdcard/djidebug").exists()) {
                openStreamingDebug();
            } else if (new File("/sdcard/Djidebug").exists()) {
                openStreamingDebug();
            }
            if (new File("/sdcard/dji_videostream.h264").exists()) {
                LOAD_STREAM_FROM_FILE = true;
            } else if (new File("/sdcard/dji_videostream.h264r").exists()) {
                LOAD_STREAM_FROM_FILE = true;
            }
            if (new File("/sdcard/djidebugdelay").exists()) {
                openDelayMeasure();
            } else if (new File("/sdcard/Djidebugdelay").exists()) {
                openDelayMeasure();
            }
        }
    }

    public static void writeGPIO(byte[] videoBuffer) {
        int test_gpio_r = -1;
        if (gpio_frame_count == 0) {
            MediaLogger.i(GPIOTAG, "testing GPIO");
        }
        gpio_frame_count++;
        if (gpio_frame_count % 100 == 0) {
            test_gpio_w = 1;
        } else {
            test_gpio_w = 0;
        }
        long t1 = SystemClock.uptimeMillis();
        try {
            gpio_out = new DataOutputStream(new FileOutputStream("/sys/dwc3_test/test_gpio"));
            gpio_out.writeBytes(test_gpio_w + "");
            gpio_out.close();
        } catch (Exception e) {
            MediaLogger.e(GPIOTAG, e);
        }
        try {
            gpio_in = new DataInputStream(new FileInputStream("/sys/dwc3_test/test_gpio"));
            try {
                test_gpio_r = Integer.parseInt(gpio_in.readLine());
            } catch (Exception e2) {
                MediaLogger.e(GPIOTAG, e2);
            }
            gpio_in.close();
        } catch (Exception e3) {
            MediaLogger.e(GPIOTAG, e3);
        }
        if (test_gpio_r != test_gpio_w) {
            MediaLogger.e(GPIOTAG, String.format(Locale.US, "test_gpio_r %d !=test_gpio_w %d. frame_count=%d", Integer.valueOf(test_gpio_r), Integer.valueOf(test_gpio_w), Integer.valueOf(gpio_frame_count)));
            return;
        }
        MediaLogger.i(GPIOTAG, "test_gpio=" + test_gpio_r + " frame_count = " + gpio_frame_count + " content=" + BytesUtil.byte2hex(Arrays.copyOf(videoBuffer, 18)) + " duration=" + (SystemClock.uptimeMillis() - t1));
    }

    public static void outputTestStream(String fileName, byte[] buf, int size) {
        StreamSaver.getInstance(fileName).write(buf, 0, size);
    }

    public static void outputTestStream(String fileName, ByteBuffer buf, int offset, int size) {
        int position = buf.position();
        int limit = buf.limit();
        buf.position(offset);
        buf.limit(size + offset);
        byte[] byteData = new byte[size];
        buf.get(byteData);
        buf.limit(limit);
        buf.position(position);
        outputTestStream(fileName, byteData, size);
    }

    public static void startStreamFromFileInput() {
        ServiceManager.getInstance().setIsFix(true);
        FPVController.native_setIsNeedRawData(false);
        FPVController.native_setFrameRate(30);
        FPVController.native_startRecvThread();
    }

    public static void stopStreamFromFileInput() {
        FPVController.native_stopRecvThread();
    }

    public static void startStreamFromFileInput(String filePath, boolean isSecondary) {
        ServiceManager.getInstance().setIsFix(true);
        FPVController.native_setIsNeedRawData(false);
        FPVController.native_setFrameRate(30);
        FPVController.native_startRecvThread2(filePath, isSecondary);
    }

    public static void stopStreamFromFileInput(boolean isSecondary) {
        FPVController.native_stopRecvThread2(isSecondary);
    }

    public static void log2File(String tag, String log) {
        DJILogHelper.getInstance().LOGD(tag, getTimeString() + ": " + log, tag);
    }

    public static String getTimeString() {
        return new SimpleDateFormat("HHmmss").format(new Date());
    }
}
