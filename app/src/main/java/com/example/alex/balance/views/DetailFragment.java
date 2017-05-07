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
import com.example.alex.balance.dagger.components.DaggerDetailFragmentComponent;
import com.example.alex.balance.dagger.modules.DetailFragmentModule;
import com.example.alex.balance.dagger.presenters.DetailFragmentPresenter;
import com.example.alex.balance.dialogs.CreateCategoryDialog;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment implements View.OnClickListener {
    public static final int EDIT_CATEGORY_KEY = 1000;
    @BindView(R.id.detail_rv)
    RecyclerView mDetailRV;
    @BindView(R.id.detail_fragment_cat_color)
    View mViewColor;
    @BindView(R.id.detail_fragment_tv_balance)
    TextView mTvTotal;
    @BindView(R.id.detail_fragment_tv_name)
    TextView mTvCatName;

    @Inject
    DetailFragmentPresenter mFragmentPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        DaggerDetailFragmentComponent.builder().detailFragmentModule(new DetailFragmentModule(this)).build().inject(this);
        mFragmentPresenter.initView(getArguments());
        view.findViewById(R.id.detail_fragment_edit_cat_rl).setOnClickListener(this);
        mTvTotal.setText(String.format(Locale.US, "%.2f", mFragmentPresenter.getTotal()));
        mDetailRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mDetailRV.setAdapter(new DetailRVAdapter(getContext(), mFragmentPresenter.getBalanceList()));
    }

    @Override
    public void onClick(View v) {
        mFragmentPresenter.showEditCatDialog();
    }

    public void showCreateCategoryDialog(String name, int color) {
        CreateCategoryDialog dialog = CreateCategoryDialog.newInstance(name, color);
        dialog.setTargetFragment(this, EDIT_CATEGORY_KEY);
        dialog.show(getFragmentManager(), CreateCategoryDialog.class.getName());
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
