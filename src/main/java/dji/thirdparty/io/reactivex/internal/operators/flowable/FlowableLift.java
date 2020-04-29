package dji.thirdparty.io.reactivex.internal.operators.flowable;

import dji.thirdparty.io.reactivex.FlowableOperator;
import dji.thirdparty.io.reactivex.exceptions.Exceptions;
import dji.thirdparty.io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public final class FlowableLift<R, T> extends AbstractFlowableWithUpstream<T, R> {
    final FlowableOperator<? extends R, ? super T> operator;

    public FlowableLift(Publisher<T> source, FlowableOperator<? extends R, ? super T> operator2) {
        super(source);
        this.operator = operator2;
    }

    public void subscribeActual(Subscriber<? super R> s) {
        try {
            Subscriber<? super T> st = this.operator.apply(s);
            if (st == null) {
                throw new NullPointerException("Operator " + this.operator + " returned a null Subscriber");
            }
            this.source.subscribe(st);
        } catch (NullPointerException e) {
            throw e;
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            RxJavaPlugins.onError(e2);
            NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            npe.initCause(e2);
            throw npe;
        }
    }
}
