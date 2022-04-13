package com.hammad.managerya.bottomNavFragments.homeFragment;

import android.content.Context;
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

import com.hammad.managerya.R;

import java.util.Random;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {

    Context context;

    public RecentAdapter(Context context) {
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return 5;
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
}
