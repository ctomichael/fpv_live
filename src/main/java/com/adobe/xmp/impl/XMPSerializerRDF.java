package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.options.SerializeOptions;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class XMPSerializerRDF {
    private static final int DEFAULT_PAD = 2048;
    private static final String PACKET_HEADER = "<?xpacket begin=\"﻿\" id=\"W5M0MpCehiHzreSzNTczkc9d\"?>";
    private static final String PACKET_TRAILER = "<?xpacket end=\"";
    private static final String PACKET_TRAILER2 = "\"?>";
    static final Set RDF_ATTR_QUALIFIER = new HashSet(Arrays.asList(XMPConst.XML_LANG, "rdf:resource", "rdf:ID", "rdf:bagID", "rdf:nodeID"));
    private static final String RDF_EMPTY_STRUCT = "<rdf:Description/>";
    private static final String RDF_RDF_END = "</rdf:RDF>";
    private static final String RDF_RDF_START = "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">";
    private static final String RDF_SCHEMA_END = "</rdf:Description>";
    private static final String RDF_SCHEMA_START = "<rdf:Description rdf:about=";
    private static final String RDF_STRUCT_END = "</rdf:Description>";
    private static final String RDF_STRUCT_START = "<rdf:Description";
    private static final String RDF_XMPMETA_END = "</x:xmpmeta>";
    private static final String RDF_XMPMETA_START = "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"";
    private SerializeOptions options;
    private CountOutputStream outputStream;
    private int padding;
    private int unicodeSize = 1;
    private OutputStreamWriter writer;
    private XMPMetaImpl xmp;

    private void addPadding(int i) throws XMPException, IOException {
        if (this.options.getExactPacketLength()) {
            int bytesWritten = this.outputStream.getBytesWritten() + (this.unicodeSize * i);
            if (bytesWritten > this.padding) {
                throw new XMPException("Can't fit into specified packet size", 107);
            }
            this.padding -= bytesWritten;
        }
        this.padding /= this.unicodeSize;
        int length = this.options.getNewline().length();
        if (this.padding >= length) {
            this.padding -= length;
            while (this.padding >= length + 100) {
                writeChars(100, ' ');
                writeNewline();
                this.padding -= length + 100;
            }
            writeChars(this.padding, ' ');
            writeNewline();
            return;
        }
        writeChars(this.padding, ' ');
    }

    private void appendNodeValue(String str, boolean z) throws IOException {
        if (str == null) {
            str = "";
        }
        write(Utils.escapeXML(str, z, true));
    }

    private boolean canBeRDFAttrProp(XMPNode xMPNode) {
        return !xMPNode.hasQualifier() && !xMPNode.getOptions().isURI() && !xMPNode.getOptions().isCompositeProperty() && !XMPConst.ARRAY_ITEM_NAME.equals(xMPNode.getName());
    }

    private void declareNamespace(String str, String str2, Set set, int i) throws IOException {
        if (str2 == null) {
            QName qName = new QName(str);
            if (qName.hasPrefix()) {
                str = qName.getPrefix();
                str2 = XMPMetaFactory.getSchemaRegistry().getNamespaceURI(str + ":");
                declareNamespace(str, str2, set, i);
            } else {
                return;
            }
        }
        if (!set.contains(str)) {
            writeNewline();
            writeIndent(i);
            write("xmlns:");
            write(str);
            write("=\"");
            write(str2);
            write(34);
            set.add(str);
        }
    }

    private void declareUsedNamespaces(XMPNode xMPNode, Set set, int i) throws IOException {
        if (xMPNode.getOptions().isSchemaNode()) {
            declareNamespace(xMPNode.getValue().substring(0, xMPNode.getValue().length() - 1), xMPNode.getName(), set, i);
        } else if (xMPNode.getOptions().isStruct()) {
            Iterator iterateChildren = xMPNode.iterateChildren();
            while (iterateChildren.hasNext()) {
                declareNamespace(((XMPNode) iterateChildren.next()).getName(), null, set, i);
            }
        }
        Iterator iterateChildren2 = xMPNode.iterateChildren();
        while (iterateChildren2.hasNext()) {
            declareUsedNamespaces((XMPNode) iterateChildren2.next(), set, i);
        }
        Iterator iterateQualifier = xMPNode.iterateQualifier();
        while (iterateQualifier.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateQualifier.next();
            declareNamespace(xMPNode2.getName(), null, set, i);
            declareUsedNamespaces(xMPNode2, set, i);
        }
    }

    private void emitRDFArrayTag(XMPNode xMPNode, boolean z, int i) throws IOException {
        if (z || xMPNode.hasChildren()) {
            writeIndent(i);
            write(z ? "<rdf:" : "</rdf:");
            if (xMPNode.getOptions().isArrayAlternate()) {
                write("Alt");
            } else if (xMPNode.getOptions().isArrayOrdered()) {
                write("Seq");
            } else {
                write("Bag");
            }
            if (!z || xMPNode.hasChildren()) {
                write(">");
            } else {
                write("/>");
            }
            writeNewline();
        }
    }

    private void endOuterRDFDescription(int i) throws IOException {
        writeIndent(i + 1);
        write("</rdf:Description>");
        writeNewline();
    }

    private String serializeAsRDF() throws IOException, XMPException {
        int i = 0;
        if (!this.options.getOmitPacketWrapper()) {
            writeIndent(0);
            write(PACKET_HEADER);
            writeNewline();
        }
        if (!this.options.getOmitXmpMetaElement()) {
            writeIndent(0);
            write(RDF_XMPMETA_START);
            if (!this.options.getOmitVersionAttribute()) {
                write(XMPMetaFactory.getVersionInfo().getMessage());
            }
            write("\">");
            writeNewline();
            i = 1;
        }
        writeIndent(i);
        write(RDF_RDF_START);
        writeNewline();
        if (this.options.getUseCanonicalFormat()) {
            serializeCanonicalRDFSchemas(i);
        } else {
            serializeCompactRDFSchemas(i);
        }
        writeIndent(i);
        write(RDF_RDF_END);
        writeNewline();
        if (!this.options.getOmitXmpMetaElement()) {
            writeIndent(i - 1);
            write(RDF_XMPMETA_END);
            writeNewline();
        }
        String str = "";
        if (this.options.getOmitPacketWrapper()) {
            return str;
        }
        for (int baseIndent = this.options.getBaseIndent(); baseIndent > 0; baseIndent--) {
            str = str + this.options.getIndent();
        }
        return ((str + PACKET_TRAILER) + (this.options.getReadOnlyPacket() ? 'r' : 'w')) + PACKET_TRAILER2;
    }

    private void serializeCanonicalRDFProperty(XMPNode xMPNode, boolean z, boolean z2, int i) throws IOException, XMPException {
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6 = true;
        String name = xMPNode.getName();
        String str = z2 ? "rdf:value" : XMPConst.ARRAY_ITEM_NAME.equals(name) ? "rdf:li" : name;
        writeIndent(i);
        write(60);
        write(str);
        boolean z7 = false;
        boolean z8 = false;
        Iterator iterateQualifier = xMPNode.iterateQualifier();
        while (true) {
            z3 = z8;
            if (!iterateQualifier.hasNext()) {
                break;
            }
            XMPNode xMPNode2 = (XMPNode) iterateQualifier.next();
            if (!RDF_ATTR_QUALIFIER.contains(xMPNode2.getName())) {
                z7 = true;
            } else {
                z3 = "rdf:resource".equals(xMPNode2.getName());
                if (!z2) {
                    write(32);
                    write(xMPNode2.getName());
                    write("=\"");
                    appendNodeValue(xMPNode2.getValue(), true);
                    write(34);
                }
            }
            z8 = z3;
        }
        if (!z7 || z2) {
            if (!xMPNode.getOptions().isCompositeProperty()) {
                if (xMPNode.getOptions().isURI()) {
                    write(" rdf:resource=\"");
                    appendNodeValue(xMPNode.getValue(), true);
                    write("\"/>");
                    writeNewline();
                    z6 = false;
                    z4 = true;
                } else if (xMPNode.getValue() == null || "".equals(xMPNode.getValue())) {
                    write("/>");
                    writeNewline();
                    z6 = false;
                    z4 = true;
                } else {
                    write(62);
                    appendNodeValue(xMPNode.getValue(), false);
                    z4 = false;
                }
            } else if (xMPNode.getOptions().isArray()) {
                write(62);
                writeNewline();
                emitRDFArrayTag(xMPNode, true, i + 1);
                if (xMPNode.getOptions().isArrayAltText()) {
                    XMPNodeUtils.normalizeLangArray(xMPNode);
                }
                Iterator iterateChildren = xMPNode.iterateChildren();
                while (iterateChildren.hasNext()) {
                    serializeCanonicalRDFProperty((XMPNode) iterateChildren.next(), z, false, i + 2);
                }
                emitRDFArrayTag(xMPNode, false, i + 1);
                z4 = true;
            } else if (z3) {
                Iterator iterateChildren2 = xMPNode.iterateChildren();
                while (iterateChildren2.hasNext()) {
                    XMPNode xMPNode3 = (XMPNode) iterateChildren2.next();
                    if (!canBeRDFAttrProp(xMPNode3)) {
                        throw new XMPException("Can't mix rdf:resource and complex fields", 202);
                    }
                    writeNewline();
                    writeIndent(i + 1);
                    write(32);
                    write(xMPNode3.getName());
                    write("=\"");
                    appendNodeValue(xMPNode3.getValue(), true);
                    write(34);
                }
                write("/>");
                writeNewline();
                z6 = false;
                z4 = true;
            } else if (!xMPNode.hasChildren()) {
                if (z) {
                    write(">");
                    writeNewline();
                    writeIndent(i + 1);
                    write(RDF_EMPTY_STRUCT);
                    z5 = true;
                } else {
                    write(" rdf:parseType=\"Resource\"/>");
                    z5 = false;
                }
                writeNewline();
                z4 = true;
                z6 = z5;
            } else {
                if (z) {
                    write(">");
                    writeNewline();
                    i++;
                    writeIndent(i);
                    write(RDF_STRUCT_START);
                    write(">");
                } else {
                    write(" rdf:parseType=\"Resource\">");
                }
                writeNewline();
                Iterator iterateChildren3 = xMPNode.iterateChildren();
                while (iterateChildren3.hasNext()) {
                    serializeCanonicalRDFProperty((XMPNode) iterateChildren3.next(), z, false, i + 1);
                }
                if (z) {
                    writeIndent(i);
                    write("</rdf:Description>");
                    writeNewline();
                    i--;
                    z4 = true;
                }
                z4 = true;
            }
        } else if (z3) {
            throw new XMPException("Can't mix rdf:resource and general qualifiers", 202);
        } else {
            if (z) {
                write(">");
                writeNewline();
                i++;
                writeIndent(i);
                write(RDF_STRUCT_START);
                write(">");
            } else {
                write(" rdf:parseType=\"Resource\">");
            }
            writeNewline();
            serializeCanonicalRDFProperty(xMPNode, z, true, i + 1);
            Iterator iterateQualifier2 = xMPNode.iterateQualifier();
            while (iterateQualifier2.hasNext()) {
                XMPNode xMPNode4 = (XMPNode) iterateQualifier2.next();
                if (!RDF_ATTR_QUALIFIER.contains(xMPNode4.getName())) {
                    serializeCanonicalRDFProperty(xMPNode4, z, false, i + 1);
                }
            }
            if (z) {
                writeIndent(i);
                write("</rdf:Description>");
                writeNewline();
                i--;
                z4 = true;
            }
            z4 = true;
        }
        if (z6) {
            if (z4) {
                writeIndent(i);
            }
            write("</");
            write(str);
            write(62);
            writeNewline();
        }
    }

    private void serializeCanonicalRDFSchema(XMPNode xMPNode, int i) throws IOException, XMPException {
        Iterator iterateChildren = xMPNode.iterateChildren();
        while (iterateChildren.hasNext()) {
            serializeCanonicalRDFProperty((XMPNode) iterateChildren.next(), this.options.getUseCanonicalFormat(), false, i + 2);
        }
    }

    private void serializeCanonicalRDFSchemas(int i) throws IOException, XMPException {
        if (this.xmp.getRoot().getChildrenLength() > 0) {
            startOuterRDFDescription(this.xmp.getRoot(), i);
            Iterator iterateChildren = this.xmp.getRoot().iterateChildren();
            while (iterateChildren.hasNext()) {
                serializeCanonicalRDFSchema((XMPNode) iterateChildren.next(), i);
            }
            endOuterRDFDescription(i);
            return;
        }
        writeIndent(i + 1);
        write(RDF_SCHEMA_START);
        writeTreeName();
        write("/>");
        writeNewline();
    }

    private void serializeCompactRDFArrayProp(XMPNode xMPNode, int i) throws IOException, XMPException {
        write(62);
        writeNewline();
        emitRDFArrayTag(xMPNode, true, i + 1);
        if (xMPNode.getOptions().isArrayAltText()) {
            XMPNodeUtils.normalizeLangArray(xMPNode);
        }
        serializeCompactRDFElementProps(xMPNode, i + 2);
        emitRDFArrayTag(xMPNode, false, i + 1);
    }

    private boolean serializeCompactRDFAttrProps(XMPNode xMPNode, int i) throws IOException {
        boolean z;
        Iterator iterateChildren = xMPNode.iterateChildren();
        boolean z2 = true;
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateChildren.next();
            if (canBeRDFAttrProp(xMPNode2)) {
                writeNewline();
                writeIndent(i);
                write(xMPNode2.getName());
                write("=\"");
                appendNodeValue(xMPNode2.getValue(), true);
                write(34);
                z = z2;
            } else {
                z = false;
            }
            z2 = z;
        }
        return z2;
    }

    private void serializeCompactRDFElementProps(XMPNode xMPNode, int i) throws IOException, XMPException {
        boolean serializeCompactRDFStructProp;
        boolean z;
        boolean z2;
        Iterator iterateChildren = xMPNode.iterateChildren();
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateChildren.next();
            if (!canBeRDFAttrProp(xMPNode2)) {
                String name = xMPNode2.getName();
                String str = XMPConst.ARRAY_ITEM_NAME.equals(name) ? "rdf:li" : name;
                writeIndent(i);
                write(60);
                write(str);
                Iterator iterateQualifier = xMPNode2.iterateQualifier();
                boolean z3 = false;
                boolean z4 = false;
                while (iterateQualifier.hasNext()) {
                    XMPNode xMPNode3 = (XMPNode) iterateQualifier.next();
                    if (!RDF_ATTR_QUALIFIER.contains(xMPNode3.getName())) {
                        z2 = z3;
                        z4 = true;
                    } else {
                        boolean equals = "rdf:resource".equals(xMPNode3.getName());
                        write(32);
                        write(xMPNode3.getName());
                        write("=\"");
                        appendNodeValue(xMPNode3.getValue(), true);
                        write(34);
                        z2 = equals;
                    }
                    z3 = z2;
                }
                if (z4) {
                    serializeCompactRDFGeneralQualifier(i, xMPNode2);
                    z = true;
                    serializeCompactRDFStructProp = true;
                } else if (!xMPNode2.getOptions().isCompositeProperty()) {
                    Object[] serializeCompactRDFSimpleProp = serializeCompactRDFSimpleProp(xMPNode2);
                    serializeCompactRDFStructProp = ((Boolean) serializeCompactRDFSimpleProp[0]).booleanValue();
                    z = ((Boolean) serializeCompactRDFSimpleProp[1]).booleanValue();
                } else if (xMPNode2.getOptions().isArray()) {
                    serializeCompactRDFArrayProp(xMPNode2, i);
                    z = true;
                    serializeCompactRDFStructProp = true;
                } else {
                    serializeCompactRDFStructProp = serializeCompactRDFStructProp(xMPNode2, i, z3);
                    z = true;
                }
                if (serializeCompactRDFStructProp) {
                    if (z) {
                        writeIndent(i);
                    }
                    write("</");
                    write(str);
                    write(62);
                    writeNewline();
                }
            }
        }
    }

    private void serializeCompactRDFGeneralQualifier(int i, XMPNode xMPNode) throws IOException, XMPException {
        write(" rdf:parseType=\"Resource\">");
        writeNewline();
        serializeCanonicalRDFProperty(xMPNode, false, true, i + 1);
        Iterator iterateQualifier = xMPNode.iterateQualifier();
        while (iterateQualifier.hasNext()) {
            serializeCanonicalRDFProperty((XMPNode) iterateQualifier.next(), false, false, i + 1);
        }
    }

    private void serializeCompactRDFSchemas(int i) throws IOException, XMPException {
        boolean z;
        writeIndent(i + 1);
        write(RDF_SCHEMA_START);
        writeTreeName();
        HashSet hashSet = new HashSet();
        hashSet.add("xml");
        hashSet.add("rdf");
        Iterator iterateChildren = this.xmp.getRoot().iterateChildren();
        while (iterateChildren.hasNext()) {
            declareUsedNamespaces((XMPNode) iterateChildren.next(), hashSet, i + 3);
        }
        boolean z2 = true;
        Iterator iterateChildren2 = this.xmp.getRoot().iterateChildren();
        while (true) {
            z = z2;
            if (!iterateChildren2.hasNext()) {
                break;
            }
            z2 = serializeCompactRDFAttrProps((XMPNode) iterateChildren2.next(), i + 2) & z;
        }
        if (!z) {
            write(62);
            writeNewline();
            Iterator iterateChildren3 = this.xmp.getRoot().iterateChildren();
            while (iterateChildren3.hasNext()) {
                serializeCompactRDFElementProps((XMPNode) iterateChildren3.next(), i + 2);
            }
            writeIndent(i + 1);
            write("</rdf:Description>");
            writeNewline();
            return;
        }
        write("/>");
        writeNewline();
    }

    private Object[] serializeCompactRDFSimpleProp(XMPNode xMPNode) throws IOException {
        Boolean bool = Boolean.TRUE;
        Boolean bool2 = Boolean.TRUE;
        if (xMPNode.getOptions().isURI()) {
            write(" rdf:resource=\"");
            appendNodeValue(xMPNode.getValue(), true);
            write("\"/>");
            writeNewline();
            bool = Boolean.FALSE;
        } else if (xMPNode.getValue() == null || xMPNode.getValue().length() == 0) {
            write("/>");
            writeNewline();
            bool = Boolean.FALSE;
        } else {
            write(62);
            appendNodeValue(xMPNode.getValue(), false);
            bool2 = Boolean.FALSE;
        }
        return new Object[]{bool, bool2};
    }

    private boolean serializeCompactRDFStructProp(XMPNode xMPNode, int i, boolean z) throws XMPException, IOException {
        boolean z2;
        Iterator iterateChildren = xMPNode.iterateChildren();
        boolean z3 = false;
        boolean z4 = false;
        while (true) {
            if (!iterateChildren.hasNext()) {
                z2 = z3;
                break;
            }
            if (canBeRDFAttrProp((XMPNode) iterateChildren.next())) {
                z2 = z3;
                z4 = true;
            } else {
                z2 = true;
            }
            if (z4 && z2) {
                break;
            }
            z3 = z2;
        }
        if (z && z2) {
            throw new XMPException("Can't mix rdf:resource qualifier and element fields", 202);
        } else if (!xMPNode.hasChildren()) {
            write(" rdf:parseType=\"Resource\"/>");
            writeNewline();
            return false;
        } else if (!z2) {
            serializeCompactRDFAttrProps(xMPNode, i + 1);
            write("/>");
            writeNewline();
            return false;
        } else if (!z4) {
            write(" rdf:parseType=\"Resource\">");
            writeNewline();
            serializeCompactRDFElementProps(xMPNode, i + 1);
            return true;
        } else {
            write(62);
            writeNewline();
            writeIndent(i + 1);
            write(RDF_STRUCT_START);
            serializeCompactRDFAttrProps(xMPNode, i + 2);
            write(">");
            writeNewline();
            serializeCompactRDFElementProps(xMPNode, i + 1);
            writeIndent(i + 1);
            write("</rdf:Description>");
            writeNewline();
            return true;
        }
    }

    private void startOuterRDFDescription(XMPNode xMPNode, int i) throws IOException {
        writeIndent(i + 1);
        write(RDF_SCHEMA_START);
        writeTreeName();
        HashSet hashSet = new HashSet();
        hashSet.add("xml");
        hashSet.add("rdf");
        declareUsedNamespaces(xMPNode, hashSet, i + 3);
        write(62);
        writeNewline();
    }

    private void write(int i) throws IOException {
        this.writer.write(i);
    }

    private void write(String str) throws IOException {
        this.writer.write(str);
    }

    private void writeChars(int i, char c) throws IOException {
        while (i > 0) {
            this.writer.write(c);
            i--;
        }
    }

    private void writeIndent(int i) throws IOException {
        for (int baseIndent = this.options.getBaseIndent() + i; baseIndent > 0; baseIndent--) {
            this.writer.write(this.options.getIndent());
        }
    }

    private void writeNewline() throws IOException {
        this.writer.write(this.options.getNewline());
    }

    private void writeTreeName() throws IOException {
        write(34);
        String name = this.xmp.getRoot().getName();
        if (name != null) {
            appendNodeValue(name, true);
        }
        write(34);
    }

    /* access modifiers changed from: protected */
    public void checkOptionsConsistence() throws XMPException {
        if (this.options.getEncodeUTF16BE() || this.options.getEncodeUTF16LE()) {
            this.unicodeSize = 2;
        }
        if (this.options.getExactPacketLength()) {
            if (this.options.getOmitPacketWrapper() || this.options.getIncludeThumbnailPad()) {
                throw new XMPException("Inconsistent options for exact size serialize", 103);
            } else if ((this.options.getPadding() & (this.unicodeSize - 1)) != 0) {
                throw new XMPException("Exact size must be a multiple of the Unicode element", 103);
            }
        } else if (this.options.getReadOnlyPacket()) {
            if (this.options.getOmitPacketWrapper() || this.options.getIncludeThumbnailPad()) {
                throw new XMPException("Inconsistent options for read-only packet", 103);
            }
            this.padding = 0;
        } else if (!this.options.getOmitPacketWrapper()) {
            if (this.padding == 0) {
                this.padding = this.unicodeSize * 2048;
            }
            if (this.options.getIncludeThumbnailPad() && !this.xmp.doesPropertyExist("http://ns.adobe.com/xap/1.0/", "Thumbnails")) {
                this.padding += this.unicodeSize * 10000;
            }
        } else if (this.options.getIncludeThumbnailPad()) {
            throw new XMPException("Inconsistent options for non-packet serialize", 103);
        } else {
            this.padding = 0;
        }
    }

    public void serialize(XMPMeta xMPMeta, OutputStream outputStream2, SerializeOptions serializeOptions) throws XMPException {
        try {
            this.outputStream = new CountOutputStream(outputStream2);
            this.writer = new OutputStreamWriter(this.outputStream, serializeOptions.getEncoding());
            this.xmp = (XMPMetaImpl) xMPMeta;
            this.options = serializeOptions;
            this.padding = serializeOptions.getPadding();
            this.writer = new OutputStreamWriter(this.outputStream, serializeOptions.getEncoding());
            checkOptionsConsistence();
            String serializeAsRDF = serializeAsRDF();
            this.writer.flush();
            addPadding(serializeAsRDF.length());
            write(serializeAsRDF);
            this.writer.flush();
            this.outputStream.close();
        } catch (IOException e) {
            throw new XMPException("Error writing to the OutputStream", 0);
        }
    }
}
