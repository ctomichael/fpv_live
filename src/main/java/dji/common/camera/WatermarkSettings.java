package dji.common.camera;

public class WatermarkSettings {
    private boolean enabledForLiveView;
    private boolean enabledForPhotos;
    private boolean enabledForVideos;

    public WatermarkSettings(boolean enabledForVideos2, boolean enabledForPhotos2) {
        this.enabledForVideos = enabledForVideos2;
        this.enabledForPhotos = enabledForPhotos2;
    }

    public WatermarkSettings(boolean enabledForVideos2, boolean enabledForPhotos2, boolean enabledForLiveView2) {
        this.enabledForVideos = enabledForVideos2;
        this.enabledForPhotos = enabledForPhotos2;
        this.enabledForLiveView = enabledForLiveView2;
    }

    public boolean isEnabledForVideos() {
        return this.enabledForVideos;
    }

    public boolean isEnabledForPhotos() {
        return this.enabledForPhotos;
    }

    public boolean isEnabledForLiveView() {
        return this.enabledForLiveView;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WatermarkSettings that = (WatermarkSettings) o;
        if (isEnabledForVideos() != that.isEnabledForVideos() || isEnabledForPhotos() != that.isEnabledForPhotos()) {
            return false;
        }
        if (isEnabledForLiveView() != that.isEnabledForLiveView()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 1;
        if (isEnabledForVideos()) {
            result = 1;
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (isEnabledForPhotos()) {
            i = 1;
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (!isEnabledForLiveView()) {
            i2 = 0;
        }
        return i4 + i2;
    }
}
