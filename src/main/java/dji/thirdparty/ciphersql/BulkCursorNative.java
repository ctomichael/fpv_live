package dji.thirdparty.ciphersql;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import dji.thirdparty.ciphersql.IContentObserver;

public abstract class BulkCursorNative extends Binder implements IBulkCursor {
    public BulkCursorNative() {
        attachInterface(this, IBulkCursor.descriptor);
    }

    public static IBulkCursor asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IBulkCursor in2 = (IBulkCursor) obj.queryLocalInterface(IBulkCursor.descriptor);
        return in2 == null ? new BulkCursorProxy(obj) : in2;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case 1:
                try {
                    data.enforceInterface(IBulkCursor.descriptor);
                    CursorWindow window = getWindow(data.readInt());
                    if (window == null) {
                        reply.writeInt(0);
                        return true;
                    }
                    reply.writeNoException();
                    reply.writeInt(1);
                    window.writeToParcel(reply, 0);
                    return true;
                } catch (Exception e) {
                    DatabaseUtils.writeExceptionToParcel(reply, e);
                    return true;
                }
            case 2:
                data.enforceInterface(IBulkCursor.descriptor);
                int count = count();
                reply.writeNoException();
                reply.writeInt(count);
                return true;
            case 3:
                data.enforceInterface(IBulkCursor.descriptor);
                String[] columnNames = getColumnNames();
                reply.writeNoException();
                reply.writeInt(columnNames.length);
                for (String str : columnNames) {
                    reply.writeString(str);
                }
                return true;
            case 4:
                data.enforceInterface(IBulkCursor.descriptor);
                boolean result = updateRows(data.readHashMap(null));
                reply.writeNoException();
                reply.writeInt(result ? 1 : 0);
                return true;
            case 5:
                data.enforceInterface(IBulkCursor.descriptor);
                boolean result2 = deleteRow(data.readInt());
                reply.writeNoException();
                reply.writeInt(result2 ? 1 : 0);
                return true;
            case 6:
                data.enforceInterface(IBulkCursor.descriptor);
                deactivate();
                reply.writeNoException();
                return true;
            case 7:
                data.enforceInterface(IBulkCursor.descriptor);
                int count2 = requery(IContentObserver.Stub.asInterface(data.readStrongBinder()), CursorWindow.CREATOR.createFromParcel(data));
                reply.writeNoException();
                reply.writeInt(count2);
                reply.writeBundle(getExtras());
                return true;
            case 8:
                data.enforceInterface(IBulkCursor.descriptor);
                onMove(data.readInt());
                reply.writeNoException();
                return true;
            case 9:
                data.enforceInterface(IBulkCursor.descriptor);
                boolean result3 = getWantsAllOnMoveCalls();
                reply.writeNoException();
                reply.writeInt(result3 ? 1 : 0);
                return true;
            case 10:
                data.enforceInterface(IBulkCursor.descriptor);
                Bundle extras = getExtras();
                reply.writeNoException();
                reply.writeBundle(extras);
                return true;
            case 11:
                data.enforceInterface(IBulkCursor.descriptor);
                Bundle returnExtras = respond(data.readBundle(getClass().getClassLoader()));
                reply.writeNoException();
                reply.writeBundle(returnExtras);
                return true;
            case 12:
                data.enforceInterface(IBulkCursor.descriptor);
                close();
                reply.writeNoException();
                return true;
            default:
                return super.onTransact(code, data, reply, flags);
        }
    }

    public IBinder asBinder() {
        return this;
    }
}
