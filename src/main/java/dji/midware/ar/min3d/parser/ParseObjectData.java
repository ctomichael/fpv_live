package dji.midware.ar.min3d.parser;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.animation.AnimationObject3d;
import dji.midware.ar.min3d.animation.KeyFrame;
import dji.midware.ar.min3d.core.Object3d;
import dji.midware.ar.min3d.parser.AParser;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Face;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;
import java.util.ArrayList;
import java.util.HashMap;

@EXClassNullAway
public class ParseObjectData {
    protected ArrayList<ParseObjectFace> faces;
    public String name;
    protected ArrayList<Number3d> normals;
    protected int numFaces;
    protected ArrayList<Uv> texCoords;
    protected ArrayList<Number3d> vertices;

    public ParseObjectData() {
        this.numFaces = 0;
        this.vertices = new ArrayList<>();
        this.texCoords = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.name = "";
        this.faces = new ArrayList<>();
    }

    public ParseObjectData(ArrayList<Number3d> vertices2, ArrayList<Uv> texCoords2, ArrayList<Number3d> normals2) {
        this.numFaces = 0;
        this.vertices = vertices2;
        this.texCoords = texCoords2;
        this.normals = normals2;
        this.name = "";
        this.faces = new ArrayList<>();
    }

    public AnimationObject3d getParsedObject(AParser.TextureAtlas textureAtlas, HashMap<String, AParser.Material> materialMap, KeyFrame[] frames) {
        AnimationObject3d obj = new AnimationObject3d(this.numFaces * 3, this.numFaces, frames.length);
        obj.name(this.name);
        obj.setFrames(frames);
        parseObject(obj, materialMap, textureAtlas);
        return obj;
    }

    public Object3d getParsedObject(HashMap<String, AParser.Material> materialMap, AParser.TextureAtlas textureAtlas) {
        Object3d obj = new Object3d(this.numFaces * 3, this.numFaces);
        obj.name(this.name);
        parseObject(obj, materialMap, textureAtlas);
        return obj;
    }

    private void parseObject(Object3d obj, HashMap<String, AParser.Material> materialMap, AParser.TextureAtlas textureAtlas) {
        Number3d newNormal;
        int numFaces2 = this.faces.size();
        int faceIndex = 0;
        boolean hasBitmaps = textureAtlas.hasBitmaps();
        for (int i = 0; i < numFaces2; i++) {
            ParseObjectFace face = this.faces.get(i);
            AParser.BitmapAsset ba = textureAtlas.getBitmapAssetByName(face.materialKey);
            for (int j = 0; j < face.faceLength; j++) {
                Number3d newVertex = this.vertices.get(face.v[j]);
                Uv newUv = face.hasuv ? this.texCoords.get(face.uv[j]).clone() : new Uv();
                if (face.hasn) {
                    newNormal = this.normals.get(face.n[j]);
                } else {
                    newNormal = new Number3d();
                }
                AParser.Material material = materialMap.get(face.materialKey);
                Color4 newColor = new Color4(255, 255, 0, 255);
                if (!(material == null || material.diffuseColor == null)) {
                    newColor.r = material.diffuseColor.r;
                    newColor.g = material.diffuseColor.g;
                    newColor.b = material.diffuseColor.b;
                    newColor.a = material.diffuseColor.a;
                }
                if (hasBitmaps && ba != null) {
                    newUv.u = ba.uOffset + (newUv.u * ba.uScale);
                    newUv.v = (ba.vOffset + ((newUv.v + 1.0f) * ba.vScale)) - 1.0f;
                }
                obj.vertices().addVertex(newVertex, newUv, newNormal, newColor);
            }
            if (face.faceLength == 3) {
                obj.faces().add(new Face(faceIndex, faceIndex + 1, faceIndex + 2));
            } else if (face.faceLength == 4) {
                obj.faces().add(new Face(faceIndex, faceIndex + 1, faceIndex + 3));
                obj.faces().add(new Face(faceIndex + 1, faceIndex + 2, faceIndex + 3));
            }
            faceIndex += face.faceLength;
        }
        if (hasBitmaps) {
            obj.textures().addById(textureAtlas.getId());
        }
        cleanup();
    }

    public void calculateFaceNormal(ParseObjectFace face) {
        Number3d v1 = this.vertices.get(face.v[0]);
        Number3d vector1 = Number3d.subtract(this.vertices.get(face.v[1]), v1);
        Number3d vector2 = Number3d.subtract(this.vertices.get(face.v[2]), v1);
        Number3d normal = new Number3d();
        normal.x = (vector1.y * vector2.z) - (vector1.z * vector2.y);
        normal.y = -((vector2.z * vector1.x) - (vector2.x * vector1.z));
        normal.z = (vector1.x * vector2.y) - (vector1.y * vector2.x);
        double normFactor = Math.sqrt((double) ((normal.x * normal.x) + (normal.y * normal.y) + (normal.z * normal.z)));
        normal.x = (float) (((double) normal.x) / normFactor);
        normal.y = (float) (((double) normal.y) / normFactor);
        normal.z = (float) (((double) normal.z) / normFactor);
        this.normals.add(normal);
        int index = this.normals.size() - 1;
        face.n = new int[3];
        face.n[0] = index;
        face.n[1] = index;
        face.n[2] = index;
        face.hasn = true;
    }

    /* access modifiers changed from: protected */
    public void cleanup() {
        this.faces.clear();
    }
}
