package dji.thirdparty.afinal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import dji.thirdparty.afinal.bitmap.core.BitmapCache;
import dji.thirdparty.afinal.bitmap.core.BitmapDisplayConfig;
import dji.thirdparty.afinal.bitmap.core.BitmapProcess;
import dji.thirdparty.afinal.bitmap.display.Displayer;
import dji.thirdparty.afinal.bitmap.display.SimpleDisplayer;
import dji.thirdparty.afinal.bitmap.download.Downloader;
import dji.thirdparty.afinal.bitmap.download.SimpleDownloader;
import dji.thirdparty.afinal.core.AsyncTask;
import dji.thirdparty.afinal.utils.Utils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FinalBitmap {
    private static FinalBitmap mFinalBitmap;
    private ExecutorService bitmapLoadAndDisplayExecutor;
    private HashMap<String, BitmapDisplayConfig> configMap = new HashMap<>();
    private BitmapProcess mBitmapProcess;
    /* access modifiers changed from: private */
    public FinalBitmapConfig mConfig;
    private Context mContext;
    /* access modifiers changed from: private */
    public boolean mExitTasksEarly = false;
    /* access modifiers changed from: private */
    public BitmapCache mImageCache;
    private boolean mInit = false;
    /* access modifiers changed from: private */
    public boolean mPauseWork = false;
    /* access modifiers changed from: private */
    public final Object mPauseWorkLock = new Object();

    private FinalBitmap(Context context) {
        this.mContext = context;
        this.mConfig = new FinalBitmapConfig(context);
        configDiskCachePath(Utils.getDiskCacheDir(context, "afinalCache").getAbsolutePath());
        configDisplayer(new SimpleDisplayer());
        configDownlader(new SimpleDownloader());
    }

    public static synchronized FinalBitmap create(Context ctx) {
        FinalBitmap finalBitmap;
        synchronized (FinalBitmap.class) {
            if (mFinalBitmap == null) {
                mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
            }
            finalBitmap = mFinalBitmap;
        }
        return finalBitmap;
    }

    public FinalBitmap configLoadingImage(Bitmap bitmap) {
        this.mConfig.defaultDisplayConfig.setLoadingBitmap(bitmap);
        return this;
    }

    public FinalBitmap configLoadingImage(int resId) {
        this.mConfig.defaultDisplayConfig.setLoadingBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), resId));
        return this;
    }

    public FinalBitmap configLoadfailImage(Bitmap bitmap) {
        this.mConfig.defaultDisplayConfig.setLoadfailBitmap(bitmap);
        return this;
    }

    public FinalBitmap configLoadfailImage(int resId) {
        this.mConfig.defaultDisplayConfig.setLoadfailBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), resId));
        return this;
    }

    public FinalBitmap configBitmapMaxHeight(int bitmapHeight) {
        this.mConfig.defaultDisplayConfig.setBitmapHeight(bitmapHeight);
        return this;
    }

    public FinalBitmap configBitmapMaxWidth(int bitmapWidth) {
        this.mConfig.defaultDisplayConfig.setBitmapWidth(bitmapWidth);
        return this;
    }

    public FinalBitmap configDownlader(Downloader downlader) {
        this.mConfig.downloader = downlader;
        return this;
    }

    public FinalBitmap configDisplayer(Displayer displayer) {
        this.mConfig.displayer = displayer;
        return this;
    }

    public FinalBitmap configDiskCachePath(String strPath) {
        if (!TextUtils.isEmpty(strPath)) {
            this.mConfig.cachePath = strPath;
        }
        return this;
    }

    public FinalBitmap configMemoryCacheSize(int size) {
        this.mConfig.memCacheSize = size;
        return this;
    }

    public FinalBitmap configMemoryCachePercent(float percent) {
        this.mConfig.memCacheSizePercent = percent;
        return this;
    }

    public FinalBitmap configDiskCacheSize(int size) {
        this.mConfig.diskCacheSize = size;
        return this;
    }

    public FinalBitmap configBitmapLoadThreadSize(int size) {
        if (size >= 1) {
            this.mConfig.poolSize = size;
        }
        return this;
    }

    public FinalBitmap configRecycleImmediately(boolean recycleImmediately) {
        this.mConfig.recycleImmediately = recycleImmediately;
        return this;
    }

    private FinalBitmap init() {
        if (!this.mInit) {
            BitmapCache.ImageCacheParams imageCacheParams = new BitmapCache.ImageCacheParams(this.mConfig.cachePath);
            if (((double) this.mConfig.memCacheSizePercent) > 0.05d && ((double) this.mConfig.memCacheSizePercent) < 0.8d) {
                imageCacheParams.setMemCacheSizePercent(this.mContext, this.mConfig.memCacheSizePercent);
            } else if (this.mConfig.memCacheSize > 2097152) {
                imageCacheParams.setMemCacheSize(this.mConfig.memCacheSize);
            } else {
                imageCacheParams.setMemCacheSizePercent(this.mContext, 0.3f);
            }
            if (this.mConfig.diskCacheSize > 5242880) {
                imageCacheParams.setDiskCacheSize(this.mConfig.diskCacheSize);
            }
            imageCacheParams.setRecycleImmediately(this.mConfig.recycleImmediately);
            this.mImageCache = new BitmapCache(imageCacheParams);
            this.bitmapLoadAndDisplayExecutor = new ThreadPoolExecutor(0, this.mConfig.poolSize, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
                /* class dji.thirdparty.afinal.FinalBitmap.AnonymousClass1 */
                private final AtomicInteger mCount = new AtomicInteger(1);

                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "FinalBitmap #" + this.mCount.getAndIncrement());
                    thread.setPriority(4);
                    return thread;
                }
            });
            this.mBitmapProcess = new BitmapProcess(this.mConfig.downloader, this.mImageCache);
            this.mInit = true;
        }
        return this;
    }

    public void display(View imageView, String uri) {
        doDisplay(imageView, uri, null);
    }

    public void display(View imageView, String uri, int imageWidth, int imageHeight) {
        BitmapDisplayConfig displayConfig = this.configMap.get(imageWidth + "_" + imageHeight);
        if (displayConfig == null) {
            displayConfig = getDisplayConfig();
            displayConfig.setBitmapHeight(imageHeight);
            displayConfig.setBitmapWidth(imageWidth);
            this.configMap.put(imageWidth + "_" + imageHeight, displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, Bitmap loadingBitmap) {
        BitmapDisplayConfig displayConfig = this.configMap.get(String.valueOf(loadingBitmap));
        if (displayConfig == null) {
            displayConfig = getDisplayConfig();
            displayConfig.setLoadingBitmap(loadingBitmap);
            this.configMap.put(String.valueOf(loadingBitmap), displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, Bitmap loadingBitmap, Bitmap laodfailBitmap) {
        BitmapDisplayConfig displayConfig = this.configMap.get(String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap));
        if (displayConfig == null) {
            displayConfig = getDisplayConfig();
            displayConfig.setLoadingBitmap(loadingBitmap);
            displayConfig.setLoadfailBitmap(laodfailBitmap);
            this.configMap.put(String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap), displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, int imageWidth, int imageHeight, Bitmap loadingBitmap, Bitmap laodfailBitmap) {
        BitmapDisplayConfig displayConfig = this.configMap.get(imageWidth + "_" + imageHeight + "_" + String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap));
        if (displayConfig == null) {
            displayConfig = getDisplayConfig();
            displayConfig.setBitmapHeight(imageHeight);
            displayConfig.setBitmapWidth(imageWidth);
            displayConfig.setLoadingBitmap(loadingBitmap);
            displayConfig.setLoadfailBitmap(laodfailBitmap);
            this.configMap.put(imageWidth + "_" + imageHeight + "_" + String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap), displayConfig);
        }
        doDisplay(imageView, uri, displayConfig);
    }

    public void display(View imageView, String uri, BitmapDisplayConfig config) {
        doDisplay(imageView, uri, config);
    }

    private void doDisplay(View imageView, String uri, BitmapDisplayConfig displayConfig) {
        if (!this.mInit) {
            init();
        }
        if (!TextUtils.isEmpty(uri) && imageView != null) {
            if (displayConfig == null) {
                displayConfig = this.mConfig.defaultDisplayConfig;
            }
            Bitmap bitmap = null;
            if (this.mImageCache != null) {
                bitmap = this.mImageCache.getBitmapFromMemoryCache(uri);
            }
            if (bitmap != null) {
                if (imageView instanceof ImageView) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }
            } else if (checkImageTask(uri, imageView)) {
                BitmapLoadAndDisplayTask task = new BitmapLoadAndDisplayTask(imageView, displayConfig);
                AsyncDrawable asyncDrawable = new AsyncDrawable(this.mContext.getResources(), displayConfig.getLoadingBitmap(), task);
                if (imageView instanceof ImageView) {
                    ((ImageView) imageView).setImageDrawable(asyncDrawable);
                } else {
                    imageView.setBackgroundDrawable(asyncDrawable);
                }
                task.executeOnExecutor(this.bitmapLoadAndDisplayExecutor, uri);
            }
        }
    }

    private BitmapDisplayConfig getDisplayConfig() {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setAnimation(this.mConfig.defaultDisplayConfig.getAnimation());
        config.setAnimationType(this.mConfig.defaultDisplayConfig.getAnimationType());
        config.setBitmapHeight(this.mConfig.defaultDisplayConfig.getBitmapHeight());
        config.setBitmapWidth(this.mConfig.defaultDisplayConfig.getBitmapWidth());
        config.setLoadfailBitmap(this.mConfig.defaultDisplayConfig.getLoadfailBitmap());
        config.setLoadingBitmap(this.mConfig.defaultDisplayConfig.getLoadingBitmap());
        return config;
    }

    /* access modifiers changed from: private */
    public void clearCacheInternalInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.clearCache();
        }
    }

    /* access modifiers changed from: private */
    public void clearDiskCacheInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.clearDiskCache();
        }
    }

    /* access modifiers changed from: private */
    public void clearCacheInBackgroud(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearCache(key);
        }
    }

    /* access modifiers changed from: private */
    public void clearDiskCacheInBackgroud(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearDiskCache(key);
        }
    }

    /* access modifiers changed from: private */
    public void closeCacheInternalInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.close();
            this.mImageCache = null;
            mFinalBitmap = null;
        }
    }

    /* access modifiers changed from: private */
    public Bitmap processBitmap(String uri, BitmapDisplayConfig config) {
        if (this.mBitmapProcess != null) {
            return this.mBitmapProcess.getBitmap(uri, config);
        }
        return null;
    }

    public Bitmap getBitmapFromCache(String uri) {
        Bitmap bitmap = getBitmapFromMemoryCache(uri);
        if (bitmap == null) {
            return getBitmapFromDiskCache(uri);
        }
        return bitmap;
    }

    public Bitmap getBitmapFromMemoryCache(String uri) {
        return this.mImageCache.getBitmapFromMemoryCache(uri);
    }

    public Bitmap getBitmapFromDiskCache(String uri) {
        return getBitmapFromDiskCache(uri, null);
    }

    public Bitmap getBitmapFromDiskCache(String uri, BitmapDisplayConfig config) {
        return this.mBitmapProcess.getFromDisk(uri, config);
    }

    private void setExitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
    }

    public void onResume() {
        setExitTasksEarly(false);
    }

    public void onPause() {
        setExitTasksEarly(true);
    }

    public void onDestroy() {
        closeCache();
    }

    public void clearCache() {
        new CacheExecutecTask().execute(1);
    }

    public void clearCache(String uri) {
        new CacheExecutecTask().execute(4, uri);
    }

    public void clearMemoryCache() {
        if (this.mImageCache != null) {
            this.mImageCache.clearMemoryCache();
        }
    }

    public void clearMemoryCache(String uri) {
        if (this.mImageCache != null) {
            this.mImageCache.clearMemoryCache(uri);
        }
    }

    public void clearDiskCache() {
        new CacheExecutecTask().execute(3);
    }

    public void clearDiskCache(String uri) {
        new CacheExecutecTask().execute(5, uri);
    }

    private void closeCache() {
        new CacheExecutecTask().execute(2);
    }

    public void exitTasksEarly(boolean exitTasksEarly) {
        this.mExitTasksEarly = exitTasksEarly;
        if (exitTasksEarly) {
            pauseWork(false);
        }
    }

    public void pauseWork(boolean pauseWork) {
        synchronized (this.mPauseWorkLock) {
            this.mPauseWork = pauseWork;
            if (!this.mPauseWork) {
                this.mPauseWorkLock.notifyAll();
            }
        }
    }

    /* access modifiers changed from: private */
    public static BitmapLoadAndDisplayTask getBitmapTaskFromImageView(View imageView) {
        Drawable drawable;
        if (imageView != null) {
            if (imageView instanceof ImageView) {
                drawable = ((ImageView) imageView).getDrawable();
            } else {
                drawable = imageView.getBackground();
            }
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getBitmapWorkerTask();
            }
        }
        return null;
    }

    private static boolean checkImageTask(Object data, View imageView) {
        BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);
        if (bitmapWorkerTask == null) {
            return true;
        }
        Object bitmapData = bitmapWorkerTask.data;
        if (bitmapData != null && bitmapData.equals(data)) {
            return false;
        }
        bitmapWorkerTask.cancel(true);
        return true;
    }

    private static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<BitmapLoadAndDisplayTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapLoadAndDisplayTask bitmapWorkerTask) {
            super(res, bitmap);
            this.bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        public BitmapLoadAndDisplayTask getBitmapWorkerTask() {
            return this.bitmapWorkerTaskReference.get();
        }
    }

    private class CacheExecutecTask extends AsyncTask<Object, Void, Void> {
        public static final int MESSAGE_CLEAR = 1;
        public static final int MESSAGE_CLEAR_DISK = 3;
        public static final int MESSAGE_CLEAR_KEY = 4;
        public static final int MESSAGE_CLEAR_KEY_IN_DISK = 5;
        public static final int MESSAGE_CLOSE = 2;

        private CacheExecutecTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Object... params) {
            switch (((Integer) params[0]).intValue()) {
                case 1:
                    FinalBitmap.this.clearCacheInternalInBackgroud();
                    return null;
                case 2:
                    FinalBitmap.this.closeCacheInternalInBackgroud();
                    return null;
                case 3:
                    FinalBitmap.this.clearDiskCacheInBackgroud();
                    return null;
                case 4:
                    FinalBitmap.this.clearCacheInBackgroud(String.valueOf(params[1]));
                    return null;
                case 5:
                    FinalBitmap.this.clearDiskCacheInBackgroud(String.valueOf(params[1]));
                    return null;
                default:
                    return null;
            }
        }
    }

    private class BitmapLoadAndDisplayTask extends AsyncTask<Object, Void, Bitmap> {
        /* access modifiers changed from: private */
        public Object data;
        private final BitmapDisplayConfig displayConfig;
        private final WeakReference<View> imageViewReference;

        public BitmapLoadAndDisplayTask(View imageView, BitmapDisplayConfig config) {
            this.imageViewReference = new WeakReference<>(imageView);
            this.displayConfig = config;
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(Object... params) {
            this.data = params[0];
            String dataString = String.valueOf(this.data);
            Bitmap bitmap = null;
            synchronized (FinalBitmap.this.mPauseWorkLock) {
                while (FinalBitmap.this.mPauseWork && !isCancelled()) {
                    try {
                        FinalBitmap.this.mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (0 == 0 && !isCancelled() && getAttachedImageView() != null && !FinalBitmap.this.mExitTasksEarly) {
                bitmap = FinalBitmap.this.processBitmap(dataString, this.displayConfig);
            }
            if (bitmap != null) {
                FinalBitmap.this.mImageCache.addToMemoryCache(dataString, bitmap);
            }
            return bitmap;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            if (!isCancelled() && !FinalBitmap.this.mExitTasksEarly) {
                View imageView = getAttachedImageView();
                if (bitmap != null && imageView != null) {
                    FinalBitmap.this.mConfig.displayer.loadCompletedisplay(imageView, bitmap, this.displayConfig);
                } else if (bitmap == null && imageView != null) {
                    FinalBitmap.this.mConfig.displayer.loadFailDisplay(imageView, this.displayConfig.getLoadfailBitmap());
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled(Bitmap bitmap) {
            super.onCancelled((Object) bitmap);
            synchronized (FinalBitmap.this.mPauseWorkLock) {
                FinalBitmap.this.mPauseWorkLock.notifyAll();
            }
        }

        private View getAttachedImageView() {
            View imageView = this.imageViewReference.get();
            if (this == FinalBitmap.getBitmapTaskFromImageView(imageView)) {
                return imageView;
            }
            return null;
        }
    }

    private class FinalBitmapConfig {
        public String cachePath;
        public BitmapDisplayConfig defaultDisplayConfig = new BitmapDisplayConfig();
        public int diskCacheSize;
        public Displayer displayer;
        public Downloader downloader;
        public int memCacheSize;
        public float memCacheSizePercent;
        public int poolSize = 3;
        public boolean recycleImmediately = true;

        public FinalBitmapConfig(Context context) {
            this.defaultDisplayConfig.setAnimation(null);
            this.defaultDisplayConfig.setAnimationType(1);
            int defaultWidth = (int) Math.floor((double) (context.getResources().getDisplayMetrics().widthPixels / 2));
            this.defaultDisplayConfig.setBitmapHeight(defaultWidth);
            this.defaultDisplayConfig.setBitmapWidth(defaultWidth);
        }
    }
}
