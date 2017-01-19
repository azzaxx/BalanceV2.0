package com.example.alex.balance.custom;

import io.realm.RealmObject;

/**
 * Created by alex on 19.01.17.
 */

public class CategoryData extends RealmObject {
    private String mName;
    private int mColor;
    private long mTimeStamp;

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
}
