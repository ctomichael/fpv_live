package com.tencent.mmkv;

public interface MMKVContentChangeNotification {
    void onContentChangedByOuterProcess(String str);
}
