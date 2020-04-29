package dji.component.activate.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.thirdparty.afinal.annotation.sqlite.Id;

@Keep
@EXClassNullAway
public class DJICareModel {
    public String CraftSN = "";
    public String GimbalSN = "";
    public String IsBuy = "";
    public String IsThird = "";
    public Integer LeftTime = 0;
    public String Slug = "";
    public Integer checkTime = 0;
    public boolean isGot = false;
    @Id
    public String mc = "";
    public Integer productType = 0;

    public boolean isCanBuy() {
        return this.IsBuy != null && (this.IsBuy.equals("null") || this.IsBuy.equals("0")) && remainTime() > 0;
    }

    public boolean isShowThird() {
        return this.IsThird != null && this.IsThird.equals("1");
    }

    public int remainTime() {
        return (int) (((long) this.LeftTime.intValue()) - ((System.currentTimeMillis() / 1000) - ((long) this.checkTime.intValue())));
    }

    public ProductType getProductTypes() {
        return ProductType.find(this.productType.intValue());
    }

    public String toString() {
        return "DJICareModel{mc='" + this.mc + '\'' + ", checkTime=" + this.checkTime + ", IsBuy='" + this.IsBuy + '\'' + ", IsThird='" + this.IsThird + '\'' + ", Slug='" + this.Slug + '\'' + ", CraftSN='" + this.CraftSN + '\'' + ", GimbalSN='" + this.GimbalSN + '\'' + ", LeftTime=" + this.LeftTime + ", isGot=" + this.isGot + ", productType=" + this.productType + '}';
    }
}
