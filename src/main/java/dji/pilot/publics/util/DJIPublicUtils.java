package dji.pilot.publics.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIPublicUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static long formatVersion(String version) {
        String[] firms;
        long result = 0;
        if (version != null && !version.isEmpty()) {
            for (String str : version.split("\\.")) {
                result = (256 * result) + ((long) Integer.parseInt(str, 10));
            }
        }
        return result;
    }

    public static int findPosByValue(Object[] values, Object value, int defaultPos) {
        int length = values.length;
        for (int i = 0; i < length; i++) {
            if (value == values[i]) {
                return i;
            }
        }
        return defaultPos;
    }

    public static int getDimens(Context context, int dimenId) {
        return (int) context.getResources().getDimension(dimenId);
    }

    public static int dp2px(Context context, int dp) {
        return (int) ((((float) dp) * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int getScreenHeight(Context context) {
        Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }

    public static int getListViewHeight(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View view = mAdapter.getView(i, null, listView);
            if (view != null) {
                view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                totalHeight += view.getMeasuredHeight();
            }
        }
        return (listView.getDividerHeight() * (mAdapter.getCount() - 1)) + totalHeight;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException}
     arg types: [java.io.File, int]
     candidates:
      ClspMth{java.io.FileOutputStream.<init>(java.lang.String, boolean):void throws java.io.FileNotFoundException}
      ClspMth{java.io.FileOutputStream.<init>(java.io.File, boolean):void throws java.io.FileNotFoundException} */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0052 A[SYNTHETIC, Splitter:B:13:0x0052] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x005b A[SYNTHETIC, Splitter:B:18:0x005b] */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeSomeLog(int r10, byte[] r11, java.lang.String r12) {
        /*
            r6 = 0
            dji.pilot.usercenter.util.FileUtil.createFile(r12)     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            int r1 = r11.length     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            int r1 = r1 + 2
            byte[] r0 = new byte[r1]     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            int r2 = r11.length     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            r3 = 0
            r4 = 0
            r1 = r11
            long r8 = dji.midware.natives.FREncrypt.encryptFRData(r0, r1, r2, r3, r4)     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            r1.<init>(r12)     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            r2 = 1
            r7.<init>(r1, r2)     // Catch:{ Exception -> 0x004f, all -> 0x0058 }
            byte[] r1 = dji.pilot.publics.util.ILogInterface.LOG_DATA_MAGIC_NUMBER     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r2 = 0
            byte r1 = r1[r2]     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r7.write(r1)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            byte[] r1 = dji.pilot.publics.util.ILogInterface.LOG_DATA_MAGIC_NUMBER     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r2 = 1
            byte r1 = r1[r2]     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r7.write(r1)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            short r1 = (short) r10     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            byte[] r1 = dji.midware.util.BytesUtil.getBytes(r1)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r7.write(r1)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            int r1 = r0.length     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            byte[] r1 = dji.midware.util.BytesUtil.getBytes(r1)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r7.write(r1)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r1 = 0
            int r2 = r0.length     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r7.write(r0, r1, r2)     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            r7.flush()     // Catch:{ Exception -> 0x0064, all -> 0x0061 }
            if (r7 == 0) goto L_0x0067
            r7.close()     // Catch:{ Exception -> 0x004c }
            r6 = r7
        L_0x004b:
            return
        L_0x004c:
            r1 = move-exception
            r6 = r7
            goto L_0x004b
        L_0x004f:
            r1 = move-exception
        L_0x0050:
            if (r6 == 0) goto L_0x004b
            r6.close()     // Catch:{ Exception -> 0x0056 }
            goto L_0x004b
        L_0x0056:
            r1 = move-exception
            goto L_0x004b
        L_0x0058:
            r1 = move-exception
        L_0x0059:
            if (r6 == 0) goto L_0x005e
            r6.close()     // Catch:{ Exception -> 0x005f }
        L_0x005e:
            throw r1
        L_0x005f:
            r2 = move-exception
            goto L_0x005e
        L_0x0061:
            r1 = move-exception
            r6 = r7
            goto L_0x0059
        L_0x0064:
            r1 = move-exception
            r6 = r7
            goto L_0x0050
        L_0x0067:
            r6 = r7
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.util.DJIPublicUtils.writeSomeLog(int, byte[], java.lang.String):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x008d A[SYNTHETIC, Splitter:B:38:0x008d] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0096 A[SYNTHETIC, Splitter:B:43:0x0096] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.List<byte[]> readSomeLog(int r18, java.lang.String r19) {
        /*
            java.util.ArrayList r12 = new java.util.ArrayList
            r12.<init>()
            boolean r5 = dji.pilot.usercenter.util.FileUtil.exist(r19)
            if (r5 == 0) goto L_0x005b
            r9 = 0
            java.io.FileInputStream r10 = new java.io.FileInputStream     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            r0 = r19
            r5.<init>(r0)     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            r10.<init>(r5)     // Catch:{ Exception -> 0x008a, all -> 0x0093 }
            int r11 = r10.available()     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            byte[] r8 = new byte[r11]     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            r16 = 0
            r15 = 0
        L_0x0021:
            if (r15 >= r11) goto L_0x002e
            int r5 = r11 - r15
            int r16 = r10.read(r8, r15, r5)     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            if (r16 <= 0) goto L_0x002e
            int r15 = r15 + r16
            goto L_0x0021
        L_0x002e:
            r13 = 0
            r4 = 0
            r17 = 0
        L_0x0032:
            int r5 = r15 + -2
            if (r13 >= r5) goto L_0x0056
        L_0x0036:
            r14 = r13
        L_0x0037:
            int r5 = r15 + -2
            if (r14 >= r5) goto L_0x0051
            int r13 = r14 + 1
            byte r5 = r8[r14]     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            byte[] r6 = dji.pilot.publics.util.ILogInterface.LOG_DATA_MAGIC_NUMBER     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            r7 = 0
            byte r6 = r6[r7]     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            if (r5 != r6) goto L_0x0036
            int r14 = r13 + 1
            byte r5 = r8[r13]     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            byte[] r6 = dji.pilot.publics.util.ILogInterface.LOG_DATA_MAGIC_NUMBER     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            r7 = 1
            byte r6 = r6[r7]     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            if (r5 != r6) goto L_0x0037
        L_0x0051:
            r13 = r14
            int r5 = r15 + -2
            if (r13 < r5) goto L_0x005c
        L_0x0056:
            if (r10 == 0) goto L_0x005b
            r10.close()     // Catch:{ Exception -> 0x009a }
        L_0x005b:
            return r12
        L_0x005c:
            int r17 = dji.midware.util.BytesUtil.getUShort(r8, r13)     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            int r13 = r13 + 2
            long r6 = dji.midware.util.BytesUtil.getUInt(r8, r13)     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            int r4 = (int) r6     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            int r13 = r13 + 4
            if (r4 <= 0) goto L_0x0088
            int r5 = r13 + r4
            if (r5 > r15) goto L_0x0088
            r0 = r17
            r1 = r18
            if (r0 != r1) goto L_0x0088
            byte[] r3 = new byte[r4]     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            r5 = 0
            java.lang.System.arraycopy(r8, r13, r3, r5, r4)     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            int r5 = r4 + -2
            byte[] r2 = new byte[r5]     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            r5 = 0
            r6 = 0
            dji.midware.natives.FREncrypt.decryptFRData(r2, r3, r4, r5, r6)     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
            r12.add(r2)     // Catch:{ Exception -> 0x00a1, all -> 0x009e }
        L_0x0088:
            int r13 = r13 + r4
            goto L_0x0032
        L_0x008a:
            r5 = move-exception
        L_0x008b:
            if (r9 == 0) goto L_0x005b
            r9.close()     // Catch:{ Exception -> 0x0091 }
            goto L_0x005b
        L_0x0091:
            r5 = move-exception
            goto L_0x005b
        L_0x0093:
            r5 = move-exception
        L_0x0094:
            if (r9 == 0) goto L_0x0099
            r9.close()     // Catch:{ Exception -> 0x009c }
        L_0x0099:
            throw r5
        L_0x009a:
            r5 = move-exception
            goto L_0x005b
        L_0x009c:
            r6 = move-exception
            goto L_0x0099
        L_0x009e:
            r5 = move-exception
            r9 = r10
            goto L_0x0094
        L_0x00a1:
            r5 = move-exception
            r9 = r10
            goto L_0x008b
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.pilot.publics.util.DJIPublicUtils.readSomeLog(int, java.lang.String):java.util.List");
    }

    public static Activity scanForActivity(Context cont) {
        if (cont == null) {
            return null;
        }
        if (cont instanceof Activity) {
            return (Activity) cont;
        }
        if (cont instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) cont).getBaseContext());
        }
        return null;
    }

    public static boolean isActivityUseful(Context context) {
        Activity activity = scanForActivity(context);
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            return true;
        }
        return false;
    }

    public static long getNumber(byte[] bytes, int offset, int length) {
        if (bytes == null || bytes.length < offset + length) {
            return 0;
        }
        long result = 0;
        for (int i = (offset + length) - 1; i >= offset; i--) {
            result = (256 * result) + ((long) (bytes[i] & 255));
        }
        return result;
    }
}
