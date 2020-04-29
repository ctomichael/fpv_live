package dji.publics.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DJIViewUtil {
    private static boolean checkNotNull(View view) {
        return view != null;
    }

    private static boolean checkIsTextView(View view) {
        return checkNotNull(view) && (view instanceof TextView);
    }

    private static boolean checkIsImageView(View view) {
        return checkNotNull(view) && (view instanceof ImageView);
    }

    private static boolean checkIsProgressBar(View view) {
        return checkNotNull(view) && (view instanceof ProgressBar);
    }

    public static void setEnable(View view, boolean enable) {
        if (checkIsTextView(view)) {
            view.setEnabled(enable);
        }
    }

    public static void setText(View view, CharSequence text) {
        if (checkIsTextView(view)) {
            ((TextView) view).setText(text);
        }
    }

    public static void setText(View view, int text) {
        if (checkIsTextView(view)) {
            ((TextView) view).setText(text);
        }
    }

    public static void appendText(View view, CharSequence text) {
        if (checkIsTextView(view)) {
            ((TextView) view).append(text);
        }
    }

    public static void setVisible(View view, boolean visible) {
        setVisibility(view, visible ? 0 : 8);
    }

    public static void setVisibility(View view, int visibility) {
        if (checkNotNull(view) && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
    }

    public static boolean isVisible(View view) {
        return checkNotNull(view) && view.getVisibility() == 0;
    }

    public static void setIndeterminate(View view, boolean indeterminate) {
        if (checkIsProgressBar(view)) {
            ((ProgressBar) view).setIndeterminate(indeterminate);
        }
    }
}
