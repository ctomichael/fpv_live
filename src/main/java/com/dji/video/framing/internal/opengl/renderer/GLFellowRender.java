package com.dji.video.framing.internal.opengl.renderer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLFellowRender extends GLIdentityRender {
    private GLRenderBase mainRender;

    public GLFellowRender(boolean isEnternal, GLRenderBase render) {
        super(isEnternal);
        this.mainRender = render;
    }

    /* access modifiers changed from: protected */
    public FloatBuffer getTriangleVerticesPosBuffer() {
        return this.mainRender.getTriangleVerticesPosBuffer();
    }

    /* access modifiers changed from: protected */
    public FloatBuffer getTriangleVerticesUvBuffer() {
        return this.mainRender.getTriangleVerticesUvBuffer();
    }

    /* access modifiers changed from: protected */
    public IntBuffer getTriangleVerticesIndices() {
        return this.mainRender.getTriangleVerticesIndices();
    }

    /* access modifiers changed from: protected */
    public int getTriangleVerticesIndicesLength() {
        return this.mainRender.getTriangleVerticesIndicesLength();
    }
}
