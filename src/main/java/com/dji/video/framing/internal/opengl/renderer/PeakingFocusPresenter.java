package com.dji.video.framing.internal.opengl.renderer;

import android.opengl.GLES20;
import com.dji.video.framing.DJIVideoCodecInnerManager;
import com.dji.video.framing.DJIVideoDecoderInterface;
import com.dji.video.framing.internal.decoder.DJIVideoDecoder;

public class PeakingFocusPresenter extends GLRenderBase {
    public static boolean DEBUG = false;
    public static String TAG = "PeakingFocusPresenter";
    protected static final String VERTEX_SHADER = "uniform mat4 uMVPMatrix;\nuniform mat4 uSTMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 v_texcoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    v_texcoord = (uSTMatrix * aTextureCoord).xy;\n}\n";
    protected static final String fragmentShaser = "#extension GL_OES_EGL_image_external : require\nprecision lowp float;\nvarying lowp vec2 v_texcoord;\n varying lowp vec4 v_overexp_texcoord;\n \n uniform samplerExternalOES sTexture;\n //const lowp vec4 dxdy = vec4(widthReci,heightReci,2.0,0)\n\n //texcord of left right top bottom\n const lowp vec4 range = vec4(0,1.0,0,1.0);\n uniform lowp float widthReci;\n uniform lowp float heightReci;\n uniform lowp float threshold;\n \n const lowp vec4 RED = vec4(1.0,0,0,1.0);\n const lowp vec4 GREEN = vec4(0,1.0,0,1.0);\n const lowp vec4 BLUE = vec4(0,0,1.0,1.0);\n \nlowp vec4 sample(lowp float dx, lowp float dy)\n{\n    lowp vec2 dif = vec2(dx,dy);\n    lowp vec2 texcord = v_texcoord.st+dif;\n    \n    lowp vec4 min_range = step(vec4(range.xz, texcord), vec4(texcord, range.yw));\n    texcord = v_texcoord.st + min_range.x*min_range.y*min_range.z*min_range.w*dif;\n    \n//    texcord = clamp(texcord, vec2(0,0), vec2(1.0,1.0));\n    return texture2D(sTexture, texcord);\n}\n \n lowp vec4 sampleNoClamp(lowp float dx, lowp float dy)\n{\n    lowp vec2 texcord = v_texcoord.st+vec2(dx,dy);\n    return texture2D(sTexture, texcord);\n}\n \nlowp float mag(lowp vec4 p)\n{\n    return length(p.rgb);\n}\n \n void main()\n {\n     lowp float blockWidth = 1.5*widthReci;\n     lowp float blockHeight = 1.5*heightReci;\n     lowp float c1 = mod(v_texcoord.x, 2.0*blockWidth);  \n     c1 = step(blockWidth, c1);  \n     lowp float c2 = mod(v_texcoord.y, 2.0*blockHeight);  \n     c2 = step(blockHeight, c2); \n     lowp float horRange = 0.99;\n     lowp float verRange = 0.97;\n     if (c1*c2>0.0 || v_texcoord.x > (horRange) || v_texcoord.y > (verRange) || v_texcoord.x < (1.0-horRange) || v_texcoord.y < (1.0-verRange)) {\n          gl_FragColor = sampleNoClamp(0.0, 0.0);\n     }\n     else {\n     lowp vec4 leftTop = sampleNoClamp(-widthReci,+heightReci);\n     lowp vec4 midTop = sampleNoClamp(0.0,+heightReci);\n     lowp vec4 rightTop = sampleNoClamp(+widthReci,+heightReci);\n     lowp vec4 leftMid = sampleNoClamp(-widthReci,0.0);\n     lowp vec4 midMid = sampleNoClamp(0.0,0.0);     lowp vec4 rightMid = sampleNoClamp(+widthReci,0.0);\n     lowp vec4 leftBottom = sampleNoClamp(-widthReci,-heightReci);\n     lowp vec4 midBottom = sampleNoClamp(0.0,-heightReci);\n     lowp vec4 rightBottom = sampleNoClamp(+widthReci,-heightReci);\n     gl_FragColor = midMid;\n     lowp float MAG = length((-6.834 * leftBottom+2.142 * midBottom-6.834 * rightBottom+2.142 * leftMid+18.717* midMid+2.142 * rightMid-6.834 * leftTop+2.142 * midTop-6.834 * rightTop).rgb);\n     if(MAG>threshold)         gl_FragColor = vec4(RED.xyz, gl_FragColor.a);\n }\n }\n";
    private int dxdyParam;
    private int height = 0;
    private int heightReciParam;
    protected final boolean isExternal;
    private float rangeBottom = 0.0f;
    private float rangeLeft = 0.0f;
    private int rangeParam;
    private float rangeRight = 1.0f;
    private float rangeTop = 1.0f;
    private float threshold = 2.7f;
    private int thresholdParam;
    private int width = 0;
    private int widthReciParam;

    public PeakingFocusPresenter(boolean isExternal2) {
        this.isExternal = isExternal2;
    }

    /* access modifiers changed from: protected */
    public String getVertexShader() {
        return VERTEX_SHADER;
    }

    /* access modifiers changed from: protected */
    public String getFragmentShader() {
        if (this.isExternal) {
            return fragmentShaser;
        }
        return fragmentShaser.replace("#extension GL_OES_EGL_image_external : require", "//#declare").replace("uniform samplerExternalOES sTexture;", "uniform sampler2D sTexture;");
    }

    public void init() {
        super.init();
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        this.widthReciParam = GLES20.glGetUniformLocation(this.mProgram, "widthReci");
        checkLocation(this.widthReciParam, "widthReci");
        this.heightReciParam = GLES20.glGetUniformLocation(this.mProgram, "heightReci");
        checkLocation(this.widthReciParam, "heightReci");
        this.thresholdParam = GLES20.glGetUniformLocation(this.mProgram, "threshold");
        checkLocation(this.widthReciParam, "threshold");
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width2) {
        this.width = width2;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height2) {
        this.height = height2;
    }

    public float getThreshold() {
        return this.threshold;
    }

    public void setThreshold(float threshold2) {
        this.threshold = threshold2;
    }

    /* access modifiers changed from: protected */
    public void preDrawArrays() {
        DJIVideoDecoderInterface djiVideoDecoderInterface = DJIVideoCodecInnerManager.getInstance().getDJIVideoDecoderInterface();
        DJIVideoDecoder decoder = null;
        if (djiVideoDecoderInterface != null) {
            decoder = djiVideoDecoderInterface.getDJIVideoDecoder();
        }
        if (decoder != null) {
            setWidth(decoder.width);
            setHeight(decoder.height);
        }
        GLES20.glUniform1f(this.widthReciParam, 1.0f / ((float) this.width));
        GLES20.glUniform1f(this.heightReciParam, 1.0f / ((float) this.height));
        GLES20.glUniform1f(this.thresholdParam, this.threshold);
    }
}
