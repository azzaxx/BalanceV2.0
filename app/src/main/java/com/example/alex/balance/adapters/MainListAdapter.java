package com.example.alex.balance.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.interfaces.RecyclerClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by alex on 10.01.17.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainViewHolder> {
    private List<BalanceData> mList;
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
        StringBuilder builder = new StringBuilder();

        addDate(data, holder);
        addComments(data, holder);
        addCategoryList(data, holder);
        setProfitOrLossImage(holder, data.isProfit());

        builder.append(data.isProfit() ? "+" : "-");
        builder.append(String.format("%.2f", data.getTotalSum()));
        holder.tvTotalSum.setText(builder);
    }

    private void setProfitOrLossImage(MainViewHolder holder, boolean isProfit) {
        holder.ivProfitOrLoss.setImageDrawable(
                mContext.getResources().getDrawable(
                        isProfit ? R.drawable.ic_profit_colored : R.drawable.ic_loss_colored));
        holder.rrCircle.setBackgroundDrawable(
                mContext.getResources().getDrawable(
                        isProfit ? R.drawable.circle_profit : R.drawable.circle_loss)
        );
    }

    private void addCategoryList(BalanceData data, MainViewHolder holder) {
        StringBuilder builder = new StringBuilder();
//        List<CategoryData> list = data.getList();

        builder.append(" ");
//        if (!list.isEmpty())
            builder.append(data.getCategory().getName());

//        for (int i = 1; i < list.size(); i++) {
//            builder.append(", ");
//            builder.append(list.get(i).getName());
//        }

        holder.tvCategoryList.setText(builder);
    }

    private void addComments(BalanceData data, MainViewHolder holder) {
        holder.tvComments.setVisibility(data.getComment().isEmpty() ? View.GONE : View.VISIBLE);
        holder.tvComments.setText(data.getComment());
    }

    private void addDate(BalanceData data, MainViewHolder holder) {
        StringBuilder builder = new StringBuilder();
        builder.append(data.getDay()).append(" ");
        builder.append(data.getMonth().substring(0, 3)).append(" ");
        builder.append(data.getYear());
        holder.tvDate.setText(builder);
    }

    public void setList(List<BalanceData> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClick(RecyclerClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    class MainViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.tv_profit_loss)
        TextView tvProfitOrLoss;
        @BindView(R.id.tv_total_sum)
        TextView tvTotalSum;
        @BindView(R.id.tv_comments)
        TextView tvComments;
        @BindView(R.id.tv_year)
        TextView tvDate;
        @BindView(R.id.tv_category_list)
        TextView tvCategoryList;
        @BindView(R.id.recycler_item_image_profit)
        ImageView ivProfitOrLoss;
        @BindView(R.id.recycler_item_rr_circle)
        RelativeLayout rrCircle;

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
