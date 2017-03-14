package com.example.alex.balance.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alex.balance.R;
import com.example.alex.balance.interfaces.ItemTouchHelperAdapter;
import com.example.alex.balance.interfaces.ItemTouchHelperViewHolder;
import com.example.alex.balance.interfaces.OnStartDragListener;

/**
 * Created by alex on 14.03.17.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private final OnStartDragListener mDragStartListener;

    public CategoryListAdapter(Context context, OnStartDragListener dragStartListener) {
        this.mDragStartListener = dragStartListener;
        this.mContext = context;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_mainv2, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, int position) {
        holder.tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 6;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        Toast.makeText(mContext, "Fffffffff", Toast.LENGTH_SHORT).show();
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        View view;
        TextView tv;

        public CategoryHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            tv = (TextView) itemView.findViewById(R.id.sssssss);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
