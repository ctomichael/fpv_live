package dji.midware.media.mp4;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

@EXClassNullAway
public class MP4Parser {
    private static final int LENGTH_BOX_SIZE = 4;
    private static final int LENGTH_BOX_TYPE = 4;
    private static final int LENGTH_PRE_READ = 100;
    private static final String TAG = MP4Parser.class.getName();
    private static MP4Parser instance;
    private boolean debug = false;
    private RandomAccessFile debugAccessFile;
    private String filePath;
    private boolean findMoov = false;
    private File h264file;
    private RandomAccessFile mAccessFile;
    private byte[] moov;
    private int offset_moov = -1;
    public MP4Info videoInfo;

    public static synchronized MP4Parser getInstance() {
        MP4Parser mP4Parser;
        synchronized (MP4Parser.class) {
            if (instance == null) {
                instance = new MP4Parser();
            }
            mP4Parser = instance;
        }
        return mP4Parser;
    }

    public void loadFile(String path) {
        this.filePath = path;
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0095 A[SYNTHETIC, Splitter:B:30:0x0095] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00aa A[SYNTHETIC, Splitter:B:38:0x00aa] */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void parseMp4File() {
        /*
            r12 = this;
            r11 = 100
            java.io.File r6 = new java.io.File
            java.lang.String r8 = r12.filePath
            r6.<init>(r8)
            boolean r8 = r6.exists()
            if (r8 != 0) goto L_0x0018
            java.lang.String r8 = dji.midware.media.mp4.MP4Parser.TAG
            java.lang.String r9 = "File not exist"
            android.util.Log.e(r8, r9)
        L_0x0017:
            return
        L_0x0018:
            r0 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x00b6 }
            java.lang.String r8 = "rw"
            r1.<init>(r6, r8)     // Catch:{ IOException -> 0x00b6 }
            r8 = 100
            byte[] r4 = new byte[r8]     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            r1.read(r4)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            r7 = 0
        L_0x0029:
            if (r7 >= r11) goto L_0x0068
            int r2 = dji.midware.media.mp4.MP4BytesUtil.getInt(r4, r7)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            int r7 = r7 + 4
            r8 = 4
            java.lang.String r3 = dji.midware.media.mp4.MP4BytesUtil.getStringUTF8(r4, r7, r8)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            int r8 = r2 + -4
            int r7 = r7 + r8
            java.lang.String r8 = dji.midware.media.mp4.MP4Parser.TAG     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            r9.<init>()     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            java.lang.String r10 = "Box size: "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            java.lang.StringBuilder r9 = r9.append(r2)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            java.lang.String r10 = " Box type:"
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            java.lang.StringBuilder r9 = r9.append(r3)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            java.lang.String r9 = r9.toString()     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            android.util.Log.d(r8, r9)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            dji.midware.media.mp4.MP4BoxType r8 = dji.midware.media.mp4.MP4BoxType.mdat     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            boolean r8 = r8._equals(r3)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            if (r8 == 0) goto L_0x0078
            r8 = 1
            r12.findMoov = r8     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
        L_0x0068:
            boolean r8 = r12.findMoov     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            if (r8 == 0) goto L_0x0085
            r12.offset_moov = r7     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            r12.locateMoovData()     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
        L_0x0071:
            if (r1 == 0) goto L_0x00b8
            r1.close()     // Catch:{ IOException -> 0x00a0 }
            r0 = r1
            goto L_0x0017
        L_0x0078:
            dji.midware.media.mp4.MP4BoxType r8 = dji.midware.media.mp4.MP4BoxType.moov     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            boolean r8 = r8._equals(r3)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            if (r8 == 0) goto L_0x0029
            r8 = 1
            r12.findMoov = r8     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            int r7 = r7 - r2
            goto L_0x0068
        L_0x0085:
            java.lang.String r8 = dji.midware.media.mp4.MP4Parser.TAG     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            java.lang.String r9 = "Box moov not find"
            android.util.Log.e(r8, r9)     // Catch:{ IOException -> 0x008e, all -> 0x00b3 }
            goto L_0x0071
        L_0x008e:
            r5 = move-exception
            r0 = r1
        L_0x0090:
            r5.printStackTrace()     // Catch:{ all -> 0x00a7 }
            if (r0 == 0) goto L_0x0017
            r0.close()     // Catch:{ IOException -> 0x009a }
            goto L_0x0017
        L_0x009a:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x0017
        L_0x00a0:
            r5 = move-exception
            r5.printStackTrace()
            r0 = r1
            goto L_0x0017
        L_0x00a7:
            r8 = move-exception
        L_0x00a8:
            if (r0 == 0) goto L_0x00ad
            r0.close()     // Catch:{ IOException -> 0x00ae }
        L_0x00ad:
            throw r8
        L_0x00ae:
            r5 = move-exception
            r5.printStackTrace()
            goto L_0x00ad
        L_0x00b3:
            r8 = move-exception
            r0 = r1
            goto L_0x00a8
        L_0x00b6:
            r5 = move-exception
            goto L_0x0090
        L_0x00b8:
            r0 = r1
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.mp4.MP4Parser.parseMp4File():void");
    }

    public int findMoovOffset(byte[] buffer) {
        int index = 0;
        Log.d(TAG, MP4BytesUtil.byte2hex(buffer, 0, 30));
        while (true) {
            if (index >= 100) {
                break;
            }
            int box_size = MP4BytesUtil.getInt(buffer, index);
            int index2 = index + 4;
            String box_type = MP4BytesUtil.getStringUTF8(buffer, index2, 4);
            index = index2 + (box_size - 4);
            Log.d(TAG, "Box size: " + box_size + " Box type:" + box_type);
            if (MP4BoxType.mdat._equals(box_type)) {
                this.findMoov = true;
                break;
            }
        }
        if (this.findMoov) {
            this.offset_moov = index;
            return index;
        }
        Log.e(TAG, "Box moov not find");
        return 0;
    }

    public void parseMoov(byte[] buffer) {
        int box_size = MP4BytesUtil.getInt(buffer, 0);
        String box_type = MP4BytesUtil.getString(buffer, 0 + 4, 4);
        Log.e(TAG, MP4BytesUtil.byte2hex(buffer, 0, 10) + "\nbox_size: " + box_size + " box_type: " + box_type);
        if (!MP4BoxType.moov._equals(box_type)) {
            Log.e(TAG, "not find box moov");
            return;
        }
        this.moov = new byte[box_size];
        System.arraycopy(buffer, 0, this.moov, 0, box_size);
        parseMoovData();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0078 A[SYNTHETIC, Splitter:B:27:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0084 A[SYNTHETIC, Splitter:B:33:0x0084] */
    /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void locateMoovData() {
        /*
            r10 = this;
            java.io.File r5 = new java.io.File
            java.lang.String r8 = r10.filePath
            r5.<init>(r8)
            boolean r8 = r5.exists()
            if (r8 != 0) goto L_0x0016
            java.lang.String r8 = dji.midware.media.mp4.MP4Parser.TAG
            java.lang.String r9 = "File not exist"
            android.util.Log.e(r8, r9)
        L_0x0015:
            return
        L_0x0016:
            r0 = 0
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile     // Catch:{ IOException -> 0x0072 }
            java.lang.String r8 = "rw"
            r1.<init>(r5, r8)     // Catch:{ IOException -> 0x0072 }
            int r8 = r10.offset_moov     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            long r8 = (long) r8     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r1.seek(r8)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r8 = 8
            byte[] r7 = new byte[r8]     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r1.read(r7)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r6 = 0
            int r2 = dji.midware.media.mp4.MP4BytesUtil.getInt(r7, r6)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            int r6 = r6 + 4
            r8 = 4
            java.lang.String r3 = dji.midware.media.mp4.MP4BytesUtil.getString(r7, r6, r8)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            dji.midware.media.mp4.MP4BoxType r8 = dji.midware.media.mp4.MP4BoxType.moov     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            boolean r8 = r8._equals(r3)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            if (r8 != 0) goto L_0x0053
            java.lang.String r8 = dji.midware.media.mp4.MP4Parser.TAG     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            java.lang.String r9 = "not find box moov"
            android.util.Log.e(r8, r9)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            if (r1 == 0) goto L_0x0015
            r1.close()     // Catch:{ IOException -> 0x004e }
            goto L_0x0015
        L_0x004e:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0015
        L_0x0053:
            byte[] r8 = new byte[r2]     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r10.moov = r8     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            int r8 = r10.offset_moov     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            long r8 = (long) r8     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r1.seek(r8)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            byte[] r8 = r10.moov     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r1.read(r8)     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            r10.parseMoovData()     // Catch:{ IOException -> 0x0090, all -> 0x008d }
            if (r1 == 0) goto L_0x0093
            r1.close()     // Catch:{ IOException -> 0x006c }
            r0 = r1
            goto L_0x0015
        L_0x006c:
            r4 = move-exception
            r4.printStackTrace()
            r0 = r1
            goto L_0x0015
        L_0x0072:
            r4 = move-exception
        L_0x0073:
            r4.printStackTrace()     // Catch:{ all -> 0x0081 }
            if (r0 == 0) goto L_0x0015
            r0.close()     // Catch:{ IOException -> 0x007c }
            goto L_0x0015
        L_0x007c:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0015
        L_0x0081:
            r8 = move-exception
        L_0x0082:
            if (r0 == 0) goto L_0x0087
            r0.close()     // Catch:{ IOException -> 0x0088 }
        L_0x0087:
            throw r8
        L_0x0088:
            r4 = move-exception
            r4.printStackTrace()
            goto L_0x0087
        L_0x008d:
            r8 = move-exception
            r0 = r1
            goto L_0x0082
        L_0x0090:
            r4 = move-exception
            r0 = r1
            goto L_0x0073
        L_0x0093:
            r0 = r1
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.media.mp4.MP4Parser.locateMoovData():void");
    }

    private boolean parseMoovData() {
        MP4MovieBox moovBox = new MP4MovieBox();
        if (!moovBox.parseData(this.moov, this.offset_moov)) {
            return false;
        }
        Log.d(TAG, "parse finish");
        this.videoInfo = new MP4Info(moovBox);
        return true;
    }

    public void prepareLoading() {
        File file = new File(this.filePath);
        if (!file.exists()) {
            Log.e(TAG, "File not exist");
            return;
        }
        try {
            this.mAccessFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (this.debug) {
            this.h264file = new File(Environment.getExternalStorageDirectory().getPath() + "/video.264");
            try {
                this.debugAccessFile = new RandomAccessFile(this.h264file, "rw");
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            }
        }
    }

    public byte[] putIFrame(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.iframe_p4p_720_16x9);
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] loadVideoFrame(int offset, int length) {
        byte[] frame = new byte[length];
        try {
            this.mAccessFile.seek((long) offset);
            this.mAccessFile.read(frame);
            int index = 0;
            while (index < length) {
                int slice_length = MP4BytesUtil.getInt(frame, index);
                frame[index] = 0;
                int index2 = index + 1;
                frame[index2] = 0;
                int index3 = index2 + 1;
                frame[index3] = 0;
                int index4 = index3 + 1;
                frame[index4] = 1;
                index = index4 + 1 + slice_length;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.debug) {
            try {
                this.debugAccessFile.write(frame);
                if (offset == this.videoInfo.sample_offset.length) {
                    this.debugAccessFile.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return frame;
    }

    public static void replaceSliceHeader(byte[] frame) {
        int index = 0;
        while (index < frame.length && index >= 0) {
            if (index > frame.length - 4) {
                Log.e(TAG, "transform 264 error: index=" + index + " frame.length=" + frame.length);
                return;
            }
            int slice_length = MP4BytesUtil.getInt(frame, index);
            if (slice_length < 0) {
                Log.e(TAG, "transform 264 error: index=" + index + " slice_length=" + slice_length);
                return;
            }
            frame[index] = 0;
            int index2 = index + 1;
            frame[index2] = 0;
            int index3 = index2 + 1;
            frame[index3] = 0;
            int index4 = index3 + 1;
            frame[index4] = 1;
            index = index4 + 1 + slice_length;
        }
    }

    public static byte[] getSPSPPSData(byte[] sps, byte[] pps) {
        byte[] spspps = new byte[(sps.length + pps.length + 8)];
        spspps[0] = 0;
        int index = 0 + 1;
        spspps[index] = 0;
        int index2 = index + 1;
        spspps[index2] = 0;
        int index3 = index2 + 1;
        spspps[index3] = 1;
        System.arraycopy(sps, 0, spspps, index3 + 1, sps.length);
        int index4 = sps.length + 4;
        spspps[index4] = 0;
        int index5 = index4 + 1;
        spspps[index5] = 0;
        int index6 = index5 + 1;
        spspps[index6] = 0;
        int index7 = index6 + 1;
        spspps[index7] = 1;
        int index8 = index7 + 1;
        System.arraycopy(pps, 0, spspps, index8, pps.length);
        int index9 = index8 + pps.length;
        return spspps;
    }

    public static byte[] insertSPSPPSData(byte[] data, byte[] sps, byte[] pps) {
        byte[] spspps = getSPSPPSData(sps, pps);
        byte[] temp = new byte[(spspps.length + data.length)];
        System.arraycopy(spspps, 0, temp, 0, spspps.length);
        System.arraycopy(data, 0, temp, spspps.length, data.length);
        return temp;
    }
}
