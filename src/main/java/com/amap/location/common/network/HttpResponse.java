package com.amap.location.common.network;

import java.util.List;
import java.util.Map;

public class HttpResponse {
    public byte[] body;
    public Map<String, List<String>> headers;
    public int statusCode;
}
