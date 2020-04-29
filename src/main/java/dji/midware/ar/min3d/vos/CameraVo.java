package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class CameraVo {
    public float aspect;
    public float fovy;
    public FrustumManaged frustum = new FrustumManaged(null);
    public Number3d position = new Number3d(0.0f, 0.0f, 5.0f);
    public Number3d target = new Number3d(0.0f, 0.0f, 0.0f);
    public Number3d upAxis = new Number3d(0.0f, 1.0f, 0.0f);
}
