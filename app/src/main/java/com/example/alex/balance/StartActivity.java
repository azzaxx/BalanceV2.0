package com.example.alex.balance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alex.balance.custom.BalanceData;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.button_profit)
    Button btnProfit;
    @BindView(R.id.button_lose)
    Button btnLose;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.bind(this);
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        btnProfit.setOnClickListener(this);
        btnLose.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RealmResults<BalanceData> list = mRealm.where(BalanceData.class).findAll();
    }

    @Override
    public void onClick(View view) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.start_activity_container, new BalanceFragment(), null).addToBackStack(null).commit();
        switch (view.getId()) {
            case R.id.button_profit:
                break;
            case R.id.button_lose:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public Realm getRealm() {
        return this.mRealm;
    }
}
