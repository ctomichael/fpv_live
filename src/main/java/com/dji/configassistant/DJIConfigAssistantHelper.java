package com.dji.configassistant;

import android.os.Environment;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Properties;

@EXClassNullAway
public class DJIConfigAssistantHelper {
    private static String configFilePath = (dirPath + File.separator + "config.properties");
    private static String dirPath = (Environment.getExternalStorageDirectory() + File.separator + DJIUsbAccessoryReceiver.myFacturer + File.separator + "assistant");
    private boolean isDJIConfigAssistantExist;
    private Properties properties;

    public static DJIConfigAssistantHelper get() {
        return SingleHolder.instance;
    }

    private static class SingleHolder {
        /* access modifiers changed from: private */
        public static DJIConfigAssistantHelper instance = new DJIConfigAssistantHelper();

        private SingleHolder() {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0066 A[SYNTHETIC, Splitter:B:29:0x0066] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x006c A[SYNTHETIC, Splitter:B:33:0x006c] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0074 A[SYNTHETIC, Splitter:B:38:0x0074] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007a A[SYNTHETIC, Splitter:B:42:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:63:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private DJIConfigAssistantHelper() {
        /*
            r9 = this;
            r9.<init>()
            r7 = 0
            r9.isDJIConfigAssistantExist = r7
            java.lang.String r7 = "com.dji.configassistant"
            boolean r7 = dji.midware.aoabridge.Utils.isAppInstalled(r7)
            r9.isDJIConfigAssistantExist = r7
            boolean r7 = r9.isDJIConfigAssistantExist
            if (r7 == 0) goto L_0x0059
            java.io.File r0 = new java.io.File
            java.lang.String r7 = com.dji.configassistant.DJIConfigAssistantHelper.dirPath
            r0.<init>(r7)
            boolean r7 = r0.exists()
            if (r7 != 0) goto L_0x0023
            r0.mkdirs()
        L_0x0023:
            r5 = 0
            r3 = 0
            java.io.File r2 = new java.io.File     // Catch:{ Exception -> 0x005d }
            java.lang.String r7 = com.dji.configassistant.DJIConfigAssistantHelper.configFilePath     // Catch:{ Exception -> 0x005d }
            r2.<init>(r7)     // Catch:{ Exception -> 0x005d }
            boolean r7 = r2.exists()     // Catch:{ Exception -> 0x005d }
            if (r7 != 0) goto L_0x0035
            r2.createNewFile()     // Catch:{ Exception -> 0x005d }
        L_0x0035:
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch:{ Exception -> 0x005d }
            java.lang.String r7 = com.dji.configassistant.DJIConfigAssistantHelper.configFilePath     // Catch:{ Exception -> 0x005d }
            r4.<init>(r7)     // Catch:{ Exception -> 0x005d }
            java.io.BufferedInputStream r6 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x0090, all -> 0x0089 }
            r6.<init>(r4)     // Catch:{ Exception -> 0x0090, all -> 0x0089 }
            java.util.Properties r7 = new java.util.Properties     // Catch:{ Exception -> 0x0093, all -> 0x008c }
            r7.<init>()     // Catch:{ Exception -> 0x0093, all -> 0x008c }
            r9.properties = r7     // Catch:{ Exception -> 0x0093, all -> 0x008c }
            java.util.Properties r7 = r9.properties     // Catch:{ Exception -> 0x0093, all -> 0x008c }
            r7.load(r6)     // Catch:{ Exception -> 0x0093, all -> 0x008c }
            if (r6 == 0) goto L_0x0097
            r6.close()     // Catch:{ Exception -> 0x005a }
            r5 = 0
        L_0x0053:
            if (r4 == 0) goto L_0x0059
            r4.close()     // Catch:{ Exception -> 0x007f }
            r3 = 0
        L_0x0059:
            return
        L_0x005a:
            r7 = move-exception
            r5 = r6
            goto L_0x0053
        L_0x005d:
            r1 = move-exception
        L_0x005e:
            r1.printStackTrace()     // Catch:{ all -> 0x0071 }
            r7 = 0
            r9.properties = r7     // Catch:{ all -> 0x0071 }
            if (r5 == 0) goto L_0x006a
            r5.close()     // Catch:{ Exception -> 0x0081 }
            r5 = 0
        L_0x006a:
            if (r3 == 0) goto L_0x0059
            r3.close()     // Catch:{ Exception -> 0x0083 }
            r3 = 0
            goto L_0x0059
        L_0x0071:
            r7 = move-exception
        L_0x0072:
            if (r5 == 0) goto L_0x0078
            r5.close()     // Catch:{ Exception -> 0x0085 }
            r5 = 0
        L_0x0078:
            if (r3 == 0) goto L_0x007e
            r3.close()     // Catch:{ Exception -> 0x0087 }
            r3 = 0
        L_0x007e:
            throw r7
        L_0x007f:
            r7 = move-exception
            goto L_0x0059
        L_0x0081:
            r7 = move-exception
            goto L_0x006a
        L_0x0083:
            r7 = move-exception
            goto L_0x0059
        L_0x0085:
            r8 = move-exception
            goto L_0x0078
        L_0x0087:
            r8 = move-exception
            goto L_0x007e
        L_0x0089:
            r7 = move-exception
            r3 = r4
            goto L_0x0072
        L_0x008c:
            r7 = move-exception
            r3 = r4
            r5 = r6
            goto L_0x0072
        L_0x0090:
            r1 = move-exception
            r3 = r4
            goto L_0x005e
        L_0x0093:
            r1 = move-exception
            r3 = r4
            r5 = r6
            goto L_0x005e
        L_0x0097:
            r5 = r6
            goto L_0x0053
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.configassistant.DJIConfigAssistantHelper.<init>():void");
    }

    public void inject(Class clazz) {
        inject(clazz, null);
    }

    public void inject(Object o) {
        if (o != null) {
            inject(o.getClass(), o);
        }
    }

    public void inject(Class clazz, Object o) {
        if (this.isDJIConfigAssistantExist && clazz != null) {
            try {
                Field[] fArr = clazz.getDeclaredFields();
                for (Field field : fArr) {
                    DJIInnerProperty property = (DJIInnerProperty) field.getAnnotation(DJIInnerProperty.class);
                    if (!(property == null || property.value() == null || property.value().isEmpty())) {
                        String key = property.value();
                        if (containKey(key)) {
                            field.setAccessible(true);
                            Class fieldClass = field.getType();
                            if (fieldClass == Boolean.class || fieldClass == Boolean.TYPE) {
                                field.setBoolean(o, getBoolean(key).booleanValue());
                            } else if (fieldClass == Integer.class || fieldClass == Integer.TYPE) {
                                field.setInt(o, getInteger(key).intValue());
                            } else if (fieldClass == String.class) {
                                field.set(o, getString(key));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean containKey(String key) {
        if (this.properties == null) {
            return false;
        }
        return this.properties.containsKey(key);
    }

    private Boolean getBoolean(String key) {
        String value = this.properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            try {
                return Boolean.valueOf(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private Integer getInteger(String key) {
        String value = this.properties.getProperty(key);
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.valueOf(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private String getString(String key) {
        return this.properties.getProperty(key);
    }
}
