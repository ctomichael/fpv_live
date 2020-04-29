package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.Iterator;

@Keep
@EXClassNullAway
public class DataCommonSetNewestVersions extends DataBase implements DJIDataSyncListener {
    private int mReceiverId = 0;
    private DeviceType mReceiverType = DeviceType.DM368;
    private ArrayList<ProductVersionObject> plist = new ArrayList<>();

    @Keep
    public static class ProductVersionObject {
        public String newestVersion;
        public String product_id;

        public byte[] getVersionBytes() {
            String version = this.newestVersion.replace(".", "");
            String v1 = version.substring(0, 2);
            String v2 = version.substring(2, 4);
            String v3 = version.substring(4, 6);
            String v4 = version.substring(6);
            Log.e("getGlassNewestVersion", "version");
            return new byte[]{BytesUtil.getUnsignedBytes(Short.parseShort(v1)), BytesUtil.getUnsignedBytes(Short.parseShort(v2)), BytesUtil.getUnsignedBytes(Short.parseShort(v3)), BytesUtil.getUnsignedBytes(Short.parseShort(v4))};
        }
    }

    public DataCommonSetNewestVersions setRecvType(DeviceType type) {
        this.mReceiverType = type;
        return this;
    }

    public DataCommonSetNewestVersions setRecevicerId(int id) {
        this.mReceiverId = id;
        return this;
    }

    public void addProductVersion(ProductVersionObject object) {
        this.plist.add(object);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[((this.plist.size() * 16) + 1)];
        this._sendData[0] = (byte) this.plist.size();
        Iterator<ProductVersionObject> it2 = this.plist.iterator();
        while (it2.hasNext()) {
            ProductVersionObject object = it2.next();
            byte[] pbytes = BytesUtil.getBytes(object.product_id);
            System.arraycopy(pbytes, 0, this._sendData, 1, pbytes.length);
            byte[] vbytes = object.getVersionBytes();
            System.arraycopy(vbytes, 0, this._sendData, 9, vbytes.length);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType.value();
        pack.receiverId = this.mReceiverId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.SetNewestVersions.value();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}
