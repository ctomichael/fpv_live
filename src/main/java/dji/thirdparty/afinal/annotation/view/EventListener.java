package dji.thirdparty.afinal.annotation.view;

import android.view.View;
import android.widget.AdapterView;
import dji.thirdparty.afinal.exception.ViewException;
import java.lang.reflect.Method;

public class EventListener implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemLongClickListener {
    private String clickMethod;
    private Object handler;
    private String itemClickMethod;
    private String itemLongClickMehtod;
    private String itemSelectMethod;
    private String longClickMethod;
    private String nothingSelectedMethod;

    public EventListener(Object handler2) {
        this.handler = handler2;
    }

    public EventListener click(String method) {
        this.clickMethod = method;
        return this;
    }

    public EventListener longClick(String method) {
        this.longClickMethod = method;
        return this;
    }

    public EventListener itemLongClick(String method) {
        this.itemLongClickMehtod = method;
        return this;
    }

    public EventListener itemClick(String method) {
        this.itemClickMethod = method;
        return this;
    }

    public EventListener select(String method) {
        this.itemSelectMethod = method;
        return this;
    }

    public EventListener noSelect(String method) {
        this.nothingSelectedMethod = method;
        return this;
    }

    public boolean onLongClick(View v) {
        return invokeLongClickMethod(this.handler, this.longClickMethod, v);
    }

    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        return invokeItemLongClickMethod(this.handler, this.itemLongClickMehtod, arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3));
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        invokeItemSelectMethod(this.handler, this.itemSelectMethod, arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3));
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        invokeNoSelectMethod(this.handler, this.nothingSelectedMethod, arg0);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        invokeItemClickMethod(this.handler, this.itemClickMethod, arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3));
    }

    public void onClick(View v) {
        invokeClickMethod(this.handler, this.clickMethod, v);
    }

    private static Object invokeClickMethod(Object handler2, String methodName, Object... params) {
        if (handler2 == null) {
            return null;
        }
        try {
            Method method = handler2.getClass().getDeclaredMethod(methodName, View.class);
            if (method != null) {
                return method.invoke(handler2, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean invokeLongClickMethod(Object handler2, String methodName, Object... params) {
        if (handler2 == null) {
            return false;
        }
        try {
            Method method = handler2.getClass().getDeclaredMethod(methodName, View.class);
            if (method != null) {
                Object obj = method.invoke(handler2, params);
                if (obj != null) {
                    return Boolean.valueOf(obj.toString()).booleanValue();
                }
                return false;
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Object invokeItemClickMethod(Object handler2, String methodName, Object... params) {
        if (handler2 == null) {
            return null;
        }
        try {
            Method method = handler2.getClass().getDeclaredMethod(methodName, AdapterView.class, View.class, Integer.TYPE, Long.TYPE);
            if (method != null) {
                return method.invoke(handler2, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean invokeItemLongClickMethod(Object handler2, String methodName, Object... params) {
        if (handler2 == null) {
            throw new ViewException("invokeItemLongClickMethod: handler is null :");
        }
        try {
            Method method = handler2.getClass().getDeclaredMethod(methodName, AdapterView.class, View.class, Integer.TYPE, Long.TYPE);
            if (method != null) {
                Object obj = method.invoke(handler2, params);
                return Boolean.valueOf(obj == null ? false : Boolean.valueOf(obj.toString()).booleanValue()).booleanValue();
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Object invokeItemSelectMethod(Object handler2, String methodName, Object... params) {
        if (handler2 == null) {
            return null;
        }
        try {
            Method method = handler2.getClass().getDeclaredMethod(methodName, AdapterView.class, View.class, Integer.TYPE, Long.TYPE);
            if (method != null) {
                return method.invoke(handler2, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object invokeNoSelectMethod(Object handler2, String methodName, Object... params) {
        if (handler2 == null) {
            return null;
        }
        try {
            Method method = handler2.getClass().getDeclaredMethod(methodName, AdapterView.class);
            if (method != null) {
                return method.invoke(handler2, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
