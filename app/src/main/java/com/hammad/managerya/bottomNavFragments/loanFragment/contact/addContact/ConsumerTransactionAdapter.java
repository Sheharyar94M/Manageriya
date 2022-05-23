package com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.LoanDetailModel;

import java.util.List;

public class ConsumerTransactionAdapter extends RecyclerView.Adapter<ConsumerTransactionAdapter.ViewHolder> {

    Context context;
    List<LoanDetailModel> loanDetailList;

    public ConsumerTransactionAdapter(Context context, List<LoanDetailModel> loanDetailModelList) {
        this.context = context;
        this.loanDetailList = loanDetailModelList;
    }

    @NonNull
    @Override
    public ConsumerTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_recycler_consumer_transaction,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumerTransactionAdapter.ViewHolder holder, int position) {

        LoanDetailModel item=loanDetailList.get(position);

        holder.textViewDate.setText(item.getLoanTransDate());

        //checking optional details
        if(item.getOptionalLoanDetail() == null || item.getOptionalLoanDetail().length() == 0) {
            holder.textViewOptionalDetail.setVisibility(View.GONE);
        }
        else if(item.getOptionalLoanDetail() != null || item.getOptionalLoanDetail().length() > 0) {
            holder.textViewOptionalDetail.setVisibility(View.VISIBLE);
            holder.textViewOptionalDetail.setText(item.getOptionalLoanDetail());
        }

        if(item.getTransactionType().equals("Lend"))
        {
            holder.flowLend.setVisibility(View.VISIBLE);

            holder.textViewBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));
            holder.textViewCurrencyRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));
            holder.textViewRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));

            //setting the loan transaction amount
            holder.textViewAmountLend.setText(item.getLoanAmount());

            //setting the borrow visibility to gone
            holder.flowBorrow.setVisibility(View.GONE);
        }
        else if(item.getTransactionType().equals("Borrow"))
        {
            holder.flowBorrow.setVisibility(View.VISIBLE);

            holder.textViewBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));
            holder.textViewCurrencyRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));
            holder.textViewRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));

            //setting the loan transaction amount
            holder.textViewAmountBorrow.setText(item.getLoanAmount());

            //setting the lend visibility to gone
            holder.flowLend.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return loanDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDate,textViewBalance,textViewCurrencyRemBalance,textViewRemBalance,textViewOptionalDetail;
        TextView textViewCurrencyLend,textViewAmountLend;
        TextView textViewCurrencyBorrow,textViewAmountBorrow;
        Flow flowLend,flowBorrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //constraint 1 views
            textViewDate=itemView.findViewById(R.id.text_view_date);
            textViewBalance=itemView.findViewById(R.id.text_bal);
            textViewCurrencyRemBalance=itemView.findViewById(R.id.txt_currency_1_cons);
            textViewRemBalance=itemView.findViewById(R.id.txt_balance);
            textViewOptionalDetail=itemView.findViewById(R.id.txt_details);

            //constraint 2 views
            textViewCurrencyLend=itemView.findViewById(R.id.txt_currency_2_cons);
            textViewAmountLend=itemView.findViewById(R.id.txt_lend);
            flowLend=itemView.findViewById(R.id.flow_lend);

            //constraint 3 views
            textViewCurrencyBorrow=itemView.findViewById(R.id.txt_currency_3_cons);
            textViewAmountBorrow=itemView.findViewById(R.id.txt_borrowed);
            flowBorrow=itemView.findViewById(R.id.flow_borrow);

            //setting the currencies
            textViewCurrencyRemBalance.setText(CURRENCY_);
            textViewCurrencyLend.setText(CURRENCY_);
            textViewCurrencyBorrow.setText(CURRENCY_);
        }
    }
}
