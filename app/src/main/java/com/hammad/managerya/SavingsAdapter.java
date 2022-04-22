package com.hammad.managerya;

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

public class SavingsAdapter extends RecyclerView.Adapter<SavingsAdapter.MyViewHolder> {

    Context context;
    private OnSavingClickListener mOnSavingClickListener;

    public SavingsAdapter(Context context,OnSavingClickListener onSavingClickListener) {
        this.context = context;
        this.mOnSavingClickListener=onSavingClickListener;
    }

    @NonNull
    @Override
    public SavingsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_saving_recycler,parent,false);
        return new MyViewHolder(view,mOnSavingClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingsAdapter.MyViewHolder holder, int position) {

        //setting the current currency to textviews
        holder.textViewCurrency1.setText(CURRENCY_);
        holder.textViewCurrency2.setText(CURRENCY_);

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewCategoryName,textViewCurrency1,textViewCurrency2;
        TextView textViewGoalAmount,textViewAmountSaved,textViewRemainingAmount;
        LinearProgressIndicator progressBar;

        OnSavingClickListener onSavingClickListener;

        public MyViewHolder(@NonNull View itemView,OnSavingClickListener onSavingClickListener) {
            super(itemView);

            imageView=itemView.findViewById(R.id.img_saving);

            textViewCategoryName=itemView.findViewById(R.id.txt_cat_saving);

            textViewCurrency1=itemView.findViewById(R.id.text_currency_1);
            textViewAmountSaved=itemView.findViewById(R.id.text_amount_saved);
            textViewGoalAmount=itemView.findViewById(R.id.text_goal_amount);

            textViewCurrency2=itemView.findViewById(R.id.text_currency_2);
            textViewRemainingAmount=itemView.findViewById(R.id.text_remaining_amount);

            progressBar=itemView.findViewById(R.id.progress_saving_recycler);

            //initializing interface instance
            this.onSavingClickListener=onSavingClickListener;

            //click listener
            itemView.setOnClickListener(view -> mOnSavingClickListener.onSavingItemClicked(getAdapterPosition()));

        }
    }

    public interface OnSavingClickListener{
        void onSavingItemClicked(int position);
    }
}
