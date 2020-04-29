package dji.thirdparty.okhttp3;

import java.io.IOException;
import javax.annotation.Nullable;

public interface Authenticator {
    public static final Authenticator NONE = new Authenticator() {
        /* class dji.thirdparty.okhttp3.Authenticator.AnonymousClass1 */

        public Request authenticate(Route route, Response response) {
            return null;
        }
    };

    @Nullable
    Request authenticate(Route route, Response response) throws IOException;
}
