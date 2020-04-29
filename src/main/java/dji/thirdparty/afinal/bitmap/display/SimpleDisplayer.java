package dji.thirdparty.afinal.bitmap.display;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import dji.thirdparty.afinal.bitmap.core.BitmapDisplayConfig;

public class SimpleDisplayer implements Displayer {
    public void loadCompletedisplay(View imageView, Bitmap bitmap, BitmapDisplayConfig config) {
        switch (config.getAnimationType()) {
            case 0:
                animationDisplay(imageView, bitmap, config.getAnimation());
                return;
            case 1:
                fadeInDisplay(imageView, bitmap);
                return;
            default:
                return;
        }
    }

    public void loadFailDisplay(View imageView, Bitmap bitmap) {
        if (imageView instanceof ImageView) {
            ((ImageView) imageView).setImageBitmap(bitmap);
        } else {
            imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
    }

    private void fadeInDisplay(View imageView, Bitmap bitmap) {
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(0), new BitmapDrawable(imageView.getResources(), bitmap)});
        if (imageView instanceof ImageView) {
            ((ImageView) imageView).setImageDrawable(td);
        } else {
            imageView.setBackgroundDrawable(td);
        }
        td.startTransition(300);
    }

    private void animationDisplay(View imageView, Bitmap bitmap, Animation animation) {
        animation.setStartTime(AnimationUtils.currentAnimationTimeMillis());
        if (imageView instanceof ImageView) {
            ((ImageView) imageView).setImageBitmap(bitmap);
        } else {
            imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
        }
        imageView.startAnimation(animation);
    }
}
