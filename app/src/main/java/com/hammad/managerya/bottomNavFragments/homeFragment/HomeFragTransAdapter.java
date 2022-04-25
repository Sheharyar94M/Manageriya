package com.hammad.managerya.bottomNavFragments.homeFragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;

public class HomeFragTransAdapter extends RecyclerView.Adapter<HomeFragTransAdapter.MyViewHolder> {

    Context context;
    private RecentTransInterface mRecentTransInterface;
    private int count;

    public HomeFragTransAdapter(Context context,RecentTransInterface recentTransInterface,int lengthCount) {
        this.context = context;
        this.mRecentTransInterface=recentTransInterface;
        this.count=lengthCount;
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

    }

    @Override
    public int getItemCount() {
        return count;
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


            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recentTransInterface.onRecentTransClick(getAdapterPosition());
                }
            });
        }
    }

    public interface RecentTransInterface
    {
        void onRecentTransClick(int position);
    }
}
