package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogUtils;
import dji.logic.album.manager.litchis.DJIFileType;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Keep
@EXClassNullAway
public class DataCameraGetSpecialFileParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetSpecialFileParams instance = null;
    private int index;
    private int subIndex;

    public static synchronized DataCameraGetSpecialFileParams getInstance() {
        DataCameraGetSpecialFileParams dataCameraGetSpecialFileParams;
        synchronized (DataCameraGetSpecialFileParams.class) {
            if (instance == null) {
                instance = new DataCameraGetSpecialFileParams();
            }
            dataCameraGetSpecialFileParams = instance;
        }
        return dataCameraGetSpecialFileParams;
    }

    public DataCameraGetSpecialFileParams setIndex(int index2) {
        this.index = index2;
        return this;
    }

    public DataCameraGetSpecialFileParams setSubindex(int subIndex2) {
        this.subIndex = subIndex2;
        return this;
    }

    public int getSubImageNum() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DJIFileType getFileType() {
        return DJIFileType.find(((Integer) get(1, 1, Integer.class)).intValue());
    }

    public int getFileSize() {
        return ((Integer) get(2, 8, Integer.class)).intValue();
    }

    public long getOrgCreateTimestamp() {
        return (long) ((Integer) get(10, 4, Integer.class)).intValue();
    }

    public long getCreateTimestamp() {
        return parseTime((long) ((Integer) get(10, 4, Integer.class)).intValue()).getTime();
    }

    public Date getCreateDate() {
        return parseTime((long) ((Integer) get(10, 4, Integer.class)).intValue());
    }

    public Date parseTime(long time) {
        int month = (int) ((time >> 21) & 15);
        int day = (int) ((time >> 16) & 31);
        int hour = (int) ((time >> 11) & 31);
        int min = (int) ((time >> 5) & 63);
        int sec = (int) (31 & time);
        try {
            return new SimpleDateFormat(DJILogUtils.FORMAT_2, Locale.ENGLISH).parse(((int) (1980 + (time >> 25))) + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        System.arraycopy(BytesUtil.getBytes(this.index), 0, this._sendData, 0, 4);
        this._sendData[4] = (byte) this.subIndex;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetPanoFileParams.value();
        start(pack, callBack);
    }
}
