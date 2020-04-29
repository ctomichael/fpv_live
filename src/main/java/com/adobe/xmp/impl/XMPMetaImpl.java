package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPDateTime;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPIterator;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPPathFactory;
import com.adobe.xmp.XMPUtils;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathParser;
import com.adobe.xmp.options.IteratorOptions;
import com.adobe.xmp.options.ParseOptions;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.properties.XMPProperty;
import java.util.Calendar;

public class XMPMetaImpl implements XMPMeta, XMPConst {
    static final /* synthetic */ boolean $assertionsDisabled = (!XMPMetaImpl.class.desiredAssertionStatus());
    private static final int VALUE_BASE64 = 7;
    private static final int VALUE_BOOLEAN = 1;
    private static final int VALUE_CALENDAR = 6;
    private static final int VALUE_DATE = 5;
    private static final int VALUE_DOUBLE = 4;
    private static final int VALUE_INTEGER = 2;
    private static final int VALUE_LONG = 3;
    private static final int VALUE_STRING = 0;
    private String packetHeader;
    private XMPNode tree;

    public XMPMetaImpl() {
        this.packetHeader = null;
        this.tree = new XMPNode(null, null, null);
    }

    public XMPMetaImpl(XMPNode xMPNode) {
        this.packetHeader = null;
        this.tree = xMPNode;
    }

    private void doSetArrayItem(XMPNode xMPNode, int i, String str, PropertyOptions propertyOptions, boolean z) throws XMPException {
        XMPNode xMPNode2 = new XMPNode(XMPConst.ARRAY_ITEM_NAME, null);
        PropertyOptions verifySetOptions = XMPNodeUtils.verifySetOptions(propertyOptions, str);
        int childrenLength = z ? xMPNode.getChildrenLength() + 1 : xMPNode.getChildrenLength();
        if (i == -1) {
            i = childrenLength;
        }
        if (1 > i || i > childrenLength) {
            throw new XMPException("Array index out of bounds", 104);
        }
        if (!z) {
            xMPNode.removeChild(i);
        }
        xMPNode.addChild(i, xMPNode2);
        setNode(xMPNode2, str, verifySetOptions, false);
    }

    private Object evaluateNodeValue(int i, XMPNode xMPNode) throws XMPException {
        String value = xMPNode.getValue();
        switch (i) {
            case 1:
                return new Boolean(XMPUtils.convertToBoolean(value));
            case 2:
                return new Integer(XMPUtils.convertToInteger(value));
            case 3:
                return new Long(XMPUtils.convertToLong(value));
            case 4:
                return new Double(XMPUtils.convertToDouble(value));
            case 5:
                return XMPUtils.convertToDate(value);
            case 6:
                return XMPUtils.convertToDate(value).getCalendar();
            case 7:
                return XMPUtils.decodeBase64(value);
            default:
                return (value != null || xMPNode.getOptions().isCompositeProperty()) ? value : "";
        }
    }

    public void appendArrayItem(String str, String str2, PropertyOptions propertyOptions, String str3, PropertyOptions propertyOptions2) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        if (propertyOptions == null) {
            propertyOptions = new PropertyOptions();
        }
        if (!propertyOptions.isOnlyArrayOptions()) {
            throw new XMPException("Only array form flags allowed for arrayOptions", 103);
        }
        PropertyOptions verifySetOptions = XMPNodeUtils.verifySetOptions(propertyOptions, null);
        XMPPath expandXPath = XMPPathParser.expandXPath(str, str2);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, expandXPath, false, null);
        if (findNode != null) {
            if (!findNode.getOptions().isArray()) {
                throw new XMPException("The named property is not an array", 102);
            }
        } else if (verifySetOptions.isArray()) {
            findNode = XMPNodeUtils.findNode(this.tree, expandXPath, true, verifySetOptions);
            if (findNode == null) {
                throw new XMPException("Failure creating array node", 102);
            }
        } else {
            throw new XMPException("Explicit arrayOptions required to create new array", 103);
        }
        doSetArrayItem(findNode, -1, str3, propertyOptions2, true);
    }

    public void appendArrayItem(String str, String str2, String str3) throws XMPException {
        appendArrayItem(str, str2, null, str3, null);
    }

    public Object clone() {
        return new XMPMetaImpl((XMPNode) this.tree.clone());
    }

    public int countArrayItems(String str, String str2) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode == null) {
            return 0;
        }
        if (findNode.getOptions().isArray()) {
            return findNode.getChildrenLength();
        }
        throw new XMPException("The named property is not an array", 102);
    }

    public void deleteArrayItem(String str, String str2, int i) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertArrayName(str2);
            deleteProperty(str, XMPPathFactory.composeArrayItemPath(str2, i));
        } catch (XMPException e) {
        }
    }

    public void deleteProperty(String str, String str2) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertPropName(str2);
            XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
            if (findNode != null) {
                XMPNodeUtils.deleteNode(findNode);
            }
        } catch (XMPException e) {
        }
    }

    public void deleteQualifier(String str, String str2, String str3, String str4) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertPropName(str2);
            deleteProperty(str, str2 + XMPPathFactory.composeQualifierPath(str3, str4));
        } catch (XMPException e) {
        }
    }

    public void deleteStructField(String str, String str2, String str3, String str4) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertStructName(str2);
            deleteProperty(str, str2 + XMPPathFactory.composeStructFieldPath(str3, str4));
        } catch (XMPException e) {
        }
    }

    public boolean doesArrayItemExist(String str, String str2, int i) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertArrayName(str2);
            return doesPropertyExist(str, XMPPathFactory.composeArrayItemPath(str2, i));
        } catch (XMPException e) {
            return false;
        }
    }

    public boolean doesPropertyExist(String str, String str2) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertPropName(str2);
            return XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null) != null;
        } catch (XMPException e) {
            return false;
        }
    }

    public boolean doesQualifierExist(String str, String str2, String str3, String str4) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertPropName(str2);
            return doesPropertyExist(str, str2 + XMPPathFactory.composeQualifierPath(str3, str4));
        } catch (XMPException e) {
            return false;
        }
    }

    public boolean doesStructFieldExist(String str, String str2, String str3, String str4) {
        try {
            ParameterAsserts.assertSchemaNS(str);
            ParameterAsserts.assertStructName(str2);
            return doesPropertyExist(str, str2 + XMPPathFactory.composeStructFieldPath(str3, str4));
        } catch (XMPException e) {
            return false;
        }
    }

    public String dumpObject() {
        return getRoot().dumpNode(true);
    }

    public XMPProperty getArrayItem(String str, String str2, int i) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        return getProperty(str, XMPPathFactory.composeArrayItemPath(str2, i));
    }

    public XMPProperty getLocalizedText(String str, String str2, String str3, String str4) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        ParameterAsserts.assertSpecificLang(str4);
        String normalizeLangValue = str3 != null ? Utils.normalizeLangValue(str3) : null;
        String normalizeLangValue2 = Utils.normalizeLangValue(str4);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode == null) {
            return null;
        }
        Object[] chooseLocalizedText = XMPNodeUtils.chooseLocalizedText(findNode, normalizeLangValue, normalizeLangValue2);
        int intValue = ((Integer) chooseLocalizedText[0]).intValue();
        final XMPNode xMPNode = (XMPNode) chooseLocalizedText[1];
        if (intValue != 0) {
            return new XMPProperty() {
                /* class com.adobe.xmp.impl.XMPMetaImpl.AnonymousClass1 */

                public String getLanguage() {
                    return xMPNode.getQualifier(1).getValue();
                }

                public PropertyOptions getOptions() {
                    return xMPNode.getOptions();
                }

                public String getValue() {
                    return xMPNode.getValue();
                }

                public String toString() {
                    return xMPNode.getValue().toString();
                }
            };
        }
        return null;
    }

    public String getObjectName() {
        return this.tree.getName() != null ? this.tree.getName() : "";
    }

    public String getPacketHeader() {
        return this.packetHeader;
    }

    public XMPProperty getProperty(String str, String str2) throws XMPException {
        return getProperty(str, str2, 0);
    }

    /* access modifiers changed from: protected */
    public XMPProperty getProperty(String str, String str2, int i) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertPropName(str2);
        final XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode == null) {
            return null;
        }
        if (i == 0 || !findNode.getOptions().isCompositeProperty()) {
            final Object evaluateNodeValue = evaluateNodeValue(i, findNode);
            return new XMPProperty() {
                /* class com.adobe.xmp.impl.XMPMetaImpl.AnonymousClass2 */

                public String getLanguage() {
                    return null;
                }

                public PropertyOptions getOptions() {
                    return findNode.getOptions();
                }

                public String getValue() {
                    if (evaluateNodeValue != null) {
                        return evaluateNodeValue.toString();
                    }
                    return null;
                }

                public String toString() {
                    return evaluateNodeValue.toString();
                }
            };
        }
        throw new XMPException("Property must be simple when a value type is requested", 102);
    }

    public byte[] getPropertyBase64(String str, String str2) throws XMPException {
        return (byte[]) getPropertyObject(str, str2, 7);
    }

    public Boolean getPropertyBoolean(String str, String str2) throws XMPException {
        return (Boolean) getPropertyObject(str, str2, 1);
    }

    public Calendar getPropertyCalendar(String str, String str2) throws XMPException {
        return (Calendar) getPropertyObject(str, str2, 6);
    }

    public XMPDateTime getPropertyDate(String str, String str2) throws XMPException {
        return (XMPDateTime) getPropertyObject(str, str2, 5);
    }

    public Double getPropertyDouble(String str, String str2) throws XMPException {
        return (Double) getPropertyObject(str, str2, 4);
    }

    public Integer getPropertyInteger(String str, String str2) throws XMPException {
        return (Integer) getPropertyObject(str, str2, 2);
    }

    public Long getPropertyLong(String str, String str2) throws XMPException {
        return (Long) getPropertyObject(str, str2, 3);
    }

    /* access modifiers changed from: protected */
    public Object getPropertyObject(String str, String str2, int i) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertPropName(str2);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode == null) {
            return null;
        }
        if (i == 0 || !findNode.getOptions().isCompositeProperty()) {
            return evaluateNodeValue(i, findNode);
        }
        throw new XMPException("Property must be simple when a value type is requested", 102);
    }

    public String getPropertyString(String str, String str2) throws XMPException {
        return (String) getPropertyObject(str, str2, 0);
    }

    public XMPProperty getQualifier(String str, String str2, String str3, String str4) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertPropName(str2);
        return getProperty(str, str2 + XMPPathFactory.composeQualifierPath(str3, str4));
    }

    public XMPNode getRoot() {
        return this.tree;
    }

    public XMPProperty getStructField(String str, String str2, String str3, String str4) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertStructName(str2);
        return getProperty(str, str2 + XMPPathFactory.composeStructFieldPath(str3, str4));
    }

    public void insertArrayItem(String str, String str2, int i, String str3) throws XMPException {
        insertArrayItem(str, str2, i, str3, null);
    }

    public void insertArrayItem(String str, String str2, int i, String str3, PropertyOptions propertyOptions) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode != null) {
            doSetArrayItem(findNode, i, str3, propertyOptions, true);
            return;
        }
        throw new XMPException("Specified array does not exist", 102);
    }

    public XMPIterator iterator() throws XMPException {
        return iterator(null, null, null);
    }

    public XMPIterator iterator(IteratorOptions iteratorOptions) throws XMPException {
        return iterator(null, null, iteratorOptions);
    }

    public XMPIterator iterator(String str, String str2, IteratorOptions iteratorOptions) throws XMPException {
        return new XMPIteratorImpl(this, str, str2, iteratorOptions);
    }

    public void normalize(ParseOptions parseOptions) throws XMPException {
        if (parseOptions == null) {
            parseOptions = new ParseOptions();
        }
        XMPNormalizer.process(this, parseOptions);
    }

    public void setArrayItem(String str, String str2, int i, String str3) throws XMPException {
        setArrayItem(str, str2, i, str3, null);
    }

    public void setArrayItem(String str, String str2, int i, String str3, PropertyOptions propertyOptions) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode != null) {
            doSetArrayItem(findNode, i, str3, propertyOptions, false);
            return;
        }
        throw new XMPException("Specified array does not exist", 102);
    }

    public void setLocalizedText(String str, String str2, String str3, String str4, String str5) throws XMPException {
        setLocalizedText(str, str2, str3, str4, str5, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00ac, code lost:
        if (r3 == null) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x00b3, code lost:
        if (r5.getChildrenLength() <= 1) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b5, code lost:
        r5.removeChild(r3);
        r5.addChild(1, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00bc, code lost:
        r1 = com.adobe.xmp.impl.XMPNodeUtils.chooseLocalizedText(r5, r1, r4);
        r6 = ((java.lang.Integer) r1[0]).intValue();
        r0 = (com.adobe.xmp.impl.XMPNode) r1[1];
        r1 = com.adobe.xmp.XMPConst.X_DEFAULT.equals(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00d5, code lost:
        switch(r6) {
            case 0: goto L_0x00e3;
            case 1: goto L_0x00ff;
            case 2: goto L_0x015a;
            case 3: goto L_0x0177;
            case 4: goto L_0x017f;
            case 5: goto L_0x0191;
            default: goto L_0x00d8;
        };
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00e2, code lost:
        throw new com.adobe.xmp.XMPException("Unexpected result from ChooseLocalizedText", 9);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00e3, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, com.adobe.xmp.XMPConst.X_DEFAULT, r14);
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00ea, code lost:
        if (r1 != false) goto L_0x00ef;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00ec, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00ef, code lost:
        if (r0 != false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00f6, code lost:
        if (r5.getChildrenLength() != 1) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x00f8, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, com.adobe.xmp.XMPConst.X_DEFAULT, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00ff, code lost:
        if (r1 != false) goto L_0x011d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0101, code lost:
        if (r2 == false) goto L_0x0118;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0103, code lost:
        if (r3 == r0) goto L_0x0118;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0105, code lost:
        if (r3 == null) goto L_0x0118;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0113, code lost:
        if (r3.getValue().equals(r0.getValue()) == false) goto L_0x0118;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x0115, code lost:
        r3.setValue(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x0118, code lost:
        r0.setValue(r14);
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x011f, code lost:
        if (com.adobe.xmp.impl.XMPMetaImpl.$assertionsDisabled != false) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0121, code lost:
        if (r2 == false) goto L_0x0125;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0123, code lost:
        if (r3 == r0) goto L_0x012b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x012a, code lost:
        throw new java.lang.AssertionError();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x012b, code lost:
        r4 = r5.iterateChildren();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x0133, code lost:
        if (r4.hasNext() == false) goto L_0x0153;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x0135, code lost:
        r0 = (com.adobe.xmp.impl.XMPNode) r4.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x013b, code lost:
        if (r0 == r3) goto L_0x012f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x013d, code lost:
        r6 = r0.getValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0141, code lost:
        if (r3 == null) goto L_0x0151;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x0143, code lost:
        r1 = r3.getValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x014b, code lost:
        if (r6.equals(r1) == false) goto L_0x012f;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x014d, code lost:
        r0.setValue(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0151, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x0153, code lost:
        if (r3 == null) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x0155, code lost:
        r3.setValue(r14);
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x015a, code lost:
        if (r2 == false) goto L_0x0171;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x015c, code lost:
        if (r3 == r0) goto L_0x0171;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x015e, code lost:
        if (r3 == null) goto L_0x0171;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:76:0x016c, code lost:
        if (r3.getValue().equals(r0.getValue()) == false) goto L_0x0171;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x016e, code lost:
        r3.setValue(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0171, code lost:
        r0.setValue(r14);
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0177, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x017a, code lost:
        if (r1 == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x017c, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x017f, code lost:
        if (r3 == null) goto L_0x018b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:84:0x0186, code lost:
        if (r5.getChildrenLength() != 1) goto L_0x018b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0188, code lost:
        r3.setValue(r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x018b, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14);
        r0 = r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0191, code lost:
        com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x0194, code lost:
        if (r1 == false) goto L_0x0199;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x0196, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0199, code lost:
        r0 = r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setLocalizedText(java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.String r13, java.lang.String r14, com.adobe.xmp.options.PropertyOptions r15) throws com.adobe.xmp.XMPException {
        /*
            r9 = this;
            com.adobe.xmp.impl.ParameterAsserts.assertSchemaNS(r10)
            com.adobe.xmp.impl.ParameterAsserts.assertArrayName(r11)
            com.adobe.xmp.impl.ParameterAsserts.assertSpecificLang(r13)
            if (r12 == 0) goto L_0x0033
            java.lang.String r0 = com.adobe.xmp.impl.Utils.normalizeLangValue(r12)
            r1 = r0
        L_0x0010:
            java.lang.String r4 = com.adobe.xmp.impl.Utils.normalizeLangValue(r13)
            com.adobe.xmp.impl.xpath.XMPPath r0 = com.adobe.xmp.impl.xpath.XMPPathParser.expandXPath(r10, r11)
            com.adobe.xmp.impl.XMPNode r2 = r9.tree
            r3 = 1
            com.adobe.xmp.options.PropertyOptions r5 = new com.adobe.xmp.options.PropertyOptions
            r6 = 7680(0x1e00, float:1.0762E-41)
            r5.<init>(r6)
            com.adobe.xmp.impl.XMPNode r5 = com.adobe.xmp.impl.XMPNodeUtils.findNode(r2, r0, r3, r5)
            if (r5 != 0) goto L_0x0036
            com.adobe.xmp.XMPException r0 = new com.adobe.xmp.XMPException
            java.lang.String r1 = "Failed to find or create array node"
            r2 = 102(0x66, float:1.43E-43)
            r0.<init>(r1, r2)
            throw r0
        L_0x0033:
            r0 = 0
            r1 = r0
            goto L_0x0010
        L_0x0036:
            com.adobe.xmp.options.PropertyOptions r0 = r5.getOptions()
            boolean r0 = r0.isArrayAltText()
            if (r0 != 0) goto L_0x0058
            boolean r0 = r5.hasChildren()
            if (r0 != 0) goto L_0x008d
            com.adobe.xmp.options.PropertyOptions r0 = r5.getOptions()
            boolean r0 = r0.isArrayAlternate()
            if (r0 == 0) goto L_0x008d
            com.adobe.xmp.options.PropertyOptions r0 = r5.getOptions()
            r2 = 1
            r0.setArrayAltText(r2)
        L_0x0058:
            r2 = 0
            r3 = 0
            java.util.Iterator r6 = r5.iterateChildren()
        L_0x005e:
            boolean r0 = r6.hasNext()
            if (r0 == 0) goto L_0x00ac
            java.lang.Object r0 = r6.next()
            com.adobe.xmp.impl.XMPNode r0 = (com.adobe.xmp.impl.XMPNode) r0
            boolean r7 = r0.hasQualifier()
            if (r7 == 0) goto L_0x0082
            java.lang.String r7 = "xml:lang"
            r8 = 1
            com.adobe.xmp.impl.XMPNode r8 = r0.getQualifier(r8)
            java.lang.String r8 = r8.getName()
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L_0x0098
        L_0x0082:
            com.adobe.xmp.XMPException r0 = new com.adobe.xmp.XMPException
            java.lang.String r1 = "Language qualifier must be first"
            r2 = 102(0x66, float:1.43E-43)
            r0.<init>(r1, r2)
            throw r0
        L_0x008d:
            com.adobe.xmp.XMPException r0 = new com.adobe.xmp.XMPException
            java.lang.String r1 = "Specified property is no alt-text array"
            r2 = 102(0x66, float:1.43E-43)
            r0.<init>(r1, r2)
            throw r0
        L_0x0098:
            java.lang.String r7 = "x-default"
            r8 = 1
            com.adobe.xmp.impl.XMPNode r8 = r0.getQualifier(r8)
            java.lang.String r8 = r8.getValue()
            boolean r7 = r7.equals(r8)
            if (r7 == 0) goto L_0x005e
            r2 = 1
            r3 = r0
        L_0x00ac:
            if (r3 == 0) goto L_0x00bc
            int r0 = r5.getChildrenLength()
            r6 = 1
            if (r0 <= r6) goto L_0x00bc
            r5.removeChild(r3)
            r0 = 1
            r5.addChild(r0, r3)
        L_0x00bc:
            java.lang.Object[] r1 = com.adobe.xmp.impl.XMPNodeUtils.chooseLocalizedText(r5, r1, r4)
            r0 = 0
            r0 = r1[r0]
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r6 = r0.intValue()
            r0 = 1
            r0 = r1[r0]
            com.adobe.xmp.impl.XMPNode r0 = (com.adobe.xmp.impl.XMPNode) r0
            java.lang.String r1 = "x-default"
            boolean r1 = r1.equals(r4)
            switch(r6) {
                case 0: goto L_0x00e3;
                case 1: goto L_0x00ff;
                case 2: goto L_0x015a;
                case 3: goto L_0x0177;
                case 4: goto L_0x017f;
                case 5: goto L_0x0191;
                default: goto L_0x00d8;
            }
        L_0x00d8:
            com.adobe.xmp.XMPException r0 = new com.adobe.xmp.XMPException
            java.lang.String r1 = "Unexpected result from ChooseLocalizedText"
            r2 = 9
            r0.<init>(r1, r2)
            throw r0
        L_0x00e3:
            java.lang.String r0 = "x-default"
            com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r0, r14)
            r0 = 1
            if (r1 != 0) goto L_0x00ef
            com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14)
        L_0x00ef:
            if (r0 != 0) goto L_0x00fe
            int r0 = r5.getChildrenLength()
            r1 = 1
            if (r0 != r1) goto L_0x00fe
            java.lang.String r0 = "x-default"
            com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r0, r14)
        L_0x00fe:
            return
        L_0x00ff:
            if (r1 != 0) goto L_0x011d
            if (r2 == 0) goto L_0x0118
            if (r3 == r0) goto L_0x0118
            if (r3 == 0) goto L_0x0118
            java.lang.String r1 = r3.getValue()
            java.lang.String r4 = r0.getValue()
            boolean r1 = r1.equals(r4)
            if (r1 == 0) goto L_0x0118
            r3.setValue(r14)
        L_0x0118:
            r0.setValue(r14)
            r0 = r2
            goto L_0x00ef
        L_0x011d:
            boolean r1 = com.adobe.xmp.impl.XMPMetaImpl.$assertionsDisabled
            if (r1 != 0) goto L_0x012b
            if (r2 == 0) goto L_0x0125
            if (r3 == r0) goto L_0x012b
        L_0x0125:
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        L_0x012b:
            java.util.Iterator r4 = r5.iterateChildren()
        L_0x012f:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x0153
            java.lang.Object r0 = r4.next()
            com.adobe.xmp.impl.XMPNode r0 = (com.adobe.xmp.impl.XMPNode) r0
            if (r0 == r3) goto L_0x012f
            java.lang.String r6 = r0.getValue()
            if (r3 == 0) goto L_0x0151
            java.lang.String r1 = r3.getValue()
        L_0x0147:
            boolean r1 = r6.equals(r1)
            if (r1 == 0) goto L_0x012f
            r0.setValue(r14)
            goto L_0x012f
        L_0x0151:
            r1 = 0
            goto L_0x0147
        L_0x0153:
            if (r3 == 0) goto L_0x0199
            r3.setValue(r14)
            r0 = r2
            goto L_0x00ef
        L_0x015a:
            if (r2 == 0) goto L_0x0171
            if (r3 == r0) goto L_0x0171
            if (r3 == 0) goto L_0x0171
            java.lang.String r1 = r3.getValue()
            java.lang.String r4 = r0.getValue()
            boolean r1 = r1.equals(r4)
            if (r1 == 0) goto L_0x0171
            r3.setValue(r14)
        L_0x0171:
            r0.setValue(r14)
            r0 = r2
            goto L_0x00ef
        L_0x0177:
            com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14)
            if (r1 == 0) goto L_0x0199
            r0 = 1
            goto L_0x00ef
        L_0x017f:
            if (r3 == 0) goto L_0x018b
            int r0 = r5.getChildrenLength()
            r1 = 1
            if (r0 != r1) goto L_0x018b
            r3.setValue(r14)
        L_0x018b:
            com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14)
            r0 = r2
            goto L_0x00ef
        L_0x0191:
            com.adobe.xmp.impl.XMPNodeUtils.appendLangItem(r5, r4, r14)
            if (r1 == 0) goto L_0x0199
            r0 = 1
            goto L_0x00ef
        L_0x0199:
            r0 = r2
            goto L_0x00ef
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.XMPMetaImpl.setLocalizedText(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.adobe.xmp.options.PropertyOptions):void");
    }

    /* access modifiers changed from: package-private */
    public void setNode(XMPNode xMPNode, Object obj, PropertyOptions propertyOptions, boolean z) throws XMPException {
        if (z) {
            xMPNode.clear();
        }
        xMPNode.getOptions().mergeWith(propertyOptions);
        if (!xMPNode.getOptions().isCompositeProperty()) {
            XMPNodeUtils.setNodeValue(xMPNode, obj);
        } else if (obj == null || obj.toString().length() <= 0) {
            xMPNode.removeChildren();
        } else {
            throw new XMPException("Composite nodes can't have values", 102);
        }
    }

    public void setObjectName(String str) {
        this.tree.setName(str);
    }

    public void setPacketHeader(String str) {
        this.packetHeader = str;
    }

    public void setProperty(String str, String str2, Object obj) throws XMPException {
        setProperty(str, str2, obj, null);
    }

    public void setProperty(String str, String str2, Object obj, PropertyOptions propertyOptions) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertPropName(str2);
        PropertyOptions verifySetOptions = XMPNodeUtils.verifySetOptions(propertyOptions, obj);
        XMPNode findNode = XMPNodeUtils.findNode(this.tree, XMPPathParser.expandXPath(str, str2), true, verifySetOptions);
        if (findNode != null) {
            setNode(findNode, obj, verifySetOptions, false);
            return;
        }
        throw new XMPException("Specified property does not exist", 102);
    }

    public void setPropertyBase64(String str, String str2, byte[] bArr) throws XMPException {
        setProperty(str, str2, bArr, null);
    }

    public void setPropertyBase64(String str, String str2, byte[] bArr, PropertyOptions propertyOptions) throws XMPException {
        setProperty(str, str2, bArr, propertyOptions);
    }

    public void setPropertyBoolean(String str, String str2, boolean z) throws XMPException {
        setProperty(str, str2, z ? XMPConst.TRUESTR : XMPConst.FALSESTR, null);
    }

    public void setPropertyBoolean(String str, String str2, boolean z, PropertyOptions propertyOptions) throws XMPException {
        setProperty(str, str2, z ? XMPConst.TRUESTR : XMPConst.FALSESTR, propertyOptions);
    }

    public void setPropertyCalendar(String str, String str2, Calendar calendar) throws XMPException {
        setProperty(str, str2, calendar, null);
    }

    public void setPropertyCalendar(String str, String str2, Calendar calendar, PropertyOptions propertyOptions) throws XMPException {
        setProperty(str, str2, calendar, propertyOptions);
    }

    public void setPropertyDate(String str, String str2, XMPDateTime xMPDateTime) throws XMPException {
        setProperty(str, str2, xMPDateTime, null);
    }

    public void setPropertyDate(String str, String str2, XMPDateTime xMPDateTime, PropertyOptions propertyOptions) throws XMPException {
        setProperty(str, str2, xMPDateTime, propertyOptions);
    }

    public void setPropertyDouble(String str, String str2, double d) throws XMPException {
        setProperty(str, str2, new Double(d), null);
    }

    public void setPropertyDouble(String str, String str2, double d, PropertyOptions propertyOptions) throws XMPException {
        setProperty(str, str2, new Double(d), propertyOptions);
    }

    public void setPropertyInteger(String str, String str2, int i) throws XMPException {
        setProperty(str, str2, new Integer(i), null);
    }

    public void setPropertyInteger(String str, String str2, int i, PropertyOptions propertyOptions) throws XMPException {
        setProperty(str, str2, new Integer(i), propertyOptions);
    }

    public void setPropertyLong(String str, String str2, long j) throws XMPException {
        setProperty(str, str2, new Long(j), null);
    }

    public void setPropertyLong(String str, String str2, long j, PropertyOptions propertyOptions) throws XMPException {
        setProperty(str, str2, new Long(j), propertyOptions);
    }

    public void setQualifier(String str, String str2, String str3, String str4, String str5) throws XMPException {
        setQualifier(str, str2, str3, str4, str5, null);
    }

    public void setQualifier(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertPropName(str2);
        if (!doesPropertyExist(str, str2)) {
            throw new XMPException("Specified property does not exist!", 102);
        }
        setProperty(str, str2 + XMPPathFactory.composeQualifierPath(str3, str4), str5, propertyOptions);
    }

    public void setStructField(String str, String str2, String str3, String str4, String str5) throws XMPException {
        setStructField(str, str2, str3, str4, str5, null);
    }

    public void setStructField(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertStructName(str2);
        setProperty(str, str2 + XMPPathFactory.composeStructFieldPath(str3, str4), str5, propertyOptions);
    }

    public void sort() {
        this.tree.sort();
    }
}
