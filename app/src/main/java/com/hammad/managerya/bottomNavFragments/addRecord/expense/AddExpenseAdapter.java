package com.hammad.managerya.bottomNavFragments.addRecord.expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;


public class AddExpenseAdapter extends RecyclerView.Adapter<AddExpenseAdapter.MyViewHolder> {

    Context context;
    int[] imagesList;
    String[] catNameList;
    int selectedPosition=-1;
    ExpenseInterfaceListener mExpenseInterfaceListener;

    public AddExpenseAdapter(Context context, int[] imagesList, String[] catNameList,ExpenseInterfaceListener interfaceListener) {
        this.context = context;
        this.imagesList = imagesList;
        this.catNameList = catNameList;
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

        holder.imageCatImage.setImageResource(imagesList[position]);

        holder.textCatName.setText(catNameList[position]);

        //click listener
        holder.imageCatImage.setOnClickListener(view -> {

            //passing the position to interface
            mExpenseInterfaceListener.onExpenseItemClicked(holder.getAdapterPosition(),holder.textCatName.getText().toString());

            if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = -1;
                notifyDataSetChanged();
                return;
            }

            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();


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
        return imagesList.length;
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
