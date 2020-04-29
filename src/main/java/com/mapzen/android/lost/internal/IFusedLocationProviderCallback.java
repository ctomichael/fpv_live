package com.mapzen.android.lost.internal;

import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.mapzen.android.lost.api.LocationAvailability;

public interface IFusedLocationProviderCallback extends IInterface {
    void onLocationAvailabilityChanged(LocationAvailability locationAvailability) throws RemoteException;

    void onLocationChanged(Location location) throws RemoteException;

    long pid() throws RemoteException;

    public static abstract class Stub extends Binder implements IFusedLocationProviderCallback {
        private static final String DESCRIPTOR = "com.mapzen.android.lost.internal.IFusedLocationProviderCallback";
        static final int TRANSACTION_onLocationAvailabilityChanged = 3;
        static final int TRANSACTION_onLocationChanged = 2;
        static final int TRANSACTION_pid = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IFusedLocationProviderCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IFusedLocationProviderCallback)) {
                return new Proxy(obj);
            }
            return (IFusedLocationProviderCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            LocationAvailability _arg0;
            Location _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    long _result = pid();
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (Location) Location.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onLocationChanged(_arg02);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = LocationAvailability.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onLocationAvailabilityChanged(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IFusedLocationProviderCallback {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public long pid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readLong();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onLocationChanged(Location location) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onLocationAvailabilityChanged(LocationAvailability locationAvailability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (locationAvailability != null) {
                        _data.writeInt(1);
                        locationAvailability.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
