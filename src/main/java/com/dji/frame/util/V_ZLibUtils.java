package com.dji.frame.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public abstract class V_ZLibUtils {
    public static byte[] compress(byte[] data) {
        byte[] output;
        byte[] bArr = new byte[0];
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                bos.write(buf, 0, compresser.deflate(buf));
            }
            output = bos.toByteArray();
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            output = data;
            e2.printStackTrace();
            try {
                bos.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                bos.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
        compresser.end();
        return output;
    }

    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);
        try {
            dos.write(data, 0, data.length);
            dos.finish();
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] decompress(byte[] data) {
        byte[] output;
        byte[] bArr = new byte[0];
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!decompresser.finished()) {
                o.write(buf, 0, decompresser.inflate(buf));
            }
            output = o.toByteArray();
            try {
                o.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e2) {
            output = data;
            e2.printStackTrace();
            try {
                o.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                o.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
        decompresser.end();
        return output;
    }

    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
        int i = 1024;
        try {
            byte[] buf = new byte[1024];
            while (true) {
                i = iis.read(buf, 0, i);
                if (i <= 0) {
                    break;
                }
                o.write(buf, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }
}
