package com.billy.cc.core.component.remote;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import com.billy.cc.core.component.CCUtil;
import com.billy.cc.core.component.IParamJsonConverter;
import dji.component.mediaprovider.DJIMediaStore;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class RemoteParamUtil {
    static IParamJsonConverter paramJsonConverter;

    static void initRemoteCCParamJsonConverter(IParamJsonConverter converter) {
        paramJsonConverter = converter;
    }

    public static String convertObject2JsonString(Object object) {
        if (paramJsonConverter != null && object != null) {
            return paramJsonConverter.object2Json(object);
        }
        if (object == null) {
            return null;
        }
        return object.toString();
    }

    public static HashMap<String, Object> toRemoteMap(Map<String, Object> data) {
        HashMap<String, Object> map = null;
        if (data != null) {
            map = new HashMap<>(data.size());
            for (String key : data.keySet()) {
                map.put(key, convertParam(data.get(key)));
            }
        }
        return map;
    }

    public static HashMap<String, Object> toLocalMap(Map<String, Object> map) {
        HashMap<String, Object> data = null;
        if (map != null) {
            data = new HashMap<>(map.size());
            for (String key : map.keySet()) {
                data.put(key, restoreParam(map.get(key)));
            }
        }
        return data;
    }

    /* access modifiers changed from: private */
    public static Object restoreParam(Object v) {
        if (v instanceof SparseArray) {
            SparseArray sp = (SparseArray) v;
            for (int i = 0; i < sp.size(); i++) {
                sp.put(sp.keyAt(i), restoreParam(sp.valueAt(i)));
            }
            return v;
        } else if (v instanceof BaseParam) {
            return ((BaseParam) v).restore();
        } else {
            return v;
        }
    }

    static Object convertParam(Object v) {
        if (v == null || (v instanceof String) || (v instanceof Integer) || (v instanceof Long) || (v instanceof Float) || (v instanceof Double) || (v instanceof Boolean) || (v instanceof Short) || (v instanceof Byte) || (v instanceof CharSequence) || (v instanceof String[]) || (v instanceof int[]) || (v instanceof byte[]) || (v instanceof long[]) || (v instanceof double[]) || (v instanceof boolean[]) || (v instanceof Bundle) || (v instanceof PersistableBundle) || (v instanceof Parcelable) || (v instanceof Parcelable[]) || (v instanceof CharSequence[]) || (v instanceof IBinder) || (v instanceof Size) || (v instanceof SizeF)) {
            return v;
        }
        if (v instanceof SparseArray) {
            SparseArray sa = (SparseArray) v;
            Object sp = new SparseArray();
            for (int i = 0; i < sa.size(); i++) {
                sp.put(sa.keyAt(i), convertParam(sa.valueAt(i)));
            }
            return sp;
        } else if (v instanceof Map) {
            return new MapParam(v);
        } else {
            if (v instanceof Collection) {
                return new CollectionParam(v);
            }
            if (v.getClass().isArray()) {
                return new ArrayParam(v);
            }
            return !(v instanceof Serializable) ? new ObjParam(v) : v;
        }
    }

    public static abstract class BaseParam implements Parcelable {
        Class<?> clazz;
        int hashCode;

        public abstract Object restore();

        BaseParam(Object obj) {
            this.clazz = obj.getClass();
            this.hashCode = obj.hashCode();
        }

        BaseParam(Parcel in2) {
            this.hashCode = in2.readInt();
            this.clazz = (Class) in2.readSerializable();
        }

        public String toString() {
            return toJson().toString();
        }

        /* access modifiers changed from: protected */
        @NonNull
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            CCUtil.put(json, "class", this.clazz);
            return json;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.hashCode);
            dest.writeSerializable(this.clazz);
        }

        public int describeContents() {
            return 0;
        }

        public int hashCode() {
            return this.hashCode;
        }

        public boolean equals(Object obj) {
            return super.equals(obj);
        }
    }

    static class ObjParam extends BaseParam {
        public static final Parcelable.Creator<ObjParam> CREATOR = new Parcelable.Creator<ObjParam>() {
            /* class com.billy.cc.core.component.remote.RemoteParamUtil.ObjParam.AnonymousClass1 */

            public ObjParam createFromParcel(Parcel in2) {
                return new ObjParam(in2);
            }

            public ObjParam[] newArray(int size) {
                return new ObjParam[size];
            }
        };
        String json;

        ObjParam(Object obj) {
            super(obj);
            if (RemoteParamUtil.paramJsonConverter != null) {
                this.json = RemoteParamUtil.paramJsonConverter.object2Json(obj);
            }
        }

        /* access modifiers changed from: protected */
        @NonNull
        public JSONObject toJson() {
            JSONObject jsonObject = super.toJson();
            CCUtil.put(jsonObject, "value", this.json);
            return jsonObject;
        }

        public Object restore() {
            if (RemoteParamUtil.paramJsonConverter != null) {
                return RemoteParamUtil.paramJsonConverter.json2Object(this.json, this.clazz);
            }
            return null;
        }

        ObjParam(Parcel in2) {
            super(in2);
            this.json = in2.readString();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.json);
        }
    }

    static class ArrayParam extends BaseParam {
        public static final Parcelable.Creator<ArrayParam> CREATOR = new Parcelable.Creator<ArrayParam>() {
            /* class com.billy.cc.core.component.remote.RemoteParamUtil.ArrayParam.AnonymousClass1 */

            public ArrayParam createFromParcel(Parcel in2) {
                return new ArrayParam(in2);
            }

            public ArrayParam[] newArray(int size) {
                return new ArrayParam[size];
            }
        };
        int length;
        ArrayList params = new ArrayList();

        ArrayParam(Object obj) {
            super(obj);
            this.length = Array.getLength(obj);
            for (int i = 0; i < this.length; i++) {
                this.params.add(RemoteParamUtil.convertParam(Array.get(obj, i)));
            }
        }

        /* access modifiers changed from: protected */
        @NonNull
        public JSONObject toJson() {
            JSONObject jsonObject = super.toJson();
            CCUtil.put(jsonObject, DJIMediaStore.FileColumns.LENGTH, Integer.valueOf(this.length));
            JSONArray array = new JSONArray();
            for (int i = 0; i < this.params.size(); i++) {
                array.put(this.params.get(i));
            }
            CCUtil.put(jsonObject, "value", array);
            return jsonObject;
        }

        public Object restore() {
            Object obj = Array.newInstance(this.clazz.getComponentType(), this.length);
            int size = this.params.size();
            for (int i = 0; i < size; i++) {
                Array.set(obj, i, RemoteParamUtil.restoreParam(this.params.get(i)));
            }
            return obj;
        }

        ArrayParam(Parcel in2) {
            super(in2);
            this.length = in2.readInt();
            this.params = in2.readArrayList(getClass().getClassLoader());
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.length);
            dest.writeList(this.params);
        }
    }

    static class CollectionParam extends BaseParam {
        public static final Parcelable.Creator<CollectionParam> CREATOR = new Parcelable.Creator<CollectionParam>() {
            /* class com.billy.cc.core.component.remote.RemoteParamUtil.CollectionParam.AnonymousClass1 */

            public CollectionParam createFromParcel(Parcel in2) {
                return new CollectionParam(in2);
            }

            public CollectionParam[] newArray(int size) {
                return new CollectionParam[size];
            }
        };
        ArrayList<Object> params = new ArrayList<>();

        CollectionParam(Object obj) {
            super(obj);
            for (Object o : (Collection) obj) {
                this.params.add(RemoteParamUtil.convertParam(o));
            }
        }

        /* access modifiers changed from: protected */
        @NonNull
        public JSONObject toJson() {
            JSONObject jsonObject = super.toJson();
            CCUtil.put(jsonObject, DJIMediaStore.FileColumns.LENGTH, Integer.valueOf(this.params.size()));
            JSONArray array = new JSONArray();
            for (int i = 0; i < this.params.size(); i++) {
                array.put(this.params.get(i));
            }
            CCUtil.put(jsonObject, "value", array);
            return jsonObject;
        }

        public Object restore() {
            try {
                Object o = this.clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                Collection collection = (Collection) o;
                Iterator<Object> it2 = this.params.iterator();
                while (it2.hasNext()) {
                    collection.add(RemoteParamUtil.restoreParam(it2.next()));
                }
                return o;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        CollectionParam(Parcel in2) {
            super(in2);
            this.params = in2.readArrayList(getClass().getClassLoader());
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeList(this.params);
        }
    }

    static class MapParam extends BaseParam {
        public static final Parcelable.Creator<MapParam> CREATOR = new Parcelable.Creator<MapParam>() {
            /* class com.billy.cc.core.component.remote.RemoteParamUtil.MapParam.AnonymousClass1 */

            public MapParam createFromParcel(Parcel in2) {
                return new MapParam(in2);
            }

            public MapParam[] newArray(int size) {
                return new MapParam[size];
            }
        };
        HashMap<Object, Object> params = new HashMap<>();

        MapParam(Object obj) {
            super(obj);
            Map map = (Map) obj;
            for (Object o : map.keySet()) {
                this.params.put(RemoteParamUtil.convertParam(o), RemoteParamUtil.convertParam(map.get(o)));
            }
        }

        /* access modifiers changed from: protected */
        @NonNull
        public JSONObject toJson() {
            JSONObject jsonObject = super.toJson();
            JSONObject value = new JSONObject();
            for (Map.Entry<Object, Object> entry : this.params.entrySet()) {
                Object key = entry.getKey();
                if (key == null) {
                    key = JSONObject.NULL;
                }
                CCUtil.put(value, key.toString(), entry.getValue());
            }
            CCUtil.put(jsonObject, "value", value);
            return jsonObject;
        }

        public Object restore() {
            try {
                Object o = this.clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                Map map = (Map) o;
                for (Object param : this.params.keySet()) {
                    map.put(RemoteParamUtil.restoreParam(param), RemoteParamUtil.restoreParam(this.params.get(param)));
                }
                return o;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        MapParam(Parcel in2) {
            super(in2);
            this.params = in2.readHashMap(getClass().getClassLoader());
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeMap(this.params);
        }
    }
}
