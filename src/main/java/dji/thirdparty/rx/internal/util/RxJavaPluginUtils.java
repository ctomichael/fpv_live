package dji.thirdparty.rx.internal.util;

import dji.thirdparty.rx.plugins.RxJavaPlugins;

public final class RxJavaPluginUtils {
    public static void handleException(Throwable e) {
        try {
            RxJavaPlugins.getInstance().getErrorHandler().handleError(e);
        } catch (Throwable pluginException) {
            handlePluginException(pluginException);
        }
    }

    private static void handlePluginException(Throwable pluginException) {
        System.err.println("RxJavaErrorHandler threw an Exception. It shouldn't. => " + pluginException.getMessage());
        pluginException.printStackTrace();
    }
}
