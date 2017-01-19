package com.example.alex.balance.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.balance.R;

/**
 * Created by alex on 18.01.17.
 */

public class CreateCategoryDialog extends DialogFragment implements View.OnClickListener {
    public static final String CREATE_CATEGORY_NAME = "create_category_dialog_name_key";
    public static final String CREATE_CATEGORY_COLOR = "create_category_dialog_color_key";
    private int[] colorId = {
            R.id.color_accent,
            R.id.color_black,
            R.id.color_blue,
            R.id.color_green,
            R.id.color_orange,
            R.id.color_pink,
            R.id.color_red,
            R.id.color_violet,
            R.id.color_yellow,
            R.id.create_category_cancel_button,
            R.id.create_category_ok_button
    };
    private TextView mTvColor;
    private EditText mETName;

    public static CreateCategoryDialog newInstance() {
        Bundle args = new Bundle();

        CreateCategoryDialog fragment = new CreateCategoryDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.create_category_dialog, null);

        for (int i : colorId) {
            view.findViewById(i).setOnClickListener(this);
        }
        mTvColor = (TextView) view.findViewById(R.id.create_category_tv_cat_color);
        mETName = (EditText) view.findViewById(R.id.create_category_dialog_et_cat_name);

        builder.setView(view);

        builder.setCancelable(false);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_category_cancel_button:
                this.getDialog().dismiss();
                break;
            case R.id.create_category_ok_button:
                if (mETName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please write name", Toast.LENGTH_SHORT).show();
                    return;
                }
                putCategoryData(mETName.getText().toString(), mETName.getCurrentTextColor());
                this.getDialog().dismiss();
                break;
            default:
                mTvColor.setTextColor(((ColorDrawable) v.getBackground()).getColor());
        }
    }

    private void putCategoryData(String name, int color) {
        if (getTargetFragment() != null) {
            Intent intent = new Intent();

            intent.putExtra(CREATE_CATEGORY_NAME, name);
            intent.putExtra(CREATE_CATEGORY_COLOR, color);

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        }
    }
}
