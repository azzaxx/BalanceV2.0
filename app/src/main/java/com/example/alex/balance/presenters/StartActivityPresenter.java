package com.example.alex.balance.presenters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.FilterSettings;
import com.example.alex.balance.dialogs.FilterDialog;
import com.example.alex.balance.views.StartActivity;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.Sort;

import static com.example.alex.balance.custom.BalanceData.BALANCE_DATA_FIELD_IS_PROFIT;
import static com.example.alex.balance.custom.BalanceData.BALANCE_DATA_FIELD_TIME;
import static com.example.alex.balance.custom.BalanceData.BALANCE_DATA_FIELD_TOTAL_SUM;
import static com.example.alex.balance.custom.FilterSettings.DEFAULT_FILTER_VALUE;

/**
 * Created by alex on 09.02.17.
 */

public class StartActivityPresenter extends BasePresenter<StartActivity> {

    public void calculateTotalBalance(List<BalanceData> results) {
        float totalBalance = 0f;
        Realm realm = mView.getRealm();
        realm.beginTransaction();

        for (BalanceData data : results) {
            if (data.isProfit()) {
                totalBalance += data.getTotalSum();
            } else {
                totalBalance -= data.getTotalSum();
            }
        }

        realm.commitTransaction();
        mView.setTotalSum(totalBalance);
    }

    private void removeDataFromCategory(BalanceData balanceData) {
        CategoryData oldData = balanceData.getCategory();
        CategoryData data = mView.getRealm().where(CategoryData.class)
                .equalTo(CategoryData.CATEGORY_FIELD_NAME, oldData.getName())
                .equalTo(CategoryData.CATEGORY_FIELD_COLOR, oldData.getColor())
                .equalTo(CategoryData.CATEGORY_FIELD_TIME, oldData.getTimeStamp())
                .findFirst();
        if (balanceData.isProfit())
            data.removeProfit(oldData.getProfit());
        else
            data.removeLoss(oldData.getLoss());
    }

    public List<BalanceData> enableFilter(final FilterSettings settings) {
        RealmQuery<BalanceData> query = mView.getRealm().where(BalanceData.class);

        if (!settings.isProfit) {
            query.equalTo(BALANCE_DATA_FIELD_IS_PROFIT, false);
        }
        if (!settings.isLoss) {
            query.equalTo(BALANCE_DATA_FIELD_IS_PROFIT, true);
        }
        if (settings.minValue != DEFAULT_FILTER_VALUE) {
            query.greaterThan(BALANCE_DATA_FIELD_TOTAL_SUM, settings.minValue);
        }
        if (settings.maxValue != DEFAULT_FILTER_VALUE) {
            query.lessThan(BALANCE_DATA_FIELD_TOTAL_SUM, settings.maxValue);
        }

        List<BalanceData> result = query.findAllSorted(BALANCE_DATA_FIELD_TIME, Sort.DESCENDING);

        if (!settings.isShowAll) {
            result = filerListByCategoryInside(result, settings);
        }

        calculateTotalBalance(result);
        return result;
    }

    private RealmList<BalanceData> filerListByCategoryInside(final List<BalanceData> result, final FilterSettings settings) {
        RealmList<BalanceData> list = new RealmList<>();

        for (int i = 0; i < result.size(); i++) {
            CategoryData category = result.get(i).getCategory();
            for (CategoryData categoryData : settings.filterCategoryList) {
                if (category.getName().equals(categoryData.getName())
                        && category.getTimeStamp() == categoryData.getTimeStamp()
                        && category.getColor() == categoryData.getColor())
                    list.add(result.get(i));
            }
        }

        return list;
    }

    public void showDeleteMessageDialog(final BalanceData balanceData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mView);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE)
                    removeItemFromList(balanceData);
                dialog.dismiss();
            }
        };
        builder.setMessage(mView.getResources().getString(R.string.are_you_sure));
        builder.setPositiveButton(mView.getResources().getString(R.string.delete), onClickListener);
        builder.setNegativeButton(mView.getResources().getString(R.string.cancel), onClickListener);
        builder.create().show();
    }

    private void removeItemFromList(BalanceData balanceData) {
        Realm realm = mView.getRealm();
        realm.beginTransaction();
        removeDataFromCategory(balanceData);
        balanceData.deleteFromRealm();
        mView.getAdapter().notifyDataSetChanged();
        realm.commitTransaction();
        calculateTotalBalance(realm.where(BalanceData.class).findAll());
    }

    public void showFilterDialog(FilterSettings settings) {
        FilterDialog.newInstance(settings).show(mView.getSupportFragmentManager(), FilterDialog.class.toString());
    }
}
