package com.dji.permission.checker;

import android.os.Environment;
import java.io.File;

class StorageReadTest implements PermissionTest {
    StorageReadTest() {
    }

    public boolean test() throws Throwable {
        File directory = Environment.getExternalStorageDirectory();
        if (!directory.exists() || !directory.canRead()) {
            return false;
        }
        long modified = directory.lastModified();
        String[] pathList = directory.list();
        if (modified <= 0 || pathList == null) {
            return false;
        }
        return true;
    }
}
