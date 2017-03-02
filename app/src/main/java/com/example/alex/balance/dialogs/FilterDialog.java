package com.example.alex.balance.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.alex.balance.R;
import com.example.alex.balance.adapters.FilterRecyclerAdapter;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.FilterSettings;
import com.example.alex.balance.views.StartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmList;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.alex.balance.custom.CategoryData.ADD_CATEGORY_NAME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_NAME;
import static com.example.alex.balance.custom.FilterSettings.DEFAULT_FILTER_VALUE;

/**
 * Created by alex on 01.02.17.
 */

public class FilterDialog extends DialogFragment implements DialogInterface.OnClickListener, AppCompatCheckBox.OnCheckedChangeListener {
    public static final String FILTER_SETTINGS_KEY = "filter_dialog_filter_settings_key";
    @BindView(R.id.filter_lose_switch)
    SwitchCompat mSwitchLoss;
    @BindView(R.id.filter_profit_switch)
    SwitchCompat mSwitchProfit;
    @BindView(R.id.filter_max_check_box)
    AppCompatCheckBox mCheckBoxMax;
    @BindView(R.id.filter_min_check_box)
    AppCompatCheckBox mCheckBoxMin;
    @BindView(R.id.filter_show_all_category_check_box)
    AppCompatCheckBox mCheckBoxShowAll;
    @BindView(R.id.filter_max_et)
    EditText mEtMax;
    @BindView(R.id.filter_min_et)
    EditText mEtMin;
    @BindView(R.id.filter_recycler_block)
    View mLockView;
    FilterRecyclerAdapter adapter;
    private Unbinder unbinder;

    public static FilterDialog newInstance(FilterSettings settings) {
        Bundle args = new Bundle();
        args.putSerializable(FILTER_SETTINGS_KEY, settings);

        FilterDialog fragment = new FilterDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.filter_dialog_layout, null);
        unbinder = ButterKnife.bind(this, view);

        mCheckBoxMax.setOnCheckedChangeListener(this);
        mCheckBoxMin.setOnCheckedChangeListener(this);
        mCheckBoxShowAll.setOnCheckedChangeListener(this);

        RecyclerView mRecycler = (RecyclerView) view.findViewById(R.id.filter_recycler_view);
        Realm realm = ((StartActivity) getActivity()).getRealm();

        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new GridLayoutManager(context, 2));
        adapter = new FilterRecyclerAdapter(context, realm.where(CategoryData.class)
                .notEqualTo(CATEGORY_FIELD_NAME, ADD_CATEGORY_NAME).findAll());
        mRecycler.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null && args.containsKey(FILTER_SETTINGS_KEY)) {
            setFilterSettings((FilterSettings) args.getSerializable(FILTER_SETTINGS_KEY));
        }

        builder.setView(view);
        builder.setNegativeButton(getContext().getString(R.string.cancel), this);
        builder.setPositiveButton(getContext().getString(R.string.apply), this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            collectFilterData();
        }
    }

    private void collectFilterData() {
        FilterSettings settings = (FilterSettings) getArguments().getSerializable(FILTER_SETTINGS_KEY);

        if (settings != null) {
            settings.isProfit = mSwitchProfit.isChecked();
            settings.isLoss = mSwitchLoss.isChecked();
            settings.isShowAll = mCheckBoxShowAll.isChecked();
            settings.minValue = mCheckBoxMin.isChecked() || mEtMin.getText().toString().isEmpty()
                    ? DEFAULT_FILTER_VALUE : Float.valueOf(mEtMin.getText().toString());
            settings.maxValue = mCheckBoxMax.isChecked() || mEtMax.getText().toString().isEmpty()
                    ? DEFAULT_FILTER_VALUE : Float.valueOf(mEtMax.getText().toString());
            settings.filterCategoryList = (RealmList<CategoryData>) adapter.getList();

            ((StartActivity) getActivity()).setFilterSettings(settings);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.filter_max_check_box:
                mEtMax.setEnabled(!isChecked);
                mEtMax.setText("");
                break;
            case R.id.filter_min_check_box:
                mEtMin.setEnabled(!isChecked);
                mEtMin.setText("");
                break;
            case R.id.filter_show_all_category_check_box:
                mLockView.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                break;
        }
    }

    public void setFilterSettings(final FilterSettings filterSettings) {
        final boolean maxChecked = filterSettings.maxValue == DEFAULT_FILTER_VALUE;
        final boolean minChecked = filterSettings.minValue == DEFAULT_FILTER_VALUE;

        mSwitchProfit.setChecked(filterSettings.isProfit);
        mSwitchLoss.setChecked(filterSettings.isLoss);
        mCheckBoxShowAll.setChecked(filterSettings.isShowAll);
        mCheckBoxMax.setChecked(maxChecked);
        mCheckBoxMin.setChecked(minChecked);

        if (!maxChecked) {
            mEtMax.setText(String.valueOf(filterSettings.maxValue));
        }

        if (!minChecked) {
            mEtMin.setText(String.valueOf(filterSettings.minValue));
        }
        adapter.setRealmList(filterSettings.filterCategoryList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
