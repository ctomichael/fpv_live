package dji.midware.data.config.Dpad;

import com.adobe.xmp.XMPError;
import com.drew.metadata.exif.ExifDirectoryBase;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum DpadProductType {
    None(0, "Unknown"),
    Pomato(101, "GL300E"),
    PomatoSdr(102, "GL300K"),
    CrystalSkyA(XMPError.BADXML, "ZS600A"),
    CrystalSkyB(202, "ZS600B"),
    Mg1S(ExifDirectoryBase.TAG_TRANSFER_FUNCTION, "AG405"),
    PomatoRTK(302, "ag410"),
    RM500(401, "rm500");
    
    private String mName;
    private int mTag;

    private DpadProductType(int tag, String productName) {
        this.mTag = tag;
        this.mName = productName;
    }

    private boolean equals(String name) {
        return this.mName != null && this.mName.equalsIgnoreCase(name);
    }

    public String getName() {
        return this.mName;
    }

    public int getTag() {
        return this.mTag;
    }

    public static DpadProductType find(String name) {
        DpadProductType result = None;
        if (name == null || name.trim().length() <= 0) {
            return result;
        }
        DpadProductType[] items = values();
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(name)) {
                return items[i];
            }
        }
        return result;
    }

    public String toString() {
        return super.toString();
    }

    public static boolean isValidType(DpadProductType type) {
        return (type == null || None == type) ? false : true;
    }
}
