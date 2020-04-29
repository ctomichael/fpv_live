package dji.thirdparty.ciphersql.database;

import android.database.Cursor;
import android.os.MemoryFile;
import java.io.IOException;

public class SQLiteContentHelper {
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.os.ParcelFileDescriptor} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.content.res.AssetFileDescriptor getBlobColumnAsAssetFile(dji.thirdparty.ciphersql.database.SQLiteDatabase r13, java.lang.String r14, java.lang.String[] r15) throws java.io.FileNotFoundException {
        /*
            r3 = 0
            android.os.MemoryFile r11 = simpleQueryForBlobMemoryFile(r13, r14, r15)     // Catch:{ IOException -> 0x0010 }
            if (r11 != 0) goto L_0x001b
            java.io.FileNotFoundException r4 = new java.io.FileNotFoundException     // Catch:{ IOException -> 0x0010 }
            java.lang.String r5 = "No results."
            r4.<init>(r5)     // Catch:{ IOException -> 0x0010 }
            throw r4     // Catch:{ IOException -> 0x0010 }
        L_0x0010:
            r10 = move-exception
            java.io.FileNotFoundException r4 = new java.io.FileNotFoundException
            java.lang.String r5 = r10.toString()
            r4.<init>(r5)
            throw r4
        L_0x001b:
            java.lang.Class r8 = r11.getClass()     // Catch:{ IOException -> 0x0010 }
            java.lang.String r4 = "getParcelFileDescriptor"
            r5 = 0
            java.lang.Class[] r5 = new java.lang.Class[r5]     // Catch:{ Exception -> 0x0045 }
            java.lang.reflect.Method r12 = r8.getDeclaredMethod(r4, r5)     // Catch:{ Exception -> 0x0045 }
            r4 = 1
            r12.setAccessible(r4)     // Catch:{ Exception -> 0x0045 }
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x0045 }
            java.lang.Object r4 = r12.invoke(r11, r4)     // Catch:{ Exception -> 0x0045 }
            r0 = r4
            android.os.ParcelFileDescriptor r0 = (android.os.ParcelFileDescriptor) r0     // Catch:{ Exception -> 0x0045 }
            r3 = r0
        L_0x0038:
            android.content.res.AssetFileDescriptor r2 = new android.content.res.AssetFileDescriptor     // Catch:{ IOException -> 0x0010 }
            r4 = 0
            int r6 = r11.length()     // Catch:{ IOException -> 0x0010 }
            long r6 = (long) r6     // Catch:{ IOException -> 0x0010 }
            r2.<init>(r3, r4, r6)     // Catch:{ IOException -> 0x0010 }
            return r2
        L_0x0045:
            r9 = move-exception
            java.lang.String r4 = "SQLiteContentHelper"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0010 }
            r5.<init>()     // Catch:{ IOException -> 0x0010 }
            java.lang.String r6 = "SQLiteCursor.java: "
            java.lang.StringBuilder r5 = r5.append(r6)     // Catch:{ IOException -> 0x0010 }
            java.lang.StringBuilder r5 = r5.append(r9)     // Catch:{ IOException -> 0x0010 }
            java.lang.String r5 = r5.toString()     // Catch:{ IOException -> 0x0010 }
            android.util.Log.i(r4, r5)     // Catch:{ IOException -> 0x0010 }
            goto L_0x0038
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.ciphersql.database.SQLiteContentHelper.getBlobColumnAsAssetFile(dji.thirdparty.ciphersql.database.SQLiteDatabase, java.lang.String, java.lang.String[]):android.content.res.AssetFileDescriptor");
    }

    private static MemoryFile simpleQueryForBlobMemoryFile(SQLiteDatabase db, String sql, String[] selectionArgs) throws IOException {
        MemoryFile file = null;
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    byte[] bytes = cursor.getBlob(0);
                    if (bytes == null) {
                        cursor.close();
                    } else {
                        file = new MemoryFile(null, bytes.length);
                        file.writeBytes(bytes, 0, 0, bytes.length);
                        cursor.close();
                    }
                }
            } finally {
                cursor.close();
            }
        }
        return file;
    }
}
