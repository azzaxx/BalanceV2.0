package com.example.alex.balance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alex.balance.adapters.MainListAdapter;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.FilterSettings;
import com.example.alex.balance.dialogs.FilterDialog;
import com.example.alex.balance.interfaces.RecyclerClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.example.alex.balance.custom.BalanceData.BALANCE_DATA_FIELD_IS_PROFIT;
import static com.example.alex.balance.custom.BalanceData.BALANCE_DATA_FIELD_TIME;
import static com.example.alex.balance.custom.BalanceData.BALANCE_DATA_FIELD_TOTAL_SUM;
import static com.example.alex.balance.custom.FilterSettings.DEFAULT_FILTER_VALUE;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.bind(this);
        Realm.init(this);
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
        calculateTotalBalance(mRealm.where(BalanceData.class).findAll());
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
                showFilterDialog();
                return;
        }

        fragment.setArguments(args);
        showFragment(fragment);
    }

    private void showFilterDialog() {
        FilterDialog.newInstance(mFilterSettings).show(getSupportFragmentManager(), FilterDialog.class.toString());
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
        calculateTotalBalance(mRealm.where(BalanceData.class).findAll());
        actionButtonsVisibility(true);
    }

    public Realm getRealm() {
        return this.mRealm;
    }

    @Override
    public void onRecyclerClick(View view, int position, BalanceData balanceData) {
        showDeleteMessageDialog(balanceData);
    }

    private void showDeleteMessageDialog(final BalanceData balanceData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE)
                    removeItemFromList(balanceData);
                dialog.dismiss();
            }
        };
        builder.setMessage("Are you sure?");
        builder.setPositiveButton("Yes", onClickListener);
        builder.setNegativeButton("No", onClickListener);
        builder.create().show();
    }

    private void removeItemFromList(BalanceData balanceData) {
        mRealm.beginTransaction();
        removeDataFromCategory(balanceData);
        balanceData.deleteFromRealm();
        mAdapter.notifyDataSetChanged();
        mRealm.commitTransaction();
        calculateTotalBalance(mRealm.where(BalanceData.class).findAll());
    }

    private void removeDataFromCategory(BalanceData balanceData) {
        if (balanceData.getList().isEmpty())
            return;

        for (CategoryData data : mRealm.where(CategoryData.class).findAll()) {
            for (CategoryData oldData : balanceData.getList()) {
                if (data.getName().equals(oldData.getName())
                        && data.getColor() == oldData.getColor()
                        && data.getTimeStamp() == oldData.getTimeStamp()) {
                    if (balanceData.isProfit())
                        data.removeProfit(oldData.getProfit());
                    else
                        data.removeLoss(oldData.getLoss());
                }
            }
        }
    }

    private void calculateTotalBalance(RealmResults<BalanceData> results) {
        float totalBalance = 0f;
        mRealm.beginTransaction();

        for (BalanceData data : results) {
            if (data.isProfit()) {
                totalBalance += data.getTotalSum();
            } else {
                totalBalance -= data.getTotalSum();
            }
        }

        mRealm.commitTransaction();
        setTotalSum(totalBalance);
    }

    private void setTotalSum(final float sum) {
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
        enableFilter();
    }

    private void enableFilter() {
        RealmQuery<BalanceData> query = mRealm.where(BalanceData.class);

        if (!mFilterSettings.isProfit) {
            query.equalTo(BALANCE_DATA_FIELD_IS_PROFIT, false);
        }
        if (!mFilterSettings.isLoss) {
            query.equalTo(BALANCE_DATA_FIELD_IS_PROFIT, true);
        }
        if (mFilterSettings.minValue != DEFAULT_FILTER_VALUE) {
            query.greaterThan(BALANCE_DATA_FIELD_TOTAL_SUM, mFilterSettings.minValue);
        }
        if (mFilterSettings.maxValue != DEFAULT_FILTER_VALUE) {
            query.lessThan(BALANCE_DATA_FIELD_TOTAL_SUM, mFilterSettings.maxValue);
        }

        RealmResults<BalanceData> result = query.findAllSorted(BALANCE_DATA_FIELD_TIME, Sort.DESCENDING);
        calculateTotalBalance(result);
        mAdapter.setList(result);
    }
}
