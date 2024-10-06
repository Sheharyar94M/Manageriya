package com.risibleapps.mywallet.bottomNavFragments.loanFragment;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.loanFragment.DB.LoanDetailEntity;
import com.risibleapps.mywallet.bottomNavFragments.loanFragment.DB.LoanEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.MyViewHolder> {

    Context context;
    List<LoanEntity> addedContactList;
    OnLoanInterface mOnLoanInterface;
    RoomDBHelper database;

    //list for saving the latest transaction amount and sum
    List<LoanDetailEntity> loanDetailEntityList=new ArrayList<>();

    public LoanAdapter(Context context,List<LoanEntity> list,OnLoanInterface onLoanInterface) {
        this.context = context;
        this.addedContactList = list;
        this.mOnLoanInterface = onLoanInterface;

        database = RoomDBHelper.getInstance(context);
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

        //getting the database data
        loanDetailEntityList = database.loanDao().getLoanTransByDate(item.getPhoneNo());

        holder.textViewName.setText(item.getContactName());
        holder.textViewNameCharacter.setText(item.getContactLetter());

        // if loanDetailEntityList size is greater than zero
        if(loanDetailEntityList.size() > 0){

            //setting the latest transaction date and amount
            holder.textViewDate.setText(getConvertedDate(loanDetailEntityList.get(0).getTransDate()));

            if(loanDetailEntityList.get(0).getAmountLend() == 0)
            {
                holder.textViewAmount.setText(String.valueOf(loanDetailEntityList.get(0).getAmountBorrow()));
                holder.textViewAmount.setTextColor(context.getResources().getColor(R.color.colorRed));
                holder.textViewCurrency.setTextColor(context.getResources().getColor(R.color.colorRed));
            }
            else  if(loanDetailEntityList.get(0).getAmountBorrow() == 0)
            {
                holder.textViewAmount.setText(String.valueOf(loanDetailEntityList.get(0).getAmountLend()));
                holder.textViewAmount.setTextColor(context.getResources().getColor(R.color.colorGreen));
                holder.textViewCurrency.setTextColor(context.getResources().getColor(R.color.colorGreen));
            }

        }
        else{
            holder.textViewDate.setText("No recent Date");
            holder.textViewAmount.setText("0");
            holder.textViewAmount.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.textViewCurrency.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }

    }

    @Override
    public int getItemCount() {
        return addedContactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {

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

    //interface
    public interface OnLoanInterface{
        void onLoanClick(int position);
        void onLoanLongClick();
    }

    //function for converting date format
    private String getConvertedDate(String databaseDate) {
        String convertedDate = "";

        //database date format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //converting date format to another
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date date = dateFormat1.parse(databaseDate);
            convertedDate = dateFormat2.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertedDate;
    }

    private void deleteDialog(int position){

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);

        alertDialog.setTitle("Delete Record")
                .setMessage("want to delete this record?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {

                    //deleting the added contact
                    database.loanDao().deleteAddedContact(addedContactList.get(position).getPhoneNo());

                    //deleting all the transactions of added contact
                    database.loanDao().deleteAllLoanTransaction(addedContactList.get(position).getPhoneNo());

                    Toast.makeText(context, "Contact Removed Successfully", Toast.LENGTH_SHORT).show();

                    //removing item from list
                    addedContactList.remove(position);
                    notifyDataSetChanged();

                    //calling the interface long click listener
                    mOnLoanInterface.onLoanLongClick();

                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> {

                    dialogInterface.dismiss();

                });

        alertDialog.create().show();
    }
}
