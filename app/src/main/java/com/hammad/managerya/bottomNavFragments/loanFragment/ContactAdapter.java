package com.hammad.managerya.bottomNavFragments.loanFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context context;
    List<ContactModel> contactModelList;
    OnContactInterfaceListener mOnInterfaceListener;

    public ContactAdapter(Context context, List<ContactModel> contactModelList,OnContactInterfaceListener interfaceListener) {
        this.context = context;
        this.contactModelList = contactModelList;
        this.mOnInterfaceListener=interfaceListener;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_contact_recycler,parent,false);
        return new MyViewHolder(view,mOnInterfaceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {

        ContactModel item = contactModelList.get(position);

        holder.textViewLetters.setText(item.getContactLetters());

        holder.textViewName.setText(item.getContactName());

        holder.textViewPhoneNo.setText(item.getPhoneNo());

    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout constraintLayout;
        TextView textViewLetters,textViewName,textViewPhoneNo,textViewContactStatus;
        OnContactInterfaceListener onContactInterfaceListener;

        public MyViewHolder(@NonNull View itemView,OnContactInterfaceListener onContactInterfaceListener) {
            super(itemView);

            constraintLayout=itemView.findViewById(R.id.constraint_contact);
            textViewLetters=itemView.findViewById(R.id.txt_contact_letters);
            textViewName=itemView.findViewById(R.id.txt_contact_name);
            textViewPhoneNo=itemView.findViewById(R.id.txt_contact_no);
            textViewContactStatus=itemView.findViewById(R.id.txt_contact_status);

            this.onContactInterfaceListener=onContactInterfaceListener;

            //click listener
            constraintLayout.setOnClickListener(view -> {
                onContactInterfaceListener.onContactClick(getAdapterPosition());
            });

        }
    }

    public interface OnContactInterfaceListener{
        void onContactClick(int position);
    }

}
