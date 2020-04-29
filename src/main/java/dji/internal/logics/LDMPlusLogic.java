package dji.internal.logics;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.encryption.util.LDMPlusEngine;
import dji.sdksharedlib.util.DJISDKCacheThreadManager;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.Subscription;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class LDMPlusLogic {
    private static final String TAG = "LDMPlusLogic";
    private boolean isLDMPlus;
    private boolean isLicenseValid;
    private String licenseOwner;
    private final List<LDMPlusListener> listeners;
    private String phoneNum;
    private Observable<Long> timer;
    private Subscription timmerSubcription;

    public interface LDMPlusListener {
        void onUpdate(String str, boolean z);
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final LDMPlusLogic INSTANCE = new LDMPlusLogic();

        private LazyHolder() {
        }
    }

    private LDMPlusLogic() {
        this.licenseOwner = "";
        this.phoneNum = "";
        this.listeners = new CopyOnWriteArrayList();
        this.timer = Observable.timer(1, TimeUnit.SECONDS).observeOn(Schedulers.computation()).repeat();
    }

    public static LDMPlusLogic getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void init() {
        startTimer();
    }

    public void addListener(@NonNull LDMPlusListener listener) {
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    public boolean removeListener(@NonNull LDMPlusListener listener) {
        return this.listeners.remove(listener);
    }

    private void removeAllListeners() {
        if (!this.listeners.isEmpty()) {
            this.listeners.clear();
        }
    }

    public void destroy() {
        removeAllListeners();
        stopTimer();
    }

    public boolean isLDMPlus() {
        return this.isLDMPlus;
    }

    public String getLicenseOwner() {
        return this.licenseOwner;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }

    private void startTimer() {
        this.timmerSubcription = this.timer.subscribe(new LDMPlusLogic$$Lambda$0(this));
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$startTimer$1$LDMPlusLogic(Long aLong) {
        if (updateLicense() && !this.listeners.isEmpty()) {
            for (LDMPlusListener listener : this.listeners) {
                if (listener != null) {
                    DJISDKCacheThreadManager.invoke(new LDMPlusLogic$$Lambda$1(this, listener), false);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final /* synthetic */ void lambda$null$0$LDMPlusLogic(LDMPlusListener listener) {
        if (listener != null) {
            listener.onUpdate(this.licenseOwner, this.isLDMPlus);
        }
    }

    private void stopTimer() {
        if (this.timmerSubcription != null && !this.timmerSubcription.isUnsubscribed()) {
            this.timmerSubcription.unsubscribe();
        }
    }

    private boolean updateLicense() {
        boolean updated = false;
        boolean isLicenseValid2 = LDMPlusEngine.getInstance().isLicenseValid();
        if (isLicenseValid2 != this.isLicenseValid) {
            this.isLicenseValid = isLicenseValid2;
            updated = true;
        }
        boolean isLDMPlus2 = LDMPlusEngine.getInstance().isLDMPlusEnabled();
        if (isLDMPlus2 != this.isLDMPlus) {
            this.isLDMPlus = isLDMPlus2;
            updated = true;
        }
        String licenseOwner2 = LDMPlusEngine.getInstance().getLicenseOwner();
        if (!this.licenseOwner.equals(licenseOwner2)) {
            this.licenseOwner = licenseOwner2;
            updated = true;
        }
        String phoneNum2 = LDMPlusEngine.getInstance().getPhoneNum();
        if (this.phoneNum.equals(phoneNum2)) {
            return updated;
        }
        this.phoneNum = phoneNum2;
        return true;
    }
}
