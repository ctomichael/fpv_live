package com.dji.service.popup.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.dji.frame.util.V_AppUtils;
import com.dji.frame.util.V_JsonUtil;
import com.dji.service.SignatureUtil;
import com.dji.service.popup.IDJIPopupInject;
import com.dji.service.popup.model.PopupModel;
import com.dji.service.popup.util.DJIPopupLogUtil;
import dji.component.accountcenter.IMemberProtocol;
import dji.component.areacode.IAreaCode;
import dji.component.areacode.IAreaCodeService;
import dji.component.flysafe.FlyForbidProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.DjiSharedPreferencesManager;
import dji.publics.protocol.ResponseBase;
import dji.publics.utils.LanguageUtils;
import dji.service.DJIAppServiceManager;
import dji.thirdparty.afinal.http.AjaxCallBack;
import dji.thirdparty.afinal.http.AjaxParams;
import dji.thirdparty.ciphersql.database.SQLiteDatabase;
import java.security.SignatureException;

@EXClassNullAway
class DJIPopupGetter {
    private static final int LEFT_CLOSE = 0;
    private static final int LEFT_NOT_AGAIN = 1;
    private static final int LOCATION_ANY = 0;
    private static final int LOCATION_FPV = 2;
    private static final int LOCATION_MAIN = 1;
    private static final int RIGHT_CLOSE = 0;
    private static final int RIGHT_JUMP_APP = 1;
    private static final int RIGHT_JUMP_H5 = 2;
    private static final String SP_KEY_IS_SHOW_POPUP_AGAIN_WITH = "SpKeyIsShowPopupAgainWith";
    private static final int STATUS_NORMAL = 0;
    private static final int TIME_ANY = 1;
    private static final int TIME_NOT_IN_FLY = 0;
    /* access modifiers changed from: private */
    public Context mContext;
    private IDJIPopupInject mInject;

    DJIPopupGetter() {
    }

    /* access modifiers changed from: package-private */
    public void initParam(Context context, IDJIPopupInject inject) {
        this.mContext = context;
        this.mInject = inject;
    }

    private String getLang() {
        String country = this.mContext.getResources().getConfiguration().locale.getCountry();
        if (TextUtils.isEmpty(country)) {
            return "en";
        }
        if (country.equalsIgnoreCase(IAreaCode.STR_AREA_CODE_CHINA)) {
            return "zh_cn";
        }
        if (country.equalsIgnoreCase("TW")) {
            return "zh_tw";
        }
        if (country.equalsIgnoreCase(IAreaCode.STR_AREA_CODE_JAPAN)) {
            return LanguageUtils.JA_DJI_LANG_CODE;
        }
        return "en";
    }

    /* access modifiers changed from: package-private */
    public void get(boolean isStartUp, String sn, String pType, String subType, String firmVersion) {
        IAreaCodeService areaCodeService = (IAreaCodeService) DJIAppServiceManager.getInstance().getService(IAreaCodeService.COMPONENT_NAME);
        AjaxParams params = new AjaxParams();
        StringBuilder debugInfo = new StringBuilder();
        String appVersion = getAppVersionName(this.mContext);
        params.put("app_version", appVersion);
        debugInfo.append("app_version=");
        debugInfo.append(appVersion);
        debugInfo.append("&");
        params.put("os_platform", FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        StringBuilder signString = new StringBuilder();
        if (!isStartUp) {
            params.put("notify_type", "1");
            debugInfo.append("type=1");
            debugInfo.append("&");
            params.put("firmware_version", firmVersion);
            debugInfo.append("firmware_version=");
            debugInfo.append(firmVersion);
            debugInfo.append("&");
            params.put("product_type", pType);
            debugInfo.append("product_type=");
            debugInfo.append(pType);
            debugInfo.append("&");
            params.put("sub_product_type", subType);
            debugInfo.append("sub_product_type=");
            debugInfo.append(subType);
            debugInfo.append("&");
        } else {
            debugInfo.append("type=0");
            debugInfo.append("&");
            params.put("notify_type", "0");
        }
        String cCode = areaCodeService.getAreaCodeFromLocal();
        params.put("nation_code", cCode);
        debugInfo.append("nation_code=");
        debugInfo.append(cCode);
        debugInfo.append("&");
        String province = areaCodeService.getLastProvince();
        params.put(ResponseBase.STRING_PROVINCE, province);
        debugInfo.append("province=");
        debugInfo.append(province);
        debugInfo.append("&");
        String city = areaCodeService.getLastCity();
        params.put(ResponseBase.STRING_CITY, city);
        debugInfo.append("city=");
        debugInfo.append(city);
        debugInfo.append("&");
        params.put("lang", getLang());
        String time = (System.currentTimeMillis() / 1000) + "";
        params.put("time", time);
        signString.append(time);
        signString.append(DJIPopupLogUtil.mSignKey);
        String signature = "";
        try {
            signature = SignatureUtil.calculateRFC2104HMAC(signString.toString(), DJIPopupLogUtil.mSignKey);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        params.put(IMemberProtocol.STRING_SIGNATURE, signature);
        if (DJIPopupLogUtil.IsDebugForDJILog) {
            DJIPopupLogUtil.LOG_FOR_DEBUG("167" + params.toString());
            DJIPopupLogUtil.LOG_FOR_RELEASE("167" + debugInfo.toString());
        } else {
            DJIPopupLogUtil.LOG_FOR_RELEASE("167" + debugInfo.toString());
        }
        V_AppUtils.getFinalHttp().post(DJIPopupLogUtil.getServerUrl(), params, new AjaxCallBack<String>() {
            /* class com.dji.service.popup.controller.DJIPopupGetter.AnonymousClass1 */

            public void onStart(boolean isResume) {
            }

            public void onLoading(long count, long current) {
            }

            public void onSuccess(String t) {
                DJIPopupLogUtil.LOG_FOR_RELEASE(" 187 " + t);
                DJIPopupGetter.this.handleGetMessage(t);
            }

            public void onFailure(Throwable t, int errorNo, String strMsg) {
                DJIPopupLogUtil.LOG_FOR_RELEASE(" 193 " + strMsg);
            }
        });
    }

    private static String getAppVersionName(Context context) {
        try {
            String vName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            if (vName != null) {
                return vName;
            }
            return "0.0.0";
        } catch (Exception e) {
            e.printStackTrace();
            return "0.0.0";
        }
    }

    /* access modifiers changed from: private */
    public void handleGetMessage(String t) {
        PopupModel model = (PopupModel) V_JsonUtil.toBean(t, PopupModel.class);
        if (check(model)) {
            PopupModel.Result[] resultArr = model.result;
            for (PopupModel.Result result : resultArr) {
                boolean isShow = DjiSharedPreferencesManager.getBoolean(this.mContext, SP_KEY_IS_SHOW_POPUP_AGAIN_WITH + result.id, true);
                DJIPopupLogUtil.LOG_FOR_DEBUG("161 message id = " + result.id + " isShow=" + isShow);
                if (isShow) {
                    handleOneItem(result);
                }
            }
        }
    }

    private boolean check(PopupModel model) {
        return model != null && model.status == 0 && model.result != null && model.result.length > 0;
    }

    private void handleOneItem(PopupModel.Result result) {
        boolean isFlying = this.mInject.isMotorUp();
        String title = result.title;
        String msg = result.content;
        String left = result.left_button_msg;
        String right = result.right_button_msg;
        DJIPopupLogUtil.LOG_FOR_DEBUG("195 popup_time = " + result.popup_time + " isFlying=" + isFlying);
        if (1 == result.popup_time || (result.popup_time == 0 && !isFlying)) {
            DJIPopupLogUtil.LOG_FOR_DEBUG("198 popup_location = " + result.popup_location);
            boolean isInMainActivity = this.mInject.isInMainPage();
            boolean isInFPV = this.mInject.isInFPV();
            if ((result.popup_location == 1 && isInMainActivity) || ((result.popup_location == 2 && isInFPV) || result.popup_location <= 0)) {
                showDialog(result, title, msg, left, right);
            }
        }
    }

    private void showDialog(final PopupModel.Result result, String title, String msg, String left, String right) {
        DialogInterface.OnClickListener leftListener = null;
        if (!TextUtils.isEmpty(left)) {
            leftListener = new DialogInterface.OnClickListener() {
                /* class com.dji.service.popup.controller.DJIPopupGetter.AnonymousClass2 */

                public void onClick(DialogInterface dialog, int which) {
                    if (result.left_button_command == 1) {
                        DjiSharedPreferencesManager.putBoolean(DJIPopupGetter.this.mContext, DJIPopupGetter.SP_KEY_IS_SHOW_POPUP_AGAIN_WITH + result.id, false);
                    }
                    dialog.dismiss();
                }
            };
        }
        DialogInterface.OnClickListener rightListener = null;
        if (!TextUtils.isEmpty(right)) {
            rightListener = new DialogInterface.OnClickListener() {
                /* class com.dji.service.popup.controller.DJIPopupGetter.AnonymousClass3 */

                public void onClick(DialogInterface dialog, int which) {
                    if (2 == result.right_button_command) {
                        try {
                            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(result.jump_url));
                            intent.setFlags(SQLiteDatabase.CREATE_IF_NECESSARY);
                            DJIPopupGetter.this.mContext.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            DJIPopupLogUtil.LOG_FOR_RELEASE("parse server uri error:" + e.getMessage());
                        }
                    }
                    dialog.dismiss();
                }
            };
        }
        this.mInject.showDialog(title, msg, left, right, leftListener, rightListener);
    }
}
