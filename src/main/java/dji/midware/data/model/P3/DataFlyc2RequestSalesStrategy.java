package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdFlyc2;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.util.ArrayList;
import java.util.List;

public class DataFlyc2RequestSalesStrategy extends DataBase implements DJIDataSyncListener {
    public boolean getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue() == 0;
    }

    public int getVersion() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public boolean isTakeOffRejected() {
        return ((Integer) get(2, 1, Integer.class)).intValue() == 1;
    }

    public int getStrategy() {
        return ((Integer) get(3, 2, Integer.class)).intValue();
    }

    public int getCurrentAreaCode() {
        return ((Integer) get(5, 2, Integer.class)).intValue();
    }

    public int getAllowToFlyAreaNumbers() {
        return ((Integer) get(7, 1, Integer.class)).intValue();
    }

    public List<Integer> getAllowToFlyAreaCodes() {
        int length = getAllowToFlyAreaNumbers();
        ArrayList<Integer> areaCodes = new ArrayList<>(length * 2);
        for (int i = 8; i <= (length * 2) + 7; i += 2) {
            areaCodes.add(Integer.valueOf(((Integer) get(i, 2, Integer.class)).intValue()));
        }
        return areaCodes;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC2.value();
        pack.cmdId = CmdIdFlyc2.CmdIdType.RequestSalesStrategy.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}
