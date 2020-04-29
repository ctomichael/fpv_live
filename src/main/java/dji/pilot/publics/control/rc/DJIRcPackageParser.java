package dji.pilot.publics.control.rc;

import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.BytesUtil;
import dji.pilot.publics.util.DJIPublicUtils;
import java.nio.charset.Charset;
import java.util.ArrayList;
import org.msgpack.core.MessagePack;

@EXClassNullAway
public class DJIRcPackageParser {
    public static final String DEBUG_PATH = "/sdcard/DJI/WM610_FW_V01.02.00.16.bin";
    public static final int LENGTH_FIRMWARE = 52;
    public static final int LENGTH_HEAD = 272;
    public static final int LENGTH_MD5 = 16;
    public static final int LENGTH_PACKAGE_HEADER = 64;
    private static final String TAG = DJIRcPackageParser.class.getSimpleName();

    public static final class FirmwareInfo {
        public int mDataLength = 0;
        public int mDeviceId = -1;
        public int mFileOffset = 0;
        public byte[] mMD5 = new byte[16];
        public int mModuleId = -1;
        public int mPrority = -1;
        public int mRawLength = 0;
        public byte[] mRawMD5 = new byte[16];
        public int mResv = -1;
        public int mType = -1;
        public long mVersion = 0;
    }

    public static final class HeaderInfo {
        public int mBuildDate = 0;
        public int mFirmwareNum = 0;
        public int mFormatVerson = 0;
        public int mHeaderLength = 0;
        public int mMagicNum = 0;
        public String mManufactureName = null;
        public String mProductName = null;
        public int[] mResv = new int[18];
    }

    public static final class ParseResult {
        public final ArrayList<FirmwareInfo> mFirmwareInfos = new ArrayList<>();
        public HeaderInfo mHeadInfo = null;
        public boolean mResult = false;
    }

    public static void parseTest() {
        ParseResult result = parsePackageHead(DEBUG_PATH, "WM610");
        if (result != null) {
            Log.d(TAG, "result[" + result.mResult + IMemberProtocol.STRING_SEPERATOR_RIGHT);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x0183 A[SYNTHETIC, Splitter:B:51:0x0183] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x018c A[SYNTHETIC, Splitter:B:56:0x018c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static dji.pilot.publics.control.rc.DJIRcPackageParser.ParseResult parsePackageHead(java.lang.String r26, java.lang.String r27) {
        /*
            r16 = 0
            if (r26 == 0) goto L_0x000a
            boolean r21 = dji.pilot.publics.util.DJIPublicUtils.isEmpty(r27)
            if (r21 == 0) goto L_0x000f
        L_0x000a:
            r17 = r16
            r18 = r16
        L_0x000e:
            return r18
        L_0x000f:
            java.io.File r6 = new java.io.File
            r0 = r26
            r6.<init>(r0)
            boolean r21 = r6.exists()
            if (r21 == 0) goto L_0x0022
            boolean r21 = r6.isFile()
            if (r21 != 0) goto L_0x0027
        L_0x0022:
            r17 = r16
            r18 = r16
            goto L_0x000e
        L_0x0027:
            long r8 = r6.length()
            r22 = 272(0x110, double:1.344E-321)
            int r21 = (r8 > r22 ? 1 : (r8 == r22 ? 0 : -1))
            if (r21 >= 0) goto L_0x0036
            r17 = r16
            r18 = r16
            goto L_0x000e
        L_0x0036:
            r14 = 0
            java.io.RandomAccessFile r15 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0170 }
            java.lang.String r21 = "r"
            r0 = r21
            r15.<init>(r6, r0)     // Catch:{ Exception -> 0x0170 }
            r21 = 64
            r0 = r21
            byte[] r4 = new byte[r0]     // Catch:{ Exception -> 0x019a, all -> 0x0192 }
            r21 = 0
            r22 = 64
            r0 = r21
            r1 = r22
            int r12 = r15.read(r4, r0, r1)     // Catch:{ Exception -> 0x019a, all -> 0x0192 }
            r21 = 64
            r0 = r21
            if (r12 != r0) goto L_0x0161
            dji.pilot.publics.control.rc.DJIRcPackageParser$ParseResult r17 = new dji.pilot.publics.control.rc.DJIRcPackageParser$ParseResult     // Catch:{ Exception -> 0x019a, all -> 0x0192 }
            r17.<init>()     // Catch:{ Exception -> 0x019a, all -> 0x0192 }
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r21 = parseHeader(r4)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r0 = r21
            r1 = r17
            r1.mHeadInfo = r0     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r0 = r17
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r0 = r0.mHeadInfo     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r21
            java.lang.String r0 = r0.mProductName     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r27
            r1 = r21
            boolean r21 = r0.equalsIgnoreCase(r1)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            if (r21 == 0) goto L_0x015f
            r22 = 0
            r0 = r22
            r15.seek(r0)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r0 = r17
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r0 = r0.mHeadInfo     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r21
            int r0 = r0.mHeaderLength     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            int r21 = r21 + -2
            r0 = r21
            byte[] r4 = new byte[r0]     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = 0
            r0 = r17
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r0 = r0.mHeadInfo     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r22 = r0
            r0 = r22
            int r0 = r0.mHeaderLength     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r22 = r0
            int r22 = r22 + -2
            r0 = r21
            r1 = r22
            int r12 = r15.read(r4, r0, r1)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r0 = r17
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r0 = r0.mHeadInfo     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r21
            int r0 = r0.mHeaderLength     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            int r21 = r21 + -2
            r0 = r21
            if (r12 != r0) goto L_0x015f
            short r20 = dji.midware.natives.GroudStation.native_calcCrc16(r4)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = 2
            r0 = r21
            byte[] r0 = new byte[r0]     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r19 = r0
            r21 = 0
            r22 = 2
            r0 = r19
            r1 = r21
            r2 = r22
            int r12 = r15.read(r0, r1, r2)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = 2
            r0 = r21
            if (r12 != r0) goto L_0x015f
            r21 = 0
            r0 = r19
            r1 = r21
            short r13 = dji.midware.util.BytesUtil.getShort(r0, r1)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r0 = r20
            if (r13 != r0) goto L_0x015f
            r0 = r17
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r0 = r0.mHeadInfo     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r21
            int r0 = r0.mFirmwareNum     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            int r21 = r21 * 52
            int r21 = r21 + 64
            int r21 = r21 + 2
            r0 = r17
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r0 = r0.mHeadInfo     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r22 = r0
            r0 = r22
            int r0 = r0.mHeaderLength     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r22 = r0
            r0 = r21
            r1 = r22
            if (r0 != r1) goto L_0x015f
            r21 = 1
            r0 = r21
            r1 = r17
            r1.mResult = r0     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r22 = 64
            r0 = r22
            r15.seek(r0)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = 52
            r0 = r21
            byte[] r4 = new byte[r0]     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r10 = 0
            r11 = r10
        L_0x012a:
            int r10 = r11 + 1
            r0 = r17
            dji.pilot.publics.control.rc.DJIRcPackageParser$HeaderInfo r0 = r0.mHeadInfo     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r21
            int r0 = r0.mFirmwareNum     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r21
            if (r11 >= r0) goto L_0x015f
            r21 = 0
            r22 = 52
            r0 = r21
            r1 = r22
            int r12 = r15.read(r4, r0, r1)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = 52
            r0 = r21
            if (r12 != r0) goto L_0x015f
            dji.pilot.publics.control.rc.DJIRcPackageParser$FirmwareInfo r7 = parseFirmware(r4)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r0 = r17
            java.util.ArrayList<dji.pilot.publics.control.rc.DJIRcPackageParser$FirmwareInfo> r0 = r0.mFirmwareInfos     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r21 = r0
            r0 = r21
            r0.add(r7)     // Catch:{ Exception -> 0x019d, all -> 0x0195 }
            r11 = r10
            goto L_0x012a
        L_0x015f:
            r16 = r17
        L_0x0161:
            if (r15 == 0) goto L_0x01a2
            r15.close()     // Catch:{ Exception -> 0x016d }
            r14 = r15
        L_0x0167:
            r17 = r16
            r18 = r16
            goto L_0x000e
        L_0x016d:
            r21 = move-exception
            r14 = r15
            goto L_0x0167
        L_0x0170:
            r5 = move-exception
        L_0x0171:
            dji.log.DJILogHelper r21 = dji.log.DJILogHelper.getInstance()     // Catch:{ all -> 0x0189 }
            java.lang.String r22 = dji.pilot.publics.control.rc.DJIRcPackageParser.TAG     // Catch:{ all -> 0x0189 }
            java.lang.String r23 = "read package exception"
            r24 = 0
            r25 = 1
            r21.LOGD(r22, r23, r24, r25)     // Catch:{ all -> 0x0189 }
            if (r14 == 0) goto L_0x0167
            r14.close()     // Catch:{ Exception -> 0x0187 }
            goto L_0x0167
        L_0x0187:
            r21 = move-exception
            goto L_0x0167
        L_0x0189:
            r21 = move-exception
        L_0x018a:
            if (r14 == 0) goto L_0x018f
            r14.close()     // Catch:{ Exception -> 0x0190 }
        L_0x018f:
            throw r21
        L_0x0190:
            r22 = move-exception
            goto L_0x018f
        L_0x0192:
            r21 = move-exception
            r14 = r15
            goto L_0x018a
        L_0x0195:
            r21 = move-exception
            r14 = r15
            r16 = r17
            goto L_0x018a
        L_0x019a:
            r5 = move-exception
            r14 = r15
            goto L_0x0171
        L_0x019d:
            r5 = move-exception
            r14 = r15
            r16 = r17
            goto L_0x0171
        L_0x01a2:
            r14 = r15
            goto L_0x0167
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.control.rc.DJIRcPackageParser.parsePackageHead(java.lang.String, java.lang.String):dji.pilot.publics.control.rc.DJIRcPackageParser$ParseResult");
    }

    private static String getStringUTF8(byte[] bytes, int offset, int length) {
        int i = offset;
        while (true) {
            if (i >= offset + length) {
                break;
            } else if (bytes[i] == 0) {
                length = i - offset;
                break;
            } else {
                i++;
            }
        }
        return new String(bytes, offset, length, Charset.forName("UTF-8"));
    }

    private static HeaderInfo parseHeader(byte[] buffer) {
        HeaderInfo header = new HeaderInfo();
        header.mMagicNum = BytesUtil.getInt(buffer, 0);
        int index = 0 + 4;
        header.mFormatVerson = BytesUtil.getShort(buffer, index);
        int index2 = index + 2;
        header.mHeaderLength = BytesUtil.getShort(buffer, index2);
        int index3 = index2 + 2;
        header.mBuildDate = BytesUtil.getInt(buffer, index3);
        int index4 = index3 + 4;
        header.mManufactureName = getStringUTF8(buffer, index4, 16);
        int index5 = index4 + 16;
        header.mProductName = getStringUTF8(buffer, index5, 16);
        int index6 = index5 + 16;
        header.mFirmwareNum = BytesUtil.getShort(buffer, index6);
        int index7 = index6 + 2;
        return header;
    }

    private static FirmwareInfo parseFirmware(byte[] buffer) {
        FirmwareInfo firmware = new FirmwareInfo();
        firmware.mDeviceId = buffer[0] & 31;
        firmware.mModuleId = (buffer[0] & MessagePack.Code.NEGFIXINT_PREFIX) >>> 5;
        int index = 0 + 1;
        firmware.mType = buffer[index] & 255;
        int index2 = index + 1;
        firmware.mPrority = buffer[index2] & 255;
        int index3 = index2 + 1;
        firmware.mResv = buffer[index3] & 255;
        int index4 = index3 + 1;
        firmware.mVersion = DJIPublicUtils.getNumber(buffer, index4, 4);
        int index5 = index4 + 4;
        firmware.mFileOffset = BytesUtil.getInt(buffer, index5);
        int index6 = index5 + 4;
        firmware.mDataLength = BytesUtil.getInt(buffer, index6);
        int index7 = index6 + 4;
        firmware.mRawLength = BytesUtil.getInt(buffer, index7);
        int index8 = index7 + 4;
        System.arraycopy(buffer, index8, firmware.mMD5, 0, 16);
        System.arraycopy(buffer, index8 + 16, firmware.mRawMD5, 0, 16);
        return firmware;
    }
}
