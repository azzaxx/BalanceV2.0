package com.example.alex.balance.interfaces;

/**
 * Created by alex on 14.03.17.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
