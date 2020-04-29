package com.dji.frame.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.webkit.MimeTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;

public class V_MediaUtil {
    private static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";
    /* access modifiers changed from: private */
    public static MediaScannerConnection sc = null;

    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
        int be;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;
        int h = options.outHeight;
        int beWidth = options.outWidth / width;
        int beHeight = h / height;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imagePath, options), width, height, 2);
    }

    public static Bitmap getVideoThumbnail(String videoPath, int width, int height, int kind) {
        return ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(videoPath, kind), width, height, 2);
    }

    public static Bitmap saveVideoThumbnail(String videoPath, String outPath) {
        Bitmap bitmap = ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(videoPath, 1), 512, 384, 2);
        V_FileUtil.saveMyBitmap(bitmap, outPath, MD5.getMD5(videoPath));
        return bitmap;
    }

    public static void scanDirAsync(Context ctx, String dir) {
        Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
        scanIntent.setData(Uri.fromFile(new File(dir)));
        ctx.sendBroadcast(scanIntent);
    }

    public static void scanFileAsync(Context ctx, String filePath) {
        Intent scanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        ctx.sendBroadcast(scanIntent);
    }

    public static void scanDir(Context ctx, final String filePath, final MediaScannerConnection.MediaScannerConnectionClient clientCallBack) {
        sc = new MediaScannerConnection(ctx, new MediaScannerConnection.MediaScannerConnectionClient() {
            /* class com.dji.frame.util.V_MediaUtil.AnonymousClass1 */

            public void onScanCompleted(String path, Uri uri) {
                clientCallBack.onScanCompleted(filePath, uri);
                V_MediaUtil.sc.disconnect();
            }

            public void onMediaScannerConnected() {
                clientCallBack.onMediaScannerConnected();
                V_MediaUtil.scan(V_MediaUtil.sc, filePath);
            }
        });
        sc.connect();
    }

    public static String getMimeTypeFromPath(String filePath) {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(filePath));
    }

    /* access modifiers changed from: private */
    public static void scan(MediaScannerConnection sc2, String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            try {
                Iterator<File> it2 = V_FileUtil.getAllFile(file).iterator();
                while (it2.hasNext()) {
                    File subFile = it2.next();
                    sc2.scanFile(subFile.getAbsolutePath(), getMimeTypeFromPath(subFile.getAbsolutePath()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            sc2.scanFile(filePath, getMimeTypeFromPath(filePath));
        }
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
            try {
                retriever.release();
            } catch (RuntimeException e) {
            }
        } catch (IllegalArgumentException e2) {
            try {
                retriever.release();
            } catch (RuntimeException e3) {
            }
        } catch (Exception e4) {
            try {
                retriever.release();
            } catch (RuntimeException e5) {
            }
        } catch (Throwable th) {
            try {
                retriever.release();
            } catch (RuntimeException e6) {
            }
            throw th;
        }
        if (1 != 3 || bitmap == null) {
            return bitmap;
        }
        return ThumbnailUtils.extractThumbnail(bitmap, width, height, 2);
    }
}
