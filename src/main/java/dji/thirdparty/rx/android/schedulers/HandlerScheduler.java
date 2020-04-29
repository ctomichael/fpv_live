package dji.thirdparty.rx.android.schedulers;

import android.os.Handler;
import dji.thirdparty.rx.Scheduler;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.android.plugins.RxAndroidPlugins;
import dji.thirdparty.rx.functions.Action0;
import dji.thirdparty.rx.internal.schedulers.ScheduledAction;
import dji.thirdparty.rx.subscriptions.CompositeSubscription;
import dji.thirdparty.rx.subscriptions.Subscriptions;
import java.util.concurrent.TimeUnit;

public final class HandlerScheduler extends Scheduler {
    private final Handler handler;

    public static HandlerScheduler from(Handler handler2) {
        if (handler2 != null) {
            return new HandlerScheduler(handler2);
        }
        throw new NullPointerException("handler == null");
    }

    HandlerScheduler(Handler handler2) {
        this.handler = handler2;
    }

    public Scheduler.Worker createWorker() {
        return new HandlerWorker(this.handler);
    }

    static class HandlerWorker extends Scheduler.Worker {
        private final CompositeSubscription compositeSubscription = new CompositeSubscription();
        /* access modifiers changed from: private */
        public final Handler handler;

        HandlerWorker(Handler handler2) {
            this.handler = handler2;
        }

        public void unsubscribe() {
            this.compositeSubscription.unsubscribe();
        }

        public boolean isUnsubscribed() {
            return this.compositeSubscription.isUnsubscribed();
        }

        public Subscription schedule(Action0 action, long delayTime, TimeUnit unit) {
            if (this.compositeSubscription.isUnsubscribed()) {
                return Subscriptions.unsubscribed();
            }
            final ScheduledAction scheduledAction = new ScheduledAction(RxAndroidPlugins.getInstance().getSchedulersHook().onSchedule(action));
            scheduledAction.addParent(this.compositeSubscription);
            this.compositeSubscription.add(scheduledAction);
            this.handler.postDelayed(scheduledAction, unit.toMillis(delayTime));
            scheduledAction.add(Subscriptions.create(new Action0() {
                /* class dji.thirdparty.rx.android.schedulers.HandlerScheduler.HandlerWorker.AnonymousClass1 */

                public void call() {
                    HandlerWorker.this.handler.removeCallbacks(scheduledAction);
                }
            }));
            return scheduledAction;
        }

        public Subscription schedule(Action0 action) {
            return schedule(action, 0, TimeUnit.MILLISECONDS);
        }
    }
}
