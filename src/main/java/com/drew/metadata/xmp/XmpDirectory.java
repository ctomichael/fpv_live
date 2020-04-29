package com.drew.metadata.xmp;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.impl.XMPMetaImpl;
import com.adobe.xmp.properties.XMPPropertyInfo;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XmpDirectory extends Directory {
    public static final int TAG_XMP_VALUE_COUNT = 65535;
    @NotNull
    protected static final HashMap<Integer, String> _tagNameMap = new HashMap<>();
    @Nullable
    private XMPMeta _xmpMeta;

    static {
        _tagNameMap.put(65535, "XMP Value Count");
    }

    public XmpDirectory() {
        setDescriptor(new XmpDescriptor(this));
    }

    @NotNull
    public String getName() {
        return "XMP";
    }

    /* access modifiers changed from: protected */
    @NotNull
    public HashMap<Integer, String> getTagNameMap() {
        return _tagNameMap;
    }

    @NotNull
    public Map<String, String> getXmpProperties() {
        Map<String, String> propertyValueByPath = new HashMap<>();
        if (this._xmpMeta != null) {
            try {
                Iterator i = this._xmpMeta.iterator();
                while (i.hasNext()) {
                    XMPPropertyInfo prop = (XMPPropertyInfo) i.next();
                    String path = prop.getPath();
                    String value = prop.getValue();
                    if (!(path == null || value == null)) {
                        propertyValueByPath.put(path, value);
                    }
                }
            } catch (XMPException e) {
            }
        }
        return Collections.unmodifiableMap(propertyValueByPath);
    }

    public void setXMPMeta(@NotNull XMPMeta xmpMeta) {
        this._xmpMeta = xmpMeta;
        int valueCount = 0;
        try {
            Iterator i = this._xmpMeta.iterator();
            while (i.hasNext()) {
                if (((XMPPropertyInfo) i.next()).getPath() != null) {
                    valueCount++;
                }
            }
            setInt(65535, valueCount);
        } catch (XMPException e) {
        }
    }

    @NotNull
    public XMPMeta getXMPMeta() {
        if (this._xmpMeta == null) {
            this._xmpMeta = new XMPMetaImpl();
        }
        return this._xmpMeta;
    }
}
