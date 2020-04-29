package dji.midware.sdr.log;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import com.amap.location.common.model.AmapLoc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DJIWifiRcLogDataReceiver {
    private static final String SDR_FILE_PREFIX = "SdrLog-";
    private static SimpleDateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US);
    private static DJIWifiRcLogDataReceiver instance;
    private FileOutputStream mFosPort1;
    private FileOutputStream mFosPort2;
    private FileOutputStream mFosPort3;
    private WifiRcLogSaveEngine mLogSaveEngine;
    private String mSdrPort1FileName;
    private String mSdrPort2FileName;
    private String mSdrPort3FileName;

    public static synchronized DJIWifiRcLogDataReceiver getInstance() {
        DJIWifiRcLogDataReceiver dJIWifiRcLogDataReceiver;
        synchronized (DJIWifiRcLogDataReceiver.class) {
            if (instance == null) {
                instance = new DJIWifiRcLogDataReceiver();
            }
            dJIWifiRcLogDataReceiver = instance;
        }
        return dJIWifiRcLogDataReceiver;
    }

    DJIWifiRcLogDataReceiver() {
    }

    public void init(Context appContext, boolean needEncrypt) {
        String logDirName = Environment.getExternalStorageDirectory() + "/DJI/" + appContext.getPackageName() + "/LOG/RC_LOG/";
        File dirFile = new File(logDirName);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        String suffix = ".txt";
        if (needEncrypt) {
            suffix = ".log";
        }
        String dateStr = fileNameFormat.format(new Date());
        this.mSdrPort1FileName = logDirName + SDR_FILE_PREFIX + dateStr + AmapLoc.RESULT_TYPE_AMAP_INDOOR + suffix;
        this.mSdrPort2FileName = logDirName + SDR_FILE_PREFIX + dateStr + AmapLoc.RESULT_TYPE_GOOGLE + suffix;
        this.mSdrPort3FileName = logDirName + SDR_FILE_PREFIX + dateStr + AmapLoc.RESULT_TYPE_CAS_INDOOR + suffix;
        this.mLogSaveEngine = new WifiRcLogSaveEngine(needEncrypt);
    }

    public void onPortLogRawDataReceive(int portIndex, byte[] dataBuffer, int offset, int size) {
        if (this.mLogSaveEngine != null && dataBuffer != null && offset + size <= dataBuffer.length) {
            byte[] writeLogs = new byte[size];
            System.arraycopy(dataBuffer, offset, writeLogs, 0, size);
            this.mLogSaveEngine.onDataReceive(getPortFos(portIndex), writeLogs);
        }
    }

    public void onDestroy() {
        if (this.mLogSaveEngine != null) {
            this.mLogSaveEngine.onDestroy();
        }
        closeFos(this.mFosPort1);
        closeFos(this.mFosPort2);
        closeFos(this.mFosPort3);
    }

    private FileOutputStream getPortFos(int portIndex) {
        if (portIndex == 1) {
            if (this.mFosPort1 == null) {
                this.mFosPort1 = createFos(this.mSdrPort1FileName);
            }
            return this.mFosPort1;
        } else if (portIndex == 2) {
            if (this.mFosPort2 == null) {
                this.mFosPort2 = createFos(this.mSdrPort2FileName);
            }
            return this.mFosPort2;
        } else {
            if (this.mFosPort3 == null) {
                this.mFosPort3 = createFos(this.mSdrPort3FileName);
            }
            return this.mFosPort3;
        }
    }

    @Nullable
    private static FileOutputStream createFos(String saveFilePath) {
        FileOutputStream fos = null;
        if (saveFilePath == null) {
            return null;
        }
        try {
            fos = new FileOutputStream(saveFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fos;
    }

    private static void closeFos(FileOutputStream fos) {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
