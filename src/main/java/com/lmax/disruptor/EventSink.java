package com.lmax.disruptor;

public interface EventSink<E> {
    void publishEvent(EventTranslator<E> eventTranslator);

    <A> void publishEvent(EventTranslatorOneArg<E, A> eventTranslatorOneArg, A a);

    <A, B, C> void publishEvent(EventTranslatorThreeArg<E, A, B, C> eventTranslatorThreeArg, A a, B b, C c);

    <A, B> void publishEvent(EventTranslatorTwoArg<E, A, B> eventTranslatorTwoArg, A a, B b);

    void publishEvent(EventTranslatorVararg eventTranslatorVararg, Object... objArr);

    <A> void publishEvents(EventTranslatorOneArg eventTranslatorOneArg, int i, int i2, Object[] objArr);

    <A> void publishEvents(EventTranslatorOneArg<E, A> eventTranslatorOneArg, A[] aArr);

    <A, B, C> void publishEvents(EventTranslatorThreeArg<E, A, B, C> eventTranslatorThreeArg, int i, int i2, A[] aArr, B[] bArr, C[] cArr);

    <A, B, C> void publishEvents(EventTranslatorThreeArg eventTranslatorThreeArg, Object[] objArr, Object[] objArr2, Object[] objArr3);

    <A, B> void publishEvents(EventTranslatorTwoArg<E, A, B> eventTranslatorTwoArg, int i, int i2, A[] aArr, B[] bArr);

    <A, B> void publishEvents(EventTranslatorTwoArg eventTranslatorTwoArg, Object[] objArr, Object[] objArr2);

    void publishEvents(EventTranslatorVararg eventTranslatorVararg, int i, int i2, Object[]... objArr);

    void publishEvents(EventTranslatorVararg eventTranslatorVararg, Object[]... objArr);

    void publishEvents(EventTranslator<E>[] eventTranslatorArr);

    void publishEvents(EventTranslator<E>[] eventTranslatorArr, int i, int i2);

    boolean tryPublishEvent(EventTranslator<E> eventTranslator);

    <A> boolean tryPublishEvent(EventTranslatorOneArg<E, A> eventTranslatorOneArg, A a);

    <A, B, C> boolean tryPublishEvent(EventTranslatorThreeArg<E, A, B, C> eventTranslatorThreeArg, A a, B b, C c);

    <A, B> boolean tryPublishEvent(EventTranslatorTwoArg<E, A, B> eventTranslatorTwoArg, A a, B b);

    boolean tryPublishEvent(EventTranslatorVararg<E> eventTranslatorVararg, Object... objArr);

    <A> boolean tryPublishEvents(EventTranslatorOneArg eventTranslatorOneArg, int i, int i2, Object[] objArr);

    <A> boolean tryPublishEvents(EventTranslatorOneArg<E, A> eventTranslatorOneArg, A[] aArr);

    <A, B, C> boolean tryPublishEvents(EventTranslatorThreeArg<E, A, B, C> eventTranslatorThreeArg, int i, int i2, A[] aArr, B[] bArr, C[] cArr);

    <A, B, C> boolean tryPublishEvents(EventTranslatorThreeArg eventTranslatorThreeArg, Object[] objArr, Object[] objArr2, Object[] objArr3);

    <A, B> boolean tryPublishEvents(EventTranslatorTwoArg<E, A, B> eventTranslatorTwoArg, int i, int i2, A[] aArr, B[] bArr);

    <A, B> boolean tryPublishEvents(EventTranslatorTwoArg eventTranslatorTwoArg, Object[] objArr, Object[] objArr2);

    boolean tryPublishEvents(EventTranslatorVararg eventTranslatorVararg, int i, int i2, Object[]... objArr);

    boolean tryPublishEvents(EventTranslatorVararg<E> eventTranslatorVararg, Object[]... objArr);

    boolean tryPublishEvents(EventTranslator<E>[] eventTranslatorArr);

    boolean tryPublishEvents(EventTranslator<E>[] eventTranslatorArr, int i, int i2);
}
