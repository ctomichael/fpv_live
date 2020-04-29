package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.gms.common.annotation.KeepForSdk;

@KeepForSdk
public class ViewUtils {
    private ViewUtils() {
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.content.res.Resources.getValue(java.lang.String, android.util.TypedValue, boolean):void throws android.content.res.Resources$NotFoundException}
     arg types: [java.lang.String, android.util.TypedValue, int]
     candidates:
      ClspMth{android.content.res.Resources.getValue(int, android.util.TypedValue, boolean):void throws android.content.res.Resources$NotFoundException}
      ClspMth{android.content.res.Resources.getValue(java.lang.String, android.util.TypedValue, boolean):void throws android.content.res.Resources$NotFoundException} */
    @KeepForSdk
    public static String getXmlAttributeString(String str, String str2, Context context, AttributeSet attributeSet, boolean z, boolean z2, String str3) {
        String attributeValue = attributeSet == null ? null : attributeSet.getAttributeValue(str, str2);
        if (attributeValue != null && attributeValue.startsWith("@string/") && z) {
            String substring = attributeValue.substring(8);
            String packageName = context.getPackageName();
            TypedValue typedValue = new TypedValue();
            try {
                context.getResources().getValue(new StringBuilder(String.valueOf(packageName).length() + 8 + String.valueOf(substring).length()).append(packageName).append(":string/").append(substring).toString(), typedValue, true);
            } catch (Resources.NotFoundException e) {
                Log.w(str3, new StringBuilder(String.valueOf(str2).length() + 30 + String.valueOf(attributeValue).length()).append("Could not find resource for ").append(str2).append(": ").append(attributeValue).toString());
            }
            if (typedValue.string != null) {
                attributeValue = typedValue.string.toString();
            } else {
                String valueOf = String.valueOf(typedValue);
                Log.w(str3, new StringBuilder(String.valueOf(str2).length() + 28 + String.valueOf(valueOf).length()).append("Resource ").append(str2).append(" was not a string: ").append(valueOf).toString());
            }
        }
        if (z2 && attributeValue == null) {
            Log.w(str3, new StringBuilder(String.valueOf(str2).length() + 33).append("Required XML attribute \"").append(str2).append("\" missing").toString());
        }
        return attributeValue;
    }
}
