package dji.thirdparty.afinal;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import dji.publics.protocol.ResponseBase;
import dji.thirdparty.afinal.annotation.view.EventListener;
import dji.thirdparty.afinal.annotation.view.Select;
import dji.thirdparty.afinal.annotation.view.ViewInject;
import java.lang.reflect.Field;

public abstract class FinalActivity extends Activity {
    public final String TAG = getClass().getSimpleName();

    private enum Method {
        Click,
        LongClick,
        ItemClick,
        itemLongClick
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initView();
    }

    public void setContentView(View view) {
        super.setContentView(view);
        initView();
    }

    private void initView() {
        ViewInject viewInject;
        Class<?> clazz = getClass();
        while (clazz != null && !clazz.getName().startsWith("android.")) {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                        if (field.get(this) == null && (viewInject = (ViewInject) field.getAnnotation(ViewInject.class)) != null) {
                            int viewId = viewInject.id();
                            if (viewId == 0) {
                                String viewName = viewInject.name();
                                if (!viewName.equals(null)) {
                                    viewId = getResources().getIdentifier(viewName, ResponseBase.STRING_ID, getPackageName());
                                }
                            }
                            field.set(this, findViewById(viewId));
                            setListener(field, viewInject.click(), Method.Click);
                            setListener(field, viewInject.longClick(), Method.LongClick);
                            setListener(field, viewInject.itemClick(), Method.ItemClick);
                            setListener(field, viewInject.itemLongClick(), Method.itemLongClick);
                            Select select = viewInject.select();
                            if (!TextUtils.isEmpty(select.selected())) {
                                setViewSelectListener(field, select.selected(), select.noSelected());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private void setViewSelectListener(Field field, String select, String noSelect) throws Exception {
        Object obj = field.get(this);
        if (obj instanceof View) {
            ((AbsListView) obj).setOnItemSelectedListener(new EventListener(this).select(select).noSelect(noSelect));
        }
    }

    private void setListener(Field field, String methodName, Method method) throws Exception {
        if (methodName != null && methodName.trim().length() != 0) {
            Object obj = field.get(this);
            switch (method) {
                case Click:
                    if (obj instanceof View) {
                        ((View) obj).setOnClickListener(new EventListener(this).click(methodName));
                        return;
                    }
                    return;
                case ItemClick:
                    if (obj instanceof AbsListView) {
                        ((AbsListView) obj).setOnItemClickListener(new EventListener(this).itemClick(methodName));
                        return;
                    }
                    return;
                case LongClick:
                    if (obj instanceof View) {
                        ((View) obj).setOnLongClickListener(new EventListener(this).longClick(methodName));
                        return;
                    }
                    return;
                case itemLongClick:
                    if (obj instanceof AbsListView) {
                        ((AbsListView) obj).setOnItemLongClickListener(new EventListener(this).itemLongClick(methodName));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void finish() {
        super.finish();
    }
}
