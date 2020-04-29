package dji.midware.ar.min3d.animation;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.vos.Number3d;

@EXClassNullAway
public class KeyFrame {
    private int[] indices;
    private String name;
    private float[] normals;
    private float[] vertices;

    public KeyFrame(String name2, float[] vertices2) {
        this.name = name2;
        this.vertices = vertices2;
    }

    public KeyFrame(String name2, float[] vertices2, float[] normals2) {
        this(name2, vertices2);
        this.normals = normals2;
    }

    public String getName() {
        return this.name;
    }

    public float[] getVertices() {
        return this.vertices;
    }

    public int[] getIndices() {
        return this.indices;
    }

    public float[] getNormals() {
        return this.normals;
    }

    public void setIndices(int[] indices2) {
        this.indices = indices2;
        float[] compressed = this.vertices;
        this.vertices = new float[(indices2.length * 3)];
        int len = indices2.length;
        int i = 0;
        int vi = 0;
        while (i < len) {
            int ii = indices2[i] * 3;
            int vi2 = vi + 1;
            this.vertices[vi] = compressed[ii];
            int vi3 = vi2 + 1;
            this.vertices[vi2] = compressed[ii + 1];
            this.vertices[vi3] = compressed[ii + 2];
            i++;
            vi = vi3 + 1;
        }
        this.normals = new float[this.vertices.length];
        int vertLen = this.vertices.length;
        int i2 = 0;
        int normalIndex = 0;
        while (i2 < vertLen) {
            Number3d normal = calculateFaceNormal(new Number3d(this.vertices[i2], this.vertices[i2 + 1], this.vertices[i2 + 2]), new Number3d(this.vertices[i2 + 3], this.vertices[i2 + 4], this.vertices[i2 + 5]), new Number3d(this.vertices[i2 + 6], this.vertices[i2 + 7], this.vertices[i2 + 8]));
            int normalIndex2 = normalIndex + 1;
            this.normals[normalIndex] = normal.x;
            int normalIndex3 = normalIndex2 + 1;
            this.normals[normalIndex2] = normal.y;
            int normalIndex4 = normalIndex3 + 1;
            this.normals[normalIndex3] = normal.z;
            int normalIndex5 = normalIndex4 + 1;
            this.normals[normalIndex4] = normal.x;
            int normalIndex6 = normalIndex5 + 1;
            this.normals[normalIndex5] = normal.y;
            int normalIndex7 = normalIndex6 + 1;
            this.normals[normalIndex6] = normal.z;
            int normalIndex8 = normalIndex7 + 1;
            this.normals[normalIndex7] = normal.x;
            int normalIndex9 = normalIndex8 + 1;
            this.normals[normalIndex8] = normal.y;
            this.normals[normalIndex9] = normal.z;
            i2 += 9;
            normalIndex = normalIndex9 + 1;
        }
    }

    public Number3d calculateFaceNormal(Number3d v1, Number3d v2, Number3d v3) {
        Number3d vector1 = Number3d.subtract(v2, v1);
        Number3d vector2 = Number3d.subtract(v3, v1);
        Number3d normal = new Number3d();
        normal.x = (vector1.y * vector2.z) - (vector1.z * vector2.y);
        normal.y = -((vector2.z * vector1.x) - (vector2.x * vector1.z));
        normal.z = (vector1.x * vector2.y) - (vector1.y * vector2.x);
        double normFactor = Math.sqrt((double) ((normal.x * normal.x) + (normal.y * normal.y) + (normal.z * normal.z)));
        normal.x = (float) (((double) normal.x) / normFactor);
        normal.y = (float) (((double) normal.y) / normFactor);
        normal.z = (float) (((double) normal.z) / normFactor);
        return normal;
    }

    public KeyFrame clone() {
        return new KeyFrame(this.name, (float[]) this.vertices.clone(), (float[]) this.normals.clone());
    }
}
