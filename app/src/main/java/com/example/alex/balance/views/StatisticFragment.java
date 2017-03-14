package com.example.alex.balance.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.BalanceData;
import com.example.alex.balance.custom.CategoryData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by alex on 26.01.17.
 */

public class StatisticFragment extends Fragment {
    @BindView(R.id.statistic_pie_chart)
    PieChart mChart;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.statistic_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StartActivity activity = ((StartActivity) getActivity());
        activity.actionButtonsVisibility(false);
        Realm realm = activity.getRealm();
        unbinder = ButterKnife.bind(this, view);
        getActivity().findViewById(R.id.welcome_text_in_statitic).setVisibility(realm.where(BalanceData.class).findAll().isEmpty() ? View.VISIBLE : View.GONE);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.TRANSPARENT);
        mChart.setTransparentCircleColor(Color.TRANSPARENT);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(2f);
        mChart.setTransparentCircleRadius(0f);
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);

        setData(realm);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend legend = mChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);

        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
    }

    private void setData(Realm realm) {
        ArrayList<Integer> colors = new ArrayList<>();

        realm.beginTransaction();

        RealmResults<CategoryData> datas = realm.where(CategoryData.class).findAll();

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getLoss() > 0) {
                entries.add(new PieEntry(datas.get(i).getLoss(), datas.get(i).getName()));
                colors.add(datas.get(i).getColor());
            }
        }

        realm.commitTransaction();

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
