package dji.midware.data.manager.P3;

import android.util.SparseBooleanArray;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.packages.P3.RecvPack;

public class DJIAirPackIdentifier {
    private SparseBooleanArray mCmdSetFilterMap = new SparseBooleanArray();
    private SparseBooleanArray mSenderTypeFilterMap = new SparseBooleanArray();

    DJIAirPackIdentifier() {
        this.mSenderTypeFilterMap.put(DeviceType.OFDM.value(), true);
        this.mSenderTypeFilterMap.put(DeviceType.DM368.value(), true);
        this.mSenderTypeFilterMap.put(DeviceType.FLYC.value(), true);
        this.mSenderTypeFilterMap.put(DeviceType.CAMERA.value(), true);
        this.mSenderTypeFilterMap.put(DeviceType.WIFI.value(), true);
        this.mSenderTypeFilterMap.put(DeviceType.SINGLE.value(), true);
        this.mSenderTypeFilterMap.put(DeviceType.BATTERY.value(), true);
        this.mCmdSetFilterMap.put(CmdSet.FLYC.value(), true);
        this.mCmdSetFilterMap.put(CmdSet.GIMBAL.value(), true);
        this.mCmdSetFilterMap.put(CmdSet.CAMERA.value(), true);
        this.mCmdSetFilterMap.put(CmdSet.CENTER.value(), true);
    }

    public boolean isAirPack(RecvPack pack) {
        return this.mSenderTypeFilterMap.get(pack.senderType) || this.mCmdSetFilterMap.get(pack.cmdSet) || (pack.cmdSet == 0 && pack.cmdId == 39);
    }
}
