package com.example.alex.balance.interfaces;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemSwipe(int position, int i);
}
