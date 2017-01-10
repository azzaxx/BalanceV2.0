package com.example.alex.balance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.dialogs.DateDialog;
import com.example.alex.balance.presenters.DataPresenter;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 04.01.17.
 */

public class BalanceFragment extends Fragment implements View.OnClickListener {
    public static final int DATE_DIALOG_REQ_CODE = 1001;

    @BindView(R.id.total_sum_tv)
    TextView mTvTotalSum;
    @BindView(R.id.date_day)
    TextView mTvDateDay;
    @BindView(R.id.date_month)
    TextView mTvDateMonth;
    @BindView(R.id.date_year)
    TextView mTvDateYear;
    @BindView(R.id.keyboard_expand)
    ExpandableLayout mKeyboardExpand;
    @BindView(R.id.edit_expand)
    ExpandableLayout mEditExpand;
    @BindView(R.id.image_keyboard_button)
    ImageView mIvKeyboard;
    @BindView(R.id.image_notes_button)
    ImageView mIvEdit;
    @BindView(R.id.keyboard_button)
    RelativeLayout mRlKeyboard;
    @BindView(R.id.notes_button)
    RelativeLayout mRlEdit;
    @BindView(R.id.del_one_button)
    RelativeLayout mRlDelOne;
    @BindView(R.id.et_comments)
    EditText mEtComments;

    private int[] mSwitchButtons = {
            R.id.category_button,
            R.id.keyboard_button,
            R.id.notes_button
    };
    private Unbinder mUnbinder;
    private DataPresenter mPresenter = new DataPresenter();

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

        mRlDelOne.setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);
        view.findViewById(R.id.button_done).setOnClickListener(this);
        view.findViewById(R.id.date_container).setOnClickListener(this);
        for (int i : mSwitchButtons) {
            view.findViewById(i).setOnClickListener(this);
        }
        mPresenter.setDate(null);
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
        switch (view.getId()) {
            case R.id.del_one_button:
                mPresenter.totalSumEditor(mTvTotalSum.getText().toString());
                break;
            case R.id.category_button:
                break;
            case R.id.keyboard_button:
                if (!mKeyboardExpand.isExpanded()) {
                    expand(mKeyboardExpand);
                    activateButton(mIvKeyboard, mRlKeyboard, getActivity().getResources().getDrawable(R.drawable.ic_keypad));
                }
                break;
            case R.id.notes_button:
                if (!mEditExpand.isExpanded()) {
                    expand(mEditExpand);
                    activateButton(mIvEdit, mRlEdit, getActivity().getResources().getDrawable(R.drawable.ic_underline_button));
                }
                break;
            case R.id.button_cancel:
                getActivity().onBackPressed();
                break;
            case R.id.button_done:
                mPresenter.addBalanceData(
                        mTvTotalSum.getText().toString(),
                        mTvDateDay.getText().toString(),
                        mTvDateMonth.getText().toString(),
                        mTvDateYear.getText().toString(),
                        mEtComments.getText().toString());
                getAct().getSupportFragmentManager().popBackStack();
                break;
            case R.id.date_container:
                DateDialog dateDialog = new DateDialog();
                dateDialog.setTargetFragment(this, DATE_DIALOG_REQ_CODE);
                dateDialog.show(getFragmentManager(), null);
                break;
        }
    }

    private void activateButton(ImageView iv, RelativeLayout relativeLayout, Drawable image) {
        mIvKeyboard.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_keypad_primary));
        mIvEdit.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_underline_button_primary));

        mRlKeyboard.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.round));
        mRlEdit.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.round));

        iv.setImageDrawable(image);
        relativeLayout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.round_white));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            mPresenter.onActivityResult(requestCode, data);
        }
    }

    private void expand(ExpandableLayout expand) {
        mKeyboardExpand.collapse();
        mEditExpand.collapse();
        expand.expand();
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
