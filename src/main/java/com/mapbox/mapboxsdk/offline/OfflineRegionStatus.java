package com.mapbox.mapboxsdk.offline;

import android.support.annotation.Keep;

public class OfflineRegionStatus {
    private final long completedResourceCount;
    private final long completedResourceSize;
    private final long completedTileCount;
    private final long completedTileSize;
    private int downloadState;
    private final long requiredResourceCount;
    private final boolean requiredResourceCountIsPrecise;

    @Keep
    private OfflineRegionStatus(int downloadState2, long completedResourceCount2, long completedResourceSize2, long completedTileCount2, long completedTileSize2, long requiredResourceCount2, boolean requiredResourceCountIsPrecise2) {
        this.downloadState = downloadState2;
        this.completedResourceCount = completedResourceCount2;
        this.completedResourceSize = completedResourceSize2;
        this.completedTileCount = completedTileCount2;
        this.completedTileSize = completedTileSize2;
        this.requiredResourceCount = requiredResourceCount2;
        this.requiredResourceCountIsPrecise = requiredResourceCountIsPrecise2;
    }

    public boolean isComplete() {
        return this.completedResourceCount >= this.requiredResourceCount;
    }

    public int getDownloadState() {
        return this.downloadState;
    }

    public long getCompletedResourceCount() {
        return this.completedResourceCount;
    }

    public long getCompletedResourceSize() {
        return this.completedResourceSize;
    }

    public long getCompletedTileCount() {
        return this.completedTileCount;
    }

    public long getCompletedTileSize() {
        return this.completedTileSize;
    }

    public long getRequiredResourceCount() {
        return this.requiredResourceCount;
    }

    public boolean isRequiredResourceCountPrecise() {
        return this.requiredResourceCountIsPrecise;
    }
}
