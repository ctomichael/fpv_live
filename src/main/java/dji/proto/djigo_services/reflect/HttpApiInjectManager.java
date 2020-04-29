package dji.proto.djigo_services.reflect;

import com.dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class HttpApiInjectManager {
    private static HttpApiInjectManager instance;
    private HttpApiToP3Injectable httpApiToP3Injectable;

    public static HttpApiInjectManager getInstance() {
        if (instance == null) {
            instance = new HttpApiInjectManager();
        }
        return instance;
    }

    private HttpApiInjectManager() {
    }

    public static HttpApiToP3Injectable getHttpApiToP3Injectable() {
        return getInstance().httpApiToP3Injectable;
    }

    public static void setHttpApiToP3Injectable(HttpApiToP3Injectable httpApiToP3Injectable2) {
        getInstance().httpApiToP3Injectable = httpApiToP3Injectable2;
    }
}
