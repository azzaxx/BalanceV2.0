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

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by alex on 10.01.17.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainViewHolder> {
    private RealmResults<BalanceData> mList;
    private Context mContext;

    public MainListAdapter(RealmResults<BalanceData> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        BalanceData data = mList.get(position);
        boolean isProfit = Double.valueOf(data.getTotalSum()) > 0;
        int color = mContext.getResources().getColor(isProfit ? R.color.green : R.color.red);

        Drawable drawable = holder.llContainer.getBackground();
        ((GradientDrawable) drawable).setStroke(1, color);
        holder.llContainer.setBackgroundDrawable(drawable);

        StringBuilder builder = new StringBuilder();
        builder.append(data.getDay()).append(" ");
        builder.append(data.getMonth()).append(" ");
        builder.append(data.getYear());
        holder.tvDate.setText(builder);

        if (data.getComment() == null) {
            holder.tvComments.setVisibility(View.GONE);
        } else {
            holder.tvComments.setText(data.getComment());
        }

        holder.tvProfitOrLose.setText(isProfit ? "Profit" : "Lose");
        holder.tvTotalSum.setText(data.getTotalSum());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
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

        public MainViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
