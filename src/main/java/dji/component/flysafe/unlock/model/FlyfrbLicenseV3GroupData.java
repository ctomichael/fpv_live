package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.dji.frame.util.V_JsonUtil;
import com.google.gson.reflect.TypeToken;
import dji.component.flysafe.model.IFlyfrbBaseDb;
import dji.component.flysafe.model.IFlyfrbInfoDbTable;
import dji.component.flysafe.util.HttpJsonParseHelper;
import dji.component.flysafe.util.NFZLogUtil;
import dji.thirdparty.afinal.annotation.sqlite.Id;
import dji.thirdparty.afinal.annotation.sqlite.Table;
import dji.thirdparty.afinal.annotation.sqlite.Transient;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

@Keep
@Table(name = "flyfrb_usr_sn_unlock_v3_license_group")
public class FlyfrbLicenseV3GroupData implements IFlyfrbBaseDb, IFlyfrbInfoDbTable {
    @Transient
    public static final String DB_WHERE_ID_FORMAT = "id=%d";
    @Transient
    public static final String DB_WHERE_USER_ID_FORMAT = "user_id=%d";
    @Transient
    public static final String JSON_KEY_LICENSE_V3 = "onboard_license_v3";
    public int id;
    public String onboard_license_v2 = "";
    public transient String onboard_license_v3 = "";
    public String sn = "";
    @Id
    public int table_id;
    @Transient
    public List<FlyfrbLicenseV3Info> unlock_licenses = new ArrayList();
    public int unlock_licenses_count;
    public long updated_at;
    public long user_id;

    @Nullable
    public static List<FlyfrbLicenseV3GroupData> parseGroupFromJson(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        List<FlyfrbLicenseV3GroupData> groupDataList = V_JsonUtil.getList(jsonStr, new TypeToken<List<FlyfrbLicenseV3GroupData>>() {
            /* class dji.component.flysafe.unlock.model.FlyfrbLicenseV3GroupData.AnonymousClass1 */
        });
        if (groupDataList == null) {
            return null;
        }
        JSONArray jarray = null;
        try {
            jarray = new JSONArray(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jarray == null) {
            NFZLogUtil.savedLOGE("Parse v3 group data from json error!! Jarray result null");
            return null;
        }
        int length = jarray.length();
        int listSize = groupDataList.size();
        int i = 0;
        while (i < length && i < listSize) {
            groupDataList.get(i).onboard_license_v3 = HttpJsonParseHelper.parseCustomKey(jarray.optJSONObject(i), JSON_KEY_LICENSE_V3);
            i++;
        }
        return groupDataList;
    }

    public List<FlyfrbLicenseV3Info> getUnlockLicenses() {
        return new ArrayList(this.unlock_licenses);
    }

    @Nullable
    public static FlyfrbLicenseV3GroupData parseFromJson(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        FlyfrbLicenseV3GroupData groupData = (FlyfrbLicenseV3GroupData) V_JsonUtil.toBean(jsonStr, FlyfrbLicenseV3GroupData.class);
        if (groupData == null) {
            return null;
        }
        groupData.onboard_license_v3 = HttpJsonParseHelper.parseCustomKey(jsonStr, JSON_KEY_LICENSE_V3);
        return groupData;
    }

    public void injectData2LicenseInfos() {
        for (FlyfrbLicenseV3Info licenseV3Info : this.unlock_licenses) {
            licenseV3Info.sn = this.sn;
            licenseV3Info.user_id = this.user_id;
            licenseV3Info.group_id = this.id;
            licenseV3Info.handlePreSaveDb();
        }
    }

    @Nullable
    public String getLicenseDataByVersion() {
        return "";
    }

    public boolean isInvalid() {
        return TextUtils.isEmpty(this.sn) && this.updated_at == 0;
    }

    public void handleNullField() {
        if (this.sn == null) {
            this.sn = "";
        }
    }
}
