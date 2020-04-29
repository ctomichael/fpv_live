package com.dji.component.fpv.base.filter;

import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MedianFilter<T> {
    private final Comparator<T> mComparator;
    private ArrayList<T> mData;
    private int mInIndex;
    private final int mWindow;

    public MedianFilter(int windowSize, Comparator<T> comparator) {
        this.mInIndex = 0;
        this.mData = new ArrayList<>();
        if (windowSize < 7 || windowSize > 1000) {
            this.mWindow = 7;
        } else {
            this.mWindow = windowSize;
        }
        this.mComparator = comparator;
    }

    public MedianFilter(@NonNull Comparator<T> comparator) {
        this.mInIndex = 0;
        this.mData = new ArrayList<>();
        this.mWindow = 7;
        this.mComparator = comparator;
    }

    @NonNull
    public T process(@NonNull T inputData) {
        T outputData = inputData;
        if (this.mData.size() > this.mWindow) {
            this.mInIndex++;
            this.mInIndex %= this.mWindow;
            this.mData.set(this.mInIndex, inputData);
            Collections.sort(this.mData, this.mComparator);
            return this.mData.get((this.mWindow - 1) / 2);
        }
        this.mData.add(inputData);
        return outputData;
    }

    public void clear() {
        this.mData.clear();
    }
}
