package com.mapbox.android.telemetry;

import android.os.Bundle;

interface EnvironmentResolver {
    void nextChain(EnvironmentResolver environmentResolver);

    ServerInformation obtainServerInformation(Bundle bundle);
}
