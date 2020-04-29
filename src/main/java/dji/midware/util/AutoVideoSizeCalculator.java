package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;

@EXClassNullAway
public class AutoVideoSizeCalculator {
    private static final String TAG = "AutoVideoSizeCalculator";
    public static String testOutput = "";
    private AutoVideoSizeListener listener;
    private Options options = new Options();
    private int[] showHeightLimit = new int[2];
    private int[] showWidthLimit = new int[2];
    private int videoHeight;
    private int videoRelHeight;
    private int videoRelWidth;
    private int videoWidth;

    public interface AutoVideoSizeListener {
        void onVideoSizeChanged(int i, int i2, int i3, int i4);
    }

    public static class Options {
        public static final float ratio16X10 = 1.6f;
        public static final float ratio16X9 = 1.7777778f;
        public static final float ratio17X9 = 1.8888888f;
        public static final float ratio18x9 = 2.0f;
        public static final float ratio244X100 = 2.44f;
        public static final float ratio3X2 = 1.5f;
        public static final float ratio4X3 = 1.3333334f;
        public static final float ratio5x4 = 1.25f;
        /* access modifiers changed from: private */
        public boolean isRotated = false;
        /* access modifiers changed from: private */
        public int screenHeight = 0;
        /* access modifiers changed from: private */
        public ScreenType screenType = ScreenType.Ratio16X9;
        /* access modifiers changed from: private */
        public int screenWidth = 0;
        /* access modifiers changed from: private */
        public VideoType videoType = VideoType.Ratio16X9;

        public enum ScreenType {
            Ratio16X9(1.7777778f),
            Ratio16X10(1.6f),
            Ratio4X3(1.3333334f),
            Ratio18x9(2.0f);
            
            private float ratio;

            private ScreenType(float ratio2) {
                this.ratio = ratio2;
            }

            public float getRatio() {
                return this.ratio;
            }
        }

        public enum VideoType {
            Ratio16X9(1.7777778f),
            Ratio3X2(1.5f),
            Ratio4X3(1.3333334f),
            Ratio4X3_IN16X9(1.3333334f, 1.7777778f),
            Ratio3X2_IN16X9(1.5f, 1.7777778f),
            Ratio17X9(1.8888888f),
            Ratio17X9_IN16x9(1.8888888f, 1.7777778f),
            Ratio5x4(1.25f),
            Ratio5x4_IN16x9(1.25f, 1.7777778f),
            Ratio244X100(2.44f);
            
            private float ratio;
            private float relRatio;

            private VideoType(float ratio2) {
                this.ratio = ratio2;
                this.relRatio = ratio2;
            }

            private VideoType(float relRatio2, float ratio2) {
                this.ratio = ratio2;
                this.relRatio = relRatio2;
            }

            public float getRatio() {
                return this.ratio;
            }

            public float getRelRatio() {
                return this.relRatio;
            }

            public boolean isNormalType() {
                return this.ratio == this.relRatio;
            }
        }

        public void setScreenTypeByScreenSize(int screenWidth2, int screenHeight2) {
            this.screenWidth = screenWidth2;
            this.screenHeight = screenHeight2;
            float screenRatio = (((float) screenWidth2) * 1.0f) / ((float) screenHeight2);
            ScreenType[] screenTypes = ScreenType.values();
            float minDiff = Float.MAX_VALUE;
            ScreenType rst = ScreenType.Ratio16X9;
            for (ScreenType screenType2 : screenTypes) {
                float curDiff = Math.abs(screenType2.getRatio() - screenRatio);
                if (curDiff < minDiff) {
                    rst = screenType2;
                    minDiff = curDiff;
                }
            }
            this.screenType = rst;
        }

        public ScreenType getScreenType() {
            return this.screenType;
        }

        public void setVideoType(VideoType videoType2) {
            this.videoType = videoType2;
        }

        public void setVideoTypeByVideoSize(int videoWidth, int videoHeight, int cameraIndex) {
            this.videoType = AutoVideoSizeCalculator.findMathRatioType((((float) videoWidth) * 1.0f) / ((float) videoHeight));
        }

        public VideoType getVideoType() {
            return this.videoType;
        }

        public void setIsRotated(boolean isRotated2) {
            this.isRotated = isRotated2;
        }

        public boolean getIsRotated() {
            return this.isRotated;
        }

        public int getScreenWidth() {
            return this.screenWidth;
        }

        public int getScreenHeight() {
            return this.screenHeight;
        }
    }

    public Options getOptions() {
        return this.options;
    }

    public void setListener(AutoVideoSizeListener listener2) {
        this.listener = listener2;
    }

    public void notifyCalc() {
        boolean isChanged = calcSize();
        if (this.listener != null && isChanged) {
            testOutput = "videoWidth=" + this.videoWidth + ", videoHeight=" + this.videoHeight + ", videoType=" + this.options.videoType + ", screenType=" + this.options.screenType;
            DJILog.e("calc", testOutput, new Object[0]);
            int wd = (int) (((float) (this.options.screenWidth - this.videoRelWidth)) / 2.0f);
            this.showWidthLimit[0] = wd;
            this.showWidthLimit[1] = this.videoRelWidth + wd;
            int hd = (int) (((float) (this.options.screenHeight - this.videoRelHeight)) / 2.0f);
            this.showHeightLimit[0] = hd;
            this.showHeightLimit[1] = this.videoRelHeight + hd;
            this.listener.onVideoSizeChanged(this.videoWidth, this.videoHeight, this.videoRelWidth, this.videoRelHeight);
        }
    }

    public boolean isVideoFullScreen() {
        return this.options.screenWidth <= this.videoWidth && this.options.screenHeight <= this.videoHeight;
    }

    private boolean calcSize() {
        int tempWidth;
        int tempHeight;
        int tempRelWidth;
        int tempRelHeight;
        boolean isChanged = false;
        int result = calcTheSideOfMatchScreen();
        if (!(this.options == null || this.options.videoType == null || this.options.screenType == null)) {
            if (this.options.isRotated) {
                if (result == 1) {
                    tempWidth = (int) (((float) this.options.screenHeight) / this.options.videoType.getRelRatio());
                    tempHeight = (int) ((((float) this.options.screenHeight) / this.options.videoType.getRelRatio()) * this.options.videoType.getRatio());
                    tempRelWidth = tempWidth;
                    tempRelHeight = this.options.screenHeight;
                } else if (result == 2) {
                    tempHeight = this.options.screenHeight;
                    tempWidth = (int) (((float) tempHeight) / this.options.videoType.getRatio());
                    tempRelHeight = tempHeight;
                    tempRelWidth = (int) (((float) tempHeight) / this.options.videoType.getRelRatio());
                } else {
                    tempHeight = this.options.screenHeight;
                    tempWidth = (int) (((float) tempHeight) / this.options.videoType.getRatio());
                    tempRelWidth = tempWidth;
                    tempRelHeight = (int) (((float) tempWidth) * this.options.videoType.getRelRatio());
                }
            } else if (result == 1) {
                tempHeight = this.options.screenHeight;
                tempWidth = (int) (((float) tempHeight) * this.options.videoType.getRatio());
                tempRelHeight = tempHeight;
                tempRelWidth = (int) (((float) tempRelHeight) * this.options.videoType.getRelRatio());
            } else if (result == 2) {
                tempWidth = this.options.screenWidth;
                tempHeight = (int) (((float) tempWidth) / this.options.videoType.getRatio());
                tempRelWidth = tempWidth;
                tempRelHeight = (int) (((float) tempWidth) / this.options.videoType.getRelRatio());
            } else {
                tempWidth = this.options.screenWidth;
                tempHeight = (int) (((float) tempWidth) / this.options.videoType.getRatio());
                tempRelWidth = (int) ((((float) tempWidth) * this.options.videoType.getRelRatio()) / this.options.videoType.getRatio());
                tempRelHeight = (int) (((float) tempRelWidth) / this.options.videoType.getRelRatio());
            }
            if (!(tempHeight == this.videoHeight && tempWidth == this.videoWidth && tempRelHeight == this.videoRelHeight && tempRelWidth == this.videoRelWidth)) {
                isChanged = true;
            }
            if (isChanged) {
                this.videoHeight = tempHeight;
                this.videoWidth = tempWidth;
                this.videoRelHeight = tempRelHeight;
                this.videoRelWidth = tempRelWidth;
            }
        }
        return isChanged;
    }

    private int calcTheSideOfMatchScreen() {
        int result;
        if (this.options == null || this.options.videoType == null || this.options.screenType == null) {
            return 0;
        }
        if (this.options.videoType.isNormalType()) {
            result = this.options.videoType.getRelRatio() < this.options.screenType.getRatio() ? 1 : 0;
        } else {
            float vratio = this.options.videoType.getRelRatio();
            float sratio = this.options.screenType.getRatio();
            result = vratio == sratio ? 1 : vratio > sratio ? 2 : 0;
        }
        return result;
    }

    public int getVideoWidth() {
        return this.videoWidth;
    }

    public int getVideoHeight() {
        return this.videoHeight;
    }

    public int getRelVideoWidth() {
        return this.videoRelWidth;
    }

    public int getRelVideoHeight() {
        return this.videoRelHeight;
    }

    public int[] getShowWidthLimit() {
        return this.showWidthLimit;
    }

    public int[] getShowHeightLimit() {
        return this.showHeightLimit;
    }

    public static Options.VideoType findMathRatioType(float videoratio) {
        Options.VideoType type = null;
        float closeValue = Float.MAX_VALUE;
        Options.VideoType[] allTypes = Options.VideoType.values();
        for (Options.VideoType t : allTypes) {
            if (Math.abs(videoratio - t.getRelRatio()) < closeValue) {
                closeValue = Math.abs(videoratio - t.getRelRatio());
                type = t;
            }
        }
        if (type == null) {
            return Options.VideoType.Ratio16X9;
        }
        return type;
    }

    private static boolean isXT2Camera(int cameraIndex) {
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC1705 == DataCameraGetPushStateInfo.getInstance().getCameraType(cameraIndex);
    }

    private static boolean isXTCamera(int cameraIndex) {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(cameraIndex);
        return cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau336 || cameraType == DataCameraGetPushStateInfo.CameraType.DJICameraTypeTau640;
    }

    private static boolean isNotRelResolutionCamera(int cameraIndex) {
        DataCameraGetPushStateInfo.CameraType cameraType = DataCameraGetPushStateInfo.getInstance().getCameraType(cameraIndex);
        return DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6510 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6520 == cameraType || DataCameraGetPushStateInfo.CameraType.DJICameraTypeFC6540 == cameraType;
    }

    private static boolean isRelativeResolutionProductOrCamera(int cameraIndex) {
        ProductType productType = DJIProductManager.getInstance().getType();
        if (productType == ProductType.KumquatS || productType == ProductType.Pomato || productType == ProductType.PomatoSDR || productType == ProductType.PomatoRTK || productType == ProductType.Orange2 || productType == ProductType.Potato || productType == ProductType.Mammoth || productType == ProductType.WM230 || productType == ProductType.WM240 || productType == ProductType.WM160 || productType == ProductType.M200 || productType == ProductType.M210 || productType == ProductType.M210RTK || isNotRelResolutionCamera(cameraIndex)) {
            return true;
        }
        return false;
    }
}
