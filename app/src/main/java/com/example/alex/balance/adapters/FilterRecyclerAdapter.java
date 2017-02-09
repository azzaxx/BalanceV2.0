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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by alex on 02.02.17.
 */

public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.FilterViewHolder> {
    private RealmResults<CategoryData> mList;
    private List<CategoryData> realmList = new RealmList<>();
    private Context mContext;

    public FilterRecyclerAdapter(Context context, RealmResults<CategoryData> resultList) {
        this.mContext = context;
        this.mList = resultList;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FilterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_item_filter, null));
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {
        final CategoryData categoryData = mList.get(position);

        holder.colorBox.setBackgroundColor(categoryData.getColor());
        holder.categoryNameCheckBox.setText(categoryData.getName());

        for (CategoryData data : realmList) {
            if (categoryData.getName().equals(data.getName()) &&
                    categoryData.getColor() == data.getColor() &&
                    categoryData.getTimeStamp() == data.getTimeStamp()) {
                holder.categoryNameCheckBox.setChecked(true);
                break;
            }
        }
    }

    public List<CategoryData> getList() {
        return this.realmList;
    }

    public void setRealmList(List<CategoryData> selectedList) {
        this.realmList = selectedList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class FilterViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.item_filter_check_box)
        CheckBox categoryNameCheckBox;
        @BindView(R.id.item_filter_color_box)
        View colorBox;

        FilterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            categoryNameCheckBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (!realmList.contains(mList.get(getAdapterPosition()))) {
                    realmList.add(mList.get(getAdapterPosition()));
                }
            } else {
                realmList.remove(mList.get(getAdapterPosition()));
            }
        }
    }
}
