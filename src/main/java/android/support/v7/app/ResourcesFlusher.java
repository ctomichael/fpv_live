package android.support.v7.app;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.Field;

class ResourcesFlusher {
    private static final String TAG = "ResourcesFlusher";
    private static Field sDrawableCacheField;
    private static boolean sDrawableCacheFieldFetched;
    private static Field sResourcesImplField;
    private static boolean sResourcesImplFieldFetched;
    private static Class sThemedResourceCacheClazz;
    private static boolean sThemedResourceCacheClazzFetched;
    private static Field sThemedResourceCache_mUnthemedEntriesField;
    private static boolean sThemedResourceCache_mUnthemedEntriesFieldFetched;

    static void flush(@NonNull Resources resources) {
        if (Build.VERSION.SDK_INT < 28) {
            if (Build.VERSION.SDK_INT >= 24) {
                flushNougats(resources);
            } else if (Build.VERSION.SDK_INT >= 23) {
                flushMarshmallows(resources);
            } else if (Build.VERSION.SDK_INT >= 21) {
                flushLollipops(resources);
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.util.Map} */
    /* JADX WARNING: Multi-variable type inference failed */
    @android.support.annotation.RequiresApi(21)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void flushLollipops(@android.support.annotation.NonNull android.content.res.Resources r6) {
        /*
            r5 = 1
            boolean r3 = android.support.v7.app.ResourcesFlusher.sDrawableCacheFieldFetched
            if (r3 != 0) goto L_0x0018
            java.lang.Class<android.content.res.Resources> r3 = android.content.res.Resources.class
            java.lang.String r4 = "mDrawableCache"
            java.lang.reflect.Field r3 = r3.getDeclaredField(r4)     // Catch:{ NoSuchFieldException -> 0x002d }
            android.support.v7.app.ResourcesFlusher.sDrawableCacheField = r3     // Catch:{ NoSuchFieldException -> 0x002d }
            java.lang.reflect.Field r3 = android.support.v7.app.ResourcesFlusher.sDrawableCacheField     // Catch:{ NoSuchFieldException -> 0x002d }
            r4 = 1
            r3.setAccessible(r4)     // Catch:{ NoSuchFieldException -> 0x002d }
        L_0x0016:
            android.support.v7.app.ResourcesFlusher.sDrawableCacheFieldFetched = r5
        L_0x0018:
            java.lang.reflect.Field r3 = android.support.v7.app.ResourcesFlusher.sDrawableCacheField
            if (r3 == 0) goto L_0x002c
            r1 = 0
            java.lang.reflect.Field r3 = android.support.v7.app.ResourcesFlusher.sDrawableCacheField     // Catch:{ IllegalAccessException -> 0x0038 }
            java.lang.Object r3 = r3.get(r6)     // Catch:{ IllegalAccessException -> 0x0038 }
            r0 = r3
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ IllegalAccessException -> 0x0038 }
            r1 = r0
        L_0x0027:
            if (r1 == 0) goto L_0x002c
            r1.clear()
        L_0x002c:
            return
        L_0x002d:
            r2 = move-exception
            java.lang.String r3 = "ResourcesFlusher"
            java.lang.String r4 = "Could not retrieve Resources#mDrawableCache field"
            android.util.Log.e(r3, r4, r2)
            goto L_0x0016
        L_0x0038:
            r2 = move-exception
            java.lang.String r3 = "ResourcesFlusher"
            java.lang.String r4 = "Could not retrieve value from Resources#mDrawableCache"
            android.util.Log.e(r3, r4, r2)
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.ResourcesFlusher.flushLollipops(android.content.res.Resources):void");
    }

    @RequiresApi(23)
    private static void flushMarshmallows(@NonNull Resources resources) {
        if (!sDrawableCacheFieldFetched) {
            try {
                sDrawableCacheField = Resources.class.getDeclaredField("mDrawableCache");
                sDrawableCacheField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Could not retrieve Resources#mDrawableCache field", e);
            }
            sDrawableCacheFieldFetched = true;
        }
        Object drawableCache = null;
        if (sDrawableCacheField != null) {
            try {
                drawableCache = sDrawableCacheField.get(resources);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "Could not retrieve value from Resources#mDrawableCache", e2);
            }
        }
        if (drawableCache != null) {
            flushThemedResourcesCache(drawableCache);
        }
    }

    @RequiresApi(24)
    private static void flushNougats(@NonNull Resources resources) {
        if (!sResourcesImplFieldFetched) {
            try {
                sResourcesImplField = Resources.class.getDeclaredField("mResourcesImpl");
                sResourcesImplField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                Log.e(TAG, "Could not retrieve Resources#mResourcesImpl field", e);
            }
            sResourcesImplFieldFetched = true;
        }
        if (sResourcesImplField != null) {
            Object resourcesImpl = null;
            try {
                resourcesImpl = sResourcesImplField.get(resources);
            } catch (IllegalAccessException e2) {
                Log.e(TAG, "Could not retrieve value from Resources#mResourcesImpl", e2);
            }
            if (resourcesImpl != null) {
                if (!sDrawableCacheFieldFetched) {
                    try {
                        sDrawableCacheField = resourcesImpl.getClass().getDeclaredField("mDrawableCache");
                        sDrawableCacheField.setAccessible(true);
                    } catch (NoSuchFieldException e3) {
                        Log.e(TAG, "Could not retrieve ResourcesImpl#mDrawableCache field", e3);
                    }
                    sDrawableCacheFieldFetched = true;
                }
                Object drawableCache = null;
                if (sDrawableCacheField != null) {
                    try {
                        drawableCache = sDrawableCacheField.get(resourcesImpl);
                    } catch (IllegalAccessException e4) {
                        Log.e(TAG, "Could not retrieve value from ResourcesImpl#mDrawableCache", e4);
                    }
                }
                if (drawableCache != null) {
                    flushThemedResourcesCache(drawableCache);
                }
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.util.LongSparseArray} */
    /* JADX WARNING: Multi-variable type inference failed */
    @android.support.annotation.RequiresApi(16)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void flushThemedResourcesCache(@android.support.annotation.NonNull java.lang.Object r7) {
        /*
            r6 = 1
            boolean r4 = android.support.v7.app.ResourcesFlusher.sThemedResourceCacheClazzFetched
            if (r4 != 0) goto L_0x0010
            java.lang.String r4 = "android.content.res.ThemedResourceCache"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ ClassNotFoundException -> 0x0015 }
            android.support.v7.app.ResourcesFlusher.sThemedResourceCacheClazz = r4     // Catch:{ ClassNotFoundException -> 0x0015 }
        L_0x000e:
            android.support.v7.app.ResourcesFlusher.sThemedResourceCacheClazzFetched = r6
        L_0x0010:
            java.lang.Class r4 = android.support.v7.app.ResourcesFlusher.sThemedResourceCacheClazz
            if (r4 != 0) goto L_0x0020
        L_0x0014:
            return
        L_0x0015:
            r1 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r5 = "Could not find ThemedResourceCache class"
            android.util.Log.e(r4, r5, r1)
            goto L_0x000e
        L_0x0020:
            boolean r4 = android.support.v7.app.ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched
            if (r4 != 0) goto L_0x0037
            java.lang.Class r4 = android.support.v7.app.ResourcesFlusher.sThemedResourceCacheClazz     // Catch:{ NoSuchFieldException -> 0x004c }
            java.lang.String r5 = "mUnthemedEntries"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ NoSuchFieldException -> 0x004c }
            android.support.v7.app.ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField = r4     // Catch:{ NoSuchFieldException -> 0x004c }
            java.lang.reflect.Field r4 = android.support.v7.app.ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField     // Catch:{ NoSuchFieldException -> 0x004c }
            r5 = 1
            r4.setAccessible(r5)     // Catch:{ NoSuchFieldException -> 0x004c }
        L_0x0035:
            android.support.v7.app.ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesFieldFetched = r6
        L_0x0037:
            java.lang.reflect.Field r4 = android.support.v7.app.ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField
            if (r4 == 0) goto L_0x0014
            r3 = 0
            java.lang.reflect.Field r4 = android.support.v7.app.ResourcesFlusher.sThemedResourceCache_mUnthemedEntriesField     // Catch:{ IllegalAccessException -> 0x0057 }
            java.lang.Object r4 = r4.get(r7)     // Catch:{ IllegalAccessException -> 0x0057 }
            r0 = r4
            android.util.LongSparseArray r0 = (android.util.LongSparseArray) r0     // Catch:{ IllegalAccessException -> 0x0057 }
            r3 = r0
        L_0x0046:
            if (r3 == 0) goto L_0x0014
            r3.clear()
            goto L_0x0014
        L_0x004c:
            r2 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r5 = "Could not retrieve ThemedResourceCache#mUnthemedEntries field"
            android.util.Log.e(r4, r5, r2)
            goto L_0x0035
        L_0x0057:
            r1 = move-exception
            java.lang.String r4 = "ResourcesFlusher"
            java.lang.String r5 = "Could not retrieve value from ThemedResourceCache#mUnthemedEntries"
            android.util.Log.e(r4, r5, r1)
            goto L_0x0046
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.app.ResourcesFlusher.flushThemedResourcesCache(java.lang.Object):void");
    }

    private ResourcesFlusher() {
    }
}
