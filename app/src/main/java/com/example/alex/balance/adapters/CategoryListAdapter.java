package com.example.alex.balance.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.interfaces.ItemTouchHelperAdapter;
import com.example.alex.balance.interfaces.ItemTouchHelperViewHolder;
import com.example.alex.balance.interfaces.OnStartDragListener;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 14.03.17.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryHolder> implements ItemTouchHelperAdapter {
    private Context mContext;
    private final OnStartDragListener mDragStartListener;
    private List<CategoryData> list;

    public CategoryListAdapter(List<CategoryData> list, Context context, OnStartDragListener dragStartListener) {
        this.mDragStartListener = dragStartListener;
        this.mContext = context;
        this.list = list;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_mainv2, parent, false));
    }

    @Override
    public void onBindViewHolder(final CategoryHolder holder, int position) {
        holder.relativeContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.mCategoryColorView.setBackgroundColor(list.get(position).getColor());
        holder.textName.setText(list.get(position).getName());
        holder.textBalance.setText(String.format("%.2f", (list.get(position).getProfit() - list.get(position).getLoss())));
        try {
            holder.mCategoryImage.setImageDrawable(Drawable.createFromStream(mContext.getAssets().open(list.get(position).getIconName() + ".png"), null));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemSwipe(int position, int direction) {
        mDragStartListener.onItemSwipe(position, direction);
    }

    class CategoryHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.category_list_tv_name)
        TextView textName;
        @BindView(R.id.category_list_tv_balance)
        TextView textBalance;
        @BindView(R.id.category_list_image_container)
        RelativeLayout relativeContainer;
        @BindView(R.id.category_list_image)
        ImageView mCategoryImage;
        @BindView(R.id.category_list_cat_color)
        View mCategoryColorView;
        private int color;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            this.color = ((ColorDrawable) itemView.getBackground()).getColor();
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(color);
        }
    }
}
