package com.mapbox.mapboxsdk.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.log.Logger;
import java.io.File;

public class FileUtils {
    private static final String TAG = "Mbgl-FileUtils";

    public interface OnCheckFileReadPermissionListener {
        void onError();

        void onReadPermissionGranted();
    }

    public interface OnCheckFileWritePermissionListener {
        void onError();

        void onWritePermissionGranted();
    }

    public static class CheckFileReadPermissionTask extends AsyncTask<File, Void, Boolean> {
        @NonNull
        private final OnCheckFileReadPermissionListener listener;

        public CheckFileReadPermissionTask(@NonNull OnCheckFileReadPermissionListener listener2) {
            this.listener = listener2;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(File... files) {
            try {
                return Boolean.valueOf(files[0].canRead());
            } catch (Exception e) {
                return false;
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            this.listener.onError();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                this.listener.onReadPermissionGranted();
            } else {
                this.listener.onError();
            }
        }
    }

    public static class CheckFileWritePermissionTask extends AsyncTask<File, Void, Boolean> {
        @NonNull
        private final OnCheckFileWritePermissionListener listener;

        public CheckFileWritePermissionTask(@NonNull OnCheckFileWritePermissionListener listener2) {
            this.listener = listener2;
        }

        /* access modifiers changed from: protected */
        public Boolean doInBackground(File... files) {
            try {
                return Boolean.valueOf(files[0].canWrite());
            } catch (Exception e) {
                return false;
            }
        }

        /* access modifiers changed from: protected */
        public void onCancelled() {
            this.listener.onError();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Boolean result) {
            if (result.booleanValue()) {
                this.listener.onWritePermissionGranted();
            } else {
                this.listener.onError();
            }
        }
    }

    public static void deleteFile(@NonNull final String path) {
        new Thread(new Runnable() {
            /* class com.mapbox.mapboxsdk.utils.FileUtils.AnonymousClass1 */

            public void run() {
                try {
                    File file = new File(path);
                    if (!file.exists()) {
                        return;
                    }
                    if (file.delete()) {
                        Logger.d(FileUtils.TAG, "File deleted to save space: " + path);
                    } else {
                        Logger.e(FileUtils.TAG, "Failed to delete file: " + path);
                    }
                } catch (Exception exception) {
                    Logger.e(FileUtils.TAG, "Failed to delete file: ", exception);
                }
            }
        }).start();
    }
}
