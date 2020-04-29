package dji.pilot.playback;

import android.support.annotation.NonNull;
import dji.component.playback.model.PlaybackFileInfo;
import dji.component.playback.model.PlaybackFileType;
import dji.component.playback.model.video.PlaybackFileResolution;
import dji.component.playback.model.video.PlaybackVideoEncodeType;
import dji.component.playback.model.video.PlaybackVideoSubType;
import dji.component.playback.model.video.PlaybackVideoType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;

public class PlaybackFileInfoUtils {
    private static final boolean isAllowLTM = false;

    public static boolean isQuickShot(@NonNull PlaybackFileInfo info) {
        return (info.fileType == PlaybackFileType.MP4 || info.fileType == PlaybackFileType.MOV) && info.subVideoType != PlaybackVideoSubType.NONE && info.videoType == PlaybackVideoType.QUICK_SHOT;
    }

    public static boolean isHLGVideo(@NonNull PlaybackFileInfo info) {
        return info.videoType == PlaybackVideoType.HLG;
    }

    public static boolean isHyperLapse(@NonNull PlaybackFileInfo info) {
        return info.videoType == PlaybackVideoType.HYPER_LAPSE;
    }

    public static boolean canDownload(@NonNull PlaybackFileInfo info) {
        return info.fileType.canDownload() && (videoCanDownload(info) || pictureCanDownload(info));
    }

    public static boolean isValidFile(@NonNull PlaybackFileInfo info) {
        return ((double) info.fileGuid) != 0.0d || !info.fileType.isVideo() || !hasFileGuid();
    }

    public static boolean canMakeLtm(@NonNull PlaybackFileInfo info) {
        return false;
    }

    private static boolean hasFileGuid() {
        ProductType type = DJIProductManager.getInstance().getType();
        return type == ProductType.KumquatX || type == ProductType.Mammoth || type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM160;
    }

    private static boolean videoCanDownload(@NonNull PlaybackFileInfo info) {
        return info.fileType.isVideo() && info.resolution.isLowerThan(PlaybackFileResolution.Size_3840_1920p) && info.encodeType != PlaybackVideoEncodeType.H265 && (((double) info.fileGuid) != 0.0d || !hasFileGuid());
    }

    private static boolean pictureCanDownload(@NonNull PlaybackFileInfo info) {
        return info.fileType.isPicture();
    }

    public static boolean panoCanDownload(@NonNull PlaybackFileInfo info) {
        boolean isRemotePano;
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.WM230 || type == ProductType.WM240 || type == ProductType.WM245) {
            isRemotePano = true;
        } else {
            isRemotePano = false;
        }
        if (!isRemotePano || info.fileType != PlaybackFileType.PANO) {
            return false;
        }
        return true;
    }

    public static boolean canSync(@NonNull PlaybackFileInfo info) {
        return info.fileType == PlaybackFileType.PANO;
    }

    /* JADX WARNING: Removed duplicated region for block: B:77:0x023b A[SYNTHETIC, Splitter:B:77:0x023b] */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0244 A[SYNTHETIC, Splitter:B:82:0x0244] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static dji.component.playback.model.PlaybackFileInfo readFromProperty(java.io.File r30) {
        /*
            dji.component.playback.model.PlaybackFileInfo r14 = new dji.component.playback.model.PlaybackFileInfo
            r14.<init>()
            r19 = 0
            java.util.Properties r16 = new java.util.Properties     // Catch:{ IOException -> 0x0233, IllegalArgumentException -> 0x0248 }
            r16.<init>()     // Catch:{ IOException -> 0x0233, IllegalArgumentException -> 0x0248 }
            java.io.FileReader r20 = new java.io.FileReader     // Catch:{ IOException -> 0x0233, IllegalArgumentException -> 0x0248 }
            r0 = r20
            r1 = r30
            r0.<init>(r1)     // Catch:{ IOException -> 0x0233, IllegalArgumentException -> 0x0248 }
            r0 = r16
            r1 = r20
            r0.load(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            java.lang.String r27 = "length"
            r0 = r16
            r1 = r27
            java.lang.String r15 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r15)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0035
            long r28 = java.lang.Long.parseLong(r15)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r28
            r14.length = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0035:
            java.lang.String r27 = "createTime"
            r0 = r16
            r1 = r27
            java.lang.String r4 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r4)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x004e
            long r28 = java.lang.Long.parseLong(r4)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r28
            r14.createTime = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x004e:
            java.lang.String r27 = "createTimeOrg"
            r0 = r16
            r1 = r27
            java.lang.String r3 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r3)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0067
            long r28 = java.lang.Long.parseLong(r3)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r28
            r14.createTimeOrg = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0067:
            java.lang.String r27 = "index"
            r0 = r16
            r1 = r27
            java.lang.String r13 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r13)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0080
            int r27 = java.lang.Integer.parseInt(r13)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.index = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0080:
            java.lang.String r27 = "subIndex"
            r0 = r16
            r1 = r27
            java.lang.String r24 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r24)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0099
            int r27 = java.lang.Integer.parseInt(r24)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.subIndex = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0099:
            java.lang.String r27 = "duration"
            r0 = r16
            r1 = r27
            java.lang.String r5 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r5)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x00b2
            int r27 = java.lang.Integer.parseInt(r5)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.duration = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x00b2:
            java.lang.String r27 = "rotation"
            r0 = r16
            r1 = r27
            java.lang.String r22 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r22)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x00cb
            int r27 = java.lang.Integer.parseInt(r22)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.rotation = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x00cb:
            java.lang.String r27 = "frameRate"
            r0 = r16
            r1 = r27
            java.lang.String r9 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r9)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x00e4
            int r27 = java.lang.Integer.parseInt(r9)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.frameRate = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x00e4:
            java.lang.String r27 = "resolution"
            r0 = r16
            r1 = r27
            java.lang.String r21 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r21)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x00fd
            dji.component.playback.model.video.PlaybackFileResolution r27 = dji.component.playback.model.video.PlaybackFileResolution.valueOf(r21)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.resolution = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x00fd:
            java.lang.String r27 = "fileType"
            r0 = r16
            r1 = r27
            java.lang.String r8 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r8)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0116
            dji.component.playback.model.PlaybackFileType r27 = dji.component.playback.model.PlaybackFileType.valueOf(r8)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.fileType = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0116:
            java.lang.String r27 = "pathLength"
            r0 = r16
            r1 = r27
            java.lang.String r17 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r17)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x012f
            int r27 = java.lang.Integer.parseInt(r17)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.pathLength = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x012f:
            java.lang.String r27 = "pathStr"
            r0 = r16
            r1 = r27
            java.lang.String r27 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.pathStr = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            java.lang.String r27 = "hasEXT"
            r0 = r16
            r1 = r27
            java.lang.String r12 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r12)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0157
            boolean r27 = java.lang.Boolean.parseBoolean(r12)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.hasEXT = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0157:
            java.lang.String r27 = "fileGuid"
            r0 = r16
            r1 = r27
            java.lang.String r7 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r7)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0170
            long r28 = java.lang.Long.parseLong(r7)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r28
            r14.fileGuid = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0170:
            java.lang.String r27 = "captureType"
            r0 = r16
            r1 = r27
            java.lang.String r2 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r2)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0189
            dji.component.playback.model.photo.PlaybackPhotoType r27 = dji.component.playback.model.photo.PlaybackPhotoType.valueOf(r2)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.captureType = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0189:
            java.lang.String r27 = "photoGroupId"
            r0 = r16
            r1 = r27
            java.lang.String r18 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r18)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x01a2
            int r27 = java.lang.Integer.parseInt(r18)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.photoGroupId = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x01a2:
            java.lang.String r27 = "starTag"
            r0 = r16
            r1 = r27
            java.lang.String r23 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r23)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x01bb
            boolean r27 = java.lang.Boolean.parseBoolean(r23)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.starTag = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x01bb:
            java.lang.String r27 = "groupType"
            r0 = r16
            r1 = r27
            java.lang.String r11 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r11)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x01d4
            dji.component.playback.model.photo.PlaybackPhotoGroupType r27 = dji.component.playback.model.photo.PlaybackPhotoGroupType.valueOf(r11)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.groupType = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x01d4:
            java.lang.String r27 = "groupNum"
            r0 = r16
            r1 = r27
            java.lang.String r10 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r10)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x01ed
            int r27 = java.lang.Integer.parseInt(r10)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.groupNum = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x01ed:
            java.lang.String r27 = "subVideoType"
            r0 = r16
            r1 = r27
            java.lang.String r25 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r25)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x020a
            int r27 = java.lang.Integer.parseInt(r25)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            dji.component.playback.model.video.PlaybackVideoSubType r27 = dji.component.playback.model.video.PlaybackVideoSubType.find(r27)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.subVideoType = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x020a:
            java.lang.String r27 = "videoType"
            r0 = r16
            r1 = r27
            java.lang.String r26 = r0.getProperty(r1)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            boolean r27 = android.text.TextUtils.isEmpty(r26)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            if (r27 != 0) goto L_0x0227
            int r27 = java.lang.Integer.parseInt(r26)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            dji.component.playback.model.video.PlaybackVideoType r27 = dji.component.playback.model.video.PlaybackVideoType.find(r27)     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
            r0 = r27
            r14.videoType = r0     // Catch:{ IOException -> 0x0252, IllegalArgumentException -> 0x0256, all -> 0x024e }
        L_0x0227:
            if (r20 == 0) goto L_0x022c
            r20.close()     // Catch:{ Exception -> 0x022f }
        L_0x022c:
            r19 = r20
        L_0x022e:
            return r14
        L_0x022f:
            r27 = move-exception
            r19 = r20
            goto L_0x022e
        L_0x0233:
            r27 = move-exception
        L_0x0234:
            r6 = r27
        L_0x0236:
            r6.printStackTrace()     // Catch:{ all -> 0x0241 }
            if (r19 == 0) goto L_0x022e
            r19.close()     // Catch:{ Exception -> 0x023f }
            goto L_0x022e
        L_0x023f:
            r27 = move-exception
            goto L_0x022e
        L_0x0241:
            r27 = move-exception
        L_0x0242:
            if (r19 == 0) goto L_0x0247
            r19.close()     // Catch:{ Exception -> 0x024c }
        L_0x0247:
            throw r27
        L_0x0248:
            r27 = move-exception
        L_0x0249:
            r6 = r27
            goto L_0x0236
        L_0x024c:
            r28 = move-exception
            goto L_0x0247
        L_0x024e:
            r27 = move-exception
            r19 = r20
            goto L_0x0242
        L_0x0252:
            r27 = move-exception
            r19 = r20
            goto L_0x0234
        L_0x0256:
            r27 = move-exception
            r19 = r20
            goto L_0x0249
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.playback.PlaybackFileInfoUtils.readFromProperty(java.io.File):dji.component.playback.model.PlaybackFileInfo");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0136 A[SYNTHETIC, Splitter:B:32:0x0136] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x013f A[SYNTHETIC, Splitter:B:37:0x013f] */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeToProperty(@android.support.annotation.NonNull dji.component.playback.model.PlaybackFileInfo r8, @android.support.annotation.NonNull java.io.File r9) {
        /*
            r2 = 0
            java.util.Properties r1 = new java.util.Properties     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.<init>()     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "length"
            long r6 = r8.length     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "createTime"
            long r6 = r8.createTime     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "createTimeOrg"
            long r6 = r8.createTimeOrg     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "index"
            int r5 = r8.index     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "subIndex"
            int r5 = r8.subIndex     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "duration"
            int r5 = r8.duration     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "rotation"
            int r5 = r8.rotation     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "frameRate"
            int r5 = r8.frameRate     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            dji.component.playback.model.video.PlaybackFileResolution r4 = r8.resolution     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            if (r4 == 0) goto L_0x0076
            java.lang.String r4 = "resolution"
            dji.component.playback.model.video.PlaybackFileResolution r5 = r8.resolution     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
        L_0x0076:
            dji.component.playback.model.PlaybackFileType r4 = r8.fileType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            if (r4 == 0) goto L_0x0086
            java.lang.String r4 = "fileType"
            dji.component.playback.model.PlaybackFileType r5 = r8.fileType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
        L_0x0086:
            java.lang.String r4 = "pathLength"
            int r5 = r8.pathLength     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = r8.pathStr     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            if (r4 == 0) goto L_0x009e
            java.lang.String r4 = "pathStr"
            java.lang.String r5 = r8.pathStr     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
        L_0x009e:
            java.lang.String r4 = "hasEXT"
            boolean r5 = r8.hasEXT     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Boolean.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "fileGuid"
            long r6 = r8.fileGuid     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Long.toString(r6)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            dji.component.playback.model.photo.PlaybackPhotoType r4 = r8.captureType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            if (r4 == 0) goto L_0x00c6
            java.lang.String r4 = "captureType"
            dji.component.playback.model.photo.PlaybackPhotoType r5 = r8.captureType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
        L_0x00c6:
            java.lang.String r4 = "photoGroupId"
            int r5 = r8.photoGroupId     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "starTag"
            boolean r5 = r8.starTag     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Boolean.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            dji.component.playback.model.photo.PlaybackPhotoGroupType r4 = r8.groupType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            if (r4 == 0) goto L_0x00ee
            java.lang.String r4 = "groupType"
            dji.component.playback.model.photo.PlaybackPhotoGroupType r5 = r8.groupType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = r5.name()     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
        L_0x00ee:
            java.lang.String r4 = "groupNum"
            int r5 = r8.groupNum     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "subVideoType"
            dji.component.playback.model.video.PlaybackVideoSubType r5 = r8.subVideoType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            int r5 = r5.value()     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "videoType"
            dji.component.playback.model.video.PlaybackVideoType r5 = r8.videoType     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            int r5 = r5.value()     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r5 = java.lang.Integer.toString(r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r1.setProperty(r4, r5)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.io.FileWriter r3 = new java.io.FileWriter     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            r3.<init>(r9)     // Catch:{ IOException -> 0x012f, NumberFormatException -> 0x0143 }
            java.lang.String r4 = "Org Video UUID info"
            r1.store(r3, r4)     // Catch:{ IOException -> 0x014e, NumberFormatException -> 0x014b, all -> 0x0148 }
            if (r3 == 0) goto L_0x012a
            r3.close()     // Catch:{ Exception -> 0x012c }
        L_0x012a:
            r2 = r3
        L_0x012b:
            return
        L_0x012c:
            r4 = move-exception
            r2 = r3
            goto L_0x012b
        L_0x012f:
            r4 = move-exception
        L_0x0130:
            r0 = r4
        L_0x0131:
            r0.printStackTrace()     // Catch:{ all -> 0x013c }
            if (r2 == 0) goto L_0x012b
            r2.close()     // Catch:{ Exception -> 0x013a }
            goto L_0x012b
        L_0x013a:
            r4 = move-exception
            goto L_0x012b
        L_0x013c:
            r4 = move-exception
        L_0x013d:
            if (r2 == 0) goto L_0x0142
            r2.close()     // Catch:{ Exception -> 0x0146 }
        L_0x0142:
            throw r4
        L_0x0143:
            r4 = move-exception
        L_0x0144:
            r0 = r4
            goto L_0x0131
        L_0x0146:
            r5 = move-exception
            goto L_0x0142
        L_0x0148:
            r4 = move-exception
            r2 = r3
            goto L_0x013d
        L_0x014b:
            r4 = move-exception
            r2 = r3
            goto L_0x0144
        L_0x014e:
            r4 = move-exception
            r2 = r3
            goto L_0x0130
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.playback.PlaybackFileInfoUtils.writeToProperty(dji.component.playback.model.PlaybackFileInfo, java.io.File):void");
    }

    public static int getRealFrameRate(@NonNull PlaybackFileInfo info) {
        switch (info.frameRate) {
            case 1:
            case 13:
                return 24;
            case 2:
                return 25;
            case 3:
                return 30;
            case 4:
                return 48;
            case 5:
                return 50;
            case 6:
                return 60;
            case 7:
                return 120;
            case 8:
                return 240;
            case 9:
            case 12:
            case 18:
            case 19:
            case 20:
            default:
                return 0;
            case 10:
                return 100;
            case 11:
                return 96;
            case 14:
                return 30;
            case 15:
                return 48;
            case 16:
                return 60;
            case 17:
                return 90;
            case 21:
                return 8;
        }
    }
}
