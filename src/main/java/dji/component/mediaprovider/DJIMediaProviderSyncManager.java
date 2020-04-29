package dji.component.mediaprovider;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.webkit.MimeTypeMap;
import dji.component.mediaprovider.DJIMediaStore;
import dji.component.playback.model.PlaybackFileInfo;
import dji.midware.data.manager.P3.DJIProductManager;
import java.io.File;
import java.util.ArrayList;

public class DJIMediaProviderSyncManager {
    private static final String KEY_FILE_GUID = "key_file_guid";
    private static final String KEY_FILE_PATH = "key_file_path";
    private static final int MSG_ADD_CACHE_FILE = 10;
    private static final int MSG_ADD_HD_FILE = 11;
    private static final String TAG = "DJIMediaProviderSyncManager";
    private static volatile DJIMediaProviderSyncManager instance;
    private Handler.Callback mCallback = new DJIMediaProviderSyncManager$$Lambda$0(this);
    private Context mContext;
    private String mDJIMainPath;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private boolean mHasStated = false;

    public static synchronized DJIMediaProviderSyncManager getInstance() {
        DJIMediaProviderSyncManager dJIMediaProviderSyncManager;
        synchronized (DJIMediaProviderSyncManager.class) {
            if (instance == null) {
                synchronized (DJIMediaProviderSyncManager.class) {
                    instance = new DJIMediaProviderSyncManager();
                }
            }
            dJIMediaProviderSyncManager = instance;
        }
        return dJIMediaProviderSyncManager;
    }

    public void init(Context context) {
        this.mContext = context;
        this.mDJIMainPath = Environment.getExternalStorageDirectory() + "/DJI/" + context.getPackageName();
    }

    public void stop() {
    }

    public void addNewSyncFile(PlaybackFileInfo fileInfo, String filePath, long fileGuid) {
        checkAndStart();
        Message msg = this.mHandler.obtainMessage(11);
        Bundle data = new Bundle();
        data.putString(KEY_FILE_PATH, filePath);
        data.putLong(KEY_FILE_GUID, fileGuid);
        msg.obj = fileInfo;
        msg.setData(data);
        this.mHandler.sendMessage(msg);
        MediaScannerConnection.scanFile(this.mContext, new String[]{filePath}, null, DJIMediaProviderSyncManager$$Lambda$1.$instance);
        this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse(filePath)));
    }

    public void addNewSyncFile(PlaybackFileInfo fileInfo, String filePath) {
        checkAndStart();
        fileInfo.pathStr = filePath;
        MediaScannerConnection.scanFile(this.mContext, new String[]{filePath}, null, DJIMediaProviderSyncManager$$Lambda$2.$instance);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(10, fileInfo));
    }

    private void addHDFile(PlaybackFileInfo fileInfo, String path, long fileGuid) {
        DJIMediaProviderUtils.saveLog(TAG, "addHDFile() file:" + path);
        Cursor cursor = null;
        if (fileGuid == 0) {
            try {
                fileGuid = fileInfo.fileGuid;
            } catch (Exception e) {
                DJIMediaProviderUtils.saveLog(TAG, "addHDFile() error:" + e);
                DJIMediaProviderUtils.closeQuietly(cursor);
                return;
            } catch (Throwable th) {
                DJIMediaProviderUtils.closeQuietly(cursor);
                throw th;
            }
        }
        if (fileGuid == 0) {
            DJIMediaProviderUtils.saveLog(TAG, "addHDFile() no file guid");
            DJIMediaProviderUtils.closeQuietly(null);
            return;
        }
        cursor = this.mContext.getContentResolver().query(DJIMediaStore.Files.getContentUri(), new String[]{"_id", DJIMediaStore.FileColumns.FILE_PATH}, "file_guid=?", new String[]{String.valueOf(fileGuid)}, null);
        if (cursor == null || !cursor.moveToNext() || cursor.getCount() != 1) {
            addCacheFile(fileInfo, path, true);
        } else {
            ArrayList<ContentProviderOperation> insertProviderOperations = new ArrayList<>();
            File cacheFile = new File(cursor.getString(1));
            if (cacheFile.exists() && !cacheFile.isDirectory()) {
                cacheFile.delete();
            }
            File file = new File(path);
            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(DJIMediaStore.Files.getContentUri());
            builder.withSelection("_id=?", new String[]{String.valueOf(cursor.getLong(0))});
            builder.withValue(DJIMediaStore.FileColumns.FILE_PATH, "");
            builder.withValue(DJIMediaStore.FileColumns.FILE_PATH_HD, path);
            bindFileInfo(builder, fileInfo, file);
            insertProviderOperations.add(builder.build());
            this.mContext.getContentResolver().applyBatch(DJIMediaStore.AUTHORITY, insertProviderOperations);
        }
        DJIMediaProviderUtils.closeQuietly(cursor);
    }

    private void addCacheFile(PlaybackFileInfo fileInfo, String filePath, boolean isHDFile) {
        DJIMediaProviderUtils.saveLog(TAG, "addCacheFile() file:" + filePath + " isHDFile:" + isHDFile);
        try {
            ArrayList<ContentProviderOperation> insertProviderOperations = new ArrayList<>();
            File file = new File(filePath);
            ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(DJIMediaStore.Files.getContentUri());
            bindFileInfo(builder, fileInfo, file);
            if (isHDFile) {
                builder.withValue(DJIMediaStore.FileColumns.FILE_PATH_HD, filePath);
            } else {
                builder.withValue(DJIMediaStore.FileColumns.FILE_PATH, filePath);
            }
            insertProviderOperations.add(builder.build());
            this.mContext.getContentResolver().applyBatch(DJIMediaStore.AUTHORITY, insertProviderOperations);
        } catch (Exception e) {
            DJIMediaProviderUtils.saveLog(TAG, "addCacheFile() error:" + e);
        }
    }

    public ArrayList<String> getCacheFilePaths(ArrayList<Long> guidList) {
        ArrayList<String> pathList = null;
        if (guidList != null && guidList.size() > 0) {
            pathList = new ArrayList<>();
            Cursor cursor = null;
            try {
                cursor = this.mContext.getContentResolver().query(DJIMediaStore.Files.getContentUri(), new String[]{"_id", DJIMediaStore.FileColumns.FILE_PATH, DJIMediaStore.FileColumns.FILE_GUID}, "file_path IS NOT NULL AND file_guid > 0", null, null);
                LongSparseArray<String> guidPathMap = new LongSparseArray<>();
                while (cursor != null && cursor.moveToNext()) {
                    String path = cursor.getString(1);
                    long j = cursor.getLong(2);
                    if (TextUtils.isEmpty(path)) {
                        path = "";
                    }
                    guidPathMap.put(j, path);
                }
                for (int i = 0; i < guidList.size(); i++) {
                    pathList.add(guidPathMap.get(guidList.get(i).longValue(), ""));
                }
            } catch (Exception e) {
                DJIMediaProviderUtils.saveLog(TAG, "syncGetCacheFilePaths() error:" + e);
            } finally {
                DJIMediaProviderUtils.closeQuietly(cursor);
            }
        }
        return pathList;
    }

    private void bindFileInfo(@NonNull ContentProviderOperation.Builder builder, @NonNull PlaybackFileInfo fileInfo, File file) {
        int value;
        int value2;
        int i;
        int value3;
        int i2;
        int value4;
        int value5;
        int value6;
        int value7;
        int i3;
        int value8;
        int value9;
        int width;
        int height;
        int i4 = 1;
        int i5 = -1;
        int i6 = 0;
        ContentProviderOperation.Builder withValue = builder.withValue(DJIMediaStore.FileColumns.LENGTH, Long.valueOf(fileInfo.length)).withValue(DJIMediaStore.FileColumns.CREATE_TIME, Long.valueOf(fileInfo.createTime)).withValue(DJIMediaStore.FileColumns.CREATE_TIME_ORG, Long.valueOf(fileInfo.createTimeOrg)).withValue(DJIMediaStore.FileColumns._INDEX, Integer.valueOf(fileInfo.index)).withValue(DJIMediaStore.FileColumns.SUB_INDEX, Integer.valueOf(fileInfo.subIndex)).withValue("duration", Integer.valueOf(fileInfo.duration)).withValue(DJIMediaStore.FileColumns.ROTATION, Integer.valueOf(fileInfo.rotation)).withValue("frame_rate", Integer.valueOf(fileInfo.frameRate));
        if (fileInfo.resolution == null) {
            value = 0;
        } else {
            value = fileInfo.resolution.value();
        }
        ContentProviderOperation.Builder withValue2 = withValue.withValue("resolution", Integer.valueOf(value));
        if (fileInfo.fileType == null) {
            value2 = 0;
        } else {
            value2 = fileInfo.fileType.value();
        }
        ContentProviderOperation.Builder withValue3 = withValue2.withValue(DJIMediaStore.FileColumns.FILE_TYPE, Integer.valueOf(value2)).withValue(DJIMediaStore.FileColumns.PATH_LENGTH, Integer.valueOf(fileInfo.pathLength));
        if (fileInfo.hasEXT) {
            i = 1;
        } else {
            i = 0;
        }
        ContentProviderOperation.Builder withValue4 = withValue3.withValue(DJIMediaStore.FileColumns.HAS_EXT, Integer.valueOf(i)).withValue(DJIMediaStore.FileColumns.FILE_GUID, Long.valueOf(fileInfo.fileGuid));
        if (fileInfo.captureType == null) {
            value3 = -1;
        } else {
            value3 = fileInfo.captureType.value();
        }
        ContentProviderOperation.Builder withValue5 = withValue4.withValue(DJIMediaStore.FileColumns.CAPTURE_TYPE, Integer.valueOf(value3)).withValue(DJIMediaStore.FileColumns.PHOTO_GROUP_ID, Integer.valueOf(fileInfo.photoGroupId));
        if (fileInfo.starTag) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        ContentProviderOperation.Builder withValue6 = withValue5.withValue(DJIMediaStore.FileColumns.STAR_TAG, Integer.valueOf(i2));
        if (fileInfo.groupType == null) {
            value4 = -1;
        } else {
            value4 = fileInfo.groupType.value();
        }
        ContentProviderOperation.Builder withValue7 = withValue6.withValue(DJIMediaStore.FileColumns.GROUP_TYPE, Integer.valueOf(value4)).withValue(DJIMediaStore.FileColumns.GROUP_NUM, Integer.valueOf(fileInfo.groupNum)).withValue(DJIMediaStore.FileColumns.GROUP_RESULT, Integer.valueOf(fileInfo.groupResult));
        if (fileInfo.videoType == null) {
            value5 = -1;
        } else {
            value5 = fileInfo.videoType.value();
        }
        ContentProviderOperation.Builder withValue8 = withValue7.withValue(DJIMediaStore.FileColumns.VIDEO_TYPE, Integer.valueOf(value5));
        if (fileInfo.subVideoType == null) {
            value6 = -1;
        } else {
            value6 = fileInfo.subVideoType.value();
        }
        ContentProviderOperation.Builder withValue9 = withValue8.withValue(DJIMediaStore.FileColumns.SUB_VIDEO_TYPE, Integer.valueOf(value6));
        if (fileInfo.encodeType == null) {
            value7 = -1;
        } else {
            value7 = fileInfo.encodeType.value();
        }
        ContentProviderOperation.Builder withValue10 = withValue9.withValue(DJIMediaStore.FileColumns.ENCODE_TYPE, Integer.valueOf(value7)).withValue(DJIMediaStore.FileColumns.FRAME_RATE_SCALE, Integer.valueOf(fileInfo.frameRateScale));
        if (fileInfo.isSync) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        ContentProviderOperation.Builder withValue11 = withValue10.withValue(DJIMediaStore.FileColumns.IS_SYNC, Integer.valueOf(i3));
        if (!fileInfo.hasOrigPhoto) {
            i4 = 0;
        }
        ContentProviderOperation.Builder withValue12 = withValue11.withValue(DJIMediaStore.FileColumns.HAS_ORI_PHOTO, Integer.valueOf(i4)).withValue(DJIMediaStore.FileColumns.FILE_NAME, fileInfo.fileName);
        if (fileInfo.audioType == null) {
            value8 = 0;
        } else {
            value8 = fileInfo.audioType.value();
        }
        ContentProviderOperation.Builder withValue13 = withValue12.withValue(DJIMediaStore.FileColumns.AUDIO_TYPE, Integer.valueOf(value8));
        if (fileInfo.samplingBit == null) {
            value9 = 0;
        } else {
            value9 = fileInfo.samplingBit.value();
        }
        ContentProviderOperation.Builder withValue14 = withValue13.withValue(DJIMediaStore.FileColumns.SAMPLING_BIT, Integer.valueOf(value9));
        if (fileInfo.samplingFrequency != null) {
            i5 = fileInfo.samplingFrequency.value();
        }
        ContentProviderOperation.Builder withValue15 = withValue14.withValue(DJIMediaStore.FileColumns.SAMPLING_FREQUENCY, Integer.valueOf(i5)).withValue(DJIMediaStore.FileColumns.DATA_SOURCE, Integer.valueOf(fileInfo.dataSource)).withValue(DJIMediaStore.FileColumns.LAST_MODIFY, Long.valueOf(file.lastModified() / 1000));
        if (fileInfo.resolution == null) {
            width = 0;
        } else {
            width = fileInfo.resolution.getWidth();
        }
        ContentProviderOperation.Builder withValue16 = withValue15.withValue("width", Integer.valueOf(width));
        if (fileInfo.resolution == null) {
            height = 0;
        } else {
            height = fileInfo.resolution.getHeight();
        }
        ContentProviderOperation.Builder withValue17 = withValue16.withValue("height", Integer.valueOf(height)).withValue(DJIMediaStore.FileColumns.MEDIA_TYPE, 0);
        if (fileInfo.productType != null) {
            i6 = fileInfo.productType.value();
        }
        withValue17.withValue(DJIMediaStore.FileColumns.FILE_PRODUCT_TYPE, Integer.valueOf(i6));
    }

    private void checkAndStart() {
        if (!this.mHasStated) {
            this.mHandlerThread = new HandlerThread(TAG);
            this.mHandlerThread.start();
            this.mHandler = new Handler(this.mHandlerThread.getLooper(), this.mCallback);
            this.mHasStated = true;
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$new$2$DJIMediaProviderSyncManager(Message msg) {
        PlaybackFileInfo fileInfo = (PlaybackFileInfo) msg.obj;
        if (this.mContext != null) {
            switch (msg.what) {
                case 10:
                    fileInfo.productType = DJIProductManager.getInstance().getType();
                    addCacheFile(fileInfo, fileInfo.pathStr, false);
                    break;
                case 11:
                    String path = msg.getData().getString(KEY_FILE_PATH);
                    long fileGuid = msg.getData().getLong(KEY_FILE_GUID);
                    fileInfo.productType = DJIProductManager.getInstance().getType();
                    fileInfo.duration *= 1000;
                    addHDFile(fileInfo, path, fileGuid);
                    break;
            }
        } else {
            DJIMediaProviderUtils.saveLog(TAG, "HandleMessage() mContext null");
        }
        return false;
    }

    public String getDJIMainPath() {
        return this.mDJIMainPath;
    }

    private static String getMimeType(String filePath) {
        String extension = "";
        int idx = filePath.lastIndexOf(".");
        if (idx > 0) {
            extension = filePath.substring(idx + 1);
        }
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
