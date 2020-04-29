package dji.midware.natives;

import android.graphics.Bitmap;
import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;

@Keep
@EXClassNullAway
public class Vision {
    public static native void HDBokeh(Bitmap bitmap);

    public static native void decodeYUV420SP(byte[] bArr, int i, int i2, String str);

    public static native int getDenseDepth(Bitmap bitmap, Bitmap[] bitmapArr, String str, String str2, double d, double d2, double d3);

    public static native void initCallback();

    public static native void pano180(String[] strArr, float[] fArr, float[] fArr2, String str);

    public static native void pano1x3(String[] strArr, float[] fArr, float[] fArr2, String str);

    public static native void pano3x3(String[] strArr, float[] fArr, float[] fArr2, String str);

    public static native void panoSphere(String[] strArr, float[] fArr, float[] fArr2, String str);

    public static native int poseFromImages(Bitmap[] bitmapArr, double d, double d2, double d3);

    public static native int[] render(String str, Bitmap bitmap, Bitmap bitmap2, int i, int i2, double d);

    public static native void saveResult(String str);

    public static native void setParams(int i, int i2, float f, int i3, int i4, float f2, float f3);

    public static native void test();

    static {
        try {
            System.loadLibrary("duml_vision_bokeh");
            System.loadLibrary("duml_vision_panorama");
            System.loadLibrary("Vision");
            Log.d(Vision.class.getName(), "load lib suc");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.d(Vision.class.getName(), "Couldn't load lib");
        }
    }

    public static void loadLibrary() {
    }
}
