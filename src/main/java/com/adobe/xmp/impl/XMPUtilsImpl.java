package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathParser;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.properties.XMPAliasInfo;
import com.drew.metadata.exif.makernotes.SonyType1MakernoteDirectory;
import java.util.Iterator;
import kotlin.text.Typography;

public class XMPUtilsImpl implements XMPConst {
    static final /* synthetic */ boolean $assertionsDisabled = (!XMPUtilsImpl.class.desiredAssertionStatus());
    private static final String COMMAS = ",，､﹐﹑、،՝";
    private static final String CONTROLS = "  ";
    private static final String QUOTES = "\"«»〝〞〟―‹›";
    private static final String SEMICOLA = ";；﹔؛;";
    private static final String SPACES = " 　〿";
    private static final int UCK_COMMA = 2;
    private static final int UCK_CONTROL = 5;
    private static final int UCK_NORMAL = 0;
    private static final int UCK_QUOTE = 4;
    private static final int UCK_SEMICOLON = 3;
    private static final int UCK_SPACE = 1;

    private XMPUtilsImpl() {
    }

    public static void appendProperties(XMPMeta xMPMeta, XMPMeta xMPMeta2, boolean z, boolean z2, boolean z3) throws XMPException {
        boolean z4;
        XMPNode xMPNode;
        ParameterAsserts.assertImplementation(xMPMeta);
        ParameterAsserts.assertImplementation(xMPMeta2);
        XMPMetaImpl xMPMetaImpl = (XMPMetaImpl) xMPMeta2;
        Iterator iterateChildren = ((XMPMetaImpl) xMPMeta).getRoot().iterateChildren();
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateChildren.next();
            XMPNode findSchemaNode = XMPNodeUtils.findSchemaNode(xMPMetaImpl.getRoot(), xMPNode2.getName(), false);
            if (findSchemaNode == null) {
                XMPNode xMPNode3 = new XMPNode(xMPNode2.getName(), xMPNode2.getValue(), new PropertyOptions().setSchemaNode(true));
                xMPMetaImpl.getRoot().addChild(xMPNode3);
                z4 = true;
                xMPNode = xMPNode3;
            } else {
                z4 = false;
                xMPNode = findSchemaNode;
            }
            Iterator iterateChildren2 = xMPNode2.iterateChildren();
            while (iterateChildren2.hasNext()) {
                XMPNode xMPNode4 = (XMPNode) iterateChildren2.next();
                if (z || !Utils.isInternalProperty(xMPNode2.getName(), xMPNode4.getName())) {
                    appendSubtree(xMPMetaImpl, xMPNode4, xMPNode, z2, z3);
                }
            }
            if (!xMPNode.hasChildren() && (z4 || z3)) {
                xMPMetaImpl.getRoot().removeChild(xMPNode);
            }
        }
    }

    private static void appendSubtree(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, XMPNode xMPNode2, boolean z, boolean z2) throws XMPException {
        XMPNode xMPNode3;
        XMPNode findChildNode = XMPNodeUtils.findChildNode(xMPNode2, xMPNode.getName(), false);
        boolean z3 = z2 ? xMPNode.getOptions().isSimple() ? xMPNode.getValue() == null || xMPNode.getValue().length() == 0 : !xMPNode.hasChildren() : false;
        if (!z2 || !z3) {
            if (findChildNode == null) {
                xMPNode2.addChild((XMPNode) xMPNode.clone());
            } else if (z) {
                xMPMetaImpl.setNode(findChildNode, xMPNode.getValue(), xMPNode.getOptions(), true);
                xMPNode2.removeChild(findChildNode);
                xMPNode2.addChild((XMPNode) xMPNode.clone());
            } else {
                PropertyOptions options = xMPNode.getOptions();
                if (options != findChildNode.getOptions()) {
                    return;
                }
                if (options.isStruct()) {
                    Iterator iterateChildren = xMPNode.iterateChildren();
                    while (iterateChildren.hasNext()) {
                        appendSubtree(xMPMetaImpl, (XMPNode) iterateChildren.next(), findChildNode, z, z2);
                        if (z2 && !findChildNode.hasChildren()) {
                            xMPNode2.removeChild(findChildNode);
                        }
                    }
                } else if (options.isArrayAltText()) {
                    Iterator iterateChildren2 = xMPNode.iterateChildren();
                    while (iterateChildren2.hasNext()) {
                        XMPNode xMPNode4 = (XMPNode) iterateChildren2.next();
                        if (xMPNode4.hasQualifier() && XMPConst.XML_LANG.equals(xMPNode4.getQualifier(1).getName())) {
                            int lookupLanguageItem = XMPNodeUtils.lookupLanguageItem(findChildNode, xMPNode4.getQualifier(1).getValue());
                            if (!z2 || !(xMPNode4.getValue() == null || xMPNode4.getValue().length() == 0)) {
                                if (lookupLanguageItem == -1) {
                                    if (!XMPConst.X_DEFAULT.equals(xMPNode4.getQualifier(1).getValue()) || !findChildNode.hasChildren()) {
                                        xMPNode4.cloneSubtree(findChildNode);
                                    } else {
                                        XMPNode xMPNode5 = new XMPNode(xMPNode4.getName(), xMPNode4.getValue(), xMPNode4.getOptions());
                                        xMPNode4.cloneSubtree(xMPNode5);
                                        findChildNode.addChild(1, xMPNode5);
                                    }
                                }
                            } else if (lookupLanguageItem != -1) {
                                findChildNode.removeChild(lookupLanguageItem);
                                if (!findChildNode.hasChildren()) {
                                    xMPNode2.removeChild(findChildNode);
                                }
                            }
                        }
                    }
                } else if (options.isArray()) {
                    Iterator iterateChildren3 = xMPNode.iterateChildren();
                    XMPNode xMPNode6 = findChildNode;
                    while (iterateChildren3.hasNext()) {
                        XMPNode xMPNode7 = (XMPNode) iterateChildren3.next();
                        Iterator iterateChildren4 = xMPNode6.iterateChildren();
                        boolean z4 = false;
                        while (iterateChildren4.hasNext()) {
                            z4 = itemValuesMatch(xMPNode7, (XMPNode) iterateChildren4.next()) ? true : z4;
                        }
                        if (!z4) {
                            xMPNode3 = (XMPNode) xMPNode7.clone();
                            xMPNode2.addChild(xMPNode3);
                        } else {
                            xMPNode3 = xMPNode6;
                        }
                        xMPNode6 = xMPNode3;
                    }
                }
            }
        } else if (findChildNode != null) {
            xMPNode2.removeChild(findChildNode);
        }
    }

    private static String applyQuotes(String str, char c, char c2, boolean z) {
        if (str == null) {
            str = "";
        }
        int i = 0;
        boolean z2 = false;
        while (i < str.length()) {
            int classifyCharacter = classifyCharacter(str.charAt(i));
            if (i == 0 && classifyCharacter == 4) {
                break;
            }
            if (classifyCharacter != 1) {
                if (classifyCharacter == 3 || classifyCharacter == 5 || (classifyCharacter == 2 && !z)) {
                    break;
                }
                z2 = false;
            } else if (z2) {
                break;
            } else {
                z2 = true;
            }
            i++;
        }
        if (i >= str.length()) {
            return str;
        }
        StringBuffer stringBuffer = new StringBuffer(str.length() + 2);
        int i2 = 0;
        while (i2 <= i && classifyCharacter(str.charAt(i)) != 4) {
            i2++;
        }
        stringBuffer.append(c).append(str.substring(0, i2));
        for (int i3 = i2; i3 < str.length(); i3++) {
            stringBuffer.append(str.charAt(i3));
            if (classifyCharacter(str.charAt(i3)) == 4 && isSurroundingQuote(str.charAt(i3), c, c2)) {
                stringBuffer.append(str.charAt(i3));
            }
        }
        stringBuffer.append(c2);
        return stringBuffer.toString();
    }

    public static String catenateArrayItems(XMPMeta xMPMeta, String str, String str2, String str3, String str4, boolean z) throws XMPException {
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        ParameterAsserts.assertImplementation(xMPMeta);
        if (str3 == null || str3.length() == 0) {
            str3 = "; ";
        }
        if (str4 == null || str4.length() == 0) {
            str4 = "\"";
        }
        XMPNode findNode = XMPNodeUtils.findNode(((XMPMetaImpl) xMPMeta).getRoot(), XMPPathParser.expandXPath(str, str2), false, null);
        if (findNode == null) {
            return "";
        }
        if (!findNode.getOptions().isArray() || findNode.getOptions().isArrayAlternate()) {
            throw new XMPException("Named property must be non-alternate array", 4);
        }
        checkSeparator(str3);
        char charAt = str4.charAt(0);
        char checkQuotes = checkQuotes(str4, charAt);
        StringBuffer stringBuffer = new StringBuffer();
        Iterator iterateChildren = findNode.iterateChildren();
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode = (XMPNode) iterateChildren.next();
            if (xMPNode.getOptions().isCompositeProperty()) {
                throw new XMPException("Array items must be simple", 4);
            }
            stringBuffer.append(applyQuotes(xMPNode.getValue(), charAt, checkQuotes, z));
            if (iterateChildren.hasNext()) {
                stringBuffer.append(str3);
            }
        }
        return stringBuffer.toString();
    }

    private static char checkQuotes(String str, char c) throws XMPException {
        char charAt;
        if (classifyCharacter(c) != 4) {
            throw new XMPException("Invalid quoting character", 4);
        }
        if (str.length() == 1) {
            charAt = c;
        } else {
            charAt = str.charAt(1);
            if (classifyCharacter(charAt) != 4) {
                throw new XMPException("Invalid quoting character", 4);
            }
        }
        if (charAt == getClosingQuote(c)) {
            return charAt;
        }
        throw new XMPException("Mismatched quote pair", 4);
    }

    private static void checkSeparator(String str) throws XMPException {
        boolean z = false;
        int i = 0;
        while (true) {
            boolean z2 = z;
            if (i < str.length()) {
                int classifyCharacter = classifyCharacter(str.charAt(i));
                if (classifyCharacter == 3) {
                    if (z2) {
                        throw new XMPException("Separator can have only one semicolon", 4);
                    }
                    z = true;
                } else if (classifyCharacter != 1) {
                    throw new XMPException("Separator can have only spaces and one semicolon", 4);
                } else {
                    z = z2;
                }
                i++;
            } else if (!z2) {
                throw new XMPException("Separator must have one semicolon", 4);
            } else {
                return;
            }
        }
    }

    private static int classifyCharacter(char c) {
        if (SPACES.indexOf(c) >= 0 || (8192 <= c && c <= 8203)) {
            return 1;
        }
        if (COMMAS.indexOf(c) >= 0) {
            return 2;
        }
        if (SEMICOLA.indexOf(c) >= 0) {
            return 3;
        }
        if (QUOTES.indexOf(c) >= 0 || ((12296 <= c && c <= 12303) || (8216 <= c && c <= 8223))) {
            return 4;
        }
        return (c < ' ' || CONTROLS.indexOf(c) >= 0) ? 5 : 0;
    }

    private static char getClosingQuote(char c) {
        switch (c) {
            case '\"':
                return Typography.quote;
            case 171:
                return Typography.rightGuillemete;
            case 187:
                return Typography.leftGuillemete;
            case 8213:
                return 8213;
            case 8216:
                return Typography.rightSingleQuote;
            case 8218:
                return 8219;
            case 8220:
                return Typography.rightDoubleQuote;
            case SonyType1MakernoteDirectory.TAG_AF_POINT_SELECTED:
                return 8223;
            case 8249:
                return 8250;
            case 8250:
                return 8249;
            case 12296:
                return 12297;
            case 12298:
                return 12299;
            case 12300:
                return 12301;
            case 12302:
                return 12303;
            case 12317:
                return 12319;
            default:
                return 0;
        }
    }

    private static boolean isClosingingQuote(char c, char c2, char c3) {
        return c == c3 || (c2 == 12317 && c == 12318) || c == 12319;
    }

    private static boolean isSurroundingQuote(char c, char c2, char c3) {
        return c == c2 || isClosingingQuote(c, c2, c3);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean itemValuesMatch(com.adobe.xmp.impl.XMPNode r6, com.adobe.xmp.impl.XMPNode r7) throws com.adobe.xmp.XMPException {
        /*
            r3 = 1
            r2 = 0
            com.adobe.xmp.options.PropertyOptions r0 = r6.getOptions()
            com.adobe.xmp.options.PropertyOptions r1 = r7.getOptions()
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto L_0x0012
            r0 = r2
        L_0x0011:
            return r0
        L_0x0012:
            int r1 = r0.getOptions()
            if (r1 != 0) goto L_0x005e
            java.lang.String r0 = r6.getValue()
            java.lang.String r1 = r7.getValue()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x0028
            r0 = r2
            goto L_0x0011
        L_0x0028:
            com.adobe.xmp.options.PropertyOptions r0 = r6.getOptions()
            boolean r0 = r0.getHasLanguage()
            com.adobe.xmp.options.PropertyOptions r1 = r7.getOptions()
            boolean r1 = r1.getHasLanguage()
            if (r0 == r1) goto L_0x003c
            r0 = r2
            goto L_0x0011
        L_0x003c:
            com.adobe.xmp.options.PropertyOptions r0 = r6.getOptions()
            boolean r0 = r0.getHasLanguage()
            if (r0 == 0) goto L_0x00ce
            com.adobe.xmp.impl.XMPNode r0 = r6.getQualifier(r3)
            java.lang.String r0 = r0.getValue()
            com.adobe.xmp.impl.XMPNode r1 = r7.getQualifier(r3)
            java.lang.String r1 = r1.getValue()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x00ce
            r0 = r2
            goto L_0x0011
        L_0x005e:
            boolean r1 = r0.isStruct()
            if (r1 == 0) goto L_0x0092
            int r0 = r6.getChildrenLength()
            int r1 = r7.getChildrenLength()
            if (r0 == r1) goto L_0x0070
            r0 = r2
            goto L_0x0011
        L_0x0070:
            java.util.Iterator r1 = r6.iterateChildren()
        L_0x0074:
            boolean r0 = r1.hasNext()
            if (r0 == 0) goto L_0x00ce
            java.lang.Object r0 = r1.next()
            com.adobe.xmp.impl.XMPNode r0 = (com.adobe.xmp.impl.XMPNode) r0
            java.lang.String r4 = r0.getName()
            com.adobe.xmp.impl.XMPNode r4 = com.adobe.xmp.impl.XMPNodeUtils.findChildNode(r7, r4, r2)
            if (r4 == 0) goto L_0x0090
            boolean r0 = itemValuesMatch(r0, r4)
            if (r0 != 0) goto L_0x0074
        L_0x0090:
            r0 = r2
            goto L_0x0011
        L_0x0092:
            boolean r1 = com.adobe.xmp.impl.XMPUtilsImpl.$assertionsDisabled
            if (r1 != 0) goto L_0x00a2
            boolean r0 = r0.isArray()
            if (r0 != 0) goto L_0x00a2
            java.lang.AssertionError r0 = new java.lang.AssertionError
            r0.<init>()
            throw r0
        L_0x00a2:
            java.util.Iterator r4 = r6.iterateChildren()
        L_0x00a6:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x00ce
            java.lang.Object r0 = r4.next()
            com.adobe.xmp.impl.XMPNode r0 = (com.adobe.xmp.impl.XMPNode) r0
            java.util.Iterator r5 = r7.iterateChildren()
        L_0x00b6:
            boolean r1 = r5.hasNext()
            if (r1 == 0) goto L_0x00d1
            java.lang.Object r1 = r5.next()
            com.adobe.xmp.impl.XMPNode r1 = (com.adobe.xmp.impl.XMPNode) r1
            boolean r1 = itemValuesMatch(r0, r1)
            if (r1 == 0) goto L_0x00b6
            r0 = r3
        L_0x00c9:
            if (r0 != 0) goto L_0x00a6
            r0 = r2
            goto L_0x0011
        L_0x00ce:
            r0 = r3
            goto L_0x0011
        L_0x00d1:
            r0 = r2
            goto L_0x00c9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.XMPUtilsImpl.itemValuesMatch(com.adobe.xmp.impl.XMPNode, com.adobe.xmp.impl.XMPNode):boolean");
    }

    public static void removeProperties(XMPMeta xMPMeta, String str, String str2, boolean z, boolean z2) throws XMPException {
        ParameterAsserts.assertImplementation(xMPMeta);
        XMPMetaImpl xMPMetaImpl = (XMPMetaImpl) xMPMeta;
        if (str2 == null || str2.length() <= 0) {
            if (str == null || str.length() <= 0) {
                Iterator iterateChildren = xMPMetaImpl.getRoot().iterateChildren();
                while (iterateChildren.hasNext()) {
                    if (removeSchemaChildren((XMPNode) iterateChildren.next(), z)) {
                        iterateChildren.remove();
                    }
                }
                return;
            }
            XMPNode findSchemaNode = XMPNodeUtils.findSchemaNode(xMPMetaImpl.getRoot(), str, false);
            if (findSchemaNode != null && removeSchemaChildren(findSchemaNode, z)) {
                xMPMetaImpl.getRoot().removeChild(findSchemaNode);
            }
            if (z2) {
                XMPAliasInfo[] findAliases = XMPMetaFactory.getSchemaRegistry().findAliases(str);
                for (XMPAliasInfo xMPAliasInfo : findAliases) {
                    XMPNode findNode = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), XMPPathParser.expandXPath(xMPAliasInfo.getNamespace(), xMPAliasInfo.getPropName()), false, null);
                    if (findNode != null) {
                        findNode.getParent().removeChild(findNode);
                    }
                }
            }
        } else if (str == null || str.length() == 0) {
            throw new XMPException("Property name requires schema namespace", 4);
        } else {
            XMPPath expandXPath = XMPPathParser.expandXPath(str, str2);
            XMPNode findNode2 = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), expandXPath, false, null);
            if (findNode2 == null) {
                return;
            }
            if (z || !Utils.isInternalProperty(expandXPath.getSegment(0).getName(), expandXPath.getSegment(1).getName())) {
                XMPNode parent = findNode2.getParent();
                parent.removeChild(findNode2);
                if (parent.getOptions().isSchemaNode() && !parent.hasChildren()) {
                    parent.getParent().removeChild(parent);
                }
            }
        }
    }

    private static boolean removeSchemaChildren(XMPNode xMPNode, boolean z) {
        Iterator iterateChildren = xMPNode.iterateChildren();
        while (iterateChildren.hasNext()) {
            XMPNode xMPNode2 = (XMPNode) iterateChildren.next();
            if (z || !Utils.isInternalProperty(xMPNode.getName(), xMPNode2.getName())) {
                iterateChildren.remove();
            }
        }
        return !xMPNode.hasChildren();
    }

    public static void separateArrayItems(XMPMeta xMPMeta, String str, String str2, String str3, PropertyOptions propertyOptions, boolean z) throws XMPException {
        String str4;
        char c;
        int i;
        String str5;
        char c2;
        int classifyCharacter;
        ParameterAsserts.assertSchemaNS(str);
        ParameterAsserts.assertArrayName(str2);
        if (str3 == null) {
            throw new XMPException("Parameter must not be null", 4);
        }
        ParameterAsserts.assertImplementation(xMPMeta);
        XMPNode separateFindCreateArray = separateFindCreateArray(str, str2, propertyOptions, (XMPMetaImpl) xMPMeta);
        int i2 = 0;
        char c3 = 0;
        int i3 = 0;
        int length = str3.length();
        while (i3 < length) {
            char c4 = c3;
            int i4 = i3;
            while (i4 < length) {
                c4 = str3.charAt(i4);
                i2 = classifyCharacter(c4);
                if (i2 == 0 || i2 == 4) {
                    break;
                }
                i4++;
            }
            if (i4 < length) {
                if (i2 != 4) {
                    char c5 = c4;
                    int i5 = i4;
                    while (i5 < length) {
                        c5 = str3.charAt(i5);
                        i2 = classifyCharacter(c5);
                        if (i2 != 0 && i2 != 4 && ((i2 != 2 || !z) && (i2 != 1 || i5 + 1 >= length || ((classifyCharacter = classifyCharacter((c5 = str3.charAt(i5 + 1)))) != 0 && classifyCharacter != 4 && (classifyCharacter != 2 || !z))))) {
                            break;
                        }
                        i5++;
                    }
                    str4 = str3.substring(i4, i5);
                    c = c5;
                    i = i5;
                } else {
                    char closingQuote = getClosingQuote(c4);
                    int i6 = i4 + 1;
                    str4 = "";
                    c = c4;
                    int i7 = i2;
                    while (true) {
                        if (i6 >= length) {
                            i2 = i7;
                            i = i6;
                            break;
                        }
                        c = str3.charAt(i6);
                        i7 = classifyCharacter(c);
                        if (i7 == 4 && isSurroundingQuote(c, c4, closingQuote)) {
                            if (i6 + 1 < length) {
                                c2 = str3.charAt(i6 + 1);
                                classifyCharacter(c2);
                            } else {
                                c2 = ';';
                            }
                            if (c != c2) {
                                if (isClosingingQuote(c, c4, closingQuote)) {
                                    i2 = i7;
                                    i = i6 + 1;
                                    break;
                                }
                                str5 = str4 + c;
                            } else {
                                str5 = str4 + c;
                                i6++;
                            }
                        } else {
                            str5 = str4 + c;
                        }
                        i6++;
                        str4 = str5;
                    }
                }
                int i8 = 1;
                while (true) {
                    if (i8 > separateFindCreateArray.getChildrenLength()) {
                        i8 = -1;
                        break;
                    } else if (str4.equals(separateFindCreateArray.getChild(i8).getValue())) {
                        break;
                    } else {
                        i8++;
                    }
                }
                if (i8 < 0) {
                    separateFindCreateArray.addChild(new XMPNode(XMPConst.ARRAY_ITEM_NAME, str4, null));
                }
                c3 = c;
                i3 = i;
            } else {
                return;
            }
        }
    }

    private static XMPNode separateFindCreateArray(String str, String str2, PropertyOptions propertyOptions, XMPMetaImpl xMPMetaImpl) throws XMPException {
        PropertyOptions verifySetOptions = XMPNodeUtils.verifySetOptions(propertyOptions, null);
        if (!verifySetOptions.isOnlyArrayOptions()) {
            throw new XMPException("Options can only provide array form", 103);
        }
        XMPPath expandXPath = XMPPathParser.expandXPath(str, str2);
        XMPNode findNode = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), expandXPath, false, null);
        if (findNode != null) {
            PropertyOptions options = findNode.getOptions();
            if (!options.isArray() || options.isArrayAlternate()) {
                throw new XMPException("Named property must be non-alternate array", 102);
            } else if (verifySetOptions.equalArrayTypes(options)) {
                throw new XMPException("Mismatch of specified and existing array form", 102);
            }
        } else {
            findNode = XMPNodeUtils.findNode(xMPMetaImpl.getRoot(), expandXPath, true, verifySetOptions.setArray(true));
            if (findNode == null) {
                throw new XMPException("Failed to create named array", 102);
            }
        }
        return findNode;
    }
}
