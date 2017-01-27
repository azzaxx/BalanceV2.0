package com.example.alex.balance.presenters;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alex.balance.BalanceFragment;
import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmList;

import static com.example.alex.balance.BalanceFragment.CREATE_CATEGORY_DIALOG_REQ_CODE;
import static com.example.alex.balance.BalanceFragment.DATE_DIALOG_REQ_CODE;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_COLOR;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_NAME;
import static com.example.alex.balance.custom.CategoryData.CATEGORY_FIELD_TIME;
import static com.example.alex.balance.custom.CategoryData.OTHER_CATEGORY_COLOR;
import static com.example.alex.balance.custom.CategoryData.OTHER_CATEGORY_NAME;
import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_COLOR;
import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_NAME;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_DAY_KEY;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_MONTH_KEY;
import static com.example.alex.balance.dialogs.DateDialog.DATE_DIALOG_YEAR_KEY;

/**
 * Created by alex on 09.01.17.
 */

public class DataPresenter extends BasePresenter<BalanceFragment> {
    public static final String DEFAULT_VALUE = "0.00";
    private static final String DOT = ".";
    private static final String ZERO = "0";
    private static final int MAX_SUM_LENGTH = 9;

    public void setDate(@Nullable Intent date) {
        Calendar calendar = Calendar.getInstance();
        int year = date == null ? calendar.get(Calendar.YEAR) : date.getIntExtra(DATE_DIALOG_YEAR_KEY, 0);
        int month = date == null ? calendar.get(Calendar.MONTH) : date.getIntExtra(DATE_DIALOG_MONTH_KEY, 0);
        int day = date == null ? calendar.get(Calendar.DAY_OF_MONTH) : date.getIntExtra(DATE_DIALOG_DAY_KEY, 0);

        mView.setDate(String.valueOf(day), new DateFormatSymbols().getMonths()[month], String.valueOf(year));
    }

    public void clearOne(String text) {
        if (text.equals(DEFAULT_VALUE)) {
            return;
        }
        String result;
        String temp = text.substring(0, text.length() - 1);
        temp = temp.replace(DOT, "");

        if (temp.length() > 2 && !temp.startsWith(ZERO)) {
            temp = temp.substring(0, temp.length() - 2) + DOT + temp.substring(temp.length() - 2, temp.length());
            result = temp;
        } else {
            if (temp.startsWith(ZERO + DOT)) {
                result = ZERO + DOT + ZERO + temp.substring(0, 1);
            } else if (temp.startsWith(ZERO + DOT + ZERO)) {
                result = DEFAULT_VALUE;
            } else {
                result = ZERO + DOT + temp.substring(0, 2);
            }
        }

        mView.setTotalSum(result);
    }

    public void addOne(String value, String previousValue) {
        if (previousValue.length() > MAX_SUM_LENGTH) {
            return;
        }
        String result;

        if (previousValue.equals(DEFAULT_VALUE)) {
            result = ZERO + DOT + ZERO + value;
        } else if (previousValue.startsWith(ZERO + DOT + ZERO)) {
            result = ZERO + DOT + previousValue.charAt(previousValue.length() - 1) + value;
        } else {
            String temp = previousValue.replace(DOT, "");
            if (temp.startsWith(ZERO)) {
                temp = temp.replaceFirst(ZERO, "");
            }
            temp += value;
            result = temp.substring(0, temp.length() - 2) + DOT + temp.substring(temp.length() - 2, temp.length());
        }

        mView.setTotalSum(result);
    }

    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == DATE_DIALOG_REQ_CODE) {
            setDate(data);
        } else if (requestCode == CREATE_CATEGORY_DIALOG_REQ_CODE) {
            createCategory(data.getStringExtra(CREATE_CATEGORY_NAME), data.getIntExtra(CREATE_CATEGORY_COLOR, -1), true);
        }
    }

    private void createCategory(String name, int color, boolean addView) {
        final long timeStamp = System.currentTimeMillis();
        Realm realmObj = mView.getAct().getRealm();
        realmObj.beginTransaction();

        CategoryData data = realmObj.createObject(CategoryData.class);
        data.setTimeStamp(timeStamp);
        data.setName(name);
        data.setColor(color);

        realmObj.commitTransaction();
        addCategory(name, color, timeStamp, addView);
    }

    private void addCategory(String name, int color, long timeStamp, boolean addView) {
        if (!addView)
            return;
        final View v = LayoutInflater.from(mView.getContext()).inflate(R.layout.recycler_item_balance, null);
        ((CheckBox) v.findViewById(R.id.item_balance_check_box)).setText(name);
        ((TextView) v.findViewById(R.id.item_balance_time_stamp)).setText(String.valueOf(timeStamp));
        v.findViewById(R.id.color_box).setBackgroundColor(color);
        v.findViewById(R.id.item_balance_remove_view).setOnClickListener(getDeleteListener(v, name, color, timeStamp));
        mView.addViewCategory(v);
    }

    private View.OnClickListener getDeleteListener(final View v, final String name, final int color, final long timeStamp) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realmObj = mView.getAct().getRealm();
                realmObj.beginTransaction();
                realmObj.where(CategoryData.class)
                        .equalTo(CATEGORY_FIELD_NAME, name)
                        .equalTo(CATEGORY_FIELD_TIME, timeStamp)
                        .equalTo(CATEGORY_FIELD_COLOR, color)
                        .findFirst().deleteFromRealm();
                realmObj.commitTransaction();
                mView.removeViewCategory(v);
            }
        };
    }

    public void reAddAllCategory() {
        mView.removeAllCategory();
        for (CategoryData data : mView.getAct().getRealm().where(CategoryData.class).findAll()) {
            addCategory(data.getName(), data.getColor(), data.getTimeStamp(),
                    !data.getName().equals(OTHER_CATEGORY_NAME) && data.getColor() != OTHER_CATEGORY_COLOR);
        }
    }

    public void addBalanceData(String totalSum, String day, String month, String year, String comment, boolean isProfit, RealmList<CategoryData> checkedList) {
        if (totalSum.equals(DEFAULT_VALUE)) {
            return;
        }

        Realm realmObj = mView.getAct().getRealm();
        realmObj.beginTransaction();

        BalanceData data = realmObj.createObject(BalanceData.class);
        data.setTotalSum(totalSum);
        data.setDay(day);
        data.setMonth(month);
        data.setYear(year);
        data.setComment(comment);
        data.setTimeStamp(System.currentTimeMillis());
        data.setIsProfit(isProfit);
        data.setList(checkedList);

        realmObj.commitTransaction();

        addSumToCategories(totalSum, isProfit, checkedList);
    }

    private void addSumToCategories(String totalSum, boolean isProfit, RealmList<CategoryData> checkedList) {
        Realm realmObj = mView.getAct().getRealm();
        realmObj.beginTransaction();

        if (checkedList.isEmpty()) {
            if (isProfit)
                getSelectedCategory(OTHER_CATEGORY_NAME, OTHER_CATEGORY_COLOR).addProfit(totalSum);
            else
                getSelectedCategory(OTHER_CATEGORY_NAME, OTHER_CATEGORY_COLOR).addLoss(totalSum);
        } else {
            for (CategoryData categoryData : checkedList) {
                CategoryData someData = getSelectedCategory(categoryData.getName(), categoryData.getColor(), categoryData.getTimeStamp());
                if (isProfit)
                    someData.addProfit(totalSum);
                else
                    someData.addLoss(totalSum);
            }
        }
        realmObj.commitTransaction();
    }

    private CategoryData getSelectedCategory(String categoryName, int categoryColor, long categoryTimeStamp) {
        return mView.getAct().getRealm().where(CategoryData.class)
                .equalTo(CATEGORY_FIELD_NAME, categoryName)
                .equalTo(CATEGORY_FIELD_TIME, categoryTimeStamp)
                .equalTo(CATEGORY_FIELD_COLOR, categoryColor)
                .findFirst();
    }

    private CategoryData getSelectedCategory(String categoryName, int categoryColor) {
        return mView.getAct().getRealm().where(CategoryData.class)
                .equalTo(CATEGORY_FIELD_NAME, categoryName)
                .equalTo(CATEGORY_FIELD_COLOR, categoryColor)
                .findFirst();
    }

    public RealmList<CategoryData> getCheckedList(LinearLayout mLlCategory) {
        RealmList<CategoryData> list = new RealmList<>();

        for (int i = 0; i < mLlCategory.getChildCount(); i++) {
            View child = mLlCategory.getChildAt(i);
            if (((CheckBox) child.findViewById(R.id.item_balance_check_box)).isChecked()) {
                final String categoryName = ((TextView) child.findViewById(R.id.item_balance_check_box)).getText().toString();
                final long categoryTimeStamp = Long.parseLong(((TextView) child.findViewById(R.id.item_balance_time_stamp)).getText().toString());
                final int categoryColor = ((ColorDrawable) child.findViewById(R.id.color_box).getBackground()).getColor();
                list.add(getSelectedCategory(categoryName, categoryColor, categoryTimeStamp));
            }
        }

        if (list.isEmpty()) {
            list.add(getSelectedCategory(OTHER_CATEGORY_NAME, OTHER_CATEGORY_COLOR));
        }

        return list;
    }

    public void createOtherCategoryIfNotExist() {
        if (getSelectedCategory(OTHER_CATEGORY_NAME, OTHER_CATEGORY_COLOR) == null) {
            createCategory(OTHER_CATEGORY_NAME, OTHER_CATEGORY_COLOR, false);
        }
    }
}
