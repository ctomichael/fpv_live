package com.mapbox.android.telemetry;

import com.amap.location.common.model.AmapLoc;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArraySet;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class TelemetryClient {
    private static final String ACCESS_TOKEN_QUERY_PARAMETER = "access_token";
    private static final String ATTACHMENTS_ENDPOINT = "/attachments/v1";
    private static final String BOUNDARY = "--01ead4a5-7a67-4703-ad02-589886e00923";
    private static final String EVENTS_ENDPOINT = "/events/v2";
    private static final String EXTRA_DEBUGGING_LOG = "Sending POST to %s with %d event(s) (user agent: %s) with payload: %s";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String LOG_TAG = "TelemetryClient";
    private static final String USER_AGENT_REQUEST_HEADER = "User-Agent";
    private String accessToken;
    private CertificateBlacklist certificateBlacklist;
    private final Logger logger;
    private TelemetryClientSettings setting;
    private String userAgent;

    TelemetryClient(String accessToken2, String userAgent2, TelemetryClientSettings setting2, Logger logger2, CertificateBlacklist certificateBlacklist2) {
        this.accessToken = accessToken2;
        this.userAgent = userAgent2;
        this.setting = setting2;
        this.logger = logger2;
        this.certificateBlacklist = certificateBlacklist2;
    }

    /* access modifiers changed from: package-private */
    public void updateAccessToken(String accessToken2) {
        this.accessToken = accessToken2;
    }

    /* access modifiers changed from: package-private */
    public void updateUserAgent(String userAgent2) {
        this.userAgent = userAgent2;
    }

    /* access modifiers changed from: package-private */
    public void sendEvents(List<Event> events, Callback callback, boolean serializeNulls) {
        sendBatch(Collections.unmodifiableList(events), callback, serializeNulls);
    }

    /* access modifiers changed from: package-private */
    public void sendAttachment(Attachment attachment, CopyOnWriteArraySet<AttachmentListener> attachmentListeners) {
        List<FileAttachment> visionAttachments = attachment.getAttachments();
        List<AttachmentMetadata> metadataList = new ArrayList<>();
        final List<String> fileIds = new ArrayList<>();
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder(BOUNDARY).setType(MultipartBody.FORM);
        for (FileAttachment fileAttachment : visionAttachments) {
            FileData fileData = fileAttachment.getFileData();
            AttachmentMetadata attachmentMetadata = fileAttachment.getAttachmentMetadata();
            metadataList.add(attachmentMetadata);
            requestBodyBuilder.addFormDataPart(AmapLoc.TYPE_OFFLINE_CELL, attachmentMetadata.getName(), RequestBody.create(fileData.getType(), new File(fileData.getFilePath())));
            fileIds.add(attachmentMetadata.getFileId());
        }
        requestBodyBuilder.addFormDataPart("attachments", new Gson().toJson(metadataList));
        RequestBody requestBody = reverseMultiForm(requestBodyBuilder);
        HttpUrl requestUrl = this.setting.getBaseUrl().newBuilder(ATTACHMENTS_ENDPOINT).addQueryParameter(ACCESS_TOKEN_QUERY_PARAMETER, this.accessToken).build();
        if (isExtraDebuggingNeeded()) {
            this.logger.debug(LOG_TAG, String.format(Locale.US, EXTRA_DEBUGGING_LOG, requestUrl, Integer.valueOf(visionAttachments.size()), this.userAgent, metadataList));
        }
        final CopyOnWriteArraySet<AttachmentListener> copyOnWriteArraySet = attachmentListeners;
        this.setting.getAttachmentClient(this.certificateBlacklist).newCall(new Request.Builder().url(requestUrl).header(USER_AGENT_REQUEST_HEADER, this.userAgent).post(requestBody).build()).enqueue(new Callback() {
            /* class com.mapbox.android.telemetry.TelemetryClient.AnonymousClass1 */

            public void onFailure(Call call, IOException exception) {
                Iterator it2 = copyOnWriteArraySet.iterator();
                while (it2.hasNext()) {
                    ((AttachmentListener) it2.next()).onAttachmentFailure(exception.getMessage(), fileIds);
                }
            }

            public void onResponse(Call call, Response response) {
                Iterator it2 = copyOnWriteArraySet.iterator();
                while (it2.hasNext()) {
                    ((AttachmentListener) it2.next()).onAttachmentResponse(response.message(), response.code(), fileIds);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void updateDebugLoggingEnabled(boolean debugLoggingEnabled) {
        this.setting = this.setting.toBuilder().debugLoggingEnabled(debugLoggingEnabled).build();
    }

    /* access modifiers changed from: package-private */
    public String obtainAccessToken() {
        return this.accessToken;
    }

    /* access modifiers changed from: package-private */
    public TelemetryClientSettings obtainSetting() {
        return this.setting;
    }

    private void sendBatch(List<Event> batch, Callback callback, boolean serializeNulls) {
        String payload = (serializeNulls ? new GsonBuilder().serializeNulls().create() : new Gson()).toJson(batch);
        RequestBody body = RequestBody.create(JSON, payload);
        HttpUrl url = this.setting.getBaseUrl().newBuilder(EVENTS_ENDPOINT).addQueryParameter(ACCESS_TOKEN_QUERY_PARAMETER, this.accessToken).build();
        if (isExtraDebuggingNeeded()) {
            this.logger.debug(LOG_TAG, String.format(Locale.US, EXTRA_DEBUGGING_LOG, url, Integer.valueOf(batch.size()), this.userAgent, payload));
        }
        this.setting.getClient(this.certificateBlacklist).newCall(new Request.Builder().url(url).header(USER_AGENT_REQUEST_HEADER, this.userAgent).post(body).build()).enqueue(callback);
    }

    private boolean isExtraDebuggingNeeded() {
        return this.setting.isDebugLoggingEnabled() || this.setting.getEnvironment().equals(Environment.STAGING);
    }

    private RequestBody reverseMultiForm(MultipartBody.Builder builder) {
        MultipartBody multipartBody = builder.build();
        MultipartBody.Builder builder2 = new MultipartBody.Builder(BOUNDARY).setType(MultipartBody.FORM);
        for (int i = multipartBody.size() - 1; i > -1; i--) {
            builder2.addPart(multipartBody.part(i));
        }
        return builder2.build();
    }
}
