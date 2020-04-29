package dji.midware.ar;

import android.opengl.Matrix;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ArMatrixUtils {
    public static float[] rotateM(float pitch, float roll, float yaw) {
        float[] matrix = new float[16];
        Matrix.setIdentityM(matrix, 0);
        float[] matrixResult = new float[16];
        float[] matrixp = new float[16];
        Matrix.setIdentityM(matrixp, 0);
        float[] matrixr = new float[16];
        Matrix.setIdentityM(matrixr, 0);
        float[] matrixy = new float[16];
        Matrix.setIdentityM(matrixy, 0);
        Matrix.rotateM(matrixp, 0, pitch, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(matrixr, 0, roll, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(matrixy, 0, yaw, 0.0f, 0.0f, -1.0f);
        Matrix.multiplyMM(matrix, 0, matrixr, 0, matrixy, 0);
        Matrix.multiplyMM(matrixResult, 0, matrix, 0, matrixp, 0);
        return matrixResult;
    }

    private static float[] getCameraTranslateM(float x, float y, float z) {
        float[] matrix = new float[16];
        float[] matrixIdentity = new float[16];
        Matrix.setIdentityM(matrixIdentity, 0);
        Matrix.translateM(matrix, 0, matrixIdentity, 0, x, y, z);
        return matrix;
    }

    public static float[] getViewM(float[] rMatrix, float x, float y, float z) {
        float[] matrix = new float[16];
        Matrix.multiplyMM(matrix, 0, getCameraTranslateM(x, y, z), 0, rMatrix, 0);
        return matrix;
    }

    public static float[] getVectorTarget(float[] viewMatrix) {
        float[] resultVec = new float[4];
        Matrix.multiplyMV(resultVec, 0, viewMatrix, 0, new float[]{0.0f, 1.0f, 0.0f, 1.0f}, 0);
        return resultVec;
    }

    public static float[] getVectorUp(float[] rMatrix) {
        float[] resultVec = new float[4];
        Matrix.multiplyMV(resultVec, 0, rMatrix, 0, new float[]{0.0f, 0.0f, 1.0f, 1.0f}, 0);
        return resultVec;
    }
}
