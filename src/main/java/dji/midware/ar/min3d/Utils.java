package dji.midware.ar.min3d;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.Object3d;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

@EXClassNullAway
public class Utils {
    private static final int BYTES_PER_FLOAT = 4;
    public static final float DEG = 0.017453292f;

    public static Bitmap makeBitmapFromResourceId(Context $context, int $id) {
        InputStream is = $context.getResources().openRawResource($id);
        try {
            return BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
    }

    public static Bitmap makeBitmapFromResourceId(int $id) {
        return makeBitmapFromResourceId(Shared.context(), $id);
    }

    public static void addQuad(Object3d $o, int $upperLeft, int $upperRight, int $lowerRight, int $lowerLeft) {
        $o.faces().add((short) $upperLeft, (short) $lowerRight, (short) $upperRight);
        $o.faces().add((short) $upperLeft, (short) $lowerLeft, (short) $lowerRight);
    }

    public static FloatBuffer makeFloatBuffer3(float $a, float $b, float $c) {
        ByteBuffer b = ByteBuffer.allocateDirect(12);
        b.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = b.asFloatBuffer();
        buffer.put($a);
        buffer.put($b);
        buffer.put($c);
        buffer.position(0);
        return buffer;
    }

    public static FloatBuffer makeFloatBuffer4(float $a, float $b, float $c, float $d) {
        ByteBuffer b = ByteBuffer.allocateDirect(16);
        b.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = b.asFloatBuffer();
        buffer.put($a);
        buffer.put($b);
        buffer.put($c);
        buffer.put($d);
        buffer.position(0);
        return buffer;
    }
}
