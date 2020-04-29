package dji.publics.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.dji.frame.util.V_FileUtil;
import com.dji.frame.util.V_JsonUtil;
import com.dji.megatronking.stringfog.lib.annotation.StringFogIgnore;
import com.google.gson.Gson;
import dji.DJIBaseApplication;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.ProductType;
import dji.pilot.publics.R;
import dji.pilot.publics.model.DJIProductVerConstants;
import dji.pilot.publics.model.DJIProductVerModel;
import dji.publics.model.DJIProductListModel;
import dji.publics.model.DJIProductModelConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@EXClassNullAway
@StringFogIgnore
public class DJIProductConfigManager implements DJIProductVerConstants {
    private static final String TAG = "DJIProductConfigManager";
    private static volatile DJIProductConfigManager instance;
    private Context context;
    private DJIProductListModel listModel;

    public static synchronized DJIProductConfigManager create(Context context2) {
        DJIProductConfigManager dJIProductConfigManager;
        synchronized (DJIProductConfigManager.class) {
            if (instance == null) {
                instance = new DJIProductConfigManager(context2.getApplicationContext());
            }
            dJIProductConfigManager = instance;
        }
        return dJIProductConfigManager;
    }

    public static synchronized DJIProductConfigManager getInstance() {
        DJIProductConfigManager dJIProductConfigManager;
        synchronized (DJIProductConfigManager.class) {
            if (instance == null) {
                dJIProductConfigManager = create(DJIBaseApplication.getAppContext());
            } else {
                dJIProductConfigManager = instance;
            }
        }
        return dJIProductConfigManager;
    }

    public DJIProductConfigManager(Context context2) {
        this.context = context2;
        loadOptimizedProductCfgs(context2);
    }

    private void loadOptimizedProductCfgs(Context context2) {
        this.listModel = new DJIProductListModel();
        this.listModel.vertion = "1";
        this.listModel.products = new ArrayList<>();
        for (DJIProductListModel.DJIProductModel model : DJIProductModelConstants.NEW_PRODUCTS) {
            this.listModel.products.add(model);
        }
        for (DJIProductListModel.DJIProductModel model2 : DJIProductModelConstants.OLD_PRODUCTS) {
            this.listModel.products.add(model2);
        }
    }

    private void loadNonOptimizedProductCfgs(Context context2) {
        String s = V_FileUtil.rawfileGetContent(context2, R.raw.product_config);
        this.listModel = (DJIProductListModel) V_JsonUtil.toBean(s, DJIProductListModel.class);
        if (this.listModel == null) {
            try {
                this.listModel = (DJIProductListModel) V_JsonUtil.getGson().fromJson(s, DJIProductListModel.class);
            } catch (Exception e) {
                this.listModel = (DJIProductListModel) new Gson().fromJson(s, DJIProductListModel.class);
            }
        }
        if (!(this.listModel == null || this.listModel.products == null)) {
            initOldProduct(this.listModel.products);
        }
        Iterator<DJIProductListModel.DJIProductModel> it2 = this.listModel.products.iterator();
        while (it2.hasNext()) {
            DJIProductListModel.DJIProductModel productModel = it2.next();
            DJIProductVerModel oldModel = (DJIProductVerModel) V_JsonUtil.toBean(V_FileUtil.rawfileGetContent(context2, R.raw.pro1), DJIProductVerModel.class);
            if (productModel.verlist != null) {
                int id = context2.getResources().getIdentifier(productModel.verlist, "raw", context2.getPackageName());
                if (id > 0) {
                    productModel.verModel = (DJIProductVerModel) V_JsonUtil.toBean(V_FileUtil.rawfileGetContent(context2, id), DJIProductVerModel.class);
                }
            } else {
                productModel.verlist = "pro1";
                productModel.verModel = oldModel;
            }
        }
    }

    public DJIProductListModel getProductConfigList() {
        return this.listModel;
    }

    public ProductType getProductTypeByServerName(String serverName) {
        Iterator<DJIProductListModel.DJIProductModel> it2 = this.listModel.products.iterator();
        while (it2.hasNext()) {
            DJIProductListModel.DJIProductModel djiProductModel = it2.next();
            if (djiProductModel.activeName.equals(serverName)) {
                return ProductType.find(djiProductModel.value);
            }
        }
        return ProductType.None;
    }

    public DJIProductListModel.DJIProductModel getProductModel(ProductType productType) {
        ArrayList<DJIProductListModel.DJIProductModel> products = this.listModel.products;
        Iterator<DJIProductListModel.DJIProductModel> it2 = products.iterator();
        while (it2.hasNext()) {
            DJIProductListModel.DJIProductModel djiProductModel = it2.next();
            if (djiProductModel.value == productType.value()) {
                return djiProductModel;
            }
        }
        return products.get(0);
    }

    public int getDevicePicConnectResId(int value, int subValue) {
        String idName = "";
        Iterator<DJIProductListModel.DJIProductModel> it2 = this.listModel.products.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            DJIProductListModel.DJIProductModel model = it2.next();
            if (value == model.value) {
                idName = model.pic_connect;
                if (model.subTypeList != null && model.subTypeList.size() > 0) {
                    Iterator<DJIProductListModel.DJIProductSubTypeModel> it3 = model.subTypeList.iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            break;
                        }
                        DJIProductListModel.DJIProductSubTypeModel subType = it3.next();
                        if (subType.value == subValue) {
                            idName = subType.pic_connect;
                            break;
                        }
                    }
                }
            }
        }
        return this.context.getResources().getIdentifier(idName, "drawable", this.context.getPackageName());
    }

    @Deprecated
    public Drawable getDevicePicConnect(int value, int subValue) {
        String idName = "";
        Iterator<DJIProductListModel.DJIProductModel> it2 = this.listModel.products.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            DJIProductListModel.DJIProductModel model = it2.next();
            if (value == model.value) {
                idName = model.pic_connect;
                if (model.subTypeList != null && model.subTypeList.size() > 0) {
                    Iterator<DJIProductListModel.DJIProductSubTypeModel> it3 = model.subTypeList.iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            break;
                        }
                        DJIProductListModel.DJIProductSubTypeModel subType = it3.next();
                        if (subType.value == subValue) {
                            idName = subType.pic_connect;
                            break;
                        }
                    }
                }
            }
        }
        return this.context.getResources().getDrawable(this.context.getResources().getIdentifier(idName, "drawable", this.context.getPackageName()));
    }

    public int getConnectTitleId(int value) {
        String idName = "";
        Iterator<DJIProductListModel.DJIProductModel> it2 = this.listModel.products.iterator();
        while (it2.hasNext()) {
            DJIProductListModel.DJIProductModel model = it2.next();
            if (value == model.value) {
                idName = model.title_connect;
            }
        }
        return this.context.getResources().getIdentifier(idName, "string", this.context.getPackageName());
    }

    public int getConnectTitleId(int value, int sub) {
        String idName = "";
        Iterator<DJIProductListModel.DJIProductModel> it2 = this.listModel.products.iterator();
        while (it2.hasNext()) {
            DJIProductListModel.DJIProductModel model = it2.next();
            if (value == model.value) {
                idName = model.title_connect;
                if (model.subTypeList != null && model.subTypeList.size() > 0) {
                    Iterator<DJIProductListModel.DJIProductSubTypeModel> it3 = model.subTypeList.iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            break;
                        }
                        DJIProductListModel.DJIProductSubTypeModel subType = it3.next();
                        if (subType.value == sub && !TextUtils.isEmpty(subType.title_connect)) {
                            idName = subType.title_connect;
                            break;
                        }
                    }
                }
            }
        }
        return this.context.getResources().getIdentifier(idName, "string", this.context.getPackageName());
    }

    public String getShareName(ProductType productType) {
        return getProductModel(productType).shareName;
    }

    public String getStoreSlug(int value, int subValue) {
        Iterator<DJIProductListModel.DJIProductModel> it2 = this.listModel.products.iterator();
        while (it2.hasNext()) {
            DJIProductListModel.DJIProductModel model = it2.next();
            if (value == model.value) {
                String slug = model.storeSlug;
                if (model.subTypeList == null || model.subTypeList.size() <= 0) {
                    return slug;
                }
                for (DJIProductListModel.DJIProductSubTypeModel subType : model.subTypeList) {
                    if (subType.value == subValue && !TextUtils.isEmpty(subType.storeSlug)) {
                        slug = subType.storeSlug;
                    }
                }
                return slug;
            }
        }
        return "";
    }

    public int getActiveTitleId(ProductType type) {
        return this.context.getResources().getIdentifier(getProductModel(type).activeTitle, "string", this.context.getPackageName());
    }

    public int getActiveTipId(ProductType type, int subValue) {
        DJIProductListModel.DJIProductModel model = getProductModel(type);
        String idName = model.activeTip;
        if (model.subTypeList != null && model.subTypeList.size() > 0) {
            Iterator<DJIProductListModel.DJIProductSubTypeModel> it2 = model.subTypeList.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                DJIProductListModel.DJIProductSubTypeModel subType = it2.next();
                if (subType.value == subValue) {
                    idName = subType.activeTip;
                    break;
                }
            }
        }
        return this.context.getResources().getIdentifier(idName, "drawable", this.context.getPackageName());
    }

    public Drawable getMarker(ProductType productType) {
        return this.context.getResources().getDrawable(this.context.getResources().getIdentifier(getProductModel(productType).icon_1, "drawable", this.context.getPackageName()));
    }

    public int getIcon3ResId(ProductType type) {
        return this.context.getResources().getIdentifier(getProductModel(type).icon_3, "drawable", this.context.getPackageName());
    }

    public String getCollegeName(ProductType type) {
        return getProductModel(type).collegeName;
    }

    public String getActiveName(ProductType type) {
        return getProductModel(type).activeName;
    }

    public String getActivePlaneName(ProductType type) {
        return getProductModel(type).activePlaneName;
    }

    public String getCollegeAppId(ProductType type) {
        return getProductModel(type).college_appid;
    }

    public int getCompassHResId(ProductType type) {
        return this.context.getResources().getIdentifier(getProductModel(type).compass_h, "drawable", this.context.getPackageName());
    }

    public int getCompassHDescResId(ProductType type) {
        return this.context.getResources().getIdentifier(getProductModel(type).compass_h_desc, "string", this.context.getPackageName());
    }

    public int getCompassVResId(ProductType type) {
        return this.context.getResources().getIdentifier(getProductModel(type).compass_v, "drawable", this.context.getPackageName());
    }

    public int getCompassVDescResId(ProductType type) {
        return this.context.getResources().getIdentifier(getProductModel(type).compass_v_desc, "string", this.context.getPackageName());
    }

    public String getMomentEnddingTitle(ProductType type) {
        return getProductModel(type).moment_endding_title;
    }

    public String getMomentEnddingSubTitle(ProductType type) {
        return getProductModel(type).moment_endding_sub_title;
    }

    public int getMultiLanguageSeriesId(ProductType type) {
        String idName = getProductModel(type).multi_language_series;
        if (TextUtils.isEmpty(idName)) {
            return 0;
        }
        return this.context.getResources().getIdentifier(idName, "string", this.context.getPackageName());
    }

    public int getMultiLanguageVersion(ProductType type) {
        return getProductModel(type).multi_language_version;
    }

    public int getMultiLanguageSubTypeId(ProductType type) {
        String idName = getProductModel(type).multi_language_sub_type;
        if (TextUtils.isEmpty(idName)) {
            return 0;
        }
        return this.context.getResources().getIdentifier(idName, "string", this.context.getPackageName());
    }

    private void initOldProduct(List<DJIProductListModel.DJIProductModel> list) {
        DJIProductListModel.DJIProductModel model = new DJIProductListModel.DJIProductModel();
        model.value = 1;
        model.name = "Inspire";
        model.activeName = "inspire1";
        model.activePlaneName = "inspire 1";
        model.activeTitle = "v2_active_inspire_1";
        model.activeTip = "v2_inpire1_icon";
        model.icon_1 = "wm100_icon";
        model.icon_3 = "pilot_icon";
        model.compass_h = "fpv_compass_horizontal";
        model.compass_v = "fpv_compass_vertical";
        model.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model.college_appid = "501";
        model.collegeName = "Inspire 1";
        model.shareName = "inspire 1";
        model.storeSlug = "inspire-1-v2";
        model.subTypeList = null;
        model.pic_connect = "v2_inpire1_icon";
        model.title_connect = "phantom_type_inspire";
        model.moment_endding_title = "INSPIRE 1";
        model.moment_endding_sub_title = "";
        model.multi_language_series = "normal_series_inspire";
        model.multi_language_version = 1;
        model.multi_language_sub_type = "";
        model.device_list_pic = "";
        list.add(model);
        DJIProductListModel.DJIProductModel model2 = new DJIProductListModel.DJIProductModel();
        model2.value = 2;
        model2.name = "P3C";
        model2.activeName = "P3C";
        model2.activePlaneName = "Phantom 3";
        model2.activeTitle = "ve_active_p3c";
        model2.activeTip = "wm100_icon";
        model2.icon_1 = "wm100_icon";
        model2.icon_3 = "litchic_icon";
        model2.compass_h = "fpv_compass_litchi_horizontal";
        model2.compass_v = "fpv_compass_litchi_vertical";
        model2.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model2.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model2.college_appid = "504";
        model2.collegeName = "P3 Standard";
        model2.shareName = "phantom3 standard";
        model2.storeSlug = "phantom-3-standard";
        model2.subTypeList = null;
        model2.pic_connect = "v2_p3c_icon";
        model2.title_connect = "phantom_type_phantom_standard";
        model2.moment_endding_title = "PHANTOM 3";
        model2.moment_endding_sub_title = "standard";
        model2.multi_language_series = "normal_series_phantom";
        model2.multi_language_version = 3;
        model2.multi_language_sub_type = "normal_sub_type_standard";
        model2.device_list_pic = "";
        list.add(model2);
        DJIProductListModel.DJIProductModel model3 = new DJIProductListModel.DJIProductModel();
        model3.value = 3;
        model3.name = "P3S";
        model3.activeName = "P3S";
        model3.activePlaneName = "Phantom 3";
        model3.activeTitle = "ve_active_p3s";
        model3.activeTip = "wm100_icon";
        model3.icon_1 = "wm100_icon";
        model3.icon_3 = "wm100_icon";
        model3.compass_h = "fpv_compass_litchi_horizontal";
        model3.compass_v = "fpv_compass_litchi_vertical";
        model3.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model3.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model3.college_appid = "502";
        model3.collegeName = "P3 Advanced";
        model3.shareName = "phantom3 advanced";
        model3.storeSlug = "phantom-3-advanced";
        model3.subTypeList = null;
        model3.pic_connect = "wm100_icon";
        model3.title_connect = "phantom_type_phantom_advance";
        model3.moment_endding_title = "PHANTOM 3";
        model3.moment_endding_sub_title = "advanced";
        model3.multi_language_series = "normal_series_phantom";
        model3.multi_language_version = 3;
        model3.multi_language_sub_type = "normal_sub_type_advanced";
        model3.device_list_pic = "";
        list.add(model3);
        DJIProductListModel.DJIProductModel model4 = new DJIProductListModel.DJIProductModel();
        model4.value = 4;
        model4.name = "P3X";
        model4.activeName = "P3X";
        model4.activePlaneName = "Phantom 3";
        model4.activeTitle = "ve_active_p3x";
        model4.activeTip = "wm100_icon";
        model4.icon_1 = "wm100_icon";
        model4.icon_3 = "litchix_icon";
        model4.compass_h = "fpv_compass_litchi_horizontal";
        model4.compass_v = "fpv_compass_litchi_vertical";
        model4.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model4.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model4.college_appid = "503";
        model4.collegeName = "P3 Professional";
        model4.shareName = "phantom3 professional";
        model4.storeSlug = "phantom-3-professional";
        model4.subTypeList = null;
        model4.pic_connect = "wm100_icon";
        model4.title_connect = "phantom_type_phantom_professional";
        model4.moment_endding_title = "PHANTOM 3";
        model4.moment_endding_sub_title = "professional";
        model4.multi_language_series = "normal_series_phantom";
        model4.multi_language_version = 3;
        model4.multi_language_sub_type = "normal_sub_type_professional";
        model4.device_list_pic = "";
        list.add(model4);
        DJIProductListModel.DJIProductModel model5 = new DJIProductListModel.DJIProductModel();
        model5.value = 5;
        model5.name = "Longan";
        model5.activeName = "OSMO";
        model5.activePlaneName = "OSMO";
        model5.activeTitle = "ve_active_osmo";
        model5.activeTip = "v2_longan_icon";
        model5.icon_1 = "wm100_icon";
        model5.icon_3 = "wm100_icon";
        model5.compass_h = "fpv_compass_litchi_horizontal";
        model5.compass_v = "fpv_compass_litchi_vertical";
        model5.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model5.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model5.college_appid = "505";
        model5.collegeName = "OSMO";
        model5.shareName = "OSMO";
        model5.storeSlug = "osmo";
        model5.subTypeList = null;
        model5.pic_connect = "wm100_icon";
        model5.title_connect = "phantom_type_phantom_longan";
        model5.moment_endding_title = "OSMO";
        model5.moment_endding_sub_title = "";
        model5.multi_language_series = "normal_series_osmo";
        model5.multi_language_version = 0;
        model5.multi_language_sub_type = "";
        model5.device_list_pic = "";
        list.add(model5);
        DJIProductListModel.DJIProductModel model6 = new DJIProductListModel.DJIProductModel();
        model6.value = 6;
        model6.name = "M100";
        model6.activeName = "M100";
        model6.activePlaneName = "M100";
        model6.activeTitle = "home_account_first_desc_1_right_n1";
        model6.activeTip = "wm100_icon";
        model6.icon_1 = "wm100_icon";
        model6.icon_3 = "m100_icon";
        model6.compass_h = "fpv_compass_m100_horizontal";
        model6.compass_v = "fpv_compass_m100_vertical";
        model6.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model6.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model6.college_appid = "506";
        model6.collegeName = "M100";
        model6.shareName = "DJI Aircraft";
        model6.storeSlug = "matrice-100";
        model6.subTypeList = null;
        model6.pic_connect = "wm100_icon";
        model6.title_connect = "phantom_type_phantom_m100";
        model6.moment_endding_title = "M100";
        model6.moment_endding_sub_title = "";
        model6.multi_language_series = "normal_series_matrice";
        model6.multi_language_version = 100;
        model6.multi_language_sub_type = "";
        model6.device_list_pic = "";
        list.add(model6);
        DJIProductListModel.DJIProductModel model7 = new DJIProductListModel.DJIProductModel();
        model7.value = 8;
        model7.name = "LB2";
        model7.activeName = "LB2";
        model7.activePlaneName = "LB2";
        model7.activeTitle = "home_account_first_desc_1_right_n1";
        model7.activeTip = "wm100_icon";
        model7.icon_1 = "wm100_icon";
        model7.icon_3 = "m100_iocn";
        model7.compass_h = "fpv_compass_m100_horizontal";
        model7.compass_v = "fpv_compass_m100_vertical";
        model7.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model7.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model7.college_appid = "508";
        model7.collegeName = "LB2";
        model7.shareName = "DJI Lightbridge 2";
        model7.storeSlug = "dji-lightbridge-2";
        model7.subTypeList = null;
        model7.pic_connect = "wm100_icon";
        model7.title_connect = "phantom_type_phantom_lb2";
        model7.moment_endding_title = "";
        model7.moment_endding_sub_title = "";
        model7.multi_language_series = "";
        model7.multi_language_version = 0;
        model7.multi_language_sub_type = "";
        model7.device_list_pic = "";
        list.add(model7);
        DJIProductListModel.DJIProductModel model8 = new DJIProductListModel.DJIProductModel();
        model8.value = 9;
        model8.name = "Inspire 1 Pro";
        model8.activeName = "inspire 1 pro";
        model8.activePlaneName = "inspire 1 pro";
        model8.activeTitle = "v2_active_inspire_pro";
        model8.activeTip = "v2_inpire1_x5_icon";
        model8.icon_1 = "wm100_icon";
        model8.icon_3 = "pilot_x5_icon";
        model8.compass_h = "fpv_compass_x5_horizontal";
        model8.compass_v = "fpv_compass_x5_vertical";
        model8.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model8.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model8.college_appid = "509";
        model8.collegeName = "Inspire 1 Pro";
        model8.shareName = "inspire 1 Pro";
        model8.storeSlug = "inspire-1-pro";
        model8.subTypeList = null;
        model8.pic_connect = "v2_inpire1_x5_icon";
        model8.title_connect = "phantom_type_inspire_pro";
        model8.moment_endding_title = "INSPIRE 1";
        model8.moment_endding_sub_title = "pro";
        model8.multi_language_series = "normal_series_inspire";
        model8.multi_language_version = 1;
        model8.multi_language_sub_type = "normal_sub_type_professional";
        model8.device_list_pic = "";
        list.add(model8);
        DJIProductListModel.DJIProductModel model9 = new DJIProductListModel.DJIProductModel();
        model9.value = 14;
        model9.name = "Zenmuse XT";
        model9.activeName = "Zenmuse XT";
        model9.activePlaneName = "zenmuse xt";
        model9.activeTitle = "v2_active_olives";
        model9.activeTip = "wm100_icon";
        model9.icon_1 = "xt_image";
        model9.icon_3 = "xt_icon";
        model9.compass_h = "fpv_compass_horizontal";
        model9.compass_v = "fpv_compass_vertical";
        model9.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model9.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model9.college_appid = "514";
        model9.collegeName = "Zenmuse XT";
        model9.shareName = "zenmuse XT";
        model9.storeSlug = "";
        model9.subTypeList = null;
        model9.pic_connect = "wm100_icon";
        model9.title_connect = "phantom_type_olives";
        model9.moment_endding_title = "Zenmuse XT";
        model9.moment_endding_sub_title = "";
        model9.multi_language_series = "";
        model9.multi_language_version = 0;
        model9.multi_language_sub_type = "";
        model9.device_list_pic = "";
        list.add(model9);
        DJIProductListModel.DJIProductModel model10 = new DJIProductListModel.DJIProductModel();
        model10.value = 16;
        model10.name = "LB2";
        model10.activeName = "LB2";
        model10.activePlaneName = "LB2";
        model10.activeTitle = "home_account_first_desc_1_right_n1";
        model10.activeTip = "wm100_icon";
        model10.icon_1 = "wm100_icon";
        model10.icon_3 = "m100_iocn";
        model10.compass_h = "fpv_compass_m100_horizontal";
        model10.compass_v = "fpv_compass_m100_vertical";
        model10.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model10.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model10.college_appid = "508";
        model10.collegeName = "LB2";
        model10.shareName = "DJI Lightbridge 2";
        model10.storeSlug = "";
        model10.subTypeList = null;
        model10.pic_connect = "wm100_icon";
        model10.title_connect = "phantom_type_phantom_lb2";
        model10.moment_endding_title = "";
        model10.moment_endding_sub_title = "";
        model10.multi_language_series = "";
        model10.multi_language_version = 0;
        model10.multi_language_sub_type = "";
        model10.device_list_pic = "";
        list.add(model10);
        DJIProductListModel.DJIProductModel model11 = new DJIProductListModel.DJIProductModel();
        model11.value = 12;
        model11.name = "P3 4K";
        model11.activeName = "P3 4K";
        model11.activePlaneName = "P3 4K";
        model11.activeTitle = "ve_active_p34k";
        model11.activeTip = "wm100_icon";
        model11.icon_1 = "wm100_icon";
        model11.icon_3 = "litchix_icon";
        model11.compass_h = "fpv_compass_litchi_horizontal";
        model11.compass_v = "fpv_compass_litchi_vertical";
        model11.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model11.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model11.college_appid = "512";
        model11.collegeName = "P3 4K";
        model11.shareName = "P3 4K";
        model11.storeSlug = "phantom3-4k";
        model11.subTypeList = null;
        model11.pic_connect = "wm100_icon";
        model11.title_connect = "phantom_type_phantom_professional";
        model11.moment_endding_title = "PHANTOM 3";
        model11.moment_endding_sub_title = "4k";
        model11.multi_language_series = "normal_series_phantom";
        model11.multi_language_version = 3;
        model11.multi_language_sub_type = "normal_sub_type_4k";
        model11.device_list_pic = "";
        list.add(model11);
        DJIProductListModel.DJIProductModel model12 = new DJIProductListModel.DJIProductModel();
        model12.value = 10;
        model12.name = "A3";
        model12.activeName = "A3";
        model12.activePlaneName = "A3";
        model12.activeTitle = "ve_active_a3";
        model12.activeTip = "v2_a3_icon";
        model12.icon_1 = "v2_a3_icon";
        model12.icon_3 = "pilot_a3_icon";
        model12.compass_h = "fpv_compass_a3_horizontal";
        model12.compass_v = "fpv_compass_a3_vertical";
        model12.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model12.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model12.college_appid = "510";
        model12.collegeName = "A3";
        model12.shareName = "A3";
        model12.storeSlug = "a3";
        model12.subTypeList = null;
        model12.pic_connect = "v2_a3_icon";
        model12.title_connect = "phantom_type_a3";
        model12.moment_endding_title = "";
        model12.moment_endding_sub_title = "";
        model12.multi_language_series = "";
        model12.multi_language_version = 0;
        model12.multi_language_sub_type = "";
        model12.device_list_pic = "";
        list.add(model12);
        DJIProductListModel.DJIProductModel model13 = new DJIProductListModel.DJIProductModel();
        model13.value = 11;
        model13.name = "M600";
        model13.activeName = "M600";
        model13.activePlaneName = "M600";
        model13.activeTitle = "home_account_first_desc_1_right_n1";
        model13.activeTip = "v2_m600_icon";
        model13.icon_1 = "v2_m600_icon";
        model13.icon_3 = "m600_icon";
        model13.compass_h = "fpv_compass_m600_horizontal";
        model13.compass_v = "fpv_compass_m600_vertical";
        model13.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model13.compass_v_desc = "fpv_checklist_compass_pm820_tip_2_desc";
        model13.college_appid = "511";
        model13.collegeName = "M600";
        model13.shareName = "M600";
        model13.storeSlug = "matrice-600";
        model13.subTypeList = null;
        model13.pic_connect = "v2_m600_connect_icon";
        model13.title_connect = "phantom_type_phantom_m600";
        model13.moment_endding_title = "M600";
        model13.moment_endding_sub_title = "";
        model13.multi_language_series = "normal_series_matrice";
        model13.multi_language_version = 600;
        model13.multi_language_sub_type = "";
        model13.device_list_pic = "";
        list.add(model13);
        DJIProductListModel.DJIProductModel model14 = new DJIProductListModel.DJIProductModel();
        model14.value = 27;
        model14.name = "M600 PRO";
        model14.activeName = "M600 PRO";
        model14.activePlaneName = "M600 PRO";
        model14.activeTitle = "home_account_first_desc_1_right_n1";
        model14.activeTip = "v2_m600_pro_connect_icon";
        model14.icon_1 = "v2_m600_pro_connect_icon";
        model14.icon_3 = "m600_icon";
        model14.compass_h = "fpv_compass_m600_horizontal";
        model14.compass_v = "fpv_compass_m600_vertical";
        model14.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model14.compass_v_desc = "fpv_checklist_compass_pm820_tip_2_desc";
        model14.college_appid = "511";
        model14.collegeName = "M600 PRO";
        model14.shareName = "M600 PRO";
        model14.storeSlug = "matrice-600-pro";
        model14.subTypeList = null;
        model14.pic_connect = "v2_m600_pro_connect_icon";
        model14.title_connect = "phantom_type_phantom_m600_pro";
        model14.moment_endding_title = "M600";
        model14.moment_endding_sub_title = "PRO";
        model14.multi_language_series = "normal_series_matrice";
        model14.multi_language_version = 600;
        model14.multi_language_sub_type = "normal_sub_type_professional";
        model14.device_list_pic = "";
        list.add(model14);
        DJIProductListModel.DJIProductModel model15 = new DJIProductListModel.DJIProductModel();
        model15.value = 15;
        model15.name = "Inspire 1 RAW";
        model15.activeName = "inspire 1 raw";
        model15.activePlaneName = "inspire 1 RAW";
        model15.activeTitle = "v2_active_inspire_raw";
        model15.activeTip = "v2_inspire1_raw_connect_icon";
        model15.icon_1 = "wm100_icon";
        model15.icon_3 = "pilot_x5r_icon";
        model15.compass_h = "fpv_compass_x5_horizontal";
        model15.compass_v = "fpv_compass_x5_vertical";
        model15.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model15.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model15.college_appid = "515";
        model15.collegeName = "Inspire 1 RAW";
        model15.shareName = "inspire 1 RAW";
        model15.storeSlug = "inspire-1-raw";
        model15.subTypeList = null;
        model15.pic_connect = "v2_inspire1_raw_connect_icon";
        model15.title_connect = "phantom_type_inspire_raw";
        model15.moment_endding_title = "INSPIRE 1";
        model15.moment_endding_sub_title = "raw";
        model15.multi_language_series = "normal_series_inspire";
        model15.multi_language_version = 1;
        model15.multi_language_sub_type = "normal_sub_type_raw";
        model15.device_list_pic = "";
        list.add(model15);
        DJIProductListModel.DJIProductModel model16 = new DJIProductListModel.DJIProductModel();
        model16.value = 18;
        model16.name = "Longan PRO";
        model16.activeName = "OSMO PRO";
        model16.activePlaneName = "OSMO PRO";
        model16.activeTitle = "ve_active_osmo_pro";
        model16.activeTip = "v2_osmo_pro_connect_icon";
        model16.icon_1 = "logo_longan_pro";
        model16.icon_3 = "logo_longan_pro";
        model16.compass_h = "fpv_compass_litchi_horizontal";
        model16.compass_v = "fpv_compass_litchi_vertical";
        model16.college_appid = "518";
        model16.collegeName = "OSMO PRO";
        model16.shareName = "OSMO PRO";
        model16.storeSlug = "osmo-pro";
        model16.subTypeList = null;
        model16.pic_connect = "v2_osmo_pro_connect_icon";
        model16.title_connect = "phantom_type_phantom_longan";
        model16.moment_endding_title = "OSMO";
        model16.moment_endding_sub_title = "PRO";
        model16.multi_language_series = "normal_series_osmo";
        model16.multi_language_version = 0;
        model16.multi_language_sub_type = "normal_sub_type_professional";
        model16.device_list_pic = "";
        list.add(model16);
        DJIProductListModel.DJIProductModel model17 = new DJIProductListModel.DJIProductModel();
        model17.value = 19;
        model17.name = "Longan RAW";
        model17.activeName = "OSMORAW";
        model17.activePlaneName = "OSMO RAW";
        model17.activeTitle = "ve_active_osmo_raw";
        model17.activeTip = "v2_osmo_raw_connect_icon";
        model17.icon_1 = "logo_longan_raw";
        model17.icon_3 = "logo_longan_raw";
        model17.compass_h = "fpv_compass_litchi_horizontal";
        model17.compass_v = "fpv_compass_litchi_vertical";
        model17.college_appid = "519";
        model17.collegeName = "OSMO RAW";
        model17.shareName = "OSMO RAW";
        model17.storeSlug = "osmo-raw";
        model17.subTypeList = null;
        model17.pic_connect = "v2_osmo_raw_connect_icon";
        model17.title_connect = "phantom_type_phantom_longan";
        model17.moment_endding_title = "OSMO";
        model17.moment_endding_sub_title = "RAW";
        model17.multi_language_series = "normal_series_osmo";
        model17.multi_language_version = 0;
        model17.multi_language_sub_type = "normal_sub_type_raw";
        model17.device_list_pic = "";
        list.add(model17);
        DJIProductListModel.DJIProductModel model18 = new DJIProductListModel.DJIProductModel();
        model18.value = 20;
        model18.name = "Longan Zoom";
        model18.activeName = "OSMO+";
        model18.activePlaneName = "OSMO+";
        model18.activeTitle = "ve_active_osmo_1s";
        model18.activeTip = "v2_osmo_1s_connect_icon";
        model18.icon_1 = "logo_longan_1s";
        model18.icon_3 = "logo_longan_1s";
        model18.compass_h = "fpv_compass_litchi_horizontal";
        model18.compass_v = "fpv_compass_litchi_vertical";
        model18.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model18.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model18.college_appid = "520";
        model18.collegeName = "OSMO+";
        model18.shareName = "OSMO+";
        model18.storeSlug = "osmo-plus";
        model18.subTypeList = null;
        model18.pic_connect = "v2_osmo_1s_connect_icon";
        model18.title_connect = "phantom_type_phantom_longan_zoom";
        model18.moment_endding_title = "OSMO";
        model18.moment_endding_sub_title = "+";
        model18.multi_language_series = "normal_series_osmo";
        model18.multi_language_version = 0;
        model18.multi_language_sub_type = "normal_sub_type_plus";
        model18.device_list_pic = "";
        list.add(model18);
        DJIProductListModel.DJIProductModel model19 = new DJIProductListModel.DJIProductModel();
        model19.value = 22;
        model19.name = "Longan Mobile";
        model19.activeName = "OSMOMobile";
        model19.activePlaneName = "OSMO Mobile";
        model19.activeTitle = "ve_active_osmo_mobile";
        model19.activeTip = "v2_longan_mobile_connect_icon";
        model19.icon_1 = "logo_longan_mobile";
        model19.icon_3 = "litchic_icon";
        model19.compass_h = "fpv_compass_litchi_horizontal";
        model19.compass_v = "fpv_compass_litchi_vertical";
        model19.compass_h_desc = "fpv_checklist_compass_tip_1_desc";
        model19.compass_v_desc = "fpv_checklist_compass_tip_2_desc";
        model19.college_appid = "505";
        model19.collegeName = "OSMO Mobile";
        model19.shareName = "OSMO Mobile";
        model19.storeSlug = "osmo-mobile";
        model19.subTypeList = null;
        model19.pic_connect = "v2_longan_mobile_connect_icon";
        model19.title_connect = "phantom_type_phantom_longan";
        model19.moment_endding_title = "OSMO";
        model19.moment_endding_sub_title = "Mobile";
        model19.multi_language_series = "normal_series_osmo";
        model19.multi_language_version = 0;
        model19.multi_language_sub_type = "normal_sub_type_mobile";
        model19.device_list_pic = "";
        list.add(model19);
    }
}
