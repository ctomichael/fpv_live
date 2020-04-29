package dji.thirdparty.ciphersql;

import android.database.CharArrayBuffer;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class CursorWindow extends android.database.CursorWindow implements Parcelable {
    public static final Parcelable.Creator<CursorWindow> CREATOR = new Parcelable.Creator<CursorWindow>() {
        /* class dji.thirdparty.ciphersql.CursorWindow.AnonymousClass1 */

        public CursorWindow createFromParcel(Parcel source) {
            return new CursorWindow(source, 0);
        }

        public CursorWindow[] newArray(int size) {
            return new CursorWindow[size];
        }
    };
    private int mStartPos;
    private long nWindow;

    private native boolean allocRow_native();

    private native void close_native();

    private native char[] copyStringToBuffer_native(int i, int i2, int i3, CharArrayBuffer charArrayBuffer);

    private native void freeLastRow_native();

    private native byte[] getBlob_native(int i, int i2);

    private native double getDouble_native(int i, int i2);

    private native long getLong_native(int i, int i2);

    private native int getNumRows_native();

    private native String getString_native(int i, int i2);

    private native int getType_native(int i, int i2);

    private native boolean isBlob_native(int i, int i2);

    private native boolean isFloat_native(int i, int i2);

    private native boolean isInteger_native(int i, int i2);

    private native boolean isNull_native(int i, int i2);

    private native boolean isString_native(int i, int i2);

    private native void native_clear();

    private native IBinder native_getBinder();

    private native void native_init(IBinder iBinder);

    private native void native_init(boolean z);

    private native boolean putBlob_native(byte[] bArr, int i, int i2);

    private native boolean putDouble_native(double d, int i, int i2);

    private native boolean putLong_native(long j, int i, int i2);

    private native boolean putNull_native(int i, int i2);

    private native boolean putString_native(String str, int i, int i2);

    private native boolean setNumColumns_native(int i);

    public CursorWindow(boolean localWindow) {
        super(localWindow);
        this.mStartPos = 0;
        native_init(localWindow);
    }

    public int getStartPosition() {
        return this.mStartPos;
    }

    public void setStartPosition(int pos) {
        this.mStartPos = pos;
    }

    public int getNumRows() {
        acquireReference();
        try {
            return getNumRows_native();
        } finally {
            releaseReference();
        }
    }

    public boolean setNumColumns(int columnNum) {
        acquireReference();
        try {
            return setNumColumns_native(columnNum);
        } finally {
            releaseReference();
        }
    }

    public boolean allocRow() {
        acquireReference();
        try {
            return allocRow_native();
        } finally {
            releaseReference();
        }
    }

    public void freeLastRow() {
        acquireReference();
        try {
            freeLastRow_native();
        } finally {
            releaseReference();
        }
    }

    public boolean putBlob(byte[] value, int row, int col) {
        acquireReference();
        try {
            return putBlob_native(value, row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean putString(String value, int row, int col) {
        acquireReference();
        try {
            return putString_native(value, row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean putLong(long value, int row, int col) {
        acquireReference();
        try {
            return putLong_native(value, row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean putDouble(double value, int row, int col) {
        acquireReference();
        try {
            return putDouble_native(value, row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean putNull(int row, int col) {
        acquireReference();
        try {
            return putNull_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean isNull(int row, int col) {
        acquireReference();
        try {
            return isNull_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public byte[] getBlob(int row, int col) {
        acquireReference();
        try {
            return getBlob_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public int getType(int row, int col) {
        acquireReference();
        try {
            return getType_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean isBlob(int row, int col) {
        acquireReference();
        try {
            return isBlob_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean isLong(int row, int col) {
        acquireReference();
        try {
            return isInteger_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean isFloat(int row, int col) {
        acquireReference();
        try {
            return isFloat_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public boolean isString(int row, int col) {
        acquireReference();
        try {
            return isString_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public String getString(int row, int col) {
        acquireReference();
        try {
            return getString_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public void copyStringToBuffer(int row, int col, CharArrayBuffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("CharArrayBuffer should not be null");
        }
        if (buffer.data == null) {
            buffer.data = new char[64];
        }
        acquireReference();
        try {
            char[] newbuf = copyStringToBuffer_native(row - this.mStartPos, col, buffer.data.length, buffer);
            if (newbuf != null) {
                buffer.data = newbuf;
            }
        } finally {
            releaseReference();
        }
    }

    public long getLong(int row, int col) {
        acquireReference();
        try {
            return getLong_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public double getDouble(int row, int col) {
        acquireReference();
        try {
            return getDouble_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public short getShort(int row, int col) {
        acquireReference();
        try {
            return (short) ((int) getLong_native(row - this.mStartPos, col));
        } finally {
            releaseReference();
        }
    }

    public int getInt(int row, int col) {
        acquireReference();
        try {
            return (int) getLong_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public float getFloat(int row, int col) {
        acquireReference();
        try {
            return (float) getDouble_native(row - this.mStartPos, col);
        } finally {
            releaseReference();
        }
    }

    public void clear() {
        acquireReference();
        try {
            this.mStartPos = 0;
            native_clear();
        } finally {
            releaseReference();
        }
    }

    public void close() {
        releaseReference();
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        if (this.nWindow != 0) {
            close_native();
        }
    }

    public static CursorWindow newFromParcel(Parcel p) {
        return CREATOR.createFromParcel(p);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStrongBinder(native_getBinder());
        dest.writeInt(this.mStartPos);
    }

    public CursorWindow(Parcel source, int foo) {
        super(true);
        IBinder nativeBinder = source.readStrongBinder();
        this.mStartPos = source.readInt();
        native_init(nativeBinder);
    }

    /* access modifiers changed from: protected */
    public void onAllReferencesReleased() {
        close_native();
        super.onAllReferencesReleased();
    }
}
