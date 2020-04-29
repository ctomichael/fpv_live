package dji.midware.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.v4.view.ViewCompat;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.natives.GroudStation;
import dji.midware.natives.Vision;
import dji.thirdparty.sanselan.ImageReadException;
import dji.thirdparty.sanselan.Sanselan;
import dji.thirdparty.sanselan.formats.tiff.TiffField;
import dji.thirdparty.sanselan.formats.tiff.TiffImageMetadata;
import dji.thirdparty.sanselan.formats.tiff.constants.TiffConstants;
import java.io.File;
import java.io.IOException;

@EXClassNullAway
public class DJIThumbNailUtils {
    public static Bitmap getThumbNailJPEG(byte[] mHeadBuffer, int start, int length) {
        boolean z;
        int mCurrentPos = start + 2;
        if (BytesUtil.getUShort(mHeadBuffer, start) != 55551) {
            return null;
        }
        while (mCurrentPos < mHeadBuffer.length) {
            int frameType = BytesUtil.getUShort(mHeadBuffer, mCurrentPos);
            int mCurrentPos2 = mCurrentPos + 2;
            int len = BytesUtil.getUShort(mHeadBuffer, mCurrentPos2);
            mCurrentPos = mCurrentPos2 + 2;
            if (frameType == 57855) {
                break;
            }
            int mCurrentPos3 = mCurrentPos + len;
            int isEnd = BytesUtil.getUShort(mHeadBuffer, mCurrentPos3);
            mCurrentPos = mCurrentPos3 + 2;
            if (55807 == isEnd) {
                return null;
            }
        }
        int exifFlag1 = BytesUtil.getUShort(mHeadBuffer, mCurrentPos);
        int mCurrentPos4 = mCurrentPos + 2;
        int exifFlag2 = BytesUtil.getUShort(mHeadBuffer, mCurrentPos4);
        int mCurrentPos5 = mCurrentPos4 + 2;
        int exifFlag3 = BytesUtil.getUShort(mHeadBuffer, mCurrentPos5);
        int mCurrentPos6 = mCurrentPos5 + 2;
        if ((exifFlag3 != 0) || ((exifFlag1 != 30789) | (exifFlag2 != 26217))) {
            return null;
        }
        int startPos = mCurrentPos6;
        int mCurrentPos7 = mCurrentPos6 + 4;
        int thumbnailSzie = 0;
        int thumbnailOffset = 0;
        boolean sizeFound = false;
        boolean offsetFound = false;
        while (true) {
            if (sizeFound && offsetFound) {
                break;
            }
            int offset = BytesUtil.getInt(mHeadBuffer, mCurrentPos7);
            int mCurrentPos8 = mCurrentPos7 + 4;
            int mCurrentPos9 = startPos + offset;
            int tagsNumber = BytesUtil.getUShort(mHeadBuffer, mCurrentPos9);
            mCurrentPos7 = mCurrentPos9 + 2;
            for (int i = 0; i < tagsNumber; i++) {
                int tagType = BytesUtil.getUShort(mHeadBuffer, mCurrentPos7);
                int mCurrentPos10 = mCurrentPos7 + 2;
                int uShort = BytesUtil.getUShort(mHeadBuffer, mCurrentPos10);
                int mCurrentPos11 = mCurrentPos10 + 2;
                int i2 = BytesUtil.getInt(mHeadBuffer, mCurrentPos11);
                int mCurrentPos12 = mCurrentPos11 + 4;
                int temp = BytesUtil.getInt(mHeadBuffer, mCurrentPos12);
                mCurrentPos7 = mCurrentPos12 + 4;
                if (tagType == 513) {
                    offsetFound = true;
                    thumbnailOffset = temp;
                } else if (tagType == 514) {
                    sizeFound = true;
                    thumbnailSzie = temp;
                }
                if (sizeFound && offsetFound) {
                    break;
                }
            }
        }
        int mCurrentPos13 = startPos + thumbnailOffset;
        if (thumbnailSzie <= 0) {
            return null;
        }
        if (mCurrentPos13 + thumbnailSzie > length) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;
        return BitmapFactory.decodeByteArray(mHeadBuffer, mCurrentPos13, thumbnailSzie, options);
    }

    public static Bitmap getThumbNailDNG(byte[] mHeadBuffer, int start, int length) {
        int mCurrentPos = start;
        int startPos = mCurrentPos;
        int mCurrentPos2 = mCurrentPos + 4;
        int thumbnailSzie = 0;
        int thumbnailOffset = 0;
        boolean sizeFound = false;
        boolean offsetFound = false;
        while (true) {
            if ((!sizeFound || !offsetFound) && mCurrentPos2 + 18 < length - start) {
                int offset = BytesUtil.getInt(mHeadBuffer, mCurrentPos2);
                int mCurrentPos3 = mCurrentPos2 + 4;
                int mCurrentPos4 = startPos + offset;
                int tagsNumber = BytesUtil.getUShort(mHeadBuffer, mCurrentPos4);
                mCurrentPos2 = mCurrentPos4 + 2;
                for (int i = 0; i < tagsNumber; i++) {
                    int tagType = BytesUtil.getUShort(mHeadBuffer, mCurrentPos2);
                    int mCurrentPos5 = mCurrentPos2 + 2;
                    int uShort = BytesUtil.getUShort(mHeadBuffer, mCurrentPos5);
                    int mCurrentPos6 = mCurrentPos5 + 2;
                    int i2 = BytesUtil.getInt(mHeadBuffer, mCurrentPos6);
                    int mCurrentPos7 = mCurrentPos6 + 4;
                    int temp = BytesUtil.getInt(mHeadBuffer, mCurrentPos7);
                    mCurrentPos2 = mCurrentPos7 + 4;
                    if (tagType == 273) {
                        offsetFound = true;
                        thumbnailOffset = temp;
                    } else if (tagType == 279) {
                        sizeFound = true;
                        thumbnailSzie = temp;
                    }
                    if (sizeFound && offsetFound) {
                        break;
                    }
                }
            }
        }
        if (!sizeFound || !offsetFound) {
            return null;
        }
        int mCurrentPos8 = startPos + thumbnailOffset;
        if (thumbnailSzie <= 0) {
            return null;
        }
        if (mCurrentPos8 + thumbnailSzie > length) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;
        return BitmapFactory.decodeByteArray(mHeadBuffer, mCurrentPos8, thumbnailSzie, options);
    }

    public static Bitmap getThumbNail(byte[] buffer, int start, int length) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;
        return BitmapFactory.decodeByteArray(buffer, start, length, options);
    }

    public static Bitmap getBokehYUV(byte[] buffer, int width, int height) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/tmp.jpg";
        Vision.decodeYUV420SP(buffer, width, height, path);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        try {
            if (!new File(path).exists()) {
                return null;
            }
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap getThumbNailThm(byte[] buffer) {
        DJILog.d("playback", "DJIFileNailLoader buffer size=" + buffer.length, new Object[0]);
        if (buffer.length < 38400) {
            return null;
        }
        return cropBitmap(Bitmap.createBitmap(GroudStation.native_yuv422ToImage(buffer, 160, 120), 160, 120, Bitmap.Config.ARGB_8888));
    }

    public static Bitmap covertTIFtoJPG(byte[] buffer, int start, int length) {
        try {
            byte[] mbuffer = new byte[length];
            System.arraycopy(buffer, start, mbuffer, 0, length);
            TiffImageMetadata metadata = (TiffImageMetadata) Sanselan.getMetadata(mbuffer);
            TiffField widthField = metadata.findField(TiffConstants.EXIF_TAG_IMAGE_WIDTH_IFD0);
            TiffField heightField = metadata.findField(TiffConstants.EXIF_TAG_IMAGE_HEIGHT_IFD0);
            TiffField startField = metadata.findField(TiffConstants.EXIF_TAG_PREVIEW_IMAGE_START_IFD0);
            TiffField lengthField = metadata.findField(TiffConstants.EXIF_TAG_PREVIEW_IMAGE_LENGTH_IFD0);
            int sampleValue = metadata.findField(TiffConstants.EXIF_TAG_BITS_PER_SAMPLE).getIntValue() / 8;
            int dataLen = lengthField.getIntValue();
            int[] colors = new int[(dataLen / sampleValue)];
            normalized(mbuffer, startField.getIntValue(), dataLen, sampleValue, colors);
            return Bitmap.createBitmap(colors, widthField.getIntValue(), heightField.getIntValue(), Bitmap.Config.ARGB_8888);
        } catch (ImageReadException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    private static void normalized(byte[] buffer, int start, int length, int sample, int[] colors) {
        int minValue = 100000;
        int maxValue = 0;
        int max = start + length;
        int i = start;
        while (i < max) {
            int j = (i - start) / sample;
            for (int j2 = 0; j2 < sample; j2++) {
                colors[j] = colors[j] + ((buffer[i + j2] & 255) << (j2 * 8));
            }
            if (minValue >= colors[j]) {
                minValue = colors[j];
            }
            if (maxValue <= colors[j]) {
                maxValue = colors[j];
            }
            i += sample;
        }
        float per = (((float) (maxValue - minValue)) * 1.0f) / 256.0f;
        for (int i2 = 0; i2 < colors.length; i2++) {
            colors[i2] = Math.round(((float) (colors[i2] - minValue)) / per);
            colors[i2] = colors[i2] | (colors[i2] << 8) | (colors[i2] << 16) | ViewCompat.MEASURED_STATE_MASK;
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap}
     arg types: [android.graphics.Bitmap, int, int, int, int, ?[OBJECT, ARRAY], int]
     candidates:
      ClspMth{android.graphics.Bitmap.createBitmap(android.util.DisplayMetrics, int[], int, int, int, int, android.graphics.Bitmap$Config):android.graphics.Bitmap}
      ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap} */
    private static Bitmap cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int cropHeight = (w * 9) / 16;
        return Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() - ((w * 9) / 16)) / 2, w, cropHeight, (Matrix) null, false);
    }
}
