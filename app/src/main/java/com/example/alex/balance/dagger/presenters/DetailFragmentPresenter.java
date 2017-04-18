package com.example.alex.balance.dagger.presenters;

import android.content.Intent;
import android.os.Bundle;

import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.dialogs.CreateCategoryDialog;
import com.example.alex.balance.views.DetailFragment;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_COLOR;
import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_NAME;

public class DetailFragmentPresenter extends BasePresenter<DetailFragment> {
    public static final String CATEGORY_POSITION_KEY = "detail_fragment_presenter_category_position_key";
    private static final int EDIT_CATEGORY_KEY = 1000;
    private CategoryData mCategoryData;
    private RealmHelper mHelper;

    @Inject
    public DetailFragmentPresenter(DetailFragment fragment, RealmHelper helper) {
        bindView(fragment);
        this.mHelper = helper;
    }

    public void initView() {
        Bundle args = mView.getArguments();
        mCategoryData = mHelper.getCategorySorted().get(args == null ? 0 : args.getInt(CATEGORY_POSITION_KEY));
        mView.setCatNameAndColor(mCategoryData.getName(), mCategoryData.getColor());
    }

    public void showEditCatDialog() {
        CreateCategoryDialog dialog = CreateCategoryDialog.newInstance(mCategoryData.getName(), mCategoryData.getColor());
        dialog.setTargetFragment(mView, EDIT_CATEGORY_KEY);
        dialog.show(mView.getFragmentManager(), CreateCategoryDialog.class.getName());
    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == EDIT_CATEGORY_KEY) {
            final String newName = data.getStringExtra(CREATE_CATEGORY_NAME);
            final int newColor = data.getIntExtra(CREATE_CATEGORY_COLOR, 0);
            mHelper.editCategoryNameAndColor(mCategoryData, newName, newColor);

            mView.setCatNameAndColor(newName, newColor);
        }
    }

    public String getTotal() {
        return String.format(Locale.US, "%.2f", mCategoryData.getProfit() - mCategoryData.getLoss());
    }

    public List<BalanceData> getBalanceList() {
        return mHelper.getBalanceList(mCategoryData);
    }
}