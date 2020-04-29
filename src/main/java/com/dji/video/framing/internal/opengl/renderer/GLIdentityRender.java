package com.dji.video.framing.internal.opengl.renderer;

import android.opengl.GLES20;

public class GLIdentityRender extends GLRenderBase {
    public static boolean DEBUG = false;
    protected static final String FRAGMENT_SHADER_EXTERNAL = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform float yuv_scale_uniform; \nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord)*yuv_scale_uniform;\n}\n";
    protected static final String FRAGMENT_SHADER_INTERNAL = "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nuniform float yuv_scale_uniform; \nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord)*yuv_scale_uniform;\n}\n";
    public static String TAG = "GLIdentityRender";
    protected static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    protected final boolean isExternal;
    private float yuv_scale = 1.0f;
    private int yuv_scale_uniform_loc;

    public GLIdentityRender(boolean isExternal2) {
        this.isExternal = isExternal2;
    }

    /* access modifiers changed from: protected */
    public String getVertexShader() {
        return VERTEX_SHADER;
    }

    /* access modifiers changed from: protected */
    public String getFragmentShader() {
        if (this.isExternal) {
            return FRAGMENT_SHADER_EXTERNAL;
        }
        return FRAGMENT_SHADER_INTERNAL;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.yuv_scale_uniform_loc = GLES20.glGetUniformLocation(this.mProgram, "yuv_scale_uniform");
        checkLocation(this.yuv_scale_uniform_loc, "yuv_scale_uniform_loc");
    }

    /* access modifiers changed from: protected */
    public void preDrawArrays() {
        GLES20.glUniform1f(this.yuv_scale_uniform_loc, this.yuv_scale);
    }

    public void setYUVScale(float scale) {
        this.yuv_scale = scale;
    }
}
