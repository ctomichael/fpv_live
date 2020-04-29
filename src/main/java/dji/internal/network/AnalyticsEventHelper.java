package dji.internal.network;

import android.content.ContentValues;
import android.database.Cursor;
import com.dji.frame.util.V_JsonUtil;
import dji.fieldAnnotation.EXClassNullAway;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@EXClassNullAway
public class AnalyticsEventHelper {
    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;
    private static final String TAG = AnalyticsEventHelper.class.getSimpleName();
    private static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat(TIME_PATTERN, Locale.US);

    static {
        sSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        sSimpleDateFormat.setLenient(false);
    }

    private AnalyticsEventHelper() {
        throw new AssertionError("No instances.");
    }

    public static String getCurrentTime() {
        return sSimpleDateFormat.format(new Date());
    }

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == 1;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static byte[] getByteArray(Cursor cursor, String columnName) {
        return cursor.getBlob(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

    public static ContentValues serializeData(DJIAnalyticsEvent event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DJIAnalyticsEvent.EVENT_DATA, V_JsonUtil.toJson(event));
        return contentValues;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v11, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: dji.internal.network.DJIAnalyticsEvent} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v16, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: dji.internal.network.DJIAnalyticsEvent} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a5 A[SYNTHETIC, Splitter:B:28:0x00a5] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00b1 A[SYNTHETIC, Splitter:B:35:0x00b1] */
    /* JADX WARNING: Removed duplicated region for block: B:48:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static dji.internal.network.DJIAnalyticsEvent deserializeEvent(android.database.Cursor r12) {
        /*
            r11 = 0
            r4 = 0
            java.lang.String r8 = "event_data"
            java.lang.String r5 = getString(r12, r8)     // Catch:{ Exception -> 0x0047 }
            boolean r8 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x0047 }
            if (r8 != 0) goto L_0x0019
            java.lang.Class<dji.internal.network.DJIAnalyticsEvent> r8 = dji.internal.network.DJIAnalyticsEvent.class
            java.lang.Object r8 = com.dji.frame.util.V_JsonUtil.toBean(r5, r8)     // Catch:{ Exception -> 0x0047 }
            r0 = r8
            dji.internal.network.DJIAnalyticsEvent r0 = (dji.internal.network.DJIAnalyticsEvent) r0     // Catch:{ Exception -> 0x0047 }
            r4 = r0
        L_0x0019:
            if (r4 != 0) goto L_0x003f
            java.lang.String r8 = "event_data"
            byte[] r3 = getByteArray(r12, r8)
            if (r3 == 0) goto L_0x003f
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream
            r1.<init>(r3)
            r6 = 0
            java.io.ObjectInputStream r7 = new java.io.ObjectInputStream     // Catch:{ Exception -> 0x0073 }
            r7.<init>(r1)     // Catch:{ Exception -> 0x0073 }
            java.lang.Object r8 = r7.readObject()     // Catch:{ Exception -> 0x00c2, all -> 0x00bf }
            r0 = r8
            dji.internal.network.DJIAnalyticsEvent r0 = (dji.internal.network.DJIAnalyticsEvent) r0     // Catch:{ Exception -> 0x00c2, all -> 0x00bf }
            r4 = r0
            r1.close()     // Catch:{ IOException -> 0x00b5 }
        L_0x003a:
            if (r7 == 0) goto L_0x003f
            r7.close()     // Catch:{ IOException -> 0x00b7 }
        L_0x003f:
            if (r4 != 0) goto L_0x0046
            dji.internal.network.DJIAnalyticsEvent r4 = new dji.internal.network.DJIAnalyticsEvent
            r4.<init>()
        L_0x0046:
            return r4
        L_0x0047:
            r2 = move-exception
            java.lang.String r8 = dji.internal.network.AnalyticsEventHelper.TAG
            java.lang.String r9 = dji.log.DJILog.exceptionToString(r2)
            java.lang.Object[] r10 = new java.lang.Object[r11]
            dji.log.DJILog.e(r8, r9, r10)
            java.lang.String r8 = dji.internal.network.AnalyticsEventHelper.TAG
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Exception deserializeEvent "
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r10 = r2.getMessage()
            java.lang.StringBuilder r9 = r9.append(r10)
            java.lang.String r9 = r9.toString()
            java.lang.Object[] r10 = new java.lang.Object[r11]
            dji.log.DJILog.e(r8, r9, r10)
            goto L_0x0019
        L_0x0073:
            r2 = move-exception
        L_0x0074:
            java.lang.String r8 = dji.internal.network.AnalyticsEventHelper.TAG     // Catch:{ all -> 0x00ab }
            java.lang.String r9 = dji.log.DJILog.exceptionToString(r2)     // Catch:{ all -> 0x00ab }
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ all -> 0x00ab }
            dji.log.DJILog.e(r8, r9, r10)     // Catch:{ all -> 0x00ab }
            java.lang.String r8 = dji.internal.network.AnalyticsEventHelper.TAG     // Catch:{ all -> 0x00ab }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ab }
            r9.<init>()     // Catch:{ all -> 0x00ab }
            java.lang.String r10 = "Exception deserializeEvent "
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ all -> 0x00ab }
            java.lang.String r10 = r2.getMessage()     // Catch:{ all -> 0x00ab }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ all -> 0x00ab }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x00ab }
            r10 = 0
            java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ all -> 0x00ab }
            dji.log.DJILog.e(r8, r9, r10)     // Catch:{ all -> 0x00ab }
            r1.close()     // Catch:{ IOException -> 0x00b9 }
        L_0x00a3:
            if (r6 == 0) goto L_0x003f
            r6.close()     // Catch:{ IOException -> 0x00a9 }
            goto L_0x003f
        L_0x00a9:
            r8 = move-exception
            goto L_0x003f
        L_0x00ab:
            r8 = move-exception
        L_0x00ac:
            r1.close()     // Catch:{ IOException -> 0x00bb }
        L_0x00af:
            if (r6 == 0) goto L_0x00b4
            r6.close()     // Catch:{ IOException -> 0x00bd }
        L_0x00b4:
            throw r8
        L_0x00b5:
            r8 = move-exception
            goto L_0x003a
        L_0x00b7:
            r8 = move-exception
            goto L_0x003f
        L_0x00b9:
            r8 = move-exception
            goto L_0x00a3
        L_0x00bb:
            r9 = move-exception
            goto L_0x00af
        L_0x00bd:
            r9 = move-exception
            goto L_0x00b4
        L_0x00bf:
            r8 = move-exception
            r6 = r7
            goto L_0x00ac
        L_0x00c2:
            r2 = move-exception
            r6 = r7
            goto L_0x0074
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.internal.network.AnalyticsEventHelper.deserializeEvent(android.database.Cursor):dji.internal.network.DJIAnalyticsEvent");
    }
}
