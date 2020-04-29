package com.adobe.xmp.impl;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPError;
import com.adobe.xmp.XMPException;
import dji.publics.protocol.ResponseBase;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ParseRDF implements XMPError, XMPConst {
    static final /* synthetic */ boolean $assertionsDisabled = (!ParseRDF.class.desiredAssertionStatus());
    public static final String DEFAULT_PREFIX = "_dflt";
    public static final int RDFTERM_ABOUT = 3;
    public static final int RDFTERM_ABOUT_EACH = 10;
    public static final int RDFTERM_ABOUT_EACH_PREFIX = 11;
    public static final int RDFTERM_BAG_ID = 12;
    public static final int RDFTERM_DATATYPE = 7;
    public static final int RDFTERM_DESCRIPTION = 8;
    public static final int RDFTERM_FIRST_CORE = 1;
    public static final int RDFTERM_FIRST_OLD = 10;
    public static final int RDFTERM_FIRST_SYNTAX = 1;
    public static final int RDFTERM_ID = 2;
    public static final int RDFTERM_LAST_CORE = 7;
    public static final int RDFTERM_LAST_OLD = 12;
    public static final int RDFTERM_LAST_SYNTAX = 9;
    public static final int RDFTERM_LI = 9;
    public static final int RDFTERM_NODE_ID = 6;
    public static final int RDFTERM_OTHER = 0;
    public static final int RDFTERM_PARSE_TYPE = 4;
    public static final int RDFTERM_RDF = 1;
    public static final int RDFTERM_RESOURCE = 5;

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0086  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.adobe.xmp.impl.XMPNode addChildNode(com.adobe.xmp.impl.XMPMetaImpl r9, com.adobe.xmp.impl.XMPNode r10, org.w3c.dom.Node r11, java.lang.String r12, boolean r13) throws com.adobe.xmp.XMPException {
        /*
            r3 = 0
            r8 = 202(0xca, float:2.83E-43)
            r2 = 1
            com.adobe.xmp.XMPSchemaRegistry r4 = com.adobe.xmp.XMPMetaFactory.getSchemaRegistry()
            java.lang.String r0 = r11.getNamespaceURI()
            if (r0 == 0) goto L_0x009f
            java.lang.String r1 = "http://purl.org/dc/1.1/"
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x001a
            java.lang.String r0 = "http://purl.org/dc/elements/1.1/"
        L_0x001a:
            java.lang.String r1 = r4.getNamespacePrefix(r0)
            if (r1 != 0) goto L_0x002e
            java.lang.String r1 = r11.getPrefix()
            if (r1 == 0) goto L_0x009b
            java.lang.String r1 = r11.getPrefix()
        L_0x002a:
            java.lang.String r1 = r4.registerNamespace(r0, r1)
        L_0x002e:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.StringBuilder r1 = r5.append(r1)
            java.lang.String r5 = r11.getLocalName()
            java.lang.StringBuilder r1 = r1.append(r5)
            java.lang.String r1 = r1.toString()
            com.adobe.xmp.options.PropertyOptions r5 = new com.adobe.xmp.options.PropertyOptions
            r5.<init>()
            if (r13 == 0) goto L_0x00cb
            com.adobe.xmp.impl.XMPNode r6 = r9.getRoot()
            java.lang.String r7 = "_dflt"
            com.adobe.xmp.impl.XMPNode r10 = com.adobe.xmp.impl.XMPNodeUtils.findSchemaNode(r6, r0, r7, r2)
            r10.setImplicit(r3)
            com.adobe.xmp.properties.XMPAliasInfo r0 = r4.findAlias(r1)
            if (r0 == 0) goto L_0x00cb
            com.adobe.xmp.impl.XMPNode r0 = r9.getRoot()
            r0.setHasAliases(r2)
            r10.setHasAliases(r2)
            r0 = r2
        L_0x0069:
            java.lang.String r3 = "rdf:li"
            boolean r3 = r3.equals(r1)
            java.lang.String r4 = "rdf:value"
            boolean r4 = r4.equals(r1)
            com.adobe.xmp.impl.XMPNode r6 = new com.adobe.xmp.impl.XMPNode
            r6.<init>(r1, r12, r5)
            r6.setAlias(r0)
            if (r4 != 0) goto L_0x00a8
            r10.addChild(r6)
        L_0x0084:
            if (r4 == 0) goto L_0x00af
            if (r13 != 0) goto L_0x0092
            com.adobe.xmp.options.PropertyOptions r0 = r10.getOptions()
            boolean r0 = r0.isStruct()
            if (r0 != 0) goto L_0x00ac
        L_0x0092:
            com.adobe.xmp.XMPException r0 = new com.adobe.xmp.XMPException
            java.lang.String r1 = "Misplaced rdf:value element"
            r0.<init>(r1, r8)
            throw r0
        L_0x009b:
            java.lang.String r1 = "_dflt"
            goto L_0x002a
        L_0x009f:
            com.adobe.xmp.XMPException r0 = new com.adobe.xmp.XMPException
            java.lang.String r1 = "XML namespace required for all elements and attributes"
            r0.<init>(r1, r8)
            throw r0
        L_0x00a8:
            r10.addChild(r2, r6)
            goto L_0x0084
        L_0x00ac:
            r10.setHasValueChild(r2)
        L_0x00af:
            if (r3 == 0) goto L_0x00ca
            com.adobe.xmp.options.PropertyOptions r0 = r10.getOptions()
            boolean r0 = r0.isArray()
            if (r0 != 0) goto L_0x00c4
            com.adobe.xmp.XMPException r0 = new com.adobe.xmp.XMPException
            java.lang.String r1 = "Misplaced rdf:li element"
            r0.<init>(r1, r8)
            throw r0
        L_0x00c4:
            java.lang.String r0 = "[]"
            r6.setName(r0)
        L_0x00ca:
            return r6
        L_0x00cb:
            r0 = r3
            goto L_0x0069
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.ParseRDF.addChildNode(com.adobe.xmp.impl.XMPMetaImpl, com.adobe.xmp.impl.XMPNode, org.w3c.dom.Node, java.lang.String, boolean):com.adobe.xmp.impl.XMPNode");
    }

    private static XMPNode addQualifierNode(XMPNode xMPNode, String str, String str2) throws XMPException {
        if (XMPConst.XML_LANG.equals(str)) {
            str2 = Utils.normalizeLangValue(str2);
        }
        XMPNode xMPNode2 = new XMPNode(str, str2, null);
        xMPNode.addQualifier(xMPNode2);
        return xMPNode2;
    }

    private static void fixupQualifiedNode(XMPNode xMPNode) throws XMPException {
        if ($assertionsDisabled || (xMPNode.getOptions().isStruct() && xMPNode.hasChildren())) {
            XMPNode child = xMPNode.getChild(1);
            if ($assertionsDisabled || "rdf:value".equals(child.getName())) {
                if (child.getOptions().getHasLanguage()) {
                    if (xMPNode.getOptions().getHasLanguage()) {
                        throw new XMPException("Redundant xml:lang for rdf:value element", XMPError.BADXMP);
                    }
                    XMPNode qualifier = child.getQualifier(1);
                    child.removeQualifier(qualifier);
                    xMPNode.addQualifier(qualifier);
                }
                for (int i = 1; i <= child.getQualifierLength(); i++) {
                    xMPNode.addQualifier(child.getQualifier(i));
                }
                for (int i2 = 2; i2 <= xMPNode.getChildrenLength(); i2++) {
                    xMPNode.addQualifier(xMPNode.getChild(i2));
                }
                if ($assertionsDisabled || xMPNode.getOptions().isStruct() || xMPNode.getHasValueChild()) {
                    xMPNode.setHasValueChild(false);
                    xMPNode.getOptions().setStruct(false);
                    xMPNode.getOptions().mergeWith(child.getOptions());
                    xMPNode.setValue(child.getValue());
                    xMPNode.removeChildren();
                    Iterator iterateChildren = child.iterateChildren();
                    while (iterateChildren.hasNext()) {
                        xMPNode.addChild((XMPNode) iterateChildren.next());
                    }
                    return;
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    private static int getRDFTermKind(Node node) {
        String localName = node.getLocalName();
        String namespaceURI = node.getNamespaceURI();
        if (namespaceURI == null && ((ResponseBase.STRING_ABOUT.equals(localName) || "ID".equals(localName)) && (node instanceof Attr) && XMPConst.NS_RDF.equals(((Attr) node).getOwnerElement().getNamespaceURI()))) {
            namespaceURI = XMPConst.NS_RDF;
        }
        if (XMPConst.NS_RDF.equals(namespaceURI)) {
            if ("li".equals(localName)) {
                return 9;
            }
            if ("parseType".equals(localName)) {
                return 4;
            }
            if ("Description".equals(localName)) {
                return 8;
            }
            if (ResponseBase.STRING_ABOUT.equals(localName)) {
                return 3;
            }
            if ("resource".equals(localName)) {
                return 5;
            }
            if ("RDF".equals(localName)) {
                return 1;
            }
            if ("ID".equals(localName)) {
                return 2;
            }
            if ("nodeID".equals(localName)) {
                return 6;
            }
            if ("datatype".equals(localName)) {
                return 7;
            }
            if ("aboutEach".equals(localName)) {
                return 10;
            }
            if ("aboutEachPrefix".equals(localName)) {
                return 11;
            }
            if ("bagID".equals(localName)) {
                return 12;
            }
        }
        return 0;
    }

    private static boolean isCoreSyntaxTerm(int i) {
        return 1 <= i && i <= 7;
    }

    private static boolean isOldTerm(int i) {
        return 10 <= i && i <= 12;
    }

    private static boolean isPropertyElementName(int i) {
        return i != 8 && !isOldTerm(i) && !isCoreSyntaxTerm(i);
    }

    private static boolean isWhitespaceNode(Node node) {
        if (node.getNodeType() != 3) {
            return false;
        }
        String nodeValue = node.getNodeValue();
        for (int i = 0; i < nodeValue.length(); i++) {
            if (!Character.isWhitespace(nodeValue.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    static XMPMetaImpl parse(Node node) throws XMPException {
        XMPMetaImpl xMPMetaImpl = new XMPMetaImpl();
        rdf_RDF(xMPMetaImpl, node);
        return xMPMetaImpl;
    }

    private static void rdf_EmptyPropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        boolean z2;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        boolean z6 = false;
        Node node2 = null;
        if (node.hasChildNodes()) {
            throw new XMPException("Nested content not allowed with rdf:resource or property attributes", 202);
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < node.getAttributes().getLength()) {
                Node item = node.getAttributes().item(i2);
                if (!"xmlns".equals(item.getPrefix()) && (item.getPrefix() != null || !"xmlns".equals(item.getNodeName()))) {
                    switch (getRDFTermKind(item)) {
                        case 0:
                            if (!"value".equals(item.getLocalName()) || !XMPConst.NS_RDF.equals(item.getNamespaceURI())) {
                                if (XMPConst.XML_LANG.equals(item.getNodeName())) {
                                    break;
                                } else {
                                    z3 = true;
                                    break;
                                }
                            } else if (z4) {
                                throw new XMPException("Empty property element can't have both rdf:value and rdf:resource", XMPError.BADXMP);
                            } else {
                                z6 = true;
                                node2 = item;
                                continue;
                            }
                        case 1:
                        case 3:
                        case 4:
                        default:
                            throw new XMPException("Unrecognized attribute of empty property element", 202);
                        case 2:
                            break;
                        case 5:
                            if (z5) {
                                throw new XMPException("Empty property element can't have both rdf:resource and rdf:nodeID", 202);
                            } else if (z6) {
                                throw new XMPException("Empty property element can't have both rdf:value and rdf:resource", XMPError.BADXMP);
                            } else {
                                z4 = true;
                                if (!z6) {
                                    node2 = item;
                                    break;
                                } else {
                                    continue;
                                }
                            }
                        case 6:
                            if (z4) {
                                throw new XMPException("Empty property element can't have both rdf:resource and rdf:nodeID", 202);
                            }
                            z5 = true;
                            continue;
                    }
                }
                i = i2 + 1;
            } else {
                XMPNode addChildNode = addChildNode(xMPMetaImpl, xMPNode, node, "", z);
                if (z6 || z4) {
                    addChildNode.setValue(node2 != null ? node2.getNodeValue() : "");
                    if (!z6) {
                        addChildNode.getOptions().setURI(true);
                        z2 = false;
                    }
                    z2 = false;
                } else {
                    if (z3) {
                        addChildNode.getOptions().setStruct(true);
                        z2 = true;
                    }
                    z2 = false;
                }
                for (int i3 = 0; i3 < node.getAttributes().getLength(); i3++) {
                    Node item2 = node.getAttributes().item(i3);
                    if (item2 != node2 && !"xmlns".equals(item2.getPrefix()) && (item2.getPrefix() != null || !"xmlns".equals(item2.getNodeName()))) {
                        switch (getRDFTermKind(item2)) {
                            case 0:
                                if (z2) {
                                    if (!XMPConst.XML_LANG.equals(item2.getNodeName())) {
                                        addChildNode(xMPMetaImpl, addChildNode, item2, item2.getNodeValue(), false);
                                        break;
                                    } else {
                                        addQualifierNode(addChildNode, XMPConst.XML_LANG, item2.getNodeValue());
                                        break;
                                    }
                                } else {
                                    addQualifierNode(addChildNode, item2.getNodeName(), item2.getNodeValue());
                                    continue;
                                }
                            case 1:
                            case 3:
                            case 4:
                            default:
                                throw new XMPException("Unrecognized attribute of empty property element", 202);
                            case 2:
                            case 6:
                                break;
                            case 5:
                                addQualifierNode(addChildNode, "rdf:resource", item2.getNodeValue());
                                continue;
                        }
                    }
                }
                return;
            }
        }
    }

    private static void rdf_LiteralPropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        int i = 0;
        XMPNode addChildNode = addChildNode(xMPMetaImpl, xMPNode, node, null, z);
        for (int i2 = 0; i2 < node.getAttributes().getLength(); i2++) {
            Node item = node.getAttributes().item(i2);
            if (!"xmlns".equals(item.getPrefix()) && (item.getPrefix() != null || !"xmlns".equals(item.getNodeName()))) {
                String namespaceURI = item.getNamespaceURI();
                String localName = item.getLocalName();
                if (XMPConst.XML_LANG.equals(item.getNodeName())) {
                    addQualifierNode(addChildNode, XMPConst.XML_LANG, item.getNodeValue());
                } else if (!XMPConst.NS_RDF.equals(namespaceURI) || (!"ID".equals(localName) && !"datatype".equals(localName))) {
                    throw new XMPException("Invalid attribute for literal property element", 202);
                }
            }
        }
        String str = "";
        while (i < node.getChildNodes().getLength()) {
            Node item2 = node.getChildNodes().item(i);
            if (item2.getNodeType() == 3) {
                str = str + item2.getNodeValue();
                i++;
            } else {
                throw new XMPException("Invalid child of literal property element", 202);
            }
        }
        addChildNode.setValue(str);
    }

    private static void rdf_NodeElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        int rDFTermKind = getRDFTermKind(node);
        if (rDFTermKind != 8 && rDFTermKind != 0) {
            throw new XMPException("Node element must be rdf:Description or typed node", 202);
        } else if (!z || rDFTermKind != 0) {
            rdf_NodeElementAttrs(xMPMetaImpl, xMPNode, node, z);
            rdf_PropertyElementList(xMPMetaImpl, xMPNode, node, z);
        } else {
            throw new XMPException("Top level typed node not allowed", XMPError.BADXMP);
        }
    }

    private static void rdf_NodeElementAttrs(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i;
            if (i2 < node.getAttributes().getLength()) {
                Node item = node.getAttributes().item(i2);
                if (!"xmlns".equals(item.getPrefix()) && (item.getPrefix() != null || !"xmlns".equals(item.getNodeName()))) {
                    int rDFTermKind = getRDFTermKind(item);
                    switch (rDFTermKind) {
                        case 0:
                            addChildNode(xMPMetaImpl, xMPNode, item, item.getNodeValue(), z);
                            continue;
                        case 1:
                        case 4:
                        case 5:
                        default:
                            throw new XMPException("Invalid nodeElement attribute", 202);
                        case 2:
                        case 3:
                        case 6:
                            if (i3 <= 0) {
                                i3++;
                                if (z && rDFTermKind == 3) {
                                    if (xMPNode.getName() != null && xMPNode.getName().length() > 0) {
                                        if (xMPNode.getName().equals(item.getNodeValue())) {
                                            break;
                                        } else {
                                            throw new XMPException("Mismatched top level rdf:about values", XMPError.BADXMP);
                                        }
                                    } else {
                                        xMPNode.setName(item.getNodeValue());
                                        continue;
                                    }
                                }
                            } else {
                                throw new XMPException("Mutally exclusive about, ID, nodeID attributes", 202);
                            }
                            break;
                    }
                }
                i = i3;
                i2++;
            } else {
                return;
            }
        }
    }

    private static void rdf_NodeElementList(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node) throws XMPException {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);
            if (!isWhitespaceNode(item)) {
                rdf_NodeElement(xMPMetaImpl, xMPNode, item, true);
            }
        }
    }

    private static void rdf_ParseTypeCollectionPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeCollection property element not allowed", XMPError.BADXMP);
    }

    private static void rdf_ParseTypeLiteralPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeLiteral property element not allowed", XMPError.BADXMP);
    }

    private static void rdf_ParseTypeOtherPropertyElement() throws XMPException {
        throw new XMPException("ParseTypeOther property element not allowed", XMPError.BADXMP);
    }

    private static void rdf_ParseTypeResourcePropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        XMPNode addChildNode = addChildNode(xMPMetaImpl, xMPNode, node, "", z);
        addChildNode.getOptions().setStruct(true);
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node item = node.getAttributes().item(i);
            if (!"xmlns".equals(item.getPrefix()) && (item.getPrefix() != null || !"xmlns".equals(item.getNodeName()))) {
                String localName = item.getLocalName();
                String namespaceURI = item.getNamespaceURI();
                if (XMPConst.XML_LANG.equals(item.getNodeName())) {
                    addQualifierNode(addChildNode, XMPConst.XML_LANG, item.getNodeValue());
                } else if (!XMPConst.NS_RDF.equals(namespaceURI) || (!"ID".equals(localName) && !"parseType".equals(localName))) {
                    throw new XMPException("Invalid attribute for ParseTypeResource property element", 202);
                }
            }
        }
        rdf_PropertyElementList(xMPMetaImpl, addChildNode, node, false);
        if (addChildNode.getHasValueChild()) {
            fixupQualifiedNode(addChildNode);
        }
    }

    private static void rdf_PropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        if (!isPropertyElementName(getRDFTermKind(node))) {
            throw new XMPException("Invalid property element name", 202);
        }
        NamedNodeMap attributes = node.getAttributes();
        ArrayList<String> arrayList = null;
        for (int i = 0; i < attributes.getLength(); i++) {
            Node item = attributes.item(i);
            if ("xmlns".equals(item.getPrefix()) || (item.getPrefix() == null && "xmlns".equals(item.getNodeName()))) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(item.getNodeName());
            }
        }
        if (arrayList != null) {
            for (String str : arrayList) {
                attributes.removeNamedItem(str);
            }
        }
        if (attributes.getLength() > 3) {
            rdf_EmptyPropertyElement(xMPMetaImpl, xMPNode, node, z);
            return;
        }
        int i2 = 0;
        while (i2 < attributes.getLength()) {
            Node item2 = attributes.item(i2);
            String localName = item2.getLocalName();
            String namespaceURI = item2.getNamespaceURI();
            String nodeValue = item2.getNodeValue();
            if (XMPConst.XML_LANG.equals(item2.getNodeName()) && (!"ID".equals(localName) || !XMPConst.NS_RDF.equals(namespaceURI))) {
                i2++;
            } else if ("datatype".equals(localName) && XMPConst.NS_RDF.equals(namespaceURI)) {
                rdf_LiteralPropertyElement(xMPMetaImpl, xMPNode, node, z);
                return;
            } else if (!"parseType".equals(localName) || !XMPConst.NS_RDF.equals(namespaceURI)) {
                rdf_EmptyPropertyElement(xMPMetaImpl, xMPNode, node, z);
                return;
            } else if ("Literal".equals(nodeValue)) {
                rdf_ParseTypeLiteralPropertyElement();
                return;
            } else if ("Resource".equals(nodeValue)) {
                rdf_ParseTypeResourcePropertyElement(xMPMetaImpl, xMPNode, node, z);
                return;
            } else if ("Collection".equals(nodeValue)) {
                rdf_ParseTypeCollectionPropertyElement();
                return;
            } else {
                rdf_ParseTypeOtherPropertyElement();
                return;
            }
        }
        if (node.hasChildNodes()) {
            for (int i3 = 0; i3 < node.getChildNodes().getLength(); i3++) {
                if (node.getChildNodes().item(i3).getNodeType() != 3) {
                    rdf_ResourcePropertyElement(xMPMetaImpl, xMPNode, node, z);
                    return;
                }
            }
            rdf_LiteralPropertyElement(xMPMetaImpl, xMPNode, node, z);
            return;
        }
        rdf_EmptyPropertyElement(xMPMetaImpl, xMPNode, node, z);
    }

    private static void rdf_PropertyElementList(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node item = node.getChildNodes().item(i);
            if (!isWhitespaceNode(item)) {
                if (item.getNodeType() != 1) {
                    throw new XMPException("Expected property element node not found", 202);
                }
                rdf_PropertyElement(xMPMetaImpl, xMPNode, item, z);
            }
        }
    }

    static void rdf_RDF(XMPMetaImpl xMPMetaImpl, Node node) throws XMPException {
        if (node.hasAttributes()) {
            rdf_NodeElementList(xMPMetaImpl, xMPMetaImpl.getRoot(), node);
            return;
        }
        throw new XMPException("Invalid attributes of rdf:RDF element", 202);
    }

    private static void rdf_ResourcePropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        if (!z || !"iX:changes".equals(node.getNodeName())) {
            XMPNode addChildNode = addChildNode(xMPMetaImpl, xMPNode, node, "", z);
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
                Node item = node.getAttributes().item(i);
                if (!"xmlns".equals(item.getPrefix()) && (item.getPrefix() != null || !"xmlns".equals(item.getNodeName()))) {
                    String localName = item.getLocalName();
                    String namespaceURI = item.getNamespaceURI();
                    if (XMPConst.XML_LANG.equals(item.getNodeName())) {
                        addQualifierNode(addChildNode, XMPConst.XML_LANG, item.getNodeValue());
                    } else if (!"ID".equals(localName) || !XMPConst.NS_RDF.equals(namespaceURI)) {
                        throw new XMPException("Invalid attribute for resource property element", 202);
                    }
                }
            }
            boolean z2 = false;
            for (int i2 = 0; i2 < node.getChildNodes().getLength(); i2++) {
                Node item2 = node.getChildNodes().item(i2);
                if (!isWhitespaceNode(item2)) {
                    if (item2.getNodeType() == 1 && !z2) {
                        boolean equals = XMPConst.NS_RDF.equals(item2.getNamespaceURI());
                        String localName2 = item2.getLocalName();
                        if (equals && "Bag".equals(localName2)) {
                            addChildNode.getOptions().setArray(true);
                        } else if (equals && "Seq".equals(localName2)) {
                            addChildNode.getOptions().setArray(true).setArrayOrdered(true);
                        } else if (!equals || !"Alt".equals(localName2)) {
                            addChildNode.getOptions().setStruct(true);
                            if (!equals && !"Description".equals(localName2)) {
                                String namespaceURI2 = item2.getNamespaceURI();
                                if (namespaceURI2 == null) {
                                    throw new XMPException("All XML elements must be in a namespace", XMPError.BADXMP);
                                }
                                addQualifierNode(addChildNode, XMPConst.RDF_TYPE, namespaceURI2 + ':' + localName2);
                            }
                        } else {
                            addChildNode.getOptions().setArray(true).setArrayOrdered(true).setArrayAlternate(true);
                        }
                        rdf_NodeElement(xMPMetaImpl, addChildNode, item2, false);
                        if (addChildNode.getHasValueChild()) {
                            fixupQualifiedNode(addChildNode);
                        } else if (addChildNode.getOptions().isArrayAlternate()) {
                            XMPNodeUtils.detectAltText(addChildNode);
                        }
                        z2 = true;
                    } else if (z2) {
                        throw new XMPException("Invalid child of resource property element", 202);
                    } else {
                        throw new XMPException("Children of resource property element must be XML elements", 202);
                    }
                }
            }
            if (!z2) {
                throw new XMPException("Missing child of resource property element", 202);
            }
        }
    }
}
