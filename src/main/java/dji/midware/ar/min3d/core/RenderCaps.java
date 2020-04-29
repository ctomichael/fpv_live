package dji.midware.ar.min3d.core;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import java.nio.IntBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

@EXClassNullAway
public class RenderCaps {
    private static int _aliasedLineSizeMax;
    private static int _aliasedLineSizeMin;
    private static int _aliasedPointSizeMax;
    private static int _aliasedPointSizeMin;
    private static boolean _isGl10Only;
    private static int _maxLights;
    private static int _maxTextureSize;
    private static int _maxTextureUnits;
    private static float _openGlVersion;
    private static int _smoothLineSizeMax;
    private static int _smoothLineSizeMin;
    private static int _smoothPointSizeMax;
    private static int _smoothPointSizeMin;

    public static float openGlVersion() {
        return _openGlVersion;
    }

    public static boolean isGl10Only() {
        return _isGl10Only;
    }

    public static int maxTextureUnits() {
        return _maxTextureUnits;
    }

    public static int aliasedPointSizeMin() {
        return _aliasedPointSizeMin;
    }

    public static int aliasedPointSizeMax() {
        return _aliasedPointSizeMax;
    }

    public static int smoothPointSizeMin() {
        return _smoothPointSizeMin;
    }

    public static int smoothPointSizeMax() {
        return _smoothPointSizeMax;
    }

    public static int aliasedLineSizeMin() {
        return _aliasedLineSizeMin;
    }

    public static int aliasedLineSizeMax() {
        return _aliasedLineSizeMax;
    }

    public static int smoothLineSizeMin() {
        return _smoothLineSizeMin;
    }

    public static int smoothLineSizeMax() {
        return _smoothLineSizeMax;
    }

    public static int maxLights() {
        return _maxLights;
    }

    static void setRenderCaps(GL10 $gl) {
        if ($gl instanceof GL11) {
            _openGlVersion = 1.1f;
        } else {
            _openGlVersion = 1.0f;
        }
        IntBuffer i = IntBuffer.allocate(1);
        $gl.glGetIntegerv(34018, i);
        _maxTextureUnits = i.get(0);
        IntBuffer i2 = IntBuffer.allocate(1);
        $gl.glGetIntegerv(3379, i2);
        _maxTextureSize = i2.get(0);
        IntBuffer i3 = IntBuffer.allocate(2);
        $gl.glGetIntegerv(33901, i3);
        _aliasedPointSizeMin = i3.get(0);
        _aliasedPointSizeMax = i3.get(1);
        IntBuffer i4 = IntBuffer.allocate(2);
        $gl.glGetIntegerv(2834, i4);
        _smoothPointSizeMin = i4.get(0);
        _smoothPointSizeMax = i4.get(1);
        IntBuffer i5 = IntBuffer.allocate(2);
        $gl.glGetIntegerv(33902, i5);
        _aliasedLineSizeMin = i5.get(0);
        _aliasedLineSizeMax = i5.get(1);
        IntBuffer i6 = IntBuffer.allocate(2);
        $gl.glGetIntegerv(2850, i6);
        _smoothLineSizeMin = i6.get(0);
        _smoothLineSizeMax = i6.get(1);
        IntBuffer i7 = IntBuffer.allocate(1);
        $gl.glGetIntegerv(3377, i7);
        _maxLights = i7.get(0);
        Log.v(Min3d.TAG, "RenderCaps - openGLVersion: " + _openGlVersion);
        Log.v(Min3d.TAG, "RenderCaps - maxTextureUnits: " + _maxTextureUnits);
        Log.v(Min3d.TAG, "RenderCaps - maxTextureSize: " + _maxTextureSize);
        Log.v(Min3d.TAG, "RenderCaps - maxLights: " + _maxLights);
    }
}
