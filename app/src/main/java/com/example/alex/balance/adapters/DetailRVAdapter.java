package com.example.alex.balance.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 05.04.17.
 */

public class DetailRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BalanceData> list;
    private static final int HEADER = 0;
    private static final int BODY = 1;
    private Context mContext;

    public DetailRVAdapter(Context context, List<BalanceData> old) {
        this.mContext = context;
        List<BalanceData> array = new ArrayList<>();

        old = sortList(old);
        if (!old.isEmpty())
            array.add(old.get(0));

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
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            headerViewHolder.mTvHeaderDate.setText(balanceData.getDay() + " " + balanceData.getMonth() + " " + balanceData.getYear());
            headerViewHolder.mTvHeaderTotal.setText(calculateTotalByDay(balanceData.getDay(), position));
        } else if (holder instanceof BodyViewHolder) {
            BodyViewHolder bodyViewHolder = (BodyViewHolder) holder;

            if (!balanceData.getComment().isEmpty()) {
                bodyViewHolder.mTvComment.setText(balanceData.getComment());
            }
            String totalSum = String.format(Locale.US, "%.2f", balanceData.getTotalSum());
            bodyViewHolder.mTvTotal.setText(balanceData.isProfit() ? "+" + totalSum : "-" + totalSum);
            bodyViewHolder.mIvProfit.setImageDrawable(
                    mContext.getResources().getDrawable(balanceData.isProfit() ? R.drawable.ic_profit_colored : R.drawable.ic_loss_colored));
            bodyViewHolder.mUnderline.setVisibility(getItemViewType(position + 1) == BODY ? View.VISIBLE : View.GONE);
        }
    }

    private String calculateTotalByDay(String day, int pos) {
        float totalSum = 0;

        for (int i = 0; i < getItemCount(); i++) {
            final BalanceData data = list.get(i);
            if (i != pos && data.getDay().equals(day)) {
                if (data.isProfit()) {
                    totalSum += data.getTotalSum();
                } else {
                    totalSum -= data.getTotalSum();
                }
            }
        }

        final String total = String.format(Locale.US, "%.2f", totalSum);

        return totalSum > 0 ? "+" + total : total;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || getItemCount() > position && !list.get(position).getDay().equals(list.get(position - 1).getDay())) {
            return HEADER;
        }
        return BODY;
    }

    private List<BalanceData> sortList(List<BalanceData> old) {
        Collections.sort(old, new Comparator<BalanceData>() {
            @Override
            public int compare(BalanceData old1, BalanceData old2) {
                try {
                    return new SimpleDateFormat("yyyy/MMM/dd", Locale.ENGLISH).parse(old1.getYear() + "/" + old1.getMonth() + "/" + old1.getDay())
                            .compareTo(new SimpleDateFormat("yyyy/MMM/dd", Locale.ENGLISH).parse(old2.getYear() + "/" + old2.getMonth() + "/" + old2.getDay()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        Collections.reverse(old);
        return old;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_view_header_date_tv)
        TextView mTvHeaderDate;
        @BindView(R.id.detail_view_header_total_tv)
        TextView mTvHeaderTotal;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BodyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.detail_view_body_iv_profit)
        ImageView mIvProfit;
        @BindView(R.id.detail_view_body_tv_total)
        TextView mTvTotal;
        @BindView(R.id.detail_view_body_tv_comments)
        TextView mTvComment;
        @BindView(R.id.detail_view_body_view_line)
        View mUnderline;

        BodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
