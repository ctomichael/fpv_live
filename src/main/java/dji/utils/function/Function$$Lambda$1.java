package dji.utils.function;

final /* synthetic */ class Function$$Lambda$1 implements Function {
    private final Function arg$1;
    private final Function arg$2;

    Function$$Lambda$1(Function function, Function function2) {
        this.arg$1 = function;
        this.arg$2 = function2;
    }

    public Function andThen(Function function) {
        return Function$$CC.andThen(this, function);
    }

    public Object apply(Object obj) {
        return this.arg$2.apply(this.arg$1.apply(obj));
    }

    public Function compose(Function function) {
        return Function$$CC.compose(this, function);
    }
}
