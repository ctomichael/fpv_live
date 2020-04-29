package com.mapbox.mapboxsdk.module.telemetry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"ParcelCreator"})
class PerformanceEvent extends MapBaseEvent {
    private static final String PERFORMANCE_TRACE = "mobile.performance_trace";
    private final List<PerformanceAttribute<String>> attributes;
    private final List<PerformanceAttribute<Double>> counters;
    private final JsonObject metadata;
    private final String sessionId;

    PerformanceEvent(PhoneState phoneState, String sessionId2, Bundle bundle) {
        super(phoneState);
        this.sessionId = sessionId2;
        this.attributes = initList(bundle.getString("attributes"), new TypeToken<ArrayList<PerformanceAttribute<String>>>() {
            /* class com.mapbox.mapboxsdk.module.telemetry.PerformanceEvent.AnonymousClass1 */
        });
        this.counters = initList(bundle.getString("counters"), new TypeToken<ArrayList<PerformanceAttribute<Double>>>() {
            /* class com.mapbox.mapboxsdk.module.telemetry.PerformanceEvent.AnonymousClass2 */
        });
        this.metadata = initMetaData(bundle.getString("metadata"));
    }

    private <T> ArrayList<PerformanceAttribute<T>> initList(String fromString, TypeToken typeToken) {
        if (fromString == null || fromString.isEmpty()) {
            return new ArrayList<>();
        }
        return (ArrayList) new Gson().fromJson(fromString, typeToken.getType());
    }

    private JsonObject initMetaData(String fromString) {
        if (fromString == null) {
            return new JsonObject();
        }
        return (JsonObject) new Gson().fromJson(fromString, JsonObject.class);
    }

    /* access modifiers changed from: package-private */
    public String getEventName() {
        return PERFORMANCE_TRACE;
    }

    /* access modifiers changed from: package-private */
    public String getSessionId() {
        return this.sessionId;
    }

    /* access modifiers changed from: package-private */
    public List<PerformanceAttribute<String>> getAttributes() {
        return this.attributes;
    }

    /* access modifiers changed from: package-private */
    public List<PerformanceAttribute<Double>> getCounters() {
        return this.counters;
    }

    /* access modifiers changed from: package-private */
    public JsonObject getMetadata() {
        return this.metadata;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PerformanceEvent that = (PerformanceEvent) o;
        if (this.sessionId != null) {
            if (!this.sessionId.equals(that.sessionId)) {
                return false;
            }
        } else if (that.sessionId != null) {
            return false;
        }
        if (this.attributes != null) {
            if (!this.attributes.equals(that.attributes)) {
                return false;
            }
        } else if (that.attributes != null) {
            return false;
        }
        if (this.counters != null) {
            if (!this.counters.equals(that.counters)) {
                return false;
            }
        } else if (that.counters != null) {
            return false;
        }
        if (this.metadata != null) {
            z = this.metadata.equals(that.metadata);
        } else if (that.metadata != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3 = 0;
        if (this.sessionId != null) {
            result = this.sessionId.hashCode();
        } else {
            result = 0;
        }
        int i4 = result * 31;
        if (this.attributes != null) {
            i = this.attributes.hashCode();
        } else {
            i = 0;
        }
        int i5 = (i4 + i) * 31;
        if (this.counters != null) {
            i2 = this.counters.hashCode();
        } else {
            i2 = 0;
        }
        int i6 = (i5 + i2) * 31;
        if (this.metadata != null) {
            i3 = this.metadata.hashCode();
        }
        return i6 + i3;
    }

    public String toString() {
        return "PerformanceEvent{sessionId='" + this.sessionId + '\'' + ", attributes=" + this.attributes + ", counters=" + this.counters + ", metadata=" + this.metadata + '}';
    }

    static class PerformanceAttribute<T> {
        private final String name;
        private final T value;

        PerformanceAttribute(String name2, T value2) {
            this.name = name2;
            this.value = value2;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PerformanceAttribute<?> that = (PerformanceAttribute) o;
            if (this.name == null ? that.name != null : !this.name.equals(that.name)) {
                return false;
            }
            if (this.value != null) {
                return this.value.equals(that.value);
            }
            if (that.value != null) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            int result;
            int i = 0;
            if (this.name != null) {
                result = this.name.hashCode();
            } else {
                result = 0;
            }
            int i2 = result * 31;
            if (this.value != null) {
                i = this.value.hashCode();
            }
            return i2 + i;
        }
    }
}
