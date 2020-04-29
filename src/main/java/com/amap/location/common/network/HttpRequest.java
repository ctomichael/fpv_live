package com.amap.location.common.network;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    public byte[] body;
    public Map<String, String> headers = new HashMap();
    public int timeout = 10000;
    public String url;
}
