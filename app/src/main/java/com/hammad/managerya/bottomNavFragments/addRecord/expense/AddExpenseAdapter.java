package com.hammad.managerya.bottomNavFragments.addRecord.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.addRecord.expense.expenseDB.ExpenseCategoryEntity;

import java.util.List;


public class AddExpenseAdapter extends RecyclerView.Adapter<AddExpenseAdapter.MyViewHolder> {

    Context context;
    int selectedPosition=-1;
    ExpenseInterfaceListener mExpenseInterfaceListener;

    List<ExpenseCategoryEntity> expenseCategoryList;

    public AddExpenseAdapter(Context context, List<ExpenseCategoryEntity> list,ExpenseInterfaceListener interfaceListener) {
        this.context = context;
        this.expenseCategoryList = list;
        this.mExpenseInterfaceListener = interfaceListener;
    }

    @NonNull
    @Override
    public AddExpenseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_recyclerview_item,parent,false);
        return new MyViewHolder(view,mExpenseInterfaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddExpenseAdapter.MyViewHolder holder, int position) {

        ExpenseCategoryEntity item=expenseCategoryList.get(position);

        holder.imageCatImage.setImageResource(item.getExpenseCatIconName());

        holder.textCatName.setText(item.getExpenseCatName());

        //click listener
        holder.imageCatImage.setOnClickListener(view -> {

            if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = -1;
                notifyDataSetChanged();
                return;
            }

            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();

            //passing the position to interface
            mExpenseInterfaceListener.onExpenseItemClicked(selectedPosition,holder.textCatName.getText().toString());

        });

        //highlight the selected item
        if(selectedPosition == holder.getAdapterPosition())
        {
            holder.imageCatImage.setBackgroundResource(R.drawable.drawable_recycler_highlight);
        }
        else {
            holder.imageCatImage.setBackgroundResource(R.drawable.white_circle_recyclerview);
        }

    }

    @Override
    public int getItemCount() {
        return expenseCategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageCatImage;
        private TextView textCatName;
        private ExpenseInterfaceListener expenseInterfaceListener;

        public MyViewHolder(@NonNull View itemView,ExpenseInterfaceListener expenseInterfaceListener) {
            super(itemView);

            imageCatImage=itemView.findViewById(R.id.img_recycler);
            textCatName=itemView.findViewById(R.id.text_recycler);
            this.expenseInterfaceListener = expenseInterfaceListener;
        }
    }

    public interface ExpenseInterfaceListener{
        void onExpenseItemClicked(int position, String catName);
    }
}
