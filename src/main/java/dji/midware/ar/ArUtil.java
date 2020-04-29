package dji.midware.ar;

import android.content.res.Resources;
import android.opengl.GLU;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogPaths;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.parser.IParser;
import dji.midware.ar.min3d.parser.Parser;
import dji.midware.ar.min3d.vos.Number3d;
import java.io.InputStream;
import java.lang.reflect.Array;
import javax.microedition.khronos.opengles.GL11;

@EXClassNullAway
public class ArUtil {
    public static Object3dContainer loadModel(Parser.Type type, Resources resources, InputStream inputStream, boolean generateMipMap, float scale) {
        IParser myParser = Parser.createParser(type, resources, inputStream, generateMipMap);
        myParser.parse();
        Object3dContainer model = myParser.getParsedObject();
        model.scale().x = scale;
        model.scale().y = scale;
        model.scale().z = scale;
        return model;
    }

    public static Object3dContainer loadModel(Parser.Type type, Resources resources, InputStream inputStream, boolean generateMipMap, float scale, float positionX, float positionY, float positionZ) {
        Object3dContainer model = loadModel(type, resources, inputStream, generateMipMap, scale);
        model.position().x = positionX;
        model.position().y = positionY;
        model.position().z = positionZ;
        return model;
    }

    public static Object3dContainer loadModel(Parser.Type type, Resources resources, InputStream inputStream, boolean generateMipMap, float scale, float positionX, float positionY, float positionZ, float rotationX, float rotationY, float rotationZ) {
        Object3dContainer model = loadModel(type, resources, inputStream, generateMipMap, scale, positionX, positionY, positionZ);
        model.rotation().x = rotationX;
        model.rotation().y = rotationY;
        model.rotation().z = rotationZ;
        return model;
    }

    public static void quaternions2EularAngles(float[] quaternions, int from, float[] eularAngles) {
        if (quaternions != null && quaternions.length - from >= 4 && eularAngles != null && eularAngles.length >= 3) {
            float w = quaternions[from + 0];
            float x = quaternions[from + 1];
            float y = quaternions[from + 2];
            float z = quaternions[from + 3];
            eularAngles[0] = (float) Math.toDegrees(Math.atan2((double) (((w * x) + (y * z)) * 2.0f), (double) (1.0f - (((x * x) + (y * y)) * 2.0f))));
            eularAngles[1] = (float) Math.toDegrees(Math.asin((double) (((w * y) - (z * x)) * 2.0f)));
            eularAngles[2] = (float) Math.toDegrees(Math.atan2((double) (((w * z) + (x * y)) * 2.0f), (double) (1.0f - (((y * y) + (z * z)) * 2.0f))));
        }
    }

    public static void eularAngles2Dcm(float[] eularAngles, float[][] dcm) {
        if (eularAngles != null && dcm != null) {
            double yaw = Math.toRadians((double) eularAngles[0]);
            double pitch = Math.toRadians((double) eularAngles[1]);
            double roll = Math.toRadians((double) eularAngles[2]);
            float cy = (float) Math.cos(yaw);
            float cp = (float) Math.cos(pitch);
            float cr = (float) Math.cos(roll);
            float sy = (float) Math.sin(yaw);
            float sp = (float) Math.sin(pitch);
            float sr = (float) Math.sin(roll);
            dcm[0][0] = cp * cy;
            dcm[1][0] = cp * sy;
            dcm[2][0] = -sp;
            dcm[0][1] = ((sr * sp) * cy) - (cr * sy);
            dcm[1][1] = (sr * sp * sy) + (cr * cy);
            dcm[2][1] = sr * cp;
            dcm[0][2] = (cr * sp * cy) + (sr * sy);
            dcm[1][2] = ((cr * sp) * sy) - (sr * cy);
            dcm[2][2] = cr * cp;
        }
    }

    public static float[] matrixMultiplyVector(float[][] matrix, float[] vector) {
        if (matrix == null || vector == null || matrix[0].length != vector.length) {
            return null;
        }
        float[] rst = new float[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < vector.length; j++) {
                rst[i] = rst[i] + (matrix[i][j] * vector[j]);
            }
        }
        return rst;
    }

    public static float[] quaternionsMultiply(float[] a, float[] b) {
        if (a == null || b == null || a.length < 4 || b.length < 4) {
            return null;
        }
        return new float[]{(((a[0] * b[0]) - (a[1] * b[1])) - (a[2] * b[2])) - (a[3] * b[3]), (((a[0] * b[1]) + (a[1] * b[0])) + (a[2] * b[3])) - (a[3] * b[2]), ((a[0] * b[2]) - (a[1] * b[3])) + (a[2] * b[0]) + (a[3] * b[1]), (((a[0] * b[3]) + (a[1] * b[2])) - (a[2] * b[1])) + (a[3] * b[0])};
    }

    public static void dcm2Quat(float[][] dcm, float[] quat) {
        if (dcm != null && quat != null && quat.length >= 4) {
            float trace = dcm[0][0] + dcm[1][1] + dcm[2][2];
            if (trace > 0.0f) {
                float s = (float) (Math.sqrt((double) (trace + 1.0f)) * 2.0d);
                quat[0] = 0.25f * s;
                quat[1] = (dcm[2][1] - dcm[1][2]) / s;
                quat[2] = (dcm[0][2] - dcm[2][0]) / s;
                quat[3] = (dcm[1][0] - dcm[0][1]) / s;
            } else if (dcm[0][0] > dcm[1][1] && dcm[0][0] > dcm[2][2]) {
                float s2 = ((float) Math.sqrt((double) (((dcm[0][0] + 1.0f) - dcm[1][1]) - dcm[2][2]))) * 2.0f;
                quat[0] = (dcm[2][1] - dcm[1][2]) / s2;
                quat[1] = 0.25f * s2;
                quat[2] = (dcm[0][1] + dcm[1][0]) / s2;
                quat[3] = (dcm[0][2] + dcm[2][0]) / s2;
            } else if (dcm[1][1] > dcm[2][2]) {
                float s3 = ((float) Math.sqrt((double) (((dcm[1][1] + 1.0f) - dcm[0][0]) - dcm[2][2]))) * 2.0f;
                quat[0] = (dcm[0][2] + dcm[2][0]) / s3;
                quat[1] = (dcm[0][1] + dcm[1][0]) / s3;
                quat[2] = 0.25f * s3;
                quat[0] = (dcm[1][2] - dcm[2][1]) / s3;
            } else {
                float s4 = ((float) Math.sqrt((double) (((dcm[2][2] + 1.0f) - dcm[0][0]) - dcm[1][1]))) * 2.0f;
                quat[0] = (dcm[1][0] + dcm[0][1]) / s4;
                quat[1] = (dcm[0][2] + dcm[2][0]) / s4;
                quat[2] = (dcm[1][2] - dcm[2][1]) / s4;
                quat[3] = 0.25f * s4;
            }
        }
    }

    public static void matrixTranspose(float[][] src, float[][] dst) {
        if (src != null && dst != null) {
            int length = Math.min(src.length, src[0].length);
            for (int i = 0; i < length; i++) {
                for (int j = i; j < length; j++) {
                    dst[i][j] = src[j][i];
                    dst[j][i] = src[i][j];
                }
            }
        }
    }

    public static float[][] matrixMulti(float[][] a, float[][] b) {
        if (a == null || b == null) {
            return null;
        }
        int length = Math.min(Math.min(a.length, a[0].length), Math.min(b.length, b[0].length));
        float[][] rst = (float[][]) Array.newInstance(Float.TYPE, length, length);
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                float sum = 0.0f;
                for (int k = 0; k < length; k++) {
                    sum += a[row][k] * b[k][col];
                }
                rst[row][col] = sum;
            }
        }
        return rst;
    }

    public static double calHeightFromFov(float verFov, float zNear) {
        return 2.0d * Math.tan((double) (verFov / 2.0f)) * ((double) zNear);
    }

    public static float[] worldCoor2ScreenCoor(GL11 gl, float posX, float posY, float posZ) {
        int[] currentViewVectorParams = new int[16];
        float[] currentModelViewMatrix = new float[16];
        float[] currentProjectMatrix = new float[16];
        gl.glGetIntegerv(2978, currentViewVectorParams, 0);
        gl.glGetFloatv(2982, currentModelViewMatrix, 0);
        gl.glGetFloatv(2983, currentProjectMatrix, 0);
        float[] win = new float[3];
        GLU.gluProject(posX, posY, posZ, currentModelViewMatrix, 0, currentProjectMatrix, 0, currentViewVectorParams, 0, win, 0);
        return win;
    }

    public static Number3d screenCoor2WorldCoor(GL11 gl, float screenX, float screenY) {
        float[] obj = new float[4];
        int[] currentViewVectorParams = new int[16];
        float[] currentModelViewMatrix = new float[16];
        float[] currentProjectMatrix = new float[16];
        gl.glGetIntegerv(2978, currentViewVectorParams, 0);
        gl.glGetFloatv(2982, currentModelViewMatrix, 0);
        gl.glGetFloatv(2983, currentProjectMatrix, 0);
        GLU.gluUnProject(screenX, screenY, 0.0f, currentModelViewMatrix, 0, currentProjectMatrix, 0, currentViewVectorParams, 0, obj, 0);
        return new Number3d(obj[0] / obj[3], obj[1] / obj[3], obj[2] / obj[3]);
    }

    public static double calcDistance(Number3d p1, Number3d p2) {
        float dx = p1.x - p2.x;
        float dy = p1.y - p2.y;
        float dz = p1.z - p2.z;
        return Math.sqrt((double) ((dx * dx) + (dy * dy) + (dz * dz)));
    }

    public static void logArMsgToFile(String log) {
        DJILog.saveLog(log, DJILogPaths.LOG_AR);
    }

    public static void logArMsgToFile(String tag, String log) {
        logArMsgToFile(tag + ": " + log);
    }
}
