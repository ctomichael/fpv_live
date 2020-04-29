package dji.utils.function;

final /* synthetic */ class Function$$Lambda$2 implements Function {
    static final Function $instance = new Function$$Lambda$2();

    private Function$$Lambda$2() {
    }

    public Function andThen(Function function) {
        return Function$$CC.andThen(this, function);
    }

    public Object apply(Object obj) {
        return Function$$CC.lambda$identity$2$Function$$CC(obj);
    }

    public Function compose(Function function) {
        return Function$$CC.compose(this, function);
    }
}
