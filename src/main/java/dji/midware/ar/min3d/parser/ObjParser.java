package dji.midware.ar.min3d.parser;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.parser.AParser;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.StringTokenizer;

@EXClassNullAway
public class ObjParser extends AParser implements IParser {
    private final String DIFFUSE_COLOR = "Kd";
    private final String DIFFUSE_TEX_MAP = "map_Kd";
    private final String FACE = "f";
    private final String MATERIAL_LIB = "mtllib";
    private final String NEW_MATERIAL = "newmtl";
    private final String NORMAL = "vn";
    private final String OBJECT = "o";
    private final String TEXCOORD = "vt";
    private final String USE_MATERIAL = "usemtl";
    private final String VERTEX = "v";

    public ObjParser(Resources resources, String resourceID, boolean generateMipMap) {
        super(resources, resourceID, Boolean.valueOf(generateMipMap));
    }

    public ObjParser(Resources resources, InputStream inputStream, boolean generateMipMap) {
        super(resources, inputStream, generateMipMap);
    }

    public void parse() {
        long startTime = Calendar.getInstance().getTimeInMillis();
        InputStream fileIn = this.resources.openRawResource(this.resources.getIdentifier(this.resourceID, null, null));
        BufferedReader buffer = new BufferedReader(new InputStreamReader(fileIn));
        this.co = new ParseObjectData(this.vertices, this.texCoords, this.normals);
        this.parseObjects.add(this.co);
        Log.d(Min3d.TAG, "Start parsing object " + this.resourceID);
        Log.d(Min3d.TAG, "Start time " + startTime);
        while (true) {
            try {
                String line = buffer.readLine();
                if (line == null) {
                    break;
                }
                StringTokenizer parts = new StringTokenizer(line, " ");
                int numTokens = parts.countTokens();
                if (numTokens != 0) {
                    String type = parts.nextToken();
                    if (type.equals("v")) {
                        Number3d vertex = new Number3d();
                        vertex.x = Float.parseFloat(parts.nextToken());
                        vertex.y = Float.parseFloat(parts.nextToken());
                        vertex.z = Float.parseFloat(parts.nextToken());
                        this.vertices.add(vertex);
                    } else if (type.equals("f")) {
                        if (numTokens == 4) {
                            this.co.numFaces++;
                            this.co.faces.add(new ObjFace(this, line, this.currentMaterialKey, 3));
                        } else if (numTokens == 5) {
                            this.co.numFaces += 2;
                            this.co.faces.add(new ObjFace(this, line, this.currentMaterialKey, 4));
                        }
                    } else if (type.equals("vt")) {
                        Uv texCoord = new Uv();
                        texCoord.u = Float.parseFloat(parts.nextToken());
                        texCoord.v = Float.parseFloat(parts.nextToken()) * -1.0f;
                        this.texCoords.add(texCoord);
                    } else if (type.equals("vn")) {
                        Number3d normal = new Number3d();
                        normal.x = Float.parseFloat(parts.nextToken());
                        normal.y = Float.parseFloat(parts.nextToken());
                        normal.z = Float.parseFloat(parts.nextToken());
                        this.normals.add(normal);
                    } else if (type.equals("mtllib")) {
                        readMaterialLib(parts.nextToken());
                    } else if (type.equals("usemtl")) {
                        this.currentMaterialKey = parts.nextToken();
                    } else if (type.equals("o")) {
                        String objName = parts.hasMoreTokens() ? parts.nextToken() : "";
                        if (this.firstObject) {
                            Log.d(Min3d.TAG, "Create object " + objName);
                            this.co.name = objName;
                            this.firstObject = false;
                        } else {
                            Log.d(Min3d.TAG, "Create object " + objName);
                            this.co = new ParseObjectData(this.vertices, this.texCoords, this.normals);
                            this.co.name = objName;
                            this.parseObjects.add(this.co);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (fileIn != null) {
                    fileIn.close();
                }
            } catch (Throwable th) {
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        throw th;
                    }
                }
                if (fileIn != null) {
                    fileIn.close();
                }
                throw th;
            }
        }
        if (buffer != null) {
            try {
                buffer.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
        }
        if (fileIn != null) {
            fileIn.close();
        }
        Log.d(Min3d.TAG, "End time " + (Calendar.getInstance().getTimeInMillis() - startTime));
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
        cleanup();
        return obj;
    }

    private void readMaterialLib(String libID) {
        StringBuffer resourceID = new StringBuffer(this.packageID);
        StringBuffer libIDSbuf = new StringBuffer(libID);
        int dotIndex = libIDSbuf.lastIndexOf(".");
        if (dotIndex > -1) {
            libIDSbuf = libIDSbuf.replace(dotIndex, dotIndex + 1, "_");
        }
        resourceID.append(":raw/");
        resourceID.append(libIDSbuf.toString());
        InputStream fileIn = this.resources.openRawResource(this.resources.getIdentifier(resourceID.toString(), null, null));
        BufferedReader buffer = new BufferedReader(new InputStreamReader(fileIn));
        String currentMaterial = "";
        while (true) {
            try {
                String line = buffer.readLine();
                if (line == null) {
                    break;
                }
                String[] parts = line.split(" ");
                if (parts.length != 0) {
                    String type = parts[0];
                    if (type.equals("newmtl")) {
                        if (parts.length > 1) {
                            currentMaterial = parts[1];
                            this.materialMap.put(currentMaterial, new AParser.Material(currentMaterial));
                        }
                    } else if (type.equals("Kd") && !type.equals("map_Kd")) {
                        ((AParser.Material) this.materialMap.get(currentMaterial)).diffuseColor = new Color4(Float.parseFloat(parts[1]) * 255.0f, Float.parseFloat(parts[2]) * 255.0f, Float.parseFloat(parts[3]) * 255.0f, 255.0f);
                    } else if (type.equals("map_Kd") && parts.length > 1) {
                        ((AParser.Material) this.materialMap.get(currentMaterial)).diffuseTextureMap = parts[1];
                        StringBuffer stringBuffer = new StringBuffer(this.packageID);
                        stringBuffer.append(":drawable/");
                        StringBuffer stringBuffer2 = new StringBuffer(parts[1]);
                        int dotIndex2 = stringBuffer2.lastIndexOf(".");
                        if (dotIndex2 > -1) {
                            stringBuffer.append(stringBuffer2.substring(0, dotIndex2));
                        } else {
                            stringBuffer.append(stringBuffer2);
                        }
                        Bitmap makeBitmapFromResourceId = Utils.makeBitmapFromResourceId(this.resources.getIdentifier(stringBuffer.toString(), null, null));
                        this.textureAtlas.addBitmapAsset(new AParser.BitmapAsset(currentMaterial, stringBuffer.toString()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        return;
                    }
                }
                if (fileIn != null) {
                    fileIn.close();
                    return;
                }
                return;
            } catch (Throwable th) {
                if (buffer != null) {
                    try {
                        buffer.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        throw th;
                    }
                }
                if (fileIn != null) {
                    fileIn.close();
                }
                throw th;
            }
        }
        if (buffer != null) {
            try {
                buffer.close();
            } catch (IOException e4) {
                e4.printStackTrace();
                return;
            }
        }
        if (fileIn != null) {
            fileIn.close();
        }
    }

    /* access modifiers changed from: protected */
    public void cleanup() {
        super.cleanup();
        this.materialMap.clear();
    }

    private class ObjFace extends ParseObjectFace {
        final /* synthetic */ ObjParser this$0;

        public ObjFace(ObjParser objParser, String line, String materialKey, int faceLength) {
            boolean emptyVt;
            boolean z;
            boolean z2 = false;
            this.this$0 = objParser;
            this.materialKey = materialKey;
            this.faceLength = faceLength;
            if (line.indexOf("//") > -1) {
                emptyVt = true;
            } else {
                emptyVt = false;
            }
            StringTokenizer parts = new StringTokenizer(emptyVt ? line.replace("//", IMemberProtocol.PARAM_SEPERATOR) : line);
            parts.nextToken();
            StringTokenizer subParts = new StringTokenizer(parts.nextToken(), IMemberProtocol.PARAM_SEPERATOR);
            int partLength = subParts.countTokens();
            if (partLength < 2 || emptyVt) {
                z = false;
            } else {
                z = true;
            }
            this.hasuv = z;
            if (partLength == 3 || (partLength == 2 && emptyVt)) {
                z2 = true;
            }
            this.hasn = z2;
            this.v = new int[faceLength];
            if (this.hasuv) {
                this.uv = new int[faceLength];
            }
            if (this.hasn) {
                this.n = new int[faceLength];
            }
            int i = 1;
            while (i < faceLength + 1) {
                subParts = i > 1 ? new StringTokenizer(parts.nextToken(), IMemberProtocol.PARAM_SEPERATOR) : subParts;
                int index = i - 1;
                this.v[index] = (short) (Short.parseShort(subParts.nextToken()) - 1);
                if (this.hasuv) {
                    this.uv[index] = (short) (Short.parseShort(subParts.nextToken()) - 1);
                }
                if (this.hasn) {
                    this.n[index] = (short) (Short.parseShort(subParts.nextToken()) - 1);
                }
                i++;
            }
        }
    }
}
