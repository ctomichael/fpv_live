package dji.common.util;

import dji.common.error.DJIError;
import dji.common.error.DJIMissionError;
import dji.common.error.DJIOnboardSDKError;
import dji.common.util.CommonCallbacks;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.listener.DJIActionCallback;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.listener.DJISetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import java.lang.reflect.Method;

@EXClassNullAway
public class CallbackUtils {

    public interface ResultPicker {
        int getResult();
    }

    public static DJIDataCallBack defaultCB(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        return new DJIDataCallBack() {
            /* class dji.common.util.CallbackUtils.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        };
    }

    public static DJIDataCallBack defaultCB(final DJISDKCacheHWAbstraction.InnerCallback callback, final Class<? extends DJIError> clazz) {
        return new DJIDataCallBack() {
            /* class dji.common.util.CallbackUtils.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    try {
                        Method method = clazz.getMethod("getDJIError", Ccode.class);
                        if (method != null) {
                            callback.onFails((DJIError) method.invoke(clazz, ccode));
                            return;
                        }
                        callback.onFails(DJIError.getDJIError(ccode));
                    } catch (Exception e) {
                        callback.onFails(DJIError.getDJIError(ccode));
                    }
                }
            }
        };
    }

    public static void onSuccess(DJISDKCacheHWAbstraction.InnerCallback callback, Object model) {
        if (callback != null) {
            callback.onSuccess(model);
        }
    }

    public static void onSuccess(CommonCallbacks.CompletionCallbackWith callback, Object model) {
        if (callback != null) {
            callback.onSuccess(model);
        }
    }

    public static void onSuccess(CommonCallbacks.CompletionCallbackWithTwoParam callback, Object model1, Object model2) {
        if (callback != null) {
            callback.onSuccess(model1, model2);
        }
    }

    public static void onFailure(CommonCallbacks.CompletionCallbackWithTwoParam callback, Ccode ccode) {
        if (callback != null) {
            callback.onFailure(DJIError.getDJIError(ccode));
        }
    }

    public static void onFailure(CommonCallbacks.CompletionCallbackWith callback, Ccode ccode) {
        if (callback != null) {
            callback.onFailure(DJIError.getDJIError(ccode));
        }
    }

    public static void onFailure(DJISDKCacheHWAbstraction.InnerCallback callback, Ccode ccode) {
        if (callback != null) {
            callback.onFails(DJIError.getDJIError(ccode));
        }
    }

    public static void onFailure(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            callback.onFails(null);
        }
    }

    public static void onFailure(DJISDKCacheHWAbstraction.InnerCallback callback, DJIError error) {
        if (callback != null) {
            callback.onFails(error);
        }
    }

    public static void onFailure(CommonCallbacks.CompletionCallback callback, DJIError error) {
        if (callback != null) {
            callback.onResult(error);
        }
    }

    public static void onFailure(CommonCallbacks.CompletionCallback callback, Ccode ccode) {
        if (callback != null) {
            callback.onResult(DJIError.getDJIError(ccode));
        }
    }

    public static void onFailure(CommonCallbacks.CompletionCallbackWith callback, DJIError error) {
        if (callback != null) {
            callback.onFailure(error);
        }
    }

    public static void onSuccess(CommonCallbacks.CompletionCallback callback) {
        if (callback != null) {
            callback.onResult(null);
        }
    }

    public static void onResult(CommonCallbacks.CompletionCallback callback, DJIError error) {
        if (callback != null) {
            callback.onResult(error);
        }
    }

    public static DJIActionCallback getActionCallback(final CommonCallbacks.CompletionCallback callback) {
        return new DJIActionCallback() {
            /* class dji.common.util.CallbackUtils.AnonymousClass3 */

            public void onSuccess(Object value) {
                CallbackUtils.onSuccess(callback);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        };
    }

    public static DJISetCallback getSetCallback(final CommonCallbacks.CompletionCallback callback) {
        return new DJISetCallback() {
            /* class dji.common.util.CallbackUtils.AnonymousClass4 */

            public void onSuccess() {
                CallbackUtils.onSuccess(callback);
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        };
    }

    public static DJIGetCallback getGetCallback(final CommonCallbacks.CompletionCallbackWith callback) {
        return new DJIGetCallback() {
            /* class dji.common.util.CallbackUtils.AnonymousClass5 */

            public void onSuccess(DJISDKCacheParamValue value) {
                if (value == null || value.getData() == null) {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
                } else {
                    CallbackUtils.onSuccess(callback, value.getData());
                }
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        };
    }

    public static DJIDataCallBack getSetterDJIDataCallback(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        return new DJIDataCallBack() {
            /* class dji.common.util.CallbackUtils.AnonymousClass6 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        };
    }

    public static DJIDataCallBack getDJIDataCallback(final CommonCallbacks.CompletionCallback callback) {
        return new DJIDataCallBack() {
            /* class dji.common.util.CallbackUtils.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        };
    }

    public static DJIDataCallBack getMissionManagerDJIDataCallback(final int value, final CommonCallbacks.CompletionCallback callback, final Runnable runnable) {
        return new DJIDataCallBack() {
            /* class dji.common.util.CallbackUtils.AnonymousClass8 */

            public void onSuccess(Object model) {
                int result = value;
                if (result == 0) {
                    CallbackUtils.onSuccess(callback);
                    return;
                }
                if (runnable != null) {
                    runnable.run();
                }
                CallbackUtils.onFailure(callback, DJIMissionError.getDJIErrorByCode(result));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        };
    }

    public static DJISDKCacheCommonMergeCallback getFlightControllerDefaultMergeSetCallback(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        return new DJISDKCacheCommonMergeCallback() {
            /* class dji.common.util.CallbackUtils.AnonymousClass9 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        };
    }

    public static DJISDKCacheCommonMergeCallback getFlightControllerDetaultMergeGetCallback(final Class type, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        return new DJISDKCacheCommonMergeCallback() {
            /* class dji.common.util.CallbackUtils.AnonymousClass10 */

            public void onSuccess(Object object) {
                boolean z = true;
                if (type == Boolean.class) {
                    DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                    if (CacheHelper.toInt(object) != 1) {
                        z = false;
                    }
                    CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
                } else if (type == Integer.class) {
                    CallbackUtils.onSuccess(callback, Integer.valueOf(CacheHelper.toInt(object)));
                } else if (type == Short.class) {
                    CallbackUtils.onSuccess(callback, Short.valueOf(CacheHelper.toShort(object)));
                } else if (type == Double.class) {
                    CallbackUtils.onSuccess(callback, Double.valueOf(CacheHelper.toDouble(object)));
                } else if (type == Float.class) {
                    CallbackUtils.onSuccess(callback, Float.valueOf(CacheHelper.toFloat(object)));
                }
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        };
    }

    public static DJIDataCallBack getFChannelCallback(final ResultPicker picker, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        return new DJIDataCallBack() {
            /* class dji.common.util.CallbackUtils.AnonymousClass11 */

            public void onSuccess(Object model) {
                if (picker.getResult() == 0) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                } else {
                    CallbackUtils.onFailure(callback, DJIOnboardSDKError.getDJIOnboardSDKError(picker.getResult()));
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        };
    }
}
