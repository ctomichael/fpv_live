package com.dji.frame.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.dji.frame.interfaces.V_CallBack;
import dji.publics.DJIExecutor;
import dji.thirdparty.afinal.core.AsyncTask;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

public class V_FileUtil {
    private static ExecutorService fileExecutor = DJIExecutor.getExecutorFor(DJIExecutor.Purpose.IO);
    /* access modifiers changed from: private */
    public static boolean mPauseWork = false;
    /* access modifiers changed from: private */
    public static Object mPauseWorkLock = new Object();

    private static class FileAsyncTask extends AsyncTask<Object, Void, String> {
        private final V_CallBack callBack;

        public FileAsyncTask(V_CallBack callBack2) {
            this.callBack = callBack2;
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Object... params) {
            synchronized (V_FileUtil.mPauseWorkLock) {
                while (V_FileUtil.mPauseWork && !isCancelled()) {
                    try {
                        V_FileUtil.mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            this.callBack.exec();
            return null;
        }

        /* access modifiers changed from: protected */
        public void onCancelled(String s) {
            super.onCancelled((Object) s);
            synchronized (V_FileUtil.mPauseWorkLock) {
                V_FileUtil.mPauseWorkLock.notifyAll();
            }
        }
    }

    /* access modifiers changed from: private */
    public static String getFullPath(String dir, String fileName) {
        return dir + File.separator + fileName;
    }

    public static Boolean fileExist(String path) {
        return Boolean.valueOf(new File(path).exists());
    }

    public static String fileGetContent(String dir, String fileName) {
        return fileGetContent(new File(getFullPath(dir, fileName)));
    }

    public static String fileGetContent(File f) {
        String data = "";
        if (f.exists()) {
            BufferedReader b = null;
            try {
                BufferedReader b2 = new BufferedReader(new FileReader(f));
                String content = "";
                try {
                    StringBuilder s = new StringBuilder();
                    while (content != null) {
                        content = b2.readLine();
                        if (content == null) {
                            break;
                        }
                        s.append(content.trim());
                    }
                    data = s.toString();
                    safeClose(b2);
                } catch (IOException e) {
                    e = e;
                    b = b2;
                } catch (Throwable th) {
                    th = th;
                    b = b2;
                    safeClose(b);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                try {
                    e.printStackTrace();
                    safeClose(b);
                    return data;
                } catch (Throwable th2) {
                    th = th2;
                    safeClose(b);
                    throw th;
                }
            }
        }
        return data;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:13:0x0025=Splitter:B:13:0x0025, B:9:0x001d=Splitter:B:9:0x001d} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] rawfileGetContent(java.io.File r8) {
        /*
            long r6 = r8.length()
            int r4 = (int) r6
            byte[] r2 = new byte[r4]
            r0 = 0
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ FileNotFoundException -> 0x001c, IOException -> 0x0024 }
            java.io.FileInputStream r5 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x001c, IOException -> 0x0024 }
            r5.<init>(r8)     // Catch:{ FileNotFoundException -> 0x001c, IOException -> 0x0024 }
            r1.<init>(r5)     // Catch:{ FileNotFoundException -> 0x001c, IOException -> 0x0024 }
            r5 = 0
            int r6 = r2.length     // Catch:{ FileNotFoundException -> 0x0037, IOException -> 0x0034, all -> 0x0031 }
            r1.read(r2, r5, r6)     // Catch:{ FileNotFoundException -> 0x0037, IOException -> 0x0034, all -> 0x0031 }
            safeClose(r1)
            r0 = r1
        L_0x001b:
            return r2
        L_0x001c:
            r3 = move-exception
        L_0x001d:
            r3.printStackTrace()     // Catch:{ all -> 0x002c }
            safeClose(r0)
            goto L_0x001b
        L_0x0024:
            r3 = move-exception
        L_0x0025:
            r3.printStackTrace()     // Catch:{ all -> 0x002c }
            safeClose(r0)
            goto L_0x001b
        L_0x002c:
            r5 = move-exception
        L_0x002d:
            safeClose(r0)
            throw r5
        L_0x0031:
            r5 = move-exception
            r0 = r1
            goto L_0x002d
        L_0x0034:
            r3 = move-exception
            r0 = r1
            goto L_0x0025
        L_0x0037:
            r3 = move-exception
            r0 = r1
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.frame.util.V_FileUtil.rawfileGetContent(java.io.File):byte[]");
    }

    public static String rawfileGetContent(Context context, int rawId) {
        String temp = null;
        InputStream is = null;
        byte[] buff = new byte[1024];
        ByteArrayOutputStream baos = null;
        try {
            is = context.getResources().openRawResource(rawId);
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            while (true) {
                try {
                    int rd = is.read(buff);
                    if (rd == -1) {
                        break;
                    }
                    baos2.write(buff, 0, rd);
                } catch (Exception e) {
                    e = e;
                    baos = baos2;
                    try {
                        e.printStackTrace();
                        safeClose(is);
                        safeClose(baos);
                        return temp;
                    } catch (Throwable th) {
                        th = th;
                        safeClose(is);
                        safeClose(baos);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    baos = baos2;
                    safeClose(is);
                    safeClose(baos);
                    throw th;
                }
            }
            String temp2 = new String(baos2.toByteArray(), "UTF-8");
            try {
                temp = temp2.replace("\r\n", "\n");
                safeClose(is);
                safeClose(baos2);
            } catch (Exception e2) {
                e = e2;
                baos = baos2;
                temp = temp2;
                e.printStackTrace();
                safeClose(is);
                safeClose(baos);
                return temp;
            } catch (Throwable th3) {
                th = th3;
                baos = baos2;
                safeClose(is);
                safeClose(baos);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            safeClose(is);
            safeClose(baos);
            return temp;
        }
        return temp;
    }

    private static void fileEdit(String dir, String fileName, String data, Boolean append) {
        FileWriter fw = null;
        try {
            FileWriter fw2 = new FileWriter(getFullPath(dir, fileName), append.booleanValue());
            try {
                fw2.write(data, 0, data.length());
                fw2.flush();
                safeClose(fw2);
            } catch (IOException e) {
                e = e;
                fw = fw2;
                try {
                    e.printStackTrace();
                    safeClose(fw);
                } catch (Throwable th) {
                    th = th;
                    safeClose(fw);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fw = fw2;
                safeClose(fw);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            safeClose(fw);
        }
    }

    private static void fileEdit(File file, String data, Boolean append) {
        FileWriter fw = null;
        try {
            FileWriter fw2 = new FileWriter(file, append.booleanValue());
            try {
                fw2.write(data, 0, data.length());
                fw2.flush();
                safeClose(fw2);
            } catch (IOException e) {
                e = e;
                fw = fw2;
                try {
                    e.printStackTrace();
                    safeClose(fw);
                } catch (Throwable th) {
                    th = th;
                    safeClose(fw);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fw = fw2;
                safeClose(fw);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            safeClose(fw);
        }
    }

    public static void fileWrite(String dir, String fileName, String data) {
        fileEdit(dir, fileName, data, false);
    }

    public static void fileWrite(File file, String data) {
        fileEdit(file, data, false);
    }

    public static void fileWrite(String path, byte[] buffer, int offset, int length) {
        FileOutputStream outputStream = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream2 = new FileOutputStream(file);
            try {
                outputStream2.write(buffer, offset, length);
                outputStream2.flush();
                safeClose(outputStream2);
            } catch (IOException e) {
                e = e;
                outputStream = outputStream2;
                try {
                    e.printStackTrace();
                    safeClose(outputStream);
                } catch (Throwable th) {
                    th = th;
                    safeClose(outputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                outputStream = outputStream2;
                safeClose(outputStream);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            safeClose(outputStream);
        }
    }

    public static void fileAppend(String dir, String fileName, String data) {
        fileEdit(dir, fileName, data, true);
    }

    public static void fileAppend(File file, String data) {
        fileEdit(file, data, true);
    }

    public static void saveMyBitmap(final Bitmap mBitmap, final String dir, final String fileName) {
        if (mBitmap != null && dir != null && fileName != null) {
            new FileAsyncTask(new V_CallBack() {
                /* class com.dji.frame.util.V_FileUtil.AnonymousClass1 */

                public void exec() {
                    FileOutputStream fOut = null;
                    try {
                        File fileDir = new File(dir);
                        if (!fileDir.exists()) {
                            fileDir.mkdir();
                        }
                        File f = new File(V_FileUtil.getFullPath(dir, fileName));
                        f.createNewFile();
                        FileOutputStream fOut2 = new FileOutputStream(f);
                        try {
                            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut2);
                            fOut2.flush();
                            V_FileUtil.safeClose(fOut2);
                        } catch (IOException e) {
                            e = e;
                            fOut = fOut2;
                            try {
                                e.printStackTrace();
                                V_FileUtil.safeClose(fOut);
                            } catch (Throwable th) {
                                th = th;
                                V_FileUtil.safeClose(fOut);
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            fOut = fOut2;
                            V_FileUtil.safeClose(fOut);
                            throw th;
                        }
                    } catch (IOException e2) {
                        e = e2;
                        e.printStackTrace();
                        V_FileUtil.safeClose(fOut);
                    }
                }
            }).executeOnExecutor(fileExecutor, "ok");
        }
    }

    public static void saveMyBitmapSync(Bitmap mBitmap, String dir, String fileName) throws IOException {
        if (mBitmap != null && dir != null && fileName != null) {
            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            File f = new File(getFullPath(dir, fileName));
            f.createNewFile();
            FileOutputStream fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        }
    }

    public static long getFileSize(File f) throws FileNotFoundException {
        long length;
        long size = 0;
        File[] flist = f.listFiles();
        int count = flist.length;
        for (int i = 0; i < count; i++) {
            if (flist[i].isDirectory()) {
                length = getFileSize(flist[i]);
            } else {
                length = flist[i].length();
            }
            size += length;
        }
        return size;
    }

    public static ArrayList<File> getAllFile(File f) throws FileNotFoundException {
        ArrayList<File> list = new ArrayList<>();
        File[] flist = f.listFiles();
        if (flist != null) {
            int count = flist.length;
            for (int i = 0; i < count; i++) {
                if (flist[i].isDirectory()) {
                    list.addAll(getAllFile(flist[i]));
                } else {
                    list.add(flist[i]);
                }
            }
        }
        return list;
    }

    public static void deleteAllFile(File f) throws FileNotFoundException {
        File[] flist;
        if (f.exists() && (flist = f.listFiles()) != null && flist.length != 0) {
            int count = flist.length;
            for (int i = 0; i < count; i++) {
                if (flist[i].isDirectory()) {
                    deleteAllFile(flist[i]);
                } else {
                    flist[i].delete();
                }
            }
        }
    }

    public static void clear(File f, int maxValue) throws FileNotFoundException {
        getFileSize(f);
        if (getFileSize(f) > ((long) (maxValue * 1024 * 1024))) {
            deleteAllFile(f);
        }
    }

    public static String getNameFromPath(String path) {
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0) {
            return null;
        }
        return path.substring(lastDot + 1);
    }

    public static byte[] toByteArray(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
        BufferedInputStream in2 = null;
        try {
            BufferedInputStream in3 = new BufferedInputStream(new FileInputStream(f));
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = in3.read(buffer, 0, 1024);
                    if (-1 != len) {
                        bos.write(buffer, 0, len);
                    } else {
                        byte[] byteArray = bos.toByteArray();
                        safeClose(in3);
                        safeClose(bos);
                        return byteArray;
                    }
                }
            } catch (IOException e) {
                e = e;
                in2 = in3;
                try {
                    e.printStackTrace();
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    safeClose(in2);
                    safeClose(bos);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                in2 = in3;
                safeClose(in2);
                safeClose(bos);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] toByteArray2(String filename) throws IOException {
        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }
        FileInputStream fs = null;
        try {
            FileInputStream fs2 = new FileInputStream(f);
            try {
                FileChannel channel = fs2.getChannel();
                ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
                do {
                } while (channel.read(byteBuffer) > 0);
                byte[] array = byteBuffer.array();
                safeClose(channel);
                safeClose(fs2);
                return array;
            } catch (IOException e) {
                e = e;
                fs = fs2;
                try {
                    e.printStackTrace();
                    throw e;
                } catch (Throwable th) {
                    th = th;
                    safeClose(null);
                    safeClose(fs);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fs = fs2;
                safeClose(null);
                safeClose(fs);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            e.printStackTrace();
            throw e;
        }
    }

    public static byte[] toByteArray3(String filename) throws IOException {
        try {
            FileChannel fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            System.out.println(byteBuffer.isLoaded());
            byte[] result = new byte[((int) fc.size())];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }
            safeClose(fc);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (Throwable th) {
            safeClose(null);
            throw th;
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        FileOutputStream fs;
        InputStream inStream = null;
        FileOutputStream fs2 = null;
        try {
            if (new File(oldPath).exists()) {
                InputStream inStream2 = new FileInputStream(oldPath);
                try {
                    fs = new FileOutputStream(newPath);
                } catch (Exception e) {
                    e = e;
                    inStream = inStream2;
                    try {
                        e.printStackTrace();
                        safeClose(inStream);
                        safeClose(fs2);
                    } catch (Throwable th) {
                        th = th;
                        safeClose(inStream);
                        safeClose(fs2);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    inStream = inStream2;
                    safeClose(inStream);
                    safeClose(fs2);
                    throw th;
                }
                try {
                    byte[] buffer = new byte[1444];
                    while (true) {
                        int byteread = inStream2.read(buffer);
                        if (byteread == -1) {
                            break;
                        }
                        fs.write(buffer, 0, byteread);
                    }
                    fs2 = fs;
                    inStream = inStream2;
                } catch (Exception e2) {
                    e = e2;
                    fs2 = fs;
                    inStream = inStream2;
                    e.printStackTrace();
                    safeClose(inStream);
                    safeClose(fs2);
                } catch (Throwable th3) {
                    th = th3;
                    fs2 = fs;
                    inStream = inStream2;
                    safeClose(inStream);
                    safeClose(fs2);
                    throw th;
                }
            }
            safeClose(inStream);
            safeClose(fs2);
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            safeClose(inStream);
            safeClose(fs2);
        }
    }

    public static boolean deleteFile(String filePath, String fileName) {
        if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName)) {
            return false;
        }
        return deleteFile(new File(filePath, fileName));
    }

    public static boolean deleteFile(String path) {
        if (!TextUtils.isEmpty(path)) {
            return deleteFile(new File(path));
        }
        return false;
    }

    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File tmp : files) {
                deleteFile(tmp);
            }
        }
        return file.delete();
    }

    public static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }
}
