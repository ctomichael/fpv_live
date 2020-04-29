package com.amap.location.common.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String BASE_PATH = "amaplocation";
    private static volatile String sAppSDCardDirPath;

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.amap.location.common.util.FileUtil.writeToFile(java.lang.String, java.io.File, boolean):boolean
     arg types: [java.lang.String, java.io.File, int]
     candidates:
      com.amap.location.common.util.FileUtil.writeToFile(byte[], java.io.File, boolean):boolean
      com.amap.location.common.util.FileUtil.writeToFile(java.lang.String, java.io.File, boolean):boolean */
    @Deprecated
    public static boolean appendToFile(String str, File file) {
        return writeToFile(str, file, true);
    }

    public static boolean deleteFileOrDir(File file) {
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(file);
        while (arrayList2.size() > 0) {
            File file2 = (File) arrayList2.remove(0);
            arrayList.add(file2);
            File[] listFiles = file2.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file3 : listFiles) {
                    if (file3.isDirectory()) {
                        arrayList2.add(file3);
                    } else if (file3.exists() && !file3.delete()) {
                        return false;
                    }
                }
                continue;
            }
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            File file4 = (File) arrayList.get(size);
            if (file4.exists() && !file4.delete()) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    private static boolean deleteFileOrDirDep(File file) {
        if (file == null || !file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                if (!deleteFileOrDir(file2)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    public static String getAppSDCardFileDir(Context context) {
        if (sAppSDCardDirPath != null) {
            return sAppSDCardDirPath;
        }
        if (isExternalStorageWritable()) {
            try {
                File file = new File(context.getExternalFilesDir(null), BASE_PATH);
                if (file.exists() || file.mkdirs()) {
                    sAppSDCardDirPath = file.getAbsolutePath();
                }
            } catch (Exception e) {
                return null;
            }
        }
        return sAppSDCardDirPath;
    }

    private static long getAvailspceInner(File file) {
        try {
            StatFs statFs = new StatFs(file.getAbsolutePath());
            if (Build.VERSION.SDK_INT >= 18) {
                return statFs.getAvailableBlocksLong() * statFs.getBlockSizeLong();
            }
            return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long getSdcardAvailableSpace(Context context) {
        if (!isExternalStorageWritable()) {
            return 0;
        }
        return getAvailspceInner(context.getExternalFilesDir(null));
    }

    public static long getSystemAvailableSpace(Context context) {
        return getAvailspceInner(context.getFilesDir());
    }

    public static boolean isExternalStorageWritable() {
        return "mounted".equals(Environment.getExternalStorageState());
    }

    public static byte[] readBytes(File file) {
        FileInputStream fileInputStream;
        byte[] bArr = null;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                bArr = e.a((InputStream) fileInputStream);
                e.a((Closeable) fileInputStream);
            } catch (IOException e) {
                e = e;
                try {
                    e.printStackTrace();
                    e.a((Closeable) fileInputStream);
                    return bArr;
                } catch (Throwable th) {
                    th = th;
                    e.a((Closeable) fileInputStream);
                    throw th;
                }
            }
        } catch (IOException e2) {
            e = e2;
            fileInputStream = null;
            e.printStackTrace();
            e.a((Closeable) fileInputStream);
            return bArr;
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            e.a((Closeable) fileInputStream);
            throw th;
        }
        return bArr;
    }

    public static List<String> readLines(File file) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2;
        Throwable th;
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader3 = null;
        if (file != null) {
            try {
                if (file.exists()) {
                    bufferedReader = new BufferedReader(new FileReader(file));
                    while (true) {
                        try {
                            String readLine = bufferedReader.readLine();
                            if (readLine == null) {
                                break;
                            }
                            arrayList.add(readLine);
                        } catch (IOException e) {
                            bufferedReader3 = bufferedReader;
                            e.a(bufferedReader3);
                            return arrayList;
                        } catch (Throwable th2) {
                            th = th2;
                            bufferedReader2 = bufferedReader;
                            e.a(bufferedReader2);
                            throw th;
                        }
                    }
                    e.a(bufferedReader);
                    return arrayList;
                }
            } catch (IOException e2) {
                e.a(bufferedReader3);
                return arrayList;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader2 = null;
                e.a(bufferedReader2);
                throw th;
            }
        }
        bufferedReader = null;
        e.a(bufferedReader);
        return arrayList;
    }

    public static String readString(File file) {
        FileInputStream fileInputStream;
        String str = null;
        try {
            fileInputStream = new FileInputStream(file);
            try {
                str = e.b(fileInputStream);
                e.a((Closeable) fileInputStream);
            } catch (IOException e) {
                e = e;
                try {
                    e.printStackTrace();
                    e.a((Closeable) fileInputStream);
                    return str;
                } catch (Throwable th) {
                    th = th;
                    e.a((Closeable) fileInputStream);
                    throw th;
                }
            }
        } catch (IOException e2) {
            e = e2;
            fileInputStream = null;
            e.printStackTrace();
            e.a((Closeable) fileInputStream);
            return str;
        } catch (Throwable th2) {
            th = th2;
            fileInputStream = null;
            e.a((Closeable) fileInputStream);
            throw th;
        }
        return str;
    }

    public static boolean setAppSDCardFileDir(File file) {
        if (!file.exists() && !file.mkdirs()) {
            return false;
        }
        sAppSDCardDirPath = file.getAbsolutePath();
        return true;
    }

    public static boolean writeToFile(String str, File file, boolean z) {
        FileWriter fileWriter;
        if (file == null) {
            return false;
        }
        FileWriter fileWriter2 = null;
        try {
            fileWriter = new FileWriter(file.getAbsolutePath(), z);
            try {
                fileWriter.write(str);
                e.a(fileWriter);
                return true;
            } catch (Exception e) {
                e = e;
                try {
                    e.printStackTrace();
                    e.a(fileWriter);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileWriter2 = fileWriter;
                    e.a(fileWriter2);
                    throw th;
                }
            }
        } catch (Exception e2) {
            e = e2;
            fileWriter = null;
            e.printStackTrace();
            e.a(fileWriter);
            return false;
        } catch (Throwable th2) {
            th = th2;
            e.a(fileWriter2);
            throw th;
        }
    }

    public static boolean writeToFile(byte[] bArr, File file, boolean z) {
        FileOutputStream fileOutputStream;
        if (file == null) {
            return false;
        }
        FileOutputStream fileOutputStream2 = null;
        try {
            fileOutputStream = new FileOutputStream(file.getAbsolutePath(), z);
            try {
                fileOutputStream.write(bArr);
                e.a(fileOutputStream);
                return true;
            } catch (Exception e) {
                e = e;
                try {
                    e.printStackTrace();
                    e.a(fileOutputStream);
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileOutputStream2 = fileOutputStream;
                    e.a(fileOutputStream2);
                    throw th;
                }
            }
        } catch (Exception e2) {
            e = e2;
            fileOutputStream = null;
            e.printStackTrace();
            e.a(fileOutputStream);
            return false;
        } catch (Throwable th2) {
            th = th2;
            e.a(fileOutputStream2);
            throw th;
        }
    }
}
