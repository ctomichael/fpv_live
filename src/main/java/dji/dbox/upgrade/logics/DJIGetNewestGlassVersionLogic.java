package dji.dbox.upgrade.logics;

import android.content.Context;
import com.dji.frame.util.V_JsonUtil;
import com.google.gson.reflect.TypeToken;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.model.DJIUpUrlModel;
import dji.dbox.upgrade.p4.server.AsyncAjaxCallback;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpgradeBaseUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCommonSetNewestVersions;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@EXClassNullAway
public class DJIGetNewestGlassVersionLogic {
    private String product_id = DJIUpDeviceType.wm220.toString();
    /* access modifiers changed from: private */
    public DJIUpServerManager serverManager;
    private DataCommonSetNewestVersions setNewestVersions = new DataCommonSetNewestVersions();

    public DJIGetNewestGlassVersionLogic(Context context) {
        this.serverManager = new DJIUpServerManager(context, this.product_id);
    }

    public void getGlassNewestVersion() {
        try {
            this.serverManager.getUrlList(new AsyncAjaxCallback<String>() {
                /* class dji.dbox.upgrade.logics.DJIGetNewestGlassVersionLogic.AnonymousClass1 */

                public void asyncOnStart(boolean b) {
                }

                public void asyncOnLoading(long count, long current) {
                }

                public void asyncOnSuccess(String s) {
                    DJIUpUrlModel urlModel = (DJIUpUrlModel) V_JsonUtil.toBean(s, DJIUpUrlModel.class);
                    if (urlModel != null && urlModel.apis != null && urlModel.apis.allfile != null) {
                        DJIUpConstants.LOGD("getGlassNewestVersion", "glass getUrlList -- onSuccess");
                        DJIGetNewestGlassVersionLogic.this.serverManager.setUrlModel(urlModel);
                        DJIGetNewestGlassVersionLogic.this.getServerList();
                    }
                }

                public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                    DJIUpConstants.LOGD("getGlassNewestVersion", "glass getUrlList -- onFailure " + strMsg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            DJIUpConstants.LOGD("getGlassNewestVersion", "glass getUrlList -- onFailure " + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void getServerList() {
        try {
            this.serverManager.getList(new AsyncAjaxCallback<String>() {
                /* class dji.dbox.upgrade.logics.DJIGetNewestGlassVersionLogic.AnonymousClass2 */

                public void asyncOnStart(boolean b) {
                }

                public void asyncOnLoading(long count, long current) {
                }

                public void asyncOnSuccess(String s) {
                    try {
                        List<DJIUpListElement> list = V_JsonUtil.getList(s, new TypeToken<List<DJIUpListElement>>() {
                            /* class dji.dbox.upgrade.logics.DJIGetNewestGlassVersionLogic.AnonymousClass2.AnonymousClass1 */
                        });
                        DJIUpConstants.LOGD("getGlassNewestVersion", "glass getServerList -- onSuccess size=" + list.size());
                        if (list.size() > 0) {
                            Collections.sort(list, new Comparator<DJIUpListElement>() {
                                /* class dji.dbox.upgrade.logics.DJIGetNewestGlassVersionLogic.AnonymousClass2.AnonymousClass2 */

                                public int compare(DJIUpListElement lhs, DJIUpListElement rhs) {
                                    return DJIUpgradeBaseUtils.compareFirVer(rhs.product_version, lhs.product_version);
                                }
                            });
                            DJIUpListElement element = (DJIUpListElement) list.get(0);
                            DJIUpConstants.LOGD("getGlassNewestVersion", "glass element.product_version " + element.product_version);
                            DJIGetNewestGlassVersionLogic.this.writeToRc(element.product_version);
                        }
                    } catch (Exception e) {
                        DJIUpConstants.LOGD("getGlassNewestVersion", "glass getServerList-Exception -- " + s);
                    }
                }

                public void asyncOnFailure(Throwable t, int errorNo, String strMsg) {
                    DJIUpConstants.LOGD("getGlassNewestVersion", "glass getServerList -- onFailure " + strMsg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            DJIUpConstants.LOGD("getGlassNewestVersion", "glass getServerList -- onFailure " + e.getMessage());
        }
    }

    /* access modifiers changed from: private */
    public void writeToRc(String product_version) {
        DeviceType deviceType;
        if (DJIProductManager.getInstance().isRcSeted()) {
            ProductType productType = DJIProductManager.getInstance().getRcType();
            deviceType = (productType == ProductType.KumquatX || productType == ProductType.KumquatS) ? DeviceType.DM368_G : DeviceType.OSD;
        } else {
            DJIUpDeviceType type = DJIUpStatusHelper.getConnectDeviceType();
            if (type == null) {
                DJIUpConstants.LOGD("getGlassNewestVersion", "glass writeToRc type null");
                return;
            }
            deviceType = DJIUpDeviceType.is1860Rc(type) ? DeviceType.OSD : DeviceType.DM368_G;
        }
        DJIUpConstants.LOGD("getGlassNewestVersion", "glass writeToRc deviceType=" + deviceType);
        this.setNewestVersions.setRecvType(deviceType);
        DataCommonSetNewestVersions.ProductVersionObject object = new DataCommonSetNewestVersions.ProductVersionObject();
        object.product_id = this.product_id;
        object.newestVersion = product_version;
        DJIUpConstants.LOGD("getGlassNewestVersion", "glass writeToRc byte2hex=" + BytesUtil.byte2hex(object.getVersionBytes()));
        this.setNewestVersions.addProductVersion(object);
        this.setNewestVersions.start(new DJIDataCallBack() {
            /* class dji.dbox.upgrade.logics.DJIGetNewestGlassVersionLogic.AnonymousClass3 */

            public void onSuccess(Object model) {
                DJIUpConstants.LOGD("getGlassNewestVersion", "glass writeToRc -- onSuccess");
            }

            public void onFailure(Ccode ccode) {
                DJIUpConstants.LOGD("getGlassNewestVersion", "glass writeToRc -- onFailure " + ccode);
            }
        });
    }
}
