package dji.publics.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class DJI3dAnimation extends Animation {
    private Camera camera;
    private float mCenterX;
    private float mCenterY;
    private float mFromXDegrees;
    private float mFromXDistances;
    private float mFromYDegrees;
    private float mFromYDistances;
    private float mFromZDegrees;
    private float mFromZDistances;
    private float mToXDegrees;
    private float mToXDistances;
    private float mToYDegrees;
    private float mToYDistances;
    private float mToZDegrees;
    private float mToZDistances;
    private Matrix matrix;

    public DJI3dAnimation(float centerX, float centerY, float fromXDegrees, float toXDegrees, float fromYDegrees, float toYDegrees, float fromZDegrees, float toZDegrees, float fromXDistances, float toXDistances, float fromYDistances, float toYDistances, float fromZDistances, float toZDistances) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        this.mFromXDegrees = fromXDegrees;
        this.mToXDegrees = toXDegrees;
        this.mFromYDegrees = fromYDegrees;
        this.mToYDegrees = toYDegrees;
        this.mFromZDegrees = fromZDegrees;
        this.mToZDegrees = toZDegrees;
        this.mFromXDistances = fromXDistances;
        this.mToXDistances = toXDistances;
        this.mFromYDistances = fromYDistances;
        this.mToYDistances = toYDistances;
        this.mFromZDistances = fromZDistances;
        this.mToZDistances = toZDistances;
    }

    public DJI3dAnimation(float centerX, float centerY, float fromXDegrees, float toXDegrees, float fromYDegrees, float toYDegrees, float fromZDegrees, float toZDegrees) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        this.mFromXDegrees = fromXDegrees;
        this.mToXDegrees = toXDegrees;
        this.mFromYDegrees = fromYDegrees;
        this.mToYDegrees = toYDegrees;
        this.mFromZDegrees = fromZDegrees;
        this.mToZDegrees = toZDegrees;
    }

    public DJI3dAnimation(float centerX, float centerY, float fromXDegrees, float toXDegrees) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        this.mFromXDegrees = fromXDegrees;
        this.mToXDegrees = toXDegrees;
    }

    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        this.camera = new Camera();
    }

    /* access modifiers changed from: protected */
    public void applyTransformation(float interpolatedTime, Transformation t) {
        float degreesX = this.mFromXDegrees + ((this.mToXDegrees - this.mFromXDegrees) * interpolatedTime);
        float degreesY = this.mFromYDegrees + ((this.mToYDegrees - this.mFromYDegrees) * interpolatedTime);
        float degreesZ = this.mFromZDegrees + ((this.mToZDegrees - this.mFromZDegrees) * interpolatedTime);
        float distancesX = this.mFromXDistances + ((this.mToXDistances - this.mFromXDistances) * interpolatedTime);
        float distancesY = this.mFromYDistances + ((this.mToYDistances - this.mFromYDistances) * interpolatedTime);
        float distancesZ = this.mFromZDistances + ((this.mToZDistances - this.mFromZDistances) * interpolatedTime);
        this.matrix = t.getMatrix();
        this.camera.save();
        this.camera.translate(distancesX, distancesY, distancesZ);
        this.camera.rotateX(degreesX);
        this.camera.rotateY(degreesY);
        this.camera.rotateZ(degreesZ);
        this.camera.getMatrix(this.matrix);
        this.camera.restore();
        this.matrix.preTranslate(-this.mCenterX, -this.mCenterY);
        this.matrix.postTranslate(this.mCenterX, this.mCenterY);
    }
}
