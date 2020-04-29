package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.vos.Face;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

@EXClassNullAway
public class FacesBufferedList {
    public static final int BYTES_PER_PROPERTY = 2;
    public static final int PROPERTIES_PER_ELEMENT = 3;
    private ShortBuffer _b;
    private int _numElements;
    private boolean _renderSubsetEnabled = false;
    private int _renderSubsetLength = 1;
    private int _renderSubsetStartIndex = 0;

    public FacesBufferedList(ShortBuffer $b, int $size) {
        ByteBuffer bb = ByteBuffer.allocateDirect($b.limit() * 2);
        bb.order(ByteOrder.nativeOrder());
        this._b = bb.asShortBuffer();
        this._b.put($b);
        this._numElements = $size;
    }

    public FacesBufferedList(int $maxElements) {
        ByteBuffer b = ByteBuffer.allocateDirect($maxElements * 3 * 2);
        b.order(ByteOrder.nativeOrder());
        this._b = b.asShortBuffer();
    }

    public int size() {
        return this._numElements;
    }

    public int capacity() {
        return this._b.capacity() / 3;
    }

    public void clear() {
        this._b.clear();
    }

    public Face get(int $index) {
        this._b.position($index * 3);
        return new Face(this._b.get(), this._b.get(), this._b.get());
    }

    public void putInFace(int $index, Face $face) {
        this._b.position($index * 3);
        $face.a = this._b.get();
        $face.b = this._b.get();
        $face.c = this._b.get();
    }

    public short getPropertyA(int $index) {
        this._b.position($index * 3);
        return this._b.get();
    }

    public short getPropertyB(int $index) {
        this._b.position(($index * 3) + 1);
        return this._b.get();
    }

    public float getPropertyC(int $index) {
        this._b.position(($index * 3) + 2);
        return (float) this._b.get();
    }

    public void renderSubsetStartIndex(int $num) {
        this._renderSubsetStartIndex = $num;
    }

    public int renderSubsetStartIndex() {
        return this._renderSubsetStartIndex;
    }

    public void renderSubsetLength(int $num) {
        this._renderSubsetLength = $num;
    }

    public int renderSubsetLength() {
        return this._renderSubsetLength;
    }

    public boolean renderSubsetEnabled() {
        return this._renderSubsetEnabled;
    }

    public void renderSubsetEnabled(boolean $b) {
        this._renderSubsetEnabled = $b;
    }

    public void add(Face $f) {
        set(this._numElements, $f);
        this._numElements++;
    }

    public void add(int $a, int $b, int $c) {
        add((short) $a, (short) $b, (short) $c);
    }

    public void add(short $a, short $b, short $c) {
        set(this._numElements, $a, $b, $c);
        this._numElements++;
    }

    public void set(int $index, Face $face) {
        this._b.position($index * 3);
        this._b.put($face.a);
        this._b.put($face.b);
        this._b.put($face.c);
    }

    public void set(int $index, short $a, short $b, short $c) {
        this._b.position($index * 3);
        this._b.put($a);
        this._b.put($b);
        this._b.put($c);
    }

    public void setPropertyA(int $index, short $a) {
        this._b.position($index * 3);
        this._b.put($a);
    }

    public void setPropertyB(int $index, short $b) {
        this._b.position(($index * 3) + 1);
        this._b.put($b);
    }

    public void setPropertyC(int $index, short $c) {
        this._b.position(($index * 3) + 2);
        this._b.put($c);
    }

    public ShortBuffer buffer() {
        return this._b;
    }

    public FacesBufferedList clone() {
        this._b.position(0);
        return new FacesBufferedList(this._b, size());
    }
}
