package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.vos.Number3d;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

@EXClassNullAway
public class Number3dBufferList {
    public static final int BYTES_PER_PROPERTY = 4;
    public static final int PROPERTIES_PER_ELEMENT = 3;
    private FloatBuffer _b;
    private int _numElements = 0;

    public Number3dBufferList(FloatBuffer $b, int $size) {
        ByteBuffer bb = ByteBuffer.allocateDirect($b.limit() * 4);
        bb.order(ByteOrder.nativeOrder());
        this._b = bb.asFloatBuffer();
        this._b.put($b);
        this._numElements = $size;
    }

    public Number3dBufferList(int $maxElements) {
        ByteBuffer bb = ByteBuffer.allocateDirect($maxElements * 3 * 4);
        bb.order(ByteOrder.nativeOrder());
        this._b = bb.asFloatBuffer();
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

    public Number3d getAsNumber3d(int $index) {
        this._b.position($index * 3);
        return new Number3d(this._b.get(), this._b.get(), this._b.get());
    }

    public void putInNumber3d(int $index, Number3d $number3d) {
        this._b.position($index * 3);
        $number3d.x = this._b.get();
        $number3d.y = this._b.get();
        $number3d.z = this._b.get();
    }

    public float getPropertyX(int $index) {
        this._b.position($index * 3);
        return this._b.get();
    }

    public float getPropertyY(int $index) {
        this._b.position(($index * 3) + 1);
        return this._b.get();
    }

    public float getPropertyZ(int $index) {
        this._b.position(($index * 3) + 2);
        return this._b.get();
    }

    public void add(Number3d $n) {
        set(this._numElements, $n);
        this._numElements++;
    }

    public void add(float $x, float $y, float $z) {
        set(this._numElements, $x, $y, $z);
        this._numElements++;
    }

    public void set(int $index, Number3d $n) {
        this._b.position($index * 3);
        this._b.put($n.x);
        this._b.put($n.y);
        this._b.put($n.z);
    }

    public void set(int $index, float $x, float $y, float $z) {
        this._b.position($index * 3);
        this._b.put($x);
        this._b.put($y);
        this._b.put($z);
    }

    public void setPropertyX(int $index, float $x) {
        this._b.position($index * 3);
        this._b.put($x);
    }

    public void setPropertyY(int $index, float $y) {
        this._b.position(($index * 3) + 1);
        this._b.put($y);
    }

    public void setPropertyZ(int $index, float $z) {
        this._b.position(($index * 3) + 2);
        this._b.put($z);
    }

    public FloatBuffer buffer() {
        return this._b;
    }

    public void overwrite(float[] $newVals) {
        this._b.position(0);
        this._b.put($newVals);
    }

    public Number3dBufferList clone() {
        this._b.position(0);
        return new Number3dBufferList(this._b, size());
    }
}
