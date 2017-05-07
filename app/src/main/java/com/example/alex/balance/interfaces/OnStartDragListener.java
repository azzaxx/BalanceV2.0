package com.example.alex.balance.interfaces;

import android.support.v7.widget.RecyclerView;

public interface OnStartDragListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);

    void onItemSwipe(int position, int direction);
}
