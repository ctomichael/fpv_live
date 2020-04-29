package dji.midware.ar.min3d.parser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import com.drew.metadata.exif.ExifDirectoryBase;
import com.drew.metadata.exif.makernotes.FujifilmMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.parser.AParser;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

@EXClassNullAway
public class Max3DSParser extends AParser implements IParser {
    private final int FACES = 16672;
    private final int IDENTIFIER_3DS = 19789;
    private final int MATERIAL = 45055;
    private final int MESH_BLOCK = 15677;
    private final int OBJECT_BLOCK = 16384;
    private final int TEXCOORD = 16704;
    private final int TEX_FILENAME = ExifDirectoryBase.TAG_FILE_SOURCE;
    private final int TEX_MAP = 41472;
    private final int TEX_NAME = ExifDirectoryBase.TAG_FLASHPIX_VERSION;
    private final int TRIMESH = FujifilmMakernoteDirectory.TAG_FACES_DETECTED;
    private final int TRI_MATERIAL = 16688;
    private final int VERTICES = 16656;
    private int chunkEndOffset;
    private int chunkID;
    private String currentObjName;
    private boolean endReached;

    public Max3DSParser(Resources resources, String resourceID, boolean generateMipMap) {
        super(resources, resourceID, Boolean.valueOf(generateMipMap));
    }

    public Max3DSParser(Resources resources, InputStream inputStream, boolean generateMipMap) {
        super(resources, inputStream, generateMipMap);
    }

    public void parse() {
        InputStream fileIn;
        if (this.inputStream != null) {
            fileIn = this.inputStream;
        } else {
            fileIn = this.resources.openRawResource(this.resources.getIdentifier(this.resourceID, null, null));
        }
        BufferedInputStream stream = new BufferedInputStream(fileIn);
        Log.d(Min3d.TAG, "Start parsing object");
        this.co = new ParseObjectData();
        this.parseObjects.add(this.co);
        try {
            readHeader(stream);
            if (this.chunkID != 19789) {
                Log.d(Min3d.TAG, "Not a valid .3DS file!");
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
                    return;
                }
                return;
            }
            Log.d(Min3d.TAG, "Found a valid .3DS file");
            while (!this.endReached) {
                readChunk(stream);
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            if (fileIn != null) {
                fileIn.close();
            }
            Log.d(Min3d.TAG, "End parsing object");
        } catch (IOException e3) {
            e3.printStackTrace();
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            if (fileIn != null) {
                fileIn.close();
            }
        } catch (Throwable th) {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                    throw th;
                }
            }
            if (fileIn != null) {
                fileIn.close();
            }
            throw th;
        }
    }

    private void readHeader(InputStream stream) throws IOException {
        this.chunkID = readShort(stream);
        this.chunkEndOffset = readInt(stream);
        this.endReached = this.chunkID < 0;
    }

    private void readChunk(InputStream stream) throws IOException {
        readHeader(stream);
        switch (this.chunkID) {
            case 15677:
            case 41472:
            case 45055:
                return;
            case 16384:
                this.currentObjName = readString(stream);
                Log.d(Min3d.TAG, "Parsing object " + this.currentObjName);
                return;
            case FujifilmMakernoteDirectory.TAG_FACES_DETECTED /*16640*/:
                if (this.firstObject) {
                    this.co.name = this.currentObjName;
                    this.firstObject = false;
                    return;
                }
                this.co = new ParseObjectData();
                this.co.name = this.currentObjName;
                this.parseObjects.add(this.co);
                return;
            case 16656:
                readVertices(stream);
                return;
            case 16672:
                readFaces(stream);
                return;
            case 16688:
                String materialName = readString(stream);
                int numFaces = readShort(stream);
                for (int i = 0; i < numFaces; i++) {
                    this.co.faces.get(readShort(stream)).materialKey = materialName;
                }
                return;
            case 16704:
                readTexCoords(stream);
                return;
            case ExifDirectoryBase.TAG_FLASHPIX_VERSION /*40960*/:
                this.currentMaterialKey = readString(stream);
                return;
            case ExifDirectoryBase.TAG_FILE_SOURCE /*41728*/:
                String fileName = readString(stream);
                StringBuffer texture = new StringBuffer(this.packageID);
                texture.append(":drawable/");
                StringBuffer textureName = new StringBuffer(fileName.toLowerCase());
                int dotIndex = textureName.lastIndexOf(".");
                if (dotIndex > -1) {
                    texture.append(textureName.substring(0, dotIndex));
                } else {
                    texture.append(textureName);
                }
                this.textureAtlas.addBitmapAsset(new AParser.BitmapAsset(this.currentMaterialKey, texture.toString()));
                return;
            default:
                skipRead(stream);
                return;
        }
    }

    private void skipRead(InputStream stream) throws IOException {
        for (int i = 0; i < this.chunkEndOffset - 6 && !this.endReached; i++) {
            this.endReached = stream.read() < 0;
        }
    }

    private void readVertices(InputStream buffer) throws IOException {
        int numVertices = readShort(buffer);
        for (int i = 0; i < numVertices; i++) {
            this.co.vertices.add(new Number3d(readFloat(buffer), readFloat(buffer), -readFloat(buffer)));
        }
    }

    private void readFaces(InputStream buffer) throws IOException {
        int triangles = readShort(buffer);
        for (int i = 0; i < triangles; i++) {
            int[] vertexIDs = {readShort(buffer), readShort(buffer), readShort(buffer)};
            readShort(buffer);
            ParseObjectFace face = new ParseObjectFace();
            face.v = vertexIDs;
            face.uv = vertexIDs;
            face.faceLength = 3;
            face.hasuv = true;
            this.co.numFaces++;
            this.co.faces.add(face);
            this.co.calculateFaceNormal(face);
        }
    }

    private void readTexCoords(InputStream buffer) throws IOException {
        int numVertices = readShort(buffer);
        for (int i = 0; i < numVertices; i++) {
            Uv uv = new Uv();
            uv.u = readFloat(buffer);
            uv.v = readFloat(buffer) * -1.0f;
            this.co.texCoords.add(uv);
        }
    }

    public Object3dContainer getParsedObject() {
        Log.d(Min3d.TAG, "Start object creation");
        Object3dContainer obj = new Object3dContainer(0, 0);
        int numObjects = this.parseObjects.size();
        Bitmap texture = null;
        if (this.textureAtlas.hasBitmaps()) {
            this.textureAtlas.generate();
            texture = this.textureAtlas.getBitmap();
            Shared.textureManager().addTextureId(texture, this.textureAtlas.getId(), this.generateMipMap);
        }
        for (int i = 0; i < numObjects; i++) {
            ParseObjectData o = (ParseObjectData) this.parseObjects.get(i);
            Log.d(Min3d.TAG, "Creating object " + o.name);
            obj.addChild(o.getParsedObject(this.materialMap, this.textureAtlas));
        }
        if (this.textureAtlas.hasBitmaps() && texture != null) {
            texture.recycle();
        }
        Log.d(Min3d.TAG, "Object creation finished");
        super.cleanup();
        return obj;
    }
}
