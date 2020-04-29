package com.dji.widget2.bubble;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.dji.widget2.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.bouncycastle.i18n.TextBundle;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u0000 \u001a2\u00020\u0001:\u0002\u001a\u001bB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0010\u001a\u00020\bH\u0002J\b\u0010\u0011\u001a\u00020\bH\u0002J\u0006\u0010\u0012\u001a\u00020\bJ\u0006\u0010\u0013\u001a\u00020\bJ\b\u0010\u0014\u001a\u00020\u0015H\u0014J\u0010\u0010\u0016\u001a\u00020\u00152\b\b\u0001\u0010\u0017\u001a\u00020\bJ\u000e\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019R\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0016\u0010\r\u001a\u00020\u00058\u0002X\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u000e\u0010\u000f¨\u0006\u001c"}, d2 = {"Lcom/dji/widget2/bubble/GuideBubbleViewKt;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "gravity", "", "(Landroid/content/Context;J)V", "arrowOffset", "", "getArrowOffset", "()I", "setArrowOffset", "(I)V", "mGravity", "mGravity$annotations", "()V", "getArrowHeight", "getArrowWidth", "getRealHeight", "getRealWidth", "onAttachedToWindow", "", "setText", "resId", TextBundle.TEXT_ENTRY, "", "Companion", "Gravity", "DJIBaseWidget_debug"}, k = 1, mv = {1, 1, 15})
/* compiled from: GuideBubbleViewKt.kt */
public final class GuideBubbleViewKt extends LinearLayout {
    public static final long BOTTOM = 4;
    public static final Companion Companion = new Companion(null);
    public static final long LEFT = 1;
    public static final long RIGHT = 2;
    @NotNull
    public static final String TAG = "GuideBubbleView";
    public static final long TOP = 3;
    private HashMap _$_findViewCache;
    private int arrowOffset;
    private final long mGravity;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, d2 = {"Lcom/dji/widget2/bubble/GuideBubbleViewKt$Gravity;", "", "DJIBaseWidget_debug"}, k = 1, mv = {1, 1, 15})
    @Retention(RetentionPolicy.RUNTIME)
    /* compiled from: GuideBubbleViewKt.kt */
    public @interface Gravity {
    }

    @Gravity
    private static /* synthetic */ void mGravity$annotations() {
    }

    public void _$_clearFindViewByIdCache() {
        if (this._$_findViewCache != null) {
            this._$_findViewCache.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public GuideBubbleViewKt(@NotNull Context context, long gravity) {
        super(context);
        Intrinsics.checkParameterIsNotNull(context, "context");
        this.mGravity = gravity;
        long j = this.mGravity;
        if (j == 1) {
            LayoutInflater.from(context).inflate(R.layout.guide_bubble_left, this);
            setOrientation(0);
        } else if (j == 2) {
            LayoutInflater.from(context).inflate(R.layout.guide_bubble_right, this);
            setOrientation(0);
        } else if (j == 3) {
            LayoutInflater.from(context).inflate(R.layout.guide_bubble_top, this);
            setOrientation(1);
        } else if (j == 4) {
            LayoutInflater.from(context).inflate(R.layout.guide_bubble_bottom, this);
            setOrientation(1);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\n"}, d2 = {"Lcom/dji/widget2/bubble/GuideBubbleViewKt$Companion;", "", "()V", "BOTTOM", "", "LEFT", "RIGHT", "TAG", "", "TOP", "DJIBaseWidget_debug"}, k = 1, mv = {1, 1, 15})
    /* compiled from: GuideBubbleViewKt.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }

    public final int getArrowOffset() {
        return this.arrowOffset;
    }

    public final void setArrowOffset(int i) {
        this.arrowOffset = i;
    }

    public final void setText(@StringRes int resId) {
        ((TextView) _$_findCachedViewById(R.id.contentTv)).setText(resId);
        measure(0, 0);
    }

    public final void setText(@NotNull String text) {
        Intrinsics.checkParameterIsNotNull(text, TextBundle.TEXT_ENTRY);
        TextView textView = (TextView) _$_findCachedViewById(R.id.contentTv);
        Intrinsics.checkExpressionValueIsNotNull(textView, "contentTv");
        textView.setText(text);
        measure(0, 0);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        int arrowPosition = 0;
        long j = this.mGravity;
        if (j == 1 || j == 2) {
            arrowPosition = getMeasuredHeight() / 2;
        } else if (j == 3 || j == 4) {
            arrowPosition = getMeasuredWidth() / 2;
        }
        int arrowWidth = getArrowWidth();
        int arrowHeight = getArrowHeight();
        ImageView imageView = (ImageView) _$_findCachedViewById(R.id.arrowIv);
        Intrinsics.checkExpressionValueIsNotNull(imageView, "arrowIv");
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (layoutParams == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layoutParams;
        long j2 = this.mGravity;
        if (j2 == 1 || j2 == 2) {
            lp.topMargin = this.arrowOffset + (arrowPosition - (arrowHeight / 2));
        } else if (j2 == 3 || j2 == 4) {
            lp.leftMargin = this.arrowOffset + (arrowPosition - (arrowWidth / 2));
        }
        ImageView imageView2 = (ImageView) _$_findCachedViewById(R.id.arrowIv);
        Intrinsics.checkExpressionValueIsNotNull(imageView2, "arrowIv");
        imageView2.setLayoutParams(lp);
    }

    public final int getRealHeight() {
        return getMeasuredHeight();
    }

    public final int getRealWidth() {
        return getMeasuredWidth();
    }

    private final int getArrowWidth() {
        ImageView imageView = (ImageView) _$_findCachedViewById(R.id.arrowIv);
        Intrinsics.checkExpressionValueIsNotNull(imageView, "arrowIv");
        Drawable arrowBackground = imageView.getBackground();
        Intrinsics.checkExpressionValueIsNotNull(arrowBackground, "arrowBackground");
        return arrowBackground.getIntrinsicWidth();
    }

    private final int getArrowHeight() {
        ImageView imageView = (ImageView) _$_findCachedViewById(R.id.arrowIv);
        Intrinsics.checkExpressionValueIsNotNull(imageView, "arrowIv");
        Drawable arrowBackground = imageView.getBackground();
        Intrinsics.checkExpressionValueIsNotNull(arrowBackground, "arrowBackground");
        return arrowBackground.getIntrinsicHeight();
    }
}
