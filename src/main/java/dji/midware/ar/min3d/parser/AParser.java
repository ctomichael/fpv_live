package dji.midware.ar.min3d.parser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.animation.AnimationObject3d;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

@EXClassNullAway
public abstract class AParser implements IParser {
    protected ParseObjectData co;
    protected String currentMaterialKey;
    protected boolean firstObject;
    protected boolean generateMipMap;
    protected InputStream inputStream;
    protected HashMap<String, Material> materialMap;
    protected ArrayList<Number3d> normals;
    protected String packageID;
    protected ArrayList<ParseObjectData> parseObjects;
    protected String resourceID;
    protected Resources resources;
    protected ArrayList<Uv> texCoords;
    protected TextureAtlas textureAtlas;
    protected ArrayList<Number3d> vertices;

    public AParser() {
        this.vertices = new ArrayList<>();
        this.texCoords = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.parseObjects = new ArrayList<>();
        this.textureAtlas = new TextureAtlas();
        this.firstObject = true;
        this.materialMap = new HashMap<>();
    }

    public AParser(Resources resources2, String resourceID2, Boolean generateMipMap2) {
        this();
        this.resources = resources2;
        this.resourceID = resourceID2;
        if (resourceID2.indexOf(":") > -1) {
            this.packageID = resourceID2.split(":")[0];
        }
        this.generateMipMap = generateMipMap2.booleanValue();
    }

    public AParser(Resources resources2, InputStream stream, boolean generateMipMap2) {
        this();
        this.resources = resources2;
        this.inputStream = stream;
        this.generateMipMap = generateMipMap2;
    }

    /* access modifiers changed from: protected */
    public void cleanup() {
        this.parseObjects.clear();
        this.textureAtlas.cleanup();
        this.vertices.clear();
        this.texCoords.clear();
        this.normals.clear();
    }

    public Object3dContainer getParsedObject() {
        return null;
    }

    public AnimationObject3d getParsedAnimationObject() {
        return null;
    }

    /* access modifiers changed from: protected */
    public String readString(InputStream stream) throws IOException {
        String result = new String();
        while (true) {
            byte inByte = (byte) stream.read();
            if (inByte == 0) {
                return result;
            }
            result = result + ((char) inByte);
        }
    }

    /* access modifiers changed from: protected */
    public int readInt(InputStream stream) throws IOException {
        return stream.read() | (stream.read() << 8) | (stream.read() << 16) | (stream.read() << 24);
    }

    /* access modifiers changed from: protected */
    public int readShort(InputStream stream) throws IOException {
        return stream.read() | (stream.read() << 8);
    }

    /* access modifiers changed from: protected */
    public float readFloat(InputStream stream) throws IOException {
        return Float.intBitsToFloat(readInt(stream));
    }

    public void parse() {
    }

    protected class BitmapAsset {
        public Bitmap bitmap;
        public String key;
        public String resourceID;
        public float uOffset;
        public float uScale;
        public boolean useForAtlasDimensions = false;
        public float vOffset;
        public float vScale;

        public BitmapAsset(String key2, String resourceID2) {
            this.key = key2;
            this.resourceID = resourceID2;
        }
    }

    protected class TextureAtlas {
        private Bitmap atlas;
        private String atlasId;
        private ArrayList<BitmapAsset> bitmaps = new ArrayList<>();

        public TextureAtlas() {
        }

        public void addBitmapAsset(BitmapAsset ba) {
            BitmapAsset existingBA = getBitmapAssetByResourceID(ba.resourceID);
            if (existingBA == null) {
                int bmResourceID = AParser.this.resources.getIdentifier(ba.resourceID, null, null);
                if (bmResourceID == 0) {
                    Log.d(Min3d.TAG, "Texture not found: " + ba.resourceID);
                    return;
                }
                Log.d(Min3d.TAG, "Adding texture " + ba.resourceID);
                Bitmap b = Utils.makeBitmapFromResourceId(bmResourceID);
                ba.useForAtlasDimensions = true;
                ba.bitmap = b;
            } else {
                ba.bitmap = existingBA.bitmap;
            }
            this.bitmaps.add(ba);
        }

        public BitmapAsset getBitmapAssetByResourceID(String resourceID) {
            int numBitmaps = this.bitmaps.size();
            for (int i = 0; i < numBitmaps; i++) {
                if (this.bitmaps.get(i).resourceID.equals(resourceID)) {
                    return this.bitmaps.get(i);
                }
            }
            return null;
        }

        public void generate() {
            Collections.sort(this.bitmaps, new BitmapHeightComparer());
            if (this.bitmaps.size() != 0) {
                BitmapAsset largestBitmap = this.bitmaps.get(0);
                int totalWidth = 0;
                int numBitmaps = this.bitmaps.size();
                int uOffset = 0;
                for (int i = 0; i < numBitmaps; i++) {
                    if (this.bitmaps.get(i).useForAtlasDimensions) {
                        totalWidth += this.bitmaps.get(i).bitmap.getWidth();
                    }
                }
                this.atlas = Bitmap.createBitmap(totalWidth, largestBitmap.bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                for (int i2 = 0; i2 < numBitmaps; i2++) {
                    BitmapAsset ba = this.bitmaps.get(i2);
                    BitmapAsset existingBA = getBitmapAssetByResourceID(ba.resourceID);
                    if (ba.useForAtlasDimensions) {
                        Bitmap b = ba.bitmap;
                        int w = b.getWidth();
                        int h = b.getHeight();
                        int[] pixels = new int[(w * h)];
                        b.getPixels(pixels, 0, w, 0, 0, w, h);
                        this.atlas.setPixels(pixels, 0, w, uOffset, 0, w, h);
                        ba.uOffset = ((float) uOffset) / ((float) totalWidth);
                        ba.vOffset = 0.0f;
                        ba.uScale = ((float) w) / ((float) totalWidth);
                        ba.vScale = ((float) h) / ((float) largestBitmap.bitmap.getHeight());
                        uOffset += w;
                        b.recycle();
                    } else {
                        ba.uOffset = existingBA.uOffset;
                        ba.vOffset = existingBA.vOffset;
                        ba.uScale = existingBA.uScale;
                        ba.vScale = existingBA.vScale;
                    }
                }
                setId(Shared.textureManager().getNewAtlasId());
            }
        }

        public Bitmap getBitmap() {
            return this.atlas;
        }

        public boolean hasBitmaps() {
            return this.bitmaps.size() > 0;
        }

        private class BitmapHeightComparer implements Comparator<BitmapAsset> {
            private BitmapHeightComparer() {
            }

            public int compare(BitmapAsset b1, BitmapAsset b2) {
                int height1 = b1.bitmap.getHeight();
                int height2 = b2.bitmap.getHeight();
                if (height1 < height2) {
                    return 1;
                }
                if (height1 == height2) {
                    return 0;
                }
                return -1;
            }
        }

        public BitmapAsset getBitmapAssetByName(String materialKey) {
            int numBitmaps = this.bitmaps.size();
            for (int i = 0; i < numBitmaps; i++) {
                if (this.bitmaps.get(i).key.equals(materialKey)) {
                    return this.bitmaps.get(i);
                }
            }
            return null;
        }

        public void cleanup() {
            int numBitmaps = this.bitmaps.size();
            for (int i = 0; i < numBitmaps; i++) {
                this.bitmaps.get(i).bitmap.recycle();
            }
            if (this.atlas != null) {
                this.atlas.recycle();
            }
            this.bitmaps.clear();
            AParser.this.vertices.clear();
            AParser.this.texCoords.clear();
            AParser.this.normals.clear();
        }

        public void setId(String newAtlasId) {
            this.atlasId = newAtlasId;
        }

        public String getId() {
            return this.atlasId;
        }
    }

    protected class Material {
        public Color4 diffuseColor;
        public String diffuseTextureMap;
        public String name;

        public Material(String name2) {
            this.name = name2;
        }
    }
}
