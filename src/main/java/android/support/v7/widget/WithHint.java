package android.support.v7.widget;

import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public interface WithHint {
    @Nullable
    CharSequence getHint();
}
