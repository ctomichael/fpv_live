package dji.pilot.publics.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.afinal.annotation.view.ViewInject;
import java.lang.reflect.Field;

@EXClassNullAway
public class DJIViewCompat {
    private static final int MAX_BITMAP_SIZE = 52428800;
    private static final int MODIFIERS_IGNORE = 1032;

    public static void visibleView(View view, int visibility) {
        if (visibility != view.getVisibility()) {
            view.setVisibility(visibility);
        }
    }

    public static void injectObject(Object object, View view) {
        ViewInject viewInject;
        int viewId;
        if (view != null && object != null) {
            Class<?> clazz = object.getClass();
            while (clazz != null) {
                String name = clazz.getName();
                if (!name.startsWith("android.") && !name.startsWith("java.")) {
                    Field[] fields = clazz.getDeclaredFields();
                    if (fields != null && fields.length > 0) {
                        for (Field field : fields) {
                            try {
                                if ((field.getModifiers() & 1032) == 0 && View.class.isAssignableFrom(field.getType())) {
                                    field.setAccessible(true);
                                    if (!(field.get(object) != null || (viewInject = (ViewInject) field.getAnnotation(ViewInject.class)) == null || (viewId = viewInject.id()) == 0)) {
                                        field.set(object, view.findViewById(viewId));
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                    clazz = clazz.getSuperclass();
                } else {
                    return;
                }
            }
        }
    }

    public static void injectMine(View view) {
        ViewInject viewInject;
        int viewId;
        if (view != null) {
            Class<?> clazz = view.getClass();
            while (clazz != null && !clazz.getName().startsWith("android.")) {
                Field[] fields = clazz.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    for (Field field : fields) {
                        try {
                            if ((field.getModifiers() & 1032) == 0 && View.class.isAssignableFrom(field.getType())) {
                                field.setAccessible(true);
                                if (!(field.get(view) != null || (viewInject = (ViewInject) field.getAnnotation(ViewInject.class)) == null || (viewId = viewInject.id()) == 0)) {
                                    field.set(view, view.findViewById(viewId));
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0066  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0077 A[SYNTHETIC, Splitter:B:19:0x0077] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0084 A[SYNTHETIC, Splitter:B:25:0x0084] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean screenCapFpv(android.view.View r13, android.graphics.Bitmap r14, java.lang.String r15) {
        /*
            r12 = 0
            r10 = 1
            r11 = 0
            r6 = 0
            r13.setDrawingCacheEnabled(r10)
            r13.setDrawingCacheBackgroundColor(r10)
            r13.setDrawingCacheQuality(r11)
            android.graphics.Bitmap r0 = r13.getDrawingCache(r11)
            if (r14 == 0) goto L_0x0064
            if (r0 == 0) goto L_0x0064
            int r8 = r0.getWidth()
            int r4 = r0.getHeight()
            android.graphics.Bitmap$Config r9 = android.graphics.Bitmap.Config.RGB_565
            android.graphics.Bitmap r7 = android.graphics.Bitmap.createBitmap(r8, r4, r9)
            android.graphics.Canvas r1 = new android.graphics.Canvas
            r1.<init>(r7)
            android.graphics.Paint r5 = new android.graphics.Paint
            r5.<init>()
            r5.setAntiAlias(r10)
            int r9 = r14.getWidth()
            int r9 = r8 - r9
            int r9 = r9 / 2
            float r9 = (float) r9
            int r10 = r14.getHeight()
            int r10 = r4 - r10
            int r10 = r10 / 2
            float r10 = (float) r10
            r1.drawBitmap(r14, r9, r10, r5)
            r1.drawBitmap(r0, r12, r12, r5)
            r2 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0070, all -> 0x007d }
            java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x0070, all -> 0x007d }
            r9.<init>(r15)     // Catch:{ Exception -> 0x0070, all -> 0x007d }
            r3.<init>(r9)     // Catch:{ Exception -> 0x0070, all -> 0x007d }
            android.graphics.Bitmap$CompressFormat r9 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x008f, all -> 0x008c }
            r10 = 100
            boolean r6 = r7.compress(r9, r10, r3)     // Catch:{ Exception -> 0x008f, all -> 0x008c }
            r7.recycle()
            r7 = 0
            if (r3 == 0) goto L_0x0064
            r3.close()     // Catch:{ Exception -> 0x0088 }
        L_0x0064:
            if (r14 == 0) goto L_0x0069
            r14.recycle()
        L_0x0069:
            r13.destroyDrawingCache()
            r13.setDrawingCacheEnabled(r11)
            return r6
        L_0x0070:
            r9 = move-exception
        L_0x0071:
            r7.recycle()
            r7 = 0
            if (r2 == 0) goto L_0x0064
            r2.close()     // Catch:{ Exception -> 0x007b }
            goto L_0x0064
        L_0x007b:
            r9 = move-exception
            goto L_0x0064
        L_0x007d:
            r9 = move-exception
        L_0x007e:
            r7.recycle()
            r7 = 0
            if (r2 == 0) goto L_0x0087
            r2.close()     // Catch:{ Exception -> 0x008a }
        L_0x0087:
            throw r9
        L_0x0088:
            r9 = move-exception
            goto L_0x0064
        L_0x008a:
            r10 = move-exception
            goto L_0x0087
        L_0x008c:
            r9 = move-exception
            r2 = r3
            goto L_0x007e
        L_0x008f:
            r9 = move-exception
            r2 = r3
            goto L_0x0071
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.util.DJIViewCompat.screenCapFpv(android.view.View, android.graphics.Bitmap, java.lang.String):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x001f A[SYNTHETIC, Splitter:B:13:0x001f] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0028 A[SYNTHETIC, Splitter:B:18:0x0028] */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void bmpToFile(android.graphics.Bitmap r4, java.lang.String r5) {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x001c, all -> 0x0025 }
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x001c, all -> 0x0025 }
            r2.<init>(r5)     // Catch:{ Exception -> 0x001c, all -> 0x0025 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x001c, all -> 0x0025 }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x0031, all -> 0x002e }
            r3 = 100
            r4.compress(r2, r3, r1)     // Catch:{ Exception -> 0x0031, all -> 0x002e }
            if (r1 == 0) goto L_0x0034
            r1.close()     // Catch:{ Exception -> 0x0019 }
            r0 = r1
        L_0x0018:
            return
        L_0x0019:
            r2 = move-exception
            r0 = r1
            goto L_0x0018
        L_0x001c:
            r2 = move-exception
        L_0x001d:
            if (r0 == 0) goto L_0x0018
            r0.close()     // Catch:{ Exception -> 0x0023 }
            goto L_0x0018
        L_0x0023:
            r2 = move-exception
            goto L_0x0018
        L_0x0025:
            r2 = move-exception
        L_0x0026:
            if (r0 == 0) goto L_0x002b
            r0.close()     // Catch:{ Exception -> 0x002c }
        L_0x002b:
            throw r2
        L_0x002c:
            r3 = move-exception
            goto L_0x002b
        L_0x002e:
            r2 = move-exception
            r0 = r1
            goto L_0x0026
        L_0x0031:
            r2 = move-exception
            r0 = r1
            goto L_0x001d
        L_0x0034:
            r0 = r1
            goto L_0x0018
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.util.DJIViewCompat.bmpToFile(android.graphics.Bitmap, java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x003e A[SYNTHETIC, Splitter:B:20:0x003e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean screenCapture(android.view.View r7, java.lang.String r8) {
        /*
            r6 = 0
            r3 = 0
            r4 = 1
            r7.setDrawingCacheEnabled(r4)
            r4 = 1048576(0x100000, float:1.469368E-39)
            r7.setDrawingCacheQuality(r4)
            android.graphics.Bitmap r0 = r7.getDrawingCache(r6)
            r1 = 0
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0032, all -> 0x003b }
            java.io.File r4 = new java.io.File     // Catch:{ Exception -> 0x0032, all -> 0x003b }
            r4.<init>(r8)     // Catch:{ Exception -> 0x0032, all -> 0x003b }
            r2.<init>(r4)     // Catch:{ Exception -> 0x0032, all -> 0x003b }
            android.graphics.Bitmap$CompressFormat r4 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x0047, all -> 0x0044 }
            r5 = 100
            boolean r3 = r0.compress(r4, r5, r2)     // Catch:{ Exception -> 0x0047, all -> 0x0044 }
            if (r2 == 0) goto L_0x004a
            r2.close()     // Catch:{ Exception -> 0x002f }
            r1 = r2
        L_0x0028:
            r7.destroyDrawingCache()
            r7.setDrawingCacheEnabled(r6)
            return r3
        L_0x002f:
            r4 = move-exception
            r1 = r2
            goto L_0x0028
        L_0x0032:
            r4 = move-exception
        L_0x0033:
            if (r1 == 0) goto L_0x0028
            r1.close()     // Catch:{ Exception -> 0x0039 }
            goto L_0x0028
        L_0x0039:
            r4 = move-exception
            goto L_0x0028
        L_0x003b:
            r4 = move-exception
        L_0x003c:
            if (r1 == 0) goto L_0x0041
            r1.close()     // Catch:{ Exception -> 0x0042 }
        L_0x0041:
            throw r4
        L_0x0042:
            r5 = move-exception
            goto L_0x0041
        L_0x0044:
            r4 = move-exception
            r1 = r2
            goto L_0x003c
        L_0x0047:
            r4 = move-exception
            r1 = r2
            goto L_0x0033
        L_0x004a:
            r1 = r2
            goto L_0x0028
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.util.DJIViewCompat.screenCapture(android.view.View, java.lang.String):boolean");
    }

    public static boolean querySoundEffectsEnabled(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "sound_effects_enabled") != 0;
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
    }

    @Nullable
    public static Drawable decodeDrawable(@NonNull Context context, int drawableResId) {
        return decodeDrawable(context, drawableResId, MAX_BITMAP_SIZE);
    }

    @Nullable
    public static Drawable decodeDrawable(@NonNull Context context, int drawableResId, int maxSize) {
        Resources res = context.getResources();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeResource(res, drawableResId, options);
        float scale = 1.0f;
        if (options.inDensity > 0) {
            scale = (((float) options.inTargetDensity) * 1.0f) / ((float) options.inDensity);
        }
        int inSampleSize = 1;
        while (((long) ((int) (((float) (options.outWidth * options.outHeight * 4)) * scale * scale))) > ((long) (maxSize * inSampleSize * inSampleSize))) {
            inSampleSize++;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeResource(res, drawableResId, options);
        if (bmp == null) {
            return null;
        }
        return new BitmapDrawable(res, bmp);
    }

    public static int getChildIndex(ViewGroup viewGroup, int viewId, int defaultValue) {
        if (viewGroup == null || viewId < 0) {
            return defaultValue;
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (viewGroup.getChildAt(i).getId() == viewId) {
                return i;
            }
        }
        return defaultValue;
    }

    private DJIViewCompat() {
    }
}
