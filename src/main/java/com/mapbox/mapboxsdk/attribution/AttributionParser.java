package com.mapbox.mapboxsdk.attribution;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import com.mapbox.mapboxsdk.R;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;

public class AttributionParser {
    private static final String IMPROVE_THIS_MAP = "Improve this map";
    private final String attributionData;
    private final Set<Attribution> attributions = new LinkedHashSet();
    private final WeakReference<Context> context;
    private final boolean withCopyrightSign;
    private final boolean withImproveMap;
    private final boolean withMapboxAttribution;
    private final boolean withTelemetryAttribution;

    AttributionParser(WeakReference<Context> context2, String attributionData2, boolean withImproveMap2, boolean withCopyrightSign2, boolean withTelemetryAttribution2, boolean withMapboxAttribution2) {
        this.context = context2;
        this.attributionData = attributionData2;
        this.withImproveMap = withImproveMap2;
        this.withCopyrightSign = withCopyrightSign2;
        this.withTelemetryAttribution = withTelemetryAttribution2;
        this.withMapboxAttribution = withMapboxAttribution2;
    }

    @NonNull
    public Set<Attribution> getAttributions() {
        return this.attributions;
    }

    @NonNull
    public String createAttributionString() {
        return createAttributionString(false);
    }

    @NonNull
    public String createAttributionString(boolean shortenedOutput) {
        StringBuilder stringBuilder = new StringBuilder(this.withCopyrightSign ? "" : "© ");
        int counter = 0;
        for (Attribution attribution : this.attributions) {
            counter++;
            stringBuilder.append(!shortenedOutput ? attribution.getTitle() : attribution.getTitleAbbreviated());
            if (counter != this.attributions.size()) {
                stringBuilder.append(" / ");
            }
        }
        return stringBuilder.toString();
    }

    /* access modifiers changed from: protected */
    public void parse() {
        parseAttributions();
        addAdditionalAttributions();
    }

    private void parseAttributions() {
        SpannableStringBuilder htmlBuilder = (SpannableStringBuilder) fromHtml(this.attributionData);
        for (URLSpan urlSpan : (URLSpan[]) htmlBuilder.getSpans(0, htmlBuilder.length(), URLSpan.class)) {
            parseUrlSpan(htmlBuilder, urlSpan);
        }
    }

    private void parseUrlSpan(@NonNull SpannableStringBuilder htmlBuilder, @NonNull URLSpan urlSpan) {
        String url = urlSpan.getURL();
        if (isUrlValid(url)) {
            String anchor = parseAnchorValue(htmlBuilder, urlSpan);
            if (isImproveThisMapAnchor(anchor)) {
                anchor = translateImproveThisMapAnchor(anchor);
            }
            this.attributions.add(new Attribution(anchor, url));
        }
    }

    private boolean isUrlValid(@NonNull String url) {
        return isValidForImproveThisMap(url) && isValidForMapbox(url);
    }

    private boolean isImproveThisMapAnchor(String anchor) {
        return anchor.equals(IMPROVE_THIS_MAP);
    }

    private String translateImproveThisMapAnchor(String anchor) {
        Context context2 = this.context.get();
        if (context2 != null) {
            return context2.getString(R.string.mapbox_telemetryImproveMap);
        }
        return anchor;
    }

    private boolean isValidForImproveThisMap(@NonNull String url) {
        return this.withImproveMap || !Attribution.IMPROVE_MAP_URLS.contains(url);
    }

    private boolean isValidForMapbox(@NonNull String url) {
        return this.withMapboxAttribution || !url.equals("https://www.mapbox.com/about/maps/");
    }

    @NonNull
    private String parseAnchorValue(SpannableStringBuilder htmlBuilder, URLSpan urlSpan) {
        int start = htmlBuilder.getSpanStart(urlSpan);
        int end = htmlBuilder.getSpanEnd(urlSpan);
        char[] charKey = new char[(end - start)];
        htmlBuilder.getChars(start, end, charKey, 0);
        return stripCopyright(String.valueOf(charKey));
    }

    @NonNull
    private String stripCopyright(@NonNull String anchor) {
        if (this.withCopyrightSign || !anchor.startsWith("© ")) {
            return anchor;
        }
        return anchor.substring(2, anchor.length());
    }

    private void addAdditionalAttributions() {
        if (this.withTelemetryAttribution) {
            Context context2 = this.context.get();
            this.attributions.add(new Attribution(context2 != null ? context2.getString(R.string.mapbox_telemetrySettings) : "Telemetry Settings", "https://www.mapbox.com/telemetry/"));
        }
    }

    private static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(html, 0);
        }
        return Html.fromHtml(html);
    }

    public static class Options {
        private String[] attributionDataStringArray;
        private final WeakReference<Context> context;
        private boolean withCopyrightSign = true;
        private boolean withImproveMap = true;
        private boolean withMapboxAttribution = true;
        private boolean withTelemetryAttribution = false;

        public Options(@NonNull Context context2) {
            this.context = new WeakReference<>(context2);
        }

        @NonNull
        public Options withAttributionData(String... attributionData) {
            this.attributionDataStringArray = attributionData;
            return this;
        }

        @NonNull
        public Options withImproveMap(boolean withImproveMap2) {
            this.withImproveMap = withImproveMap2;
            return this;
        }

        @NonNull
        public Options withCopyrightSign(boolean withCopyrightSign2) {
            this.withCopyrightSign = withCopyrightSign2;
            return this;
        }

        @NonNull
        public Options withTelemetryAttribution(boolean withTelemetryAttribution2) {
            this.withTelemetryAttribution = withTelemetryAttribution2;
            return this;
        }

        @NonNull
        public Options withMapboxAttribution(boolean withMapboxAttribution2) {
            this.withMapboxAttribution = withMapboxAttribution2;
            return this;
        }

        @NonNull
        public AttributionParser build() {
            if (this.attributionDataStringArray == null) {
                throw new IllegalStateException("Using builder without providing attribution data");
            }
            AttributionParser attributionParser = new AttributionParser(this.context, parseAttribution(this.attributionDataStringArray), this.withImproveMap, this.withCopyrightSign, this.withTelemetryAttribution, this.withMapboxAttribution);
            attributionParser.parse();
            return attributionParser;
        }

        private String parseAttribution(String[] attribution) {
            StringBuilder builder = new StringBuilder();
            for (String attr : attribution) {
                if (!attr.isEmpty()) {
                    builder.append(attr);
                }
            }
            return builder.toString();
        }
    }
}
