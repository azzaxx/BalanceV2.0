package com.example.alex.balance.custom;

import android.graphics.Color;

import io.realm.RealmObject;

/**
 * Created by alex on 19.01.17.
 */

public class CategoryData extends RealmObject {
    public static final String CATEGORY_FIELD_NAME = "mName";
    public static final String CATEGORY_FIELD_COLOR = "mColor";
    public static final String CATEGORY_FIELD_TIME = "mTimeStamp";
    public static final String OTHER_CATEGORY_NAME = "Other";
    public static final int OTHER_CATEGORY_COLOR = Color.parseColor("#876ED7");

    private String mName;
    private int mColor;
    private long mTimeStamp;
    private float mProfit;
    private float mLoss;

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public float getProfit() {
        return mProfit;
    }

    public void setProfit(float mProfit) {
        this.mProfit = mProfit;
    }

    public float getLoss() {
        return mLoss;
    }

    public void setLoss(float mLoss) {
        this.mLoss = mLoss;
    }

    public void addLoss(float Loss) {
        this.mLoss += Loss;
    }

    public void addProfit(float profit) {
        this.mProfit += profit;
    }

    public void removeLoss(float Loss) {
        this.mLoss -= Loss;
    }

    public void removeProfit(float profit) {
        this.mProfit -= profit;
    }

    public void addLoss(String Loss) {
        addLoss(Float.parseFloat(Loss));
    }

    public void addProfit(String profit) {
        addProfit(Float.parseFloat(profit));
    }

    public void addProfOrLoss(String value, boolean isProfit) {
        if (isProfit)
            addProfit(value);
        else
            addLoss(value);
    }

    public void removeLoss(String Loss) {
        removeLoss(Float.parseFloat(Loss));
    }

    public void removeProfit(String profit) {
        removeProfit(Float.parseFloat(profit));
    }
}
