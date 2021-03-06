package com.example.alex.balance.custom.realm;

import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_COLOR;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_NAME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_TIME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_IS_PROFIT_NAME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_LOSS_NAME;

public class RealmHelper {
    private Realm mRealm;

    public RealmHelper() {
        mRealm = Realm.getInstance(BalanceRealmConfig.getRealmConfiguration());
    }

    public RealmResults<CategoryData> getCategorySorted() {
        return mRealm.where(CategoryData.class).findAllSorted(new String[]{CATEGORY_LOSS_NAME, CATEGORY_IS_PROFIT_NAME}, new Sort[]{Sort.DESCENDING, Sort.DESCENDING});
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

    public CategoryData createCategoryData(String name, int color) {
        mRealm.beginTransaction();
        CategoryData categoryData = mRealm.createObject(CategoryData.class);
        categoryData.setName(name);
        categoryData.setTimeStamp(System.currentTimeMillis());
        categoryData.setColor(color);
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

    public void setLastCategoryDate(CategoryData categoryData, String day, String month) {
        mRealm.beginTransaction();
        getSelectedCategory(categoryData.getName(), categoryData.getColor(), categoryData.getTimeStamp()).setLastDate(day, month);
        mRealm.commitTransaction();
    }

    public void editCategoryNameAndColor(CategoryData categoryData, String newName, int newColor) {
        mRealm.beginTransaction();
        CategoryData newData = getSelectedCategory(categoryData.getName(), categoryData.getColor(), categoryData.getTimeStamp());
        newData.setName(newName);
        newData.setColor(newColor);
        mRealm.commitTransaction();
    }

    public List<BalanceData> getBalanceList(CategoryData data) {
        List<BalanceData> list = new ArrayList<>();

        for (BalanceData balanceData : mRealm.where(BalanceData.class).findAll()) {
            if (balanceData.getCategory().getTimeStamp() == data.getTimeStamp())
                list.add(balanceData);
        }

        return list;
    }
}
