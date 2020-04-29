package kotlin.jvm.internal;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\t\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0004H\u0004J\u001d\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\u0004*\u00028\u0000H$¢\u0006\u0002\u0010\u0018R\u001a\u0010\u0006\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\u0005R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000bX\u0004¢\u0006\n\n\u0002\u0010\u000e\u0012\u0004\b\f\u0010\r¨\u0006\u0019"}, d2 = {"Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "T", "", "size", "", "(I)V", "position", "getPosition", "()I", "setPosition", "spreads", "", "spreads$annotations", "()V", "[Ljava/lang/Object;", "addSpread", "", "spreadArgument", "(Ljava/lang/Object;)V", "toArray", "values", "result", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getSize", "(Ljava/lang/Object;)I", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: PrimitiveSpreadBuilders.kt */
public abstract class PrimitiveSpreadBuilder<T> {
    private int position;
    private final int size;
    private final T[] spreads = new Object[this.size];

    private static /* synthetic */ void spreads$annotations() {
    }

    /* access modifiers changed from: protected */
    public abstract int getSize(@NotNull Object obj);

    public PrimitiveSpreadBuilder(int size2) {
        this.size = size2;
    }

    /* access modifiers changed from: protected */
    public final int getPosition() {
        return this.position;
    }

    /* access modifiers changed from: protected */
    public final void setPosition(int i) {
        this.position = i;
    }

    public final void addSpread(@NotNull T spreadArgument) {
        Intrinsics.checkParameterIsNotNull(spreadArgument, "spreadArgument");
        T[] tArr = this.spreads;
        int i = this.position;
        this.position = i + 1;
        tArr[i] = spreadArgument;
    }

    /* access modifiers changed from: protected */
    public final int size() {
        int totalLength = 0;
        int i = 0;
        int i2 = this.size - 1;
        if (0 <= i2) {
            while (true) {
                T t = this.spreads[i];
                totalLength += t != null ? getSize(t) : 1;
                if (i == i2) {
                    break;
                }
                i++;
            }
        }
        return totalLength;
    }

    /* access modifiers changed from: protected */
    @NotNull
    public final T toArray(@NotNull T values, @NotNull T result) {
        Intrinsics.checkParameterIsNotNull(values, "values");
        Intrinsics.checkParameterIsNotNull(result, "result");
        int dstIndex = 0;
        int copyValuesFrom = 0;
        int i = this.size - 1;
        if (0 <= i) {
            int i2 = 0;
            while (true) {
                Object spreadArgument = this.spreads[i2];
                if (spreadArgument != null) {
                    if (copyValuesFrom < i2) {
                        System.arraycopy(values, copyValuesFrom, result, dstIndex, i2 - copyValuesFrom);
                        dstIndex += i2 - copyValuesFrom;
                    }
                    int spreadSize = getSize(spreadArgument);
                    System.arraycopy(spreadArgument, 0, result, dstIndex, spreadSize);
                    dstIndex += spreadSize;
                    copyValuesFrom = i2 + 1;
                }
                if (i2 == i) {
                    break;
                }
                i2++;
            }
        }
        if (copyValuesFrom < this.size) {
            System.arraycopy(values, copyValuesFrom, result, dstIndex, this.size - copyValuesFrom);
        }
        return result;
    }
}
