package com.dji.video.framing.internal.opengl.renderer;

import android.opengl.GLES20;

public class GLUndistortRender extends GLRenderBase {
    protected static final String FRAGMENT_SHADER_EXTERNAL = "#extension GL_OES_EGL_image_external : require\nprecision highp float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nuniform float k1;\nuniform float k2;\nuniform float k3;\nuniform float p1;\nuniform float p2;\nvoid main() {\n \tfloat xd=vTextureCoord.x*2.0-1.0; \n    float yd=vTextureCoord.y*2.0-1.0; \n    float rd2=xd*xd+yd*yd;    \n    float xc=xd*(1.0-k1*rd2+k2*rd2*rd2+k3*rd2*rd2*rd2)-2.0*p1*xd*yd+p2*(rd2+2.0*xd*xd); \n   float yc=yd*(1.0-k1*rd2+k2*rd2*rd2+k3*rd2*rd2*rd2)+p1*(rd2+2.0*yd*yd)+2.0*p2*xd*yd; \n   if (xc>1.0||yc>1.0||xc<-1.0||yc<-1.0) { \n   \tgl_FragColor = vec4(0.0, 0.0, 0.0, 1.0); \n   } else { \n\t\tgl_FragColor = texture2D(sTexture, vec2(xc*0.5+0.5, yc*0.5+0.5));\n   } \n }\n";
    protected static final String FRAGMENT_SHADER_INTERNAL = "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
    protected static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    protected final boolean isExternal;
    private float k1;
    private float k2;
    private float k3;
    private float p1;
    private float p2;
    private int u_k1;
    private int u_k2;
    private int u_k3;
    private int u_p1;
    private int u_p2;

    public GLUndistortRender(boolean isExternal2) {
        this(isExternal2, 1.0f);
    }

    public GLUndistortRender(boolean isExternal2, float height_scale) {
        this.k1 = -0.02226907f;
        this.k2 = 0.01961271f;
        this.k3 = 0.00522009f;
        this.p1 = -0.00380254f;
        this.p2 = 0.0f;
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
        this.u_k1 = GLES20.glGetUniformLocation(this.mProgram, "k1");
        checkLocation(this.u_k1, "u_k1");
        this.u_k2 = GLES20.glGetUniformLocation(this.mProgram, "k2");
        checkLocation(this.u_k2, "u_k2");
        this.u_k3 = GLES20.glGetUniformLocation(this.mProgram, "k3");
        checkLocation(this.u_k3, "u_k3");
        this.u_p1 = GLES20.glGetUniformLocation(this.mProgram, "p1");
        checkLocation(this.u_p1, "u_p1");
        this.u_p2 = GLES20.glGetUniformLocation(this.mProgram, "p2");
        checkLocation(this.u_p2, "u_p2");
    }

    /* access modifiers changed from: protected */
    public void preDrawArrays() {
        GLES20.glUniform1f(this.u_k1, this.k1);
        GLES20.glUniform1f(this.u_k2, this.k2);
        GLES20.glUniform1f(this.u_k3, this.k3);
        GLES20.glUniform1f(this.u_p1, this.p1);
        GLES20.glUniform1f(this.u_p2, this.p2);
        GLES20.glDrawArrays(5, 4, 4);
    }

    public void setDistortionParameters(float k12, float k22, float k32, float p12, float p22) {
        this.k1 = k12;
        this.k2 = k22;
        this.k3 = k32;
        this.p1 = p12;
        this.p2 = p22;
    }
}
