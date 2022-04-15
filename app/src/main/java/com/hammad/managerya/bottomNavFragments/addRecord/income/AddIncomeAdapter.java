package com.hammad.managerya.bottomNavFragments.addRecord.income;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;

public class AddIncomeAdapter extends RecyclerView.Adapter<AddIncomeAdapter.MyViewHolder> {

    Context context;
    int[] imagesList;
    String[] catNameList;

    int selectedPosition=-1;

    public AddIncomeAdapter(Context context, int[] imagesList, String[] catNameList) {
        this.context = context;
        this.imagesList = imagesList;
        this.catNameList = catNameList;
    }

    @NonNull
    @Override
    public AddIncomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_recyclerview_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddIncomeAdapter.MyViewHolder holder, int position) {

        holder.imageCatImage.setImageResource(imagesList[position]);

        holder.textCatName.setText(catNameList[position]);

        //click listener
        holder.imageCatImage.setOnClickListener(view -> {

            if (selectedPosition == holder.getAdapterPosition()) {
                selectedPosition = -1;
                notifyDataSetChanged();
                return;
            }

            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();

            //showing toast
            Toast.makeText(context, holder.textCatName.getText(), Toast.LENGTH_SHORT).show();

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
        return /*imagesList.length*/3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageCatImage;
        private TextView textCatName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageCatImage=itemView.findViewById(R.id.img_recycler);
            textCatName=itemView.findViewById(R.id.text_recycler);
        }
    }
}
