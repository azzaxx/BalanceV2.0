package com.example.alex.balance.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.adapters.DetailRVAdapter;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.realm.RealmHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 05.04.17.
 */

public class DetailFragment extends Fragment {
    public static final String CATEGORY_POSITION_KEY = "detail_fragment_category_position_key";
    private Unbinder mUnbinder;
    @BindView(R.id.detail_rv)
    RecyclerView mDetailRV;
    @BindView(R.id.detail_fragment_cat_color)
    View mViewColor;
    @BindView(R.id.detail_fragment_tv_balance)
    TextView mTvTotal;
    @BindView(R.id.detail_fragment_tv_name)
    TextView mTvCatName;
    private DetailRVAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        Bundle args = getArguments();
        CategoryData categoryData = RealmHelper.getInstance().getCategorySorted().get(args == null ? 0 : args.getInt(CATEGORY_POSITION_KEY));

        mViewColor.setBackgroundColor(categoryData.getColor());
        mTvTotal.setText(String.format("%.2f", (categoryData.getProfit() - categoryData.getLoss())));
        mTvCatName.setText(categoryData.getName());

        mDetailRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DetailRVAdapter(getContext(), RealmHelper.getInstance().getBalanceList(categoryData));
        mDetailRV.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }
}
