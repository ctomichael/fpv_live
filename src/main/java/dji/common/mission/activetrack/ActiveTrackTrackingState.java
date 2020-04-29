package dji.common.mission.activetrack;

import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Arrays;

@EXClassNullAway
public class ActiveTrackTrackingState {
    private final SubjectSensingState[] autoSensedSubjects;
    private final int completionPercentage;
    private final ActiveTrackCannotConfirmReason reason;
    private final ActiveTrackTargetState state;
    private final int targetIndex;
    private final RectF targetRect;
    private final ActiveTrackTargetType type;

    @Nullable
    public ActiveTrackTargetState getState() {
        return this.state;
    }

    @Nullable
    public ActiveTrackTargetType getType() {
        return this.type;
    }

    @Nullable
    public RectF getTargetRect() {
        return this.targetRect;
    }

    public int getTargetIndex() {
        return this.targetIndex;
    }

    public int getCompletionPercentage() {
        return this.completionPercentage;
    }

    @NonNull
    public ActiveTrackCannotConfirmReason getReason() {
        return this.reason;
    }

    @Nullable
    public SubjectSensingState[] getAutoSensedSubjects() {
        return this.autoSensedSubjects;
    }

    private ActiveTrackTrackingState(Builder builder) {
        this.state = builder.state;
        this.type = builder.type;
        this.targetRect = builder.targetRect;
        this.targetIndex = builder.targetIndex;
        this.completionPercentage = builder.completionPercentage;
        this.reason = builder.reason;
        this.autoSensedSubjects = builder.autoSensedSubjects;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3 = 0;
        if (this.state != null) {
            result = this.state.hashCode();
        } else {
            result = 0;
        }
        int i4 = result * 31;
        if (this.type != null) {
            i = this.type.hashCode();
        } else {
            i = 0;
        }
        int i5 = (i4 + i) * 31;
        if (this.targetRect != null) {
            i2 = this.targetRect.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (((((i5 + i2) * 31) + this.targetIndex) * 31) + this.completionPercentage) * 31;
        if (this.reason != null) {
            i3 = this.reason.hashCode();
        }
        return ((i6 + i3) * 31) + Arrays.hashCode(this.autoSensedSubjects);
    }

    public boolean equals(Object obj) {
        boolean z;
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.targetRect == null || ((ActiveTrackTrackingState) obj).targetRect == null) {
            if (this.targetRect == null || ((ActiveTrackTrackingState) obj).targetRect == null) {
                return false;
            }
        } else if (!this.targetRect.equals(((ActiveTrackTrackingState) obj).targetRect)) {
            return false;
        }
        if (this.state == ((ActiveTrackTrackingState) obj).state && this.type == ((ActiveTrackTrackingState) obj).type && this.targetIndex == ((ActiveTrackTrackingState) obj).targetIndex && this.completionPercentage == ((ActiveTrackTrackingState) obj).completionPercentage && this.reason == ((ActiveTrackTrackingState) obj).reason && isAutoSensedSubjectsEquals(((ActiveTrackTrackingState) obj).autoSensedSubjects)) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    private boolean isAutoSensedSubjectsEquals(Object o) {
        if (this.autoSensedSubjects == null && o == null) {
            return true;
        }
        if (this.autoSensedSubjects == null || o == null) {
            return false;
        }
        if (!this.autoSensedSubjects.getClass().isArray() || !o.getClass().isArray()) {
            return this.autoSensedSubjects.equals(o);
        }
        return arrayEquals(this.autoSensedSubjects, o);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean} */
    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean}
     arg types: [java.lang.Object, java.lang.Object]
     candidates:
      ClspMth{java.util.Arrays.equals(double[], double[]):boolean}
      ClspMth{java.util.Arrays.equals(int[], int[]):boolean}
      ClspMth{java.util.Arrays.equals(boolean[], boolean[]):boolean}
      ClspMth{java.util.Arrays.equals(byte[], byte[]):boolean}
      ClspMth{java.util.Arrays.equals(long[], long[]):boolean}
      ClspMth{java.util.Arrays.equals(float[], float[]):boolean}
      ClspMth{java.util.Arrays.equals(short[], short[]):boolean}
      ClspMth{java.util.Arrays.equals(char[], char[]):boolean}
      ClspMth{java.util.Arrays.equals(java.lang.Object[], java.lang.Object[]):boolean} */
    private boolean arrayEquals(Object a, Object b) {
        if (a.getClass() == int[].class) {
            return Arrays.equals((int[]) ((int[]) a), (int[]) ((int[]) b));
        }
        if (a.getClass() == boolean[].class) {
            return Arrays.equals((boolean[]) ((boolean[]) a), (boolean[]) ((boolean[]) b));
        }
        if (a.getClass() == byte[].class) {
            return Arrays.equals((byte[]) ((byte[]) a), (byte[]) ((byte[]) b));
        }
        if (a.getClass() == char[].class) {
            return Arrays.equals((char[]) ((char[]) a), (char[]) ((char[]) b));
        }
        if (a.getClass() == long[].class) {
            return Arrays.equals((long[]) ((long[]) a), (long[]) ((long[]) b));
        }
        if (a.getClass() == short[].class) {
            return Arrays.equals((short[]) ((short[]) a), (short[]) ((short[]) b));
        }
        if (a.getClass() == float[].class) {
            return Arrays.equals((float[]) ((float[]) a), (float[]) ((float[]) b));
        }
        if (a.getClass() == double[].class) {
            return Arrays.equals((double[]) ((double[]) a), (double[]) ((double[]) b));
        }
        try {
            return Arrays.equals((Object[]) ((Object[]) a), (Object[]) ((Object[]) b));
        } catch (Exception e) {
            return false;
        }
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public SubjectSensingState[] autoSensedSubjects;
        /* access modifiers changed from: private */
        public int completionPercentage;
        /* access modifiers changed from: private */
        public ActiveTrackCannotConfirmReason reason;
        /* access modifiers changed from: private */
        public ActiveTrackTargetState state;
        /* access modifiers changed from: private */
        public int targetIndex;
        /* access modifiers changed from: private */
        public RectF targetRect;
        /* access modifiers changed from: private */
        public ActiveTrackTargetType type;

        public Builder state(ActiveTrackTargetState state2) {
            this.state = state2;
            return this;
        }

        public Builder targetType(ActiveTrackTargetType type2) {
            this.type = type2;
            return this;
        }

        public Builder targetRect(RectF targetRect2) {
            this.targetRect = targetRect2;
            return this;
        }

        public Builder index(int targetIndex2) {
            this.targetIndex = targetIndex2;
            return this;
        }

        public Builder completionPercentage(int completionPercentage2) {
            this.completionPercentage = completionPercentage2;
            return this;
        }

        public Builder reason(ActiveTrackCannotConfirmReason reason2) {
            this.reason = reason2;
            return this;
        }

        public Builder autoSensedSubjects(SubjectSensingState[] autoSensedSubjects2) {
            this.autoSensedSubjects = autoSensedSubjects2;
            return this;
        }

        public ActiveTrackTrackingState build() {
            return new ActiveTrackTrackingState(this);
        }
    }
}
