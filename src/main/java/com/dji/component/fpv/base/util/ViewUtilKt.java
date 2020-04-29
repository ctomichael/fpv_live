package com.dji.component.fpv.base.util;

import android.support.annotation.DimenRes;
import android.support.annotation.StringRes;
import android.view.View;
import dji.publics.protocol.ResponseBase;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0001\u001a\u0014\u0010\u0004\u001a\u00020\u0005*\u00020\u00022\b\b\u0001\u0010\u0003\u001a\u00020\u0001¨\u0006\u0006"}, d2 = {"getDimensionPixelSize", "", "Landroid/view/View;", ResponseBase.STRING_ID, "getString", "", "DJIFpvBase_debug"}, k = 2, mv = {1, 1, 15})
/* compiled from: ViewUtil.kt */
public final class ViewUtilKt {
    public static final int getDimensionPixelSize(@NotNull View $this$getDimensionPixelSize, @DimenRes int id) {
        Intrinsics.checkParameterIsNotNull($this$getDimensionPixelSize, "$this$getDimensionPixelSize");
        return $this$getDimensionPixelSize.getResources().getDimensionPixelSize(id);
    }

    @NotNull
    public static final String getString(@NotNull View $this$getString, @StringRes int id) {
        Intrinsics.checkParameterIsNotNull($this$getString, "$this$getString");
        String string = $this$getString.getResources().getString(id);
        Intrinsics.checkExpressionValueIsNotNull(string, "resources.getString(id)");
        return string;
    }
}
