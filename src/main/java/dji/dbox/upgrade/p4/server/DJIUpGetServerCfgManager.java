package dji.dbox.upgrade.p4.server;

import android.content.Context;
import com.dji.frame.util.V_FileUtil;
import com.dji.frame.util.V_JsonUtil;
import com.google.gson.reflect.TypeToken;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpUrlModel;
import dji.dbox.upgrade.p4.model.paser.DJIUpXmlPaser;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpStatusOfflineHelper;
import dji.dbox.upgrade.p4.utils.DJIUpgradeBaseUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.natives.UpgradeVerify;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@EXClassNullAway
public class DJIUpGetServerCfgManager {
    private static final String TAG = "DJIUpGetServerCfgManager";
    public static final String latestCfgName = ".cfg.sig";
    public static final String listName = "list.json";
    private boolean isAlive;
    /* access modifiers changed from: private */
    public boolean isOffline;
    /* access modifiers changed from: private */
    public List<DJIUpListElement> list;
    private DJIUpGetServerCfgListener listener;
    /* access modifiers changed from: private */
    public String productId;
    /* access modifiers changed from: private */
    public int serverCfgIndex;
    /* access modifiers changed from: private */
    public DJIUpServerManager serverManager;

    public interface DJIUpGetServerCfgListener {
        void onGetFail(String str);

        void onGetSuccess(List<DJIUpListElement> list);
    }

    public DJIUpGetServerCfgManager(Context context, String product_id) {
        this.serverManager = new DJIUpServerManager(context, product_id);
        this.productId = product_id;
    }

    public void setIsOffline(boolean isOffline2) {
        this.isOffline = isOffline2;
    }

    public void setListener(DJIUpGetServerCfgListener listener2) {
        this.listener = listener2;
    }

    public void start() {
        this.isAlive = true;
        if (this.list != null) {
            this.list.clear();
        }
        if (this.serverManager.isUrlGetted()) {
            getServerList();
        } else {
            getUrlList();
        }
    }

    public void stop() {
        this.isAlive = false;
    }

    /* access modifiers changed from: private */
    public void onFail(String s) {
        DJIUpConstants.LOGD(TAG, s);
        this.listener.onGetFail(s);
        this.isAlive = false;
    }

    private void onSuc() {
        this.listener.onGetSuccess(this.list);
        this.isAlive = false;
    }

    private void getUrlList() {
        if (this.isAlive) {
            try {
                this.serverManager.getUrlList(new AsyncAjaxCallback<String>() {
                    /* class dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager.AnonymousClass1 */

                    public void asyncOnStart(boolean b) {
                    }

                    public void asyncOnLoading(long count, long current) {
                    }

                    public void asyncOnSuccess(String t) {
                        DJIUpUrlModel urlModel = (DJIUpUrlModel) V_JsonUtil.toBean(t, DJIUpUrlModel.class);
                        if (urlModel == null || urlModel.apis == null || urlModel.apis.allfile == null) {
                            DJIUpGetServerCfgManager.this.onFail("getUrlList -- urlModel null");
                            return;
                        }
                        DJIUpConstants.LOGD_WIFI(DJIUpGetServerCfgManager.TAG, "getUrlList -- onSuccess");
                        DJIUpGetServerCfgManager.this.serverManager.setUrlModel(urlModel);
                        DJIUpGetServerCfgManager.this.getServerList();
                    }

                    public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                        DJIUpGetServerCfgManager.this.onFail("getUrlList -- onFailure " + strMsg);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                onFail("getUrlList --" + e.getMessage());
            }
        }
    }

    private String getDir() {
        String target = this.serverManager.getCfgTarget() + DJIUpServerManager.latestFiles;
        File dir = new File(target);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return target;
    }

    private static String getDir(String target) {
        String target2 = target + DJIUpServerManager.latestFiles;
        File dir = new File(target2);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return target2;
    }

    private void copyToDir(String product_version, File f) {
        f.renameTo(new File(getDir() + product_version + ".cfg.sig"));
    }

    private File isExistCfg(String product_version) {
        return new File(getDir() + product_version + ".cfg.sig");
    }

    /* access modifiers changed from: private */
    public void saveList(String t) {
        V_FileUtil.fileWrite(new File(getDir() + listName), t);
    }

    /* access modifiers changed from: private */
    public static List<DJIUpListElement> parseList(String t) {
        Object valueOf;
        if (t == null) {
            return null;
        }
        try {
            List<DJIUpListElement> list2 = V_JsonUtil.getList(t, new TypeToken<List<DJIUpListElement>>() {
                /* class dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager.AnonymousClass2 */
            });
            StringBuilder append = new StringBuilder().append("getServerList -- onSuccess size=");
            if (list2 == null) {
                valueOf = "";
            } else {
                valueOf = Integer.valueOf(list2.size());
            }
            DJIUpConstants.LOGD_WIFI(TAG, append.append(valueOf).toString());
            if (list2 == null || list2.size() <= 0) {
                return null;
            }
            Collections.sort(list2, new Comparator<DJIUpListElement>() {
                /* class dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager.AnonymousClass3 */

                public int compare(DJIUpListElement lhs, DJIUpListElement rhs) {
                    return DJIUpgradeBaseUtils.compareFirVer(rhs.product_version, lhs.product_version);
                }
            });
            filterElements(list2);
            return list2;
        } catch (Exception e) {
            DJIUpConstants.LOGD_WIFI(TAG, "parseList " + e.getMessage());
            return null;
        }
    }

    public static List<DJIUpListElement> parseLocalList(Context context, String target, String productId2) {
        Object valueOf;
        DJIUpConstants.LOGD_WIFI(TAG, "parseLocalList target=" + target + " productId=" + productId2);
        boolean isOffline2 = DJIUpDeviceType.find(productId2).isOffline();
        if (target == null || (context == null && !isOffline2)) {
            return null;
        }
        try {
            List<DJIUpListElement> list2 = V_JsonUtil.getList(V_FileUtil.fileGetContent(new File(getDir(target) + listName)), new TypeToken<List<DJIUpListElement>>() {
                /* class dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager.AnonymousClass4 */
            });
            StringBuilder append = new StringBuilder().append("getServerList offline -- onSuccess size=");
            if (list2 == null) {
                valueOf = "null";
            } else {
                valueOf = Integer.valueOf(list2.size());
            }
            DJIUpConstants.LOGD_WIFI(TAG, append.append(valueOf).toString());
            if (list2 == null || list2.size() <= 0) {
                return null;
            }
            filterElements(list2);
            Iterator<DJIUpListElement> iterator = list2.iterator();
            while (iterator.hasNext()) {
                DJIUpListElement element = iterator.next();
                String version = element.product_version;
                if (isOffline2) {
                    element.cfgModel = parseCfg(getDir(target) + element.product_version + ".cfg.sig", productId2);
                } else {
                    element.cfgModel = parseCfg(target + DJIUpServerManager.tarFiles + File.separator + version + File.separator + DJIUpServerManager.getCfgNamePre(productId2, version) + ".cfg.sig", productId2);
                    if (!DJIUpStatusHelper.isPackageDownloaded(context, DJIUpDeviceType.find(productId2), element) && element.cfgModel != null) {
                        element.cfgModel = null;
                    }
                }
                if (element.cfgModel != null) {
                    DJIUpConstants.LOGD_WIFI(TAG, "read offline " + version + " cfg " + element.cfgModel.releaseVersion);
                } else {
                    iterator.remove();
                    DJIUpConstants.LOGD_WIFI(TAG, "read offline " + version + " cfg null remove it");
                }
            }
            return list2;
        } catch (Exception e) {
            DJIUpConstants.LOGD_WIFI(TAG, "parseLocalList " + e.getMessage());
            return null;
        }
    }

    private static DJIUpCfgModel parseCfg(String path, String product_id) {
        String outpath = path.replace(".cfg.sig", DJIUpDeviceType.MODE_PARSER_SUFFIX);
        if (UpgradeVerify.native_verifyCfg(path, outpath, DJIUpDeviceType.find(product_id).isNeedVerify())) {
            try {
                File file = new File(outpath);
                DJIUpCfgModel cfgModel = DJIUpXmlPaser.getCfgModel(file);
                DJIUpConstants.LOGD_WIFI(TAG, "parseCfg--verify ok , delete=" + file.delete());
                return cfgModel;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                DJIUpConstants.LOGD_WIFI(TAG, "getCfg FileNotFoundException");
            }
        } else {
            DJIUpConstants.LOGD_WIFI(TAG, "parseCfg --verify failure");
            return null;
        }
    }

    /* access modifiers changed from: private */
    public void getServerList() {
        if (this.isAlive) {
            try {
                DJIUpConstants.LOGD_WIFI(TAG, "getServerList start");
                this.serverManager.getList(new AsyncAjaxCallback<String>() {
                    /* class dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager.AnonymousClass5 */

                    public void asyncOnStart(boolean b) {
                    }

                    public void asyncOnLoading(long count, long current) {
                    }

                    public void asyncOnSuccess(String t) {
                        DJIUpConstants.LOGD_WIFI(DJIUpGetServerCfgManager.TAG, "getServerList over");
                        try {
                            List unused = DJIUpGetServerCfgManager.this.list = DJIUpGetServerCfgManager.parseList(t);
                            if (DJIUpGetServerCfgManager.this.list != null) {
                                int unused2 = DJIUpGetServerCfgManager.this.serverCfgIndex = -1;
                                DJIUpGetServerCfgManager.this.nextCfg();
                            } else {
                                DJIUpGetServerCfgManager.this.onFail("get list null");
                            }
                            if (DJIUpGetServerCfgManager.this.isOffline || DJIUpStatusOfflineHelper.isLastDevice(DJIUpGetServerCfgManager.this.productId)) {
                                DJIUpGetServerCfgManager.this.saveList(t);
                            }
                        } catch (Exception e) {
                            DJIUpGetServerCfgManager.this.onFail("getServerList-Exception -- " + t);
                        }
                    }

                    public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                        DJIUpGetServerCfgManager.this.onFail("getServerCFG -- onFailure " + strMsg);
                    }
                });
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                onFail("getServerCFG --" + e.getMessage());
            }
        }
    }

    private static void filterElements(List<DJIUpListElement> list2) {
        int size = list2.size();
        int i = 0;
        while (i < size) {
            DJIUpListElement element = list2.get(i);
            if (element.isDeprecated()) {
                list2.remove(element);
                size--;
                i--;
            }
            i++;
        }
    }

    /* access modifiers changed from: private */
    public void nextCfg() {
        if (this.isAlive) {
            if (this.list == null) {
                onFail("");
            } else if (this.serverCfgIndex + 1 < this.list.size()) {
                this.serverCfgIndex++;
                getCfg(this.list.get(this.serverCfgIndex));
            } else {
                onSuc();
            }
        }
    }

    private void getCfg(final DJIUpListElement djiUpListElement) {
        DJIUpConstants.LOGD_WIFI(TAG, "getCfg from server --" + djiUpListElement.product_version);
        if (this.isOffline) {
            File file = isExistCfg(djiUpListElement.product_version);
            if (file.exists()) {
                onSucGetCfg(djiUpListElement, file);
                return;
            }
        } else {
            File ff = new File(this.serverManager.getCfgFilePath(djiUpListElement.product_version));
            if (ff.exists()) {
                onSucGetCfg(djiUpListElement, ff);
                return;
            }
        }
        try {
            this.serverManager.getCfgNew(new AsyncAjaxCallback<File>() {
                /* class dji.dbox.upgrade.p4.server.DJIUpGetServerCfgManager.AnonymousClass6 */

                public void asyncOnStart(boolean b) {
                }

                public void asyncOnLoading(long count, long current) {
                }

                public void asyncOnSuccess(File t) {
                    File destFile = new File(t.getAbsolutePath().replace(DJIUpConstants.UP_TEMP_FILE, ""));
                    t.renameTo(destFile);
                    DJIUpGetServerCfgManager.this.onSucGetCfg(djiUpListElement, destFile);
                }

                public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                    DJIUpGetServerCfgManager.this.onFail("getCfg from server --onFailure " + strMsg);
                }
            }, djiUpListElement.product_version, DJIUpConstants.UP_TEMP_FILE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            onFail("getCfg from server --onFailure " + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void onSucGetCfg(DJIUpListElement djiUpListElement, File t) {
        if (this.isOffline) {
            copyToDir(djiUpListElement.product_version, t);
            nextCfg();
            return;
        }
        String path = t.getAbsolutePath();
        DJIUpConstants.LOGD_WIFI(TAG, "t=" + t.getAbsolutePath());
        DJIUpCfgModel cfgModel = parseCfg(path, this.productId);
        if (cfgModel != null) {
            djiUpListElement.cfgModel = cfgModel;
            nextCfg();
            return;
        }
        onFail("getCfg null");
    }
}
