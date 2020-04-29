package dji.publics.model;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.pilot.publics.model.DJIProductVerModel;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DJIProductListModel {
    public ArrayList<DJIProductModel> products;
    public String vertion;

    @Keep
    public static class DJIProductModel {
        public String activeName;
        public String activePlaneName;
        public String activeTip;
        public String activeTitle;
        public String collegeName;
        public String college_appid;
        public String compass_h;
        public String compass_h_desc;
        public String compass_v;
        public String compass_v_desc;
        public String device_list_pic;
        public String icon_1;
        public String icon_3;
        public String moment_endding_sub_title;
        public String moment_endding_title;
        public String multi_language_series;
        public String multi_language_sub_type;
        public int multi_language_version;
        public String name;
        public String pic_connect;
        public String shareName;
        public String storeSlug;
        public List<DJIProductSubTypeModel> subTypeList;
        public String title_connect;
        public int value;
        public DJIProductVerModel verModel;
        public String verlist;

        public DJIProductModel value(int value2) {
            this.value = value2;
            return this;
        }

        public DJIProductModel name(String name2) {
            this.name = name2;
            return this;
        }

        public DJIProductModel collegeName(String collegeName2) {
            this.collegeName = collegeName2;
            return this;
        }

        public DJIProductModel shareName(String shareName2) {
            this.shareName = shareName2;
            return this;
        }

        public DJIProductModel storeSlug(String storeSlug2) {
            this.storeSlug = storeSlug2;
            return this;
        }

        public DJIProductModel icon(String icon_12, String icon_32) {
            this.icon_1 = icon_12;
            this.icon_3 = icon_32;
            return this;
        }

        public DJIProductModel compass(String compass_h2, String compass_v2, String compass_h_desc2, String compass_v_desc2) {
            this.compass_h = compass_h2;
            this.compass_v = compass_v2;
            this.compass_h_desc = compass_h_desc2;
            this.compass_v_desc = compass_v_desc2;
            return this;
        }

        public DJIProductModel collegeAppid(String collegeAppid) {
            this.college_appid = collegeAppid;
            return this;
        }

        public DJIProductModel verlist(String verlist2) {
            this.verlist = verlist2;
            return this;
        }

        public DJIProductModel addSubTypeModel(DJIProductSubTypeModel subTypeModel) {
            if (this.subTypeList == null) {
                this.subTypeList = new ArrayList();
            }
            this.subTypeList.add(subTypeModel);
            return this;
        }

        public DJIProductModel activeInfo(String activeName2, String activePlaneName2, String activeTitle2, String activeTip2) {
            this.activeName = activeName2;
            this.activePlaneName = activePlaneName2;
            this.activeTitle = activeTitle2;
            this.activeTip = activeTip2;
            return this;
        }

        public DJIProductModel verModel(DJIProductVerModel verModel2) {
            this.verModel = verModel2;
            return this;
        }

        public DJIProductModel connectInfo(String pic_connect2, String title_connect2) {
            this.pic_connect = pic_connect2;
            this.title_connect = title_connect2;
            return this;
        }

        public DJIProductModel momentEnddingInfo(String moment_endding_title2, String moment_endding_sub_title2) {
            this.moment_endding_title = moment_endding_title2;
            this.moment_endding_sub_title = moment_endding_sub_title2;
            return this;
        }

        public DJIProductModel multiLanguageInfo(String multi_language_series2, int multi_language_version2, String multi_language_sub_type2) {
            this.multi_language_series = multi_language_series2;
            this.multi_language_version = multi_language_version2;
            this.multi_language_sub_type = multi_language_sub_type2;
            return this;
        }

        public DJIProductModel deviceListPic(String device_list_pic2) {
            this.device_list_pic = device_list_pic2;
            return this;
        }
    }

    @Keep
    public static class DJIProductSubTypeModel {
        public String activeTip;
        public String name;
        public String pic_connect;
        public String storeSlug;
        public String title_connect;
        public int value;

        public DJIProductSubTypeModel value(int value2) {
            this.value = value2;
            return this;
        }

        public DJIProductSubTypeModel name(String name2) {
            this.name = name2;
            return this;
        }

        public DJIProductSubTypeModel connectPic(String pic_connect2) {
            this.pic_connect = pic_connect2;
            return this;
        }

        public DJIProductSubTypeModel connecttitle(String title_connect2) {
            this.title_connect = title_connect2;
            return this;
        }

        public DJIProductSubTypeModel activeTip(String activeTip2) {
            this.activeTip = activeTip2;
            return this;
        }

        public DJIProductSubTypeModel storeSlug(String storeSlug2) {
            this.storeSlug = storeSlug2;
            return this;
        }
    }
}
