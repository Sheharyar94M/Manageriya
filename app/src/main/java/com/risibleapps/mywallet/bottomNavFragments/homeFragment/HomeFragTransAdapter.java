package com.risibleapps.mywallet.bottomNavFragments.homeFragment;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HomeFragTransAdapter extends RecyclerView.Adapter<HomeFragTransAdapter.MyViewHolder> {

    Context context;
    List<HomeRecentTransModel> recentTransList;
    private RecentTransInterface mRecentTransInterface;

    RoomDBHelper database;

    public HomeFragTransAdapter(Context context, RecentTransInterface recentTransInterface, List<HomeRecentTransModel> list) {
        this.context = context;
        this.mRecentTransInterface = recentTransInterface;
        this.recentTransList = list;

        database = RoomDBHelper.getInstance(context);
    }

    @NonNull
    @Override
    public HomeFragTransAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_home_fragment_recyclerview, parent, false);
        return new MyViewHolder(view, mRecentTransInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragTransAdapter.MyViewHolder holder, int position) {

        //model class item
        HomeRecentTransModel item = recentTransList.get(position);

        //category icon
        holder.imageView.setImageResource(item.getCatIcon());

        String catName= item.getCatName();

        //if category name contains '\n', then removes the space
        if(catName.contains("\n"))
        {
            catName = catName.replace("\n","");
        }

        //category name
        holder.textViewCategory.setText(catName);

        //transaction date
        //holder.textViewDate.setText(item.getTransDate());
        holder.textViewDate.setText(getConvertedDate(item.getTransDate()));

        //transaction amount
        holder.textViewAmount.setText(String.valueOf(item.getTransAmount()));

        //checking whether the transaction type is income or expense
        if (item.getTransType().equals("Income")) {
            holder.textViewAmount.append(" +");
            holder.textViewAmount.setTextColor(context.getResources().getColor(R.color.colorGreen));

        }
        else if (item.getTransType().equals("Expense")) {
            holder.textViewAmount.append(" -");
            holder.textViewAmount.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

    }

    @Override
    public int getItemCount() {
        return recentTransList.size();
    }

    public interface RecentTransInterface {
        void onRecentTransClick(int position);
        void onRecentTransLongClick();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener,MenuItem.OnMenuItemClickListener {

        ConstraintLayout constraintLayout;
        ImageView imageView;
        TextView textViewCategory, textViewCash, textViewAmount, textViewDate;
        RecentTransInterface recentTransInterface;

        public MyViewHolder(@NonNull View itemView, RecentTransInterface recentTransInterface) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.constraint_home_frag_recycler);

            imageView = itemView.findViewById(R.id.img_home_frag_recycler);

            textViewCategory = itemView.findViewById(R.id.text_view_cat_home_frag_recycler);
            textViewCash = itemView.findViewById(R.id.text_view_cash_home_frag_recycler);
            textViewAmount = itemView.findViewById(R.id.text_view_amount_home_frag_recycler);
            textViewDate = itemView.findViewById(R.id.text_view_date_home_frag_recycler);

            this.recentTransInterface = recentTransInterface;

            //long click listener
            itemView.setOnCreateContextMenuListener(this);

            constraintLayout.setOnClickListener(view -> recentTransInterface.onRecentTransClick(getAdapterPosition()));
        }

        //overridden method for long click listener
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            MenuItem deleteRecordItem = contextMenu.add(Menu.NONE,1,1,"Delete Record");
            deleteRecordItem.setOnMenuItemClickListener(this);
        }

        //menu item click listener
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            int position=getAdapterPosition();

            if(position != RecyclerView.NO_POSITION)
            {
                switch (menuItem.getItemId())
                {
                    case 1:
                        if(recentTransList.get(position).getTransType().equals("Income"))
                        {
                            //delete alert dialog
                            deleteAlertDialog("Income",recentTransList.get(position),position);
                            //mRecentTransInterface.onRecentTransLongClick("Income",recentTransList.get(position),position);

                        }
                        else if(recentTransList.get(position).getTransType().equals("Expense"))
                        {
                            //delete alert dialog
                            deleteAlertDialog("Expense",recentTransList.get(position),position);
                            //mRecentTransInterface.onRecentTransLongClick("Expense",recentTransList.get(position),position);
                        }

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

    //alert dialog
    private void deleteAlertDialog(String type, HomeRecentTransModel item, int position){

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);

        alertDialog.setTitle("Delete Record")
                .setMessage("want to delete this record?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {

                    if(type.equals("Income"))
                    {
                        database.incomeDetailDao().deleteIncomeDetail(item.getCatId(),item.getRecordId());
                        Toast.makeText(context, "Record Delete Successfully", Toast.LENGTH_SHORT).show();
                        recentTransList.remove(position);
                        notifyDataSetChanged();

                        //calling the long click interface
                        mRecentTransInterface.onRecentTransLongClick();
                    }
                    else if(type.equals("Expense"))
                    {
                        database.expenseDetailDao().deleteExpenseDetail(item.getCatId(),item.getRecordId());
                        Toast.makeText(context, "Record Delete Successfully", Toast.LENGTH_SHORT).show();
                        recentTransList.remove(position);
                        notifyDataSetChanged();

                        //calling the long click interface
                        mRecentTransInterface.onRecentTransLongClick();
                    }

                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

        alertDialog.create().show();
    }
}
