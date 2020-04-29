package com.dji.permission.checker;

import android.os.Environment;
import java.io.File;

class StorageWriteTest implements PermissionTest {
    StorageWriteTest() {
    }

    public boolean test() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (!directory.exists() || !directory.canWrite()) {
            return false;
        }
        File file = new File(directory, "ANDROID.PERMISSION.TEST");
        if (file.exists()) {
            return file.delete();
        }
        return file.createNewFile();
    }
}
