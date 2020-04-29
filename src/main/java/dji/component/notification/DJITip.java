package dji.component.notification;

import android.support.annotation.NonNull;
import android.view.View;
import com.mapbox.mapboxsdk.style.layers.Property;
import dji.utils.function.Consumer;
import dji.utils.function.Supplier;

public class DJITip {
    public static final int DJI_TIP_ID_DATABASE = 20;
    public static final int DJI_TIP_ID_DJICARE = 30;
    public static final int DJI_TIP_ID_FIRMWARE = 10;
    /* access modifiers changed from: private */
    public static final Consumer<?> EMPTY_CONSUMER = DJITip$$Lambda$0.$instance;
    /* access modifiers changed from: private */
    public static final Runnable EMPTY_RUNNABLE = DJITip$$Lambda$1.$instance;
    Priority initialPriority;
    ITipActions tipActions;
    int tipId;
    View tipView;

    public interface ITipActions {
        boolean isPersistent();

        void onAdded(IPriorityBinder iPriorityBinder);

        void onClick();

        void onHide(boolean z);

        void onShow(boolean z);

        void onSlideUp(boolean z);
    }

    public interface IPriorityBinder {
        void dispose();

        @NonNull
        Priority getPriority();

        boolean isDisposed();

        void setDisposeAction(Runnable runnable);

        void setPriority(@NonNull Priority priority);
    }

    static final /* synthetic */ void lambda$static$0$DJITip(Object val) {
    }

    static final /* synthetic */ void lambda$static$1$DJITip() {
    }

    public int getTipId() {
        return this.tipId;
    }

    public View getTipView() {
        return this.tipView;
    }

    public Priority getInitialPriority() {
        return this.initialPriority;
    }

    public ITipActions getTipActions() {
        return this.tipActions;
    }

    private DJITip(final Builder builder) {
        this.tipId = builder.tipId;
        this.tipView = builder.tipView;
        this.tipActions = builder.tipActions;
        this.initialPriority = builder.initialPriority;
        if (this.tipActions == null) {
            this.tipActions = new ITipActions() {
                /* class dji.component.notification.DJITip.AnonymousClass1 */

                public void onClick() {
                    builder.onClick.run();
                }

                public void onHide(boolean fromUser) {
                    builder.onHide.accept(Boolean.valueOf(fromUser));
                }

                public void onShow(boolean selfPush) {
                    builder.onShow.accept(Boolean.valueOf(selfPush));
                }

                public void onSlideUp(boolean doesHide) {
                    builder.onSlideUp.accept(Boolean.valueOf(doesHide));
                }

                public boolean isPersistent() {
                    return ((Boolean) builder.isPersistentSupplier.get()).booleanValue();
                }

                public void onAdded(IPriorityBinder binder) {
                    builder.onAdded.accept(binder);
                }
            };
        }
    }

    public String toString() {
        return "DJITip{tipId=" + this.tipId + ", tipView=" + this.tipView + ", tipActions=" + this.tipActions + '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(DJITip copy) {
        Builder builder = new Builder();
        int unused = builder.tipId = copy.getTipId();
        View unused2 = builder.tipView = copy.getTipView();
        ITipActions unused3 = builder.tipActions = copy.getTipActions();
        return builder;
    }

    public enum Priority {
        GONE("gone"),
        VISIBLE(Property.VISIBLE),
        DJI_CARE_HINT("dji_care"),
        DB_UPDATE_NORMAL("db_update_normal"),
        FW_CONSISTENT("fw_consistent"),
        FW_UPDATE_NORMAL("fw_update_normal"),
        DB_UPDATE_FORCE("db_update_force"),
        FW_UPDATE_FORCE("fw_update_force"),
        DB_UPDATE_SUCCESS("db_update_success"),
        DB_UPDATE_FAILURE("db_update_failure"),
        FW_UPDATE_SUCCESS("fw_update_success"),
        FW_UPDATE_FAILURE("fw_update_failure"),
        DB_UPDATING("db_updating"),
        FW_UPDATING("fw_updating");
        
        String description;

        private Priority(String description2) {
            this.description = description2;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public Priority initialPriority;
        /* access modifiers changed from: private */
        public Supplier<Boolean> isPersistentSupplier;
        /* access modifiers changed from: private */
        public Consumer<IPriorityBinder> onAdded;
        /* access modifiers changed from: private */
        public Runnable onClick;
        /* access modifiers changed from: private */
        public Consumer<Boolean> onHide;
        /* access modifiers changed from: private */
        public Consumer<Boolean> onShow;
        /* access modifiers changed from: private */
        public Consumer<Boolean> onSlideUp;
        /* access modifiers changed from: private */
        public ITipActions tipActions;
        /* access modifiers changed from: private */
        public int tipId;
        /* access modifiers changed from: private */
        public View tipView;

        static final /* synthetic */ Boolean lambda$new$0$DJITip$Builder() {
            return false;
        }

        private Builder() {
            this.onClick = DJITip.EMPTY_RUNNABLE;
            this.onHide = DJITip.EMPTY_CONSUMER;
            this.onShow = DJITip.EMPTY_CONSUMER;
            this.onSlideUp = DJITip.EMPTY_CONSUMER;
            this.isPersistentSupplier = DJITip$Builder$$Lambda$0.$instance;
            this.onAdded = DJITip.EMPTY_CONSUMER;
            this.initialPriority = Priority.VISIBLE;
        }

        public Builder tipId(int val) {
            this.tipId = val;
            return this;
        }

        public Builder tipView(View val) {
            this.tipView = val;
            return this;
        }

        public Builder onClick(@NonNull Runnable val) {
            this.onClick = val;
            return this;
        }

        public Builder onHide(@NonNull Consumer<Boolean> val) {
            this.onHide = val;
            return this;
        }

        public Builder onShow(@NonNull Consumer<Boolean> val) {
            this.onShow = val;
            return this;
        }

        public Builder onSlideUp(@NonNull Consumer<Boolean> val) {
            this.onSlideUp = val;
            return this;
        }

        public Builder isPersistentSupplier(@NonNull Supplier<Boolean> val) {
            this.isPersistentSupplier = val;
            return this;
        }

        public Builder onAdded(@NonNull Consumer<IPriorityBinder> val) {
            this.onAdded = val;
            return this;
        }

        public Builder initialPriority(@NonNull Priority val) {
            this.initialPriority = val;
            return this;
        }

        public Builder tipActions(@NonNull ITipActions val) {
            this.tipActions = val;
            return this;
        }

        public DJITip build() {
            return new DJITip(this);
        }
    }
}
