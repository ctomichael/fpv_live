package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.ArUtil;
import dji.midware.ar.min3d.Shared;
import dji.midware.ar.min3d.vos.TextureVo;
import java.util.ArrayList;

@EXClassNullAway
public class TextureList {
    private ArrayList<TextureVo> _t = new ArrayList<>();

    public boolean add(TextureVo $texture) {
        if (!Shared.textureManager().contains($texture.textureId)) {
            return false;
        }
        return this._t.add($texture);
    }

    public void add(int $index, TextureVo $texture) {
        this._t.add($index, $texture);
    }

    public TextureVo addById(String $textureId) {
        if (!Shared.textureManager().contains($textureId)) {
            ArUtil.logArMsgToFile("Could not create TextureVo using textureId \"" + $textureId + "\". TextureManager does not contain that id.");
            return null;
        }
        TextureVo t = new TextureVo($textureId);
        this._t.add(t);
        return t;
    }

    public boolean addReplace(TextureVo $texture) {
        this._t.clear();
        return this._t.add($texture);
    }

    public boolean remove(TextureVo $texture) {
        return this._t.remove($texture);
    }

    public boolean removeById(String $textureId) {
        TextureVo t = getById($textureId);
        if (t != null) {
            return this._t.remove(t);
        }
        throw new Error("No match in TextureList for id \"" + $textureId + "\"");
    }

    public void removeAll() {
        for (int i = 0; i < this._t.size(); i++) {
            this._t.remove(0);
        }
    }

    public TextureVo get(int $index) {
        return this._t.get($index);
    }

    public TextureVo getById(String $textureId) {
        for (int i = 0; i < this._t.size(); i++) {
            if ($textureId == this._t.get(i).textureId) {
                return this._t.get(i);
            }
        }
        return null;
    }

    public int size() {
        return this._t.size();
    }

    public void clear() {
        this._t.clear();
    }

    public TextureVo[] toArray() {
        TextureVo[] ret = new TextureVo[this._t.toArray().length];
        for (int i = 0; i < this._t.size(); i++) {
            ret[i] = this._t.get(i);
        }
        return ret;
    }

    public String[] getIds() {
        String[] a = new String[this._t.size()];
        for (int i = 0; i < this._t.size(); i++) {
            a[i] = this._t.get(i).textureId;
        }
        return a;
    }
}
