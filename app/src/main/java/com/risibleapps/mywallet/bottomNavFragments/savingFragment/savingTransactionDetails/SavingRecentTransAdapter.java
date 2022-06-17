package com.risibleapps.mywallet.bottomNavFragments.savingFragment.savingTransactionDetails;

import static com.risibleapps.mywallet.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.savingFragment.DB.SavingModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SavingRecentTransAdapter extends RecyclerView.Adapter<SavingRecentTransAdapter.ViewHolder> {

    Context context;
    List<SavingModel> savingTransactionList;
    OnSavingRecentTransListener mOnSavingRecentTransListener;

    //room database instance
    RoomDBHelper database;

    public SavingRecentTransAdapter(Context context,List<SavingModel> list,OnSavingRecentTransListener listener) {
        this.context = context;
        this.savingTransactionList = list;
        this.mOnSavingRecentTransListener = listener;

        database = RoomDBHelper.getInstance(context);
    }

    @NonNull
    @Override
    public SavingRecentTransAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_recent_saving_transaction,parent,false);
        return new ViewHolder(view,mOnSavingRecentTransListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SavingRecentTransAdapter.ViewHolder holder, int position) {

        SavingModel item = savingTransactionList.get(position);

        //setting the values to textviews
        holder.imageViewCategory.setImageResource(item.getSavingIcon());
        holder.textViewSavingGoalTitle.setText(item.getSavingTitle());
        holder.textViewAmount.setText(String.valueOf(item.getSavingGoalAmount()));
        holder.textViewDate.setText(getConvertedDate(item.getSavingTargetDate()));

    }

    @Override
    public int getItemCount() {
        return savingTransactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener{

        TextView textViewSavingGoalTitle, textViewCurrency,textViewAmount,textViewDate;
        ImageView imageViewCategory;
        OnSavingRecentTransListener onSavingRecentTransListener;

        public ViewHolder(@NonNull View itemView,OnSavingRecentTransListener listener) {
            super(itemView);

            textViewSavingGoalTitle =itemView.findViewById(R.id.text_category);
            textViewCurrency=itemView.findViewById(R.id.text_currency_);
            textViewAmount=itemView.findViewById(R.id.text_amount_);
            textViewDate=itemView.findViewById(R.id.text_date_);

            imageViewCategory=itemView.findViewById(R.id.img__1);

            //initializing interface
            this.onSavingRecentTransListener = listener;

            //setting the currency to textview
            textViewCurrency.setText(CURRENCY_);

            //long click interface
            itemView.setOnCreateContextMenuListener(this);

        }

        //long click
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
        This function is used to convert date from 'yyyy-MM-dd' to 'MMM dd, yyyy' format
        2022-05-27 to May 27, 2022
    */
    private String getConvertedDate(String databaseDate) {
        String convertedDate = "";

        //database date format
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

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

    //interface
    public interface OnSavingRecentTransListener{
        void onSavingRecentTransLongClick();
    }

    private void deleteDialog(int position){

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);

        alertDialog.setTitle("Delete Record")
                .setMessage("want to delete this record?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {

                    //deleting the saving goal transaction
                    database.savingDao().deleteSavingGoalTransaction(savingTransactionList.get(position).getId(),savingTransactionList.get(position).getSavingId());
                    Toast.makeText(context, "Saving Goal Transaction Deleted", Toast.LENGTH_SHORT).show();

                    //removing item from list
                    savingTransactionList.remove(position);
                    notifyDataSetChanged();

                    //calling the interface long click listener
                    mOnSavingRecentTransListener.onSavingRecentTransLongClick();


                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> {

                    dialogInterface.dismiss();

                });

        alertDialog.create().show();
    }

}
