package dji.midware.ar.min3d.core;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusCameraSettingsMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusFocusInfoMakernoteDirectory;
import com.drew.metadata.exif.makernotes.OlympusImageProcessingMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.animation.AnimationObject3d;
import dji.midware.ar.min3d.vos.FrustumManaged;
import dji.midware.ar.min3d.vos.Light;
import dji.midware.ar.min3d.vos.RenderType;
import dji.midware.ar.min3d.vos.TextureVo;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

@EXClassNullAway
public class Renderer implements GLSurfaceView.Renderer {
    public static final int FRAMERATE_SAMPLEINTERVAL_MS = 1000;
    public static final int NUM_GLLIGHTS = 8;
    private ActivityManager _activityManager;
    private float _fps = 0.0f;
    private long _frameCount = 0;
    private GL10 _gl;
    private boolean _logFps = false;
    private ActivityManager.MemoryInfo _memoryInfo;
    private Scene _scene;
    private boolean _scratchB;
    private FloatBuffer _scratchFloatBuffer;
    private IntBuffer _scratchIntBuffer;
    private float _surfaceAspectRatio;
    private TextureManager _textureManager;
    private long _timeLastSample;

    public Renderer(Scene $scene) {
        this._scene = $scene;
        this._scratchIntBuffer = IntBuffer.allocate(4);
        this._scratchFloatBuffer = FloatBuffer.allocate(4);
        this._textureManager = new TextureManager();
        Shared.textureManager(this._textureManager);
        this._activityManager = (ActivityManager) Shared.context().getSystemService("activity");
        this._memoryInfo = new ActivityManager.MemoryInfo();
    }

    public void onSurfaceCreated(GL10 $gl, EGLConfig eglConfig) {
        Log.i(Min3d.TAG, "Renderer.onSurfaceCreated()");
        RenderCaps.setRenderCaps($gl);
        setGl($gl);
        reset();
        this._scene.init();
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
        Log.i(Min3d.TAG, "Renderer.onSurfaceChanged()");
        setGl(this._gl);
        this._surfaceAspectRatio = ((float) w) / ((float) h);
        this._gl.glViewport(0, 0, w, h);
        this._gl.glMatrixMode(5889);
        this._gl.glLoadIdentity();
        updateViewFrustrum();
    }

    public void onDrawFrame(GL10 gl) {
        this._scene.update();
        drawSetup();
        drawScene();
        if (this._logFps) {
            doFps();
        }
    }

    public float getSurfaceAspectRatio() {
        return this._surfaceAspectRatio;
    }

    public GL10 gl() {
        return this._gl;
    }

    public float fps() {
        return this._fps;
    }

    public long availMem() {
        this._activityManager.getMemoryInfo(this._memoryInfo);
        return this._memoryInfo.availMem;
    }

    /* access modifiers changed from: protected */
    public void drawSetup() {
        if (this._scene.camera().frustum.isDirty()) {
            updateViewFrustrum();
        }
        this._gl.glMatrixMode(5888);
        this._gl.glLoadIdentity();
        GLU.gluLookAt(this._gl, this._scene.camera().position.x, this._scene.camera().position.y, this._scene.camera().position.z, this._scene.camera().target.x, this._scene.camera().target.y, this._scene.camera().target.z, this._scene.camera().upAxis.x, this._scene.camera().upAxis.y, this._scene.camera().upAxis.z);
        if (this._scene.backgroundColor().isDirty()) {
            this._gl.glClearColor(((float) this._scene.backgroundColor().r()) / 255.0f, ((float) this._scene.backgroundColor().g()) / 255.0f, ((float) this._scene.backgroundColor().b()) / 255.0f, ((float) this._scene.backgroundColor().a()) / 255.0f);
            this._scene.backgroundColor().clearDirtyFlag();
        }
        this._gl.glClear(FujifilmMakernoteDirectory.TAG_FACES_DETECTED);
        drawSetupLights();
        this._gl.glEnableClientState(32884);
    }

    /* access modifiers changed from: protected */
    public void drawSetupLights() {
        for (int glIndex = 0; glIndex < 8; glIndex++) {
            if (this._scene.lights().glIndexEnabledDirty()[glIndex]) {
                if (this._scene.lights().glIndexEnabled()[glIndex]) {
                    this._gl.glEnable(glIndex + 16384);
                    Light light = this._scene.lights().getLightByGlIndex(glIndex);
                    if (light != null) {
                        light.setAllDirty();
                    }
                } else {
                    this._gl.glDisable(glIndex + 16384);
                }
                this._scene.lights().glIndexEnabledDirty()[glIndex] = false;
            }
        }
        Light[] lights = this._scene.lights().toArray();
        for (Light light2 : lights) {
            if (light2.isDirty()) {
                int glLightId = this._scene.lights().getGlIndexByLight(light2) + 16384;
                if (light2.position.isDirty()) {
                    light2.commitPositionAndTypeBuffer();
                    this._gl.glLightfv(glLightId, 4611, light2._positionAndTypeBuffer);
                    light2.position.clearDirtyFlag();
                }
                if (light2.ambient.isDirty()) {
                    light2.ambient.commitToFloatBuffer();
                    this._gl.glLightfv(glLightId, OlympusImageProcessingMakernoteDirectory.TagFacesDetected, light2.ambient.floatBuffer());
                    light2.ambient.clearDirtyFlag();
                }
                if (light2.diffuse.isDirty()) {
                    light2.diffuse.commitToFloatBuffer();
                    this._gl.glLightfv(glLightId, 4609, light2.diffuse.floatBuffer());
                    light2.diffuse.clearDirtyFlag();
                }
                if (light2.specular.isDirty()) {
                    light2.specular.commitToFloatBuffer();
                    this._gl.glLightfv(glLightId, OlympusImageProcessingMakernoteDirectory.TagMaxFaces, light2.specular.floatBuffer());
                    light2.specular.clearDirtyFlag();
                }
                if (light2.emissive.isDirty()) {
                    light2.emissive.commitToFloatBuffer();
                    this._gl.glLightfv(glLightId, OlympusFocusInfoMakernoteDirectory.TagImageStabilization, light2.emissive.floatBuffer());
                    light2.emissive.clearDirtyFlag();
                }
                if (light2.direction.isDirty()) {
                    light2.direction.commitToFloatBuffer();
                    this._gl.glLightfv(glLightId, OlympusFocusInfoMakernoteDirectory.TagExternalFlashBounce, light2.direction.floatBuffer());
                    light2.direction.clearDirtyFlag();
                }
                if (light2._spotCutoffAngle.isDirty()) {
                    this._gl.glLightf(glLightId, 4614, light2._spotCutoffAngle.get());
                }
                if (light2._spotExponent.isDirty()) {
                    this._gl.glLightf(glLightId, OlympusFocusInfoMakernoteDirectory.TagExternalFlashZoom, light2._spotExponent.get());
                }
                if (light2._isVisible.isDirty()) {
                    if (light2.isVisible()) {
                        this._gl.glEnable(glLightId);
                    } else {
                        this._gl.glDisable(glLightId);
                    }
                    light2._isVisible.clearDirtyFlag();
                }
                if (light2._attenuation.isDirty()) {
                    this._gl.glLightf(glLightId, OlympusImageProcessingMakernoteDirectory.TagFaceDetectFrameCrop, light2._attenuation.getX());
                    this._gl.glLightf(glLightId, OlympusFocusInfoMakernoteDirectory.TagInternalFlash, light2._attenuation.getY());
                    this._gl.glLightf(glLightId, OlympusFocusInfoMakernoteDirectory.TagManualFlash, light2._attenuation.getZ());
                }
                light2.clearDirtyFlag();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawScene() {
        if (this._scene.fogEnabled()) {
            this._gl.glFogf(2917, (float) this._scene.fogType().glValue());
            this._gl.glFogf(2915, this._scene.fogNear());
            this._gl.glFogf(2916, this._scene.fogFar());
            this._gl.glFogfv(2918, this._scene.fogColor().toFloatBuffer());
            this._gl.glEnable(2912);
        } else {
            this._gl.glDisable(2912);
        }
        for (int i = 0; i < this._scene.children().size(); i++) {
            Object3d o = this._scene.children().get(i);
            if (o.animationEnabled()) {
                ((AnimationObject3d) o).update();
            }
            drawObject(o);
        }
    }

    /* access modifiers changed from: protected */
    public void drawObject(Object3d $o) {
        int pos;
        int len;
        if ($o.isVisible()) {
            if (!$o.hasNormals() || !$o.normalsEnabled()) {
                this._gl.glDisableClientState(32885);
            } else {
                $o.vertices().normals().buffer().position(0);
                this._gl.glNormalPointer(FujifilmMakernoteDirectory.TAG_MAX_APERTURE_AT_MIN_FOCAL, 0, $o.vertices().normals().buffer());
                this._gl.glEnableClientState(32885);
            }
            if (this._scene.lightingEnabled() && $o.hasNormals() && $o.normalsEnabled() && $o.lightingEnabled()) {
                this._gl.glEnable(2896);
            } else {
                this._gl.glDisable(2896);
            }
            this._gl.glGetIntegerv(2900, this._scratchIntBuffer);
            if ($o.shadeModel().glConstant() != this._scratchIntBuffer.get(0)) {
                this._gl.glShadeModel($o.shadeModel().glConstant());
            }
            if (!$o.hasVertexColors() || !$o.vertexColorsEnabled()) {
                this._gl.glColor4f(((float) $o.defaultColor().r) / 255.0f, ((float) $o.defaultColor().g) / 255.0f, ((float) $o.defaultColor().b) / 255.0f, ((float) $o.defaultColor().a) / 255.0f);
                this._gl.glDisableClientState(32886);
            } else {
                $o.vertices().colors().buffer().position(0);
                this._gl.glColorPointer(4, FujifilmMakernoteDirectory.TAG_FILM_MODE, 0, $o.vertices().colors().buffer());
                this._gl.glEnableClientState(32886);
            }
            this._gl.glGetIntegerv(2903, this._scratchIntBuffer);
            this._scratchB = this._scratchIntBuffer.get(0) != 0;
            if ($o.colorMaterialEnabled() != this._scratchB) {
                if ($o.colorMaterialEnabled()) {
                    this._gl.glEnable(2903);
                } else {
                    this._gl.glDisable(2903);
                }
            }
            if ($o.renderType() == RenderType.POINTS) {
                if ($o.pointSmoothing()) {
                    this._gl.glEnable(2832);
                } else {
                    this._gl.glDisable(2832);
                }
                this._gl.glPointSize($o.pointSize());
            }
            if ($o.renderType() == RenderType.LINES || $o.renderType() == RenderType.LINE_STRIP || $o.renderType() == RenderType.LINE_LOOP) {
                if ($o.lineSmoothing()) {
                    this._gl.glEnable(2848);
                } else {
                    this._gl.glDisable(2848);
                }
                this._gl.glLineWidth($o.lineWidth());
            }
            if ($o.doubleSidedEnabled()) {
                this._gl.glDisable(2884);
            } else {
                this._gl.glEnable(2884);
            }
            drawObject_textures($o);
            this._gl.glPushMatrix();
            this._gl.glTranslatef($o.position().x, $o.position().y, $o.position().z);
            this._gl.glRotatef($o.rotation().z, 0.0f, 0.0f, 1.0f);
            this._gl.glRotatef($o.rotation().y, 0.0f, 1.0f, 0.0f);
            this._gl.glRotatef($o.rotation().x, 1.0f, 0.0f, 0.0f);
            this._gl.glScalef($o.scale().x, $o.scale().y, $o.scale().z);
            $o.vertices().points().buffer().position(0);
            this._gl.glVertexPointer(3, FujifilmMakernoteDirectory.TAG_MAX_APERTURE_AT_MIN_FOCAL, 0, $o.vertices().points().buffer());
            if (!$o.ignoreFaces()) {
                if (!$o.faces().renderSubsetEnabled()) {
                    pos = 0;
                    len = $o.faces().size();
                } else {
                    pos = $o.faces().renderSubsetStartIndex() * 3;
                    len = $o.faces().renderSubsetLength();
                }
                $o.faces().buffer().position(pos);
                this._gl.glDrawElements($o.renderType().glValue(), len * 3, FujifilmMakernoteDirectory.TAG_DEVELOPMENT_DYNAMIC_RANGE, $o.faces().buffer());
            } else {
                this._gl.glDrawArrays($o.renderType().glValue(), 0, $o.vertices().size());
            }
            if ($o instanceof Object3dContainer) {
                Object3dContainer container = (Object3dContainer) $o;
                for (int i = 0; i < container.children().size(); i++) {
                    drawObject(container.children().get(i));
                }
            }
            this._gl.glPopMatrix();
        }
    }

    private void drawObject_textures(Object3d $o) {
        int i = 0;
        while (i < RenderCaps.maxTextureUnits()) {
            this._gl.glActiveTexture(33984 + i);
            this._gl.glClientActiveTexture(33984 + i);
            if (!$o.hasUvs() || !$o.texturesEnabled()) {
                this._gl.glBindTexture(3553, 0);
                this._gl.glDisable(3553);
                this._gl.glDisableClientState(32888);
            } else {
                $o.vertices().uvs().buffer().position(0);
                this._gl.glTexCoordPointer(2, FujifilmMakernoteDirectory.TAG_MAX_APERTURE_AT_MIN_FOCAL, 0, $o.vertices().uvs().buffer());
                TextureVo textureVo = i < $o.textures().size() ? $o.textures().get(i) : null;
                if (textureVo != null) {
                    this._gl.glBindTexture(3553, this._textureManager.getGlTextureId(textureVo.textureId));
                    this._gl.glEnable(3553);
                    this._gl.glEnableClientState(32888);
                    this._gl.glTexParameterf(3553, 10241, (float) (this._textureManager.hasMipMap(textureVo.textureId) ? 9985 : 9728));
                    this._gl.glTexParameterf(3553, 10240, 9729.0f);
                    for (int j = 0; j < textureVo.textureEnvs.size(); j++) {
                        this._gl.glTexEnvx(8960, textureVo.textureEnvs.get(j).pname, textureVo.textureEnvs.get(j).param);
                    }
                    this._gl.glTexParameterx(3553, 10242, textureVo.repeatU ? 10497 : 33071);
                    this._gl.glTexParameterx(3553, 10243, textureVo.repeatV ? 10497 : 33071);
                    if (textureVo.offsetU != 0.0f || textureVo.offsetV != 0.0f) {
                        this._gl.glMatrixMode(5890);
                        this._gl.glLoadIdentity();
                        this._gl.glTranslatef(textureVo.offsetU, textureVo.offsetV, 0.0f);
                        this._gl.glMatrixMode(5888);
                    }
                } else {
                    this._gl.glBindTexture(3553, 0);
                    this._gl.glDisable(3553);
                    this._gl.glDisableClientState(32888);
                }
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    public int uploadTextureAndReturnId(Bitmap $bitmap, boolean $generateMipMap) {
        int[] a = new int[1];
        this._gl.glGenTextures(1, a, 0);
        int glTextureId = a[0];
        this._gl.glBindTexture(3553, glTextureId);
        if (!$generateMipMap || !(this._gl instanceof GL11)) {
            this._gl.glTexParameterf(3553, 33169, 0.0f);
        } else {
            this._gl.glTexParameterf(3553, 33169, 1.0f);
        }
        GLUtils.texImage2D(3553, 0, $bitmap, 0);
        return glTextureId;
    }

    /* access modifiers changed from: package-private */
    public void deleteTexture(int $glTextureId) {
        this._gl.glDeleteTextures(1, new int[]{$glTextureId}, 0);
    }

    /* access modifiers changed from: protected */
    public void updateViewFrustrum() {
        FrustumManaged vf = this._scene.camera().frustum;
        float n = vf.shortSideLength() / 2.0f;
        float lt = vf.horizontalCenter() - (this._surfaceAspectRatio * n);
        float rt = vf.horizontalCenter() + (this._surfaceAspectRatio * n);
        float btm = vf.verticalCenter() - (n * 1.0f);
        float top = vf.verticalCenter() + (n * 1.0f);
        if (this._surfaceAspectRatio > 1.0f) {
            lt *= 1.0f / this._surfaceAspectRatio;
            rt *= 1.0f / this._surfaceAspectRatio;
            btm *= 1.0f / this._surfaceAspectRatio;
            top *= 1.0f / this._surfaceAspectRatio;
        }
        this._gl.glMatrixMode(5889);
        this._gl.glLoadIdentity();
        if (this._scene.camera().fovy <= 0.0f || this._scene.camera().aspect <= 0.0f) {
            this._gl.glFrustumf(lt, rt, btm, top, vf.zNear(), vf.zFar());
        } else {
            GLU.gluPerspective(this._gl, this._scene.camera().fovy, this._scene.camera().aspect, vf.zNear(), vf.zFar());
        }
        vf.clearDirtyFlag();
    }

    public void logFps(boolean $b) {
        this._logFps = $b;
        if (this._logFps) {
            this._timeLastSample = System.currentTimeMillis();
            this._frameCount = 0;
        }
    }

    private void setGl(GL10 $gl) {
        this._gl = $gl;
    }

    private void doFps() {
        this._frameCount++;
        long now = System.currentTimeMillis();
        long delta = now - this._timeLastSample;
        if (delta >= 1000) {
            this._fps = ((float) this._frameCount) / (((float) delta) / 1000.0f);
            this._activityManager.getMemoryInfo(this._memoryInfo);
            Log.v(Min3d.TAG, "FPS: " + Math.round(this._fps) + ", availMem: " + Math.round((float) (this._memoryInfo.availMem / 1048576)) + "MB");
            this._timeLastSample = now;
            this._frameCount = 0;
        }
    }

    private void reset() {
        Shared.textureManager().reset();
        this._gl.glEnable(32925);
        this._gl.glClearDepthf(1.0f);
        this._gl.glDepthFunc(513);
        this._gl.glDepthRangef(0.0f, 1.0f);
        this._gl.glDepthMask(true);
        this._gl.glEnable(3042);
        this._gl.glBlendFunc(770, 771);
        this._gl.glTexParameterf(3553, 10241, 9728.0f);
        this._gl.glTexParameterf(3553, 10240, 9729.0f);
        this._gl.glFrontFace(OlympusCameraSettingsMakernoteDirectory.TagManometerReading);
        this._gl.glCullFace(1029);
        this._gl.glEnable(2884);
        for (int i = 16384; i < 16392; i++) {
            this._gl.glDisable(i);
        }
    }
}
