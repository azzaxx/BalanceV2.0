package com.example.alex.balance.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alex.balance.R;
import com.example.alex.balance.adapters.CategoryListAdapter;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.custom.SimpleItemTouchHelperCallback;
import com.example.alex.balance.dagger.components.DaggerStartActivityComponent;
import com.example.alex.balance.dagger.modules.ActivityModule;
import com.example.alex.balance.dagger.presenters.StartActivityPresenter;
import com.example.alex.balance.dialogs.CreateCategoryDialog;
import com.example.alex.balance.interfaces.OnStartDragListener;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements OnStartDragListener {
    public static final String PROFIT_LOSS_KEY = "start_activity_profit_or_loss_key";
    public static final String CATEGORY_POSITION_KEY = "start_activity_category_position_key";
    private static int MENU_ITEM_ID = 1000;

    @BindView(R.id.recycler_view_list)
    RecyclerView mRVList;
    @BindView(R.id.pie_chart)
    PieChart mChart;
    private ItemTouchHelper mItemTouchHelper;
    private List<CategoryData> list;
    private CategoryListAdapter adapter;
    @Inject
    StartActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        ButterKnife.bind(this);
        DaggerStartActivityComponent.builder().activityModule(new ActivityModule(this)).build().inject(this);

        list = mPresenter.getCatList();
        adapter = new CategoryListAdapter(list, this, this);
        mRVList.setHasFixedSize(true);
        mRVList.setLayoutManager(new LinearLayoutManager(this));
        mRVList.setAdapter(adapter);
        mPresenter.setupChart(mChart);
        mChart.setData(mPresenter.setData(list));
        mChart.setDrawEntryLabels(false);
        mChart.highlightValues(null);
        mChart.invalidate();
        mChart.setCenterText("Balance: " + String.format(Locale.US, "%.2f", mPresenter.calculateTotalBalance(list)));
        mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(adapter));
        mItemTouchHelper.attachToRecyclerView(mRVList);
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, null).addToBackStack(null).commit();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
//        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_ID, 0, "Create category");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ITEM_ID) {
            CreateCategoryDialog.newInstance().show(getSupportFragmentManager(), StartActivity.class.getName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void popBackStack() {
        getSupportFragmentManager().popBackStack();
        notifyChanges();
    }

    private void notifyChanges() {
        List<CategoryData> dataList = mPresenter.getCategoryList();
        adapter.notifyDataSetChanged();
        mPresenter.setData(dataList);
        mChart.setCenterText("Balance: " + String.format(Locale.US, "%.2f", mPresenter.calculateTotalBalance(dataList)));
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

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
    }

    public void createCategory(Intent intent) {
        mPresenter.createCategory(intent);
        notifyChanges();
    }
}
