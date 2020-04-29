package com.mapzen.android.lost.internal;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import com.mapzen.lost.R;

public class SettingsDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private DialogInterface.OnClickListener externalListener;

    public void setOnClickListener(DialogInterface.OnClickListener listener) {
        this.externalListener = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity()).setTitle((CharSequence) null).setMessage(R.string.settings_alert_title).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).setPositiveButton(R.string.ok, this).create();
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        if (this.externalListener != null) {
            this.externalListener.onClick(dialogInterface, i);
        }
    }
}
