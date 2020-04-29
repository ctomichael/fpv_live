package dji.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import dji.sdksharedlib.keycatalog.CameraKeys;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap bytes2Bitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable bitmap2Drawable(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return new BitmapDrawable(AppUtils.getApp().getResources(), bitmap);
    }

    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
        if (drawable == null) {
            return null;
        }
        return bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    public static Drawable bytes2Drawable(byte[] bytes) {
        return bitmap2Drawable(bytes2Bitmap(bytes));
    }

    public static boolean isImage(String filePath) {
        if (filePath == null) {
            return false;
        }
        String path = filePath.toUpperCase();
        if (path.endsWith(".PNG") || path.endsWith(".JPG") || path.endsWith(".JPEG") || path.endsWith(".BMP") || path.endsWith(".GIF") || path.endsWith(".WEBP")) {
            return true;
        }
        return false;
    }

    public static Bitmap getBitmap(File file) {
        if (file == null) {
            return null;
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static Bitmap getBitmap(File file, int maxWidth, int maxHeight) {
        if (file == null) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static Bitmap scale(Bitmap src, int newWidth, int newHeight, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        if (newWidth > src.getWidth() || newHeight > src.getHeight()) {
        }
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (src == ret || !recycle || src.isRecycled()) {
            return ret;
        }
        src.recycle();
        return ret;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap}
     arg types: [android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, int]
     candidates:
      ClspMth{android.graphics.Bitmap.createBitmap(android.util.DisplayMetrics, int[], int, int, int, int, android.graphics.Bitmap$Config):android.graphics.Bitmap}
      ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap} */
    public static Bitmap rotate(Bitmap src, int degrees, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        if (degrees == 0) {
            return src;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate((float) degrees);
        Bitmap ret = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (ret != src && recycle && !src.isRecycled()) {
            src.recycle();
        }
        return ret;
    }

    public static Bitmap rotate(Bitmap src, ExifInterface exif, boolean recycle) {
        int degree;
        if (src == null) {
            return null;
        }
        if (exif == null) {
            return src;
        }
        switch (exif.getAttributeInt(CameraKeys.ORIENTATION, 0)) {
            case 3:
                degree = 180;
                break;
            case 4:
            case 5:
            case 7:
            default:
                degree = 0;
                break;
            case 6:
                degree = 90;
                break;
            case 8:
                degree = 270;
                break;
        }
        if (degree != 0) {
            return rotate(src, degree, recycle);
        }
        return src;
    }

    @Nullable
    public static Bitmap createAppropriateBmp(String filePath, int dstWidth, int dstHeight) {
        Log.d("createFitBmp", "executed");
        if (TextUtils.isEmpty(filePath) || dstWidth <= 0 || dstHeight <= 0) {
            return null;
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        if (opts.outWidth > dstWidth || opts.outHeight > dstHeight) {
            int wRatio = Math.round((((float) opts.outWidth) * 1.0f) / ((float) dstWidth));
            int hRatio = Math.round((((float) opts.outHeight) * 1.0f) / ((float) dstHeight));
            if (wRatio >= hRatio) {
                hRatio = wRatio;
            }
            opts.inSampleSize = hRatio;
        } else {
            opts.inSampleSize = 1;
        }
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, opts);
    }

    public static int getRotateDegree(String filePath) {
        try {
            switch (new ExifInterface(filePath).getAttributeInt(CameraKeys.ORIENTATION, 1)) {
                case 3:
                    return 180;
                case 4:
                case 5:
                case 7:
                default:
                    return 0;
                case 6:
                    return 90;
                case 8:
                    return 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static Bitmap toRound(Bitmap src, boolean recycle) {
        return toRound(src, 0, 0, recycle);
    }

    public static Bitmap toRound(Bitmap src, @IntRange(from = 0) int borderSize, @ColorInt int borderColor, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        int size = Math.min(width, height);
        Paint paint = new Paint(1);
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        float center = ((float) size) / 2.0f;
        RectF rectF = new RectF(0.0f, 0.0f, (float) width, (float) height);
        rectF.inset(((float) (width - size)) / 2.0f, ((float) (height - size)) / 2.0f);
        Matrix matrix = new Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        matrix.preScale(((float) size) / ((float) width), ((float) size) / ((float) height));
        BitmapShader shader = new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        Canvas canvas = new Canvas(ret);
        canvas.drawRoundRect(rectF, center, center, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) borderSize);
            canvas.drawCircle(((float) width) / 2.0f, ((float) height) / 2.0f, center - (((float) borderSize) / 2.0f), paint);
        }
        if (!recycle || src.isRecycled()) {
            return ret;
        }
        src.recycle();
        return ret;
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius, boolean recycle) {
        return toRoundCorner(src, radius, 0, 0, recycle);
    }

    public static Bitmap toRoundCorner(Bitmap src, float radius, @IntRange(from = 0) int borderSize, @ColorInt int borderColor, boolean recycle) {
        if (isEmptyBitmap(src)) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        Paint paint = new Paint(1);
        Bitmap ret = Bitmap.createBitmap(width, height, src.getConfig());
        paint.setShader(new BitmapShader(src, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        Canvas canvas = new Canvas(ret);
        RectF rectF = new RectF(0.0f, 0.0f, (float) width, (float) height);
        float halfBorderSize = ((float) borderSize) / 2.0f;
        rectF.inset(halfBorderSize, halfBorderSize);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth((float) borderSize);
            paint.setStrokeCap(Paint.Cap.ROUND);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
        if (!recycle || src.isRecycled()) {
            return ret;
        }
        src.recycle();
        return ret;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (true) {
            width >>= 1;
            if (width < maxWidth || (height = height >> 1) < maxHeight) {
                return inSampleSize;
            }
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    private static boolean isEmptyBitmap(Bitmap bitmap) {
        return bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0;
    }
}
