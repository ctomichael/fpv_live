package dji.utils.function;

final /* synthetic */ class BiFunction$$Lambda$0 implements BiFunction {
    private final BiFunction arg$1;
    private final Function arg$2;

    BiFunction$$Lambda$0(BiFunction biFunction, Function function) {
        this.arg$1 = biFunction;
        this.arg$2 = function;
    }

    public BiFunction andThen(Function function) {
        return BiFunction$$CC.andThen(this, function);
    }

    public Object apply(Object obj, Object obj2) {
        return this.arg$2.apply(this.arg$1.apply(obj, obj2));
    }
}
