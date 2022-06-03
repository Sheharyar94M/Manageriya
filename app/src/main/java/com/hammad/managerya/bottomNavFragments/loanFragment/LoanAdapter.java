package com.hammad.managerya.bottomNavFragments.loanFragment;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.loanFragment.DB.LoanEntity;

import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    Context context;
    List<LoanEntity> addedContactList;
    OnLoanInterface mOnLoanInterface;

    public LoanAdapter(Context context,List<LoanEntity> list,OnLoanInterface onLoanInterface) {
        this.context = context;
        this.addedContactList = list;
        this.mOnLoanInterface = onLoanInterface;
    }

    @NonNull
    @Override
    public LoanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_recycler_loan,parent,false);
        return new MyViewHolder(view,mOnLoanInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.MyViewHolder holder, int position) {

        //creating item for loop iteration
        LoanEntity item=addedContactList.get(position);

        holder.textViewName.setText(item.getContactName());
        holder.textViewNameCharacter.setText(item.getContactLetter());

    }

    @Override
    public int getItemCount() {
        return addedContactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNameCharacter,textViewName,textViewCurrency,textViewAmount,textViewDate;
        ConstraintLayout constraintLayout;
        OnLoanInterface onLoanInterface;

        public MyViewHolder(@NonNull View itemView,OnLoanInterface onLoanInterface) {
            super(itemView);

            textViewNameCharacter=itemView.findViewById(R.id.text_1_loan);
            textViewName=itemView.findViewById(R.id.text_2_loan);
            textViewCurrency=itemView.findViewById(R.id.text_3_loan);
            textViewAmount=itemView.findViewById(R.id.text_4_loan);
            textViewDate=itemView.findViewById(R.id.text_5_loan);
            constraintLayout=itemView.findViewById(R.id.constraint_11);

            //loan interface
            this.onLoanInterface = onLoanInterface;

            //setting the selected currency
            textViewCurrency.setText(CURRENCY_);

            //interface click listener
            constraintLayout.setOnClickListener(view -> mOnLoanInterface.onLoanClick(getAdapterPosition()));
        }
    }

    public interface OnLoanInterface{
        void onLoanClick(int position);
    }
}
