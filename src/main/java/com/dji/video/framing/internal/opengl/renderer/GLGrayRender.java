package com.dji.video.framing.internal.opengl.renderer;

import android.opengl.GLES20;
import com.dji.video.framing.utils.DJIVideoUtil;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import java.nio.ByteBuffer;

public class GLGrayRender extends GLRenderBase {
    private static String TAG = "GLRGB2YUVRender";
    private final String FRAGMENT_SHADER_EXTERNAL_FULL_RANGE = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 yuv;\nvec3 rgb;\nrgb.r=texture2D(sTexture, vTextureCoord).r;\nrgb.g=texture2D(sTexture, vTextureCoord).g;\nrgb.b=texture2D(sTexture, vTextureCoord).b;\nyuv.r=0.299*rgb.r+0.587*rgb.g+0.114*rgb.b;\nyuv.g=-0.148*rgb.r-0.289*rgb.g+0.437*rgb.b+0.5;\nyuv.b=0.615*rgb.r-0.515*rgb.g-0.100*rgb.b+0.5;\n;gl_FragColor =vec4(yuv,1.0);\n}\n";
    private final String FRAGMENT_SHADER_EXTERNAL_VIDEO_RANGE = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 yuv;\nvec3 rgb;\nrgb.r=texture2D(sTexture, vTextureCoord).r;\nrgb.g=texture2D(sTexture, vTextureCoord).g;\nrgb.b=texture2D(sTexture, vTextureCoord).b;\nyuv.r=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\nyuv.g=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\nyuv.b=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\n;gl_FragColor =vec4(yuv,1.0);\n}\n";
    private final String RGB2YUV_VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    private byte[] dst;
    final boolean isExternal;
    final boolean isFullRange;
    private int preheight = 0;
    private int prewidth = 0;
    protected boolean saveFirstReadData = false;
    private byte[] src;

    public GLGrayRender(boolean isExternal2, boolean isFullRange2) {
        this.isExternal = isExternal2;
        this.isFullRange = isFullRange2;
    }

    public void readYUVData(ByteBuffer yuvBuffer, int width, int height) {
        GLES20.glPixelStorei(3333, 4);
        GLES20.glReadPixels(0, 0, width, height, 6408, FujifilmMakernoteDirectory.TAG_FILM_MODE, yuvBuffer);
        try {
            if (this.saveFirstReadData) {
                this.saveFirstReadData = false;
                if (!(this.src != null && this.prewidth == width && this.preheight == height)) {
                    this.src = new byte[(width * height * 4)];
                    this.dst = new byte[((int) (((double) (width * height)) * 1.5d))];
                }
                yuvBuffer.position(0);
                yuvBuffer.limit(width * height * 4);
                yuvBuffer.get(this.src, 0, this.src.length);
                DJIVideoUtil.YUVFormatConvert(this.src, this.dst, width, height);
                DJIVideoUtil.writeBufferToFile(ByteBuffer.wrap(this.dst), 0, this.dst.length, System.currentTimeMillis() + ".yuv");
            }
        } catch (Exception e) {
        }
    }

    public void readYUVData(ByteBuffer yuvBuffer, int left, int bottom, int right, int top) {
        GLES20.glPixelStorei(3333, 4);
        GLES20.glReadPixels(left, bottom, right, top, 6408, FujifilmMakernoteDirectory.TAG_FILM_MODE, yuvBuffer);
    }

    /* access modifiers changed from: protected */
    public String getVertexShader() {
        return "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    }

    /* access modifiers changed from: protected */
    public String getFragmentShader() {
        if (this.isExternal) {
            if (this.isFullRange) {
                return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 yuv;\nvec3 rgb;\nrgb.r=texture2D(sTexture, vTextureCoord).r;\nrgb.g=texture2D(sTexture, vTextureCoord).g;\nrgb.b=texture2D(sTexture, vTextureCoord).b;\nyuv.r=0.299*rgb.r+0.587*rgb.g+0.114*rgb.b;\nyuv.g=-0.148*rgb.r-0.289*rgb.g+0.437*rgb.b+0.5;\nyuv.b=0.615*rgb.r-0.515*rgb.g-0.100*rgb.b+0.5;\n;gl_FragColor =vec4(yuv,1.0);\n}\n";
            }
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 yuv;\nvec3 rgb;\nrgb.r=texture2D(sTexture, vTextureCoord).r;\nrgb.g=texture2D(sTexture, vTextureCoord).g;\nrgb.b=texture2D(sTexture, vTextureCoord).b;\nyuv.r=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\nyuv.g=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\nyuv.b=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\n;gl_FragColor =vec4(yuv,1.0);\n}\n";
        } else if (this.isFullRange) {
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 yuv;\nvec3 rgb;\nrgb.r=texture2D(sTexture, vTextureCoord).r;\nrgb.g=texture2D(sTexture, vTextureCoord).g;\nrgb.b=texture2D(sTexture, vTextureCoord).b;\nyuv.r=0.299*rgb.r+0.587*rgb.g+0.114*rgb.b;\nyuv.g=-0.148*rgb.r-0.289*rgb.g+0.437*rgb.b+0.5;\nyuv.b=0.615*rgb.r-0.515*rgb.g-0.100*rgb.b+0.5;\n;gl_FragColor =vec4(yuv,1.0);\n}\n".replace("#extension GL_OES_EGL_image_external : require", "//#declare").replace("uniform samplerExternalOES sTexture;", "uniform sampler2D sTexture;");
        } else {
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 yuv;\nvec3 rgb;\nrgb.r=texture2D(sTexture, vTextureCoord).r;\nrgb.g=texture2D(sTexture, vTextureCoord).g;\nrgb.b=texture2D(sTexture, vTextureCoord).b;\nyuv.r=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\nyuv.g=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\nyuv.b=0.2568*rgb.r+0.5041*rgb.g+0.0979*rgb.b+0.0625;\n;gl_FragColor =vec4(yuv,1.0);\n}\n".replace("#extension GL_OES_EGL_image_external : require", "//#declare").replace("uniform samplerExternalOES sTexture;", "uniform sampler2D sTexture;");
        }
    }

    /* access modifiers changed from: protected */
    public void onInit() {
    }

    /* access modifiers changed from: protected */
    public void preDrawArrays() {
    }
}
