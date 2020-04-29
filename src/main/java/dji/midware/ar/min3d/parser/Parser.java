package dji.midware.ar.min3d.parser;

import android.content.res.Resources;
import dji.fieldAnnotation.EXClassNullAway;
import java.io.InputStream;

@EXClassNullAway
public class Parser {

    public enum Type {
        OBJ,
        MAX_3DS,
        MD2
    }

    public static IParser createParser(Type type, Resources resources, String resourceID, boolean generateMipMap) {
        switch (type) {
            case OBJ:
                return new ObjParser(resources, resourceID, generateMipMap);
            case MAX_3DS:
                return new Max3DSParser(resources, resourceID, generateMipMap);
            case MD2:
                return new MD2Parser(resources, resourceID, generateMipMap);
            default:
                return null;
        }
    }

    public static IParser createParser(Type type, Resources resources, InputStream stream, boolean generateMipMap) {
        switch (type) {
            case OBJ:
                return new ObjParser(resources, stream, generateMipMap);
            case MAX_3DS:
                return new Max3DSParser(resources, stream, generateMipMap);
            case MD2:
                return new MD2Parser(resources, stream, generateMipMap);
            default:
                return null;
        }
    }
}
