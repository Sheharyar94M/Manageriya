package com.risibleapps.mywallet.bottomNavFragments.homeFragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.BudgetActivity;
import com.risibleapps.mywallet.bottomNavFragments.walletFragment.budget.RoomDB.BudgetDetailsModel;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {

    Context context;
    Random random=new Random();
    String colorCode;

    List<String> colorsList= Arrays.asList("#fc0505","#fc3705","#fc6805","#fc8505","#fc9105","#fcae05"
            ,"#fcbf05","#fce005","#f0fc05","#cbfc05","#bafc05","#9efc05","#79fc05","#53fc05","#05fc60"
            ,"#05fc7d","#05fcd3","#05fcf0","#05f8fc","#05e0fc","#0579fc","#5805fc","#6c05fc"
            ,"#7405fc","#8905fc","#a205fc","#c305fc","#e005fc","#fc05fc","#fc05e0"
            ,"#fc0599","#fc0558","#fc0543","#fc052e","#fc0522","#fc0505");

    private List<BudgetDetailsModel> addedBudgetList;

    private float budgetSpend;
    private float budgetTotal;

    private RoomDBHelper database;

    private int percentage;

    private String currentSelectedMonth="";

    public RecentAdapter(Context context,String month,List<BudgetDetailsModel> list) {
        this.context = context;
        this.addedBudgetList = list;
        this.budgetSpend = 0;
        this.budgetTotal = 0;
        this.percentage = 0;
        this.currentSelectedMonth = month;

        database = RoomDBHelper.getInstance(context);
    }

    @NonNull
    @Override
    public RecentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_frag_home_recent_recyclerview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentAdapter.MyViewHolder holder, int position) {

        BudgetDetailsModel item = addedBudgetList.get(position);

        //for getting details of particular category expenses (which is budget spent for that category)
        budgetSpend = getBudgetSpend(item.getBudgetCatId(), currentSelectedMonth);

        //total budget of category
        budgetTotal = item.getBudgetLimit();

        //setting the values
        holder.imageViewIcon.setImageResource(item.getBudgetIcon());

        //if the category name contain new line literal (\n) then replace that liberal with no space so that the category can be displayed in single line
        if(item.getBudgetCatName().contains("\n")) {
            holder.textViewCat.setText(item.getBudgetCatName().replace("\n",""));
        }
        else if(!(item.getBudgetCatName().contains("\n"))) {
            holder.textViewCat.setText(item.getBudgetCatName());
        }

        holder.textViewAmount.setText(String.valueOf((int) budgetSpend));
        holder.textViewAmount.append(" of ");
        holder.textViewAmount.append(String.valueOf((int) budgetTotal));

        //random color code from colorsList
        colorCode =colorsList.get(random.nextInt(colorsList.size()));

        //set the randomly generated color as progress tint
        holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(colorCode)));

        //setting the progress bar values
        holder.progressBar.setMax(item.getBudgetLimit());
        holder.progressBar.setProgress((int) budgetSpend);

        //for percentage
        percentage = (int) ((budgetSpend / budgetTotal) * 100);
        holder.textViewPercentage.setText(String.valueOf(percentage));
        holder.textViewPercentage.append(" %");

        //itemview click listener
        holder.constraintLayout.setOnClickListener(view -> {
            Intent intent=new Intent(context, BudgetActivity.class);
            //intent.putExtra("BudgetCatId",item.getBudgetCatId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return addedBudgetList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout constraintLayout;
        ImageView imageViewIcon;
        TextView textViewPercentage, textViewCat,textViewAmount;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout=itemView.findViewById(R.id.constraint_3);

            imageViewIcon=itemView.findViewById(R.id.img_recent_frag_home);

            textViewPercentage=itemView.findViewById(R.id.text_view_recent_frag_home);
            textViewCat=itemView.findViewById(R.id.text_view_cat_recent_frag_home);
            textViewAmount=itemView.findViewById(R.id.text_view_amount_recent_frag_home);

            progressBar=itemView.findViewById(R.id.progress_recent_frag_home);
        }
    }

    //function for getting the details of particular category (expense transactions)
    private int getBudgetSpend(int budgetCatId,String date) {

        return database.expenseDetailDao().getExpenseCategorySum(budgetCatId,date);
    }
}
