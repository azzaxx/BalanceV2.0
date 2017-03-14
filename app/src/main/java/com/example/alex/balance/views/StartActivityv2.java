package com.example.alex.balance.views;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.example.alex.balance.R;
import com.example.alex.balance.adapters.CategoryListAdapter;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.SimpleItemTouchHelperCallback;
import com.example.alex.balance.custom.realm.BalanceRealmConfig;
import com.example.alex.balance.interfaces.OnStartDragListener;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by alex on 13.03.17.
 */

public class StartActivityv2 extends AppCompatActivity implements OnStartDragListener {
    public static final String PROFIT_LOSS_KEY = "start_activity_profit_or_loss_key";
    public static final String CATEGORY_POSITION_KEY = "start_activity_category_position_key";

    @BindView(R.id.recycler_view_list)
    RecyclerView mRVList;
    @BindView(R.id.pie_chart)
    PieChart mChart;
    private Realm mRealm;
    private ItemTouchHelper mItemTouchHelper;
    private List<CategoryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activyty_v2);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ButterKnife.bind(this);
//        mRealm = Realm.getDefaultInstance();
        mRealm = Realm.getInstance(BalanceRealmConfig.getRealmConfiguration());


        if (mRealm.where(CategoryData.class).findAll().isEmpty()) {
            list = new ArrayList<>();

            for (int i = 0; i < 9; i++) {
                mRealm.beginTransaction();

                CategoryData data = mRealm.createObject(CategoryData.class);
                data.setName("blablabal " + i);
                list.add(data);

                mRealm.commitTransaction();
            }
        } else {
            list = mRealm.where(CategoryData.class).findAll();
        }

        mRVList.setHasFixedSize(true);
        mRVList.setLayoutManager(new LinearLayoutManager(this));

        CategoryListAdapter adapter = new CategoryListAdapter(list, this, this);
        mRVList.setAdapter(adapter);

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setCenterText("Hello, world!");
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

        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        mChart.getLegend().setEnabled(false);
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRVList);
    }

    private void setData(int count, float range) {
        String[] mParties = new String[]{
                "Party E", "Party E", "Party E", "Party E", "Party E", "Party F", "Party G", "Party H",
                "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
                "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
                "Party Y", "Party Z"
        };
        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5),
                    mParties[i % mParties.length],
                    getResources().getDrawable(R.drawable.ic_keypad_primary)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.GREEN);
        data.setDrawValues(false);

//        data.setValueTypeface(mTfLight);
        mChart.setData(data);
        mChart.setDrawEntryLabels(false);
        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, null).addToBackStack(null).commit();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
//        mItemTouchHelper.startDrag(viewHolder);
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
    }

    public Realm getRealm() {
        return this.mRealm;
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        Bundle args = new Bundle();
        args.putInt(CATEGORY_POSITION_KEY, position);

        if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
            Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
            args.putInt(PROFIT_LOSS_KEY, 1);
        } else {
            Toast.makeText(this, "Left", Toast.LENGTH_SHORT).show();
            args.putInt(PROFIT_LOSS_KEY, -1);
        }

        Fragment frg = new BalanceFragment();
        frg.setArguments(args);
        showFragment(frg);
    }

    public CategoryData getCategoryByPosition(int position) {
        return list.get(position);
    }
}
