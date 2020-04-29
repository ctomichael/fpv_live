package dji.midware.ar.min3d.core;

import android.graphics.Bitmap;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.ArUtil;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.Shared;
import java.util.HashMap;
import java.util.Set;

@EXClassNullAway
public class TextureManager {
    private static int _atlasId = 0;
    private static int _counter = 1000001;
    private HashMap<String, Boolean> _idToHasMipMap;
    private HashMap<String, Integer> _idToTextureName;

    public TextureManager() {
        reset();
    }

    public void reset() {
        Object[] a;
        Renderer renderer = Shared.renderer();
        if (!(renderer == null || this._idToTextureName == null)) {
            for (Object obj : this._idToTextureName.keySet().toArray()) {
                renderer.deleteTexture(getGlTextureId((String) obj));
            }
        }
        this._idToTextureName = new HashMap<>();
        this._idToHasMipMap = new HashMap<>();
    }

    public String addTextureId(Bitmap $b, String $id, boolean $generateMipMap) {
        Renderer renderer = Shared.renderer();
        if (renderer == null) {
            return null;
        }
        if (this._idToTextureName.containsKey($id)) {
            ArUtil.logArMsgToFile("Texture id \"" + $id + "\" already exists.");
            return null;
        }
        String s = $id;
        this._idToTextureName.put(s, Integer.valueOf(renderer.uploadTextureAndReturnId($b, $generateMipMap)));
        this._idToHasMipMap.put(s, Boolean.valueOf($generateMipMap));
        _counter++;
        return s;
    }

    public String addTextureId(Bitmap $b, String $id) {
        return addTextureId($b, $id, false);
    }

    public String createTextureId(Bitmap $b, boolean $generateMipMap) {
        return addTextureId($b, _counter + "", $generateMipMap);
    }

    public void deleteTexture(String $textureId) {
        Renderer renderer = Shared.renderer();
        if (renderer != null) {
            renderer.deleteTexture(this._idToTextureName.get($textureId).intValue());
            this._idToTextureName.remove($textureId);
            this._idToHasMipMap.remove($textureId);
        }
    }

    public String[] getTextureIds() {
        Set<String> set = this._idToTextureName.keySet();
        String[] a = new String[set.size()];
        set.toArray(a);
        return a;
    }

    /* access modifiers changed from: package-private */
    public int getGlTextureId(String $textureId) {
        return this._idToTextureName.get($textureId).intValue();
    }

    /* access modifiers changed from: package-private */
    public boolean hasMipMap(String $textureId) {
        return this._idToHasMipMap.get($textureId).booleanValue();
    }

    public boolean contains(String $textureId) {
        return this._idToTextureName.containsKey($textureId);
    }

    private String arrayToString(String[] $a) {
        String s = "";
        for (int i = 0; i < $a.length; i++) {
            s = s + $a[i].toString() + " | ";
        }
        return s;
    }

    private void logContents() {
        Log.v(Min3d.TAG, "TextureManager contents updated - " + arrayToString(getTextureIds()));
    }

    public String getNewAtlasId() {
        int i = _atlasId;
        _atlasId = i + 1;
        return "atlas".concat(Integer.toString(i));
    }
}
