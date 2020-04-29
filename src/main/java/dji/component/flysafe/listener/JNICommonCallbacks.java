package dji.component.flysafe.listener;

import android.support.annotation.Keep;

@Keep
public class JNICommonCallbacks {
    private static int PackManagerUnknownError = 300;

    @Keep
    public interface JNICommonCallbackWith<T> {
        void onResult(T t);
    }

    @Keep
    public interface JNICommonCallback {
        void onFailure(int i);

        void onSuccess();
    }

    @Keep
    public interface JNIEnumCallback {
        void onFailure(DatabaseErrorJni databaseErrorJni);

        void onSuccess();
    }

    static /* synthetic */ int access$004() {
        int i = PackManagerUnknownError + 1;
        PackManagerUnknownError = i;
        return i;
    }

    @Keep
    public enum DatabaseErrorJni {
        DataBaseErrorStart(JNICommonCallbacks.access$004()),
        DataBaseFileError(JNICommonCallbacks.access$004()),
        DataBaseInvalidParams(JNICommonCallbacks.access$004()),
        DataBaseNotReady(JNICommonCallbacks.access$004()),
        DataBaseFileUnknownError(JNICommonCallbacks.access$004()),
        OTHER(10000);
        
        private static volatile DatabaseErrorJni[] sValues = null;
        int mValue = -1;

        private DatabaseErrorJni(int i) {
            this.mValue = i;
        }

        public int getValue() {
            return this.mValue;
        }

        public static DatabaseErrorJni ofValue(int value) {
            if (sValues == null) {
                sValues = values();
            }
            DatabaseErrorJni[] databaseErrorJniArr = sValues;
            for (DatabaseErrorJni ts : databaseErrorJniArr) {
                if (ts.mValue == value) {
                    return ts;
                }
            }
            return OTHER;
        }
    }
}
