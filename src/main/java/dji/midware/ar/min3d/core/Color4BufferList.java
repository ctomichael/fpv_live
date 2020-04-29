package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.vos.Color4;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@EXClassNullAway
public class Color4BufferList {
    public static final int BYTES_PER_PROPERTY = 1;
    public static final int PROPERTIES_PER_ELEMENT = 4;
    private ByteBuffer _b;
    private int _numElements;

    public Color4BufferList(ByteBuffer $b, int $size) {
        this._b = ByteBuffer.allocate($b.limit() * 1);
        this._b.put($b);
        this._numElements = $size;
    }

    public Color4BufferList(int $maxElements) {
        this._b = ByteBuffer.allocateDirect($maxElements * 4 * 1);
        this._b.order(ByteOrder.nativeOrder());
    }

    public int size() {
        return this._numElements;
    }

    public int capacity() {
        return this._b.capacity() / 4;
    }

    public void clear() {
        this._b.clear();
    }

    public Color4 getAsColor4(int $index) {
        this._b.position($index * 4);
        return new Color4((short) this._b.get(), (short) this._b.get(), (short) this._b.get(), (short) this._b.get());
    }

    public void putInColor4(int $index, Color4 $color4) {
        this._b.position($index * 4);
        $color4.r = (short) this._b.get();
        $color4.g = (short) this._b.get();
        $color4.b = (short) this._b.get();
        $color4.a = (short) this._b.get();
    }

    public short getPropertyR(int $index) {
        this._b.position($index * 4);
        return (short) this._b.get();
    }

    public short getPropertyG(int $index) {
        this._b.position(($index * 4) + 1);
        return (short) this._b.get();
    }

    public float getPropertyB(int $index) {
        this._b.position(($index * 4) + 2);
        return (float) ((short) this._b.get());
    }

    public float getPropertyA(int $index) {
        this._b.position(($index * 4) + 3);
        return (float) ((short) this._b.get());
    }

    public void add(Color4 $c) {
        set(this._numElements, $c);
        this._numElements++;
    }

    public void add(short $r, short $g, short $b, short $a) {
        set(this._numElements, $r, $g, $b, $a);
        this._numElements++;
    }

    public void set(int $index, Color4 $c) {
        this._b.position($index * 4);
        this._b.put((byte) $c.r);
        this._b.put((byte) $c.g);
        this._b.put((byte) $c.b);
        this._b.put((byte) $c.a);
    }

    public void set(int $index, short $r, short $g, short $b, short $a) {
        this._b.position($index * 4);
        this._b.put((byte) $r);
        this._b.put((byte) $g);
        this._b.put((byte) $b);
        this._b.put((byte) $a);
    }

    public void setPropertyR(int $index, short $r) {
        this._b.position($index * 4);
        this._b.put((byte) $r);
    }

    public void setPropertyG(int $index, short $g) {
        this._b.position(($index * 4) + 1);
        this._b.put((byte) $g);
    }

    public void setPropertyB(int $index, short $b) {
        this._b.position(($index * 4) + 2);
        this._b.put((byte) $b);
    }

    public void setPropertyA(int $index, short $a) {
        this._b.position(($index * 4) + 3);
        this._b.put((byte) $a);
    }

    public ByteBuffer buffer() {
        return this._b;
    }

    public Color4BufferList clone() {
        this._b.position(0);
        return new Color4BufferList(this._b, size());
    }
}
