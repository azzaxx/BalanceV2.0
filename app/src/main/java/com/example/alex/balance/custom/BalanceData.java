package com.example.alex.balance.custom;

import io.realm.RealmObject;

/**
 * Created by alex on 09.01.17.
 */

public class BalanceData extends RealmObject {
    private String mTotalSum;
    private String mDay;
    private String mMonth;
    private String mYear;
    private String mComment;

    public String getTotalSum() {
        return mTotalSum;
    }

    public void setTotalSum(String mTotalSum) {
        this.mTotalSum = mTotalSum;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String mDay) {
        this.mDay = mDay;
    }

    public String getMonth() {
        return mMonth;
    }

    public void setMonth(String mMonth) {
        this.mMonth = mMonth;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String mYear) {
        this.mYear = mYear;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }

    @Override
    public String toString() {
        return "Total sum: " + mTotalSum +
                ", day: " + mDay +
                ", month: " + mMonth +
                ", year: " + mYear +
                ", comment: " + mComment;
    }
}
