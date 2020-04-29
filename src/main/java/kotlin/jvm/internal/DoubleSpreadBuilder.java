package kotlin.jvm.internal;

import com.dji.component.fpv.base.errorpop.ErrorPopModel;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0013\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\u0002J\f\u0010\f\u001a\u00020\u0004*\u00020\u0002H\u0014R\u000e\u0010\u0006\u001a\u00020\u0002X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlin/jvm/internal/DoubleSpreadBuilder;", "Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "", "size", "", "(I)V", "values", ErrorPopModel.ACTION_ADD, "", "value", "", "toArray", "getSize", "kotlin-stdlib"}, k = 1, mv = {1, 1, 15})
/* compiled from: PrimitiveSpreadBuilders.kt */
public final class DoubleSpreadBuilder extends PrimitiveSpreadBuilder<double[]> {
    private final double[] values;

    public DoubleSpreadBuilder(int size) {
        super(size);
        this.values = new double[size];
    }

    /* access modifiers changed from: protected */
    public int getSize(@NotNull double[] $this$getSize) {
        Intrinsics.checkParameterIsNotNull($this$getSize, "$this$getSize");
        return $this$getSize.length;
    }

    public final void add(double value) {
        double[] dArr = this.values;
        int position = getPosition();
        setPosition(position + 1);
        dArr[position] = value;
    }

    @NotNull
    public final double[] toArray() {
        return (double[]) toArray(this.values, new double[size()]);
    }
}
