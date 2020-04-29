package com.mapbox.android.telemetry;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class CertificateBlacklist implements ConfigurationChangeHandler {
    private static final String BLACKLIST_FILE = "MapboxBlacklist";
    private static final String LOG_TAG = "MapboxBlacklist";
    private final Context context;
    private final List<String> revokedKeys = new CopyOnWriteArrayList();

    CertificateBlacklist(Context context2, ConfigurationClient configurationClient) {
        this.context = context2;
        configurationClient.addHandler(this);
        if (configurationClient.shouldUpdate()) {
            configurationClient.update();
        } else {
            retrieveBlackList(context2.getFilesDir(), false);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isBlacklisted(String hash) {
        return this.revokedKeys.contains(hash);
    }

    private void retrieveBlackList(File path, boolean overwrite) {
        if (path.isDirectory()) {
            File file = new File(path, "MapboxBlacklist");
            if (file.exists()) {
                try {
                    List<String> blacklist = obtainBlacklistContents(file);
                    if (!blacklist.isEmpty()) {
                        if (overwrite) {
                            this.revokedKeys.clear();
                        }
                        this.revokedKeys.addAll(blacklist);
                    }
                } catch (IOException exception) {
                    Log.e("MapboxBlacklist", exception.getMessage());
                }
            }
        }
    }

    private boolean saveBlackList(String data) {
        if (!isValidContent(data)) {
            return false;
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = this.context.openFileOutput("MapboxBlacklist", 0);
            outputStream.write(data.getBytes());
            if (outputStream == null) {
                return true;
            }
            try {
                outputStream.close();
                return true;
            } catch (IOException exception) {
                Log.e("MapboxBlacklist", exception.getMessage());
                return false;
            }
        } catch (IOException exception2) {
            Log.e("MapboxBlacklist", exception2.getMessage());
            if (outputStream == null) {
                return false;
            }
            try {
                outputStream.close();
                return false;
            } catch (IOException exception3) {
                Log.e("MapboxBlacklist", exception3.getMessage());
                return false;
            }
        } catch (Throwable th) {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException exception4) {
                    Log.e("MapboxBlacklist", exception4.getMessage());
                }
            }
            throw th;
        }
    }

    private static boolean isValidContent(String data) {
        boolean z;
        if (TextUtils.isEmpty(data)) {
            return false;
        }
        Gson gson = new GsonBuilder().create();
        try {
            JsonElement jsonElement = ((JsonObject) gson.fromJson(data, JsonObject.class)).get("RevokedCertKeys");
            JsonArray jsonArray = jsonElement.isJsonArray() ? (JsonArray) gson.fromJson(jsonElement, JsonArray.class) : null;
            if (jsonArray == null || jsonArray.size() <= 0) {
                z = false;
            } else {
                z = true;
            }
            return z;
        } catch (JsonSyntaxException exception) {
            Log.e("MapboxBlacklist", exception.getMessage());
            return false;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r9v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.util.List<java.lang.String>} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.gson.Gson.fromJson(java.io.Reader, java.lang.Class):T
     arg types: [java.io.BufferedReader, java.lang.Class]
     candidates:
      com.google.gson.Gson.fromJson(com.google.gson.JsonElement, java.lang.Class):T
      com.google.gson.Gson.fromJson(com.google.gson.JsonElement, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(com.google.gson.stream.JsonReader, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(java.io.Reader, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(java.lang.String, java.lang.Class):T
      com.google.gson.Gson.fromJson(java.lang.String, java.lang.reflect.Type):T
      com.google.gson.Gson.fromJson(java.io.Reader, java.lang.Class):T */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.String> obtainBlacklistContents(java.io.File r12) throws java.io.IOException {
        /*
            r11 = this;
            java.io.FileInputStream r4 = new java.io.FileInputStream
            r4.<init>(r12)
            java.io.BufferedReader r8 = new java.io.BufferedReader
            java.io.InputStreamReader r9 = new java.io.InputStreamReader
            r9.<init>(r4)
            r8.<init>(r9)
            com.google.gson.Gson r3 = new com.google.gson.Gson
            r3.<init>()
            r1 = 0
            java.lang.Class<com.google.gson.JsonObject> r9 = com.google.gson.JsonObject.class
            java.lang.Object r6 = r3.fromJson(r8, r9)     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            com.google.gson.JsonObject r6 = (com.google.gson.JsonObject) r6     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            if (r6 == 0) goto L_0x003b
            java.lang.String r9 = "RevokedCertKeys"
            com.google.gson.JsonArray r5 = r6.getAsJsonArray(r9)     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            com.mapbox.android.telemetry.CertificateBlacklist$1 r9 = new com.mapbox.android.telemetry.CertificateBlacklist$1     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            r9.<init>()     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            java.lang.reflect.Type r7 = r9.getType()     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            java.lang.String r9 = r5.toString()     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            java.lang.Object r9 = r3.fromJson(r9, r7)     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            r0 = r9
            java.util.List r0 = (java.util.List) r0     // Catch:{ JsonIOException -> 0x0041, JsonSyntaxException -> 0x0052 }
            r1 = r0
        L_0x003b:
            r8.close()
            if (r1 == 0) goto L_0x004d
        L_0x0040:
            return r1
        L_0x0041:
            r2 = move-exception
        L_0x0042:
            java.lang.String r9 = "MapboxBlacklist"
            java.lang.String r10 = r2.getMessage()
            android.util.Log.e(r9, r10)
            goto L_0x003b
        L_0x004d:
            java.util.List r1 = java.util.Collections.emptyList()
            goto L_0x0040
        L_0x0052:
            r2 = move-exception
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapbox.android.telemetry.CertificateBlacklist.obtainBlacklistContents(java.io.File):java.util.List");
    }

    public void onUpdate(String data) {
        if (saveBlackList(data)) {
            retrieveBlackList(this.context.getFilesDir(), true);
        }
    }
}
