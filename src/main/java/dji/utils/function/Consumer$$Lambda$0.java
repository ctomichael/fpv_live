package dji.utils.function;

final /* synthetic */ class Consumer$$Lambda$0 implements Consumer {
    private final Consumer arg$1;
    private final Consumer arg$2;

    Consumer$$Lambda$0(Consumer consumer, Consumer consumer2) {
        this.arg$1 = consumer;
        this.arg$2 = consumer2;
    }

    public void accept(Object obj) {
        Consumer$$CC.lambda$andThen$0$Consumer$$CC(this.arg$1, this.arg$2, obj);
    }

    public Consumer andThen(Consumer consumer) {
        return Consumer$$CC.andThen(this, consumer);
    }
}
