package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import dji.component.flysafe.util.ProtobufHelper;
import dji.flysafe.v3.License;
import dji.flysafe.v3.LicenseDataHeight;
import dji.midware.data.model.P3.DataWhiteListRequestLicense;

@Keep
public class HeightUnlockLicense extends WhiteListLicense {
    private int mHeight = -1;

    public HeightUnlockLicense() {
    }

    public HeightUnlockLicense(DataWhiteListRequestLicense licenseData, int index) {
        super(licenseData, index);
    }

    public HeightUnlockLicense(License protobufLicense) {
        super(protobufLicense);
        LicenseDataHeight prtbHeight = protobufLicense.data.height;
        if (prtbHeight != null) {
            this.mHeight = ProtobufHelper.toInt(prtbHeight.height_limit);
        }
    }
}
