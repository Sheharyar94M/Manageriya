package com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingGoal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;

import java.util.List;

public class SavingCategoryAdapter extends RecyclerView.Adapter<SavingCategoryAdapter.ViewHolder> {

    Context context;
    List<SavingCategoryModel> categoryList;
    OnSavingCategoryListener mOnSavingCategoryListener;

    int selectedPosition=-1;

    public SavingCategoryAdapter(Context context, List<SavingCategoryModel> categoryList, OnSavingCategoryListener mOnSavingCategoryListener) {
        this.context = context;
        this.categoryList = categoryList;
        this.mOnSavingCategoryListener = mOnSavingCategoryListener;
    }

    @NonNull
    @Override
    public SavingCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_recyclerview_item, parent, false);
        return new ViewHolder(view,mOnSavingCategoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingCategoryAdapter.ViewHolder holder, int position) {

        SavingCategoryModel item = categoryList.get(position);

        holder.imageCatImage.setImageResource(item.getCategoryImage());

        holder.textCatName.setText(item.getCategoryName());

        //click listener
        holder.imageCatImage.setOnClickListener(view -> {

            if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = -1;
                notifyDataSetChanged();
                return;
            }

            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();

            //passing the position to interface
            mOnSavingCategoryListener.onSavingCategoryClick(position);

        });

        //highlight the selected item
        if(selectedPosition == holder.getAdapterPosition()) {
            holder.imageCatImage.setBackgroundResource(R.drawable.drawable_recycler_highlight);
        }
        else {
            holder.imageCatImage.setBackgroundResource(R.drawable.white_circle_recyclerview);
        }

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCatImage;
        TextView textCatName;
        OnSavingCategoryListener onSavingCategoryListener;

        public ViewHolder(@NonNull View itemView,OnSavingCategoryListener onSavingCategoryListener) {
            super(itemView);

            imageCatImage = itemView.findViewById(R.id.img_recycler);
            textCatName = itemView.findViewById(R.id.text_recycler);
            this.onSavingCategoryListener = onSavingCategoryListener;
        }
    }

    public interface OnSavingCategoryListener{
        void onSavingCategoryClick(int position);
    }
}
