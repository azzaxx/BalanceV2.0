package com.example.alex.balance.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.interfaces.RecyclerClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by alex on 10.01.17.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainViewHolder> {
    private RealmResults<BalanceData> mList;
    private Context mContext;
    private RecyclerClick mOnItemClick;

    public MainListAdapter(RealmResults<BalanceData> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        BalanceData data = mList.get(position);

        setBackgroundColor(data, holder);
        addDate(data, holder);
        addComments(data, holder);
        holder.tvProfitOrLose.setText(data.isProfit() ? mContext.getString(R.string.profit) : mContext.getString(R.string.lose));
        holder.tvTotalSum.setText(data.getTotalSum());
    }

    private void setBackgroundColor(BalanceData data, MainViewHolder holder) {
        Drawable drawable = holder.llContainer.getBackground();
        ((GradientDrawable) drawable).setStroke(1, mContext.getResources().getColor(data.isProfit() ? R.color.green : R.color.red));
        holder.llContainer.setBackgroundDrawable(drawable);
    }

    private void addComments(BalanceData data, MainViewHolder holder) {
        holder.tvComments.setVisibility(data.getComment().isEmpty() ? View.GONE : View.VISIBLE);
        holder.tvComments.setText(data.getComment());
    }

    private void addDate(BalanceData data, MainViewHolder holder) {
        StringBuilder builder = new StringBuilder();
        builder.append(data.getDay()).append(" ");
        builder.append(data.getMonth()).append(" ");
        builder.append(data.getYear());
        holder.tvDate.setText(builder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClick(RecyclerClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.tv_profit_lose)
        TextView tvProfitOrLose;
        @BindView(R.id.tv_total_sum)
        TextView tvTotalSum;
        @BindView(R.id.tv_comments)
        TextView tvComments;
        @BindView(R.id.tv_year)
        TextView tvDate;
        @BindView(R.id.ll_sum_text_container)
        LinearLayout llContainer;

        MainViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public boolean onLongClick(View v) {
            mOnItemClick.onRecyclerClick(v, getAdapterPosition(), mList.get(getAdapterPosition()));
            return false;
        }
    }
}
