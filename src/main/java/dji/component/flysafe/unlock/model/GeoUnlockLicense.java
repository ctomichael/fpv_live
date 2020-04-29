package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.v3.License;
import dji.flysafe.v3.LicenseDataArea;
import dji.midware.data.model.P3.DataWhiteListRequestLicense;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class GeoUnlockLicense extends WhiteListLicense {
    private List<Integer> areaIds = new ArrayList();

    public void setAreaIds(List<Integer> areaIds2) {
        this.areaIds = new ArrayList(areaIds2);
    }

    public List<Integer> getAreaIds() {
        return this.areaIds;
    }

    public GeoUnlockLicense(DataWhiteListRequestLicense licenseData, int index) {
        super(licenseData, index);
        this.areaIds = new ArrayList(licenseData.getGEOAreaIds());
    }

    public GeoUnlockLicense(License protobufLicense) {
        super(protobufLicense);
        LicenseDataArea prtbArea = protobufLicense.data.area;
        if (prtbArea != null && prtbArea.area_ids != null) {
            this.areaIds = new ArrayList(prtbArea.area_ids);
        }
    }

    public GeoUnlockLicense() {
    }

    public boolean containsByAreaIds(List<Integer> ids) {
        if (ids == null || ids.size() != this.areaIds.size()) {
            return false;
        }
        for (int i = 0; i != ids.size(); i++) {
            boolean isFound = false;
            for (int j = 0; j != this.areaIds.size(); j++) {
                if (ids.get(i).equals(this.areaIds.get(j))) {
                    isFound = true;
                }
            }
            if (!isFound) {
                return false;
            }
        }
        return true;
    }

    public String getAreaIdsStr() {
        String res = "";
        int size = this.areaIds.size();
        for (int i = 0; i < size; i++) {
            res = res + this.areaIds.get(i);
            if (i < size - 1) {
                res = res + ", ";
            }
        }
        return res;
    }

    public boolean containsAreaId(int id) {
        for (Integer num : this.areaIds) {
            if (num.intValue() == id) {
                return true;
            }
        }
        return false;
    }
}
