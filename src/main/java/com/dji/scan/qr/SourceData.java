package com.dji.scan.qr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import com.google.zxing.PlanarYUVLuminanceSource;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.ByteArrayOutputStream;

@EXClassNullAway
public class SourceData {
    private Rect cropRect;
    private byte[] data;
    private int dataHeight;
    private int dataWidth;
    private int imageFormat;
    private int rotation;

    public SourceData(byte[] data2, int dataWidth2, int dataHeight2, int imageFormat2, int rotation2) {
        this.data = data2;
        this.dataWidth = dataWidth2;
        this.dataHeight = dataHeight2;
        this.rotation = rotation2;
        this.imageFormat = imageFormat2;
        if (dataWidth2 * dataHeight2 > data2.length) {
            throw new IllegalArgumentException("Image data does not match the resolution. " + dataWidth2 + "x" + dataHeight2 + " > " + data2.length);
        }
    }

    public Rect getCropRect() {
        return this.cropRect;
    }

    public void setCropRect(Rect cropRect2) {
        this.cropRect = cropRect2;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getDataWidth() {
        return this.dataWidth;
    }

    public int getDataHeight() {
        return this.dataHeight;
    }

    public boolean isRotated() {
        return this.rotation % 180 != 0;
    }

    public int getImageFormat() {
        return this.imageFormat;
    }

    public PlanarYUVLuminanceSource createSource() {
        byte[] rotated = rotateCameraPreview(this.rotation, this.data, this.dataWidth, this.dataHeight);
        if (isRotated()) {
            return new PlanarYUVLuminanceSource(rotated, this.dataHeight, this.dataWidth, this.cropRect.left, this.cropRect.top, this.cropRect.width(), this.cropRect.height(), false);
        }
        return new PlanarYUVLuminanceSource(rotated, this.dataWidth, this.dataHeight, this.cropRect.left, this.cropRect.top, this.cropRect.width(), this.cropRect.height(), false);
    }

    public Bitmap getBitmap() {
        return getBitmap(1);
    }

    public Bitmap getBitmap(int scaleFactor) {
        return getBitmap(this.cropRect, scaleFactor);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap}
     arg types: [android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, int]
     candidates:
      ClspMth{android.graphics.Bitmap.createBitmap(android.util.DisplayMetrics, int[], int, int, int, int, android.graphics.Bitmap$Config):android.graphics.Bitmap}
      ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap} */
    private Bitmap getBitmap(Rect cropRect2, int scaleFactor) {
        if (isRotated()) {
            cropRect2 = new Rect(cropRect2.top, cropRect2.left, cropRect2.bottom, cropRect2.right);
        }
        YuvImage img = new YuvImage(this.data, this.imageFormat, this.dataWidth, this.dataHeight, null);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        img.compressToJpeg(cropRect2, 90, buffer);
        byte[] jpegData = buffer.toByteArray();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegData, 0, jpegData.length, options);
        if (this.rotation == 0) {
            return bitmap;
        }
        Matrix imageMatrix = new Matrix();
        imageMatrix.postRotate((float) this.rotation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), imageMatrix, false);
    }

    public static byte[] rotateCameraPreview(int cameraRotation, byte[] data2, int imageWidth, int imageHeight) {
        switch (cameraRotation) {
            case 0:
            default:
                return data2;
            case 90:
                return rotateCW(data2, imageWidth, imageHeight);
            case 180:
                return rotate180(data2, imageWidth, imageHeight);
            case 270:
                return rotateCCW(data2, imageWidth, imageHeight);
        }
    }

    public static byte[] rotateCW(byte[] data2, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[(imageWidth * imageHeight)];
        int i = 0;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data2[(y * imageWidth) + x];
                i++;
            }
        }
        return yuv;
    }

    public static byte[] rotate180(byte[] data2, int imageWidth, int imageHeight) {
        int n = imageWidth * imageHeight;
        byte[] yuv = new byte[n];
        int i = n - 1;
        for (int j = 0; j < n; j++) {
            yuv[i] = data2[j];
            i--;
        }
        return yuv;
    }

    public static byte[] rotateCCW(byte[] data2, int imageWidth, int imageHeight) {
        int n = imageWidth * imageHeight;
        byte[] yuv = new byte[n];
        int i = n - 1;
        for (int x = 0; x < imageWidth; x++) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                yuv[i] = data2[(y * imageWidth) + x];
                i--;
            }
        }
        return yuv;
    }
}
