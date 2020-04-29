package dji.component.mediaprovider;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import dji.component.accountcenter.IMemberProtocol;
import dji.component.mediaprovider.DJIMediaStore;
import dji.component.playback.model.PlaybackFileInfo;
import dji.log.DJILog;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DJIMediaProviderUtils {
    public static final String COUNT = "files_count";
    private static final String TAG = "DJIMediaProviderUtils";

    public static String getDJIPath() {
        return DJIMediaProviderSyncManager.getInstance().getDJIMainPath();
    }

    public static Cursor getDJIAllFileInfo(ContentResolver resolver) {
        return resolver.query(DJIMediaStore.Files.getContentUri(), null, null, null, "last_modify DESC");
    }

    public static Cursor getSystemAllFolderPath(ContentResolver resolver) {
        return resolver.query(MediaStore.Files.getContentUri("external"), new String[]{"distinct parent", "_data", "COUNT(_data) as files_count"}, "media_type= ? OR media_type = ?) group by (parent", new String[]{String.valueOf(3), String.valueOf(1)}, "files_count DESC");
    }

    public static Cursor getFolderFiles(ContentResolver resolver, int folderID) {
        return resolver.query(MediaStore.Files.getContentUri("external"), null, "parent = ? AND (media_type= ? OR media_type = ? )", new String[]{String.valueOf(folderID), String.valueOf(3), String.valueOf(1)}, "date_modified DESC");
    }

    public static boolean updateDJIFileFavorStatus(int id, ContentResolver resolver, int starTag) {
        if (id == 0) {
            return false;
        }
        ArrayList<ContentProviderOperation> updateOperations = new ArrayList<>();
        updateOperations.add(ContentProviderOperation.newUpdate(DJIMediaStore.Files.getContentUri()).withSelection("_id=?", new String[]{String.valueOf(id)}).withValue(DJIMediaStore.FileColumns.STAR_TAG, Integer.valueOf(starTag)).build());
        try {
            DJILog.d(TAG, "updateDJIFileFavorStatus() applyBatch result:" + resolver.applyBatch(DJIMediaStore.AUTHORITY, updateOperations)[0].count, new Object[0]);
            return true;
        } catch (Exception e) {
            DJILog.e(TAG, "updateDJIFileFavorStatus() applyBatch error:" + e, new Object[0]);
            return false;
        }
    }

    public static boolean deleteFile(int id, ContentResolver resolver, boolean isDJIUri) {
        List<Integer> list = new ArrayList<>();
        list.add(Integer.valueOf(id));
        return deleteFile(list, resolver, isDJIUri);
    }

    public static boolean deleteFile(List<Integer> ids, ContentResolver resolver, boolean isDJIUri) {
        Uri uri;
        String selction;
        String authority;
        if (ids == null || ids.size() == 0) {
            return false;
        }
        ArrayList<ContentProviderOperation> deleteProviderOperations = new ArrayList<>();
        if (isDJIUri) {
            uri = DJIMediaStore.Files.getContentUri();
            selction = "_id=?";
            authority = DJIMediaStore.AUTHORITY;
        } else {
            uri = MediaStore.Files.getContentUri("external");
            selction = "_id=?";
            authority = "media";
        }
        Iterator<Integer> it2 = ids.iterator();
        while (it2.hasNext()) {
            deleteProviderOperations.add(ContentProviderOperation.newDelete(uri).withSelection(selction, new String[]{String.valueOf(it2.next())}).build());
        }
        try {
            saveLog(TAG, "deleteFile() applyBatch result:" + resolver.applyBatch(authority, deleteProviderOperations).length);
            return true;
        } catch (Exception e) {
            saveLog(TAG, "deleteFile() mScanDeleteList applyBatch error:" + e);
            return false;
        }
    }

    public static void addHDMediaFiles(PlaybackFileInfo fileInfo, @NonNull String filePath) {
        saveLog(TAG, "addHDMediaFiles() filePath:" + filePath);
        PlaybackFileInfo newInfo = fileInfo.clone();
        long fileGuid = newInfo.fileGuid;
        if (fileGuid == 0) {
            fileGuid = generateFileKeyId(newInfo);
        }
        if (filePath.contains("//")) {
            filePath = filePath.replace("//", IMemberProtocol.PARAM_SEPERATOR);
        }
        DJIMediaProviderSyncManager.getInstance().addNewSyncFile(newInfo, filePath, fileGuid);
    }

    private static long generateFileKeyId(@NonNull PlaybackFileInfo fileInfo) {
        return (long) (fileInfo.fileType.name() + fileInfo.createTime + fileInfo.length + fileInfo.index + fileInfo.subIndex).hashCode();
    }

    public static void addVideoCacheMediaFiles(PlaybackFileInfo fileInfo, String filePath) {
        saveLog(TAG, "addVideoCacheMediaFiles() filePath:" + filePath);
        DJIMediaProviderSyncManager.getInstance().addNewSyncFile(fileInfo.clone(), filePath);
    }

    public static Uri getMediaUriFormFilePath(Context context, String path) {
        Uri mediaUri = MediaStore.Files.getContentUri("external");
        Cursor cursor = null;
        Uri uri = null;
        try {
            cursor = context.getContentResolver().query(mediaUri, new String[]{"_id"}, "_data=? ", new String[]{path}, null);
            if (cursor != null && cursor.moveToFirst()) {
                uri = ContentUris.withAppendedId(mediaUri, cursor.getLong(cursor.getColumnIndex("_id")));
            }
        } catch (Exception e) {
            saveLog(TAG, "getMediaUriFormFilePath() error:" + e);
        } finally {
            closeQuietly(cursor);
        }
        return uri;
    }

    public static ArrayList<String> syncGetCacheFilePaths(ArrayList<Long> guidList) {
        return DJIMediaProviderSyncManager.getInstance().getCacheFilePaths(guidList);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static final void saveLog(String TAG2, String msg) {
        DJILog.logWriteD(TAG2, msg, "DJIMediaProvider", new Object[0]);
    }

    public static final String getDJIVideoPath() {
        return getDJIPath() + DJIMediaProviderConstant.VIDEO_ORG;
    }

    public static final String getDJIPhotoPath() {
        return getDJIPath() + DJIMediaProviderConstant.PHOTO_ORG;
    }

    public static final String getDJIIntelligentPath() {
        return getDJIPath() + DJIMediaProviderConstant.INTELLIGENT_ORG;
    }

    public static final String getDJIEditorCachePath() {
        return getDJIPath() + DJIMediaProviderConstant.EDITOR_CACHE;
    }

    public static final String getDJILtmPath() {
        return getDJIPath() + DJIMediaProviderConstant.LTM_CACHE;
    }

    public static final String getDJIGalleryCachePath() {
        return getDJIPath() + DJIMediaProviderConstant.PHOTO_THUMB_SCREEN;
    }

    public static final String getDJI4kTranscodePath() {
        return getDJIPath() + DJIMediaProviderConstant.TRANSCODE_4K;
    }
}
