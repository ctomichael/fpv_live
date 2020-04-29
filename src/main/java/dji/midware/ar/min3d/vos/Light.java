package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.interfaces.IDirtyParent;
import java.nio.FloatBuffer;

@EXClassNullAway
public class Light extends AbstractDirtyManaged implements IDirtyParent {
    public Number3dManaged _attenuation = new Number3dManaged(1.0f, 0.0f, 0.0f, this);
    public BooleanManaged _isVisible = new BooleanManaged(true, this);
    public FloatBuffer _positionAndTypeBuffer = Utils.makeFloatBuffer4(0.0f, 0.0f, 0.0f, 0.0f);
    public FloatManaged _spotCutoffAngle = new FloatManaged(180.0f, this);
    public FloatManaged _spotExponent = new FloatManaged(0.0f, this);
    private LightType _type = LightType.DIRECTIONAL;
    public Color4Managed ambient = new Color4Managed(205, 205, 205, 255, this);
    public Color4Managed diffuse = new Color4Managed(0, 0, 0, 255, this);
    public Number3dManaged direction = new Number3dManaged(0.0f, 0.0f, -1.0f, this);
    public Color4Managed emissive = new Color4Managed(0, 0, 0, 255, this);
    public Number3dManaged position = new Number3dManaged(0.0f, 0.0f, 1.0f, this);
    public Color4Managed specular = new Color4Managed(0, 0, 0, 255, this);

    public Light() {
        super(null);
        setDirtyFlag();
    }

    public boolean isVisible() {
        return this._isVisible.get();
    }

    public void isVisible(Boolean $b) {
        this._isVisible.set($b.booleanValue());
    }

    public LightType type() {
        return this._type;
    }

    public void type(LightType $type) {
        this._type = $type;
        this.position.setDirtyFlag();
    }

    public float spotExponent() {
        return this._spotExponent.get();
    }

    public void spotExponent(float $f) {
        if ($f < 0.0f) {
            $f = 0.0f;
        }
        if ($f > 128.0f) {
            $f = 128.0f;
        }
        this._spotExponent.set($f);
    }

    public float spotCutoffAngle() {
        return this._spotCutoffAngle.get();
    }

    public void spotCutoffAngle(Float $f) {
        if ($f.floatValue() < 0.0f) {
            this._spotCutoffAngle.set(0.0f);
        } else if ($f.floatValue() <= 90.0f) {
            this._spotCutoffAngle.set($f.floatValue());
        } else if ($f.floatValue() == 180.0f) {
            this._spotCutoffAngle.set($f.floatValue());
        } else {
            this._spotCutoffAngle.set(90.0f);
        }
    }

    public void spotCutoffAngleNone() {
        this._spotCutoffAngle.set(180.0f);
    }

    public float attenuationConstant() {
        return this._attenuation.getX();
    }

    public void attenuationConstant(float $normalizedValue) {
        this._attenuation.setX($normalizedValue);
        setDirtyFlag();
    }

    public float attenuationLinear() {
        return this._attenuation.getY();
    }

    public void attenuationLinear(float $normalizedValue) {
        this._attenuation.setY($normalizedValue);
        setDirtyFlag();
    }

    public float attenuationQuadratic() {
        return this._attenuation.getZ();
    }

    public void attenuationQuadratic(float $normalizedValue) {
        this._attenuation.setZ($normalizedValue);
        setDirtyFlag();
    }

    public void attenuationSetAll(float $constant, float $linear, float $quadratic) {
        this._attenuation.setAll($constant, $linear, $quadratic);
        setDirtyFlag();
    }

    public void setAllDirty() {
        this.position.setDirtyFlag();
        this.ambient.setDirtyFlag();
        this.diffuse.setDirtyFlag();
        this.specular.setDirtyFlag();
        this.emissive.setDirtyFlag();
        this.direction.setDirtyFlag();
        this._spotCutoffAngle.setDirtyFlag();
        this._spotExponent.setDirtyFlag();
        this._attenuation.setDirtyFlag();
        this._isVisible.setDirtyFlag();
    }

    public void onDirty() {
        setDirtyFlag();
    }

    public void commitPositionAndTypeBuffer() {
        this._positionAndTypeBuffer.position(0);
        this._positionAndTypeBuffer.put(this.position.getX());
        this._positionAndTypeBuffer.put(this.position.getY());
        this._positionAndTypeBuffer.put(this.position.getZ());
        this._positionAndTypeBuffer.put(this._type.glValue());
        this._positionAndTypeBuffer.position(0);
    }
}
