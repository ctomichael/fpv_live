package dji.thirdparty.io.reactivex.observers;

import dji.thirdparty.io.reactivex.Notification;
import dji.thirdparty.io.reactivex.disposables.Disposable;
import dji.thirdparty.io.reactivex.exceptions.CompositeException;
import dji.thirdparty.io.reactivex.functions.Predicate;
import dji.thirdparty.io.reactivex.internal.functions.Functions;
import dji.thirdparty.io.reactivex.internal.functions.ObjectHelper;
import dji.thirdparty.io.reactivex.internal.util.ExceptionHelper;
import dji.thirdparty.io.reactivex.observers.BaseTestConsumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class BaseTestConsumer<T, U extends BaseTestConsumer<T, U>> implements Disposable {
    protected boolean checkSubscriptionOnce;
    protected long completions;
    protected final CountDownLatch done = new CountDownLatch(1);
    protected final List<Throwable> errors = new ArrayList();
    protected int establishedFusionMode;
    protected int initialFusionMode;
    protected Thread lastThread;
    protected final List<T> values = new ArrayList();

    public abstract U assertNotSubscribed();

    public abstract U assertSubscribed();

    public final Thread lastThread() {
        return this.lastThread;
    }

    public final List<T> values() {
        return this.values;
    }

    public final List<Throwable> errors() {
        return this.errors;
    }

    public final long completions() {
        return this.completions;
    }

    public final boolean isTerminated() {
        return this.done.getCount() == 0;
    }

    public final int valueCount() {
        return this.values.size();
    }

    public final int errorCount() {
        return this.errors.size();
    }

    /* access modifiers changed from: protected */
    public final AssertionError fail(String message) {
        StringBuilder b = new StringBuilder(message.length() + 64);
        b.append(message);
        b.append(" (").append("latch = ").append(this.done.getCount()).append(", ").append("values = ").append(this.values.size()).append(", ").append("errors = ").append(this.errors.size()).append(", ").append("completions = ").append(this.completions).append(')');
        AssertionError ae = new AssertionError(b.toString());
        if (!this.errors.isEmpty()) {
            if (this.errors.size() == 1) {
                ae.initCause(this.errors.get(0));
            } else {
                ae.initCause(new CompositeException(this.errors));
            }
        }
        return ae;
    }

    public final U await() throws InterruptedException {
        if (this.done.getCount() != 0) {
            this.done.await();
        }
        return this;
    }

    public final boolean await(long time, TimeUnit unit) throws InterruptedException {
        return this.done.getCount() == 0 || this.done.await(time, unit);
    }

    public final U assertComplete() {
        long c = this.completions;
        if (c == 0) {
            throw fail("Not completed");
        } else if (c <= 1) {
            return this;
        } else {
            throw fail("Multiple completions: " + c);
        }
    }

    public final U assertNotComplete() {
        long c = this.completions;
        if (c == 1) {
            throw fail("Completed!");
        } else if (c <= 1) {
            return this;
        } else {
            throw fail("Multiple completions: " + c);
        }
    }

    public final U assertNoErrors() {
        if (this.errors.size() == 0) {
            return this;
        }
        throw fail("Error(s) present: " + this.errors);
    }

    public final U assertError(Throwable error) {
        return assertError(Functions.equalsWith(error));
    }

    public final U assertError(Class<? extends Throwable> errorClass) {
        return assertError(Functions.isInstanceOf(errorClass));
    }

    public final U assertError(Predicate<Throwable> errorPredicate) {
        int s = this.errors.size();
        if (s == 0) {
            throw fail("No errors");
        }
        boolean found = false;
        Iterator i$ = this.errors.iterator();
        while (true) {
            if (!i$.hasNext()) {
                break;
            }
            try {
                if (errorPredicate.test(i$.next())) {
                    found = true;
                    break;
                }
            } catch (Exception ex) {
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        if (!found) {
            throw fail("Error not present");
        } else if (s == 1) {
            return this;
        } else {
            throw fail("Error present but other errors as well");
        }
    }

    public final U assertValue(T value) {
        if (this.values.size() != 1) {
            throw fail("Expected: " + valueAndClass(value) + ", Actual: " + this.values);
        }
        T v = this.values.get(0);
        if (ObjectHelper.equals(value, v)) {
            return this;
        }
        throw fail("Expected: " + valueAndClass(value) + ", Actual: " + valueAndClass(v));
    }

    public final U assertValue(Predicate<T> valuePredicate) {
        assertValueAt(0, valuePredicate);
        if (this.values.size() <= 1) {
            return this;
        }
        throw fail("Value present but other values as well");
    }

    public final U assertValueAt(int index, Predicate<T> valuePredicate) {
        if (this.values.size() == 0) {
            throw fail("No values");
        } else if (index >= this.values.size()) {
            throw fail("Invalid index: " + index);
        } else {
            boolean found = false;
            try {
                if (valuePredicate.test(this.values.get(index))) {
                    found = true;
                }
                if (found) {
                    return this;
                }
                throw fail("Value not present");
            } catch (Exception ex) {
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
    }

    public static String valueAndClass(Object o) {
        if (o != null) {
            return o + " (class: " + o.getClass().getSimpleName() + ")";
        }
        return "null";
    }

    public final U assertValueCount(int count) {
        int s = this.values.size();
        if (s == count) {
            return this;
        }
        throw fail("Value counts differ; Expected: " + count + ", Actual: " + s);
    }

    public final U assertNoValues() {
        return assertValueCount(0);
    }

    public final U assertValues(T... values2) {
        int s = this.values.size();
        if (s != values2.length) {
            throw fail("Value count differs; Expected: " + values2.length + " " + Arrays.toString(values2) + ", Actual: " + s + " " + this.values);
        }
        for (int i = 0; i < s; i++) {
            T v = this.values.get(i);
            T u = values2[i];
            if (!ObjectHelper.equals(u, v)) {
                throw fail("Values at position " + i + " differ; Expected: " + valueAndClass(u) + ", Actual: " + valueAndClass(v));
            }
        }
        return this;
    }

    public final U assertValueSet(Collection<? extends T> expected) {
        if (expected.isEmpty()) {
            assertNoValues();
        } else {
            for (T v : this.values) {
                if (!expected.contains(v)) {
                    throw fail("Value not in the expected collection: " + valueAndClass(v));
                }
            }
        }
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:5:0x0019  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final U assertValueSequence(java.lang.Iterable<? extends T> r10) {
        /*
            r9 = this;
            r2 = 0
            java.util.List<T> r7 = r9.values
            java.util.Iterator r6 = r7.iterator()
            java.util.Iterator r3 = r10.iterator()
        L_0x000b:
            boolean r0 = r3.hasNext()
            boolean r1 = r6.hasNext()
            if (r0 == 0) goto L_0x0017
            if (r1 != 0) goto L_0x0039
        L_0x0017:
            if (r0 == 0) goto L_0x0081
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "More values received than expected ("
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r2)
            java.lang.String r8 = ")"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            java.lang.AssertionError r7 = r9.fail(r7)
            throw r7
        L_0x0039:
            java.lang.Object r5 = r3.next()
            java.lang.Object r4 = r6.next()
            boolean r7 = dji.thirdparty.io.reactivex.internal.functions.ObjectHelper.equals(r4, r5)
            if (r7 != 0) goto L_0x007e
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Values at position "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r2)
            java.lang.String r8 = " differ; Expected: "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r8 = valueAndClass(r4)
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r8 = ", Actual: "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r8 = valueAndClass(r5)
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            java.lang.AssertionError r7 = r9.fail(r7)
            throw r7
        L_0x007e:
            int r2 = r2 + 1
            goto L_0x000b
        L_0x0081:
            if (r1 == 0) goto L_0x00a3
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Fever values received than expected ("
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r2)
            java.lang.String r8 = ")"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            java.lang.AssertionError r7 = r9.fail(r7)
            throw r7
        L_0x00a3:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.thirdparty.io.reactivex.observers.BaseTestConsumer.assertValueSequence(java.lang.Iterable):dji.thirdparty.io.reactivex.observers.BaseTestConsumer");
    }

    public final U assertTerminated() {
        if (this.done.getCount() != 0) {
            throw fail("Subscriber still running!");
        }
        long c = this.completions;
        if (c > 1) {
            throw fail("Terminated with multiple completions: " + c);
        }
        int s = this.errors.size();
        if (s > 1) {
            throw fail("Terminated with multiple errors: " + s);
        } else if (c == 0 || s == 0) {
            return this;
        } else {
            throw fail("Terminated with multiple completions and errors: " + c);
        }
    }

    public final U assertNotTerminated() {
        if (this.done.getCount() != 0) {
            return this;
        }
        throw fail("Subscriber terminated!");
    }

    public final boolean awaitTerminalEvent() {
        try {
            await();
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public final boolean awaitTerminalEvent(long duration, TimeUnit unit) {
        try {
            return await(duration, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public final U assertErrorMessage(String message) {
        int s = this.errors.size();
        if (s == 0) {
            throw fail("No errors");
        } else if (s == 1) {
            String errorMessage = this.errors.get(0).getMessage();
            if (ObjectHelper.equals(message, errorMessage)) {
                return this;
            }
            throw fail("Error message differs; Expected: " + message + ", Actual: " + errorMessage);
        } else {
            throw fail("Multiple errors");
        }
    }

    public final List<List<Object>> getEvents() {
        List<List<Object>> result = new ArrayList<>();
        result.add(values());
        result.add(errors());
        List<Object> completeList = new ArrayList<>();
        for (long i = 0; i < this.completions; i++) {
            completeList.add(Notification.createOnComplete());
        }
        result.add(completeList);
        return result;
    }

    public final U assertResult(T... values2) {
        return assertSubscribed().assertValues(values2).assertNoErrors().assertComplete();
    }

    public final U assertFailure(Class<? extends Throwable> error, T... values2) {
        return assertSubscribed().assertValues(values2).assertError(error).assertNotComplete();
    }

    public final U assertFailure(Predicate<Throwable> errorPredicate, T... values2) {
        return assertSubscribed().assertValues(values2).assertError(errorPredicate).assertNotComplete();
    }

    public final U assertFailureAndMessage(Class<? extends Throwable> error, String message, T... values2) {
        return assertSubscribed().assertValues(values2).assertError(error).assertErrorMessage(message).assertNotComplete();
    }

    public final U awaitDone(long time, TimeUnit unit) {
        try {
            if (!this.done.await(time, unit)) {
                dispose();
            }
            return this;
        } catch (InterruptedException ex) {
            dispose();
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    public final U assertEmpty() {
        return assertSubscribed().assertNoValues().assertNoErrors().assertNotComplete();
    }
}
