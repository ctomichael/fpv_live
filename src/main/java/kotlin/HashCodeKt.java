package kotlin;

import kotlin.internal.InlineOnly;
import org.jetbrains.annotations.Nullable;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0000\n\u0000\u001a\u000f\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u0002H\b¨\u0006\u0003"}, d2 = {"hashCode", "", "", "kotlin-stdlib"}, k = 2, mv = {1, 1, 15})
/* compiled from: HashCode.kt */
public final class HashCodeKt {
    @SinceKotlin(version = "1.3")
    @InlineOnly
    private static final int hashCode(@Nullable Object $this$hashCode) {
        if ($this$hashCode != null) {
            return $this$hashCode.hashCode();
        }
        return 0;
    }
}
