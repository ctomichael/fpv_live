package com.mapbox.mapboxsdk.maps.renderer.egl;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.os.EnvironmentCompat;
import com.mapbox.mapboxsdk.constants.MapboxConstants;
import com.mapbox.mapboxsdk.log.Logger;
import com.mapbox.mapboxsdk.utils.Compare;
import java.util.ArrayList;
import java.util.Collections;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class EGLConfigChooser implements GLSurfaceView.EGLConfigChooser {
    private static final int EGL_CONFORMANT = 12354;
    private static final int EGL_OPENGL_ES2_BIT = 4;
    private static final String TAG = "Mbgl-EGLConfigChooser";
    private boolean translucentSurface;

    public EGLConfigChooser() {
        this(false);
    }

    public EGLConfigChooser(boolean translucentSurface2) {
        this.translucentSurface = translucentSurface2;
    }

    public EGLConfig chooseConfig(@NonNull EGL10 egl, EGLDisplay display) {
        int[] configAttribs = getConfigAttributes();
        int[] numConfigs = getNumberOfConfigurations(egl, display, configAttribs);
        if (numConfigs[0] < 1) {
            Logger.e(TAG, "eglChooseConfig() returned no configs.");
            throw new EGLConfigException("eglChooseConfig() failed");
        }
        EGLConfig config = chooseBestMatchConfig(egl, display, getPossibleConfigurations(egl, display, configAttribs, numConfigs));
        if (config != null) {
            return config;
        }
        Logger.e(TAG, "No config chosen");
        throw new EGLConfigException("No config chosen");
    }

    @NonNull
    private int[] getNumberOfConfigurations(EGL10 egl, EGLDisplay display, int[] configAttributes) {
        int[] numConfigs = new int[1];
        if (egl.eglChooseConfig(display, configAttributes, null, 0, numConfigs)) {
            return numConfigs;
        }
        Logger.e(TAG, String.format(MapboxConstants.MAPBOX_LOCALE, "eglChooseConfig(NULL) returned error %d", Integer.valueOf(egl.eglGetError())));
        throw new EGLConfigException("eglChooseConfig() failed");
    }

    @NonNull
    private EGLConfig[] getPossibleConfigurations(EGL10 egl, EGLDisplay display, int[] configAttributes, int[] numConfigs) {
        EGLConfig[] configs = new EGLConfig[numConfigs[0]];
        if (egl.eglChooseConfig(display, configAttributes, configs, numConfigs[0], numConfigs)) {
            return configs;
        }
        Logger.e(TAG, String.format(MapboxConstants.MAPBOX_LOCALE, "eglChooseConfig() returned error %d", Integer.valueOf(egl.eglGetError())));
        throw new EGLConfigException("eglChooseConfig() failed");
    }

    enum BufferFormat {
        Format16Bit(3),
        Format32BitNoAlpha(1),
        Format32BitAlpha(2),
        Format24Bit(0),
        Unknown(4);
        
        int value;

        private BufferFormat(int value2) {
            this.value = value2;
        }
    }

    enum DepthStencilFormat {
        Format16Depth8Stencil(1),
        Format24Depth8Stencil(0);
        
        int value;

        private DepthStencilFormat(int value2) {
            this.value = value2;
        }
    }

    private EGLConfig chooseBestMatchConfig(@NonNull EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
        BufferFormat bufferFormat;
        DepthStencilFormat depthStencilFormat;
        ArrayList arrayList = new ArrayList();
        int i = 0;
        int length = configs.length;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= length) {
                break;
            }
            EGLConfig config = configs[i3];
            i++;
            int caveat = getConfigAttr(egl, display, config, 12327);
            int conformant = getConfigAttr(egl, display, config, EGL_CONFORMANT);
            int bits = getConfigAttr(egl, display, config, 12320);
            int red = getConfigAttr(egl, display, config, 12324);
            int green = getConfigAttr(egl, display, config, 12323);
            int blue = getConfigAttr(egl, display, config, 12322);
            int alpha = getConfigAttr(egl, display, config, 12321);
            int configAttr = getConfigAttr(egl, display, config, 12350);
            int depth = getConfigAttr(egl, display, config, 12325);
            int stencil = getConfigAttr(egl, display, config, 12326);
            if (((depth == 24 || depth == 16) & (stencil == 8) & (getConfigAttr(egl, display, config, 12338) == 0)) && (getConfigAttr(egl, display, config, 12337) == 0)) {
                if (bits == 16 && red == 5 && green == 6 && blue == 5 && alpha == 0) {
                    bufferFormat = BufferFormat.Format16Bit;
                } else if (bits == 32 && red == 8 && green == 8 && blue == 8 && alpha == 0) {
                    bufferFormat = BufferFormat.Format32BitNoAlpha;
                } else if (bits == 32 && red == 8 && green == 8 && blue == 8 && alpha == 8) {
                    bufferFormat = BufferFormat.Format32BitAlpha;
                } else if (bits == 24 && red == 8 && green == 8 && blue == 8 && alpha == 0) {
                    bufferFormat = BufferFormat.Format24Bit;
                } else {
                    bufferFormat = BufferFormat.Unknown;
                }
                if (depth == 16 && stencil == 8) {
                    depthStencilFormat = DepthStencilFormat.Format16Depth8Stencil;
                } else {
                    depthStencilFormat = DepthStencilFormat.Format24Depth8Stencil;
                }
                boolean isNotConformant = (conformant & 4) != 4;
                boolean isCaveat = caveat != 12344;
                if (bufferFormat != BufferFormat.Unknown) {
                    arrayList.add(new Comparable<AnonymousClass1Config>(bufferFormat, depthStencilFormat, isNotConformant, isCaveat, i, config) {
                        /* class com.mapbox.mapboxsdk.maps.renderer.egl.EGLConfigChooser.AnonymousClass1Config */
                        private final BufferFormat bufferFormat;
                        /* access modifiers changed from: private */
                        public final EGLConfig config;
                        private final DepthStencilFormat depthStencilFormat;
                        private final int index;
                        /* access modifiers changed from: private */
                        public final boolean isCaveat;
                        /* access modifiers changed from: private */
                        public final boolean isNotConformant;

                        {
                            this.bufferFormat = bufferFormat;
                            this.depthStencilFormat = depthStencilFormat;
                            this.isNotConformant = isNotConformant;
                            this.isCaveat = isCaveat;
                            this.index = index;
                            this.config = config;
                        }

                        public int compareTo(@NonNull AnonymousClass1Config other) {
                            int i = Compare.compare(this.bufferFormat.value, other.bufferFormat.value);
                            if (i != 0) {
                                return i;
                            }
                            int i2 = Compare.compare(this.depthStencilFormat.value, other.depthStencilFormat.value);
                            if (i2 != 0) {
                                return i2;
                            }
                            int i3 = Compare.compare(this.isNotConformant, other.isNotConformant);
                            if (i3 != 0) {
                                return i3;
                            }
                            int i4 = Compare.compare(this.isCaveat, other.isCaveat);
                            if (i4 != 0) {
                                return i4;
                            }
                            int i5 = Compare.compare(this.index, other.index);
                            if (i5 != 0) {
                                return i5;
                            }
                            return 0;
                        }
                    });
                }
            }
            i2 = i3 + 1;
        }
        Collections.sort(arrayList);
        if (arrayList.size() == 0) {
            throw new EGLConfigException("No matching configurations after filtering");
        }
        AnonymousClass1Config bestMatch = (AnonymousClass1Config) arrayList.get(0);
        if (bestMatch.isCaveat) {
            Logger.w(TAG, "Chosen config has a caveat.");
        }
        if (bestMatch.isNotConformant) {
            Logger.w(TAG, "Chosen config is not conformant.");
        }
        return bestMatch.config;
    }

    private int getConfigAttr(EGL10 egl, EGLDisplay display, EGLConfig config, int attributeName) {
        int[] attributevalue = new int[1];
        if (egl.eglGetConfigAttrib(display, config, attributeName, attributevalue)) {
            return attributevalue[0];
        }
        Logger.e(TAG, String.format(MapboxConstants.MAPBOX_LOCALE, "eglGetConfigAttrib(%d) returned error %d", Integer.valueOf(attributeName), Integer.valueOf(egl.eglGetError())));
        throw new EGLConfigException("eglGetConfigAttrib() failed");
    }

    private int[] getConfigAttributes() {
        boolean emulator;
        int i = 0;
        if (inEmulator() || inGenymotion()) {
            emulator = true;
        } else {
            emulator = false;
        }
        Logger.i(TAG, String.format("In emulator: %s", Boolean.valueOf(emulator)));
        int[] iArr = new int[25];
        iArr[0] = 12327;
        iArr[1] = 12344;
        iArr[2] = 12339;
        iArr[3] = 4;
        iArr[4] = 12320;
        iArr[5] = 16;
        iArr[6] = 12324;
        iArr[7] = 5;
        iArr[8] = 12323;
        iArr[9] = 6;
        iArr[10] = 12322;
        iArr[11] = 5;
        iArr[12] = 12321;
        if (this.translucentSurface) {
            i = 8;
        }
        iArr[13] = i;
        iArr[14] = 12325;
        iArr[15] = 16;
        iArr[16] = 12326;
        iArr[17] = 8;
        iArr[18] = emulator ? 12344 : EGL_CONFORMANT;
        iArr[19] = 4;
        iArr[20] = emulator ? 12344 : 12351;
        iArr[21] = 12430;
        iArr[22] = 12352;
        iArr[23] = 4;
        iArr[24] = 12344;
        return iArr;
    }

    private boolean inEmulator() {
        if (Build.FINGERPRINT.startsWith("generic") || Build.FINGERPRINT.startsWith(EnvironmentCompat.MEDIA_UNKNOWN) || Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") || Build.MODEL.contains("Android SDK built for x86") || ((Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")) || "google_sdk".equals(Build.PRODUCT) || System.getProperty("ro.kernel.qemu") != null)) {
            return true;
        }
        return false;
    }

    private boolean inGenymotion() {
        return Build.MANUFACTURER.contains("Genymotion");
    }
}
