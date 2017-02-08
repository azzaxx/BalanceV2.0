package com.example.alex.balance.custom;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by alex on 09.01.17.
 */

public class BalanceData extends RealmObject {
    public static final String BALANCE_DATA_FIELD_TIME = "mTimeStamp";
    public static final String BALANCE_DATA_FIELD_IS_PROFIT = "mIsProfit";
    public static final String BALANCE_DATA_FIELD_TOTAL_SUM = "mTotalSum";
    private float mTotalSum;
    private String mDay;
    private String mMonth;
    private String mYear;
    private String mComment;
    private long mTimeStamp;
    private boolean mIsProfit;
    private RealmList<CategoryData> mList;

    public float getTotalSum() {
        return mTotalSum;
    }

    public void setTotalSum(float mTotalSum) {
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

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public boolean isProfit() {
        return mIsProfit;
    }

    public void setIsProfit(boolean mIsProfit) {
        this.mIsProfit = mIsProfit;
    }

    public List<CategoryData> getList() {
        return mList;
    }

    public void setList(RealmList<CategoryData> mList) {
        this.mList = mList;
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
