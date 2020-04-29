package dji.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileUtils {
    private static double SIZE_GB = 1.073741824E9d;
    private static double SIZE_KB = 1024.0d;
    private static double SIZE_MB = 1048756.0d;

    public static boolean isFileExists(String filePath) {
        return isFileExists(getFileByPath(filePath));
    }

    public static boolean isFileExists(File file) {
        if (file == null) {
            return false;
        }
        return file.exists();
    }

    public static File getFileByPath(String filePath) {
        if (hasSpace(filePath)) {
            return null;
        }
        return new File(filePath);
    }

    private static boolean hasSpace(String filePath) {
        if (filePath == null) {
            return true;
        }
        int len = filePath.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isWhitespace(filePath.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDirectory(File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    public static boolean isDirectory(String path) {
        return isDirectory(getFileByPath(path));
    }

    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || (file.isFile() && file.delete()));
    }

    public static boolean deleteFile(String filePath) {
        return deleteFile(getFileByPath(filePath));
    }

    public static void deleteFileOnExist(String filePath) {
        if (filePath != null) {
            File f = new File(filePath);
            if (f.exists()) {
                f.deleteOnExit();
            }
        }
    }

    public static void deleteFileOnExist(File file) {
        if (file != null && file.exists()) {
            file.deleteOnExit();
        }
    }

    public static boolean deleteDir(String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    public static boolean deleteDir(File dir) {
        if (!dir.exists()) {
            return true;
        }
        if (dir == null || !dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (!(files == null || files.length == 0)) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory() && !deleteDir(file)) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean rename(String filePath, String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    public static boolean rename(File file, String newName) {
        if (file == null || !file.exists() || hasSpace(newName)) {
            return false;
        }
        if (newName.equals(file.getName())) {
            return true;
        }
        File newFile = new File(file.getParent() + File.separator + newName);
        if (newFile.exists() || !file.renameTo(newFile)) {
            return false;
        }
        return true;
    }

    public static int getSubFileCount(String path, FilenameFilter filter) {
        File[] subs;
        if (path == null) {
            return 0;
        }
        File file = new File(path);
        if (!file.exists() || !file.isDirectory() || (subs = file.listFiles(filter)) == null) {
            return 0;
        }
        return subs.length;
    }

    public static List<String> searchNameInSubFile(String path, FilenameFilter filter, String fileName) {
        List<String> files = new ArrayList<>();
        if (path != null) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                String[] subs = file.list(filter);
                int len = subs != null ? subs.length : 0;
                for (int i = 0; i < len; i++) {
                    if (subs[i].contains(fileName)) {
                        files.add(path + subs[i]);
                    }
                }
            }
        }
        return files;
    }

    public static boolean createFile(String path) {
        if (path != null) {
            return createFile(new File(path));
        }
        return false;
    }

    public static boolean createFile(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists() && file.isFile()) {
            return true;
        }
        if (!mkdirs(file.getParentFile())) {
            return false;
        }
        try {
            file.createNewFile();
            if (!file.exists() || !file.isFile()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            if (!file.exists() || !file.isFile()) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            if (!file.exists() || !file.isFile()) {
            }
            throw th;
        }
    }

    public static boolean mkdirs(String path) {
        if (path != null) {
            return mkdirs(new File(path));
        }
        return false;
    }

    public static boolean mkdirs(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        try {
            file.mkdirs();
            if (!(file.exists() && file.isDirectory())) {
                try {
                    file.mkdirs();
                } catch (Exception e) {
                }
            }
            if (!file.exists() || !file.isDirectory()) {
                return false;
            }
            return true;
        } catch (Exception e2) {
            if (!(file.exists() && file.isDirectory())) {
                try {
                    file.mkdirs();
                } catch (Exception e3) {
                }
            }
            if (!file.exists() || !file.isDirectory()) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            if (!(file.exists() && file.isDirectory())) {
                try {
                    file.mkdirs();
                } catch (Exception e4) {
                }
            }
            if (!file.exists() || !file.isDirectory()) {
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x002e A[SYNTHETIC, Splitter:B:17:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0033 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0044 A[SYNTHETIC, Splitter:B:29:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readFile(java.lang.String r8) {
        /*
            r4 = 0
            if (r8 == 0) goto L_0x0031
            java.io.File r2 = new java.io.File
            r2.<init>(r8)
            boolean r6 = isFile(r2)
            if (r6 == 0) goto L_0x0031
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0058, all -> 0x0041 }
            java.io.FileReader r6 = new java.io.FileReader     // Catch:{ Exception -> 0x0058, all -> 0x0041 }
            r6.<init>(r2)     // Catch:{ Exception -> 0x0058, all -> 0x0041 }
            r1.<init>(r6)     // Catch:{ Exception -> 0x0058, all -> 0x0041 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x005a, all -> 0x0051 }
            r5.<init>()     // Catch:{ Exception -> 0x005a, all -> 0x0051 }
            r3 = 0
        L_0x001f:
            java.lang.String r3 = r1.readLine()     // Catch:{ Exception -> 0x0029, all -> 0x0054 }
            if (r3 == 0) goto L_0x0037
            r5.append(r3)     // Catch:{ Exception -> 0x0029, all -> 0x0054 }
            goto L_0x001f
        L_0x0029:
            r6 = move-exception
            r0 = r1
            r4 = r5
        L_0x002c:
            if (r0 == 0) goto L_0x0031
            r0.close()     // Catch:{ Exception -> 0x004d }
        L_0x0031:
            if (r4 != 0) goto L_0x0048
            java.lang.String r6 = ""
        L_0x0036:
            return r6
        L_0x0037:
            if (r1 == 0) goto L_0x005d
            r1.close()     // Catch:{ Exception -> 0x003e }
            r4 = r5
            goto L_0x0031
        L_0x003e:
            r6 = move-exception
            r4 = r5
            goto L_0x0031
        L_0x0041:
            r6 = move-exception
        L_0x0042:
            if (r0 == 0) goto L_0x0047
            r0.close()     // Catch:{ Exception -> 0x004f }
        L_0x0047:
            throw r6
        L_0x0048:
            java.lang.String r6 = r4.toString()
            goto L_0x0036
        L_0x004d:
            r6 = move-exception
            goto L_0x0031
        L_0x004f:
            r7 = move-exception
            goto L_0x0047
        L_0x0051:
            r6 = move-exception
            r0 = r1
            goto L_0x0042
        L_0x0054:
            r6 = move-exception
            r0 = r1
            r4 = r5
            goto L_0x0042
        L_0x0058:
            r6 = move-exception
            goto L_0x002c
        L_0x005a:
            r6 = move-exception
            r0 = r1
            goto L_0x002c
        L_0x005d:
            r4 = r5
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.readFile(java.lang.String):java.lang.String");
    }

    public static boolean writeStringToFile(File target, String content, String encoding) throws IOException {
        if (content == null) {
            return false;
        }
        return writeBytesToFile(target, content.getBytes(encoding));
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0036 A[SYNTHETIC, Splitter:B:18:0x0036] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x003b A[SYNTHETIC, Splitter:B:21:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0040 A[SYNTHETIC, Splitter:B:24:0x0040] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeBytesToFile(java.io.File r10, byte[] r11) throws java.io.IOException {
        /*
            r7 = 0
            if (r11 == 0) goto L_0x0009
            boolean r8 = createFile(r10)
            if (r8 != 0) goto L_0x000a
        L_0x0009:
            return r7
        L_0x000a:
            r7 = 0
            r0 = 0
            r4 = 0
            r6 = 0
            r8 = 4096(0x1000, float:5.74E-42)
            byte[] r2 = new byte[r8]
            r3 = -1
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch:{ all -> 0x0064 }
            r1.<init>(r11)     // Catch:{ all -> 0x0064 }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ all -> 0x0066 }
            r5.<init>(r10)     // Catch:{ all -> 0x0066 }
            java.nio.channels.FileChannel r8 = r5.getChannel()     // Catch:{ all -> 0x0031 }
            java.nio.channels.FileLock r6 = r8.lock()     // Catch:{ all -> 0x0031 }
        L_0x0025:
            int r3 = r1.read(r2)     // Catch:{ all -> 0x0031 }
            r8 = -1
            if (r3 == r8) goto L_0x0044
            r8 = 0
            r5.write(r2, r8, r3)     // Catch:{ all -> 0x0031 }
            goto L_0x0025
        L_0x0031:
            r8 = move-exception
            r4 = r5
            r0 = r1
        L_0x0034:
            if (r6 == 0) goto L_0x0039
            r6.release()     // Catch:{ IOException -> 0x005e }
        L_0x0039:
            if (r4 == 0) goto L_0x003e
            r4.close()     // Catch:{ IOException -> 0x0060 }
        L_0x003e:
            if (r0 == 0) goto L_0x0043
            r0.close()     // Catch:{ IOException -> 0x0062 }
        L_0x0043:
            throw r8
        L_0x0044:
            r5.flush()     // Catch:{ all -> 0x0031 }
            r7 = 1
            if (r6 == 0) goto L_0x004d
            r6.release()     // Catch:{ IOException -> 0x005a }
        L_0x004d:
            if (r5 == 0) goto L_0x0052
            r5.close()     // Catch:{ IOException -> 0x005c }
        L_0x0052:
            if (r1 == 0) goto L_0x0009
            r1.close()     // Catch:{ IOException -> 0x0058 }
            goto L_0x0009
        L_0x0058:
            r8 = move-exception
            goto L_0x0009
        L_0x005a:
            r8 = move-exception
            goto L_0x004d
        L_0x005c:
            r8 = move-exception
            goto L_0x0052
        L_0x005e:
            r9 = move-exception
            goto L_0x0039
        L_0x0060:
            r9 = move-exception
            goto L_0x003e
        L_0x0062:
            r9 = move-exception
            goto L_0x0043
        L_0x0064:
            r8 = move-exception
            goto L_0x0034
        L_0x0066:
            r8 = move-exception
            r0 = r1
            goto L_0x0034
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.writeBytesToFile(java.io.File, byte[]):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x001f A[SYNTHETIC, Splitter:B:15:0x001f] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x002b A[SYNTHETIC, Splitter:B:21:0x002b] */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeBytesToFile(java.io.File r4, byte[] r5, boolean r6) {
        /*
            r1 = 0
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ IOException -> 0x0019 }
            r2.<init>(r4, r6)     // Catch:{ IOException -> 0x0019 }
            r2.write(r5)     // Catch:{ IOException -> 0x0037, all -> 0x0034 }
            r2.flush()     // Catch:{ IOException -> 0x0037, all -> 0x0034 }
            if (r2 == 0) goto L_0x003a
            r2.close()     // Catch:{ IOException -> 0x0013 }
            r1 = r2
        L_0x0012:
            return
        L_0x0013:
            r0 = move-exception
            r0.printStackTrace()
            r1 = r2
            goto L_0x0012
        L_0x0019:
            r0 = move-exception
        L_0x001a:
            r0.printStackTrace()     // Catch:{ all -> 0x0028 }
            if (r1 == 0) goto L_0x0012
            r1.close()     // Catch:{ IOException -> 0x0023 }
            goto L_0x0012
        L_0x0023:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0012
        L_0x0028:
            r3 = move-exception
        L_0x0029:
            if (r1 == 0) goto L_0x002e
            r1.close()     // Catch:{ IOException -> 0x002f }
        L_0x002e:
            throw r3
        L_0x002f:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x002e
        L_0x0034:
            r3 = move-exception
            r1 = r2
            goto L_0x0029
        L_0x0037:
            r0 = move-exception
            r1 = r2
            goto L_0x001a
        L_0x003a:
            r1 = r2
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.writeBytesToFile(java.io.File, byte[], boolean):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x002e A[SYNTHETIC, Splitter:B:19:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0037 A[SYNTHETIC, Splitter:B:24:0x0037] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeToFile(java.lang.String r5, java.lang.String r6, boolean r7) {
        /*
            r2 = 0
            if (r5 == 0) goto L_0x002a
            boolean r3 = createFile(r5)
            if (r3 == 0) goto L_0x002a
            if (r6 == 0) goto L_0x002a
            int r3 = r6.length()
            if (r3 <= 0) goto L_0x002a
            r0 = 0
            java.io.BufferedWriter r1 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x002b, all -> 0x0034 }
            java.io.FileWriter r3 = new java.io.FileWriter     // Catch:{ Exception -> 0x002b, all -> 0x0034 }
            r3.<init>(r5, r7)     // Catch:{ Exception -> 0x002b, all -> 0x0034 }
            r4 = 1024(0x400, float:1.435E-42)
            r1.<init>(r3, r4)     // Catch:{ Exception -> 0x002b, all -> 0x0034 }
            r1.write(r6)     // Catch:{ Exception -> 0x0042, all -> 0x003f }
            r1.flush()     // Catch:{ Exception -> 0x0042, all -> 0x003f }
            r2 = 1
            if (r1 == 0) goto L_0x002a
            r1.close()     // Catch:{ Exception -> 0x003b }
        L_0x002a:
            return r2
        L_0x002b:
            r3 = move-exception
        L_0x002c:
            if (r0 == 0) goto L_0x002a
            r0.close()     // Catch:{ Exception -> 0x0032 }
            goto L_0x002a
        L_0x0032:
            r3 = move-exception
            goto L_0x002a
        L_0x0034:
            r3 = move-exception
        L_0x0035:
            if (r0 == 0) goto L_0x003a
            r0.close()     // Catch:{ Exception -> 0x003d }
        L_0x003a:
            throw r3
        L_0x003b:
            r3 = move-exception
            goto L_0x002a
        L_0x003d:
            r4 = move-exception
            goto L_0x003a
        L_0x003f:
            r3 = move-exception
            r0 = r1
            goto L_0x0035
        L_0x0042:
            r3 = move-exception
            r0 = r1
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.writeToFile(java.lang.String, java.lang.String, boolean):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0047 A[SYNTHETIC, Splitter:B:22:0x0047] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004c A[SYNTHETIC, Splitter:B:25:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0055 A[SYNTHETIC, Splitter:B:30:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x005a A[SYNTHETIC, Splitter:B:33:0x005a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean writeFileByMapping(java.lang.String r10, java.lang.String r11, java.lang.String r12) {
        /*
            r9 = 0
            if (r10 == 0) goto L_0x0043
            boolean r1 = isFile(r10)
            if (r1 == 0) goto L_0x0043
            if (r11 == 0) goto L_0x0043
            int r1 = r11.length()
            if (r1 <= 0) goto L_0x0043
            r6 = 0
            r0 = 0
            java.io.RandomAccessFile r7 = new java.io.RandomAccessFile     // Catch:{ Exception -> 0x0044, all -> 0x0052 }
            java.lang.String r1 = "rw"
            r7.<init>(r10, r1)     // Catch:{ Exception -> 0x0044, all -> 0x0052 }
            java.nio.channels.FileChannel r0 = r7.getChannel()     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            java.nio.channels.FileChannel$MapMode r1 = java.nio.channels.FileChannel.MapMode.READ_WRITE     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            r2 = 0
            int r4 = r11.length()     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            int r4 = r4 + 4
            long r4 = (long) r4     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            java.nio.MappedByteBuffer r8 = r0.map(r1, r2, r4)     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            byte[] r1 = r11.getBytes(r12)     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            r8.put(r1)     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            r8.force()     // Catch:{ Exception -> 0x006b, all -> 0x0068 }
            r9 = 1
            if (r0 == 0) goto L_0x003e
            r0.close()     // Catch:{ Exception -> 0x005e }
        L_0x003e:
            if (r7 == 0) goto L_0x0043
            r7.close()     // Catch:{ Exception -> 0x0060 }
        L_0x0043:
            return r9
        L_0x0044:
            r1 = move-exception
        L_0x0045:
            if (r0 == 0) goto L_0x004a
            r0.close()     // Catch:{ Exception -> 0x0062 }
        L_0x004a:
            if (r6 == 0) goto L_0x0043
            r6.close()     // Catch:{ Exception -> 0x0050 }
            goto L_0x0043
        L_0x0050:
            r1 = move-exception
            goto L_0x0043
        L_0x0052:
            r1 = move-exception
        L_0x0053:
            if (r0 == 0) goto L_0x0058
            r0.close()     // Catch:{ Exception -> 0x0064 }
        L_0x0058:
            if (r6 == 0) goto L_0x005d
            r6.close()     // Catch:{ Exception -> 0x0066 }
        L_0x005d:
            throw r1
        L_0x005e:
            r1 = move-exception
            goto L_0x003e
        L_0x0060:
            r1 = move-exception
            goto L_0x0043
        L_0x0062:
            r1 = move-exception
            goto L_0x004a
        L_0x0064:
            r2 = move-exception
            goto L_0x0058
        L_0x0066:
            r2 = move-exception
            goto L_0x005d
        L_0x0068:
            r1 = move-exception
            r6 = r7
            goto L_0x0053
        L_0x006b:
            r1 = move-exception
            r6 = r7
            goto L_0x0045
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.writeFileByMapping(java.lang.String, java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x002e A[SYNTHETIC, Splitter:B:17:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0033 A[SYNTHETIC, Splitter:B:20:0x0033] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0038 A[SYNTHETIC, Splitter:B:23:0x0038] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0055 A[SYNTHETIC, Splitter:B:41:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x005a A[SYNTHETIC, Splitter:B:44:0x005a] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x005f A[SYNTHETIC, Splitter:B:47:0x005f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyStreamByStream(java.io.InputStream r9, java.lang.String r10) {
        /*
            r6 = 0
            if (r9 == 0) goto L_0x003b
            if (r10 == 0) goto L_0x003b
            r0 = 0
            r3 = 0
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x007a, all -> 0x0052 }
            r1.<init>(r9)     // Catch:{ Exception -> 0x007a, all -> 0x0052 }
            java.io.BufferedOutputStream r4 = new java.io.BufferedOutputStream     // Catch:{ Exception -> 0x007c, all -> 0x0073 }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x007c, all -> 0x0073 }
            r7.<init>(r10)     // Catch:{ Exception -> 0x007c, all -> 0x0073 }
            r4.<init>(r7)     // Catch:{ Exception -> 0x007c, all -> 0x0073 }
            r7 = 2048(0x800, float:2.87E-42)
            byte[] r2 = new byte[r7]     // Catch:{ Exception -> 0x0029, all -> 0x0076 }
            r5 = 0
        L_0x001b:
            r7 = 0
            r8 = 2048(0x800, float:2.87E-42)
            int r5 = r1.read(r2, r7, r8)     // Catch:{ Exception -> 0x0029, all -> 0x0076 }
            if (r5 <= 0) goto L_0x003c
            r7 = 0
            r4.write(r2, r7, r5)     // Catch:{ Exception -> 0x0029, all -> 0x0076 }
            goto L_0x001b
        L_0x0029:
            r7 = move-exception
            r3 = r4
            r0 = r1
        L_0x002c:
            if (r0 == 0) goto L_0x0031
            r0.close()     // Catch:{ Exception -> 0x0067 }
        L_0x0031:
            if (r9 == 0) goto L_0x0036
            r9.close()     // Catch:{ Exception -> 0x0069 }
        L_0x0036:
            if (r3 == 0) goto L_0x003b
            r3.close()     // Catch:{ Exception -> 0x006b }
        L_0x003b:
            return r6
        L_0x003c:
            r4.flush()     // Catch:{ Exception -> 0x0029, all -> 0x0076 }
            r6 = 1
            if (r1 == 0) goto L_0x0045
            r1.close()     // Catch:{ Exception -> 0x0063 }
        L_0x0045:
            if (r9 == 0) goto L_0x004a
            r9.close()     // Catch:{ Exception -> 0x0065 }
        L_0x004a:
            if (r4 == 0) goto L_0x003b
            r4.close()     // Catch:{ Exception -> 0x0050 }
            goto L_0x003b
        L_0x0050:
            r7 = move-exception
            goto L_0x003b
        L_0x0052:
            r7 = move-exception
        L_0x0053:
            if (r0 == 0) goto L_0x0058
            r0.close()     // Catch:{ Exception -> 0x006d }
        L_0x0058:
            if (r9 == 0) goto L_0x005d
            r9.close()     // Catch:{ Exception -> 0x006f }
        L_0x005d:
            if (r3 == 0) goto L_0x0062
            r3.close()     // Catch:{ Exception -> 0x0071 }
        L_0x0062:
            throw r7
        L_0x0063:
            r7 = move-exception
            goto L_0x0045
        L_0x0065:
            r7 = move-exception
            goto L_0x004a
        L_0x0067:
            r7 = move-exception
            goto L_0x0031
        L_0x0069:
            r7 = move-exception
            goto L_0x0036
        L_0x006b:
            r7 = move-exception
            goto L_0x003b
        L_0x006d:
            r8 = move-exception
            goto L_0x0058
        L_0x006f:
            r8 = move-exception
            goto L_0x005d
        L_0x0071:
            r8 = move-exception
            goto L_0x0062
        L_0x0073:
            r7 = move-exception
            r0 = r1
            goto L_0x0053
        L_0x0076:
            r7 = move-exception
            r3 = r4
            r0 = r1
            goto L_0x0053
        L_0x007a:
            r7 = move-exception
            goto L_0x002c
        L_0x007c:
            r7 = move-exception
            r0 = r1
            goto L_0x002c
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.copyStreamByStream(java.io.InputStream, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0037 A[SYNTHETIC, Splitter:B:17:0x0037] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003c A[SYNTHETIC, Splitter:B:20:0x003c] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0041 A[SYNTHETIC, Splitter:B:23:0x0041] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x005e A[SYNTHETIC, Splitter:B:41:0x005e] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0063 A[SYNTHETIC, Splitter:B:44:0x0063] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0068 A[SYNTHETIC, Splitter:B:47:0x0068] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyStreamByBuffer(java.io.InputStream r9, java.lang.String r10) {
        /*
            r6 = 0
            if (r9 == 0) goto L_0x0044
            if (r10 == 0) goto L_0x0044
            r0 = 0
            r3 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0083, all -> 0x005b }
            java.io.InputStreamReader r7 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0083, all -> 0x005b }
            r7.<init>(r9)     // Catch:{ Exception -> 0x0083, all -> 0x005b }
            r8 = 1024(0x400, float:1.435E-42)
            r1.<init>(r7, r8)     // Catch:{ Exception -> 0x0083, all -> 0x005b }
            java.io.BufferedWriter r4 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x0085, all -> 0x007c }
            java.io.FileWriter r7 = new java.io.FileWriter     // Catch:{ Exception -> 0x0085, all -> 0x007c }
            r7.<init>(r10)     // Catch:{ Exception -> 0x0085, all -> 0x007c }
            r8 = 1024(0x400, float:1.435E-42)
            r4.<init>(r7, r8)     // Catch:{ Exception -> 0x0085, all -> 0x007c }
            r7 = 1024(0x400, float:1.435E-42)
            char[] r2 = new char[r7]     // Catch:{ Exception -> 0x0032, all -> 0x007f }
            r5 = 0
        L_0x0024:
            r7 = 0
            r8 = 1024(0x400, float:1.435E-42)
            int r5 = r1.read(r2, r7, r8)     // Catch:{ Exception -> 0x0032, all -> 0x007f }
            if (r5 <= 0) goto L_0x0045
            r7 = 0
            r4.write(r2, r7, r5)     // Catch:{ Exception -> 0x0032, all -> 0x007f }
            goto L_0x0024
        L_0x0032:
            r7 = move-exception
            r3 = r4
            r0 = r1
        L_0x0035:
            if (r0 == 0) goto L_0x003a
            r0.close()     // Catch:{ Exception -> 0x0070 }
        L_0x003a:
            if (r9 == 0) goto L_0x003f
            r9.close()     // Catch:{ Exception -> 0x0072 }
        L_0x003f:
            if (r3 == 0) goto L_0x0044
            r3.close()     // Catch:{ Exception -> 0x0074 }
        L_0x0044:
            return r6
        L_0x0045:
            r4.flush()     // Catch:{ Exception -> 0x0032, all -> 0x007f }
            r6 = 1
            if (r1 == 0) goto L_0x004e
            r1.close()     // Catch:{ Exception -> 0x006c }
        L_0x004e:
            if (r9 == 0) goto L_0x0053
            r9.close()     // Catch:{ Exception -> 0x006e }
        L_0x0053:
            if (r4 == 0) goto L_0x0044
            r4.close()     // Catch:{ Exception -> 0x0059 }
            goto L_0x0044
        L_0x0059:
            r7 = move-exception
            goto L_0x0044
        L_0x005b:
            r7 = move-exception
        L_0x005c:
            if (r0 == 0) goto L_0x0061
            r0.close()     // Catch:{ Exception -> 0x0076 }
        L_0x0061:
            if (r9 == 0) goto L_0x0066
            r9.close()     // Catch:{ Exception -> 0x0078 }
        L_0x0066:
            if (r3 == 0) goto L_0x006b
            r3.close()     // Catch:{ Exception -> 0x007a }
        L_0x006b:
            throw r7
        L_0x006c:
            r7 = move-exception
            goto L_0x004e
        L_0x006e:
            r7 = move-exception
            goto L_0x0053
        L_0x0070:
            r7 = move-exception
            goto L_0x003a
        L_0x0072:
            r7 = move-exception
            goto L_0x003f
        L_0x0074:
            r7 = move-exception
            goto L_0x0044
        L_0x0076:
            r8 = move-exception
            goto L_0x0061
        L_0x0078:
            r8 = move-exception
            goto L_0x0066
        L_0x007a:
            r8 = move-exception
            goto L_0x006b
        L_0x007c:
            r7 = move-exception
            r0 = r1
            goto L_0x005c
        L_0x007f:
            r7 = move-exception
            r3 = r4
            r0 = r1
            goto L_0x005c
        L_0x0083:
            r7 = move-exception
            goto L_0x0035
        L_0x0085:
            r7 = move-exception
            r0 = r1
            goto L_0x0035
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.copyStreamByBuffer(java.io.InputStream, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0040 A[SYNTHETIC, Splitter:B:20:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0045 A[SYNTHETIC, Splitter:B:23:0x0045] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x005d A[SYNTHETIC, Splitter:B:38:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0062 A[SYNTHETIC, Splitter:B:41:0x0062] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFileByBuffer(java.lang.String r9, java.lang.String r10) {
        /*
            r5 = 0
            if (r9 == 0) goto L_0x0048
            if (r10 == 0) goto L_0x0048
            java.io.File r6 = new java.io.File
            r6.<init>(r9)
            boolean r7 = r6.exists()
            if (r7 == 0) goto L_0x0048
            boolean r7 = r6.isFile()
            if (r7 == 0) goto L_0x0048
            r0 = 0
            r2 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0077, all -> 0x005a }
            java.io.FileReader r7 = new java.io.FileReader     // Catch:{ Exception -> 0x0077, all -> 0x005a }
            r7.<init>(r9)     // Catch:{ Exception -> 0x0077, all -> 0x005a }
            r8 = 1024(0x400, float:1.435E-42)
            r1.<init>(r7, r8)     // Catch:{ Exception -> 0x0077, all -> 0x005a }
            java.io.BufferedWriter r3 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x0079, all -> 0x0070 }
            java.io.FileWriter r7 = new java.io.FileWriter     // Catch:{ Exception -> 0x0079, all -> 0x0070 }
            r7.<init>(r10)     // Catch:{ Exception -> 0x0079, all -> 0x0070 }
            r8 = 1024(0x400, float:1.435E-42)
            r3.<init>(r7, r8)     // Catch:{ Exception -> 0x0079, all -> 0x0070 }
            r4 = 0
        L_0x0031:
            java.lang.String r4 = r1.readLine()     // Catch:{ Exception -> 0x003b, all -> 0x0073 }
            if (r4 == 0) goto L_0x0049
            r3.write(r4)     // Catch:{ Exception -> 0x003b, all -> 0x0073 }
            goto L_0x0031
        L_0x003b:
            r7 = move-exception
            r2 = r3
            r0 = r1
        L_0x003e:
            if (r0 == 0) goto L_0x0043
            r0.close()     // Catch:{ Exception -> 0x0068 }
        L_0x0043:
            if (r2 == 0) goto L_0x0048
            r2.close()     // Catch:{ Exception -> 0x006a }
        L_0x0048:
            return r5
        L_0x0049:
            r3.flush()     // Catch:{ Exception -> 0x003b, all -> 0x0073 }
            r5 = 1
            if (r1 == 0) goto L_0x0052
            r1.close()     // Catch:{ Exception -> 0x0066 }
        L_0x0052:
            if (r3 == 0) goto L_0x0048
            r3.close()     // Catch:{ Exception -> 0x0058 }
            goto L_0x0048
        L_0x0058:
            r7 = move-exception
            goto L_0x0048
        L_0x005a:
            r7 = move-exception
        L_0x005b:
            if (r0 == 0) goto L_0x0060
            r0.close()     // Catch:{ Exception -> 0x006c }
        L_0x0060:
            if (r2 == 0) goto L_0x0065
            r2.close()     // Catch:{ Exception -> 0x006e }
        L_0x0065:
            throw r7
        L_0x0066:
            r7 = move-exception
            goto L_0x0052
        L_0x0068:
            r7 = move-exception
            goto L_0x0043
        L_0x006a:
            r7 = move-exception
            goto L_0x0048
        L_0x006c:
            r8 = move-exception
            goto L_0x0060
        L_0x006e:
            r8 = move-exception
            goto L_0x0065
        L_0x0070:
            r7 = move-exception
            r0 = r1
            goto L_0x005b
        L_0x0073:
            r7 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x005b
        L_0x0077:
            r7 = move-exception
            goto L_0x003e
        L_0x0079:
            r7 = move-exception
            r0 = r1
            goto L_0x003e
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.copyFileByBuffer(java.lang.String, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x004c A[SYNTHETIC, Splitter:B:20:0x004c] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0051 A[SYNTHETIC, Splitter:B:23:0x0051] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0056 A[SYNTHETIC, Splitter:B:26:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x005b A[SYNTHETIC, Splitter:B:29:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x007e A[SYNTHETIC, Splitter:B:51:0x007e] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0083 A[SYNTHETIC, Splitter:B:54:0x0083] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0088 A[SYNTHETIC, Splitter:B:57:0x0088] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x008d A[SYNTHETIC, Splitter:B:60:0x008d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean copyFileByChannel(java.lang.String r11, java.lang.String r12) {
        /*
            r6 = 0
            if (r11 == 0) goto L_0x005e
            if (r12 == 0) goto L_0x005e
            java.io.File r7 = new java.io.File
            r7.<init>(r11)
            boolean r9 = r7.exists()
            if (r9 == 0) goto L_0x005e
            boolean r9 = r7.isFile()
            if (r9 == 0) goto L_0x005e
            r2 = 0
            r4 = 0
            r8 = 0
            r1 = 0
            r0 = 0
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00ae, all -> 0x007b }
            r3.<init>(r7)     // Catch:{ Exception -> 0x00ae, all -> 0x007b }
            java.io.FileOutputStream r5 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x00b0, all -> 0x00a7 }
            r5.<init>(r12)     // Catch:{ Exception -> 0x00b0, all -> 0x00a7 }
            java.nio.channels.FileChannel r8 = r3.getChannel()     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            java.nio.channels.FileChannel r1 = r5.getChannel()     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            r9 = 1024(0x400, float:1.435E-42)
            java.nio.ByteBuffer r0 = java.nio.ByteBuffer.allocate(r9)     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            r0.clear()     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
        L_0x0036:
            r9 = -1
            int r10 = r8.read(r0)     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            if (r9 == r10) goto L_0x005f
            r0.flip()     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            r1.write(r0)     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            r0.clear()     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            goto L_0x0036
        L_0x0047:
            r9 = move-exception
            r4 = r5
            r2 = r3
        L_0x004a:
            if (r1 == 0) goto L_0x004f
            r1.close()     // Catch:{ Exception -> 0x0097 }
        L_0x004f:
            if (r8 == 0) goto L_0x0054
            r8.close()     // Catch:{ Exception -> 0x0099 }
        L_0x0054:
            if (r2 == 0) goto L_0x0059
            r2.close()     // Catch:{ Exception -> 0x009b }
        L_0x0059:
            if (r4 == 0) goto L_0x005e
            r4.close()     // Catch:{ Exception -> 0x009d }
        L_0x005e:
            return r6
        L_0x005f:
            r9 = 1
            r1.force(r9)     // Catch:{ Exception -> 0x0047, all -> 0x00aa }
            r6 = 1
            if (r1 == 0) goto L_0x0069
            r1.close()     // Catch:{ Exception -> 0x0091 }
        L_0x0069:
            if (r8 == 0) goto L_0x006e
            r8.close()     // Catch:{ Exception -> 0x0093 }
        L_0x006e:
            if (r3 == 0) goto L_0x0073
            r3.close()     // Catch:{ Exception -> 0x0095 }
        L_0x0073:
            if (r5 == 0) goto L_0x005e
            r5.close()     // Catch:{ Exception -> 0x0079 }
            goto L_0x005e
        L_0x0079:
            r9 = move-exception
            goto L_0x005e
        L_0x007b:
            r9 = move-exception
        L_0x007c:
            if (r1 == 0) goto L_0x0081
            r1.close()     // Catch:{ Exception -> 0x009f }
        L_0x0081:
            if (r8 == 0) goto L_0x0086
            r8.close()     // Catch:{ Exception -> 0x00a1 }
        L_0x0086:
            if (r2 == 0) goto L_0x008b
            r2.close()     // Catch:{ Exception -> 0x00a3 }
        L_0x008b:
            if (r4 == 0) goto L_0x0090
            r4.close()     // Catch:{ Exception -> 0x00a5 }
        L_0x0090:
            throw r9
        L_0x0091:
            r9 = move-exception
            goto L_0x0069
        L_0x0093:
            r9 = move-exception
            goto L_0x006e
        L_0x0095:
            r9 = move-exception
            goto L_0x0073
        L_0x0097:
            r9 = move-exception
            goto L_0x004f
        L_0x0099:
            r9 = move-exception
            goto L_0x0054
        L_0x009b:
            r9 = move-exception
            goto L_0x0059
        L_0x009d:
            r9 = move-exception
            goto L_0x005e
        L_0x009f:
            r10 = move-exception
            goto L_0x0081
        L_0x00a1:
            r10 = move-exception
            goto L_0x0086
        L_0x00a3:
            r10 = move-exception
            goto L_0x008b
        L_0x00a5:
            r10 = move-exception
            goto L_0x0090
        L_0x00a7:
            r9 = move-exception
            r2 = r3
            goto L_0x007c
        L_0x00aa:
            r9 = move-exception
            r4 = r5
            r2 = r3
            goto L_0x007c
        L_0x00ae:
            r9 = move-exception
            goto L_0x004a
        L_0x00b0:
            r9 = move-exception
            r2 = r3
            goto L_0x004a
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.copyFileByChannel(java.lang.String, java.lang.String):boolean");
    }

    public static boolean moveFile(File srcFile, File desFile) {
        if (srcFile == null || desFile == null) {
            return false;
        }
        return srcFile.renameTo(desFile);
    }

    public static boolean moveFile(String srcPath, String desPath) {
        if (srcPath == null || desPath == null) {
            return false;
        }
        File srcFile = getFileByPath(srcPath);
        File desFile = getFileByPath(desPath);
        if (srcFile == null || desFile == null) {
            return false;
        }
        return getFileByPath(srcPath).renameTo(getFileByPath(desPath));
    }

    public static String concatPath(String... paths) {
        boolean suffixSeparator;
        boolean prefixSeparator;
        StringBuilder result = new StringBuilder();
        if (paths != null) {
            for (String path : paths) {
                if (path != null && path.length() > 0) {
                    int len = result.length();
                    if (len <= 0 || result.charAt(len - 1) != File.separatorChar) {
                        suffixSeparator = false;
                    } else {
                        suffixSeparator = true;
                    }
                    if (path.charAt(0) == File.separatorChar) {
                        prefixSeparator = true;
                    } else {
                        prefixSeparator = false;
                    }
                    if (suffixSeparator && prefixSeparator) {
                        result.append(path.substring(1));
                    } else if (suffixSeparator || prefixSeparator) {
                        result.append(path);
                    } else {
                        result.append(File.separatorChar);
                        result.append(path);
                    }
                }
            }
        }
        return result.toString();
    }

    public static long getFileLastModified(String filePath) {
        return getFileLastModified(getFileByPath(filePath));
    }

    public static long getFileLastModified(File file) {
        if (file == null) {
            return -1;
        }
        return file.lastModified();
    }

    private static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        }
        if (((double) byteNum) < SIZE_KB) {
            return String.format(Locale.getDefault(), "%.3fB", Double.valueOf((double) byteNum));
        } else if (((double) byteNum) < SIZE_MB) {
            return String.format(Locale.getDefault(), "%.3fKB", Double.valueOf(((double) byteNum) / SIZE_KB));
        } else if (((double) byteNum) < SIZE_GB) {
            return String.format(Locale.getDefault(), "%.3fMB", Double.valueOf(((double) byteNum) / SIZE_MB));
        } else {
            return String.format(Locale.getDefault(), "%.3fGB", Double.valueOf(((double) byteNum) / SIZE_GB));
        }
    }

    public static byte[] getFileMD5(String filePath) {
        return getFileMD5(getFileByPath(filePath));
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x003a A[SYNTHETIC, Splitter:B:22:0x003a] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0046 A[SYNTHETIC, Splitter:B:28:0x0046] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getFileMD5(java.io.File r8) {
        /*
            r6 = 0
            if (r8 != 0) goto L_0x0004
        L_0x0003:
            return r6
        L_0x0004:
            r1 = 0
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ NoSuchAlgorithmException -> 0x0033, IOException -> 0x004f }
            r4.<init>(r8)     // Catch:{ NoSuchAlgorithmException -> 0x0033, IOException -> 0x004f }
            java.lang.String r7 = "MD5"
            java.security.MessageDigest r5 = java.security.MessageDigest.getInstance(r7)     // Catch:{ NoSuchAlgorithmException -> 0x0033, IOException -> 0x004f }
            java.security.DigestInputStream r2 = new java.security.DigestInputStream     // Catch:{ NoSuchAlgorithmException -> 0x0033, IOException -> 0x004f }
            r2.<init>(r4, r5)     // Catch:{ NoSuchAlgorithmException -> 0x0033, IOException -> 0x004f }
            r7 = 262144(0x40000, float:3.67342E-40)
            byte[] r0 = new byte[r7]     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
        L_0x001a:
            int r7 = r2.read(r0)     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            if (r7 > 0) goto L_0x001a
            java.security.MessageDigest r5 = r2.getMessageDigest()     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            byte[] r6 = r5.digest()     // Catch:{ NoSuchAlgorithmException -> 0x0055, IOException -> 0x0058, all -> 0x0052 }
            if (r2 == 0) goto L_0x0003
            r2.close()     // Catch:{ IOException -> 0x002e }
            goto L_0x0003
        L_0x002e:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0003
        L_0x0033:
            r7 = move-exception
        L_0x0034:
            r3 = r7
        L_0x0035:
            r3.printStackTrace()     // Catch:{ all -> 0x0043 }
            if (r1 == 0) goto L_0x0003
            r1.close()     // Catch:{ IOException -> 0x003e }
            goto L_0x0003
        L_0x003e:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0003
        L_0x0043:
            r6 = move-exception
        L_0x0044:
            if (r1 == 0) goto L_0x0049
            r1.close()     // Catch:{ IOException -> 0x004a }
        L_0x0049:
            throw r6
        L_0x004a:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0049
        L_0x004f:
            r7 = move-exception
        L_0x0050:
            r3 = r7
            goto L_0x0035
        L_0x0052:
            r6 = move-exception
            r1 = r2
            goto L_0x0044
        L_0x0055:
            r7 = move-exception
            r1 = r2
            goto L_0x0034
        L_0x0058:
            r7 = move-exception
            r1 = r2
            goto L_0x0050
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.utils.FileUtils.getFileMD5(java.io.File):byte[]");
    }

    public static long getFileLength(File file) {
        if (!isFile(file)) {
            return -1;
        }
        return file.length();
    }

    public static long getFileLength(String filePath) {
        if (filePath != null && filePath.matches("[a-zA-z]+://[^\\s]*")) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(filePath).openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    return (long) conn.getContentLength();
                }
                return -1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getFileLength(getFileByPath(filePath));
    }

    public static String getDirName(File file) {
        if (file == null) {
            return "";
        }
        return getDirName(file.getAbsolutePath());
    }

    public static String getDirName(String filePath) {
        int lastSep;
        return (!hasSpace(filePath) && (lastSep = filePath.lastIndexOf(File.separator)) != -1) ? filePath.substring(0, lastSep + 1) : "";
    }

    public static String getFileName(File file) {
        if (file == null) {
            return "";
        }
        return getFileName(file.getAbsolutePath());
    }

    public static String getFileName(String filePath) {
        if (hasSpace(filePath)) {
            return "";
        }
        int lastSep = filePath.lastIndexOf(File.separator);
        return lastSep != -1 ? filePath.substring(lastSep + 1) : filePath;
    }

    public static String getFileNameNoExtension(File file) {
        if (file == null) {
            return "";
        }
        return getFileNameNoExtension(file.getPath());
    }

    public static String getFileNameNoExtension(String filePath) {
        if (hasSpace(filePath)) {
            return "";
        }
        int lastPoi = filePath.lastIndexOf(46);
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastSep == -1) {
            if (lastPoi != -1) {
                return filePath.substring(0, lastPoi);
            }
            return filePath;
        } else if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1);
        } else {
            return filePath.substring(lastSep + 1, lastPoi);
        }
    }

    public static String getFileExtension(File file) {
        if (file == null) {
            return "";
        }
        return getFileExtension(file.getPath());
    }

    public static String getFileExtension(String filePath) {
        if (hasSpace(filePath)) {
            return "";
        }
        int lastPoi = filePath.lastIndexOf(46);
        int lastSep = filePath.lastIndexOf(File.separator);
        if (lastPoi == -1 || lastSep >= lastPoi) {
            return "";
        }
        return filePath.substring(lastPoi + 1);
    }

    public static String getDirSize(String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }

    public static String getDirSize(File dir) {
        long len = getDirLength(dir);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    public static String getFileSize(String filePath) {
        long len = getFileLength(filePath);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    public static String getFileSize(File file) {
        long len = getFileLength(file);
        return len == -1 ? "" : byte2FitMemorySize(len);
    }

    public static long getDirLength(String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }

    public static long getDirLength(File dir) {
        long length;
        if (!isDirectory(dir)) {
            return -1;
        }
        long len = 0;
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return 0;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                length = getDirLength(file);
            } else {
                length = file.length();
            }
            len += length;
        }
        return len;
    }
}
