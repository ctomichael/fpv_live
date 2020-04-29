package dji.logic.vision;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.model.P3.DataEyeGetPushDrawState;
import dji.midware.data.model.P3.DataEyeGetPushTrajectory;
import dji.midware.util.DJIEventBusUtil;
import java.util.TreeSet;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJITrajectoryHelper {
    private final TreeSet<TrajectoryInfo> mTrajectoryInfoSet;

    public enum TrajectoryEvent {
        TRAJECTORY_FINISHED
    }

    public TreeSet<TrajectoryInfo> getTrajectoryInfoSet() {
        return this.mTrajectoryInfoSet;
    }

    public static DJITrajectoryHelper getInstance() {
        return SingletonHolder.mInstance;
    }

    public void updateTrajectory(DataEyeGetPushTrajectory data) {
        if (data != null && data.getRecData() != null) {
            synchronized (this.mTrajectoryInfoSet) {
                int sessionId = data.getSessionId();
                TrajectoryInfo ti = null;
                if (!this.mTrajectoryInfoSet.isEmpty()) {
                    ti = this.mTrajectoryInfoSet.first();
                }
                if (!(ti == null || sessionId == ti.mSessionId)) {
                    this.mTrajectoryInfoSet.clear();
                }
                TrajectoryInfo tmp = new TrajectoryInfo(data.getFragmentIndex(), data.isLastFragment(), data.getSessionId(), data.getCount(), data.getPolynomialTrajectory());
                this.mTrajectoryInfoSet.add(tmp);
                if (tmp.mbLastFragment && DataEyeGetPushDrawState.getInstance().getState() == DataEyeGetPushDrawState.DrawState.READY_TO_GO) {
                    EventBus.getDefault().post(TrajectoryEvent.TRAJECTORY_FINISHED);
                }
            }
        }
    }

    public void checkNotifyDataFinished() {
        synchronized (this.mTrajectoryInfoSet) {
            if (!this.mTrajectoryInfoSet.isEmpty() && this.mTrajectoryInfoSet.last().mbLastFragment) {
                EventBus.getDefault().post(TrajectoryEvent.TRAJECTORY_FINISHED);
            }
        }
    }

    public void clearAllDatas() {
        synchronized (this.mTrajectoryInfoSet) {
            if (!this.mTrajectoryInfoSet.isEmpty()) {
                this.mTrajectoryInfoSet.clear();
                EventBus.getDefault().post(TrajectoryEvent.TRAJECTORY_FINISHED);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (DataCameraEvent.ConnectLose == event) {
            clearAllDatas();
        }
    }

    private DJITrajectoryHelper() {
        this.mTrajectoryInfoSet = new TreeSet<>();
        DJIEventBusUtil.register(this);
    }

    private static final class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJITrajectoryHelper mInstance = new DJITrajectoryHelper();

        private SingletonHolder() {
        }
    }

    public static final class TrajectoryInfo implements Comparable<TrajectoryInfo> {
        public int mCount = 0;
        public int mFragmentIndex = 0;
        public DataEyeGetPushTrajectory.PolynomialTrajectory[] mPolynomialTrajectories = null;
        public int mSessionId = 0;
        public boolean mbLastFragment = false;

        public TrajectoryInfo(int index, boolean last, int sessionId, int count, DataEyeGetPushTrajectory.PolynomialTrajectory[] polys) {
            this.mFragmentIndex = index;
            this.mbLastFragment = last;
            this.mSessionId = sessionId;
            this.mCount = count;
            this.mPolynomialTrajectories = polys;
        }

        public int hashCode() {
            int result = 17 + this.mSessionId + 527;
            return result + (result * 31) + this.mFragmentIndex;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(32);
            sb.append("sessionId-").append(this.mSessionId).append(";index-").append(this.mFragmentIndex).append(";count-").append(this.mCount).append(";last-").append(this.mbLastFragment);
            return sb.toString();
        }

        public boolean equals(Object o) {
            boolean ret = super.equals(o);
            if (ret || !(o instanceof TrajectoryInfo)) {
                return ret;
            }
            TrajectoryInfo tmp = (TrajectoryInfo) o;
            return this.mSessionId == tmp.mSessionId && this.mFragmentIndex == tmp.mFragmentIndex;
        }

        public int compareTo(TrajectoryInfo another) {
            return this.mFragmentIndex - another.mFragmentIndex;
        }
    }
}
