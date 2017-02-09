package com.example.alex.balance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.balance.adapters.MainListAdapter;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.FilterSettings;
import com.example.alex.balance.interfaces.RecyclerClick;
import com.example.alex.balance.presenters.StartActivityPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.Sort;

import static com.example.alex.balance.custom.BalanceData.BALANCE_DATA_FIELD_TIME;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, RecyclerClick {
    public static final String PROFIT_LOSS_KEY = "start_activity_profit_or_loss_key";
    @BindView(R.id.button_profit)
    Button btnProfit;
    @BindView(R.id.button_loss)
    Button btnLoss;
    @BindView(R.id.recycler_view_list)
    RecyclerView mRVList;
    private Realm mRealm;
    private MainListAdapter mAdapter;
    private FilterSettings mFilterSettings = new FilterSettings();
    private StartActivityPresenter mPresenter = new StartActivityPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.bind(this);
        Realm.init(this);
        mPresenter.bindView(this);
        mRealm = Realm.getDefaultInstance();

        btnProfit.setOnClickListener(this);
        btnLoss.setOnClickListener(this);

        mRVList.setHasFixedSize(true);
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MainListAdapter(mRealm.where(BalanceData.class).findAllSorted(BALANCE_DATA_FIELD_TIME, Sort.DESCENDING), this);
        mAdapter.setOnItemClick(this);
        mRVList.setAdapter(mAdapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        findViewById(R.id.action_bar_statistic).setOnClickListener(this);
        findViewById(R.id.action_bar_filter).setOnClickListener(this);
        mPresenter.calculateTotalBalance(mRealm.where(BalanceData.class).findAll());
    }

    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        Fragment fragment = new BalanceFragment();

        switch (view.getId()) {
            case R.id.button_profit:
                args.putInt(PROFIT_LOSS_KEY, 1);
                break;
            case R.id.button_loss:
                args.putInt(PROFIT_LOSS_KEY, -1);
                break;
            case R.id.action_bar_statistic:
                fragment = new StatisticFragment();
                break;
            case R.id.action_bar_filter:
                mPresenter.showFilterDialog(mFilterSettings);
                return;
        }

        fragment.setArguments(args);
        showFragment(fragment);
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.start_activity_container, fragment, null).addToBackStack(null).commit();
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
        mPresenter.calculateTotalBalance(mRealm.where(BalanceData.class).findAll());
        actionButtonsVisibility(true);
    }

    public Realm getRealm() {
        return this.mRealm;
    }

    public MainListAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override
    public void onRecyclerClick(View view, int position, BalanceData balanceData) {
        mPresenter.showDeleteMessageDialog(balanceData);
    }

    public void setTotalSum(final float sum) {
        String text = "Balance: ";
        text += String.format("%.2f", sum);
        ((TextView) findViewById(R.id.action_bar_title)).setText(text);
        welcomeTextVisibility();
    }

    public void actionButtonsVisibility(boolean isVisible) {
        findViewById(R.id.action_bar_buttons_container).setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void welcomeTextVisibility() {
        findViewById(R.id.welcome_text).setVisibility(mRealm.where(BalanceData.class).findAll().isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void setFilterSettings(FilterSettings settings) {
        this.mFilterSettings = settings;
        mAdapter.setList(mPresenter.enableFilter(mFilterSettings));
    }
}
