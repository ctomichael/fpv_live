package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataWhiteListRequestLicense;

@Keep
@EXClassNullAway
public class ParameterConfigurationLicense extends WhiteListLicense {
    private int type;
    private int[] values;

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public int[] getValues() {
        return this.values;
    }

    public void setValues(int[] values2) {
        this.values = values2;
    }

    public ParameterConfigurationLicense() {
    }

    public ParameterConfigurationLicense(DataWhiteListRequestLicense licenseData, int index) {
        super(licenseData, index);
        this.type = licenseData.getParameterConfigurationType();
        this.values = licenseData.getParameterConfigurationValues();
    }
}
