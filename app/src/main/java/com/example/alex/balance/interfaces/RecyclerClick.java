package com.example.alex.balance.interfaces;

import android.view.View;

import com.example.alex.balance.custom.BalanceData;

/**
 * Created by alex on 16.01.17.
 */

public interface RecyclerClick {
    void onRecyclerClick(View view, int position, BalanceData balanceData);
}
