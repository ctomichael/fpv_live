package dji.internal.mock.abstractions;

import dji.common.product.Model;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.mock.MockDJISDKCache;
import dji.midware.component.DJIComponentManager;
import dji.sdksharedlib.hardware.abstractions.product.DJIProductAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockProductAbstraction extends DJIProductAbstraction {
    /* access modifiers changed from: private */
    public DJIComponentManager.PlatformType fakePlatformType = DJIComponentManager.PlatformType.FoldingDrone;
    /* access modifiers changed from: private */
    public Model fakeProductModel = Model.MAVIC_PRO;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    private boolean shouldFakeDisconnection = true;

    public MockProductAbstraction() {
        if (this.shouldFakeDisconnection) {
            generateFakeConnectivityData();
        } else {
            updateProduct();
        }
    }

    private void generateFakeConnectivityData() {
        Observable.timer(1, TimeUnit.SECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockProductAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                boolean unused = MockProductAbstraction.this.isConnected = !MockProductAbstraction.this.isConnected;
                MockProductAbstraction.this.notifyValueChangeForKeyPathFromSetter(Boolean.valueOf(MockProductAbstraction.this.isConnected), DJISDKCacheKeys.CONNECTION);
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    /* access modifiers changed from: protected */
    public void updateProduct() {
        Observable.timer(5, TimeUnit.SECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockProductAbstraction.AnonymousClass2 */

            public Observable<Boolean> call(Long aLong) {
                MockProductAbstraction.this.notifyValueChangeForKeyPathFromSetter(true, DJISDKCacheKeys.CONNECTION);
                MockProductAbstraction.this.notifyValueChangeForKeyPath(MockProductAbstraction.this.fakeProductModel, MockProductAbstraction.this.convertKeyToPath(ProductKeys.MODEL_NAME));
                MockProductAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockProductAbstraction.this.fakeProductModel == Model.OSMO || MockProductAbstraction.this.fakeProductModel == Model.OSMO_MOBILE || MockProductAbstraction.this.fakeProductModel == Model.OSMO_PRO || MockProductAbstraction.this.fakeProductModel == Model.OSMO_RAW), MockProductAbstraction.this.convertKeyToPath(ProductKeys.IS_OSMO));
                MockDJISDKCache.getInstance().notifyFakeComponentChanged();
                DJIComponentManager.getInstance().setPlatformType(MockProductAbstraction.this.fakePlatformType);
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }
}
