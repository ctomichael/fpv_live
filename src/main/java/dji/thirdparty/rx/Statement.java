package dji.thirdparty.rx;

import dji.thirdparty.rx.functions.Func0;
import dji.thirdparty.rx.operators.OperatorIfThen;
import dji.thirdparty.rx.operators.OperatorSwitchCase;
import dji.thirdparty.rx.operators.OperatorWhileDoWhile;
import java.util.Map;

public final class Statement {
    private static final Func0True TRUE = new Func0True();

    private Statement() {
        throw new IllegalStateException("No instances!");
    }

    public static <K, R> Observable<R> switchCase(Func0<? extends K> caseSelector, Map<? super K, ? extends Observable<? extends R>> mapOfCases) {
        return switchCase(caseSelector, mapOfCases, Observable.empty());
    }

    public static <K, R> Observable<R> switchCase(Func0<? extends K> caseSelector, Map<? super K, ? extends Observable<? extends R>> mapOfCases, Scheduler scheduler) {
        return switchCase(caseSelector, mapOfCases, Observable.empty().subscribeOn(scheduler));
    }

    public static <K, R> Observable<R> switchCase(Func0<? extends K> caseSelector, Map<? super K, ? extends Observable<? extends R>> mapOfCases, Observable<? extends R> defaultCase) {
        return Observable.create(new OperatorSwitchCase(caseSelector, mapOfCases, defaultCase));
    }

    public static <T> Observable<T> doWhile(Observable<? extends T> source, Func0<Boolean> postCondition) {
        return Observable.create(new OperatorWhileDoWhile(source, TRUE, postCondition));
    }

    public static <T> Observable<T> whileDo(Observable<? extends T> source, Func0<Boolean> preCondition) {
        return Observable.create(new OperatorWhileDoWhile(source, preCondition, preCondition));
    }

    public static <R> Observable<R> ifThen(Func0<Boolean> condition, Observable<? extends R> then) {
        return ifThen(condition, then, Observable.empty());
    }

    public static <R> Observable<R> ifThen(Func0<Boolean> condition, Observable<? extends R> then, Scheduler scheduler) {
        return ifThen(condition, then, Observable.empty().subscribeOn(scheduler));
    }

    public static <R> Observable<R> ifThen(Func0<Boolean> condition, Observable<? extends R> then, Observable<? extends R> orElse) {
        return Observable.create(new OperatorIfThen(condition, then, orElse));
    }

    private static final class Func0True implements Func0<Boolean> {
        private Func0True() {
        }

        public Boolean call() {
            return true;
        }
    }
}
