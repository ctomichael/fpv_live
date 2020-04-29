package dji.midware.media;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import com.dji.frame.util.V_ActivityUtil;
import com.dji.frame.util.V_DiskUtil;
import com.dji.mapkit.lbs.configuration.Defaults;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.media.metadata.VideoMetadataManager;
import dji.midware.natives.FPVController;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.TreeSet;

@EXClassNullAway
public class DJIVideoUtil {
    public static final int ANDROID_MP4_ENCODER_INPUTSURFACE_MIN_SDK = 18;
    public static final int ANDROID_MP4_MUXER_MIN_SDK = 18;
    public static final String[] AUDIO_ENCODING_FORMAT = {"audio/mp4a-latm", "audio/aac", "audio/mpeg"};
    public static byte[][] AUD_ARR = {new byte[]{0, 0, 0, 1, 9, Tnaf.POW_2_WIDTH}, new byte[]{0, 0, 0, 0, 0, 1, 9, Tnaf.POW_2_WIDTH}, new byte[]{0, 0, 0, 0, 0, 0, 0, 1, 9, Tnaf.POW_2_WIDTH}, new byte[]{0, 0, 0, 1, 12, 0, 0, 0, 1, 9, Tnaf.POW_2_WIDTH}, new byte[]{0, 0, 0, 1, 9, Tnaf.POW_2_WIDTH, 0, 0, 0, 1, 9, Tnaf.POW_2_WIDTH}};
    public static final int EXTRA_PARSING_MEM_SIZE = 1024;
    public static final int FPS = 30;
    private static final double FPS_MEASURE_THRESHOLD = 20.0d;
    private static final long FRAME_INDEX_MAX = 1048575;
    private static final long FRAME_NUM_MAX = 60;
    private static final long FRAME_NUM_MAX_MUX = 63;
    private static final long FRAME_PTS_MAX = 16777215;
    public static final int H264FrameCapacity = 138240;
    private static final int H264FrameCapacity_1080 = 1048576;
    public static final int IFRAME_DURATION_MASK = 31;
    public static final int IFRAME_DURATION_MASK_LARGE = 151;
    private static final String KEY_CROP_BOTTOM = "crop-bottom";
    private static final String KEY_CROP_LEFT = "crop-left";
    private static final String KEY_CROP_RIGHT = "crop-right";
    private static final String KEY_CROP_TOP = "crop-top";
    public static final int KEY_I_FRAME_INTERVAL = 1;
    public static final int MIN_FRAME_RECORD = 150;
    public static int MP4_BIT_RATE = 10485760;
    public static int Midea_Signal_Delay_MSec = 1000;
    public static final int NUM_FRAME_JUMPED = 120;
    public static final int RECORD_VIDEO_HEIGHT = 720;
    public static final int RECORD_VIDEO_WIDTH = 1280;
    public static final int STREAM_MAX_HEIGHT = 720;
    public static final int STREAM_MAX_WIDTH = 1280;
    private static String TAG = "DJIVideoUtil";
    public static final String[] VIDEO_ENCODING_FORMAT = {"video/avc", "video/mp4v-es"};
    public static final int YUVFrameCapacity = 11059200;
    private static Context context = null;
    public static long conversionTimes = 0;
    public static long durationSum = 0;
    private static ArrayList<byte[]> extraBufList = new ArrayList<>(ExtraMemInvokePoint.values().length);
    private static DecimalFormat fileDecimalFormat = new DecimalFormat("#0.#");
    private static DecimalFormat fileIntegerFormat = new DecimalFormat("#0");
    private static final int[] fpsCommonValues = {24, 30, 60};
    public static boolean isAllDebugClose = false;
    public static boolean isAllDebugOpen = false;
    public static boolean isRender = true;
    public static boolean isTranscodeOnline = false;
    private static final int position = 6;
    private static final long start_time = System.currentTimeMillis();
    public static boolean test = false;
    public static boolean useFFMpegForLowSDK = false;

    public static class Resolution {
        public int height;
        public int width;
    }

    public enum ExtraMemInvokePoint {
        DJIMediaPlayer,
        DJIMediaPlayerLitchix,
        H264FileLoader,
        MammothStreamService,
        SwUdpService,
        P3CLiveStreamService,
        UsbHostServiceLBChanneParser,
        UsbHostRC,
        Other
    }

    public enum NalType {
        Slice(1),
        Dpa(2),
        Dpb(3),
        Dpc(4),
        IdrSlice(5),
        Sei(6),
        Sps(7),
        Pps(8),
        Aud(9),
        EndSequence(10),
        EndStream(11),
        FillerData(12),
        SpsExt(13),
        AuxiliarySlice(19),
        FfIgnore(267448321);
        
        private int value;

        public int value() {
            return this.value;
        }

        private NalType(int value2) {
            this.value = value2;
        }
    }

    public static class TreeSetPool<T> {
        private int capacity;
        private TreeSet<T> mTreeSet;

        public TreeSetPool(int capacity2, Comparator<T> comparator) {
            this.capacity = capacity2;
            this.mTreeSet = new TreeSet<>(comparator);
        }

        public synchronized T obtain(T t) {
            T rst;
            rst = this.mTreeSet.ceiling(t);
            if (rst != null) {
                this.mTreeSet.remove(rst);
            }
            return rst;
        }

        public synchronized void recycle(T t) {
            if (t != null) {
                while (this.capacity > 0 && this.mTreeSet.size() > this.capacity) {
                    this.mTreeSet.remove(this.mTreeSet.first());
                }
                if (this.capacity <= 0 || this.mTreeSet.size() != this.capacity) {
                    this.mTreeSet.add(t);
                } else {
                    T firstBuf = this.mTreeSet.first();
                    if (firstBuf == null || this.mTreeSet.comparator().compare(t, firstBuf) > 0) {
                        this.mTreeSet.remove(firstBuf);
                        this.mTreeSet.add(t);
                    }
                }
            }
        }

        public synchronized void clear() {
            this.mTreeSet.clear();
        }
    }

    public static long getComprehensivePts(long ptsMs, long frameIndex, long frameNum) {
        long valid_frameIndex = FRAME_INDEX_MAX;
        long valid_frameNum = 60;
        if (frameIndex <= FRAME_INDEX_MAX) {
            valid_frameIndex = frameIndex;
        }
        long valid_pts = (ptsMs - start_time) & FRAME_PTS_MAX;
        if (frameNum <= 60) {
            valid_frameNum = frameNum;
        }
        return (((valid_frameIndex << 24) + valid_pts) << 6) + valid_frameNum;
    }

    public static long getPtsMs(long comPts) {
        return start_time + ((268435440 & comPts) >>> 6);
    }

    public static int getFrameIndex(long comPts) {
        return (int) ((281474708275200L & comPts) >>> 30);
    }

    public static int getFrameNum(long comPts) {
        return (int) (FRAME_NUM_MAX_MUX & comPts);
    }

    public static int getH264FrameMaxSize(int width, int height, int bitRate) {
        return 1258300;
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDirPackgetPath(String shortFileName) {
        if (getContext() == null) {
            return null;
        }
        return V_DiskUtil.getExternalCacheDirPath(getContext(), shortFileName);
    }

    public static String getOutputFileNameWithoutSuffix() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + "_Cache";
    }

    public static int getFPS() {
        DJIVideoDecoder decoder = DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface().getDJIVideoDecoder();
        if (decoder == null) {
            return 30;
        }
        double avgFps = 1000.0d / decoder.getFrameQueueinIntervalAvgValue();
        if (avgFps < 0.0d) {
            DJILogHelper.getInstance().LOGE(TAG, "getFPS: cannot get real time fps");
            return 30;
        }
        for (int i = 0; i < fpsCommonValues.length - 1; i++) {
            if (avgFps < ((double) fpsCommonValues[i]) + ((((double) (fpsCommonValues[i + 1] - fpsCommonValues[i])) / 100.0d) * FPS_MEASURE_THRESHOLD)) {
                return fpsCommonValues[i];
            }
        }
        return fpsCommonValues[fpsCommonValues.length - 1];
    }

    public static double getDurationPerFrame() {
        return 1000.0d / ((double) getFPS());
    }

    public static long getDurationPerFrameUs() {
        return (long) (Defaults.SECOND_IN_NANOS / getFPS());
    }

    public static long getPresentationTimeUs(int indexOfFrame) {
        return getDurationPerFrameUs() * ((long) indexOfFrame);
    }

    public static boolean checkAndCreateDirectory(String fullPath) {
        File dir = new File(fullPath);
        if (dir.exists()) {
            return true;
        }
        boolean success = dir.mkdirs();
        if (success) {
            return success;
        }
        MediaLogger.show("can't create the directory " + fullPath);
        return success;
    }

    public static void setContext(Context context2) {
        if (ServiceManager.getInstance() == null) {
            context = context2;
        }
    }

    public static Context getContext() {
        ServiceManager.getInstance();
        Context re = ServiceManager.getContext();
        return re != null ? re : context;
    }

    public static boolean isDebug() {
        return isDebug(true);
    }

    public static boolean isDebug(boolean otherSwitch) {
        Context context2 = getContext();
        if (context2 != null && !V_ActivityUtil.isApkDebugable(context2)) {
            return false;
        }
        if (isAllDebugClose) {
            return false;
        }
        if (isAllDebugOpen) {
            return true;
        }
        return otherSwitch;
    }

    public static boolean isVideoFromDroneSD(String filePath) {
        File file;
        String name;
        if (filePath == null || (file = new File(filePath)) == null || (name = file.getName()) == null) {
            return false;
        }
        String name2 = name.toLowerCase(Locale.US);
        if (name2.startsWith("org_") || name2.startsWith("dji_")) {
            return true;
        }
        return false;
    }

    @TargetApi(18)
    public static int findBestColorFormat(MediaCodec codec) {
        int re;
        int[] supportedColors = codec.getCodecInfo().getCapabilitiesForType(VIDEO_ENCODING_FORMAT[0]).colorFormats;
        Arrays.sort(supportedColors);
        MediaLogger.show(codec.getName() + " supports the colors: " + Arrays.toString(supportedColors));
        if (Arrays.binarySearch(supportedColors, 19) >= 0) {
            re = 19;
        } else if (Arrays.binarySearch(supportedColors, 21) >= 0) {
            re = 21;
        } else {
            re = supportedColors[0];
        }
        Log.i(TAG, codec.getName() + "'s best color is: " + re);
        return re;
    }

    @TargetApi(18)
    public static int findBestColorFormat(MediaCodec encoder, MediaCodec decoder) {
        int[] encoderColors = encoder.getCodecInfo().getCapabilitiesForType(VIDEO_ENCODING_FORMAT[0]).colorFormats;
        Log.i(TAG, encoder.getName() + " supports the colors: " + Arrays.toString(encoderColors));
        HashSet<Integer> enSet = new HashSet<>();
        for (int i : encoderColors) {
            enSet.add(Integer.valueOf(i));
        }
        int[] decoderColors = decoder.getCodecInfo().getCapabilitiesForType(VIDEO_ENCODING_FORMAT[0]).colorFormats;
        Log.i(TAG, decoder.getName() + " supports the colors: " + Arrays.toString(decoderColors));
        HashSet<Integer> deSet = new HashSet<>();
        for (int i2 : decoderColors) {
            deSet.add(Integer.valueOf(i2));
        }
        int re = -1;
        if (enSet.contains(21) && deSet.contains(21)) {
            re = 21;
        } else if (!enSet.contains(19) || !deSet.contains(19)) {
            enSet.retainAll(deSet);
            if (!enSet.isEmpty()) {
                re = ((Integer) enSet.toArray()[0]).intValue();
            }
        } else {
            re = 19;
        }
        Log.i(TAG, "Found common color: " + re);
        return re;
    }

    public static void writeBufferToFile(ByteBuffer byteBuffer, int offset, int size, String fileName) {
        byte[] temp = new byte[size];
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(VideoMetadataManager.getSourceVideoDirectory() + fileName);
            try {
                BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
                try {
                    byteBuffer.position(offset);
                    byteBuffer.limit(offset + size);
                    byteBuffer.get(temp, 0, size);
                    byteBuffer.clear();
                    bos.write(temp, 0, size);
                    bos.flush();
                    bos.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    Log.i(TAG, fileName + " saved done");
                }
            } catch (IOException e2) {
                e = e2;
                e.printStackTrace();
                Log.i(TAG, fileName + " saved done");
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
            Log.i(TAG, fileName + " saved done");
        }
        Log.i(TAG, fileName + " saved done");
    }

    public static int[] getYUVSupportColors() {
        return new int[]{19, 21, 2141391876, 2141391872};
    }

    public static void YUVFormatConvert(byte[] src, byte[] dst, int w, int h) {
        YUVFormatConvert(src, 0, dst, 0, w, h, 19);
    }

    public static Resolution getResolutionReliably(MediaFormat format) {
        int integer;
        int integer2;
        Resolution re = new Resolution();
        boolean hasCrop = format.containsKey(KEY_CROP_RIGHT) && format.containsKey(KEY_CROP_LEFT) && format.containsKey(KEY_CROP_BOTTOM) && format.containsKey(KEY_CROP_TOP);
        if (hasCrop) {
            integer = (format.getInteger(KEY_CROP_RIGHT) - format.getInteger(KEY_CROP_LEFT)) + 1;
        } else {
            integer = format.getInteger("width");
        }
        re.width = integer;
        if (hasCrop) {
            integer2 = (format.getInteger(KEY_CROP_BOTTOM) - format.getInteger(KEY_CROP_TOP)) + 1;
        } else {
            integer2 = format.getInteger("height");
        }
        re.height = integer2;
        return re;
    }

    public static void YUVFormatConvert(ByteBuffer src, ByteBuffer dst, int w, int h, int targetFormat) {
        long start_time2 = System.currentTimeMillis();
        FPVController.native_transcodeYUVConvert(src, dst, w, h, targetFormat);
        durationSum += System.currentTimeMillis() - start_time2;
        conversionTimes++;
    }

    public static void YUVFormatConvert(byte[] src, int src_offset, byte[] dst, int dst_offset, int w, int h, int format) {
        int uPos;
        int vPos;
        int vPos2;
        int uPos2;
        int pos;
        int pos2;
        int yPos = dst_offset;
        switch (format) {
            case 21:
                uPos = yPos + (w * h);
                vPos = uPos + 1;
                break;
            case 2141391872:
            case 2141391876:
                uPos = (w * h) + yPos + 20480;
                vPos = uPos + 1;
                break;
            default:
                uPos = yPos + (w * h);
                vPos = uPos + ((w * h) / 4);
                break;
        }
        int pos3 = src_offset;
        int height = 0;
        while (height < h) {
            int width = 0;
            while (true) {
                int pos4 = pos;
                int vPos3 = vPos2;
                int uPos3 = uPos2;
                int yPos2 = yPos;
                if (width < w) {
                    yPos = yPos2 + 1;
                    int pos5 = pos4 + 1;
                    dst[yPos2] = src[pos4];
                    if (height % 2 == 0 && width % 2 == 0) {
                        switch (format) {
                            case 21:
                            case 2141391872:
                            case 2141391876:
                                int pos6 = pos5 + 1;
                                dst[uPos3] = src[pos5];
                                uPos2 = uPos3 + 2;
                                pos2 = pos6 + 1;
                                dst[vPos3] = src[pos6];
                                vPos2 = vPos3 + 2;
                                break;
                            default:
                                uPos2 = uPos3 + 1;
                                int pos7 = pos5 + 1;
                                dst[uPos3] = src[pos5];
                                vPos2 = vPos3 + 1;
                                pos2 = pos7 + 1;
                                dst[vPos3] = src[pos7];
                                break;
                        }
                        pos = pos2 + 1;
                    } else {
                        pos = pos5 + 3;
                        vPos2 = vPos3;
                        uPos2 = uPos3;
                    }
                    width++;
                } else {
                    height++;
                    pos3 = pos4;
                    vPos = vPos3;
                    uPos = uPos3;
                    yPos = yPos2;
                }
            }
        }
    }

    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static long getAvailableExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return -1;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    public static long getTotalExternalMemorySize() {
        if (!externalMemoryAvailable()) {
            return -1;
        }
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }

    public static String formatStorage(long size, boolean isInteger) {
        DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
        if (size < PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID && size > 0) {
            return df.format((double) size) + "B";
        }
        if (size < 1048576) {
            return df.format(((double) size) / 1024.0d) + "KB";
        }
        if (size < 1073741824) {
            return df.format(((double) size) / 1048576.0d) + "MB";
        }
        return df.format(((double) size) / 1.073741824E9d) + "GB";
    }

    public static byte[] extraMemForParsing(ExtraMemInvokePoint invokePoint, byte[] src, int offset, int length) {
        if (extraBufList.size() == 0) {
            for (int i = 0; i < ExtraMemInvokePoint.values().length; i++) {
                extraBufList.add(null);
            }
        }
        byte[] extraBufForParsing = extraBufList.get(invokePoint.ordinal());
        int bufSize = (length - offset) + 1024;
        if (extraBufForParsing == null || extraBufForParsing.length < bufSize) {
            extraBufForParsing = new byte[bufSize];
            extraBufList.set(invokePoint.ordinal(), extraBufForParsing);
        } else {
            Arrays.fill(extraBufForParsing, (byte) 0);
        }
        System.arraycopy(src, offset, extraBufForParsing, 0, length);
        return extraBufForParsing;
    }

    public static int findNextNal(byte[] data, int start, int length) {
        if (data == null || start < 0 || length <= 0 || start + length > data.length) {
            return -1;
        }
        int curStatus = 0;
        int lastIndex = start + length;
        for (int i = start; i < lastIndex; i++) {
            switch (data[i] & 255) {
                case 0:
                    if (curStatus >= 2) {
                        break;
                    } else {
                        curStatus++;
                        break;
                    }
                case 1:
                    if (curStatus != 2) {
                        curStatus = 0;
                        break;
                    } else {
                        return i - 2;
                    }
                default:
                    curStatus = 0;
                    break;
            }
        }
        return -1;
    }

    public static int findFrameHead(byte[] data, int start, int length) {
        int nalType;
        int curIndex = start;
        while (true) {
            int curIndex2 = findNextNal(data, curIndex, (length - curIndex) + start);
            if (curIndex2 <= 0) {
                return -1;
            }
            if (curIndex2 + 3 < data.length && ((nalType = data[curIndex2 + 3] & 31) == NalType.Sps.value() || nalType == NalType.Pps.value() || nalType == NalType.Slice.value() || nalType == NalType.IdrSlice.value())) {
                return curIndex2;
            }
            curIndex = curIndex2 + 4;
        }
    }

    public static boolean isCurrentStreamGop() {
        DJIVideoDecoder decoder;
        if (DJIProductManager.getInstance().getType() == ProductType.WM240 || DJIProductManager.getInstance().getType() == ProductType.WM245 || (decoder = ServiceManager.getInstance().getDecoder()) == null || DJIVideoDecoder.getIframeRawId(DJIProductManager.getInstance().getType(), decoder.getVideoWidth(), decoder.getVideoHeight()) != -1) {
            return false;
        }
        return true;
    }

    public static String dataToString(byte[] data, int start, int len) {
        if (data == null || data.length <= start) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        int i = start;
        while (i < start + len && i < data.length) {
            if (i != start) {
                sb.append(", ");
            }
            sb.append("0x").append(Integer.toHexString(data[i] & 255));
            i++;
        }
        return sb.toString();
    }
}
