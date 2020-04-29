package com.amap.location.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* compiled from: GZipUtil */
public class d {
    public static byte[] a(byte[] bArr) throws Exception {
        GZIPOutputStream gZIPOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            } catch (Exception e) {
                e = e;
                gZIPOutputStream = null;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                gZIPOutputStream = null;
                e.a(byteArrayOutputStream);
                e.a(gZIPOutputStream);
                throw th;
            }
            try {
                gZIPOutputStream.write(bArr);
                gZIPOutputStream.finish();
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                e.a(byteArrayOutputStream);
                e.a(gZIPOutputStream);
                return byteArray;
            } catch (Exception e2) {
                e = e2;
                throw e;
            }
        } catch (Exception e3) {
            e = e3;
            gZIPOutputStream = null;
            byteArrayOutputStream = null;
            throw e;
        } catch (Throwable th3) {
            th = th3;
            gZIPOutputStream = null;
            byteArrayOutputStream = null;
            e.a(byteArrayOutputStream);
            e.a(gZIPOutputStream);
            throw th;
        }
    }

    public static byte[] b(byte[] bArr) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream;
        GZIPInputStream gZIPInputStream;
        ByteArrayInputStream byteArrayInputStream;
        try {
            byteArrayInputStream = new ByteArrayInputStream(bArr);
            try {
                gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            } catch (Exception e) {
                e = e;
                byteArrayOutputStream = null;
                gZIPInputStream = null;
                try {
                    throw e;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = null;
                gZIPInputStream = null;
                e.a((Closeable) byteArrayInputStream);
                e.a((Closeable) gZIPInputStream);
                e.a(byteArrayOutputStream);
                throw th;
            }
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (Exception e2) {
                e = e2;
                byteArrayOutputStream = null;
                throw e;
            } catch (Throwable th3) {
                th = th3;
                byteArrayOutputStream = null;
                e.a((Closeable) byteArrayInputStream);
                e.a((Closeable) gZIPInputStream);
                e.a(byteArrayOutputStream);
                throw th;
            }
            try {
                e.a(gZIPInputStream, byteArrayOutputStream);
                byteArrayOutputStream.flush();
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                e.a((Closeable) byteArrayInputStream);
                e.a((Closeable) gZIPInputStream);
                e.a(byteArrayOutputStream);
                return byteArray;
            } catch (Exception e3) {
                e = e3;
                throw e;
            }
        } catch (Exception e4) {
            e = e4;
            byteArrayOutputStream = null;
            gZIPInputStream = null;
            byteArrayInputStream = null;
            throw e;
        } catch (Throwable th4) {
            th = th4;
            byteArrayOutputStream = null;
            gZIPInputStream = null;
            byteArrayInputStream = null;
            e.a((Closeable) byteArrayInputStream);
            e.a((Closeable) gZIPInputStream);
            e.a(byteArrayOutputStream);
            throw th;
        }
    }
}
