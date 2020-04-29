package com.dji.video.framing.internal.opengl.renderer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.DJIVideoDecoderInterface;
import com.dji.video.framing.internal.opengl.GLUtil;
import java.nio.ByteBuffer;

public class OverExposureWarner extends GLRenderBase {
    private static final String TAG = "OverExposureWarner";
    private static String fragmentShader = "//#declare \nvarying highp vec2 v_texcoord; \nvarying highp vec4 v_overexp_texcoord; \nuniform sampler2D sTexture; \nuniform sampler2D s_texture_overexp; \n// use alpha channel to store lumaince \n// const highp vec4 luminanceVec = vec4(0.2126, 0.7152, 0.0722, 1.0); \nvoid main() \n{\t  \n  \t//get rgb color \n\t\thighp vec4 rgb_color = texture2D(sTexture, v_texcoord); \n//get over exposed texture color \n     highp vec4 over_exposed_tex_color = vec4(texture2D(s_texture_overexp, v_overexp_texcoord.xy).a);  \n     highp float lumaince = 0.2568*rgb_color.r+0.5041*rgb_color.g+0.0979*rgb_color.b+0.0625;  \n     highp float blend_factor = clamp(lumaince*64.0 - v_overexp_texcoord.w, 0.0 ,1.0)*v_overexp_texcoord.z;  \n     highp vec4 ret_color = mix(rgb_color, over_exposed_tex_color, blend_factor); \n    gl_FragColor = vec4(ret_color.xyz, 1.0);  \n }  \n";
    private static String vertexShader = "attribute vec4 aPosition; \nattribute vec4 aTextureCoord; \nuniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\n//x:width in {0, 1}, y:height in {0, 1} z:offset in {0, 1}, w:blend factor \nuniform vec4 overexp_texture_param; \nvarying vec2 v_texcoord; \nvarying vec4 v_overexp_texcoord; \n \t void main() \n\t { \n\t     gl_Position = uMVPMatrix * aPosition; \n\t     v_texcoord = (uSTMatrix * aTextureCoord).xy; \n\t     v_overexp_texcoord = vec4(v_texcoord.x * overexp_texture_param.x + overexp_texture_param.z,\t                               v_texcoord.y * overexp_texture_param.y,\t                               ceil(overexp_texture_param.w), overexp_texture_param.w*64.0); \n\t } \n";
    private int bw_texture_height;
    private int bw_texture_width;
    private boolean isExternal;
    private int mus_texture_overexp;
    private int overexp_texture_param;
    private long start_time_ms;
    private int textureResID = 0;
    private int texture_overexp_ID = -1;

    public static class OverExposureWarnerStatus {
        public boolean enable = false;
        public int resID = 0;

        public OverExposureWarnerStatus(boolean enable2, int resID2) {
            this.enable = enable2;
            this.resID = resID2;
        }
    }

    public OverExposureWarner(boolean isExternal2, int textureResID2) {
        this.isExternal = isExternal2;
        this.textureResID = textureResID2;
    }

    /* access modifiers changed from: protected */
    public String getVertexShader() {
        return vertexShader;
    }

    /* access modifiers changed from: protected */
    public String getFragmentShader() {
        if (this.isExternal) {
            return fragmentShader.replace("//#declare", "#extension GL_OES_EGL_image_external : require").replace("uniform sampler2D sTexture;", "uniform samplerExternalOES sTexture;");
        }
        return fragmentShader;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        DJIVideoDecoderInterface djiVideoDecoderInterface = DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface();
        if (djiVideoDecoderInterface != null && djiVideoDecoderInterface.getContext() != null) {
            Bitmap bw_bmp = BitmapFactory.decodeResource(djiVideoDecoderInterface.getContext().getResources(), this.textureResID);
            this.bw_texture_width = bw_bmp.getWidth();
            this.bw_texture_height = bw_bmp.getHeight();
            ByteBuffer bw_buffer = ByteBuffer.allocate(this.bw_texture_width * this.bw_texture_height * 4);
            bw_bmp.copyPixelsToBuffer(bw_buffer);
            bw_buffer.clear();
            this.texture_overexp_ID = GLUtil.genTexture(3553, true);
            GLUtil.loadTextureData(this.texture_overexp_ID, 3553, bw_buffer, this.bw_texture_width, this.bw_texture_height);
            this.mus_texture_overexp = GLES20.glGetUniformLocation(this.mProgram, "s_texture_overexp");
            checkLocation(this.mus_texture_overexp, "s_texture_overexp");
            this.overexp_texture_param = GLES20.glGetUniformLocation(this.mProgram, "overexp_texture_param");
            checkLocation(this.overexp_texture_param, "overexp_texture_param");
            this.start_time_ms = System.currentTimeMillis();
        }
    }

    /* access modifiers changed from: protected */
    public void preDrawArrays() {
        bindTexture(33985, 3553, this.texture_overexp_ID);
        GLES20.glUniform1i(this.mus_texture_overexp, 1);
        GLES20.glUniform4f(this.overexp_texture_param, ((float) this.viewport_width) / ((float) this.bw_texture_width), ((float) this.viewport_height) / ((float) this.bw_texture_height), ((float) (System.currentTimeMillis() - this.start_time_ms)) / 2000.0f, 0.85f);
    }

    public void release() {
        super.release();
        if (this.texture_overexp_ID != -1) {
            GLUtil.destroyTexture(this.texture_overexp_ID);
        }
    }
}
