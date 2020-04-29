package dji.midware.sdr.log;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.StatFs;
import android.support.annotation.Keep;
import android.support.v4.media.session.PlaybackStateCompat;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.MidWare;
import dji.midware.data.model.P3.DataOsdSetSdrStartLog;
import dji.midware.interfaces.DJIDataCallBack;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Keep
@EXClassNullAway
public class DJISdrLogDataReciever {
    private static boolean DEBUG = true;
    private static final int LOG_CACHE_SIZE = 1024;
    /* access modifiers changed from: private */
    public static String TAG = "DJISdrLogDataReciever";
    private static SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    /* access modifiers changed from: private */
    public static volatile FileOutputStream fosLogFilePort1 = null;
    /* access modifiers changed from: private */
    public static volatile FileOutputStream fosLogFilePort2 = null;
    /* access modifiers changed from: private */
    public static volatile FileOutputStream fosLogFilePort3 = null;
    private static DJISdrLogDataReciever instance = null;
    public static volatile boolean isOpenSdrLog = false;
    private static boolean isRecieveLog = false;
    private static WeakReference<Context> mContext = null;
    private static final String sdCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String sdrFileEnd = "-Port";
    private static final String sdrFilePrex = "SdrLog-";
    private static final String sdrLogDirName = (Environment.getExternalStorageDirectory() + "/DJI/SDR_LOG/");
    private static String sdr_port1_fileName;
    private static String sdr_port2_fileName;
    private static String sdr_port3_fileName;
    private int log1_cur_size = 0;
    private int log2_cur_size = 0;
    private int log3_cur_size = 0;
    private Handler mLogHandler;
    private HandlerThread mWriteLogThread;
    private byte[] port1_log_cache = new byte[1024];
    private byte[] port2_log_cache = new byte[1024];
    private byte[] port3_log_cache = new byte[1024];

    public static synchronized DJISdrLogDataReciever getInstance() {
        DJISdrLogDataReciever dJISdrLogDataReciever;
        synchronized (DJISdrLogDataReciever.class) {
            if (instance == null) {
                instance = new DJISdrLogDataReciever();
                if (!(MidWare.context == null || MidWare.context.get() == null)) {
                    mContext = new WeakReference<>(MidWare.context.get().getApplicationContext());
                }
                initLogFile();
            }
            dJISdrLogDataReciever = instance;
        }
        return dJISdrLogDataReciever;
    }

    public static void checkStorage() {
        String extStore = System.getenv("EXTERNAL_STORAGE");
        File f_exts = new File(extStore);
        if (DEBUG) {
            DJILogHelper.getInstance().LOGD(TAG, "checkStorage extStore= " + extStore, false, true);
            DJILogHelper.getInstance().LOGD(TAG, "checkStorage f_exts= " + f_exts.exists(), false, true);
        }
        String secStore = System.getenv("SECONDARY_STORAGE");
        File f_secs = new File(secStore);
        if (DEBUG) {
            DJILogHelper.getInstance().LOGD(TAG, "checkStorage secStore= " + secStore, false, true);
            DJILogHelper.getInstance().LOGD(TAG, "checkStorage f_secs= " + f_secs.exists(), false, true);
        }
    }

    private static long getAvailableStore(String filePath) {
        StatFs statFs = new StatFs(filePath);
        long availableSpare = ((((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
        if (DEBUG) {
            DJILogHelper.getInstance().LOGD(TAG, "getAvailableStore size= " + availableSpare, false, true);
        }
        return availableSpare;
    }

    private static void initLogFile() {
        if (isOpenSdrLog) {
            if (getAvailableStore(sdCardRoot) < 500) {
                DJILogHelper.getInstance().LOGE(TAG, "Init sdr log failed\nSdcard free size small than 500M.");
                return;
            }
            String dateStr = fileNameFormat.format(new Date());
            sdr_port1_fileName = sdrFilePrex + dateStr + "-CPARM0.log";
            sdr_port2_fileName = sdrFilePrex + dateStr + "-CPDSP0.log";
            sdr_port3_fileName = sdrFilePrex + dateStr + "-CPDSP1.log";
            if (Environment.getExternalStorageState().equals("mounted")) {
                File dir = new File(sdrLogDirName);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (DEBUG) {
                    DJILogHelper.getInstance().LOGD(TAG, "create sdr port1 log file: " + sdrLogDirName + sdr_port1_fileName, false, true);
                    DJILogHelper.getInstance().LOGD(TAG, "create sdr port2 log file: " + sdrLogDirName + sdr_port2_fileName, false, true);
                    DJILogHelper.getInstance().LOGD(TAG, "create sdr port3 log file: " + sdrLogDirName + sdr_port3_fileName, false, true);
                }
            }
        }
    }

    public boolean getIsRecieveFlag() {
        return isRecieveLog;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
    public void createLogFiles() {
        if (isOpenSdrLog) {
            String dateStr = fileNameFormat.format(new Date());
            sdr_port1_fileName = sdrFilePrex + dateStr + "-CPARM0.log";
            sdr_port2_fileName = sdrFilePrex + dateStr + "-CPDSP0.log";
            sdr_port3_fileName = sdrFilePrex + dateStr + "-CPDSP1.log";
            try {
                fosLogFilePort1 = new FileOutputStream(sdrLogDirName + sdr_port1_fileName, true);
                fosLogFilePort2 = new FileOutputStream(sdrLogDirName + sdr_port2_fileName, true);
                fosLogFilePort3 = new FileOutputStream(sdrLogDirName + sdr_port3_fileName, true);
            } catch (Exception e) {
            }
        }
    }

    public void setLogSaveEnable(boolean enable) {
        isOpenSdrLog = enable;
        if (sdr_port1_fileName == null) {
            initLogFile();
        }
    }

    public void setRcLogEnableToGround() {
        new DataOsdSetSdrStartLog().setIsOpen(true).start((DJIDataCallBack) null);
    }

    public void onRecvLogPort1(byte[] dataBuffer, int size) {
        onAoaRecvLogPort1(dataBuffer, 0, size);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
    public void onAoaRecvLogPort1(byte[] dataBuffer, int offset, int size) {
        if (isOpenSdrLog) {
            if (DEBUG) {
            }
            checkWriteThreadValid();
            try {
                if (fosLogFilePort1 == null && DEBUG) {
                    fosLogFilePort1 = new FileOutputStream(sdrLogDirName + sdr_port1_fileName, true);
                }
                if (fosLogFilePort1 != null) {
                    if (this.log1_cur_size + size >= 1024) {
                        final byte[] writeLogs = new byte[this.log1_cur_size];
                        System.arraycopy(this.port1_log_cache, 0, writeLogs, 0, this.log1_cur_size);
                        this.mLogHandler.post(new Runnable() {
                            /* class dji.midware.sdr.log.DJISdrLogDataReciever.AnonymousClass1 */

                            public void run() {
                                try {
                                    DJILogHelper.getInstance().LOGD(DJISdrLogDataReciever.TAG, "---------------->onRecvLogPort1 write size=" + writeLogs.length, false, false);
                                    DJISdrLogDataReciever.fosLogFilePort1.write(writeLogs, 0, writeLogs.length);
                                    DJISdrLogDataReciever.fosLogFilePort1.flush();
                                } catch (Exception e) {
                                }
                            }
                        });
                        this.log1_cur_size = 0;
                    }
                    System.arraycopy(dataBuffer, offset, this.port1_log_cache, this.log1_cur_size, size);
                    this.log1_cur_size += size;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onRecvLogPort2(byte[] dataBuffer, int size) {
        onAoaRecvLogPort2(dataBuffer, 0, size);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
    public void onAoaRecvLogPort2(byte[] dataBuffer, int offset, int size) {
        if (isOpenSdrLog) {
            if (size > 0) {
                isRecieveLog = true;
            }
            if (DEBUG) {
            }
            checkWriteThreadValid();
            try {
                if (fosLogFilePort2 == null && DEBUG) {
                    fosLogFilePort2 = new FileOutputStream(sdrLogDirName + sdr_port2_fileName, true);
                }
                if (fosLogFilePort2 != null) {
                    if (this.log2_cur_size + size >= 1024) {
                        final byte[] writeLogs = new byte[this.log2_cur_size];
                        System.arraycopy(this.port2_log_cache, 0, writeLogs, 0, this.log2_cur_size);
                        this.mLogHandler.post(new Runnable() {
                            /* class dji.midware.sdr.log.DJISdrLogDataReciever.AnonymousClass2 */

                            public void run() {
                                try {
                                    DJILogHelper.getInstance().LOGD(DJISdrLogDataReciever.TAG, "---------------->onRecvLogPort2 write size=" + writeLogs.length, false, false);
                                    DJISdrLogDataReciever.fosLogFilePort2.write(writeLogs, 0, writeLogs.length);
                                    DJISdrLogDataReciever.fosLogFilePort2.flush();
                                } catch (Exception e) {
                                }
                            }
                        });
                        this.log2_cur_size = 0;
                    }
                    System.arraycopy(dataBuffer, offset, this.port2_log_cache, this.log2_cur_size, size);
                    this.log2_cur_size += size;
                }
            } catch (Exception e) {
                e.printStackTrace();
                DJILogHelper.getInstance().LOGD(TAG, e.getMessage(), true, true);
            }
        }
    }

    public void onRecvLogPort3(byte[] dataBuffer, int size) {
        onAoaRecvLogPort3(dataBuffer, 0, size);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.lang.String, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException} */
    public void onAoaRecvLogPort3(byte[] dataBuffer, int offset, int size) {
        if (isOpenSdrLog) {
            if (size > 0) {
                isRecieveLog = true;
            }
            if (DEBUG) {
                DJILogHelper.getInstance().LOGD(TAG, "---------------->onRecvLogPort3 receive size=" + size, false, false);
            }
            checkWriteThreadValid();
            try {
                if (fosLogFilePort3 == null && DEBUG) {
                    fosLogFilePort3 = new FileOutputStream(sdrLogDirName + sdr_port3_fileName, true);
                }
                if (fosLogFilePort3 != null) {
                    if (this.log3_cur_size + size >= 1024) {
                        final byte[] writeLogs = new byte[this.log3_cur_size];
                        System.arraycopy(this.port3_log_cache, 0, writeLogs, 0, this.log3_cur_size);
                        this.mLogHandler.post(new Runnable() {
                            /* class dji.midware.sdr.log.DJISdrLogDataReciever.AnonymousClass3 */

                            public void run() {
                                try {
                                    DJILogHelper.getInstance().LOGD(DJISdrLogDataReciever.TAG, "---------------->onRecvLogPort3 write size=" + writeLogs.length, false, false);
                                    DJISdrLogDataReciever.fosLogFilePort3.write(writeLogs, 0, writeLogs.length);
                                    DJISdrLogDataReciever.fosLogFilePort3.flush();
                                } catch (Exception e) {
                                }
                            }
                        });
                        this.log3_cur_size = 0;
                    }
                    System.arraycopy(dataBuffer, offset, this.port3_log_cache, this.log3_cur_size, size);
                    this.log3_cur_size += size;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void checkWriteThreadValid() {
        if (this.mWriteLogThread == null || !this.mWriteLogThread.isAlive()) {
            this.mWriteLogThread = new HandlerThread("sdr_log_write_thread");
            this.mWriteLogThread.start();
        }
        if (this.mLogHandler == null || this.mLogHandler.getLooper() != this.mWriteLogThread.getLooper()) {
            this.mLogHandler = new Handler(this.mWriteLogThread.getLooper());
        }
    }

    public void onDestroy() {
        try {
            if (fosLogFilePort1 != null) {
                fosLogFilePort1.write(this.port1_log_cache, 0, this.log1_cur_size);
                this.log1_cur_size = 0;
                fosLogFilePort1.close();
                fosLogFilePort1 = null;
            }
            if (fosLogFilePort2 != null) {
                fosLogFilePort2.write(this.port2_log_cache, 0, this.log2_cur_size);
                this.log2_cur_size = 0;
                fosLogFilePort2.close();
                fosLogFilePort2 = null;
            }
            if (fosLogFilePort3 != null) {
                fosLogFilePort3.write(this.port3_log_cache, 0, this.log3_cur_size);
                this.log3_cur_size = 0;
                fosLogFilePort3.close();
                fosLogFilePort3 = null;
            }
            if (DEBUG && mContext != null && mContext.get() != null) {
                MediaScannerConnection.scanFile(mContext.get(), new String[]{sdrLogDirName + sdr_port1_fileName}, null, null);
                MediaScannerConnection.scanFile(mContext.get(), new String[]{sdrLogDirName + sdr_port2_fileName}, null, null);
                MediaScannerConnection.scanFile(mContext.get(), new String[]{sdrLogDirName + sdr_port3_fileName}, null, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scanLogFile() {
        try {
            if (fosLogFilePort1 != null) {
                fosLogFilePort1.write(this.port1_log_cache, 0, this.log1_cur_size);
                this.log1_cur_size = 0;
                fosLogFilePort1.close();
                fosLogFilePort1 = null;
            }
            if (fosLogFilePort2 != null) {
                fosLogFilePort2.write(this.port2_log_cache, 0, this.log2_cur_size);
                this.log2_cur_size = 0;
                fosLogFilePort2.close();
                fosLogFilePort2 = null;
            }
            if (fosLogFilePort3 != null) {
                fosLogFilePort3.write(this.port3_log_cache, 0, this.log3_cur_size);
                this.log3_cur_size = 0;
                fosLogFilePort3.close();
                fosLogFilePort3 = null;
            }
        } catch (Exception e) {
        }
        if (isOpenSdrLog && mContext != null && mContext.get() != null) {
            MediaScannerConnection.scanFile(mContext.get(), new String[]{sdrLogDirName + sdr_port1_fileName}, null, null);
            MediaScannerConnection.scanFile(mContext.get(), new String[]{sdrLogDirName + sdr_port2_fileName}, null, null);
            MediaScannerConnection.scanFile(mContext.get(), new String[]{sdrLogDirName + sdr_port3_fileName}, null, null);
        }
    }
}
