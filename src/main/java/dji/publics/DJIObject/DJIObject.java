package dji.publics.DJIObject;

public abstract class DJIObject {
    /* access modifiers changed from: protected */
    public abstract void onCreate();

    public abstract void onDestroy();

    public DJIObject() {
        onCreate();
    }
}
