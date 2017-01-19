package com.example.alex.balance.presenters;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.example.alex.balance.BalanceFragment;
import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import io.realm.Realm;

import static com.example.alex.balance.BalanceFragment.CREATE_CATEGORY_DIALOG_REQ_CODE;
import static com.example.alex.balance.BalanceFragment.DATE_DIALOG_REQ_CODE;
import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_COLOR;
import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_NAME;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_DAY_KEY;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_MONTH_KEY;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_YEAR_KEY;

/**
 * Created by alex on 09.01.17.
 */

public class DataPresenter extends BasePresenter<BalanceFragment> {
    public static final String DEFAULT_VALUE = "0.00";
    private static final String DOT = ".";
    private static final String ZERO = "0";
    private static final int MAX_SUM_LENGTH = 9;

    public void setDate(@Nullable Intent date) {
        Calendar calendar = Calendar.getInstance();
        int year = date == null ? calendar.get(Calendar.YEAR) : date.getIntExtra(DATE_DIALOG_YEAR_KEY, 0);
        int month = date == null ? calendar.get(Calendar.MONTH) : date.getIntExtra(DATE_DIALOG_MONTH_KEY, 0);
        int day = date == null ? calendar.get(Calendar.DAY_OF_MONTH) : date.getIntExtra(DATE_DIALOG_DAY_KEY, 0);

        mView.setDate(String.valueOf(day), new DateFormatSymbols().getMonths()[month], String.valueOf(year));
    }

    public void clearOne(String text) {
        if (text.equals(DEFAULT_VALUE)) {
            return;
        }

        String result;
        String temp = text.substring(0, text.length() - 1);
        temp = temp.replace(DOT, "");

        if (temp.length() > 2 && !temp.startsWith(ZERO)) {
            temp = temp.substring(0, temp.length() - 2) + DOT + temp.substring(temp.length() - 2, temp.length());
            result = temp;
        } else {
            if (temp.startsWith(ZERO + DOT)) {
                result = ZERO + DOT + ZERO + temp.substring(0, 1);
            } else if (temp.startsWith(ZERO + DOT + ZERO)) {
                result = DEFAULT_VALUE;
            } else {
                result = ZERO + DOT + temp.substring(0, 2);
            }
        }

        mView.setTotalSum(result);
    }

    public void addOne(String value, String previousValue) {
        if (previousValue.length() > MAX_SUM_LENGTH) {
            return;
        }
        String result;

        if (previousValue.equals(DEFAULT_VALUE)) {
            result = ZERO + DOT + ZERO + value;
        } else if (previousValue.startsWith(ZERO + DOT + ZERO)) {
            result = ZERO + DOT + previousValue.charAt(previousValue.length() - 1) + value;
        } else {
            String temp = previousValue.replace(DOT, "");
            if (temp.startsWith(ZERO)) {
                temp = temp.replaceFirst(ZERO, "");
            }
            temp += value;
            result = temp.substring(0, temp.length() - 2) + DOT + temp.substring(temp.length() - 2, temp.length());
        }

        mView.setTotalSum(result);
    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == DATE_DIALOG_REQ_CODE) {
            setDate(data);
        } else if (requestCode == CREATE_CATEGORY_DIALOG_REQ_CODE) {
            createCategory(data.getStringExtra(CREATE_CATEGORY_NAME), data.getIntExtra(CREATE_CATEGORY_COLOR, -1));
        }
    }

    private void createCategory(String name, int color) {
        Realm realmObj = mView.getAct().getRealm();
        realmObj.beginTransaction();

        CategoryData data = realmObj.createObject(CategoryData.class);
        data.setTimeStamp(System.currentTimeMillis());
        data.setName(name);
        data.setColor(color);

        realmObj.commitTransaction();
        addCategory(name, color);
    }

    private void addCategory(String name, int color) {
        View v = LayoutInflater.from(mView.getContext()).inflate(R.layout.recycler_item_balance, null);
        ((CheckBox) v.findViewById(R.id.item_balance_check_box)).setText(name);
        v.findViewById(R.id.color_box).setBackgroundColor(color);
        mView.addViewCategory(v);
    }

    public void reAddAllCategory() {
        mView.removeAllCategory();
        for (CategoryData data : mView.getAct().getRealm().where(CategoryData.class).findAll()) {
            addCategory(data.getName(), data.getColor());
        }
    }

    public void addBalanceData(String totalSum, String day, String month, String year, String comment, boolean mIsProfit) {
        if (totalSum.equals(DEFAULT_VALUE)) {
            return;
        }

        Realm realmObj = mView.getAct().getRealm();
        realmObj.beginTransaction();

        BalanceData data = realmObj.createObject(BalanceData.class);
        data.setTotalSum(totalSum);
        data.setDay(day);
        data.setMonth(month);
        data.setYear(year);
        data.setComment(comment);
        data.setTimeStamp(System.currentTimeMillis());
        data.setIsProfit(mIsProfit);

        realmObj.commitTransaction();
    }
}
