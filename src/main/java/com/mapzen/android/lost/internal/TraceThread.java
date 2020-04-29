package com.mapzen.android.lost.internal;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import java.io.File;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class TraceThread extends Thread {
    private static final String TAG_LAT = "lat";
    private static final String TAG_LNG = "lon";
    private static final String TAG_SPEED = "speed";
    private static final String TAG_TRACK_POINT = "trkpt";
    /* access modifiers changed from: private */
    public boolean canceled;
    private final Context context;
    /* access modifiers changed from: private */
    public final MockEngine engine;
    private Location previous;
    private final SleepFactory sleepFactory;
    private final File traceFile;

    TraceThread(Context context2, File traceFile2, MockEngine engine2, SleepFactory sleepFactory2) {
        this.context = context2;
        this.traceFile = traceFile2;
        this.engine = engine2;
        this.sleepFactory = sleepFactory2;
    }

    public void cancel() {
        this.canceled = true;
        interrupt();
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v4, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: org.w3c.dom.NodeList} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v7, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: org.w3c.dom.NodeList} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r12 = this;
            javax.xml.parsers.DocumentBuilderFactory r5 = javax.xml.parsers.DocumentBuilderFactory.newInstance()
            javax.xml.xpath.XPathFactory r10 = javax.xml.xpath.XPathFactory.newInstance()
            javax.xml.xpath.XPath r9 = r10.newXPath()
            java.lang.String r4 = "//trkpt"
            java.lang.String r7 = "//speed"
            r6 = 0
            r8 = 0
            javax.xml.parsers.DocumentBuilder r1 = r5.newDocumentBuilder()     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            java.io.File r10 = r12.traceFile     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            org.w3c.dom.Document r2 = r1.parse(r10)     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            java.lang.String r10 = "//trkpt"
            javax.xml.xpath.XPathExpression r10 = r9.compile(r10)     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            javax.xml.namespace.QName r11 = javax.xml.xpath.XPathConstants.NODESET     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            java.lang.Object r10 = r10.evaluate(r2, r11)     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            r0 = r10
            org.w3c.dom.NodeList r0 = (org.w3c.dom.NodeList) r0     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            r6 = r0
            java.lang.String r10 = "//speed"
            javax.xml.xpath.XPathExpression r10 = r9.compile(r10)     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            javax.xml.namespace.QName r11 = javax.xml.xpath.XPathConstants.NODESET     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            java.lang.Object r10 = r10.evaluate(r2, r11)     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            r0 = r10
            org.w3c.dom.NodeList r0 = (org.w3c.dom.NodeList) r0     // Catch:{ ParserConfigurationException -> 0x0044, SAXException -> 0x0049, IOException -> 0x004e, XPathExpressionException -> 0x0053 }
            r8 = r0
        L_0x0040:
            r12.parse(r6, r8)
            return
        L_0x0044:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0040
        L_0x0049:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0040
        L_0x004e:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0040
        L_0x0053:
            r3 = move-exception
            r3.printStackTrace()
            goto L_0x0040
        */
        throw new UnsupportedOperationException("Method not decompiled: com.mapzen.android.lost.internal.TraceThread.run():void");
    }

    private void parse(NodeList nodeList, NodeList speedList) {
        if (nodeList != null) {
            for (int i = 0; i < nodeList.getLength(); i++) {
                postMockLocation(nodeToLocation(nodeList, speedList, i));
                sleepFastestInterval();
            }
        }
    }

    private Location nodeToLocation(NodeList nodeList, NodeList speedList, int i) {
        Node node = nodeList.item(i);
        String lat = node.getAttributes().getNamedItem("lat").getNodeValue();
        String lng = node.getAttributes().getNamedItem(TAG_LNG).getNodeValue();
        Location location = new Location(MockEngine.MOCK_PROVIDER);
        location.setLatitude(Double.parseDouble(lat));
        location.setLongitude(Double.parseDouble(lng));
        location.setTime(System.currentTimeMillis());
        if (!(speedList.item(i) == null || speedList.item(i).getFirstChild() == null)) {
            location.setSpeed(Float.parseFloat(speedList.item(i).getFirstChild().getNodeValue()));
        }
        if (this.previous != null) {
            location.setBearing(this.previous.bearingTo(location));
        }
        this.previous = location;
        return location;
    }

    private void sleepFastestInterval() {
        LocationRequestUnbundled request = this.engine.getRequest();
        if (request != null) {
            this.sleepFactory.sleep(request.getFastestInterval());
        }
    }

    private void postMockLocation(final Location mockLocation) {
        new Handler(this.context.getMainLooper()).post(new Runnable() {
            /* class com.mapzen.android.lost.internal.TraceThread.AnonymousClass1 */

            public void run() {
                if (!TraceThread.this.canceled) {
                    TraceThread.this.engine.setLocation(mockLocation);
                }
            }
        });
    }
}
