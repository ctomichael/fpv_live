package dji.internal.network;

import android.database.Cursor;
import dji.fieldAnnotation.EXClassNullAway;
import dji.thirdparty.rx.functions.Func1;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.json.JSONObject;

@EXClassNullAway
public class DJIAnalyticsEvent implements Serializable {
    public static final String EVENT_DATA = "event_data";
    public static final String ID = "_id";
    public static final Func1<Cursor, DJIAnalyticsEvent> MAPPER = new Func1<Cursor, DJIAnalyticsEvent>() {
        /* class dji.internal.network.DJIAnalyticsEvent.AnonymousClass1 */

        public DJIAnalyticsEvent call(Cursor cursor) {
            return AnalyticsEventHelper.deserializeEvent(cursor);
        }
    };
    public static final String TABLE = "event_item";
    private String appKey;
    private String cameraName;
    private String category;
    private String connectedSessionId;
    private String displayName;
    private String event;
    private String eventCreatedTimestamp;
    private String eventId;
    private Map<String, Object> extra;
    private String installId;
    private boolean isReleaseMode;
    private String locale;
    private String platform;
    private String productFirmwareVersion;
    private String productId;
    private String productName;
    private String registeredSessionId;
    private String remoteControllerId;
    private String remoteControllerName;
    private String sdkVersion;

    public DJIAnalyticsEvent() {
    }

    public DJIAnalyticsEvent(String time, String installId2, String eventId2, String displayName2, String appKey2, String platform2, String sdkVersion2, String locale2, String regSessionId, String conSessionId, String productId2, String productName2, String remoteControllerId2, String remoteControllerName2, String cameraName2, String category2, String event2, String productFirmwareVersion2, boolean isReleaseMode2, Map<String, Object> extra2) {
        this.eventCreatedTimestamp = time;
        this.installId = installId2;
        this.eventId = eventId2;
        this.displayName = displayName2;
        this.appKey = appKey2;
        this.platform = platform2;
        this.sdkVersion = sdkVersion2;
        this.locale = locale2;
        this.registeredSessionId = regSessionId;
        this.connectedSessionId = conSessionId;
        this.productId = productId2;
        this.productName = productName2;
        this.remoteControllerId = remoteControllerId2;
        this.remoteControllerName = remoteControllerName2;
        this.cameraName = cameraName2;
        this.category = category2;
        this.event = event2;
        this.extra = extra2;
        this.productFirmwareVersion = productFirmwareVersion2;
        this.isReleaseMode = isReleaseMode2;
    }

    public String getEvent() {
        return this.event;
    }

    public String getEventCategory() {
        return this.category;
    }

    public String getEventId() {
        return this.eventId;
    }

    public String getEventCreatedTimestamp() {
        return this.eventCreatedTimestamp;
    }

    public static class EventBuilder {
        private String appKey;
        private String cameraName;
        private String category;
        private String connectedSessionId;
        private String displayName;
        private String event;
        private String eventId;
        private Map<String, Object> extra;
        private String installId;
        private boolean isReleaseMode;
        private String locale;
        private String platform;
        private String productFirmwareVersion;
        private String productId;
        private String productName;
        private String registeredSessionId;
        private String remoteControllerId;
        private String remoteControllerName;
        private String sdkVersion;
        private String time;

        public EventBuilder installId(String installId2) {
            this.installId = installId2;
            return this;
        }

        public EventBuilder displayName(String displayName2) {
            this.displayName = displayName2;
            return this;
        }

        public EventBuilder appKey(String appKey2) {
            this.appKey = appKey2;
            return this;
        }

        public EventBuilder platform(String platform2) {
            this.platform = platform2;
            return this;
        }

        public EventBuilder sdkVersion(String sdkVersion2) {
            this.sdkVersion = sdkVersion2;
            return this;
        }

        public EventBuilder locale(String locale2) {
            this.locale = locale2;
            return this;
        }

        public EventBuilder registeredSessionId(String regSessionId) {
            this.registeredSessionId = regSessionId;
            return this;
        }

        public EventBuilder connectedSessionId(String conSessionId) {
            this.connectedSessionId = conSessionId;
            return this;
        }

        public EventBuilder productId(String productId2) {
            this.productId = productId2;
            return this;
        }

        public EventBuilder productName(String productName2) {
            this.productName = productName2;
            return this;
        }

        public EventBuilder cameraName(String cameraName2) {
            this.cameraName = cameraName2;
            return this;
        }

        public EventBuilder remoteControllerId(String remoteControllerId2) {
            this.remoteControllerId = remoteControllerId2;
            return this;
        }

        public EventBuilder remoteControllerName(String remoteControllerName2) {
            this.remoteControllerName = remoteControllerName2;
            return this;
        }

        public EventBuilder category(String category2) {
            this.category = category2;
            return this;
        }

        public EventBuilder productFirmwareVersion(String productFirmwareVersion2) {
            this.productFirmwareVersion = productFirmwareVersion2;
            return this;
        }

        public EventBuilder isReleaseMode(boolean isRelease) {
            this.isReleaseMode = isRelease;
            return this;
        }

        public EventBuilder event(String event2) {
            this.event = event2;
            return this;
        }

        public EventBuilder extra(JSONObject extra2) {
            Map<String, Object> map = new HashMap<>();
            try {
                Iterator<?> keys = extra2.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    map.put(key, extra2.get(key));
                }
            } catch (Exception e) {
            }
            this.extra = map;
            return this;
        }

        public EventBuilder extra(Map<String, Object> extra2) {
            this.extra = extra2;
            return this;
        }

        public synchronized DJIAnalyticsEvent build() {
            this.eventId = UUID.randomUUID().toString();
            this.time = AnalyticsEventHelper.getCurrentTime();
            return new DJIAnalyticsEvent(this.time, this.installId, this.eventId, this.displayName, this.appKey, this.platform, this.sdkVersion, this.locale, this.registeredSessionId, this.connectedSessionId, this.productId, this.productName, this.remoteControllerId, this.remoteControllerName, this.cameraName, this.category, this.event, this.productFirmwareVersion, this.isReleaseMode, this.extra);
        }
    }

    public String printString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("EventCreatedTimestamp: ").append(this.eventCreatedTimestamp).append("\n").append("InstallID: ").append(this.installId).append("\n").append("EventID: ").append(this.eventId).append("\n").append("DisplayName: ").append(this.displayName).append("\n").append("AppKey: ").append(this.appKey).append("\n").append("Platform: ").append(this.platform).append("\n").append("SdkVersion: ").append(this.sdkVersion).append("\n").append("Locale: ").append(this.locale).append("\n").append("RegisteredSessionId: ").append(this.registeredSessionId).append("\n").append("ConnectedSessionId: ").append(this.connectedSessionId).append("\n").append("ProductId: ").append(this.productId).append("\n").append("ProductName: ").append(this.productName).append("\n").append("RemoteControllerId: ").append(this.remoteControllerId).append("\n").append("RemoteControllerName: ").append(this.remoteControllerName).append("\n").append("CameraName: ").append(this.cameraName).append("\n").append("EventCategory: ").append(this.category).append("\n").append("EventName: ").append(this.event).append("\n");
        if (this.extra != null) {
            sb.append("\nOptional Key/Value pairs: ").append("\n");
            for (Map.Entry<String, Object> item : this.extra.entrySet()) {
                sb.append((String) item.getKey()).append(": ");
                if (item.getValue() != null) {
                    sb.append(item.getValue().toString()).append("\n");
                } else {
                    sb.append("null").append("\n");
                }
            }
        }
        return sb.toString();
    }
}
