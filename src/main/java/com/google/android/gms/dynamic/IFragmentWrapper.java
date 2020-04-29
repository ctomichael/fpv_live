package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.internal.common.zzb;
import com.google.android.gms.internal.common.zzc;

public interface IFragmentWrapper extends IInterface {

    public static abstract class Stub extends zzb implements IFragmentWrapper {
        public Stub() {
            super("com.google.android.gms.dynamic.IFragmentWrapper");
        }

        public static class zza extends com.google.android.gms.internal.common.zza implements IFragmentWrapper {
            zza(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.dynamic.IFragmentWrapper");
            }

            public final IObjectWrapper zzae() throws RemoteException {
                Parcel zza = zza(2, zza());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(zza.readStrongBinder());
                zza.recycle();
                return asInterface;
            }

            public final Bundle getArguments() throws RemoteException {
                Parcel zza = zza(3, zza());
                Bundle bundle = (Bundle) zzc.zza(zza, Bundle.CREATOR);
                zza.recycle();
                return bundle;
            }

            public final int getId() throws RemoteException {
                Parcel zza = zza(4, zza());
                int readInt = zza.readInt();
                zza.recycle();
                return readInt;
            }

            public final IFragmentWrapper zzaf() throws RemoteException {
                Parcel zza = zza(5, zza());
                IFragmentWrapper asInterface = Stub.asInterface(zza.readStrongBinder());
                zza.recycle();
                return asInterface;
            }

            public final IObjectWrapper zzag() throws RemoteException {
                Parcel zza = zza(6, zza());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(zza.readStrongBinder());
                zza.recycle();
                return asInterface;
            }

            public final boolean getRetainInstance() throws RemoteException {
                Parcel zza = zza(7, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final String getTag() throws RemoteException {
                Parcel zza = zza(8, zza());
                String readString = zza.readString();
                zza.recycle();
                return readString;
            }

            public final IFragmentWrapper zzah() throws RemoteException {
                Parcel zza = zza(9, zza());
                IFragmentWrapper asInterface = Stub.asInterface(zza.readStrongBinder());
                zza.recycle();
                return asInterface;
            }

            public final int getTargetRequestCode() throws RemoteException {
                Parcel zza = zza(10, zza());
                int readInt = zza.readInt();
                zza.recycle();
                return readInt;
            }

            public final boolean getUserVisibleHint() throws RemoteException {
                Parcel zza = zza(11, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final IObjectWrapper zzai() throws RemoteException {
                Parcel zza = zza(12, zza());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(zza.readStrongBinder());
                zza.recycle();
                return asInterface;
            }

            public final boolean isAdded() throws RemoteException {
                Parcel zza = zza(13, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final boolean isDetached() throws RemoteException {
                Parcel zza = zza(14, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final boolean isHidden() throws RemoteException {
                Parcel zza = zza(15, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final boolean isInLayout() throws RemoteException {
                Parcel zza = zza(16, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final boolean isRemoving() throws RemoteException {
                Parcel zza = zza(17, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final boolean isResumed() throws RemoteException {
                Parcel zza = zza(18, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final boolean isVisible() throws RemoteException {
                Parcel zza = zza(19, zza());
                boolean zza2 = zzc.zza(zza);
                zza.recycle();
                return zza2;
            }

            public final void zza(IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel zza = zza();
                zzc.zza(zza, iObjectWrapper);
                zzb(20, zza);
            }

            public final void setHasOptionsMenu(boolean z) throws RemoteException {
                Parcel zza = zza();
                zzc.writeBoolean(zza, z);
                zzb(21, zza);
            }

            public final void setMenuVisibility(boolean z) throws RemoteException {
                Parcel zza = zza();
                zzc.writeBoolean(zza, z);
                zzb(22, zza);
            }

            public final void setRetainInstance(boolean z) throws RemoteException {
                Parcel zza = zza();
                zzc.writeBoolean(zza, z);
                zzb(23, zza);
            }

            public final void setUserVisibleHint(boolean z) throws RemoteException {
                Parcel zza = zza();
                zzc.writeBoolean(zza, z);
                zzb(24, zza);
            }

            public final void startActivity(Intent intent) throws RemoteException {
                Parcel zza = zza();
                zzc.zza(zza, intent);
                zzb(25, zza);
            }

            public final void startActivityForResult(Intent intent, int i) throws RemoteException {
                Parcel zza = zza();
                zzc.zza(zza, intent);
                zza.writeInt(i);
                zzb(26, zza);
            }

            public final void zzb(IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel zza = zza();
                zzc.zza(zza, iObjectWrapper);
                zzb(27, zza);
            }
        }

        public static IFragmentWrapper asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IFragmentWrapper");
            if (queryLocalInterface instanceof IFragmentWrapper) {
                return (IFragmentWrapper) queryLocalInterface;
            }
            return new zza(iBinder);
        }

        /* access modifiers changed from: protected */
        public final boolean zza(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 2:
                    IObjectWrapper zzae = zzae();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, zzae);
                    break;
                case 3:
                    Bundle arguments = getArguments();
                    parcel2.writeNoException();
                    zzc.zzb(parcel2, arguments);
                    break;
                case 4:
                    int id = getId();
                    parcel2.writeNoException();
                    parcel2.writeInt(id);
                    break;
                case 5:
                    IFragmentWrapper zzaf = zzaf();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, zzaf);
                    break;
                case 6:
                    IObjectWrapper zzag = zzag();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, zzag);
                    break;
                case 7:
                    boolean retainInstance = getRetainInstance();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, retainInstance);
                    break;
                case 8:
                    String tag = getTag();
                    parcel2.writeNoException();
                    parcel2.writeString(tag);
                    break;
                case 9:
                    IFragmentWrapper zzah = zzah();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, zzah);
                    break;
                case 10:
                    int targetRequestCode = getTargetRequestCode();
                    parcel2.writeNoException();
                    parcel2.writeInt(targetRequestCode);
                    break;
                case 11:
                    boolean userVisibleHint = getUserVisibleHint();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, userVisibleHint);
                    break;
                case 12:
                    IObjectWrapper zzai = zzai();
                    parcel2.writeNoException();
                    zzc.zza(parcel2, zzai);
                    break;
                case 13:
                    boolean isAdded = isAdded();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, isAdded);
                    break;
                case 14:
                    boolean isDetached = isDetached();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, isDetached);
                    break;
                case 15:
                    boolean isHidden = isHidden();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, isHidden);
                    break;
                case 16:
                    boolean isInLayout = isInLayout();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, isInLayout);
                    break;
                case 17:
                    boolean isRemoving = isRemoving();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, isRemoving);
                    break;
                case 18:
                    boolean isResumed = isResumed();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, isResumed);
                    break;
                case 19:
                    boolean isVisible = isVisible();
                    parcel2.writeNoException();
                    zzc.writeBoolean(parcel2, isVisible);
                    break;
                case 20:
                    zza(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                case 21:
                    setHasOptionsMenu(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 22:
                    setMenuVisibility(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 23:
                    setRetainInstance(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 24:
                    setUserVisibleHint(zzc.zza(parcel));
                    parcel2.writeNoException();
                    break;
                case 25:
                    startActivity((Intent) zzc.zza(parcel, Intent.CREATOR));
                    parcel2.writeNoException();
                    break;
                case 26:
                    startActivityForResult((Intent) zzc.zza(parcel, Intent.CREATOR), parcel.readInt());
                    parcel2.writeNoException();
                    break;
                case 27:
                    zzb(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    Bundle getArguments() throws RemoteException;

    int getId() throws RemoteException;

    boolean getRetainInstance() throws RemoteException;

    String getTag() throws RemoteException;

    int getTargetRequestCode() throws RemoteException;

    boolean getUserVisibleHint() throws RemoteException;

    boolean isAdded() throws RemoteException;

    boolean isDetached() throws RemoteException;

    boolean isHidden() throws RemoteException;

    boolean isInLayout() throws RemoteException;

    boolean isRemoving() throws RemoteException;

    boolean isResumed() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void setHasOptionsMenu(boolean z) throws RemoteException;

    void setMenuVisibility(boolean z) throws RemoteException;

    void setRetainInstance(boolean z) throws RemoteException;

    void setUserVisibleHint(boolean z) throws RemoteException;

    void startActivity(Intent intent) throws RemoteException;

    void startActivityForResult(Intent intent, int i) throws RemoteException;

    void zza(IObjectWrapper iObjectWrapper) throws RemoteException;

    IObjectWrapper zzae() throws RemoteException;

    IFragmentWrapper zzaf() throws RemoteException;

    IObjectWrapper zzag() throws RemoteException;

    IFragmentWrapper zzah() throws RemoteException;

    IObjectWrapper zzai() throws RemoteException;

    void zzb(IObjectWrapper iObjectWrapper) throws RemoteException;
}
