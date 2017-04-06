package com.example.alex.balance.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.realm.RealmHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 05.04.17.
 */

public class DetailRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BalanceData> list;
    public static final int HEADER = 0;
    public static final int BODY = 1;
    private Context mContext;

    public DetailRVAdapter(Context context) {
        this.mContext = context;
        List<BalanceData> array = new ArrayList<>();
        List<BalanceData> old = RealmHelper.getInstance().getBalanceList(RealmHelper.getInstance().getCategorySorted().get(0));

        for (int i = 0; i < old.size(); i++) {
            array.add(old.get(i));

            if (i != 0 && !old.get(i - 1).getDay().equals(old.get(i).getDay())) {
                array.add(old.get(i));
            }
        }

        this.list = array;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_view_header, parent, false));
        }
        return new BodyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_view_body, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final BalanceData balanceData = list.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).mTvHeader.setText(balanceData.getDay() + " " + balanceData.getMonth() + " " + balanceData.getYear());
        } else if (holder instanceof BodyViewHolder) {
            BodyViewHolder body = (BodyViewHolder) holder;

            if (balanceData.getComment().isEmpty()) {
                body.mTvComment.setVisibility(View.GONE);
            } else {
                body.mTvComment.setText(balanceData.getComment());
            }

            String totalSum = String.format("%.2f", balanceData.getTotalSum());
            body.mTvTotal.setText(balanceData.isProfit() ? totalSum : "-" + totalSum);
            body.mIvProfit.setImageDrawable(
                    mContext.getResources().getDrawable(
                            balanceData.isProfit() ? R.drawable.ic_profit_colored : R.drawable.ic_loss_colored));

            body.mUnderline.setVisibility(getItemViewType(position + 1) == BODY ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else if (getItemCount() > position && !list.get(position).getDay().equals(list.get(position - 1).getDay())) {
            return HEADER;
        }
        return BODY;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_view_header_tv)
        TextView mTvHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_view_body_rl_delete)
        RelativeLayout mRlDelete;
        @BindView(R.id.detail_view_body_rl_edit)
        RelativeLayout mRlEdit;
        @BindView(R.id.detail_view_body_iv_profit)
        ImageView mIvProfit;
        @BindView(R.id.detail_view_body_tv_total)
        TextView mTvTotal;
        @BindView(R.id.detail_view_body_tv_comments)
        TextView mTvComment;
        @BindView(R.id.detail_view_body_view_line)
        View mUnderline;

        public BodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
