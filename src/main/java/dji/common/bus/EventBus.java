package dji.common.bus;

import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.subjects.Subject;

@EXClassNullAway
public class EventBus<T> {
    private final Subject<T, T> subject;

    EventBus(Subject<T, T> subject2) {
        this.subject = subject2;
    }

    public <E extends T> void post(E event) {
        this.subject.onNext(event);
    }

    public Observable<T> register() {
        return this.subject;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Class, java.lang.Class<E>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <E extends T> dji.thirdparty.rx.Observable<E> register(java.lang.Class<E> r2) {
        /*
            r1 = this;
            dji.thirdparty.rx.subjects.Subject<T, T> r0 = r1.subject
            dji.thirdparty.rx.Observable r0 = r0.ofType(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.common.bus.EventBus.register(java.lang.Class):dji.thirdparty.rx.Observable");
    }

    public <E1 extends T, E2 extends T> Observable<Object> register(Class<E1> class1, Class<E2> class2) {
        return Observable.merge(register(class1), register(class2));
    }

    public <E1 extends T, E2 extends T, E3 extends T> Observable<Object> register(Class<E1> class1, Class<E2> class2, Class<E3> class3) {
        return Observable.merge(register(class1), register(class2), register(class3));
    }

    public <E1 extends T, E2 extends T, E3 extends T, E4 extends T> Observable<Object> register(Class<E1> class1, Class<E2> class2, Class<E3> class3, Class<E4> class4) {
        return Observable.merge(register(class1), register(class2), register(class3), register(class4));
    }

    public Observable<T> serializedRegister() {
        return this.subject.serialize().onBackpressureLatest();
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [java.lang.Class, java.lang.Class<E>], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <E extends T> dji.thirdparty.rx.Observable<E> serializedRegister(java.lang.Class<E> r2) {
        /*
            r1 = this;
            dji.thirdparty.rx.subjects.Subject<T, T> r0 = r1.subject
            dji.thirdparty.rx.Observable r0 = r0.ofType(r2)
            dji.thirdparty.rx.Observable r0 = r0.serialize()
            dji.thirdparty.rx.Observable r0 = r0.onBackpressureLatest()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.common.bus.EventBus.serializedRegister(java.lang.Class):dji.thirdparty.rx.Observable");
    }
}
