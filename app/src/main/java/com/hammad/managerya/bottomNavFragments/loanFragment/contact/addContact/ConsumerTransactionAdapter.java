package com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.loanFragment.DB.LoanDetailEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConsumerTransactionAdapter extends RecyclerView.Adapter<ConsumerTransactionAdapter.ViewHolder> {

    Context context;
    List<LoanDetailEntity> loanDetailList;
    OnConsumerTransactionListener mConsumerTransactionListener;

    RoomDBHelper database;

    public ConsumerTransactionAdapter(Context context, List<LoanDetailEntity> loanDetailModelList,OnConsumerTransactionListener listener) {
        this.context = context;
        this.loanDetailList = loanDetailModelList;
        this.mConsumerTransactionListener = listener;

        database = RoomDBHelper.getInstance(context);
    }

    @NonNull
    @Override
    public ConsumerTransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_recycler_consumer_transaction,parent,false);
        return new ViewHolder(view, mConsumerTransactionListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumerTransactionAdapter.ViewHolder holder, int position) {

        LoanDetailEntity item=loanDetailList.get(position);

        //setting the date
        holder.textViewDate.setText(getConvertedDate(item.getTransDate()));

        //checking optional details
        if(item.getOptionalDesc() == null || item.getOptionalDesc().length() == 0) {
            holder.textViewOptionalDetail.setVisibility(View.GONE);
        }
        else if(item.getOptionalDesc() != null || item.getOptionalDesc().length() > 0) {
            holder.textViewOptionalDetail.setVisibility(View.VISIBLE);
            holder.textViewOptionalDetail.setText(item.getOptionalDesc());
        }

        if(item.getAmountLend() > 0) {
            holder.flowLend.setVisibility(View.VISIBLE);

            holder.textViewBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));
            holder.textViewCurrencyRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));
            holder.textViewRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightGreen));

            //setting the loan transaction amount
            holder.textViewAmountLend.setText(String.valueOf(item.getAmountLend()));

            //setting the borrow visibility to gone
            holder.flowBorrow.setVisibility(View.GONE);
        }
        else if(item.getAmountBorrow() > 0) {
            holder.flowBorrow.setVisibility(View.VISIBLE);

            holder.textViewBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));
            holder.textViewCurrencyRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));
            holder.textViewRemBalance.setBackgroundColor(context.getResources().getColor(R.color.colorLightRed));

            //setting the loan transaction amount
            holder.textViewAmountBorrow.setText(String.valueOf(item.getAmountBorrow()));

            //setting the lend visibility to gone
            holder.flowLend.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return loanDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{

        TextView textViewDate,textViewBalance,textViewCurrencyRemBalance,textViewRemBalance,textViewOptionalDetail;
        TextView textViewCurrencyLend,textViewAmountLend;
        TextView textViewCurrencyBorrow,textViewAmountBorrow;
        Flow flowLend,flowBorrow;

        OnConsumerTransactionListener onConsumerTransactionListener;

        public ViewHolder(@NonNull View itemView,OnConsumerTransactionListener listener) {
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

            //initializing interface
            this.onConsumerTransactionListener = listener;

            //long click listener
            itemView.setOnCreateContextMenuListener(this);
        }

        //for long click
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem deleteRecordItem=contextMenu.add(Menu.NONE,1,1,"Delete Record");
            deleteRecordItem.setOnMenuItemClickListener(this);
        }

        //long click listener
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            int position=getAdapterPosition();

            if(position != RecyclerView.NO_POSITION)
            {
                switch (menuItem.getItemId())
                {
                    case 1:
                        //delete dialog
                        deleteDialog(position);
                    return true;
                }
            }

            return false;
        }
    }

    /*
        This function is used to convert date from 'yyyy-MM-dd HH:mm:ss' to 'MMM dd, yyyy hh:mm aaa' format
        2022-05-27 11:05:32 to May 27, 2022 11:05 am
    */
    private String getConvertedDate(String databaseDate) {
        String convertedDate = "";

        //database date format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //converting date format to another
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy hh:mm aaa");
        try {
            Date date = dateFormat1.parse(databaseDate);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    //interface
    public interface OnConsumerTransactionListener{
        void onConsumerTransLongClick();
    }

    private void deleteDialog(int position){

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);

        alertDialog.setTitle("Delete Record")
                .setMessage("want to delete this record?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {

                    //deleting a particular transaction
                    database.loanDao().deleteLoanTransaction(loanDetailList.get(position).getLoanId(),loanDetailList.get(position).getPhoneNo());
                    Toast.makeText(context, "Transaction Deleted Successfully", Toast.LENGTH_SHORT).show();

                    //removing the item from list
                    loanDetailList.remove(position);
                    notifyDataSetChanged();

                    //calling the long click interface
                    mConsumerTransactionListener.onConsumerTransLongClick();

                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> {

                    dialogInterface.dismiss();

                });

        alertDialog.create().show();
    }
}
