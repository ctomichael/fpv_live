package dji.midware.ar.min3d.parser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.animation.AnimationObject3d;
import dji.midware.ar.min3d.animation.KeyFrame;
import dji.midware.ar.min3d.parser.AParser;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@EXClassNullAway
public class MD2Parser extends AParser implements IParser {
    private String currentTextureName;
    private KeyFrame[] frames;
    private MD2Header header;

    public MD2Parser(Resources resources, String resourceID, boolean generateMipMap) {
        super(resources, resourceID, Boolean.valueOf(generateMipMap));
    }

    public MD2Parser(Resources resources, InputStream inputStream, boolean generateMipMap) {
        super(resources, inputStream, generateMipMap);
    }

    public AnimationObject3d getParsedAnimationObject() {
        Log.d(Min3d.TAG, "Start object creation");
        Bitmap texture = null;
        if (this.textureAtlas.hasBitmaps()) {
            this.textureAtlas.generate();
            texture = this.textureAtlas.getBitmap();
            Shared.textureManager().addTextureId(texture, this.textureAtlas.getId(), this.generateMipMap);
        }
        Log.d(Min3d.TAG, "Creating object " + this.co.name);
        AnimationObject3d animObj = this.co.getParsedObject(this.textureAtlas, this.materialMap, this.frames);
        if (this.textureAtlas.hasBitmaps() && texture != null) {
            texture.recycle();
        }
        Log.d(Min3d.TAG, "Object creation finished");
        super.cleanup();
        return animObj;
    }

    public void parse() {
        InputStream fileIn = this.resources.openRawResource(this.resources.getIdentifier(this.resourceID, null, null));
        BufferedInputStream stream = new BufferedInputStream(fileIn);
        this.co = new ParseObjectData();
        this.header = new MD2Header();
        Log.d(Min3d.TAG, "Start parsing MD2 file");
        try {
            this.header.parse(stream);
            this.frames = new KeyFrame[this.header.numFrames];
            byte[] bytes = new byte[(this.header.offsetEnd - 68)];
            stream.read(bytes);
            getMaterials(stream, bytes);
            getTexCoords(stream, bytes);
            getFrames(stream, bytes);
            getTriangles(stream, bytes);
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
            if (fileIn != null) {
                fileIn.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return;
                }
            }
            if (fileIn != null) {
                fileIn.close();
            }
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                    throw th;
                }
            }
            if (fileIn != null) {
                fileIn.close();
            }
            throw th;
        }
    }

    private void getMaterials(BufferedInputStream stream, byte[] bytes) throws IOException {
        LittleEndianDataInputStream is = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes, this.header.offsetSkins - 68, bytes.length - this.header.offsetSkins));
        for (int i = 0; i < this.header.numSkins; i++) {
            String skinPath = is.readString(64);
            StringBuffer texture = new StringBuffer(this.packageID);
            texture.append(":drawable/");
            StringBuffer textureName = new StringBuffer(skinPath.substring(skinPath.lastIndexOf(IMemberProtocol.PARAM_SEPERATOR) + 1, skinPath.length()).toLowerCase());
            int dotIndex = textureName.lastIndexOf(".");
            if (dotIndex > -1) {
                texture.append(textureName.substring(0, dotIndex));
            } else {
                texture.append(textureName);
            }
            this.currentTextureName = texture.toString();
            this.textureAtlas.addBitmapAsset(new AParser.BitmapAsset(this.currentTextureName, this.currentTextureName));
        }
    }

    private void getTexCoords(BufferedInputStream stream, byte[] bytes) throws IOException {
        LittleEndianDataInputStream is = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes, this.header.offsetTexCoord - 68, bytes.length - this.header.offsetTexCoord));
        for (int i = 0; i < this.header.numTexCoord; i++) {
            this.co.texCoords.add(new Uv(((float) is.readShort()) / ((float) this.header.skinWidth), ((float) is.readShort()) / ((float) this.header.skinHeight)));
        }
    }

    private void getFrames(BufferedInputStream stream, byte[] bytes) throws IOException {
        String name;
        LittleEndianDataInputStream is = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes, this.header.offsetFrames - 68, bytes.length - this.header.offsetFrames));
        new ArrayList();
        for (int i = 0; i < this.header.numFrames; i++) {
            float scaleX = is.readFloat();
            float scaleY = is.readFloat();
            float scaleZ = is.readFloat();
            float translateX = is.readFloat();
            float translateY = is.readFloat();
            float translateZ = is.readFloat();
            String name2 = is.readString(16);
            if (name2.indexOf("_") > 0) {
                name = name2.subSequence(0, name2.lastIndexOf("_")).toString();
            } else {
                name = name2.substring(0, 6).replaceAll("[0-9]{1,2}$", "");
            }
            Log.d(Min3d.TAG, "frame name: " + name);
            float[] vertices = new float[(this.header.numVerts * 3)];
            int index = 0;
            int j = 0;
            while (j < this.header.numVerts) {
                int index2 = index + 1;
                vertices[index] = (((float) is.readUnsignedByte()) * scaleX) + translateX;
                int index3 = index2 + 1;
                vertices[index2] = (((float) is.readUnsignedByte()) * scaleY) + translateY;
                int index4 = index3 + 1;
                vertices[index3] = (((float) is.readUnsignedByte()) * scaleZ) + translateZ;
                int readUnsignedByte = is.readUnsignedByte();
                if (i == 0) {
                    this.co.vertices.add(new Number3d(vertices[index4 - 3], vertices[index4 - 2], vertices[index4 - 1]));
                }
                j++;
                index = index4;
            }
            this.frames[i] = new KeyFrame(name, vertices);
        }
    }

    private void getTriangles(BufferedInputStream stream, byte[] bytes) throws IOException {
        LittleEndianDataInputStream is = new LittleEndianDataInputStream(new ByteArrayInputStream(bytes, this.header.offsetTriangles - 68, bytes.length - this.header.offsetTriangles));
        int[] indices = new int[(this.header.numTriangles * 3)];
        int index = 0;
        for (int i = 0; i < this.header.numTriangles; i++) {
            int[] vertexIDs = new int[3];
            int[] uvIDS = new int[3];
            int readUnsignedShort = is.readUnsignedShort();
            vertexIDs[2] = readUnsignedShort;
            indices[index + 2] = readUnsignedShort;
            int readUnsignedShort2 = is.readUnsignedShort();
            vertexIDs[1] = readUnsignedShort2;
            indices[index + 1] = readUnsignedShort2;
            int readUnsignedShort3 = is.readUnsignedShort();
            vertexIDs[0] = readUnsignedShort3;
            indices[index] = readUnsignedShort3;
            index += 3;
            uvIDS[2] = is.readUnsignedShort();
            uvIDS[1] = is.readUnsignedShort();
            uvIDS[0] = is.readUnsignedShort();
            ParseObjectFace f = new ParseObjectFace();
            f.v = vertexIDs;
            f.uv = uvIDS;
            f.hasuv = true;
            f.hasn = true;
            f.faceLength = 3;
            f.materialKey = this.currentTextureName;
            this.co.numFaces++;
            this.co.faces.add(f);
            this.co.calculateFaceNormal(f);
        }
        for (int j = 0; j < this.header.numFrames; j++) {
            this.frames[j].setIndices(indices);
        }
    }

    private class MD2Header {
        public int frameSize;
        public int id;
        public int numFrames;
        public int numGLCommands;
        public int numSkins;
        public int numTexCoord;
        public int numTriangles;
        public int numVerts;
        public int offsetEnd;
        public int offsetFrames;
        public int offsetGLCommands;
        public int offsetSkins;
        public int offsetTexCoord;
        public int offsetTriangles;
        public int skinHeight;
        public int skinWidth;
        public int version;

        private MD2Header() {
        }

        public void parse(InputStream stream) throws Exception {
            this.id = MD2Parser.this.readInt(stream);
            this.version = MD2Parser.this.readInt(stream);
            if (this.id == 844121161 && this.version == 8) {
                this.skinWidth = MD2Parser.this.readInt(stream);
                this.skinHeight = MD2Parser.this.readInt(stream);
                this.frameSize = MD2Parser.this.readInt(stream);
                this.numSkins = MD2Parser.this.readInt(stream);
                this.numVerts = MD2Parser.this.readInt(stream);
                this.numTexCoord = MD2Parser.this.readInt(stream);
                this.numTriangles = MD2Parser.this.readInt(stream);
                this.numGLCommands = MD2Parser.this.readInt(stream);
                this.numFrames = MD2Parser.this.readInt(stream);
                this.offsetSkins = MD2Parser.this.readInt(stream);
                this.offsetTexCoord = MD2Parser.this.readInt(stream);
                this.offsetTriangles = MD2Parser.this.readInt(stream);
                this.offsetFrames = MD2Parser.this.readInt(stream);
                this.offsetGLCommands = MD2Parser.this.readInt(stream);
                this.offsetEnd = MD2Parser.this.readInt(stream);
                return;
            }
            throw new Exception("This is not a valid MD2 file.");
        }
    }
}
