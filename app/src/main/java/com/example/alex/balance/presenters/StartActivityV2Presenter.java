package com.example.alex.balance.presenters;

import android.graphics.Color;

import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.views.StartActivityv2;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.example.alex.balance.custom.CategoryData.OTHER_CATEGORY_ICON;
import static com.example.alex.balance.custom.CategoryData.OTHER_CATEGORY_NAME;

/**
 * Created by alex on 15.03.17.
 */

public class StartActivityV2Presenter extends BasePresenter<StartActivityv2> {
    public void setupChart(PieChart mChart) {
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setCenterTextSize(24f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(82f);
        mChart.setTransparentCircleRadius(77f);
        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.getLegend().setEnabled(false);
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
        mChart.setCenterTextColor(Color.WHITE);
    }

    public void setData(List<CategoryData> list, PieChart mChart) {
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

        mChart.setData(data);
        mChart.setDrawEntryLabels(false);
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private float calculateTotalBalance(List<CategoryData> datas) {
        float totalBalance = 0f;

        for (CategoryData data : datas) {
            totalBalance += data.getProfit() - data.getLoss();
        }

        return totalBalance;
    }

    public String totalBalance(List<CategoryData> datas) {
        return "Balance: " + String.format("%.2f", calculateTotalBalance(datas));
    }

    public List<CategoryData> createCategoryData() {
        List<CategoryData> datas = new ArrayList<>();
        final String[] imagesFile = {OTHER_CATEGORY_ICON, "mobile_home", "mouse_trap_mouse", "wardrobe", "washing_machine"};
        final String[] imagesName = {OTHER_CATEGORY_NAME, "Mobile Home", "Mouse Trap", "Wardrobe", "Washing Machine"};
        final String[] imagesColor = {"#00CC00", "#FFD300", "#0B61A4", "#D2006B", "#1E90FF"};

        for (int i = 0; i < imagesFile.length && i < imagesName.length; i++) {
            datas.add(RealmHelper.getInstance().createCategoryData(imagesName[i], imagesFile[i], imagesColor[i]));
        }

        return datas;
    }
}
