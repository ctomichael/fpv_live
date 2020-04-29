package com.mapbox.mapboxsdk.http;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.log.Logger;
import java.io.IOException;
import java.io.InputStream;

class LocalRequestTask extends AsyncTask<String, Void, byte[]> {
    private static final String TAG = "Mbgl-LocalRequestTask";
    private OnLocalRequestResponse requestResponse;

    public interface OnLocalRequestResponse {
        void onResponse(byte[] bArr);
    }

    LocalRequestTask(OnLocalRequestResponse requestResponse2) {
        this.requestResponse = requestResponse2;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public byte[] doInBackground(String... strings) {
        return loadFile(Mapbox.getApplicationContext().getAssets(), "integration/" + strings[0].substring(8).replaceAll("%20", " ").replaceAll("%2c", ","));
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(@Nullable byte[] bytes) {
        super.onPostExecute((Object) bytes);
        if (bytes != null && this.requestResponse != null) {
            this.requestResponse.onResponse(bytes);
        }
    }

    @Nullable
    private static byte[] loadFile(AssetManager assets, @NonNull String path) {
        byte[] buffer = null;
        InputStream input = null;
        try {
            input = assets.open(path);
            buffer = new byte[input.available()];
            input.read(buffer);
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception) {
                    logFileError(exception);
                }
            }
        } catch (IOException exception2) {
            logFileError(exception2);
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception3) {
                    logFileError(exception3);
                }
            }
        } catch (Throwable th) {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException exception4) {
                    logFileError(exception4);
                }
            }
            throw th;
        }
        return buffer;
    }

    private static void logFileError(Exception exception) {
        Logger.e(TAG, "Load file failed", exception);
        MapStrictMode.strictModeViolation("Load file failed", exception);
    }
}
