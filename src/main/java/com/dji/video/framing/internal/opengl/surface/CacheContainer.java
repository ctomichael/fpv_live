package com.dji.video.framing.internal.opengl.surface;

import com.dji.video.framing.VideoLog;
import java.util.ArrayList;
import java.util.List;

public class CacheContainer {
    private List<Object> recyleList = new ArrayList();
    private List<Object> usedList = new ArrayList();

    public CacheContainer(Object[] objs) {
        for (Object o : objs) {
            this.recyleList.add(o);
        }
    }

    public Object obtain() {
        if (this.recyleList.size() > 0) {
            this.usedList.add(this.recyleList.remove(0));
        } else {
            VideoLog.d("draw", "obtain null ", new Object[0]);
        }
        if (this.usedList.size() > 0) {
            return this.usedList.get(this.usedList.size() - 1);
        }
        return null;
    }

    public Object peak() {
        if (this.usedList.size() <= 0) {
            return null;
        }
        Object o = this.usedList.remove(0);
        this.recyleList.add(o);
        return o;
    }

    public int getSize() {
        return this.usedList.size();
    }
}
