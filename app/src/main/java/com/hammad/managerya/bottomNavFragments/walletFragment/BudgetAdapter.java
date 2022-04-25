package com.hammad.managerya.bottomNavFragments.walletFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hammad.managerya.R;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.MyViewHolder> {

    Context context;
    private OnBudgetClickListener mOnBudgetClickListener;

    public BudgetAdapter(Context context, OnBudgetClickListener onBudgetClickListener) {
        this.context = context;
        this.mOnBudgetClickListener=onBudgetClickListener;
    }

    @NonNull
    @Override
    public BudgetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_budget_recycler,parent,false);
        return new MyViewHolder(view,mOnBudgetClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.MyViewHolder holder, int position) {

        //setting the current currency to textviews
        holder.textViewCurrency1.setText(CURRENCY_);
        holder.textViewCurrency2.setText(CURRENCY_);

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewCategoryName,textViewCurrency1,textViewCurrency2;
        TextView textViewGoalAmount,textViewAmountSaved,textViewRemainingAmount;
        LinearProgressIndicator progressBar;

        OnBudgetClickListener onBudgetClickListener;

        public MyViewHolder(@NonNull View itemView,OnBudgetClickListener onBudgetClickListener) {
            super(itemView);

            imageView=itemView.findViewById(R.id.img_budget);

            textViewCategoryName=itemView.findViewById(R.id.txt_cat_budget);

            textViewCurrency1=itemView.findViewById(R.id.text_currency_1_budget);
            textViewAmountSaved=itemView.findViewById(R.id.text_amount_budget);
            textViewGoalAmount=itemView.findViewById(R.id.text_goal_amount_budget);

            textViewCurrency2=itemView.findViewById(R.id.text_currency_2_budget);
            textViewRemainingAmount=itemView.findViewById(R.id.text_remaining_amount_budget);

            progressBar=itemView.findViewById(R.id.progress_budget_recycler);

            //initializing interface instance
            this.onBudgetClickListener=onBudgetClickListener;

            //click listener
            itemView.setOnClickListener(view -> onBudgetClickListener.onBudgetItemClicked(getAdapterPosition()));

        }
    }

    public interface OnBudgetClickListener{
        void onBudgetItemClicked(int position);
    }
}
