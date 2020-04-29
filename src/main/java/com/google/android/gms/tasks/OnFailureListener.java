package com.google.android.gms.tasks;

import android.support.annotation.NonNull;

public interface OnFailureListener {
    void onFailure(@NonNull Exception exc);
}
