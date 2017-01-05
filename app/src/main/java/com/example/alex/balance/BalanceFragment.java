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

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 * Created by alex on 04.01.17.
 */

public class BalanceFragment extends Fragment implements View.OnClickListener {
    private ExpandableLayout mKeyboardExpand, mEditExpand;
    private ImageView mIvKeyboard, mIvEdit;
    private RelativeLayout mRlKeyboard, mRlEdit;
    private int[] mSwitchButtons = {
            R.id.category_button,
            R.id.keyboard_button,
            R.id.notes_button
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.balance_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mKeyboardExpand = (ExpandableLayout) view.findViewById(R.id.keyboard_expand);
        mEditExpand = (ExpandableLayout) view.findViewById(R.id.edit_expand);

        mIvKeyboard = (ImageView) view.findViewById(R.id.image_keyboard_button);
        mIvEdit = (ImageView) view.findViewById(R.id.image_notes_button);

        mRlKeyboard = (RelativeLayout) view.findViewById(R.id.keyboard_button);
        mRlEdit = (RelativeLayout) view.findViewById(R.id.notes_button);

        for (int i : mSwitchButtons) {
            view.findViewById(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
}
