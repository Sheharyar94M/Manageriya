package com.hammad.managerya.bottomNavFragments.loanFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    Context context;

    public LoanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LoanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_recycler_loan,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.MyViewHolder holder, int position) {

        //setting the selected currency
        holder.textViewCurrency.setText(CURRENCY_);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNameCharacter,textViewName,textViewCurrency,textViewAmount,textViewDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameCharacter=itemView.findViewById(R.id.text_1_loan);
            textViewName=itemView.findViewById(R.id.text_2_loan);
            textViewCurrency=itemView.findViewById(R.id.text_3_loan);
            textViewAmount=itemView.findViewById(R.id.text_4_loan);
            textViewDate=itemView.findViewById(R.id.text_5_loan);
        }
    }
}
