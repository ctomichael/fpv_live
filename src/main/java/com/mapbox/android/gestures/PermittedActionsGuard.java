package com.mapbox.android.gestures;

import android.support.annotation.IntRange;

class PermittedActionsGuard {
    private static final int BITS_PER_PERMITTED_ACTION = 8;
    private static final int NO_ACTION_PERMITTED = 255;
    private static final int PERMITTED_ACTION_MASK = 255;

    PermittedActionsGuard() {
    }

    /* access modifiers changed from: package-private */
    public boolean isMissingActions(int action, @IntRange(from = 0) int eventPointerCount, @IntRange(from = 0) int internalPointerCount) {
        long permittedActions = updatePermittedActions(eventPointerCount, internalPointerCount);
        if (((long) action) == permittedActions) {
            return false;
        }
        while (permittedActions != 0) {
            if (((long) action) == (permittedActions & 255)) {
                return false;
            }
            permittedActions >>= 8;
        }
        return true;
    }

    private long updatePermittedActions(@IntRange(from = 0) int eventPointerCount, @IntRange(from = 0) int internalPointerCount) {
        long permittedActions;
        long permittedActions2;
        if (internalPointerCount == 0) {
            permittedActions2 = (0 << 8) + 0;
        } else if (Math.abs(eventPointerCount - internalPointerCount) > 1) {
            return 255;
        } else {
            if (eventPointerCount > internalPointerCount) {
                permittedActions2 = (0 << 8) + 5;
            } else if (eventPointerCount < internalPointerCount) {
                return 255;
            } else {
                if (eventPointerCount == 1) {
                    permittedActions = (0 << 8) + 1;
                } else {
                    permittedActions = (0 << 8) + 6;
                }
                permittedActions2 = (permittedActions << 8) + 2;
            }
        }
        return permittedActions2;
    }
}
