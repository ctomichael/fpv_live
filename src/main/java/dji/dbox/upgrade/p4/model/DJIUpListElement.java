package dji.dbox.upgrade.p4.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import java.net.URLDecoder;

@Keep
@EXClassNullAway
public class DJIUpListElement {
    public DJIUpCfgModel cfgModel;
    public String flow;
    public boolean isAllow;
    public boolean isFromSDCard;
    public boolean isUpEnforce;
    public String product_version;
    public ReleaseNote release_note;
    public String sdTarPath;

    public boolean isDeprecated() {
        return this.flow != null && this.flow.equals("deprecated");
    }

    @Keep
    public static class ReleaseNote {
        public String en;
        public String ja;
        public String zh_cn;
        public String zh_tw;

        public String get() {
            String result = "";
            if (this.ja != null) {
                result = this.ja;
            }
            if (this.zh_cn != null) {
                result = this.zh_cn;
            }
            if (this.zh_tw != null) {
                result = this.zh_tw;
            }
            if (this.en != null) {
                result = this.en;
            }
            try {
                return URLDecoder.decode(result.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B"), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
                return result;
            }
        }
    }
}
