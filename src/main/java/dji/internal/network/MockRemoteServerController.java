package dji.internal.network;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class MockRemoteServerController extends RemoteServerController {
    private MockRemoteServerController() {
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final MockRemoteServerController INSTANCE = new MockRemoteServerController();

        private LazyHolder() {
        }
    }

    public static MockRemoteServerController getInstance() {
        return LazyHolder.INSTANCE;
    }
}
