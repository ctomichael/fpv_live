package com.google.firebase;

import android.support.annotation.NonNull;

public class FirebaseApiNotAvailableException extends FirebaseException {
    public FirebaseApiNotAvailableException(@NonNull String str) {
        super(str);
    }
}
