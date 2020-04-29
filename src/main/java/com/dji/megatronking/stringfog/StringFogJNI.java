package com.dji.megatronking.stringfog;

import android.util.Log;
import com.dji.megatronking.stringfog.lib.annotation.StringFogIgnore;

@StringFogIgnore
public class StringFogJNI {
    public static native byte[] nativeGetXXXX(String str);

    public static native void test();

    static {
        try {
            Log.e("StringFogJNI", "start load lib");
            System.loadLibrary("StringFogJNI");
            Log.e("StringFogJNI", "load lib suc");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Log.e("StringFogJNI", "Couldn't load lib");
        }
    }
}
