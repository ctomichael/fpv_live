package com.billy.cc.core.component;

public interface IParamJsonConverter {
    <T> T json2Object(String str, Class<T> cls);

    String object2Json(Object obj);
}
