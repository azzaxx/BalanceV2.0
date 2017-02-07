package com.example.alex.balance.custom;

import java.io.Serializable;

import io.realm.RealmList;

/**
 * Created by alex on 07.02.17.
 */

public class FilterSettings implements Serializable {
    public final static boolean DEFAULT_PROFIT = true;
    public final static boolean DEFAULT_LOSS = true;
    public final static boolean DEFAULT_SHOW_ALL = true;
    public final static float DEFAULT_FILTER_VALUE = -1f;

    public boolean isProfit = DEFAULT_PROFIT;
    public boolean isLoss = DEFAULT_LOSS;
    public boolean isShowAll = DEFAULT_SHOW_ALL;
    public float minValue = DEFAULT_FILTER_VALUE;
    public float maxValue = DEFAULT_FILTER_VALUE;
    public RealmList<CategoryData> filterCategoryList = new RealmList<>();
}
