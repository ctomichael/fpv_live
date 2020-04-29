package dji.midware.ar.min3d.core;

import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Min3d;
import dji.midware.ar.min3d.vos.Light;
import java.util.ArrayList;
import java.util.HashMap;

@EXClassNullAway
public class ManagedLightList {
    private ArrayList<Integer> _availGlIndices;
    private boolean[] _glIndexEnabled;
    private boolean[] _glIndexEnabledDirty;
    private HashMap<Light, Integer> _lightToGlIndex;
    private ArrayList<Light> _lights;

    public ManagedLightList() {
        reset();
    }

    public void reset() {
        Log.i(Min3d.TAG, "ManagedLightList.reset()");
        this._availGlIndices = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            this._availGlIndices.add(Integer.valueOf(i));
        }
        this._lightToGlIndex = new HashMap<>();
        this._glIndexEnabled = new boolean[8];
        this._glIndexEnabledDirty = new boolean[8];
        for (int i2 = 0; i2 < 8; i2++) {
            this._glIndexEnabled[i2] = false;
            this._glIndexEnabledDirty[i2] = true;
        }
        this._lights = new ArrayList<>();
    }

    public boolean add(Light $light) {
        if (this._lights.contains($light)) {
            return false;
        }
        if (this._lights.size() > 8) {
            throw new Error("Exceeded maximum number of Lights");
        }
        boolean add = this._lights.add($light);
        int glIndex = this._availGlIndices.remove(0).intValue();
        this._lightToGlIndex.put($light, Integer.valueOf(glIndex));
        this._glIndexEnabled[glIndex] = true;
        this._glIndexEnabledDirty[glIndex] = true;
        return add;
    }

    public void remove(Light $light) {
        if (this._lights.remove($light)) {
            int glIndex = this._lightToGlIndex.get($light).intValue();
            this._availGlIndices.add(Integer.valueOf(glIndex));
            this._glIndexEnabled[glIndex] = false;
            this._glIndexEnabledDirty[glIndex] = true;
        }
    }

    public void removeAll() {
        reset();
    }

    public int size() {
        return this._lights.size();
    }

    public Light get(int $index) {
        return this._lights.get($index);
    }

    public Light[] toArray() {
        return (Light[]) this._lights.toArray(new Light[this._lights.size()]);
    }

    /* access modifiers changed from: package-private */
    public int getGlIndexByLight(Light $light) {
        return this._lightToGlIndex.get($light).intValue();
    }

    /* access modifiers changed from: package-private */
    public Light getLightByGlIndex(int $glIndex) {
        for (int i = 0; i < this._lights.size(); i++) {
            Light light = this._lights.get(i);
            Integer integer = this._lightToGlIndex.get(light);
            if (integer != null && integer.intValue() == $glIndex) {
                return light;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean[] glIndexEnabledDirty() {
        return this._glIndexEnabledDirty;
    }

    /* access modifiers changed from: package-private */
    public boolean[] glIndexEnabled() {
        return this._glIndexEnabled;
    }
}
