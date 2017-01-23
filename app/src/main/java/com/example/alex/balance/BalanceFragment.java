package com.example.alex.balance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.dialogs.CreateCategoryDialog;
import com.example.alex.balance.dialogs.DateDialog;
import com.example.alex.balance.presenters.DataPresenter;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.example.alex.balance.StartActivity.PROFIT_LOSE_KEY;
import static com.example.alex.balance.presenters.DataPresenter.DEFAULT_VALUE;

/**
 * Created by alex on 04.01.17.
 */

public class BalanceFragment extends Fragment implements View.OnClickListener {
    public static final int DATE_DIALOG_REQ_CODE = 1001;
    public static final int CREATE_CATEGORY_DIALOG_REQ_CODE = 1002;

    @BindView(R.id.total_sum_tv)
    TextView mTvTotalSum;
    @BindView(R.id.date_day)
    TextView mTvDateDay;
    @BindView(R.id.date_month)
    TextView mTvDateMonth;
    @BindView(R.id.date_year)
    TextView mTvDateYear;
    @BindView(R.id.add_new_category)
    TextView mAddNewCategory;
    @BindView(R.id.keyboard_expand)
    ExpandableLayout mKeyboardExpand;
    @BindView(R.id.edit_expand)
    ExpandableLayout mEditExpand;
    @BindView(R.id.category_expand)
    ExpandableLayout mCategoryExpand;
    @BindView(R.id.image_keyboard_button)
    ImageView mIvKeyboard;
    @BindView(R.id.image_notes_button)
    ImageView mIvEdit;
    @BindView(R.id.image_category_button)
    ImageView mIvCategory;
    @BindView(R.id.keyboard_button)
    RelativeLayout mRlKeyboard;
    @BindView(R.id.notes_button)
    RelativeLayout mRlEdit;
    @BindView(R.id.category_button)
    RelativeLayout mRlCategory;
    @BindView(R.id.del_one_button)
    RelativeLayout mRlDelOne;
    @BindView(R.id.et_comments)
    EditText mEtComments;
    @BindView(R.id.ll_category)
    LinearLayout mLlCategory;

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
            mIsProfit = getArguments().getInt(PROFIT_LOSE_KEY) > 0;
        }

        mRlDelOne.setOnClickListener(this);
        view.findViewById(R.id.button_cancel).setOnClickListener(this);
        view.findViewById(R.id.button_done).setOnClickListener(this);
        view.findViewById(R.id.date_container).setOnClickListener(this);
        mRlKeyboard.setOnClickListener(this);
        mRlEdit.setOnClickListener(this);
        mRlCategory.setOnClickListener(this);
        mAddNewCategory.setOnClickListener(this);

        for (int i : mNumberButtons) {
            view.findViewById(i).setOnClickListener(this);
        }

        mPresenter.setDate(null);
        mPresenter.reAddAllCategory();
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
        Drawable resource = getActivity().getResources().getDrawable(viewId == R.id.category_button
                ? R.drawable.ic_tag : viewId == R.id.keyboard_button
                ? R.drawable.ic_keypad : R.drawable.ic_underline_button);

        switch (viewId) {
            case R.id.b_clear:
                setTotalSum(DEFAULT_VALUE);
                break;
            case R.id.del_one_button:
                mPresenter.clearOne(mTvTotalSum.getText().toString());
                break;
            case R.id.category_button:
                if (!mCategoryExpand.isExpanded()) {
                    expand(mCategoryExpand);
                    activateButton(mIvCategory, mRlCategory, resource);
                }
                break;
            case R.id.keyboard_button:
                if (!mKeyboardExpand.isExpanded()) {
                    expand(mKeyboardExpand);
                    activateButton(mIvKeyboard, mRlKeyboard, resource);
                }
                break;
            case R.id.notes_button:
                if (!mEditExpand.isExpanded()) {
                    expand(mEditExpand);
                    activateButton(mIvEdit, mRlEdit, resource);
                }
                break;
            case R.id.button_cancel:
                getActivity().onBackPressed();
                break;
            case R.id.button_done:
//                ((CheckBox)mLlCategory.getChildAt(8).findViewById(R.id.item_balance_check_box)).isChecked()
                mPresenter.addBalanceData(
                        mTvTotalSum.getText().toString(),
                        mTvDateDay.getText().toString(),
                        mTvDateMonth.getText().toString(),
                        mTvDateYear.getText().toString(),
                        mEtComments.getText().toString(),
                        mIsProfit);
                getAct().popBackStack();
                break;
            case R.id.date_container:
                DateDialog dateDialog = new DateDialog();
                dateDialog.setTargetFragment(this, DATE_DIALOG_REQ_CODE);
                dateDialog.show(getFragmentManager(), DateDialog.class.getName());
                break;
            case R.id.add_new_category:
                CreateCategoryDialog dialog = CreateCategoryDialog.newInstance();
                dialog.setTargetFragment(this, CREATE_CATEGORY_DIALOG_REQ_CODE);
                dialog.show(getFragmentManager(), CreateCategoryDialog.class.getName());
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

    private void activateButton(ImageView iv, RelativeLayout relativeLayout, Drawable image) {
        mIvKeyboard.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_keypad_primary));
        mIvEdit.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_underline_button_primary));
        mIvCategory.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_tag_primary));

        mRlKeyboard.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.round));
        mRlEdit.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.round));
        mRlCategory.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.round));

        iv.setImageDrawable(image);
        relativeLayout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.round_white));
    }

    public void addViewCategory(View view) {
        mLlCategory.addView(view);
    }

    public void removeAllCategory() {
        mLlCategory.removeAllViews();
    }

    public void removeViewCategory(View view) {
        mLlCategory.removeView(view);
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
        mCategoryExpand.collapse();
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
