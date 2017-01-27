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
import com.example.alex.balance.interfaces.RecyclerClick;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.Sort;

import static com.example.alex.balance.custom.BalanceData.BALANCEDATA_FIELD_TIME;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, RecyclerClick {
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
        mAdapter = new MainListAdapter(mRealm.where(BalanceData.class).findAllSorted(BALANCEDATA_FIELD_TIME, Sort.DESCENDING), this);
        mAdapter.setOnItemClick(this);
        mRVList.setAdapter(mAdapter);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        findViewById(R.id.action_bar_statistic).setOnClickListener(this);
        calculateTotalBalance();
    }

    @Override
    public void onClick(View view) {
        Bundle args = new Bundle();
        Fragment fragment = new BalanceFragment();

        switch (view.getId()) {
            case R.id.button_profit:
                args.putInt(PROFIT_LOSE_KEY, 1);
                break;
            case R.id.button_lose:
                args.putInt(PROFIT_LOSE_KEY, -1);
                break;
            case R.id.action_bar_statistic:
                fragment = new StatisticFragment();
                break;
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
        calculateTotalBalance();
    }

    public Realm getRealm() {
        return this.mRealm;
    }

    @Override
    public void onRecyclerClick(View view, int position, BalanceData balanceData) {
        showDialog(balanceData);
    }

    private void showDialog(final BalanceData balanceData) {
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
        calculateTotalBalance();
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
                        data.removeLose(oldData.getLose());
                }
            }
        }
    }

    private void calculateTotalBalance() {
        float totalBalance = 0f;

        mRealm.beginTransaction();

        for (BalanceData data : mRealm.where(BalanceData.class).findAll()) {
            if (data.isProfit()) {
                totalBalance += Float.parseFloat(data.getTotalSum());
            } else {
                totalBalance -= Float.parseFloat(data.getTotalSum());
            }
        }

        mRealm.commitTransaction();
        setTotalSum(totalBalance);
    }

    private void setTotalSum(final float sum) {
        String text = "Balance: ";
        text += String.format("%.2f", sum);
        ((TextView) findViewById(R.id.action_bar_title)).setText(text);
    }
}
