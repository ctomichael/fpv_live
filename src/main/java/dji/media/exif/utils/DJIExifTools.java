package dji.media.exif.utils;

import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import dji.thirdparty.sanselan.ImageWriteException;
import dji.thirdparty.sanselan.formats.tiff.constants.ExifTagConstants;
import dji.thirdparty.sanselan.formats.tiff.constants.TagInfo;
import dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory;
import dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DJIExifTools {
    public static void updateExifByMap(File destFile, Float fnumber, String shutter, Integer exposureMode, Integer whiteBalance, Integer iso, String model, String date) {
        try {
            ExifInterface exif = new ExifInterface(destFile.getAbsolutePath());
            exif.setAttribute("ExposureTime", shutter);
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(destFile.getAbsolutePath(), opts);
        HashMap<TagInfo, Object> hashMap = new HashMap<>();
        hashMap.put(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_WIDTH, Integer.valueOf(opts.outWidth));
        hashMap.put(ExifTagConstants.EXIF_TAG_EXIF_IMAGE_LENGTH, Integer.valueOf(opts.outHeight));
        hashMap.put(ExifTagConstants.EXIF_TAG_EXPOSURE_MODE, exposureMode);
        hashMap.put(ExifTagConstants.EXIF_TAG_WHITE_BALANCE_1, whiteBalance);
        hashMap.put(ExifTagConstants.EXIF_TAG_ISO, iso);
        hashMap.put(ExifTagConstants.EXIF_TAG_FNUMBER, fnumber);
        hashMap.put(ExifTagConstants.EXIF_TAG_MODEL, model);
        hashMap.put(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, date);
        hashMap.put(ExifTagConstants.EXIF_TAG_CREATE_DATE, date);
        updateExifByMap(destFile, hashMap);
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x00a3 A[SYNTHETIC, Splitter:B:26:0x00a3] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00c8 A[SYNTHETIC, Splitter:B:35:0x00c8] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00fc A[SYNTHETIC, Splitter:B:54:0x00fc] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0107 A[SYNTHETIC, Splitter:B:59:0x0107] */
    /* JADX WARNING: Removed duplicated region for block: B:81:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:82:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:85:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:12:0x0071=Splitter:B:12:0x0071, B:32:0x00c3=Splitter:B:32:0x00c3, B:51:0x00f7=Splitter:B:51:0x00f7, B:23:0x009e=Splitter:B:23:0x009e} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void updateExifByMap(java.io.File r14, java.util.HashMap<dji.thirdparty.sanselan.formats.tiff.constants.TagInfo, java.lang.Object> r15) {
        /*
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = r14.getAbsolutePath()
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r12 = ".tmp"
            java.lang.StringBuilder r11 = r11.append(r12)
            java.lang.String r8 = r11.toString()
            java.io.File r7 = new java.io.File
            r7.<init>(r8)
            r9 = 0
            r11 = 73
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet r6 = getSanselanOutputSet(r14, r11)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.util.List r5 = r6.getDirectories()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            int r11 = r5.size()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            if (r11 <= 0) goto L_0x007a
            r11 = 0
            java.lang.Object r0 = r5.get(r11)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory r0 = (dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory) r0     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
        L_0x0035:
            java.util.Set r11 = r15.entrySet()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.util.Iterator r3 = r11.iterator()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
        L_0x003d:
            boolean r11 = r3.hasNext()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            if (r11 == 0) goto L_0x00ce
            java.lang.Object r2 = r3.next()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Object r11 = r2.getKey()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r11 = (dji.thirdparty.sanselan.formats.tiff.constants.TagInfo) r11     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            r0.removeField(r11)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Object r11 = r2.getValue()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            boolean r11 = r11 instanceof java.lang.Number     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            if (r11 == 0) goto L_0x007f
            java.lang.Object r11 = r2.getKey()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r11 = (dji.thirdparty.sanselan.formats.tiff.constants.TagInfo) r11     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            int r13 = r6.byteOrder     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Object r12 = r2.getValue()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Number r12 = (java.lang.Number) r12     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField r4 = dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField.create(r11, r13, r12)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            r0.add(r4)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            goto L_0x003d
        L_0x0070:
            r1 = move-exception
        L_0x0071:
            r1.printStackTrace()     // Catch:{ all -> 0x0104 }
            if (r9 == 0) goto L_0x0079
            r9.close()     // Catch:{ IOException -> 0x010b }
        L_0x0079:
            return
        L_0x007a:
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory r0 = r6.getOrCreateExifDirectory()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            goto L_0x0035
        L_0x007f:
            java.lang.Object r11 = r2.getValue()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            boolean r11 = r11 instanceof java.lang.String     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            if (r11 == 0) goto L_0x00a9
            java.lang.Object r11 = r2.getKey()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r11 = (dji.thirdparty.sanselan.formats.tiff.constants.TagInfo) r11     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            int r13 = r6.byteOrder     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Object r12 = r2.getValue()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.String r12 = (java.lang.String) r12     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField r4 = dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField.create(r11, r13, r12)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            r0.add(r4)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            goto L_0x003d
        L_0x009d:
            r1 = move-exception
        L_0x009e:
            r1.printStackTrace()     // Catch:{ all -> 0x0104 }
            if (r9 == 0) goto L_0x0079
            r9.close()     // Catch:{ IOException -> 0x00a7 }
            goto L_0x0079
        L_0x00a7:
            r11 = move-exception
            goto L_0x0079
        L_0x00a9:
            java.lang.Object r11 = r2.getKey()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r11 = (dji.thirdparty.sanselan.formats.tiff.constants.TagInfo) r11     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            int r13 = r6.byteOrder     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Object r12 = r2.getValue()     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Number[] r12 = (java.lang.Number[]) r12     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.lang.Number[] r12 = (java.lang.Number[]) r12     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField r4 = dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField.create(r11, r13, r12)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            r0.add(r4)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            goto L_0x003d
        L_0x00c2:
            r1 = move-exception
        L_0x00c3:
            r1.printStackTrace()     // Catch:{ all -> 0x0104 }
            if (r9 == 0) goto L_0x0079
            r9.close()     // Catch:{ IOException -> 0x00cc }
            goto L_0x0079
        L_0x00cc:
            r11 = move-exception
            goto L_0x0079
        L_0x00ce:
            java.io.BufferedOutputStream r10 = new java.io.BufferedOutputStream     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            java.io.FileOutputStream r11 = new java.io.FileOutputStream     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            r11.<init>(r7)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            r10.<init>(r11)     // Catch:{ ImageWriteException -> 0x0070, FileNotFoundException -> 0x009d, ImageReadException -> 0x00c2, IOException -> 0x00f6 }
            dji.thirdparty.sanselan.formats.jpeg.exifRewrite.ExifRewriter r11 = new dji.thirdparty.sanselan.formats.jpeg.exifRewrite.ExifRewriter     // Catch:{ ImageWriteException -> 0x011c, FileNotFoundException -> 0x0119, ImageReadException -> 0x0116, IOException -> 0x0113, all -> 0x0110 }
            r11.<init>()     // Catch:{ ImageWriteException -> 0x011c, FileNotFoundException -> 0x0119, ImageReadException -> 0x0116, IOException -> 0x0113, all -> 0x0110 }
            r11.updateExifMetadataLossless(r14, r10, r6)     // Catch:{ ImageWriteException -> 0x011c, FileNotFoundException -> 0x0119, ImageReadException -> 0x0116, IOException -> 0x0113, all -> 0x0110 }
            r10.close()     // Catch:{ ImageWriteException -> 0x011c, FileNotFoundException -> 0x0119, ImageReadException -> 0x0116, IOException -> 0x0113, all -> 0x0110 }
            boolean r11 = r14.delete()     // Catch:{ ImageWriteException -> 0x011c, FileNotFoundException -> 0x0119, ImageReadException -> 0x0116, IOException -> 0x0113, all -> 0x0110 }
            if (r11 == 0) goto L_0x00ec
            r7.renameTo(r14)     // Catch:{ ImageWriteException -> 0x011c, FileNotFoundException -> 0x0119, ImageReadException -> 0x0116, IOException -> 0x0113, all -> 0x0110 }
        L_0x00ec:
            if (r10 == 0) goto L_0x0120
            r10.close()     // Catch:{ IOException -> 0x00f3 }
            r9 = r10
            goto L_0x0079
        L_0x00f3:
            r11 = move-exception
            r9 = r10
            goto L_0x0079
        L_0x00f6:
            r1 = move-exception
        L_0x00f7:
            r1.printStackTrace()     // Catch:{ all -> 0x0104 }
            if (r9 == 0) goto L_0x0079
            r9.close()     // Catch:{ IOException -> 0x0101 }
            goto L_0x0079
        L_0x0101:
            r11 = move-exception
            goto L_0x0079
        L_0x0104:
            r11 = move-exception
        L_0x0105:
            if (r9 == 0) goto L_0x010a
            r9.close()     // Catch:{ IOException -> 0x010e }
        L_0x010a:
            throw r11
        L_0x010b:
            r11 = move-exception
            goto L_0x0079
        L_0x010e:
            r12 = move-exception
            goto L_0x010a
        L_0x0110:
            r11 = move-exception
            r9 = r10
            goto L_0x0105
        L_0x0113:
            r1 = move-exception
            r9 = r10
            goto L_0x00f7
        L_0x0116:
            r1 = move-exception
            r9 = r10
            goto L_0x00c3
        L_0x0119:
            r1 = move-exception
            r9 = r10
            goto L_0x009e
        L_0x011c:
            r1 = move-exception
            r9 = r10
            goto L_0x0071
        L_0x0120:
            r9 = r10
            goto L_0x0079
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.media.exif.utils.DJIExifTools.updateExifByMap(java.io.File, java.util.HashMap):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00fb A[SYNTHETIC, Splitter:B:39:0x00fb] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0157 A[SYNTHETIC, Splitter:B:65:0x0157] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x016c A[SYNTHETIC, Splitter:B:75:0x016c] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x017e A[SYNTHETIC, Splitter:B:83:0x017e] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:62:0x0152=Splitter:B:62:0x0152, B:36:0x00f6=Splitter:B:36:0x00f6, B:72:0x0167=Splitter:B:72:0x0167} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyExifData(java.io.File r24, java.io.File r25, java.util.List<dji.thirdparty.sanselan.formats.tiff.constants.TagInfo> r26) {
        /*
            java.lang.StringBuilder r21 = new java.lang.StringBuilder
            r21.<init>()
            java.lang.String r22 = r25.getAbsolutePath()
            java.lang.StringBuilder r21 = r21.append(r22)
            java.lang.String r22 = ".tmp"
            java.lang.StringBuilder r21 = r21.append(r22)
            java.lang.String r18 = r21.toString()
            r16 = 0
            r19 = 0
            android.graphics.BitmapFactory$Options r10 = new android.graphics.BitmapFactory$Options
            r10.<init>()
            r21 = 1
            r0 = r21
            r10.inJustDecodeBounds = r0
            java.lang.String r21 = r25.getAbsolutePath()
            r0 = r21
            android.graphics.BitmapFactory.decodeFile(r0, r10)
            java.io.File r17 = new java.io.File     // Catch:{ ImageReadException -> 0x01b9, ImageWriteException -> 0x0151, IOException -> 0x0166 }
            r17.<init>(r18)     // Catch:{ ImageReadException -> 0x01b9, ImageWriteException -> 0x0151, IOException -> 0x0166 }
            r21 = 73
            r0 = r24
            r1 = r21
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet r15 = getSanselanOutputSet(r0, r1)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            int r0 = r15.byteOrder     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r21 = r0
            r0 = r25
            r1 = r21
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet r3 = getSanselanOutputSet(r0, r1)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            int r0 = r15.byteOrder     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r21 = r0
            int r0 = r3.byteOrder     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r22 = r0
            r0 = r21
            r1 = r22
            if (r0 == r1) goto L_0x006e
            r21 = 0
            if (r19 == 0) goto L_0x0060
            r19.close()     // Catch:{ IOException -> 0x018d }
        L_0x0060:
            if (r17 == 0) goto L_0x006b
            boolean r22 = r17.exists()
            if (r22 == 0) goto L_0x006b
            r17.delete()
        L_0x006b:
            r16 = r17
        L_0x006d:
            return r21
        L_0x006e:
            r3.getOrCreateExifDirectory()     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            java.util.List r11 = r15.getDirectories()     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r8 = 0
        L_0x0076:
            int r21 = r11.size()     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r0 = r21
            if (r8 >= r0) goto L_0x010d
            java.lang.Object r12 = r11.get(r8)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory r12 = (dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory) r12     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputDirectory r4 = getOrCreateExifDirectory(r3, r12)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            if (r4 != 0) goto L_0x008d
        L_0x008a:
            int r8 = r8 + 1
            goto L_0x0076
        L_0x008d:
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r21 = dji.thirdparty.sanselan.formats.tiff.constants.ExifTagConstants.EXIF_TAG_EXIF_IMAGE_WIDTH     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            int r0 = r15.byteOrder     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r22 = r0
            int r0 = r10.outWidth     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r23 = r0
            java.lang.Integer r23 = java.lang.Integer.valueOf(r23)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField r7 = dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField.create(r21, r22, r23)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r4.add(r7)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r21 = dji.thirdparty.sanselan.formats.tiff.constants.ExifTagConstants.EXIF_TAG_EXIF_IMAGE_LENGTH     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            int r0 = r15.byteOrder     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r22 = r0
            int r0 = r10.outHeight     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r23 = r0
            java.lang.Integer r23 = java.lang.Integer.valueOf(r23)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField r6 = dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField.create(r21, r22, r23)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r4.add(r6)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            java.util.ArrayList r14 = r12.getFields()     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r9 = 0
        L_0x00bc:
            int r21 = r14.size()     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r0 = r21
            if (r9 >= r0) goto L_0x008a
            java.lang.Object r13 = r14.get(r9)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField r13 = (dji.thirdparty.sanselan.formats.tiff.write.TiffOutputField) r13     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            if (r26 == 0) goto L_0x00e6
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r0 = r13.tagInfo     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r21 = r0
            r0 = r26
            r1 = r21
            boolean r21 = r0.contains(r1)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            if (r21 == 0) goto L_0x00e6
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r0 = r13.tagInfo     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r21 = r0
            r0 = r21
            r4.removeField(r0)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
        L_0x00e3:
            int r9 = r9 + 1
            goto L_0x00bc
        L_0x00e6:
            dji.thirdparty.sanselan.formats.tiff.constants.TagInfo r0 = r13.tagInfo     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r21 = r0
            r0 = r21
            r4.removeField(r0)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r4.add(r13)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            goto L_0x00e3
        L_0x00f3:
            r5 = move-exception
            r16 = r17
        L_0x00f6:
            r5.printStackTrace()     // Catch:{ all -> 0x017b }
            if (r19 == 0) goto L_0x00fe
            r19.close()     // Catch:{ IOException -> 0x0192 }
        L_0x00fe:
            if (r16 == 0) goto L_0x0109
            boolean r21 = r16.exists()
            if (r21 == 0) goto L_0x0109
            r16.delete()
        L_0x0109:
            r21 = 0
            goto L_0x006d
        L_0x010d:
            java.io.BufferedOutputStream r20 = new java.io.BufferedOutputStream     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            java.io.FileOutputStream r21 = new java.io.FileOutputStream     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r0 = r21
            r1 = r17
            r0.<init>(r1)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            r20.<init>(r21)     // Catch:{ ImageReadException -> 0x00f3, ImageWriteException -> 0x01af, IOException -> 0x01a5, all -> 0x019b }
            dji.thirdparty.sanselan.formats.jpeg.exifRewrite.ExifRewriter r21 = new dji.thirdparty.sanselan.formats.jpeg.exifRewrite.ExifRewriter     // Catch:{ ImageReadException -> 0x01bc, ImageWriteException -> 0x01b3, IOException -> 0x01a9, all -> 0x019f }
            r21.<init>()     // Catch:{ ImageReadException -> 0x01bc, ImageWriteException -> 0x01b3, IOException -> 0x01a9, all -> 0x019f }
            r0 = r21
            r1 = r25
            r2 = r20
            r0.updateExifMetadataLossless(r1, r2, r3)     // Catch:{ ImageReadException -> 0x01bc, ImageWriteException -> 0x01b3, IOException -> 0x01a9, all -> 0x019f }
            r20.close()     // Catch:{ ImageReadException -> 0x01bc, ImageWriteException -> 0x01b3, IOException -> 0x01a9, all -> 0x019f }
            boolean r21 = r25.delete()     // Catch:{ ImageReadException -> 0x01bc, ImageWriteException -> 0x01b3, IOException -> 0x01a9, all -> 0x019f }
            if (r21 == 0) goto L_0x0139
            r0 = r17
            r1 = r25
            r0.renameTo(r1)     // Catch:{ ImageReadException -> 0x01bc, ImageWriteException -> 0x01b3, IOException -> 0x01a9, all -> 0x019f }
        L_0x0139:
            r21 = 1
            if (r20 == 0) goto L_0x0140
            r20.close()     // Catch:{ IOException -> 0x0190 }
        L_0x0140:
            if (r17 == 0) goto L_0x014b
            boolean r22 = r17.exists()
            if (r22 == 0) goto L_0x014b
            r17.delete()
        L_0x014b:
            r19 = r20
            r16 = r17
            goto L_0x006d
        L_0x0151:
            r5 = move-exception
        L_0x0152:
            r5.printStackTrace()     // Catch:{ all -> 0x017b }
            if (r19 == 0) goto L_0x015a
            r19.close()     // Catch:{ IOException -> 0x0195 }
        L_0x015a:
            if (r16 == 0) goto L_0x0109
            boolean r21 = r16.exists()
            if (r21 == 0) goto L_0x0109
            r16.delete()
            goto L_0x0109
        L_0x0166:
            r5 = move-exception
        L_0x0167:
            r5.printStackTrace()     // Catch:{ all -> 0x017b }
            if (r19 == 0) goto L_0x016f
            r19.close()     // Catch:{ IOException -> 0x0197 }
        L_0x016f:
            if (r16 == 0) goto L_0x0109
            boolean r21 = r16.exists()
            if (r21 == 0) goto L_0x0109
            r16.delete()
            goto L_0x0109
        L_0x017b:
            r21 = move-exception
        L_0x017c:
            if (r19 == 0) goto L_0x0181
            r19.close()     // Catch:{ IOException -> 0x0199 }
        L_0x0181:
            if (r16 == 0) goto L_0x018c
            boolean r22 = r16.exists()
            if (r22 == 0) goto L_0x018c
            r16.delete()
        L_0x018c:
            throw r21
        L_0x018d:
            r22 = move-exception
            goto L_0x0060
        L_0x0190:
            r22 = move-exception
            goto L_0x0140
        L_0x0192:
            r21 = move-exception
            goto L_0x00fe
        L_0x0195:
            r21 = move-exception
            goto L_0x015a
        L_0x0197:
            r21 = move-exception
            goto L_0x016f
        L_0x0199:
            r22 = move-exception
            goto L_0x0181
        L_0x019b:
            r21 = move-exception
            r16 = r17
            goto L_0x017c
        L_0x019f:
            r21 = move-exception
            r19 = r20
            r16 = r17
            goto L_0x017c
        L_0x01a5:
            r5 = move-exception
            r16 = r17
            goto L_0x0167
        L_0x01a9:
            r5 = move-exception
            r19 = r20
            r16 = r17
            goto L_0x0167
        L_0x01af:
            r5 = move-exception
            r16 = r17
            goto L_0x0152
        L_0x01b3:
            r5 = move-exception
            r19 = r20
            r16 = r17
            goto L_0x0152
        L_0x01b9:
            r5 = move-exception
            goto L_0x00f6
        L_0x01bc:
            r5 = move-exception
            r19 = r20
            r16 = r17
            goto L_0x00f6
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.media.exif.utils.DJIExifTools.copyExifData(java.io.File, java.io.File, java.util.List):boolean");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [dji.thirdparty.sanselan.common.IImageMetadata], assign insn: 0x0002: INVOKE  (r2v0 ? I:dji.thirdparty.sanselan.common.IImageMetadata) = (r5v0 'jpegImageFile' java.io.File A[D('jpegImageFile' java.io.File)]) type: STATIC call: dji.thirdparty.sanselan.Sanselan.getMetadata(java.io.File):dji.thirdparty.sanselan.common.IImageMetadata */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet getSanselanOutputSet(java.io.File r5, int r6) throws java.io.IOException, dji.thirdparty.sanselan.ImageReadException, dji.thirdparty.sanselan.ImageWriteException {
        /*
            r0 = 0
            r3 = 0
            dji.thirdparty.sanselan.common.IImageMetadata r2 = dji.thirdparty.sanselan.Sanselan.getMetadata(r5)
            boolean r4 = r2 instanceof dji.thirdparty.sanselan.formats.jpeg.JpegImageMetadata
            if (r4 == 0) goto L_0x0023
            r1 = r2
            dji.thirdparty.sanselan.formats.jpeg.JpegImageMetadata r1 = (dji.thirdparty.sanselan.formats.jpeg.JpegImageMetadata) r1
            if (r1 == 0) goto L_0x0019
            dji.thirdparty.sanselan.formats.tiff.TiffImageMetadata r0 = r1.getExif()
            if (r0 == 0) goto L_0x0019
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet r3 = r0.getOutputSet()
        L_0x0019:
            if (r3 != 0) goto L_0x0022
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet r3 = new dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet
            if (r0 != 0) goto L_0x002d
        L_0x001f:
            r3.<init>(r6)
        L_0x0022:
            return r3
        L_0x0023:
            r0 = r2
            dji.thirdparty.sanselan.formats.tiff.TiffImageMetadata r0 = (dji.thirdparty.sanselan.formats.tiff.TiffImageMetadata) r0
            if (r0 == 0) goto L_0x0019
            dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet r3 = r0.getOutputSet()
            goto L_0x0019
        L_0x002d:
            dji.thirdparty.sanselan.formats.tiff.TiffContents r4 = r0.contents
            dji.thirdparty.sanselan.formats.tiff.TiffHeader r4 = r4.header
            int r6 = r4.byteOrder
            goto L_0x001f
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.media.exif.utils.DJIExifTools.getSanselanOutputSet(java.io.File, int):dji.thirdparty.sanselan.formats.tiff.write.TiffOutputSet");
    }

    private static TiffOutputDirectory getOrCreateExifDirectory(TiffOutputSet outputSet, TiffOutputDirectory outputDirectory) {
        TiffOutputDirectory result = outputSet.findDirectory(outputDirectory.type);
        if (result != null) {
            return result;
        }
        TiffOutputDirectory result2 = new TiffOutputDirectory(outputDirectory.type);
        try {
            outputSet.addDirectory(result2);
            return result2;
        } catch (ImageWriteException e) {
            return null;
        }
    }
}
