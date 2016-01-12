package ru.katakin.sample.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ru.katakin.sample.R;

public class SimpleInfoDialog extends DialogFragment {

    public static SimpleInfoDialog newInstance(String msg, boolean cancelable, String key) {
        Bundle args = new Bundle();
        args.putString("msg", msg);
        args.putBoolean("cancelable", cancelable);
        args.putString("key", key);

        SimpleInfoDialog dialog = new SimpleInfoDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String msg = args.getString("msg");
        boolean cancelable = args.getBoolean("cancelable");
        final String key = args.getString("key");
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setMessage(msg)
                .setCancelable(cancelable)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (getActivity() instanceof BaseActivity)
                            ((BaseActivity) getActivity()).doDismiss(key);
                    }
                })
                .setPositiveButton(R.string.close, null);
        return adb.create();
    }
}