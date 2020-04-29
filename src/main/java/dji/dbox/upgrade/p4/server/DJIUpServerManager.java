package dji.dbox.upgrade.p4.server;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.dji.api.FlightHttpApi;
import com.dji.frame.util.MD5;
import com.dji.frame.util.V_ActivityUtil;
import com.dji.frame.util.V_AppUtils;
import com.dji.frame.util.V_FileUtil;
import dji.component.accountcenter.IMemberProtocol;
import dji.component.flysafe.FlyForbidProtocol;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpUrlModel;
import dji.dbox.upgrade.p4.utils.DJITarUtils;
import dji.dbox.upgrade.p4.utils.DJIUpFilesUtils;
import dji.dbox.upgrade.p4.utils.DJIUpStatusOfflineHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.broadcastReceivers.DJINetWorkReceiver;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.natives.UpgradeVerify;
import dji.midware.util.BytesUtil;
import dji.proto.djigo_services.AbTestRequestWrapper;
import dji.publics.utils.LanguageUtils;
import dji.thirdparty.afinal.FinalHttp;
import dji.thirdparty.afinal.http.AjaxCallBack;
import dji.thirdparty.afinal.http.HttpHandler;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.http.entity.StringEntity;

@EXClassNullAway
public class DJIUpServerManager {
    public static final String CFG_NAME_INFIX = "_ac_fw_v";
    private static final String TAG = "DJIUpServerManager";
    private static final boolean isDebug = true;
    public static final String latestFiles = "latest/";
    public static final String outFiles = "out";
    public static final String tarFiles = "tarFiles";
    private String cfgNamePre = "";
    /* access modifiers changed from: private */
    public String cfgTarget = "";
    private final String charset = "UTF-8";
    private final String contentType = AbTestRequestWrapper.CONTENT_TYPE;
    private final Context context;
    private String fileAppend;
    private FinalHttp finalHttp;
    private String product_id = "";
    private String tarInDir = "tarFiles/";
    /* access modifiers changed from: private */
    public String tarOutDir = "out/";
    private DJIUpUrlModel urlModel;

    public interface TarCallBack {
        void onFailed();

        void onSuccess();

        void onZipProgress(int i);
    }

    public DJIUpServerManager(Context context2, String product_id2) {
        this.context = context2;
        this.finalHttp = V_AppUtils.getFinalHttp(context2);
        this.product_id = product_id2;
        this.fileAppend = getFileAppend(context2);
        this.cfgTarget = this.fileAppend + DJIUpConstants.directory + product_id2 + IMemberProtocol.PARAM_SEPERATOR;
        this.tarInDir = this.cfgTarget + this.tarInDir;
        this.tarOutDir = this.tarInDir + this.tarOutDir;
        File cfgTargetFile = new File(this.tarOutDir);
        if (!cfgTargetFile.exists()) {
            cfgTargetFile.mkdirs();
        }
    }

    private static String getFileAppend(Context context2) {
        if (V_ActivityUtil.isApkDebugable(context2)) {
            return Environment.getExternalStorageDirectory() + "/DJI/UpgradeDebug";
        }
        return context2.getFilesDir().getAbsolutePath();
    }

    public static String getcfgTarget(Context context2, String product_id2) {
        return getFileAppend(context2) + DJIUpConstants.directory + product_id2 + IMemberProtocol.PARAM_SEPERATOR;
    }

    public static String getcfgImageTarget(Context context2, String product_id2, String product_version, String moduleName) {
        return getcfgTarget(context2, product_id2) + tarFiles + IMemberProtocol.PARAM_SEPERATOR + product_version + IMemberProtocol.PARAM_SEPERATOR + moduleName;
    }

    public String getCfgTarget() {
        return this.cfgTarget;
    }

    /* access modifiers changed from: private */
    public String getCfgSuffix() {
        return DJIUpDeviceType.find(this.product_id).getCfgSuffix();
    }

    public String getProductId() {
        return this.product_id;
    }

    public boolean isSameProduct(String product_id2) {
        return this.product_id.equals(product_id2);
    }

    public void setUrlModel(DJIUpUrlModel model) {
        DJIUpStatusOfflineHelper.saveUrlModel(model);
        this.urlModel = model;
    }

    public void deleteFiles(DJIUpCfgModel exludeCfgModel) {
        DJIUpFilesUtils.deleteFiles(this.context, this.fileAppend + DJIUpConstants.directory, exludeCfgModel.releaseVersion);
    }

    public List<String> getUpgradeFiles(String verCode, List<String> fileNames) {
        String imageDirPath = getImageDirPath(verCode);
        String cfgFilePath = getCfgFilePath(verCode);
        if (new File(cfgFilePath).exists()) {
            fileNames.add(cfgFilePath.replace(imageDirPath, ""));
        } else {
            fileNames.add(this.cfgTarget + latestFiles + verCode + getCfgSuffix());
        }
        List<String> upgradeFiles = new ArrayList<>();
        for (String name : fileNames) {
            upgradeFiles.add((name.contains(IMemberProtocol.PARAM_SEPERATOR) ? new File(name) : new File(imageDirPath, name)).getAbsolutePath());
        }
        return upgradeFiles;
    }

    public void tarPackage(final String verCode, final ArrayList<String> fileNames, final TarCallBack callBack) {
        this.cfgNamePre = getCfgNamePre(this.product_id, verCode);
        DJIUpConstants.LOGE("", "tar package start");
        if (isHasTar(verCode)) {
            DJIUpConstants.LOGE("", "tar package have exist in local");
            callBack.onSuccess();
            return;
        }
        DJIUpConstants.LOGE("", "tar package thread start");
        new Thread(new Runnable() {
            /* class dji.dbox.upgrade.p4.server.DJIUpServerManager.AnonymousClass1 */

            public void run() {
                String mtarInDir = DJIUpServerManager.this.getImageDirPath(verCode);
                String cfgFilePath = DJIUpServerManager.this.getCfgFilePath(verCode);
                if (new File(cfgFilePath).exists()) {
                    fileNames.add(cfgFilePath.replace(mtarInDir, ""));
                } else {
                    fileNames.add(DJIUpServerManager.this.cfgTarget + DJIUpServerManager.latestFiles + verCode + DJIUpServerManager.this.getCfgSuffix());
                }
                File cfgTargetFile = new File(DJIUpServerManager.this.tarOutDir);
                if (!cfgTargetFile.exists()) {
                    cfgTargetFile.mkdirs();
                }
                DJITarUtils.tarFiles(DJIUpServerManager.this.getTarFilePath(), mtarInDir, fileNames, callBack, new DJITarUtils.Callback() {
                    /* class dji.dbox.upgrade.p4.server.DJIUpServerManager.AnonymousClass1.C00031 */

                    public void onTarSuccess() {
                        DJIUpConstants.LOGE("", "tar package onSuccess");
                        callBack.onSuccess();
                    }

                    public void onTarException(Exception e) {
                        DJIUpConstants.LOGE("", "tar package failed=" + e.getMessage());
                        new File(DJIUpServerManager.this.getTarFilePath()).delete();
                        e.printStackTrace();
                        callBack.onFailed();
                    }
                });
            }
        }, "upServer").start();
    }

    static String getCfgNamePre(String productId, String verCode) {
        StringBuilder builder = new StringBuilder();
        builder.append(productId).append(CFG_NAME_INFIX).append(verCode.replace(".", "_"));
        if (DJIUpDeviceType.wm335.getProductId().equals(productId) && !ServiceManager.getInstance().isRemoteOK()) {
            builder.append("_rc");
        }
        return builder.toString();
    }

    public String getTarFilePath() {
        return this.tarOutDir + this.cfgNamePre + ".tar";
    }

    private String getTarFilePath(String verCode) {
        return this.tarOutDir + getCfgNamePre(this.product_id, verCode) + ".tar";
    }

    public boolean isHasTar(String verCode) {
        return new File(getTarFilePath(verCode)).exists();
    }

    public String getCfgFilePath(String verCode) {
        String mtarInDir = getImageDirPath(verCode);
        File file = new File(mtarInDir);
        if (file.exists() || file.mkdirs()) {
            return mtarInDir + getCfgNamePre(this.product_id, verCode) + getCfgSuffix();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public String getImageDirPath(String verCode) {
        return this.tarInDir + verCode + IMemberProtocol.PARAM_SEPERATOR;
    }

    public String getImageFilePath(String verCode, String target) {
        return getImageDirPath(verCode) + target;
    }

    public void getUrlList(AjaxCallBack<String> callBack) throws UnsupportedEncodingException {
        if (!DJINetWorkReceiver.getNetWorkStatusNoPing(this.context)) {
            callBack.onFailure(null, 0, "network no connected");
            return;
        }
        StringBuilder builder = new StringBuilder();
        long time = System.currentTimeMillis();
        builder.append("time=" + time);
        builder.append("&os=" + FlyForbidProtocol.PLATFORM_NAME_FOR_JNI);
        builder.append("&version=" + DJIUpConstants.appVersion);
        builder.append("&signature=" + MD5.getMD5(UpgradeVerify.native_getMD5Input(time + FlyForbidProtocol.PLATFORM_NAME_FOR_JNI + DJIUpConstants.appVersion, 1)));
        this.finalHttp.post(FlightHttpApi.getLoadConfigGetUrlUrl(), new StringEntity(builder.toString(), "UTF-8"), AbTestRequestWrapper.CONTENT_TYPE, callBack);
    }

    public void getList(AjaxCallBack<String> callBack) throws UnsupportedEncodingException {
        if (this.urlModel == null || this.urlModel.apis == null) {
            callBack.onFailure(null, 0, "urlModel null");
        } else {
            getList(this.urlModel.apis.allfile, this.product_id, callBack);
        }
    }

    private void getList(String url, String pid, AjaxCallBack<String> callBack) throws UnsupportedEncodingException {
        String lan;
        DJIUpConstants.LOGE("server", "getList product_id=" + pid);
        StringBuilder builder = new StringBuilder();
        builder.append("product_id=" + pid);
        builder.append("&signature=" + MD5.getMD5(UpgradeVerify.native_getMD5Input(pid + DJIUpConstants.getUserAccount())));
        if (DJIUpConstants.isLogin()) {
            builder.append("&token=" + DJIUpConstants.getUserToken());
            builder.append("&account=" + DJIUpConstants.getUserAccount());
        }
        String lan2 = Locale.getDefault().toString().toLowerCase();
        if (lan2.contains(LanguageUtils.JA_DJI_LANG_CODE)) {
            lan = LanguageUtils.JA_DJI_LANG_CODE;
        } else if (lan2.contains(LanguageUtils.CN_DJI_LANG_CODE)) {
            lan = "zh_cn";
        } else if (lan2.contains(LanguageUtils.TW_DJI_LANG_CODE)) {
            lan = "zh_tw";
        } else if (lan2.contains("en")) {
            lan = "en";
        } else {
            lan = "en";
        }
        builder.append("&lan=" + lan);
        this.finalHttp.post(url, new StringEntity(builder.toString(), "UTF-8"), AbTestRequestWrapper.CONTENT_TYPE, callBack);
    }

    public void saveServerList(String listJson) {
        String target = this.cfgTarget + latestFiles;
        File dir = new File(target);
        if (dir.exists() || dir.mkdirs()) {
            V_FileUtil.fileWrite(new File(target + DJIUpGetServerCfgManager.listName), listJson);
        }
    }

    public HttpHandler<File> getCfg(AjaxCallBack<File> callBack, String product_version) throws UnsupportedEncodingException {
        if (this.urlModel == null || this.urlModel.apis == null) {
            callBack.onFailure(null, 0, "urlModel null");
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("product_id=" + this.product_id);
        builder.append("&product_version=" + product_version);
        builder.append("&signature=" + MD5.getMD5(UpgradeVerify.native_getMD5Input(this.product_id + product_version + DJIUpConstants.getUserToken())));
        if (DJIUpConstants.isLogin()) {
            builder.append("&token=" + DJIUpConstants.getUserToken());
            builder.append("&account=" + DJIUpConstants.getUserAccount());
        }
        return this.finalHttp.postDownload(this.urlModel.apis.config, getCfgFilePath(product_version), new StringEntity(builder.toString(), "UTF-8"), AbTestRequestWrapper.CONTENT_TYPE, false, false, callBack);
    }

    public HttpHandler<File> getCfgNew(AjaxCallBack<File> callBack, String product_version, String ext) throws UnsupportedEncodingException {
        if (this.urlModel == null || this.urlModel.apis == null) {
            callBack.onFailure(null, 0, "urlModel null");
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("product_id=" + this.product_id);
        builder.append("&product_version=" + product_version);
        builder.append("&signature=" + MD5.getMD5(UpgradeVerify.native_getMD5Input(this.product_id + product_version + DJIUpConstants.getUserToken())));
        if (DJIUpConstants.isLogin()) {
            builder.append("&token=" + DJIUpConstants.getUserToken());
            builder.append("&account=" + DJIUpConstants.getUserAccount());
        }
        return this.finalHttp.postDownload(this.urlModel.apis.config, getCfgFilePath(product_version) + ext, new StringEntity(builder.toString(), "UTF-8"), AbTestRequestWrapper.CONTENT_TYPE, false, false, callBack);
    }

    public void getDownPath(AjaxCallBack<String> callBack, String product_version, String module_version, String module_id) throws UnsupportedEncodingException {
        if (this.urlModel == null || this.urlModel.apis == null) {
            callBack.onFailure(null, 0, "urlModel null");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("signature=" + MD5.getMD5(UpgradeVerify.native_getMD5Input(this.product_id + product_version + module_id + module_version + DJIUpConstants.getUserToken())));
        builder.append("&product_id=" + this.product_id);
        builder.append("&product_version=" + product_version);
        builder.append("&module_id=" + module_id);
        builder.append("&module_version=" + module_version);
        if (DJIUpConstants.isLogin()) {
            builder.append("&token=" + DJIUpConstants.getUserToken());
        }
        this.finalHttp.post(this.urlModel.apis.down, new StringEntity(builder.toString(), "UTF-8"), AbTestRequestWrapper.CONTENT_TYPE, callBack);
    }

    public HttpHandler<File> downloadPkg(String target, AjaxCallBack<File> callBack, String product_version, String module_version, String module_id, long size, String type) throws UnsupportedEncodingException {
        String path = getImageFilePath(product_version, target);
        File file = new File(path.replace(DJIUpConstants.UP_TEMP_FILE, ""));
        File file2 = new File(path);
        DJIUpConstants.LOGD(TAG, file.getName() + " exists=" + file.exists() + " length=" + file.length() + " temp exists=" + file2.exists() + " length=" + file2.length());
        if (file.exists() && file.length() >= size) {
            callBack.onLoading(size, size);
            callBack.onSuccess(file);
            return null;
        } else if (file2.exists() && file2.length() >= size && file2.renameTo(file)) {
            callBack.onLoading(size, size);
            callBack.onSuccess(file2);
            return null;
        } else if (this.urlModel == null || this.urlModel.apis == null) {
            callBack.onFailure(null, 0, "urlModel null");
            return null;
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("signature=" + MD5.getMD5(UpgradeVerify.native_getMD5Input(this.product_id + product_version + module_id + module_version + DJIUpConstants.getUserToken())));
            builder.append("&product_id=" + this.product_id);
            builder.append("&product_version=" + product_version);
            builder.append("&module_id=" + module_id);
            builder.append("&module_version=" + module_version);
            if (type != null && !type.equals("")) {
                builder.append("&filename=" + target.replace(DJIUpConstants.UP_TEMP_FILE, ""));
            }
            if (DJIUpConstants.isLogin()) {
                builder.append("&token=" + DJIUpConstants.getUserToken());
                builder.append("&account=" + DJIUpConstants.getUserAccount());
            }
            return this.finalHttp.postDownload(this.urlModel.apis.firm, getImageFilePath(product_version, target), new StringEntity(builder.toString(), "UTF-8"), AbTestRequestWrapper.CONTENT_TYPE, true, callBack);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.thirdparty.afinal.FinalHttp.download(java.lang.String, java.lang.String, boolean, dji.thirdparty.afinal.http.AjaxCallBack<java.io.File>):dji.thirdparty.afinal.http.HttpHandler<java.io.File>
     arg types: [java.lang.String, java.lang.String, int, dji.thirdparty.afinal.http.AjaxCallBack<java.io.File>]
     candidates:
      dji.thirdparty.afinal.FinalHttp.download(java.lang.String, dji.thirdparty.afinal.http.AjaxParams, java.lang.String, dji.thirdparty.afinal.http.AjaxCallBack<java.io.File>):dji.thirdparty.afinal.http.HttpHandler<java.io.File>
      dji.thirdparty.afinal.FinalHttp.download(java.lang.String, java.lang.String, boolean, dji.thirdparty.afinal.http.AjaxCallBack<java.io.File>):dji.thirdparty.afinal.http.HttpHandler<java.io.File> */
    public HttpHandler<File> downloadPkg(String downPath, String target, String product_version, long size, AjaxCallBack<File> callBack) throws UnsupportedEncodingException {
        File t = new File(getImageFilePath(product_version, target));
        if (t.exists()) {
            Log.d("download", "t.length()=" + t.length() + " size=" + size + " result=" + (t.length() >= size));
            if (t.length() < size) {
                Log.d("download", "downPath=" + downPath);
            }
        }
        if (!t.exists() || t.length() < size) {
            return this.finalHttp.download(downPath, getImageFilePath(product_version, target), true, callBack);
        }
        callBack.onLoading(size, size);
        callBack.onSuccess(t);
        return null;
    }

    private String getHexString(int value) {
        return "0x" + BytesUtil.byte2hex((byte) value);
    }

    public boolean isUrlGetted() {
        return this.urlModel != null;
    }
}
