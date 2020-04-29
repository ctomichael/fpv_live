package com.mapbox.android.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Comparator;

public final class FileUtils {
    private static final int DEFAULT_BUFFER_SIZE_IN_BYTES = 4096;
    private static final String LOG_TAG = "FileUtils";

    private FileUtils() {
    }

    @NonNull
    public static File getFile(@NonNull Context context, @NonNull String fileName) {
        return new File(context.getFilesDir(), fileName);
    }

    @NonNull
    public static String readFromFile(@NonNull File file) throws FileNotFoundException {
        Reader inputStreamReader = new InputStreamReader(new FileInputStream(file));
        StringWriter output = new StringWriter();
        try {
            char[] buffer = new char[4096];
            while (true) {
                int count = inputStreamReader.read(buffer);
                if (count != -1) {
                    output.write(buffer, 0, count);
                } else {
                    try {
                        break;
                    } catch (IOException ioe) {
                        Log.e(LOG_TAG, ioe.toString());
                    }
                }
            }
            inputStreamReader.close();
        } catch (IOException ioe2) {
            Log.w(LOG_TAG, ioe2.toString());
            try {
                inputStreamReader.close();
            } catch (IOException ioe3) {
                Log.e(LOG_TAG, ioe3.toString());
            }
        } catch (Throwable th) {
            try {
                inputStreamReader.close();
            } catch (IOException ioe4) {
                Log.e(LOG_TAG, ioe4.toString());
            }
            throw th;
        }
        return output.toString();
    }

    public static void writeToFile(@NonNull File file, @NonNull String content) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        try {
            writer.write(content);
            writer.flush();
            try {
                writer.close();
            } catch (IOException ioe) {
                Log.e(LOG_TAG, ioe.toString());
            }
        } catch (IOException ioe2) {
            Log.e(LOG_TAG, ioe2.toString());
            try {
                writer.close();
            } catch (IOException ioe3) {
                Log.e(LOG_TAG, ioe3.toString());
            }
        } catch (Throwable th) {
            try {
                writer.close();
            } catch (IOException ioe4) {
                Log.e(LOG_TAG, ioe4.toString());
            }
            throw th;
        }
    }

    public static void deleteFile(@NonNull File file) {
        if (!file.delete()) {
            Log.w(LOG_TAG, "Could not delete file: " + file);
        }
    }

    @NonNull
    public static File[] listAllFiles(File directory) {
        if (directory == null) {
            return new File[0];
        }
        File[] files = directory.listFiles();
        return files == null ? new File[0] : files;
    }

    public static void deleteFirst(@NonNull File[] files, @NonNull Comparator<File> sortedBy, int numFiles) {
        Arrays.sort(files, sortedBy);
        int size = Math.min(files.length, numFiles);
        for (int i = 0; i < size; i++) {
            if (!files[i].delete()) {
                Log.w(LOG_TAG, "Failed to delete file: " + files[i]);
            }
        }
    }

    public static final class LastModifiedComparator implements Comparator<File> {
        public int compare(File o1, File o2) {
            long o1LastModified = o1.lastModified();
            long o2LastModified = o2.lastModified();
            if (o1LastModified < o2LastModified) {
                return -1;
            }
            return o1LastModified == o2LastModified ? 0 : 1;
        }
    }
}
