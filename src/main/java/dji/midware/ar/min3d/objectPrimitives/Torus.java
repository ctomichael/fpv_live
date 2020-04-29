package dji.midware.ar.min3d.objectPrimitives;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;
import dji.midware.ar.min3d.vos.Vertex3d;

@EXClassNullAway
public class Torus extends Object3dContainer {
    private final int MIN_SEGMENTSH;
    private final int MIN_SEGMENTSW;
    private float largeRadius;
    private int segmentsH;
    private int segmentsW;
    private float smallRadius;

    public Torus() {
        this(2.0f, 1.0f, 12, 8, new Color4());
    }

    public Torus(Color4 color) {
        this(2.0f, 1.0f, 12, 8, color);
    }

    public Torus(float largeRadius2, float smallRadius2, int segmentsW2, int segmentsH2) {
        this(largeRadius2, smallRadius2, segmentsW2, segmentsH2, new Color4());
    }

    public Torus(float largeRadius2, float smallRadius2, int segmentsW2, int segmentsH2, Color4 color) {
        super(segmentsW2 * segmentsH2 * 2 * 3, segmentsW2 * segmentsH2 * 2);
        this.MIN_SEGMENTSW = 3;
        this.MIN_SEGMENTSH = 2;
        this.largeRadius = largeRadius2;
        this.smallRadius = smallRadius2;
        this.segmentsW = Math.max(3, segmentsW2);
        this.segmentsH = Math.max(2, segmentsH2);
        defaultColor(color);
        build();
    }

    private void build() {
        float r1 = this.largeRadius;
        float r2 = this.smallRadius;
        int steps1 = this.segmentsW;
        int steps2 = this.segmentsH;
        float step1r = (float) (6.283185307179586d / ((double) steps1));
        float step2r = (float) (6.283185307179586d / ((double) steps2));
        float a1a = 0.0f;
        float a1b = step1r;
        int vcount = 0;
        float s = 0.0f;
        while (s < ((float) steps1)) {
            float a2a = 0.0f;
            float a2b = step2r;
            float s2 = 0.0f;
            while (s2 < ((float) steps2)) {
                Vertex3d v0 = getVertex(a1a, r1, a2a, r2);
                Vertex3d v1 = getVertex(a1b, r1, a2a, r2);
                Vertex3d v2 = getVertex(a1b, r1, a2b, r2);
                Vertex3d v3 = getVertex(a1a, r1, a2b, r2);
                float ux1 = s / ((float) steps1);
                float ux0 = (1.0f + s) / ((float) steps1);
                float uy0 = s2 / ((float) steps2);
                float uy1 = (1.0f + s2) / ((float) steps2);
                vertices().addVertex(v0.position, new Uv(1.0f - ux1, uy0), v0.normal, defaultColor());
                vertices().addVertex(v1.position, new Uv(1.0f - ux0, uy0), v1.normal, defaultColor());
                vertices().addVertex(v2.position, new Uv(1.0f - ux0, uy1), v2.normal, defaultColor());
                vertices().addVertex(v3.position, new Uv(1.0f - ux1, uy1), v3.normal, defaultColor());
                faces().add(vcount, vcount + 1, vcount + 2);
                faces().add(vcount, vcount + 2, vcount + 3);
                vcount += 4;
                s2 += 1.0f;
                a2a = a2b;
                a2b += step2r;
            }
            s += 1.0f;
            a1a = a1b;
            a1b += step1r;
        }
    }

    private Vertex3d getVertex(float a1, float r1, float a2, float r2) {
        Vertex3d vertex = new Vertex3d();
        vertex.normal = new Number3d();
        float ca1 = (float) Math.cos((double) a1);
        float sa1 = (float) Math.sin((double) a1);
        float ca2 = (float) Math.cos((double) a2);
        float sa2 = (float) Math.sin((double) a2);
        vertex.normal.x = ca2 * ca1;
        vertex.normal.y = sa2;
        vertex.normal.z = (-ca2) * sa1;
        vertex.position.x = (vertex.normal.x * r2) + (r1 * ca1);
        vertex.position.y = vertex.normal.y * r2;
        Number3d number3d = vertex.position;
        number3d.z = (vertex.normal.z * r2) + ((-r1) * sa1);
        return vertex;
    }
}
