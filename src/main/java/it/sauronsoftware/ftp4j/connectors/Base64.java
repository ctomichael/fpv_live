package it.sauronsoftware.ftp4j.connectors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

class Base64 {
    static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    static char pad = '=';

    Base64() {
    }

    public static String encode(String str) throws RuntimeException {
        try {
            return new String(encode(str.getBytes()), "ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("ASCII is not supported!", e);
        }
    }

    public static String encode(String str, String charset) throws RuntimeException {
        try {
            try {
                return new String(encode(str.getBytes(charset)), "ASCII");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("ASCII is not supported!", e);
            }
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Unsupported charset: " + charset, e2);
        }
    }

    public static String decode(String str) throws RuntimeException {
        try {
            return new String(decode(str.getBytes("ASCII")));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("ASCII is not supported!", e);
        }
    }

    public static String decode(String str, String charset) throws RuntimeException {
        try {
            try {
                return new String(decode(str.getBytes("ASCII")), charset);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unsupported charset: " + charset, e);
            }
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("ASCII is not supported!", e2);
        }
    }

    public static byte[] encode(byte[] bytes) throws RuntimeException {
        return encode(bytes, 0);
    }

    public static byte[] encode(byte[] bytes, int wrapAt) throws RuntimeException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            encode(inputStream, outputStream, wrapAt);
            try {
                inputStream.close();
            } catch (Throwable th) {
            }
            try {
                outputStream.close();
            } catch (Throwable th2) {
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        } catch (Throwable th3) {
            try {
                inputStream.close();
            } catch (Throwable th4) {
            }
            outputStream.close();
            throw th3;
        }
    }

    public static byte[] decode(byte[] bytes) throws RuntimeException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            decode(inputStream, outputStream);
            try {
                inputStream.close();
            } catch (Throwable th) {
            }
            try {
                outputStream.close();
            } catch (Throwable th2) {
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected I/O error", e);
        } catch (Throwable th3) {
            try {
                inputStream.close();
            } catch (Throwable th4) {
            }
            outputStream.close();
            throw th3;
        }
    }

    public static void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
        encode(inputStream, outputStream, 0);
    }

    public static void encode(InputStream inputStream, OutputStream outputStream, int wrapAt) throws IOException {
        Base64OutputStream aux = new Base64OutputStream(outputStream, wrapAt);
        copy(inputStream, aux);
        aux.commit();
    }

    public static void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(new Base64InputStream(inputStream), outputStream);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x001d A[SYNTHETIC, Splitter:B:15:0x001d] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0022 A[SYNTHETIC, Splitter:B:18:0x0022] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void encode(java.io.File r6, java.io.File r7, int r8) throws java.io.IOException {
        /*
            r0 = 0
            r2 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x001a }
            r1.<init>(r6)     // Catch:{ all -> 0x001a }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x002e }
            r3.<init>(r7)     // Catch:{ all -> 0x002e }
            encode(r1, r3, r8)     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x0014
            r3.close()     // Catch:{ Throwable -> 0x0026 }
        L_0x0014:
            if (r1 == 0) goto L_0x0019
            r1.close()     // Catch:{ Throwable -> 0x0028 }
        L_0x0019:
            return
        L_0x001a:
            r4 = move-exception
        L_0x001b:
            if (r2 == 0) goto L_0x0020
            r2.close()     // Catch:{ Throwable -> 0x002a }
        L_0x0020:
            if (r0 == 0) goto L_0x0025
            r0.close()     // Catch:{ Throwable -> 0x002c }
        L_0x0025:
            throw r4
        L_0x0026:
            r4 = move-exception
            goto L_0x0014
        L_0x0028:
            r4 = move-exception
            goto L_0x0019
        L_0x002a:
            r5 = move-exception
            goto L_0x0020
        L_0x002c:
            r5 = move-exception
            goto L_0x0025
        L_0x002e:
            r4 = move-exception
            r0 = r1
            goto L_0x001b
        L_0x0031:
            r4 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.ftp4j.connectors.Base64.encode(java.io.File, java.io.File, int):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x001d A[SYNTHETIC, Splitter:B:15:0x001d] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0022 A[SYNTHETIC, Splitter:B:18:0x0022] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void encode(java.io.File r6, java.io.File r7) throws java.io.IOException {
        /*
            r0 = 0
            r2 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x001a }
            r1.<init>(r6)     // Catch:{ all -> 0x001a }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x002e }
            r3.<init>(r7)     // Catch:{ all -> 0x002e }
            encode(r1, r3)     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x0014
            r3.close()     // Catch:{ Throwable -> 0x0026 }
        L_0x0014:
            if (r1 == 0) goto L_0x0019
            r1.close()     // Catch:{ Throwable -> 0x0028 }
        L_0x0019:
            return
        L_0x001a:
            r4 = move-exception
        L_0x001b:
            if (r2 == 0) goto L_0x0020
            r2.close()     // Catch:{ Throwable -> 0x002a }
        L_0x0020:
            if (r0 == 0) goto L_0x0025
            r0.close()     // Catch:{ Throwable -> 0x002c }
        L_0x0025:
            throw r4
        L_0x0026:
            r4 = move-exception
            goto L_0x0014
        L_0x0028:
            r4 = move-exception
            goto L_0x0019
        L_0x002a:
            r5 = move-exception
            goto L_0x0020
        L_0x002c:
            r5 = move-exception
            goto L_0x0025
        L_0x002e:
            r4 = move-exception
            r0 = r1
            goto L_0x001b
        L_0x0031:
            r4 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.ftp4j.connectors.Base64.encode(java.io.File, java.io.File):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x001d A[SYNTHETIC, Splitter:B:15:0x001d] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0022 A[SYNTHETIC, Splitter:B:18:0x0022] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void decode(java.io.File r6, java.io.File r7) throws java.io.IOException {
        /*
            r0 = 0
            r2 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ all -> 0x001a }
            r1.<init>(r6)     // Catch:{ all -> 0x001a }
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ all -> 0x002e }
            r3.<init>(r7)     // Catch:{ all -> 0x002e }
            decode(r1, r3)     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x0014
            r3.close()     // Catch:{ Throwable -> 0x0026 }
        L_0x0014:
            if (r1 == 0) goto L_0x0019
            r1.close()     // Catch:{ Throwable -> 0x0028 }
        L_0x0019:
            return
        L_0x001a:
            r4 = move-exception
        L_0x001b:
            if (r2 == 0) goto L_0x0020
            r2.close()     // Catch:{ Throwable -> 0x002a }
        L_0x0020:
            if (r0 == 0) goto L_0x0025
            r0.close()     // Catch:{ Throwable -> 0x002c }
        L_0x0025:
            throw r4
        L_0x0026:
            r4 = move-exception
            goto L_0x0014
        L_0x0028:
            r4 = move-exception
            goto L_0x0019
        L_0x002a:
            r5 = move-exception
            goto L_0x0020
        L_0x002c:
            r5 = move-exception
            goto L_0x0025
        L_0x002e:
            r4 = move-exception
            r0 = r1
            goto L_0x001b
        L_0x0031:
            r4 = move-exception
            r2 = r3
            r0 = r1
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: it.sauronsoftware.ftp4j.connectors.Base64.decode(java.io.File, java.io.File):void");
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] b = new byte[1024];
        while (true) {
            int len = inputStream.read(b);
            if (len != -1) {
                outputStream.write(b, 0, len);
            } else {
                return;
            }
        }
    }
}
