package com.example.alex.balance.dagger.presenters;

import android.content.Intent;
import android.graphics.Color;

import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.views.StartActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.realm.RealmResults;

import static com.example.alex.balance.custom.CategoryData.OTHER_CATEGORY_NAME;
import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_COLOR;
import static com.example.alex.balance.dialogs.CreateCategoryDialog.CREATE_CATEGORY_NAME;

public class StartActivityPresenter extends BasePresenter<StartActivity> {
    private RealmHelper mHelper;

    @Inject
    public StartActivityPresenter(StartActivity view, RealmHelper helper) {
        bindView(view);
        this.mHelper = helper;
    }

    public void setupChart(PieChart mChart) {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setCenterTextSize(24f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setTransparentCircleColor(Color.BLUE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(82f);
        mChart.setTransparentCircleRadius(77f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.getLegend().setEnabled(false);
        mChart.setEntryLabelColor(Color.BLUE);
        mChart.setEntryLabelTextSize(12f);
        mChart.setCenterTextColor(Color.BLUE);
    }

    public PieData setData(List<CategoryData> list) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            CategoryData data = list.get(i);
            float value = data.getProfit() - data.getLoss();
            if (value < 0) {
                entries.add(new PieEntry(value * -1, data.getName()));
                colors.add(data.getColor());
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.GREEN);
        data.setDrawValues(false);

        return data;
    }

    public float calculateTotalBalance(List<CategoryData> datas) {
        float totalBalance = 0f;

        for (CategoryData data : datas) {
            totalBalance += data.getProfit() - data.getLoss();
        }

        return totalBalance;
    }

    private List<CategoryData> createCategoryData() {
        List<CategoryData> datas = new ArrayList<>();
        final String[] imagesName = {OTHER_CATEGORY_NAME, "Mobile Home", "Mouse Trap", "Wardrobe", "Washing Machine"};
        final String[] imagesColor = {"#00CC00", "#FFD300", "#0B61A4", "#D2006B", "#1E90FF"};

        for (int i = 0; i < imagesName.length && i < imagesName.length; i++) {
            datas.add(mHelper.createCategoryData(imagesName[i], Color.parseColor(imagesColor[i])));
        }

        return datas;
    }

    public List<CategoryData> getCatList() {
        RealmResults<CategoryData> realmResults = getCategoryList();
        return realmResults.isEmpty() ? createCategoryData() : realmResults;
    }

    public RealmResults<CategoryData> getCategoryList() {
        return mHelper.getCategorySorted();
    }

    public void createCategory(Intent intent) {
        mHelper.createCategoryData(intent.getStringExtra(CREATE_CATEGORY_NAME), intent.getIntExtra(CREATE_CATEGORY_COLOR, 0));
    }
}
