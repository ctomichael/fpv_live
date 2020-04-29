package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;
import dji.midware.util.BytesUtil;
import org.bouncycastle.asn1.eac.CertificateBody;

@Keep
@EXClassNullAway
public class DataOsdGetPushSignalQuality extends DataBase {
    private static DataOsdGetPushSignalQuality instance = null;
    private int Aerial1DownSignalQuality;
    private int Aerial1UpSignalQuality;
    private int Aerial2DownSignalQuality;
    private int Aerial2UpSignalQuality;
    private int downSignalQuality;
    private int upSignalQuality;

    public static synchronized DataOsdGetPushSignalQuality getInstance() {
        DataOsdGetPushSignalQuality dataOsdGetPushSignalQuality;
        synchronized (DataOsdGetPushSignalQuality.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSignalQuality();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = false;
            }
            dataOsdGetPushSignalQuality = instance;
        }
        return dataOsdGetPushSignalQuality;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        super.setPushRecPack(pack);
    }

    public boolean isGetRcQuality() {
        return (this._recData == null || (this._recData[0] & 128) == 0) ? false : true;
    }

    public int getUpSignalQuality() {
        if (!(this._recData == null || (this._recData[0] & 128) == 0)) {
            this.upSignalQuality = ((Integer) get(0, 1, Integer.class)).intValue() & CertificateBody.profileType;
        }
        return this.upSignalQuality;
    }

    public int getDownSignalQuality() {
        if (this._recData != null && (this._recData[0] & 128) == 0) {
            this.downSignalQuality = ((Integer) get(0, 1, Integer.class)).intValue() & CertificateBody.profileType;
        }
        return this.downSignalQuality;
    }

    public int getAerial1UpSignalQuality() {
        if (!(this._recData == null || (this._recData[0] & 128) == 0)) {
            this.Aerial1UpSignalQuality = BytesUtil.getSignedInt(this._recData[1]);
        }
        return this.Aerial1UpSignalQuality;
    }

    public int getAerial1DownSignalQuality() {
        if (this._recData != null && (this._recData[0] & 128) == 0) {
            this.Aerial1DownSignalQuality = BytesUtil.getSignedInt(this._recData[1]);
        }
        return this.Aerial1DownSignalQuality;
    }

    public int getAerial2UpSignalQuality() {
        if (!(this._recData == null || (this._recData[0] & 128) == 0)) {
            this.Aerial2UpSignalQuality = BytesUtil.getSignedInt(this._recData[2]);
        }
        return this.Aerial2UpSignalQuality;
    }

    public int getAerial2DownSignalQuality() {
        if (this._recData != null && (this._recData[0] & 128) == 0) {
            this.Aerial2DownSignalQuality = BytesUtil.getSignedInt(this._recData[2]);
        }
        return this.Aerial2DownSignalQuality;
    }

    public byte[] getResource() {
        return this._recData;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public String toString() {
        return "DataOsdGetPushSignalQuality{upSignalQuality=" + getUpSignalQuality() + ", downSignalQuality=" + getDownSignalQuality() + '}';
    }
}
