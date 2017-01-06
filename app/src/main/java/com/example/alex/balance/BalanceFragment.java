package com.example.alex.balance;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by alex on 04.01.17.
 */

public class BalanceFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.total_sum_tv)
    TextView mTvTotalSum;
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

    private int[] mSwitchButtons = {
            R.id.category_button,
            R.id.keyboard_button,
            R.id.notes_button
    };
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);

        mRlDelOne.setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);
        for (int i : mSwitchButtons) {
            view.findViewById(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.del_one_button:
                String totalSumTxt = mTvTotalSum.getText().toString();
                if (totalSumTxt.length() == 1) {
                    mTvTotalSum.setText("0");
                } else if (totalSumTxt.charAt(totalSumTxt.length() - 2) == '.') {
                    mTvTotalSum.setText(totalSumTxt.substring(0, totalSumTxt.length() - 2));
                } else {
                    mTvTotalSum.setText(totalSumTxt.substring(0, totalSumTxt.length() - 1));
                }
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

    private void expand(ExpandableLayout expand) {
        mKeyboardExpand.collapse();
        mEditExpand.collapse();
        expand.expand();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
