package com.dji.component.fpv.base;

import android.support.annotation.VisibleForTesting;
import com.dji.component.fpv.base.providers.FpvScopeProvider;
import com.dji.rx.sharedlib.MockableCacheHelper;
import com.dji.rx.sharedlib.SharedLibPushObservable;
import java.lang.reflect.Field;
import java.util.Map;

public abstract class AbstractViewModel {
    protected BulletinBoardProvider mBridge;
    protected MockableCacheHelper mCacheHelper = new MockableCacheHelper();
    private Map<String, ? super FpvScopeProvider> mFpvScopeProviderMap;

    public abstract void onCreateView();

    public abstract void onDestroyView();

    public AbstractViewModel(BulletinBoardProvider bridge) {
        this.mBridge = bridge;
    }

    public AbstractViewModel(BulletinBoardProvider bridge, Map<String, ? super FpvScopeProvider> fpvScopeProviderMap) {
        this.mBridge = bridge;
        this.mFpvScopeProviderMap = fpvScopeProviderMap;
    }

    @VisibleForTesting(otherwise = 5)
    public void injectMockDataModel() throws IllegalAccessException {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == SharedLibPushObservable.class) {
                field.setAccessible(true);
                ((SharedLibPushObservable) field.get(this)).setCacheHelper(this.mCacheHelper);
            }
        }
    }

    /* access modifiers changed from: protected */
    public <T> T getFpvScopeProvider(Class<T> clazz) {
        return this.mFpvScopeProviderMap.get(clazz.getCanonicalName());
    }

    /* access modifiers changed from: protected */
    public BulletinBoard getBulletinBoard() {
        return this.mBridge.getBulletinBoard();
    }
}
