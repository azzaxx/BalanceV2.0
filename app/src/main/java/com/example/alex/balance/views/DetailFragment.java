package com.example.alex.balance.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.balance.R;
import com.example.alex.balance.adapters.DetailRVAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 05.04.17.
 */

public class DetailFragment extends Fragment {
    private Unbinder mUnbinder;
    @BindView(R.id.detail_rv)
    RecyclerView mDetailRV;
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

        mDetailRV.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DetailRVAdapter(getContext());
        mDetailRV.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mUnbinder.unbind();
    }
}
