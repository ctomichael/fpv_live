package dji.thirdparty.rx.subscriptions;

import dji.thirdparty.rx.Subscription;
import java.util.concurrent.atomic.AtomicReference;

public final class SerialSubscription implements Subscription {
    final AtomicReference<State> state = new AtomicReference<>(new State(false, Subscriptions.empty()));

    private static final class State {
        final boolean isUnsubscribed;
        final Subscription subscription;

        State(boolean u, Subscription s) {
            this.isUnsubscribed = u;
            this.subscription = s;
        }

        /* access modifiers changed from: package-private */
        public State unsubscribe() {
            return new State(true, this.subscription);
        }

        /* access modifiers changed from: package-private */
        public State set(Subscription s) {
            return new State(this.isUnsubscribed, s);
        }
    }

    public boolean isUnsubscribed() {
        return this.state.get().isUnsubscribed;
    }

    public void unsubscribe() {
        State oldState;
        AtomicReference<State> localState = this.state;
        do {
            oldState = localState.get();
            if (oldState.isUnsubscribed) {
                return;
            }
        } while (!localState.compareAndSet(oldState, oldState.unsubscribe()));
        oldState.subscription.unsubscribe();
    }

    public void set(Subscription s) {
        State oldState;
        if (s == null) {
            throw new IllegalArgumentException("Subscription can not be null");
        }
        AtomicReference<State> localState = this.state;
        do {
            oldState = localState.get();
            if (oldState.isUnsubscribed) {
                s.unsubscribe();
                return;
            }
        } while (!localState.compareAndSet(oldState, oldState.set(s)));
        oldState.subscription.unsubscribe();
    }

    public Subscription get() {
        return this.state.get().subscription;
    }
}
