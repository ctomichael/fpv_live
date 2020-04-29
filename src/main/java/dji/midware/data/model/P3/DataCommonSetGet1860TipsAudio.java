package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.pilot.publics.model.ICameraResMode;

@Keep
@EXClassNullAway
public class DataCommonSetGet1860TipsAudio extends DataBase implements DJIDataSyncListener {
    private static DataCommonSetGet1860TipsAudio instance = null;
    private AudioSubCmd mAudioSubCmd = AudioSubCmd.OTHER;

    public static synchronized DataCommonSetGet1860TipsAudio getInstance() {
        DataCommonSetGet1860TipsAudio dataCommonSetGet1860TipsAudio;
        synchronized (DataCommonSetGet1860TipsAudio.class) {
            if (instance == null) {
                instance = new DataCommonSetGet1860TipsAudio();
            }
            dataCommonSetGet1860TipsAudio = instance;
        }
        return dataCommonSetGet1860TipsAudio;
    }

    public DataCommonSetGet1860TipsAudio setAudioSubCmd(AudioSubCmd audioSubCmd) {
        this.mAudioSubCmd = audioSubCmd;
        return this;
    }

    public boolean isTipsAudioOpened() {
        if (this._recData == null || this._recData.length <= 0) {
            return true;
        }
        return ((Integer) get(0, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mAudioSubCmd.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DM368.value();
        pack.receiverId = 1;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.SetGetTipsAudio.value();
        start(pack, callBack);
    }

    @Keep
    public enum AudioSubCmd {
        DISABLE_SOUND(ICameraResMode.ICameraVideoResolutionRes.VR_MAX),
        ENABLE_SOUND(254),
        GET_SOUND(255),
        OTHER(-1);
        
        private int mValue;

        private AudioSubCmd(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean _equals(int b) {
            return this.mValue == b;
        }

        public static AudioSubCmd find(int b) {
            AudioSubCmd result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}
