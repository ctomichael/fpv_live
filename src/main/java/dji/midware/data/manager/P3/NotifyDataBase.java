package dji.midware.data.manager.P3;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.interfaces.DJIDataSyncNotifyListener;

@EXClassNullAway
public abstract class NotifyDataBase extends DataBase implements DJIDataSyncNotifyListener, DJIDataSyncListener {
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.midware.interfaces.DJIDataSyncNotifyListener.start(dji.midware.interfaces.DJIDataCallBack, boolean):void
     arg types: [dji.midware.interfaces.DJIDataCallBack, int]
     candidates:
      dji.midware.data.manager.P3.DataBase.start(dji.midware.data.packages.P3.SendPack, dji.midware.interfaces.DJIDataCallBack):void
      dji.midware.interfaces.DJIDataSyncNotifyListener.start(dji.midware.interfaces.DJIDataCallBack, boolean):void */
    public void start(DJIDataCallBack callBack) {
        start(callBack, true);
    }
}
