package com.example.alex.balance.custom.realm;

import android.graphics.Color;

import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_COLOR;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_NAME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_TIME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_IS_PROFIT_NAME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_LOSS_NAME;

/**
 * Created by alex on 29.03.17.
 */

public class RealmHelper {
    private static volatile RealmHelper mRealmHelper;
    private static Realm mRealm;

    public static RealmHelper getInstance() {
        if (mRealmHelper == null) {
            synchronized (RealmHelper.class) {
                if (mRealmHelper == null)
                    mRealmHelper = new RealmHelper();
            }
        }
        return mRealmHelper;
    }

    public RealmHelper() {
        mRealm = Realm.getInstance(BalanceRealmConfig.getRealmConfiguration());
    }

    public RealmResults<CategoryData> getCategorySorted() {
        return mRealm.where(CategoryData.class).findAllSorted(new String[]{CATEGORY_LOSS_NAME, CATEGORY_IS_PROFIT_NAME}, new Sort[]{Sort.ASCENDING, Sort.ASCENDING});
    }

    public void createBalanceData(String totalSum, String day, String month, String year, String comment, boolean isProfit, CategoryData categoryData) {
        mRealm.beginTransaction();

        BalanceData data = mRealm.createObject(BalanceData.class);
        data.setTotalSum(Float.parseFloat(totalSum));
        data.setDay(day);
        data.setMonth(month);
        data.setYear(year);
        data.setComment(comment);
        data.setTimeStamp(System.currentTimeMillis());
        data.setIsProfit(isProfit);
        data.setCategory(categoryData);

        mRealm.commitTransaction();
    }

    public CategoryData createCategoryData(String name, String icon, String color) {
        mRealm.beginTransaction();
        CategoryData categoryData = mRealm.createObject(CategoryData.class);
        categoryData.setName(name);
        categoryData.setIconName(icon);
        categoryData.setColor(Color.parseColor(color));
        categoryData.setTimeStamp(System.currentTimeMillis());
        mRealm.commitTransaction();

        return categoryData;
    }

    public CategoryData getSelectedCategory(String categoryName, int categoryColor, long categoryTimeStamp) {
        return mRealm.where(CategoryData.class)
                .equalTo(CATEGORY_FIELD_NAME, categoryName)
                .equalTo(CATEGORY_FIELD_TIME, categoryTimeStamp)
                .equalTo(CATEGORY_FIELD_COLOR, categoryColor)
                .findFirst();
    }

    public void addCategoryProfitOrLose(String totalSum, boolean isProfit, CategoryData categoryData) {
        mRealm.beginTransaction();
        getSelectedCategory(categoryData.getName(), categoryData.getColor(), categoryData.getTimeStamp()).addProfOrLoss(totalSum, isProfit);
        mRealm.commitTransaction();
    }
}
