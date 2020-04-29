package dji.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import dji.log.impl.SimpleEncryption;
import dji.log.impl.SimpleFileFormat;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DJILogFileConfig {
    String LINE_FEED;
    String LOG_FILE_PREFIX;
    String LOG_FILE_TYPE;
    String LOG_PATH_ROOT;
    String LOG_TIME_FORMAT;
    long SPACE_MARGINAL;
    IEncryption encryption;
    IFileFormat fileFormat;
    boolean open;
    ExecutorService service;
    int versionCode;
    String versionName;

    public static class Builder {
        private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT, 10));
        private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
        private static final int MAXIMUM_POOL_SIZE = 10;
        DJILogFileConfig config = new DJILogFileConfig();

        public Builder(Context context) {
            setOpen(true);
            setSpaceMarginal(104857600);
            setFilePrefix("log-");
            setEncryption(new SimpleEncryption());
            setFileFormat(new SimpleFileFormat());
            setLogTimeFormat(DJILogConstant.LOG_INFO_TIME_FORMAT);
            setLineFeed("\r\n");
            if (context != null) {
                setPathRoot((Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator) + DJILogConstant.LOG_DIRECTORY_ROOT + context.getPackageName() + File.separator + DJILogConstant.LOG_DIR_ROOT);
                try {
                    PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
                    setVersionCode(info.versionCode);
                    setVersionName(info.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                setExecutorService(new ThreadPoolExecutor(CORE_POOL_SIZE, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory() {
                    /* class dji.log.DJILogFileConfig.Builder.AnonymousClass1 */
                    private final AtomicInteger mCount = new AtomicInteger(1);

                    public Thread newThread(@NonNull Runnable r) {
                        Thread thread = new Thread(r, "DJI Log #" + this.mCount.getAndIncrement());
                        thread.setPriority(3);
                        return thread;
                    }
                }));
            }
        }

        public Builder setOpen(boolean open) {
            this.config.open = open;
            return this;
        }

        public Builder setSpaceMarginal(long spaceMarginal) {
            this.config.SPACE_MARGINAL = spaceMarginal;
            return this;
        }

        public Builder setFilePrefix(String filePrefix) {
            this.config.LOG_FILE_PREFIX = filePrefix;
            return this;
        }

        public Builder setFileType(String fileType) {
            this.config.LOG_FILE_TYPE = fileType;
            return this;
        }

        public Builder setPathRoot(String pathRoot) {
            this.config.LOG_PATH_ROOT = pathRoot;
            return this;
        }

        public Builder setEncryption(IEncryption encryption) {
            this.config.encryption = encryption;
            return this;
        }

        public Builder setFileFormat(IFileFormat fileFormat) {
            this.config.fileFormat = fileFormat;
            return this;
        }

        public Builder setLogTimeFormat(String logTimeFormat) {
            this.config.LOG_TIME_FORMAT = logTimeFormat;
            return this;
        }

        public Builder setLineFeed(String lineFeed) {
            this.config.LINE_FEED = lineFeed;
            return this;
        }

        public Builder setVersionCode(int versionCode) {
            this.config.versionCode = versionCode;
            return this;
        }

        public Builder setVersionName(String versionName) {
            this.config.versionName = versionName;
            return this;
        }

        public Builder setExecutorService(ExecutorService service) {
            this.config.service = service;
            return this;
        }

        public DJILogFileConfig build() {
            return this.config;
        }
    }
}
