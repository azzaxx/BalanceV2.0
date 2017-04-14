package com.example.alex.balance.views;

import android.app.Activity;
import android.content.Intent;
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
import com.example.alex.balance.presenters.DetailFragmentPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 05.04.17.
 */

public class DetailFragment extends Fragment implements View.OnClickListener {
    private Unbinder mUnbinder;
    @BindView(R.id.detail_rv)
    RecyclerView mDetailRV;
    @BindView(R.id.detail_fragment_cat_color)
    View mViewColor;
    @BindView(R.id.detail_fragment_tv_balance)
    TextView mTvTotal;
    @BindView(R.id.detail_fragment_tv_name)
    TextView mTvCatName;

    private DetailFragmentPresenter mFragmentPresenter = new DetailFragmentPresenter();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mFragmentPresenter.bindView(this);
        mFragmentPresenter.initView();
        view.findViewById(R.id.detail_fragment_edit_cat_rl).setOnClickListener(this);

        mTvTotal.setText(mFragmentPresenter.getTotal());
        mDetailRV.setLayoutManager(new LinearLayoutManager(getContext()));
        DetailRVAdapter adapter = new DetailRVAdapter(getContext(), mFragmentPresenter.getBalanceList());
        mDetailRV.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        mFragmentPresenter.showEditCatDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mFragmentPresenter.onActivityResult(requestCode, data);
        }
    }

    public void setCatNameAndColor(String newName, int newColor) {
        mTvCatName.setText(newName);
        mViewColor.setBackgroundColor(newColor);
    }
}
