package com.example.alex.balance.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.balance.R;

/**
 * Created by alex on 18.01.17.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryListHolder> {
    private String[] list;

    public CategoryListAdapter(String[] list) {
        this.list = list;
    }

    @Override
    public CategoryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_balance, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    class CategoryListHolder extends RecyclerView.ViewHolder {
        public CategoryListHolder(View itemView) {
            super(itemView);
        }
    }
}
