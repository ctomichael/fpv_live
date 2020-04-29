package com.mapbox.android.gestures;

import android.util.Pair;

public class PointerDistancePair extends Pair<Integer, Integer> {
    public PointerDistancePair(Integer first, Integer second) {
        super(first, second);
    }

    public boolean equals(Object o) {
        if (o instanceof PointerDistancePair) {
            PointerDistancePair otherPair = (PointerDistancePair) o;
            if ((!((Integer) this.first).equals(otherPair.first) || !((Integer) this.second).equals(otherPair.second)) && (!((Integer) this.first).equals(otherPair.second) || !((Integer) this.second).equals(otherPair.first))) {
                return false;
            }
            return true;
        }
        return false;
    }
}
