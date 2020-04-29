package com.dji.widget2.bubble;

import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u001a\u001a\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00032\b\u0010\u000b\u001a\u0004\u0018\u00010\u0003\u001a\"\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u00032\b\u0010\u000b\u001a\u0004\u0018\u00010\u00032\u0006\u0010\f\u001a\u00020\r\u001a\u0016\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\f\u001a\u00020\r\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"FRAGMENT_CON", "", "findAnchorView", "Landroid/view/View;", "rootView", "Landroid/view/ViewGroup;", "contentDescription", "", "getLocationInView", "Landroid/graphics/RectF;", "parent", "child", "padding", "", "transformRectF", "rect", "Landroid/graphics/Rect;", "DJIBaseWidget_debug"}, k = 2, mv = {1, 1, 15})
/* compiled from: ViewUtil.kt */
public final class ViewUtilKt {
    private static final String FRAGMENT_CON = "NoSaveStateFrameLayout";

    @Nullable
    public static final View findAnchorView(@NotNull ViewGroup rootView, @StringRes int contentDescription) {
        Intrinsics.checkParameterIsNotNull(rootView, "rootView");
        String keyContentDescription = rootView.getContext().getString(contentDescription);
        ArrayList outViews = new ArrayList();
        rootView.findViewsWithText(outViews, keyContentDescription, 2);
        if (outViews.size() > 0) {
            return (View) outViews.get(0);
        }
        return null;
    }

    @NotNull
    public static final RectF getLocationInView(@Nullable View parent, @Nullable View child) {
        return getLocationInView(parent, child, 0.0f);
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 146 */
    @org.jetbrains.annotations.NotNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final android.graphics.RectF getLocationInView(@org.jetbrains.annotations.Nullable android.view.View r9, @org.jetbrains.annotations.Nullable android.view.View r10, float r11) {
        /*
            r6 = 0
            if (r10 == 0) goto L_0x00c6
            if (r9 == 0) goto L_0x00c6
            r2 = r6
            android.view.View r2 = (android.view.View) r2
            android.content.Context r1 = r10.getContext()
            boolean r7 = r1 instanceof android.app.Activity
            if (r7 == 0) goto L_0x0020
            android.app.Activity r1 = (android.app.Activity) r1
            android.view.Window r7 = r1.getWindow()
            java.lang.String r8 = "context.window"
            kotlin.jvm.internal.Intrinsics.checkExpressionValueIsNotNull(r7, r8)
            android.view.View r2 = r7.getDecorView()
        L_0x0020:
            android.graphics.Rect r3 = new android.graphics.Rect
            r3.<init>()
            android.graphics.Rect r5 = new android.graphics.Rect
            r5.<init>()
            r4 = r10
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r10, r9)
            if (r7 == 0) goto L_0x003b
            r10.getHitRect(r3)
            android.graphics.RectF r6 = transformRectF(r3, r11)
        L_0x0038:
            return r6
        L_0x0039:
            android.view.View r4 = (android.view.View) r4
        L_0x003b:
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r4, r2)
            r7 = r7 ^ 1
            if (r7 == 0) goto L_0x00ae
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r4, r9)
            r7 = r7 ^ 1
            if (r7 == 0) goto L_0x00ae
            if (r4 == 0) goto L_0x0050
            r4.getHitRect(r5)
        L_0x0050:
            java.lang.String r8 = "NoSaveStateFrameLayout"
            if (r4 == 0) goto L_0x008d
            java.lang.Class r7 = r4.getClass()
        L_0x0059:
            java.lang.String r7 = java.lang.String.valueOf(r7)
            boolean r7 = kotlin.jvm.internal.Intrinsics.areEqual(r8, r7)
            r7 = r7 ^ 1
            if (r7 == 0) goto L_0x0073
            int r7 = r3.left
            int r8 = r5.left
            int r7 = r7 + r8
            r3.left = r7
            int r7 = r3.top
            int r8 = r5.top
            int r7 = r7 + r8
            r3.top = r7
        L_0x0073:
            if (r4 == 0) goto L_0x008f
            android.view.ViewParent r4 = r4.getParent()
        L_0x0079:
            android.view.View r4 = (android.view.View) r4
            if (r4 != 0) goto L_0x0091
            r0 = 0
            java.lang.String r7 = "the view is not showing in the window!"
            java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            java.lang.Throwable r6 = (java.lang.Throwable) r6
            throw r6
        L_0x008d:
            r7 = r6
            goto L_0x0059
        L_0x008f:
            r4 = r6
            goto L_0x0079
        L_0x0091:
            android.view.ViewParent r7 = r4.getParent()
            if (r7 == 0) goto L_0x003b
            android.view.ViewParent r7 = r4.getParent()
            boolean r7 = r7 instanceof android.support.v4.view.ViewPager
            if (r7 == 0) goto L_0x003b
            android.view.ViewParent r4 = r4.getParent()
            if (r4 != 0) goto L_0x0039
            kotlin.TypeCastException r6 = new kotlin.TypeCastException
            java.lang.String r7 = "null cannot be cast to non-null type android.view.View"
            r6.<init>(r7)
            throw r6
        L_0x00ae:
            int r6 = r3.left
            int r7 = r10.getMeasuredWidth()
            int r6 = r6 + r7
            r3.right = r6
            int r6 = r3.top
            int r7 = r10.getMeasuredHeight()
            int r6 = r6 + r7
            r3.bottom = r6
            android.graphics.RectF r6 = transformRectF(r3, r11)
            goto L_0x0038
        L_0x00c6:
            android.graphics.RectF r6 = new android.graphics.RectF
            r6.<init>()
            goto L_0x0038
        */
        throw new UnsupportedOperationException("Method not decompiled: com.dji.widget2.bubble.ViewUtilKt.getLocationInView(android.view.View, android.view.View, float):android.graphics.RectF");
    }

    @NotNull
    public static final RectF transformRectF(@NotNull Rect rect, float padding) {
        Intrinsics.checkParameterIsNotNull(rect, "rect");
        RectF rectF = new RectF();
        rectF.left = ((float) rect.left) - padding;
        rectF.top = ((float) rect.top) - padding;
        rectF.right = ((float) rect.right) + padding;
        rectF.bottom = ((float) rect.bottom) + padding;
        return rectF;
    }
}
