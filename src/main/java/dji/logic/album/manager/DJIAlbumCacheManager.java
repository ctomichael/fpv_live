package dji.logic.album.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Keep;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.LruCache;
import com.dji.frame.util.V_DiskUtil;
import com.dji.frame.util.V_FileUtil;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@Keep
@EXClassNullAway
public class DJIAlbumCacheManager {
    private static DJIAlbumCacheManager instance;
    private RandomAccessFile accessFile;
    private String cachePath = "";
    private LruCache<String, Bitmap> mMemoryCache;
    private BitmapFactory.Options options;
    private String renameTo = null;

    public static synchronized DJIAlbumCacheManager getInstance(Context context) {
        DJIAlbumCacheManager dJIAlbumCacheManager;
        synchronized (DJIAlbumCacheManager.class) {
            if (instance == null) {
                instance = new DJIAlbumCacheManager(context);
            }
            dJIAlbumCacheManager = instance;
        }
        return dJIAlbumCacheManager;
    }

    public static synchronized DJIAlbumCacheManager getInstance() {
        DJIAlbumCacheManager dJIAlbumCacheManager;
        synchronized (DJIAlbumCacheManager.class) {
            dJIAlbumCacheManager = instance;
        }
        return dJIAlbumCacheManager;
    }

    public DJIAlbumCacheManager(Context context) {
        this.cachePath = V_DiskUtil.getExternalCacheDirPath(context, "CACHE_IMAGE/");
        File dirFile = new File(this.cachePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        this.options = new BitmapFactory.Options();
        this.options.inPreferredConfig = Bitmap.Config.RGB_565;
        this.mMemoryCache = new LruCache<String, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) / 16) {
            /* class dji.logic.album.manager.DJIAlbumCacheManager.AnonymousClass1 */

            /* access modifiers changed from: protected */
            public int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemory(String key, Bitmap bitmap) {
        if (key != null && bitmap != null && !isBitmapExistInMemory(key)) {
            this.mMemoryCache.put(key, bitmap);
        }
    }

    public void addBitmapToMemory(String nameKey, byte[] buffer, int offset, int length) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, offset, length, this.options);
        if (nameKey != null && bitmap != null) {
            this.mMemoryCache.put(nameKey, bitmap);
        }
    }

    public Bitmap getBitmapFromMemory(String key) {
        return this.mMemoryCache.get(key);
    }

    public boolean isBitmapExistInMemory(String key) {
        return getBitmapFromMemory(key) != null;
    }

    public void clearMemCache() {
        this.mMemoryCache.evictAll();
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0054 A[SYNTHETIC, Splitter:B:33:0x0054] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addBitmapToDisk(java.lang.String r7, android.graphics.Bitmap r8) {
        /*
            r6 = this;
            if (r7 == 0) goto L_0x0004
            if (r8 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            boolean r4 = r6.isBitmapExistInDisk(r7)
            if (r4 != 0) goto L_0x0004
            java.io.File r1 = new java.io.File
            java.lang.String r4 = r6.getPath(r7)
            r1.<init>(r4)
            r2 = 0
            boolean r4 = r1.createNewFile()     // Catch:{ IOException -> 0x0042 }
            if (r4 != 0) goto L_0x0026
            if (r2 == 0) goto L_0x0004
            r2.close()     // Catch:{ IOException -> 0x0021 }
            goto L_0x0004
        L_0x0021:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0004
        L_0x0026:
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0042 }
            r3.<init>(r1)     // Catch:{ IOException -> 0x0042 }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ IOException -> 0x0060, all -> 0x005d }
            r5 = 100
            r8.compress(r4, r5, r3)     // Catch:{ IOException -> 0x0060, all -> 0x005d }
            r3.flush()     // Catch:{ IOException -> 0x0060, all -> 0x005d }
            if (r3 == 0) goto L_0x0063
            r3.close()     // Catch:{ IOException -> 0x003c }
            r2 = r3
            goto L_0x0004
        L_0x003c:
            r0 = move-exception
            r0.printStackTrace()
            r2 = r3
            goto L_0x0004
        L_0x0042:
            r0 = move-exception
        L_0x0043:
            r0.printStackTrace()     // Catch:{ all -> 0x0051 }
            if (r2 == 0) goto L_0x0004
            r2.close()     // Catch:{ IOException -> 0x004c }
            goto L_0x0004
        L_0x004c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0004
        L_0x0051:
            r4 = move-exception
        L_0x0052:
            if (r2 == 0) goto L_0x0057
            r2.close()     // Catch:{ IOException -> 0x0058 }
        L_0x0057:
            throw r4
        L_0x0058:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0057
        L_0x005d:
            r4 = move-exception
            r2 = r3
            goto L_0x0052
        L_0x0060:
            r0 = move-exception
            r2 = r3
            goto L_0x0043
        L_0x0063:
            r2 = r3
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.logic.album.manager.DJIAlbumCacheManager.addBitmapToDisk(java.lang.String, android.graphics.Bitmap):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x004c A[SYNTHETIC, Splitter:B:31:0x004c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addBitmapToDiskNoDecorder(java.lang.String r6, byte[] r7, int r8, int r9) {
        /*
            r5 = this;
            boolean r4 = r5.isBitmapExistInDisk(r6)
            if (r4 == 0) goto L_0x0007
        L_0x0006:
            return
        L_0x0007:
            java.io.File r1 = new java.io.File
            java.lang.String r4 = r5.getPath(r6)
            r1.<init>(r4)
            r2 = 0
            boolean r4 = r1.createNewFile()     // Catch:{ IOException -> 0x003a }
            if (r4 != 0) goto L_0x0022
            if (r2 == 0) goto L_0x0006
            r2.close()     // Catch:{ IOException -> 0x001d }
            goto L_0x0006
        L_0x001d:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0006
        L_0x0022:
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x003a }
            r3.<init>(r1)     // Catch:{ IOException -> 0x003a }
            r3.write(r7, r8, r9)     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            r3.flush()     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            if (r3 == 0) goto L_0x005b
            r3.close()     // Catch:{ IOException -> 0x0034 }
            r2 = r3
            goto L_0x0006
        L_0x0034:
            r0 = move-exception
            r0.printStackTrace()
            r2 = r3
            goto L_0x0006
        L_0x003a:
            r0 = move-exception
        L_0x003b:
            r0.printStackTrace()     // Catch:{ all -> 0x0049 }
            if (r2 == 0) goto L_0x0006
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0006
        L_0x0044:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0006
        L_0x0049:
            r4 = move-exception
        L_0x004a:
            if (r2 == 0) goto L_0x004f
            r2.close()     // Catch:{ IOException -> 0x0050 }
        L_0x004f:
            throw r4
        L_0x0050:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x004f
        L_0x0055:
            r4 = move-exception
            r2 = r3
            goto L_0x004a
        L_0x0058:
            r0 = move-exception
            r2 = r3
            goto L_0x003b
        L_0x005b:
            r2 = r3
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.logic.album.manager.DJIAlbumCacheManager.addBitmapToDiskNoDecorder(java.lang.String, byte[], int, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x004c A[SYNTHETIC, Splitter:B:29:0x004c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addBitmapToDiskNoCheck(java.lang.String r7, android.graphics.Bitmap r8) {
        /*
            r6 = this;
            java.io.File r1 = new java.io.File
            java.lang.String r4 = r6.getPath(r7)
            r1.<init>(r4)
            r2 = 0
            boolean r4 = r1.createNewFile()     // Catch:{ IOException -> 0x003a }
            if (r4 != 0) goto L_0x001b
            if (r2 == 0) goto L_0x0015
            r2.close()     // Catch:{ IOException -> 0x0016 }
        L_0x0015:
            return
        L_0x0016:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0015
        L_0x001b:
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x003a }
            r3.<init>(r1)     // Catch:{ IOException -> 0x003a }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            r5 = 100
            r8.compress(r4, r5, r3)     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            r3.flush()     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            r3.close()     // Catch:{ IOException -> 0x0058, all -> 0x0055 }
            if (r3 == 0) goto L_0x005b
            r3.close()     // Catch:{ IOException -> 0x0034 }
            r2 = r3
            goto L_0x0015
        L_0x0034:
            r0 = move-exception
            r0.printStackTrace()
            r2 = r3
            goto L_0x0015
        L_0x003a:
            r0 = move-exception
        L_0x003b:
            r0.printStackTrace()     // Catch:{ all -> 0x0049 }
            if (r2 == 0) goto L_0x0015
            r2.close()     // Catch:{ IOException -> 0x0044 }
            goto L_0x0015
        L_0x0044:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0015
        L_0x0049:
            r4 = move-exception
        L_0x004a:
            if (r2 == 0) goto L_0x004f
            r2.close()     // Catch:{ IOException -> 0x0050 }
        L_0x004f:
            throw r4
        L_0x0050:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x004f
        L_0x0055:
            r4 = move-exception
            r2 = r3
            goto L_0x004a
        L_0x0058:
            r0 = move-exception
            r2 = r3
            goto L_0x003b
        L_0x005b:
            r2 = r3
            goto L_0x0015
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.logic.album.manager.DJIAlbumCacheManager.addBitmapToDiskNoCheck(java.lang.String, android.graphics.Bitmap):void");
    }

    public Bitmap getBitmapFromDisk(String key) {
        return BitmapFactory.decodeFile(getPath(key), this.options);
    }

    public boolean isBitmapExistInDisk(String key) {
        if (!key.contains("org") || this.renameTo == null || !new File(getPath(this.renameTo, key)).exists()) {
            return new File(getPath(key)).exists();
        }
        return true;
    }

    public synchronized void openStreamCover(String key) {
        File f = new File(getPath(key));
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            this.accessFile = new RandomAccessFile(f, "rws");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return;
    }

    public synchronized void openStream(String key) {
        File f = new File(getPath(key));
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.accessFile = new RandomAccessFile(f, "rws");
            this.accessFile.seek(this.accessFile.length());
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return;
    }

    public synchronized void closeStream() {
        if (this.accessFile != null) {
            try {
                this.accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.accessFile = null;
        }
        return;
    }

    public synchronized void closeStream(long createtime) {
        if (this.accessFile != null) {
            try {
                this.accessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.accessFile = null;
        }
        return;
    }

    public synchronized void seekFile(long pos) {
        try {
            this.accessFile.seek(pos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    public synchronized void writeBuffer(byte[] buffer, int byteOffset, int byteCount) {
        try {
            if (this.accessFile != null) {
                this.accessFile.write(buffer, byteOffset, byteCount);
                if (this.accessFile.getFD() != null) {
                    this.accessFile.getFD().sync();
                } else {
                    DJILog.logWriteE("playback", "accessFile.getFD() is null!", "playback", new Object[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            DJILog.logWriteE("playback", "writeBuffer interrupted: " + Thread.currentThread(), "playback", new Object[0]);
        }
        return;
    }

    public long getLenCacheInDisk(String key) {
        File f = new File(getPath(key));
        if (f.exists()) {
            return f.length();
        }
        return 0;
    }

    public String getPath(String key) {
        File dirFile = new File(this.cachePath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return this.cachePath + key;
    }

    public String getPath(String pre, String key) {
        return pre + key;
    }

    public void clearDiskCache() {
        try {
            V_FileUtil.deleteAllFile(new File(this.cachePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setRenameTo(String renameTo2) {
        this.renameTo = renameTo2;
    }
}
