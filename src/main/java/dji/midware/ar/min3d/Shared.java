package dji.midware.ar.min3d;

import android.content.Context;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.Renderer;
import dji.midware.ar.min3d.core.TextureManager;

@EXClassNullAway
public class Shared {
    private static Context _context;
    private static Renderer _renderer;
    private static TextureManager _textureManager;

    public static Context context() {
        return _context;
    }

    public static void context(Context $c) {
        _context = $c == null ? null : $c.getApplicationContext();
    }

    public static Renderer renderer() {
        return _renderer;
    }

    public static void renderer(Renderer $r) {
        _renderer = $r;
    }

    public static TextureManager textureManager() {
        return _textureManager;
    }

    public static void textureManager(TextureManager $bm) {
        _textureManager = $bm;
    }
}
