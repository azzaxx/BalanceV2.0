package com.example.alex.balance.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.dialogs.DateDialog;
import com.example.alex.balance.presenters.DataPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.alex.balance.presenters.DataPresenter.DEFAULT_VALUE;
import static com.example.alex.balance.views.StartActivity.PROFIT_LOSS_KEY;
import static com.example.alex.balance.views.StartActivity.CATEGORY_POSITION_KEY;

/**
 * Created by alex on 04.01.17.
 */

public class BalanceFragment extends Fragment implements View.OnClickListener{
    public static final int DATE_DIALOG_REQ_CODE = 1001;

    @BindView(R.id.total_sum_tv)
    TextView mTvTotalSum;
    @BindView(R.id.date_day)
    TextView mTvDateDay;
    @BindView(R.id.date_month)
    TextView mTvDateMonth;
    @BindView(R.id.date_year)
    TextView mTvDateYear;
    @BindView(R.id.del_one_button)
    RelativeLayout mRlDelOne;
    @BindView(R.id.et_comments)
    EditText mEtComments;

    private int[] mNumberButtons = {
            R.id.b0,
            R.id.b1,
            R.id.b2,
            R.id.b3,
            R.id.b4,
            R.id.b5,
            R.id.b6,
            R.id.b7,
            R.id.b8,
            R.id.b9,
            R.id.b_clear
    };
    private Unbinder mUnbinder;
    private DataPresenter mPresenter = new DataPresenter();
    private boolean mIsProfit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        mPresenter.bindView(this);
        if (getArguments() != null) {
            mIsProfit = getArguments().getInt(PROFIT_LOSS_KEY) > 0;
        }
        initButtons(view);
        mPresenter.setDate(null);
    }

    private void initButtons(View view) {
        mRlDelOne.setOnClickListener(this);

        view.findViewById(R.id.button_done).setOnClickListener(this);
        view.findViewById(R.id.date_container).setOnClickListener(this);
        for (int i : mNumberButtons) {
            view.findViewById(i).setOnClickListener(this);
        }
    }

    public void setDate(@NonNull String day, @NonNull String month, @NonNull String year) {
        mTvDateDay.setText(day);
        mTvDateMonth.setText(month);
        mTvDateYear.setText(year);
    }

    public void setTotalSum(@NonNull String total) {
        mTvTotalSum.setText(total);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        switch (viewId) {
            case R.id.b_clear:
                setTotalSum(DEFAULT_VALUE);
                break;
            case R.id.del_one_button:
                mPresenter.clearOne(mTvTotalSum.getText().toString());
                break;
            case R.id.button_done:
                mPresenter.addBalanceData(
                        mTvTotalSum.getText().toString(),
                        mTvDateDay.getText().toString(),
                        mTvDateMonth.getText().toString(),
                        mTvDateYear.getText().toString(),
                        mEtComments.getText().toString(),
                        mIsProfit,
                        getAct().getCategoryByPosition(getArguments().getInt(CATEGORY_POSITION_KEY)));
                getAct().popBackStack();
                break;
            case R.id.date_container:
                DateDialog dateDialog = new DateDialog();
                dateDialog.setTargetFragment(this, DATE_DIALOG_REQ_CODE);
                dateDialog.show(getFragmentManager(), DateDialog.class.getName());
                break;
            default:
                for (int i = 0; i < mNumberButtons.length - 1; i++) {
                    if (viewId == mNumberButtons[i]) {
                        mPresenter.addOne(String.valueOf(i), mTvTotalSum.getText().toString());
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.onActivityResult(requestCode, data);
        }
    }

    public StartActivity getAct() {
        return (StartActivity) getActivity();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
