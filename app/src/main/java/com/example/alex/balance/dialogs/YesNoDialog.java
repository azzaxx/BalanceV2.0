package com.example.alex.balance.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by alex on 16.01.17.
 */

public class YesNoDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private static final String DLG_TITLE_KEY = "dlg_yes_no_title_key";
    private static final String DLG_YES_KEY = "dlg_yes_no_yes_btn_key";
    private static final String DLG_NO_KEY = "dlg_yes_no_no_btn_key";
    private static final String DLG_CONTENT_KEY = "dlg_yes_no_content_key";
    public static final String DLG_YES_NO_RESULT = "dlg_yes_no_result_key";

    public static YesNoDialog newInstance(String title,
                                          String content,
                                          String yesBtn,
                                          String noBtn) {
        Bundle args = new Bundle();

        args.putString(DLG_TITLE_KEY, title);
        args.putString(DLG_CONTENT_KEY, content);
        args.putString(DLG_YES_KEY, yesBtn);
        args.putString(DLG_NO_KEY, noBtn);

        YesNoDialog fragment = new YesNoDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        if (args.getString(DLG_TITLE_KEY) != null) {
            dialog.setTitle(args.getString(DLG_TITLE_KEY));
        }
        if (args.getString(DLG_CONTENT_KEY) != null) {
            dialog.setMessage(args.getString(DLG_CONTENT_KEY));
        }
        if (args.getString(DLG_YES_KEY) != null) {
            dialog.setPositiveButton(args.getString(DLG_YES_KEY), this);
        }
        if (args.getString(DLG_NO_KEY) != null) {
            dialog.setNegativeButton(args.getString(DLG_NO_KEY), this);
        }

        return dialog.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, new Intent().putExtra(DLG_YES_NO_RESULT, which));
        }
        this.dismiss();
    }
}
