package com.example.alex.balance.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.interfaces.ItemTouchHelperAdapter;
import com.example.alex.balance.interfaces.ItemTouchHelperViewHolder;
import com.example.alex.balance.interfaces.OnStartDragListener;
import com.example.alex.balance.views.DetailFragment;
import com.example.alex.balance.views.StartActivityv2;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 14.03.17.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private final OnStartDragListener mDragStartListener;
    private List<CategoryData> list;
    private View.OnClickListener clickListener;

    public CategoryListAdapter(List<CategoryData> list, Context context, OnStartDragListener dragStartListener, View.OnClickListener clickListener) {
        this.mDragStartListener = dragStartListener;
        this.mContext = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_mainv2, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, int position) {
        final CategoryData data = list.get(position);
        holder.mCategoryColorView.setBackgroundColor(data.getColor());
        holder.textName.setText(data.getName());
        holder.textBalance.setText(String.format("%.2f", (data.getProfit() - data.getLoss())));
        holder.mTvCategoryDate.setText(data.getLastDate());
        holder.mRVMore.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        mDragStartListener.onItemSwipe(position, direction);
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.category_list_tv_name)
        TextView textName;
        @BindView(R.id.category_list_tv_balance)
        TextView textBalance;
        @BindView(R.id.category_list_cat_color)
        View mCategoryColorView;
        @BindView(R.id.category_list_tv_date)
        TextView mTvCategoryDate;
        @BindView(R.id.keyboard_expand)
        ExpandableLayout mExpand;
        @BindView(R.id.category_list_rv_show_more)
        RelativeLayout mRVMore;
        private int color;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            this.color = ((ColorDrawable) itemView.getBackground()).getColor();
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(color);
        }
    }
}
