package com.mapzen.android.lost.api;

import com.mapzen.android.lost.internal.DwellServiceIntentFactory;
import com.mapzen.android.lost.internal.FusedLocationProviderApiImpl;
import com.mapzen.android.lost.internal.FusedLocationServiceCallbackManager;
import com.mapzen.android.lost.internal.FusedLocationServiceConnectionManager;
import com.mapzen.android.lost.internal.GeofencingApiImpl;
import com.mapzen.android.lost.internal.GeofencingServiceIntentFactory;
import com.mapzen.android.lost.internal.LostClientManager;
import com.mapzen.android.lost.internal.LostRequestManager;
import com.mapzen.android.lost.internal.PendingIntentIdGenerator;
import com.mapzen.android.lost.internal.SettingsApiImpl;

public class LocationServices {
    public static final FusedLocationProviderApi FusedLocationApi = new FusedLocationProviderApiImpl(new FusedLocationServiceConnectionManager(), new FusedLocationServiceCallbackManager(), LostRequestManager.shared(), LostClientManager.shared());
    public static final GeofencingApi GeofencingApi = new GeofencingApiImpl(new GeofencingServiceIntentFactory(), new DwellServiceIntentFactory(), new PendingIntentIdGenerator());
    public static final SettingsApi SettingsApi = new SettingsApiImpl();
}
