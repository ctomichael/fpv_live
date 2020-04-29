package dji.internal.diagnostics.handler;

import dji.diagnostics.model.DJIDiagnostics;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.diagnostics.model.DJIDiagnosticsType;
import dji.internal.diagnostics.DiagnosticsBaseHandler;
import dji.internal.diagnostics.DiagnosticsHandlerUpdateObserver;
import dji.internal.diagnostics.handler.util.DiagnosticsIfModel;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.util.HashSet;
import java.util.Set;

public class ProductDiagnosticsHandler extends DiagnosticsBaseHandler implements DJIParamAccessListener {
    private final DJIDiagnosticsType TYPE = DJIDiagnosticsType.PRODUCT;
    private DiagnosticsIfModel<Boolean> mDiagnosticsConnection1Model;
    private DiagnosticsIfModel<Boolean> mDiagnosticsConnection2Model;
    private DJISDKCacheKey mProductConnectionKey;
    private DJISDKCacheKey mRcConnectionKey;

    public ProductDiagnosticsHandler(DiagnosticsHandlerUpdateObserver observer) {
        super(observer);
        init();
    }

    private void init() {
        this.mProductConnectionKey = KeyHelper.getProductKey(DJISDKCacheKeys.CONNECTION);
        this.mRcConnectionKey = KeyHelper.getRemoteControllerKey(DJISDKCacheKeys.CONNECTION);
        this.mDiagnosticsConnection1Model = new DiagnosticsIfModel<>((int) DJIDiagnosticsError.Product.NOT_CONNECTED_AIRCRAFT, new ProductDiagnosticsHandler$$Lambda$0(this));
        this.mDiagnosticsConnection2Model = new DiagnosticsIfModel<>((int) DJIDiagnosticsError.Product.DISCONNECTED, new ProductDiagnosticsHandler$$Lambda$1(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$init$0$ProductDiagnosticsHandler(Boolean aBoolean) {
        return !((Boolean) CacheHelper.getValue(this.mProductConnectionKey, false)).booleanValue() && ((Boolean) CacheHelper.getValue(this.mRcConnectionKey, false)).booleanValue();
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ boolean lambda$init$1$ProductDiagnosticsHandler(Boolean aBoolean) {
        return !((Boolean) CacheHelper.getValue(this.mProductConnectionKey, false)).booleanValue() && !((Boolean) CacheHelper.getValue(this.mRcConnectionKey, false)).booleanValue();
    }

    /* access modifiers changed from: protected */
    public void onRegister() {
        CacheHelper.addListener(this, this.mProductConnectionKey, this.mRcConnectionKey);
        onConnectChange();
    }

    /* access modifiers changed from: protected */
    public void onUnregister() {
        CacheHelper.removeListener(this);
    }

    public Set<DJIDiagnostics> getDiagnosisList() {
        Set<DJIDiagnostics> diagnosisList = new HashSet<>();
        DiagnosticsIfModel<Boolean> diagnosticsIfModel = this.mDiagnosticsConnection1Model;
        DJIDiagnosticsType dJIDiagnosticsType = this.TYPE;
        diagnosisList.getClass();
        diagnosticsIfModel.doIfError(dJIDiagnosticsType, ProductDiagnosticsHandler$$Lambda$2.get$Lambda(diagnosisList));
        DiagnosticsIfModel<Boolean> diagnosticsIfModel2 = this.mDiagnosticsConnection2Model;
        DJIDiagnosticsType dJIDiagnosticsType2 = this.TYPE;
        diagnosisList.getClass();
        diagnosticsIfModel2.doIfError(dJIDiagnosticsType2, ProductDiagnosticsHandler$$Lambda$3.get$Lambda(diagnosisList));
        return diagnosisList;
    }

    public void reset(int componentIndex) {
    }

    public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
        postToDiagnosticBackgroudThread(new ProductDiagnosticsHandler$$Lambda$4(this, key));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$onValueChange$2$ProductDiagnosticsHandler(DJISDKCacheKey key) {
        if (key.equals(this.mProductConnectionKey) || key.equals(this.mRcConnectionKey)) {
            onConnectChange();
        }
    }

    private void onConnectChange() {
        if (this.mDiagnosticsConnection1Model.statusApply(true) || this.mDiagnosticsConnection2Model.statusApply(true)) {
            notifyChange();
        }
    }
}
