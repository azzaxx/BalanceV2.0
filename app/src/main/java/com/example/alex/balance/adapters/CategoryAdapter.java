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

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alex on 24.02.17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private View mIvSelectedCategory;
    private Context mContext;
    public String[] images = {"mobile_home", "mouse_trap_mouse", "wardrobe", "washing_machine"};

    public CategoryAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.mCategoryName.setText(images[position]);

        try {
            InputStream is = mContext.getAssets().open(images[position] + ".png");
            Drawable d = Drawable.createFromStream(is, null);
            holder.mCategoryIcon.setImageDrawable(d);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return images.length;
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
            if (view != mIvSelectedCategory) {
                mDrawable = mRlImageContainer.getBackground();
                mRlImageContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.round_white));
                if (mIvSelectedCategory != null) {
                    mIvSelectedCategory.findViewById(R.id.recycler_item_category_image_container).setBackgroundDrawable(mDrawable);
                }
                mIvSelectedCategory = view;
            }
        }
    }
}
