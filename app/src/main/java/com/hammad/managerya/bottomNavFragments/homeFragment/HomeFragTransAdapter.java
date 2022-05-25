package com.hammad.managerya.bottomNavFragments.homeFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.bottomNavFragments.homeFragment.homeDB.HomeRecentTransModel;
import com.hammad.managerya.R;

import java.util.List;

public class HomeFragTransAdapter extends RecyclerView.Adapter<HomeFragTransAdapter.MyViewHolder> {

    Context context;
    private RecentTransInterface mRecentTransInterface;

    List<HomeRecentTransModel> recentTransList;

    public HomeFragTransAdapter(Context context,RecentTransInterface recentTransInterface,List<HomeRecentTransModel> list) {
        this.context = context;
        this.mRecentTransInterface=recentTransInterface;
        this.recentTransList = list;
    }

    @NonNull
    @Override
    public HomeFragTransAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.layout_home_fragment_recyclerview,parent,false);
        return new MyViewHolder(view,mRecentTransInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragTransAdapter.MyViewHolder holder, int position) {

        //model class item
        HomeRecentTransModel item= recentTransList.get(position);

        //category icon
        holder.imageView.setImageResource(item.getCatIcon());

        //category name
        holder.textViewCategory.setText(item.getCatName());

        //transaction date
        holder.textViewDate.setText(item.getTransDate());

        //transaction amount
        holder.textViewAmount.setText(String.valueOf(item.getTransAmount()));

        //checking whether the transaction type is income or expense
        if(item.getTransType().equals("Income"))
        {
            holder.textViewAmount.append(" +");
            holder.textViewAmount.setTextColor(context.getResources().getColor(R.color.colorGreen));

        }
        else if(item.getTransType().equals("Expense"))
        {
            holder.textViewAmount.append(" -");
            holder.textViewAmount.setTextColor(context.getResources().getColor(R.color.colorRed));
        }

    }

    @Override
    public int getItemCount() {
        return recentTransList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout constraintLayout;
        ImageView imageView;
        TextView textViewCategory,textViewCash,textViewAmount,textViewDate;
        RecentTransInterface recentTransInterface;

        public MyViewHolder(@NonNull View itemView,RecentTransInterface recentTransInterface) {
            super(itemView);

            constraintLayout=itemView.findViewById(R.id.constraint_home_frag_recycler);

            imageView=itemView.findViewById(R.id.img_home_frag_recycler);

            textViewCategory=itemView.findViewById(R.id.text_view_cat_home_frag_recycler);
            textViewCash=itemView.findViewById(R.id.text_view_cash_home_frag_recycler);
            textViewAmount=itemView.findViewById(R.id.text_view_amount_home_frag_recycler);
            textViewDate=itemView.findViewById(R.id.text_view_date_home_frag_recycler);

            this.recentTransInterface=recentTransInterface;


            constraintLayout.setOnClickListener(view -> recentTransInterface.onRecentTransClick(getAdapterPosition()));
        }
    }

    public interface RecentTransInterface
    {
        void onRecentTransClick(int position);
    }
}
