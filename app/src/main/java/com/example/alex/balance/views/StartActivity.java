package com.example.alex.balance.views;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.alex.balance.R;
import com.example.alex.balance.adapters.CategoryListAdapter;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.SimpleItemTouchHelperCallback;
import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.interfaces.OnStartDragListener;
import com.example.alex.balance.presenters.StartActivityPresenter;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by alex on 13.03.17.
 */

public class StartActivity extends AppCompatActivity implements OnStartDragListener {
    public static final String PROFIT_LOSS_KEY = "start_activity_profit_or_loss_key";
    public static final String CATEGORY_POSITION_KEY = "start_activity_category_position_key";

    @BindView(R.id.recycler_view_list)
    RecyclerView mRVList;
    @BindView(R.id.pie_chart)
    PieChart mChart;
    private StartActivityPresenter mPresenter = new StartActivityPresenter();
    private ItemTouchHelper mItemTouchHelper;
    private List<CategoryData> list;
    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activyty);
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        ButterKnife.bind(this);
        mPresenter.bindView(this);

        RealmResults<CategoryData> realmResults = RealmHelper.getInstance().getCategorySorted();

        list = realmResults.isEmpty() ? mPresenter.createCategoryData() : realmResults;

        adapter = new CategoryListAdapter(list, this, this);

        mRVList.setHasFixedSize(true);
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        mRVList.setAdapter(adapter);
        mPresenter.setupChart(mChart);
        mPresenter.setData(list, mChart);

        mChart.setCenterText(mPresenter.totalBalance(list));
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRVList);
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, null).addToBackStack(null).commit();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
//        mItemTouchHelper.startDrag(viewHolder);
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
        adapter.notifyDataSetChanged();
        notifyChanges();
    }

    private void notifyChanges() {
        List<CategoryData> dataList = RealmHelper.getInstance().getCategorySorted();
        mPresenter.setData(dataList, mChart);
        mChart.setCenterText(mPresenter.totalBalance(dataList));
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        Bundle args = new Bundle();
        args.putInt(CATEGORY_POSITION_KEY, position);

        if (direction == ItemTouchHelper.RIGHT || direction == ItemTouchHelper.END) {
            args.putInt(PROFIT_LOSS_KEY, -1);
        } else {
            args.putInt(PROFIT_LOSS_KEY, 1);
        }

        Fragment frg = new BalanceFragment();
        frg.setArguments(args);
        showFragment(frg);
    }

    public CategoryData getCategoryByPosition(int position) {
        return list.get(position);
    }

    @Override
    public void onBackPressed() {
        popBackStack();
    }
}
