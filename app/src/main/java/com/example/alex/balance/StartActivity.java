package com.example.alex.balance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.button_profit)
    Button btnProfit;
    @BindView(R.id.button_lose)
    Button btnLose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.bind(this);

        btnProfit.setOnClickListener(this);
        btnLose.setOnClickListener(this);
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
}
