package com.hammad.managerya.bottomNavFragments.walletFragment.budget;

import static com.hammad.managerya.bottomNavFragments.homeFragment.HomeFragment.CURRENCY_;

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.walletFragment.budget.RoomDB.BudgetDetailsModel;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.MyViewHolder> {

    Context context;
    private OnBudgetClickListener mOnBudgetClickListener;

    private List<BudgetDetailsModel> addedBudgetList;

    private int budgetSpend;

    private RoomDBHelper database;

    public BudgetAdapter(Context context, List<BudgetDetailsModel> list, OnBudgetClickListener onBudgetClickListener) {
        this.context = context;
        this.addedBudgetList = list;
        this.mOnBudgetClickListener = onBudgetClickListener;
        this.budgetSpend = 0;

        database = RoomDBHelper.getInstance(context);
    }

    @NonNull
    @Override
    public BudgetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_budget_recycler, parent, false);
        return new MyViewHolder(view, mOnBudgetClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetAdapter.MyViewHolder holder, int position) {

        //setting the current currency to textviews
        holder.textViewCurrency1.setText(CURRENCY_);
        holder.textViewCurrency2.setText(CURRENCY_);

        BudgetDetailsModel item = addedBudgetList.get(position);

        //for getting details of particular category expenses (which is budget spent for that category)
        budgetSpend = getBudgetSpend(item.getBudgetCatId(), item.getBudgetDate());

        //if the category name contain new line literal (\n) then replace that liberal with no space so that the category can be displayed in single line
        if(item.getBudgetCatName().contains("\n")) {
            holder.textViewCategoryName.setText(item.getBudgetCatName().replace("\n",""));
        }
        else if(!(item.getBudgetCatName().contains("\n"))) {
            holder.textViewCategoryName.setText(item.getBudgetCatName());
        }

        //setting the values
        holder.imageView.setImageResource(item.getBudgetIcon());

        holder.textViewGoalAmount.setText(String.valueOf(item.getBudgetLimit()));

        holder.textViewAmountSpend.setText(String.valueOf(budgetSpend));

        holder.textViewRemainingAmount.setText(String.valueOf(item.getBudgetLimit() - budgetSpend));

        //setting the progress bar values
        holder.progressBar.setMax(item.getBudgetLimit());
        holder.progressBar.setProgress(budgetSpend);
    }

    @Override
    public int getItemCount() {
        return addedBudgetList.size();
    }

    //function for getting the details of particular category (expense transactions)
    private int getBudgetSpend(int budgetCatId,String date) {

        return database.expenseDetailDao().getExpenseCategorySum(budgetCatId,date);
    }

    public interface OnBudgetClickListener {
        void onBudgetItemClicked(int position);
        void onBudgetItemLongClick();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        ImageView imageView;
        TextView textViewCategoryName, textViewCurrency1, textViewCurrency2;
        TextView textViewGoalAmount, textViewAmountSpend, textViewRemainingAmount;
        LinearProgressIndicator progressBar;

        OnBudgetClickListener onBudgetClickListener;

        public MyViewHolder(@NonNull View itemView, OnBudgetClickListener onBudgetClickListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.img_budget);

            textViewCategoryName = itemView.findViewById(R.id.txt_cat_budget);

            textViewCurrency1 = itemView.findViewById(R.id.text_currency_1_budget);
            textViewAmountSpend = itemView.findViewById(R.id.text_amount_budget);
            textViewGoalAmount = itemView.findViewById(R.id.text_goal_amount_budget);

            textViewCurrency2 = itemView.findViewById(R.id.text_currency_2_budget);
            textViewRemainingAmount = itemView.findViewById(R.id.text_remaining_amount_budget);

            progressBar = itemView.findViewById(R.id.progress_budget_recycler);

            //initializing interface instance
            this.onBudgetClickListener = onBudgetClickListener;

            //for long click
            itemView.setOnCreateContextMenuListener(this);

            //click listener
            itemView.setOnClickListener(view -> onBudgetClickListener.onBudgetItemClicked(getAdapterPosition()));

        }

        //for long click
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            MenuItem deleteRecordItem=contextMenu.add(Menu.NONE,1,1,"Delete Record");
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
                        //delete dialog
                        deleteDialog(position);
                    return true;
                }
            }

            return false;
        }
    }

    //delete dialog
    private void deleteDialog(int position){

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);

        alertDialog.setTitle("Delete Record")
                .setMessage("want to delete this record?")
                .setPositiveButton("DELETE", (dialogInterface, i) -> {

                    //deleting the budget
                    database.budgetDao().deleteBudget(addedBudgetList.get(position).getBudgetCatId());
                    Toast.makeText(context, "Budget Deleted", Toast.LENGTH_SHORT).show();

                    addedBudgetList.remove(position);
                    notifyDataSetChanged();

                    //calling the long click interface function
                    mOnBudgetClickListener.onBudgetItemLongClick();

                })
                .setNegativeButton("CANCEL", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });

        alertDialog.create().show();
    }
}
