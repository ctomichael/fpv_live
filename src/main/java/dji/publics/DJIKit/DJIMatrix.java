package dji.publics.DJIKit;

import android.graphics.Camera;
import android.graphics.Matrix;

public class DJIMatrix {
    private float centerX;
    private float centerY;
    private float degX;
    private float degY;
    private float degZ;
    private float deltaX;
    private float deltaY;
    private float deltaZ;
    private boolean isCenter = false;
    private boolean isRotate = false;
    private boolean isScale = false;
    private boolean isTranslate = false;
    private Camera mCamera = new Camera();
    private Matrix mMatrix = new Matrix();
    private float scaleX;
    private float scaleY;

    public void setTranslate(float x, float y, float z) {
        this.deltaX = x;
        this.deltaY = y;
        this.deltaZ = z;
        this.isTranslate = true;
    }

    public void setRotate(float x, float y, float z) {
        this.degX = x;
        this.degY = y;
        this.degZ = z;
        this.isRotate = true;
    }

    public void setScale(float x, float y) {
        this.scaleX = x;
        this.scaleY = y;
        this.isScale = true;
    }

    public void setCenter(float x, float y) {
        this.centerX = x;
        this.centerY = y;
        this.isCenter = true;
    }

    public Matrix get() {
        this.mCamera.save();
        if (this.isTranslate) {
            translate();
        }
        if (this.isRotate) {
            rotate();
        }
        this.mCamera.getMatrix(this.mMatrix);
        this.mCamera.restore();
        if (this.isCenter) {
            this.mMatrix.preTranslate(-this.centerX, -this.centerY);
            this.mMatrix.postTranslate(this.centerX, this.centerY);
        }
        if (this.isScale) {
            this.mMatrix.preScale(this.scaleX, this.scaleY);
        }
        return this.mMatrix;
    }

    public void reset() {
        this.deltaX = 0.0f;
        this.deltaY = 0.0f;
        this.deltaZ = 0.0f;
        this.isTranslate = false;
        this.degX = 0.0f;
        this.degY = 0.0f;
        this.degZ = 0.0f;
        this.isRotate = false;
        this.centerX = 0.0f;
        this.centerY = 0.0f;
        this.isCenter = false;
        this.scaleX = 0.0f;
        this.scaleY = 0.0f;
        this.isScale = false;
    }

    private void translate() {
        this.mCamera.translate(this.deltaX, this.deltaY, this.deltaZ);
    }

    private void rotate() {
        this.mCamera.rotateX(this.degX);
        this.mCamera.rotateY(this.degY);
        this.mCamera.rotateY(this.degZ);
    }
}
