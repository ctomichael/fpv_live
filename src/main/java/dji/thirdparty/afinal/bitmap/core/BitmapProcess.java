package dji.thirdparty.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import dji.thirdparty.afinal.bitmap.core.BytesBufferPool;
import dji.thirdparty.afinal.bitmap.download.Downloader;

public class BitmapProcess {
    private static final int BYTESBUFFER_SIZE = 204800;
    private static final int BYTESBUFFE_POOL_SIZE = 4;
    private static final BytesBufferPool sMicroThumbBufferPool = new BytesBufferPool(4, BYTESBUFFER_SIZE);
    private BitmapCache mCache;
    private Downloader mDownloader;

    public BitmapProcess(Downloader downloader, BitmapCache cache) {
        this.mDownloader = downloader;
        this.mCache = cache;
    }

    public Bitmap getBitmap(String url, BitmapDisplayConfig config) {
        byte[] data;
        Bitmap bitmap = getFromDisk(url, config);
        if (bitmap == null && (data = this.mDownloader.download(url)) != null && data.length > 0) {
            if (config == null) {
                return BitmapFactory.decodeByteArray(data, 0, data.length);
            }
            bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(data, 0, data.length, config.getBitmapWidth(), config.getBitmapHeight());
            this.mCache.addToDiskCache(url, data);
        }
        return bitmap;
    }

    public Bitmap getFromDisk(String key, BitmapDisplayConfig config) {
        BytesBufferPool.BytesBuffer buffer = sMicroThumbBufferPool.get();
        Bitmap b = null;
        try {
            if (this.mCache.getImageData(key, buffer) && buffer.length - buffer.offset > 0) {
                b = config != null ? BitmapDecoder.decodeSampledBitmapFromByteArray(buffer.data, buffer.offset, buffer.length, config.getBitmapWidth(), config.getBitmapHeight()) : BitmapFactory.decodeByteArray(buffer.data, buffer.offset, buffer.length);
            }
            return b;
        } finally {
            sMicroThumbBufferPool.recycle(buffer);
        }
    }
}
