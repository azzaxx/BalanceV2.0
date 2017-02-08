package com.example.alex.balance.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.CategoryData;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by alex on 02.02.17.
 */

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.FilterViewHolder> {
    private RealmResults<CategoryData> mList;
    private RealmList<CategoryData> realmList = new RealmList<>();
    private Context mContext;

    public FilterRecyclerAdapter(Context context, RealmResults<CategoryData> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_item_filter, null));
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        holder.colorBox.setBackgroundColor(mList.get(position).getColor());
        holder.categoryNameCheckBox.setText(mList.get(position).getName());
    }

    public RealmList<CategoryData> getList() {
        return this.realmList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder {
        CheckBox categoryNameCheckBox;
        View colorBox;

        FilterViewHolder(View itemView) {
            super(itemView);
            categoryNameCheckBox = (CheckBox) itemView.findViewById(R.id.item_filter_check_box);
            colorBox = itemView.findViewById(R.id.item_filter_color_box);

            categoryNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        realmList.add(mList.get(getAdapterPosition()));
                    else
                        realmList.remove(mList.get(getAdapterPosition()));
                }
            });
        }
    }
}
