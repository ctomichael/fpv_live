package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.vos.Uv;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

@EXClassNullAway
public class UvBufferList {
    public static final int BYTES_PER_PROPERTY = 4;
    public static final int PROPERTIES_PER_ELEMENT = 2;
    private FloatBuffer _b;
    private int _numElements = 0;

    public UvBufferList(FloatBuffer $b, int $size) {
        ByteBuffer bb = ByteBuffer.allocateDirect($b.limit() * 4);
        bb.order(ByteOrder.nativeOrder());
        this._b = bb.asFloatBuffer();
        this._b.put($b);
        this._numElements = $size;
    }

    public UvBufferList(int $maxElements) {
        ByteBuffer bb = ByteBuffer.allocateDirect($maxElements * 2 * 4);
        bb.order(ByteOrder.nativeOrder());
        this._b = bb.asFloatBuffer();
    }

    public int size() {
        return this._numElements;
    }

    public int capacity() {
        return this._b.capacity() / 2;
    }

    public void clear() {
        this._b.clear();
    }

    public Uv getAsUv(int $index) {
        this._b.position($index * 2);
        return new Uv(this._b.get(), this._b.get());
    }

    public void putInUv(int $index, Uv $uv) {
        this._b.position($index * 2);
        $uv.u = this._b.get();
        $uv.v = this._b.get();
    }

    public float getPropertyU(int $index) {
        this._b.position($index * 2);
        return this._b.get();
    }

    public float getPropertyV(int $index) {
        this._b.position(($index * 2) + 1);
        return this._b.get();
    }

    public void add(Uv $uv) {
        set(this._numElements, $uv);
        this._numElements++;
    }

    public void add(float $u, float $v) {
        set(this._numElements, $u, $v);
        this._numElements++;
    }

    public void set(int $index, Uv $uv) {
        this._b.position($index * 2);
        this._b.put($uv.u);
        this._b.put($uv.v);
    }

    public void set(int $index, float $u, float $v) {
        this._b.position($index * 2);
        this._b.put($u);
        this._b.put($v);
    }

    public void setPropertyU(int $index, float $u) {
        this._b.position($index * 2);
        this._b.put($u);
    }

    public void setPropertyV(int $index, float $v) {
        this._b.position(($index * 2) + 1);
        this._b.put($v);
    }

    public FloatBuffer buffer() {
        return this._b;
    }

    public UvBufferList clone() {
        this._b.position(0);
        return new UvBufferList(this._b, size());
    }
}
