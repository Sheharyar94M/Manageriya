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

import java.util.List;

public class ConsumerTransactionAdapter extends RecyclerView.Adapter<ConsumerTransactionAdapter.ViewHolder> {

    Context context;
    List<String> stringList;

    public ConsumerTransactionAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
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

        if(stringList.get(position).equals("Lend"))
        {
            holder.flowLend.setVisibility(View.VISIBLE);

            holder.textViewBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));
            holder.textViewCurrencyRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));
            holder.textViewRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));

            //setting the borrow visibility to gone
            holder.flowBorrow.setVisibility(View.GONE);
        }
        else if(stringList.get(position).equals("Borrow"))
        {
            holder.flowBorrow.setVisibility(View.VISIBLE);

            holder.textViewBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));
            holder.textViewCurrencyRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));
            holder.textViewRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));

            //setting the lend visibility to gone
            holder.flowLend.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDate,textViewBalance,textViewCurrencyRemBalance,textViewRemBalance;
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
