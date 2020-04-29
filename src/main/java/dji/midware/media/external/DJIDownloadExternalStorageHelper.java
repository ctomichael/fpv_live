package dji.midware.media.external;

import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import dji.log.DJILog;
import dji.midware.media.external.DJIExternalStorageController;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import dji.midware.util.DjiSharedPreferencesManager;
import java.io.File;
import java.io.IOException;
import org.greenrobot.eventbus.EventBus;

public class DJIDownloadExternalStorageHelper extends DJIBaseExternalStorageHelper {
    private static final String KEY_DOWNLOAD_SRC_FILE_PATH = "external_sd_download_source_file_path";
    private static final String KEY_EXTERNAL_STORAGE_ENABLE = "download_external_storage_enable";
    private static final String TAG = "DJIDownloadExternalStorageHelper";

    public DJIDownloadExternalStorageHelper(String filePath, DocumentFile documentFile) {
        super(filePath, documentFile);
    }

    public void start() {
        setSrcFilePathToSp(this.filePath);
        super.start();
    }

    public void stop(boolean needDeleteSrcFile) {
        setSrcFilePathToSp("");
        if (needDeleteSrcFile) {
            super.stop(true);
        } else if (this.externalFileDf != null && this.copyController != null) {
            this.copyController.stopAndDelete(this.externalFileDf);
        }
    }

    private static void setSrcFilePathToSp(String path) {
        logd("setSrcFilePathToSp: path: " + path);
        DjiSharedPreferencesManager.putString(DJIExternalStorageController.getContext(), KEY_DOWNLOAD_SRC_FILE_PATH, path);
    }

    private static String getSrcFilePathFromSp() {
        logd("getSrcFilePathFromSp: ");
        return DjiSharedPreferencesManager.getString(DJIExternalStorageController.getContext(), KEY_DOWNLOAD_SRC_FILE_PATH, "");
    }

    public static void checkUnfinishedFile() {
        logd("checkUnfinishedFile: 0");
        String filePath = getSrcFilePathFromSp();
        logd("checkUnfinishedFile: filepath: " + filePath);
        if (DJIExternalStorageController.getInstance().isExteranSDGranted() && !TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                logd("checkUnfinishedFile: path exist");
                file.delete();
            }
            String infoPath = filePath.substring(0, filePath.lastIndexOf(".")) + ".info";
            logd("checkUnfinishedFile: info path: " + infoPath);
            File infoFile = new File(infoPath);
            if (infoFile.exists() && infoFile.isFile()) {
                logd("checkUnfinishedFile: info path exist");
                DocumentFile infoDf = DJIExternalStorageController.checkAndCreateFile(getExternalSdDownloadDirDf(), "", infoFile.getName());
                if (infoDf != null) {
                    logd("checkUnfinishedFile: info df uri: " + infoDf.getUri());
                    try {
                        DJIExternalStorageController.copyToExternalFile(infoFile, infoDf, true);
                    } catch (IOException e) {
                        DJILog.e(TAG, "checkUnfinishedFile: ", e, new Object[0]);
                    }
                }
            }
            String orgPath = filePath.substring(0, filePath.lastIndexOf(".")) + ".org";
            logd("checkUnfinishedFile: org path: " + orgPath);
            File orgFile = new File(orgPath);
            if (orgFile.exists() && orgFile.isFile()) {
                logd("checkUnfinishedFile: org path exist");
                DocumentFile orgDf = DJIExternalStorageController.checkAndCreateFile(getExternalSdDownloadDirDf(), "", orgFile.getName());
                if (orgDf != null) {
                    logd("checkUnfinishedFile: org df uri: " + orgDf.getUri());
                    try {
                        DJIExternalStorageController.copyToExternalFile(orgFile, orgDf, true);
                    } catch (IOException e2) {
                        DJILog.e(TAG, "checkUnfinishedFile: ", e2, new Object[0]);
                    }
                }
            }
            setSrcFilePathToSp("");
        }
    }

    public static DocumentFile getExternalSdDownloadDirDf() {
        DocumentFile rootDirDf;
        Uri rootUri = DJIExternalStorageController.getInstance().getGlobalTreeUriSAF();
        if (rootUri == null || (rootDirDf = DocumentFile.fromTreeUri(DJIExternalStorageController.getContext(), rootUri)) == null) {
            return null;
        }
        return DJIExternalStorageController.checkAndCreateDir(DJIExternalStorageController.checkAndCreateDir(DJIExternalStorageController.checkAndCreateDir(rootDirDf, DJIUsbAccessoryReceiver.myFacturer), DJIExternalStorageController.getContext().getPackageName()), "DJI FLY");
    }

    public static void setDownloadExternalStorageEnable(boolean enable) {
        DjiSharedPreferencesManager.putBoolean(DJIExternalStorageController.getContext(), KEY_EXTERNAL_STORAGE_ENABLE, enable);
        EventBus.getDefault().post(new DJIExternalStorageController.ExternalStorageEvent(enable ? 4 : 5));
    }

    public static boolean getDownloadExternalStorageEnable() {
        return DjiSharedPreferencesManager.getBoolean(DJIExternalStorageController.getContext(), KEY_EXTERNAL_STORAGE_ENABLE, false);
    }

    public static String getExternalDownloadPath() {
        if (!DJIExternalStorageController.getInstance().isExteranSDGranted()) {
            return "";
        }
        return DJIExternalStorageController.getInstance().getSecondaryStorage() + "/DJI/" + DJIExternalStorageController.getContext().getPackageName() + "/DJI FLY/";
    }

    private static void logd(String log) {
    }
}
