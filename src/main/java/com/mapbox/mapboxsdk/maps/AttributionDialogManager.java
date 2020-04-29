package com.mapbox.mapboxsdk.maps;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.mapbox.mapboxsdk.MapStrictMode;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.attribution.Attribution;
import com.mapbox.mapboxsdk.attribution.AttributionParser;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.style.sources.Source;
import dji.publics.protocol.ResponseBase;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributionDialogManager implements View.OnClickListener, DialogInterface.OnClickListener {
    private static final String MAP_FEEDBACK_STYLE_URI_REGEX = "^(.*://[^:^/]*)/(.*)/(.*)";
    private static final String MAP_FEEDBACK_URL = "https://apps.mapbox.com/feedback";
    private static final String MAP_FEEDBACK_URL_LOCATION_FRAGMENT_FORMAT = "/%f/%f/%f/%f/%d";
    private static final String MAP_FEEDBACK_URL_OLD = "https://www.mapbox.com/map-feedback";
    private Set<Attribution> attributionSet;
    /* access modifiers changed from: private */
    @NonNull
    public final Context context;
    private AlertDialog dialog;
    @NonNull
    private final MapboxMap mapboxMap;

    public AttributionDialogManager(@NonNull Context context2, @NonNull MapboxMap mapboxMap2) {
        this.context = context2;
        this.mapboxMap = mapboxMap2;
    }

    public void onClick(@NonNull View view) {
        this.attributionSet = new AttributionBuilder(this.mapboxMap, view.getContext()).build();
        boolean isActivityFinishing = false;
        if (this.context instanceof Activity) {
            isActivityFinishing = ((Activity) this.context).isFinishing();
        }
        if (!isActivityFinishing) {
            showAttributionDialog(getAttributionTitles());
        }
    }

    /* access modifiers changed from: protected */
    public void showAttributionDialog(@NonNull String[] attributionTitles) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(R.string.mapbox_attributionsDialogTitle);
        builder.setAdapter(new ArrayAdapter(this.context, R.layout.mapbox_attribution_list_item, attributionTitles), this);
        this.dialog = builder.show();
    }

    private String[] getAttributionTitles() {
        List<String> titles = new ArrayList<>();
        for (Attribution attribution : this.attributionSet) {
            titles.add(attribution.getTitle());
        }
        return (String[]) titles.toArray(new String[titles.size()]);
    }

    public void onClick(DialogInterface dialog2, int which) {
        if (isLatestEntry(which)) {
            showTelemetryDialog();
        } else {
            showMapAttributionWebPage(which);
        }
    }

    public void onStop() {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
    }

    private boolean isLatestEntry(int attributionKeyIndex) {
        return attributionKeyIndex == getAttributionTitles().length + -1;
    }

    private void showTelemetryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle(R.string.mapbox_attributionTelemetryTitle);
        builder.setMessage(R.string.mapbox_attributionTelemetryMessage);
        builder.setPositiveButton(R.string.mapbox_attributionTelemetryPositive, new DialogInterface.OnClickListener() {
            /* class com.mapbox.mapboxsdk.maps.AttributionDialogManager.AnonymousClass1 */

            public void onClick(@NonNull DialogInterface dialog, int which) {
                TelemetryDefinition telemetry = Mapbox.getTelemetry();
                if (telemetry != null) {
                    telemetry.setUserTelemetryRequestState(true);
                }
                dialog.cancel();
            }
        });
        builder.setNeutralButton(R.string.mapbox_attributionTelemetryNeutral, new DialogInterface.OnClickListener() {
            /* class com.mapbox.mapboxsdk.maps.AttributionDialogManager.AnonymousClass2 */

            public void onClick(@NonNull DialogInterface dialog, int which) {
                AttributionDialogManager.this.showWebPage(AttributionDialogManager.this.context.getResources().getString(R.string.mapbox_telemetryLink));
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.mapbox_attributionTelemetryNegative, new DialogInterface.OnClickListener() {
            /* class com.mapbox.mapboxsdk.maps.AttributionDialogManager.AnonymousClass3 */

            public void onClick(@NonNull DialogInterface dialog, int which) {
                TelemetryDefinition telemetry = Mapbox.getTelemetry();
                if (telemetry != null) {
                    telemetry.setUserTelemetryRequestState(false);
                }
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void showMapAttributionWebPage(int which) {
        String url = ((Attribution[]) this.attributionSet.toArray(new Attribution[this.attributionSet.size()]))[which].getUrl();
        if (url.contains(MAP_FEEDBACK_URL_OLD) || url.contains(MAP_FEEDBACK_URL)) {
            url = buildMapFeedbackMapUrl(Mapbox.getAccessToken());
        }
        showWebPage(url);
    }

    /* access modifiers changed from: package-private */
    @NonNull
    public String buildMapFeedbackMapUrl(@Nullable String accessToken) {
        Uri.Builder builder = Uri.parse(MAP_FEEDBACK_URL).buildUpon();
        CameraPosition cameraPosition = this.mapboxMap.getCameraPosition();
        if (cameraPosition != null) {
            builder.encodedFragment(String.format(Locale.getDefault(), MAP_FEEDBACK_URL_LOCATION_FRAGMENT_FORMAT, Double.valueOf(cameraPosition.target.getLongitude()), Double.valueOf(cameraPosition.target.getLatitude()), Double.valueOf(cameraPosition.zoom), Double.valueOf(cameraPosition.bearing), Integer.valueOf((int) cameraPosition.tilt)));
        }
        String packageName = this.context.getApplicationContext().getPackageName();
        if (packageName != null) {
            builder.appendQueryParameter("referrer", packageName);
        }
        if (accessToken != null) {
            builder.appendQueryParameter("access_token", accessToken);
        }
        Style style = this.mapboxMap.getStyle();
        if (style != null) {
            Matcher matcher = Pattern.compile(MAP_FEEDBACK_STYLE_URI_REGEX).matcher(style.getUri());
            if (matcher.find()) {
                String styleOwner = matcher.group(2);
                builder.appendQueryParameter("owner", styleOwner).appendQueryParameter(ResponseBase.STRING_ID, matcher.group(3));
            }
        }
        return builder.build().toString();
    }

    /* access modifiers changed from: private */
    public void showWebPage(@NonNull String url) {
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            this.context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(this.context, R.string.mapbox_attributionErrorNoBrowser, 1).show();
            MapStrictMode.strictModeViolation(exception);
        }
    }

    private static class AttributionBuilder {
        @NonNull
        private final WeakReference<Context> context;
        private final MapboxMap mapboxMap;

        AttributionBuilder(MapboxMap mapboxMap2, Context context2) {
            this.mapboxMap = mapboxMap2;
            this.context = new WeakReference<>(context2);
        }

        /* access modifiers changed from: private */
        public Set<Attribution> build() {
            Context context2 = this.context.get();
            if (context2 == null) {
                return Collections.emptySet();
            }
            List<String> attributions = new ArrayList<>();
            Style style = this.mapboxMap.getStyle();
            if (style != null) {
                for (Source source : style.getSources()) {
                    String attribution = source.getAttribution();
                    if (!attribution.isEmpty()) {
                        attributions.add(attribution);
                    }
                }
            }
            return new AttributionParser.Options(context2).withCopyrightSign(true).withImproveMap(true).withTelemetryAttribution(true).withAttributionData((String[]) attributions.toArray(new String[attributions.size()])).build().getAttributions();
        }
    }
}
