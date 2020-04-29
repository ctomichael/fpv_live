package com.drew.metadata.xmp;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.SerializeOptions;
import com.drew.metadata.Metadata;
import java.io.OutputStream;

public class XmpWriter {
    public static boolean write(OutputStream os, Metadata data) {
        XmpDirectory dir = (XmpDirectory) data.getFirstDirectoryOfType(XmpDirectory.class);
        if (dir == null) {
            return false;
        }
        try {
            XMPMetaFactory.serialize(dir.getXMPMeta(), os, new SerializeOptions().setOmitPacketWrapper(true));
            return true;
        } catch (XMPException e) {
            e.printStackTrace();
            return false;
        }
    }
}
