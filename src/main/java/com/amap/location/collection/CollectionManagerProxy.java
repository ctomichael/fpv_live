package com.amap.location.collection;

import android.content.Context;
import android.support.annotation.NonNull;
import com.amap.location.common.network.IHttpClient;
import com.amap.openapi.at;
import com.amap.openapi.ay;

public class CollectionManagerProxy {
    private static final String TAG = "CollectionManagerProxy";
    private ay mCloudWrapper;
    private a mCollectionManager;
    private CollectionConfig mConfig;
    private Context mContext;
    private IHttpClient mHttpClient;
    private boolean mIsInit;

    public static String getVersion() {
        return "v74";
    }

    /* access modifiers changed from: private */
    public synchronized void onConfigChanged() {
        if (this.mIsInit) {
            this.mCollectionManager.b();
            this.mCollectionManager = new a(this.mContext, this.mConfig, this.mHttpClient);
            this.mCollectionManager.a();
        }
    }

    public synchronized void destroy() {
        if (this.mIsInit) {
            this.mCloudWrapper.b();
            this.mCollectionManager.b();
            this.mIsInit = false;
        }
    }

    public synchronized at getNetworkLocatorUploadData() {
        at atVar;
        atVar = null;
        if (this.mIsInit) {
            atVar = this.mCollectionManager.c();
        }
        return atVar;
    }

    public synchronized void init(@NonNull Context context, @NonNull CollectionConfig collectionConfig, @NonNull IHttpClient iHttpClient) {
        if (!this.mIsInit) {
            this.mIsInit = true;
            this.mContext = context.getApplicationContext();
            this.mConfig = collectionConfig;
            this.mHttpClient = iHttpClient;
            this.mCloudWrapper = new ay(this.mContext, this.mConfig, this.mHttpClient, new ay.a() {
                /* class com.amap.location.collection.CollectionManagerProxy.AnonymousClass1 */

                public void a() {
                    CollectionManagerProxy.this.onConfigChanged();
                }
            });
            this.mCloudWrapper.a();
            this.mCollectionManager = new a(this.mContext, this.mConfig, iHttpClient);
            this.mCollectionManager.a();
        }
    }

    public synchronized void onNetworkLocatorUploadFinish(boolean z, at atVar) {
        if (this.mIsInit) {
            this.mCollectionManager.a(z, atVar);
        }
    }
}
