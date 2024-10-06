package com.risibleapps.mywallet.bottomNavFragments.addRecord.income;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.bottomNavFragments.addRecord.income.incomeDB.IncomeCategoryEntity;

import java.util.List;

public class AddIncomeAdapter extends RecyclerView.Adapter<AddIncomeAdapter.MyViewHolder> {

    Context context;

    int selectedPosition=-1;
    private IncomeAdapterInterface mIncomeAdapterInterface;

    List<IncomeCategoryEntity> incomeCategoryList;

    public AddIncomeAdapter(Context context, List<IncomeCategoryEntity> incomeCategoryList,IncomeAdapterInterface adapterInterface) {
        this.context = context;
        this.incomeCategoryList = incomeCategoryList;
        this.mIncomeAdapterInterface = adapterInterface;
    }

    @NonNull
    @Override
    public AddIncomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_recyclerview_item,parent,false);
        return new MyViewHolder(view,mIncomeAdapterInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AddIncomeAdapter.MyViewHolder holder, int position) {

        IncomeCategoryEntity item=incomeCategoryList.get(position);

        holder.imageViewCat.setImageResource(item.getIncomeCatIconName());

        holder.textCatName.setText(item.getIncomeCatName());

        //click listener
        holder.imageViewCat.setOnClickListener(view -> {

            //passing the position to interface
            mIncomeAdapterInterface.onIncomeItemClicked(holder.getAdapterPosition(),holder.textCatName.getText().toString());

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
            holder.imageViewCat.setBackgroundResource(R.drawable.drawable_recycler_highlight);
        }
        else {
            holder.imageViewCat.setBackgroundResource(R.drawable.white_circle_recyclerview);
        }

    }

    @Override
    public int getItemCount() {
        return incomeCategoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewCat;
        private TextView textCatName;
        private IncomeAdapterInterface incomeAdapterInterface;

        public MyViewHolder(@NonNull View itemView,IncomeAdapterInterface incomeAdapterInterface) {
            super(itemView);

            imageViewCat =itemView.findViewById(R.id.img_recycler);
            textCatName=itemView.findViewById(R.id.text_recycler);
            this.incomeAdapterInterface=incomeAdapterInterface;
        }
    }

    public interface IncomeAdapterInterface{
        void onIncomeItemClicked(int position, String catName);
    }
}
