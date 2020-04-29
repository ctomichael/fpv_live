package dji.midware.data.manager.packplugin.record;

import android.os.Environment;
import android.text.format.DateFormat;
import com.google.protobuf.ByteString;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdIdSimulator;
import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.manager.packplugin.record.CmdPackPlugin;
import dji.midware.data.packages.P3.Pack;
import dji.midware.interfaces.CmdIdInterface;
import dji.midware.util.ContextUtil;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DJIPackRecordPlugin {
    private static final String DATA_FORMAT = "yyyy-MM-dd-hh-mm-ss";
    private static final boolean IS_OPEN = true;
    private static DJIPackRecordPlugin instance = null;
    private Set<Integer> mRecvPackBlackList = new HashSet();
    private DJIPackRecordSaveEngine mSaveEngine;
    private Set<Integer> mSendPackBlackList = new HashSet();

    public static synchronized DJIPackRecordPlugin getInstance() {
        DJIPackRecordPlugin dJIPackRecordPlugin;
        synchronized (DJIPackRecordPlugin.class) {
            if (instance == null) {
                instance = new DJIPackRecordPlugin();
            }
            dJIPackRecordPlugin = instance;
        }
        return dJIPackRecordPlugin;
    }

    private DJIPackRecordPlugin() {
        String savePath = Environment.getExternalStorageDirectory() + "/DJI/" + ContextUtil.getContext().getPackageName() + "/LOG/CACHE/command_record/";
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        initBlackList();
        this.mSaveEngine = new DJIPackRecordSaveEngine(savePath + ((Object) DateFormat.format(DATA_FORMAT, new Date())) + ".dat");
    }

    private void initBlackList() {
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.FLYC, CmdIdFlyc.CmdIdType.GetPushFlightRecord)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.FLYC, CmdIdFlyc.CmdIdType.GetParamsByHash)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.RequestFile)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.GetCfgFile)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.GetPushLog)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.GetPushFiles)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.GetPushFile)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.TransferFile)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.OSD, CmdIdOsd.CmdIdType.GetPushSdrStatusInfo)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.OSD, CmdIdOsd.CmdIdType.GetPushSdrStatusGroundInfo)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.SIMULATOR, CmdIdSimulator.CmdIdType.GetPushFlightStatusParams)));
        this.mRecvPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.WIFI, CmdIdWifi.CmdIdType.GetPushLog)));
        this.mSendPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.FLYC, CmdIdFlyc.CmdIdType.GetPushFlightRecord)));
        this.mSendPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.TranslateData)));
        this.mSendPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.TransferFile)));
        this.mSendPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.HeartBeat)));
        this.mSendPackBlackList.add(Integer.valueOf(getSimpleHashOfPack(CmdSet.COMMON, CmdIdCommon.CmdIdType.GetCfgFile)));
    }

    private int getSimpleHashOfPack(CmdSet cmdset, CmdIdInterface cmdid) {
        return getSimpleHashOfPack4Int(cmdset.value(), cmdid.value());
    }

    private int getSimpleHashOfPack4Int(int cmdset, int cmdid) {
        return (cmdset * 257) + cmdid;
    }

    public void savePack(Pack pack, PackType4Plugin packType) {
        if (packType == PackType4Plugin.PUSH && this.mRecvPackBlackList.contains(Integer.valueOf(getSimpleHashOfPack4Int(pack.cmdSet, pack.cmdId)))) {
            return;
        }
        if ((packType != PackType4Plugin.SEND && packType != PackType4Plugin.ACK) || !this.mSendPackBlackList.contains(Integer.valueOf(getSimpleHashOfPack4Int(pack.cmdSet, pack.cmdId)))) {
            CmdPackPlugin.DJIV1Pack4Save.Builder builder = CmdPackPlugin.DJIV1Pack4Save.newBuilder();
            builder.setCmdSet(pack.cmdSet).setCmdId(pack.cmdId).setSenderType(pack.senderType).setSenderId(pack.senderId).setSeq(pack.seq).setCmdtype(packType.value()).setTimestamp(System.currentTimeMillis());
            if (pack.data != null) {
                builder.setData(ByteString.copyFrom(pack.data));
            }
            this.mSaveEngine.onPackComing(builder.build());
        }
    }

    public void onDestroy() {
        this.mSaveEngine.onDestroy();
    }

    public enum PackType4Plugin {
        PUSH(0),
        ACK(1),
        SEND(2),
        UNKNOWN(255);
        
        private static PackType4Plugin[] values = values();
        private int data;

        private PackType4Plugin(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        private boolean _equals(int b) {
            return this.data == b;
        }

        public static PackType4Plugin find(int b) {
            PackType4Plugin result = UNKNOWN;
            for (int i = 0; i < values.length; i++) {
                if (values[i]._equals(b)) {
                    return values[i];
                }
            }
            return result;
        }
    }
}
