package com.dji.video.framing.internal.opengl.renderer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.DJIVideoDecoderInterface;
import com.dji.video.framing.internal.opengl.GLUtil;
import java.nio.ByteBuffer;

public class GLLutRender extends GLRenderBase {
    public static boolean DEBUG = false;
    public static String TAG = "GLLutRender";
    protected static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying highp vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    protected static final String fragmentShader = ("//#declare \nprecision highp float;\nvarying highp  vec2 vTextureCoord;\n// varying vec2 vTextureCoord2; // TODO: This is not used\n\nuniform sampler2D sTexture;\nuniform sampler2D lookupTable; // lookup texture\n\nvoid main()\n{\n     float intensity = 1.0;\n     vec4 textureColor = texture2D(sTexture, vTextureCoord);\n\n     highp float blueColor = textureColor.b * 63.0;\n\n     highp vec2 quad1;\n     quad1.y = floor(floor(blueColor) / 8.0);\n     quad1.x = floor(blueColor) - (quad1.y * 8.0);\n\n     highp vec2 quad2;\n     quad2.y = floor(ceil(blueColor) / 8.0);\n     quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n\n     highp vec2 texPos1;\n     texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n     texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n\n     highp vec2 texPos2;\n     texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n     texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n     vec4 newColor1 = texture2D(lookupTable, texPos1);\n     vec4 newColor2 = texture2D(lookupTable, texPos2);\n\n     vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n     gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensity);\n" + (DEBUG ? "     if(vTextureCoord.x>=(0.5))\n     {\n           gl_FragColor = texture2D(sTexture, vTextureCoord);     }\n" : "") + "}\n");
    protected final boolean isExternal;
    private int lutId;
    private int musTextureLookupTable = -1;
    private int textureHeight;
    private int textureLookupTableId = -1;
    private int textureWidth;

    public GLLutRender(int lutId2, boolean isExternal2) {
        this.lutId = lutId2;
        this.isExternal = isExternal2;
    }

    /* access modifiers changed from: protected */
    public String getVertexShader() {
        return VERTEX_SHADER;
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
            Bitmap lutBm = BitmapFactory.decodeResource(djiVideoDecoderInterface.getContext().getResources(), this.lutId);
            this.textureWidth = lutBm.getWidth();
            this.textureHeight = lutBm.getHeight();
            ByteBuffer lutBuf = ByteBuffer.allocate(this.textureWidth * this.textureHeight * 4);
            lutBm.copyPixelsToBuffer(lutBuf);
            lutBuf.clear();
            this.textureLookupTableId = GLUtil.genTexture(3553, true);
            GLUtil.loadTextureData(this.textureLookupTableId, 3553, lutBuf, this.textureWidth, this.textureHeight);
            this.musTextureLookupTable = GLES20.glGetUniformLocation(this.mProgram, "lookupTable");
            checkLocation(this.musTextureLookupTable, "lookupTable");
        }
    }

    /* access modifiers changed from: protected */
    public void preDrawArrays() {
        bindTexture(33985, 3553, this.textureLookupTableId);
        GLES20.glUniform1i(this.musTextureLookupTable, 1);
    }

    public void release() {
        super.release();
        if (this.textureLookupTableId != -1) {
            GLUtil.destroyTexture(this.textureLookupTableId);
        }
    }
}
