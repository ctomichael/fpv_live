package dji.thirdparty.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.view.animation.Animation;

public class BitmapDisplayConfig {
    private Animation animation;
    private int animationType;
    private int bitmapHeight;
    private int bitmapWidth;
    private Bitmap loadfailBitmap;
    private Bitmap loadingBitmap;

    public int getBitmapWidth() {
        return this.bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth2) {
        this.bitmapWidth = bitmapWidth2;
    }

    public int getBitmapHeight() {
        return this.bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight2) {
        this.bitmapHeight = bitmapHeight2;
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public void setAnimation(Animation animation2) {
        this.animation = animation2;
    }

    public int getAnimationType() {
        return this.animationType;
    }

    public void setAnimationType(int animationType2) {
        this.animationType = animationType2;
    }

    public Bitmap getLoadingBitmap() {
        return this.loadingBitmap;
    }

    public void setLoadingBitmap(Bitmap loadingBitmap2) {
        this.loadingBitmap = loadingBitmap2;
    }

    public Bitmap getLoadfailBitmap() {
        return this.loadfailBitmap;
    }

    public void setLoadfailBitmap(Bitmap loadfailBitmap2) {
        this.loadfailBitmap = loadfailBitmap2;
    }

    public class AnimationType {
        public static final int fadeIn = 1;
        public static final int userDefined = 0;

        public AnimationType() {
        }
    }
}
