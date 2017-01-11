package com.example.alex.balance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.alex.balance.adapters.MainListAdapter;
import com.example.alex.balance.custom.BalanceData;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String PROFIT_LOSE_KEY = "start_activity_profit_or_lose_key";
    @BindView(R.id.button_profit)
    Button btnProfit;
    @BindView(R.id.button_lose)
    Button btnLose;
    @BindView(R.id.recycler_view_list)
    RecyclerView mRVList;
    private Realm mRealm;
    private MainListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.bind(this);
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        btnProfit.setOnClickListener(this);
        btnLose.setOnClickListener(this);

        mRVList.setHasFixedSize(true);
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainListAdapter(mRealm.where(BalanceData.class).findAllSorted("mTimeStamp", Sort.DESCENDING), this);
        mRVList.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();

        switch (view.getId()) {
            case R.id.button_profit:
                args.putInt(PROFIT_LOSE_KEY, 1);
                break;
            case R.id.button_lose:
                args.putInt(PROFIT_LOSE_KEY, -1);
                break;
        }
        BalanceFragment fragment = new BalanceFragment();
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.start_activity_container, fragment, null).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
        mAdapter.notifyDataSetChanged();
    }

    public Realm getRealm() {
        return this.mRealm;
    }
}
