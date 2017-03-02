package com.example.alex.balance.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alex.balance.R;
import com.example.alex.balance.custom.CategoryData;
import com.example.alex.balance.interfaces.RecyclerClickCategory;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 24.02.17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private View mIvSelectedCategory;
    private int mSelectedCategoryPosition;
    private Context mContext;
    private List<CategoryData> mDataList;
    private RecyclerClickCategory mRecyclerClickCategory;

    public CategoryAdapter(Context context, List<CategoryData> categories) {
        this.mContext = context;
        mDataList = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.mCategoryName.setText(mDataList.get(position).getName());

        try {
            holder.mCategoryIcon.setImageDrawable(Drawable.createFromStream(mContext.getAssets().open(mDataList.get(position).getIconName() + ".png"), null));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public CategoryData getCurrentCategory() {
        if (mSelectedCategoryPosition == 0)
            return mDataList.get(1);
        return mDataList.get(mSelectedCategoryPosition);
    }

    public void setOnRecyclerClick(RecyclerClickCategory clickListener) {
        this.mRecyclerClickCategory = clickListener;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recycler_item_category_name_text)
        TextView mCategoryName;
        @BindView(R.id.recycler_item_category_image)
        ImageView mCategoryIcon;
        @BindView(R.id.recycler_item_category_image_container)
        RelativeLayout mRlImageContainer;
        private Drawable mDrawable;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();
            if (mRecyclerClickCategory != null) {
                mRecyclerClickCategory.onRecyclerInClick(view, position);
            }
            if (view != mIvSelectedCategory && position != 0) {
                mDrawable = mRlImageContainer.getBackground();
                mRlImageContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.round_white));
                if (mIvSelectedCategory != null) {
                    mIvSelectedCategory.findViewById(R.id.recycler_item_category_image_container).setBackgroundDrawable(mDrawable);
                }
                mIvSelectedCategory = view;
                mSelectedCategoryPosition = position;
            }
        }
    }
}
