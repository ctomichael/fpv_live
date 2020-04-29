package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import com.dji.frame.util.V_JsonUtil;
import com.dji.mapkit.core.models.DJILatLng;
import com.google.gson.reflect.TypeToken;
import dji.component.flysafe.model.IFlyfrbBaseDb;
import dji.component.flysafe.model.IFlyfrbInfoDbTable;
import dji.thirdparty.afinal.annotation.sqlite.Id;
import dji.thirdparty.afinal.annotation.sqlite.Table;
import dji.thirdparty.afinal.annotation.sqlite.Transient;
import java.util.ArrayList;
import java.util.List;

@Keep
@Table(name = "flyfrb_unlock_license_v3_detail")
public class FlyfrbLicenseV3Info implements IFlyfrbBaseDb, IFlyfrbInfoDbTable {
    @Transient
    public static final String DB_WHERE_GROUP_ID_FORMAT = "group_id=%d";
    @Transient
    public static final String DB_WHERE_USER_ID_FORMAT = "user_id=%d";
    @Transient
    public List<Integer> area_ids = new ArrayList();
    public String country = "";
    public int country_number;
    public String description = "";
    public long end_at;
    public int group_id;
    public int height;
    public int id;
    public float lat;
    public float lng;
    @Transient
    public List<DJILatLng> mFormatPolygonPoints = new ArrayList();
    public String points = "";
    public int radius;
    public String save_area_ids = "";
    public String sn = "";
    public long start_at;
    @Id
    public int table_id;
    public int type;
    public long user_id;

    public void handlePreSaveDb() {
        this.save_area_ids = V_JsonUtil.toJson((List) this.area_ids);
    }

    @Nullable
    public List<Integer> getAreaIds() {
        if (this.area_ids == null || this.area_ids.isEmpty()) {
            this.area_ids = V_JsonUtil.getList(this.save_area_ids, new TypeToken<List<Integer>>() {
                /* class dji.component.flysafe.unlock.model.FlyfrbLicenseV3Info.AnonymousClass1 */
            });
        }
        if (this.area_ids == null) {
            this.area_ids = new ArrayList();
        }
        return this.area_ids;
    }

    public String getAreaIdsStr() {
        getAreaIds();
        String res = "";
        int size = this.area_ids.size();
        for (int i = 0; i < size; i++) {
            res = res + this.area_ids.get(i);
            if (i < size - 1) {
                res = res + ", ";
            }
        }
        return res;
    }

    public boolean containsGeoAreaIds(List<Integer> ids) {
        if (this.type != 0) {
            return false;
        }
        getAreaIds();
        if (ids == null || this.area_ids == null) {
            return false;
        }
        for (int i = 0; i != ids.size(); i++) {
            boolean isFound = false;
            for (int j = 0; j != this.area_ids.size(); j++) {
                if (ids.get(i).equals(this.area_ids.get(j))) {
                    isFound = true;
                }
            }
            if (!isFound) {
                return false;
            }
        }
        return true;
    }

    public void handleNullField() {
        if (this.description == null) {
            this.description = "";
        }
        if (this.save_area_ids == null) {
            this.save_area_ids = "";
        }
        if (this.country == null) {
            this.country = "";
        }
        if (this.points == null) {
            this.points = "";
        }
        if (this.sn == null) {
            this.sn = "";
        }
    }
}
