package com.example.alex.balance.presenters;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.alex.balance.BalanceFragment;
import com.example.alex.balance.custom.BalanceData;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import io.realm.Realm;

import static com.example.alex.balance.BalanceFragment.DATE_DIALOG_REQ_CODE;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_DAY_KEY;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_MONTH_KEY;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_YEAR_KEY;

/**
 * Created by alex on 09.01.17.
 */

public class DataPresenter extends BasePresenter<BalanceFragment> {

    public void setDate(@Nullable Intent date) {
        Calendar calendar = Calendar.getInstance();
        int year = date == null ? calendar.get(Calendar.YEAR) : date.getIntExtra(DATE_DIALOG_YEAR_KEY, 0);
        int month = date == null ? calendar.get(Calendar.MONTH) : date.getIntExtra(DATE_DIALOG_MONTH_KEY, 0);
        int day = date == null ? calendar.get(Calendar.DAY_OF_MONTH) : date.getIntExtra(DATE_DIALOG_DAY_KEY, 0);

        mView.setDate(String.valueOf(day), new DateFormatSymbols().getMonths()[month], String.valueOf(year));
    }

    public void totalSumEditor(String text) {
        String result;

        if (text.length() == 1) {
            result = "0";
        } else if (text.charAt(text.length() - 2) == '.') {
            result = text.substring(0, text.length() - 2);
        } else {
            result = text.substring(0, text.length() - 1);
        }

        mView.setTotalSum(result);
    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == DATE_DIALOG_REQ_CODE) {
            setDate(data);
        }
    }

    public void addBalanceData(String totalSum, String day, String month, String year) {
        Realm realmObj = mView.getAct().getRealm();

        realmObj.beginTransaction();

        BalanceData data = realmObj.createObject(BalanceData.class);
        data.setTotalSum(totalSum);
        data.setDay(day);
        data.setMonth(month);
        data.setYear(year);

        realmObj.commitTransaction();
    }
}
