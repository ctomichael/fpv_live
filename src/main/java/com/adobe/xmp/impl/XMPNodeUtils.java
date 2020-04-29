package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPDateTime;
import com.adobe.xmp.XMPDateTimeFactory;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPUtils;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathSegment;
import com.adobe.xmp.options.PropertyOptions;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class XMPNodeUtils implements XMPConst {
    static final /* synthetic */ boolean $assertionsDisabled = (!XMPNodeUtils.class.desiredAssertionStatus());
    static final int CLT_FIRST_ITEM = 5;
    static final int CLT_MULTIPLE_GENERIC = 3;
    static final int CLT_NO_VALUES = 0;
    static final int CLT_SINGLE_GENERIC = 2;
    static final int CLT_SPECIFIC_MATCH = 1;
    static final int CLT_XDEFAULT = 4;

    private XMPNodeUtils() {
    }

    static void appendLangItem(XMPNode xMPNode, String str, String str2) throws XMPException {
        XMPNode xMPNode2 = new XMPNode(XMPConst.ARRAY_ITEM_NAME, str2, null);
        XMPNode xMPNode3 = new XMPNode(XMPConst.XML_LANG, str, null);
        xMPNode2.addQualifier(xMPNode3);
        if (!XMPConst.X_DEFAULT.equals(xMPNode3.getValue())) {
            xMPNode.addChild(xMPNode2);
        } else {
            xMPNode.addChild(1, xMPNode2);
        }
    }

    static Object[] chooseLocalizedText(XMPNode xMPNode, String str, String str2) throws XMPException {
        if (!xMPNode.getOptions().isArrayAltText()) {
            throw new XMPException("Localized text array is not alt-text", 102);
        } else if (!xMPNode.hasChildren()) {
            return new Object[]{new Integer(0), null};
        } else {
            Iterator iterateChildren = xMPNode.iterateChildren();
            XMPNode xMPNode2 = null;
            XMPNode xMPNode3 = null;
            int i = 0;
            while (iterateChildren.hasNext()) {
                XMPNode xMPNode4 = (XMPNode) iterateChildren.next();
                if (xMPNode4.getOptions().isCompositeProperty()) {
                    throw new XMPException("Alt-text array item is not simple", 102);
                } else if (!xMPNode4.hasQualifier() || !XMPConst.XML_LANG.equals(xMPNode4.getQualifier(1).getName())) {
                    throw new XMPException("Alt-text array item has no language qualifier", 102);
                } else {
                    String value = xMPNode4.getQualifier(1).getValue();
                    if (str2.equals(value)) {
                        return new Object[]{new Integer(1), xMPNode4};
                    }
                    if (str != null && value.startsWith(str)) {
                        if (xMPNode3 == null) {
                            xMPNode3 = xMPNode4;
                        }
                        i++;
                        xMPNode4 = xMPNode2;
                    } else if (!XMPConst.X_DEFAULT.equals(value)) {
                        xMPNode4 = xMPNode2;
                    }
                    xMPNode2 = xMPNode4;
                }
            }
            if (i == 1) {
                return new Object[]{new Integer(2), xMPNode3};
            } else if (i > 1) {
                return new Object[]{new Integer(3), xMPNode3};
            } else if (xMPNode2 != null) {
                return new Object[]{new Integer(4), xMPNode2};
            } else {
                return new Object[]{new Integer(5), xMPNode.getChild(1)};
            }
        }
    }

    static void deleteNode(XMPNode xMPNode) {
        XMPNode parent = xMPNode.getParent();
        if (xMPNode.getOptions().isQualifier()) {
            parent.removeQualifier(xMPNode);
        } else {
            parent.removeChild(xMPNode);
        }
        if (!parent.hasChildren() && parent.getOptions().isSchemaNode()) {
            parent.getParent().removeChild(parent);
        }
    }

    static void detectAltText(XMPNode xMPNode) {
        boolean z;
        if (xMPNode.getOptions().isArrayAlternate() && xMPNode.hasChildren()) {
            Iterator iterateChildren = xMPNode.iterateChildren();
            while (true) {
                if (iterateChildren.hasNext()) {
                    if (((XMPNode) iterateChildren.next()).getOptions().getHasLanguage()) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (z) {
                xMPNode.getOptions().setArrayAltText(true);
                normalizeLangArray(xMPNode);
            }
        }
    }

    static XMPNode findChildNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        if (!xMPNode.getOptions().isSchemaNode() && !xMPNode.getOptions().isStruct()) {
            if (!xMPNode.isImplicit()) {
                throw new XMPException("Named children only allowed for schemas and structs", 102);
            } else if (xMPNode.getOptions().isArray()) {
                throw new XMPException("Named children not allowed for arrays", 102);
            } else if (z) {
                xMPNode.getOptions().setStruct(true);
            }
        }
        XMPNode findChildByName = xMPNode.findChildByName(str);
        if (findChildByName == null && z) {
            findChildByName = new XMPNode(str, new PropertyOptions());
            findChildByName.setImplicit(true);
            xMPNode.addChild(findChildByName);
        }
        if ($assertionsDisabled || findChildByName != null || !z) {
            return findChildByName;
        }
        throw new AssertionError();
    }

    private static int findIndexedItem(XMPNode xMPNode, String str, boolean z) throws XMPException {
        try {
            int parseInt = Integer.parseInt(str.substring(1, str.length() - 1));
            if (parseInt < 1) {
                throw new XMPException("Array index must be larger than zero", 102);
            }
            if (z && parseInt == xMPNode.getChildrenLength() + 1) {
                XMPNode xMPNode2 = new XMPNode(XMPConst.ARRAY_ITEM_NAME, null);
                xMPNode2.setImplicit(true);
                xMPNode.addChild(xMPNode2);
            }
            return parseInt;
        } catch (NumberFormatException e) {
            throw new XMPException("Array index not digits.", 102);
        }
    }

    static XMPNode findNode(XMPNode xMPNode, XMPPath xMPPath, boolean z, PropertyOptions propertyOptions) throws XMPException {
        XMPNode xMPNode2;
        if (xMPPath == null || xMPPath.size() == 0) {
            throw new XMPException("Empty XMPPath", 102);
        }
        XMPNode findSchemaNode = findSchemaNode(xMPNode, xMPPath.getSegment(0).getName(), z);
        if (findSchemaNode == null) {
            return null;
        }
        if (findSchemaNode.isImplicit()) {
            findSchemaNode.setImplicit(false);
            xMPNode2 = findSchemaNode;
        } else {
            xMPNode2 = null;
        }
        int i = 1;
        while (i < xMPPath.size()) {
            try {
                findSchemaNode = followXPathStep(findSchemaNode, xMPPath.getSegment(i), z);
                if (findSchemaNode != null) {
                    if (findSchemaNode.isImplicit()) {
                        findSchemaNode.setImplicit(false);
                        if (i == 1 && xMPPath.getSegment(i).isAlias() && xMPPath.getSegment(i).getAliasForm() != 0) {
                            findSchemaNode.getOptions().setOption(xMPPath.getSegment(i).getAliasForm(), true);
                        } else if (i < xMPPath.size() - 1 && xMPPath.getSegment(i).getKind() == 1 && !findSchemaNode.getOptions().isCompositeProperty()) {
                            findSchemaNode.getOptions().setStruct(true);
                        }
                        if (xMPNode2 == null) {
                            xMPNode2 = findSchemaNode;
                        }
                    }
                    i++;
                } else if (!z) {
                    return null;
                } else {
                    deleteNode(xMPNode2);
                    return null;
                }
            } catch (XMPException e) {
                if (xMPNode2 != null) {
                    deleteNode(xMPNode2);
                }
                throw e;
            }
        }
        if (xMPNode2 != null) {
            findSchemaNode.getOptions().mergeWith(propertyOptions);
            findSchemaNode.setOptions(findSchemaNode.getOptions());
        }
        return findSchemaNode;
    }

    private static XMPNode findQualifierNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        if ($assertionsDisabled || !str.startsWith("?")) {
            XMPNode findQualifierByName = xMPNode.findQualifierByName(str);
            if (findQualifierByName != null || !z) {
                return findQualifierByName;
            }
            XMPNode xMPNode2 = new XMPNode(str, null);
            xMPNode2.setImplicit(true);
            xMPNode.addQualifier(xMPNode2);
            return xMPNode2;
        }
        throw new AssertionError();
    }

    static XMPNode findSchemaNode(XMPNode xMPNode, String str, String str2, boolean z) throws XMPException {
        if ($assertionsDisabled || xMPNode.getParent() == null) {
            XMPNode findChildByName = xMPNode.findChildByName(str);
            if (findChildByName != null || !z) {
                return findChildByName;
            }
            XMPNode xMPNode2 = new XMPNode(str, new PropertyOptions().setSchemaNode(true));
            xMPNode2.setImplicit(true);
            String namespacePrefix = XMPMetaFactory.getSchemaRegistry().getNamespacePrefix(str);
            if (namespacePrefix == null) {
                if (str2 == null || str2.length() == 0) {
                    throw new XMPException("Unregistered schema namespace URI", 101);
                }
                namespacePrefix = XMPMetaFactory.getSchemaRegistry().registerNamespace(str, str2);
            }
            xMPNode2.setValue(namespacePrefix);
            xMPNode.addChild(xMPNode2);
            return xMPNode2;
        }
        throw new AssertionError();
    }

    static XMPNode findSchemaNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        return findSchemaNode(xMPNode, str, null, z);
    }

    private static XMPNode followXPathStep(XMPNode xMPNode, XMPPathSegment xMPPathSegment, boolean z) throws XMPException {
        int lookupQualSelector;
        int kind = xMPPathSegment.getKind();
        if (kind == 1) {
            return findChildNode(xMPNode, xMPPathSegment.getName(), z);
        }
        if (kind == 2) {
            return findQualifierNode(xMPNode, xMPPathSegment.getName().substring(1), z);
        }
        if (!xMPNode.getOptions().isArray()) {
            throw new XMPException("Indexing applied to non-array", 102);
        }
        if (kind == 3) {
            lookupQualSelector = findIndexedItem(xMPNode, xMPPathSegment.getName(), z);
        } else if (kind == 4) {
            lookupQualSelector = xMPNode.getChildrenLength();
        } else if (kind == 6) {
            String[] splitNameAndValue = Utils.splitNameAndValue(xMPPathSegment.getName());
            lookupQualSelector = lookupFieldSelector(xMPNode, splitNameAndValue[0], splitNameAndValue[1]);
        } else if (kind == 5) {
            String[] splitNameAndValue2 = Utils.splitNameAndValue(xMPPathSegment.getName());
            lookupQualSelector = lookupQualSelector(xMPNode, splitNameAndValue2[0], splitNameAndValue2[1], xMPPathSegment.getAliasForm());
        } else {
            throw new XMPException("Unknown array indexing step in FollowXPathStep", 9);
        }
        if (1 > lookupQualSelector || lookupQualSelector > xMPNode.getChildrenLength()) {
            return null;
        }
        return xMPNode.getChild(lookupQualSelector);
    }

    private static int lookupFieldSelector(XMPNode xMPNode, String str, String str2) throws XMPException {
        int i = -1;
        for (int i2 = 1; i2 <= xMPNode.getChildrenLength() && i < 0; i2++) {
            XMPNode child = xMPNode.getChild(i2);
            if (!child.getOptions().isStruct()) {
                throw new XMPException("Field selector must be used on array of struct", 102);
            }
            int i3 = 1;
            while (true) {
                if (i3 > child.getChildrenLength()) {
                    break;
                }
                XMPNode child2 = child.getChild(i3);
                if (str.equals(child2.getName()) && str2.equals(child2.getValue())) {
                    i = i2;
                    break;
                }
                i3++;
            }
        }
        return i;
    }

    static int lookupLanguageItem(XMPNode xMPNode, String str) throws XMPException {
        if (!xMPNode.getOptions().isArray()) {
            throw new XMPException("Language item must be used on array", 102);
        }
        for (int i = 1; i <= xMPNode.getChildrenLength(); i++) {
            XMPNode child = xMPNode.getChild(i);
            if (child.hasQualifier() && XMPConst.XML_LANG.equals(child.getQualifier(1).getName()) && str.equals(child.getQualifier(1).getValue())) {
                return i;
            }
        }
        return -1;
    }

    private static int lookupQualSelector(XMPNode xMPNode, String str, String str2, int i) throws XMPException {
        int i2 = 1;
        if (XMPConst.XML_LANG.equals(str)) {
            int lookupLanguageItem = lookupLanguageItem(xMPNode, Utils.normalizeLangValue(str2));
            if (lookupLanguageItem >= 0 || (i & 4096) <= 0) {
                return lookupLanguageItem;
            }
            XMPNode xMPNode2 = new XMPNode(XMPConst.ARRAY_ITEM_NAME, null);
            xMPNode2.addQualifier(new XMPNode(XMPConst.XML_LANG, XMPConst.X_DEFAULT, null));
            xMPNode.addChild(1, xMPNode2);
            return 1;
        }
        while (true) {
            int i3 = i2;
            if (i3 >= xMPNode.getChildrenLength()) {
                return -1;
            }
            Iterator iterateQualifier = xMPNode.getChild(i3).iterateQualifier();
            while (iterateQualifier.hasNext()) {
                XMPNode xMPNode3 = (XMPNode) iterateQualifier.next();
                if (str.equals(xMPNode3.getName()) && str2.equals(xMPNode3.getValue())) {
                    return i3;
                }
            }
            i2 = i3 + 1;
        }
    }

    static void normalizeLangArray(XMPNode xMPNode) {
        if (xMPNode.getOptions().isArrayAltText()) {
            int i = 2;
            while (i <= xMPNode.getChildrenLength()) {
                XMPNode child = xMPNode.getChild(i);
                if (!child.hasQualifier() || !XMPConst.X_DEFAULT.equals(child.getQualifier(1).getValue())) {
                    i++;
                } else {
                    try {
                        xMPNode.removeChild(i);
                        xMPNode.addChild(1, child);
                    } catch (XMPException e) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    }
                    if (i == 2) {
                        xMPNode.getChild(2).setValue(child.getValue());
                        return;
                    }
                    return;
                }
            }
        }
    }

    static String serializeNodeValue(Object obj) {
        String convertFromBoolean = obj == null ? null : obj instanceof Boolean ? XMPUtils.convertFromBoolean(((Boolean) obj).booleanValue()) : obj instanceof Integer ? XMPUtils.convertFromInteger(((Integer) obj).intValue()) : obj instanceof Long ? XMPUtils.convertFromLong(((Long) obj).longValue()) : obj instanceof Double ? XMPUtils.convertFromDouble(((Double) obj).doubleValue()) : obj instanceof XMPDateTime ? XMPUtils.convertFromDate((XMPDateTime) obj) : obj instanceof GregorianCalendar ? XMPUtils.convertFromDate(XMPDateTimeFactory.createFromCalendar((GregorianCalendar) obj)) : obj instanceof byte[] ? XMPUtils.encodeBase64((byte[]) obj) : obj.toString();
        if (convertFromBoolean != null) {
            return Utils.removeControlChars(convertFromBoolean);
        }
        return null;
    }

    static void setNodeValue(XMPNode xMPNode, Object obj) {
        String serializeNodeValue = serializeNodeValue(obj);
        if (!xMPNode.getOptions().isQualifier() || !XMPConst.XML_LANG.equals(xMPNode.getName())) {
            xMPNode.setValue(serializeNodeValue);
        } else {
            xMPNode.setValue(Utils.normalizeLangValue(serializeNodeValue));
        }
    }

    static PropertyOptions verifySetOptions(PropertyOptions propertyOptions, Object obj) throws XMPException {
        if (propertyOptions == null) {
            propertyOptions = new PropertyOptions();
        }
        if (propertyOptions.isArrayAltText()) {
            propertyOptions.setArrayAlternate(true);
        }
        if (propertyOptions.isArrayAlternate()) {
            propertyOptions.setArrayOrdered(true);
        }
        if (propertyOptions.isArrayOrdered()) {
            propertyOptions.setArray(true);
        }
        if (!propertyOptions.isCompositeProperty() || obj == null || obj.toString().length() <= 0) {
            propertyOptions.assertConsistency(propertyOptions.getOptions());
            return propertyOptions;
        }
        throw new XMPException("Structs and arrays can't have values", 103);
    }
}
