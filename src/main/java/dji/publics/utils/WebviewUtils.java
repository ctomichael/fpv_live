package dji.publics.utils;

import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebviewUtils {
    public static void setupWebView(WebView view) {
        if (view != null) {
            view.getSettings().setDomStorageEnabled(true);
            view.getSettings().setSupportZoom(true);
            view.getSettings().setBuiltInZoomControls(true);
            view.getSettings().setDisplayZoomControls(false);
            view.getSettings().setPluginState(WebSettings.PluginState.ON);
            view.getSettings().setMediaPlaybackRequiresUserGesture(false);
            view.getSettings().setAllowFileAccess(false);
            view.getSettings().setAllowFileAccessFromFileURLs(false);
            view.getSettings().setAllowUniversalAccessFromFileURLs(false);
            view.getSettings().setUseWideViewPort(true);
            view.getSettings().setLoadWithOverviewMode(true);
            view.getSettings().setSupportMultipleWindows(false);
            view.getSettings().setCacheMode(-1);
            if (Build.VERSION.SDK_INT >= 21) {
                view.getSettings().setMixedContentMode(0);
            }
        }
    }
}
