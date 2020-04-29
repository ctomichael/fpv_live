package com.dji.csdkcommon;

import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.interfaces.CmdIdInterface;

public enum PackType {
    OFDMPushOsd(0, CmdSet.FLYC, CmdIdFlyc.CmdIdType.GetPushCommon),
    OFDMPushLowFreqOsd(1, CmdSet.FLYC, CmdIdFlyc.CmdIdType.GetPushHome),
    EnterUpgradeModeRequest(23, CmdSet.COMMON, CmdIdCommon.CmdIdType.RequestUpgrade),
    QueryDeviceState(24, CmdSet.COMMON, CmdIdCommon.CmdIdType.GetDeviceStatus),
    FirmwareDataReceiveRequest(25, CmdSet.COMMON, CmdIdCommon.CmdIdType.RequestReceiveData),
    FirmwareDataTransfer(26, CmdSet.COMMON, CmdIdCommon.CmdIdType.TranslateData),
    CommonFileTransfer(27, CmdSet.COMMON, CmdIdCommon.CmdIdType.TransferFile),
    FirmwareDataTransferFinish(28, CmdSet.COMMON, CmdIdCommon.CmdIdType.TranslateComplete),
    RebootDevice(29, CmdSet.COMMON, CmdIdCommon.CmdIdType.RestartDevice),
    GetVersion(30, CmdSet.COMMON, CmdIdCommon.CmdIdType.GetVersion),
    UpgradeStatePush(31, CmdSet.COMMON, CmdIdCommon.CmdIdType.GetPushUpgradeStatus),
    TrafficControl(32, CmdSet.RC, CmdIdRc.CmdIdType.GetPushRcFlowControl),
    GetDeviceFile(33, CmdSet.COMMON, CmdIdCommon.CmdIdType.GetCfgFile),
    UpgradeSetAction(34, CmdSet.COMMON, CmdIdCommon.CmdIdType.ControlUpgrade),
    DeviceRequestUpgradePack(35, CmdSet.COMMON, CmdIdCommon.CmdIdType.UpgradeSelfRequest),
    UNKNOWN(255, CmdSet.OTHER, CmdIdCommon.CmdIdType.Other);
    
    private static volatile PackType[] sValues = null;
    private CmdIdInterface mCmdId = CmdIdCommon.CmdIdType.Other;
    private CmdSet mCmdSet = CmdSet.OTHER;
    private boolean mHasResultCode = false;
    private int mValue;

    private PackType(int value, CmdSet cmdSet, CmdIdInterface cmdId) {
        this.mValue = value;
        this.mCmdSet = cmdSet;
        this.mCmdId = cmdId;
    }

    private PackType(int value, CmdSet cmdSet, CmdIdInterface cmdId, boolean hasResultCode) {
        this.mValue = value;
        this.mCmdSet = cmdSet;
        this.mCmdId = cmdId;
        this.mHasResultCode = hasResultCode;
    }

    public CmdSet getCmdSet() {
        return this.mCmdSet;
    }

    public CmdIdInterface getCmdId() {
        return this.mCmdId;
    }

    public static PackType findByValue(int value) {
        if (sValues == null) {
            sValues = values();
        }
        PackType[] packTypeArr = sValues;
        for (PackType ts : packTypeArr) {
            if (ts.mValue == value) {
                return ts;
            }
        }
        return UNKNOWN;
    }

    public static PackType findByCmd(int cmdSet, int cmdId) {
        if (sValues == null) {
            sValues = values();
        }
        PackType[] packTypeArr = sValues;
        for (PackType ts : packTypeArr) {
            if (ts.mCmdSet.value() == cmdSet && ts.mCmdId.value() == cmdId) {
                return ts;
            }
        }
        return UNKNOWN;
    }

    public boolean hasResultCode() {
        return this.mHasResultCode;
    }

    public int getValue() {
        return this.mValue;
    }
}
