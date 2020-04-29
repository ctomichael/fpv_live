package com.adobe.xmp.impl;

import com.adobe.xmp.XMPError;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.options.ParseOptions;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMPMetaParser {
    private static final Object XMP_RDF = new Object();
    private static DocumentBuilderFactory factory = createDocumentBuilderFactory();

    private XMPMetaParser() {
    }

    private static DocumentBuilderFactory createDocumentBuilderFactory() {
        DocumentBuilderFactory newInstance = DocumentBuilderFactory.newInstance();
        newInstance.setNamespaceAware(true);
        newInstance.setIgnoringComments(true);
        try {
            newInstance.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            newInstance.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            newInstance.setFeature("http://xml.org/sax/features/external-general-entities", false);
            newInstance.setFeature("http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl", false);
            newInstance.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            newInstance.setFeature("http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities", false);
            newInstance.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            newInstance.setXIncludeAware(false);
            newInstance.setExpandEntityReferences(false);
        } catch (Exception e) {
        }
        return newInstance;
    }

    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.Object[]], assign insn: null */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object[] findRootNode(org.w3c.dom.Node r8, boolean r9, java.lang.Object[] r10) {
        /*
            r7 = 7
            r3 = 0
            org.w3c.dom.NodeList r4 = r8.getChildNodes()
            r2 = r3
        L_0x0007:
            int r0 = r4.getLength()
            if (r2 >= r0) goto L_0x0091
            org.w3c.dom.Node r1 = r4.item(r2)
            short r0 = r1.getNodeType()
            if (r7 != r0) goto L_0x0036
            java.lang.String r5 = "xpacket"
            r0 = r1
            org.w3c.dom.ProcessingInstruction r0 = (org.w3c.dom.ProcessingInstruction) r0
            java.lang.String r0 = r0.getTarget()
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0036
            if (r10 == 0) goto L_0x0032
            r0 = 2
            org.w3c.dom.ProcessingInstruction r1 = (org.w3c.dom.ProcessingInstruction) r1
            java.lang.String r1 = r1.getData()
            r10[r0] = r1
        L_0x0032:
            int r0 = r2 + 1
            r2 = r0
            goto L_0x0007
        L_0x0036:
            r0 = 3
            short r5 = r1.getNodeType()
            if (r0 == r5) goto L_0x0032
            short r0 = r1.getNodeType()
            if (r7 == r0) goto L_0x0032
            java.lang.String r0 = r1.getNamespaceURI()
            java.lang.String r5 = r1.getLocalName()
            java.lang.String r6 = "xmpmeta"
            boolean r6 = r6.equals(r5)
            if (r6 != 0) goto L_0x005d
            java.lang.String r6 = "xapmeta"
            boolean r6 = r6.equals(r5)
            if (r6 == 0) goto L_0x006b
        L_0x005d:
            java.lang.String r6 = "adobe:ns:meta/"
            boolean r6 = r6.equals(r0)
            if (r6 == 0) goto L_0x006b
            java.lang.Object[] r10 = findRootNode(r1, r3, r10)
        L_0x006a:
            return r10
        L_0x006b:
            if (r9 != 0) goto L_0x0089
            java.lang.String r6 = "RDF"
            boolean r5 = r6.equals(r5)
            if (r5 == 0) goto L_0x0089
            java.lang.String r5 = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0089
            if (r10 == 0) goto L_0x006a
            r10[r3] = r1
            r0 = 1
            java.lang.Object r1 = com.adobe.xmp.impl.XMPMetaParser.XMP_RDF
            r10[r0] = r1
            goto L_0x006a
        L_0x0089:
            java.lang.Object[] r0 = findRootNode(r1, r9, r10)
            if (r0 == 0) goto L_0x0032
            r10 = r0
            goto L_0x006a
        L_0x0091:
            r10 = 0
            goto L_0x006a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.XMPMetaParser.findRootNode(org.w3c.dom.Node, boolean, java.lang.Object[]):java.lang.Object[]");
    }

    public static XMPMeta parse(Object obj, ParseOptions parseOptions) throws XMPException {
        ParameterAsserts.assertNotNull(obj);
        if (parseOptions == null) {
            parseOptions = new ParseOptions();
        }
        Object[] findRootNode = findRootNode(parseXml(obj, parseOptions), parseOptions.getRequireXMPMeta(), new Object[3]);
        if (findRootNode == null || findRootNode[1] != XMP_RDF) {
            return new XMPMetaImpl();
        }
        XMPMetaImpl parse = ParseRDF.parse((Node) findRootNode[0]);
        parse.setPacketHeader((String) findRootNode[2]);
        return !parseOptions.getOmitNormalization() ? XMPNormalizer.process(parse, parseOptions) : parse;
    }

    private static Document parseInputSource(InputSource inputSource) throws XMPException {
        try {
            DocumentBuilder newDocumentBuilder = factory.newDocumentBuilder();
            newDocumentBuilder.setErrorHandler(null);
            return newDocumentBuilder.parse(inputSource);
        } catch (SAXException e) {
            throw new XMPException("XML parsing failure", XMPError.BADXML, e);
        } catch (ParserConfigurationException e2) {
            throw new XMPException("XML Parser not correctly configured", 0, e2);
        } catch (IOException e3) {
            throw new XMPException("Error reading the XML-file", XMPError.BADSTREAM, e3);
        }
    }

    private static Document parseXml(Object obj, ParseOptions parseOptions) throws XMPException {
        return obj instanceof InputStream ? parseXmlFromInputStream((InputStream) obj, parseOptions) : obj instanceof byte[] ? parseXmlFromBytebuffer(new ByteBuffer((byte[]) obj), parseOptions) : parseXmlFromString((String) obj, parseOptions);
    }

    private static Document parseXmlFromBytebuffer(ByteBuffer byteBuffer, ParseOptions parseOptions) throws XMPException {
        InputSource inputSource = new InputSource(byteBuffer.getByteStream());
        try {
            if (parseOptions.getDisallowDoctype()) {
                try {
                    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                } catch (Throwable th) {
                }
            }
            return parseInputSource(inputSource);
        } catch (XMPException e) {
            if (e.getErrorCode() == 201 || e.getErrorCode() == 204) {
                if (parseOptions.getAcceptLatin1()) {
                    byteBuffer = Latin1Converter.convert(byteBuffer);
                }
                if (!parseOptions.getFixControlChars()) {
                    return parseInputSource(new InputSource(byteBuffer.getByteStream()));
                }
                try {
                    return parseInputSource(new InputSource(new FixASCIIControlsReader(new InputStreamReader(byteBuffer.getByteStream(), byteBuffer.getEncoding()))));
                } catch (UnsupportedEncodingException e2) {
                    throw new XMPException("Unsupported Encoding", 9, e);
                }
            } else {
                throw e;
            }
        }
    }

    private static Document parseXmlFromInputStream(InputStream inputStream, ParseOptions parseOptions) throws XMPException {
        if (!parseOptions.getAcceptLatin1() && !parseOptions.getFixControlChars()) {
            return parseInputSource(new InputSource(inputStream));
        }
        try {
            return parseXmlFromBytebuffer(new ByteBuffer(inputStream), parseOptions);
        } catch (IOException e) {
            throw new XMPException("Error reading the XML-file", XMPError.BADSTREAM, e);
        }
    }

    private static Document parseXmlFromString(String str, ParseOptions parseOptions) throws XMPException {
        new InputSource(new StringReader(str));
        try {
            if (parseOptions.getDisallowDoctype()) {
                try {
                    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
                } catch (Throwable th) {
                }
            }
            return parseInputSource(new InputSource(new StringReader(str)));
        } catch (XMPException e) {
            if (e.getErrorCode() == 201 && parseOptions.getFixControlChars()) {
                return parseInputSource(new InputSource(new FixASCIIControlsReader(new StringReader(str))));
            }
            throw e;
        }
    }
}
